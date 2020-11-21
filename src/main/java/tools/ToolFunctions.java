package tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.GlobalData;
import common.RestCallOutput;
import commonEnum.DisplayType;
import dto.TestrackDTO;
import dto.TestrackDisplayDTO;
import javafx.scene.control.ComboBox;
import service.RestCallService;

import javax.swing.*;
import java.util.Iterator;
import java.util.Map;

public class ToolFunctions {

    private GlobalData globalData;
    private RestCallService restService;

    public ToolFunctions(GlobalData gd,
                         RestCallService rs) {
         this.globalData = gd;
         this.restService = rs;
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
                dlmRackData.addElement("    Version:  " + disp.getVersion());
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






}
