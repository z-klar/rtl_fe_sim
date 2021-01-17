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
public class JanusDtlsDTO {
    private String fingerprint;
    @JsonAlias("remote-fingerprint")
    private String remote_fingerprint;
    @JsonAlias("dtls-role")
    private String dtls_role;
    @JsonAlias("dtls-state")
    private String dtlas_state;
    private int valid;
    private int ready;
    @JsonAlias("scto-association")
    private int scto_association;
}
