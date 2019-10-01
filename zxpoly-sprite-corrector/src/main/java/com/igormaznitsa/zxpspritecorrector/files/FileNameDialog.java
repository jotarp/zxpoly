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

package com.igormaznitsa.zxpspritecorrector.files;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTextField;
import org.apache.commons.io.FilenameUtils;

public final class FileNameDialog extends javax.swing.JDialog {

  private static final long serialVersionUID = -5397359066835236154L;

  private boolean approved;
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton buttonCancel;
  private javax.swing.JButton buttonOk;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JTextField textFileName0;
  private javax.swing.JTextField textFileName1;
  private javax.swing.JTextField textFileName2;
  private javax.swing.JTextField textFileName3;
  private javax.swing.JFormattedTextField textZxFileName0;
  private javax.swing.JFormattedTextField textZxFileName1;
  private javax.swing.JFormattedTextField textZxFileName2;
  private javax.swing.JFormattedTextField textZxFileName3;
  private javax.swing.JFormattedTextField textZxFileType0;
  private javax.swing.JFormattedTextField textZxFileType1;
  private javax.swing.JFormattedTextField textZxFileType2;
  private javax.swing.JFormattedTextField textZxFileType3;
  public FileNameDialog(final JFrame parent, final String title, final String[] fileName, final String[] zxName, final char[] zxType) {
    super(parent, true);
    initComponents();

    processFileName(this.textFileName0, fileName, 0);
    processZxFileName(this.textZxFileName0, zxName, 0);
    processZxFileType(this.textZxFileType0, zxType, 0);

    processFileName(this.textFileName1, fileName, 1);
    processZxFileName(this.textZxFileName1, zxName, 1);
    processZxFileType(this.textZxFileType1, zxType, 1);

    processFileName(this.textFileName2, fileName, 2);
    processZxFileName(this.textZxFileName2, zxName, 2);
    processZxFileType(this.textZxFileType2, zxType, 2);

    processFileName(this.textFileName3, fileName, 3);
    processZxFileName(this.textZxFileName3, zxName, 3);
    processZxFileType(this.textZxFileType3, zxType, 3);

    setTitle(title);
    setLocationRelativeTo(parent);
  }

  public static String[] makeFileNames(final String baseName) {
    final String[] result = new String[4];
    for (int i = 0; i < 4; i++) {
      result[i] = FilenameUtils.getBaseName(baseName) + '_' + i + '.' + FilenameUtils.getExtension(baseName);
    }
    return result;
  }

  private void processFileName(final JTextField field, final String[] array, final int index) {
    if (array == null || array[index] == null) {
      field.setEnabled(false);
    } else {
      field.setText(array[index]);
    }
  }

  private void processZxFileName(final JTextField field, final String[] array, final int index) {
    if (array == null || array[index] == null) {
      field.setEnabled(false);
    } else {
      field.setText(array[index]);
    }
  }

  private void processZxFileType(final JTextField field, final char[] array, final int index) {
    if (array == null || array[index] == 0) {
      field.setEnabled(false);
    } else {
      field.setText(Character.toString(array[index]));
    }
  }

  public boolean approved() {
    return approved;
  }

  public String[] getFileName() {
    final String[] result = new String[4];
    if (this.textFileName0.isEnabled()) {
      result[0] = this.textFileName0.getText().trim();
    }
    if (this.textFileName1.isEnabled()) {
      result[1] = this.textFileName1.getText().trim();
    }
    if (this.textFileName2.isEnabled()) {
      result[2] = this.textFileName2.getText().trim();
    }
    if (this.textFileName3.isEnabled()) {
      result[3] = this.textFileName3.getText().trim();
    }

    return result;
  }

  public String[] getZxName() {
    final String[] result = new String[4];
    if (this.textZxFileName0.isEnabled()) {
      result[0] = this.textZxFileName0.getText().trim();
    }
    if (this.textZxFileName1.isEnabled()) {
      result[1] = this.textZxFileName1.getText().trim();
    }
    if (this.textZxFileName2.isEnabled()) {
      result[2] = this.textZxFileName2.getText().trim();
    }
    if (this.textZxFileName3.isEnabled()) {
      result[3] = this.textZxFileName3.getText().trim();
    }

    return result;
  }

  private Character getCharacter(final JFormattedTextField field) {
    final String text = field.getText().trim();
    return text.isEmpty() ? null : text.charAt(0);
  }

