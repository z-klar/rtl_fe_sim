package dto.janus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
