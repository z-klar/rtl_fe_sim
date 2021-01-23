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
public class JanusSdpDTO {
    private String profile;
    private String local;
    private String remote;

    public ArrayList<String> toStringArray(int offset) {
        final int MAX_LEN = 150;
        ArrayList<String> al = new ArrayList<>();
        String format = String.format("%%%ds%%25s:%%s", offset);
        al.add(String.format(format, " ", "---------------  S D P  ------------------", ""));
        al.add(String.format(format, " ", "profile", profile));
        String spom = local;
        if(local.length() > MAX_LEN) spom = local.substring(0, MAX_LEN);
        al.add(String.format(format, " ", "local", spom + "...."));
        spom = local;
        if(remote.length() > MAX_LEN) spom = remote.substring(0, MAX_LEN);
        al.add(String.format(format, " ", "remote", spom + "...."));
        al.add(String.format(format, " ", "...........................................", ""));
        return al;
    }


}
