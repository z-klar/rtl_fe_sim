package dto.janus;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

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
    private boolean valid;
    private boolean ready;
    @JsonAlias("scto-association")
    private int scto_association;

    public ArrayList<String> toStringArray(int offset) {
        ArrayList<String> al = new ArrayList<>();
        String format = String.format("%%%ds%%25s:%%s", offset);
        al.add(String.format(format, " ", "--------------- COmponent DTLS ---------------", ""));
        al.add(String.format(format, " ", "fingerprint", fingerprint));
        al.add(String.format(format, " ", "remote_fingerprint", remote_fingerprint));
        al.add(String.format(format, " ", "dtls_role", dtls_role));
        al.add(String.format(format, " ", "dtlas_state", dtlas_state));
        al.add(String.format(format, " ", "valid", valid));
        al.add(String.format(format, " ", "ready", ready));
        al.add(String.format(format, " ", "scto_association", scto_association));
        al.add(String.format(format, " ", "..............................................", ""));
        return al;
    }


}
