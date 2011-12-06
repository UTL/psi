package webApplication.grafica;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import webApplication.business.ComponenteSemplice;

/**
 * La superclasse per le finestre di aggiunta di un elemento ad un elemento
 * composite o alternative
 * 
 * @author Andrea
 */
public abstract class AddComponent extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5278124928304422904L;

	protected static final int DEFAULTEMPIMP = 1;

	protected boolean returnValue;

	protected JPanel basePane;
	protected JButton button_back;
	private static final String BACK = "Cancel";
	protected JButton buttonAdd;
	private static final String ADD = "Add";

	/**
	 * Costruisce la finestra per l'aggiunta di un elemento in un elemento
	 * composite o alternative
	 * 
	 * @param owner
	 *            La finestra che ha fatto la richiesta
	 * @param title
	 *            Il nome dell'elemento in cui si desidera fare l'aggiunta
	 */
	protected AddComponent(Window owner, String title) {
		super(owner, ModalityType.APPLICATION_MODAL);

		returnValue = false;

		buildWindow(title);

		// centro la finestra
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ownerDim = owner.getSize();
		int w = getWidth();
		int h = getHeight();
		setLocation((screenDim.width - ownerDim.width) / 2 + (ownerDim.width - w) / 2, (screenDim.height - ownerDim.height) / 2 + (ownerDim.height - h) / 2);

		buildButtonBack();

		buildButtonAdd();

	}

	/**
	 * Ritorna l'elemento da aggiungere alla lista delle opzioni dell'elemento
	 * padre
	 * 
	 * @return L'elemento da aggiungere
	 */
	protected abstract ComponenteSemplice getComponente();

	/**
	 * Costruisce la finestra
	 * 
	 * @param title
	 *            Il nome dell'elemento padre
	 */
	private void buildWindow(String title) {
		setResizable(false);
		setTitle("Add new element to " + title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 532, 542);

		basePane = new JPanel();
		basePane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(basePane);
		basePane.setLayout(null);
	}

	/**
	 * Costruisce il bottone Back
	 */
	private void buildButtonBack() {
		button_back = new JButton(BACK);
		button_back.setActionCommand(BACK);
		button_back.addActionListener(this);
		button_back.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
		button_back.setBounds(341, 468, 82, 27);
		basePane.add(button_back);
	}

	/**
	 * Costruisce il bottone Add
	 */
	private void buildButtonAdd() {
		buttonAdd = new JButton(ADD);
		buttonAdd.setActionCommand(ADD);
		buttonAdd.addActionListener(this);
		

		buttonAdd.setFont(new Font("Arial Black", Font.PLAIN, buttonAdd.getFont().getSize()+1 ));
//		setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		buttonAdd.setBounds(433, 468, 82, 27);
		// buttonAdd.setEnabled(false); FIXME da disabilitare una volta
		// completato il listener
		basePane.add(buttonAdd);
	}

	/**
	 * Mostra la finestra
	 * 
	 * @return Ritorna true se si deve aggiungere l'elemento (premuto bottone
	 *         Add), false se l'azione e stata annullata (premuto bottone Back)
	 */
	protected boolean showDialog() {
		setVisible(true);
		return returnValue;
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(BACK)) {
			dispose();
		} else if (e.getActionCommand().equals(ADD)) {
			returnValue = true;
			dispose();
		}
	}

}
