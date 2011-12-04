package webApplication.grafica;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * Il pannello per la visualizzazione del contenuto di un elemento di tipo
 * Immagine
 * 
 * @author Andrea
 */
public class PannelloImage extends PannelloGeneric implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7674891211887682540L;

	private ImagePreview image;
	protected JTextField imagepath;

	private static final String LABELIMAGE = "Image path:";
	private static final String BTNLOADIMAGE = "Load an image";
	private static final String BTNTOOLTIP = "Click here to select the file of the desired image";
	private static final String LOAD_IMAGE = "Load image";
	protected static final String IMAGEPATHTOOLTIP = "Path of the image file";
	protected static final String IMAGEPATHERRORTOOLTIP = "The path is wrong";

	private static final int labelWidth = 94;
	private static final int labelHeight = 14;
	private static final int imagePreviewWidth = 217;
	private static final int imagePreviewHeight = 192;
	private static final int btnHeight = 27;
	private static final int btnWidth = 130;

	/**
	 * Crea il pannello ed i relativi campi
	 */
	protected PannelloImage() {
		super();

		JLabel lblFilePath = new JLabel(LABELIMAGE);
		lblFilePath.setBounds(0, 0, labelWidth, labelHeight);
		add(lblFilePath);

		image = new ImagePreview(imagePreviewWidth);
		image.setBorder(new LineBorder(Color.GRAY));
		image.setBackground(Color.WHITE);
		image.setBounds(lblFilePath.getX() + 200, lblFilePath.getY(), imagePreviewWidth, imagePreviewHeight);
		add(image);

		imagepath = new JTextField();
		imagepath.setToolTipText(IMAGEPATHTOOLTIP);
		imagepath.setBounds(lblFilePath.getX(), lblFilePath.getY() + 30, 174, 22);
		imagepath.getDocument().addDocumentListener(image);
		// TODO aggiungere gestione degli errori?
		add(imagepath);

		JButton btnImportImage = new JButton(BTNLOADIMAGE);
		btnImportImage.setActionCommand(LOAD_IMAGE);
		btnImportImage.setToolTipText(BTNTOOLTIP);
		btnImportImage.addActionListener(this);
		btnImportImage.setBounds(lblFilePath.getX() + (imagepath.getWidth() - btnWidth) / 2, imagepath.getY() + 40, btnWidth, btnHeight);
		add(btnImportImage);
	}

	/**
	 * Setta il path relativo all'immagine
	 * 
	 * @param t
	 *            Il path dell'immagine
	 */
	protected void setPath(String t) {
		imagepath.setText(t);
	}

	/**
	 * Ritorna il path relativo all'immagine
	 * 
	 * @return Il path dell'immagine
	 */
	protected String getPath() {
		return imagepath.getText();
	}
	
	protected boolean isCorrect() {
		return !Utils.redify(imagepath, !MainWindow.isPathCorrect(imagepath.getText()));
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(LOAD_IMAGE)) {
			JFileChooser fc = new JFileChooser(MainWindow.defImageDir);
			ImagePreview imgPrev = new ImagePreview();
			fc.setAccessory(imgPrev);
			fc.addPropertyChangeListener(imgPrev);
			fc.setFileFilter(new ImageFileFilter());
			fc.setAcceptAllFileFilterUsed(false);
			int choice = fc.showOpenDialog(this);
			if (choice == JFileChooser.APPROVE_OPTION) {
				imagepath.setText(fc.getSelectedFile().getPath());
			}
		}
	}

}
