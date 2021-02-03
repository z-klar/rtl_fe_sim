package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.GlobalData;
import common.JsonProcessing;
import common.RestCallOutput;
import dto.AccessTokenDto;
import dto.TestrackDTO;
import dto.UserDto;
import dto.groups.LabDetailDTO;
import dto.groups.UserDetailPerLabDTO;
import dto.janus.*;
import tables.JanusCommonTable;
import tables.JanusOverviewTable;
import tools.JanusTools;
import tools.ToolFunctions;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class LabService {
    private JsonProcessing jsonProcessing;
    private DefaultListModel<String> lbModel;

    private GlobalData globalData;

    /**
     * #########################################################################
     * @param dlm
     * @param globalData
     */
    public LabService(DefaultListModel<String> dlm, GlobalData globalData) {
        lbModel = dlm;
        jsonProcessing = new JsonProcessing(dlm);
        this.globalData = globalData;
    }

    /**
     **************************************************************************
     * @param Method
     * @param Properties
     * @param TxData
     * @param surl
     * @return
     */
    public RestCallOutput SendRestApiRequest(String Method, Map<String,String> Properties,
                                             String TxData, String surl) {
        RestCallOutput ro = new RestCallOutput();
        try {
            URL url = new URL(surl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            if(TxData != null) con.setDoOutput(true);
            con.setRequestMethod(Method);
            for (Map.Entry<String,String> entry : Properties.entrySet())
                con.setRequestProperty(entry.getKey(), entry.getValue());

            if(TxData != null) {
                byte[] out = TxData.getBytes(StandardCharsets.UTF_8);
                OutputStream os = con.getOutputStream();
                os.write(out);
                os.flush();
            }
            //---------------------- send data ---------------------
            ro.setResultCode(con.getResponseCode());

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                ro.setDataMsg(content.toString());
                in.close();
            }
            catch(Exception ex) {
                ro.setDataMsg("--- No data ---");
            }
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                ro.setErrorMsg(content.toString());
                in.close();
            }
            catch(Exception ex) {
                ro.setErrorMsg("--- No Error ---");
            }
            con.disconnect();
            return(ro);
        }
        catch(ConnectException ex) {
            ro.setResultCode(1000);
            ro.setErrorMsg("ConnectException: " + ex.getMessage());
            return(ro);
        }
        catch(UnknownHostException ex) {
            ro.setResultCode(1000);
            ro.setErrorMsg("UnknownHostException: " + ex.getMessage());
            return(ro);
        }
        catch(Exception ex) {
            ro.setErrorMsg("Exception: " + ex.getMessage());
            return(ro);
        }
    }

    /**
     *
     * @param url
     * @return
     */
    public RestCallOutput getAllLabs(String url) {
        Map<String, String> props = null;
        String jsonString = "";
        AccessTokenDto acc = globalData.token;
        String token = acc.getToken();
        props = new HashMap<>();
        props.put("Authorization", "Bearer " + token);
        String surl = url + "/lab/";
        RestCallOutput ro = SendRestApiRequest("GET", props, null, surl);
        if(ro.getResultCode() < 300) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<LabDetailDTO> listLabs = mapper.readValue(ro.getDataMsg(), new TypeReference<List<LabDetailDTO>>() {
                });
                ro.setOutputData(listLabs);
            }
            catch(Exception ex) {
               ro.setResultCode(1001);
               ro.setErrorMsg(ex.getMessage());
            }
        }
        return ro;
    }

    /**
     *
     * @param labId
     * @return
     */
    public RestCallOutput getUsersForLab(int labId) {
        Map<String, String> props = null;
        String jsonString = "";
        String token = globalData.token.getToken();
        props = new HashMap<>();
        props.put("Authorization", "Bearer " + token);

        String surl = String.format("http://%s:%s/lab/%d/users",
                                globalData.getBeIP(), globalData.getBePort(), labId);
        RestCallOutput ro = SendRestApiRequest("GET", props, null, surl);
        if(ro.getResultCode() < 300) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<UserDetailPerLabDTO> listUsers = mapper.readValue(ro.getDataMsg(), new TypeReference<List<UserDetailPerLabDTO>>() {
                });
                ro.setOutputData(listUsers);
            }
            catch(Exception ex) {
                ro.setResultCode(1001);
                ro.setErrorMsg(ex.getMessage());
            }
        }
        return ro;
    }

    public RestCallOutput getRacksForLab(int labId) {
        Map<String, String> props = null;
        String jsonString = "";
        String token = globalData.token.getToken();
        props = new HashMap<>();
        props.put("Authorization", "Bearer " + token);

        String surl = String.format("http://%s:%s/lab/%d/testracks",
                globalData.getBeIP(), globalData.getBePort(), labId);
        RestCallOutput ro = SendRestApiRequest("GET", props, null, surl);
        if(ro.getResultCode() < 300) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<TestrackDTO> listRacks = mapper.readValue(ro.getDataMsg(), new TypeReference<List<TestrackDTO>>() {
                });
                ro.setOutputData(listRacks);
            }
            catch(Exception ex) {
                ro.setResultCode(1001);
                ro.setErrorMsg(ex.getMessage());
            }
        }
        return ro;
    }

    /**
     *
     * @param rack TestrackDTO with updated infos
     * @return
     */
    public RestCallOutput UpdateTestrack(TestrackDTO rack) {
        RestCallOutput ro = new RestCallOutput();
        Map<String, String> props = null;
        String token = globalData.token.getToken();
        String jsonString = "";
        try {
            props = new HashMap<>();
            props.put("Content-Type", "application/json");
            props.put("Authorization", "Bearer " + token);

            ObjectMapper mapper = new ObjectMapper();
            jsonString = mapper.writeValueAsString(rack);
        }
        catch (Exception ex) {
            ro.setResultCode(1001);
            ro.setErrorMsg(ex.getMessage());
            return ro;
        }
        String surl = String.format("http://%s:%s/testrack/%d",
                globalData.getBeIP(), globalData.getBePort(), rack.getId());

        ro = SendRestApiRequest("PUT", props, jsonString, surl);
        return ro;
    }
    /**
     *
     * @param assign
     * @return
     */
    public RestCallOutput AddUserToLab(UserDetailPerLabDTO assign, String labId) {
        RestCallOutput ro = new RestCallOutput();
        Map<String, String> props = null;
        String token = globalData.token.getToken();
        String jsonString = "";
        try {
            props = new HashMap<>();
            props.put("Content-Type", "application/json");
            props.put("Authorization", "Bearer " + token);

            ObjectMapper mapper = new ObjectMapper();
            jsonString = mapper.writeValueAsString(assign);
        }
        catch (Exception ex) {
            ro.setResultCode(1001);
            ro.setErrorMsg(ex.getMessage());
            return ro;
        }
        String surl = String.format("http://%s:%s/lab/%s/users",
                globalData.getBeIP(), globalData.getBePort(), labId);

        ro = SendRestApiRequest("POST", props, jsonString, surl);
        return ro;
    }
    /**
     *
     * @param userId
     * @param labId
     * @return
     */
    public RestCallOutput RemoveUserFromLab(String userId, String labId) {
        RestCallOutput ro = new RestCallOutput();
        String token = globalData.token.getToken();
        Map<String, String> props = null;
        props = new HashMap<>();
        props.put("Authorization", "Bearer " + token);
        String surl = String.format("http://%s:%s/lab/%s/users/%s",
                globalData.getBeIP(), globalData.getBePort(), labId, userId);

        ro = SendRestApiRequest("DELETE", props, null, surl);
        return ro;
    }

    /**
     *
     * @param labId
     * @return
     */
    public RestCallOutput removeLab(int labId) {
        Map<String, String> props = null;
        String jsonString = "";
        AccessTokenDto acc = globalData.token;
        String token = acc.getToken();
        props = new HashMap<>();
        props.put("Authorization", "Bearer " + token);
        String surl = String.format("http://%s:%s/lab/%d",
                globalData.getBeIP(), globalData.getBePort(), labId);
        RestCallOutput ro = SendRestApiRequest("DELETE", props, null, surl);
        return ro;
    }

    /**
     *
     * @param labName
     * @return
     */
    public RestCallOutput createNewLab(String labName) {
        Map<String, String> props = null;
        String jsonString = "";
        AccessTokenDto acc = globalData.token;
        String token = acc.getToken();
        props = new HashMap<>();
        props.put("Authorization", "Bearer " + token);
        props.put("Content-Type", "application/json");

        String surl = String.format("http://%s:%s/lab/",
                globalData.getBeIP(), globalData.getBePort());

        jsonString = String.format("{ \"name\" : \"%s\" }", labName);
        RestCallOutput ro = SendRestApiRequest("POST", props, jsonString, surl);
        return ro;
    }
}
