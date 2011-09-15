package webApplication.grafica;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import webApplication.business.ComponenteComposto;
import webApplication.grafica.TreePanel.AddAction;

public class AddWindow extends JDialog implements ActionListener	{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7676372160353228088L;
	
	private JButton btnAdd;
	public AddWindow(JFrame owner)	{
		super(owner,Dialog.ModalityType.APPLICATION_MODAL);
		init();
	}

	private void init()	{
		setTitle("Add New Element");
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		btnAdd = new JButton("Add");
		panel.add(btnAdd, BorderLayout.NORTH);
		btnAdd.setActionCommand("ADD");
		btnAdd.addActionListener(this);
		
		JButton btnCancel = new JButton("Cancel");
		panel.add(btnCancel, BorderLayout.SOUTH);
		btnCancel.addActionListener(this);
		
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("ADD"))	{
			//METODO 1
			TreePanel panel = ((ProvaAlbero)this.getParent()).treePanel;
			ComponenteComposto comp = new ComponenteComposto("Composite1","Comp",0,0);
			AddAction addAction = panel.new AddAction();
			addAction.putValue("Componente", comp);
			addAction.putValue("ParentIndex", -1);
			JButton hiddenNoErrorButton = new JButton();
			hiddenNoErrorButton.addActionListener(addAction);
			hiddenNoErrorButton.doClick();
			
			//METODO 2
			/*TreePanel panel = ((ProvaAlbero)this.getParent()).treePanel;
			ComponenteComposto comp = new ComponenteComposto("Composite1","Comp",0,0);
			//panel.addNode(null,new DefaultMutableTreeNode(comp));
			AddAction addAction = panel.new AddAction(comp);
			addAction.actionPerformed(e);*/
		}
		this.dispose();
	}

}
