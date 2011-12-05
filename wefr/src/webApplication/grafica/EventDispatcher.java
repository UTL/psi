package webApplication.grafica;

import java.awt.Cursor;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import javax.swing.JMenuItem;

import webApplication.business.Componente;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;
import webApplication.grafica.TreePanel.AddAction;
import webApplication.grafica.TreePanel.ChangeFieldAction;
import webApplication.grafica.TreePanel.ChangePriorityAction;
import webApplication.grafica.TreePanel.MoveAction;
import webApplication.grafica.TreePanel.NewAction;
import webApplication.grafica.TreePanel.RedoAction;
import webApplication.grafica.TreePanel.RemoveAction;
import webApplication.grafica.TreePanel.UndoAction;

/**
 * Gestore aggregato di tutti gli eventi che si occupa di redistribuirli
 * correttamente
 * 
 * @author Andrea
 * 
 */
public class EventDispatcher implements ActionListener, PropertyChangeListener, DocumentListener, TreeSelectionListener, FlavorListener, KeyEventDispatcher, WindowListener, FocusListener {

	private static final String CLEARALL = "This will delete the current work.\n Do you wish to continue?";
	private static final String DELETEMESSAGE = "You are going to delete ";
	private static final String CONFIRMMESSAGE = "\n Do you want to continue?";
	private static final String NOTEMPTYMESSAGE = " is not empty.\n Deleting this element will delete all the elements inside.\n Do you want to continue?";
	private static final String CLOSEMESSAGE = "Any unsaved changes made to the current work will be lost.\n Do you really want to close the current work";

	private static boolean haveCutted = false;

	private TreePanel panel;
	private UndoAction undoAction;
	private RedoAction redoAction;
	private UndoManager undoManager;
	private JComponent focusOwner;
	
	private JComponent focus;

