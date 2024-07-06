/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raven_cell;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author ACER
 */
public class TableActionCellEditor2 extends DefaultCellEditor{
    private TableActionEvent2 event;
    public TableActionCellEditor2(TableActionEvent2 event) {
        super(new JCheckBox());
        this.event = event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        PannelAction2 action = new PannelAction2();
        action.initEvent(event, row);
        action.setBackground(table.getSelectionBackground());
        return action;
    }
}
