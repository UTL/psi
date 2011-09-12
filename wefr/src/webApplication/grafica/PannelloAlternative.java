package webApplication.grafica;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteSemplice;

public class PannelloAlternative extends JPanel implements ListSelectionListener, ActionListener{
	private static final long serialVersionUID = 1285953256642735080L;
	
	private AListenerRemoveFromAlt actionAlternative;
	private JButton bott_up;
	private JButton bott_down;
	private ButtonRemover bott_del;
	private JButton bott_addExist;
	private JList list_alt;
	private ComponenteAlternative alternativeComp;
	private final static String DELETE = "Delete";
	private JFrame mainW;
	
	public PannelloAlternative(JFrame m){
		bott_up= new JButton();
		bott_up.setBounds(12, 4, 46, 53);
		bott_down= new JButton();
		bott_down.setBounds(12, 100, 46, 53);
		bott_del= new ButtonRemover(DELETE);
		bott_del.setBounds(65, 165, 90, 27);
		bott_addExist= new JButton();
		bott_addExist.setBounds(197, 165, 121, 27);
		list_alt= new JList();
		mainW = m;

		buildPanel();
	}
	
	public PannelloAlternative(JFrame m, ComponenteAlternative c){
		this(m);
		alternativeComp = c;
	}
	
	public PannelloAlternative(JButton b_up, JButton b_down, ButtonRemover b_del, JButton b_addExist, JList l_alt) {
		bott_up=b_up;
		bott_down=b_down;
		bott_del=b_del;
		bott_addExist=b_addExist;
		list_alt=l_alt;
		buildPanel();
		
	}
	
	private void buildPanel(){
		
		//TODO cambiare le icone terribili dei bottoni up e down

		
		bott_up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				moveAlternativeElements(MainWindow.MOVE_UP);}});
		this.setLayout(null);
		bott_up.setToolTipText("Click here to increase the priority of selected element");
		this.add(bott_up);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(70, 4, 350, 149);
		this.add(scrollPane);
		
		scrollPane.setViewportView(list_alt);

		bott_down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				moveAlternativeElements(MainWindow.MOVE_DOWN);
		}});
		bott_down.setToolTipText("Click here to decrease the priority of selected element");
		this.add(bott_down);
		actionAlternative = new AListenerRemoveFromAlt(list_alt);
		bott_del.addActionListener(actionAlternative);
		this.add(bott_del);
		this.add(bott_addExist);

		JButton button_addNewAlter = new JButton("Add new");
		button_addNewAlter.setBounds(322, 165, 98, 27);
		button_addNewAlter.addActionListener(this);
		this.add(button_addNewAlter);
		
		
	}

	private void moveAlternativeElements(int upOrDown) {
		int shift;
		
		Vector<ComponenteSemplice> listaAlternative = alternativeComp.getAlternative();
		int i;
		int[] toMove = list_alt.getSelectedIndices();
		
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
		
		
		alternativeComp.setAlternative(listaAlternative);
		popolaProperties();
		list_alt.setSelectedIndices(toMove);
	}
	
	
	public void setAlternativeComponent(ComponenteAlternative comp){
		alternativeComp = comp;
		popolaProperties();
	}
	
	private void popolaProperties() {
		//--------------- MAIN WINDOW --------------//
		//setGenerici(selected, "Alternative");
		//MainWindow.setContentLayout(MainWindow.PANEL_ALT);
		//--------------- END MAIN WINDOW --------------//
		
		Container listContainer= list_alt.getParent();
		
		if(list_alt != null && listContainer!=null)
			listContainer.remove(list_alt);

		list_alt = new JList(Utils.extractNomiComponenti(alternativeComp.getAlternative()));
		
		listContainer.add(list_alt);
		
		Utils.buttonDeleteMgmt(list_alt,bott_del);
		Utils.buttonUpDownMgmt(list_alt, bott_up, bott_down);
		
		list_alt.addListSelectionListener(this);

		listContainer.repaint();

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		/*if(arg0.getSource()==list_composite)
			Utils.buttonDeleteMgmt(list_composite,button_deleteFromComp);*/
			Utils.buttonUpDownMgmt(list_alt, bott_up, bott_down);
			Utils.buttonDeleteMgmt(list_alt,bott_del);
		}
		
	

	private void addElementToAlternative(ComponenteSemplice componente) {
		int[] selected = list_alt.getSelectedIndices();
		((ComponenteAlternative)alternativeComp).aggiungiAlternativa(componente);
		popolaProperties();
		list_alt.setSelectedIndices(selected);
		Utils.buttonDeleteMgmt(list_alt,bott_del);
		Utils.buttonUpDownMgmt(list_alt, bott_up, bott_down);
	}

	
	
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
		for(i=list_alt.getSelectedIndices().length-1; i>=0; i--){
			alternativeComp.cancellaAlternativa(list_alt.getSelectedIndices()[i]);
		}
		popolaProperties();
	}
		
}
	
	
	

