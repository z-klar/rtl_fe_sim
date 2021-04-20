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
public class JanusRtcpStatsIndDTO {
    private int base;
    private int rtt;
    private int lost;
    @JsonAlias("lost-by-remote")
    private int lost_by_remote;
    @JsonAlias("jitter-local")
    private int jitter_local;
    @JsonAlias("jitter-remote")
    private int jitter_remote;
    @JsonAlias("in-link-quality")
    private int in_link_quality;
    @JsonAlias("in-media-link-quality")
    private int in_media_link_quality;
    @JsonAlias("out-link-quality")
    private int out_link_quality;
    @JsonAlias("out-media-link-quality")
    private int out_media_link_quality;

    public ArrayList<String> toStringArray(int offset) {
        ArrayList<String> al = new ArrayList<>();
        String format = String.format("%%%ds%%25s:%%s", offset);
        al.add(String.format(format, " ", "---------------- RTCP Stats INDIVIDUAL -----------", ""));
        al.add(String.format(format, " ", "base", base));
        al.add(String.format(format, " ", "rtt", rtt));
        al.add(String.format(format, " ", "lost", lost));
        al.add(String.format(format, " ", "lost_by_remote", lost_by_remote));
        al.add(String.format(format, " ", "jitter_local", jitter_local));
        al.add(String.format(format, " ", "jitter_remote", jitter_remote));
        al.add(String.format(format, " ", "in_link_quality", in_link_quality));
        al.add(String.format(format, " ", "in_media_link_quality", in_media_link_quality));
        al.add(String.format(format, " ", "out_link_quality", out_link_quality));
        al.add(String.format(format, " ", "out_media_link_quality", out_media_link_quality));
        al.add(String.format(format, " ", "...................................................", ""));
        return al;
    }


}
