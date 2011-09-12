package webApplication.grafica;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JComponent;

public class TransferActionListener implements ActionListener, PropertyChangeListener	{
	
	private JComponent focusOwner;
	
	public TransferActionListener()	{
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addPropertyChangeListener("permanentFocusOwner", this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object o = evt.getNewValue();
		if (o instanceof JComponent)	{
			focusOwner = (JComponent)o;
		}
		else	{
			focusOwner = null;
		}
	}

	@Override
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
