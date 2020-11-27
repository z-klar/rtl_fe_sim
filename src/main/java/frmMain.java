import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
import tools.ToolFunctions;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.List;

public class frmMain extends JFrame {
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
    private JButton btnGetSysInfo;
    private JButton btnUpdateConfig;
    private JTabbedPane tabbedPane6;
    private JList lbSysInfo;
    private JComboBox cbFesimRacks;
    private JCheckBox chkVerboseLogging;
    private JButton btnTestrackChangeDispVersion;
    private JComboBox cbTestrackDisplayType;
    private JTextField txTestrackDisplayVersion;
    private JButton btnGetSignalServerInfo;
    private JTextField txStatusBar1;
    private JTextField txStatusBar2;
    private JTextField txStatusBar0;
    private JButton btnConfigAddUrl;
    private JComboBox cbConfigUrl;
    private JButton btnConfigDeleteUrl;
    private JButton btnTokensUpdate;
    private JTextArea txaTokensAccessToken;
    private JList lbTokensAccessToken;
    private JList lbTokensRefreshToken;
    private JTextArea txaTokensRefreshToken;

    static JFrame frame;

    private DefaultListModel<String> dlmUserLog = new DefaultListModel<>();
    private DefaultListModel<String> dlmRackData = new DefaultListModel<>();
    private DefaultListModel<String> dlmSignal = new DefaultListModel<>();
    private DefaultListModel<String> dlmSysInfo = new DefaultListModel<>();
    private DefaultListModel<String> dlmAccessToken = new DefaultListModel<>();
    private DefaultListModel<String> dlmRefreshToken = new DefaultListModel<>();

    private RestCallService restCall;
    private GlobalData globalData = new GlobalData();
    private JsonProcessing jsonProcessing;

    private timerListener Casovac = new timerListener();
    private Timer timer = new Timer(1000, Casovac);
    private boolean Running = false;
    private boolean ControllingTestrack = false;
    private int Counter1 = 0, Counter2 = 0, Counter3 = 0;
    private ToolFunctions tools;
    private ConfigurationData Config = new ConfigurationData();

