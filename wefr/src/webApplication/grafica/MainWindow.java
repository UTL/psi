package webApplication.grafica;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;

import webApplication.grafica.TreePanel;


import javax.swing.BoxLayout;

public class MainWindow extends JFrame implements TreeSelectionListener, WindowListener, MyEventClassListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JPanel contentPane;
	
	
	
	
	private static JTextArea editorPane_text;
	private static JTextField textField_imagepath;
	private static JPanel content_panel;
	private static JPanel panel_composite;
	private static PannelloAlt pannello_alterplus;
	private static PannelloComp pannello_comp;
	private static JPanel errorePath;
	private static JPanel erroreTestoLink;
	private static JPanel erroreUrl;
	private static JButton button_1;
	private static String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	
	public static final int LOADSAVE = 0;
	public static final int IMAGE = 1;
	public static final int TEXT = 2;
	
	
	
	private static Options frameOptions  = new Options();
	
	private CustomFCSave fcSave = new CustomFCSave(frameOptions, this);
	private CustomFCLoad fcLoad = new CustomFCLoad(frameOptions, this);
	private CustomFCImage fcImage=new CustomFCImage(frameOptions, this);
	private CustomFCText fcText=new CustomFCText(frameOptions, this);
	
	private static Componente focused;

	private static Testo focusedTxt;
	private static Immagine focusedImg;
	private static Link focusedLnk;
	private static final String URL_REGEX =
            "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);
	
	
	private static final String[] categorie = { "Necessary", "Indifferent", "Expendable"}; //FIXME Andrebbero rese globali per tutte le classi??
	private static final String[] importanze = { "Greatly", "Normally", "Not at all"}; //FIXME Andrebbero rese globali per tutte le classi?? E ne mancano 2 che non ricordo

	private static JTextField textField_linktext;
	private static JTextField textField_url;
	
	private static final String PANEL_TXT="panel_text";
	private static final String PANEL_IMG="panel_image";
	private static final String PANEL_LNK="panel_link";
	public static final String PANEL_ALT="panel_alternative";
	private static final String PANEL_CMP="panel_composite";
	
	public static final int MOVE_UP = -1;
	public static final int MOVE_DOWN = +1;
	
	private GenericProperties genProperties;

	
	
	public MainWindowData data = new MainWindowData();
	private static TreePanel albero;

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
	/**
	 * 
	 */
	public MainWindow() {
		setTitle("EUD-MAMBA");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 728, 502);
		setResizable(false);
		data.setThisWindow(this);
		
		frameOptions = new Options();
		frameOptions.addEventListener(this);
		frameOptions.addWindowListener(this);

		setJMenuBar(this.new MenuPanel().CreatePanel()); //aggiungo la menubar
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panelButtonsBar = new JPanel();
		
		initPanelButtonsBar(panelButtonsBar);

		JPanel properties = new JPanel();
		JPanel presentation_panel = new JPanel();
		JPanel id_panel = new JPanel();
		
		initEmptyPanels(properties, presentation_panel,id_panel ); //Crea i JPanel vuoti
		
		contentPane.add(properties);
		
		genProperties= this.new GenericProperties(presentation_panel, id_panel); //Popola i JPanel dall'inner class
		
		
		initContentPanel(properties);

		initPanelImage();
		
		initPanelComposite();
		
		initPanelAlternative();

		initPanelPanelLink();
		

		initPanelText();

		initPanelTree();
		
		

		// TODO rimuovere invocazione a testing concluso
		popolaOggetti();

	}

	private void initPanelTree() {
		albero = new TreePanel();
		albero.setBounds(15, 63, 222, 378);
		contentPane.add(albero);
		albero.setLayout(new BoxLayout(albero, BoxLayout.X_AXIS));
		albero.getTree().addTreeSelectionListener(this);
	}

	private void initPanelText() {
		JPanel panel_text = new JPanel();
		content_panel.add(panel_text, PANEL_TXT);
		panel_text.setLayout(null);

		JLabel label_namecontent = new JLabel("Name:");
		label_namecontent.setBounds(12, 5, 45, 15);
		panel_text.add(label_namecontent);
		
		editorPane_text = new JTextArea();
		JScrollPane scrollingArea = new JScrollPane(editorPane_text);
		scrollingArea.setBounds(12, 32, 408, 156);
		editorPane_text.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateTextContent();
			}
			public void removeUpdate(DocumentEvent e) {
				updateTextContent();
			}
			public void insertUpdate(DocumentEvent e) {
				updateTextContent();

			}
		});
		
		//editorPane_text.setBounds(12, 32, 408, 156);
		panel_text.add(scrollingArea);
	}

	private void initPanelPanelLink() {
		JPanel panel_link = new JPanel();
		content_panel.add(panel_link, PANEL_LNK);
		panel_link.setLayout(null);

		JLabel lbl_linktxt = new JLabel("Link text:");
		lbl_linktxt.setBounds(13, 14, 78, 15);
		panel_link.add(lbl_linktxt);
		

		textField_linktext = new JTextField();
		textField_linktext.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {

				updateLinkText();
			}
			public void removeUpdate(DocumentEvent e) {
				updateLinkText();
				// text was deleted
			}
			public void insertUpdate(DocumentEvent e) {
				updateLinkText();

				// text was inserted
			}
		});
		
		textField_linktext.setColumns(10);
		textField_linktext.setBounds(93, 12, 313, 19);
		panel_link.add(textField_linktext);

		JLabel lbl_url = new JLabel("URL:");
		lbl_url.setBounds(12, 47, 78, 15);
		panel_link.add(lbl_url);
		
		textField_url = new JTextField();
		
		textField_url.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateLinkUrl();
			}
			public void removeUpdate(DocumentEvent e) {
				updateLinkUrl();
			}
			public void insertUpdate(DocumentEvent e) {
				updateLinkUrl();

			}
		});
		textField_url.setColumns(10);
		textField_url.setBounds(93, 45, 313, 19);
		panel_link.add(textField_url);
		
		erroreTestoLink = new JPanel();
		erroreTestoLink.setBorder(new LineBorder(Color.RED));
		erroreTestoLink.setToolTipText("");
		erroreTestoLink.setBounds(90, 9, 319, 24);
		panel_link.add(erroreTestoLink);
		
		erroreUrl = new JPanel();
		erroreUrl.setBorder(new LineBorder(Color.RED));
		erroreUrl.setToolTipText("");
		erroreUrl.setBounds(90, 42, 319, 24);
		panel_link.add(erroreUrl);
	}

	private void initPanelAlternative() {
		pannello_alterplus = new PannelloAlt(this, frameOptions);
		//buildPanelAlternative(panel_alternative, button_up, button_down, button_delFromAlt, button_AddExisAlt, list_alternative);
		content_panel.add(pannello_alterplus, PANEL_ALT);
	}

	private void initPanelComposite() {
		panel_composite = new JPanel();

		content_panel.add(panel_composite, PANEL_CMP);
		panel_composite.setLayout(null);
		pannello_comp = new PannelloComp(this, frameOptions);
		pannello_comp.bott_del.setLocation(33, 165);
		pannello_comp.setSize(426, 196);
		pannello_comp.setLocation(-16, 1);
		panel_composite.add(pannello_comp);
	}

	private void initContentPanel(JPanel properties) {
		// TODO mettere immagine priorità per alternative, up e down
		content_panel = new JPanel();
		content_panel.setBorder(new TitledBorder(new LineBorder(new Color(184,
				207, 229)), " Content ", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));
		content_panel.setBounds(12, 155, 442, 225);
		properties.add(content_panel);
		content_panel.setLayout(new CardLayout(0, 0));
	}

	private void initPanelImage() {
		JPanel panel_image = new JPanel();
		content_panel.add(panel_image, PANEL_IMG);
		panel_image.setLayout(null);

		JLabel label_2 = new JLabel("File path:");
		label_2.setBounds(22, 12, 91, 14);
		panel_image.add(label_2);
				
		textField_imagepath = new JTextField();
		textField_imagepath.setToolTipText("Path of the image file");
		textField_imagepath.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				
				updateImagePath();
			}
			public void removeUpdate(DocumentEvent e) {
				updateImagePath();
			// text was deleted
			}
			public void insertUpdate(DocumentEvent e) {
				updateImagePath();

			// text was inserted
			}
			});
		
		textField_imagepath.setBounds(22, 42, 292, 22);
		panel_image.add(textField_imagepath);
		
		JButton button_browseImg = new JButton("Browse\r\n");
		button_browseImg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				fcImage.showDialog();
				fcImage.setJTFPath(textField_imagepath);
				
			}
		});
		button_browseImg.setBounds(331, 39, 89, 29);
		panel_image.add(button_browseImg);
		
		errorePath = new JPanel();
		errorePath.setToolTipText("The file doesn't exist or is not readable");
		errorePath.setBorder(new LineBorder(Color.RED));
		errorePath.setBounds(19, 38, 300, 30);
		panel_image.add(errorePath);
	}

	private void initPanelButtonsBar(JPanel panelButtonsBar) {
		panelButtonsBar.setBounds(5, 0, 710, 37);
		panelButtonsBar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		contentPane.add(panelButtonsBar);
		panelButtonsBar.setLayout(null);

		JButton button = new JButton("");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				setFocus(data.getTxt());
			}
		});
		button.setBounds(12, 4, 30, 30);
		panelButtonsBar.add(button);
		button.setToolTipText("Open");
		button.setIcon(new ImageIcon(
				MainWindow.class
						.getResource("/com/sun/java/swing/plaf/motif/icons/TreeOpen.gif")));

		JButton button_3 = new JButton("");
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setFocus(data.getImg());
			}
		});
		button_3.setToolTipText("Open");
		button_3.setBounds(45, 4, 30, 30);
		panelButtonsBar.add(button_3);

		JButton button_4 = new JButton("");
		button_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setFocus(data.getLnk());
			}
		});
		button_4.setToolTipText("Open");
		button_4.setBounds(78, 4, 30, 30);
		panelButtonsBar.add(button_4);

		button_1 = new JButton("");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				setFocus(data.getAlt());
			}
		});
		button_1.setToolTipText("Open");
		button_1.setBounds(120, 4, 30, 30);
		panelButtonsBar.add(button_1);

		JButton button_2 = new JButton("");
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setFocus(data.getCmp());
			}
		});
		button_2.setToolTipText("Open");
		button_2.setBounds(153, 4, 30, 30);
		panelButtonsBar.add(button_2);

		JButton button_5 = new JButton("");
		button_5.setEnabled(false);
		
		button_5.setIcon(new ImageIcon("/home/enrico/Documenti/PSI/icons/list-add-md.png"));
		button_5.setToolTipText("Open");
		button_5.setBounds(195, 4, 30, 30);
		panelButtonsBar.add(button_5);

		JButton button_6 = new JButton("");
		button_6.setToolTipText("Open");
		button_6.setBounds(228, 4, 30, 30);
		panelButtonsBar.add(button_6);

		JButton addButton = new JButton(".");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addNewWizard();
			}
		});
		
		
		//addButton.setIcon(new ImageIcon(MainWindow.class.getResource("/webApplication/grafica/add_icon.gif")));
		addButton.setToolTipText("Open");
		addButton.setBounds(277, 4, 30, 30);
		panelButtonsBar.add(addButton);

		JButton button_8 = new JButton("");
		//button_8.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/gtk/resources/gtk-cancel-4.png")));
		button_8.setToolTipText("Open");
		button_8.setBounds(310, 4, 30, 30);
		panelButtonsBar.add(button_8);

		JButton btnGenerateWebsite = new JButton("GENERATE WEBSITE");
		
		btnGenerateWebsite.setToolTipText("Open");
		btnGenerateWebsite.setBounds(365, 4, 187, 30);
		boldify(btnGenerateWebsite);
		panelButtonsBar.add(btnGenerateWebsite);
	}
	private void initEmptyPanels(JPanel properties,JPanel presentation_panel, JPanel id_panel ) {
		
		properties.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229), 1, true), " Properties ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		properties.setLayout(null);
		properties.setBounds(249, 49, 466, 392);
		
		presentation_panel.setBorder(new TitledBorder(new LineBorder(new Color(
				184, 207, 229)), " Presentation ", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));
		presentation_panel.setBounds(217, 18, 237, 125);
		properties.add(presentation_panel);
		presentation_panel.setLayout(null);
		
		id_panel.setBounds(12, 18, 193, 125);
		properties.add(id_panel);
		id_panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207,
				229)), " ID ", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(51, 51, 51)));
		id_panel.setLayout(null);
		
	}

	private void addNewWizard() {
		//TODO agganciarci il wizard e non l'addnew
		//TODO andrebbe creata una classe e tolto il codice da qui

		//setEnabled(false);
		//setFocusable(false);
		Wizard nuovo = new Wizard(frameOptions);
		nuovo.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				setEnabled(true);
				setFocusable(true);
			}
		});
		nuovo.addEventListener(new MyEventClassListener(){

			@Override
			public void handleMyEventClassEvent(
					MyEventClass e) {
						setEnabled(true);
						setFocusable(true);

						if(e != null){
							albero.addNode(null,e.getComponente());
							
							
						}
			}

			});

		nuovo.setVisible(true);
	}

	

	

	private void popolaProperties(Testo selected) {
		genProperties.setGenerici(selected, "Text");
		editorPane_text.setText(selected.getTesto());

		setContentLayout(PANEL_TXT);
	}

	private void popolaProperties(Immagine selected) {
		genProperties.setGenerici(selected, "Image");
		textField_imagepath.setText(selected.getPath());

		setContentLayout(PANEL_IMG);

	}
	
	private void popolaProperties(Link selected){
		genProperties.setGenerici(selected,"Link");
		textField_url.setText(selected.getUri());

		textField_linktext.setText(selected.getTesto());

		setContentLayout(PANEL_LNK);
	}

	private void popolaProperties(ComponenteAlternative selected) {
		genProperties.setGenerici(selected, "Alternative");
		
		pannello_alterplus.setComponent(selected);
		setContentLayout(PANEL_ALT);

	}
	
	private void popolaProperties(ComponenteComposto selected){

		pannello_comp.setComponent(selected);

		genProperties.setGenerici(selected,"Composite");
		
		setContentLayout(PANEL_CMP);

	}
	
	
	public static void setContentLayout(String panel){
		CardLayout cl = (CardLayout)(content_panel.getLayout());
        cl.show(content_panel, panel);
	}

	 //metodo per popolare oggetti per farci prove
	 private void popolaOggetti()	{ data.setImg(new Immagine("immagineeee", "immagini!",
	 1,0,"/questo/e/un/path")); data.getImg().setNome("immagineeee");
	 data.getImg().setCategoria("immagini!"); data.getImg().setPath("/questo/e/un/path");
	 data.getImg().setVisibilita(2); data.getImg().setEnfasi(0);
	 data.setLnk(new Link("", "!", 0,0,"", "")); data.getLnk().setNome("linkozzo");
	 data.getLnk().setCategoria("altra"); data.getLnk().setVisibilita(1);
	 data.getLnk().setUri("www.url.it"); data.getLnk().setTesto("clicca qui"); data.getLnk().setEnfasi(2);
	 data.setTxt(new Testo("", "", 0, 0, "")); data.getTxt().setNome("testo");
	 data.getTxt().setCategoria("testi!"); data.getTxt().setVisibilita(0); data.getTxt().setEnfasi(1);
	 data.getTxt().setTesto("scriviamoci tanta roba");
	 data.setAlt(new ComponenteAlternative("alternativa", "alterniamoci", 2, 0));
	 data.setCmp(new ComponenteComposto("Compostato", "compi", 1, 1));
	 data.getCmp().aggiungiOpzione(data.getImg()); data.getCmp().aggiungiOpzione(data.getLnk());
	 data.getCmp().aggiungiOpzione(data.getTxt());
	 }
	 


	// metodo per togliere il focus all'oggetto precedente
	private static void unFocus() {
		focused = null;
		focusedTxt = null;
		focusedImg = null;
		focusedLnk = null;
	}

	public void setFocus(Componente selected){
		if (selected.getType()==Testo.TEXTTYPE)
			setFocus((Testo)selected);
		else if (selected.getType()==Immagine.IMAGETYPE)
			setFocus((Immagine)selected);
		else if (selected.getType()==ComponenteComposto.COMPOSTOTYPE)
			setFocus((ComponenteComposto)selected);
		else if (selected.getType()==ComponenteAlternative.ALTERNATIVETYPE)
			setFocus((ComponenteAlternative)selected);
		else if (selected.getType()==Link.LINKTYPE)
			setFocus((Link)selected);
	}
	
	private void setFocus(Immagine selected) {
		unFocus();
		focusedImg = selected;
		setFocusGeneric(selected);
		popolaProperties(selected);

	}

	private void setFocus(Testo selected) {
		unFocus();
		focusedTxt = selected;
		setFocusGeneric(selected);
		popolaProperties(selected);

	}

	private void setFocus(Link selected) {
		unFocus();
		focusedLnk = selected;
		setFocusGeneric(selected);
		popolaProperties(selected);

	}

	private void setFocus(ComponenteComposto selected) {
		unFocus();
		setFocusGeneric(selected);
		popolaProperties(selected);

	}

	private void setFocus(ComponenteAlternative selected) {
		unFocus();
		setFocusGeneric(selected);
		popolaProperties(selected);

	}

	private static void setFocusGeneric(Componente comp) {
		focused = comp;
	}

	

	private void updateTextContent() {
		if (focusedTxt != null)
			focusedTxt.setTesto(editorPane_text.getText());
	}
	
	private boolean checkLinkText(){
		//TODO bisognerebbe controllare i caratteri da escapare
		if(textField_linktext.getText().trim().length()!=0){
			textField_linktext.setToolTipText("Text of the link"); //TODO tooltip inutile al 100%
			erroreTestoLink.setVisible(false);
			return true;
		}
		else {
			erroreTestoLink.setVisible(true); 
			textField_linktext.setToolTipText("Link text can't be empty");
		}
		return false;
	}
	
	private void updateLinkText(){
		if(focusedLnk!= null)
			focusedLnk.setTesto(textField_linktext.getText());
		checkLinkText();
			//TODO sarebbe bello sollevare un'eccezione ogni volta che questo if non si verifica (e anche per tutti gli altri metodi di update)
		}
	
	private void updateLinkUrl(){
		if(focusedLnk!= null)
			focusedLnk.setUri(textField_url.getText());
		checkLinkUrl();

	}
	
	private boolean checkLinkUrl(){
		if(isUrlCorrect(textField_url.getText())){
			erroreUrl.setVisible(false);
			textField_url.setToolTipText("URL of the link");
			return true;
		}
		else {
			erroreUrl.setVisible(true);
			textField_url.setToolTipText("The URL is not correct");
		}
		return false;
	}
	
	private boolean isUrlCorrect(String text) {
		//TODO serve una regex per controllare le url!
		Matcher urlMatcher = URL_PATTERN.matcher(text);
        if (!urlMatcher.matches()){
            return false;}
		return true;
	}


	private void updateImagePath() {
		if(focusedImg!= null)
			focusedImg.setPath(textField_imagepath.getText());
		checkImagePath();
	}

	private boolean checkImagePath() {
		if(isPathCorrect(textField_imagepath.getText())){
			errorePath.setVisible(false);
			textField_imagepath.setToolTipText("Path of the image file");
			return true;
		}
		else {
			errorePath.setVisible(true);
			textField_imagepath.setToolTipText("The file doesn't exist or is not readable");
		}
		return false;
	}
	
	
	public static boolean isPathCorrect(String path){
		//TODO fare prove con files e dir verificare che funzioni
		File daControllare = new File(path);
		if(daControllare.isFile() && daControllare.canRead())
			return true;
		return false;
	}
	
	private void boldify(JButton button) {
		Font newButtonFont = new Font(button.getFont().getName(), Font.ITALIC
				+ Font.BOLD, button.getFont().getSize() + 1);
		button.setFont(newButtonFont);
	}

	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		
		JTree tree = albero.getTree();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if (node == null || node.isRoot())	{
			tree.clearSelection();
			//non � stato selezionato nulla o � stata selezionata la radice;
			return;
			}
		
		setFocus((Componente)node.getUserObject());
		
	}


	@Override
	public void handleMyEventClassEvent(MyEventClass e) {
		setEnabled(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}
	class MenuPanel implements ActionListener, MyEventClassListener, MouseListener{

		private final static String NEW = "New";
		private final static String FILE = "File";
		private final static String OPEN = "Load"; //forse meglio "load"?
		private final static String SAVE = "Save";
		private final static String EXIT = "Exit";
		private final static String EDIT = "Edit";
		private final static String UNDO = "Undo";
		private final static String REDO = "Redo";
		private final static String CUT = "Cut";
		private final static String COPY = "Copy";
		private final static String PASTE = "Paste";
		private final static String OPTIONS = "Options";

		private JMenuBar menuBar;
		private JMenu menuFile;
		private JMenuItem menuItemNew;
		private JMenuItem menuItemOpen;
		private JMenuItem menuItemSave;
		private JMenuItem menuItemExit;

		private JMenuBar CreatePanel(){
			menuBar = new JMenuBar();
			

			buildMenuFile(menuBar);

			buildMenuEdit(menuBar);

			buildMenuOptions(menuBar);
			return menuBar;
		}

		private void buildMenuOptions(JMenuBar mb) {
			JMenu mnOptions = new JMenu(OPTIONS);
			mb.add(mnOptions);

			JMenuItem mntmOptions = new JMenuItem("Image directory");

			mntmOptions.addActionListener(this);
			mnOptions.add(mntmOptions);
		}

		private void buildMenuEdit(JMenuBar mb) {
			JMenu mnEdit = new JMenu(EDIT);
			mnEdit.setEnabled(false);
			mb.add(mnEdit);

			JMenuItem mntmUndo = new JMenuItem(UNDO);
			mntmUndo.setEnabled(false);
			mnEdit.add(mntmUndo);

			JMenuItem mntmRedo = new JMenuItem(REDO);
			mnEdit.add(mntmRedo);
			mntmRedo.setEnabled(false);

			JSeparator separator_1 = new JSeparator();
			mnEdit.add(separator_1);

			JMenuItem mntmCut = new JMenuItem(CUT);
			mnEdit.add(mntmCut);
			mntmCut.setEnabled(false);

			JMenuItem mntmCopy = new JMenuItem(COPY);
			mnEdit.add(mntmCopy);
			mntmCopy.setEnabled(false);

			JMenuItem mntmPaste = new JMenuItem(PASTE);
			mnEdit.add(mntmPaste);
			mntmPaste.setEnabled(false);
		}

		private void buildMenuFile(JMenuBar mb) {
			menuFile = new JMenu(FILE);
			mb.add(menuFile);

			menuItemNew = new JMenuItem(NEW);
			menuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
			menuItemNew.addMouseListener(this);
			menuFile.add(menuItemNew);

			menuItemOpen = new JMenuItem(OPEN);
			menuItemOpen.addMouseListener(this);
			menuFile.add(menuItemOpen);

			menuItemSave = new JMenuItem(SAVE);
			menuItemSave.addMouseListener(this);
			menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
			menuFile.add(menuItemSave);

			JSeparator separator = new JSeparator();
			menuFile.add(separator);

			menuItemExit = new JMenuItem(EXIT);
			menuItemExit.addMouseListener(this);
			menuFile.add(menuItemExit);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("click");
			try {
				setEnabled(false);
				//TIP qua probabilmente c'e' la sol http://castever.wordpress.com/2008/07/31/how-to-create-your-own-events-in-java/
				

				frameOptions.setVisible(true);

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		@Override
		public void handleMyEventClassEvent(MyEventClass e) {
			setEnabled(true);

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {}

		@Override
		public void mouseEntered(MouseEvent arg0) {}

		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getSource()==menuItemNew)
				newAction();
			else if (e.getSource()==menuItemOpen)
				loadAction();
			else if(e.getSource()==menuItemExit)
				exitAction();
			else if(e.getSource()==menuItemSave)
				saveAction();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	class GenericProperties implements ActionListener, DocumentListener{
		private  JTextField textField_Name;
		private  JTextField textField_Type;
		private  JTextField textField_Category;
		private  JComboBox comboBox_Importance;
		private  JComboBox comboBox_Emphasize;
		private final static String IMP = "imp";
		private final static String EMP = "emp";
		
		public GenericProperties(JPanel presentation_panel, JPanel id_panel){
			buildIdPanel(id_panel);
			
			buildPresentationPanel(presentation_panel);
		}

		private void buildPresentationPanel(JPanel presentation_panel) {
			

			initPresentationJLabels(presentation_panel);
					
			comboBox_Importance = new JComboBox(importanze);
			comboBox_Importance.setActionCommand(IMP);
			comboBox_Importance.addActionListener(this);
			
			comboBox_Importance.setBounds(111, 49, 112, 24);
			presentation_panel.add(comboBox_Importance);

			textField_Category = new JTextField();
			textField_Category.getDocument().addDocumentListener(this);
			
			textField_Category.setColumns(10);
			textField_Category.setBounds(109, 22, 114, 19);
			presentation_panel.add(textField_Category);

			comboBox_Emphasize = new JComboBox(categorie);
			comboBox_Emphasize.setActionCommand(EMP);
			comboBox_Emphasize.addActionListener(this);
			
			comboBox_Emphasize.setBounds(111, 85, 112, 24);
			presentation_panel.add(comboBox_Emphasize);
		}

		private void initPresentationJLabels(JPanel presentation_panel) {
			JLabel lblCategory = new JLabel("Category:");
			lblCategory.setBounds(12, 24, 81, 15);
			presentation_panel.add(lblCategory);

			JLabel lblEmphasize = new JLabel("Emphasize:");
			lblEmphasize.setBounds(12, 54, 81, 14);
			presentation_panel.add(lblEmphasize);
			
			JLabel lblImportance = new JLabel("Importance:");
			lblImportance.setBounds(12, 86, 97, 15);
			presentation_panel.add(lblImportance);
		}

		private void buildIdPanel(JPanel id_panel) {
			JLabel lblName = new JLabel("Name:");
			lblName.setBounds(12, 40, 51, 15);
			id_panel.add(lblName);

			textField_Name = new JTextField();
			textField_Name.setToolTipText("name");
			
			// TODO mettere check che il nome non sia gia' esistente
			textField_Name.getDocument().addDocumentListener(this);
			textField_Name.setBounds(67, 40, 114, 19);
			id_panel.add(textField_Name);
			textField_Name.setColumns(10);

			JLabel lblType = new JLabel("Type:");
			lblType.setBounds(12, 70, 51, 15);
			id_panel.add(lblType);

			textField_Type = new JTextField();
			textField_Type.setEditable(false);
			textField_Type.setColumns(10);
			textField_Type.setBounds(67, 70, 114, 19);
			id_panel.add(textField_Type);
		}
		
		protected void setGenerici(Componente selected, String type) {
			textField_Name.setText(selected.getNome());

			textField_Category.setText(selected.getCategoria());

			textField_Type.setText(type);

			// TODO verificare che l'ordine sia giusto (che il numero restituito dal
			// getenfasi corrisp a quello del menu a tendina)
			comboBox_Emphasize.setSelectedIndex(selected.getEnfasi());
			comboBox_Importance.setSelectedIndex(selected.getVisibilita());
		}
		
		// TODO verificare se va
		private void setFocusedName() {
			if (focused != null)
				focused.setNome(textField_Name.getText());
		}

		private void setFocusedCategory() {
			if (focused != null)
				focused.setCategoria(textField_Category.getText());
		}

		private void setFocusedEmph() {
			if (focused != null)
				focused.setEnfasi(comboBox_Emphasize.getSelectedIndex());//
		}

		private void setFocusedImpo() {
			if (focused != null)
				focused.setVisibilita(comboBox_Importance.getSelectedIndex());// focused.setVisibilita
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getActionCommand()==IMP)
				setFocusedImpo();
			else if(arg0.getActionCommand()==EMP)
				setFocusedEmph();
			
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			manageDocumentEvent(arg0);
				
			
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			manageDocumentEvent(arg0);
			
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			manageDocumentEvent(arg0);
			
		}
		
		private void manageDocumentEvent(DocumentEvent arg0) {
			if(arg0.getDocument()==textField_Category.getDocument())
				setFocusedCategory();
			else if(arg0.getDocument()==textField_Name.getDocument()){
				setFocusedName();
				albero.getTree().repaint();
			}
		}
		
	}
	private void exitAction() {
		//TODO chiedere se bisogna salvare
		dispose();
		System.exit(0);
	}

	public void saveAction() {
		fcSave.showDialog();
		if(fcSave.getFilePath().length()>0){
		ObjectOutputStream aStream = null;

		try {
			aStream = new ObjectOutputStream(new FileOutputStream(fcSave.getFilePath()));
			albero.getComponenti();
			aStream.writeObject(albero.getComponenti());
			//albero.getTree().getLastSelectedPathComponent()
			aStream.close();
			
		}catch (FileNotFoundException e){
			JOptionPane.showMessageDialog(this, "File "+fcSave.getFile().getName()+" cannot be saved in selected directory",
				    "Error saving data",
				    JOptionPane.ERROR_MESSAGE);
		} 
		
		catch (IOException e) {
			JOptionPane.showMessageDialog(this, "File "+fcSave.getFile().getName()+" cannot be saved",
				    "Error saving data",
				    JOptionPane.ERROR_MESSAGE);
		}
		System.out.println("vai");
		}
		
	}

	private void newAction(){
		//TODO creare nuovo JTree
	}

	private void loadAction(){
		fcLoad.showDialog();
		ObjectInputStream aStream = null;
		if(fcLoad.getFile()!=null)
		{
			try {
				albero.getComponenti();
				aStream = new ObjectInputStream(new FileInputStream(fcLoad.getFile()));
				albero.setComponenti((Vector <Componente>) aStream.readObject());
				aStream.close();
				//System.out.println("Il tipo del primo nodo e': "+read.getType());
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, "File "+fcLoad.getFile().getName()+" not found",
					    "Unexpected error",
					    JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this,
					    "The file \"" +fcLoad.getFile().getName() +"\"\n isn't a EUDMamba project or it is corrupted",
					    "Error loading data",
					    JOptionPane.ERROR_MESSAGE);
			
				// TODO Auto-generated catch block
				//e.printStackTrace();
				
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(this, "Unexpected error",
					    "Error loading data",
					    JOptionPane.ERROR_MESSAGE);
			
		}
			
		if (fcLoad.getFilePath().length()>0)
			//TODO aprire un JDialog per chiedere di salvare se il vecchio proj e' stato modificato
			data.setCurrentProject(fcLoad.getFilePath());
		
		
	}
		}

	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		setEnabled(true);
		setFocusable(true);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}
	
	
	
         
	
}














