package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import commonEnum.DisplayType;
import commonEnum.TestrackAvailability;
import commonEnum.TestrackPlatform;
import commonEnum.TestrackVehicle;
import lombok.Data;
import model.Lab;

import java.util.HashSet;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestrackDTO {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String vin;
    private Lab lab;
    private TestrackVehicle vehicle;
    private TestrackAvailability availability;
    private NetworkDTO network;
    private Set<RelayDefinitionDTO> relayDefinitions = new HashSet<>();
    private Set<TestrackDisplayDTO> testrackDisplays = new HashSet<>();

}
