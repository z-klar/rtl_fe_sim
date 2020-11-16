package dto;

import commonEnum.DisplayType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestrackDisplayDTO {

    private Long id;
    private DisplayType type;
    private int width;
    private int height;
    private int mgbport;
    private String version;
}
