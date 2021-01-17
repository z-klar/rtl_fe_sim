package common;

import dto.AccessTokenDto;
import dto.MmDevice;
import dto.TestrackDTO;
import dto.UserDto;

import java.util.List;

public class GlobalData {

    public AccessTokenDto token;
    public List<UserDto> users;
    public List<TestrackDTO> testracks;
    public List<MmDevice> mmDevices;

    public List<String> abtCommands;
    public List<String> fpkCommands;

    public final String TRANSACTION_ID = "abcdef";

}
