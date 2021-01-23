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
public class JanusStreamDirectionDTO {
    @JsonAlias("audio-send")
    private boolean audio_send;
    @JsonAlias("audio-recv")
    private boolean audio_recv;
    @JsonAlias("video-send")
    private boolean video_send;
    @JsonAlias("video-recv")
    private boolean video_recv;

    public ArrayList<String> toStringArray(int offset) {
        ArrayList<String> al = new ArrayList<>();
        String format = String.format("%%%ds%%25s:%%s", offset);
        al.add(String.format(format, " ", "---------------- StreamDirection -------------", ""));
        al.add(String.format(format, " ", "audio_send", audio_send));
        al.add(String.format(format, " ", "audio_recv", audio_recv));
        al.add(String.format(format, " ", "video_send", video_send));
        al.add(String.format(format, " ", "video_recv", video_recv));
        al.add(String.format(format, " ", "..............................................", ""));
        return al;
    }



}
