package tables.labs;

public class LabTableRow {
    public int Id;
    public String Name;
    public String CreatedOn;
    public String RackCOunt;
    public String UserCount;

    public LabTableRow(int Id, String name, String CreatedOn,
                       String RackCOunt, String UserCount) {
        this.Id = Id;
        this.Name = name;
        this.CreatedOn = CreatedOn;
        this.RackCOunt = RackCOunt;
        this.UserCount = UserCount;
    }

    public Object[] toObject() {
        return new Object[]{ Id, Name, CreatedOn, RackCOunt, UserCount };
    }
}
