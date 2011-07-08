package PSI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.TextField;
import java.awt.Choice;
import java.awt.List;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Wizard extends JFrame {

	private JPanel contentPane;
	private TextField textField;
	private Choice choice;
	
	
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
		
		//commento inutile
		//TODO mettere input da history (magari con un metodo)
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new TextField();
		textField.setBounds(102, 107, 147, 19);
		contentPane.add(textField);
		textField.setText("TODO Default"); //TODO mettere default incrementale
		textField.getText();
		
		choice = new Choice();
		choice.setBounds(102, 160, 147, 21);
		contentPane.add(choice);
		choice.add("Text");
		choice.add("Image");
		choice.add("Link");
		choice.add("Alternative");
		choice.add("Composite");
		
		//TODO implementare la lettura della scelta:
		//	void Select (int Selection) 
		//		Sets the index of the selected item.
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(12, 217, 424, 41);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnNext = new JButton("Next");
		btnNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Next");
				System.out.println("textField: "+ textField.getText());
			 	System.out.println("choice: "+ choice.getSelectedItem());
			}
		});
		btnNext.setBounds(346, 0, 66, 25);
		panel_1.add(btnNext);
	}
}
