package tools;

import common.GlobalData;
import common.RestCallOutput;
import commonEnum.LabInvitationState;
import commonEnum.RtlRoles;
import dto.TestrackDTO;
import dto.UserDto;
import dto.groups.LabDetailDTO;
import dto.groups.UserDetailPerLabDTO;
import model.Lab;
import service.LabService;
import service.RestCallService;
import tables.labs.*;

import javax.swing.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Vector;

public class LabTools {

    private JTable tblMain, tblUsers, tblRacks;
    private DefaultListModel<String> dlmLog;
    private GlobalData globalData;
    private LabService labService;
    private RestCallService restCallService;
    private JComboBox<String> cbUsers;
    private JComboBox<String> cbRoles;
    private JComboBox<String> cbStates;
    private DefaultListModel<String> dlmLabAdmins;

    /**
     *
     * @param main
     * @param users
     * @param racks
     * @param dlm
     * @param globalData
     * @param labService
     * @param restCallService
     * @param cbUsers
     */
    public LabTools(JTable main, JTable users, JTable racks,
                    DefaultListModel<String> dlm,
                    GlobalData globalData,
                    LabService labService,
                    RestCallService restCallService,
                    JComboBox<String> cbUsers,
                    JComboBox<String> cbRoles,
                    JComboBox<String> cbStates,
                    DefaultListModel<String> dlmLabAdmins) {
        this.tblMain = main;
        this.tblRacks = racks;
        this.tblUsers = users;
        this.dlmLog = dlm;
        this.globalData = globalData;
        this.labService = labService;
        this.restCallService = restCallService;
        this.cbUsers = cbUsers;
        this.cbRoles = cbRoles;
        this.cbStates = cbStates;
        this.dlmLabAdmins = dlmLabAdmins;
    }

    public void ClearAllTabs() {
        Vector<LabTableRow> rows = new Vector<>();
        LabTableModel model = new LabTableModel(rows);
        tblUsers.setModel(model);
    }
    public void ClearLabUserTab() {
        Vector<LabUserTableRow> rows = new Vector<>();
        LabUserTableModel model = new LabUserTableModel(rows);
        tblUsers.setModel(model);
    }
    public void ClearLabRackTab() {
        Vector<LabTestrackTableRow> rows = new Vector<>();
        LabTestrackTableModel model = new LabTestrackTableModel(rows);
        tblRacks.setModel(model);
    }

