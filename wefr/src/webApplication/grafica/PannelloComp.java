package webApplication.grafica;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.ComponenteSemplice;

public class PannelloComp extends PannelloGeneric {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8901038109746479675L;

	/**
	 * @wbp.parser.constructor
	 */
	public PannelloComp(JFrame m) {
		super(m);
	}

	public PannelloComp(JFrame m, Componente c) {
		super(m, c);
	}

	public PannelloComp(JButton b_up, JButton b_down, ButtonRemover b_del,
			JButton b_addExist, JList l_alt) {
		super(b_up, b_down, b_del, b_addExist, l_alt);
	}

	@Override
	protected void buildPanel() {
		setLayout(null);

		

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(34, 4, 386, 149);
		this.add(scrollPane);
		
		scrollPane.setViewportView(list_components);

		
		actionAlternative = new AListenerRemoveFromAlt(list_components);
		bott_del.addActionListener(actionAlternative);
		this.add(bott_del);
		this.add(bott_addExist);

		JButton button_addNewAlter = new JButton("Add new");
		button_addNewAlter.setBounds(322, 165, 98, 27);
		button_addNewAlter.addActionListener(this);
		this.add(button_addNewAlter);

	}

	@Override
	protected void upDownMgmt() {
		

	}

	@Override
	protected void addCSempl(ComponenteSemplice componente) {
		((ComponenteComposto)compostoComp).aggiungiOpzione(componente);

	}

	@Override
	protected void assignComponent(Componente c) {
		if(c.getType()==ComponenteComposto.COMPOSTOTYPE)
			compostoComp= (ComponenteComposto) c;
		else;//FIXME qua andrebbe sollevata un'eccezione
	}

	@Override
	protected void removeElement(int i) {
		compostoComp.cancellaOpzione(list_components.getSelectedIndices()[i]);
	}

	@Override
	protected void checkEmptyComponent() {
		
			if(compostoComp!= null)
				list_components = new JList(Utils.extractNomiComponenti(compostoComp.getOpzioni()));
			else
				list_components = new JList();
		
		
	}



}