    /**
     *
     */
    public frmMain() {
        lbUserLog.setModel(dlmUserLog);
        lbTestrackData.setModel(dlmRackData);
        lbLogSignal.setModel(dlmSignal);
        lbSysInfo.setModel(dlmSysInfo);
        lbTokensAccessToken.setModel(dlmAccessToken);
        lbTokensRefreshToken.setModel(dlmRefreshToken);

        dlmUserLog.addElement("Start log .....");

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
        btnUserMailResendVerifyEmail.addActionListener(e -> ResendVerifyEmail());
        btnHeartbeatUpdateRacks.addActionListener(e -> HeartbeatUpdateRacks());
        btnHeartbeatSendHb.addActionListener(e -> SendOneHeartBeat());
        btnStartSimulation.addActionListener(e -> SwitchRunning());
        btnCreatRack.addActionListener(e -> CreateNewRack());
        btnDeleteTestrack.addActionListener(e -> DeleteTestrack());
        btnClearTestrackData.addActionListener(e -> dlmRackData.clear());
        lbGetRackDetails.addActionListener(e -> tools.GetRackDetails(dlmRackData, cbHeartbeatRacks));
        btnAddDisplay.addActionListener(e -> RackAddDisplay());
        btnRemoveDisplay.addActionListener(e -> RackRemoveDisplay());
        btnClearSignalLog.addActionListener(e -> dlmSignal.clear());
        btnSignalReadAllDevices.addActionListener(e -> ReadAllMmDevices());
        btnSignaUpdateDevices.addActionListener(e -> UpdateDevices());
        btnCreateNewDevice.addActionListener(e -> CreateNewMmDevice());
        btnDeleteMmDevice.addActionListener(e -> DeleteMmDevice());
        btnSendRackHandOver.addActionListener(e -> HandOverRack());
        chkFrontendConnectRack.addActionListener(e -> SwitchRackControl());
        btnGetSysInfo.addActionListener(e -> tools.GetSystemInfo(dlmSysInfo));
        btnUpdateConfig.addActionListener(e -> UpdateConfiguration());
        btnTestrackChangeDispVersion.addActionListener(e -> ChangeDisplayVersion());
        cbHeartbeatRacks.addActionListener(e -> SelectedTestrackChanged());
        cbTestrackDisplayType.addActionListener(e -> TestrackDisplayTypeChanged());
        btnGetSignalServerInfo.addActionListener(e -> tools.GetSignalServerInfo(dlmSysInfo));

        txStatusBar1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                StatusBarMouseClicked();
            }
        });

        Config = LoadConfiguration(cbConfigUrl);
        restCall = new RestCallService(
                cbConfigUrl.getSelectedItem().toString(),
                Integer.parseInt(txBePort.getText()),
                dlmUserLog,
                Integer.parseInt(txConfigSignalServerPort.getText())
        );
        frame.setTitle("RTL FrontEnd Simulator: " + cbConfigUrl.getSelectedItem().toString());

        tools = new ToolFunctions(globalData, restCall,
                txStatusBar1, dlmUserLog);


        btnConfigAddUrl.addActionListener(e -> AddUrlToList());
        btnConfigDeleteUrl.addActionListener(e -> DeleteUrl());
        btnTokensUpdate.addActionListener(e -> UpdateTokens());

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Bye, bye !!!");
                tools.SaveConfiguration(Config);
            }
        }));
    }
    /**
     * @param cbUrls
     * @return
     */
    public ConfigurationData LoadConfiguration(JComboBox<String> cbUrls) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            BufferedReader vstup = new BufferedReader(new FileReader("RTL_FE_SIM.json"));
            ConfigurationData c = mapper.readValue(vstup, ConfigurationData.class);
            Collections.sort(c.getHostUrls());
            cbUrls.removeAllItems();
            for (String url : c.getHostUrls()) {
                cbUrls.addItem(url);
            }
            vstup.close();
            return(c);
        }
        catch (IOException e) {
            dlmUserLog.addElement("Error Reading Config File !");
            dlmUserLog.addElement(e.getLocalizedMessage());
            ConfigurationData c = new ConfigurationData();
            c.getHostUrls().add("localhost");
            cbUrls.removeAllItems();
            cbUrls.addItem("localhost");
            return(c);
        }
    }


    /**
     *
     */
    private void UpdateTokens() {
        if(globalData.token == null) {
            JOptionPane.showMessageDialog(null, "No Access Token available !");
            return;
        }
        String strAccess = globalData.token.getToken();
        txaTokensAccessToken.setText(strAccess);
        tools.ParseToken(strAccess, dlmAccessToken, txaTokensRefreshToken);

        strAccess = globalData.token.getRefreshToken();
        txaTokensRefreshToken.setText(strAccess);
        tools.ParseToken(strAccess, dlmRefreshToken, txaTokensRefreshToken);

    }
    /**
     *
     */
    private void DeleteUrl() {
        String url = cbConfigUrl.getSelectedItem().toString();
        int i = 0, j = -1;
        for (String u : Config.getHostUrls()) {
            if(u.equals(url)) {
                j = i;
                break;
            }
            i++;
        }
        if(j != -1) Config.getHostUrls().remove(j);
        cbConfigUrl.removeAllItems();
        for (String u : Config.getHostUrls()) {
            cbConfigUrl.addItem(u);
        }
    }
    /**
     *
     */
    private void AddUrlToList() {
        if(txBeIpAddress.getText().length() > 0) {
            Config.getHostUrls().add(txBeIpAddress.getText());
            Collections.sort(Config.getHostUrls());
            cbConfigUrl.removeAllItems();
            for (String u : Config.getHostUrls()) {
                cbConfigUrl.addItem(u);
            }
        }
    }
    /**
     *
     */
    private void UpdateConfiguration() {
        restCall.UpdateUrl(cbConfigUrl.getSelectedItem().toString(),
                Integer.parseInt(txBePort.getText()));
        frame.setTitle("RTL FrontEnd Simulator: " + cbConfigUrl.getSelectedItem().toString());
    }
    /**
     *
     */
    private void StatusBarMouseClicked() {
        tools.ClearErrMessages();
    }
    /**
     *
     */
    private void TestrackDisplayTypeChanged() {
        String rackName = cbHeartbeatRacks.getSelectedItem().toString();
        if(rackName.length() < 5) return;
        int rackId = Integer.parseInt(tools.parseRackId(rackName));
        int rackpos = tools.GetRackOrderById(rackId);
        if (rackpos >= 0) {
            TestrackDTO rack = globalData.testracks.get(rackpos);
            Iterator<TestrackDisplayDTO> dispList;
            dispList = rack.getTestrackDisplays().iterator();
            try {
                while (dispList.hasNext()) {
                    TestrackDisplayDTO disp = dispList.next();
                    if (cbTestrackDisplayType.getSelectedItem().toString().equals(disp.getType().toString())) {
                        txTestrackDisplayVersion.setText(disp.getVersion() == null ? "" : disp.getVersion());
                    }
                }
            }
            catch(Exception ex) {

            }
        }
    }

    /**
     *
     */
    private void SelectedTestrackChanged() {
        try {
            String rackName = cbHeartbeatRacks.getSelectedItem().toString();
            int rackId = Integer.parseInt(tools.parseRackId(rackName));
            int rackpos = tools.GetRackOrderById(rackId);
            if (rackpos >= 0) {
                cbTestrackDisplayType.removeAllItems();
                TestrackDTO rack = globalData.testracks.get(rackpos);
                int i = 0;
                Iterator<TestrackDisplayDTO> it = rack.getTestrackDisplays().iterator();
                while (it.hasNext()) {
                    TestrackDisplayDTO disp = it.next();
                    cbTestrackDisplayType.addItem(disp.getType().toString());
                }
            } else {
                dlmUserLog.addElement("GetRackDetails: Rack=" + rackName + "   -> Unknown POS !");
            }
        }
        catch(Exception ex) {

        }
    }
    /**
     *
     */
    private void ChangeDisplayVersion() {
        String rackName = cbHeartbeatRacks.getSelectedItem().toString();
        int rackId = Integer.parseInt(tools.parseRackId(rackName));
        int rackpos = tools.GetRackOrderById(rackId);
        if (rackpos >= 0) {
            TestrackDTO rack = globalData.testracks.get(rackpos);
            Iterator<TestrackDisplayDTO> dispList;
            dispList = rack.getTestrackDisplays().iterator();
            while (dispList.hasNext()) {
                TestrackDisplayDTO disp = dispList.next();
                if(cbTestrackDisplayType.getSelectedItem().toString().equals(disp.getType().toString())) {
                    disp.setVersion(txTestrackDisplayVersion.getText());
                    tools.UpdateRack(rack, dlmUserLog);
                    GetAllTestracks();
                }
            }
        }
    }

    /**
     *
     */
    private void HandOverRack() {
        String rackId = tools.parseRackId(cbHeartbeatRacks.getSelectedItem().toString());
        if (rackId.length() > 0) {
            RestCallOutput res = restCall.sendHandOverRack(rackId, globalData.token.getToken(), true);
            int iRes = res.getResultCode();
            if(iRes < 300) {
                String msg = "Response: " + iRes ;
                txRackHandOverResponse.setText(msg);
            }
            else {
                String msg = "Response: " + iRes + "  ERR: " + res.getErrorMsg() ;
                txRackHandOverResponse.setText(msg);
                JOptionPane.showMessageDialog(null, "ERROR: Result code: " + iRes);
            }
        }
    }
    /**
     *
     */
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
        String surl = "http://" + cbConfigUrl.getSelectedItem().toString() + ":" + txConfigSignalServerPort.getText()
                + "/device";
        RestCallOutput ro = restCall.SendRestApiRequest("DELETE", props, jsonString, surl);
        JOptionPane.showMessageDialog(null, "RESULT CODE: " + ro.getResultCode());
        if (ro.getResultCode() > 230) {
            dlmSignal.addElement("POST Err: " + ro.getErrorMsg());
        }

    }
    /**
     *
     */
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
        }
        catch (Exception ex) {
        }
        String surl = "http://" + cbConfigUrl.getSelectedItem().toString() + ":" + txConfigSignalServerPort.getText()
                + "/device";
        RestCallOutput ro = restCall.SendRestApiRequest("POST", props, jsonString, surl);
        JOptionPane.showMessageDialog(null, "RESULT CODE: " + ro.getResultCode());
        if (ro.getResultCode() > 230) {
            dlmSignal.addElement("POST Err: " + ro.getErrorMsg());
        }

    }
    /**
     *
     */
    private void UpdateDevices() {
        cbSignalDevices.removeAllItems();
        for (MmDevice dev : globalData.mmDevices) cbSignalDevices.addItem(dev.getId());
    }
    /**
     *
     */
    private void ReadAllMmDevices() {
        dlmSignal.addElement("Reading all devices from SIGNAL ");
        String surl = "http://" + cbConfigUrl.getSelectedItem().toString() + ":" + txConfigSignalServerPort.getText() + "/devices";
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
    /**
     *
     */
    private void RackAddDisplay() {
        String rackName = cbHeartbeatRacks.getSelectedItem().toString();
        int rackId = Integer.parseInt(tools.parseRackId(rackName));
        int rackpos = tools.GetRackOrderById(rackId);

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
                    tools.UpdateRack(rack, dlmUserLog);
                    break;
                case 1:
                    dlmRackData.addElement(" Removing FPK .....");
                    AddDisplay(rack, DisplayType.FPK);
                    tools.UpdateRack(rack, dlmUserLog);
                    break;
            }
        }
    }
    /**
     * @param rack
     * @param type
     */
    private void AddDisplay(TestrackDTO rack, DisplayType type) {
        TestrackDisplayDTO display0 = new TestrackDisplayDTO();

        display0.setType(type);
        display0.setWidth(1000);
        display0.setHeight(600);
        display0.setMgbport(0);
        rack.getTestrackDisplays().add(display0);
    }
    /**
     *
     */
    private void RackRemoveDisplay() {
        String rackName = cbHeartbeatRacks.getSelectedItem().toString();
        int rackId = Integer.parseInt(tools.parseRackId(rackName));
        int rackpos = tools.GetRackOrderById(rackId);

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
                    tools.RemoveFpk(rack);
                    tools.UpdateRack(rack, dlmUserLog);
                    break;
                case 3:
                    dlmRackData.addElement(" Removing HUD .....");
                    tools.RemoveHud(rack);
                    tools.UpdateRack(rack, dlmUserLog);
                    break;
            }
        }
    }
    private void DeleteTestrack() {
        String rackId = tools.parseRackId(cbHeartbeatRacks.getSelectedItem().toString());
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
                    Object sel = cbFesimRacks.getSelectedItem();
                    if(sel == null) {
                        chkFrontendConnectRack.setSelected(false);
                        JOptionPane.showMessageDialog(null, "No testrack selected !");
                        return;
                    }
                    String selection = cbFesimRacks.getSelectedItem().toString();
                    String sRackId = tools.parseRackId(selection);
                    RestCallOutput ro = restCall.sendHandOverRack(sRackId,
                                                    globalData.token.getToken(), false);
                    if(ro.getResultCode() < 300) {
                        txFrontEndLastSingleResponse.setText("SendHandOverRack:   RESULT=" + ro.getResultCode());
                        ControllingTestrack = true;
                    }
                    else {
                        txFrontEndLastSingleResponse.setText(
                                "ERR: RESULT=" + ro.getResultCode() + "  MSG=" + ro.getErrorMsg());
                        chkFrontendConnectRack.setSelected(false);
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
        String rackId = tools.parseRackId(cbHeartbeatRacks.getSelectedItem().toString());
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
                txRackHeartbeatResponse.setText(msg + "   ERR: " + res.getErrorMsg());
                JOptionPane.showMessageDialog(null, "ERROR: Result code: " + iRes);
            }
        }
    }

    private void HeartbeatUpdateRacks() {
        cbHeartbeatRacks.removeAllItems();
        cbFesimRacks.removeAllItems();
        for (int i = 0; i < globalData.testracks.size(); i++) {
            String spom = "ID:" + globalData.testracks.get(i).getId();
            spom += "   Rack:" + globalData.testracks.get(i).getName();
            cbHeartbeatRacks.addItem(spom);
            cbFesimRacks.addItem(spom);
        }
    }

    private void ResendVerifyEmail() {
        String UserId = GetUserIdByEmail(cbUserMailUsers.getSelectedItem().toString());
        if (UserId.length() > 0) {
            RestCallOutput ro = restCall.sendVerifyEmail(UserId, globalData.token.getToken());
            int iRes = ro.getResultCode();
            JOptionPane.showMessageDialog(null, "Result code: " + iRes);
            if(iRes > 299) {
                dlmUserLog.addElement(ro.getErrorMsg());
            }
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
        getAllUsers();
        cbUserMailUsers.removeAllItems();
        for (int i = 0; i < globalData.users.size(); i++) {
            cbUserMailUsers.addItem(globalData.users.get(i).getEmail());
        }
    }

    /**
     *
     */
    private void GetAllTestracks() {
        if(globalData.token == null) {
            tools.SetErrMsgInStatusBar("GetAllTestracks: NO LOGIN token !");
            return;
        }
        RestCallOutput res = restCall.readAllTestracks(globalData.token.getToken(), true);
        if(res.getResultCode() > 299) {
            dlmUserLog.addElement("Get all testracks:  Error=" + res.getResultCode());
            dlmUserLog.addElement(res.getErrorMsg());
            return;
        }
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
            HeartbeatUpdateRacks();
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
            cbUserMailUsers.removeAllItems();
            for (int i = 0; i < users.size(); i++) {
                UserDto user = users.get(i);
                result += user.getId() + "  |  " + user.getUsername()
                        + "  |  " + user.getFirstName() + "  |  " + user.getLastName()
                        + "  |  " + user.getEmail() + "  |  " + user.getRole() + "\n";
                rows.add(new UserTable1(user.getId(), user.getUsername(), user.getFirstName(),
                        user.getLastName(), user.getEmail()));
                cbUserMailUsers.addItem(user.getEmail());
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
            dlmUserLog.addElement("getLoginToken:   RES=" + res.getResultCode());
            dlmUserLog.addElement("ERR: " + res.getErrorMsg());
        }
    }

    private void TimerEvent() {
        try {
            if (Running) {
                lbSysTime.setText(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toLocalTime().toString());
                Counter1++;
                if (Counter1 >= 3600) {  // EACH 3600 seconds
                    Counter1 = 0;
                    RestCallOutput res = restCall.getLoginToken(txUserUserName.getText(), txUserPassword.getText(), false);
                    globalData.token = (AccessTokenDto) res.getOutputData();
                }
                //---------  EACH SECOND  -----------
                if (globalData.token == null) {
                    tools.SetErrMsgInStatusBar("GetAllTestracks: NO LOGIN token !");
                    return;
                }
                RestCallOutput res = restCall.readAllTestracks(globalData.token.getToken(), false);
                globalData.testracks = (List<TestrackDTO>) res.getOutputData();
                UpdateRackTable();
                if (chkFrontendConnectRack.isSelected()) {
                    RestCallOutput ro = restCall.sendHeartbeat(
                            tools.parseRackId(cbFesimRacks.getSelectedItem().toString()),
                            globalData.token.getToken(), false);
                    txFrontEndLastResponse.setText("Send HB:  RESULT=" + ro.getResultCode());
                }
            } else {
                lbSysTime.setText("Stopped ....");
            }
        }
        catch(Exception ex) {
            dlmUserLog.addElement(ex.getMessage());
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
        frame = new JFrame("RTL FrontEnd Simulator");

        URL imgURL = frmMain.class.getResource("Avatar.png");
        Image img = new ImageIcon(imgURL).getImage();
        frame.setIconImage(img);

        frame.setContentPane(new frmMain().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(1100, 700));
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
        panelMain.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        panelMain.add(tabbedPane1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        tabbedPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("User Functions", panel1);
        tabbedPane2 = new JTabbedPane();
        panel1.add(tabbedPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2.addTab("Main Functions", panel2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 2, new Insets(5, 5, 5, 5), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Login   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel3.getFont()), new Color(-14672672)));
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
        btnUserLogin = new JButton();
        btnUserLogin.setText("Login");
        panel3.add(btnUserLogin, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(244, 25), null, 0, false));
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
        txUserPassword = new JPasswordField();
        txUserPassword.setText("Rtljefajn2020");
        panel3.add(txUserPassword, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(6, 1, new Insets(5, 5, 5, 5), -1, -1));
        panel2.add(panel4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(386, 131), null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Resend Verification Email", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel4.getFont()), new Color(-14672672)));
        cbUserMailUsers = new JComboBox();
        panel4.add(cbUserMailUsers, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnUserMailResendVerifyEmail = new JButton();
        btnUserMailResendVerifyEmail.setText("Resend Verification Email");
        panel4.add(btnUserMailResendVerifyEmail, new GridConstraints(3, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, 12, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setHorizontalAlignment(4);
        label4.setText("User:");
        panel4.add(label4, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 2, new Insets(5, 5, 5, 5), -1, -1));
        panel2.add(panel5, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Existing User List   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel5.getFont()), new Color(-14672672)));
        btnGetAllUsers = new JButton();
        btnGetAllUsers.setText("Get All Users");
        panel5.add(btnGetAllUsers, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 25), null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel5.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel5.add(scrollPane1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblUser = new JTable();
        scrollPane1.setViewportView(tblUser);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(4, 2, new Insets(5, 5, 5, 5), -1, -1));
        panel2.add(panel6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(386, 131), null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Create New User    ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel6.getFont()), new Color(-14672672)));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, 12, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setHorizontalAlignment(4);
        label5.setText("First  Name:");
        panel6.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, Font.BOLD, 12, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Last  Name:");
        panel6.add(label6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null, Font.BOLD, 12, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setHorizontalAlignment(4);
        label7.setText("Email:");
        panel6.add(label7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCreateUser = new JButton();
        btnCreateUser.setText("Create New User");
        panel6.add(btnCreateUser, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateUserLastName = new JTextField();
        Font txCreateUserLastNameFont = this.$$$getFont$$$(null, -1, 12, txCreateUserLastName.getFont());
        if (txCreateUserLastNameFont != null) txCreateUserLastName.setFont(txCreateUserLastNameFont);
        txCreateUserLastName.setText("");
        panel6.add(txCreateUserLastName, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateUserFirstName = new JTextField();
        Font txCreateUserFirstNameFont = this.$$$getFont$$$(null, -1, 12, txCreateUserFirstName.getFont());
        if (txCreateUserFirstNameFont != null) txCreateUserFirstName.setFont(txCreateUserFirstNameFont);
        txCreateUserFirstName.setText("");
        panel6.add(txCreateUserFirstName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateUserEmail = new JTextField();
        Font txCreateUserEmailFont = this.$$$getFont$$$(null, -1, 12, txCreateUserEmail.getFont());
        if (txCreateUserEmailFont != null) txCreateUserEmail.setFont(txCreateUserEmailFont);
        txCreateUserEmail.setText("");
        panel6.add(txCreateUserEmail, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2.addTab("Tokens", panel7);
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(247);
        splitPane1.setDividerSize(8);
        splitPane1.setOrientation(0);
        panel7.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        splitPane1.setLeftComponent(panel8);
        panel8.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "  Access Token  ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel8.getFont()), new Color(-14672672)));
        final JSplitPane splitPane2 = new JSplitPane();
        splitPane2.setDividerSize(8);
        panel8.add(splitPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane2.setLeftComponent(panel9);
        btnTokensUpdate = new JButton();
        btnTokensUpdate.setText("Update");
        panel9.add(btnTokensUpdate, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel9.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel9.add(scrollPane2, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txaTokensAccessToken = new JTextArea();
        Font txaTokensAccessTokenFont = this.$$$getFont$$$("Courier New", Font.PLAIN, -1, txaTokensAccessToken.getFont());
        if (txaTokensAccessTokenFont != null) txaTokensAccessToken.setFont(txaTokensAccessTokenFont);
        txaTokensAccessToken.setLineWrap(true);
        scrollPane2.setViewportView(txaTokensAccessToken);
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane2.setRightComponent(panel10);
        final JScrollPane scrollPane3 = new JScrollPane();
        panel10.add(scrollPane3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbTokensAccessToken = new JList();
        Font lbTokensAccessTokenFont = this.$$$getFont$$$("Courier New", Font.PLAIN, -1, lbTokensAccessToken.getFont());
        if (lbTokensAccessTokenFont != null) lbTokensAccessToken.setFont(lbTokensAccessTokenFont);
        scrollPane3.setViewportView(lbTokensAccessToken);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        splitPane1.setRightComponent(panel11);
        panel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "  Refresh Token  ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel11.getFont()), new Color(-14672672)));
        final JSplitPane splitPane3 = new JSplitPane();
        splitPane3.setDividerSize(8);
        panel11.add(splitPane3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane3.setLeftComponent(panel12);
        final JScrollPane scrollPane4 = new JScrollPane();
        panel12.add(scrollPane4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txaTokensRefreshToken = new JTextArea();
        Font txaTokensRefreshTokenFont = this.$$$getFont$$$("Courier New", Font.PLAIN, 12, txaTokensRefreshToken.getFont());
        if (txaTokensRefreshTokenFont != null) txaTokensRefreshToken.setFont(txaTokensRefreshTokenFont);
        txaTokensRefreshToken.setLineWrap(true);
        scrollPane4.setViewportView(txaTokensRefreshToken);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane3.setRightComponent(panel13);
        final JScrollPane scrollPane5 = new JScrollPane();
        panel13.add(scrollPane5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbTokensRefreshToken = new JList();
        Font lbTokensRefreshTokenFont = this.$$$getFont$$$("Courier New", Font.PLAIN, -1, lbTokensRefreshToken.getFont());
        if (lbTokensRefreshTokenFont != null) lbTokensRefreshToken.setFont(lbTokensRefreshTokenFont);
        scrollPane5.setViewportView(lbTokensRefreshToken);
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Testrack Functions", panel14);
        tabbedPane3 = new JTabbedPane();
        panel14.add(tabbedPane3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        tabbedPane3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new GridLayoutManager(2, 2, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane3.addTab("Testrack List", panel15);
        panel15.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane6 = new JScrollPane();
        panel15.add(scrollPane6, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblTestracks = new JTable();
        scrollPane6.setViewportView(tblTestracks);
        btnGetAllTestracks = new JButton();
        btnGetAllTestracks.setText("Get All Testracks");
        panel15.add(btnGetAllTestracks, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel15.add(spacer4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new GridLayoutManager(5, 7, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane3.addTab("Testrack Services", panel16);
        panel16.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        btnHeartbeatUpdateRacks = new JButton();
        btnHeartbeatUpdateRacks.setText("Update  Testracks");
        panel16.add(btnHeartbeatUpdateRacks, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$(null, Font.BOLD, 12, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setText("Testrack:");
        panel16.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbHeartbeatRacks = new JComboBox();
        cbHeartbeatRacks.setEditable(false);
        panel16.add(cbHeartbeatRacks, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnHeartbeatSendHb = new JButton();
        btnHeartbeatSendHb.setText("Send HeartBeat Tick");
        panel16.add(btnHeartbeatSendHb, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDeleteTestrack = new JButton();
        btnDeleteTestrack.setBackground(new Color(-6612697));
        btnDeleteTestrack.setForeground(new Color(-792));
        btnDeleteTestrack.setText("Delete Testrack");
        panel16.add(btnDeleteTestrack, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnClearTestrackData = new JButton();
        btnClearTestrackData.setText("Clear Data");
        panel16.add(btnClearTestrackData, new GridConstraints(3, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lbGetRackDetails = new JButton();
        lbGetRackDetails.setText("Get Testrack Details");
        panel16.add(lbGetRackDetails, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel16.add(spacer5, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnAddDisplay = new JButton();
        btnAddDisplay.setText("Add Display");
        panel16.add(btnAddDisplay, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRemoveDisplay = new JButton();
        btnRemoveDisplay.setText("Remove Display");
        panel16.add(btnRemoveDisplay, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txRackHeartbeatResponse = new JTextField();
        panel16.add(txRackHeartbeatResponse, new GridConstraints(1, 2, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnSendRackHandOver = new JButton();
        btnSendRackHandOver.setText("Hand Over Selected Rack");
        panel16.add(btnSendRackHandOver, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txRackHandOverResponse = new JTextField();
        panel16.add(txRackHandOverResponse, new GridConstraints(2, 2, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JSplitPane splitPane4 = new JSplitPane();
        splitPane4.setDividerSize(6);
        panel16.add(splitPane4, new GridConstraints(4, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane4.setLeftComponent(panel17);
        final JScrollPane scrollPane7 = new JScrollPane();
        panel17.add(scrollPane7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbTestrackData = new JList();
        Font lbTestrackDataFont = this.$$$getFont$$$("Courier New", Font.PLAIN, 12, lbTestrackData.getFont());
        if (lbTestrackDataFont != null) lbTestrackData.setFont(lbTestrackDataFont);
        scrollPane7.setViewportView(lbTestrackData);
        final JPanel panel18 = new JPanel();
        panel18.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane4.setRightComponent(panel18);
        final JPanel panel19 = new JPanel();
        panel19.setLayout(new GridLayoutManager(4, 3, new Insets(5, 5, 5, 5), -1, -1));
        panel18.add(panel19, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel19.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Display Version Change", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel19.getFont()), new Color(-14672672)));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$(null, Font.BOLD, 12, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setText("Display:");
        panel19.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel19.add(spacer6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel19.add(spacer7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cbTestrackDisplayType = new JComboBox();
        panel19.add(cbTestrackDisplayType, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null, Font.BOLD, 12, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setText("Version:");
        panel19.add(label10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txTestrackDisplayVersion = new JTextField();
        panel19.add(txTestrackDisplayVersion, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnTestrackChangeDispVersion = new JButton();
        btnTestrackChangeDispVersion.setText("Change Display Version");
        panel19.add(btnTestrackChangeDispVersion, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel20 = new JPanel();
        panel20.setLayout(new GridLayoutManager(13, 4, new Insets(5, 5, 5, 5), -1, -1));
        Font panel20Font = this.$$$getFont$$$(null, Font.BOLD, 16, panel20.getFont());
        if (panel20Font != null) panel20.setFont(panel20Font);
        tabbedPane3.addTab("Create Rack", panel20);
        panel20.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null, Font.BOLD, 12, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setText("Name:");
        panel20.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel20.add(spacer8, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$(null, Font.BOLD, 12, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setText("Description:");
        panel20.add(label12, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        Font label13Font = this.$$$getFont$$$(null, Font.BOLD, 12, label13.getFont());
        if (label13Font != null) label13.setFont(label13Font);
        label13.setText("Address:");
        panel20.add(label13, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        Font label14Font = this.$$$getFont$$$(null, Font.BOLD, 12, label14.getFont());
        if (label14Font != null) label14.setFont(label14Font);
        label14.setText("Platform:");
        panel20.add(label14, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbCreateRackPlatform = new JComboBox();
        panel20.add(cbCreateRackPlatform, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        txCreateRackName = new JTextField();
        Font txCreateRackNameFont = this.$$$getFont$$$(null, -1, 12, txCreateRackName.getFont());
        if (txCreateRackNameFont != null) txCreateRackName.setFont(txCreateRackNameFont);
        txCreateRackName.setText("Testrack #11");
        panel20.add(txCreateRackName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackDescr = new JTextField();
        Font txCreateRackDescrFont = this.$$$getFont$$$(null, -1, 12, txCreateRackDescr.getFont());
        if (txCreateRackDescrFont != null) txCreateRackDescr.setFont(txCreateRackDescrFont);
        txCreateRackDescr.setText("Demo rack");
        panel20.add(txCreateRackDescr, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackAddr = new JTextField();
        Font txCreateRackAddrFont = this.$$$getFont$$$(null, -1, 12, txCreateRackAddr.getFont());
        if (txCreateRackAddrFont != null) txCreateRackAddr.setFont(txCreateRackAddrFont);
        txCreateRackAddr.setText("Pribram");
        panel20.add(txCreateRackAddr, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label15 = new JLabel();
        Font label15Font = this.$$$getFont$$$(null, Font.BOLD, 12, label15.getFont());
        if (label15Font != null) label15.setFont(label15Font);
        label15.setText("IP Address::");
        panel20.add(label15, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackIpAddr = new JTextField();
        Font txCreateRackIpAddrFont = this.$$$getFont$$$(null, -1, 12, txCreateRackIpAddr.getFont());
        if (txCreateRackIpAddrFont != null) txCreateRackIpAddr.setFont(txCreateRackIpAddrFont);
        txCreateRackIpAddr.setText("192.168.1.222");
        panel20.add(txCreateRackIpAddr, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label16 = new JLabel();
        Font label16Font = this.$$$getFont$$$(null, Font.BOLD, 12, label16.getFont());
        if (label16Font != null) label16.setFont(label16Font);
        label16.setText("Quido Port:");
        panel20.add(label16, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackQuidoPort = new JTextField();
        Font txCreateRackQuidoPortFont = this.$$$getFont$$$(null, -1, 12, txCreateRackQuidoPort.getFont());
        if (txCreateRackQuidoPortFont != null) txCreateRackQuidoPort.setFont(txCreateRackQuidoPortFont);
        txCreateRackQuidoPort.setText("8085");
        panel20.add(txCreateRackQuidoPort, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label17 = new JLabel();
        Font label17Font = this.$$$getFont$$$(null, Font.BOLD, 16, label17.getFont());
        if (label17Font != null) label17.setFont(label17Font);
        label17.setHorizontalTextPosition(0);
        label17.setText("Network:");
        panel20.add(label17, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        Font label18Font = this.$$$getFont$$$(null, Font.BOLD, 16, label18.getFont());
        if (label18Font != null) label18.setFont(label18Font);
        label18.setHorizontalTextPosition(0);
        label18.setText("Relay:");
        panel20.add(label18, new GridConstraints(5, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label19 = new JLabel();
        Font label19Font = this.$$$getFont$$$(null, Font.BOLD, 12, label19.getFont());
        if (label19Font != null) label19.setFont(label19Font);
        label19.setText("Position:");
        panel20.add(label19, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        Font label20Font = this.$$$getFont$$$(null, Font.BOLD, 12, label20.getFont());
        if (label20Font != null) label20.setFont(label20Font);
        label20.setText("Name:");
        panel20.add(label20, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label21 = new JLabel();
        Font label21Font = this.$$$getFont$$$(null, Font.BOLD, 12, label21.getFont());
        if (label21Font != null) label21.setFont(label21Font);
        label21.setText("Type:");
        panel20.add(label21, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackRelayPosition = new JTextField();
        Font txCreateRackRelayPositionFont = this.$$$getFont$$$(null, -1, 12, txCreateRackRelayPosition.getFont());
        if (txCreateRackRelayPositionFont != null) txCreateRackRelayPosition.setFont(txCreateRackRelayPositionFont);
        txCreateRackRelayPosition.setText("2");
        panel20.add(txCreateRackRelayPosition, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackRelayName = new JTextField();
        Font txCreateRackRelayNameFont = this.$$$getFont$$$(null, -1, 12, txCreateRackRelayName.getFont());
        if (txCreateRackRelayNameFont != null) txCreateRackRelayName.setFont(txCreateRackRelayNameFont);
        txCreateRackRelayName.setText("Kl.15");
        panel20.add(txCreateRackRelayName, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRaclRelayType = new JTextField();
        Font txCreateRaclRelayTypeFont = this.$$$getFont$$$(null, -1, 12, txCreateRaclRelayType.getFont());
        if (txCreateRaclRelayTypeFont != null) txCreateRaclRelayType.setFont(txCreateRaclRelayTypeFont);
        txCreateRaclRelayType.setText("SWITCH");
        panel20.add(txCreateRaclRelayType, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label22 = new JLabel();
        Font label22Font = this.$$$getFont$$$(null, Font.BOLD, 16, label22.getFont());
        if (label22Font != null) label22.setFont(label22Font);
        label22.setHorizontalTextPosition(0);
        label22.setText("");
        panel20.add(label22, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label23 = new JLabel();
        Font label23Font = this.$$$getFont$$$(null, Font.BOLD, 16, label23.getFont());
        if (label23Font != null) label23.setFont(label23Font);
        label23.setHorizontalTextPosition(0);
        label23.setText("Display");
        panel20.add(label23, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label24 = new JLabel();
        Font label24Font = this.$$$getFont$$$(null, Font.BOLD, 12, label24.getFont());
        if (label24Font != null) label24.setFont(label24Font);
        label24.setText("Type:");
        panel20.add(label24, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbCreateRackDispType = new JComboBox();
        panel20.add(cbCreateRackDispType, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        final JLabel label25 = new JLabel();
        Font label25Font = this.$$$getFont$$$(null, Font.BOLD, 12, label25.getFont());
        if (label25Font != null) label25.setFont(label25Font);
        label25.setText("Width:");
        panel20.add(label25, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label26 = new JLabel();
        Font label26Font = this.$$$getFont$$$(null, Font.BOLD, 12, label26.getFont());
        if (label26Font != null) label26.setFont(label26Font);
        label26.setText("Height:");
        panel20.add(label26, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label27 = new JLabel();
        Font label27Font = this.$$$getFont$$$(null, Font.BOLD, 12, label27.getFont());
        if (label27Font != null) label27.setFont(label27Font);
        label27.setText("# of Devices:");
        panel20.add(label27, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackDispWidth = new JTextField();
        Font txCreateRackDispWidthFont = this.$$$getFont$$$(null, -1, 12, txCreateRackDispWidth.getFont());
        if (txCreateRackDispWidthFont != null) txCreateRackDispWidth.setFont(txCreateRackDispWidthFont);
        txCreateRackDispWidth.setText("1568");
        panel20.add(txCreateRackDispWidth, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackDispHeight = new JTextField();
        Font txCreateRackDispHeightFont = this.$$$getFont$$$(null, -1, 12, txCreateRackDispHeight.getFont());
        if (txCreateRackDispHeightFont != null) txCreateRackDispHeight.setFont(txCreateRackDispHeightFont);
        txCreateRackDispHeight.setText("704");
        panel20.add(txCreateRackDispHeight, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnCreatRack = new JButton();
        btnCreatRack.setText("CREATE NEW TESTRACK");
        panel20.add(btnCreatRack, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label28 = new JLabel();
        Font label28Font = this.$$$getFont$$$(null, -1, 10, label28.getFont());
        if (label28Font != null) label28.setFont(label28Font);
        label28.setText("Note: 1st display is always ABT, if at least 2 displays the 2nd one is FPK and if 3 displays the last one is HUD");
        panel20.add(label28, new GridConstraints(10, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackNoOfDevices = new JTextField();
        Font txCreateRackNoOfDevicesFont = this.$$$getFont$$$(null, -1, 12, txCreateRackNoOfDevices.getFont());
        if (txCreateRackNoOfDevicesFont != null) txCreateRackNoOfDevices.setFont(txCreateRackNoOfDevicesFont);
        txCreateRackNoOfDevices.setText("3");
        panel20.add(txCreateRackNoOfDevices, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel21 = new JPanel();
        panel21.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Front End", panel21);
        tabbedPane4 = new JTabbedPane();
        panel21.add(tabbedPane4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel22 = new JPanel();
        panel22.setLayout(new GridLayoutManager(5, 4, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane4.addTab("Front End Simulation", panel22);
        panel22.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        btnStartSimulation = new JButton();
        btnStartSimulation.setBackground(new Color(-515537));
        btnStartSimulation.setText("START Simulation");
        panel22.add(btnStartSimulation, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel22.add(spacer9, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lbSysTime = new JLabel();
        lbSysTime.setText("Label");
        panel22.add(lbSysTime, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkFrontendConnectRack = new JCheckBox();
        chkFrontendConnectRack.setText("Connect to rack ID:");
        panel22.add(chkFrontendConnectRack, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane8 = new JScrollPane();
        panel22.add(scrollPane8, new GridConstraints(3, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblFrontendRacks = new JTable();
        scrollPane8.setViewportView(tblFrontendRacks);
        final JLabel label29 = new JLabel();
        Font label29Font = this.$$$getFont$$$(null, Font.BOLD, 12, label29.getFont());
        if (label29Font != null) label29.setFont(label29Font);
        label29.setText("Last Cyclic Response:");
        panel22.add(label29, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txFrontEndLastResponse = new JTextField();
        Font txFrontEndLastResponseFont = this.$$$getFont$$$(null, -1, 12, txFrontEndLastResponse.getFont());
        if (txFrontEndLastResponseFont != null) txFrontEndLastResponse.setFont(txFrontEndLastResponseFont);
        txFrontEndLastResponse.setText("");
        panel22.add(txFrontEndLastResponse, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(244, 26), null, 0, false));
        final JLabel label30 = new JLabel();
        Font label30Font = this.$$$getFont$$$(null, Font.BOLD, 12, label30.getFont());
        if (label30Font != null) label30.setFont(label30Font);
        label30.setText("Last Single Response:");
        panel22.add(label30, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txFrontEndLastSingleResponse = new JTextField();
        Font txFrontEndLastSingleResponseFont = this.$$$getFont$$$(null, -1, 12, txFrontEndLastSingleResponse.getFont());
        if (txFrontEndLastSingleResponseFont != null)
            txFrontEndLastSingleResponse.setFont(txFrontEndLastSingleResponseFont);
        txFrontEndLastSingleResponse.setText("");
        panel22.add(txFrontEndLastSingleResponse, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(244, 26), null, 0, false));
        cbFesimRacks = new JComboBox();
        panel22.add(cbFesimRacks, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel23 = new JPanel();
        panel23.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Signal Server", panel23);
        tabbedPane5 = new JTabbedPane();
        panel23.add(tabbedPane5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel24 = new JPanel();
        panel24.setLayout(new GridLayoutManager(1, 2, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane5.addTab("Main", panel24);
        panel24.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JSplitPane splitPane5 = new JSplitPane();
        splitPane5.setDividerLocation(365);
        splitPane5.setOrientation(0);
        panel24.add(splitPane5, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel25 = new JPanel();
        panel25.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane5.setLeftComponent(panel25);
        btnSignalReadAllDevices = new JButton();
        btnSignalReadAllDevices.setText("Read All Devices");
        panel25.add(btnSignalReadAllDevices, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel25.add(spacer10, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane9 = new JScrollPane();
        panel25.add(scrollPane9, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblSignal = new JTable();
        scrollPane9.setViewportView(tblSignal);
        final JPanel panel26 = new JPanel();
        panel26.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane5.setRightComponent(panel26);
        btnClearSignalLog = new JButton();
        btnClearSignalLog.setText("Clear Log");
        panel26.add(btnClearSignalLog, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        panel26.add(spacer11, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane10 = new JScrollPane();
        panel26.add(scrollPane10, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbLogSignal = new JList();
        Font lbLogSignalFont = this.$$$getFont$$$("Courier New", Font.PLAIN, 12, lbLogSignal.getFont());
        if (lbLogSignalFont != null) lbLogSignal.setFont(lbLogSignalFont);
        scrollPane10.setViewportView(lbLogSignal);
        final JPanel panel27 = new JPanel();
        panel27.setLayout(new GridLayoutManager(3, 5, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane5.addTab("Devices Management", panel27);
        panel27.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label31 = new JLabel();
        Font label31Font = this.$$$getFont$$$(null, Font.BOLD, 12, label31.getFont());
        if (label31Font != null) label31.setFont(label31Font);
        label31.setText("Device ID:");
        panel27.add(label31, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer12 = new Spacer();
        panel27.add(spacer12, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        cbSignalDevices = new JComboBox();
        panel27.add(cbSignalDevices, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnSignaUpdateDevices = new JButton();
        btnSignaUpdateDevices.setText("Update  Devices");
        panel27.add(btnSignaUpdateDevices, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel28 = new JPanel();
        panel28.setLayout(new GridLayoutManager(7, 4, new Insets(5, 5, 5, 5), -1, -1));
        panel27.add(panel28, new GridConstraints(2, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel28.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   New Device   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel28.getFont()), new Color(-14672672)));
        final JLabel label32 = new JLabel();
        Font label32Font = this.$$$getFont$$$(null, Font.BOLD, 12, label32.getFont());
        if (label32Font != null) label32.setFont(label32Font);
        label32.setText("Device ID:");
        panel28.add(label32, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer13 = new Spacer();
        panel28.add(spacer13, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txNewDeviceId = new JTextField();
        Font txNewDeviceIdFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceId.getFont());
        if (txNewDeviceIdFont != null) txNewDeviceId.setFont(txNewDeviceIdFont);
        txNewDeviceId.setText("ID");
        panel28.add(txNewDeviceId, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label33 = new JLabel();
        Font label33Font = this.$$$getFont$$$(null, Font.BOLD, 12, label33.getFont());
        if (label33Font != null) label33.setFont(label33Font);
        label33.setText("Name:");
        panel28.add(label33, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label34 = new JLabel();
        Font label34Font = this.$$$getFont$$$(null, Font.BOLD, 12, label34.getFont());
        if (label34Font != null) label34.setFont(label34Font);
        label34.setText("Description");
        panel28.add(label34, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txNewDeviceName = new JTextField();
        Font txNewDeviceNameFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceName.getFont());
        if (txNewDeviceNameFont != null) txNewDeviceName.setFont(txNewDeviceNameFont);
        txNewDeviceName.setText("Name");
        panel28.add(txNewDeviceName, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txNewDeviceDescr = new JTextField();
        Font txNewDeviceDescrFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceDescr.getFont());
        if (txNewDeviceDescrFont != null) txNewDeviceDescr.setFont(txNewDeviceDescrFont);
        txNewDeviceDescr.setText("Description");
        panel28.add(txNewDeviceDescr, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label35 = new JLabel();
        Font label35Font = this.$$$getFont$$$(null, Font.BOLD, 12, label35.getFont());
        if (label35Font != null) label35.setFont(label35Font);
        label35.setText("In Audio Port:");
        panel28.add(label35, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label36 = new JLabel();
        Font label36Font = this.$$$getFont$$$(null, Font.BOLD, 12, label36.getFont());
        if (label36Font != null) label36.setFont(label36Font);
        label36.setText("In Video Port:");
        panel28.add(label36, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txNewDeviceInAudio = new JTextField();
        Font txNewDeviceInAudioFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceInAudio.getFont());
        if (txNewDeviceInAudioFont != null) txNewDeviceInAudio.setFont(txNewDeviceInAudioFont);
        txNewDeviceInAudio.setText("7000");
        panel28.add(txNewDeviceInAudio, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txNewDeviceInVideo = new JTextField();
        Font txNewDeviceInVideoFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceInVideo.getFont());
        if (txNewDeviceInVideoFont != null) txNewDeviceInVideo.setFont(txNewDeviceInVideoFont);
        txNewDeviceInVideo.setText("7002");
        panel28.add(txNewDeviceInVideo, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label37 = new JLabel();
        Font label37Font = this.$$$getFont$$$(null, Font.BOLD, 12, label37.getFont());
        if (label37Font != null) label37.setFont(label37Font);
        label37.setText("HOST Audio OUT:");
        panel28.add(label37, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label38 = new JLabel();
        Font label38Font = this.$$$getFont$$$(null, Font.BOLD, 12, label38.getFont());
        if (label38Font != null) label38.setFont(label38Font);
        label38.setText("HOST Audio PORT:");
        panel28.add(label38, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txNewDeviceHostAudio = new JTextField();
        Font txNewDeviceHostAudioFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceHostAudio.getFont());
        if (txNewDeviceHostAudioFont != null) txNewDeviceHostAudio.setFont(txNewDeviceHostAudioFont);
        txNewDeviceHostAudio.setText("localhost");
        panel28.add(txNewDeviceHostAudio, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txNewDeviceOutAudioPort = new JTextField();
        Font txNewDeviceOutAudioPortFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceOutAudioPort.getFont());
        if (txNewDeviceOutAudioPortFont != null) txNewDeviceOutAudioPort.setFont(txNewDeviceOutAudioPortFont);
        txNewDeviceOutAudioPort.setText("7004");
        panel28.add(txNewDeviceOutAudioPort, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnCreateNewDevice = new JButton();
        Font btnCreateNewDeviceFont = this.$$$getFont$$$(null, Font.BOLD, 16, btnCreateNewDevice.getFont());
        if (btnCreateNewDeviceFont != null) btnCreateNewDevice.setFont(btnCreateNewDeviceFont);
        btnCreateNewDevice.setText("Create New Device");
        panel28.add(btnCreateNewDevice, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDeleteMmDevice = new JButton();
        btnDeleteMmDevice.setBackground(new Color(-6612697));
        btnDeleteMmDevice.setForeground(new Color(-792));
        btnDeleteMmDevice.setText("Delete Device");
        panel27.add(btnDeleteMmDevice, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel29 = new JPanel();
        panel29.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Log", panel29);
        final JSplitPane splitPane6 = new JSplitPane();
        splitPane6.setDividerSize(8);
        splitPane6.setOrientation(0);
        panel29.add(splitPane6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel30 = new JPanel();
        panel30.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane6.setLeftComponent(panel30);
        btnUserClearLog = new JButton();
        btnUserClearLog.setText("Clear Log");
        panel30.add(btnUserClearLog, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane11 = new JScrollPane();
        panel30.add(scrollPane11, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(0, 135), null, 0, false));
        lbUserLog = new JList();
        Font lbUserLogFont = this.$$$getFont$$$("Courier New", Font.PLAIN, -1, lbUserLog.getFont());
        if (lbUserLogFont != null) lbUserLog.setFont(lbUserLogFont);
        scrollPane11.setViewportView(lbUserLog);
        final Spacer spacer14 = new Spacer();
        panel30.add(spacer14, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel31 = new JPanel();
        panel31.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane6.setRightComponent(panel31);
        btnUserClearText = new JButton();
        btnUserClearText.setText("Clear Text");
        panel31.add(btnUserClearText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer15 = new Spacer();
        panel31.add(spacer15, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane12 = new JScrollPane();
        panel31.add(scrollPane12, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane12.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txaUserFeedback = new JTextArea();
        Font txaUserFeedbackFont = this.$$$getFont$$$("Courier New", Font.PLAIN, -1, txaUserFeedback.getFont());
        if (txaUserFeedbackFont != null) txaUserFeedback.setFont(txaUserFeedbackFont);
        txaUserFeedback.setLineWrap(true);
        scrollPane12.setViewportView(txaUserFeedback);
        final JPanel panel32 = new JPanel();
        panel32.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane1.addTab("Configuration", panel32);
        panel32.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel33 = new JPanel();
        panel33.setLayout(new GridLayoutManager(4, 9, new Insets(0, 0, 0, 0), -1, -1));
        panel32.add(panel33, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel33.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Configuration / Connection Parameters   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 11, panel33.getFont()), new Color(-14672672)));
        final JLabel label39 = new JLabel();
        Font label39Font = this.$$$getFont$$$(null, Font.BOLD, 12, label39.getFont());
        if (label39Font != null) label39.setFont(label39Font);
        label39.setText("BE IP Address");
        panel33.add(label39, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label40 = new JLabel();
        Font label40Font = this.$$$getFont$$$(null, Font.BOLD, 12, label40.getFont());
        if (label40Font != null) label40.setFont(label40Font);
        label40.setText("BE Port");
        panel33.add(label40, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txBePort = new JTextField();
        Font txBePortFont = this.$$$getFont$$$(null, -1, 12, txBePort.getFont());
        if (txBePortFont != null) txBePort.setFont(txBePortFont);
        txBePort.setText("8089");
        panel33.add(txBePort, new GridConstraints(0, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer16 = new Spacer();
        panel33.add(spacer16, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label41 = new JLabel();
        Font label41Font = this.$$$getFont$$$(null, Font.BOLD, 12, label41.getFont());
        if (label41Font != null) label41.setFont(label41Font);
        label41.setText("Signal Server Port");
        panel33.add(label41, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txConfigSignalServerPort = new JTextField();
        Font txConfigSignalServerPortFont = this.$$$getFont$$$(null, -1, 12, txConfigSignalServerPort.getFont());
        if (txConfigSignalServerPortFont != null) txConfigSignalServerPort.setFont(txConfigSignalServerPortFont);
        txConfigSignalServerPort.setText("8890");
        panel33.add(txConfigSignalServerPort, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label42 = new JLabel();
        Font label42Font = this.$$$getFont$$$(null, Font.BOLD, 10, label42.getFont());
        if (label42Font != null) label42.setFont(label42Font);
        label42.setText("  ");
        panel33.add(label42, new GridConstraints(3, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label43 = new JLabel();
        Font label43Font = this.$$$getFont$$$(null, -1, 6, label43.getFont());
        if (label43Font != null) label43.setFont(label43Font);
        label43.setText("      ");
        panel33.add(label43, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkVerboseLogging = new JCheckBox();
        chkVerboseLogging.setText("Verbose Logging");
        panel33.add(chkVerboseLogging, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbConfigUrl = new JComboBox();
        Font cbConfigUrlFont = this.$$$getFont$$$(null, -1, 12, cbConfigUrl.getFont());
        if (cbConfigUrlFont != null) cbConfigUrl.setFont(cbConfigUrlFont);
        panel33.add(cbConfigUrl, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txBeIpAddress = new JTextField();
        Font txBeIpAddressFont = this.$$$getFont$$$(null, -1, 12, txBeIpAddress.getFont());
        if (txBeIpAddressFont != null) txBeIpAddress.setFont(txBeIpAddressFont);
        txBeIpAddress.setText("");
        panel33.add(txBeIpAddress, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnConfigAddUrl = new JButton();
        btnConfigAddUrl.setText("Add URL:");
        panel33.add(btnConfigAddUrl, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnConfigDeleteUrl = new JButton();
        btnConfigDeleteUrl.setText("Delete URL");
        panel33.add(btnConfigDeleteUrl, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnUpdateConfig = new JButton();
        btnUpdateConfig.setMargin(new Insets(5, 5, 5, 5));
        btnUpdateConfig.setText("Update Configuration");
        panel33.add(btnUpdateConfig, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel34 = new JPanel();
        panel34.setLayout(new GridLayoutManager(2, 5, new Insets(5, 5, 5, 5), -1, -1));
        panel32.add(panel34, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel34.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   SW Version   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 11, panel34.getFont()), new Color(-14672672)));
        final JLabel label44 = new JLabel();
        Font label44Font = this.$$$getFont$$$(null, Font.BOLD, 12, label44.getFont());
        if (label44Font != null) label44.setFont(label44Font);
        label44.setHorizontalAlignment(4);
        label44.setText("SW Version: ");
        panel34.add(label44, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JTextField textField1 = new JTextField();
        textField1.setEditable(false);
        Font textField1Font = this.$$$getFont$$$(null, -1, 12, textField1.getFont());
        if (textField1Font != null) textField1.setFont(textField1Font);
        textField1.setHorizontalAlignment(2);
        textField1.setText("1.0.4.3");
        panel34.add(textField1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label45 = new JLabel();
        Font label45Font = this.$$$getFont$$$(null, Font.BOLD, 12, label45.getFont());
        if (label45Font != null) label45.setFont(label45Font);
        label45.setHorizontalAlignment(4);
        label45.setHorizontalTextPosition(4);
        label45.setText("Build Date:");
        panel34.add(label45, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JTextField textField2 = new JTextField();
        textField2.setEditable(false);
        Font textField2Font = this.$$$getFont$$$(null, -1, 12, textField2.getFont());
        if (textField2Font != null) textField2.setFont(textField2Font);
        textField2.setText("2020-11-27");
        panel34.add(textField2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer17 = new Spacer();
        panel34.add(spacer17, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label46 = new JLabel();
        Font label46Font = this.$$$getFont$$$(null, Font.BOLD, 8, label46.getFont());
        if (label46Font != null) label46.setFont(label46Font);
        label46.setHorizontalAlignment(4);
        label46.setText("    ");
        panel34.add(label46, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer18 = new Spacer();
        panel32.add(spacer18, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel35 = new JPanel();
        panel35.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("System", panel35);
        tabbedPane6 = new JTabbedPane();
        panel35.add(tabbedPane6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel36 = new JPanel();
        panel36.setLayout(new GridLayoutManager(2, 3, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane6.addTab("System Info", panel36);
        panel36.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        btnGetSysInfo = new JButton();
        btnGetSysInfo.setText("Get BE Info");
        panel36.add(btnGetSysInfo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer19 = new Spacer();
        panel36.add(spacer19, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane13 = new JScrollPane();
        panel36.add(scrollPane13, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbSysInfo = new JList();
        Font lbSysInfoFont = this.$$$getFont$$$("Courier New", Font.PLAIN, 14, lbSysInfo.getFont());
        if (lbSysInfoFont != null) lbSysInfo.setFont(lbSysInfoFont);
        scrollPane13.setViewportView(lbSysInfo);
        btnGetSignalServerInfo = new JButton();
        btnGetSignalServerInfo.setText("Get Signal Server Info");
        panel36.add(btnGetSignalServerInfo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel37 = new JPanel();
        panel37.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel37, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel38 = new JPanel();
        panel38.setLayout(new GridLayoutManager(1, 1, new Insets(3, 3, 3, 3), -1, -1));
        panel37.add(panel38, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel38.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txStatusBar1 = new JTextField();
        txStatusBar1.setBackground(new Color(-11282636));
        txStatusBar1.setEditable(false);
        Font txStatusBar1Font = this.$$$getFont$$$(null, -1, 11, txStatusBar1.getFont());
        if (txStatusBar1Font != null) txStatusBar1.setFont(txStatusBar1Font);
        txStatusBar1.setText("[0] Ready");
        panel38.add(txStatusBar1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel39 = new JPanel();
        panel39.setLayout(new GridLayoutManager(1, 1, new Insets(3, 3, 3, 3), -1, -1));
        panel37.add(panel39, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel39.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txStatusBar0 = new JTextField();
        txStatusBar0.setBackground(new Color(-2368028));
        txStatusBar0.setEditable(false);
        txStatusBar0.setEnabled(true);
        Font txStatusBar0Font = this.$$$getFont$$$(null, -1, 11, txStatusBar0.getFont());
        if (txStatusBar0Font != null) txStatusBar0.setFont(txStatusBar0Font);
        txStatusBar0.setText("RTL FrontEnd Simulator");
        panel39.add(txStatusBar0, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel40 = new JPanel();
        panel40.setLayout(new GridLayoutManager(1, 1, new Insets(3, 3, 3, 3), -1, -1));
        panel37.add(panel40, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel40.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txStatusBar2 = new JTextField();
        txStatusBar2.setBackground(new Color(-4012853));
        txStatusBar2.setEditable(false);
        Font txStatusBar2Font = this.$$$getFont$$$(null, -1, 11, txStatusBar2.getFont());
        if (txStatusBar2Font != null) txStatusBar2.setFont(txStatusBar2Font);
        panel40.add(txStatusBar2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer20 = new Spacer();
        panelMain.add(spacer20, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
