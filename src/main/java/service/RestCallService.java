package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.RestCallOutput;
import dto.*;
import tools.ToolFunctions;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class RestCallService {

    private String BeIp;
    private int BePort;
    private int SignalPort;
    private DefaultListModel dlmUserLog;



    public RestCallService(String ip, int port,
                           DefaultListModel dlm, int sig) {
        this.BeIp = ip;
        this.BePort = port;
        this.dlmUserLog = dlm;
        this.SignalPort = sig;
    }

    public void UpdateUrl(String url, int port) {
        BeIp = url;
        BePort = port;
    }

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

    public RestCallOutput getLoginToken(String username, String password, boolean verbose) {
        if(verbose) dlmUserLog.addElement("Getting access token: USR=" + username + "  PWD=" + password);
        String surl = "http://" + BeIp + ":" + BePort + "/user/token";
        if(verbose) dlmUserLog.addElement("URL: " + surl);

        Map<String, String> props = new HashMap<>();
        props.put("Content-Type", "application/x-www-form-urlencoded");

        try {
            Map<String, String> arguments = new HashMap<>();
            arguments.put("username", username);
            arguments.put("password", password); // This is a fake password obviously
            StringJoiner sj = new StringJoiner("&");
            for (Map.Entry<String, String> entry : arguments.entrySet())
                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(entry.getValue(), "UTF-8"));
            RestCallOutput ro = SendRestApiRequest("POST", props, sj.toString(), surl);
            if(ro.getResultCode() > 299) return ro;
            //Creating the ObjectMapper object
            ObjectMapper mapper = new ObjectMapper();
            //Converting the JSONString to POJO
            AccessTokenDto token = mapper.readValue(ro.getDataMsg(), AccessTokenDto.class);
            ro.setOutputData(token);
            return (ro);
        }
        catch(Exception ex) {
            RestCallOutput ro = new RestCallOutput();
            ro.setResultCode(1001);
            ro.setErrorMsg(ex.getMessage());
            return(ro);
        }
    }

    public RestCallOutput readAllUsers(String token, boolean verbose ) {
        if(verbose) dlmUserLog.addElement("Getting user lisr ....");
        String surl = "http://" + BeIp + ":" + BePort + "/user/";
        if(verbose) dlmUserLog.addElement("URL: " + surl);

        RestCallOutput ro = new RestCallOutput();
        try {

            Map<String, String> props = new HashMap<>();
            props.put("Accept", "*/*");
            props.put("Authorization", "Bearer " + token);
            ro = SendRestApiRequest("GET", props, null, surl);

            ObjectMapper mapper = new ObjectMapper();
            List<UserDto> listUsers = mapper.readValue(ro.getDataMsg(), new TypeReference<List<UserDto>>(){});
            ro.setOutputData(listUsers);
            return (ro);
        }
        catch(Exception ex) {
            return(ro);
        }
    }

    /**
     *
     * @param token
     * @param verbose
     * @return
     */
    public RestCallOutput readUserProfile(String token, boolean verbose ) {
        if(verbose) dlmUserLog.addElement("Getting user profile ....");
        String surl = "http://" + BeIp + ":" + BePort + "/user/profile";
        if(verbose) dlmUserLog.addElement("URL: " + surl);

        RestCallOutput ro = new RestCallOutput();
        try {
            Map<String, String> props = new HashMap<>();
            props.put("Accept", "*/*");
            props.put("Authorization", "Bearer " + token);
            ro = SendRestApiRequest("GET", props, null, surl);
            return (ro);
        }
        catch(Exception ex) {
            return(ro);
        }
    }
    /**
     *
     * @param token
     * @param verbose
     * @return
     */
    public RestCallOutput readAllTestracks(String token, boolean verbose ) {
        if(verbose) dlmUserLog.addElement("Getting testrack list ....");
        String surl = "http://" + BeIp + ":" + BePort + "/testrack/";
        if(verbose) dlmUserLog.addElement("URL: " + surl);
        RestCallOutput ro = new RestCallOutput();
        try {
            Map<String, String> props = new HashMap<>();
            props.put("Accept", "*/*");
            props.put("Authorization", "Bearer " + token);
            ro = SendRestApiRequest("GET", props, null, surl);
            // API returns pageable result:
            // {  "content" : [ testrack_data],
            //   "pageable" : .....
            //   further elements ...
            // }
            ObjectMapper mapper = new ObjectMapper();
            PageableRacksDTO racks = mapper.readValue(ro.getDataMsg(), PageableRacksDTO.class);
            ro.setOutputData(racks.getContent());
            return (ro);
        }
        catch(Exception ex) {
            ro.AddErrorText("Exception: " + ex.getClass().toString());
            ro.AddErrorText("Message: " + ex.getMessage());
            ro.setResultCode(1001);
            return(ro);
        }
    }

    public RestCallOutput createNewUser(UserDto user, String token, boolean verbose) {
        if(verbose) dlmUserLog.addElement("Creating new user: USR=" + user.getFirstName() + " " + user.getLastName());
        String surl = "http://" + BeIp + ":" + BePort + "/user/";
        if(verbose) dlmUserLog.addElement("URL: " + surl);

        try {
            Map<String, String> props = new HashMap<>();
            props.put("Content-Type", "application/json");
            props.put("Authorization", "Bearer " + token);
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(user);
            RestCallOutput ro = SendRestApiRequest("POST", props, jsonString, surl);
            return(ro);
        }
        catch(Exception ex) {
            return(null);
        }
    }

    public RestCallOutput deleteUser(String userId, String token, boolean verbose) {
        if(verbose) dlmUserLog.addElement("Deleting user: UsrId=" + userId);
        String surl = "http://" + BeIp + ":" + BePort + "/user/" + userId;
        if(verbose) dlmUserLog.addElement("URL: " + surl);

        try {
            Map<String, String> props = new HashMap<>();
            props.put("Authorization", "Bearer " + token);
            RestCallOutput ro = SendRestApiRequest("DELETE", props, null, surl);
            return(ro);
        }
        catch(Exception ex) {
            return(null);
        }
    }

    public RestCallOutput setPassword(String userId, String token, boolean verbose, String pwd) {
        if(verbose) dlmUserLog.addElement("Setting PWD for user: UsrId=" + userId);
        String surl = "http://" + BeIp + ":" + BePort + "/user/" + userId + "/password";
        if(verbose) dlmUserLog.addElement("URL: " + surl);

        try {
            Map<String, String> props = new HashMap<>();
            props.put("Authorization", "Bearer " + token);
            RestCallOutput ro = SendRestApiRequest("PUT", props, pwd, surl);
            return(ro);
        }
        catch(Exception ex) {
            return(null);
        }
    }

    public RestCallOutput sendHeartbeat(String rackId, String token, boolean verbose) {
        if(verbose) dlmUserLog.addElement("Sending HeartBeat Tick  to " + rackId);
        String surl = "http://" + BeIp + ":" + BePort + "/testrack/" + rackId + "/heartbeat";
        if(verbose) dlmUserLog.addElement("URL: " + surl);

        try {
            Map<String, String> props = new HashMap<>();
            props.put("Authorization", "Bearer " + token);

            RestCallOutput ro = SendRestApiRequest("POST", props, null, surl);
            return(ro);
        }
        catch(Exception ex) {
            return(null);
        }
    }

    public RestCallOutput sendHandOverRack(String rackId, String token, boolean verbose) {
        if(verbose) dlmUserLog.addElement("Sending HandOver request  to " + rackId);
        String surl = "http://" + BeIp + ":" + BePort + "/testrack/" + rackId + "/control";
        if(verbose) dlmUserLog.addElement("URL: " + surl);

        RestCallOutput ro = new RestCallOutput();
        try {
            Map<String, String> props = new HashMap<>();
            props.put("Authorization", "Bearer " + token);

            ro = SendRestApiRequest("POST", props, null, surl);
            return(ro);
        }
        catch(Exception ex) {
            return(ro);
        }
    }

    public RestCallOutput createNewRack(TestrackDTO rack, String token, boolean verbose) {
        if(verbose) dlmUserLog.addElement("Creating new rack: ");
        String surl = "http://" + BeIp + ":" + BePort + "/testrack/";
        if(verbose) dlmUserLog.addElement("URL: " + surl);

        try {

            Map<String, String> props = new HashMap<>();
            props.put("Content-Type", "application/json");
            props.put("Authorization", "Bearer " + token);

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(rack);

            RestCallOutput ro = SendRestApiRequest("POST", props, jsonString, surl);
            ro.setInfoMsg(jsonString);
            return(ro);
        }
        catch(Exception ex) {
            return(null);
        }
    }

    public RestCallOutput updateTestrack(TestrackDTO rack, String token, boolean verbose) {
        if(verbose) dlmUserLog.addElement("Updating rack: ");
        String surl = "http://" + BeIp + ":" + BePort + "/testrack/" + rack.getId() ;
        if(verbose) dlmUserLog.addElement("URL: " + surl);

        try {

            Map<String, String> props = new HashMap<>();
            props.put("Content-Type", "application/json");
            props.put("Authorization", "Bearer " + token);

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(rack);
            if(verbose) ToolFunctions.logSplit(dlmUserLog, jsonString, 120, "Update Testrack");

            RestCallOutput ro = SendRestApiRequest("PUT", props, jsonString, surl);
            ro.setInfoMsg(jsonString);
            return(ro);
        }
        catch(Exception ex) {
            return(null);
        }
    }

    public RestCallOutput deleteTestrack(String rackId, String token, boolean verbose) {
        if(verbose) dlmUserLog.addElement("Deleting Rack " + rackId);
        String surl = "http://" + BeIp + ":" + BePort + "/testrack/" + rackId ;
        if(verbose) dlmUserLog.addElement("URL: " + surl);

        Map<String, String> props = new HashMap<>();
        props.put("Authorization", "Bearer " + token);
        RestCallOutput ro = SendRestApiRequest("DELETE", props, null, surl);
        return(ro);
    }

    public RestCallOutput sendVerifyEmail(String userId, String token) {
        dlmUserLog.addElement("Sending verification email to " + userId);
        String surl = "http://" + BeIp + ":" + BePort + "/user/" + userId + "/email";
        dlmUserLog.addElement("URL: " + surl);

        HttpURLConnection con = null;
        int status = 0;
        Map<String, String> props = new HashMap<>();
        props.put("Authorization", "Bearer " + token);
        RestCallOutput ro = SendRestApiRequest("POST", props, null, surl);
        return(ro);
    }

    public RestCallOutput getSystemInfo(String token) {
        String surl = "http://" + BeIp + ":" + BePort + "/sysinfo";
        Map<String, String> props = new HashMap<>();
        props.put("Content-Type", "application/json");
        props.put("Authorization", "Bearer " + token);
        RestCallOutput res = SendRestApiRequest("GET", props,null, surl);
        return res;
    }

    public RestCallOutput getSignalServerInfo() {
        String surl = "http://" + BeIp + ":" + SignalPort + "/devices/heartbeat";
        Map<String, String> props = new HashMap<>();
        props.put("Content-Type", "application/json");
        RestCallOutput res = SendRestApiRequest("GET", props,null, surl);
        return res;
    }

    public RestCallOutput sendAbtCommand(String RackId, String cmd, String token) {
        String surl = "http://" + BeIp + ":" + BePort + "/can/display/";
        surl += RackId + "/execute?command=" + cmd;
        dlmUserLog.addElement("*** sendAbtCommand ***");
        dlmUserLog.addElement(surl);
        Map<String, String> props = new HashMap<>();
        props.put("Content-Type", "application/json");
        props.put("Authorization", "Bearer " + token);
        RestCallOutput res = SendRestApiRequest("GET", props,null, surl);
        return res;
    }

    public RestCallOutput sendFpkCommand(String RackId, String cmd, String token) {
        String surl = "http://" + BeIp + ":" + BePort + "/can/display/";
        surl += RackId + "/executemfl?command=" + cmd;
        dlmUserLog.addElement("*** sendFpkCommand ***");
        dlmUserLog.addElement(surl);
        Map<String, String> props = new HashMap<>();
        props.put("Content-Type", "application/json");
        props.put("Authorization", "Bearer " + token);
        RestCallOutput res = SendRestApiRequest("GET", props,null, surl);
        return res;
    }

    public RestCallOutput sendTouchCommand(String RackId, int x, int y, String token) {
        String surl = "http://" + BeIp + ":" + BePort + "/can/display/";
        surl += RackId + "/press?x=" + x + "&y=" + y + "&displayType=ABT" ;
        dlmUserLog.addElement("*** sendTOUCHCommand ***");
        dlmUserLog.addElement(surl);
        Map<String, String> props = new HashMap<>();
        props.put("Content-Type", "application/json");
        props.put("Authorization", "Bearer " + token);
        RestCallOutput res = SendRestApiRequest("POST", props,null, surl);
        return res;
    }

    public RestCallOutput getAllLinks(String surl, boolean verbose) {
        Map<String, String> props = new HashMap<>();
        props.put("Accept", "*/*");

        try {
            RestCallOutput ro = SendRestApiRequest("GET", props, null, surl);
            return (ro);
        }
        catch(Exception ex) {
            return(null);
        }
    }


}
