package tables;

import commonEnum.TestrackAvailability;
import commonEnum.TestrackPlatform;
import dto.NetworkDTO;

public class TestrackTable1 {
    public Long id;
    public String name;
    public String description;
    public String address;
    public TestrackPlatform platform;
    public TestrackAvailability availability;
    public String IpAddr;
    public int videoPort;

    public TestrackTable1(Long id, String name, String decr, String addr,
                          TestrackPlatform platform, TestrackAvailability avail,
                          String ip, int port) {
        this.id = id;
        this.name = name;
        this.description = decr;
        this.address = addr;
        this.platform = platform;
        this.availability = avail;
        this.IpAddr = ip;
        this.videoPort = port;
    }

    public Object[] toObject() {
        return new Object[]{id, name, description, address,
                            platform, availability, IpAddr, videoPort};
    }
}
