package common;

import dto.AccessTokenDto;
import dto.MmDevice;
import dto.TestrackDTO;
import dto.UserDto;
import dto.groups.LabDetailDTO;
import lombok.Data;
import model.Lab;
import model.LoggerRecord;

import java.util.ArrayList;
import java.util.List;

@Data
public class GlobalData {

    public AccessTokenDto token;
    public List<UserDto> users;
    public List<TestrackDTO> testracks;
    public List<MmDevice> mmDevices;
    public List<LabDetailDTO> labs;
    public List<String> abtCommands;
    public List<String> fpkCommands;

    public final String TRANSACTION_ID = "abcdef";

    private ArrayList<LoggerRecord> loggerRecords = new ArrayList<>();

    private String BeIP;
    private String BePort;
    private int LastSelectedLabId;
    private int LastSelectedTestrackId;

    public void InitGlobalData() {
        token = null;
        users = new ArrayList<UserDto>();
        testracks = new ArrayList<TestrackDTO>();
        labs = new ArrayList<LabDetailDTO>();
    }

}
