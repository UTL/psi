package webApplication.grafica;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import webApplication.business.ComponenteMolteplice;
import webApplication.business.ComponenteSemplice;

/**
 * Il pannello per la visualizzazione del contenuto di un elemento di tipo
 * Composite
 * 
 * @author Andrea
 */
public class PannelloComp extends PannelloGeneric implements ListSelectionListener, ActionListener/*, MyEventClassListener, WindowListener, FocusListener*/ {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9085009029414071030L;

	protected JList list_components;
	protected Vector<Integer> originalIndexes;
	protected Vector<Integer> newIndexes;
	private int simpleElementCount;
	protected boolean isWizard;

	protected DefaultListModel model;
	protected ComponenteMolteplice comp;

	private static final String ELEMENTLIST = "Elements list:";
	protected JScrollPane scrollPane;

	protected JButton addNew;
	protected JButton addExist;
	protected JButton delete;

	private static final String DELETE = "Delete";
	protected static final String DELETEACTION = "Delete";
	private static final String ADDEXIST = "Add existing";
	protected static final String ADDEXISTACTION = "Add existing";
	private static final String ADDNEW = "Add new";
	protected static final String ADDNEWACTION = "Add new";

	private static final int LBLHEIGHT = 14;
	private static final int LBLWIDTH = 100;
	private static final int SCROLLWIDTH = 420;
	private static final int SCROLLHEIGHT = 135;
	private static final int BTNWIDTH = 105;
	private static final int BTNHEIGHT = 27;

	/**
	 * Crea il pannello ed i relativi campi
	 * 
	 * @param _isWizard	True se la finestra che richiama questo elemento e il Wizard, False viceversa
	 */
	public PannelloComp(boolean _isWizard) {
		super();

		isWizard = _isWizard;
		
		if (isWizard) {
			originalIndexes = new Vector<Integer>();
			newIndexes = new Vector<Integer>();
		}

		model = new DefaultListModel();
		list_components = new JList(model);
		list_components.getSelectionModel().addListSelectionListener(this);

		JLabel lblElementList = new JLabel(ELEMENTLIST);
		lblElementList.setBounds(0, 0, LBLWIDTH, LBLHEIGHT);
		add(lblElementList);

		scrollPane = new JScrollPane(list_components);
		scrollPane.setBounds(lblElementList.getX(), lblElementList.getY() + 20, SCROLLWIDTH, SCROLLHEIGHT);
		add(scrollPane);

		delete = new JButton(DELETE);
		delete.setActionCommand(DELETEACTION);
		if (isWizard) {
			delete.addActionListener(this);
		} else {
			delete.addActionListener(MainWindow.eventDispatcher);
		}
		delete.setBounds(scrollPane.getX(), scrollPane.getY() + SCROLLHEIGHT + 11, BTNWIDTH, BTNHEIGHT);
		delete.setEnabled(false);
		add(delete);

		addNew = new JButton(ADDNEW);
		addNew.setActionCommand(ADDNEWACTION);
		if (isWizard) {
			this.addNew.addActionListener(this);
		} else {
			this.addNew.addActionListener(MainWindow.eventDispatcher);
		}
		addNew.setBounds(scrollPane.getWidth() - BTNWIDTH, delete.getY(), BTNWIDTH, BTNHEIGHT);
		add(addNew);

		addExist = new JButton(ADDEXIST);
		addExist.setActionCommand(ADDEXISTACTION);
		simpleElementCount = MainWindow.albero.getFirstLevelSimpleComponent().size();
		if (simpleElementCount <= 0) {
			addExist.setEnabled(false);
		}
		if (isWizard) {
			addExist.addActionListener(this);
		} else {
			addExist.addActionListener(MainWindow.eventDispatcher);
		}
		addExist.setBounds(addNew.getX() - BTNWIDTH - 10, delete.getY(), BTNWIDTH, BTNHEIGHT);
		add(addExist);
	}

	protected void setOpzioni(Vector<ComponenteSemplice> opzioni) {
		try{
			model.clear();
			for (int i = 0; i < opzioni.size(); i++) {
				model.add(i, opzioni.get(i));
			}
		
		}
		catch(NullPointerException e){}
	}

