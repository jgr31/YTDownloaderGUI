package gelabert.ytdownloadergui;

/*
 * This class is intentionally hand-edited to match the .form.
 * It uses absolute (null) layout per assignment requirements.
 */

public class PreferencesPanel extends javax.swing.JPanel {

    public interface Listener {
        void onBackRequested();
        void onApplyRequested();
    }

    private Listener listener;

    public PreferencesPanel() {
        initComponents();
        // Load saved values
        jTextFieldTempPath.setText(AppPreferences.getTempPath());
        jCheckBoxCreateM3U.setSelected(AppPreferences.isCreateM3U());
        int kb = AppPreferences.getSpeedLimitKBs();
        jTextFieldSpeedLimit.setText(kb == 0 ? "" : String.valueOf(kb));
        jTextFieldYtDlpPath.setText(AppPreferences.getYtDlpPath());
    }

    public void setListener(Listener l) {
        this.listener = l;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTempPath = new javax.swing.JLabel();
        jTextFieldTempPath = new javax.swing.JTextField();
        jButtonSelectPath = new javax.swing.JButton();
        jCheckBoxCreateM3U = new javax.swing.JCheckBox();
        jTextFieldSpeedLimit = new javax.swing.JTextField();
        jLabelSpeedLimit = new javax.swing.JLabel();
        jTextFieldYtDlpPath = new javax.swing.JTextField();
        jButtonSelectYtDlp = new javax.swing.JButton();
        jButtonBack = new javax.swing.JButton();
        jButtonApply = new javax.swing.JButton();

        setLayout(null);

        jLabelTempPath.setText("Path for temporary files:");
        add(jLabelTempPath);
        jLabelTempPath.setBounds(20, 20, 170, 16);
        add(jTextFieldTempPath);
        jTextFieldTempPath.setBounds(190, 20, 260, 22);

        jButtonSelectPath.setText("Browse...");
        jButtonSelectPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectPathActionPerformed(evt);
            }
        });
        add(jButtonSelectPath);
        jButtonSelectPath.setBounds(460, 20, 100, 23);

        jCheckBoxCreateM3U.setText("Create .m3u file for playlists");
        add(jCheckBoxCreateM3U);
        jCheckBoxCreateM3U.setBounds(20, 70, 220, 20);
        add(jTextFieldSpeedLimit);
        jTextFieldSpeedLimit.setBounds(250, 110, 100, 22);

        jLabelSpeedLimit.setText("Download speed limit (KB/s, blank=unlimited):");
        add(jLabelSpeedLimit);
        jLabelSpeedLimit.setBounds(20, 110, 320, 16);
        add(jTextFieldYtDlpPath);
        jTextFieldYtDlpPath.setBounds(20, 160, 430, 22);

        jButtonSelectYtDlp.setText("Select yt-dlp binary");
        jButtonSelectYtDlp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectYtDlpActionPerformed(evt);
            }
        });
        add(jButtonSelectYtDlp);
        jButtonSelectYtDlp.setBounds(460, 160, 180, 23);

        jButtonBack.setText("Back");
        jButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackActionPerformed(evt);
            }
        });
        add(jButtonBack);
        jButtonBack.setBounds(560, 220, 80, 23);

        jButtonApply.setText("Apply");
        jButtonApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyActionPerformed(evt);
            }
        });
        add(jButtonApply);
        jButtonApply.setBounds(460, 220, 80, 23);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSelectPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectPathActionPerformed
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            jTextFieldTempPath.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_jButtonSelectPathActionPerformed

    private void jButtonSelectYtDlpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectYtDlpActionPerformed
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            jTextFieldYtDlpPath.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_jButtonSelectYtDlpActionPerformed

    private void jButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackActionPerformed
        persist();
        if (listener != null) listener.onBackRequested();
    }//GEN-LAST:event_jButtonBackActionPerformed

    private void jButtonApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyActionPerformed
        persist();
        if (listener != null) listener.onApplyRequested();
        javax.swing.JOptionPane.showMessageDialog(this, "Preferences saved.", "Preferences", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonApplyActionPerformed

    private void persist() {
        AppPreferences.setTempPath(jTextFieldTempPath.getText().trim());
        AppPreferences.setCreateM3U(jCheckBoxCreateM3U.isSelected());
        String v = jTextFieldSpeedLimit.getText().trim();
        int kb = 0;
        if (!v.isEmpty()) {
            try { kb = Integer.parseInt(v); } catch (NumberFormatException ignored) {}
        }
        AppPreferences.setSpeedLimitKBs(kb);
        AppPreferences.setYtDlpPath(jTextFieldYtDlpPath.getText().trim());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonApply;
    private javax.swing.JButton jButtonBack;
    private javax.swing.JButton jButtonSelectPath;
    private javax.swing.JButton jButtonSelectYtDlp;
    private javax.swing.JCheckBox jCheckBoxCreateM3U;
    private javax.swing.JLabel jLabelSpeedLimit;
    private javax.swing.JLabel jLabelTempPath;
    private javax.swing.JTextField jTextFieldSpeedLimit;
    private javax.swing.JTextField jTextFieldTempPath;
    private javax.swing.JTextField jTextFieldYtDlpPath;
    // End of variables declaration//GEN-END:variables
}