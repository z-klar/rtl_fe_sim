package tables.labs;

public class LabUserTableRow {
    public String Id;
    public String Name;
    public String State;
    public String Role;

    public LabUserTableRow(String Id, String name, String State, String Role) {
        this.Id = Id;
        this.Name = name;
        this.Role = Role;
        this.State = State;
    }

    public Object[] toObject() {
        return new Object[]{ Id, Name, State, Role };
    }
}
