package webApplication.grafica;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class Options extends JFrame implements ActionListener{

	private static final String TITLE = " Default directories ";

	private static final String BROWSE = "Browse";

	private static final String PATH_IMAGE = "Path of the image file";

	private static final String DONE = "Done";

	private static final String LOAD_SAVE_LBL = "Load and save:";

	private static final String TEXT_LBL = "Text files:";

	private static final String IMAGES = "Images:";

	/**
	 * 
	 */
	private static final long serialVersionUID = -4742226718972158722L;

	private static final String OPTIONS = "Options";

	private static final String DONE_ACT = "done";

	private static final String LOAD_ACT = "load";

	private static final String TEXT_ACT = "txt_act";

	private static final String IMAGE_ACT = "img";
	
	private JPanel contentPane;
	private JTextField textField_defImgDir;
	private JTextField textField_defTxtDir;
	private JTextField textField_defSaveLoadDir;
	//private String[] defaultDirectories= {"","",""};
	private String defDirImage;
	private String defDirText;
	private String defDirLoadSave;
	private JPanel panel_dirs;
	
	 private ArrayList<MyEventClassListener> _listeners = new ArrayList<MyEventClassListener>();
	
	 public synchronized void addEventListener(MyEventClassListener listener)  {
		 _listeners.add(listener);
	 }
	 public synchronized void removeEventListener(MyEventClassListener listener)   {
		 _listeners.remove(listener);
	 }

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Options frame = new Options();
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
	public Options() {
		buildJPanel();
		
		buildFieldImage();
		
		buildFieldText();
		
		buildFieldSaveLoad();
		
		buildLabels();
		
		buildButtonDone();
		
		
	}
	private void buildJPanel() {
		setResizable(false);
		setTitle(OPTIONS);
		setDefaultCloseOperation(closeOperation());
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		panel_dirs = new JPanel();
		panel_dirs.setBorder(new TitledBorder(null, TITLE, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_dirs.setBounds(12, 0, 424, 218);
		contentPane.add(panel_dirs);
		panel_dirs.setLayout(null);
	}
	private void buildLabels() {
		JLabel lblImageDirectory = new JLabel(IMAGES);
		lblImageDirectory.setBounds(15, 22, 131, 15);
		panel_dirs.add(lblImageDirectory);
		
		JLabel lblTextFileDirectory = new JLabel(TEXT_LBL);
		lblTextFileDirectory.setBounds(15, 87, 131, 15);
		panel_dirs.add(lblTextFileDirectory);
		
		JLabel lblLoadAndSave = new JLabel(LOAD_SAVE_LBL);
		lblLoadAndSave.setBounds(15, 150, 131, 15);
		panel_dirs.add(lblLoadAndSave);
	}
	
	private void buildButtonDone() {
		JButton btnDone = new JButton(DONE);
		btnDone.addActionListener(this);
		btnDone.setActionCommand(DONE_ACT);
		
		btnDone.setBounds(347, 230, 89, 29);
		contentPane.add(btnDone);
	}
	
	private void buildFieldSaveLoad() {
		textField_defSaveLoadDir = new JTextField();
		textField_defSaveLoadDir.setEnabled(false);
		textField_defSaveLoadDir.setToolTipText(PATH_IMAGE);
		textField_defSaveLoadDir.setBounds(15, 178, 292, 22);
		panel_dirs.add(textField_defSaveLoadDir);
		
		JButton btnBrowseDir_2 = new JButton(BROWSE);
		btnBrowseDir_2.addActionListener(this);
		btnBrowseDir_2.setActionCommand(LOAD_ACT);
		
		btnBrowseDir_2.setBounds(324, 175, 89, 29);
		panel_dirs.add(btnBrowseDir_2);
	}
	
	private void buildFieldText() {
		textField_defTxtDir = new JTextField();
		textField_defTxtDir.setEnabled(false);
		textField_defTxtDir.setToolTipText(PATH_IMAGE);
		textField_defTxtDir.setBounds(15, 116, 292, 22);
		panel_dirs.add(textField_defTxtDir);
		
		JButton btnBrowseDir_1 = new JButton(BROWSE);
		btnBrowseDir_1.addActionListener(this);
		btnBrowseDir_1.setActionCommand(TEXT_ACT);
		
		btnBrowseDir_1.setBounds(324, 113, 89, 29);
		panel_dirs.add(btnBrowseDir_1);
	}
	private void buildFieldImage() {
		textField_defImgDir = new JTextField();
		textField_defImgDir.setEnabled(false);
		textField_defImgDir.setToolTipText("");
		textField_defImgDir.setBounds(15, 43, 292, 22);
		panel_dirs.add(textField_defImgDir);
		
		JButton btnBrowseDir = new JButton(BROWSE);
		btnBrowseDir.addActionListener(this);
		btnBrowseDir.setActionCommand(IMAGE_ACT);
		
		btnBrowseDir.setBounds(324, 40, 89, 29);
		panel_dirs.add(btnBrowseDir);
	}
	
	public String getDefDirImage(){
		return defDirImage;
	}
	public String getDefDirText(){
		return defDirText;
	}
	public String getDefDirLoadSave(){
		return defDirLoadSave;
	}
	
	
	private void chooseFile(int chooserValue, JFileChooser fc, JTextField target, int dir){
		MainWindow.chooseFile(chooserValue,  fc, target);
		if (chooserValue == JFileChooser.APPROVE_OPTION){ //TODO controllare che il length ritorni il n di stringhe del vettore e non dei caratteri contenuti
			if (dir == 0)
				defDirImage =fc.getSelectedFile().getAbsolutePath();
			else if (dir == 1)
				defDirText =fc.getSelectedFile().getAbsolutePath();
			else
				defDirLoadSave = fc.getSelectedFile().getAbsolutePath();
		}
	}	
	private synchronized void fireEvent() {	
		MyEventClass event = new MyEventClass(this);
		Iterator<MyEventClassListener> i = _listeners.iterator();
		while(i.hasNext())  {
			((MyEventClassListener) i.next()).handleMyEventClassEvent(event);
		}
	}
	
	public void windowLostFocus(WindowEvent evt) {
		System.out.println("focus lost options");
		requestFocusInWindow();
		}
	
	private int closeOperation()	{
		fireEvent();
		setVisible(false);
		dispose();
		return 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand()==DONE_ACT)
			closeOperation();
		else if(arg0.getActionCommand()==LOAD_ACT)
			setDefaultDirectory(textField_defSaveLoadDir, 2);
		else if(arg0.getActionCommand()==TEXT_ACT)
			setDefaultDirectory(textField_defTxtDir,1);
		else if(arg0.getActionCommand()==IMAGE_ACT)
			setDefaultDirectory(textField_defImgDir,0);
	}
	
	private void setDefaultDirectory(JTextField target, int i){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooseFile(fileChooser.showOpenDialog(contentPane), fileChooser, target,i);
	}
	
}