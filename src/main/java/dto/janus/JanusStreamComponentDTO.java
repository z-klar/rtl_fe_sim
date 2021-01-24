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
public class JanusStreamComponentDTO {
    private int id;
    private String state;
    private long connected;
    @JsonAlias("local-candidates")
    private String [] local_candidates;
    @JsonAlias("remote-candidates")
    private String [] remote_candidates;
    @JsonAlias("selected-pair")
    private String selected_pair;
    private JanusDtlsDTO dtls;
    private JanusStatsDTO in_stats;
    private JanusStatsDTO out_stats;

    public ArrayList<String> toStringArray(int offset, int index) {
        ArrayList<String> al = new ArrayList<>();
        String format = String.format("%%%ds%%25s:%%s", offset);
        String format2 = String.format("%%%ds==================================== StreamComponent[%d] =====================================", offset, index);
        String format3 = String.format("%%%ds.................................. StreamComponent[%d] END ...................................", offset, index);
        al.add(String.format(format2, " "));
        al.add(String.format(format, " ", "id", id));
        al.add(String.format(format, " ", "state", state));
        al.add(String.format(format, " ", "connected", connected));
        al.add(String.format(format, " ", "   local_candidates", " "));
        for(String s : local_candidates) al.add(String.format(format, " ", " ", s));
        al.add(String.format(format, " ", "   remote_candidates", " "));
        for(String s : remote_candidates) al.add(String.format(format, " ", " ", s));
        al.add(String.format(format, " ", "selected_pair", selected_pair));
        al.addAll(dtls.toStringArray(offset+5));
        al.add(String.format(format, " ", "     ------------------- INPUT  -------------------", " "));
        al.addAll(in_stats.toStringArray(offset+5));
        al.add(String.format(format, " ", "     ------------------- OUTPUT  ------------------", " "));
        al.addAll(out_stats.toStringArray(offset+5));
        al.add(String.format(format3, " "));

        return al;
    }

}
