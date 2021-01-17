package dto.janus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JanusHandlesRequestDTO {
    private String janus;           // command
    private String session_id;      // command
    private String transaction;     // transaction ID
    private String admin_secret;    // admin PWD


}
