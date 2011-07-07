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

public class Wizard extends JFrame {

	private JPanel contentPane;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		TextField textField = new TextField();
		textField.setBounds(102, 107, 147, 19);
		contentPane.add(textField);
		
		Choice choice = new Choice();
		choice.setBounds(102, 160, 147, 21);
		contentPane.add(choice);
		choice.add("Text");
		choice.add("Image");
		choice.add("Link");
		choice.add("Alternative");
		choice.add("Composite");
		
		JButton btnNext = new JButton("Next");
		btnNext.setBounds(319, 234, 117, 25);
		contentPane.add(btnNext);
	}
}