    /**
     *
     * @return
     */
    public int RemoveLab() {
        int selectedRow = tblMain.getSelectedRow();
        if(selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "No LAB selected !");
            return -1;
        }
        int labId = Integer.parseInt(tblMain.getValueAt(selectedRow, 0).toString());
        RestCallOutput ro = labService.removeLab(labId);
        if(ro.getResultCode() > 299) {
            JOptionPane.showMessageDialog(null, "Error deleting lab - see the logger !");
            dlmLog.addElement(ro.getErrorMsg());
            return -2;
        }
        return 0;
    }

    /**
     *
     * @param name
     * @return
     */
    public int createLab(String name) {
        RestCallOutput ro = labService.createNewLab(name);
        if(ro.getResultCode() > 299) {
            JOptionPane.showMessageDialog(null, "Error creating lab - see the logger !");
            dlmLog.addElement(ro.getErrorMsg());
            return -2;
        }
        return 0;
    }
    /**
     *
     * @param row Index of selected row in tha main lab table
     * @return
     */
    public int UpdateUserAndRacks(int row) {
        int labId;

        Object obj = tblMain.getValueAt(row, 0);
        try { labId = Integer.parseInt(obj.toString()); }
        catch(Exception ex) {
            dlmLog.addElement("Error: " + ex.getMessage());
            return -1;
        }
        globalData.setLastSelectedLabId(labId);
        UpdateUserAndRacksByLabId(labId);
        return 0;
    }
    /**
     *
     * @param labId
     * @return
     */
    public int UpdateUserAndRacksByLabId(int labId) {
        globalData.setLastSelectedLabId(labId);
        ClearLabUserTab();
        List<UserDetailPerLabDTO> labUsers = getUserList(labId);
        Vector<LabUserTableRow> rows = new Vector<>();
        for(UserDetailPerLabDTO user : labUsers) rows.add(user.convertToUserTableRow());
        LabUserTableModel model = new LabUserTableModel(rows);
        tblUsers.setModel(model);

        ClearLabRackTab();
        List<TestrackDTO> labRacks = getRackList(labId);
        Vector<LabTestrackTableRow> trows = new Vector<>();
        for(TestrackDTO rack : labRacks) trows.add(rack.convertToLabTableRow());
        LabTestrackTableModel tmodel = new LabTestrackTableModel(trows);
        tblRacks.setModel(tmodel);

        dlmLabAdmins.clear();
        LabDetailDTO lab = globalData.labs.stream().filter(p -> p.getId() ==labId).findFirst().orElse(null);
        for(String admin : lab.getAdministrators()) dlmLabAdmins.addElement(admin);

        return 0;
    }
    /**
     *
     * @return
     */
    public int RemoveRackFromLab() {
        int selectedRow = tblRacks.getSelectedRow();
        if(selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "No testrack selected !");
            return -1;
        }
        int rackId = Integer.parseInt(tblRacks.getValueAt(selectedRow, 0).toString());
        TestrackDTO rack = findRackById(rackId);
        if(rack == null) {
            dlmLog.addElement("Testrack NOT found !!!!");
            return -2;
        }
        rack.setLab(null);
        RestCallOutput ro = labService.UpdateTestrack(rack);
        if(ro.getResultCode() > 299) {
            dlmLog.addElement("Error: " + ro.getErrorMsg());
            return -3;
        }
        UpdateUserAndRacksByLabId(globalData.getLastSelectedLabId());
        return 0;
    }
    /**
     *
     * @return
     */
    public int RemoveUserFromLab() {
        int selectedRow = tblUsers.getSelectedRow();
        if(selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "No user selected !");
            return -1;
        }
        String userId =tblUsers.getValueAt(selectedRow, 0).toString();
        String labId = String.format("%d", globalData.getLastSelectedLabId());
        RestCallOutput ro = labService.RemoveUserFromLab(userId, labId);
        if(ro.getResultCode() > 299) {
            dlmLog.addElement("Error: " + ro.getErrorMsg());
            return -3;
        }
        UpdateUserAndRacksByLabId(globalData.getLastSelectedLabId());
        return 0;
    }

    /**
     *
     * @return
     */
    public int ModifyUserInLab() {
        int selectedRow = tblUsers.getSelectedRow();
        if(selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "No user selected !");
            return -1;
        }
        String userId =tblUsers.getValueAt(selectedRow, 0).toString();
        String labId = String.format("%d", globalData.getLastSelectedLabId());
        UserDetailPerLabDTO assign = new UserDetailPerLabDTO();
        String role = cbRoles.getSelectedItem().toString();
        assign.setRole(RtlRoles.valueOf(role));
        RestCallOutput ro = labService.ModifyUserInLab(assign, labId, userId);
        if(ro.getResultCode() > 299) {
            dlmLog.addElement("Error: " + ro.getErrorMsg());
            return -3;
        }
        UpdateUserAndRacksByLabId(globalData.getLastSelectedLabId());
        return 0;
    }
    /**
     *
     * @param sRackId
     * @return
     */
    public int AddTrackToLab(String sRackId) {
        int rackId;
        try { rackId = Integer.parseInt(sRackId); }
        catch (Exception ex) {
            dlmLog.addElement("Error: " + ex.getMessage());
            return -1;
        }
        TestrackDTO rack = findRackById(rackId);
        if(rack == null) {
            dlmLog.addElement("Testrack NOT found !!!!");
            return -2;
        }
        Lab lab = findLabById(globalData.getLastSelectedLabId());
        rack.setLab(lab);
        RestCallOutput ro = labService.UpdateTestrack(rack);
        if(ro.getResultCode() > 299) {
            dlmLog.addElement("Error: " + ro.getErrorMsg());
            return -3;
        }
        UpdateUserAndRacksByLabId(globalData.getLastSelectedLabId());
        return 0;
    }
    /**
     *
     * @return
     */
    public int AddUserToLab() {
        String role = cbRoles.getSelectedItem().toString();
        String state = cbStates.getSelectedItem().toString();
        String spom = cbUsers.getSelectedItem().toString();
        String userId = spom.substring(0, spom.indexOf(" "));
        String labId = String.format("%d", globalData.getLastSelectedLabId());

        UserDetailPerLabDTO assign = new UserDetailPerLabDTO(userId, "", LabInvitationState.valueOf(state),
                                                             RtlRoles.valueOf(role));

        RestCallOutput ro = labService.AddUserToLab(assign, labId);
        if(ro.getResultCode() > 299) {
            dlmLog.addElement("Error: " + ro.getErrorMsg());
            return -3;
        }
        UpdateUserAndRacksByLabId(globalData.getLastSelectedLabId());
        return 0;
    }
    /**
     *
     */
    public void UpdateDependencies() {
        cbUsers.removeAllItems();
        for(UserDto user : globalData.users) {
            cbUsers.addItem(user.getId() + "   " + user.getEmail());
        }
    }
    /**
     *
     * @param labId
     * @return
     */
    private List<UserDetailPerLabDTO> getUserList(int labId) {
        RestCallOutput ro = labService.getUsersForLab(labId);
        if(ro.getResultCode()>299) {
            dlmLog.addElement("Error: " + ro.getErrorMsg());
            return null;
        }
        else {
            return (List<UserDetailPerLabDTO>) ro.getOutputData();
        }
    }
    /**
     *
     * @param labId
     * @return
     */
    private List<TestrackDTO> getRackList(int labId) {
        RestCallOutput ro = labService.getRacksForLab(labId);
        if(ro.getResultCode()>299) {
            dlmLog.addElement("Error: " + ro.getErrorMsg());
            return null;
        }
        else {
            return (List<TestrackDTO>) ro.getOutputData();
        }
    }
    /**
     *
     * @param labId
     * @return
     */
    private Lab findLabById(int labId) {
        for(LabDetailDTO lab : globalData.labs) {
            if(lab.getId() == labId) {
                return new Lab(labId, lab.getName(), ConvertToTimestamp(lab.getCreatedOn()));
            }
        }
        return null;
    }
    /**
     *
     * @param millis
     * @return
     */
    private Timestamp ConvertToTimestamp(long millis) {
        Instant instant = Instant.ofEpochSecond(millis);
        return Timestamp.from(instant);
    }
    /**
     *
     * @param id
     * @return
     */
    private TestrackDTO findRackById(int id) {
       List<TestrackDTO> racks = globalData.testracks;
       for(TestrackDTO rack : racks) {
           if(rack.getId() == (long)id) return rack;
       }
       return null;
    }
}
