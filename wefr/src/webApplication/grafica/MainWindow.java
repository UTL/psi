package webApplication.grafica;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;

import webApplication.grafica.TreePanel;

import javax.swing.BoxLayout;

public class MainWindow extends JFrame {/*
										 * implements WindowListener,
										 * ActionListener, DocumentListener
										 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// DEFAULT DIRECTORY
	protected static final String DEFAULTVALUE = System.getProperty("user.home");
	protected static String defSLDir;
	protected static String defTextDir;
	protected static String defImageDir;

	// FRAME TITLE
	private static final String JFRAMETITLE = "EUD-MAMBA - ";
	private static String defaultTitle = "Nuovo progetto ";
	private static int initProjNum = 1;

	// PANELS
	private static JPanel contentPane;
	protected static TreePanel albero;
	protected static PropertiesPanel properties;
	protected static StatusBar statusBar;

	// MENU ITEM
	private static JMenuItem mntmNew;
	private static JMenuItem mntmOpen;
	private static JMenuItem mntmSave;
	private static JMenuItem mntmOptions;
	private static JMenuItem mntmUndo;
	private static JMenuItem mntmRedo;
	private static JMenuItem mntmCopy;
	private static JMenuItem mntmCut;
	private static JMenuItem mntmPaste;
	private static JMenuItem mntmAdd;
	private static JMenuItem mntmDel;
	private static JMenuItem mntmExit;

	// BOTTONI:
	protected static JButton btnNew;
	protected static JButton btnOpen;
	protected static JButton btnSave;
	protected static JButton btnUndo;
	protected static JButton btnRedo;
	protected static JButton btnCopy;
	protected static JButton btnCut;
	protected static JButton btnPaste;
	protected static JButton btnAdd;
	protected static JButton btnDel;
	protected static JButton btnGenXML;

	// ACTIONCOMMAND
	protected static final String OPENCOMMAND = "Open";
	protected static final String SAVECOMMAND = "Save";
	protected static final String EXITCOMMAND = "Exit";
	protected static final String GENERATEXMLCOMMAND="Generate Website";

	// ICONE:
	protected static final String BASEPATH = "icon/";
	private static final String NEWICON = "new.png";
	private static final String OPENICON = "open.png";
	private static final String SAVEICON = "save.png";
	private static final String ENUNDOICON = "undo.png";
	private static final String DISUNDOICON = "disundo.png";
	private static final String ENREDOICON = "redo.png";
	private static final String DISREDOICON = "disredo.png";
	private static final String ENCOPYICON = "copy.png";
	private static final String DISCOPYICON = "discopy.png";
	private static final String ENCUTICON = "cut.png";
	private static final String DISCUTICON = "discut.png";
	private static final String ENPASTEICON = "paste.png";
	private static final String DISPASTEICON = "dispaste.png";
	private static final String ADDICON = "add.png";
	private static final String ENREMOVEICON = "remove.png";
	private static final String DISREMOVEICON = "disremove.png";
	private static final String ENGENERATEXML = "genXML.png";
	private static final String DISGENERATEXML = "disgenXML.png";

	// COMBOBOX
	protected static final String[] necessity = { "Necessary", "Indifferent",
			"Expendable" };
	protected static final String[] emphasis = { "Greatly", "Normally",
			"Not at all" };

	// GESTORE EVENTI
	protected static EventDispatcher eventDispatcher;

	private static final String BASEELEMENTNAME = "Element";
	protected static int count = 0;

	protected ButtonsBar buttonsBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {

		defSLDir = DEFAULTVALUE;
		defTextDir = DEFAULTVALUE;
		defImageDir = DEFAULTVALUE;

		setTitle(JFRAMETITLE + defaultTitle + initProjNum);
		initProjNum++;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 728, 523);
		setResizable(false);

		// centro la finestra
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = getWidth();
		int h = getHeight();
		setLocation((screenDim.width - w) / 2, (screenDim.height - h) / 2);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		initPanelTree();
		eventDispatcher = new EventDispatcher();
		
		albero.getTree().addFocusListener(eventDispatcher);
		
		// setta la barra del menu
		setJMenuBar(new MenuPanel());

		// genera la barra dei bottoni
		buttonsBar = new ButtonsBar();
		contentPane.add(buttonsBar);

		properties = new PropertiesPanel();
		contentPane.add(properties);
		
		statusBar = new StatusBar();
		contentPane.add(statusBar);
		
		MainWindow.albero.getTree().setCellRenderer(new CustomCellRenderer());
	}

	private void initPanelTree() {
		albero = new TreePanel();
		albero.setBounds(15, 63, 222, 378);
		albero.setLayout(new BoxLayout(albero, BoxLayout.X_AXIS));
		contentPane.add(albero);
	}

	/**
	 * Attiva/Disattiva la funzionalita Undo
	 * 
	 * @param state
	 *            Lo stato della funzione
	 */
	protected static void undoState(boolean state) {
		btnUndo.setEnabled(state);
		mntmUndo.setEnabled(state);
	}

