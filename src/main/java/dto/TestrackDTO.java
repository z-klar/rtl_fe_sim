package dto;

import commonEnum.DisplayType;
import commonEnum.TestrackAvailability;
import commonEnum.TestrackPlatform;
import commonEnum.TestrackVehicle;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TestrackDTO {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String vin;
    private TestrackVehicle vehicle;
    private TestrackAvailability availability;
    private NetworkDTO network;
    private Set<RelayDefinitionDTO> relayDefinitions = new HashSet<>();
    private Set<TestrackDisplayDTO> testrackDisplays = new HashSet<>();

}
