package webApplication.grafica;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.TextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JList;
import java.awt.TextArea;
import java.awt.event.TextListener;
import java.awt.event.TextEvent;

public class Wizard extends JFrame {

	private JPanel contentPane;
	private TextField textField;
	private JComboBox choice;
	private JTabbedPane tabbedPane;
	private JButton btnDone;
	private TextArea textArea;
	private JButton button;
	private JButton button_4;
	private TextField textField_5;
	private JButton btnDone_2;
	private TextField textField_1;
	private JComboBox choice_1;
	private JComboBox choice_2;
	private TextField textField_3;
	private TextField textField_4;

	// TODO come nel MainWindow, le tre stringhe tipi categorie e importanze
	// andrebbero estratte
	private final static String[] tipi = { "Text", "Image", "Link",
			"Alternative", "Composite" };
	private static final String[] categorie = { "Necessary", "Indifferent",
			"Expandable" }; // FIXME Andrebbero rese globali per tutte le
							// classi??
	private static final String[] importanze = { "Greatly", "Normally",
			"Not at all" }; // FIXME Andrebbero rese globali per tutte le
							// classi?? E ne mancano 2 che non ricordo

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Wizard frame = new Wizard();
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
	public Wizard() {
		setResizable(false);
		setTitle("Add element");

		// TODO mettere input da history (magari con un metodo)

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 477, 354);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(0, 0, 471, 326);
		contentPane.add(tabbedPane);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		tabbedPane.addTab("Step1", null, panel, null);
		panel.setLayout(null);

		choice = new JComboBox(tipi);
		choice.setBounds(181, 165, 174, 20);
		panel.add(choice);

		textField = new TextField();
		textField.addTextListener(new TextListener() {
			public void textValueChanged(TextEvent e) {
				if (textField.getText().equalsIgnoreCase(""))
					button.setEnabled(false);
				else
					button.setEnabled(true);
			}
		});
		textField.setBounds(181, 106, 174, 22);
		panel.add(textField);
		textField.setText("Element0"); // TODO mettere default incrementale

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setLayout(null);
		panel_1.setBounds(10, 261, 446, 49);
		panel.add(panel_1);

		button = new JButton("Next");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(1);

