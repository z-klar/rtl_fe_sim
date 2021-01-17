package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.GlobalData;
import common.JsonProcessing;
import common.RestCallOutput;
import dto.janus.*;
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
import java.util.Map;

public class JanusService {
    private JsonProcessing jsonProcessing;
    private DefaultListModel<String> lbModel;
    private DefaultListModel<String> lbJanus;

    private GlobalData globalData;
    private String TRANSACTION_ID;

    public JanusService(DefaultListModel<String> dlm, GlobalData globalData, DefaultListModel<String> dlmJanus) {
        lbModel = dlm;
        lbJanus = dlmJanus;
        jsonProcessing = new JsonProcessing(dlm);
        this.globalData = globalData;
        TRANSACTION_ID = globalData.TRANSACTION_ID;
    }

    public RestCallOutput getSession(String url, String password) {
        Map<String, String> props = null;
        String jsonString = "";
        try {
            props = new HashMap<>();
            props.put("Content-Type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            JanusSessionsRequestDTO req = new JanusSessionsRequestDTO("list_sessions", "TRANSACTION_ID", password);
            jsonString = mapper.writeValueAsString(req);
        }
        catch (Exception ex) {
        }
        String surl = url;
        RestCallOutput ro = SendRestApiRequest("POST", props, jsonString, surl);
        return ro;
    }

    /**
     *
     * @param url
     * @param password
     * @return
     */
    public JanusHandlesResponseDTO getHandles(String url, String password, String session_id) {
        Map<String, String> props = null;
        String jsonString = "";
        try {
            props = new HashMap<>();
            props.put("Content-Type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            JanusHandlesRequestDTO req = new JanusHandlesRequestDTO("list_handles", "TRANSACTION_ID", session_id, password);
            jsonString = mapper.writeValueAsString(req);
        }
        catch (Exception ex) {
        }
        String surl = url + "/" + session_id;
        RestCallOutput ro = SendRestApiRequest("POST", props, jsonString, surl);
        ToolFunctions.logSplit(lbModel, ro.getDataMsg(), 140, "  getHandles  ");
        JanusHandlesResponseDTO resp = new JanusHandlesResponseDTO();
        if(ro.getResultCode() > 299) {
            resp.setJanus("error");
            resp.setTransaction(ro.getErrorMsg());
        }
        else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                resp = mapper.readValue(ro.getDataMsg(), JanusHandlesResponseDTO.class);
            }
            catch(Exception ex) {
                resp.setJanus("error");
                resp.setTransaction(ex.getMessage());
            }
        }
        return resp;
    }

    public JanusHandlesInfoResponseDTO getHandleInfo(String url, String password,
                                            String session_id, String handle_id) {
        Map<String, String> props = null;
        String jsonString = "";
        try {
            props = new HashMap<>();
            props.put("Content-Type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            JanusHandlesInfoRequestDTO req = new JanusHandlesInfoRequestDTO(
                                                "handle_info", "TRANSACTION_ID",
                                                      session_id, handle_id,  password);
            jsonString = mapper.writeValueAsString(req);
        }
        catch (Exception ex) {
        }
        String surl = url + "/" + session_id + "/" + handle_id;
        RestCallOutput ro = SendRestApiRequest("POST", props, jsonString, surl);
        ToolFunctions.logSplit(lbJanus, ro.getDataMsg(), 140, "   getHandleInfo   ");
        JanusHandlesInfoResponseDTO resp = new JanusHandlesInfoResponseDTO();
        if(ro.getResultCode() > 299) {
            resp.setJanus("error");
            resp.setTransaction(ro.getErrorMsg());
        }
        else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                resp = mapper.readValue(ro.getDataMsg(), JanusHandlesInfoResponseDTO.class);
            }
            catch(Exception ex) {
                resp.setJanus("error");
                resp.setTransaction(ex.getMessage());
            }
        }
        return resp;
    }


    /**
     *
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



}
