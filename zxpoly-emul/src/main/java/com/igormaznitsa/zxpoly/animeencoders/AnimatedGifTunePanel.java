/*
 * Copyright (C) 2019 Igor Maznitsa
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

package com.igormaznitsa.zxpoly.animeencoders;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Locale;

public class AnimatedGifTunePanel extends javax.swing.JPanel {

  private static final long serialVersionUID = -5725762636857034093L;
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton buttonChooseFile;
  private javax.swing.JCheckBox checkBoxRepeat;
  private javax.swing.Box.Filler filler1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel labelFrameRate;
  private javax.swing.JLabel labelRepeat;
  private javax.swing.JSpinner spinnerFrameRate;
  private javax.swing.JTextField textFieldFile;

  public AnimatedGifTunePanel(final AnimGifOptions options) {
    initComponents();
    this.textFieldFile.setText(options.filePath);
    this.checkBoxRepeat.setSelected(options.repeat);
    this.spinnerFrameRate.setValue(options.frameRate);
  }

  public AnimGifOptions getValue() {
    return new AnimGifOptions(this.textFieldFile.getText(), (Integer) this.spinnerFrameRate.getValue(), this.checkBoxRepeat.isSelected());
  }

  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    labelFrameRate = new javax.swing.JLabel();
    checkBoxRepeat = new javax.swing.JCheckBox();
    jLabel1 = new javax.swing.JLabel();
    buttonChooseFile = new javax.swing.JButton();
    textFieldFile = new javax.swing.JTextField();
    filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
    spinnerFrameRate = new javax.swing.JSpinner();
    labelRepeat = new javax.swing.JLabel();

    setLayout(new java.awt.GridBagLayout());

    labelFrameRate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labelFrameRate.setText("Frame rate: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
    add(labelFrameRate, gridBagConstraints);

    checkBoxRepeat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.weightx = 1000.0;
    add(checkBoxRepeat, gridBagConstraints);

    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel1.setText("File: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
    add(jLabel1, gridBagConstraints);

    buttonChooseFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/igormaznitsa/zxpoly/icons/disk.png"))); // NOI18N
    buttonChooseFile.addActionListener(this::buttonChooseFileActionPerformed);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
    add(buttonChooseFile, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 250;
    gridBagConstraints.weightx = 1000.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
    add(textFieldFile, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.weighty = 1000.0;
    add(filler1, gridBagConstraints);

    spinnerFrameRate.setModel(new javax.swing.SpinnerNumberModel(10, 1, 20, 1));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
    add(spinnerFrameRate, gridBagConstraints);

    labelRepeat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labelRepeat.setText("Repeat: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    add(labelRepeat, gridBagConstraints);
  }

  private void buttonChooseFileActionPerformed(java.awt.event.ActionEvent evt) {
    final JFileChooser fileChooser = new JFileChooser(this.textFieldFile.getText());
    fileChooser.setAcceptAllFileFilterUsed(true);
    fileChooser.setMultiSelectionEnabled(false);

    fileChooser.setFileFilter(new FileFilter() {
      @Override
      public boolean accept(final File f) {
        return f.isDirectory() || f.getName().toLowerCase(Locale.ENGLISH).endsWith(".gif");
      }

      @Override
      public String getDescription() {
        return "Animated GIF files (*.gif)";
      }
    });

    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      if (!selectedFile.getName().contains(".")) {
        selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".gif");
      }
      this.textFieldFile.setText(selectedFile.getAbsolutePath());
    }
  }

  public static final class AnimGifOptions {

    public final String filePath;
    public final int frameRate;
    public final boolean repeat;

    public AnimGifOptions(final String filePath, final int frameRate, final boolean repeat) {
      this.filePath = filePath;
      this.frameRate = frameRate;
      this.repeat = repeat;
    }
  }
}
