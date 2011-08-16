package webApplication.grafica;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

import webApplication.business.Componente;

public class CustomTreeEditor extends JPanel implements	TreeCellEditor, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5065263639548994591L;
	private JLabel test;
	private JPanel editorPane;
	
	public CustomTreeEditor()	{
		test = new JLabel("");
		add(test);
	}
	
	/*public void show(DefaultMutableTreeNode node)	{
		//qui si implementeranno le operazioni da fare sul nodo per ora visualizzo solo i dati
		Componente comp = (Componente)node.getUserObject();
		System.out.println(comp.getNome());
		System.out.println(comp.getCategoria());
		System.out.println(comp.getVisibilita());
		System.out.println(comp.getEnfasi());
		System.out.println(comp.getType());
		if (comp.getType() == Testo.TEXTTYPE)	{
			String testo = ((Testo)comp).getTesto();
			System.out.println(testo);
		}
		if (comp.getType() == ComponenteComposto.COMPOSTOTYPE)	{
			System.out.println(((ComponenteComposto)comp).getComponenti());
		}
		if (comp.getType() == ComponenteAlternative.ALTERNATIVETYPE)	{
			System.out.println(((ComponenteAlternative)comp).getAlternative());
		}
	}*/

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelCellEditing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		if (anEvent instanceof MouseEvent)	{
			return true;
		}
		return false;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,boolean selected, boolean expanded, boolean leaf, int row) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		Componente comp = (Componente)node.getUserObject();
		test.setText(comp.getType());
		return editorPane;
	}

}
