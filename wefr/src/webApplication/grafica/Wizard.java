package webApplication.grafica;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;

public class Wizard extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8706297359676605479L;

	protected boolean returnValue = false;

	private static final String BASEELEMENTNAME = "Element";
	private static int count = 0;// TODO spostare nel MainWindow

	private static final String BASETITLE = "Create new element";

	private static JPanel contentPane;
	private static JTabbedPane tabbedPane;
	protected static JTextField name;
	private static JTextField category;
	private static JComboBox choice_type;
	private static JComboBox choice_emphasis;
	private static JComboBox choice_visibility;

	private PannelloText pannelloText;
	private PannelloImage pannelloImage;
	private PannelloLink pannelloLink;
	private PannelloAlt pannelloAlt;
	private PannelloComp pannelloComp;

	private static JButton btnExit;
	private static JButton btnBack;
	private static JButton btnNext;
	private static JButton btnDone;

	// TAB TITLE & ICON & TOOLTIP
	private static final String FIRSTSTEPTITLE = "1. Identification";
	private static final String FIRSTSTEPTOOLTIP = "Specify the name and type of the element";

	private static final String SECONDSTEPTITLE = "2. Presentation";
	private static final String SECONDSTEPTOOLTIP = "Specify visualization properties of the element";

	private static final String THIRDSTEPTITLE = "3. Content";
	private static final String THIRDSTEPTOOLTIP = "Specify what the element represents";

	// BUTTONS
	private static final String NEXTTEXT = "Next";
	private static final String EXITTEXT = "Cancel";
	private static final String BACKTEXT = "Back";
	private static final String DONETEXT = "Done";
	private static final String NEXTACTION = "Next";
	private static final String EXITACTION = "Cancel";
	private static final String BACKACTION = "Back";
	protected static final String DONEACTION = "Done";

	private final static String[] tipi = { Testo.TEXTTYPE, Immagine.IMAGETYPE,
			Link.LINKTYPE, ComponenteAlternative.ALTERNATIVETYPE,
			ComponenteComposto.COMPOSTOTYPE };

	/**
	 * Create the frame.
	 */
	public Wizard(JFrame owner) {
		super(owner, ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setTitle(BASETITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 477, 374);

		// centro la finestra
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ownerDim = owner.getSize();
		int w = getWidth();
		int h = getHeight();
		setLocation((screenDim.width - ownerDim.width) / 2 + (ownerDim.width - w) / 2, (screenDim.height - ownerDim.height) / 2	+ (ownerDim.height - h) / 2);

		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.WRAP_TAB_LAYOUT);
		tabbedPane.setBounds(0, 0, 471, 374);
		contentPane.add(tabbedPane);

		// Primo Tabbed Panel
		JPanel firstStep = new JPanel();
		firstStep.setLayout(null);
		tabbedPane.addTab(FIRSTSTEPTITLE, null, firstStep, FIRSTSTEPTOOLTIP);

		createBreadCumb(firstStep, 1);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(125, 109, 46, 14);
		firstStep.add(lblName);

		name = new JTextField(setDefaultName());
		name.setBounds(181, 106, 174, 22);
		// name.setText(setDefaultName());
		// TODO aggiungere il document listener
		firstStep.add(name);

		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(130, 168, 46, 14);
		firstStep.add(lblType);

		choice_type = new JComboBox(tipi);
		choice_type.setBounds(181, 165, 174, 20);
		firstStep.add(choice_type);

		createButtonsBar(firstStep, 1);

		// Secondo Tabbed Panel
		JPanel secondStep = new JPanel();
		secondStep.setLayout(null);
		tabbedPane.addTab(SECONDSTEPTITLE, null, secondStep, SECONDSTEPTOOLTIP);
		tabbedPane.setEnabledAt(1, false);

		createBreadCumb(secondStep, 2);

		JLabel lblCategoryIdentifier = new JLabel("Category identifier:");
		lblCategoryIdentifier.setBounds(56, 94, 108, 14);
		secondStep.add(lblCategoryIdentifier);

		category = new JTextField("Category0");
		category.setBounds(181, 92, 174, 22);
		category.setText("Category0");
		secondStep.add(category);
		// TODO aggiungere il document listener

		JLabel lblImportance = new JLabel("Necessity:");
		lblImportance.setBounds(105, 193, 108, 14);
		secondStep.add(lblImportance);

		choice_visibility = new JComboBox(MainWindow.necessity);
		choice_visibility.setBounds(180, 192, 174, 20);
		choice_visibility.setSelectedIndex(1);
		secondStep.add(choice_visibility);

		JLabel lblEnphasize = new JLabel("Emphasizes:");
		lblEnphasize.setBounds(91, 144, 108, 14);// 195
		secondStep.add(lblEnphasize);

		choice_emphasis = new JComboBox(MainWindow.emphasis);
		choice_emphasis.setBounds(180, 143, 174, 20);// 192
		choice_emphasis.setSelectedIndex(1);
		secondStep.add(choice_emphasis);

		createButtonsBar(secondStep, 2);

		// Terzo Tabbed Panel
		JPanel thirdStep = new JPanel();
		thirdStep.setLayout(null);
		tabbedPane.addTab(THIRDSTEPTITLE, null, thirdStep, THIRDSTEPTOOLTIP);
		tabbedPane.setEnabledAt(2, false);
	}

	// TODO spostare nel MainWindow
	protected String setDefaultName() {
		// FIXME Non ancora funzionante: count deve essere messo in MainWindow e
		// devo incrementarlo solo quando creo un elemento con il nome di
		// default
		String defaultName = BASEELEMENTNAME + count;
		return defaultName;
	}

	private void createBreadCumb(JPanel panel, int currentStep) {
		JLabel lblStep = new JLabel("Step: ");
		lblStep.setBounds(26, 21, 46, 14);
		panel.add(lblStep);

		JPanel stepOneIndicator = new JPanel();
		if (currentStep == 1) {
			stepOneIndicator.setBackground(Color.GREEN);
			stepOneIndicator.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.GREEN, null));
		} else {
			stepOneIndicator.setBackground(SystemColor.control);
			stepOneIndicator.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		}
		stepOneIndicator.setBounds(61, 7, 46, 43);
		panel.add(stepOneIndicator);
		stepOneIndicator.setLayout(null);

		JLabel one = new JLabel("1");
		one.setHorizontalAlignment(SwingConstants.CENTER);
		one.setBounds(10, 0, 26, 43);
		one.setFont(new Font("Tahoma", Font.PLAIN, 20));
		stepOneIndicator.add(one);

		JPanel stepTwoIndicator = new JPanel();
		if (currentStep == 2) {
			stepTwoIndicator.setBackground(Color.GREEN);
			stepTwoIndicator.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.GREEN, null));
		} else {
			stepTwoIndicator.setBackground(SystemColor.control);
			stepTwoIndicator.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		}
		stepTwoIndicator.setLayout(null);
		stepTwoIndicator.setBounds(117, 7, 46, 43);
		panel.add(stepTwoIndicator);

		JLabel two = new JLabel("2");
		two.setHorizontalAlignment(SwingConstants.CENTER);
		two.setFont(new Font("Tahoma", Font.PLAIN, 20));
		two.setBounds(10, 0, 26, 43);
		stepTwoIndicator.add(two);

		JPanel stepThreeIndicator = new JPanel();
		if (currentStep == 3) {
			stepThreeIndicator.setBackground(Color.GREEN);
			stepThreeIndicator.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.GREEN, null));
		} else {
			stepThreeIndicator.setBackground(SystemColor.control);
			stepThreeIndicator.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		}
		stepThreeIndicator.setLayout(null);
		stepThreeIndicator.setBounds(173, 7, 46, 43);
		panel.add(stepThreeIndicator);

		JLabel three = new JLabel("3");
		three.setHorizontalAlignment(SwingConstants.CENTER);
		three.setFont(new Font("Tahoma", Font.PLAIN, 20));
		three.setBounds(10, 0, 26, 43);
		stepThreeIndicator.add(three);

		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBounds(10, 54, 446, 2);
		panel.add(separator);
	}

	private void createButtonsBar(JPanel panel, int currentStep) {
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		buttonsPanel.setLayout(null);
		buttonsPanel.setBounds(10, 263, 446, 49);
		panel.add(buttonsPanel);

		btnNext = new JButton(NEXTTEXT);
		// button_next_s1.addActionListener(this);
		btnNext.setActionCommand(NEXTACTION);
		btnNext.addActionListener(this);
		btnNext.setBounds(350, 11, 86, 27);

		btnBack = new JButton(BACKTEXT);
		btnBack.setActionCommand(BACKACTION);
		btnBack.addActionListener(this);
		btnBack.setBounds(260, 11, 86, 27);

		btnExit = new JButton(EXITTEXT);
		btnExit.setActionCommand(EXITACTION);
		btnExit.addActionListener(this);
		btnExit.setBounds(10, 11, 86, 27);
		buttonsPanel.add(btnExit);

		btnDone = new JButton(DONETEXT);
		btnDone.setActionCommand(DONEACTION);
		btnDone.setFont(new Font(btnDone.getFont().getName(), Font.BOLD, btnDone.getFont().getSize() + 2));
		btnDone.addActionListener(this);
		btnDone.setBounds(350, 11, 86, 27);

		switch (currentStep) {
		case 1:
			buttonsPanel.add(btnNext);
			break;
		case 2:
			buttonsPanel.add(btnNext);
			buttonsPanel.add(btnBack);
			break;
		case 3:
			buttonsPanel.add(btnDone);
			buttonsPanel.add(btnBack);
			break;
		}
	}

	private void buildTextPanel() {
		JPanel thirdPanel = (JPanel) tabbedPane.getComponent(2);
		thirdPanel.removeAll();

		createBreadCumb(thirdPanel, 3);

		// NOTA: per la history non ricreo l'elemento se ritorno a questo passo
		// dopo essere tornato indietro
		if (pannelloText == null) {
			pannelloText = new PannelloText();
		}
		pannelloText.setLocation(25, 61);
		thirdPanel.add(pannelloText);

		createButtonsBar(thirdPanel, 3);
	}

	private void buildImagePanel() {
		JPanel thirdPanel = (JPanel) tabbedPane.getComponent(2);
		thirdPanel.removeAll();

		createBreadCumb(thirdPanel, 3);

		// NOTA: per la history non ricreo l'elemento se ritorno a questo passo
		// dopo essere tornato indietro
		if (pannelloImage == null) {
			pannelloImage = new PannelloImage();
		}
		pannelloImage.setLocation(25, 61);
		thirdPanel.add(pannelloImage);

		createButtonsBar(thirdPanel, 3);
	}

	private void buildLinkPanel() {
		JPanel thirdPanel = (JPanel) tabbedPane.getComponent(2);
		thirdPanel.removeAll();

		createBreadCumb(thirdPanel, 3);

		if (pannelloLink == null) {
			pannelloLink = new PannelloLink();
		}
		pannelloLink.setLocation(25, 61);
		thirdPanel.add(pannelloLink);

		createButtonsBar(thirdPanel, 3);
	}

	private void buildAlternativePanel() {
		JPanel thirdPanel = (JPanel) tabbedPane.getComponent(2);
		thirdPanel.removeAll();

		createBreadCumb(thirdPanel, 3);

		// NOTA: per la history non ricreo l'elemento se ritorno a questo passo
		// dopo essere tornato indietro
		if (pannelloAlt == null)
			pannelloAlt = new PannelloAlt(true);
		pannelloAlt.setLocation(25, 61);
		thirdPanel.add(pannelloAlt);

		createButtonsBar(thirdPanel, 3);
	}

	private void buildCompositePanel() {
		JPanel thirdPanel = (JPanel) tabbedPane.getComponent(2);
		thirdPanel.removeAll();

		createBreadCumb(thirdPanel, 3);

		// NOTA: per la history non ricreo l'elemento se ritorno a questo passo
		// dopo essere tornato indietro
		if (pannelloComp == null)
			pannelloComp = new PannelloComp(true);
		pannelloComp.setLocation(25, 61);
		thirdPanel.add(pannelloComp);

		createButtonsBar(thirdPanel, 3);
	}

	protected boolean showDialog() {
		setVisible(true);
		return returnValue;
	}

	protected Componente buildNewComponent() {
		if (choice_type.getSelectedItem().equals(Testo.TEXTTYPE)) {
			Testo newTesto = new Testo(name.getText(), category.getText(), choice_visibility.getSelectedIndex(), choice_emphasis.getSelectedIndex(), pannelloText.getText());
			return newTesto;
		} else if (choice_type.getSelectedItem().equals(Immagine.IMAGETYPE)) {
			Immagine newImage = new Immagine(name.getText(), category.getText(), choice_visibility.getSelectedIndex(), choice_emphasis.getSelectedIndex(), pannelloImage.getPath());
			return newImage;
		} else if (choice_type.getSelectedItem().equals(Link.LINKTYPE)) {
			Link newLink = new Link(name.getText(), category.getText(), choice_visibility.getSelectedIndex(), choice_emphasis.getSelectedIndex(), pannelloLink.getPath(),
					pannelloLink.getText());
			return newLink;
		} else if (choice_type.getSelectedItem().equals(ComponenteAlternative.ALTERNATIVETYPE)) {
			ComponenteAlternative compAlt = new ComponenteAlternative(name.getText(), category.getText(), choice_visibility.getSelectedIndex(), choice_emphasis.getSelectedIndex());
			compAlt.setOpzioni(pannelloAlt.getOpzioni());
			return compAlt;
		} else if (choice_type.getSelectedItem().equals(
				ComponenteComposto.COMPOSTOTYPE)) {
			ComponenteComposto compComp = new ComponenteComposto(name.getText(), category.getText(), choice_visibility.getSelectedIndex(), choice_emphasis.getSelectedIndex());
			compComp.setOpzioni(pannelloComp.getOpzioni());
			return compComp;
		}
		return null;
	}

	protected Vector<Integer> getOriginalIndexes() {
		if (choice_type.getSelectedItem().equals(ComponenteAlternative.ALTERNATIVETYPE)) {
			return pannelloAlt.getOriginalIndexes();
		} else if (choice_type.getSelectedItem().equals(ComponenteComposto.COMPOSTOTYPE)) {
			return pannelloComp.getOriginalIndexes();
		}
		return null;
	}

	protected Vector<Integer> getNewIndexes() {
		if (choice_type.getSelectedItem().equals(ComponenteAlternative.ALTERNATIVETYPE)) {
			return pannelloAlt.getNewIndexes();
		} else if (choice_type.getSelectedItem().equals(ComponenteComposto.COMPOSTOTYPE)) {
			return pannelloComp.getNewIndexes();
		}
		return null;
	}

	// FIXME Disabilitare i tab non correnti o disabilitare solo quelli non
	// ancora affrontati?
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(BACKACTION)) {
			// gestisco il bottone Back
			tabbedPane.setEnabledAt(tabbedPane.getSelectedIndex() - 1, true);
			tabbedPane.setEnabledAt(tabbedPane.getSelectedIndex(), false);
			tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);
		} else if (e.getActionCommand().equals(NEXTACTION)) {
			// gestisco il bottone Next
			tabbedPane.setEnabledAt(tabbedPane.getSelectedIndex() + 1, true);
			tabbedPane.setEnabledAt(tabbedPane.getSelectedIndex(), false);
			tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() + 1);
			if ((tabbedPane.getSelectedIndex()) == 2) {
				if (tipi[choice_type.getSelectedIndex()].equals(Testo.TEXTTYPE)) {
					buildTextPanel();
				} else if (tipi[choice_type.getSelectedIndex()].equals(Immagine.IMAGETYPE)) {
					buildImagePanel();
				} else if (tipi[choice_type.getSelectedIndex()].equals(Link.LINKTYPE)) {
					buildLinkPanel();
				} else if (tipi[choice_type.getSelectedIndex()].equals(ComponenteAlternative.ALTERNATIVETYPE)) {
					buildAlternativePanel();
				} else if (tipi[choice_type.getSelectedIndex()].equals(ComponenteComposto.COMPOSTOTYPE)) {
					buildCompositePanel();
				}
			}
		} else if (e.getActionCommand().equals(EXITACTION)) {
			// gestisco il bottone EXIT
			dispose();
		} else if (e.getActionCommand().equals(DONEACTION)) {
			// gestisco il bottone Done
			returnValue = true;
			dispose();
		}

		if (!((tabbedPane.getSelectedIndex()) == 0)) {
			setTitle(BASETITLE + " - " + name.getText() + " ["+ choice_type.getSelectedItem() + "]");
		} else {
			setTitle(BASETITLE);
		}
	}

	/*
	 * TODO Verificare tutto quando c'� qui sottoTODOTODOTODOTODOTODOTODOTODO
	 * TODO
	 */
	/*
	 * protected ComponenteComposto buildComposite() { cmp = new
	 * ComponenteComposto(name.getText(), category.getText(),
	 * choice_enphasy.getSelectedIndex(), choice_enphasy.getSelectedIndex());
	 * return cmp; }
	 * 
	 * protected ComponenteAlternative buildAlternative() { alt = new
	 * ComponenteAlternative(name.getText(), category.getText(),
	 * choice_enphasy.getSelectedIndex(), choice_enphasy.getSelectedIndex());
	 * return alt; }
	 * 
	 * private void chooseFile(int chooserValue, JFileChooser fc, JTextField
	 * target){ //TODO settare le cartelle di default if (chooserValue ==
	 * JFileChooser.APPROVE_OPTION) {
	 * target.setText(fc.getSelectedFile().getAbsolutePath()); }
	 * 
	 * }
	 * 
	 * private void chooseFile(int chooserValue, JFileChooser fc, JTextArea
	 * target) throws IOException{ //TODO settare le cartelle di default if
	 * (chooserValue == JFileChooser.APPROVE_OPTION) { File file =
	 * fc.getSelectedFile(); String letta = readFile(file);
	 * target.setText(letta);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * public static String readFile (File file) throws IOException{ //FIXME se
	 * si prova a leggere il file /dev/zero va in loop infinito (la console
	 * continua a leggere e scrollare...) String letto = ""; FileReader reader =
	 * new FileReader(file); while(true) { int x = reader.read(); // read
	 * restituisce un intero che vale -1 se il file è finito if (x == -1)
	 * break; char c = (char) x; letto = letto+c; System.out.println( letto+c);
	 * } return letto; }
	 * 
	 * private ArrayList<MyEventClassListener> _listeners = new
	 * ArrayList<MyEventClassListener>();
	 * 
	 * public synchronized void addEventListener(MyEventClassListener listener)
	 * { _listeners.add(listener); } public synchronized void
	 * removeEventListener(MyEventClassListener listener) {
	 * _listeners.remove(listener); }
	 * 
	 * private synchronized void fireEvent() { MyEventClass event = new
	 * MyEventClass(this); Iterator<MyEventClassListener> i =
	 * _listeners.iterator(); while(i.hasNext()) { ((MyEventClassListener)
	 * i.next()).handleMyEventClassEvent(event); } }
	 * 
	 * private void updateImagePath() {
	 * 
	 * if(focusedImg!= null) focusedImg.setPath(textField_imagepath.getText());
	 * checkImagePath(); }
	 * 
	 * private boolean checkImagePath() {
	 * if(isPathCorrect(textField_imagepath.getText())){
	 * textField_imagepath.setToolTipText("Path of the image file");
	 * btnDone_Image.setEnabled(true); return true; } else {
	 * textField_imagepath.
	 * setToolTipText("The file doesn't exist or is not readable");
	 * btnDone_Image.setEnabled(false); } return false; }
	 * 
	 private boolean isPathCorrect(String path){
		 File daControllare = new
			 File(path); if(daControllare.isFile() && daControllare.canRead())
				 return true;
			 return false;
	 }
	 * 
	 * private synchronized void fireEvent(boolean onlyDispose) { MyEventClass
	 * event = null; if (onlyDispose == CREATENEWCOMP) event = new
	 * MyEventClass(this, buildNewComp()); Iterator<MyEventClassListener> i =
	 * _listeners.iterator(); while(i.hasNext()) { ((MyEventClassListener)
	 * i.next()).handleMyEventClassEvent(event); } }
	 * 
	 * protected Componente buildNewComp() { System.out.println("builder");
	 * if(choice_type.getSelectedItem()==Testo.TEXTTYPE) return new
	 * Testo(name.getText(), category.getText(),
	 * choice_enphasy.getSelectedIndex(), choice_enphasy.getSelectedIndex(),
	 * text.getText()); else
	 * if(choice_type.getSelectedItem()==Immagine.IMAGETYPE) return new
	 * Immagine(name.getText(), category.getText(),
	 * choice_enphasy.getSelectedIndex(), choice_enphasy.getSelectedIndex(),
	 * textField_imagepath.getText()); else
	 * if(choice_type.getSelectedItem()==Link.LINKTYPE) return new
	 * Link(name.getText(), category.getText(),
	 * choice_enphasy.getSelectedIndex(), choice_enphasy.getSelectedIndex(),
	 * textField_url.getText(), textField_url.getText()); else
	 * if(choice_type.getSelectedItem()==ComponenteAlternative.ALTERNATIVETYPE){
	 * 
	 * return alt; } else {
	 * 
	 * return cmp; } }
	 * 
	 * private void manageTooltips(Component component, boolean b) { if
	 * (component == textField_imagepath){ if(b)
	 * textField_imagepath.setToolTipText(AddNew.URL_EMPTY); else
	 * textField_imagepath.setToolTipText(AddNew.URL); } else if (component ==
	 * name){ if(b) name.setToolTipText(AddNew.NAME_EMPTY); else
	 * name.setToolTipText(AddNew.NAME); if
	 * (MainWindow.nameExistsAll(name.getText()))
	 * name.setToolTipText(AddNew.NAME_EXISTING); } }
	 * 
	 * @Override public void changedUpdate(DocumentEvent e) {
	 * updateComponent(e);
	 * 
	 * }
	 * 
	 * @Override public void insertUpdate(DocumentEvent e) { updateComponent(e);
	 * 
	 * }
	 * 
	 * @Override public void removeUpdate(DocumentEvent e) { updateComponent(e);
	 * 
	 * }
	 * 
	 * private void updateComponent(DocumentEvent e) {
	 * if(textField_imagepath.getDocument()==e.getDocument()){ imageUpdate(); }
	 * else if(name.getDocument()==e.getDocument() ){
	 * Utils.redify(name,Utils.isBlank
	 * (name)||MainWindow.nameExistsAll(name.getText())); manageTooltips(name,
	 * Utils.isBlank(name)); button_next_s1.setEnabled(!Utils.isBlank(name) &&
	 * !MainWindow.nameExistsAll(name.getText()));
	 * 
	 * } else if(text.getDocument()==e.getDocument()){
	 * Utils.redify(text,Utils.isBlank(text));
	 * btnDone_text.setEnabled(!Utils.isBlank(text));
	 * 
	 * } else if(category.getDocument()==e.getDocument()){
	 * Utils.redify(category,Utils.isBlank(category));
	 * button_next_s2.setEnabled(!Utils.isBlank(category)); }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * private void imageUpdate() { updateImagePath();
	 * Utils.redify(textField_imagepath,Utils.isBlank(textField_imagepath));
	 * manageTooltips(textField_imagepath, Utils.isBlank(textField_imagepath));
	 * }
	 * 
	 * public void oldActionPerformed(ActionEvent e) { if
	 * (e.getActionCommand().equals(NEXT1)){ tabbedPane.setSelectedIndex(1); }
	 * 
	 * else if (e.getActionCommand().equals(EXIT)){ fireEvent(); dispose(); }
	 * 
	 * else if (e.getActionCommand().equals(NEXT2)){ nextAction(); }
	 * 
	 * else if (e.getActionCommand().equals(BACK)){
	 * tabbedPane.setSelectedIndex(0); }
	 * 
	 * else if (e.getActionCommand().equals(DONE)){ //testo composite
	 * alternative
	 * 
	 * createAndDispose(); } else if (e.getActionCommand().equals(BACK2)){
	 * tabbedPane.setSelectedIndex(1); } else if
	 * (e.getActionCommand().equals(LOAD_TEXT)){ loadTextAction(); } else if
	 * (e.getActionCommand().equals(DONE_LINK)){ //lnk= new Link(name.getText(),
	 * category.getText(), impo.getSelectedIndex(),
	 * emph.getSelectedIndex(),textField_url.getText(),
	 * textField_linktext.getText()); createAndDispose(); }
	 * 
	 * else if (e.getActionCommand().equals(DONE_IMG)){ //img = new
	 * Immagine(name.getText(), category.getText(),
	 * impo.getSelectedIndex(),emph.getSelectedIndex(),
	 * textField_imagepath.getText()); createAndDispose(); } else
	 * if(e.getActionCommand().equals(LOAD_IMG)){ /*fcImage.showDialog();
	 * fcImage.setJTFPath(textField_imagepath); btnDone_Image.setEnabled(true);
	 * }
	 * 
	 * 
	 * }
	 * 
	 * private void createAndDispose() { fireEvent(CREATENEWCOMP); dispose(); }
	 * 
	 * protected void loadTextAction() { try { //TODO escapare caratteri
	 * speciali fcText.showDialog();
	 * 
	 * if (fcText.getFile() != null){ String letto =
	 * Wizard.readFile(fcText.getFile()); if ( letto!= null && letto.length()>0)
	 * text.setText(letto); } } catch (IOException e1) { } }
	 * 
	 * protected void nextAction() { if
	 * (choice_type.getSelectedItem().equals(tipi[0]))
	 * tabbedPane.setSelectedIndex(2); else if
	 * (choice_type.getSelectedItem().equals(tipi[2]))
	 * tabbedPane.setSelectedIndex(3); else if
	 * (choice_type.getSelectedItem().equals(tipi[1]))
	 * tabbedPane.setSelectedIndex(4); else if
	 * (choice_type.getSelectedItem().equals(tipi[4])){ // composite
	 * 
	 * panel_comp.setComponent(buildComposite());
	 * tabbedPane.setSelectedIndex(5); } else if
	 * (choice_type.getSelectedItem().equals(tipi[3])){ //alternative
	 * panel_alt.setComponent(buildAlternative());
	 * tabbedPane.setSelectedIndex(6); }
	 * 
	 * }
	 * 
	 * void manageDoneButton(boolean enable){ if
	 * (choice_type.getSelectedItem().equals(tipi[3]))
	 * button_doneAlt.setEnabled(enable);
	 * 
	 * else if (choice_type.getSelectedItem().equals(tipi[4]))
	 * button_doneComp.setEnabled(enable);
	 * 
	 * }
	 */

	public static boolean nameExistsAll(String s) {
		return (MainWindow.albero.nameExists(s) || s.equals(name.getText())); 
	}

}
