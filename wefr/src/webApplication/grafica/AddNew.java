package webApplication.grafica;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolTip;

import webApplication.business.ComponenteSemplice;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;
import webApplication.grafica.TreePanel.AddAction;

import java.awt.event.ActionEvent;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNew extends JDialog implements DocumentListener, FocusListener ,KeyListener, ActionListener {

	private static final String ADD = "Add";
	private static final String FILE_PATH = "File path:";
	private static final String IMAGE = "Image";
	private static final String DIALOG = "Dialog";
	private static final String TEXT2 = "Text";
	private static final String LINK = "Link";
	private static final String LINK_TEXT = "Link text:";
	private static final String URL2 = "URL:";
	private static final String NAME2 = "Name:";
	private static final String CATEGORY = "Category:";
	

	
	static final String LOAD_TOOLTIP = "Click to load text from an existing file";
	static final String IMPORT_BTN = "Import from file";
	static final String NAME = "Name of the new element";
	static final String NAME_EMPTY = "It's mandatory to fill the name field";
	
	static final String CATE = "Category of the new element";
	static final String CATE_EMPTY = "It's mandatory to fill the category field";
	
	static final String URL = "URL of the link";
	static final String URL_EMPTY = "This field must be a valid url (ex www.myweb.com)";
	
	static final String TEXT = "Text that will be displayed in the web page";
	static final String TEXT_EMPTY = "It's mandatory to fill the text field";
	
	static final String PATH = "Path of image file";
	static final String PATH_ERROR = "The path is wrong, select an image using \"Browse\" button";
	
	static final String RBTN_LINK = "Select to enable fields of Link element";
	static final String RBTN_TEXT = "Select to enable fields of Text element";
	static final String RBTN_IMAGE = "Select to enable fields of Image element";
	
	//ACTIONS
	private static final String RDBTN_LINK = "rdbtnLink";
	private static final String RDBTN_TEXT = "rdbtnText";
	private static final String RDBTN_IMAGE = "rdbtnImage";
	private static final String CREATE_EXIT = "createAndExit";
	private static final String BACK = "Back";
	private static final String BROWSE = "Browse";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3236559861349447627L;
	private JPanel contentPane;
	private JTextField textField_category;
	private JTextField textField_name;
	private JTextField textField_linkText;
	private JTextField textField_url;
	private JTextField textField_imagePath;
	
	private static final String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private Pattern valid_url = Pattern.compile(regex);
	private static final String allowedUrlChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVXYWZZ0123456789+&@#/%?=~_|!:,.;";
	
	
	private JPanel panel_link;
	private JPanel panel_text;
	private JPanel panel_image; 
	
	private static JTextArea textArea;
	private JScrollPane scrollingArea;
	private JButton button_back;
	private JButton buttonAdd;
	
	private JRadioButton rdbtnLink;
	private JRadioButton rdbtnText;
	private JRadioButton rdbtnImage;
	
	private static boolean ONLYDISPOSE = true;
	private static boolean CREATENEWCOMP = false;
	
	private CustomFCText fcText;
	private CustomFCImage fcImage;
	
	private Options frameOptions;
	private AddAction addAction;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddNew frame = new AddNew(this, new Options(), "");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/
	
	/**
	 * Create the frame.
	 */

	public AddNew(Window window,Options fOptions, String title) {
		super(window, ModalityType.APPLICATION_MODAL);
		//this.setModalityType();
		frameOptions=fOptions;
		fcText = new CustomFCText(frameOptions, this);
		fcImage=new CustomFCImage(frameOptions, this);

		setResizable(false);
		setTitle("Add new element to "+ title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 532, 542);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField_category = new JTextField();
		textField_category.setColumns(10);
		textField_category.setBounds(229, 36, 114, 19);
		contentPane.add(textField_category);
		
		textField_name = new JTextField();
		textField_name.setToolTipText(NAME);
		textField_name.setColumns(10);
		textField_name.setBounds(229, 11, 114, 19);
		contentPane.add(textField_name);
		
		JLabel name = new JLabel(NAME2);
		name.setBounds(119, 13, 51, 15);
		contentPane.add(name);
		
		JLabel category = new JLabel(CATEGORY);
		category.setBounds(119, 38, 81, 15);
		contentPane.add(category);
		
		panel_link = new JPanel(){
		      public JToolTip createToolTip() {
		          MultiLineToolTip tip = new MultiLineToolTip();
		          tip.setComponent(this);
		          return tip;
		        }
		      };
		panel_link.setEnabled(false);
		
		panel_link.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), " Link ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
		panel_link.setBounds(92, 72, 426, 84);
		contentPane.add(panel_link);
		panel_link.setLayout(null);
		
		JLabel lblUrl = new JLabel(URL2);
		lblUrl.setBounds(24, 23, 51, 15);
		panel_link.add(lblUrl);
		
		JLabel lblLink = new JLabel(LINK_TEXT);
		lblLink.setBounds(24, 48, 81, 15);
		panel_link.add(lblLink);
		
		textField_linkText = new JTextField();
		textField_linkText.setColumns(10);
		textField_linkText.setBounds(111, 46, 303, 19);
		panel_link.add(textField_linkText);
		
		textField_url = new JTextField();
		textField_url.setColumns(10);
		textField_url.setBounds(111, 21, 303, 19);
		panel_link.add(textField_url);
		
		rdbtnLink = new JRadioButton(LINK);
		rdbtnLink.setToolTipText(RBTN_LINK);
		
		rdbtnLink.setBounds(8, 110, 68, 23);
		contentPane.add(rdbtnLink);
		
		rdbtnText = new JRadioButton(TEXT2);
		rdbtnText.setSelected(true);
		rdbtnText.setToolTipText(RBTN_TEXT);
		rdbtnText.setBounds(8, 238, 68, 23);
		contentPane.add(rdbtnText);
		
		panel_text = new JPanel();
		panel_text.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229), 1, true), " Text ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_text.setLayout(null);
		panel_text.setEnabled(false);
		panel_text.setBounds(92, 168, 426, 183);
		contentPane.add(panel_text);
		
		JButton button = new JButton(IMPORT_BTN);
		button.setToolTipText(LOAD_TOOLTIP);
		button.addActionListener(this);
		button.setActionCommand(IMPORT_BTN);
		button.setFont(new Font(DIALOG, Font.PLAIN, 12));
		button.setBounds(12, 152, 142, 19);
		panel_text.add(button);
		
		textArea = new JTextArea();
		scrollingArea = new JScrollPane(textArea);
		scrollingArea.setBorder(new LineBorder(new Color(204, 204, 204), 1, true));
		scrollingArea.setBounds(12, 20, 402, 120);

		panel_text.add(scrollingArea);
		
		rdbtnImage = new JRadioButton(IMAGE);
		rdbtnImage.setBounds(8, 378, 68, 23);
		rdbtnImage.setToolTipText(RBTN_IMAGE);

		contentPane.add(rdbtnImage);
		
		panel_image = new JPanel();
		panel_image.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), " Image ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
		panel_image.setLayout(null);
		panel_image.setEnabled(false);
		panel_image.setBounds(92, 363, 426, 88);
		contentPane.add(panel_image);
		
		JLabel lblPath = new JLabel(FILE_PATH);
		lblPath.setBounds(12, 24, 81, 15);
		panel_image.add(lblPath);
		
		textField_imagePath = new JTextField();
		
		textField_imagePath.setColumns(10);
		textField_imagePath.setBounds(12, 51, 301, 19);
		textField_imagePath.getDocument().addDocumentListener(this);
		panel_image.add(textField_imagePath);
		
		JButton button_browse = new JButton(BROWSE);
		button_browse.addActionListener(this);
		button_browse.setActionCommand(BROWSE);
		
		button_browse.setFont(new Font(DIALOG, Font.PLAIN, 12));
		button_browse.setBounds(325, 51, 89, 19);
		panel_image.add(button_browse);

		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnImage);
		bg.add(rdbtnText);
		bg.add(rdbtnLink);
		
		button_back = new JButton(BACK);
		button_back.setActionCommand(BACK);
		button_back.addActionListener(this);
		
		button_back.setFont(new Font(DIALOG, Font.PLAIN, 12));
		button_back.setBounds(341, 468, 82, 27);
		contentPane.add(button_back);
		
		buttonAdd = new JButton(ADD);
		
		if (this.getOwner() instanceof Wizard)
			buttonAdd.setActionCommand(CREATE_EXIT);
		else 
			buttonAdd.setActionCommand(BACK);

		buttonAdd.addActionListener(this);

		if (this.getOwner() instanceof MainWindow)
		{	
			addAction = MainWindow.albero.new AddAction();

			buttonAdd.addActionListener(addAction);
			buttonAdd.addFocusListener(this);
		}
		buttonAdd.setFont(new Font(DIALOG, Font.BOLD, 12));
		buttonAdd.setBounds(433, 468, 82, 27);
		contentPane.add(buttonAdd);
		
		rdbtnImage.setActionCommand(RDBTN_IMAGE);
		rdbtnImage.addActionListener(this);
		
		rdbtnText.setActionCommand(RDBTN_TEXT);
		rdbtnImage.addActionListener(this);
		
		
		rdbtnLink.setActionCommand(RDBTN_LINK);
		rdbtnImage.addActionListener(this);
		
		
		textField_category.getDocument().addDocumentListener(this);
		textField_linkText.getDocument().addDocumentListener(this);
		textField_name.getDocument().addDocumentListener(this);
		textField_url.getDocument().addDocumentListener(this);
		textArea.getDocument().addDocumentListener(this);
		
		textField_url.addKeyListener(this);
		
		enabler(panel_text);
		
		redify(textField_name,isBlank(textField_name));
		redify(textField_category,isBlank(textField_category));
		updateAddBtn();
		
		 MultiLineToolTip tip = new MultiLineToolTip();
	        tip.setComponent(panel_link);
	        panel_link.setToolTipText("Hello\nworld");
		//this.setVisible(true);
	}
	
	private void enabler(JPanel toEnable){
		enablerDisabler(panel_image, false);
		enablerDisabler(panel_link, false);
		enablerDisabler(panel_text, false);
		enablerDisabler(toEnable, true);
	}
	
	private void enablerDisabler(JPanel toUpdate, boolean enable){
		setBordi(toUpdate, enable);
		toUpdate.setEnabled(enable);
		Component[] figli =  toUpdate.getComponents();
		int i;
		for(i=0; i < figli.length;i++)
		{
			
			updateComponent(figli[i], enable);
			

		}
		
	}
	
	private void updateComponent(Component figlio, boolean enable){
		figlio.setEnabled(enable);
		if(figlio == scrollingArea){
			updateComponent(textArea,enable);
			
			//le tre righe qua sotto sono un workaround per un baco delle JScrollPane
			scrollingArea.getHorizontalScrollBar().setEnabled(enable);
			scrollingArea.getVerticalScrollBar().setEnabled(enable);
			scrollingArea.getViewport().getView().setEnabled(enable);
			manageTooltips(textArea, isBlank(textArea));
		}
		if (figlio instanceof javax.swing.JTextField || figlio instanceof javax.swing.JTextArea){
			if(enable){
				if(figlio == textField_url)
					redify((JTextComponent) figlio,isBlank((JTextComponent) figlio)||errorUrl());
				else
					redify((JTextComponent) figlio,isBlank((JTextComponent) figlio));
				figlio.setBackground(new Color(255, 255, 255));
				}
			else{
				((JTextComponent)figlio).setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
				figlio.setBackground(new Color(245, 245, 245));
				
			}
		}
	}
	
	private void setBordi(JPanel toDisable, boolean enable) {
		if(enable){
			if(toDisable== panel_image)				
				panel_image.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229), 1, true), " Image ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			else if (toDisable == panel_link)
				panel_link.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229), 1, true), " Link ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			else
				panel_text.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229), 1, true), " Text ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));

		}
		else{
			if(toDisable== panel_image)
				panel_image.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), " Image ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
			else if (toDisable == panel_link)
				panel_link.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), " Link ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
			else
				panel_text.setBorder(new TitledBorder(new LineBorder(new Color(204, 204, 204), 1, true), " Text ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(153, 153, 153)));
		}
		
	}
	
	private void redify(JTextComponent toRed, boolean b){
		if(b){
			toRed.setBorder(new LineBorder(new Color(255, 0, 0), 1, true));//bordo rosso
			
		}
		else {
			toRed.setBorder(new LineBorder(new Color(184, 207, 229), 1, true));//bordo normale
			
		}
		manageTooltips(toRed, b);
			
	}

	private void manageTooltips(Component component, boolean b) {
		if (component == textField_name){
			if(b)
				textField_name.setToolTipText(NAME_EMPTY);
			else
				textField_name.setToolTipText(NAME);
		}
		else if(component == textField_category){
			if(b)
				textField_category.setToolTipText(CATE_EMPTY);
			else
				textField_category.setToolTipText(CATE);
		}
		else if(component == textField_url){
			if(b)
				textField_url.setToolTipText(URL_EMPTY);
			else
				textField_url.setToolTipText(URL);
			}
		else if(component == textField_linkText ){
			if(b)
				textField_linkText.setToolTipText(TEXT_EMPTY);
			else
				textField_linkText.setToolTipText(TEXT);
			}
		else if(component == textArea){
			if(b)
				textArea.setToolTipText(TEXT_EMPTY);
			else
				textArea.setToolTipText(TEXT);
		}
		else if(component == textField_imagePath){
			if(b)
				textField_imagePath.setToolTipText(TEXT_EMPTY);
			else
				textField_imagePath.setToolTipText(TEXT);
		}
	}

	private void changeEvent(DocumentEvent e) {
		if(e.getDocument()== textField_url.getDocument())
			redify(fromDocToJComp(e.getDocument()),isBlank((fromDocToJComp(e.getDocument())))||errorUrl());
		else if(e.getDocument()==textField_imagePath.getDocument()){
			checkPath();
		}
		else
			redify(fromDocToJComp(e.getDocument()),isBlank((fromDocToJComp(e.getDocument()))));
		updateAddBtn();
	}

		private JTextComponent fromDocToJComp(Document doc){
			if(doc== textField_category.getDocument())
				return textField_category;
			else if(doc== textField_imagePath.getDocument())
				return textField_imagePath;
		else if(doc== textField_linkText.getDocument())
			return textField_linkText;
		else if(doc== textField_name.getDocument())
			return textField_name;
		else if(doc== textField_url.getDocument())
			return textField_url;
		else if (doc== textArea.getDocument())
			return textArea;
		return null;
	}
	
	private boolean isBlank(JTextComponent toCheck){
		if (toCheck.getText().trim().length()>0)
			return false;
		return true;
	}
	
	
	private boolean nameExists(){
		//TODO scrivere la funzione
		return false;
	}
	
	private boolean erroriPresenti(){
		boolean result;
		result=isBlank(textField_name) || nameExists();
		result= result || isBlank(textField_category);
		if (rdbtnImage.isSelected())
			result= result || isBlank(textField_imagePath) || fileError();
		
		else if
			(rdbtnLink.isSelected())
			result= result || isBlank(textField_linkText) || isBlank(textField_url) || errorUrl();
		else 
			result= result || isBlank(textArea);
		return result;	
		
	}

	private boolean errorUrl() {
		if(!IsMatch(textField_url.getText())&& IsMatch("http://"+textField_url.getText())){
			//textField_url.setText("http://"+textField_url.getText());
			//String temp = "http://"+textField_url.getText();
			//textField_url.setText(temp);
			}
		//System.out.println("Match errorurl"+IsMatch(textField_url.getText()));
		return !(IsMatch(textField_url.getText()) || IsMatch("http://"+textField_url.getText()));
	}

	private boolean fileError(){
		return !MainWindow.isPathCorrect(textField_imagePath.getText());
	}
	
	private ArrayList<MyEventClassListener> _listeners = new ArrayList<MyEventClassListener>();

	public void addEventListener(MyEventClassListener listener) {
		_listeners.add(listener);
	}
	
	private void checkPath(){
		redify(textField_imagePath, !MainWindow.isPathCorrect(textField_imagePath.getText()));
	}
	
	private void readFile(){
		try {
			//TODO escapare caratteri speciali
			fcText.showDialog();

			if (fcText.getFile() != null){
				String letto = Wizard.readFile(fcText.getFile());
				if ( letto!= null && letto.length()>0)
					textArea.setText(letto);
			}
		} catch (IOException e) {
		}
	}

	private ComponenteSemplice getNuovoComp(){
		//TODO escapare i vari campi
		
		ComponenteSemplice output = null;
		if (!erroriPresenti()){
			if (rdbtnImage.isSelected())
				
				output = new Immagine(textField_name.getText(), textField_category.getText(), 0, 0, textField_imagePath.getText());

			else if (rdbtnLink.isSelected())
				output = new Link(textField_name.getText(), textField_category.getText(), 0,0, textField_url.getText(), textField_linkText.getText());
			else 
				output= new Testo(textField_name.getText(), textField_category.getText(),0,0,textArea.getText());
		}
		return output;
	}
	
	private void setPath(){
		fcImage.showDialog();
		
		if (fcImage.getFilePath().length()>0)
			textField_imagePath.setText(fcImage.getFilePath());
	}
	
	private void updateAddBtn(){
		updateComponent(buttonAdd, !erroriPresenti());
	}

	private synchronized void fireEvent(boolean onlyDispose) {	
		MyEventClass event = null;
		if (onlyDispose == CREATENEWCOMP)
			event = new MyEventClass(this, getNuovoComp());
		Iterator<MyEventClassListener> i = _listeners.iterator();
		while(i.hasNext())  {
			((MyEventClassListener) i.next()).handleMyEventClassEvent(event);
		}
	}
	
	private boolean IsMatch(String s) {
		try {
			
			Matcher matcher = valid_url.matcher(s);
			return matcher.matches();
		} catch (RuntimeException e) {
			return false;
		}   
	}
	
	private boolean allowedChar(char c){
		if(allowedUrlChar.indexOf(c)==-1)
			return false;
		return true;
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		changeEvent(arg0);		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		changeEvent(arg0);
		
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		changeEvent(arg0);
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(allowedChar(arg0.getKeyChar()))
			arg0.consume();
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(allowedChar(arg0.getKeyChar()))
			arg0.consume();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if(!allowedChar(arg0.getKeyChar()))
			arg0.consume();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand()==IMPORT_BTN){
			readFile();//MainWindow.fileChooser(MainWindow.TEXT), textArea);
			updateAddBtn();
		}
		else if(e.getActionCommand()==BROWSE){
			setPath();
			updateAddBtn();
		}
		else if (e.getActionCommand()==BACK){
			fireEvent(ONLYDISPOSE);
			dispose();
		}
		else if(e.getActionCommand()==CREATE_EXIT){
			fireEvent(CREATENEWCOMP);
			dispose();
		}
		else if(e.getActionCommand()==RDBTN_IMAGE){
			enabler(panel_image);
			updateAddBtn();
		}
		else if(e.getActionCommand()==RDBTN_TEXT){
			enabler(panel_text);
			updateAddBtn();
		}
		else if(e.getActionCommand()==RDBTN_LINK){
			enabler(panel_link);
			updateAddBtn();
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		addAction.putValue("Componente", getNuovoComp());
		addAction.putValue("ParentIndex", (MainWindow.albero.getTree().getSelectionRows()[0]-1));
	}

	@Override
	public void focusLost(FocusEvent e) {
		//non mi serve
		
	}  

}





