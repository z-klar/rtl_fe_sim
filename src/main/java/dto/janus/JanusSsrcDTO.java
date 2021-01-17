package dto.janus;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JanusSsrcDTO {
    private long audio;
    private long video;
    @JsonAlias("audio-peer")
    private long audio_peer;
    @JsonAlias("video-peer")
    private long video_peer;

}