	/**
	 * Attiva/Disattiva la funzionalita Redo
	 * 
	 * @param state
	 *            Lo stato della funzione
	 */
	protected static void redoState(boolean state) {
		btnRedo.setEnabled(state);
		mntmRedo.setEnabled(state);
	}

	/**
	 * Attiva/Disattiva la funzionalita di Copia
	 * 
	 * @param state
	 *            Lo stato della funzione
	 */
	protected static void copyState(boolean state) {
		btnCopy.setEnabled(state);
		mntmCopy.setEnabled(state);
	}

	/**
	 * Attiva/Disattiva la funzione di Taglia
	 * 
	 * @param state
	 *            Lo stato della funzione
	 */
	protected static void cutState(boolean state) {
		btnCut.setEnabled(state);
		mntmCut.setEnabled(state);
	}

	/**
	 * Attiva/Disattiva la funzione di Paste
	 * 
	 * @param state
	 *            Lo stato della funzione
	 */
	protected static void pasteState(boolean state) {
		btnPaste.setEnabled(state);
		mntmPaste.setEnabled(state);
	}

	/**
	 * Attiva/Disattiva la funzionalita di Delete
	 * @param state	Lo stato della funzione
	 */
	protected static void removeState(boolean state) {
		btnDel.setEnabled(state);
		mntmDel.setEnabled(state);
	}

	/**
	 * Crea un nuovo progetto
	 */
	protected void newProject() {
		properties.showProperties(null);
		setTitle(JFRAMETITLE + defaultTitle + initProjNum);
		initProjNum++;
	}

	/**
	 * Chiude la finestra
	 */
	protected void exitProject() {
		dispose();
		System.exit(0);
	}

