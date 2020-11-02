package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageableRacksDTO {
    private List<TestrackDTO> content;
    private boolean last;
    private int totalPages;
    private int totalElements;
    private int numberOfElements;
    private int size;
    private int number;
    private boolean first;
    private boolean empty;

}
