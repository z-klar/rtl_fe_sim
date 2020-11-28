package tables;

public class RelayTableRow {
    public int Position;
    public String Name;
    public String Type;

    public RelayTableRow(int pos, String name, String type) {
        this.Position = pos;
        this.Name = name;
        this.Type = type;
    }

    public Object[] toObject() {
        return new Object[]{ Position, Name, Type };
    }
}
