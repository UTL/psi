package webApplication.grafica;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public abstract class PannelloGeneric extends JPanel implements ListSelectionListener, ActionListener{
	private static final long serialVersionUID = 4733717394320867492L;

	protected AListenerRemoveFromAlt actionAlternative;
	protected JButton bott_up;
	protected JButton bott_down;
	protected ButtonRemover bott_del;
	protected JButton bott_addExist;
	protected JList list_components;
	protected ComponenteAlternative alternativeComp;
	protected ComponenteComposto compostoComp;
	private final static String DELETE = "Delete";
	private JFrame mainW;
	
	/*
	 * @wbp.parser.constructor
	 */
	public PannelloGeneric(JFrame m){
		bott_up= new JButton();
		bott_up.setBounds(12, 4, 46, 53);
		bott_down= new JButton();
		bott_down.setBounds(12, 100, 46, 53);
		bott_del= new ButtonRemover(DELETE);
		bott_del.setBounds(65, 165, 90, 27);
		bott_addExist= new JButton();
		bott_addExist.setBounds(197, 165, 121, 27);
		list_components= new JList();
		mainW = m;

		buildPanel();
	}
	
	public PannelloGeneric(JFrame m, Componente c){
		this(m);
		assignComponent(c);
	}

	abstract protected void assignComponent(Componente c);
	
	public PannelloGeneric(JButton b_up, JButton b_down, ButtonRemover b_del, JButton b_addExist, JList l_alt) {
		bott_up=b_up;
		bott_down=b_down;
		bott_del=b_del;
		bott_addExist=b_addExist;
		list_components=l_alt;
		buildPanel();
		
	}
	
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
		
		if(alternativeComp != null)
			list_components = new JList(Utils.extractNomiComponenti(alternativeComp.getOpzioni()));
		else
			list_components = new JList();
		
		listContainer.add(list_components);
		list_components.addListSelectionListener(this);

		Utils.buttonDeleteMgmt(list_components,bott_del);
		upDownMgmt();
		
		
		listContainer.repaint();

	}

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
		mainW.setEnabled(false);
		AddNew nuovo = new AddNew();
		
		nuovo.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				mainW.setEnabled(true);
		}});
		
		nuovo.addEventListener(new MyEventClassListener(){

			@Override
			public void handleMyEventClassEvent(
					MyEventClass e) {
					mainW.setEnabled(true);
						if(e != null){
							addElementToAlternative((ComponenteSemplice) e.getComponente());
							}}

			@Override
			public void handleMyEventClassEvent(EventObject e) {}
			});

		nuovo.setVisible(true);
	}
	
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

	
}
