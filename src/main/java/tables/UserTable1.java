package tables;

public class UserTable1 {
    public String ID;
    public String UserName;
    public String FirstName;
    public String LastName;
    public String Email;

    public UserTable1(String id, String name, String first, String last, String mail) {
        ID = id;
        UserName = name;
        FirstName = first;
        LastName = last;
        Email = mail;
    }

    public Object[] toObject() {
        return new Object[]{ID, UserName, FirstName, LastName, Email};
    }
}
