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
public class JanusHandleFlagsDTO {
    @JsonAlias("got-offer")
    private boolean got_offer;
    @JsonAlias("got-answer")
    private boolean got_answer;
    private boolean negotiated;
    @JsonAlias("processing-offer")
    private boolean processing_offer;
    private boolean starting;
    @JsonAlias("ice-restart")
    private boolean ice_restart;
    private boolean ready;
    private boolean stopped;
    private boolean alert;
    private boolean trickle;
    @JsonAlias("all-trickles")
    private boolean all_trickles;
    @JsonAlias("resend-trickles")
    private boolean resend_trickles;
    @JsonAlias("trickle_synced")
    private boolean trickle_synced;
    @JsonAlias("data-channels")
    private boolean data_channels;
    @JsonAlias("has-audio")
    private boolean has_audio;
    @JsonAlias("has-video")
    private boolean has_video;
    @JsonAlias("new-datachan-sdp")
    private boolean new_datachan_sdp;
    @JsonAlias("rfc4588-rtx")
    private boolean rfc4588_rtx;
    private boolean cleaning;
    private boolean e2ee;
}
