import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import common.GlobalData;
import common.JsonProcessing;
import common.RestCallOutput;
import commonEnum.DisplayType;
import commonEnum.TestrackPlatform;
import dto.*;
import service.RestCallService;
import tables.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.List;

public class frmMain {
    private JPanel panelMain;
    private JTabbedPane tabbedPane1;
    private JTextField txBeIpAddress;
    private JTextField txBePort;
    private JTextField txUserUserName;
    private JTextField txUserPassword;
    private JButton btnUserLogin;
    private JList lbUserLog;
    private JButton btnUserClearLog;
    private JTextArea txaUserFeedback;
    private JButton btnUserClearText;
    private JTable tblUser;
    private JButton btnGetAllUsers;
    private JTabbedPane tabbedPane2;
    private JTextField txCreateUserFirstName;
    private JTextField txCreateUserLastName;
    private JTextField txCreateUserEmail;
    private JButton btnCreateUser;
    private JTabbedPane tabbedPane3;
    private JTable tblTestracks;
    private JButton btnGetAllTestracks;
    private JComboBox cbUserMailUsers;
    private JButton btnUserMailUpdateUsers;
    private JButton btnUserMailResendVerifyEmail;
    private JButton btnHeartbeatUpdateRacks;
    private JComboBox cbHeartbeatRacks;
    private JButton btnHeartbeatSendHb;
    private JTabbedPane tabbedPane4;
    private JButton btnStartSimulation;
    private JLabel lbSysTime;
    private JCheckBox chkFrontendConnectRack;
    private JTextField txFrontendRackId;
    private JTable tblFrontendRacks;
    private JComboBox cbCreateRackPlatform;
    private JTextField txCreateRackName;
    private JTextField txCreateRackDescr;
    private JTextField txCreateRackAddr;
    private JTextField txCreateRackIpAddr;
    private JTextField txCreateRackQuidoPort;
    private JTextField txCreateRackRelayPosition;
    private JTextField txCreateRackRelayName;
    private JTextField txCreateRaclRelayType;
    private JComboBox cbCreateRackDispType;
    private JTextField txCreateRackDispWidth;
    private JTextField txCreateRackDispHeight;
    private JTextField txCreateRackDispPort;
    private JButton btnCreatRack;
    private JButton btnDeleteTestrack;
    private JList lbTestrackData;
    private JButton btnClearTestrackData;
    private JButton lbGetRackDetails;
    private JTextField txCreateRackNoOfDevices;
    private JButton btnAddDisplay;
    private JButton btnRemoveDisplay;
    private JTabbedPane tabbedPane5;
    private JTextField txConfigSignalServerPort;
    private JButton btnSignalReadAllDevices;
    private JList lbLogSignal;
    private JTable tblSignal;
    private JButton btnClearSignalLog;
    private JComboBox cbSignalDevices;
    private JButton btnSignaUpdateDevices;
    private JTextField txNewDeviceId;
    private JTextField txNewDeviceName;
    private JTextField txNewDeviceDescr;
    private JTextField txNewDeviceInAudio;
    private JTextField txNewDeviceInVideo;
    private JTextField txNewDeviceHostAudio;
    private JTextField txNewDeviceOutAudioPort;
    private JButton btnCreateNewDevice;
    private JButton btnDeleteMmDevice;
    private JTextField txUserLoginState;
    private JTextField txRackHeartbeatResponse;
    private JButton btnSendRackHandOver;
    private JTextField txRackHandOverResponse;
    private JTextField txFrontEndLastResponse;
    private JTextField txFrontEndLastSingleResponse;


    private DefaultListModel<String> dlmUserLog = new DefaultListModel<>();
    private DefaultListModel<String> dlmRackData = new DefaultListModel<>();
    private DefaultListModel<String> dlmSignal = new DefaultListModel<>();

    private RestCallService restCall;
    private GlobalData globalData = new GlobalData();
    private JsonProcessing jsonProcessing;

    private timerListener Casovac = new timerListener();
    private Timer timer = new Timer(1000, Casovac);
    private boolean Running = false;
    private boolean ControllingTestrack = false;
    private int Counter1 = 0, Counter2 = 0, Counter3 = 0;

    /**
     *
     */
    public frmMain() {
        lbUserLog.setModel(dlmUserLog);
        lbTestrackData.setModel(dlmRackData);
        lbLogSignal.setModel(dlmSignal);

        dlmUserLog.addElement("Start log .....");

        restCall = new RestCallService(
                txBeIpAddress.getText(),
                Integer.parseInt(txBePort.getText()),
                dlmUserLog
        );
        jsonProcessing = new JsonProcessing(dlmSignal);

        cbCreateRackPlatform.removeAllItems();
        for (TestrackPlatform p : TestrackPlatform.values())
            cbCreateRackPlatform.addItem(p);
        cbCreateRackDispType.removeAllItems();
        for (DisplayType p : DisplayType.values())
            cbCreateRackDispType.addItem(p);

        timer.start();

        btnUserClearLog.addActionListener(e -> dlmUserLog.clear());
        btnUserLogin.addActionListener(e -> getLoginToken());
        btnUserClearText.addActionListener(e -> txaUserFeedback.setText(""));
        btnGetAllUsers.addActionListener(e -> getAllUsers());
        btnCreateUser.addActionListener(e -> createNewUser());
        btnGetAllTestracks.addActionListener(e -> GetAllTestracks());
        btnUserMailUpdateUsers.addActionListener(e -> UpdateUserListInMail());
        btnUserMailResendVerifyEmail.addActionListener(e -> ResendVerifyEmail());
        btnHeartbeatUpdateRacks.addActionListener(e -> HeartbeatUpdateRacks());
        btnHeartbeatSendHb.addActionListener(e -> SendOneHeartBeat());
        btnStartSimulation.addActionListener(e -> SwitchRunning());
        btnCreatRack.addActionListener(e -> CreateNewRack());
        btnDeleteTestrack.addActionListener(e -> DeleteTestrack());
        btnClearTestrackData.addActionListener(e -> dlmRackData.clear());
        lbGetRackDetails.addActionListener(e -> GetRackDetails());
        btnAddDisplay.addActionListener(e -> RackAddDisplay());
        btnRemoveDisplay.addActionListener(e -> RackRemoveDisplay());
        btnClearSignalLog.addActionListener(e -> dlmSignal.clear());
        btnSignalReadAllDevices.addActionListener(e -> ReadAllMmDevices());
        btnSignaUpdateDevices.addActionListener(e -> UpdateDevices());
        btnCreateNewDevice.addActionListener(e -> CreateNewMmDevice());
        btnDeleteMmDevice.addActionListener(e -> DeleteMmDevice());
        btnSendRackHandOver.addActionListener(e -> HandOverRack());
        chkFrontendConnectRack.addActionListener(e -> SwitchRackControl());
    }

    private void HandOverRack() {
        String rackId = GetRackIdByName(cbHeartbeatRacks.getSelectedItem().toString());
        if (rackId.length() > 0) {
            RestCallOutput res = restCall.sendHandOverRack(rackId, globalData.token.getToken(), true);
            int iRes = res.getResultCode();
            if(iRes < 300) {
                String msg = "Response: " + iRes ;
                txRackHandOverResponse.setText(msg);
            }
            else {
                String msg = "Response: " + iRes ;
                txRackHandOverResponse.setText(msg);
                JOptionPane.showMessageDialog(null, "ERROR: Result code: " + iRes);
            }
        }
    }

