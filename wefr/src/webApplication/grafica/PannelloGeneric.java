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

import webApplication.business.ComponenteSemplice;
import webApplication.grafica.TreePanel.RemoveAction;

public abstract class PannelloGeneric extends JPanel implements ListSelectionListener, ActionListener, MyEventClassListener, WindowListener{
	private static final long serialVersionUID = 4733717394320867492L;

	protected JButton bott_up;
	protected JButton bott_down;
	protected JButton bott_del;
	protected JButton bott_addExist;
	protected JList list_components;
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
		bott_del.setActionCommand(DELETE);
		
		if (!(m instanceof Wizard))	{
			remAction = MainWindow.albero.new RemoveAction();
			bott_del.addActionListener(remAction);
			bott_del.addFocusListener(new FocusListener()	{

				@Override
				public void focusGained(FocusEvent arg0) {
					// TODO Auto-generated method stub
					remAction.putValue("Indexes", list_components.getSelectedIndices());
				}

				@Override
				public void focusLost(FocusEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}
		bott_addExist= new JButton();
		bott_addExist.setBounds(197, 165, 121, 27);
		list_components= new JList();
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
		
		Container listContainer= list_components.getParent();
		
		if(list_components != null && listContainer!=null)
			listContainer.remove(list_components);
		
		checkEmptyComponent();
		
		listContainer.add(list_components);
		list_components.addListSelectionListener(this);

		Utils.buttonDeleteMgmt(list_components,bott_del);
		upDownMgmt();
		
		
		listContainer.repaint();

	}

	abstract protected void checkEmptyComponent();

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

	
	
	//TODO probabilmente si disattivera' solo il pannello e non tutta la finestra
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()!= DELETE){/*&&(())){*/
			parentWindow.setEnabled(false);
			AddNew nuovo = new AddNew((Window)this.getTopLevelAncestor(),frameOptions, addNewTitle());

			nuovo.addWindowListener(this);
			nuovo.addEventListener(this);

			nuovo.setVisible(true);
		}
		else{
			 removeElements();
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


	@Override
	public void handleMyEventClassEvent(
			MyEventClass e) {
		parentWindow.setEnabled(true);
		if(e != null){
			addElementToAlternative((ComponenteSemplice) e.getComponente());
		}}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		parentWindow.setEnabled(true);	
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
