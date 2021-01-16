package dto.janus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JanusSessionsResponseDTO {
    private String janus;           // command
    private String transaction;     // transaction ID
    private String [] sessions;     // session ID's
}
