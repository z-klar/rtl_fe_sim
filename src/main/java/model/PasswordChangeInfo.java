package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeInfo {

    public PasswordChangeInfo(String pwd) {
        oldPassword = "";
        password = pwd;
    }

    private String oldPassword;
    private String password;

}
