package webApplication.grafica;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import webApplication.business.Componente;
import webApplication.business.ComponenteAlternative;
import webApplication.business.ComponenteComposto;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;

/**
 * Il renderer dei nodi limitatamente per le icone a seconda del tipo di componente che rappresenta 
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
	
	private static String BASEPATH = "icon/";
	private static String HOMEICON = "Home.png"; 
	private static String TEXTICONNAME = "Testo.png";
	private static String IMAGEICONNAME = "Immagine.png";
	private static String LINKICONNAME = "Link.png";
	private static String ALTERNATIVEICONNAME = "Alternativa.png";
	private static String COMPOSITEICONNAME = "Composto.png";
	
	
	/**
	 * Il costruttore base
	 */
	public CustomCellRenderer()	{
		homeIcon = createImageIcon(BASEPATH+HOMEICON);
		textIcon = createImageIcon(BASEPATH+TEXTICONNAME);
		imageIcon = createImageIcon(BASEPATH+IMAGEICONNAME);
		linkIcon = createImageIcon(BASEPATH+LINKICONNAME);
		compositeIcon = createImageIcon(BASEPATH+COMPOSITEICONNAME);
		alternativeIcon = createImageIcon(BASEPATH+ALTERNATIVEICONNAME);
	}
	
	
    /* (non-Javadoc)
     * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)	{
    	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    	DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) value;
    	if (nodo.getParent()!=null)	{
    		try	{
    			Componente comp = (Componente) nodo.getUserObject();
    			//Java SE6 non supporta lo switch con String solo da Java SE 7
    			String objType = comp.getType();
    			if (objType==Testo.TEXTTYPE)	{
    				setIcon(textIcon);
    			}	else if (objType==Immagine.IMAGETYPE)	{
    				setIcon(imageIcon);
    			}	else if (objType==Link.LINKTYPE)	{
    				setIcon(linkIcon);
    			}	else if (objType==ComponenteComposto.COMPOSTOTYPE)	{
    				setIcon(compositeIcon);
    			}	else if (objType==ComponenteAlternative.ALTERNATIVETYPE)	{
    				setIcon(alternativeIcon);
    			}
    		}
    		catch(ClassCastException e){
    			//eccezione non gestita perchè non si potrà mai verificare
    		}
    	}
    	else	{
    		setIcon(homeIcon);
    	}
    	return this;
    }
	
    /**
     * Carica l'immagine partendo dal suo path
     * @param path	Il path dell'immagine
     * @return		L'immagine caricata
     */
    private static ImageIcon createImageIcon(String path) {
    	return new ImageIcon(path);
    }

}
