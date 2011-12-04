package webApplication.grafica;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * Gestisce la preview di una immagine
 * 
 * @author Andrea
 * 
 */
public class ImagePreview extends JPanel implements PropertyChangeListener,
		DocumentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2892326495977019007L;

	private static final String IMAGEPREVIEWTOOLTIP = "Preview of the choosen image";
	private int width, height, size;
	private ImageIcon icon;
	private Image image;
	private static final int ACCSIZE = 155;
	private Color bg;

	/**
	 * Crea il pannello specificando la grandezza
	 * 
	 * @param s
	 *            La grandezza
	 */
	public ImagePreview(int s) {
		size = s;
		setToolTipText(IMAGEPREVIEWTOOLTIP);
		setPreferredSize(new Dimension(size, -1));
		setBackground(Color.GRAY);
		bg = getBackground();
	}

	/**
	 * Crea il pannello settando la grandezza ad un valore specifico
	 */
	public ImagePreview() {
		this(ACCSIZE);
	}

	/**
	 * Riscala l'immagine adattandola alle dimensioni specificate
	 */
	private void scaleImage() {
		width = image.getWidth(this);
		height = image.getHeight(this);
		double ratio = 1.0;
		if (width >= height) {
			ratio = (double) (size - 5) / width;
			width = size - 5;
			height = (int) (height * ratio);
		} else {
			if (getHeight() > 150) {
				ratio = (double) (size - 5) / height;
				height = size - 5;
				width = (int) (width * ratio);
			} else {
				ratio = (double) getHeight() / height;
				height = getHeight();
				width = (int) (width * ratio);
			}
		}
		image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paintComponent(Graphics g) {
		g.setColor(bg);
		g.fillRect(0, 0, size, getHeight());
		g.drawImage(image, (getWidth() - width) / 2,
				(getHeight() - height) / 2, this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();

		// Make sure we are responding to the right event.
		if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
			File selection = (File) e.getNewValue();
			String name;
			if (selection == null)
				return;
			else
				name = selection.getAbsolutePath();
			icon = new ImageIcon(name);
			image = icon.getImage();
			scaleImage();
			repaint();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void insertUpdate(DocumentEvent e) {
		try {
			this.propertyChange(new PropertyChangeEvent(e.getDocument(), JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, 0, new File(e.getDocument().getText(0, e.getDocument().getLength()))));
		} catch (BadLocationException e1) {
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeUpdate(DocumentEvent e) {
		insertUpdate(e);
	}

	/**
	 * {@inheritDoc}
	 */
	public void changedUpdate(DocumentEvent e) {
		insertUpdate(e);
	}
}
