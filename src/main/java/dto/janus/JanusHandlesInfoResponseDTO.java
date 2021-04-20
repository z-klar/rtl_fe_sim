package dto.janus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JanusHandlesInfoResponseDTO {
    private String janus;                  // command
    private String session_id;             // session
    private String transaction;            // transaction ID
    private String handle_id;              // handle
    private JanusHandleInfoDTO info;       // info
}
