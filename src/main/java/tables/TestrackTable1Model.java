package tables;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class TestrackTable1Model extends AbstractTableModel {
    private String[] columnNames =
            { "ID", "Name", "Description", "Address",
              "Vehicle", "Availability", "IP Address",
              "MGB Port[0]"};

    private Object[][] data;

    public TestrackTable1Model(Vector<TestrackTable1> rowData) {
        data = new Object[rowData.size()][columnNames.length];
        for (int i = 0; i < rowData.size(); i++) {
            data[i] = rowData.get(i).toObject();
        }
    }
    public int getRowCount() {
        //return(data.length / columnNames.length);
        return(data.length);
    }
    public int getColumnCount() {
        return(columnNames.length);
    }
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    public String getColumnName(int col) {
        return columnNames[col];
    }
}
