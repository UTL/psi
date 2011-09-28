package webApplication.grafica;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collections;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.ComponenteMolteplice;

import webApplication.business.ComponenteSemplice;
import webApplication.grafica.TreePanel.RemoveAction;

public abstract class PannelloGeneric extends JPanel implements ListSelectionListener, ActionListener, MyEventClassListener, WindowListener, FocusListener{
	private static final String REFRESH_LIST = "RefreshList";

	private static final long serialVersionUID = 4733717394320867492L;

	protected JButton bott_up;
	protected JButton bott_down;
	protected JButton bott_del;
	protected JButton bott_addExist;
	protected CustomJList list_components;
	protected ComponenteAlternative alternativeComp;
	protected ComponenteComposto compostoComp;
	private final static String DELETE = "Delete";
	private Component parentWindow;
	private Options frameOptions;
	private RemoveAction remAction;
	protected JButton button_addNewAlter = new JButton("Add new");
	
	/*
	 * @wbp.parser.constructor
	 */
	/**
	 * @wbp.parser.constructor
	 */
	public PannelloGeneric(Component m, Options o){
		bott_up= new JButton();
		bott_up.setBounds(12, 4, 46, 53);
		bott_down= new JButton();
		bott_down.setBounds(12, 100, 46, 53);
		bott_del= new JButton(DELETE);
		bott_del.setBounds(65, 165, 90, 27);
		
		bott_del.addActionListener(this);
		
		if (!(m instanceof Wizard))	{
			remAction = MainWindow.albero.new RemoveAction();
			bott_del.addActionListener(remAction);
			bott_del.addFocusListener(this);
			
			bott_del.setActionCommand(REFRESH_LIST);
		}
		else
			bott_del.setActionCommand(DELETE);
		
		bott_addExist= new JButton();
		bott_addExist.setBounds(197, 165, 121, 27);
		list_components= new CustomJList();
		parentWindow = m;
		frameOptions = o;
		if(m instanceof Wizard){
		
			bott_del.addActionListener(this);	
		}
		button_addNewAlter.addActionListener(this);
		buildPanel();
	}
	
	public PannelloGeneric(Component m, Componente c, Options o){
		this(m, o);
		assignComponent(c);
	}

	abstract protected void assignComponent(Componente c);
	
	
	
	abstract protected void buildPanel();

	void moveAlternativeElements(int upOrDown) {
		int shift;
		
		Vector<ComponenteSemplice> listaAlternative = alternativeComp.getOpzioni();
		int i;
		int[] toMove = list_components.getSelectedIndices();
		
		if(upOrDown == MainWindow.MOVE_UP){
			shift=MainWindow.MOVE_UP;
			for (i=0; i<toMove.length; i++){
				Collections.swap(listaAlternative,toMove[i], toMove[i]+shift);
				toMove[i]= toMove[i]+shift;
			}
		}
		else if(upOrDown == MainWindow.MOVE_DOWN){
			shift = MainWindow.MOVE_DOWN;
			for (i=toMove.length-1; i>=0; i--){
				Collections.swap(listaAlternative,toMove[i], toMove[i]+shift);
				toMove[i]= toMove[i]+shift;
			}
		}
		else 
			return;
		
		//FIXME bisognerebbe controllare che l'elemento col focus sia davvero un alternativa e in caso di errore sollevare un eccezione
		
		
		alternativeComp.setOpzioni(listaAlternative);
		popolaProperties();
		list_components.setSelectedIndices(toMove);
	}
	
	
	public void setComponent(Componente comp){
		assignComponent(comp);
		popolaProperties();
	}
	
	private void popolaProperties() {		
		listUpdate();

		Utils.buttonDeleteMgmt(list_components,bott_del);
		upDownMgmt();
	}

	private void listUpdate() {
		Container listContainer= list_components.getParent();
		
		if(list_components != null && listContainer!=null)
			listContainer.remove(list_components);

		updateJList();

		listContainer.add(list_components);
		list_components.addListSelectionListener(this);

		listContainer.repaint();

		if(this.getTopLevelAncestor() instanceof Wizard){
			((Wizard) this.getTopLevelAncestor()).manageDoneButton(list_components.getModel().getSize()>0);
		}
		
		if(list_components.getModel().getSize()==0)
			list_components.setToolTipText("At least one element is needed, click \"Add new\" or \"Add existing\" to add a new one");
		
		
		
	}

	
	private boolean updateJList() 
		{
			if(componentNotNull()){
				list_components = new CustomJList(Utils.extractNomiComponenti(alternativeComp.getOpzioni()));
			return false;
		}
			else
				list_components = new CustomJList();
			return true;
		}

	abstract protected boolean componentNotNull();

	abstract protected void upDownMgmt();
	
	//Utils.buttonUpDownMgmt(list_components, bott_up, bott_down);
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		upDownMgmt();
			Utils.buttonDeleteMgmt(list_components,bott_del);
		}
		
	

	
	private void addElementToAlternative(ComponenteSemplice componente) {
		int[] selected = list_components.getSelectedIndices();
		addCSempl(componente);
		popolaProperties();
		list_components.setSelectedIndices(selected);
		Utils.buttonDeleteMgmt(list_components,bott_del);
		upDownMgmt();
	}

	abstract protected void addCSempl(ComponenteSemplice componente);
	//{
	//	((ComponenteAlternative)alternativeComp).aggiungiAlternativa(componente);
	//}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(e.getActionCommand()== DELETE)
			removeElements();
		else if(e.getActionCommand()==REFRESH_LIST){
			listUpdate();
			Utils.buttonDeleteMgmt(list_components,bott_del);
		}
			
		else
			{
			//parentWindow.setEnabled(false);
			AddNew nuovo = new AddNew((Window)this.getTopLevelAncestor(),frameOptions, addNewTitle());

			//nuovo.addWindowListener(this);
			nuovo.addEventListener(this);

			nuovo.setVisible(true);
		}
		
		//Ricostruire lista quando andrea cancella un elemento (nel mainwindow non cancella)
	}
	
	protected abstract String addNewTitle();

	public void removeElements(){
		int i; 
		for(i=list_components.getSelectedIndices().length-1; i>=0; i--){
			removeElement(i);
		}
		popolaProperties();
	}
	/**
	 * 
	 */

	abstract protected void removeElement(int i) ;

	public ComponenteSemplice getNthComp(int i){
		
		return getComponente().getOpzione(i);
		
	}
	

	protected abstract ComponenteMolteplice getComponente();

	@Override
	public void handleMyEventClassEvent(
			MyEventClass e) {
		parentWindow.setEnabled(true);
		if(e != null){
			addElementToAlternative((ComponenteSemplice) e.getComponente());
		}}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// non serve
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// non serve
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		parentWindow.setEnabled(true);	
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// non serve
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// non serve
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// non serve
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// non serve
		
	}
	
	@Override
	public void focusGained(FocusEvent arg0) {
		remAction.putValue(RemoveAction.INDEXES, list_components.getSelectedIndices());
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// non serve
		
	}


	
}
