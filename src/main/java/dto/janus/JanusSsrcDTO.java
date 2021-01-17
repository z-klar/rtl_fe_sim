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
    private int audio;
    private int video;
    @JsonAlias("audio-peer")
    private int audio_peer;
    @JsonAlias("video-peer")
    private int video_peer;

}
