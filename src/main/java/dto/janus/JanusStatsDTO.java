package dto.janus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JanusStatsDTO {
    private long audio_bytes;
    private long audio_packets;
    private long video_bytes;
    private long video_packets;
    private long data_bytes;
    private boolean do_audio_nacks;
    private boolean do_video_nacks;
    private long audio_nacks;
    private long video_nacks;
    private long audio_bytes_lastsec;
    private long video_bytes_lastsec;

    public ArrayList<String> toStringArray(int offset) {
        ArrayList<String> al = new ArrayList<>();
        String format = String.format("%%%ds%%25s:%%s", offset);
        al.add(String.format(format, " ", "------------------ Statistics ----------------", ""));
        al.add(String.format(format, " ", "audio_bytes", audio_bytes));
        al.add(String.format(format, " ", "audio_packets", audio_packets));
        al.add(String.format(format, " ", "video_bytes", video_bytes));
        al.add(String.format(format, " ", "video_packets", video_packets));
        al.add(String.format(format, " ", "data_bytes", data_bytes));
        al.add(String.format(format, " ", "do_audio_nacks", do_audio_nacks));
        al.add(String.format(format, " ", "do_video_nacks", do_video_nacks));
        al.add(String.format(format, " ", "audio_nacks", audio_nacks));
        al.add(String.format(format, " ", "video_nacks", video_nacks));
        al.add(String.format(format, " ", "audio_bytes_lastsec", audio_bytes_lastsec));
        al.add(String.format(format, " ", "video_bytes_lastsec", video_bytes_lastsec));
        al.add(String.format(format, " ", "...............................................", ""));
        return al;
    }


}
