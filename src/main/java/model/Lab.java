package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lab {
    private int id;
    private String name;
    private Timestamp createdOn;


    public Lab(int id, String name, Timestamp created) {
        this.id = id;
        this.name = name;
        this.createdOn = created;
    }
}
