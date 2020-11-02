package dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RelayDefinitionDTO {

    private Long id;
    private int position;
    private String name;
    private String type;
    private Boolean valueeditable;
}
