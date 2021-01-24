package tables;

public class JanusCommonTable {
    public String key;
    public String value;


    public JanusCommonTable(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Object[] toObject() {
        return new Object[]{key, value };
    }
}