	/**
	 * Salva il progetto
	 */
	protected void saveProject() {
		JFileChooser fc = new JFileChooser(defSLDir);
		fc.addChoosableFileFilter(new EUDFileFilter());
		fc.setAcceptAllFileFilterUsed(false);
		int choice = fc.showSaveDialog(null);
		if (choice == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String path = file.getPath();
			if (!file.getName().endsWith(EUDFileFilter.EXTENSION)) {
				path = path + "." + EUDFileFilter.EXTENSION;
			}
			try {
				ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(path));
				outStream.writeObject(albero.getComponenti());
				outStream.writeObject(defImageDir);
				outStream.writeObject(defTextDir);
				outStream.writeObject(defSLDir);
				outStream.close();
				if (file.getName().lastIndexOf(".")==-1) {
					setTitle(JFRAMETITLE + file.getName().substring(0, file.getName().length()));
				} else {
					setTitle(JFRAMETITLE + file.getName().substring(0, file.getName().lastIndexOf(".")));
				}				
				initProjNum = 1;
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "File " + file.getName() + " cannot be saved in selected directory", "Error saving data", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "File " + file.getName() + " cannot be saved", "Error saving data", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Carica un progetto esistente
	 */
	protected void loadProject() {
		JFileChooser fc = new JFileChooser(defSLDir);
		fc.addChoosableFileFilter(new EUDFileFilter());
		fc.setAcceptAllFileFilterUsed(false);
		int choice = fc.showOpenDialog(null);
		if (choice == JFileChooser.APPROVE_OPTION) {
			try {
				ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()));
				@SuppressWarnings({"unchecked" })
				Vector<Componente> temp = (Vector<Componente>) inStream.readObject();
				setTitle(JFRAMETITLE + fc.getSelectedFile().getName().substring(0, fc.getSelectedFile().getName().lastIndexOf(".")));
				initProjNum = 1;
				//TODO caricare gli elementi del file
//				ObjectInputStream aStream = null;

//				albero.getComponenti();
//				aStream = new ObjectInputStream(new FileInputStream(fcLoad.getFile()));
				
				//Soluzione per questo warning
				//http://stackoverflow.com/questions/509076/how-do-i-address-unchecked-cast-warnings
//				Vector <Componente> temp = (Vector <Componente>) aStream.readObject();
				albero.clear();
				albero.setComponenti(temp);
				defImageDir = (String) inStream.readObject();
				defTextDir = (String) inStream.readObject();
				defSLDir = (String) inStream.readObject();
				
				inStream.close();
				properties.showProperties(null);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, "File " + fc.getSelectedFile().getName() + " not found", "Unexpected error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "The file \"" + fc.getSelectedFile().getName() + "\"\n isn't a EUDMamba project or it is corrupted", "Error loading data", JOptionPane.ERROR_MESSAGE);
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(this, "Error in reading file \"" + fc.getSelectedFile().getName() + "\"\n", "Error loading data", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public static boolean isPathCorrect(String path){
		File daControllare = new File(path);
		if(daControllare.isFile() && daControllare.canRead())
			return true;
		return false;
	}
	
	protected static String setDefaultName() {
		String defaultName;
		do {
			 defaultName = BASEELEMENTNAME + count;
			count++;
		} while (MainWindow.albero.nameExists(defaultName));
		return defaultName;
	}

	protected static class MenuPanel extends JMenuBar {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3377449617130242987L;

		protected static final String OPTIONSCOMMAND = "optionopanel";

		private final static String NEW = "New";
		private final static String FILE = "File";
		private final static String OPEN = "Load";
		private final static String SAVE = "Save";
		private final static String OPTIONS = "Preferences";
		private final static String EXIT = "Exit";
		private final static String EDIT = "Edit";
		private final static String UNDO = "Undo";
		private final static String REDO = "Redo";
		private final static String CUT = "Cut";
		private final static String COPY = "Copy";
		private final static String PASTE = "Paste";
		private final static String ADD = "Add element";
		private final static String DELETE = "Delete element";

		private JMenuBar menuBar;
		private JMenu menuFile;

		protected MenuPanel() {
			super();
			buildMenuFile(menuBar);
			buildMenuEdit(menuBar);
		}

		private void buildMenuFile(JMenuBar mb) {
			menuFile = new JMenu(FILE);
			add(menuFile);

			mntmNew = new JMenuItem(NEW);
			mntmNew.setActionCommand(TreePanel.NewAction.NEWCOMMAND);
			mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
			mntmNew.addActionListener(eventDispatcher);
			menuFile.add(mntmNew);

			mntmOpen = new JMenuItem(OPEN);
			mntmOpen.setActionCommand(MainWindow.OPENCOMMAND);
			mntmOpen.addActionListener(eventDispatcher);
			menuFile.add(mntmOpen);

			mntmSave = new JMenuItem(SAVE);
			mntmSave.setActionCommand(MainWindow.SAVECOMMAND);
			mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
			mntmSave.addActionListener(eventDispatcher);
			menuFile.add(mntmSave);

			menuFile.add(new JSeparator());

			mntmOptions = new JMenuItem(OPTIONS);
			mntmOptions.setActionCommand(OPTIONSCOMMAND);
			mntmOptions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_MASK));
			mntmOptions.addActionListener(eventDispatcher);
			menuFile.add(mntmOptions);

			menuFile.add(new JSeparator());

			mntmExit = new JMenuItem(EXIT);
			mntmExit.addActionListener(eventDispatcher);
			menuFile.add(mntmExit);
		}

		private void buildMenuEdit(JMenuBar mb) {
			JMenu mnEdit = new JMenu(EDIT);
			add(mnEdit);

			mntmUndo = new JMenuItem(UNDO);
			mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
			mntmUndo.setActionCommand(TreePanel.UndoAction.UNDOCOMMAND);
			mntmUndo.addActionListener(eventDispatcher);
			mntmUndo.setEnabled(false);
			mnEdit.add(mntmUndo);

			mntmRedo = new JMenuItem(REDO);
			mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
			mntmRedo.setActionCommand(TreePanel.RedoAction.REDOCOMMAND);
			mntmRedo.addActionListener(eventDispatcher);
			mntmRedo.setEnabled(false);
			mnEdit.add(mntmRedo);

			mnEdit.add(new JSeparator());

			mntmCut = new JMenuItem(CUT);
			mntmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
			mntmCut.setActionCommand((String) TransferHandler.getCutAction().getValue(Action.NAME));
			mntmCut.addActionListener(eventDispatcher);
			mntmCut.setEnabled(false);
			mnEdit.add(mntmCut);

			mntmCopy = new JMenuItem(COPY);
			mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
			mntmCopy.setActionCommand((String) TransferHandler.getCopyAction().getValue(Action.NAME));
			mntmCopy.addActionListener(eventDispatcher);
			mntmCopy.setEnabled(false);
			mnEdit.add(mntmCopy);

			mntmPaste = new JMenuItem(PASTE);
			mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
			mntmPaste.setActionCommand((String) TransferHandler.getPasteAction().getValue(Action.NAME));
			mntmPaste.addActionListener(eventDispatcher);
			mntmPaste.setEnabled(false);
			mnEdit.add(mntmPaste);

			mnEdit.add(new JSeparator());

			mntmAdd = new JMenuItem(ADD);
			mntmAdd.setActionCommand(TreePanel.AddAction.ADDCOMMAND);
			mntmAdd.addActionListener(eventDispatcher);
			mnEdit.add(mntmAdd);

			mntmDel = new JMenuItem(DELETE);
			mntmDel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
			mntmDel.setActionCommand(TreePanel.RemoveAction.REMOVECOMMAND);
			mntmDel.addActionListener(eventDispatcher);
			mntmDel.setEnabled(false);
			mnEdit.add(mntmDel);
		}

	}

	private static class ButtonsBar extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1615509761000662933L;

		private ButtonsBar() {
			setBounds(5, 0, 710, 37);
			setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			setLayout(null);

			Icon enabledIcon = new ImageIcon(BASEPATH + NEWICON);
			btnNew = new JButton(enabledIcon);
			btnNew.setActionCommand(TreePanel.NewAction.NEWCOMMAND);
			btnNew.setToolTipText("New");
			btnNew.setBounds(12, 4, 30, 30);
			add(btnNew);
			btnNew.addActionListener(eventDispatcher);

			enabledIcon = new ImageIcon(BASEPATH + OPENICON);
			btnOpen = new JButton(enabledIcon);
			btnOpen.setActionCommand(OPENCOMMAND);
			btnOpen.setToolTipText("Open");
			btnOpen.setBounds(45, 4, 30, 30);
			add(btnOpen);
			btnOpen.addActionListener(eventDispatcher);

			enabledIcon = new ImageIcon(BASEPATH + SAVEICON);
			btnSave = new JButton(enabledIcon);
			btnSave.setActionCommand(SAVECOMMAND);
			btnSave.setToolTipText("Save");
			btnSave.setBounds(78, 4, 30, 30);
			add(btnSave);
			btnSave.addActionListener(eventDispatcher);

			enabledIcon = new ImageIcon(BASEPATH + ENUNDOICON);
			Icon disabledIcon = new ImageIcon(BASEPATH + DISUNDOICON);
			btnUndo = new JButton(enabledIcon);
			btnUndo.setDisabledIcon(disabledIcon);
			btnUndo.setActionCommand(TreePanel.UndoAction.UNDOCOMMAND);
			btnUndo.setEnabled(false);
			btnUndo.setToolTipText("Undo");
			btnUndo.setBounds(120, 4, 30, 30);
			btnUndo.addActionListener(eventDispatcher);
			add(btnUndo);

			enabledIcon = new ImageIcon(BASEPATH + ENREDOICON);
			disabledIcon = new ImageIcon(BASEPATH + DISREDOICON);
			btnRedo = new JButton(enabledIcon);
			btnRedo.setDisabledIcon(disabledIcon);
			btnRedo.setActionCommand(TreePanel.RedoAction.REDOCOMMAND);
			btnRedo.setEnabled(false);
			btnRedo.setToolTipText("Redo");
			btnRedo.setBounds(153, 4, 30, 30);
			btnRedo.addActionListener(eventDispatcher);
			add(btnRedo);

			enabledIcon = new ImageIcon(BASEPATH + ENCOPYICON);
			disabledIcon = new ImageIcon(BASEPATH + DISCOPYICON);
			btnCopy = new JButton(enabledIcon);
			btnCopy.setDisabledIcon(disabledIcon);
			btnCopy.setEnabled(false);
			btnCopy.setToolTipText("Copy");
			btnCopy.setBounds(195, 4, 30, 30);
			btnCopy.setActionCommand((String) TransferHandler.getCopyAction().getValue(Action.NAME));
			btnCopy.addActionListener(eventDispatcher);
//			btnCopy.addFocusListener(eventDispatcher);
			add(btnCopy);

			enabledIcon = new ImageIcon(BASEPATH + ENCUTICON);
			disabledIcon = new ImageIcon(BASEPATH + DISCUTICON);
			btnCut = new JButton(enabledIcon);
			btnCut.setDisabledIcon(disabledIcon);
			btnCut.setActionCommand((String) TransferHandler.getCutAction().getValue(Action.NAME));
			btnCut.setEnabled(false);
			btnCut.setToolTipText("Cut");
			btnCut.setBounds(228, 4, 30, 30);
			btnCut.addActionListener(eventDispatcher);
			add(btnCut);

			enabledIcon = new ImageIcon(BASEPATH + ENPASTEICON);
			disabledIcon = new ImageIcon(BASEPATH + DISPASTEICON);
			btnPaste = new JButton(enabledIcon);
			btnPaste.setDisabledIcon(disabledIcon);
			btnPaste.setActionCommand((String) TransferHandler.getPasteAction().getValue(Action.NAME));
			btnPaste.setEnabled(false);
			btnPaste.setToolTipText("Paste");
			btnPaste.setBounds(261, 4, 30, 30);
			btnPaste.addActionListener(eventDispatcher);
			add(btnPaste);

			enabledIcon = new ImageIcon(BASEPATH + ADDICON);
			btnAdd = new JButton(enabledIcon);
			btnAdd.setActionCommand(TreePanel.AddAction.ADDCOMMAND);
			btnAdd.addActionListener(eventDispatcher);
			btnAdd.setToolTipText("Add");
			btnAdd.setBounds(313, 4, 30, 30);
			add(btnAdd);

			enabledIcon = new ImageIcon(BASEPATH + ENREMOVEICON);
			disabledIcon = new ImageIcon(BASEPATH + DISREMOVEICON);
			btnDel = new JButton(enabledIcon);
			btnDel.setDisabledIcon(disabledIcon);
			btnDel.setEnabled(false);
			btnDel.setActionCommand(TreePanel.RemoveAction.REMOVECOMMAND);
			btnDel.setToolTipText("Delete");
			btnDel.setBounds(346, 4, 30, 30);
			btnDel.addActionListener(eventDispatcher);
			add(btnDel);
			
			enabledIcon = new ImageIcon(BASEPATH + ENREMOVEICON);
			disabledIcon = new ImageIcon(BASEPATH + DISREMOVEICON);
			btnGenXML = new JButton("Testo di prova", enabledIcon);
			enabledIcon = new ImageIcon(BASEPATH + ENGENERATEXML);
			disabledIcon = new ImageIcon(BASEPATH + DISGENERATEXML);
			btnGenXML = new JButton(GENERATEXMLCOMMAND, enabledIcon);
			btnGenXML.setDisabledIcon(disabledIcon);
			btnGenXML.setEnabled(false);
			btnGenXML.setActionCommand(GENERATEXMLCOMMAND);
			btnGenXML.setToolTipText("Create the new Web page");
			btnGenXML.setBounds(501, 4, 187, 30);
			btnGenXML.addActionListener(eventDispatcher);
			btnGenXML.setFont(new Font("Arial Black", Font.PLAIN, btnGenXML.getFont().getSize()+1 ));
			add(btnGenXML);
		}

	}

