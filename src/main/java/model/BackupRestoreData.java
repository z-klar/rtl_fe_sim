package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dto.TestrackDTO;
import dto.UserDto;
import dto.groups.LabDetailDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class BackupRestoreData {
    private List<UserDto> users;
    private List<TestrackDTO> racks;
    private List<LabDetailDTO> labs;

}
