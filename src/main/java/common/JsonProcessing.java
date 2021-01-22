package common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.MmDevice;
import model.CommonOutput;
import model.LoggerRecord;

import javax.swing.*;
import java.io.DataInput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsonProcessing {

    private final Logger log = LoggerFactory.getLogger(JsonProcessing.class);

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

    /**
     *
     * @param jsonInput
     * @return
     */
    public CommonOutput GetAllLoggers(String jsonInput) {
        CommonOutput out = new CommonOutput();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(jsonInput);
            log.info("Main element: size = " + actualObj.size());

            Iterator<Map.Entry<String, JsonNode>> root = actualObj.fields();
            while(root.hasNext()) {
                Map.Entry<String, JsonNode> entry = root.next();
                String fieldName = entry.getKey();
                log.info("  - Field: " + fieldName);
                if(fieldName.contains("loggers")) ProcessLoggerList(entry.getValue(), out);
            }
            return out;
        }
        catch(Exception ex) {
            out.getErrorMsg().add("Exception: ");
            out.getErrorMsg().add(ex.getMessage());
            return out;
        }
    }
    private void ProcessLoggerList(JsonNode root, CommonOutput out) {
        ArrayList<LoggerRecord> alRes = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> list = root.fields();
        while(list.hasNext()) {
            LoggerRecord lr = new LoggerRecord();
            Map.Entry<String, JsonNode> entry = list.next();
            String fieldName = entry.getKey();
            log.info("  - Logger: " + fieldName);
            lr.setName(fieldName);
            JsonNode node = entry.getValue();
            Iterator<Map.Entry<String, JsonNode>> link = node.fields();
            while(link.hasNext()) {
                Map.Entry<String, JsonNode> detail = link.next();
                log.info("      - " + detail.getKey() + " : " + detail.getValue().asText());
                if(detail.getKey().contains("configured")) lr.setConfiguredLevel(detail.getValue().asText());
                if(detail.getKey().contains("effective")) lr.setEffectiveLevel(detail.getValue().asText());
            }
            alRes.add(lr);
        }
        out.setResult(alRes);
    }


}