	protected static class PropertiesPanel extends JPanel implements DocumentListener, ListDataListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2712196620314697384L;

		private static final String TITLE = " Properties ";
		private static final String EMPTYSELECTION = "Select an element to show its properties";
		private static final String EMPTYSELECTIONTOOLTIP = "Select an element from the left panel or create e new one to show its properties here";

		private JPanel empty_panel;
		private JPanel element_properties_panel;
		private JPanel id_panel;
		private JPanel presentation_panel;
		private JPanel content_panel;
		protected PannelloText pannelloText;
		protected PannelloImage pannelloImage;
		protected PannelloLink pannelloLink;
		protected PannelloComp pannelloComp;
		protected PannelloAlt pannelloAlt;

		protected static final String PRESENTATIONPANELTITLE = " Presentation ";
		protected static final String IDPANELTITLE = " ID ";
		private static final String CONTENTPANELTITLE = " Content ";

		private static final String NAME = "Name:";
		private static final String NAMETOOLTIP = "The name of the element";
		private static final String CATEGORY = "Category:";
		private static final String CATEGORYTOOLTIP = "The category at which the element belongs";
		private static final String TYPE = "Type:";
		private static final String TYPETOOLTIP = "The type of the element";
		private static final String IMPORTANCE = "Necessity:";
		private static final String IMPORTANCETOOLTIP = "How much this element is necessary";
		private static final String EMPHASIS = "Emphasizes:";
		private static final String EMPHASISTOOLTIP = "How much emphasis has the element";

