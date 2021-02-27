package tables;

public class DisplayTableRow {
    public String Type;
    public int Width;
    public int Height;
    public int Port;

    public DisplayTableRow(String Type, int Width, int Height, int Port) {
        this.Type = Type;
        this.Width = Width;
        this.Height = Height;
        this.Port = Port;
    }

    public Object[] toObject() {
        return new Object[]{ Type, Width, Height, Port };
    }
}
