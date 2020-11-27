package dto;

import lombok.Data;

import java.util.*;

@Data
public class ConfigurationData {
    private List<String> hostUrls;

    public ConfigurationData() {
        hostUrls = new ArrayList<String>();
    }
}
