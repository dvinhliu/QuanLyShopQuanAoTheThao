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
public class TableActionCellEditor1 extends DefaultCellEditor {
    private TableActionEvent1 event;
    public TableActionCellEditor1(TableActionEvent1 event) {
        super(new JCheckBox());
        this.event = event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        PannelAction1 action = new PannelAction1();
        action.initEvent(event, row);
        action.setBackground(table.getSelectionBackground());
        return action;
    }
}
