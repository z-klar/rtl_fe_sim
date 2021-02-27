package tables;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class DisplayTableModel extends AbstractTableModel {
    private String[] columnNames =
            { "Type", "Width", "Height", "Port" };

    private Object[][] data;

    public DisplayTableModel(Vector<DisplayTableRow> rowData) {
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
