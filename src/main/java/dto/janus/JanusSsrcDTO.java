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
public class JanusSsrcDTO {
    private long audio;
    private long video;
    @JsonAlias("audio-peer")
    private long audio_peer;
    @JsonAlias("video-peer")
    private long video_peer;

    public ArrayList<String> toStringArray(int offset) {
        ArrayList<String> al = new ArrayList<>();
        String format = String.format("%%%ds%%25s:%%s", offset);
        al.add(String.format(format, " ", "------------------- SSRC --------------------", ""));
        al.add(String.format(format, " ", "audio", audio));
        al.add(String.format(format, " ", "video", video));
        al.add(String.format(format, " ", "audio_peer", audio_peer));
        al.add(String.format(format, " ", "video_peer", video_peer));
        al.add(String.format(format, " ", "..............................................", ""));
        return al;
    }


}