  public Character[] getZxType() {
    final Character[] result = new Character[4];
    if (this.textZxFileType0.isEnabled()) {
      result[0] = getCharacter(this.textZxFileType0);
    }
    if (this.textZxFileType1.isEnabled()) {
      result[1] = getCharacter(this.textZxFileType1);
    }
    if (this.textZxFileType2.isEnabled()) {
      result[2] = getCharacter(this.textZxFileType2);
    }
    if (this.textZxFileType3.isEnabled()) {
      result[3] = getCharacter(this.textZxFileType3);
    }

    return result;
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    buttonCancel = new javax.swing.JButton();
    buttonOk = new javax.swing.JButton();
    jPanel1 = new javax.swing.JPanel();
    textFileName0 = new javax.swing.JTextField();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    textZxFileName0 = new javax.swing.JFormattedTextField();
    textZxFileType0 = new javax.swing.JFormattedTextField();
    jPanel2 = new javax.swing.JPanel();
    jLabel4 = new javax.swing.JLabel();
    textFileName1 = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    textZxFileName1 = new javax.swing.JFormattedTextField();
    textZxFileType1 = new javax.swing.JFormattedTextField();
    jPanel3 = new javax.swing.JPanel();
    jLabel10 = new javax.swing.JLabel();
    textFileName3 = new javax.swing.JTextField();
    jLabel11 = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    textZxFileName3 = new javax.swing.JFormattedTextField();
    textZxFileType3 = new javax.swing.JFormattedTextField();
    jPanel4 = new javax.swing.JPanel();
    jLabel7 = new javax.swing.JLabel();
    textFileName2 = new javax.swing.JTextField();
    jLabel8 = new javax.swing.JLabel();
    jLabel9 = new javax.swing.JLabel();
    textZxFileName2 = new javax.swing.JFormattedTextField();
    textZxFileType2 = new javax.swing.JFormattedTextField();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    buttonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/igormaznitsa/zxpspritecorrector/icons/cross.png"))); // NOI18N
    buttonCancel.setText("Cancel");
    buttonCancel.addActionListener(this::buttonCancelActionPerformed);

    buttonOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/igormaznitsa/zxpspritecorrector/icons/tick.png"))); // NOI18N
    buttonOk.setText("Ok");
    buttonOk.addActionListener(this::buttonOkActionPerformed);

    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Block CPU0"));

    jLabel1.setText("File name:");

    jLabel2.setText("ZX File name:");

    jLabel3.setText("ZX File type:");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textZxFileType0, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFileName0)
                            .addComponent(textZxFileName0))))
                .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFileName0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textZxFileName0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(textZxFileType0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
    );

    jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, jLabel1, jLabel2, jLabel3, textFileName0, textZxFileName0, textZxFileType0);

    jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Block CPU1"));

    jLabel4.setText("File name:");

    jLabel5.setText("ZX File name:");

    jLabel6.setText("ZX File type:");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textZxFileType1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFileName1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                            .addComponent(textZxFileName1))))
                .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFileName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(textZxFileName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(textZxFileType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
    );

    jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, jLabel4, jLabel5, jLabel6, textFileName1, textZxFileName1, textZxFileType1);

    jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Block CPU3"));

    jLabel10.setText("File name:");

    jLabel11.setText("ZX File name:");

    jLabel12.setText("ZX File type:");

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textZxFileType3, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFileName3, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                            .addComponent(textZxFileName3))))
                .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFileName3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(textZxFileName3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(textZxFileType3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
    );

    jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, jLabel10, jLabel11, jLabel12, textFileName3, textZxFileName3, textZxFileType3);

    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Block CPU2"));

    jLabel7.setText("File name:");

    jLabel8.setText("ZX File name:");

    jLabel9.setText("ZX File type:");

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textZxFileType2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFileName2, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                            .addComponent(textZxFileName2))))
                .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFileName2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(textZxFileName2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(textZxFileType2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
    );

    jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, jLabel7, jLabel8, jLabel9, textFileName2, textZxFileName2, textZxFileType2);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancel)))
                .addContainerGap())
    );

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, buttonCancel, buttonOk);

    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonOk)
                    .addComponent(buttonCancel))
                .addContainerGap())
    );

    layout.linkSize(javax.swing.SwingConstants.VERTICAL, jPanel1, jPanel2, jPanel3, jPanel4);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
    approved = true;
    dispose();
  }//GEN-LAST:event_buttonOkActionPerformed

  private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
    dispose();
  }//GEN-LAST:event_buttonCancelActionPerformed
  // End of variables declaration//GEN-END:variables
}
