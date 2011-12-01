package webApplication.grafica;

import java.awt.Component;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.ComponenteMolteplice;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;

/**
 * Il renderer dei nodi limitatamente per le icone a seconda del tipo di
 * componente che rappresenta
 * 
 * @author Andrea
 * 
 */
public class CustomCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8057343107481280548L;
	private ImageIcon homeIcon;
	private ImageIcon textIcon;
	private ImageIcon imageIcon;
	private ImageIcon linkIcon;
	private ImageIcon compositeIcon;
	private ImageIcon alternativeIcon;
	private ImageIcon errorIcon;
	private ImageIcon warningIcon;

	private static String BASEPATH = "icon/";
	private static String HOMEICON = "Home.png";
	private static String TEXTICONNAME = "Testo.png";
	private static String IMAGEICONNAME = "Immagine.png";
	private static String LINKICONNAME = "Link.png";
	private static String ALTERNATIVEICONNAME = "Alternativa.png";
	private static String COMPOSITEICONNAME = "Composto.png";
	private static String ERRORICONNAME = "error.png";
	private static String WARNINGICONNAME = "warning.png";

	private static String ERRORTOOLTIPTEXT = "The choosen component name is already taken";
	private static String WARNINGTOOLTIPTEXT = "The element contains al least one element. Consider adding more internal components";

	/**
	 * Il costruttore del renderer per l'albero
	 */
	public CustomCellRenderer() {
		homeIcon = createImageIcon(BASEPATH + HOMEICON);
		textIcon = createImageIcon(BASEPATH + TEXTICONNAME);
		imageIcon = createImageIcon(BASEPATH + IMAGEICONNAME);
		linkIcon = createImageIcon(BASEPATH + LINKICONNAME);
		compositeIcon = createImageIcon(BASEPATH + COMPOSITEICONNAME);
		alternativeIcon = createImageIcon(BASEPATH + ALTERNATIVEICONNAME);
		errorIcon = createImageIcon(BASEPATH + ERRORICONNAME);
		warningIcon = createImageIcon(BASEPATH + WARNINGICONNAME);
	}

	/**
	 * {@inheritDoc}
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DisabledNode node = (DisabledNode) value;
		if (node != tree.getModel().getRoot()) {
			if (hasError(tree, node)) {
				node.isCorrect = false;
				return this;
			}
			Componente comp = (Componente) node.getUserObject();
			// Java SE6 non supporta lo switch con String solo da Java SE 7
			String objType = comp.getType();
			if (objType.equals(Testo.TEXTTYPE)) {
				setIcon(textIcon);
			} else if (objType.equals(Immagine.IMAGETYPE)) {
				setIcon(imageIcon);
			} else if (objType.equals(Link.LINKTYPE)) {
				setIcon(linkIcon);
			} else if (objType.equals(ComponenteComposto.COMPOSTOTYPE)) {
				setIcon(compositeIcon);
			} else if (objType.equals(ComponenteAlternative.ALTERNATIVETYPE)) {
				setIcon(alternativeIcon);
			}
			setToolTipText(null);// fa in modo che il tooltip sparica spostandosi da un nodo con errore/warning
			if (!node.getEnabled()) {
				System.out.println("Nodo disabilitato");
				setEnabled(false);
			}
		} else {
			setIcon(homeIcon);
			if(node.getChildCount()==0) {
				node.isCorrect = false;
				MainWindow.btnGenXML.setEnabled(MainWindow.albero.isCorrect());
				return this;
			}
			setToolTipText(null);// fa in modo che il tooltip sparica spostandosi da un nodo con errore/warning
			
		}
		node.isCorrect = true;
		MainWindow.btnGenXML.setEnabled(MainWindow.albero.isCorrect());
		return this;
	}

	private boolean hasError(JTree tree, DisabledNode node) {
		try {
			TreePanel panel = (TreePanel) (((tree.getParent()).getParent()).getParent());
			// Recupero il path degli elementi con quel nome
			Vector<TreePath> elPath = panel.getPathForName((((Componente) node.getUserObject()).getNome()));
			if (elPath.size() != 1) {
				// Se ho un solo elemento con quel nome, l'elemento � "node" quindi non lo considero
				if (elPath.contains(new TreePath(node.getPath()))) {
					// Se il path di node � tra quelli con nome non
					// consentito setto l'icona di errore e setto il tooltip
					setIcon(errorIcon);
					setToolTipText(ERRORTOOLTIPTEXT);
					return true;
				}
			} else if ((!((Componente) node.getUserObject()).isSimple()) && (((ComponenteMolteplice) node.getUserObject()).getOpzioni().size() < 1)) {
				setIcon(errorIcon);
				setToolTipText(ERRORTOOLTIPTEXT);
				return true;
				//				setIcon(warningIcon);
				//				setToolTipText(WARNINGTOOLTIPTEXT);
				//				return false;
			}
		} catch (NullPointerException e) {
		} catch (ClassCastException e) {
		}
		return false;
	}

	/**
	 * Carica l'immagine partendo dal suo path
	 * 
	 * @param path
	 *            Il path dell'immagine
	 * @return L'immagine caricata
	 */
	private static ImageIcon createImageIcon(String path) {
		return new ImageIcon(path);
	}

}
