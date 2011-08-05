package webApplication.grafica;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;

public class CustomTreeEditor extends AbstractCellEditor implements	TreeCellEditor, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5065263639548994591L;
	private static String SAVE_COMMAND = "save";
	

	
	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Component getTreeCellEditorComponent(JTree arg0, Object arg1,
			boolean arg2, boolean arg3, boolean arg4, int arg5) {
		// TODO Auto-generated method stub
		return null;
	}

}
