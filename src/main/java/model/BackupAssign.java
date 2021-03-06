package model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import commonEnum.LabInvitationState;
import commonEnum.RtlRoles;
import dto.groups.UserDetailPerLabDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackupAssign {
    private String id;
    private int labId;
    private String username;        // = email
    private LabInvitationState state;   // PENDING, CONFIRMED
    private RtlRoles role;          // rtl_user, rtl_admin

    public BackupAssign(UserDetailPerLabDTO assign, int labId) {
        this.setId(assign.getId());
        this.setUsername(assign.getUsername());
        this.setState(assign.getState());
        this.setRole(assign.getRole());
        this.labId = labId;
    }

    @Override
    public String toString() {
        return "UserDetailPerLabDTO: {" +
                "id=" + id +
                "  labId=" + labId +
                "  userName=" + username +
                "  state=" + state +
                "  role=" + role +
                "}";
    }

}
