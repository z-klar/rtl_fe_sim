package dto.groups;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import tables.labs.LabTableRow;

import java.util.Calendar;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LabDetailDTO {
    private int id;
    private String name;
    private int testrackCount;
    private int userCount;
    private long createdOn;
    private List<String> administrators;

    public String getCreatedOnString() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(createdOn);
        return String.format("%d.%d.%d %02d:%02d:%02d",
                c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
    }
    public String getUserCountString() {
        return String.format("%d", userCount);
    }
    public String getTestrackCountString() {
        return String.format("%d", testrackCount);
    }
    public String getAdminsString() {
        String spom = "";
        if(administrators != null) {
            for (String admin : administrators) {
                spom += admin;
                spom += " | ";
            }
        }
        return spom;
    }

    public LabTableRow ConvertToTableRow() {
        return new LabTableRow(id, name,
                getCreatedOnString(),
                getTestrackCountString(),
                getUserCountString(),
                getAdminsString());
    }
}
