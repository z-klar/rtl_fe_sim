package common;

import dto.AccessTokenDto;
import dto.MmDevice;
import dto.TestrackDTO;
import dto.UserDto;
import lombok.Data;
import model.LoggerRecord;

import java.util.ArrayList;
import java.util.List;

@Data
public class GlobalData {

    public AccessTokenDto token;
    public List<UserDto> users;
    public List<TestrackDTO> testracks;
    public List<MmDevice> mmDevices;

    public List<String> abtCommands;
    public List<String> fpkCommands;

    public final String TRANSACTION_ID = "abcdef";

    private ArrayList<LoggerRecord> loggerRecords = new ArrayList<>();

}
