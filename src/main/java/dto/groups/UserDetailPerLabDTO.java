package dto.groups;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import commonEnum.LabInvitationState;
import commonEnum.RtlRoles;
import tables.labs.LabUserTableRow;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailPerLabDTO {
    private String id;
    private String username;        // = email
    private LabInvitationState state;   // PENDING, CONFIRMED
    private RtlRoles role;          // rtl_user, rtl_admin

    public UserDetailPerLabDTO(String id, String user, LabInvitationState state, RtlRoles role) {
        this.id = id;
        this.username = user;
        this.state = state;
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDetailPerLabDTO: {" +
                "id=" + id +
                "  userName=" + username +
                "  state=" + state +
                "  role=" + role +
                "}";
    }

    public LabUserTableRow convertToUserTableRow() {
        return new LabUserTableRow(id, username, state.name(), role.name());
    }
}
