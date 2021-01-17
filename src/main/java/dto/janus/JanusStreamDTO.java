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
public class JanusStreamDTO {
    private int id;
    private int ready;
    private boolean disabled;
    private JanusSsrcDTO ssrc;
    private JanusStreamComponentDTO [] components;
}
