package dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class NetworkDTO {

    private Long id;
    private String ip;
    private Integer blackboxPort;
    private Integer quidoPort;
    private Integer remoteDesktopInternalPort;
    private String remoteDesktopExternalIp;
    private Integer remoteDesktopExternalPort;
    private Integer mgbAbtPort;
    private Integer mgbFpkPort;
    private Integer mgbHudPort;
}
