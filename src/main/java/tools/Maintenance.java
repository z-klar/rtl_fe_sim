package tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.GlobalData;
import common.RestCallOutput;
import dto.AccessTokenDto;
import dto.UserDto;
import dto.groups.LabDetailDTO;
import dto.groups.LabUserAssignmentDTO;
import service.RestCallService;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Maintenance {
    private JTextField txCheckAssigns;
    private DefaultListModel<String> dlmLog;
    private DefaultListModel<String> dlmPrivateLog;
    private GlobalData globalData;
    private RestCallService restCallService;

    public Maintenance(JTextField txCheckAssigns, DefaultListModel<String> dlmLog,
                       GlobalData globalData, RestCallService restCallService,
                       DefaultListModel<String> dlmPrivateLog) {
        this.txCheckAssigns = txCheckAssigns;
        this.globalData = globalData;
        this.restCallService = restCallService;
        this.dlmLog = dlmLog;
        this.dlmPrivateLog = dlmPrivateLog;
    }

    /**
     *
     */
    public void CheckAssigns() {

        RestCallOutput ro = getAllAssigns();
        if(ro.getResultCode() > 299) {
            JOptionPane.showMessageDialog(null, "Error checking assigns - see the LOG !");
            dlmLog.addElement("CheckAllAssignments:");
            dlmLog.addElement(ro.getErrorMsg());
            return;
        }
        List<LabUserAssignmentDTO> assigns = (List<LabUserAssignmentDTO>)ro.getOutputData();
        int iRes = 0;
        txCheckAssigns.setText("Working ....");
        dlmPrivateLog.clear();
        for(LabUserAssignmentDTO la : assigns) {
            String userId = la.getUserId();
            int labId = la.getLabId();
            int recId = la.getRecordId();
            if(!userExists(userId)) {
                iRes++;
                dlmLog.addElement("Rec #:" + recId +  "  Lab=" + labId + "   User [" + userId + "]  NOT in Keycloak !");
            }
        }
        if(iRes == 0) {
            txCheckAssigns.setText("All assignments are OK");
        }
        else {
            txCheckAssigns.setText("Found " + iRes + " issues - see the logger !");
        }
    }

    /**
     *
     * @param userId
     * @return
     */
    private boolean userExists(String userId) {
        for(UserDto user : globalData.users) {
            if(user.getId().equals(userId)) return true;
        }
        return false;
    }
    /**
     *
     * @return
     */
    public RestCallOutput getAllAssigns() {
        Map<String, String> props = null;
        String token = globalData.token.getToken();
        props = new HashMap<>();
        props.put("Authorization", "Bearer " + token);
        String surl = String.format("http://%s:%s/lab/assignments", globalData.getBeIP(), globalData.getBePort());
        RestCallOutput ro = restCallService.SendRestApiRequest("GET", props, null, surl);
        if(ro.getResultCode() < 300) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<LabUserAssignmentDTO> list = mapper.readValue(ro.getDataMsg(), new TypeReference<List<LabUserAssignmentDTO>>() {
                });
                ro.setOutputData(list);
            }
            catch(Exception ex) {
                ro.setResultCode(1001);
                ro.setErrorMsg(ex.getMessage());
            }
        }
        return ro;
    }
    public void CheckWrongAssigns(boolean DeleteWrong) {
        RestCallOutput ro = getWrongAssigns(DeleteWrong);
        if(ro.getResultCode() > 299) {
            JOptionPane.showMessageDialog(null, "Error checking assigns - see the LOG !");
            dlmLog.addElement("CheckWrongAssignments:");
            dlmLog.addElement(ro.getErrorMsg());
            return;
        }
        dlmPrivateLog.clear();
        List<LabUserAssignmentDTO> assigns = (List<LabUserAssignmentDTO>)ro.getOutputData();
        int iRes = assigns.size();
        for(LabUserAssignmentDTO la : assigns) {
            String userId = la.getUserId();
            int labId = la.getLabId();
            int recId = la.getRecordId();
            dlmPrivateLog.addElement("Rec #:" + recId +  "  Lab=" + labId + "   User [" + userId + "]  NOT in Keycloak !");
        }
        if(iRes == 0) {
            txCheckAssigns.setText("All assignments are OK");
        }
        else {
            txCheckAssigns.setText("Found " + iRes + " issues - see the logger !");
        }
    }
    /**
     *
     * @param DeleteWrong
     * @return
     */
    public RestCallOutput getWrongAssigns(boolean DeleteWrong) {
        Map<String, String> props = null;
        String token = globalData.token.getToken();
        props = new HashMap<>();
        props.put("Authorization", "Bearer " + token);
        String surl = String.format("http://%s:%s/lab/assignments", globalData.getBeIP(), globalData.getBePort());
        if(DeleteWrong) surl += "?delete=true";
        else surl += "?delete=false";
        RestCallOutput ro = restCallService.SendRestApiRequest("POST", props, null, surl);
        if(ro.getResultCode() < 300) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<LabUserAssignmentDTO> list = mapper.readValue(ro.getDataMsg(), new TypeReference<List<LabUserAssignmentDTO>>() {
                });
                ro.setOutputData(list);
            }
            catch(Exception ex) {
                ro.setResultCode(1001);
                ro.setErrorMsg(ex.getMessage());
            }
        }
        return ro;
    }

}
