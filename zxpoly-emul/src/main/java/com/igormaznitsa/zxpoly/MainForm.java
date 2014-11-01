/*
 * Copyright (C) 2014 Raydac Research Group Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.igormaznitsa.zxpoly;

import com.igormaznitsa.zxpoly.components.*;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.*;

public class MainForm extends javax.swing.JFrame implements Runnable {
  private static final long serialVersionUID = 7309959798344327441L;

  public static final Logger log = Logger.getLogger("UI");
  
  private final Motherboard board;
 
  private final long SCREEN_REFRESH_DELAY = 100L;
  
  private class KeyboardDispatcher implements KeyEventDispatcher {
    private final KeyboardAndTape keyboard;
    
    public KeyboardDispatcher(final KeyboardAndTape kbd){
      this.keyboard = kbd;
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
      this.keyboard.onKeyEvent(e);
      return false;
    }
  }  
  
  public MainForm() throws IOException {
    initComponents();
    log.info("Loading test rom");
    this.board = new Motherboard(ZXRom.read(Utils.findResourceOrError("com/igormaznitsa/zxpoly/rom/zxpolytest.rom")));
//    this.board = new Motherboard(ZXRom.read(Utils.findResourceOrError("com/igormaznitsa/zxpoly/rom/opense.rom")));
    log.info("Main form completed");
    this.board.reset();

    this.scrollPanel.getViewport().add(this.board.getVideoController());

    final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    manager.addKeyEventDispatcher(new KeyboardDispatcher(this.board.getKeyboard()));
    
    final Thread daemon = new Thread(this,"ZXPolyThread");
    daemon.setDaemon(true);
    daemon.start();
    
    pack();
  }

  @Override
  public void run() {
    long nextSystemInt = System.currentTimeMillis() + 20;
    long nextScreenRefresh = System.currentTimeMillis() + SCREEN_REFRESH_DELAY;
    while(!Thread.currentThread().isInterrupted()){
      final boolean intsignal;
      if (nextSystemInt<=System.currentTimeMillis()){
        intsignal = true;
        nextSystemInt = System.currentTimeMillis()+20;
      }else{
        intsignal = false;
      }
      this.board.step(intsignal);
      if (nextScreenRefresh<=System.currentTimeMillis()){
        updateScreen();
        nextScreenRefresh = System.currentTimeMillis() + SCREEN_REFRESH_DELAY;
      }
    }
  }

  private void updateScreen(){
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        board.getVideoController().refreshComponent();
      }
    });
  }
  
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    scrollPanel = new javax.swing.JScrollPane();
    menuBar = new javax.swing.JMenuBar();
    menuFile = new javax.swing.JMenu();
    menuFileReset = new javax.swing.JMenuItem();
    menuOptions = new javax.swing.JMenu();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    getContentPane().add(scrollPanel, java.awt.BorderLayout.CENTER);

    menuFile.setText("File");

    menuFileReset.setText("Reset");
    menuFileReset.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuFileResetActionPerformed(evt);
      }
    });
    menuFile.add(menuFileReset);

    menuBar.add(menuFile);

    menuOptions.setText("Options");
    menuBar.add(menuOptions);

    setJMenuBar(menuBar);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void menuFileResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileResetActionPerformed
    this.board.reset();
  }//GEN-LAST:event_menuFileResetActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JMenu menuFile;
  private javax.swing.JMenuItem menuFileReset;
  private javax.swing.JMenu menuOptions;
  private javax.swing.JScrollPane scrollPanel;
  // End of variables declaration//GEN-END:variables
}