		protected JTextField textField_Name;
		protected JTextField textField_Type;
		protected JTextField textField_Category;
		protected JComboBox comboBox_Visibility;
		protected final static String IMP = "imp";
		protected JComboBox comboBox_Emphasis;
		protected final static String EMP = "emp";

		protected PropertiesPanel() {
			super();
			setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229), 1, true), TITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			setLayout(null);
			setBounds(249, 49, 466, 392);
			setToolTipText(EMPTYSELECTIONTOOLTIP);

			initEmptyPanel();

			initElementPropertiesPanel();

			add(empty_panel);
			repaint();
		}

		private void initEmptyPanel() {
			empty_panel = new JPanel();
			empty_panel.setBounds(113, 181, 240, 30);
			JLabel emptyAdvice = new JLabel(EMPTYSELECTION);
			emptyAdvice.setEnabled(false);
			empty_panel.add(emptyAdvice);
		}

		private void initElementPropertiesPanel() {
			element_properties_panel = new JPanel();
			element_properties_panel.setLayout(null);
			element_properties_panel.setBounds(10, 20, 446, 362);

			buildPresentationPanel();

			buildIDPanel();

			buildContentPanel();
		}

		private void buildIDPanel() {
			id_panel = new JPanel();
			id_panel.setLayout(null);
			id_panel.setBounds(2, 2, 199, 125);
			id_panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), IDPANELTITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			element_properties_panel.add(id_panel);

			JLabel lblName = new JLabel(NAME);
			lblName.setBounds(12, 41, 51, 15);
			id_panel.add(lblName);

			textField_Name = new JTextField();
			textField_Name.setToolTipText(NAMETOOLTIP);
			textField_Name.setBounds(67, 40, 120, 19);
			textField_Name.getDocument().addDocumentListener(this);
			textField_Name.addFocusListener(eventDispatcher);
			id_panel.add(textField_Name);

			JLabel lblType = new JLabel(TYPE);
			lblType.setBounds(12, 71, 51, 15);
			id_panel.add(lblType);

			textField_Type = new JTextField();
			textField_Type.setToolTipText(TYPETOOLTIP);
			textField_Type.setEditable(false);
			textField_Type.setColumns(10);
			textField_Type.setBounds(67, 70, 120, 19);
			id_panel.add(textField_Type);
		}

		private void buildPresentationPanel() {
			presentation_panel = new JPanel();
			presentation_panel.setLayout(null);
			presentation_panel.setBounds(207, 2, 237, 125);
			presentation_panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), PRESENTATIONPANELTITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			element_properties_panel.add(presentation_panel);

			JLabel lblCategory = new JLabel(CATEGORY);
			lblCategory.setBounds(15, 21, 81, 19);
			presentation_panel.add(lblCategory);

			textField_Category = new JTextField();
			textField_Category.setToolTipText(CATEGORYTOOLTIP);
			textField_Category.setBounds(107, 19, 112, 24);
			textField_Category.addFocusListener(eventDispatcher);
			presentation_panel.add(textField_Category);

			JLabel lblEmphasize = new JLabel(EMPHASIS);
			lblEmphasize.setBounds(15, 54, 81, 19);
			presentation_panel.add(lblEmphasize);

			comboBox_Emphasis = new JComboBox(emphasis);
			comboBox_Emphasis.setToolTipText(EMPHASISTOOLTIP);
			comboBox_Emphasis.setActionCommand(EMP);
			comboBox_Emphasis.setBounds(107, 52, 112, 24);
			presentation_panel.add(comboBox_Emphasis);

			JLabel lblImportance = new JLabel(IMPORTANCE);
			lblImportance.setBounds(15, 87, 81, 19);
			presentation_panel.add(lblImportance);

			comboBox_Visibility = new JComboBox(necessity);
			comboBox_Visibility.setToolTipText(IMPORTANCETOOLTIP);
			comboBox_Visibility.setActionCommand(IMP);
			comboBox_Visibility.setBounds(107, 85, 112, 24);
			presentation_panel.add(comboBox_Visibility);
		}

		private void buildContentPanel() {
			content_panel = new JPanel();
			content_panel.setLayout(null);
			content_panel.setBounds(2, 135, 442, 225);
			content_panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), CONTENTPANELTITLE, TitledBorder.LEADING,	TitledBorder.TOP, null, new Color(51, 51, 51)));
			element_properties_panel.add(content_panel);

			pannelloText = new PannelloText();
			pannelloText.setBounds(12, 20, pannelloText.getWidth(), pannelloText.getHeight());
			pannelloText.textArea.getDocument().addDocumentListener(this);
			pannelloText.textArea.addFocusListener(eventDispatcher);
			pannelloImage = new PannelloImage();
			pannelloImage.setBounds(12, 20, pannelloImage.getWidth(), pannelloImage.getHeight());
			pannelloImage.imagepath.getDocument().addDocumentListener(this);
			pannelloImage.imagepath.addFocusListener(eventDispatcher);
			pannelloLink = new PannelloLink();
			pannelloLink.setBounds(12, 20, pannelloLink.getWidth(), pannelloLink.getHeight());
			pannelloLink.urlPath.getDocument().addDocumentListener(this);
			pannelloLink.urlText.getDocument().addDocumentListener(this);
			pannelloLink.urlPath.addFocusListener(eventDispatcher);
			pannelloLink.urlText.addFocusListener(eventDispatcher);
			pannelloComp = new PannelloComp(false);
			pannelloComp.setBounds(12, 20, pannelloComp.getWidth(), pannelloComp.getHeight());
			pannelloComp.list_components.getModel().addListDataListener(this);
			pannelloAlt = new PannelloAlt(false);
			pannelloAlt.setBounds(12, 20, pannelloAlt.getWidth(), pannelloAlt.getHeight());
			pannelloAlt.list_components.getModel().addListDataListener(this);
		}

		protected void showProperties(DisabledNode node) {
//		protected void showProperties(Componente comp) {
			if (node == null) {
//			if (comp == null) {
				setToolTipText(EMPTYSELECTIONTOOLTIP);
				remove(element_properties_panel);
				add(empty_panel);
				revalidate();
				repaint();
			} else {
				setToolTipText(null);
				remove(empty_panel);
				updateProperties(node);
//				updateProperties(comp);
				add(element_properties_panel);
				revalidate();
				repaint();
			}
		}

		private void updateProperties(DisabledNode node) {
			Componente comp = (Componente) node.getUserObject();
//		private void updateProperties(Componente comp) {
			// rimuovo i listener per non scatenare eventi di modifica dei campi
			removeListeners();
			
			textField_Name.setText(comp.getNome());
			Utils.redify(textField_Name, Utils.isBlank(textField_Name)||(albero.getPathForName(textField_Name.getText())).size()>1);
			textField_Category.setText(comp.getCategoria());
			Utils.redify(textField_Category, Utils.isBlank(textField_Category));
			textField_Type.setText(comp.getType());
			comboBox_Emphasis.setSelectedIndex(comp.getEnfasi());
			comboBox_Visibility.setSelectedIndex(comp.getVisibilita());
			if (comp.getType().equals(Testo.TEXTTYPE)) {
				pannelloText.setText(((Testo) comp).getTesto());
				content_panel.removeAll();
				content_panel.add(pannelloText);
				Utils.redify(pannelloText.textArea, !pannelloText.isCorrect());
				if(!pannelloText.isCorrect()) {
					pannelloText.textArea.setToolTipText(PannelloText.ERRORTEXTTOOLTIP);
				} else {
					pannelloText.textArea.setToolTipText(PannelloText.TEXTTOOLTIP);
				}
			} else if (comp.getType().equals(Immagine.IMAGETYPE)) {
				pannelloImage.setPath(((Immagine) comp).getPath());
				content_panel.removeAll();
				content_panel.add(pannelloImage);
				Utils.redify(pannelloImage.imagepath, !pannelloImage.isCorrect());
				if(!pannelloImage.isCorrect()) {
					pannelloImage.imagepath.setToolTipText(PannelloImage.IMAGEPATHERRORTOOLTIP);
				} else {
					pannelloImage.imagepath.setToolTipText(PannelloImage.IMAGEPATHTOOLTIP);
				}
			} else if (comp.getType().equals(Link.LINKTYPE)) {
				pannelloLink.setPath(((Link) comp).getUri());
				pannelloLink.setText(((Link) comp).getTesto());
				content_panel.removeAll();
				content_panel.add(pannelloLink);
				Utils.redify(pannelloLink.urlText, !pannelloLink.isTextCorrect());
				if (!pannelloLink.isTextCorrect()) {
					pannelloLink.urlText.setToolTipText(PannelloLink.ERRORTEXTTOOLTIP);
				} else {
					pannelloLink.urlText.setToolTipText(PannelloLink.TEXTTOOLTIP);
				}
			} else if (comp.getType().equals(ComponenteComposto.COMPOSTOTYPE)) {
				content_panel.removeAll();
				pannelloComp.setOpzioni(((ComponenteComposto) comp).getOpzioni());
				pannelloComp.updateButtonAddExisting();
				content_panel.add(pannelloComp);
			} else if (comp.getType().equals(ComponenteAlternative.ALTERNATIVETYPE)) {
				content_panel.removeAll();
				pannelloAlt.setOpzioni(((ComponenteAlternative) comp).getOpzioni());
				pannelloAlt.updateButtonAddExisting();
				content_panel.add(pannelloAlt);
			}
			
			//riaggiungo i listener per ascoltare eventi di modifica dei campi
			addListeners();
		}
		
		/**
		 * Aggiungo i listener agli elementi del pannello delle proprieta
		 */
		protected void addListeners() {
			textField_Name.getDocument().addDocumentListener(eventDispatcher);
			textField_Name.getDocument().addDocumentListener(this);
			textField_Category.getDocument().addDocumentListener(eventDispatcher);
			textField_Category.getDocument().addDocumentListener(this);
			comboBox_Emphasis.addActionListener(eventDispatcher);
			comboBox_Visibility.addActionListener(eventDispatcher);
			pannelloText.textArea.getDocument().addDocumentListener(eventDispatcher);
			pannelloText.textArea.getDocument().addDocumentListener(this);
			pannelloImage.imagepath.getDocument().addDocumentListener(eventDispatcher);
			pannelloImage.imagepath.getDocument().addDocumentListener(this);
			pannelloLink.urlPath.getDocument().addDocumentListener(eventDispatcher);
			pannelloLink.urlText.getDocument().addDocumentListener(eventDispatcher);
			pannelloLink.urlPath.getDocument().addDocumentListener(this);
			pannelloLink.urlText.getDocument().addDocumentListener(this);
		}

		/**
		 * Rimuove i listener agli elementi del pannello delle proprieta
		 */
		protected void removeListeners() {
			textField_Name.getDocument().removeDocumentListener(eventDispatcher);
			textField_Name.getDocument().removeDocumentListener(this);
			textField_Category.getDocument().removeDocumentListener(eventDispatcher);
			textField_Category.getDocument().removeDocumentListener(this);
			comboBox_Emphasis.removeActionListener(eventDispatcher);
			comboBox_Visibility.removeActionListener(eventDispatcher);
			pannelloText.textArea.getDocument().removeDocumentListener(eventDispatcher);
			pannelloText.textArea.getDocument().removeDocumentListener(this);
			pannelloImage.imagepath.getDocument().removeDocumentListener(eventDispatcher);
			pannelloImage.imagepath.getDocument().removeDocumentListener(this);
			pannelloLink.urlPath.getDocument().removeDocumentListener(eventDispatcher);
			pannelloLink.urlText.getDocument().removeDocumentListener(eventDispatcher);
			pannelloLink.urlPath.getDocument().removeDocumentListener(this);
			pannelloLink.urlText.getDocument().removeDocumentListener(this);
		}
		
		private void changeEvent(DocumentEvent e) {
			if (e.getDocument() == textField_Name.getDocument()) {
				Utils.redify(textField_Name, (Utils.isBlank(textField_Name)));
			} else if (e.getDocument() == textField_Category.getDocument()) {
				Utils.redify(textField_Category, (Utils.isBlank(textField_Category)));
			} else if (e.getDocument() == pannelloImage.imagepath.getDocument()) {
				Utils.redify(pannelloImage.imagepath, !pannelloImage.isCorrect());
				if(!pannelloImage.isCorrect()) {
					pannelloImage.imagepath.setToolTipText(PannelloImage.IMAGEPATHERRORTOOLTIP);
				} else {
					pannelloImage.imagepath.setToolTipText(PannelloImage.IMAGEPATHTOOLTIP);
				}
			} else if (e.getDocument() == pannelloText.textArea.getDocument()) {
				Utils.redify(pannelloText.textArea, !pannelloText.isCorrect());
				if(!pannelloText.isCorrect()) {
					pannelloText.textArea.setToolTipText(PannelloText.ERRORTEXTTOOLTIP);
				} else {
					pannelloText.textArea.setToolTipText(PannelloText.TEXTTOOLTIP);
				}
			} else if (e.getDocument() == pannelloLink.urlPath.getDocument()) {
				Utils.redify(pannelloLink.urlPath, !pannelloLink.isPathCorrect());
				if (!pannelloLink.isPathCorrect()) {
					pannelloLink.urlPath.setToolTipText(PannelloLink.ERRORPATHTOOLTIP);
				} else {
					pannelloLink.urlPath.setToolTipText(PannelloLink.PATHTOOLTIP);
				}
			} else if (e.getDocument() == pannelloLink.urlText.getDocument()) {
				Utils.redify(pannelloLink.urlText, !pannelloLink.isTextCorrect());
				if (!pannelloLink.isTextCorrect()) {
					pannelloLink.urlText.setToolTipText(PannelloLink.ERRORTEXTTOOLTIP);
				} else {
					pannelloLink.urlText.setToolTipText(PannelloLink.TEXTTOOLTIP);
				}
			}
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			changeEvent(e);

		}


		@Override
		public void insertUpdate(DocumentEvent e) {
			changeEvent(e);

		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			changeEvent(e);

		}

		@Override
		public void intervalAdded(ListDataEvent e) {
			contentsChanged(e);
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			contentsChanged(e);
		}

		@Override
		public void contentsChanged(ListDataEvent e) {
			DisabledNode node = ((DisabledNode)albero.getTree().getSelectionPath().getLastPathComponent());
			if (((Componente)((DisabledNode)albero.getTree().getSelectionPath().getLastPathComponent()).getUserObject()).getType().equals(ComponenteAlternative.ALTERNATIVETYPE)) {
				if (!pannelloAlt.isCorrect()) {
					node.isCorrect = false;
				} else {
					node.isCorrect = true;
				}
			} else {
				if (!pannelloComp.isCorrect()) {
					node.isCorrect = false;
				} else {
					node.isCorrect = true;
				}
			}
		}
	}
	
	protected static class StatusBar extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4038960744094640548L;
		
		private Shape circle;
		
		protected StatusBar() {
			super();
			setBounds(0,452,722,20);
			setBorder(new LineBorder(Color.GRAY));
			circle = new Ellipse2D.Float(getWidth()-25f,2.5f,15,15);
		}
		
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D ga = (Graphics2D) g;
			ga.draw(circle);
			if (albero.isCorrect()) {
				ga.setPaint(Color.GREEN);
			} else {
				ga.setPaint(Color.RED);
			}
			ga.fill(circle);
		}
		
	}
	
}
