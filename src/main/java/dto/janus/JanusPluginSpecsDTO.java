package dto.janus;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

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

    public ArrayList<String> toStringArray(int offset) {
        ArrayList<String> al = new ArrayList<>();
        String format = String.format("%%%ds%%25s:%%s", offset);
        al.add(String.format(format, " ", "---------------- PluginSpecs -------------", ""));
        al.add(String.format(format, " ", "state", state));
        al.add(String.format(format, " ", "hangingup", hangingup));
        al.add(String.format(format, " ", "started", started));
        al.add(String.format(format, " ", "dataready", dataready));
        al.add(String.format(format, " ", "paused", paused));
        al.add(String.format(format, " ", "stopping", stopping));
        al.add(String.format(format, " ", "destroyed", destroyed));
        al.add(String.format(format, " ", "..........................................", ""));
        return al;
    }

}
