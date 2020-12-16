package tables;

import commonEnum.TestrackAvailability;
import commonEnum.TestrackPlatform;
import commonEnum.TestrackVehicle;
import dto.NetworkDTO;

public class TestrackTable1 {
    public Long id;
    public String name;
    public String description;
    public String address;
    public TestrackVehicle vehicle;
    public TestrackAvailability availability;
    public String IpAddr;
    public int videoPort;
    public  String vin;

    public TestrackTable1(Long id, String name, String decr, String addr,
                          TestrackVehicle vehicle, TestrackAvailability avail,
                          String ip, int port, String vin) {
        this.id = id;
        this.name = name;
        this.description = decr;
        this.address = addr;
        this.vehicle = vehicle;
        this.availability = avail;
        this.IpAddr = ip;
        this.videoPort = port;
        this.vin = vin;
    }

    public Object[] toObject() {
        return new Object[]{id, name, description, address,
                            vehicle, availability, IpAddr, videoPort, vin};
    }
}
