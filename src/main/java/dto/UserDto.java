package dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Comparable<UserDto> {

    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private boolean emailVerified;
    private String role;
    private String [] requiredActions;
    private String password;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username=" + username +
                ", email=" + email +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", enabled=" + enabled +
                ", emailVerified=" + emailVerified +
                "}";
    }

    @Override
    public int compareTo(UserDto o) {
        if(getEmail() == null) {
            if(o.getEmail() == null) return 0;
            else  return -1;
        }
        else {
            if(o.getEmail() == null) return 1;
            else return getEmail().compareTo(o.getEmail());
        }
    }
}
