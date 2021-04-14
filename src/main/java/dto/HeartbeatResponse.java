package dto;

import lombok.Data;

@Data
public class HeartbeatResponse {
    private String controlledBy;
    private boolean kicked;
    private String kickMessage;
}
