package dto.janus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JanusSessionsRequestDTO {
    private String janus;           // command
    private String transaction;     // transaction ID
    private String admin_secret;    // admin PWD


}
