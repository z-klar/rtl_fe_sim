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
    private long video_bytes;
    private long data_bytes;
    private int audio_nacks;
    private int video_nacks;
    private int audio_bytes_lastsec;
    private int video_bytes_lastsec;
}
