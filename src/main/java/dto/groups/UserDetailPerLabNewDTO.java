package dto.groups;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import commonEnum.LabInvitationState;
import commonEnum.RtlRoles;
import lombok.Data;
import lombok.NoArgsConstructor;
import tables.labs.LabUserTableRow;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailPerLabNewDTO {
    private String username;        // = email
    private RtlRoles role;          // rtl_user, rtl_admin

    public UserDetailPerLabNewDTO(String user, RtlRoles role) {
        this.username = user;
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDetailPerLabDTO: {" +
                "  userName=" + username +
                "  role=" + role +
                "}";
    }

    public LabUserTableRow convertToUserTableRow() {
        return new LabUserTableRow("", username, "", role.name());
    }
}
