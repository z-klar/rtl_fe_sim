package tables;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;

public class RelayTableModel  extends AbstractTableModel {
    public static final  String[] columnNames =
            { "Position", "Name", "Type" };

    private Object[][] data;

    public RelayTableModel(Vector<RelayTableRow> rowData) {
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
