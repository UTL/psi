package webApplication.grafica;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import webApplication.business.ComponenteSemplice;

/**
 * Il pannello per la visualizzazione del contenuto di un elemento di tipo
 * Composite
 * 
 * @author Andrea
 */
public class PannelloAlt extends PannelloComp implements ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2009369529793525573L;

	protected JButton bottUp;
	protected JButton bottDown;

	private static final String UPTOOLTIP = "Click here to increase the priority of selected elements";
	private static final String UPICON = "up.png";
	private static final String DISUPICON = "disup.png";
	protected static final String UPCOMMAND = "Up";
	private static final String DOWNTOOLTIP = "Click here to decrease the priority of selected elements";
	private static final String DOWNICON = "down.png";
	private static final String DISDOWNICON = "disdown.png";
	protected static final String DOWNCOMMAND = "Down";

	private static final int BTNMOVESIZE = 46;;

	/**
	 * Crea il pannello ed i relativi campi
	 * 
	 * @param isWizard
	 *            True se la finestra che richiama questo elemento e il Wizard,
	 *            False viceversa
	 */
	public PannelloAlt(boolean isWizard) {
		super(isWizard);

		
		Icon enabledIcon = new ImageIcon(this.getClass().getClassLoader().getResource(MainWindow.BASEPATH + UPICON));
		Icon disabledIcon = new ImageIcon(this.getClass().getClassLoader().getResource(MainWindow.BASEPATH + DISUPICON));
		bottUp = new JButton(enabledIcon);
		bottUp.setDisabledIcon(disabledIcon);
		bottUp.setActionCommand(UPCOMMAND);
		bottUp.setToolTipText(UPTOOLTIP);
		bottUp.setBounds(super.scrollPane.getX(), super.scrollPane.getY(), BTNMOVESIZE, BTNMOVESIZE);
		bottUp.setEnabled(false);
		if (isWizard) {
			bottUp.addActionListener(this);
		} else {
			bottUp.addActionListener(MainWindow.eventDispatcher);
		}
		add(bottUp);

		enabledIcon = new ImageIcon(this.getClass().getClassLoader().getResource(MainWindow.BASEPATH + DOWNICON));
		disabledIcon = new ImageIcon(this.getClass().getClassLoader().getResource(MainWindow.BASEPATH + DISDOWNICON));
		bottDown = new JButton(enabledIcon);
		bottDown.setDisabledIcon(disabledIcon);
		bottDown.setActionCommand(DOWNCOMMAND);
		bottDown.setToolTipText(DOWNTOOLTIP);
		bottDown.setBounds(super.scrollPane.getX(), bottUp.getY() + super.scrollPane.getHeight() - (BTNMOVESIZE + 1), BTNMOVESIZE, BTNMOVESIZE);
		bottDown.setEnabled(false);
		if (isWizard) {
			bottDown.addActionListener(this);
		} else {
			bottDown.addActionListener(MainWindow.eventDispatcher);
		}
		add(bottDown);
		list_components.getSelectionModel().addListSelectionListener(this);

		int newX = super.scrollPane.getX() + bottUp.getX() + bottUp.getWidth() + 4;
		super.scrollPane.setBounds(newX, super.scrollPane.getY(), super.scrollPane.getWidth() - newX, super.scrollPane.getHeight());
		super.delete.setBounds(newX, super.delete.getY(), super.delete.getWidth(), super.delete.getHeight());
	}
	
	protected void moveElements(int[] indici, int gap) {
		Vector<ComponenteSemplice> opzioni = getOpzioni();
		if (gap==-1) {
			for (int i = 0; i < indici.length; i++) {
				Collections.swap(opzioni, indici[i], indici[i] - 1);
				if (isWizard) {
					if ((newIndexes.contains(indici[i])) && (newIndexes.contains(indici[i] - 1))) {
						Collections.swap(newIndexes, newIndexes.indexOf(indici[i]), newIndexes.indexOf(indici[i] - 1));
					} else if (newIndexes.contains(indici[i])) {
						int index = newIndexes.indexOf(indici[i]);
						newIndexes.set(index, indici[i] - 1);
					} else if (newIndexes.contains(indici[i] - 1)) {
						int index = newIndexes.indexOf(indici[i] - 1);
						newIndexes.set(index, indici[i]);
					}
				}
				indici[i] = indici[i] - 1;
			}
		} else {
			for (int i = (indici.length - 1); i >= 0; i--) {
				Collections.swap(opzioni, indici[i], indici[i] + 1);
				if (isWizard) {
					if ((newIndexes.contains(indici[i])) && (newIndexes.contains(indici[i] + 1))) {
						Collections.swap(newIndexes, newIndexes.indexOf(indici[i]), newIndexes.indexOf(indici[i] + 1));
					} else if (newIndexes.contains(indici[i])) {
						int index = newIndexes.indexOf(indici[i]);
						newIndexes.set(index, indici[i] + 1);
					} else if (newIndexes.contains(indici[i] + 1)) {
						int index = newIndexes.indexOf(indici[i] + 1);
						newIndexes.set(index, indici[i]);
					}
				}
				indici[i] = indici[i] + 1;
			}
		}
		setOpzioni(opzioni);
		list_components.setSelectedIndices(indici);
	}

	/**
	 * {@inheritDoc}
	 */
	public void valueChanged(ListSelectionEvent e) {
		// gestisce il pulsante delete
		super.valueChanged(e);

		// gestisce i pulsanti di up e down
		if (list_components.isSelectionEmpty()) {
			bottUp.setEnabled(false);
			bottDown.setEnabled(false);
		} else {
			int num_elem = list_components.getModel().getSize();
			int max_selected = list_components.getMaxSelectionIndex();
			int min_selected = list_components.getMinSelectionIndex();

			if (num_elem < 2 || max_selected == -1 || (min_selected == 0 && max_selected == num_elem - 1)) {
				bottDown.setEnabled(false);
				bottUp.setEnabled(false);
			} else if (min_selected == 0) {
				bottDown.setEnabled(true);
				bottUp.setEnabled(false);
			} else if (max_selected == num_elem - 1) {
				bottDown.setEnabled(false);
				bottUp.setEnabled(true);
			} else {
				bottDown.setEnabled(true);
				bottUp.setEnabled(true);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(UPCOMMAND)) {
			int[] indici = list_components.getSelectedIndices();
			moveElements(indici, -1);
		} else if (e.getActionCommand().equals(DOWNCOMMAND)) {
			int[] indici = list_components.getSelectedIndices();
			moveElements(indici, +1);
		} else if (e.getActionCommand().equals(PannelloComp.ADDNEWACTION)) {
			super.actionPerformed(e);
		} else if (e.getActionCommand().equals(PannelloComp.ADDEXISTACTION)) {
			super.actionPerformed(e);
		} else if (e.getActionCommand().equals(PannelloComp.DELETEACTION)) {
			super.actionPerformed(e);
		}
	}

}
