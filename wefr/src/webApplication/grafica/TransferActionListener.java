package webApplication.grafica;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JComponent;

/**
 * Traduce un evento da tastiera nel corretto evento sulla base del focus corrente
 * @author Andrea
 *
 */
public class TransferActionListener implements ActionListener, PropertyChangeListener	{
	//si occupa di catturare gli shortcut e lanciare l'azione correlata al listener
	private JComponent focusOwner;
	

	/**
	 * Il costruttore base
	 */
	public TransferActionListener()	{
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addPropertyChangeListener("permanentFocusOwner", this);
	}

	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		//recupera il focus corrente
		Object o = evt.getNewValue();
		if (o instanceof JComponent)	{
			focusOwner = (JComponent)o;
		}
		else	{
			focusOwner = null;
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		if (focusOwner == null)	{
			return;
		}
		String action = (String)evt.getActionCommand();
		Action a = focusOwner.getActionMap().get(action);
		if (a!=null)	{
			a.actionPerformed(new ActionEvent(focusOwner,ActionEvent.ACTION_PERFORMED,null));
		}
	}

}
