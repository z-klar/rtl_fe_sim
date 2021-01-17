package dto.janus;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JanusPluginSpecsDTO {
    private String state;
    private boolean hangingup;
    private boolean started;
    private boolean dataready;
    private boolean paused;
    private boolean stopping;
    private boolean destroyed;
}
