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
public class JanusStreamDTO {
    private int id;
    private int ready;
    private boolean disabled;
    private JanusSsrcDTO ssrc;
    private JanusStreamDirectionDTO direction;
    private JanusStreamComponentDTO [] components;
    @JsonAlias("nack-que-ms")
    private int nack_que_ms;
    private JanusRtcpStatsDTO rtcp_stats;

    public ArrayList<String> toStringArray(int offset, int index) {
        ArrayList<String> al = new ArrayList<>();
        String format = String.format("%%%ds%%25s:%%s", offset);
        String format2 = String.format("%%%ds******************************************** Stream[%d] ********************************************", offset, index);
        String format3 = String.format("%%%ds........................................... Stream[%d] END .........................................", offset, index);
        al.add(String.format(format2, " "));
        al.add(String.format(format, " ", "id", id));
        al.add(String.format(format, " ", "ready", ready));
        al.add(String.format(format, " ", "disabled", disabled));
        al.add(String.format(format, " ", "nack_que_ms", nack_que_ms));
        al.addAll(ssrc.toStringArray(offset + 5));
        al.addAll(direction.toStringArray(offset + 5));
        al.addAll(rtcp_stats.toStringArray(offset+5));
        for(int i=0; i<components.length; i++)
            al.addAll(components[i].toStringArray(offset+5, i));
        al.add(String.format(format3, " "));
        return al;
    }


}
