package tables;

public class DevicesTable1 {
    public String ID;
    public String Name;
    public String Description;
    public int IncomingAudio;
    public int IncomingVideo;
    public String HostOutgoingAudio;
    public int PortOutgoingAudio;
    public boolean Active;

    public DevicesTable1(String id, String name, String descr, int inAudio,
                         int inVideo, String hostAudioOut, int portAudioOut, boolean act) {
        ID = id;
        Name = name;
        Description = descr;
        IncomingAudio = inAudio;
        IncomingVideo = inVideo;
        HostOutgoingAudio = hostAudioOut;
        PortOutgoingAudio = portAudioOut;
        Active = act;
    }

    public Object[] toObject() {
        return new Object[]{ID, Name, Description, IncomingAudio,
                            IncomingVideo, HostOutgoingAudio, PortOutgoingAudio, Active};
    }
}
