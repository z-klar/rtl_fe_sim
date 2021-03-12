package dto.groups;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import commonEnum.LabInvitationState;
import commonEnum.RtlRoles;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LabUserAssignmentDTO {
    private int RecordId;
    private String UserId;
    private int LabId;
    private RtlRoles Role;
    private LabInvitationState State;

}
