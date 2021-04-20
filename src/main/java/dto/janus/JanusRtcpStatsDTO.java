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
public class JanusRtcpStatsDTO {
    private JanusRtcpStatsIndDTO audio;
    private JanusRtcpStatsIndDTO video;

    public ArrayList<String> toStringArray(int offset) {
        ArrayList<String> al = new ArrayList<>();
        String format = String.format("%%%ds%%25s:%%s", offset);
        al.add(String.format(format, " ", "-------------- RTCP Stats AUDIO --------------------", ""));
        al.addAll(audio.toStringArray(offset+5));
        al.add(String.format(format, " ", "-------------- RTCP Stats VIDEO --------------------", ""));
        al.addAll(video.toStringArray(offset+5));
        al.add(String.format(format, " ", "...................................................", ""));
        return al;
    }


}