    private void DeleteMmDevice() {
        MmDevice device = new MmDevice();
        device.setId(cbSignalDevices.getSelectedItem().toString());

        Map<String, String> props = null;
        String jsonString = "";
        try {
            props = new HashMap<>();
            props.put("Content-Type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            jsonString = mapper.writeValueAsString(device);
        } catch (Exception ex) {

        }
        String surl = "http://" + txBeIpAddress.getText() + ":" + txConfigSignalServerPort.getText()
                + "/device";
        RestCallOutput ro = restCall.SendRestApiRequest("DELETE", props, jsonString, surl);
        JOptionPane.showMessageDialog(null, "RESULT CODE: " + ro.getResultCode());
        if (ro.getResultCode() > 230) {
            dlmSignal.addElement("POST Err: " + ro.getErrorMsg());
        }

    }

    private void CreateNewMmDevice() {
        MmDevice device = new MmDevice();
        device.setId(txNewDeviceId.getText());
        device.setName(txNewDeviceName.getText());
        device.setDescription(txNewDeviceDescr.getText());
        device.setPortIncomingAudio(Integer.parseInt(txNewDeviceInAudio.getText()));
        device.setPortIncomingVideo(Integer.parseInt(txNewDeviceInVideo.getText()));
        device.setHostOutgoingAudio(txNewDeviceHostAudio.getText());
        device.setPortOutgoingAudio(Integer.parseInt(txNewDeviceOutAudioPort.getText()));
        device.setActive(true);

        Map<String, String> props = null;
        String jsonString = "";
        try {
            props = new HashMap<>();
            props.put("Content-Type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            jsonString = mapper.writeValueAsString(device);
        } catch (Exception ex) {

        }
        String surl = "http://" + txBeIpAddress.getText() + ":" + txConfigSignalServerPort.getText()
                + "/device";
        RestCallOutput ro = restCall.SendRestApiRequest("POST", props, jsonString, surl);
        JOptionPane.showMessageDialog(null, "RESULT CODE: " + ro.getResultCode());
        if (ro.getResultCode() > 230) {
            dlmSignal.addElement("POST Err: " + ro.getErrorMsg());
        }

    }

    private void UpdateDevices() {
        cbSignalDevices.removeAllItems();
        for (MmDevice dev : globalData.mmDevices) cbSignalDevices.addItem(dev.getId());
    }

    private void ReadAllMmDevices() {
        dlmSignal.addElement("Reading all devices from SIGNAL ");
        String surl = "http://" + txBeIpAddress.getText() + ":" + txConfigSignalServerPort.getText() + "/devices";
        dlmSignal.addElement("URL: " + surl);

        try {
            Map<String, String> props = new HashMap<>();
            props.put("Accept", "*/*");
            RestCallOutput ro = restCall.SendRestApiRequest("GET", props, null, surl);
            dlmSignal.addElement("RESULT: " + ro.getResultCode());
            if (ro.getResultCode() < 300) {
                globalData.mmDevices = jsonProcessing.ParseDeviceList(ro.getDataMsg());
                Vector<DevicesTable1> data = new Vector<>();
                for (MmDevice dev : globalData.mmDevices) {
                    DevicesTable1 row = new DevicesTable1(dev.getId(), dev.getName(), dev.getDescription(),
                            dev.getPortIncomingAudio(), dev.getPortIncomingVideo(),
                            dev.getHostOutgoingAudio(), dev.getPortOutgoingAudio(),
                            dev.isActive());
                    data.add(row);
                }
                tblSignal.setModel(new DevicesTable1Model(data));
            }
        } catch (Exception ex) {
            dlmSignal.addElement("ERR: " + ex.getMessage());
        }
    }

    private void RackAddDisplay() {
        int rackpos = GetRackOrderByName(cbHeartbeatRacks.getSelectedItem().toString());
        if (rackpos >= 0) {
            TestrackDTO rack = globalData.testracks.get(rackpos);

            int noDisplays = rack.getTestrackDisplays().size();
            dlmRackData.addElement(" Current no.of displays = " + noDisplays);
            switch (noDisplays) {
                case 3:
                    dlmRackData.addElement(" Unable to add display !!!! ");
                    return;
                case 2:
                    dlmRackData.addElement(" Adding HUD .....");
                    AddDisplay(rack, DisplayType.HUD);
                    UpdateRack(rack);
                    break;
                case 1:
                    dlmRackData.addElement(" Removing FPK .....");
                    AddDisplay(rack, DisplayType.FPK);
                    UpdateRack(rack);
                    break;
            }
        }
    }

    private void AddDisplay(TestrackDTO rack, DisplayType type) {
        TestrackDisplayDTO display0 = new TestrackDisplayDTO();

        display0.setType(type);
        display0.setWidth(1000);
        display0.setHeight(600);
        display0.setMgbport(0);
        rack.getTestrackDisplays().add(display0);
    }

    private void RackRemoveDisplay() {
        int rackpos = GetRackOrderByName(cbHeartbeatRacks.getSelectedItem().toString());
        if (rackpos >= 0) {
            TestrackDTO rack = globalData.testracks.get(rackpos);

            int noDisplays = rack.getTestrackDisplays().size();
            dlmRackData.addElement(" Current no.of displays = " + noDisplays);
            switch (noDisplays) {
                case 1:
                    dlmRackData.addElement(" Unable to remove display !!!! ");
                    return;
                case 2:
                    dlmRackData.addElement(" Removing FPK .....");
                    RemoveFpk(rack);
                    UpdateRack(rack);
                    break;
                case 3:
                    dlmRackData.addElement(" Removing HUD .....");
                    RemoveHud(rack);
                    UpdateRack(rack);
                    break;
            }
        }
    }

    private void RemoveFpk(TestrackDTO rack) {
        for (Iterator<TestrackDisplayDTO> iterator = rack.getTestrackDisplays().iterator(); iterator.hasNext(); ) {
            TestrackDisplayDTO disp = iterator.next();
            if (disp.getType() == DisplayType.FPK) {
                iterator.remove();
            }
        }
    }

    private void RemoveHud(TestrackDTO rack) {
        for (Iterator<TestrackDisplayDTO> iterator = rack.getTestrackDisplays().iterator(); iterator.hasNext(); ) {
            TestrackDisplayDTO disp = iterator.next();
            if (disp.getType() == DisplayType.HUD) {
                iterator.remove();
            }
        }
    }

    private void UpdateRack(TestrackDTO rack) {
        RestCallOutput res = restCall.updateTestrack(rack, globalData.token.getToken(), true);
        JOptionPane.showMessageDialog(null, "Result code: " + res.getResultCode());
        dlmUserLog.addElement("JSON:");
        dlmUserLog.addElement(res.getInfoMsg());
        if (res.getResultCode() >= 300) {
            dlmUserLog.addElement("Error:");
            dlmUserLog.addElement(res.getErrorMsg());
            dlmUserLog.addElement("MSG:");
            dlmUserLog.addElement(res.getDataMsg());
        }
    }


    private void GetRackDetails() {
        int rackpos = GetRackOrderByName(cbHeartbeatRacks.getSelectedItem().toString());
        if (rackpos >= 0) {
            TestrackDTO rack = globalData.testracks.get(rackpos);
            dlmRackData.addElement("===  Displays ===");
            int i = 0;
            Iterator<TestrackDisplayDTO> it = rack.getTestrackDisplays().iterator();
            while (it.hasNext()) {
                TestrackDisplayDTO disp = it.next();
                dlmRackData.addElement(" [" + i + "]");
                dlmRackData.addElement("    ID:       " + disp.getId());
                dlmRackData.addElement("    Type:     " + disp.getType().toString());
                dlmRackData.addElement("    Width:    " + disp.getWidth());
                dlmRackData.addElement("    Height:   " + disp.getHeight());
                dlmRackData.addElement("    MGB Port: " + disp.getMgbport());
                i++;
            }
        }
    }

    private void DeleteTestrack() {

        String rackId = GetRackIdByName(cbHeartbeatRacks.getSelectedItem().toString());
        if (rackId.length() > 0) {
            RestCallOutput res = restCall.deleteTestrack(rackId, globalData.token.getToken(), true);
            int iRes = res.getResultCode();
            JOptionPane.showMessageDialog(null, "Result code: " + iRes);
        }
    }

    /**
     *
     */
    private void CreateNewRack() {

        int NoDevices = Integer.parseInt(txCreateRackNoOfDevices.getText());
        TestrackDTO rack = new TestrackDTO();
        NetworkDTO network = new NetworkDTO();
        TestrackDisplayDTO display0 = new TestrackDisplayDTO();
        TestrackDisplayDTO display1 = new TestrackDisplayDTO();
        TestrackDisplayDTO display2 = new TestrackDisplayDTO();
        RelayDefinitionDTO relay = new RelayDefinitionDTO();
        ArrayList<TestrackDisplayDTO> displays = new ArrayList<>();

        network.setIp(txCreateRackIpAddr.getText());
        network.setQuidoPort(Integer.parseInt(txCreateRackQuidoPort.getText()));

        display0.setType(DisplayType.ABT);
        display0.setWidth(Integer.parseInt(txCreateRackDispWidth.getText()));
        display0.setHeight(Integer.parseInt(txCreateRackDispHeight.getText()));
        display0.setMgbport(0);
        displays.add(display0);

        if (NoDevices > 1) {
            display1.setType(DisplayType.FPK);
            display1.setWidth(1200);
            display1.setHeight(480);
            display0.setMgbport(0);
            displays.add(display1);
        }
        if (NoDevices > 2) {
            display2.setType(DisplayType.HUD);
            display2.setWidth(600);
            display2.setHeight(300);
            display0.setMgbport(0);
            displays.add(display2);
        }
        relay.setName(txCreateRackRelayName.getText());
        relay.setPosition(Integer.parseInt(txCreateRackRelayPosition.getText()));
        relay.setType(txCreateRaclRelayType.getText());
        relay.setValueeditable(true);

        rack.setName(txCreateRackName.getText());
        rack.setDescription(txCreateRackDescr.getText());
        rack.setAddress(txCreateRackAddr.getText());
        rack.setPlatform((TestrackPlatform) cbCreateRackPlatform.getSelectedItem());
        rack.setNetwork(network);
        rack.setTestrackDisplays(new HashSet<TestrackDisplayDTO>(displays));
        rack.setRelayDefinitions(new HashSet<RelayDefinitionDTO>(Arrays.asList(relay)));

        RestCallOutput res = restCall.createNewRack(rack, globalData.token.getToken(), true);
        JOptionPane.showMessageDialog(null, "Result code: " + res.getResultCode());
        dlmUserLog.addElement("JSON:");
        dlmUserLog.addElement(res.getInfoMsg());
        if (res.getResultCode() >= 300) {
            dlmUserLog.addElement("Error:");
            dlmUserLog.addElement(res.getErrorMsg());
            dlmUserLog.addElement("MSG:");
            dlmUserLog.addElement(res.getDataMsg());
        }

    }

    /**
     *
     */
    private void SwitchRackControl() {

        if(chkFrontendConnectRack.isSelected()) {  // was checked
            if(Running) {   // start/stop controlling ?
                if(ControllingTestrack) { // now controlling -> stop
                    ControllingTestrack = false;
                }
                else {  // now NOT Controlling -> start sequence
                    // send CONTROL
                    RestCallOutput ro = restCall.sendHandOverRack(txFrontendRackId.getText(),
                                                    globalData.token.getToken(), false);
                    if(ro.getResultCode() < 300) {
                        txFrontEndLastSingleResponse.setText("SendHandOverRack:   RESULT=" + ro.getResultCode());
                    }
                    else {
                        txFrontEndLastSingleResponse.setText(
                                "ERR: RESULT=" + ro.getResultCode() + "  MSG=" + ro.getErrorMsg());
                    }
                }
            }
            else {  // error - cannot start control if not running
                txFrontEndLastResponse.setText("Cannot start CONTROL if FE SIM is not running!");
            }
        }
        else {  // was unchecked
            ControllingTestrack = false;
        }
    }

    private void SwitchRunning() {
        if (Running) {
            Running = false;
            btnStartSimulation.setBackground(new Color(255, 100, 100));
            btnStartSimulation.setText("START Simulation");
        } else {
            Running = true;
            btnStartSimulation.setBackground(new Color(100, 255, 100));
            btnStartSimulation.setText("STOP Simulation");
            Counter1 = Counter2 = Counter3 = 0;
        }
    }

    private void SendOneHeartBeat() {
        String rackId = GetRackIdByName(cbHeartbeatRacks.getSelectedItem().toString());
        if (rackId.length() > 0) {
            RestCallOutput res = restCall.sendHeartbeat(rackId, globalData.token.getToken(), true);
            int iRes = res.getResultCode();
            if(iRes < 300) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    HeartbeatResponse resp = mapper.readValue(res.getDataMsg(), HeartbeatResponse.class);
                    String msg = "Response: " + iRes + "     CurrentUser:" + resp.getControlledBy();
                    txRackHeartbeatResponse.setText(msg);
                }
                catch(Exception ex) {
                    dlmRackData.addElement("Error during JSON parsing:");
                    dlmRackData.addElement(res.getDataMsg());
                    dlmRackData.addElement(ex.getMessage());
                    return;
                }
            }
            else {
                String msg = "Response: " + iRes ;
                txRackHeartbeatResponse.setText(msg);
                JOptionPane.showMessageDialog(null, "ERROR: Result code: " + iRes);
            }
        }
    }

