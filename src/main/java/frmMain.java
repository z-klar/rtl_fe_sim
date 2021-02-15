import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import common.GlobalData;
import common.JsonProcessing;
import common.RestCallOutput;
import commonEnum.DisplayType;
import commonEnum.LabInvitationState;
import commonEnum.RtlRoles;
import commonEnum.TestrackVehicle;
import dto.*;
import dto.groups.LabDetailDTO;
import dto.janus.*;
import model.CommonOutput;
import model.LoggerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.JanusService;
import service.LabService;
import service.RestCallService;
import tables.*;
import tables.labs.LabTableModel;
import tables.labs.LabTableRow;
import tools.JanusTools;
import tools.LabTools;
import tools.ToolFunctions;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class frmMain extends JFrame implements ActionListener {
    private final Logger log = LoggerFactory.getLogger(JsonProcessing.class);

    // region UI elements
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
    private JTable tblRealys;
    private JButton btnCreatRackAddRelay;
    private JButton btnCreateRackRemoveRelay;
    private JButton btnUserProfileGetProfile;
    private JTextArea txaUserProfileUserProfile;
    private JTextField txCreateRackVin;
    private JTabbedPane tabbedPane7;
    private JComboBox cbButtonsAbt;
    private JButton btnButtonsSendAbt;
    private JComboBox cbButtonsFpk;
    private JButton btnButtonsSendFpk;
    private JComboBox cbButtonsFpk2;
    private JButton btnButtonsSendFpk2;
    private JTextField txButtonsCoordX;
    private JTextField txButtonsCoordY;
    private JButton btnButtonsSendTouch;
    private JComboBox cbButtonsRacks;
    private JButton btnButtonsUpdateRacks;
    private JButton btnButtonsSendFpkSeq;
    private JTabbedPane tabbedPane8;
    private JButton btnClearJanusLog;
    private JList lbJanusRawLog;
    private JTextField txJanusBaseUrl;
    private JPasswordField txJanusAdminPwd;
    private JButton btnJanusGetSessions;
    private JList lbJanusMainLog;
    private JButton btnJanusClearMainLog;
    private JCheckBox chkJanusRemoveMainLog;
    private JButton btnJanusSaveLog;
    private JButton btnActuatorGetLogs;
    private JList lbActuator;
    private JButton btnActuatorClearLog;
    private JTable tblData1;
    private JTextField txJanusTestsHandleInfoFileName;
    private JButton btnJanusTestsBrowseHandleInfoFile;
    private JButton btnJanusTestsParseHandleInfoFile;
    private JButton btnJanusMainGetSession;
    private JList lbJanusMainSessions;
    private JList lbJanusMainHandles;
    private JButton btnJanusMainGesHandles;
    private JList lbJanusMainHandleInfo;
    private JButton btnJanusMainGetHandleInfo;
    private JButton btnGetJanusOverview;
    private JTable tblJanusOverview;
    private JTable tblJanusMainStream;
    private JTable tblJanusMainComponent;
    private JTable tblLabsMain;
    private JTable tblLabUsers;
    private JTable tblLabRack;
    private JButton btnLabAddUser;
    private JButton btnLabRemoveUser;
    private JButton btnLabAddRack;
    private JButton btnLabRemoveRack;
    private JButton btnLabUpdateLab;
    private JTabbedPane tabbedPane10;
    private JButton btnLabClearLog;
    private JList lbLabsLog;
    private JTextField txLabNewRackId;
    private JComboBox cbLabNewUser;
    private JComboBox cbLabNewRole;
    private JComboBox cbLabNewState;
    private JButton btnLabUpdateDependencies;
    private JList lbLabAdmins;
    private JButton btnLabRemoveLab;
    private JTextField txLabNewLabName;
    private JButton btnLabCreateNewLab;
    private JButton btnLabModifyUser;

    static JFrame frame;

    //endregion
    private final DefaultListModel<String> dlmUserLog = new DefaultListModel<>();
    private final DefaultListModel<String> dlmRackData = new DefaultListModel<>();
    private final DefaultListModel<String> dlmSignal = new DefaultListModel<>();
    private final DefaultListModel<String> dlmSysInfo = new DefaultListModel<>();
    private final DefaultListModel<String> dlmAccessToken = new DefaultListModel<>();
    private final DefaultListModel<String> dlmRefreshToken = new DefaultListModel<>();
    private final DefaultListModel<String> dlmJanusRawLog = new DefaultListModel<>();
    private final DefaultListModel<String> dlmJanusMainLog = new DefaultListModel<>();
    private final DefaultListModel<String> dlmActuator = new DefaultListModel<>();
    private final DefaultListModel<String> dlmJanusMainSessions = new DefaultListModel<>();
    private final DefaultListModel<String> dlmJanusMainHandles = new DefaultListModel<>();
    private final DefaultListModel<String> dlmJanusMainHandleInfo = new DefaultListModel<>();
    private final DefaultListModel<String> dlmLabsLog = new DefaultListModel<>();
    private final DefaultListModel<String> dlmLabAdmins = new DefaultListModel<>();

    private final RestCallService restCall;
    private final GlobalData globalData = new GlobalData();
    private final JsonProcessing jsonProcessing;
    private final JanusService janusService;
    private final LabService labService;
    private final LabTools labTools;

    private final timerListener Casovac = new timerListener();
    private final Timer timer = new Timer(1000, Casovac);
    private boolean Running = false;
    private boolean ControllingTestrack = false;
    private int Counter1 = 0;
    private ToolFunctions tools;
    private ConfigurationData Config = new ConfigurationData();
    private int TableMouseClickRow;

    JPopupMenu popLoggers = new JPopupMenu("pop_loggers");
    private String[] popLoggersLabels = {"Set to OFF", "Set to ERROR", "Set to WARN",
            "Set to INFO", "Set to DEBUG", "Set to TRACE"};
    private String[] popLoggersCommands = {"OFF", "ERROR", "WARN", "INFO", "DEBUG", "TRACE"};
    JPopupMenu popMain = new JPopupMenu("pop_main");


    private ArrayList<RelayDefinitionDTO> relays = new ArrayList<>();
    /**
     *
     */
    public frmMain() {
        //------ init popup menu for Spring Actuator ---------
        for(String label : popLoggersLabels) {
            JMenuItem mnuPpLogOFF = new JMenuItem(label);
            mnuPpLogOFF.addActionListener((ActionListener) this);
            popLoggers.add(mnuPpLogOFF);
        }

        lbUserLog.setModel(dlmUserLog);
        lbTestrackData.setModel(dlmRackData);
        lbLogSignal.setModel(dlmSignal);
        lbSysInfo.setModel(dlmSysInfo);
        lbTokensAccessToken.setModel(dlmAccessToken);
        lbTokensRefreshToken.setModel(dlmRefreshToken);
        lbJanusRawLog.setModel(dlmJanusRawLog);
        lbJanusMainLog.setModel(dlmJanusMainLog);
        lbActuator.setModel(dlmActuator);
        lbJanusMainSessions.setModel(dlmJanusMainSessions);
        lbJanusMainHandles.setModel(dlmJanusMainHandles);
        lbJanusMainHandleInfo.setModel(dlmJanusMainHandleInfo);
        lbLabsLog.setModel(dlmLabsLog);
        lbLabAdmins.setModel(dlmLabAdmins);

        cbCreateRackPlatform.removeAllItems();
        for (TestrackVehicle v : TestrackVehicle.values())
            cbCreateRackPlatform.addItem(v);
        cbCreateRackDispType.removeAllItems();
        for (DisplayType p : DisplayType.values())
            cbCreateRackDispType.addItem(p);

        timer.start();

        Config = LoadConfiguration(cbConfigUrl);
        restCall = new RestCallService(
                cbConfigUrl.getSelectedItem().toString(),
                Integer.parseInt(txBePort.getText()),
                dlmUserLog,
                Integer.parseInt(txConfigSignalServerPort.getText())
        );
        jsonProcessing = new JsonProcessing(dlmSignal);
        tools = new ToolFunctions(globalData, restCall,
                txStatusBar1, dlmUserLog);
        janusService = new JanusService(dlmJanusRawLog, globalData, dlmJanusRawLog);
        labService = new LabService(dlmLabsLog, globalData);
        labTools = new LabTools(tblLabsMain, tblLabUsers, tblLabRack, dlmLabsLog,
                                globalData, labService, restCall, cbLabNewUser,
                                cbLabNewRole, cbLabNewState, dlmLabAdmins);
        // region UI elements init
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
        btnConfigAddUrl.addActionListener(e -> AddUrlToList());
        btnConfigDeleteUrl.addActionListener(e -> DeleteUrl());
        btnTokensUpdate.addActionListener(e -> UpdateTokens());
        btnCreatRackAddRelay.addActionListener(e -> AddRelay());
        btnCreateRackRemoveRelay.addActionListener(e -> RemoveRelay());
        btnUserProfileGetProfile.addActionListener(e -> GetUserProfile());
        btnButtonsSendAbt.addActionListener(e -> SendAbtButton());
        btnButtonsUpdateRacks.addActionListener(e -> HeartbeatUpdateRacks());
        btnButtonsSendFpk.addActionListener(e -> SendFpkButton(1));
        btnButtonsSendFpk2.addActionListener(e -> SendFpkButton(2));
        btnButtonsSendFpkSeq.addActionListener(e -> SendFpkSequence());
        btnButtonsSendTouch.addActionListener(e -> SendTouchCommand());
        tblRealys.getParent().setSize(500,200);
        tools.InitButtonCommand(cbButtonsAbt, cbButtonsFpk, cbButtonsFpk2);
        btnClearJanusLog.addActionListener(e -> dlmJanusRawLog.clear());
        btnJanusClearMainLog.addActionListener(e -> dlmJanusMainLog.clear());
        btnJanusGetSessions.addActionListener(e ->  JanusGetSessions());
        btnJanusSaveLog.addActionListener(e -> SaveJanusLog());
        btnActuatorClearLog.addActionListener(e -> dlmActuator.clear());
        btnActuatorGetLogs.addActionListener(e -> GetLoggers());
        btnJanusTestsBrowseHandleInfoFile.addActionListener(e -> GetJanusTestHandleInfoFile());
        btnJanusTestsParseHandleInfoFile.addActionListener(e -> JanusTestParseHandleInfoFile());
        btnJanusMainGetSession.addActionListener(e -> GetJanusSessions());
        btnJanusMainGesHandles.addActionListener(e -> GetJanusHandles());
        btnJanusMainGetHandleInfo.addActionListener(e -> GetHandleInfo());
        btnGetJanusOverview.addActionListener(e ->  GetJanusOverview());
        btnLabClearLog.addActionListener(e -> dlmLabsLog.clear());
        btnLabUpdateLab.addActionListener(e -> GetAllLabs());
        btnLabAddRack.addActionListener(e ->  labTools.AddTrackToLab(txLabNewRackId.getText()));
        btnLabRemoveRack.addActionListener(e -> labTools.RemoveRackFromLab());
        btnLabUpdateDependencies.addActionListener(e -> labTools.UpdateDependencies());
        btnLabAddUser.addActionListener(e -> labTools.AddUserToLab());
        btnLabRemoveUser.addActionListener(e -> labTools.RemoveUserFromLab());
        btnLabRemoveLab.addActionListener(e ->  labTools.RemoveLab());
        btnLabCreateNewLab.addActionListener(e -> labTools.createLab(txLabNewLabName.getText()));
        btnLabModifyUser.addActionListener(e -> labTools.ModifyUserInLab());
//endregion
        txStatusBar1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                StatusBarMouseClicked();
            }
        });

        //=============  runtime hook to be executed when shutting down ============
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Bye, bye !!!");
            tools.SaveConfiguration(Config);
        }));

        frame.setTitle("RTL FrontEnd Simulator: " + cbConfigUrl.getSelectedItem().toString());

        tabbedPane3.addChangeListener(e -> {
            if(tabbedPane3.getSelectedIndex() == 2) {
                Vector <RelayTableRow> rows = new Vector<>();
                RelayTableModel model = new RelayTableModel(rows);
                tblRealys.setModel(model);
            }
        });
        tblData1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int button = e.getButton();
                if (button == MouseEvent.BUTTON3) {
                    int selRow = tblData1.rowAtPoint(e.getPoint());
                    ProcessMouseClick(selRow, e.getX(), e.getY());
                }
            }
        });

        tblJanusOverview.getSelectionModel().addListSelectionListener(new RowListener());
        tblLabsMain.getSelectionModel().addListSelectionListener(new LabRowListener());

        globalData.setBeIP(cbConfigUrl.getSelectedItem().toString());
        globalData.setBePort(txBePort.getText());

        cbLabNewRole.addItem("rtl_user");
        cbLabNewRole.addItem("rtl_admin");
        Arrays.asList(LabInvitationState.values()).forEach(state -> cbLabNewState.addItem(state));

    }

    /**
     * *************************************************************************************
     */
    private class LabRowListener implements  ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            int row = tblLabsMain.getSelectedRow();
            if (row >= 0) labTools.UpdateUserAndRacks(row);
        }
    }
    /**
     *
     */
    private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            String text = "";
            int streamId, componentId;
            if (event.getValueIsAdjusting()) {
                return;
            }
            int row = tblJanusOverview.getSelectedRow();
            if(row >= 0) {
                GetJanusOverview();
                String session = "", handle = "";
                String cell = tblJanusOverview.getValueAt(row, 5).toString();  // compId
                if(cell.length() > 0) {
                    componentId = Integer.parseInt(cell);
                    cell = tblJanusOverview.getValueAt(row, 3).toString();  // streamId
                    if(cell.length() > 0) {
                        streamId = Integer.parseInt(cell);
                        int ii = row;
                        while(tblJanusOverview.getValueAt(ii, 1).toString().length() == 0) ii--;
                        handle = tblJanusOverview.getValueAt(ii, 1).toString();
                        while(tblJanusOverview.getValueAt(ii, 0).toString().length() == 0) ii--;
                        session = tblJanusOverview.getValueAt(ii, 0).toString();
                        Vector<JanusCommonTable> stream = new Vector<>();
                        Vector<JanusCommonTable> component = new Vector<>();
                        String url = "http://" + cbConfigUrl.getSelectedItem().toString() + ":7088/admin";
                        String pwd = String.valueOf(txJanusAdminPwd.getPassword());
                        janusService.GetComponentDetails(session, handle, streamId, componentId,stream,
                                                         component, url, pwd);
                        JanusCommonTableModel streamModel = new JanusCommonTableModel(stream);
                        tblJanusMainStream.setModel(streamModel);
                        JanusCommonTableModel compModel = new JanusCommonTableModel(component);
                        tblJanusMainComponent.setModel(compModel);
                    }
                }
            }
        }
    }
    /**
     * ********************************************************************************************
     * *******************************************************************************************
     */

    /**
     *
     */
    public void GetAllLabs() {
        if(globalData.token == null) {
            JOptionPane.showMessageDialog(null, "No logged user !");
            return;
        }
        String url = "http://" + cbConfigUrl.getSelectedItem().toString() + ":"
                + Integer.parseInt(txBePort.getText()) ;
        RestCallOutput ro = labService.getAllLabs(url);
        if(ro.getResultCode() > 299) {
            dlmLabsLog.addElement(ro.getErrorMsg());
        }
        else {
            List<LabDetailDTO> labs = (List<LabDetailDTO>)ro.getOutputData();
            globalData.setLabs(labs);
            Vector<LabTableRow> rows = new Vector<>();
            for(LabDetailDTO lab : labs) rows.add(lab.ConvertToTableRow());
            LabTableModel model = new LabTableModel(rows);
            tblLabsMain.setModel(model);
        }
    }
    /**
     *
     */
    private void GetJanusOverview() {

        String url = "http://" + cbConfigUrl.getSelectedItem().toString() + ":7088/admin";
        String pwd = String.valueOf(txJanusAdminPwd.getPassword());
        Vector<JanusOverviewTable> rows = new Vector<>();
        int res = janusService.getJanusOverviewData(rows, url, pwd);
        if(res < 0) {
            JOptionPane.showMessageDialog(null, "Error !!!");
            return;
        }
        JanusOverviewTableModel model = new JanusOverviewTableModel(rows);
        tblJanusOverview.setModel(model);
        tblJanusOverview.getColumnModel().getColumn(0).setPreferredWidth(65);
        tblJanusOverview.getColumnModel().getColumn(1).setPreferredWidth(65);
        tblJanusOverview.getColumnModel().getColumn(2).setPreferredWidth(195);
        tblJanusOverview.getColumnModel().getColumn(3).setPreferredWidth(15);
        tblJanusOverview.getColumnModel().getColumn(4).setPreferredWidth(95);
        tblJanusOverview.getColumnModel().getColumn(5).setPreferredWidth(15);

        Vector<JanusCommonTable> stream = new Vector<>();
        Vector<JanusCommonTable> component = new Vector<>();
        JanusCommonTableModel streamModel = new JanusCommonTableModel(stream);
        tblJanusMainStream.setModel(streamModel);
        JanusCommonTableModel compModel = new JanusCommonTableModel(component);
        tblJanusMainComponent.setModel(compModel);
    }
    /**
     *
     */
    private void GetHandleInfo() {
        int selectedHandle = lbJanusMainHandles.getSelectedIndex();
        if(selectedHandle < 0) {
            JOptionPane.showMessageDialog(null, "No handle selected !!");
            return;
        }
        String url = "http://" + cbConfigUrl.getSelectedItem().toString() + ":7088/admin";
        String pwd = String.valueOf(txJanusAdminPwd.getPassword());
        int selectedSession = lbJanusMainSessions.getSelectedIndex();
        String session = dlmJanusMainSessions.get(selectedSession).toString();
        String handle = dlmJanusMainHandles.get(selectedHandle).toString();
        dlmJanusMainHandleInfo.clear();
        JanusHandlesInfoResponseDTO handleObj = janusService.getHandleInfo(url, pwd, session, handle);
        if(handleObj.getJanus().contains("error")) {
            JOptionPane.showMessageDialog(null, "Error: " + handleObj.getTransaction());
        }
        else {
            JanusHandleInfoDTO hi = handleObj.getInfo();
            dlmJanusMainHandleInfo.addElement(String.format("- %-20s:%s", "transport", hi.getSession_transport()));
            dlmJanusMainHandleInfo.addElement(String.format("- %-20s:%s", "plugin", hi.getPlugin()));
            dlmJanusMainHandleInfo.addElement(String.format("- %-20s:%s", "ice-mode", hi.getIce_mode()));
            dlmJanusMainHandleInfo.addElement(String.format("- %-20s:%s", "ice-role", hi.getIce_role()));
            dlmJanusMainHandleInfo.addElement(String.format("- %-20s:%d", "queued-packets", hi.getQueued_packets()));
            dlmJanusMainHandleInfo.addElement(String.format("- %-20s:%d", "# Streams", hi.getStreams().length));
        }
    }
    /**
     *
     */
    private void GetJanusHandles() {
        int selectedSession = lbJanusMainSessions.getSelectedIndex();
        if(selectedSession < 0) {
            JOptionPane.showMessageDialog(null, "No session selected !!");
            return;
        }
        String url = "http://" + cbConfigUrl.getSelectedItem().toString() + ":7088/admin";
        String pwd = String.valueOf(txJanusAdminPwd.getPassword());
        String session = dlmJanusMainSessions.get(selectedSession).toString();
        JanusHandlesResponseDTO handleList = janusService.getHandles(url, pwd, session);
        dlmJanusMainHandles.clear();
        if(!handleList.getJanus().contains("success")) {
            dlmJanusMainHandles.addElement("     !!!!ERR!!!!  " + handleList.getTransaction());
        }
        else {
            for(String handle : handleList.getHandles()) {
                dlmJanusMainHandles.addElement(handle);
            }
        }
    }
    /**
     *
     */
    private void GetJanusSessions() {
        String url = "http://" + cbConfigUrl.getSelectedItem().toString() + ":7088/admin";
        String pwd = String.valueOf(txJanusAdminPwd.getPassword());
        RestCallOutput ro = janusService.getSession(url, pwd);
        if (ro.getResultCode() > 299) {
            JOptionPane.showMessageDialog(null, "Error:\n" + ro.getErrorMsg());
            return;
        }
        JanusSessionsResponseDTO resp = JanusTools.getJanusSessionListResp(ro.getDataMsg());
        if(!resp.getJanus().contains("success")) {
            dlmJanusMainSessions.addElement(resp.getTransaction());
        }
        else {
            dlmJanusMainSessions.clear();
            for(String sess : resp.getSessions())
                dlmJanusMainSessions.addElement(sess);
        }
    }
    /**
     *
     */
    private void JanusTestParseHandleInfoFile() {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JanusHandleInfoDTO hi = mapper.readValue(new File(txJanusTestsHandleInfoFileName.getText()),
                                                    JanusHandleInfoDTO.class);
            ArrayList<String> al = hi.toStringArray();
            for(String s : al) dlmJanusMainLog.addElement(s);
        }
        catch(Exception ex) {
            ToolFunctions.logSplit(dlmJanusRawLog, ex.getMessage(), 120);
        }

    }
    /**
     *
     */
    private void GetJanusTestHandleInfoFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter flt =new FileNameExtensionFilter("JSON files", "json");
        chooser.setFileFilter(flt);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            txJanusTestsHandleInfoFileName.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }
    /**
     * Overall Event Handler
     *
     * @param e Event which occurred
     */
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        JPopupMenu comp = (JPopupMenu) source.getComponent().getParent();
        String label = source.getText();
        UpdateLogLevel(popLoggersCommands[FindLabel(label, popLoggersLabels)]);
    }
    private int FindLabel(String label, String[] labels) {
        for (int i = 0; i < labels.length; i++) {
            if (labels[i].equals(label)) return (i);
        }
        return (1000);
    }
    private void ProcessMouseClick(int row, int x, int y) {
        TableMouseClickRow = row;
        popLoggers.show(tblData1, x, y);
    }
    /**
     * @param newLevel
     */
    private void UpdateLogLevel(String newLevel) {
        String surl = "http://" + cbConfigUrl.getSelectedItem().toString() + ":"
                + Integer.parseInt(txBePort.getText()) + "/actuator/loggers/";
        surl += globalData.getLoggerRecords().get(TableMouseClickRow).getName();
        Map<String, String> props = new HashMap<>();
        props.put("Content-Type", "application/json");
        String json = "{ \"configuredLevel\" : \"" + newLevel + "\" }";
        log.info("Update LOG level:");
        log.info(surl);
        log.info(json);
        RestCallOutput ro = restCall.SendRestApiRequest("POST", props, json, surl);
        log.info("Result code: " + ro.getResultCode());
        if (ro.getResultCode() < 300) {
            GetLoggers();
            DisplayLoggers();
        } else {
            log.info(ro.getErrorMsg());
        }
    }
    /**
     *
     */
    private void DisplayLoggers() {
        Vector<Loggers> rows = new Vector<>();
        for (LoggerRecord lr : globalData.getLoggerRecords()) {
            rows.add(new Loggers(lr.getName(), lr.getConfiguredLevel(), lr.getEffectiveLevel()));
        }
        LoggersModel model = new LoggersModel(rows);
        tblData1.setModel(model);
    }
    /**
     *
     */
    private void GetLoggers() {
        String surl = "http://" + cbConfigUrl.getSelectedItem().toString() + ":"
                + Integer.parseInt(txBePort.getText()) + "/actuator/loggers";
        RestCallOutput ro = restCall.getAllLinks(surl, true);
        if (ro.getResultCode() < 300) {
            dlmActuator.addElement("ResultCode=" + ro.getResultCode());
            CommonOutput co = jsonProcessing.GetAllLoggers(ro.getDataMsg());
            globalData.setLoggerRecords((ArrayList<LoggerRecord>) co.getResult());
            DisplayLoggers();
        }
    }
    /**
     *
     */
    private void SaveJanusLog() {
        try {
            PrintWriter vystup = new PrintWriter(new FileOutputStream("JanusLog.log"));
            for(int i=0; i<dlmJanusRawLog.getSize(); i++) vystup.println(dlmJanusRawLog.get(i));
            vystup.close();
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    /**
     *
     */
    private void JanusGetSessions() {
       String url = "http://" + cbConfigUrl.getSelectedItem().toString() + ":7088/admin";
       String pwd = String.valueOf(txJanusAdminPwd.getPassword());
       if(chkJanusRemoveMainLog.isSelected()) {
           dlmJanusMainLog.clear();
           dlmJanusRawLog.clear();
       }
       RestCallOutput ro = janusService.getSession(url, pwd);
       if (ro.getResultCode() > 299) {
           JOptionPane.showMessageDialog(null, "Error:\n" + ro.getErrorMsg());
           return;
       }
       JanusSessionsResponseDTO resp = JanusTools.getJanusSessionListResp(ro.getDataMsg());
       if(!resp.getJanus().contains("success")) {
           dlmJanusMainLog.addElement(resp.getTransaction());
       }
       else {
           dlmJanusMainLog.addElement("**************************************************************************");
           dlmJanusMainLog.addElement(String.format("%-20s:%s", "janus", resp.getJanus()));
           dlmJanusMainLog.addElement(String.format("%-20s:%s", "transaction", resp.getTransaction()));
           dlmJanusMainLog.addElement(String.format("sessions:"));
           for(String sess : resp.getSessions())
               dlmJanusMainLog.addElement(String.format("    - %s", sess));
       }
        for(String sess : resp.getSessions()) {
            dlmJanusMainLog.addElement(".............................................................................");
            dlmJanusMainLog.addElement(String.format("Session:  %s", sess));
            JanusHandlesResponseDTO handleList = janusService.getHandles(url, pwd, sess);
            if(!handleList.getJanus().contains("success")) {
                dlmJanusMainLog.addElement("     !!!!ERR!!!!  " + handleList.getTransaction());
            }
            else {
                dlmJanusMainLog.addElement(String.format("  - %-20s:%s", "janus", handleList.getJanus()));
                dlmJanusMainLog.addElement(String.format("  - %-20s:%s", "session_id", handleList.getSession_id()));
                dlmJanusMainLog.addElement(String.format("  - %-20s:%s", "transaction", handleList.getTransaction()));
                dlmJanusMainLog.addElement(String.format("  - %-20s:", "handles"));

                for(String handle : handleList.getHandles()) {
                    dlmJanusMainLog.addElement("     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .");
                    dlmJanusMainLog.addElement(String.format("    - %-20s", handle));
                    JanusHandlesInfoResponseDTO handleObj = janusService.getHandleInfo(url, pwd, sess, handle);
                    ToolFunctions.dumpHandleInfoResponse(handleObj, dlmJanusRawLog);
                    JanusHandleInfoDTO hi = handleObj.getInfo();
                    if(hi == null) {
                        dlmJanusMainLog.addElement("              NULL !!!!");
                    }
                    else {
                        dlmJanusMainLog.addElement(String.format("     - %-20s:%s", "transport", hi.getSession_transport()));
                        dlmJanusMainLog.addElement(String.format("     - %-20s:%s", "plugin", hi.getPlugin()));
                        dlmJanusMainLog.addElement(String.format("     - %-20s:%s", "ice-mode", hi.getIce_mode()));
                        dlmJanusMainLog.addElement(String.format("     - %-20s:%s", "ice-role", hi.getIce_role()));
                        dlmJanusMainLog.addElement(String.format("     - %-20s:%d", "queued-packets", hi.getQueued_packets()));
                        dlmJanusMainLog.addElement(String.format("     - %-20s:%d", "# Streams", hi.getStreams().length));
                    }
                }
            }
        }
    }
    /**
     *
     */
    private void SendTouchCommand() {
        if (globalData.token == null) {
            JOptionPane.showMessageDialog(null, "No Access token available !");
            return;
        }
        String rackId = tools.parseRackId(cbButtonsRacks.getSelectedItem().toString());
        int x = Integer.parseInt(txButtonsCoordX.getText());
        int y = Integer.parseInt(txButtonsCoordY.getText());
        RestCallOutput res = restCall.sendTouchCommand(rackId, x, y, globalData.token.getToken());
        if (res.getResultCode() > 299) {
            JOptionPane.showMessageDialog(null, "Error:\n" + res.getErrorMsg());
        }
    }
    /**
     *
     */
    private void SendFpkSequence() {
       SendFpkButton(1);
       try { Thread.sleep(500); } catch(Exception ex) { log.info("Sleep error !");}
       SendFpkButton(2);
    }
    /**
     *
     */
    private void SendFpkButton(int ord) {
        if (globalData.token == null) {
            JOptionPane.showMessageDialog(null, "No Access token available !");
            return;
        }
        String cmd;
        if(ord == 1) cmd = cbButtonsFpk.getSelectedItem().toString();
        else cmd = cbButtonsFpk2.getSelectedItem().toString();
        String rackId = tools.parseRackId(cbButtonsRacks.getSelectedItem().toString());

        RestCallOutput res = restCall.sendFpkCommand(rackId, cmd, globalData.token.getToken());
        if (res.getResultCode() > 299) {
            JOptionPane.showMessageDialog(null, "Error:\n" + res.getErrorMsg());
        }
    }
    /**
     *
     */
    private void SendAbtButton() {
        if (globalData.token == null) {
            JOptionPane.showMessageDialog(null, "No Access token available !");
            return;
        }
        String cmd = cbButtonsAbt.getSelectedItem().toString();
        String rackId = tools.parseRackId(cbButtonsRacks.getSelectedItem().toString());

        RestCallOutput res = restCall.sendAbtCommand(rackId, cmd, globalData.token.getToken());
        if (res.getResultCode() > 299) {
            JOptionPane.showMessageDialog(null, "Error:\n" + res.getErrorMsg());
        }
    }
    /**
     *
     */
    private void GetUserProfile() {
        if (globalData.token == null) {
            JOptionPane.showMessageDialog(null, "No Access token available !");
            return;
        }
        RestCallOutput res = restCall.readUserProfile(globalData.token.getToken(), true);
        if (res.getResultCode() > 299) {
            JOptionPane.showMessageDialog(null, "Error:\n" + res.getErrorMsg());
        }
        ArrayList<String> json = jsonProcessing.ParseJsonObject(res.getDataMsg());
        StringBuilder spom = new StringBuilder();
        for(String s : json) {
            spom.append(s);
            spom.append("\n");
        }
        txaUserProfileUserProfile.setText(spom.toString());
    }
    /**
     *
     */
    private void RemoveRelay() {
        int sel = tblRealys.getSelectedRow();
        if(sel < 0) {
            JOptionPane.showMessageDialog(null, "No relay selected !");
            return;
        }
        int pos = (int)tblRealys.getModel().getValueAt(sel, 0);
        relays.remove(findRelay(pos));
        Vector<RelayTableRow> rows = new Vector<>();
        for(RelayDefinitionDTO r : relays) {
            rows.add(new RelayTableRow(r.getPosition(), r.getName(), r.getType()));
        }
        tblRealys.setModel(new RelayTableModel(rows));
    }
    /**
     * @param pos Relay position
     * @return >= if OK, otherwise -1
     */
    private int findRelay(int pos) {
        for(int i=0; i<relays.size(); i++) {
            if(relays.get(i).getPosition() == pos) {
                return(i);
            }
        }
        return(-1);
    }
    /**
     *
     */
    private void AddRelay() {
        RelayDefinitionDTO relay = new RelayDefinitionDTO();
        relay.setName(txCreateRackRelayName.getText());
        relay.setPosition(Integer.parseInt(txCreateRackRelayPosition.getText()));
        relay.setType(txCreateRaclRelayType.getText());
        relay.setValueeditable(true);
        relays.add(relay);

        Vector<RelayTableRow> rows = new Vector<>();
        for(RelayDefinitionDTO r : relays) {
            rows.add(new RelayTableRow(r.getPosition(), r.getName(), r.getType()));
        }
        tblRealys.setModel(new RelayTableModel(rows));
    }
    /**
     * @param cbUrls load URL configs from a file
     * @return internal config
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
        globalData.setBeIP(cbConfigUrl.getSelectedItem().toString());
        globalData.setBePort(txBePort.getText());
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
     * @param rack rack to which display shall be added
     * @param type disply type (ABT, FPK, HUD ...)
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
        rack.setName(txCreateRackName.getText());
        rack.setDescription(txCreateRackDescr.getText());
        rack.setAddress(txCreateRackAddr.getText());
        rack.setVin(txCreateRackVin.getText());
        rack.setVehicle((TestrackVehicle) cbCreateRackPlatform.getSelectedItem());
        rack.setNetwork(network);
        rack.setTestrackDisplays(new HashSet<>(displays));
        rack.setRelayDefinitions(new HashSet<>(relays));

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
    /**
     *
     */
    private void SwitchRunning() {
        if (Running) {
            Running = false;
            btnStartSimulation.setBackground(new Color(255, 100, 100));
            btnStartSimulation.setText("START Simulation");
        } else {
            Running = true;
            btnStartSimulation.setBackground(new Color(100, 255, 100));
            btnStartSimulation.setText("STOP Simulation");
            Counter1 = 0;
        }
    }
    /**
     *
     */
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
    /**
     *
     */
    private void HeartbeatUpdateRacks() {
        cbHeartbeatRacks.removeAllItems();
        cbFesimRacks.removeAllItems();
        cbButtonsRacks.removeAllItems();
        for (int i = 0; i < globalData.testracks.size(); i++) {
            String spom = "ID:" + globalData.testracks.get(i).getId();
            spom += "   Rack:" + globalData.testracks.get(i).getName();
            cbHeartbeatRacks.addItem(spom);
            cbFesimRacks.addItem(spom);
            cbButtonsRacks.addItem(spom);
        }
    }
    /**
     *
     */
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
            ToolFunctions.logSplit(dlmUserLog, res.getErrorMsg(), 100, "Error");
            tools.SetErrMsgInStatusBar("GetAllTestracks: error !");
            return;
        }
        dlmUserLog.addElement("Get all testracks:  data read, res = " + res.getResultCode());
        txaUserFeedback.setText(res.getDataMsg());
        List<TestrackDTO> racks = (List<TestrackDTO>) res.getOutputData();
        Vector<TestrackTable1> rows = new Vector<>();
        if (racks != null) {
            globalData.testracks = racks;
            for (int i = 0; i < racks.size(); i++) {
                TestrackDTO rack = racks.get(i);
                ArrayList<TestrackDisplayDTO> dl = new ArrayList<TestrackDisplayDTO>(rack.getTestrackDisplays());
                rows.add(new TestrackTable1(rack.getId(), rack.getName(), rack.getDescription(),
                        rack.getAddress(), rack.getVehicle(), rack.getAvailability(),
                        rack.getNetwork().getIp(),
                        dl.size() == 0 ? 0 : leastPort(dl), rack.getVin(),
                        rack.getLab()==null ? " " : rack.getLab().getName()));
            }
            TestrackTable1Model model = new TestrackTable1Model(rows);
            tblTestracks.setModel(model);
            HeartbeatUpdateRacks();
        }
    }
    /**
     * @param displays
     * @return
     */
    private int leastPort(ArrayList<TestrackDisplayDTO> displays) {
       int res = displays.get(0).getMgbport();
       for(TestrackDisplayDTO disp : displays) {
           if(disp.getMgbport() < res) res = disp.getMgbport();
       }
       return(res);
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
            StringBuilder result = new StringBuilder();
            cbUserMailUsers.removeAllItems();
            for (int i = 0; i < users.size(); i++) {
                UserDto user = users.get(i);
                result.append(user.getId()).append("  |  ").append(user.getUsername()).append("  |  ")
                        .append(user.getFirstName()).append("  |  ").append(user.getLastName())
                        .append("  |  ").append(user.getEmail()).append("  |  ").append(user.getRole()).append("\n");
                rows.add(new UserTable1(user.getId(), user.getUsername(), user.getFirstName(),
                        user.getLastName(), user.getEmail()));
                cbUserMailUsers.addItem(user.getEmail());
            }
            txaUserFeedback.setText(result.toString());
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
            getAllUsers();
            GetAllTestracks();
        }
        else {
            txUserLoginState.setText("Log Error !");
            JOptionPane.showMessageDialog(null, "Error getting logged - see the Logger");
            dlmUserLog.addElement("getLoginToken:   RES=" + res.getResultCode());
            dlmUserLog.addElement("ERR: " + res.getErrorMsg());
        }
    }
    /**
     * ===================================================================================
     * Main timer event
     */
    private void TimerEvent() {
        try {
            lbSysTime.setText(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toLocalTime().toString());
            Counter1++;
            if (Counter1 >= 3600) {  // EACH 3600 seconds
                Counter1 = 0;
                RestCallOutput res = restCall.getLoginToken(txUserUserName.getText(), txUserPassword.getText(), false);
                globalData.token = (AccessTokenDto) res.getOutputData();
            }
            if (Running) {
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
            }
            else {
                lbSysTime.setText("Stopped ....");
            }
        }
        catch(Exception ex) {
            dlmUserLog.addElement(ex.getMessage());
        }
    }
    /**
     *
     */
    private void UpdateRackTable() {
        Vector<TestrackTable1> rows = new Vector<>();
        for (int i = 0; i < globalData.testracks.size(); i++) {
            TestrackDTO rack = globalData.testracks.get(i);
            ArrayList<TestrackDisplayDTO> dl = new ArrayList<>(rack.getTestrackDisplays());
            rows.add(new TestrackTable1(rack.getId(), rack.getName(), rack.getDescription(),
                    rack.getAddress(), rack.getVehicle(), rack.getAvailability(),
                    rack.getNetwork().getIp(),
                    dl.size() == 0 ? 0 : dl.get(0).getMgbport(), rack.getVin(), rack.getLab().getName()));
        }
        TestrackTable1Model model = new TestrackTable1Model(rows);
        tblFrontendRacks.setModel(model);
    }
    /**
     * #####################################################################
     * Main function
     *
     * @param args COmmand line arguments
     *             <p>
     *             #####################################################################
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
        frame.setSize(new Dimension(1300, 700));
    }
    /**
     * ----------------------------------------------------------------
     * Timer listener
     */
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
        panelMain.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        panelMain.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
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
        panel14.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2.addTab("User Profile", panel14);
        btnUserProfileGetProfile = new JButton();
        btnUserProfileGetProfile.setText("Get Profile");
        panel14.add(btnUserProfileGetProfile, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel14.add(spacer4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane6 = new JScrollPane();
        panel14.add(scrollPane6, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txaUserProfileUserProfile = new JTextArea();
        Font txaUserProfileUserProfileFont = this.$$$getFont$$$("Courier New", Font.PLAIN, 12, txaUserProfileUserProfile.getFont());
        if (txaUserProfileUserProfileFont != null) txaUserProfileUserProfile.setFont(txaUserProfileUserProfileFont);
        txaUserProfileUserProfile.setLineWrap(true);
        scrollPane6.setViewportView(txaUserProfileUserProfile);
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Testrack Functions", panel15);
        final JSplitPane splitPane4 = new JSplitPane();
        splitPane4.setDividerLocation(459);
        splitPane4.setDividerSize(6);
        splitPane4.setOrientation(0);
        panel15.add(splitPane4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane4.setRightComponent(panel16);
        panel16.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Testrack List   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel16.getFont()), new Color(-14672672)));
        final JScrollPane scrollPane7 = new JScrollPane();
        panel16.add(scrollPane7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblTestracks = new JTable();
        scrollPane7.setViewportView(tblTestracks);
        btnGetAllTestracks = new JButton();
        btnGetAllTestracks.setText("Get All Testracks");
        panel16.add(btnGetAllTestracks, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane4.setLeftComponent(panel17);
        tabbedPane3 = new JTabbedPane();
        panel17.add(tabbedPane3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        tabbedPane3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel18 = new JPanel();
        panel18.setLayout(new GridLayoutManager(5, 7, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane3.addTab("Testrack Services", panel18);
        panel18.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$(null, Font.BOLD, 12, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setText("Testrack:");
        panel18.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbHeartbeatRacks = new JComboBox();
        cbHeartbeatRacks.setEditable(false);
        panel18.add(cbHeartbeatRacks, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnHeartbeatSendHb = new JButton();
        btnHeartbeatSendHb.setText("Send HeartBeat Tick");
        panel18.add(btnHeartbeatSendHb, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDeleteTestrack = new JButton();
        btnDeleteTestrack.setBackground(new Color(-6612697));
        btnDeleteTestrack.setForeground(new Color(-792));
        btnDeleteTestrack.setText("Delete Testrack");
        panel18.add(btnDeleteTestrack, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnClearTestrackData = new JButton();
        btnClearTestrackData.setText("Clear Data");
        panel18.add(btnClearTestrackData, new GridConstraints(3, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lbGetRackDetails = new JButton();
        lbGetRackDetails.setText("Get Testrack Details");
        panel18.add(lbGetRackDetails, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel18.add(spacer5, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnAddDisplay = new JButton();
        btnAddDisplay.setText("Add Display");
        panel18.add(btnAddDisplay, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRemoveDisplay = new JButton();
        btnRemoveDisplay.setText("Remove Display");
        panel18.add(btnRemoveDisplay, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txRackHeartbeatResponse = new JTextField();
        panel18.add(txRackHeartbeatResponse, new GridConstraints(1, 2, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnSendRackHandOver = new JButton();
        btnSendRackHandOver.setText("Hand Over Selected Rack");
        panel18.add(btnSendRackHandOver, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txRackHandOverResponse = new JTextField();
        panel18.add(txRackHandOverResponse, new GridConstraints(2, 2, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JSplitPane splitPane5 = new JSplitPane();
        splitPane5.setDividerSize(6);
        panel18.add(splitPane5, new GridConstraints(4, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel19 = new JPanel();
        panel19.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane5.setLeftComponent(panel19);
        final JScrollPane scrollPane8 = new JScrollPane();
        panel19.add(scrollPane8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbTestrackData = new JList();
        Font lbTestrackDataFont = this.$$$getFont$$$("Courier New", Font.PLAIN, 12, lbTestrackData.getFont());
        if (lbTestrackDataFont != null) lbTestrackData.setFont(lbTestrackDataFont);
        scrollPane8.setViewportView(lbTestrackData);
        final JPanel panel20 = new JPanel();
        panel20.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane5.setRightComponent(panel20);
        final JPanel panel21 = new JPanel();
        panel21.setLayout(new GridLayoutManager(4, 3, new Insets(5, 5, 5, 5), -1, -1));
        panel20.add(panel21, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel21.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Display Version Change", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel21.getFont()), new Color(-14672672)));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$(null, Font.BOLD, 12, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setText("Display:");
        panel21.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel21.add(spacer6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel21.add(spacer7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cbTestrackDisplayType = new JComboBox();
        panel21.add(cbTestrackDisplayType, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null, Font.BOLD, 12, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setText("Version:");
        panel21.add(label10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txTestrackDisplayVersion = new JTextField();
        panel21.add(txTestrackDisplayVersion, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnTestrackChangeDispVersion = new JButton();
        btnTestrackChangeDispVersion.setText("Change Display Version");
        panel21.add(btnTestrackChangeDispVersion, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnHeartbeatUpdateRacks = new JButton();
        btnHeartbeatUpdateRacks.setText("Update  Testracks");
        panel18.add(btnHeartbeatUpdateRacks, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel22 = new JPanel();
        panel22.setLayout(new GridLayoutManager(4, 4, new Insets(5, 5, 5, 5), -1, -1));
        Font panel22Font = this.$$$getFont$$$(null, Font.BOLD, 16, panel22.getFont());
        if (panel22Font != null) panel22.setFont(panel22Font);
        tabbedPane3.addTab("Create Rack", panel22);
        panel22.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final Spacer spacer8 = new Spacer();
        panel22.add(spacer8, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(367, 14), null, 0, false));
        final JPanel panel23 = new JPanel();
        panel23.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel22.add(panel23, new GridConstraints(0, 3, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel23.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Relays   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel23.getFont()), new Color(-14672672)));
        final JScrollPane scrollPane9 = new JScrollPane();
        panel23.add(scrollPane9, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(453, 69), null, 0, false));
        tblRealys = new JTable();
        scrollPane9.setViewportView(tblRealys);
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null, Font.BOLD, 12, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setText("Position:");
        panel23.add(label11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackRelayPosition = new JTextField();
        Font txCreateRackRelayPositionFont = this.$$$getFont$$$(null, -1, 12, txCreateRackRelayPosition.getFont());
        if (txCreateRackRelayPositionFont != null) txCreateRackRelayPosition.setFont(txCreateRackRelayPositionFont);
        txCreateRackRelayPosition.setText("2");
        panel23.add(txCreateRackRelayPosition, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackRelayName = new JTextField();
        Font txCreateRackRelayNameFont = this.$$$getFont$$$(null, -1, 12, txCreateRackRelayName.getFont());
        if (txCreateRackRelayNameFont != null) txCreateRackRelayName.setFont(txCreateRackRelayNameFont);
        txCreateRackRelayName.setText("Kl.15");
        panel23.add(txCreateRackRelayName, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$(null, Font.BOLD, 12, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setText("Name:");
        panel23.add(label12, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        Font label13Font = this.$$$getFont$$$(null, Font.BOLD, 12, label13.getFont());
        if (label13Font != null) label13.setFont(label13Font);
        label13.setText("Type:");
        panel23.add(label13, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRaclRelayType = new JTextField();
        Font txCreateRaclRelayTypeFont = this.$$$getFont$$$(null, -1, 12, txCreateRaclRelayType.getFont());
        if (txCreateRaclRelayTypeFont != null) txCreateRaclRelayType.setFont(txCreateRaclRelayTypeFont);
        txCreateRaclRelayType.setText("SWITCH");
        panel23.add(txCreateRaclRelayType, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnCreatRackAddRelay = new JButton();
        btnCreatRackAddRelay.setText("Add Relay");
        panel23.add(btnCreatRackAddRelay, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCreateRackRemoveRelay = new JButton();
        btnCreateRackRemoveRelay.setText("Remove Relay");
        panel23.add(btnCreateRackRemoveRelay, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel24 = new JPanel();
        panel24.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel22.add(panel24, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel24.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Testrack Identification   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel24.getFont()), new Color(-14672672)));
        final JLabel label14 = new JLabel();
        Font label14Font = this.$$$getFont$$$(null, Font.BOLD, 12, label14.getFont());
        if (label14Font != null) label14.setFont(label14Font);
        label14.setText("Name:");
        panel24.add(label14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackName = new JTextField();
        Font txCreateRackNameFont = this.$$$getFont$$$(null, -1, 12, txCreateRackName.getFont());
        if (txCreateRackNameFont != null) txCreateRackName.setFont(txCreateRackNameFont);
        txCreateRackName.setText("Testrack #11");
        panel24.add(txCreateRackName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(367, 26), null, 0, false));
        final JLabel label15 = new JLabel();
        Font label15Font = this.$$$getFont$$$(null, Font.BOLD, 12, label15.getFont());
        if (label15Font != null) label15.setFont(label15Font);
        label15.setText("Description:");
        panel24.add(label15, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackDescr = new JTextField();
        Font txCreateRackDescrFont = this.$$$getFont$$$(null, -1, 12, txCreateRackDescr.getFont());
        if (txCreateRackDescrFont != null) txCreateRackDescr.setFont(txCreateRackDescrFont);
        txCreateRackDescr.setText("Demo rack");
        panel24.add(txCreateRackDescr, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(367, 26), null, 0, false));
        cbCreateRackPlatform = new JComboBox();
        panel24.add(cbCreateRackPlatform, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(367, 25), null, 0, false));
        final JLabel label16 = new JLabel();
        Font label16Font = this.$$$getFont$$$(null, Font.BOLD, 12, label16.getFont());
        if (label16Font != null) label16.setFont(label16Font);
        label16.setText("Platform:");
        panel24.add(label16, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackAddr = new JTextField();
        Font txCreateRackAddrFont = this.$$$getFont$$$(null, -1, 12, txCreateRackAddr.getFont());
        if (txCreateRackAddrFont != null) txCreateRackAddr.setFont(txCreateRackAddrFont);
        txCreateRackAddr.setText("Pribram");
        panel24.add(txCreateRackAddr, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(367, 26), null, 0, false));
        final JLabel label17 = new JLabel();
        Font label17Font = this.$$$getFont$$$(null, Font.BOLD, 12, label17.getFont());
        if (label17Font != null) label17.setFont(label17Font);
        label17.setText("Address:");
        panel24.add(label17, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        Font label18Font = this.$$$getFont$$$(null, Font.BOLD, 12, label18.getFont());
        if (label18Font != null) label18.setFont(label18Font);
        label18.setText("VIN:");
        panel24.add(label18, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackVin = new JTextField();
        Font txCreateRackVinFont = this.$$$getFont$$$(null, -1, 12, txCreateRackVin.getFont());
        if (txCreateRackVinFont != null) txCreateRackVin.setFont(txCreateRackVinFont);
        txCreateRackVin.setText("ABCDEFGHIJKLM");
        panel24.add(txCreateRackVin, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(367, 26), null, 0, false));
        final JPanel panel25 = new JPanel();
        panel25.setLayout(new GridLayoutManager(5, 2, new Insets(3, 3, 3, 3), -1, -1));
        panel22.add(panel25, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel25.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Displays   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel25.getFont()), new Color(-14672672)));
        final JLabel label19 = new JLabel();
        Font label19Font = this.$$$getFont$$$(null, Font.BOLD, 12, label19.getFont());
        if (label19Font != null) label19.setFont(label19Font);
        label19.setText("Type:");
        panel25.add(label19, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbCreateRackDispType = new JComboBox();
        panel25.add(cbCreateRackDispType, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(367, 25), null, 0, false));
        final JLabel label20 = new JLabel();
        Font label20Font = this.$$$getFont$$$(null, Font.BOLD, 12, label20.getFont());
        if (label20Font != null) label20.setFont(label20Font);
        label20.setText("Width:");
        panel25.add(label20, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label21 = new JLabel();
        Font label21Font = this.$$$getFont$$$(null, Font.BOLD, 12, label21.getFont());
        if (label21Font != null) label21.setFont(label21Font);
        label21.setText("Height:");
        panel25.add(label21, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackDispWidth = new JTextField();
        Font txCreateRackDispWidthFont = this.$$$getFont$$$(null, -1, 12, txCreateRackDispWidth.getFont());
        if (txCreateRackDispWidthFont != null) txCreateRackDispWidth.setFont(txCreateRackDispWidthFont);
        txCreateRackDispWidth.setText("1568");
        panel25.add(txCreateRackDispWidth, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(367, 26), null, 0, false));
        txCreateRackDispHeight = new JTextField();
        Font txCreateRackDispHeightFont = this.$$$getFont$$$(null, -1, 12, txCreateRackDispHeight.getFont());
        if (txCreateRackDispHeightFont != null) txCreateRackDispHeight.setFont(txCreateRackDispHeightFont);
        txCreateRackDispHeight.setText("704");
        panel25.add(txCreateRackDispHeight, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(367, 26), null, 0, false));
        final JLabel label22 = new JLabel();
        Font label22Font = this.$$$getFont$$$(null, Font.BOLD, 12, label22.getFont());
        if (label22Font != null) label22.setFont(label22Font);
        label22.setText("# of Devices:");
        panel25.add(label22, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackNoOfDevices = new JTextField();
        Font txCreateRackNoOfDevicesFont = this.$$$getFont$$$(null, -1, 12, txCreateRackNoOfDevices.getFont());
        if (txCreateRackNoOfDevicesFont != null) txCreateRackNoOfDevices.setFont(txCreateRackNoOfDevicesFont);
        txCreateRackNoOfDevices.setText("3");
        panel25.add(txCreateRackNoOfDevices, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(367, 26), null, 0, false));
        final JLabel label23 = new JLabel();
        Font label23Font = this.$$$getFont$$$(null, -1, 10, label23.getFont());
        if (label23Font != null) label23.setFont(label23Font);
        label23.setText("Note: 1st display is always ABT, if at least 2 displays the 2nd one is FPK and if 3 displays the last one is HUD");
        panel25.add(label23, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel26 = new JPanel();
        panel26.setLayout(new GridLayoutManager(3, 2, new Insets(3, 3, 3, 3), -1, -1));
        panel22.add(panel26, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel26.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Network   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel26.getFont()), new Color(-14672672)));
        final JLabel label24 = new JLabel();
        Font label24Font = this.$$$getFont$$$(null, Font.BOLD, 12, label24.getFont());
        if (label24Font != null) label24.setFont(label24Font);
        label24.setText("IP Address::");
        panel26.add(label24, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label25 = new JLabel();
        Font label25Font = this.$$$getFont$$$(null, Font.BOLD, 12, label25.getFont());
        if (label25Font != null) label25.setFont(label25Font);
        label25.setText("Quido Port:");
        panel26.add(label25, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackQuidoPort = new JTextField();
        Font txCreateRackQuidoPortFont = this.$$$getFont$$$(null, -1, 12, txCreateRackQuidoPort.getFont());
        if (txCreateRackQuidoPortFont != null) txCreateRackQuidoPort.setFont(txCreateRackQuidoPortFont);
        txCreateRackQuidoPort.setText("8085");
        panel26.add(txCreateRackQuidoPort, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackIpAddr = new JTextField();
        Font txCreateRackIpAddrFont = this.$$$getFont$$$(null, -1, 12, txCreateRackIpAddr.getFont());
        if (txCreateRackIpAddrFont != null) txCreateRackIpAddr.setFont(txCreateRackIpAddrFont);
        txCreateRackIpAddr.setText("192.168.1.222");
        panel26.add(txCreateRackIpAddr, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel26.add(spacer9, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnCreatRack = new JButton();
        btnCreatRack.setText("          CREATE NEW TESTRACK          ");
        panel22.add(btnCreatRack, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel27 = new JPanel();
        panel27.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Front End", panel27);
        tabbedPane4 = new JTabbedPane();
        panel27.add(tabbedPane4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel28 = new JPanel();
        panel28.setLayout(new GridLayoutManager(5, 4, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane4.addTab("Front End Simulation", panel28);
        panel28.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        btnStartSimulation = new JButton();
        btnStartSimulation.setBackground(new Color(-515537));
        btnStartSimulation.setText("START Simulation");
        panel28.add(btnStartSimulation, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel28.add(spacer10, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lbSysTime = new JLabel();
        lbSysTime.setText("Label");
        panel28.add(lbSysTime, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkFrontendConnectRack = new JCheckBox();
        chkFrontendConnectRack.setText("Connect to rack ID:");
        panel28.add(chkFrontendConnectRack, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane10 = new JScrollPane();
        panel28.add(scrollPane10, new GridConstraints(3, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblFrontendRacks = new JTable();
        scrollPane10.setViewportView(tblFrontendRacks);
        final JLabel label26 = new JLabel();
        Font label26Font = this.$$$getFont$$$(null, Font.BOLD, 12, label26.getFont());
        if (label26Font != null) label26.setFont(label26Font);
        label26.setText("Last Cyclic Response:");
        panel28.add(label26, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txFrontEndLastResponse = new JTextField();
        Font txFrontEndLastResponseFont = this.$$$getFont$$$(null, -1, 12, txFrontEndLastResponse.getFont());
        if (txFrontEndLastResponseFont != null) txFrontEndLastResponse.setFont(txFrontEndLastResponseFont);
        txFrontEndLastResponse.setText("");
        panel28.add(txFrontEndLastResponse, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(244, 26), null, 0, false));
        final JLabel label27 = new JLabel();
        Font label27Font = this.$$$getFont$$$(null, Font.BOLD, 12, label27.getFont());
        if (label27Font != null) label27.setFont(label27Font);
        label27.setText("Last Single Response:");
        panel28.add(label27, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txFrontEndLastSingleResponse = new JTextField();
        Font txFrontEndLastSingleResponseFont = this.$$$getFont$$$(null, -1, 12, txFrontEndLastSingleResponse.getFont());
        if (txFrontEndLastSingleResponseFont != null)
            txFrontEndLastSingleResponse.setFont(txFrontEndLastSingleResponseFont);
        txFrontEndLastSingleResponse.setText("");
        panel28.add(txFrontEndLastSingleResponse, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(244, 26), null, 0, false));
        cbFesimRacks = new JComboBox();
        panel28.add(cbFesimRacks, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel29 = new JPanel();
        panel29.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Labs", panel29);
        final JSplitPane splitPane6 = new JSplitPane();
        splitPane6.setDividerLocation(400);
        splitPane6.setDividerSize(6);
        splitPane6.setOrientation(0);
        panel29.add(splitPane6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel30 = new JPanel();
        panel30.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane6.setLeftComponent(panel30);
        tabbedPane10 = new JTabbedPane();
        panel30.add(tabbedPane10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel31 = new JPanel();
        panel31.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane10.addTab("Main", panel31);
        final JSplitPane splitPane7 = new JSplitPane();
        splitPane7.setDividerLocation(134);
        splitPane7.setDividerSize(6);
        splitPane7.setOrientation(0);
        panel31.add(splitPane7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel32 = new JPanel();
        panel32.setLayout(new GridLayoutManager(2, 3, new Insets(3, 3, 3, 3), -1, -1));
        splitPane7.setLeftComponent(panel32);
        panel32.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   LAB List   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel32.getFont()), new Color(-14672672)));
        final JScrollPane scrollPane11 = new JScrollPane();
        panel32.add(scrollPane11, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblLabsMain = new JTable();
        scrollPane11.setViewportView(tblLabsMain);
        btnLabUpdateLab = new JButton();
        btnLabUpdateLab.setText("Update");
        panel32.add(btnLabUpdateLab, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnLabRemoveLab = new JButton();
        btnLabRemoveLab.setText("Remove LAB");
        panel32.add(btnLabRemoveLab, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        panel32.add(spacer11, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel33 = new JPanel();
        panel33.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane7.setRightComponent(panel33);
        final JSplitPane splitPane8 = new JSplitPane();
        splitPane8.setDividerLocation(600);
        splitPane8.setDividerSize(6);
        panel33.add(splitPane8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel34 = new JPanel();
        panel34.setLayout(new GridLayoutManager(3, 8, new Insets(3, 3, 3, 3), -1, -1));
        splitPane8.setRightComponent(panel34);
        panel34.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   LAB's User List   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel34.getFont()), new Color(-14672672)));
        btnLabRemoveUser = new JButton();
        btnLabRemoveUser.setMargin(new Insets(0, 0, 0, 0));
        btnLabRemoveUser.setText("Remove User");
        panel34.add(btnLabRemoveUser, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane12 = new JScrollPane();
        panel34.add(scrollPane12, new GridConstraints(0, 0, 1, 8, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblLabUsers = new JTable();
        scrollPane12.setViewportView(tblLabUsers);
        btnLabAddUser = new JButton();
        btnLabAddUser.setMargin(new Insets(0, 0, 0, 0));
        btnLabAddUser.setText("Add User");
        panel34.add(btnLabAddUser, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label28 = new JLabel();
        Font label28Font = this.$$$getFont$$$(null, Font.BOLD, 13, label28.getFont());
        if (label28Font != null) label28.setFont(label28Font);
        label28.setText("New User:");
        panel34.add(label28, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label29 = new JLabel();
        Font label29Font = this.$$$getFont$$$(null, Font.BOLD, 13, label29.getFont());
        if (label29Font != null) label29.setFont(label29Font);
        label29.setText("State:");
        panel34.add(label29, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbLabNewState = new JComboBox();
        panel34.add(cbLabNewState, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbLabNewRole = new JComboBox();
        panel34.add(cbLabNewRole, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label30 = new JLabel();
        Font label30Font = this.$$$getFont$$$(null, Font.BOLD, 13, label30.getFont());
        if (label30Font != null) label30.setFont(label30Font);
        label30.setText("Role:");
        panel34.add(label30, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbLabNewUser = new JComboBox();
        Font cbLabNewUserFont = this.$$$getFont$$$("Courier New", -1, 12, cbLabNewUser.getFont());
        if (cbLabNewUserFont != null) cbLabNewUser.setFont(cbLabNewUserFont);
        panel34.add(cbLabNewUser, new GridConstraints(2, 1, 1, 7, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnLabModifyUser = new JButton();
        btnLabModifyUser.setMargin(new Insets(0, 0, 0, 0));
        btnLabModifyUser.setText("Modify User");
        panel34.add(btnLabModifyUser, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel35 = new JPanel();
        panel35.setLayout(new GridLayoutManager(2, 6, new Insets(3, 3, 3, 3), -1, -1));
        splitPane8.setLeftComponent(panel35);
        panel35.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   LAB's Testrack List   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel35.getFont()), new Color(-14672672)));
        final JScrollPane scrollPane13 = new JScrollPane();
        panel35.add(scrollPane13, new GridConstraints(0, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblLabRack = new JTable();
        scrollPane13.setViewportView(tblLabRack);
        btnLabRemoveRack = new JButton();
        btnLabRemoveRack.setText("Remove Rack");
        panel35.add(btnLabRemoveRack, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnLabAddRack = new JButton();
        btnLabAddRack.setText("Add Rack");
        panel35.add(btnLabAddRack, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label31 = new JLabel();
        Font label31Font = this.$$$getFont$$$(null, Font.BOLD, 13, label31.getFont());
        if (label31Font != null) label31.setFont(label31Font);
        label31.setText("New Rack ID:");
        panel35.add(label31, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txLabNewRackId = new JTextField();
        panel35.add(txLabNewRackId, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer12 = new Spacer();
        panel35.add(spacer12, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel36 = new JPanel();
        panel36.setLayout(new GridLayoutManager(3, 3, new Insets(3, 3, 3, 3), -1, -1));
        tabbedPane10.addTab("Create LAB", panel36);
        panel36.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label32 = new JLabel();
        Font label32Font = this.$$$getFont$$$(null, Font.BOLD, 13, label32.getFont());
        if (label32Font != null) label32.setFont(label32Font);
        label32.setText("LAB Name:");
        panel36.add(label32, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer13 = new Spacer();
        panel36.add(spacer13, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer14 = new Spacer();
        panel36.add(spacer14, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txLabNewLabName = new JTextField();
        Font txLabNewLabNameFont = this.$$$getFont$$$(null, -1, 12, txLabNewLabName.getFont());
        if (txLabNewLabNameFont != null) txLabNewLabName.setFont(txLabNewLabNameFont);
        txLabNewLabName.setText("New Lab");
        panel36.add(txLabNewLabName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(367, 26), null, 0, false));
        btnLabCreateNewLab = new JButton();
        btnLabCreateNewLab.setText("Create New Lab");
        panel36.add(btnLabCreateNewLab, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer15 = new Spacer();
        panel36.add(spacer15, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel37 = new JPanel();
        panel37.setLayout(new GridLayoutManager(2, 4, new Insets(3, 3, 3, 3), -1, -1));
        splitPane6.setRightComponent(panel37);
        panel37.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        btnLabClearLog = new JButton();
        btnLabClearLog.setText("Clear");
        panel37.add(btnLabClearLog, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnLabUpdateDependencies = new JButton();
        btnLabUpdateDependencies.setText("Update Dependencies");
        panel37.add(btnLabUpdateDependencies, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer16 = new Spacer();
        panel37.add(spacer16, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JSplitPane splitPane9 = new JSplitPane();
        splitPane9.setDividerLocation(900);
        splitPane9.setDividerSize(6);
        panel37.add(splitPane9, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel38 = new JPanel();
        panel38.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane9.setLeftComponent(panel38);
        panel38.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Message / Error Log   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel38.getFont()), new Color(-14672672)));
        final JScrollPane scrollPane14 = new JScrollPane();
        panel38.add(scrollPane14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbLabsLog = new JList();
        Font lbLabsLogFont = this.$$$getFont$$$("Courier New", -1, 13, lbLabsLog.getFont());
        if (lbLabsLogFont != null) lbLabsLog.setFont(lbLabsLogFont);
        scrollPane14.setViewportView(lbLabsLog);
        final JPanel panel39 = new JPanel();
        panel39.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane9.setRightComponent(panel39);
        panel39.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   LAB Administrators   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel39.getFont()), new Color(-14672672)));
        final JScrollPane scrollPane15 = new JScrollPane();
        panel39.add(scrollPane15, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbLabAdmins = new JList();
        Font lbLabAdminsFont = this.$$$getFont$$$(null, Font.PLAIN, 12, lbLabAdmins.getFont());
        if (lbLabAdminsFont != null) lbLabAdmins.setFont(lbLabAdminsFont);
        scrollPane15.setViewportView(lbLabAdmins);
        final JPanel panel40 = new JPanel();
        panel40.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Signal Server", panel40);
        tabbedPane5 = new JTabbedPane();
        panel40.add(tabbedPane5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel41 = new JPanel();
        panel41.setLayout(new GridLayoutManager(1, 2, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane5.addTab("Main", panel41);
        panel41.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JSplitPane splitPane10 = new JSplitPane();
        splitPane10.setDividerLocation(365);
        splitPane10.setOrientation(0);
        panel41.add(splitPane10, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel42 = new JPanel();
        panel42.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane10.setLeftComponent(panel42);
        btnSignalReadAllDevices = new JButton();
        btnSignalReadAllDevices.setText("Read All Devices");
        panel42.add(btnSignalReadAllDevices, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer17 = new Spacer();
        panel42.add(spacer17, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane16 = new JScrollPane();
        panel42.add(scrollPane16, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblSignal = new JTable();
        scrollPane16.setViewportView(tblSignal);
        final JPanel panel43 = new JPanel();
        panel43.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane10.setRightComponent(panel43);
        btnClearSignalLog = new JButton();
        btnClearSignalLog.setText("Clear Log");
        panel43.add(btnClearSignalLog, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer18 = new Spacer();
        panel43.add(spacer18, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane17 = new JScrollPane();
        panel43.add(scrollPane17, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbLogSignal = new JList();
        Font lbLogSignalFont = this.$$$getFont$$$("Courier New", Font.PLAIN, 12, lbLogSignal.getFont());
        if (lbLogSignalFont != null) lbLogSignal.setFont(lbLogSignalFont);
        scrollPane17.setViewportView(lbLogSignal);
        final JPanel panel44 = new JPanel();
        panel44.setLayout(new GridLayoutManager(3, 5, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane5.addTab("Devices Management", panel44);
        panel44.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label33 = new JLabel();
        Font label33Font = this.$$$getFont$$$(null, Font.BOLD, 12, label33.getFont());
        if (label33Font != null) label33.setFont(label33Font);
        label33.setText("Device ID:");
        panel44.add(label33, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer19 = new Spacer();
        panel44.add(spacer19, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        cbSignalDevices = new JComboBox();
        panel44.add(cbSignalDevices, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnSignaUpdateDevices = new JButton();
        btnSignaUpdateDevices.setText("Update  Devices");
        panel44.add(btnSignaUpdateDevices, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel45 = new JPanel();
        panel45.setLayout(new GridLayoutManager(7, 4, new Insets(5, 5, 5, 5), -1, -1));
        panel44.add(panel45, new GridConstraints(2, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel45.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   New Device   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel45.getFont()), new Color(-14672672)));
        final JLabel label34 = new JLabel();
        Font label34Font = this.$$$getFont$$$(null, Font.BOLD, 12, label34.getFont());
        if (label34Font != null) label34.setFont(label34Font);
        label34.setText("Device ID:");
        panel45.add(label34, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer20 = new Spacer();
        panel45.add(spacer20, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txNewDeviceId = new JTextField();
        Font txNewDeviceIdFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceId.getFont());
        if (txNewDeviceIdFont != null) txNewDeviceId.setFont(txNewDeviceIdFont);
        txNewDeviceId.setText("ID");
        panel45.add(txNewDeviceId, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label35 = new JLabel();
        Font label35Font = this.$$$getFont$$$(null, Font.BOLD, 12, label35.getFont());
        if (label35Font != null) label35.setFont(label35Font);
        label35.setText("Name:");
        panel45.add(label35, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label36 = new JLabel();
        Font label36Font = this.$$$getFont$$$(null, Font.BOLD, 12, label36.getFont());
        if (label36Font != null) label36.setFont(label36Font);
        label36.setText("Description");
        panel45.add(label36, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txNewDeviceName = new JTextField();
        Font txNewDeviceNameFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceName.getFont());
        if (txNewDeviceNameFont != null) txNewDeviceName.setFont(txNewDeviceNameFont);
        txNewDeviceName.setText("Name");
        panel45.add(txNewDeviceName, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txNewDeviceDescr = new JTextField();
        Font txNewDeviceDescrFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceDescr.getFont());
        if (txNewDeviceDescrFont != null) txNewDeviceDescr.setFont(txNewDeviceDescrFont);
        txNewDeviceDescr.setText("Description");
        panel45.add(txNewDeviceDescr, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label37 = new JLabel();
        Font label37Font = this.$$$getFont$$$(null, Font.BOLD, 12, label37.getFont());
        if (label37Font != null) label37.setFont(label37Font);
        label37.setText("In Audio Port:");
        panel45.add(label37, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label38 = new JLabel();
        Font label38Font = this.$$$getFont$$$(null, Font.BOLD, 12, label38.getFont());
        if (label38Font != null) label38.setFont(label38Font);
        label38.setText("In Video Port:");
        panel45.add(label38, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txNewDeviceInAudio = new JTextField();
        Font txNewDeviceInAudioFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceInAudio.getFont());
        if (txNewDeviceInAudioFont != null) txNewDeviceInAudio.setFont(txNewDeviceInAudioFont);
        txNewDeviceInAudio.setText("7000");
        panel45.add(txNewDeviceInAudio, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txNewDeviceInVideo = new JTextField();
        Font txNewDeviceInVideoFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceInVideo.getFont());
        if (txNewDeviceInVideoFont != null) txNewDeviceInVideo.setFont(txNewDeviceInVideoFont);
        txNewDeviceInVideo.setText("7002");
        panel45.add(txNewDeviceInVideo, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label39 = new JLabel();
        Font label39Font = this.$$$getFont$$$(null, Font.BOLD, 12, label39.getFont());
        if (label39Font != null) label39.setFont(label39Font);
        label39.setText("HOST Audio OUT:");
        panel45.add(label39, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label40 = new JLabel();
        Font label40Font = this.$$$getFont$$$(null, Font.BOLD, 12, label40.getFont());
        if (label40Font != null) label40.setFont(label40Font);
        label40.setText("HOST Audio PORT:");
        panel45.add(label40, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txNewDeviceHostAudio = new JTextField();
        Font txNewDeviceHostAudioFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceHostAudio.getFont());
        if (txNewDeviceHostAudioFont != null) txNewDeviceHostAudio.setFont(txNewDeviceHostAudioFont);
        txNewDeviceHostAudio.setText("localhost");
        panel45.add(txNewDeviceHostAudio, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txNewDeviceOutAudioPort = new JTextField();
        Font txNewDeviceOutAudioPortFont = this.$$$getFont$$$(null, -1, 12, txNewDeviceOutAudioPort.getFont());
        if (txNewDeviceOutAudioPortFont != null) txNewDeviceOutAudioPort.setFont(txNewDeviceOutAudioPortFont);
        txNewDeviceOutAudioPort.setText("7004");
        panel45.add(txNewDeviceOutAudioPort, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnCreateNewDevice = new JButton();
        Font btnCreateNewDeviceFont = this.$$$getFont$$$(null, Font.BOLD, 16, btnCreateNewDevice.getFont());
        if (btnCreateNewDeviceFont != null) btnCreateNewDevice.setFont(btnCreateNewDeviceFont);
        btnCreateNewDevice.setText("Create New Device");
        panel45.add(btnCreateNewDevice, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDeleteMmDevice = new JButton();
        btnDeleteMmDevice.setBackground(new Color(-6612697));
        btnDeleteMmDevice.setForeground(new Color(-792));
        btnDeleteMmDevice.setText("Delete Device");
        panel44.add(btnDeleteMmDevice, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel46 = new JPanel();
        panel46.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Buttons / Touches", panel46);
        tabbedPane7 = new JTabbedPane();
        panel46.add(tabbedPane7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel47 = new JPanel();
        panel47.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane7.addTab("Buttons", panel47);
        final JSplitPane splitPane11 = new JSplitPane();
        splitPane11.setDividerLocation(435);
        splitPane11.setDividerSize(7);
        panel47.add(splitPane11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel48 = new JPanel();
        panel48.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        splitPane11.setLeftComponent(panel48);
        panel48.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Buttons   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel48.getFont()), new Color(-14672672)));
        final JPanel panel49 = new JPanel();
        panel49.setLayout(new GridLayoutManager(1, 3, new Insets(5, 5, 5, 5), -1, -1));
        panel48.add(panel49, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel49.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   ABT   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel49.getFont()), new Color(-14672672)));
        final JLabel label41 = new JLabel();
        Font label41Font = this.$$$getFont$$$(null, Font.BOLD, 12, label41.getFont());
        if (label41Font != null) label41.setFont(label41Font);
        label41.setText("Button:  ");
        panel49.add(label41, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbButtonsAbt = new JComboBox();
        cbButtonsAbt.setEditable(false);
        panel49.add(cbButtonsAbt, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnButtonsSendAbt = new JButton();
        btnButtonsSendAbt.setText("Send Button");
        panel49.add(btnButtonsSendAbt, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel50 = new JPanel();
        panel50.setLayout(new GridLayoutManager(6, 3, new Insets(5, 5, 5, 5), -1, -1));
        panel48.add(panel50, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel50.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   FPK   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel50.getFont()), new Color(-14672672)));
        final JLabel label42 = new JLabel();
        Font label42Font = this.$$$getFont$$$(null, Font.BOLD, 12, label42.getFont());
        if (label42Font != null) label42.setFont(label42Font);
        label42.setText("Button 1:  ");
        panel50.add(label42, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbButtonsFpk = new JComboBox();
        cbButtonsFpk.setEditable(false);
        panel50.add(cbButtonsFpk, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnButtonsSendFpk = new JButton();
        btnButtonsSendFpk.setText("Send Button");
        panel50.add(btnButtonsSendFpk, new GridConstraints(0, 2, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label43 = new JLabel();
        Font label43Font = this.$$$getFont$$$(null, Font.BOLD, 12, label43.getFont());
        if (label43Font != null) label43.setFont(label43Font);
        label43.setText("Button 2:  ");
        panel50.add(label43, new GridConstraints(1, 0, 4, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbButtonsFpk2 = new JComboBox();
        cbButtonsFpk2.setEditable(false);
        panel50.add(cbButtonsFpk2, new GridConstraints(2, 1, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnButtonsSendFpk2 = new JButton();
        btnButtonsSendFpk2.setText("Send Button");
        panel50.add(btnButtonsSendFpk2, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer21 = new Spacer();
        panel50.add(spacer21, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        btnButtonsSendFpkSeq = new JButton();
        btnButtonsSendFpkSeq.setText("Send Button 1 + Button 2");
        panel50.add(btnButtonsSendFpkSeq, new GridConstraints(4, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer22 = new Spacer();
        panel50.add(spacer22, new GridConstraints(4, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer23 = new Spacer();
        panel48.add(spacer23, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel51 = new JPanel();
        panel51.setLayout(new GridLayoutManager(3, 5, new Insets(5, 5, 5, 5), -1, -1));
        splitPane11.setRightComponent(panel51);
        panel51.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Touches  ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel51.getFont()), new Color(-14672672)));
        final JLabel label44 = new JLabel();
        Font label44Font = this.$$$getFont$$$(null, Font.BOLD, 12, label44.getFont());
        if (label44Font != null) label44.setFont(label44Font);
        label44.setText("Coord. X:");
        panel51.add(label44, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer24 = new Spacer();
        panel51.add(spacer24, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txButtonsCoordX = new JTextField();
        Font txButtonsCoordXFont = this.$$$getFont$$$(null, -1, 12, txButtonsCoordX.getFont());
        if (txButtonsCoordXFont != null) txButtonsCoordX.setFont(txButtonsCoordXFont);
        txButtonsCoordX.setText("0");
        panel51.add(txButtonsCoordX, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(233, 26), null, 0, false));
        txButtonsCoordY = new JTextField();
        Font txButtonsCoordYFont = this.$$$getFont$$$(null, -1, 12, txButtonsCoordY.getFont());
        if (txButtonsCoordYFont != null) txButtonsCoordY.setFont(txButtonsCoordYFont);
        txButtonsCoordY.setText("0");
        panel51.add(txButtonsCoordY, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(159, 26), null, 0, false));
        final JLabel label45 = new JLabel();
        Font label45Font = this.$$$getFont$$$(null, Font.BOLD, 12, label45.getFont());
        if (label45Font != null) label45.setFont(label45Font);
        label45.setText("Coord. Y:");
        panel51.add(label45, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer25 = new Spacer();
        panel51.add(spacer25, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnButtonsSendTouch = new JButton();
        btnButtonsSendTouch.setText("Send Touch");
        panel51.add(btnButtonsSendTouch, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel52 = new JPanel();
        panel52.setLayout(new GridLayoutManager(1, 4, new Insets(5, 5, 5, 5), -1, -1));
        panel47.add(panel52, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        panel52.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label46 = new JLabel();
        Font label46Font = this.$$$getFont$$$(null, Font.BOLD, 12, label46.getFont());
        if (label46Font != null) label46.setFont(label46Font);
        label46.setText("Testrack:");
        panel52.add(label46, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer26 = new Spacer();
        panel52.add(spacer26, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        cbButtonsRacks = new JComboBox();
        cbButtonsRacks.setEditable(false);
        panel52.add(cbButtonsRacks, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnButtonsUpdateRacks = new JButton();
        btnButtonsUpdateRacks.setText("Update  Testracks");
        panel52.add(btnButtonsUpdateRacks, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer27 = new Spacer();
        panel47.add(spacer27, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel53 = new JPanel();
        panel53.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane7.addTab("Touches", panel53);
        final JPanel panel54 = new JPanel();
        panel54.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Janus", panel54);
        tabbedPane8 = new JTabbedPane();
        panel54.add(tabbedPane8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel55 = new JPanel();
        panel55.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane8.addTab("Overview", panel55);
        btnGetJanusOverview = new JButton();
        btnGetJanusOverview.setText("Get Data");
        panel55.add(btnGetJanusOverview, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSplitPane splitPane12 = new JSplitPane();
        splitPane12.setDividerLocation(300);
        splitPane12.setDividerSize(6);
        splitPane12.setOrientation(0);
        panel55.add(splitPane12, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel56 = new JPanel();
        panel56.setLayout(new GridLayoutManager(1, 1, new Insets(3, 3, 3, 3), -1, -1));
        splitPane12.setLeftComponent(panel56);
        panel56.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Session List   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel56.getFont()), new Color(-14672672)));
        final JScrollPane scrollPane18 = new JScrollPane();
        panel56.add(scrollPane18, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblJanusOverview = new JTable();
        scrollPane18.setViewportView(tblJanusOverview);
        final JPanel panel57 = new JPanel();
        panel57.setLayout(new GridLayoutManager(1, 1, new Insets(3, 3, 3, 3), -1, -1));
        splitPane12.setRightComponent(panel57);
        panel57.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JSplitPane splitPane13 = new JSplitPane();
        splitPane13.setDividerLocation(500);
        splitPane13.setDividerSize(6);
        panel57.add(splitPane13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel58 = new JPanel();
        panel58.setLayout(new GridLayoutManager(1, 1, new Insets(3, 3, 3, 3), -1, -1));
        splitPane13.setLeftComponent(panel58);
        panel58.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Stream List   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel58.getFont()), new Color(-14672672)));
        final JScrollPane scrollPane19 = new JScrollPane();
        panel58.add(scrollPane19, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblJanusMainStream = new JTable();
        scrollPane19.setViewportView(tblJanusMainStream);
        final JPanel panel59 = new JPanel();
        panel59.setLayout(new GridLayoutManager(1, 1, new Insets(3, 3, 3, 3), -1, -1));
        splitPane13.setRightComponent(panel59);
        panel59.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Component List   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel59.getFont()), new Color(-14672672)));
        final JScrollPane scrollPane20 = new JScrollPane();
        panel59.add(scrollPane20, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblJanusMainComponent = new JTable();
        scrollPane20.setViewportView(tblJanusMainComponent);
        final Spacer spacer28 = new Spacer();
        panel55.add(spacer28, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel60 = new JPanel();
        panel60.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane8.addTab("Get Data", panel60);
        btnJanusGetSessions = new JButton();
        btnJanusGetSessions.setText("Get Sessions");
        panel60.add(btnJanusGetSessions, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer29 = new Spacer();
        panel60.add(spacer29, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane21 = new JScrollPane();
        panel60.add(scrollPane21, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbJanusMainLog = new JList();
        Font lbJanusMainLogFont = this.$$$getFont$$$("Courier New", -1, 14, lbJanusMainLog.getFont());
        if (lbJanusMainLogFont != null) lbJanusMainLog.setFont(lbJanusMainLogFont);
        scrollPane21.setViewportView(lbJanusMainLog);
        btnJanusClearMainLog = new JButton();
        btnJanusClearMainLog.setText("Clear Log");
        panel60.add(btnJanusClearMainLog, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkJanusRemoveMainLog = new JCheckBox();
        chkJanusRemoveMainLog.setSelected(true);
        chkJanusRemoveMainLog.setText("Clear Log Before Query");
        panel60.add(chkJanusRemoveMainLog, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel61 = new JPanel();
        panel61.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane8.addTab("Raw Data", panel61);
        btnClearJanusLog = new JButton();
        btnClearJanusLog.setText("Clear");
        panel61.add(btnClearJanusLog, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane22 = new JScrollPane();
        panel61.add(scrollPane22, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbJanusRawLog = new JList();
        Font lbJanusRawLogFont = this.$$$getFont$$$("Courier New", -1, 12, lbJanusRawLog.getFont());
        if (lbJanusRawLogFont != null) lbJanusRawLog.setFont(lbJanusRawLogFont);
        scrollPane22.setViewportView(lbJanusRawLog);
        btnJanusSaveLog = new JButton();
        btnJanusSaveLog.setText("Save Log");
        panel61.add(btnJanusSaveLog, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer30 = new Spacer();
        panel61.add(spacer30, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel62 = new JPanel();
        panel62.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane8.addTab("Tests", panel62);
        final JLabel label47 = new JLabel();
        label47.setText("HandleInfo File:");
        panel62.add(label47, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer31 = new Spacer();
        panel62.add(spacer31, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer32 = new Spacer();
        panel62.add(spacer32, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txJanusTestsHandleInfoFileName = new JTextField();
        txJanusTestsHandleInfoFileName.setText("E:\\RTL\\Tests\\janus\\Janus\\handleInfo01.json");
        panel62.add(txJanusTestsHandleInfoFileName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnJanusTestsBrowseHandleInfoFile = new JButton();
        btnJanusTestsBrowseHandleInfoFile.setText("...");
        btnJanusTestsBrowseHandleInfoFile.setToolTipText("Browse for input JSON file");
        panel62.add(btnJanusTestsBrowseHandleInfoFile, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnJanusTestsParseHandleInfoFile = new JButton();
        btnJanusTestsParseHandleInfoFile.setText("Parse");
        btnJanusTestsParseHandleInfoFile.setToolTipText("Parse selected file");
        panel62.add(btnJanusTestsParseHandleInfoFile, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel63 = new JPanel();
        panel63.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane8.addTab("Main Data", panel63);
        final JSplitPane splitPane14 = new JSplitPane();
        splitPane14.setDividerLocation(240);
        splitPane14.setDividerSize(6);
        splitPane14.setOrientation(0);
        panel63.add(splitPane14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel64 = new JPanel();
        panel64.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane14.setRightComponent(panel64);
        final JPanel panel65 = new JPanel();
        panel65.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        splitPane14.setLeftComponent(panel65);
        btnJanusMainGetSession = new JButton();
        btnJanusMainGetSession.setText("Get Sessions");
        panel65.add(btnJanusMainGetSession, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane23 = new JScrollPane();
        panel65.add(scrollPane23, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbJanusMainSessions = new JList();
        Font lbJanusMainSessionsFont = this.$$$getFont$$$("Courier 10 Pitch", -1, 14, lbJanusMainSessions.getFont());
        if (lbJanusMainSessionsFont != null) lbJanusMainSessions.setFont(lbJanusMainSessionsFont);
        scrollPane23.setViewportView(lbJanusMainSessions);
        final JScrollPane scrollPane24 = new JScrollPane();
        panel65.add(scrollPane24, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbJanusMainHandles = new JList();
        Font lbJanusMainHandlesFont = this.$$$getFont$$$("Courier 10 Pitch", -1, 14, lbJanusMainHandles.getFont());
        if (lbJanusMainHandlesFont != null) lbJanusMainHandles.setFont(lbJanusMainHandlesFont);
        scrollPane24.setViewportView(lbJanusMainHandles);
        btnJanusMainGesHandles = new JButton();
        btnJanusMainGesHandles.setText("Get Handles");
        panel65.add(btnJanusMainGesHandles, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane25 = new JScrollPane();
        panel65.add(scrollPane25, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbJanusMainHandleInfo = new JList();
        Font lbJanusMainHandleInfoFont = this.$$$getFont$$$("Courier 10 Pitch", -1, 14, lbJanusMainHandleInfo.getFont());
        if (lbJanusMainHandleInfoFont != null) lbJanusMainHandleInfo.setFont(lbJanusMainHandleInfoFont);
        scrollPane25.setViewportView(lbJanusMainHandleInfo);
        btnJanusMainGetHandleInfo = new JButton();
        btnJanusMainGetHandleInfo.setText("Get Handle Info");
        panel65.add(btnJanusMainGetHandleInfo, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel66 = new JPanel();
        panel66.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("System", panel66);
        tabbedPane6 = new JTabbedPane();
        panel66.add(tabbedPane6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel67 = new JPanel();
        panel67.setLayout(new GridLayoutManager(2, 3, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane6.addTab("System Info", panel67);
        panel67.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        btnGetSysInfo = new JButton();
        btnGetSysInfo.setText("Get BE Info");
        panel67.add(btnGetSysInfo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer33 = new Spacer();
        panel67.add(spacer33, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane26 = new JScrollPane();
        panel67.add(scrollPane26, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbSysInfo = new JList();
        Font lbSysInfoFont = this.$$$getFont$$$("Courier New", Font.PLAIN, 14, lbSysInfo.getFont());
        if (lbSysInfoFont != null) lbSysInfo.setFont(lbSysInfoFont);
        scrollPane26.setViewportView(lbSysInfo);
        btnGetSignalServerInfo = new JButton();
        btnGetSignalServerInfo.setText("Get Signal Server Info");
        panel67.add(btnGetSignalServerInfo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel68 = new JPanel();
        panel68.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Dynamic Log Level", panel68);
        btnActuatorGetLogs = new JButton();
        btnActuatorGetLogs.setText("Get Loggers");
        panel68.add(btnActuatorGetLogs, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer34 = new Spacer();
        panel68.add(spacer34, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JSplitPane splitPane15 = new JSplitPane();
        splitPane15.setDividerLocation(800);
        splitPane15.setDividerSize(6);
        panel68.add(splitPane15, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel69 = new JPanel();
        panel69.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane15.setRightComponent(panel69);
        final JScrollPane scrollPane27 = new JScrollPane();
        panel69.add(scrollPane27, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbActuator = new JList();
        scrollPane27.setViewportView(lbActuator);
        final JPanel panel70 = new JPanel();
        panel70.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane15.setLeftComponent(panel70);
        final JScrollPane scrollPane28 = new JScrollPane();
        panel70.add(scrollPane28, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblData1 = new JTable();
        scrollPane28.setViewportView(tblData1);
        btnActuatorClearLog = new JButton();
        btnActuatorClearLog.setText("Clear Log");
        panel68.add(btnActuatorClearLog, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel71 = new JPanel();
        panel71.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Log", panel71);
        final JSplitPane splitPane16 = new JSplitPane();
        splitPane16.setDividerSize(8);
        splitPane16.setOrientation(0);
        panel71.add(splitPane16, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel72 = new JPanel();
        panel72.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane16.setLeftComponent(panel72);
        btnUserClearLog = new JButton();
        btnUserClearLog.setText("Clear Log");
        panel72.add(btnUserClearLog, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane29 = new JScrollPane();
        panel72.add(scrollPane29, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(0, 135), null, 0, false));
        lbUserLog = new JList();
        Font lbUserLogFont = this.$$$getFont$$$("Courier New", Font.PLAIN, -1, lbUserLog.getFont());
        if (lbUserLogFont != null) lbUserLog.setFont(lbUserLogFont);
        scrollPane29.setViewportView(lbUserLog);
        final Spacer spacer35 = new Spacer();
        panel72.add(spacer35, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel73 = new JPanel();
        panel73.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane16.setRightComponent(panel73);
        btnUserClearText = new JButton();
        btnUserClearText.setText("Clear Text");
        panel73.add(btnUserClearText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer36 = new Spacer();
        panel73.add(spacer36, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane30 = new JScrollPane();
        panel73.add(scrollPane30, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane30.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txaUserFeedback = new JTextArea();
        Font txaUserFeedbackFont = this.$$$getFont$$$("Courier New", Font.PLAIN, -1, txaUserFeedback.getFont());
        if (txaUserFeedbackFont != null) txaUserFeedback.setFont(txaUserFeedbackFont);
        txaUserFeedback.setLineWrap(true);
        scrollPane30.setViewportView(txaUserFeedback);
        final JPanel panel74 = new JPanel();
        panel74.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane1.addTab("Configuration", panel74);
        panel74.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel75 = new JPanel();
        panel75.setLayout(new GridLayoutManager(4, 8, new Insets(0, 0, 0, 0), -1, -1));
        panel74.add(panel75, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel75.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Configuration / Connection Parameters   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel75.getFont()), new Color(-14672672)));
        final JLabel label48 = new JLabel();
        Font label48Font = this.$$$getFont$$$(null, Font.BOLD, 12, label48.getFont());
        if (label48Font != null) label48.setFont(label48Font);
        label48.setText("BE IP Address");
        panel75.add(label48, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label49 = new JLabel();
        Font label49Font = this.$$$getFont$$$(null, Font.BOLD, 12, label49.getFont());
        if (label49Font != null) label49.setFont(label49Font);
        label49.setHorizontalAlignment(4);
        label49.setHorizontalTextPosition(11);
        label49.setText("BE Port");
        panel75.add(label49, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txBePort = new JTextField();
        Font txBePortFont = this.$$$getFont$$$(null, -1, 12, txBePort.getFont());
        if (txBePortFont != null) txBePort.setFont(txBePortFont);
        txBePort.setText("8089");
        panel75.add(txBePort, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer37 = new Spacer();
        panel75.add(spacer37, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label50 = new JLabel();
        Font label50Font = this.$$$getFont$$$(null, Font.BOLD, 12, label50.getFont());
        if (label50Font != null) label50.setFont(label50Font);
        label50.setText("Signal Server Port");
        panel75.add(label50, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txConfigSignalServerPort = new JTextField();
        Font txConfigSignalServerPortFont = this.$$$getFont$$$(null, -1, 12, txConfigSignalServerPort.getFont());
        if (txConfigSignalServerPortFont != null) txConfigSignalServerPort.setFont(txConfigSignalServerPortFont);
        txConfigSignalServerPort.setText("8890");
        panel75.add(txConfigSignalServerPort, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label51 = new JLabel();
        Font label51Font = this.$$$getFont$$$(null, Font.BOLD, 10, label51.getFont());
        if (label51Font != null) label51.setFont(label51Font);
        label51.setText("  ");
        panel75.add(label51, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkVerboseLogging = new JCheckBox();
        chkVerboseLogging.setText("Verbose Logging");
        panel75.add(chkVerboseLogging, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbConfigUrl = new JComboBox();
        Font cbConfigUrlFont = this.$$$getFont$$$(null, -1, 12, cbConfigUrl.getFont());
        if (cbConfigUrlFont != null) cbConfigUrl.setFont(cbConfigUrlFont);
        panel75.add(cbConfigUrl, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txBeIpAddress = new JTextField();
        Font txBeIpAddressFont = this.$$$getFont$$$(null, -1, 12, txBeIpAddress.getFont());
        if (txBeIpAddressFont != null) txBeIpAddress.setFont(txBeIpAddressFont);
        txBeIpAddress.setText("");
        panel75.add(txBeIpAddress, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnConfigAddUrl = new JButton();
        btnConfigAddUrl.setText("Add URL:");
        panel75.add(btnConfigAddUrl, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnConfigDeleteUrl = new JButton();
        btnConfigDeleteUrl.setText("Delete URL");
        panel75.add(btnConfigDeleteUrl, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnUpdateConfig = new JButton();
        btnUpdateConfig.setMargin(new Insets(5, 5, 5, 5));
        btnUpdateConfig.setText("Update Configuration");
        panel75.add(btnUpdateConfig, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label52 = new JLabel();
        Font label52Font = this.$$$getFont$$$(null, Font.BOLD, 12, label52.getFont());
        if (label52Font != null) label52.setFont(label52Font);
        label52.setText("Janus Admin API PWD:");
        panel75.add(label52, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txJanusAdminPwd = new JPasswordField();
        txJanusAdminPwd.setText("janusoverlord");
        panel75.add(txJanusAdminPwd, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel76 = new JPanel();
        panel76.setLayout(new GridLayoutManager(2, 5, new Insets(5, 5, 5, 5), -1, -1));
        panel74.add(panel76, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel76.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   SW Version   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, 12, panel76.getFont()), new Color(-14672672)));
        final JLabel label53 = new JLabel();
        Font label53Font = this.$$$getFont$$$(null, Font.BOLD, 12, label53.getFont());
        if (label53Font != null) label53.setFont(label53Font);
        label53.setHorizontalAlignment(4);
        label53.setText("SW Version: ");
        panel76.add(label53, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JTextField textField1 = new JTextField();
        textField1.setEditable(false);
        Font textField1Font = this.$$$getFont$$$(null, -1, 12, textField1.getFont());
        if (textField1Font != null) textField1.setFont(textField1Font);
        textField1.setHorizontalAlignment(2);
        textField1.setText("1.1.0.11");
        panel76.add(textField1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label54 = new JLabel();
        Font label54Font = this.$$$getFont$$$(null, Font.BOLD, 12, label54.getFont());
        if (label54Font != null) label54.setFont(label54Font);
        label54.setHorizontalAlignment(4);
        label54.setHorizontalTextPosition(4);
        label54.setText("Build Date:");
        panel76.add(label54, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JTextField textField2 = new JTextField();
        textField2.setEditable(false);
        Font textField2Font = this.$$$getFont$$$(null, -1, 12, textField2.getFont());
        if (textField2Font != null) textField2.setFont(textField2Font);
        textField2.setText("2021-02-03");
        panel76.add(textField2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer38 = new Spacer();
        panel76.add(spacer38, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label55 = new JLabel();
        Font label55Font = this.$$$getFont$$$(null, Font.BOLD, 8, label55.getFont());
        if (label55Font != null) label55.setFont(label55Font);
        label55.setHorizontalAlignment(4);
        label55.setText("    ");
        panel76.add(label55, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer39 = new Spacer();
        panel74.add(spacer39, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel77 = new JPanel();
        panel77.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel77, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel78 = new JPanel();
        panel78.setLayout(new GridLayoutManager(1, 1, new Insets(3, 3, 3, 3), -1, -1));
        panel77.add(panel78, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel78.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txStatusBar1 = new JTextField();
        txStatusBar1.setBackground(new Color(-11282636));
        txStatusBar1.setEditable(false);
        Font txStatusBar1Font = this.$$$getFont$$$(null, -1, 11, txStatusBar1.getFont());
        if (txStatusBar1Font != null) txStatusBar1.setFont(txStatusBar1Font);
        txStatusBar1.setText("[0] Ready");
        panel78.add(txStatusBar1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel79 = new JPanel();
        panel79.setLayout(new GridLayoutManager(1, 1, new Insets(3, 3, 3, 3), -1, -1));
        panel77.add(panel79, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel79.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txStatusBar0 = new JTextField();
        txStatusBar0.setBackground(new Color(-2368028));
        txStatusBar0.setEditable(false);
        txStatusBar0.setEnabled(true);
        Font txStatusBar0Font = this.$$$getFont$$$(null, -1, 11, txStatusBar0.getFont());
        if (txStatusBar0Font != null) txStatusBar0.setFont(txStatusBar0Font);
        txStatusBar0.setText("RTL FrontEnd Simulator");
        panel79.add(txStatusBar0, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel80 = new JPanel();
        panel80.setLayout(new GridLayoutManager(1, 1, new Insets(3, 3, 3, 3), -1, -1));
        panel77.add(panel80, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel80.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txStatusBar2 = new JTextField();
        txStatusBar2.setBackground(new Color(-4012853));
        txStatusBar2.setEditable(false);
        Font txStatusBar2Font = this.$$$getFont$$$(null, -1, 11, txStatusBar2.getFont());
        if (txStatusBar2Font != null) txStatusBar2.setFont(txStatusBar2Font);
        panel80.add(txStatusBar2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer40 = new Spacer();
        panelMain.add(spacer40, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }


}
