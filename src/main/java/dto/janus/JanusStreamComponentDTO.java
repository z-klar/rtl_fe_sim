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
public class JanusStreamComponentDTO {
    private int id;
    private String state;
    @JsonAlias("local-candidates")
    private String [] local_candidates;
    @JsonAlias("remote-candidates")
    private String [] remote_candidates;
    @JsonAlias("selected-pair")
    private String selected_pair;
    private JanusDtlsDTO dtls;
    private JanusStatsDTO in_stats;
    private JanusStatsDTO out_stats;
}
