package webApplication.grafica;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import webApplication.business.ComponenteSemplice;
import webApplication.business.Immagine;
import webApplication.business.Link;
import webApplication.business.Testo;

/**
 * Finestra per l'aggiunta ad un componente composite o alternative di un
 * elemento gia inserito nell'albero
 * 
 * @author Andrea
 */
public class AddExisting extends AddComponent implements ActionListener,
		TreeSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5872420453534844447L;

	private Vector<DisabledNode> nodes;
	private Vector<Integer> indexes;
	private Vector<Integer> existingAlreadyInserted;
	private ComponenteSemplice selectedComp;
	private int indexNodeToMove;

	private static final String TITLE = "Addable node list:";
	private static final String ROOTNAME = "Available elements";
	private static final String CONTENTTITLE = "Selected Element";

	private static final String NAME = "Name:";
	private static final String CATEGORY = "Category:";
	private static final String TYPE = "Type:";
	private static final String TEXT = "Text:";
	private static final String IMAGEPATH = "Path:";
	private static final String URLTEXT = "URL text:";
	private static final String URLPATH = "URL path:";

	private int labelHeight = 20;
	private int treeWidth = 200;

	private JLabel titleLabel;
	private JScrollPane scrollPane;
	private JPanel propertiesPanel;
	private JLabel lblName;
	private JPanel textPanel;
	private JScrollPane textPane;
	private JPanel imagePanel;
	private JTextField imagePath;
	private JPanel linkPanel;
	private JTextField urlPath;
	private JTextField urlText;

	private JTree simpleElementTree;
	private DisabledNode root;
	private JTextField name;
	private JTextField category;
	private JTextField type;
	private JTextArea text;

	/**
	 * Costruisce la finestra per l'aggiunta di un elemento in un elemento
	 * composite o alternative
	 * 
	 * @param owner	La finestra che ha fatto la richiesta
	 * @param title	Il nome dell'elemento in cui si desidera fare l'aggiunta
	 */
	protected AddExisting(Window owner, String title, Vector<Integer> alreadyInserted) {
		super(owner, title);

		titleLabel = new JLabel(TITLE);
		titleLabel.setBounds(20, 20, 120, labelHeight);
		basePane.add(titleLabel);

		buildTree(alreadyInserted);

		buildPropertiesPanel();
	}

	/**
	 * Costruisce il pannello contenente l'albero per la scelta
	 */
	private void buildTree(Vector<Integer> alreadyInserted) {
		root = new DisabledNode(ROOTNAME);

		addNodes(alreadyInserted);
		simpleElementTree = new JTree(new DefaultTreeModel(root));
		simpleElementTree.setCellRenderer(new CustomCellRenderer());
		simpleElementTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		simpleElementTree.addTreeSelectionListener(this);
		simpleElementTree.setDragEnabled(false);
		simpleElementTree.setEditable(false);

		// TODO cambiare la visualizzazione per tipo o alfabetica
		scrollPane = new JScrollPane(simpleElementTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(titleLabel.getX(), titleLabel.getY() + 30, treeWidth, getHeight() - (getHeight() - button_back.getY())	- (titleLabel.getY() + 30) - 20);
		basePane.add(scrollPane);
	}

	/**
	 * Aggiunge i nodi all'albero
	 */
	private void addNodes(Vector<Integer> alreadyInserted) {
		existingAlreadyInserted = alreadyInserted;
		// NOTA: uso una hashmap ma potrei anche usare un array di vector
		HashMap<Integer, DisabledNode> map = MainWindow.albero.getFirstLevelSimpleComponent();
		nodes = new Vector<DisabledNode>();
		indexes = new Vector<Integer>();
		Set<Integer> set = map.keySet();
		Iterator<Integer> it = set.iterator();
		while (it.hasNext()) {
			int indice = (Integer) it.next();
			// non mostro gli elementi esistenti nell'albero originale ma che
			// sono stati inseriti nella lista in precedenza
			if (!existingAlreadyInserted.contains(indice)) {
				nodes.add((DisabledNode) map.get(indice));
				indexes.add(indice);
				root.add((DisabledNode) map.get(indice));
			}
		}
	}

	/**
	 * Costruisce il pannello delle proprieta
	 */
	private void buildPropertiesPanel() {
		propertiesPanel = new JPanel();
		propertiesPanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229), 1, true), CONTENTTITLE, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		propertiesPanel.setBounds(scrollPane.getX() + scrollPane.getWidth()	+ 20, scrollPane.getY(), getWidth() - (2 * scrollPane.getX() + scrollPane.getWidth() + 20),	scrollPane.getHeight());
		propertiesPanel.setLayout(null);
		basePane.add(propertiesPanel);

		lblName = new JLabel(NAME);
		lblName.setBounds(30, 30, 54, labelHeight);
		propertiesPanel.add(lblName);
		name = new JTextField();
		name.setBounds(lblName.getX() + lblName.getWidth() + 15, lblName.getY(), 143, labelHeight);
		name.setEditable(false);
		propertiesPanel.add(name);

		JLabel lblCategory = new JLabel(CATEGORY);
		lblCategory.setBounds(lblName.getX(), lblName.getY() + lblName.getHeight() + 30, lblName.getWidth(), labelHeight);
		propertiesPanel.add(lblCategory);
		category = new JTextField();
		category.setBounds(name.getX(), lblCategory.getY(), name.getWidth(), labelHeight);
		category.setEditable(false);
		propertiesPanel.add(category);

		JLabel lblType = new JLabel(TYPE);
		lblType.setBounds(lblName.getX(), lblCategory.getY() + lblCategory.getHeight() + 30, lblName.getWidth(), labelHeight);
		propertiesPanel.add(lblType);

		type = new JTextField();
		type.setBounds(category.getX(), lblType.getY(), name.getWidth(), labelHeight);
		type.setEditable(false);
		propertiesPanel.add(type);

		buildTextPanel();

		buildImagePanel();

		buildLinkPanel();
	}

	/**
	 * Costruisce il sottopannello di preview del contenuto di un elemento Testo
	 */
	private void buildTextPanel() {
		textPanel = new JPanel();
		textPanel.setLayout(null);
		textPanel.setBounds(20, type.getY() + type.getHeight() + 30, propertiesPanel.getWidth() - 40, propertiesPanel.getHeight() - (type.getY() + type.getHeight() + 30) - lblName.getX());

		JLabel lblText = new JLabel(TEXT);
		lblText.setBounds(10, 0, lblName.getWidth(), labelHeight);
		textPanel.add(lblText);

		text = new JTextArea();
		text.setEditable(false);
		textPane = new JScrollPane(text);
		textPane.setBounds(lblText.getX(), lblText.getY() + lblText.getHeight()	+ 10, textPanel.getWidth() - 2 * lblText.getX(), textPanel.getHeight() - (lblText.getY() + lblText.getHeight() + 10));
		textPanel.add(textPane);
	}

	/**
	 * Costruisce il sottopannello di preview del contenuto di un elemento
	 * Immagine
	 */
	private void buildImagePanel() {
		imagePanel = new JPanel();
		imagePanel.setLayout(null);
		imagePanel.setBounds(20, type.getY() + type.getHeight() + 30, propertiesPanel.getWidth() - 40, propertiesPanel.getHeight() - (type.getY() + type.getHeight() + 30) - lblName.getX());

		JLabel lblImagePath = new JLabel(IMAGEPATH);
		lblImagePath.setBounds(10, 0, lblName.getWidth(), labelHeight);
		imagePanel.add(lblImagePath);

		imagePath = new JTextField();
		imagePath.setBounds(lblImagePath.getX() + lblImagePath.getWidth() + 15, lblImagePath.getY(), name.getWidth(), labelHeight);
		imagePath.setEditable(false);
		imagePanel.add(imagePath);

		int imagePreviewWidth = imagePanel.getWidth() - 2 * lblImagePath.getX();
		ImagePreview imagePreview = new ImagePreview(imagePreviewWidth);
		imagePreview.setBounds(lblImagePath.getX(), lblImagePath.getY() + lblImagePath.getHeight() + 10, imagePreviewWidth, imagePanel.getHeight() - (lblImagePath.getY() + lblImagePath.getHeight() + 10));
		imagePanel.add(imagePreview);

		imagePath.getDocument().addDocumentListener(imagePreview);
	}

	/**
	 * Costruisce il sottopannello di preview del contenuto di un elemento Link
	 */
	private void buildLinkPanel() {
		linkPanel = new JPanel();
		linkPanel.setLayout(null);
		linkPanel.setBounds(20, type.getY() + type.getHeight() + 30, propertiesPanel.getWidth() - 40, propertiesPanel.getHeight() - (type.getY() + type.getHeight() + 30) - lblName.getX());

		JLabel lblUrlText = new JLabel(URLTEXT);
		lblUrlText.setBounds(10, 60, lblName.getWidth(), labelHeight);
		linkPanel.add(lblUrlText);

		urlPath = new JTextField();
		urlPath.setEditable(false);
		urlPath.setBounds(lblUrlText.getX() + lblUrlText.getWidth() + 15, lblUrlText.getY(), name.getWidth(), labelHeight);
		urlPath.setEditable(false);
		linkPanel.add(urlPath);

		JLabel lblUrlPath = new JLabel(URLPATH);
		lblUrlPath.setBounds(lblUrlText.getX(), lblUrlText.getY() + lblUrlText.getHeight() + 30, lblName.getWidth(), labelHeight);
		linkPanel.add(lblUrlPath);

		urlText = new JTextField();
		urlText.setEditable(false);
		urlText.setBounds(lblUrlText.getX() + lblUrlText.getWidth() + 15, lblUrlPath.getY(), name.getWidth(), labelHeight);
		urlText.setEditable(false);
		linkPanel.add(urlText);
	}

	/**
	 * Resetta i campi relativi a nome, categoria e tipo
	 */
	private void resetFields() {
		name.setText("");
		category.setText("");
		type.setText("");
	}

	/**
	 * {@inheritDoc}
	 */
	protected ComponenteSemplice getComponente() {
		return selectedComp;
	}

	/**
	 * Ritorna gli indici dei nodi esistenti da spostare nel componente
	 * 
	 * @return Gli indici dell'albero principale
	 */
	protected int getIndexesNodesToMove() {
		return indexNodeToMove;
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}

	/**
	 * {@inheritDoc}
	 */
	public void valueChanged(TreeSelectionEvent e) {
		if ((simpleElementTree.isSelectionEmpty()) || (((DisabledNode) simpleElementTree.getSelectionPath().getLastPathComponent())).isRoot()) {
			buttonAdd.setEnabled(false);
			simpleElementTree.clearSelection();
			selectedComp = null;
			indexNodeToMove = -1;
			resetFields();
			propertiesPanel.remove(textPanel);
			propertiesPanel.remove(imagePanel);
			propertiesPanel.remove(linkPanel);
			propertiesPanel.revalidate();
			propertiesPanel.repaint();
		} else {
			buttonAdd.setEnabled(true);
			selectedComp = (ComponenteSemplice) ((DisabledNode) simpleElementTree.getSelectionPath().getLastPathComponent()).getUserObject();
			indexNodeToMove = indexes.get(nodes.indexOf((DisabledNode) simpleElementTree.getSelectionPath().getLastPathComponent()));
			name.setText(selectedComp.getNome());
			category.setText(selectedComp.getCategoria());
			type.setText(selectedComp.getType());
			if (selectedComp.getType().equals(Testo.TEXTTYPE)) {
				text.setText(((Testo) selectedComp).getTesto());
				propertiesPanel.add(textPanel);
				propertiesPanel.remove(imagePanel);
				propertiesPanel.remove(linkPanel);
				propertiesPanel.revalidate();
				propertiesPanel.repaint();
			} else if (selectedComp.getType().equals(Immagine.IMAGETYPE)) {
				imagePath.setText(((Immagine) selectedComp).getPath());
				propertiesPanel.remove(textPanel);
				propertiesPanel.add(imagePanel);
				propertiesPanel.remove(linkPanel);
				propertiesPanel.revalidate();
				propertiesPanel.repaint();
			} else if (selectedComp.getType().equals(Link.LINKTYPE)) {
				urlPath.setText(((Link) selectedComp).getUri());
				urlText.setText(((Link) selectedComp).getTesto());
				propertiesPanel.remove(textPanel);
				propertiesPanel.remove(imagePanel);
				propertiesPanel.add(linkPanel);
				propertiesPanel.revalidate();
				propertiesPanel.repaint();
			}
		}
	}

}
