package dto;

import lombok.Data;

@Data
public class MmDevice {

    private String id;
    private String name;
    private String description;
    private int portIncomingAudio;
    private int portIncomingVideo;
    private String hostOutgoingAudio;
    private int portOutgoingAudio;
    private boolean active;
}
