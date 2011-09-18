package webApplication.grafica;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import webApplication.business.ComponenteComposto;
import webApplication.grafica.TreePanel.AddAction;

public class AddWindow extends JDialog implements ActionListener, FocusListener	{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7676372160353228088L;
	
	private JButton btnAdd;
	private AddAction addAction;
	
	/**
	 * Costruttore per la finestra del wizard
	 * @param owner La finestra di lavoro principale
	 */
	public AddWindow(JFrame owner)	{
		super(owner,Dialog.ModalityType.APPLICATION_MODAL);
		init();
	}

	/**
	 * Inizializza la finestra per il wizard
	 */
	private void init()	{
		setTitle("Add New Element");
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JButton btnCancel = new JButton("Cancel");
		panel.add(btnCancel, BorderLayout.NORTH);
		btnCancel.addActionListener(this);
		
		btnAdd = new JButton("Done");
		panel.add(btnAdd, BorderLayout.SOUTH);
		btnAdd.setActionCommand("ADD");
		btnAdd.addActionListener(this);
		btnAdd.addFocusListener(this);
		TreePanel treePanel = ((ProvaAlbero)this.getParent()).treePanel;
		addAction = treePanel.new AddAction();
		btnAdd.addActionListener(addAction);
		
		pack();
		setVisible(true);
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//qualunque click sui bottoni chiude la finestra
		this.dispose();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent arg0) {
		// nel momento in cui il bottone di Done guadagna il focus, setto i valori così al click è già tutto pronto!
		ComponenteComposto comp = new ComponenteComposto("Composite1","Comp",0,0);
		addAction.putValue("Componente", comp);
		addAction.putValue("ParentIndex", -1);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		//non mi interessa
	}

}