				/*
				 * System.out.println("textField: "+ textField.getText());
				 * System.out.println("choice: "+ choice.getSelectedItem());
				 */
			}
		});
		button.setBounds(375, 11, 66, 27);
		panel_1.add(button);

		JButton button_1 = new JButton("Exit");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Exit");
			}
		});
		button_1.setBounds(10, 11, 66, 27);
		panel_1.add(button_1);

		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBounds(10, 54, 446, 2);
		panel.add(separator);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(129, 109, 46, 14);
		panel.add(lblName);

		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(130, 168, 46, 14);
		panel.add(lblType);
		textField.getText();

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Step2", null, panel_2, null);
		panel_2.setLayout(null);

		textField_1 = new TextField();
		textField_1.setBounds(180, 92, 174, 22);
		textField_1.setText("Category0");
		panel_2.add(textField_1);

		choice_1 = new JComboBox(categorie);
		choice_1.setBounds(180, 192, 174, 20);
		panel_2.add(choice_1);
		choice_1.setSelectedIndex(1);

		choice_2 = new JComboBox(importanze);
		choice_2.setBounds(180, 143, 174, 20);
		panel_2.add(choice_2);
		choice_2.setSelectedIndex(1);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.setLayout(null);
		panel_3.setBounds(10, 261, 446, 49);
		panel_2.add(panel_3);

		JButton button_2 = new JButton("Next");
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (choice.getSelectedItem().equals(tipi[0]))
					tabbedPane.setSelectedIndex(2);
				else if (choice.getSelectedItem().equals(tipi[2]))
					tabbedPane.setSelectedIndex(3);
				else if (choice.getSelectedItem().equals(tipi[1]))
					tabbedPane.setSelectedIndex(4);
				else if (choice.getSelectedItem().equals(tipi[4]))
					tabbedPane.setSelectedIndex(5);
				else if (choice.getSelectedItem().equals(tipi[3]))
					tabbedPane.setSelectedIndex(6);
			}
		});
		button_2.setBounds(375, 11, 66, 27);
		panel_3.add(button_2);

		JButton button_3 = new JButton("Exit");
		button_3.setBounds(10, 11, 66, 27);
		panel_3.add(button_3);

		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(0);
			}
		});
		btnBack.setBounds(299, 11, 66, 27);
		panel_3.add(btnBack);

		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLACK);
		separator_1.setBounds(10, 54, 446, 2);
		panel_2.add(separator_1);

		JLabel lblCategoryIdentifier = new JLabel("Category identifier:");
		lblCategoryIdentifier.setBounds(56, 95, 108, 14);
		panel_2.add(lblCategoryIdentifier);

		JLabel lblEmphasize = new JLabel("Emphasize:");
		lblEmphasize.setBounds(97, 146, 108, 14);
		panel_2.add(lblEmphasize);

		JLabel lblImportance = new JLabel("Importance:");
		lblImportance.setBounds(93, 195, 108, 14);
		panel_2.add(lblImportance);

		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Step3-t", null, panel_4, null);
		panel_4.setLayout(null);

		JLabel lblEnterText = new JLabel("Enter text:");
		lblEnterText.setBounds(25, 69, 94, 14);
		panel_4.add(lblEnterText);

		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.BLACK);
		separator_2.setBounds(10, 52, 446, 2);
		panel_4.add(separator_2);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_5.setLayout(null);
		panel_5.setBounds(10, 261, 446, 49);
		panel_4.add(panel_5);

		btnDone = new JButton("Done");
		btnDone.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("Name:" + textField.getText());
				System.out.println("Type:" + choice.getSelectedItem());
				System.out.println("Category:" + textField_1.getText());
				System.out.println("Emphasize:" + choice_2.getSelectedItem());
				System.out.println("Importance:" + choice_1.getSelectedItem());
				System.out.println("Text:" + textArea.getText());
			}
		});
		btnDone.setEnabled(false);
		btnDone.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDone.setBounds(375, 11, 66, 27);
		panel_5.add(btnDone);

		JButton button_5 = new JButton("Exit");
		button_5.setBounds(10, 11, 66, 27);
		panel_5.add(button_5);

		JButton button_6 = new JButton("Back");
		button_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		button_6.setBounds(299, 11, 66, 27);
		panel_5.add(button_6);

		JButton btnImportFromFile = new JButton("Import from file");
		btnImportFromFile.setBounds(25, 223, 143, 27);
		panel_4.add(btnImportFromFile);

		textArea = new TextArea();
		textArea.addTextListener(new TextListener() {
			public void textValueChanged(TextEvent arg0) {
				if (textArea.getText().equalsIgnoreCase(""))
					btnDone.setEnabled(false);
				else
					btnDone.setEnabled(true);
			}
		});
		textArea.setBounds(25, 89, 423, 124);
		panel_4.add(textArea);

		JPanel panel_6 = new JPanel();
		tabbedPane.addTab("Step3-l", null, panel_6, null);
		panel_6.setLayout(null);

		JLabel lblLinkTarget = new JLabel("URL:");
		lblLinkTarget.setBounds(132, 121, 66, 14);
		panel_6.add(lblLinkTarget);

		textField_3 = new TextField();
		textField_3.setBounds(204, 113, 174, 22);
		panel_6.add(textField_3);

		textField_4 = new TextField();
		textField_4.setBounds(204, 158, 174, 22);
		panel_6.add(textField_4);

		JLabel lblLinkText = new JLabel("Link text:");
		lblLinkText.setBounds(132, 166, 66, 14);
		panel_6.add(lblLinkText);

		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.BLACK);
		separator_3.setBounds(10, 52, 446, 2);
		panel_6.add(separator_3);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_7.setLayout(null);
		panel_7.setBounds(10, 259, 446, 49);
		panel_6.add(panel_7);

		JButton btnDone_1 = new JButton("Done");
		btnDone_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Name:" + textField.getText());
				System.out.println("Type:" + choice.getSelectedItem());
				System.out.println("Category:" + textField_1.getText());
				System.out.println("Emphasize:" + choice_2.getSelectedItem());
				System.out.println("Importance:" + choice_1.getSelectedItem());
				System.out.println("Value: link target:"
						+ textField_3.getText() + "  link text:"
						+ textField_4.getText());
			}
		});
		btnDone_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDone_1.setBounds(375, 11, 66, 27);
		panel_7.add(btnDone_1);

		JButton button_8 = new JButton("Exit");
		button_8.setBounds(10, 11, 66, 27);
		panel_7.add(button_8);

		JButton button_9 = new JButton("Back");
		button_9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		button_9.setBounds(299, 11, 66, 27);
		panel_7.add(button_9);

		JPanel panel_8 = new JPanel();
		tabbedPane.addTab("Step3-i", null, panel_8, null);
		panel_8.setLayout(null);

		JSeparator separator_4 = new JSeparator();
		separator_4.setForeground(Color.BLACK);
		separator_4.setBounds(10, 52, 446, 2);
		panel_8.add(separator_4);

		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_9.setLayout(null);
		panel_9.setBounds(10, 259, 446, 49);
		panel_8.add(panel_9);

		btnDone_2 = new JButton("Done");
		btnDone_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Name:" + textField.getText());
				System.out.println("Type:" + choice.getSelectedItem());
				System.out.println("Category:" + textField_1.getText());
				System.out.println("Emphasize:" + choice_2.getSelectedItem());
				System.out.println("Importance:" + choice_1.getSelectedItem());
				System.out.println("File path:" + textField_5.getText());
			}
		});
		btnDone_2.setEnabled(false);
		btnDone_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDone_2.setBounds(375, 11, 66, 27);
		panel_9.add(btnDone_2);

		JButton button_11 = new JButton("Exit");
		button_11.setBounds(10, 11, 66, 27);
		panel_9.add(button_11);

		JButton button_12 = new JButton("Back");
		button_12.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		button_12.setBounds(299, 11, 66, 27);
		panel_9.add(button_12);

		JLabel lblFilePath = new JLabel("File path:");
		lblFilePath.setBounds(87, 146, 66, 14);
		panel_8.add(lblFilePath);

		textField_5 = new TextField();
		textField_5.addTextListener(new TextListener() {
			public void textValueChanged(TextEvent e) {
				if (textField_5.getText().equalsIgnoreCase(""))
					btnDone_2.setEnabled(false);
				else
					btnDone_2.setEnabled(true);
			}
		});
		textField_5.setBounds(150, 143, 174, 22);
		panel_8.add(textField_5);

		JButton btnBrowse = new JButton("Browse\r\n");
		btnBrowse.setBounds(346, 139, 78, 29);
		panel_8.add(btnBrowse);

		JPanel panel_10 = new JPanel();
		tabbedPane.addTab("Step3-c", null, panel_10, null);
		panel_10.setLayout(null);

		JPanel panel_12 = new JPanel();
		panel_12.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_12.setLayout(null);
		panel_12.setBounds(10, 259, 446, 49);
		panel_10.add(panel_12);

		button_4 = new JButton("Done");
		button_4.setFont(new Font("Tahoma", Font.BOLD, 12));
		button_4.setBounds(375, 11, 66, 27);
		panel_12.add(button_4);

		JButton button_7 = new JButton("Exit");
		button_7.setBounds(10, 11, 66, 27);
		panel_12.add(button_7);

		JButton button_10 = new JButton("Back");
		button_10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		button_10.setBounds(299, 11, 66, 27);
		panel_12.add(button_10);

		JSeparator separator_5 = new JSeparator();
		separator_5.setForeground(Color.BLACK);
		separator_5.setBounds(10, 51, 446, 2);
		panel_10.add(separator_5);

		JLabel lblElements = new JLabel("Elements:");
		lblElements.setBounds(28, 64, 91, 13);
		panel_10.add(lblElements);

		JList list = new JList();
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		list.setBounds(28, 89, 408, 132);
		panel_10.add(list);

		JButton button_16 = new JButton("Delete");
		button_16.setBounds(28, 226, 91, 27);
		panel_10.add(button_16);

		JButton button_17 = new JButton("Add existing");
		button_17.setBounds(211, 226, 121, 27);
		panel_10.add(button_17);

		JButton button_18 = new JButton("Add new");
		button_18.setBounds(336, 226, 100, 27);
		panel_10.add(button_18);

		JPanel panel_11 = new JPanel();
		tabbedPane.addTab("Step3-a", null, panel_11, null);
		panel_11.setLayout(null);

		JPanel panel_13 = new JPanel();
		panel_13.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_13.setLayout(null);
		panel_13.setBounds(10, 259, 446, 49);
		panel_11.add(panel_13);

		JButton button_13 = new JButton("Done");
		button_13.setFont(new Font("Tahoma", Font.BOLD, 12));
		button_13.setBounds(375, 11, 66, 27);
		panel_13.add(button_13);

		JButton button_14 = new JButton("Exit");
		button_14.setBounds(10, 11, 66, 27);
		panel_13.add(button_14);

		JButton button_15 = new JButton("Back");
		button_15.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				tabbedPane.setSelectedIndex(1);
			}
		});

		button_15.setBounds(299, 11, 66, 27);
		panel_13.add(button_15);

		JSeparator separator_6 = new JSeparator();
		separator_6.setForeground(Color.BLACK);
		separator_6.setBounds(10, 51, 446, 2);
		panel_11.add(separator_6);

		JLabel label = new JLabel("Elements:");
		label.setBounds(72, 64, 66, 14);
		panel_11.add(label);

		JList list_1 = new JList();
		list_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		list_1.setBounds(73, 89, 355, 132);
		panel_11.add(list_1);

		JButton btnNewButton = new JButton("^");
		btnNewButton
				.setToolTipText("Click here to increase the priority of selected element");
		btnNewButton.setBounds(20, 89, 43, 62);
		panel_11.add(btnNewButton);

		JButton btnV = new JButton("v");
		btnV.setToolTipText("Click here to decrease the priority of selected element");
		btnV.setBounds(20, 159, 43, 62);
		panel_11.add(btnV);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(73, 226, 76, 27);
		panel_11.add(btnDelete);

		JButton btnAddExisting = new JButton("Add existing");
		btnAddExisting.setBounds(223, 226, 104, 27);
		panel_11.add(btnAddExisting);

		JButton btnAddNew = new JButton("Add new");
		btnAddNew.setBounds(330, 226, 98, 27);
		panel_11.add(btnAddNew);
	}
}
