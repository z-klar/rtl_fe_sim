package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.GlobalData;
import common.JsonProcessing;
import common.RestCallOutput;
import dto.janus.*;
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
import java.util.Map;
import java.util.Vector;

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

    /**
     *
     * @return
     */
    public int getJanusOverviewData(Vector<JanusOverviewTable> rows,
                                    String url, String pwd) {
        RestCallOutput ro = getSession(url, pwd);
        if (ro.getResultCode() > 299) {
            JOptionPane.showMessageDialog(null, "Error:\n" + ro.getErrorMsg());
            return -1;
        }
        JanusSessionsResponseDTO resp = JanusTools.getJanusSessionListResp(ro.getDataMsg());
        if(!resp.getJanus().contains("success")) {
            return -2;
        }
        else {
            for(String sess : resp.getSessions()) {
                rows.add(new JanusOverviewTable(sess, "", "", "", "", "",""));
                JanusHandlesResponseDTO handleList = getHandles(url, pwd, sess);
                if(!handleList.getJanus().contains("success")) {
                    return -4;
                }
                else {
                    for(String handle : handleList.getHandles()) {
                        rows.add(new JanusOverviewTable("", handle, "", "", "","", ""));
                        JanusHandlesInfoResponseDTO handleObj = getHandleInfo(url, pwd, sess, handle);
                        if(handleObj.getJanus().contains("error")) {
                            return -5;
                        }
                        else {
                            JanusHandleInfoDTO hi = handleObj.getInfo();
                            String data = "PG=" + hi.getPlugin() + " #stream:" + hi.getStreams().length;
                            if(hi.getStreams().length == 0) {
                                rows.add(new JanusOverviewTable("", "", data, "",
                                                              "", "", ""));
                            }
                            else {
                                for(int i=0; i<hi.getStreams().length; i++) {
                                    JanusStreamDTO stream = hi.getStreams()[i];
                                    String streamId = String.format("%d",stream.getId());
                                    String streamData = String.format("Rdy=%d #comp=%d",
                                            stream.getReady(), stream.getComponents().length);
                                    if(stream.getComponents().length == 0) {
                                        if (i == 0)
                                            rows.add(new JanusOverviewTable("", "", data, streamId,
                                                    streamData, "", ""));
                                        else
                                            rows.add(new JanusOverviewTable("", "", "", streamId,
                                                    streamData, "", ""));
                                    }
                                    else {
                                        for(int j=0; j<stream.getComponents().length; j++) {
                                            JanusStreamComponentDTO comp = stream.getComponents()[j];
                                            String compId = String.format("%d", comp.getId());
                                            String compData = String.format("state=%s video_packets=%d video_nacks=%d",
                                                    comp.getState(), comp.getOut_stats().getVideo_packets(),
                                                    comp.getOut_stats().getVideo_nacks());
                                            if (j == 0)
                                                rows.add(new JanusOverviewTable("", "", data, streamId,
                                                        streamData, compId, compData));
                                            else
                                                rows.add(new JanusOverviewTable("", "", "", streamId,
                                                        streamData, compId, compData));
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        return 0;
    }


}
