package tables.labs;

public class LabTestrackTableRow {
    public String Id;
    public String Name;
    public String Description;
    public String Address;
    public String Vehicle;
    public String Vin;

    public LabTestrackTableRow(String Id, String name, String Description,
                               String Address, String Vehicle, String Vin) {
        this.Id = Id;
        this.Name = name;
        this.Description = Description;
        this.Address = Address;
        this.Vehicle = Vehicle;
        this.Vin = Vin;
    }

    public Object[] toObject() {
        return new Object[]{ Id, Name, Description, Address, Vehicle, Vin };
    }
}
