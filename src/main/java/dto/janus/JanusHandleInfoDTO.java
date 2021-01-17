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
public class JanusHandleInfoDTO {
    private String session_id;
    private String session_last_activity;
    private String session_transport;
    private String handle_id;
    @JsonAlias("loop-running")
    private boolean loop_running;
    private long created;
    private long current_time;
    private String plugin;
    private JanusPluginSpecsDTO plugin_specific;
    private JanusHandleFlagsDTO flags;
    @JsonAlias("agent-created")
    private long agent_created;
    @JsonAlias("ice-mode")
    private String ice_mode;
    @JsonAlias("ice-role")
    private String ice_role;
    private JanusSdpDTO sdps;
    private int queued_packets;
    private JanusStreamDTO [] streams;
}
