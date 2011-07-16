package webApplication.grafica;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class options extends JFrame {

	private JPanel contentPane;
	private JTextField textField_defImgDir;
	private JTextField textField_defTxtDir;
	private JTextField textField_defSaveLoadDir;
	private String[] defaultDirectories= {"","",""};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					options frame = new options();
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
	public String[] menu() {
		setTitle("Options");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_dirs = new JPanel();
		panel_dirs.setBorder(new TitledBorder(null, " Default directories ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_dirs.setBounds(12, 0, 424, 218);
		contentPane.add(panel_dirs);
		panel_dirs.setLayout(null);
		
		textField_defImgDir = new JTextField();
		textField_defImgDir.setEnabled(false);
		textField_defImgDir.setToolTipText("");
		textField_defImgDir.setBounds(15, 43, 292, 22);
		panel_dirs.add(textField_defImgDir);
		
		JButton btnBrowseDir = new JButton("Browse");
		btnBrowseDir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooseFile(fileChooser.showOpenDialog(contentPane), fileChooser, textField_defImgDir,0);
			}
		});
		btnBrowseDir.setBounds(324, 40, 89, 29);
		panel_dirs.add(btnBrowseDir);
		
		textField_defTxtDir = new JTextField();
		textField_defTxtDir.setEnabled(false);
		textField_defTxtDir.setToolTipText("Path of the image file");
		textField_defTxtDir.setBounds(15, 116, 292, 22);
		panel_dirs.add(textField_defTxtDir);
		
		JButton btnBrowseDir_1 = new JButton("Browse");
		btnBrowseDir_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooseFile(fileChooser.showOpenDialog(contentPane), fileChooser, textField_defTxtDir,1);
			}
		});
		btnBrowseDir_1.setBounds(324, 113, 89, 29);
		panel_dirs.add(btnBrowseDir_1);
		
		textField_defSaveLoadDir = new JTextField();
		textField_defSaveLoadDir.setEnabled(false);
		textField_defSaveLoadDir.setToolTipText("Path of the image file");
		textField_defSaveLoadDir.setBounds(15, 178, 292, 22);
		panel_dirs.add(textField_defSaveLoadDir);
		
		JButton btnBrowseDir_2 = new JButton("Browse");
		btnBrowseDir_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooseFile(fileChooser.showOpenDialog(contentPane), fileChooser, textField_defSaveLoadDir,2);
			}
		});
		btnBrowseDir_2.setBounds(324, 175, 89, 29);
		panel_dirs.add(btnBrowseDir_2);
		
		JLabel lblImageDirectory = new JLabel("Images:");
		lblImageDirectory.setBounds(15, 22, 131, 15);
		panel_dirs.add(lblImageDirectory);
		
		JLabel lblTextFileDirectory = new JLabel("Text files:");
		lblTextFileDirectory.setBounds(15, 87, 131, 15);
		panel_dirs.add(lblTextFileDirectory);
		
		JLabel lblLoadAndSave = new JLabel("Load and save:");
		lblLoadAndSave.setBounds(15, 150, 131, 15);
		panel_dirs.add(lblLoadAndSave);
		
		JButton btnDone = new JButton("Done");
		btnDone.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		
		btnDone.setBounds(347, 230, 89, 29);
		contentPane.add(btnDone);
		return defaultDirectories;
	}
	
	private void chooseFile(int chooserValue, JFileChooser fc, JTextField target, int dir){
		MainWindow.chooseFile(chooserValue,  fc, target);
		if (chooserValue == JFileChooser.APPROVE_OPTION && dir > -1 && dir < defaultDirectories.length) //TODO controllare che il length ritorni il n di stringhe del vettore e non dei caratteri contenuti
			defaultDirectories[dir]=fc.getSelectedFile().getAbsolutePath();
	}
	
}
