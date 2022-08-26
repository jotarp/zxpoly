/*
 * Copyright (C) 2014-2019 Igor Maznitsa
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

package com.igormaznitsa.zxpoly.ui;

import com.igormaznitsa.zxpoly.Version;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.swing.event.HyperlinkEvent;
import org.apache.commons.io.IOUtils;

public class AboutDialog extends javax.swing.JDialog implements Version {

  private static final long serialVersionUID = 6729883219284422519L;
  private javax.swing.JButton buttonOk;
  private javax.swing.JEditorPane editorPane;
  private javax.swing.JScrollPane jScrollPane1;

  public AboutDialog(final java.awt.Frame parent) {
    super(parent, true);
    initComponents();

    this.editorPane.addHyperlinkListener((final HyperlinkEvent e) -> {
      if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        try {
          Desktop.getDesktop().browse(e.getURL().toURI());
        } catch (Exception ex) {
        }
      } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
        editorPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
        editorPane.setCursor(Cursor.getDefaultCursor());
      }
    });

    this.editorPane.setContentType("text/html");
    try {
      String htmlText = IOUtils.toString(openAboutResource("index.html"), StandardCharsets.UTF_8);
      htmlText = htmlText
          .replace("${version.major}", Integer.toString(VERSION_MAJOR))
          .replace("${version.minor}", Integer.toString(VERSION_MINOR))
          .replace("${version.build}", Integer.toString(VERSION_BUILD));

      this.editorPane.setText(htmlText);
      this.editorPane.setCaretPosition(0);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    this.getRootPane().setDefaultButton(this.buttonOk);

    setLocationRelativeTo(parent);
  }

  private void initComponents() {

    buttonOk = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    editorPane = new javax.swing.JEditorPane();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Help");

    buttonOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/igormaznitsa/zxpoly/icons/ok.png"))); // NOI18N
    buttonOk.setText("Ok");
    buttonOk.addActionListener(this::buttonOkActionPerformed);
    buttonOk.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        buttonOkKeyReleased(evt);
      }
    });

    editorPane.setEditable(false);
    editorPane.setFocusable(false);
    jScrollPane1.setViewportView(editorPane);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                                    .addComponent(buttonOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap())
    );
    layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(buttonOk)
                            .addContainerGap())
    );

    pack();
  }

  private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {
    setVisible(false);
  }

  private void buttonOkKeyReleased(java.awt.event.KeyEvent evt) {
    if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
      this.dispose();
    }
  }

  private InputStream openAboutResource(final String name) {
    return AboutDialog.class.getClassLoader().getResourceAsStream("com/igormaznitsa/zxpoly/about/" + name);
  }

}
