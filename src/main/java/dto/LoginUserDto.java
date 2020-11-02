package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginUserDto {

    private String username;
    //    private String email;
    private String password;

    public LoginUserDto(String usr, String pwd) {
        username = usr;
        password = pwd;
    }


    @Override
    public String toString() {
        return "User{" +
                "username=" + username +
                ", password=" + password +
                "}";
    }

}
