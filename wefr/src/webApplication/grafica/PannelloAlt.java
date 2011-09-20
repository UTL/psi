package webApplication.grafica;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.ComponenteSemplice;

public class PannelloAlt extends PannelloGeneric {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2009369529793525573L;

	/*
	 * @wbp.parser.constructor
	 */
	/**
	 * @wbp.parser.constructor
	 */
	public PannelloAlt(Component m, Options o) {
		super(m, o);
		// TODO Auto-generated constructor stub
	}

	public PannelloAlt(Component m, ComponenteAlternative c, Options o) {
		super(m, c, o);
		// TODO Auto-generated constructor stub
	}

	public PannelloAlt(JButton b_up, JButton b_down, JButton b_del,
			JButton b_addExist, JList l_alt) {
		super(b_up, b_down, b_del, b_addExist, l_alt);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void buildPanel() {
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
				
				scrollPane.setViewportView(list_components);

				bott_down.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						moveAlternativeElements(MainWindow.MOVE_DOWN);
				}});
				bott_down.setToolTipText("Click here to decrease the priority of selected element");
				this.add(bott_down);
				
				this.add(bott_del);
				this.add(bott_addExist);

				JButton button_addNewAlter = new JButton("Add new");
				button_addNewAlter.setBounds(322, 165, 98, 27);
				button_addNewAlter.addActionListener(this);
				this.add(button_addNewAlter);

	}

	@Override
	protected void upDownMgmt() {
		Utils.buttonUpDownMgmt(list_components, bott_up, bott_down);

	}

	@Override
	protected void addCSempl(ComponenteSemplice componente) {
		((ComponenteAlternative)alternativeComp).aggiungiOpzione(componente);

	}

	@Override
	protected void assignComponent(Componente c) {
		
		if (c.getType()==ComponenteAlternative.ALTERNATIVETYPE)
			alternativeComp = (ComponenteAlternative) c;
		else ; //FIXME qua andrebbe sollevata un'eccezione
	}

	@Override
	protected void removeElement(int i) {
		alternativeComp.cancellaOpzione(list_components.getSelectedIndices()[i]);
	}

	@Override
	protected void checkEmptyComponent() 
		{
			if(alternativeComp != null)
				list_components = new JList(Utils.extractNomiComponenti(alternativeComp.getOpzioni()));
			else
				list_components = new JList();
		}

	


		
	

}
