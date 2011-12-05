package webApplication.grafica;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Options extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4742226718972158722L;

	private static final String FRAMETITLE = "Preferences";
	private static final String PANELTITLE = " Default directories for: ";

	private static final String BROWSE = "Browse";
	private static final String DONE = "Save";
	private static final String RESET = "Restore Defaults";
	private static final String RESETTOOLTIP = "Resets all the path to the default values";
	private static final String CANCEL = "Cancel";
	private static final String PATH_IMAGE = "Enter the path where you usually keep your image files";
	private static final String PATH_TEXT = "Enter the path where you usually keep your text files";
	private static final String PATH_LOSA = "Enter the path where you usually keep your EUD-Mamba saved projects";
	private static final String LOAD_SAVE_LBL = "Saved projects:";
	private static final String TEXT_LBL = "Text files:";
	private static final String IMAGES = "Image files:";

	private static final String RESET_ACT = "Reset";
	private static final String CANCEL_ACT = "Cancel";
	private static final String DONE_ACT = "Done_options";
	private static final String LOAD_ACT = "load_options";
	private static final String TEXT_ACT = "txt_act_options";
	private static final String IMAGE_ACT = "img_options";

	private static final String APPROVEBUTTONTEXT = "Select";

	private JPanel contentPane;
	private JPanel panel_dirs;
	private JTextField textField_defImgDir;
	private JTextField textField_defTxtDir;
	private JTextField textField_defSaveLoadDir;

	/**
	 * Create the frame.
	 */
	public Options(JFrame owner) {
		super(owner, ModalityType.APPLICATION_MODAL);

		buildJDialog();

		// centra la finestra
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = getWidth();
		int h = getHeight();
		setLocation((screenDim.width - w) / 2, (screenDim.height - h) / 2);

		buildFieldImage();

		buildFieldText();

		buildFieldSaveLoad();

		buildLabels();

		buildButtons();

		setVisible(true);
	}

	private void buildJDialog() {
		setResizable(false);
		setTitle(FRAMETITLE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 365);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel_dirs = new JPanel();
		panel_dirs.setBorder(new TitledBorder(null, PANELTITLE,
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_dirs.setBounds(11, 12, 424, 270);
		panel_dirs.setLayout(null);
		contentPane.add(panel_dirs);
	}

	private void buildFieldImage() {
		textField_defImgDir = new JTextField(MainWindow.defImageDir);
		textField_defImgDir.setToolTipText(PATH_IMAGE);
		textField_defImgDir.setBounds(15, 45, 292, 22);
		panel_dirs.add(textField_defImgDir);

		JButton btnBrowseDir = new JButton(BROWSE);
		btnBrowseDir.addActionListener(this);
		btnBrowseDir.setActionCommand(IMAGE_ACT);

		btnBrowseDir.setBounds(324, 42, 89, 29);
		panel_dirs.add(btnBrowseDir);
	}

	private void buildFieldText() {
		textField_defTxtDir = new JTextField(MainWindow.defTextDir);
		textField_defTxtDir.setToolTipText(PATH_TEXT);
		textField_defTxtDir.setBounds(15, 110, 292, 22);
		panel_dirs.add(textField_defTxtDir);
		JButton btnBrowseDir_1 = new JButton(BROWSE);
		btnBrowseDir_1.addActionListener(this);
		btnBrowseDir_1.setActionCommand(TEXT_ACT);
		btnBrowseDir_1.setBounds(324, 107, 89, 29);
		panel_dirs.add(btnBrowseDir_1);
	}

	private void buildFieldSaveLoad() {
		textField_defSaveLoadDir = new JTextField(MainWindow.defSLDir);
		textField_defSaveLoadDir.setToolTipText(PATH_LOSA);
		textField_defSaveLoadDir.setBounds(15, 178, 292, 22);
		panel_dirs.add(textField_defSaveLoadDir);

		JButton btnBrowseDir_2 = new JButton(BROWSE);
		btnBrowseDir_2.addActionListener(this);
		btnBrowseDir_2.setActionCommand(LOAD_ACT);

		btnBrowseDir_2.setBounds(324, 175, 89, 29);
		panel_dirs.add(btnBrowseDir_2);
	}

	private void buildLabels() {
		JLabel lblImageDirectory = new JLabel(IMAGES);
		lblImageDirectory.setBounds(15, 22, 131, 15);
		panel_dirs.add(lblImageDirectory);

		JLabel lblTextFileDirectory = new JLabel(TEXT_LBL);
		lblTextFileDirectory.setBounds(15, 87, 131, 15);
		panel_dirs.add(lblTextFileDirectory);

		JLabel lblLoadAndSave = new JLabel(LOAD_SAVE_LBL);
		lblLoadAndSave.setBounds(15, 155, 131, 15);
		panel_dirs.add(lblLoadAndSave);
	}

	private void buildButtons() {
		JButton btnReset = new JButton(RESET);
		btnReset.addActionListener(this);
		btnReset.setToolTipText(RESETTOOLTIP);
		btnReset.setActionCommand(RESET_ACT);
		btnReset.setBounds(15, 223, 130, 29);
		panel_dirs.add(btnReset);

		JButton btnCancel = new JButton(CANCEL);
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand(CANCEL_ACT);
		btnCancel.setBounds(235, 294, 89, 29);
		contentPane.add(btnCancel);

		JButton btnDone = new JButton(DONE);
		boldify(btnDone);
		btnDone.addActionListener(this);
		btnDone.setActionCommand(DONE_ACT);
		btnDone.setBounds(344, 294, 89, 29);
		contentPane.add(btnDone);
	}

	private void boldify(JButton button) {
		//btnDone.setFont(new Font("Arial Black", Font.PLAIN, btnDone.getFont().getSize()+1 ));

		Font newButtonFont = new Font("Arial Black", Font.PLAIN, button.getFont().getSize() + 1);
		button.setFont(newButtonFont);
	}

	private void resetToDefault() {
		textField_defImgDir.setText(MainWindow.DEFAULTVALUE);
		textField_defTxtDir.setText(MainWindow.DEFAULTVALUE);
		textField_defSaveLoadDir.setText(MainWindow.DEFAULTVALUE);
	}

	private void doneOperation() {
		MainWindow.defImageDir = textField_defImgDir.getText();
		MainWindow.defTextDir = textField_defTxtDir.getText();
		MainWindow.defSLDir = textField_defSaveLoadDir.getText();
		dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(RESET_ACT)) {
			resetToDefault();
		} else if (e.getActionCommand().equals(CANCEL_ACT)) {
			dispose();
		} else if (e.getActionCommand().equals(DONE_ACT)) {
			doneOperation();
		} else {
			if (e.getActionCommand().equals(LOAD_ACT)) {
				JFileChooser fc = new JFileChooser(MainWindow.defSLDir);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int choice = fc.showDialog(this, APPROVEBUTTONTEXT);
				if (choice == JFileChooser.APPROVE_OPTION) {
					textField_defSaveLoadDir.setText(fc.getSelectedFile().getPath());
				}
			} else if (e.getActionCommand().equals(TEXT_ACT)) {
				JFileChooser fc = new JFileChooser(MainWindow.defTextDir);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int choice = fc.showDialog(this, APPROVEBUTTONTEXT);
				if (choice == JFileChooser.APPROVE_OPTION) {
					textField_defTxtDir.setText(fc.getSelectedFile().getPath());
				}
			} else if (e.getActionCommand().equals(IMAGE_ACT)) {
				JFileChooser fc = new JFileChooser(MainWindow.defImageDir);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int choice = fc.showDialog(this, APPROVEBUTTONTEXT);
				if (choice == JFileChooser.APPROVE_OPTION) {
					textField_defImgDir.setText(fc.getSelectedFile().getPath());
				}
			}
		}
	}

}