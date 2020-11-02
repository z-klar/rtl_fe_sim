import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import common.GlobalData;
import common.RestCallOutput;
import commonEnum.DisplayType;
import commonEnum.TestrackPlatform;
import dto.*;
import service.RestCallService;
import tables.TestrackTable1;
import tables.TestrackTable1Model;
import tables.UserTable1;
import tables.UserTable1Model;

import javax.swing.*;
import javax.swing.Timer;
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


    private DefaultListModel<String> dlmUserLog = new DefaultListModel<>();
    private DefaultListModel<String> dlmRackData = new DefaultListModel<>();

    private RestCallService restCall;
    private GlobalData globalData = new GlobalData();

    private timerListener Casovac = new timerListener();
    private Timer timer = new Timer(1000, Casovac);
    private boolean Running = false;
    private int Counter1 = 0, Counter2 = 0, Counter3 = 0;

    /**
     *
     */
    public frmMain() {
        lbUserLog.setModel(dlmUserLog);
        lbTestrackData.setModel(dlmRackData);

        dlmUserLog.addElement("Start log .....");

        restCall = new RestCallService(
                txBeIpAddress.getText(),
                Integer.parseInt(txBePort.getText()),
                dlmUserLog
        );

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
            JOptionPane.showMessageDialog(null, "Result code: " + iRes);
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
            int iRes = restCall.sendVerifyEmail(UserId, globalData.token.getToken());
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
        RestCallOutput res = restCall.readAllUsers(globalData.token.getToken(), true);
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
        AccessTokenDto token = (AccessTokenDto) res.getOutputData();
        txaUserFeedback.setText("Access TOKEN:\n" + token.getToken()
                + "\nRefresh TOKEN:\n" + token.getRefreshToken());
        globalData.token = token;
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
            if (chkFrontendConnectRack.isSelected())
                restCall.sendHeartbeat(txFrontendRackId.getText(), globalData.token.getToken(), false);
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
        panel1.setLayout(new GridLayoutManager(5, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Main", panel1);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 12, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("User  Name:");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txUserUserName = new JTextField();
        Font txUserUserNameFont = this.$$$getFont$$$(null, -1, 12, txUserUserName.getFont());
        if (txUserUserNameFont != null) txUserUserName.setFont(txUserUserNameFont);
        txUserUserName.setText("admin");
        panel1.add(txUserUserName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 12, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Password:");
        panel1.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txUserPassword = new JTextField();
        Font txUserPasswordFont = this.$$$getFont$$$(null, -1, 12, txUserPassword.getFont());
        if (txUserPasswordFont != null) txUserPassword.setFont(txUserPasswordFont);
        txUserPassword.setText("admin");
        panel1.add(txUserPassword, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnUserLogin = new JButton();
        btnUserLogin.setText("Login");
        panel1.add(btnUserLogin, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnUserClearText = new JButton();
        btnUserClearText.setText("Clear Text");
        panel1.add(btnUserClearText, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txaUserFeedback = new JTextArea();
        txaUserFeedback.setLineWrap(true);
        panel1.add(txaUserFeedback, new GridConstraints(3, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 155), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(4, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblUser = new JTable();
        scrollPane1.setViewportView(tblUser);
        btnGetAllUsers = new JButton();
        btnGetAllUsers.setText("Get All Users");
        panel1.add(btnGetAllUsers, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("User Functions", panel2);
        tabbedPane2 = new JTabbedPane();
        panel2.add(tabbedPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2.addTab("Create User", panel3);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, 12, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("First  Name:");
        panel3.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txCreateUserFirstName = new JTextField();
        Font txCreateUserFirstNameFont = this.$$$getFont$$$(null, -1, 12, txCreateUserFirstName.getFont());
        if (txCreateUserFirstNameFont != null) txCreateUserFirstName.setFont(txCreateUserFirstNameFont);
        txCreateUserFirstName.setText("");
        panel3.add(txCreateUserFirstName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, 12, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Last  Name:");
        panel3.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateUserLastName = new JTextField();
        Font txCreateUserLastNameFont = this.$$$getFont$$$(null, -1, 12, txCreateUserLastName.getFont());
        if (txCreateUserLastNameFont != null) txCreateUserLastName.setFont(txCreateUserLastNameFont);
        txCreateUserLastName.setText("");
        panel3.add(txCreateUserLastName, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, 12, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Email:");
        panel3.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateUserEmail = new JTextField();
        Font txCreateUserEmailFont = this.$$$getFont$$$(null, -1, 12, txCreateUserEmail.getFont());
        if (txCreateUserEmailFont != null) txCreateUserEmail.setFont(txCreateUserEmailFont);
        txCreateUserEmail.setText("");
        panel3.add(txCreateUserEmail, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnCreateUser = new JButton();
        btnCreateUser.setText("Create New User");
        panel3.add(btnCreateUser, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2.addTab("Mail Functions", panel4);
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, Font.BOLD, 12, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("User:");
        panel4.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel4.add(spacer3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel4.add(spacer4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cbUserMailUsers = new JComboBox();
        panel4.add(cbUserMailUsers, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnUserMailUpdateUsers = new JButton();
        btnUserMailUpdateUsers.setText("Update User List");
        panel4.add(btnUserMailUpdateUsers, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnUserMailResendVerifyEmail = new JButton();
        btnUserMailResendVerifyEmail.setText("Resend Verification Email");
        panel4.add(btnUserMailResendVerifyEmail, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Testrack Functions", panel5);
        tabbedPane3 = new JTabbedPane();
        panel5.add(tabbedPane3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane3.addTab("Testrack List", panel6);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel6.add(scrollPane2, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblTestracks = new JTable();
        scrollPane2.setViewportView(tblTestracks);
        btnGetAllTestracks = new JButton();
        btnGetAllTestracks.setText("Get All Testracks");
        panel6.add(btnGetAllTestracks, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel6.add(spacer5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(4, 7, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane3.addTab("Testrack Services", panel7);
        btnHeartbeatUpdateRacks = new JButton();
        btnHeartbeatUpdateRacks.setText("Update  Testracks");
        panel7.add(btnHeartbeatUpdateRacks, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null, Font.BOLD, 12, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("Testrack:");
        panel7.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbHeartbeatRacks = new JComboBox();
        panel7.add(cbHeartbeatRacks, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        btnHeartbeatSendHb = new JButton();
        btnHeartbeatSendHb.setText("Send HeartBeat Tick");
        panel7.add(btnHeartbeatSendHb, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDeleteTestrack = new JButton();
        btnDeleteTestrack.setBackground(new Color(-6612697));
        btnDeleteTestrack.setForeground(new Color(-792));
        btnDeleteTestrack.setText("Delete Testrack");
        panel7.add(btnDeleteTestrack, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane3 = new JScrollPane();
        panel7.add(scrollPane3, new GridConstraints(3, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbTestrackData = new JList();
        Font lbTestrackDataFont = this.$$$getFont$$$("Courier New", -1, 14, lbTestrackData.getFont());
        if (lbTestrackDataFont != null) lbTestrackData.setFont(lbTestrackDataFont);
        scrollPane3.setViewportView(lbTestrackData);
        btnClearTestrackData = new JButton();
        btnClearTestrackData.setText("Clear Data");
        panel7.add(btnClearTestrackData, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lbGetRackDetails = new JButton();
        lbGetRackDetails.setText("Get Testrack Details");
        panel7.add(lbGetRackDetails, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel7.add(spacer6, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnAddDisplay = new JButton();
        btnAddDisplay.setText("Add Display");
        panel7.add(btnAddDisplay, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRemoveDisplay = new JButton();
        btnRemoveDisplay.setText("Remove Display");
        panel7.add(btnRemoveDisplay, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(13, 4, new Insets(0, 0, 0, 0), -1, -1));
        Font panel8Font = this.$$$getFont$$$(null, Font.BOLD, 16, panel8.getFont());
        if (panel8Font != null) panel8.setFont(panel8Font);
        tabbedPane3.addTab("Create Rack", panel8);
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$(null, Font.BOLD, 12, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setText("Name:");
        panel8.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel8.add(spacer7, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$(null, Font.BOLD, 12, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setText("Description:");
        panel8.add(label9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null, Font.BOLD, 12, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setText("Address:");
        panel8.add(label10, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null, Font.BOLD, 12, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setText("Platform:");
        panel8.add(label11, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbCreateRackPlatform = new JComboBox();
        panel8.add(cbCreateRackPlatform, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        txCreateRackName = new JTextField();
        Font txCreateRackNameFont = this.$$$getFont$$$(null, -1, 12, txCreateRackName.getFont());
        if (txCreateRackNameFont != null) txCreateRackName.setFont(txCreateRackNameFont);
        txCreateRackName.setText("Testrack #11");
        panel8.add(txCreateRackName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackDescr = new JTextField();
        Font txCreateRackDescrFont = this.$$$getFont$$$(null, -1, 12, txCreateRackDescr.getFont());
        if (txCreateRackDescrFont != null) txCreateRackDescr.setFont(txCreateRackDescrFont);
        txCreateRackDescr.setText("Demo rack");
        panel8.add(txCreateRackDescr, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackAddr = new JTextField();
        Font txCreateRackAddrFont = this.$$$getFont$$$(null, -1, 12, txCreateRackAddr.getFont());
        if (txCreateRackAddrFont != null) txCreateRackAddr.setFont(txCreateRackAddrFont);
        txCreateRackAddr.setText("Pribram");
        panel8.add(txCreateRackAddr, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$(null, Font.BOLD, 12, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setText("IP Address::");
        panel8.add(label12, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackIpAddr = new JTextField();
        Font txCreateRackIpAddrFont = this.$$$getFont$$$(null, -1, 12, txCreateRackIpAddr.getFont());
        if (txCreateRackIpAddrFont != null) txCreateRackIpAddr.setFont(txCreateRackIpAddrFont);
        txCreateRackIpAddr.setText("192.168.1.222");
        panel8.add(txCreateRackIpAddr, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label13 = new JLabel();
        Font label13Font = this.$$$getFont$$$(null, Font.BOLD, 12, label13.getFont());
        if (label13Font != null) label13.setFont(label13Font);
        label13.setText("Quido Port:");
        panel8.add(label13, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackQuidoPort = new JTextField();
        Font txCreateRackQuidoPortFont = this.$$$getFont$$$(null, -1, 12, txCreateRackQuidoPort.getFont());
        if (txCreateRackQuidoPortFont != null) txCreateRackQuidoPort.setFont(txCreateRackQuidoPortFont);
        txCreateRackQuidoPort.setText("8085");
        panel8.add(txCreateRackQuidoPort, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label14 = new JLabel();
        Font label14Font = this.$$$getFont$$$(null, Font.BOLD, 16, label14.getFont());
        if (label14Font != null) label14.setFont(label14Font);
        label14.setHorizontalTextPosition(0);
        label14.setText("Network:");
        panel8.add(label14, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        Font label15Font = this.$$$getFont$$$(null, Font.BOLD, 16, label15.getFont());
        if (label15Font != null) label15.setFont(label15Font);
        label15.setHorizontalTextPosition(0);
        label15.setText("Relay:");
        panel8.add(label15, new GridConstraints(5, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        Font label16Font = this.$$$getFont$$$(null, Font.BOLD, 12, label16.getFont());
        if (label16Font != null) label16.setFont(label16Font);
        label16.setText("Position:");
        panel8.add(label16, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label17 = new JLabel();
        Font label17Font = this.$$$getFont$$$(null, Font.BOLD, 12, label17.getFont());
        if (label17Font != null) label17.setFont(label17Font);
        label17.setText("Name:");
        panel8.add(label17, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        Font label18Font = this.$$$getFont$$$(null, Font.BOLD, 12, label18.getFont());
        if (label18Font != null) label18.setFont(label18Font);
        label18.setText("Type:");
        panel8.add(label18, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackRelayPosition = new JTextField();
        Font txCreateRackRelayPositionFont = this.$$$getFont$$$(null, -1, 12, txCreateRackRelayPosition.getFont());
        if (txCreateRackRelayPositionFont != null) txCreateRackRelayPosition.setFont(txCreateRackRelayPositionFont);
        txCreateRackRelayPosition.setText("2");
        panel8.add(txCreateRackRelayPosition, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackRelayName = new JTextField();
        Font txCreateRackRelayNameFont = this.$$$getFont$$$(null, -1, 12, txCreateRackRelayName.getFont());
        if (txCreateRackRelayNameFont != null) txCreateRackRelayName.setFont(txCreateRackRelayNameFont);
        txCreateRackRelayName.setText("Kl.15");
        panel8.add(txCreateRackRelayName, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRaclRelayType = new JTextField();
        Font txCreateRaclRelayTypeFont = this.$$$getFont$$$(null, -1, 12, txCreateRaclRelayType.getFont());
        if (txCreateRaclRelayTypeFont != null) txCreateRaclRelayType.setFont(txCreateRaclRelayTypeFont);
        txCreateRaclRelayType.setText("SWITCH");
        panel8.add(txCreateRaclRelayType, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label19 = new JLabel();
        Font label19Font = this.$$$getFont$$$(null, Font.BOLD, 16, label19.getFont());
        if (label19Font != null) label19.setFont(label19Font);
        label19.setHorizontalTextPosition(0);
        label19.setText("");
        panel8.add(label19, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        Font label20Font = this.$$$getFont$$$(null, Font.BOLD, 16, label20.getFont());
        if (label20Font != null) label20.setFont(label20Font);
        label20.setHorizontalTextPosition(0);
        label20.setText("Display");
        panel8.add(label20, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label21 = new JLabel();
        Font label21Font = this.$$$getFont$$$(null, Font.BOLD, 12, label21.getFont());
        if (label21Font != null) label21.setFont(label21Font);
        label21.setText("Type:");
        panel8.add(label21, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbCreateRackDispType = new JComboBox();
        panel8.add(cbCreateRackDispType, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(143, 25), null, 0, false));
        final JLabel label22 = new JLabel();
        Font label22Font = this.$$$getFont$$$(null, Font.BOLD, 12, label22.getFont());
        if (label22Font != null) label22.setFont(label22Font);
        label22.setText("Width:");
        panel8.add(label22, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label23 = new JLabel();
        Font label23Font = this.$$$getFont$$$(null, Font.BOLD, 12, label23.getFont());
        if (label23Font != null) label23.setFont(label23Font);
        label23.setText("Height:");
        panel8.add(label23, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label24 = new JLabel();
        Font label24Font = this.$$$getFont$$$(null, Font.BOLD, 12, label24.getFont());
        if (label24Font != null) label24.setFont(label24Font);
        label24.setText("# of Devices:");
        panel8.add(label24, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackDispWidth = new JTextField();
        Font txCreateRackDispWidthFont = this.$$$getFont$$$(null, -1, 12, txCreateRackDispWidth.getFont());
        if (txCreateRackDispWidthFont != null) txCreateRackDispWidth.setFont(txCreateRackDispWidthFont);
        txCreateRackDispWidth.setText("1568");
        panel8.add(txCreateRackDispWidth, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txCreateRackDispHeight = new JTextField();
        Font txCreateRackDispHeightFont = this.$$$getFont$$$(null, -1, 12, txCreateRackDispHeight.getFont());
        if (txCreateRackDispHeightFont != null) txCreateRackDispHeight.setFont(txCreateRackDispHeightFont);
        txCreateRackDispHeight.setText("704");
        panel8.add(txCreateRackDispHeight, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnCreatRack = new JButton();
        btnCreatRack.setText("CREATE NEW TESTRACK");
        panel8.add(btnCreatRack, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label25 = new JLabel();
        Font label25Font = this.$$$getFont$$$(null, -1, 10, label25.getFont());
        if (label25Font != null) label25.setFont(label25Font);
        label25.setText("Note: 1st display is always ABT, if at least 2 displays the 2nd one is FPK and if 3 displays the last one is HUD");
        panel8.add(label25, new GridConstraints(10, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCreateRackNoOfDevices = new JTextField();
        Font txCreateRackNoOfDevicesFont = this.$$$getFont$$$(null, -1, 12, txCreateRackNoOfDevices.getFont());
        if (txCreateRackNoOfDevicesFont != null) txCreateRackNoOfDevices.setFont(txCreateRackNoOfDevicesFont);
        txCreateRackNoOfDevices.setText("3");
        panel8.add(txCreateRackNoOfDevices, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Front End", panel9);
        tabbedPane4 = new JTabbedPane();
        panel9.add(tabbedPane4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane4.addTab("Front End Simulation", panel10);
        btnStartSimulation = new JButton();
        btnStartSimulation.setBackground(new Color(-515537));
        btnStartSimulation.setText("START Simulation");
        panel10.add(btnStartSimulation, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel10.add(spacer8, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lbSysTime = new JLabel();
        lbSysTime.setText("Label");
        panel10.add(lbSysTime, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkFrontendConnectRack = new JCheckBox();
        chkFrontendConnectRack.setText("Connect to rack ID:");
        panel10.add(chkFrontendConnectRack, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txFrontendRackId = new JTextField();
        Font txFrontendRackIdFont = this.$$$getFont$$$(null, -1, 12, txFrontendRackId.getFont());
        if (txFrontendRackIdFont != null) txFrontendRackId.setFont(txFrontendRackIdFont);
        txFrontendRackId.setText("1");
        panel10.add(txFrontendRackId, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane4 = new JScrollPane();
        panel10.add(scrollPane4, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblFrontendRacks = new JTable();
        scrollPane4.setViewportView(tblFrontendRacks);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Log", panel11);
        final JScrollPane scrollPane5 = new JScrollPane();
        panel11.add(scrollPane5, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(0, 135), null, 0, false));
        lbUserLog = new JList();
        scrollPane5.setViewportView(lbUserLog);
        btnUserClearLog = new JButton();
        btnUserClearLog.setText("Clear Log");
        panel11.add(btnUserClearLog, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel11.add(spacer9, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Configuration", panel12);
        final JLabel label26 = new JLabel();
        Font label26Font = this.$$$getFont$$$(null, Font.BOLD, 12, label26.getFont());
        if (label26Font != null) label26.setFont(label26Font);
        label26.setText("BE IP Address");
        panel12.add(label26, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel12.add(spacer10, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        panel12.add(spacer11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txBeIpAddress = new JTextField();
        Font txBeIpAddressFont = this.$$$getFont$$$(null, -1, 12, txBeIpAddress.getFont());
        if (txBeIpAddressFont != null) txBeIpAddress.setFont(txBeIpAddressFont);
        txBeIpAddress.setText("localhost");
        panel12.add(txBeIpAddress, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label27 = new JLabel();
        Font label27Font = this.$$$getFont$$$(null, Font.BOLD, 12, label27.getFont());
        if (label27Font != null) label27.setFont(label27Font);
        label27.setText("BE Port");
        panel12.add(label27, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txBePort = new JTextField();
        Font txBePortFont = this.$$$getFont$$$(null, -1, 12, txBePort.getFont());
        if (txBePortFont != null) txBePort.setFont(txBePortFont);
        txBePort.setText("8089");
        panel12.add(txBePort, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
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
