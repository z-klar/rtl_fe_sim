package dto.janus;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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


    public ArrayList<String> toStringArray() {
        ArrayList<String> al = new ArrayList<>();
        al.add(String.format("%25s:%s", "session_id", session_id));
        al.add(String.format("%25s:%s", "session_last_activity", session_last_activity));
        al.add(String.format("%25s:%s", "session_transport", session_transport));
        al.add(String.format("%25s:%s", "handle_id", handle_id));
        al.add(String.format("%25s:%s", "loop_running", loop_running));
        al.add(String.format("%25s:%d", "created", created));
        al.add(String.format("%25s:%d", "current_time", current_time));
        al.add(String.format("%25s:%s", "plugin", plugin));
        al.addAll(plugin_specific.toStringArray(5));
        al.addAll(flags.toStringArray(5));
        al.add(String.format("%25s:%d", "agent_created", agent_created));
        al.add(String.format("%25s:%s", "ice_mode", ice_mode));
        al.addAll(sdps.toStringArray(5));
        al.add(String.format("%25s:%d", "queued_packets", queued_packets));
        for(int i=0; i<streams.length; i++)
            al.addAll(streams[i].toStringArray(5, i));

        return al;
    }

}