	protected Vector<ComponenteSemplice> getOpzioni() {
		Vector<ComponenteSemplice> options = new Vector<ComponenteSemplice>();
		for (int i = 0; i < list_components.getModel().getSize(); i++) {
			options.add((ComponenteSemplice) list_components.getModel().getElementAt(i));
		}
		return options;
	}

	protected Vector<Integer> getOriginalIndexes() {
		return originalIndexes;
	}

	protected Vector<Integer> getNewIndexes() {
		return newIndexes;
	}

	protected void addNewComponent(ComponenteSemplice comp) {
		model.add(model.getSize(), comp);
		list_components.setSelectedIndex(model.getSize() - 1);
	}

	protected void addExistingComponent(int indexToMove, ComponenteSemplice comp) {
		model.add(model.getSize(), comp);
		list_components.setSelectedIndex(model.getSize() - 1);
		if (isWizard) {
			originalIndexes.add(indexToMove);
			newIndexes.add(model.getSize() - 1);
		}
		updateButtonAddExisting();
	}

	protected void updateButtonAddExisting() {
		if (isWizard) {
			if ((simpleElementCount - originalIndexes.size()) <= 0) {
				addExist.setEnabled(false);
			}
		}
		else {
			if (MainWindow.albero.getFirstLevelSimpleComponent().size() > 0) {
				addExist.setEnabled(true);
			}
			else {
				addExist.setEnabled(false);
			}
		}
	}
	
	protected void removeExistingComponent(int i) {
		if (isWizard) {
			if (newIndexes.contains(i)) {
				int indice = newIndexes.indexOf(i);
				// NOTA: usare removeElementAt e NON remove -> il primo
				// riduce la grandezza di Vector il secondo no!
				newIndexes.removeElementAt(indice);
				// sistemo i newindexes degli elementi successivi a quello rimosso
				// NOTA: li scorro nuovamente tutti perche nell'alternative l'ordine puo variare
				for (int j=0; j<newIndexes.size(); j++) {
					if (newIndexes.get(j)>i) {
						newIndexes.set(j, newIndexes.get(j)-1);
					}
				}
				originalIndexes.removeElementAt(indice);
				if ((simpleElementCount - originalIndexes.size()) > 0) {
					addExist.setEnabled(true);
				}
			}
		}
		if(i<model.size())
			model.removeElementAt(i);
		manageDelBtn();
	}
	
	protected boolean redify() {
		if (isEmpty()) {
			scrollPane.setBorder(new LineBorder(new Color(255, 0, 0), 1, true));// bordo rosso
			return true;
		} else {
			scrollPane.setBorder(new LineBorder(new Color(184, 207, 229), 1, true));// bordo normale
			return false;
		}
	}
	
	protected boolean isEmpty() {
		if (list_components.getModel().getSize() == 0) {
			return true;
		}
		return false;
	}
	
	protected boolean isCorrect() {
		return !redify();
	}

	/**
	 * {@inheritDoc}
	 */
	public void valueChanged(ListSelectionEvent e) {
		manageDelBtn();
	}

	private void manageDelBtn() {
		if (list_components.isSelectionEmpty()) {
			delete.setEnabled(false);
		} else {
			delete.setEnabled(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ADDNEWACTION)) {
			AddNew an = new AddNew((Window) this.getTopLevelAncestor(), Wizard.name.getText());
			if (an.showDialog()) {
				addNewComponent(an.getComponente());
			}
		} else if (e.getActionCommand().equals(ADDEXISTACTION)) {
			AddExisting ae = new AddExisting((Window) this.getTopLevelAncestor(), Wizard.name.getText(), originalIndexes);
			if (ae.showDialog()) {
				addExistingComponent(ae.getIndexesNodesToMove(), ae.getComponente());
			}
		} else if (e.getActionCommand().equals(DELETEACTION)) {
			// NOTA: devo farlo al contrario dal maggiore al minore altrimenti
			// cambiano gli indici degli elementi!
			for (int i = list_components.getSelectedIndices().length - 1; i >= 0; i--) {
				removeExistingComponent(list_components.getSelectedIndices()[i]);
			}
		}
	}

}
