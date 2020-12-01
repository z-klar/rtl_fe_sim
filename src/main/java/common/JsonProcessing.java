package common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.MmDevice;

import javax.swing.*;
import java.io.DataInput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonProcessing {

    private DefaultListModel<String> dlmLog;

    public JsonProcessing(DefaultListModel<String> dlm) {
        this.dlmLog = dlm;
    }

    public List<MmDevice> ParseDeviceList(String JsonString) {
        List<MmDevice> devices = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(JsonString);
            Iterator<Map.Entry<String, JsonNode>> root = actualObj.fields();
            while (root.hasNext()) {
                Map.Entry<String, JsonNode> entry = root.next();
                String fieldName = entry.getKey();
                //ObjectMapper mapper2 = new ObjectMapper();
                MmDevice dev = mapper.treeToValue(entry.getValue(), MmDevice.class);
                dlmLog.addElement(" - Field: " + fieldName + "  NAME:" + dev.getName() + "   PORT:" + dev.getPortIncomingVideo());
                devices.add(dev);
            }
            return (devices);
        } catch (Exception ex) {
            dlmLog.addElement("Error: " + ex.getMessage());
            return (null);
        }
    }
    /**
     *
     */
    public ArrayList<String> ParseJsonObject(String JsonString) {
        String spom;
        ArrayList<String> output = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(JsonString);
            Iterator<Map.Entry<String, JsonNode>> root = actualObj.fields();
            while(root.hasNext()) {
                Map.Entry<String, JsonNode> entry = root.next();
                spom = String.format("%-23s %s", entry.getKey(), entry.getValue().asText());
                output.add(spom);
            }
            return(output);
        }
        catch(Exception ex) {
            output.add("Error: " + ex.getMessage());
            return(output);
        }
    }


}
