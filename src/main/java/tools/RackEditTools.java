package tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.GlobalData;
import common.RestCallOutput;
import commonEnum.DisplayType;
import commonEnum.RtlRoles;
import commonEnum.TestrackVehicle;
import dto.*;
import dto.groups.LabDetailDTO;
import dto.groups.UserDetailPerLabDTO;
import dto.groups.UserDetailPerLabNewDTO;
import model.Lab;
import service.LabService;
import service.RestCallService;
import tables.*;
import tables.labs.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

public class RackEditTools {

    private DefaultListModel<String> dlmLog;
    private GlobalData globalData;
    private JComboBox<String> cbVehicleTypes;
    private JComboBox<String> cbDispTypes;
    private JComboBox<String> cbRelayTypes;
    private JComboBox<String> cbRelayFunctions;
    private JTextField txName, txDescr, txAddress, txIpAddress, txQuidoPort;
    private JTextField txDispWidth, txDispHeight, txRelayPos;
    private JTextField txVin;
    private JTable tblDisplays, tblRelays;
    private boolean verbose = false;
    private RestCallService restCallService;

    private TestrackDTO currTestrack;

    /**
     *
     * @param dlm
     * @param globalData
     * @param cbVehicleTypes
     * @param cbDispTypes
     * @param txName
     * @param txDescr
     * @param txAddress
     * @param txIpAddress
     * @param txQuidoPort
     * @param txDispWidth
     * @param txDispHeight
     * @param txRelayPos
     * @param cbRelayFunctions
     * @param cbRelayTypes
     * @param txVin
     * @param tblDisplays
     * @param tblRelays
     * @param restCallService
     */
    public RackEditTools(DefaultListModel<String> dlm,
                         GlobalData globalData,
                         JComboBox cbVehicleTypes, JComboBox cbDispTypes,
                         JTextField txName, JTextField txDescr, JTextField txAddress,
                         JTextField txIpAddress, JTextField txQuidoPort, JTextField txDispWidth,
                         JTextField txDispHeight, JTextField txRelayPos, JComboBox cbRelayFunctions,
                         JComboBox cbRelayTypes, JTextField txVin,
                         JTable tblDisplays, JTable tblRelays, RestCallService restCallService) {

        this.dlmLog = dlm;
        this.globalData = globalData;
        this.cbVehicleTypes = cbVehicleTypes;
        this.cbDispTypes = cbDispTypes;
        this.txName = txName;
        this.txDescr = txDescr;
        this.txAddress = txAddress;
        this.txIpAddress = txIpAddress;
        this.txQuidoPort = txQuidoPort;
        this.txDispWidth = txDispWidth;
        this.txDispHeight = txDispHeight;
        this.txRelayPos = txRelayPos;
        this.cbRelayFunctions = cbRelayFunctions;
        this.cbRelayTypes = cbRelayTypes;
        this.txVin = txVin;
        this.tblDisplays = tblDisplays;
        this.tblRelays = tblRelays;
        this.restCallService = restCallService;
    }