    /**
     * Find the ID (database column) of given testrack
     *
     * @param name
     * @return
     */
    private String GetRackIdByName(String name) {
        for (int i = 0; i < globalData.testracks.size(); i++) {
            if (globalData.testracks.get(i).getName().equals(name)) {
                return globalData.testracks.get(i).getId().toString();
            }
        }
        return "";
    }

    /**
     * Find the order of given testrack in globalData.Testracks
     *
     * @param name
     * @return
     */
    private int GetRackOrderByName(String name) {
        for (int i = 0; i < globalData.testracks.size(); i++) {
            if (globalData.testracks.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private void HeartbeatUpdateRacks() {
        cbHeartbeatRacks.removeAllItems();
        for (int i = 0; i < globalData.testracks.size(); i++) {
            cbHeartbeatRacks.addItem(globalData.testracks.get(i).getName());
        }
    }

    private void ResendVerifyEmail() {
        String UserId = GetUserIdByEmail(cbUserMailUsers.getSelectedItem().toString());
        if (UserId.length() > 0) {
            int iRes = restCall.sendVerifyEmail(UserId, globalData.token.getToken()).getResultCode();
            JOptionPane.showMessageDialog(null, "Result code: " + iRes);
        }
    }

    private String GetUserIdByEmail(String email) {
        for (int i = 0; i < globalData.users.size(); i++) {
            if (globalData.users.get(i).getEmail().equals(email)) {
                return globalData.users.get(i).getId();
            }
        }
        return "";
    }

    private void UpdateUserListInMail() {
        cbUserMailUsers.removeAllItems();
        for (int i = 0; i < globalData.users.size(); i++) {
            cbUserMailUsers.addItem(globalData.users.get(i).getEmail());
        }
    }

    /**
     *
     */
    private void GetAllTestracks() {
        RestCallOutput res = restCall.readAllTestracks(globalData.token.getToken(), true);
        List<TestrackDTO> racks = (List<TestrackDTO>) res.getOutputData();
        Vector<TestrackTable1> rows = new Vector<>();
        if (racks != null) {
            globalData.testracks = racks;
            for (int i = 0; i < racks.size(); i++) {
                TestrackDTO rack = racks.get(i);
                ArrayList<TestrackDisplayDTO> dl = new ArrayList<TestrackDisplayDTO>(rack.getTestrackDisplays());
                rows.add(new TestrackTable1(rack.getId(), rack.getName(), rack.getDescription(),
                        rack.getAddress(), rack.getPlatform(), rack.getAvailability(),
                        rack.getNetwork().getIp(),
                        dl.size() == 0 ? 0 : dl.get(0).getMgbport()));
            }
            TestrackTable1Model model = new TestrackTable1Model(rows);
            tblTestracks.setModel(model);
        }
    }

    /**
     *
     */
    private void createNewUser() {
        UserDto user = new UserDto();
        user.setEmail(txCreateUserEmail.getText());
        user.setFirstName(txCreateUserFirstName.getText());
        user.setLastName(txCreateUserLastName.getText());
        RestCallOutput res = restCall.createNewUser(user, globalData.token.getToken(), true);
        int ires = res.getResultCode();
        JOptionPane.showMessageDialog(null, "Result Code = " + ires);
    }

    /**
     *
     */
    private void getAllUsers() {
        if (globalData.token == null) {
            JOptionPane.showMessageDialog(null, "No Access token available !");
            return;
        }
        RestCallOutput res = restCall.readAllUsers(globalData.token.getToken(), true);
        if (res.getResultCode() > 299) {
            JOptionPane.showMessageDialog(null, "Error:\n" + res.getErrorMsg());
        }
        List<UserDto> users = (List<UserDto>) res.getOutputData();
        Vector<UserTable1> rows = new Vector<>();
        if (users != null) {
            globalData.users = users;
            String result = "";
            for (int i = 0; i < users.size(); i++) {
                UserDto user = users.get(i);
                result += user.getId() + "  |  " + user.getUsername()
                        + "  |  " + user.getFirstName() + "  |  " + user.getLastName()
                        + "  |  " + user.getEmail() + "  |  " + user.getRole() + "\n";
                rows.add(new UserTable1(user.getId(), user.getUsername(), user.getFirstName(),
                        user.getLastName(), user.getEmail()));
            }
            txaUserFeedback.setText(result);
            UserTable1Model model = new UserTable1Model(rows);
            tblUser.setModel(model);
        }
    }

    /**
     *
     */
    private void getLoginToken() {
        RestCallOutput res = restCall.getLoginToken(txUserUserName.getText(), txUserPassword.getText(), true);
        if(res.getResultCode() < 300) {
            AccessTokenDto token = (AccessTokenDto) res.getOutputData();
            txaUserFeedback.setText("Access TOKEN:\n" + token.getToken()
                    + "\nRefresh TOKEN:\n" + token.getRefreshToken());
            globalData.token = token;
            txUserLoginState.setText("Successfully Logged");
        }
        else {
            txUserLoginState.setText("Log Error !");
            JOptionPane.showMessageDialog(null, "Error getting logged - see the Logger");
            dlmUserLog.addElement(res.getErrorMsg());
        }
    }

    private void TimerEvent() {

        if (Running) {
            lbSysTime.setText(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toLocalTime().toString());
            Counter1++;
            if (Counter1 >= 3600) {  // EACH 3600 seconds
                Counter1 = 0;
                RestCallOutput res = restCall.getLoginToken(txUserUserName.getText(), txUserPassword.getText(), false);
                globalData.token = (AccessTokenDto) res.getOutputData();
            }
            //---------  EACH SECOND  -----------
            RestCallOutput res = restCall.readAllTestracks(globalData.token.getToken(), false);
            globalData.testracks = (List<TestrackDTO>) res.getOutputData();
            UpdateRackTable();
            if (chkFrontendConnectRack.isSelected()) {
                RestCallOutput ro = restCall.sendHeartbeat(
                        txFrontendRackId.getText(), globalData.token.getToken(), false);
                txFrontEndLastResponse.setText("Send HB:  RESULT=" + ro.getResultCode());
            }
        } else {
            lbSysTime.setText("Stopped ....");
        }
    }

    private void UpdateRackTable() {
        Vector<TestrackTable1> rows = new Vector<>();
        for (int i = 0; i < globalData.testracks.size(); i++) {
            TestrackDTO rack = globalData.testracks.get(i);
            ArrayList<TestrackDisplayDTO> dl = new ArrayList<TestrackDisplayDTO>(rack.getTestrackDisplays());
            rows.add(new TestrackTable1(rack.getId(), rack.getName(), rack.getDescription(),
                    rack.getAddress(), rack.getPlatform(), rack.getAvailability(),
                    rack.getNetwork().getIp(),
                    dl.size() == 0 ? 0 : dl.get(0).getMgbport()));
        }
        TestrackTable1Model model = new TestrackTable1Model(rows);
        tblFrontendRacks.setModel(model);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("RTL FrontEnd Simulator");
        frame.setContentPane(new frmMain().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(900, 600));
    }

    private class timerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            TimerEvent();
        }
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        panelMain.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("User Functions", panel1);
        tabbedPane2 = new JTabbedPane();
        panel1.add(tabbedPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2.addTab("Create User", panel2);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblUser = new JTable();
        scrollPane1.setViewportView(tblUser);
        btnGetAllUsers = new JButton();
        btnGetAllUsers.setText("Get All Users");
        panel2.add(btnGetAllUsers, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(386, 30), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 2, new Insets(5, 5, 5, 5), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Login", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, -1, 11, panel3.getFont()), new Color(-15527846)));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 12, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(4);
        label1.setText("User  Name:");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(115, 16), null, 0, false));
        txUserUserName = new JTextField();
        Font txUserUserNameFont = this.$$$getFont$$$(null, -1, 12, txUserUserName.getFont());
        if (txUserUserNameFont != null) txUserUserName.setFont(txUserUserNameFont);
        txUserUserName.setText("admin");
        panel3.add(txUserUserName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(244, 26), null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 12, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setHorizontalAlignment(4);
        label2.setText("Password:");
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(115, 16), null, 0, false));
        txUserPassword = new JTextField();
        Font txUserPasswordFont = this.$$$getFont$$$(null, -1, 12, txUserPassword.getFont());
        if (txUserPasswordFont != null) txUserPassword.setFont(txUserPasswordFont);
        txUserPassword.setText("admin");
        panel3.add(txUserPassword, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(244, 26), null, 0, false));
        btnUserLogin = new JButton();
        btnUserLogin.setText("Login");
        panel3.add(btnUserLogin, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(244, 30), null, 0, false));
        txUserLoginState = new JTextField();
        Font txUserLoginStateFont = this.$$$getFont$$$(null, -1, 12, txUserLoginState.getFont());
        if (txUserLoginStateFont != null) txUserLoginState.setFont(txUserLoginStateFont);
        txUserLoginState.setText("Not Logged");
        panel3.add(txUserLoginState, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(244, 26), null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, 12, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setHorizontalAlignment(4);
        label3.setText("Result:");
        panel3.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(115, 16), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(4, 2, new Insets(5, 5, 5, 5), -1, -1));
        panel2.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(386, 131), null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Create New User", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, -1, 11, panel4.getFont()), new Color(-15527846)));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, 12, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setHorizontalAlignment(4);
        label4.setText("First  Name:");
        panel4.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, 12, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Last  Name:");
        panel4.add(label5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, Font.BOLD, 12, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setHorizontalAlignment(4);
        label6.setText("Email:");
        panel4.add(label6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateUserFirstName = new JTextField();
        Font txCreateUserFirstNameFont = this.$$$getFont$$$(null, -1, 12, txCreateUserFirstName.getFont());
        if (txCreateUserFirstNameFont != null) txCreateUserFirstName.setFont(txCreateUserFirstNameFont);
        txCreateUserFirstName.setText("");
        panel4.add(txCreateUserFirstName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateUserLastName = new JTextField();
        Font txCreateUserLastNameFont = this.$$$getFont$$$(null, -1, 12, txCreateUserLastName.getFont());
        if (txCreateUserLastNameFont != null) txCreateUserLastName.setFont(txCreateUserLastNameFont);
        txCreateUserLastName.setText("");
        panel4.add(txCreateUserLastName, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateUserEmail = new JTextField();
        Font txCreateUserEmailFont = this.$$$getFont$$$(null, -1, 12, txCreateUserEmail.getFont());
        if (txCreateUserEmailFont != null) txCreateUserEmail.setFont(txCreateUserEmailFont);
        txCreateUserEmail.setText("");
        panel4.add(txCreateUserEmail, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnCreateUser = new JButton();
        btnCreateUser.setText("Create New User");
        panel4.add(btnCreateUser, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2.addTab("Mail Functions", panel5);
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null, Font.BOLD, 12, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("User:");
        panel5.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel5.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cbUserMailUsers = new JComboBox();
        panel5.add(cbUserMailUsers, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnUserMailUpdateUsers = new JButton();
        btnUserMailUpdateUsers.setText("Update User List");
        panel5.add(btnUserMailUpdateUsers, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnUserMailResendVerifyEmail = new JButton();
        btnUserMailResendVerifyEmail.setText("Resend Verification Email");
        panel5.add(btnUserMailResendVerifyEmail, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Testrack Functions", panel6);
        tabbedPane3 = new JTabbedPane();
        panel6.add(tabbedPane3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane3.addTab("Testrack List", panel7);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel7.add(scrollPane2, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblTestracks = new JTable();
        scrollPane2.setViewportView(tblTestracks);
        btnGetAllTestracks = new JButton();
        btnGetAllTestracks.setText("Get All Testracks");
        panel7.add(btnGetAllTestracks, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel7.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(5, 7, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane3.addTab("Testrack Services", panel8);
        btnHeartbeatUpdateRacks = new JButton();
        btnHeartbeatUpdateRacks.setText("Update  Testracks");
        panel8.add(btnHeartbeatUpdateRacks, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$(null, Font.BOLD, 12, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setText("Testrack:");
        panel8.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbHeartbeatRacks = new JComboBox();
        panel8.add(cbHeartbeatRacks, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnHeartbeatSendHb = new JButton();
        btnHeartbeatSendHb.setText("Send HeartBeat Tick");
        panel8.add(btnHeartbeatSendHb, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDeleteTestrack = new JButton();
        btnDeleteTestrack.setBackground(new Color(-6612697));
        btnDeleteTestrack.setForeground(new Color(-792));
        btnDeleteTestrack.setText("Delete Testrack");
        panel8.add(btnDeleteTestrack, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane3 = new JScrollPane();
        panel8.add(scrollPane3, new GridConstraints(4, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbTestrackData = new JList();
        Font lbTestrackDataFont = this.$$$getFont$$$("Courier New", -1, 12, lbTestrackData.getFont());
        if (lbTestrackDataFont != null) lbTestrackData.setFont(lbTestrackDataFont);
        scrollPane3.setViewportView(lbTestrackData);
        btnClearTestrackData = new JButton();
        btnClearTestrackData.setText("Clear Data");
        panel8.add(btnClearTestrackData, new GridConstraints(3, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lbGetRackDetails = new JButton();
        lbGetRackDetails.setText("Get Testrack Details");
        panel8.add(lbGetRackDetails, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel8.add(spacer4, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnAddDisplay = new JButton();
        btnAddDisplay.setText("Add Display");
        panel8.add(btnAddDisplay, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRemoveDisplay = new JButton();
        btnRemoveDisplay.setText("Remove Display");
        panel8.add(btnRemoveDisplay, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txRackHeartbeatResponse = new JTextField();
        panel8.add(txRackHeartbeatResponse, new GridConstraints(1, 2, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnSendRackHandOver = new JButton();
        btnSendRackHandOver.setText("Hand Over Selected Rack");
        panel8.add(btnSendRackHandOver, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txRackHandOverResponse = new JTextField();
        panel8.add(txRackHandOverResponse, new GridConstraints(2, 2, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(13, 4, new Insets(0, 0, 0, 0), -1, -1));
        Font panel9Font = this.$$$getFont$$$(null, Font.BOLD, 16, panel9.getFont());
        if (panel9Font != null) panel9.setFont(panel9Font);
        tabbedPane3.addTab("Create Rack", panel9);
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$(null, Font.BOLD, 12, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setText("Name:");
        panel9.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel9.add(spacer5, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null, Font.BOLD, 12, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setText("Description:");
        panel9.add(label10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null, Font.BOLD, 12, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setText("Address:");
        panel9.add(label11, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$(null, Font.BOLD, 12, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setText("Platform:");
        panel9.add(label12, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbCreateRackPlatform = new JComboBox();
        panel9.add(cbCreateRackPlatform, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        txCreateRackName = new JTextField();
        Font txCreateRackNameFont = this.$$$getFont$$$(null, -1, 12, txCreateRackName.getFont());
        if (txCreateRackNameFont != null) txCreateRackName.setFont(txCreateRackNameFont);
        txCreateRackName.setText("Testrack #11");
        panel9.add(txCreateRackName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackDescr = new JTextField();
        Font txCreateRackDescrFont = this.$$$getFont$$$(null, -1, 12, txCreateRackDescr.getFont());
        if (txCreateRackDescrFont != null) txCreateRackDescr.setFont(txCreateRackDescrFont);
        txCreateRackDescr.setText("Demo rack");
        panel9.add(txCreateRackDescr, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackAddr = new JTextField();
        Font txCreateRackAddrFont = this.$$$getFont$$$(null, -1, 12, txCreateRackAddr.getFont());
        if (txCreateRackAddrFont != null) txCreateRackAddr.setFont(txCreateRackAddrFont);
        txCreateRackAddr.setText("Pribram");
        panel9.add(txCreateRackAddr, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label13 = new JLabel();
        Font label13Font = this.$$$getFont$$$(null, Font.BOLD, 12, label13.getFont());
        if (label13Font != null) label13.setFont(label13Font);
        label13.setText("IP Address::");
        panel9.add(label13, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackIpAddr = new JTextField();
        Font txCreateRackIpAddrFont = this.$$$getFont$$$(null, -1, 12, txCreateRackIpAddr.getFont());
        if (txCreateRackIpAddrFont != null) txCreateRackIpAddr.setFont(txCreateRackIpAddrFont);
        txCreateRackIpAddr.setText("192.168.1.222");
        panel9.add(txCreateRackIpAddr, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label14 = new JLabel();
        Font label14Font = this.$$$getFont$$$(null, Font.BOLD, 12, label14.getFont());
        if (label14Font != null) label14.setFont(label14Font);
        label14.setText("Quido Port:");
        panel9.add(label14, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackQuidoPort = new JTextField();
        Font txCreateRackQuidoPortFont = this.$$$getFont$$$(null, -1, 12, txCreateRackQuidoPort.getFont());
        if (txCreateRackQuidoPortFont != null) txCreateRackQuidoPort.setFont(txCreateRackQuidoPortFont);
        txCreateRackQuidoPort.setText("8085");
        panel9.add(txCreateRackQuidoPort, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label15 = new JLabel();
        Font label15Font = this.$$$getFont$$$(null, Font.BOLD, 16, label15.getFont());
        if (label15Font != null) label15.setFont(label15Font);
        label15.setHorizontalTextPosition(0);
        label15.setText("Network:");
        panel9.add(label15, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        Font label16Font = this.$$$getFont$$$(null, Font.BOLD, 16, label16.getFont());
        if (label16Font != null) label16.setFont(label16Font);
        label16.setHorizontalTextPosition(0);
        label16.setText("Relay:");
        panel9.add(label16, new GridConstraints(5, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label17 = new JLabel();
        Font label17Font = this.$$$getFont$$$(null, Font.BOLD, 12, label17.getFont());
        if (label17Font != null) label17.setFont(label17Font);
        label17.setText("Position:");
        panel9.add(label17, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        Font label18Font = this.$$$getFont$$$(null, Font.BOLD, 12, label18.getFont());
        if (label18Font != null) label18.setFont(label18Font);
        label18.setText("Name:");
        panel9.add(label18, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label19 = new JLabel();
        Font label19Font = this.$$$getFont$$$(null, Font.BOLD, 12, label19.getFont());
        if (label19Font != null) label19.setFont(label19Font);
        label19.setText("Type:");
        panel9.add(label19, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackRelayPosition = new JTextField();
        Font txCreateRackRelayPositionFont = this.$$$getFont$$$(null, -1, 12, txCreateRackRelayPosition.getFont());
        if (txCreateRackRelayPositionFont != null) txCreateRackRelayPosition.setFont(txCreateRackRelayPositionFont);
        txCreateRackRelayPosition.setText("2");
        panel9.add(txCreateRackRelayPosition, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackRelayName = new JTextField();
        Font txCreateRackRelayNameFont = this.$$$getFont$$$(null, -1, 12, txCreateRackRelayName.getFont());
        if (txCreateRackRelayNameFont != null) txCreateRackRelayName.setFont(txCreateRackRelayNameFont);
        txCreateRackRelayName.setText("Kl.15");
        panel9.add(txCreateRackRelayName, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRaclRelayType = new JTextField();
        Font txCreateRaclRelayTypeFont = this.$$$getFont$$$(null, -1, 12, txCreateRaclRelayType.getFont());
        if (txCreateRaclRelayTypeFont != null) txCreateRaclRelayType.setFont(txCreateRaclRelayTypeFont);
        txCreateRaclRelayType.setText("SWITCH");
        panel9.add(txCreateRaclRelayType, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label20 = new JLabel();
        Font label20Font = this.$$$getFont$$$(null, Font.BOLD, 16, label20.getFont());
        if (label20Font != null) label20.setFont(label20Font);
        label20.setHorizontalTextPosition(0);
        label20.setText("");
        panel9.add(label20, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label21 = new JLabel();
        Font label21Font = this.$$$getFont$$$(null, Font.BOLD, 16, label21.getFont());
        if (label21Font != null) label21.setFont(label21Font);
        label21.setHorizontalTextPosition(0);
        label21.setText("Display");
        panel9.add(label21, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label22 = new JLabel();
        Font label22Font = this.$$$getFont$$$(null, Font.BOLD, 12, label22.getFont());
        if (label22Font != null) label22.setFont(label22Font);
        label22.setText("Type:");
        panel9.add(label22, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbCreateRackDispType = new JComboBox();
        panel9.add(cbCreateRackDispType, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        final JLabel label23 = new JLabel();
        Font label23Font = this.$$$getFont$$$(null, Font.BOLD, 12, label23.getFont());
        if (label23Font != null) label23.setFont(label23Font);
        label23.setText("Width:");
        panel9.add(label23, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label24 = new JLabel();
        Font label24Font = this.$$$getFont$$$(null, Font.BOLD, 12, label24.getFont());
        if (label24Font != null) label24.setFont(label24Font);
        label24.setText("Height:");
        panel9.add(label24, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label25 = new JLabel();
        Font label25Font = this.$$$getFont$$$(null, Font.BOLD, 12, label25.getFont());
        if (label25Font != null) label25.setFont(label25Font);
        label25.setText("# of Devices:");
        panel9.add(label25, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackDispWidth = new JTextField();
        Font txCreateRackDispWidthFont = this.$$$getFont$$$(null, -1, 12, txCreateRackDispWidth.getFont());
        if (txCreateRackDispWidthFont != null) txCreateRackDispWidth.setFont(txCreateRackDispWidthFont);
        txCreateRackDispWidth.setText("1568");
        panel9.add(txCreateRackDispWidth, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackDispHeight = new JTextField();
        Font txCreateRackDispHeightFont = this.$$$getFont$$$(null, -1, 12, txCreateRackDispHeight.getFont());
        if (txCreateRackDispHeightFont != null) txCreateRackDispHeight.setFont(txCreateRackDispHeightFont);
        txCreateRackDispHeight.setText("704");
        panel9.add(txCreateRackDispHeight, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnCreatRack = new JButton();
        btnCreatRack.setText("CREATE NEW TESTRACK");
        panel9.add(btnCreatRack, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label26 = new JLabel();
        Font label26Font = this.$$$getFont$$$(null, -1, 10, label26.getFont());
        if (label26Font != null) label26.setFont(label26Font);
        label26.setText("Note: 1st display is always ABT, if at least 2 displays the 2nd one is FPK and if 3 displays the last one is HUD");
        panel9.add(label26, new GridConstraints(10, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackNoOfDevices = new JTextField();
        Font txCreateRackNoOfDevicesFont = this.$$$getFont$$$(null, -1, 12, txCreateRackNoOfDevices.getFont());
        if (txCreateRackNoOfDevicesFont != null) txCreateRackNoOfDevices.setFont(txCreateRackNoOfDevicesFont);
        txCreateRackNoOfDevices.setText("3");
        panel9.add(txCreateRackNoOfDevices, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Front End", panel10);
        tabbedPane4 = new JTabbedPane();
        panel10.add(tabbedPane4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane4.addTab("Front End Simulation", panel11);
        btnStartSimulation = new JButton();
        btnStartSimulation.setBackground(new Color(-515537));
        btnStartSimulation.setText("START Simulation");
        panel11.add(btnStartSimulation, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel11.add(spacer6, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lbSysTime = new JLabel();
        lbSysTime.setText("Label");
        panel11.add(lbSysTime, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkFrontendConnectRack = new JCheckBox();
        chkFrontendConnectRack.setText("Connect to rack ID:");
        panel11.add(chkFrontendConnectRack, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txFrontendRackId = new JTextField();
        Font txFrontendRackIdFont = this.$$$getFont$$$(null, -1, 12, txFrontendRackId.getFont());
        if (txFrontendRackIdFont != null) txFrontendRackId.setFont(txFrontendRackIdFont);
        txFrontendRackId.setText("1");
        panel11.add(txFrontendRackId, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane4 = new JScrollPane();
        panel11.add(scrollPane4, new GridConstraints(3, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblFrontendRacks = new JTable();
        scrollPane4.setViewportView(tblFrontendRacks);
        final JLabel label27 = new JLabel();
        Font label27Font = this.$$$getFont$$$(null, Font.BOLD, 12, label27.getFont());
        if (label27Font != null) label27.setFont(label27Font);
        label27.setText("Last Cyclic Response:");
        panel11.add(label27, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txFrontEndLastResponse = new JTextField();
        Font txFrontEndLastResponseFont = this.$$$getFont$$$(null, -1, 12, txFrontEndLastResponse.getFont());
        if (txFrontEndLastResponseFont != null) txFrontEndLastResponse.setFont(txFrontEndLastResponseFont);
        txFrontEndLastResponse.setText("");
        panel11.add(txFrontEndLastResponse, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(244, 26), null, 0, false));
        final JLabel label28 = new JLabel();
        Font label28Font = this.$$$getFont$$$(null, Font.BOLD, 12, label28.getFont());
        if (label28Font != null) label28.setFont(label28Font);
        label28.setText("Last Single Response:");
        panel11.add(label28, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txFrontEndLastSingleResponse = new JTextField();
        Font txFrontEndLastSingleResponseFont = this.$$$getFont$$$(null, -1, 12, txFrontEndLastSingleResponse.getFont());
        if (txFrontEndLastSingleResponseFont != null)
            txFrontEndLastSingleResponse.setFont(txFrontEndLastSingleResponseFont);
        txFrontEndLastSingleResponse.setText("");
        panel11.add(txFrontEndLastSingleResponse, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(244, 26), null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Signal Server", panel12);
        tabbedPane5 = new JTabbedPane();
        panel12.add(tabbedPane5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane5.addTab("Main", panel13);
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setOrientation(0);
        panel13.add(splitPane1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setLeftComponent(panel14);
        btnSignalReadAllDevices = new JButton();
        btnSignalReadAllDevices.setText("Read All Devices");
        panel14.add(btnSignalReadAllDevices, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel14.add(spacer7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane5 = new JScrollPane();
        panel14.add(scrollPane5, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblSignal = new JTable();
        scrollPane5.setViewportView(tblSignal);
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setRightComponent(panel15);
        btnClearSignalLog = new JButton();
        btnClearSignalLog.setText("Clear Log");
        panel15.add(btnClearSignalLog, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel15.add(spacer8, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane6 = new JScrollPane();
        panel15.add(scrollPane6, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbLogSignal = new JList();
        Font lbLogSignalFont = this.$$$getFont$$$("Courier New", -1, 12, lbLogSignal.getFont());
        if (lbLogSignalFont != null) lbLogSignal.setFont(lbLogSignalFont);
        scrollPane6.setViewportView(lbLogSignal);
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane5.addTab("Devices Management", panel16);
        final JLabel label29 = new JLabel();
        Font label29Font = this.$$$getFont$$$(null, Font.BOLD, 12, label29.getFont());
        if (label29Font != null) label29.setFont(label29Font);
        label29.setText("Device ID:");
        panel16.add(label29, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel16.add(spacer9, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel16.add(spacer10, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cbSignalDevices = new JComboBox();
        panel16.add(cbSignalDevices, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnSignaUpdateDevices = new JButton();
        btnSignaUpdateDevices.setText("Update  Devices");
        panel16.add(btnSignaUpdateDevices, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label30 = new JLabel();
        Font label30Font = this.$$$getFont$$$(null, -1, 36, label30.getFont());
        if (label30Font != null) label30.setFont(label30Font);
        label30.setText(" ");
        panel16.add(label30, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new GridLayoutManager(7, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel16.add(panel17, new GridConstraints(2, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel17.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "New Device", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel17.getFont()), null));
        final JLabel label31 = new JLabel();
        Font label31Font = this.$$$getFont$$$(null, Font.BOLD, 12, label31.getFont());
        if (label31Font != null) label31.setFont(label31Font);
        label31.setText("Device ID:");
        panel17.add(label31, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        panel17.add(spacer11, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txNewDeviceId = new JTextField();
        Font txNewDeviceIdFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceId.getFont());
        if (txNewDeviceIdFont != null) txNewDeviceId.setFont(txNewDeviceIdFont);
        txNewDeviceId.setText("ID");
        panel17.add(txNewDeviceId, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label32 = new JLabel();
        Font label32Font = this.$$$getFont$$$(null, Font.BOLD, 12, label32.getFont());
        if (label32Font != null) label32.setFont(label32Font);
        label32.setText("Name:");
        panel17.add(label32, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label33 = new JLabel();
        Font label33Font = this.$$$getFont$$$(null, Font.BOLD, 12, label33.getFont());
        if (label33Font != null) label33.setFont(label33Font);
        label33.setText("Description");
        panel17.add(label33, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txNewDeviceName = new JTextField();
        Font txNewDeviceNameFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceName.getFont());
        if (txNewDeviceNameFont != null) txNewDeviceName.setFont(txNewDeviceNameFont);
        txNewDeviceName.setText("Name");
        panel17.add(txNewDeviceName, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txNewDeviceDescr = new JTextField();
        Font txNewDeviceDescrFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceDescr.getFont());
        if (txNewDeviceDescrFont != null) txNewDeviceDescr.setFont(txNewDeviceDescrFont);
        txNewDeviceDescr.setText("Description");
        panel17.add(txNewDeviceDescr, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label34 = new JLabel();
        Font label34Font = this.$$$getFont$$$(null, Font.BOLD, 12, label34.getFont());
        if (label34Font != null) label34.setFont(label34Font);
        label34.setText("In Audio Port:");
        panel17.add(label34, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label35 = new JLabel();
        Font label35Font = this.$$$getFont$$$(null, Font.BOLD, 12, label35.getFont());
        if (label35Font != null) label35.setFont(label35Font);
        label35.setText("In Video Port:");
        panel17.add(label35, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txNewDeviceInAudio = new JTextField();
        Font txNewDeviceInAudioFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceInAudio.getFont());
        if (txNewDeviceInAudioFont != null) txNewDeviceInAudio.setFont(txNewDeviceInAudioFont);
        txNewDeviceInAudio.setText("7000");
        panel17.add(txNewDeviceInAudio, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txNewDeviceInVideo = new JTextField();
        Font txNewDeviceInVideoFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceInVideo.getFont());
        if (txNewDeviceInVideoFont != null) txNewDeviceInVideo.setFont(txNewDeviceInVideoFont);
        txNewDeviceInVideo.setText("7002");
        panel17.add(txNewDeviceInVideo, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label36 = new JLabel();
        Font label36Font = this.$$$getFont$$$(null, Font.BOLD, 12, label36.getFont());
        if (label36Font != null) label36.setFont(label36Font);
        label36.setText("HOST Audio OUT:");
        panel17.add(label36, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label37 = new JLabel();
        Font label37Font = this.$$$getFont$$$(null, Font.BOLD, 12, label37.getFont());
        if (label37Font != null) label37.setFont(label37Font);
        label37.setText("HOST Audio PORT:");
        panel17.add(label37, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txNewDeviceHostAudio = new JTextField();
        Font txNewDeviceHostAudioFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceHostAudio.getFont());
        if (txNewDeviceHostAudioFont != null) txNewDeviceHostAudio.setFont(txNewDeviceHostAudioFont);
        txNewDeviceHostAudio.setText("localhost");
        panel17.add(txNewDeviceHostAudio, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txNewDeviceOutAudioPort = new JTextField();
        Font txNewDeviceOutAudioPortFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceOutAudioPort.getFont());
        if (txNewDeviceOutAudioPortFont != null) txNewDeviceOutAudioPort.setFont(txNewDeviceOutAudioPortFont);
        txNewDeviceOutAudioPort.setText("7004");
        panel17.add(txNewDeviceOutAudioPort, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnCreateNewDevice = new JButton();
        Font btnCreateNewDeviceFont = this.$$$getFont$$$(null, Font.BOLD, 16, btnCreateNewDevice.getFont());
        if (btnCreateNewDeviceFont != null) btnCreateNewDevice.setFont(btnCreateNewDeviceFont);
        btnCreateNewDevice.setText("Create New Device");
        panel17.add(btnCreateNewDevice, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDeleteMmDevice = new JButton();
        btnDeleteMmDevice.setBackground(new Color(-6612697));
        btnDeleteMmDevice.setForeground(new Color(-792));
        btnDeleteMmDevice.setText("Delete Device");
        panel16.add(btnDeleteMmDevice, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel18 = new JPanel();
        panel18.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Log", panel18);
        final JScrollPane scrollPane7 = new JScrollPane();
        panel18.add(scrollPane7, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(0, 135), null, 0, false));
        lbUserLog = new JList();
        Font lbUserLogFont = this.$$$getFont$$$("Courier New", -1, -1, lbUserLog.getFont());
        if (lbUserLogFont != null) lbUserLog.setFont(lbUserLogFont);
        scrollPane7.setViewportView(lbUserLog);
        btnUserClearLog = new JButton();
        btnUserClearLog.setText("Clear Log");
        panel18.add(btnUserClearLog, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer12 = new Spacer();
        panel18.add(spacer12, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        txaUserFeedback = new JTextArea();
        Font txaUserFeedbackFont = this.$$$getFont$$$("JetBrains Mono", -1, -1, txaUserFeedback.getFont());
        if (txaUserFeedbackFont != null) txaUserFeedback.setFont(txaUserFeedbackFont);
        txaUserFeedback.setLineWrap(true);
        panel18.add(txaUserFeedback, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 155), null, 0, false));
        btnUserClearText = new JButton();
        btnUserClearText.setText("Clear Text");
        panel18.add(btnUserClearText, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel19 = new JPanel();
        panel19.setLayout(new GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Configuration", panel19);
        final JLabel label38 = new JLabel();
        Font label38Font = this.$$$getFont$$$(null, Font.BOLD, 12, label38.getFont());
        if (label38Font != null) label38.setFont(label38Font);
        label38.setText("BE IP Address");
        panel19.add(label38, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer13 = new Spacer();
        panel19.add(spacer13, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer14 = new Spacer();
        panel19.add(spacer14, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txBeIpAddress = new JTextField();
        Font txBeIpAddressFont = this.$$$getFont$$$(null, -1, 12, txBeIpAddress.getFont());
        if (txBeIpAddressFont != null) txBeIpAddress.setFont(txBeIpAddressFont);
        txBeIpAddress.setText("localhost");
        panel19.add(txBeIpAddress, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label39 = new JLabel();
        Font label39Font = this.$$$getFont$$$(null, Font.BOLD, 12, label39.getFont());
        if (label39Font != null) label39.setFont(label39Font);
        label39.setText("BE Port");
        panel19.add(label39, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txBePort = new JTextField();
        Font txBePortFont = this.$$$getFont$$$(null, -1, 12, txBePort.getFont());
        if (txBePortFont != null) txBePort.setFont(txBePortFont);
        txBePort.setText("8089");
        panel19.add(txBePort, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label40 = new JLabel();
        Font label40Font = this.$$$getFont$$$(null, Font.BOLD, 12, label40.getFont());
        if (label40Font != null) label40.setFont(label40Font);
        label40.setText("Signal Server Port");
        panel19.add(label40, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txConfigSignalServerPort = new JTextField();
        Font txConfigSignalServerPortFont = this.$$$getFont$$$(null, -1, 12, txConfigSignalServerPort.getFont());
        if (txConfigSignalServerPortFont != null) txConfigSignalServerPort.setFont(txConfigSignalServerPortFont);
        txConfigSignalServerPort.setText("8890");
        panel19.add(txConfigSignalServerPort, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
