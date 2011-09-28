package webApplication.grafica;

import java.awt.Container;
import java.awt.event.MouseEvent;

import javax.swing.JList;

import webApplication.business.Componente;

public class CustomJList extends JList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7663308026890717316L;

	public CustomJList(String[] s) {
		super(s);
	}

	public CustomJList() {
		super();
	}

	//TODO rendere i tooltip multilinea
	@Override
	public String getToolTipText(MouseEvent e){
		int index = locationToIndex(e.getPoint());

		Container gPanel = getPannelloGeneric(this);
		if(gPanel != null && index != -1){
			if(getCellBounds(index, index).contains(e.getPoint()))
				return "Nome: "+((PannelloGeneric) gPanel).getNthComp(index).getNome()+"\nTipo: "+((PannelloGeneric) gPanel).getNthComp(index).getType();		
		}
		return null;
		
	}

	private Container getPannelloGeneric(Container c) {
		
		if(c instanceof PannelloGeneric)
			return c;
		else if(c.getParent() == null)
			return null;
		else 
			return getPannelloGeneric(c.getParent());	
	}
	
	
	
}
