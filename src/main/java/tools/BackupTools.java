package tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.GlobalData;
import dto.TestrackDTO;
import dto.UserDto;
import model.BackupRestoreData;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class BackupTools {
    private JCheckBox chkBackupUsers;
    private JCheckBox chkBackupLabs;
    private JCheckBox chkBackupRacks;
    private JCheckBox chkBackupAssigns;
    private JCheckBox chkRestoreUsers;
    private JCheckBox chkRestoreLabs;
    private JCheckBox chkRestoreRacks;
    private JCheckBox chkRestoreAssigns;
    private JTextField txFileName;
    private DefaultListModel<String> dlmLog;
    private GlobalData globalData;

    /**
     *
     * @param backupUsers
     * @param backupLabs
     * @param backupRacks
     * @param backupAssigns
     * @param restoreUsers
     * @param restoreLabs
     * @param restoreRacks
     * @param restoreAssigns
     * @param fileName
     * @param dlmLog
     */
    public BackupTools(JCheckBox backupUsers,JCheckBox backupLabs,
                       JCheckBox backupRacks,JCheckBox backupAssigns,
                       JCheckBox restoreUsers,JCheckBox restoreLabs,
                       JCheckBox restoreRacks,JCheckBox restoreAssigns,
                       JTextField fileName, DefaultListModel<String> dlmLog,
                       GlobalData globalData) {

        this.chkBackupUsers = backupUsers;
        this.chkBackupLabs = backupLabs;
        this.chkBackupRacks = backupRacks;
        this.chkBackupAssigns = backupAssigns;
        this.chkRestoreUsers = restoreUsers;
        this.chkRestoreLabs = restoreLabs;
        this.chkRestoreRacks = restoreRacks;
        this.chkRestoreAssigns = restoreAssigns;
        this.txFileName = fileName;
        this.dlmLog = dlmLog;
        this.globalData = globalData;
    }

    /**
     *
     */
    public void doRestore() {
        dlmLog.addElement("Starting restore:");
        if(txFileName.getText().length() == 0) {
            dlmLog.addElement("!!! No valid input filename !!!");
            return;
        }
        if(chkRestoreUsers.isSelected()) restoreUsers();
        if(chkRestoreLabs.isSelected()) restoreLabs();
        if(chkRestoreRacks.isSelected()) restoreRacks();
        if(chkRestoreAssigns.isSelected()) restoreAssigns();
    }
    /**
     *
     */
    public void doBackup() {
        dlmLog.addElement("Starting backup:");
        BackupRestoreData data = new BackupRestoreData();
        if(txFileName.getText().length() == 0) {
            dlmLog.addElement("!!! No valid output filename !!!");
            return;
        }
        if(chkBackupUsers.isSelected()) backupUsers(data);
        if(chkBackupLabs.isSelected()) backupLabs(data);
        if(chkBackupRacks.isSelected()) backupRacks(data);
        if(chkBackupAssigns.isSelected()) backupAssigns(data);
        try {
            ObjectMapper mapper = new ObjectMapper();
            PrintWriter vystup = new PrintWriter(new FileOutputStream(txFileName.getText(), true));
            String js = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            vystup.write(js);
            vystup.close();
        }
        catch(Exception ex) {
            dlmLog.addElement("!!!!!!!!!!!!   E  R  R  O  R    !!!!!!!!!!!!!");
            dlmLog.addElement(ex.getMessage());
        }
    }

    /**
     *
     */
    private void backupUsers(BackupRestoreData data) {
        dlmLog.addElement("  - BackingUp users: ");
        data.setUsers(globalData.users);
    }
    /**
     *
     */
    private void backupLabs(BackupRestoreData data) {
        dlmLog.addElement("  - BackingUp labs: ");
        data.setLabs(globalData.labs);
    }
    private void backupRacks(BackupRestoreData data) {
        dlmLog.addElement("  - BackingUp racks: ");
        data.setRacks(globalData.testracks);
    }
    private void backupAssigns(BackupRestoreData data) {

    }
    private void restoreUsers() {

    }
    private void restoreLabs() {

    }
    private void restoreRacks() {

    }
    private void restoreAssigns() {

    }


}