    /**
     *
     * @param TestrackId
     */
    public void UpdateRackView(int TestrackId) {
        RestCallOutput ro = GetSingleTestrack(TestrackId);
        if(ro.getResultCode() > 299) {
            dlmLog.addElement(ro.getErrorMsg());
            JOptionPane.showMessageDialog(null, "Error reading testrack !");
            return;
        }
        currTestrack = (TestrackDTO) ro.getOutputData();
        UpdateStoredRack();
    }
    /**
     *
     */
    public void UpdateStoredRack() {
        txName.setText(currTestrack.getName());
        txAddress.setText(currTestrack.getAddress());
        txDescr.setText(currTestrack.getDescription());
        txIpAddress.setText(currTestrack.getNetwork().getIp());
        txQuidoPort.setText(currTestrack.getNetwork().getQuidoPort().toString());
        txVin.setText(currTestrack.getVin());
        updateCombobox(cbVehicleTypes, currTestrack.getVehicle().toString());

        Vector<RelayTableRow> rows = new Vector<>();
        for(RelayDefinitionDTO r : currTestrack.getRelayDefinitions()) {
            rows.add(new RelayTableRow(r.getPosition(), r.getName(), r.getType()));
        }
        tblRelays.setModel(new RelayTableModel(rows));

        Vector<DisplayTableRow> rows2 = new Vector<>();
        for(TestrackDisplayDTO r : currTestrack.getTestrackDisplays()) {
            rows2.add(new DisplayTableRow(r.getType().toString(), r.getWidth(), r.getHeight(), r.getMgbport()));
        }
        tblDisplays.setModel(new DisplayTableModel(rows2));
    }
    /**
     *
     */
    public void UpdateTestrack() {
        currTestrack.setName(txName.getText());
        currTestrack.setDescription(txDescr.getText());
        currTestrack.setAddress(txAddress.getText());
        currTestrack.setVin(txVin.getText());
        currTestrack.getNetwork().setIp(txIpAddress.getText());
        currTestrack.getNetwork().setBlackboxPort(Integer.parseInt(txQuidoPort.getText()));
        currTestrack.setVehicle(TestrackVehicle.valueOf(cbVehicleTypes.getSelectedItem().toString()));
        RestCallOutput ro = restCallService.updateTestrack(currTestrack, globalData.token.getToken(), true);
        if(ro.getResultCode() < 300)
            JOptionPane.showMessageDialog(null, "Testrack updated !");
        else
            JOptionPane.showMessageDialog(null, "Error updating testrack - see the LOG !");
    }
    /**
     *
     */
    public void RemoveDisplay() {
        int selectedLine = tblDisplays.getSelectedRow();
        if(selectedLine < 0) {
            JOptionPane.showMessageDialog(null, "No display selected !");
            return;
        }
        String type = (String) tblDisplays.getValueAt(selectedLine, 0);
        RemoveDisplayFromList(type);
        UpdateStoredRack();
    }
    private void RemoveDisplayFromList(String type) {
        for(TestrackDisplayDTO display : currTestrack.getTestrackDisplays()) {
            if(display.getType().toString().equals(type)) {
                currTestrack.getTestrackDisplays().remove(display);
                return;
            }
        }
    }
    /**
     *
     */
    public void RemoveRelay() {
        int selectedLine = tblRelays.getSelectedRow();
        if(selectedLine < 0) {
            JOptionPane.showMessageDialog(null, "No relay selected !");
            return;
        }
        int position = (Integer)tblRelays.getValueAt(selectedLine, 0);
        RemoveRelayFromList(position);
        UpdateStoredRack();
    }
    /**
     *
     * @param position
     */
    private void RemoveRelayFromList(int position) {
        for(RelayDefinitionDTO relay : currTestrack.getRelayDefinitions()) {
            if(relay.getPosition() == position) {
                currTestrack.getRelayDefinitions().remove(relay);
                return;
            }
        }
    }
    /**
     *
     */
    public void AddRelay() {
        int pos;
        try {
            pos = Integer.parseInt(txRelayPos.getText());
        }catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid position number !");
            return;
        }
        String name = cbRelayFunctions.getSelectedItem().toString();
        String type = cbRelayTypes.getSelectedItem().toString();

        RelayDefinitionDTO relay = new RelayDefinitionDTO();
        relay.setName(name);
        relay.setPosition(pos);
        relay.setType(type);
        relay.setValueeditable(true);
        currTestrack.getRelayDefinitions().add(relay);
        UpdateStoredRack();
    }

    /**
     *
     */
    public void AddDisplay() {
        int width, height;
        try {
            width = Integer.parseInt(txDispWidth.getText());
            height = Integer.parseInt(txDispHeight.getText());
        }catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid width / height !");
            return;
        }
        String type = cbDispTypes.getSelectedItem().toString();

        TestrackDisplayDTO display = new TestrackDisplayDTO();
        display.setType(DisplayType.valueOf(type));
        display.setWidth(width);
        display.setHeight(height);
        currTestrack.getTestrackDisplays().add(display);
        UpdateStoredRack();
    }
    /**
     *
     * @param cb
     * @param selectedTopic
     */
    private void updateCombobox(JComboBox cb, String selectedTopic) {
       for(int i=0; i<cb.getItemCount(); i++) {
           String topic = cb.getItemAt(i).toString();
           if(topic.equals(selectedTopic)) cb.setSelectedIndex(i);
       }
    }
    /**
     *
     * @param TestrackId
     * @return
     */
    private RestCallOutput GetSingleTestrack(int TestrackId) {
        String BeIp = globalData.getBeIP();
        String BePort = globalData.getBePort();
        if(verbose) dlmLog.addElement("Getting single testrack ....");
        String surl = "http://" + BeIp + ":" + BePort + "/testrack/" + TestrackId;
        if(verbose) dlmLog.addElement("URL: " + surl);
        RestCallOutput ro = new RestCallOutput();
        try {
            Map<String, String> props = new HashMap<>();
            props.put("Accept", "*/*");
            props.put("Authorization", "Bearer " + globalData.getToken().getToken());
            ro = SendRestApiRequest("GET", props, null, surl);
            ObjectMapper mapper = new ObjectMapper();
            TestrackDTO rack = mapper.readValue(ro.getDataMsg(), TestrackDTO.class);
            ro.setOutputData(rack);
            return (ro);
        }
        catch(Exception ex) {
            ro.AddErrorText("Exception: " + ex.getClass().toString());
            ro.AddErrorText("Message: " + ex.getMessage());
            ro.setResultCode(1001);
            return(ro);
        }
    }


    /**
     *
     * @param Method
     * @param Properties
     * @param TxData
     * @param surl
     * @return
     */
    private RestCallOutput SendRestApiRequest(String Method, Map<String,String> Properties,
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
            ro.setResultCode(1001);
            ro.setErrorMsg("Exception: " + ex.getMessage());
            return(ro);
        }
    }

}