	/**
	 * Il costruttore generale
	 */
	protected EventDispatcher() {
		panel = MainWindow.albero;
		undoAction = panel.new UndoAction();
		redoAction = panel.new RedoAction();
		undoManager = panel.getUndoManager();
		// NOTA: non ho bisogno di gestire le azioni di copia/incolla/paste dato
		// che il TransferHandler le gestisce autonomamente

		// mi metto in ascolto per capire quando abilitare il bottone di paste
		((TreeTransferHandler) panel.getTree().getTransferHandler()).getClipboard().addFlavorListener(this);

		// mi metto in ascolto sugli eventi relativi all'albero
		panel.getTree().addTreeSelectionListener(this);
		
		// per gestire gli eventi della tastiera->shortcut
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		((MainWindow)panel.getTopLevelAncestor()).addWindowListener(this);
		manager.addKeyEventDispatcher(this);
		manager.addPropertyChangeListener("permanentFocusOwner", this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void propertyChange(PropertyChangeEvent e) {
		// recupera il focus corrente che mi serve per capire cosa fare
		Object o = e.getNewValue();
		if (o instanceof JComponent) {
			focusOwner = (JComponent) o;
		} else {
			focusOwner = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		// Prima gestisco una eventuale azione di incolla
		if (e.getActionCommand().equals((String) TransferHandler.getPasteAction().getValue(Action.NAME))) {
			// gestisco il comando incolla
			haveCutted = false;
			if (!(focus instanceof JTree)) {
				((TreeTransferHandler) panel.getTree().getTransferHandler()).cancelCut();
			}
			//			focusOwner = MainWindow.albero.getTree();
			// forzo il focus sull'albero per attivare il transferhandler dell'albero
			System.out.println("Focus: "+focus.getClass().getCanonicalName());
			Action a = focus.getActionMap().get(e.getActionCommand());
			if (a != null) {
				a.actionPerformed(new ActionEvent(focus, ActionEvent.ACTION_PERFORMED, null));
			}
		} else {
			// Se sto cercando di compiere un'altra azione lasciando un taglia
			// in sospeso: riattivo prima il nodo
			if (haveCutted) {
				((TreeTransferHandler) panel.getTree().getTransferHandler()).cancelCut();
				haveCutted = false;
			}
			if ((e.getActionCommand().equals(TreePanel.NewAction.NEWCOMMAND))) {
				if (e.getSource() instanceof JMenuItem) {
					// NOTA: JMenuItem ha come TopLevelAncestor null cambio la
					// source al bottone di new
					e.setSource(MainWindow.btnNew);
				}
				int choice = 0;
				if (((DisabledNode) MainWindow.albero.getTree().getModel().getRoot()).getChildCount() != 0) {
					// se l'albero non e vuoto chiedo conferma prima di resettare tutto
					choice = JOptionPane.showConfirmDialog(((JButton)e.getSource()).getTopLevelAncestor(), CLEARALL, "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				}
				if (choice == JOptionPane.YES_OPTION) {
//				if (choice == 0) {
					NewAction newOrCloseAction = panel.new NewAction();
					newOrCloseAction.actionPerformed(e);
				}
			} else if (e.getActionCommand().equals(MainWindow.OPENCOMMAND)) {
				if (e.getSource() instanceof JMenuItem) {
					// NOTA: JMenuItem ha come TopLevelAncestor null cambio la
					// source al bottone di open
					e.setSource(MainWindow.btnOpen);
				}
				((MainWindow) ((JButton) e.getSource()).getTopLevelAncestor()).loadProject();
			} else if (e.getActionCommand().equals(MainWindow.SAVECOMMAND)) {
				if (e.getSource() instanceof JMenuItem) {
					// NOTA: JMenuItem ha come TopLevelAncestor null cambio la
					// source al bottone di save
					e.setSource(MainWindow.btnSave);
				}
				((MainWindow) ((JButton) e.getSource()).getTopLevelAncestor()).saveProject();
			} else if (e.getActionCommand().equals(MainWindow.MenuPanel.OPTIONSCOMMAND)) {
				new Options(((MainWindow) MainWindow.btnSave.getTopLevelAncestor()));
			} else if (e.getActionCommand().equals(MainWindow.EXITCOMMAND)) {
				if (e.getSource() instanceof JMenuItem) {
					// NOTA: JMenuItem ha come TopLevelAncestor null cambio la
					// source ad un bottone qualunque
					e.setSource(MainWindow.btnNew);
				}
				int choice = 0;
				if (!MainWindow.albero.isEmpty()) {
					choice = JOptionPane.showConfirmDialog(((MainWindow) ((JButton) e.getSource()).getTopLevelAncestor()), CLOSEMESSAGE, "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				}
				if (choice == JOptionPane.YES_OPTION) {
//				if (choice == 0) {
					((MainWindow) ((JButton) e.getSource()).getTopLevelAncestor()).exitProject();
				}
			} else if (e.getActionCommand().equals(TreePanel.UndoAction.UNDOCOMMAND)) {
				undoAction.actionPerformed(e);
			} else if (e.getActionCommand().equals(TreePanel.RedoAction.REDOCOMMAND)) {
				redoAction.actionPerformed(e);
			} else if (e.getActionCommand().equals((String) TransferHandler.getCopyAction().getValue(Action.NAME))) {
				// gestisco il comando copia
//				focusOwner = MainWindow.albero.getTree();
				System.out.println("Focus: "+focus.getClass().getCanonicalName());
				// forzo il focus sull'albero per attivare il transferhandler dell'albero
				Action a = focus.getActionMap().get(e.getActionCommand());
				if (a != null) {
					a.actionPerformed(new ActionEvent(focus, ActionEvent.ACTION_PERFORMED, null));
				}
				MainWindow.pasteState(true);
			} else if (e.getActionCommand().equals((String) TransferHandler.getCutAction().getValue(Action.NAME))) {
				// gestisco il comando taglia
				haveCutted = true;
				System.out.println("Focus: "+focus.getClass().getCanonicalName());
				// forzo il focus sull'albero per attivare il transferhandler dell'albero
				Action a = focus.getActionMap().get(e.getActionCommand());
				if (a != null) {
					a.actionPerformed(new ActionEvent(focus, ActionEvent.ACTION_PERFORMED, null));
				}
				MainWindow.pasteState(true);
			} else if (e.getActionCommand().equals(TreePanel.AddAction.ADDCOMMAND)) {
				if (e.getSource() instanceof JMenuItem) {
					// NOTA: JMenuItem ha come TopLevelAncestor null cambio la
					// source al bottone di add
					e.setSource(MainWindow.btnAdd);
				}
				Wizard wz = new Wizard(((MainWindow) ((JComponent) e.getSource()).getTopLevelAncestor()));
				if (wz.showDialog()) {
					AddAction addAction = panel.new AddAction();
					addAction.putValue(TreePanel.AddAction.COMPONENTE, wz.buildNewComponent());
					addAction.putValue(TreePanel.AddAction.ORIGINALINDEXES, wz.getOriginalIndexes());
					addAction.putValue(TreePanel.AddAction.NEWINDEXES, wz.getNewIndexes());
					addAction.putValue(TreePanel.AddAction.PARENTINDEX, -1);
					addAction.actionPerformed(e);
				}
			} else if (e.getActionCommand().equals(TreePanel.RemoveAction.REMOVECOMMAND)) {
				if (e.getSource() instanceof JMenuItem) {
					// NOTA: JMenuItem ha come TopLevelAncestor null cambio la
					// source al bottone di new
					e.setSource(MainWindow.btnDel);
				}
				DisabledNode nodeToRemove = (DisabledNode) MainWindow.albero.getTree().getSelectionPath().getLastPathComponent();
				String name = nodeToRemove.toString();
				// chiede conferma per l'eliminazione del nodo
				String message = DELETEMESSAGE + name + CONFIRMMESSAGE;
				int choice = JOptionPane.showConfirmDialog(((JButton) e.getSource()).getTopLevelAncestor(), message, "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (choice == JOptionPane.YES_OPTION) {
					if (nodeToRemove.getChildCount() != 0) {
						message = name + NOTEMPTYMESSAGE;
						// se il nodo e complesso e non vuoto, chiedo conferma di eliminazione di tutti gli elementi interni
						choice = JOptionPane.showConfirmDialog(((JButton) e.getSource()).getTopLevelAncestor(), message, "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (choice == JOptionPane.NO_OPTION) {
							return;
						}
					}
					RemoveAction removeAction = panel.new RemoveAction();
					removeAction.putValue(RemoveAction.INDEXES, null);
					removeAction.actionPerformed(e);
				}
			} else if (e.getActionCommand().equals(PannelloComp.ADDNEWACTION)) {
				PannelloComp pc = (PannelloComp) ((JButton) e.getSource()).getParent();
				AddNew an = new AddNew(((MainWindow) ((JButton) e.getSource()).getTopLevelAncestor()), ((Componente) ((DisabledNode) MainWindow.albero.getTree().getSelectionPath().getLastPathComponent()).getUserObject()).getNome());
				if (an.showDialog()) {
					// aggiorno la lista visualizzata
					pc.addNewComponent(an.getComponente());

					// aggiungo il nodo all'albero
					AddAction addAction = panel.new AddAction();
					addAction.putValue(TreePanel.AddAction.COMPONENTE, an.getComponente());
					addAction.putValue(TreePanel.AddAction.ORIGINALINDEXES, null);
					addAction.putValue(TreePanel.AddAction.NEWINDEXES, null);
					int parentIndex = MainWindow.albero.getTree().getModel().getIndexOfChild(MainWindow.albero.getTree().getModel().getRoot(), MainWindow.albero.getTree().getSelectionPath().getLastPathComponent());
					addAction.putValue(TreePanel.AddAction.PARENTINDEX, parentIndex);
					addAction.actionPerformed(e);
				}
			} else if (e.getActionCommand().equals(PannelloComp.ADDEXISTACTION)) {
				PannelloComp pc = (PannelloComp) ((JButton) e.getSource()).getParent();
				DisabledNode selectedNode = (DisabledNode) MainWindow.albero.getTree().getSelectionPath().getLastPathComponent();
				AddExisting ae = new AddExisting(((MainWindow) ((JButton) e.getSource()).getTopLevelAncestor()), ((Componente) (selectedNode).getUserObject()).getNome(), new Vector<Integer>());
				if (ae.showDialog()) {
					// aggiorno la lista visualizzata e lo stato del bottone
					pc.addExistingComponent(ae.getIndexesNodesToMove(), ae.getComponente());

					// aggiorno l'albero
					MoveAction moveAction = panel.new MoveAction();
					int parentIndex = ((DisabledNode) MainWindow.albero.getTree().getModel().getRoot()).getIndex(selectedNode);
					moveAction.putValue(TreePanel.MoveAction.OLDPARENTINDEX, -1);
					moveAction.putValue(TreePanel.MoveAction.NEWPARENTINDEX, parentIndex);
					moveAction.putValue(TreePanel.MoveAction.OLDINDEX, ae.getIndexesNodesToMove());
					moveAction.putValue(TreePanel.MoveAction.NEWINDEX, selectedNode.getChildCount());
					moveAction.moveNode();
				}
			} else if (e.getActionCommand().equals(PannelloComp.DELETEACTION)) {
				PannelloComp pc = (PannelloComp) ((JButton) e.getSource()).getParent();
				int choice = JOptionPane.showConfirmDialog(((JButton) e.getSource()).getTopLevelAncestor(), DELETEMESSAGE+"the selected nodes."+CONFIRMMESSAGE, "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (choice == JOptionPane.YES_OPTION) {
					int[] indici = pc.list_components.getSelectedIndices();
					//aggiorno la lista del pannello
					for (int i = indici.length - 1; i >= 0; i--) {
						pc.removeExistingComponent(indici[i]);
					}
					//aggiorno l'albero
					RemoveAction removeAction = panel.new RemoveAction();
					removeAction.putValue(TreePanel.RemoveAction.INDEXES, indici);
					removeAction.actionPerformed(e);
				}
			} else if ((e.getActionCommand().equals(PannelloAlt.UPCOMMAND)) || (e.getActionCommand().equals(PannelloAlt.DOWNCOMMAND))) {
				PannelloAlt pa = (PannelloAlt) ((JButton) e.getSource()).getParent();
				ChangePriorityAction changePriorityAction = panel.new ChangePriorityAction();
				changePriorityAction.putValue(TreePanel.ChangePriorityAction.OLDINDEXES, pa.list_components.getSelectedIndices());
				changePriorityAction.putValue(TreePanel.ChangePriorityAction.PARENTINDEX, ((DisabledNode)MainWindow.albero.getTree().getModel().getRoot()).getIndex((DisabledNode)MainWindow.albero.getTree().getSelectionPath().getLastPathComponent()));
				if (e.getActionCommand().equals(PannelloAlt.UPCOMMAND)) {
					changePriorityAction.putValue(TreePanel.ChangePriorityAction.GAP, -1);
					//aggiorno la lista
					pa.moveElements(pa.list_components.getSelectedIndices(), -1);
				} else {
					changePriorityAction.putValue(TreePanel.ChangePriorityAction.GAP, +1);
					//aggiorno la lista
					pa.moveElements(pa.list_components.getSelectedIndices(), +1);
				}
				changePriorityAction.actionPerformed(e);
			} else if (e.getActionCommand().equals(MainWindow.PropertiesPanel.EMP)) {
				DisabledNode selectedNode = (DisabledNode) MainWindow.albero.getTree().getSelectionPath().getLastPathComponent();
				ChangeFieldAction changeFieldAction = panel.new ChangeFieldAction();
				changeFieldAction.putValue(TreePanel.ChangeFieldAction.FIELD, TreePanel.ChangeFieldAction.EMPHASIS);
				changeFieldAction.putValue(ChangeFieldAction.OLDVALUE, ((Componente)selectedNode.getUserObject()).getEnfasi());
				changeFieldAction.putValue(TreePanel.ChangeFieldAction.NEWVALUE, (((JComboBox)e.getSource()).getSelectedIndex()));
				changeFieldAction.actionPerformed(e);
			} else if (e.getActionCommand().equals(MainWindow.PropertiesPanel.IMP)) {
				DisabledNode selectedNode = (DisabledNode) MainWindow.albero.getTree().getSelectionPath().getLastPathComponent();
				ChangeFieldAction changeFieldAction = panel.new ChangeFieldAction();
				changeFieldAction.putValue(TreePanel.ChangeFieldAction.FIELD, TreePanel.ChangeFieldAction.VISIBILITY);
				changeFieldAction.putValue(ChangeFieldAction.OLDVALUE, ((Componente)selectedNode.getUserObject()).getEnfasi());
				changeFieldAction.putValue(TreePanel.ChangeFieldAction.NEWVALUE, (((JComboBox)e.getSource()).getSelectedIndex()));
				changeFieldAction.actionPerformed(e);
			} else if (e.getActionCommand().equals(MainWindow.GENERATEXMLCOMMAND)) {
				XMLGenerator generator = new XMLGenerator((MainWindow)((JComponent)e.getSource()).getTopLevelAncestor());
				Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
				generator.generateXML();
			}
		}

		// gestisco l'attivazione di Undo e Redo
		MainWindow.undoState(undoManager.canUndo());
		MainWindow.redoState(undoManager.canRedo());
		
		if (focus != null) {
			focus.requestFocusInWindow();
		} else {
			MainWindow.albero.getTree().requestFocusInWindow();
		}
		
		MainWindow.statusBar.repaint();
		MainWindow.albero.revalidate();
	}

	/**
	 * {@inheritDoc}
	 */
	public void changedUpdate(DocumentEvent e) {
		// non mi serve gestirlo
	}

	/**
	 * {@inheritDoc}
	 */
	public void insertUpdate(DocumentEvent e) {
		if (haveCutted) {
			((TreeTransferHandler) panel.getTree().getTransferHandler()).cancelCut();
			haveCutted = false;
		}
		DisabledNode selectedNode = (DisabledNode) MainWindow.albero.getTree().getSelectionPath().getLastPathComponent();
		ChangeFieldAction changeFieldAction = panel.new ChangeFieldAction();
		if (e.getDocument().equals(MainWindow.properties.textField_Name.getDocument())) {
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.FIELD, TreePanel.ChangeFieldAction.NAME);
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.OLDVALUE, ((Componente)selectedNode.getUserObject()).getNome());
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.NEWVALUE, (MainWindow.properties.textField_Name.getText()));
			changeFieldAction.actionPerformed(new ActionEvent(MainWindow.properties.textField_Name, ActionEvent.ACTION_PERFORMED,""));
		} else if (e.getDocument().equals(MainWindow.properties.textField_Category.getDocument())) {
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.FIELD, TreePanel.ChangeFieldAction.CATEGORY);
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.OLDVALUE, ((Componente)selectedNode.getUserObject()).getCategoria());
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.NEWVALUE, (MainWindow.properties.textField_Category.getText()));
			changeFieldAction.actionPerformed(new ActionEvent(MainWindow.properties.textField_Category, ActionEvent.ACTION_PERFORMED,""));
		} else if (e.getDocument().equals(MainWindow.properties.pannelloText.textArea.getDocument())) {
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.FIELD, TreePanel.ChangeFieldAction.TEXT);
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.OLDVALUE, ((Testo)selectedNode.getUserObject()).getTesto());
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.NEWVALUE, (MainWindow.properties.pannelloText.getText()));
			changeFieldAction.actionPerformed(new ActionEvent(MainWindow.properties.pannelloText.textArea, ActionEvent.ACTION_PERFORMED,""));
		} else if (e.getDocument().equals(MainWindow.properties.pannelloImage.imagepath.getDocument())) {
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.FIELD, TreePanel.ChangeFieldAction.IMAGE);
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.OLDVALUE, ((Immagine)selectedNode.getUserObject()).getPath());
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.NEWVALUE, (MainWindow.properties.pannelloImage.getPath()));
			changeFieldAction.actionPerformed(new ActionEvent(MainWindow.properties.pannelloImage.imagepath, ActionEvent.ACTION_PERFORMED,""));
		} else if (e.getDocument().equals(MainWindow.properties.pannelloLink.urlPath.getDocument())) {
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.FIELD, TreePanel.ChangeFieldAction.URLPATH);
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.OLDVALUE, ((Link)selectedNode.getUserObject()).getUri());
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.NEWVALUE, (MainWindow.properties.pannelloLink.getPath()));
			changeFieldAction.actionPerformed(new ActionEvent(MainWindow.properties.pannelloLink.urlPath, ActionEvent.ACTION_PERFORMED,""));
		} else if (e.getDocument().equals(MainWindow.properties.pannelloLink.urlText.getDocument())) {
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.FIELD, TreePanel.ChangeFieldAction.URLTEXT);
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.OLDVALUE, ((Link)selectedNode.getUserObject()).getTesto());
			changeFieldAction.putValue(TreePanel.ChangeFieldAction.NEWVALUE, (MainWindow.properties.pannelloLink.getText()));
			changeFieldAction.actionPerformed(new ActionEvent(MainWindow.properties.pannelloLink.urlText, ActionEvent.ACTION_PERFORMED,""));
		}
		MainWindow.statusBar.repaint();
		MainWindow.albero.getTree().repaint();
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeUpdate(DocumentEvent e) {
		// richiama lo stesso evento di modifica del campo
		insertUpdate(e);
	}

	/**
	 * {@inheritDoc}
	 */
	public void valueChanged(TreeSelectionEvent e) {
		// gestisco i bottoni di cut copy e delete
		if (!(e.getPath().getLastPathComponent() == null) && !((DisabledNode) e.getPath().getLastPathComponent()).isRoot()) {
			// elemento selezionato diverso dalla root
			MainWindow.properties.showProperties((DisabledNode) e.getPath().getLastPathComponent());
//			MainWindow.properties.showProperties((Componente) ((DisabledNode) e.getPath().getLastPathComponent()).getUserObject());
			MainWindow.copyState(true);
			MainWindow.cutState(true);
			MainWindow.removeState(true);
			return;
		}
		// root o nessuna selezione
		MainWindow.properties.showProperties(null);
		MainWindow.albero.getTree().clearSelection();
		MainWindow.copyState(false);
		MainWindow.cutState(false);
		MainWindow.removeState(false);
	}

	/**
	 * {@inheritDoc}
	 */
	public void flavorsChanged(FlavorEvent e) {
		// gestisco il bottone di paste
		MainWindow.pasteState(true);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean dispatchKeyEvent(KeyEvent e) {
		// il pulsante ESC annulla una azione di taglia
		if (((e.getKeyCode()) == KeyEvent.VK_ESCAPE) && (haveCutted==true))	{
			((TreeTransferHandler)MainWindow.albero.getTree().getTransferHandler()).cancelCut();
		}
		return false;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// non utile
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowClosing(WindowEvent e) {
		int choice = 0;
		if (!MainWindow.albero.isEmpty()) {
			//choice = JOptionPane.showConfirmDialog(((MainWindow) e.getWindow()), CLOSEMESSAGE, "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			//new String[] {"Yes", "No"}, "No")
			JOptionPane.showOptionDialog(((MainWindow) e.getWindow()), CLOSEMESSAGE, "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,  null, new String[] {"Yes", "No"}, "No");
		}
		if (choice == JOptionPane.YES_OPTION) {
			((MainWindow) e.getWindow()).exitProject();
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// non utile
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// non utile
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// non utile
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// non utile
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// non utile
	}

	@Override
	public void focusGained(FocusEvent e) {
		if ((e.getComponent() instanceof JTree) || (((e.getOppositeComponent() instanceof JTree) && (!(e.getComponent() instanceof JTextComponent))))) {
			focus = MainWindow.albero.getTree();
			MainWindow.pasteState(((TreeTransferHandler)MainWindow.albero.getTree().getTransferHandler()).getClipboard().getContents(null)!=null);
		} else if (e.getComponent() instanceof JTextComponent) {
			focus = (JComponent) e.getComponent();
			MainWindow.pasteState(Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null)!=null);
		} else if ((e.getOppositeComponent() instanceof JTextComponent) && (!(e.getComponent() instanceof JTree))) {
			focus = (JComponent) e.getOppositeComponent();
			MainWindow.pasteState(Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null)!=null);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		//non mi serve
	}

}
