package dto.janus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JanusHandlesResponseDTO {
    private String janus;           // command
    private String session_id;      // command
    private String transaction;     // transaction ID
    private String [] handles;      // session ID's
}
