package tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.GlobalData;
import common.RestCallOutput;
import commonEnum.DisplayType;
import dto.ConfigurationData;
import dto.RelayDefinitionDTO;
import dto.TestrackDTO;
import dto.TestrackDisplayDTO;
import javafx.scene.control.ComboBox;
import service.RestCallService;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ToolFunctions {

    private GlobalData globalData;
    private RestCallService restService;
    private JTextField txStatusBar;
    private DefaultListModel<String> dlmLog;

    private int NoErrors1 = 0;

    public ToolFunctions(GlobalData gd,
                         RestCallService rs,
                         JTextField sts,
                         DefaultListModel<String> dlm) {
         this.globalData = gd;
         this.restService = rs;
         this.txStatusBar = sts;
         this.dlmLog = dlm;
    }

    /**
     *
     * @param dlmSysInfo
     */
    public void GetSignalServerInfo(DefaultListModel<String> dlmSysInfo) {
        String spom;

        dlmSysInfo.clear();
        if(globalData.token == null) {
            dlmSysInfo.addElement("User NOT logged !!!");
            SetErrMsgInStatusBar("User NOT logged !!!");
            return;
        }
        RestCallOutput res = restService.getSignalServerInfo();
        int iRes = res.getResultCode();
        if(iRes < 300) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode actualObj = mapper.readTree(res.getDataMsg());
                Iterator<Map.Entry<String, JsonNode>> root = actualObj.fields();
                while(root.hasNext()) {
                    Map.Entry<String, JsonNode> entry = root.next();
                    spom = String.format("%-23s %s", entry.getKey(), entry.getValue().asText());
                    dlmSysInfo.addElement(spom);
                }
            }
            catch(Exception ex) {
                dlmSysInfo.addElement("Error parsing JSON:");
                dlmSysInfo.addElement(res.getDataMsg());
            }
        }
        else {
            dlmSysInfo.addElement("ERROR: " + res.getResultCode());
            dlmSysInfo.addElement(res.getErrorMsg());
        }
    }
    /**
     *
     */
    public void GetSystemInfo(DefaultListModel dlmSysInfo) {
        String spom;

        dlmSysInfo.clear();
        if(globalData.token == null) {
            dlmSysInfo.addElement("User NOT logged !!!");
            SetErrMsgInStatusBar("User NOT logged !!!");
            return;
        }
        RestCallOutput res = restService.getSystemInfo(globalData.token.getToken());
        int iRes = res.getResultCode();
        if(iRes < 300) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode actualObj = mapper.readTree(res.getDataMsg());
                Iterator<Map.Entry<String, JsonNode>> root = actualObj.fields();
                while(root.hasNext()) {
                    Map.Entry<String, JsonNode> entry = root.next();
                    spom = String.format("%-23s %s", entry.getKey(), entry.getValue().asText());
                    dlmSysInfo.addElement(spom);
                }
            }
            catch(Exception ex) {
                dlmSysInfo.addElement("Error parsing JSON:");
                dlmSysInfo.addElement(res.getDataMsg());
            }
        }
        else {
            dlmSysInfo.addElement("ERROR: " + res.getResultCode());
            dlmSysInfo.addElement(res.getErrorMsg());
        }
    }

    public void RemoveFpk(TestrackDTO rack) {
        for (Iterator<TestrackDisplayDTO> iterator = rack.getTestrackDisplays().iterator(); iterator.hasNext(); ) {
            TestrackDisplayDTO disp = iterator.next();
            if (disp.getType() == DisplayType.FPK) {
                iterator.remove();
            }
        }
    }

    public void RemoveHud(TestrackDTO rack) {
        for (Iterator<TestrackDisplayDTO> iterator = rack.getTestrackDisplays().iterator(); iterator.hasNext(); ) {
            TestrackDisplayDTO disp = iterator.next();
            if (disp.getType() == DisplayType.HUD) {
                iterator.remove();
            }
        }
    }

    public void UpdateRack(TestrackDTO rack, DefaultListModel<String> dlmUserLog) {
        RestCallOutput res = restService.updateTestrack(rack, globalData.token.getToken(), true);
        dlmUserLog.addElement("JSON:");
        dlmUserLog.addElement(res.getInfoMsg());
        if (res.getResultCode() >= 300) {
            JOptionPane.showMessageDialog(null, "ERROR: Result code = " + res.getResultCode());
            dlmUserLog.addElement("Error:");
            dlmUserLog.addElement(res.getErrorMsg());
            dlmUserLog.addElement("MSG:");
            dlmUserLog.addElement(res.getDataMsg());
        }
    }

    public void GetRackDetails(DefaultListModel<String> dlmRackData,
                               JComboBox<String> cbHeartbeatRacks) {
        String rackName = cbHeartbeatRacks.getSelectedItem().toString();
        int rackId = Integer.parseInt(parseRackId(rackName));
        int rackpos = GetRackOrderById(rackId);
        if (rackpos >= 0) {
            TestrackDTO rack = globalData.testracks.get(rackpos);
            dlmRackData.clear();
            dlmRackData.addElement("======  Displays ======");
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
                dlmRackData.addElement("    Version:  " + disp.getVersion());
                i++;
            }

            dlmRackData.addElement("=======  Relays ========");
            i = 0;
            Iterator<RelayDefinitionDTO> ir = rack.getRelayDefinitions().iterator();
            while (ir.hasNext()) {
                RelayDefinitionDTO rel = ir.next();
                dlmRackData.addElement(" [" + i + "]");
                dlmRackData.addElement("    ID:       " + rel.getId());
                dlmRackData.addElement("    Name:     " + rel.getName());
                dlmRackData.addElement("    Type:     " + rel.getType());
                dlmRackData.addElement("    Position: " + rel.getPosition());
                dlmRackData.addElement("    Editable: " + rel.getValueeditable());
                i++;
            }
        }
        else {
            dlmRackData.addElement("GetRackDetails: Rack=" + rackName + "   -> Unknown POS !");
        }
    }
    /**
     * Find the ID (database column) of given testrack
     *
     * @param name
     * @return
     */
    public String GetRackIdByName(String name) {
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
    public int GetRackOrderByName(String name) {
        for (int i = 0; i < globalData.testracks.size(); i++) {
            if (globalData.testracks.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public int GetRackOrderById(int id) {
        for (int i = 0; i < globalData.testracks.size(); i++) {
            if (globalData.testracks.get(i).getId() == (long)id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * rackDescr contains something like "ID:23   "RACK:desccription"
     *
     * @param rackDescr
     * @return
     */
    public String parseRackId(String rackDescr) {
        String sRes = "";
        int pos1 = rackDescr.indexOf(":");
        String spom1 = rackDescr.substring(pos1+1);
        int pos2 = spom1.indexOf(" ");
        spom1 = spom1.substring(0, pos2);
        long id = Long.parseLong(spom1);
        return(String.format("%d", id));
    }
    /**
     *
     */
    public void SetErrMsgInStatusBar(String msg) {
        NoErrors1++;
        txStatusBar.setText(String.format("[%d] %s               ", NoErrors1, msg));
        txStatusBar.setBackground(new Color(255, 100, 100));
    }
    /**
     *
     */
    public void ClearErrMessages() {
        NoErrors1 = 0;
        txStatusBar.setText("[0] Ready");
        txStatusBar.setBackground(new Color(100, 255, 100));
    }

    public void SaveConfiguration(ConfigurationData cfg) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PrintWriter vystup = new PrintWriter(new FileOutputStream("RTL_FE_SIM.json"));
            String json = mapper.writeValueAsString(cfg);
            vystup.write(json);
            vystup.close();
        }
        catch(IOException e) {
            System.out.println("Error saving config:");
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void ParseToken(String token, DefaultListModel<String> dlm, JTextArea txa) {
        String spom, spom2;
        Object value;

        dlm.clear();
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        for (Map.Entry<String,Claim> entry : claims.entrySet()) {
            Claim cl = entry.getValue();
            value = cl.asBoolean();
            if(value != null) spom2 = (boolean)value ? "TRUE" : "FALSE";
            else {
                value = cl.asInt();
                if(value != null) spom2 = value.toString();
                else {
                    value = cl.asArray(String.class);
                    if(value != null) {
                        String [] arr = (String [])value;
                        spom2 = "["; boolean first = true;
                        for(String a : arr) {
                            if (first) {
                                spom2 += a;
                                first = false;
                            } else {
                                spom2 += ("," + a);
                            }
                        }
                        spom2 += "]";
                    }
                    else {
                        value = cl.asMap();
                        if(value != null) {
                            Map<String,Object> mapka = (Map<String, Object>)value;
                            spom2 = "["; boolean first = true;
                            for(Map.Entry<String,Object> ee : mapka.entrySet()) {
                                if(first) {
                                    first = false;
                                    spom2 += (ee.getKey() + "{}");
                                }
                                else {
                                    spom2 += ("," + ee.getKey() + "{}");
                                }
                            }
                            spom2 += "]";
                        }
                        else {
                            spom2 = cl.asString();
                        }
                    }
                }
            }
            spom = String.format("%20s : %s", entry.getKey(), spom2);
            dlm.addElement(spom);
        }

        /*
        spom = jwt.getPayload();
        byte[] decodedBytes = Base64.getDecoder().decode(spom);
        String decodedString = new String(decodedBytes);
        txa.setText(decodedString);
         */
    }

    /**
     *
     */
    public void InitButtonCommand(JComboBox<String> cbAbt,
                                  JComboBox<String> cbFpk,
                                  JComboBox<String> cbFpk2) {
        globalData.abtCommands = new ArrayList<>();
        globalData.abtCommands.add("HOME_PRESSED");
        globalData.abtCommands.add("HOME_RELEASED");
        globalData.abtCommands.add("MENU_PRESSED");
        globalData.abtCommands.add("MENU_RELEASED");
        globalData.abtCommands.add("POWER_PRESSED");
        globalData.abtCommands.add("POWER_RELEASED");

        cbAbt.removeAllItems();
        for(String btn : globalData.abtCommands) cbAbt.addItem(btn);
        cbAbt.setSelectedIndex(0);

        globalData.fpkCommands = new ArrayList<>();
        globalData.fpkCommands.add("KEY_RELEASED_NO_KEY");
        globalData.fpkCommands.add("CONTEXT_MENU");
        globalData.fpkCommands.add("MENU_UP_NEXT_SCREEN");
        globalData.fpkCommands.add("MENU_DOWN_NEXT_SCREEN");
        globalData.fpkCommands.add("UP");
        globalData.fpkCommands.add("DOWN");
        globalData.fpkCommands.add("UP_THUMBWHEEL");
        globalData.fpkCommands.add("DOWN_THUMBWHEEL");
        globalData.fpkCommands.add("OK_THUMBWHEEL_BUTTON");
        globalData.fpkCommands.add("CANCEL_ESCAPE");
        globalData.fpkCommands.add("MAIN_MENU");
        globalData.fpkCommands.add("SIDE_MENU_LEFT");
        globalData.fpkCommands.add("SIDE_MENU_RIGHT");
        globalData.fpkCommands.add("FAS_MENU");
        globalData.fpkCommands.add("LEFT_RIGHT_THUMBWHEEL");
        globalData.fpkCommands.add("VOLUME_UP");
        globalData.fpkCommands.add("VOLUME_DOWN");
        globalData.fpkCommands.add("VOLUME_UP_THUMBWHEEL");
        globalData.fpkCommands.add("VOLUME_DOWN_THUMBWHEEL");
        globalData.fpkCommands.add("VOLUME_THUMBWHEEL_BUTTON");
        globalData.fpkCommands.add("AUDIO_SOURCE");
        globalData.fpkCommands.add("ARROW_A_UP_RIGHT");
        globalData.fpkCommands.add("ARROW_A_DOWN_LEFT");
        globalData.fpkCommands.add("ARROW_B_UP_RIGHT");
        globalData.fpkCommands.add("ARROW_B_DOWN_LEFT");
        globalData.fpkCommands.add("PTT_PUSHTOTALK");
        globalData.fpkCommands.add("PTT_CANCEL");
        globalData.fpkCommands.add("ROUT_INFO");
        globalData.fpkCommands.add("HOOK");
        globalData.fpkCommands.add("HANG_UP");
        globalData.fpkCommands.add("OFF_HOOK");
        globalData.fpkCommands.add("LIGHT_ON_OFF");
        globalData.fpkCommands.add("MUTE");
        globalData.fpkCommands.add("JOKER1");
        globalData.fpkCommands.add("JOKER2");
        globalData.fpkCommands.add("LENKRAD_HEIZUNG");
        globalData.fpkCommands.add("TRAVEL_ASSIST");
        Collections.sort(globalData.fpkCommands);

        cbFpk.removeAllItems();
        for(String btn : globalData.fpkCommands) cbFpk.addItem(btn);
        cbFpk.setSelectedIndex(0);
        cbFpk2.removeAllItems();
        for(String btn : globalData.fpkCommands) cbFpk2.addItem(btn);
        cbFpk2.setSelectedIndex(0);

    }

}
