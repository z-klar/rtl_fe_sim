package tables.labs;

public class LabTableRow {
    public int Id;
    public String Name;
    public String CreatedOn;
    public String RackCOunt;
    public String UserCount;
    public String Admins;

    public LabTableRow(int Id, String name, String CreatedOn,
                       String RackCOunt, String UserCount, String Admins) {
        this.Id = Id;
        this.Name = name;
        this.CreatedOn = CreatedOn;
        this.RackCOunt = RackCOunt;
        this.UserCount = UserCount;
        this.Admins = Admins;
    }

    public Object[] toObject() {
        return new Object[]{ Id, Name, CreatedOn, RackCOunt, UserCount, Admins };
    }
}
