package PSI;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.UIManager;
//import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class Albero extends JTree {

	
	
	public Albero() {
		super();
		//ca
		init();
	}

	public Albero(Object[] value) {
		super(value);
		init();
	}

	public Albero(Vector<?> value) {
		super(value);
		init();
	}

	public Albero(Hashtable<?, ?> value) {
		super(value);
		init();
	}

	public Albero(TreeNode root) {
		super(root);
		init();
	}

	public Albero(TreeModel newModel) {
		super(newModel);
		init();
	}

	public Albero(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
		init();
	}
	
	private void init(){
		this.setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("JTree") {
					{
						//TODO rimuovere cose inutili qua sotto
						DefaultMutableTreeNode node_1;
						node_1 = new DefaultMutableTreeNode("colors");
							node_1.add(new DefaultMutableTreeNode("blue"));
							node_1.add(new DefaultMutableTreeNode("violet"));
							node_1.add(new DefaultMutableTreeNode("red"));
							node_1.add(new DefaultMutableTreeNode("yellow"));
						add(node_1);
						node_1 = new DefaultMutableTreeNode("sports");
							node_1.add(new DefaultMutableTreeNode("basketball"));
							node_1.add(new DefaultMutableTreeNode("soccer"));
							node_1.add(new DefaultMutableTreeNode("football"));
							node_1.add(new DefaultMutableTreeNode("hockey"));
						add(node_1);
						node_1 = new DefaultMutableTreeNode("food");
							node_1.add(new DefaultMutableTreeNode("hot dogs"));
							node_1.add(new DefaultMutableTreeNode("pizza"));
							node_1.add(new DefaultMutableTreeNode("ravioli"));
							node_1.add(new DefaultMutableTreeNode("bananas"));
						add(node_1);
					}
				}
			));
			this.setBounds(5, 49, 216, 281);
			this.setCellRenderer(new CustomTreeRenderer());
	}
	
	private void setIcon(){
		// Retrieve the three icons
		/*Icon leafIcon = new ImageIcon("leaf.gif");
		Icon openIcon = new ImageIcon("open.gif");
		Icon closedIcon = new ImageIcon("closed.gif");

		// Update only one tree instance
		CustomTreeRenderer renderer = (CustomTreeRenderer)this.getCellRenderer();
		renderer.setLeafIcon(leafIcon);
		renderer.setClosedIcon(closedIcon);
		renderer.setOpenIcon(openIcon);
		
		// Remove the icons
		renderer.setLeafIcon(null);
		renderer.setClosedIcon(null);
		renderer.setOpenIcon(null);

		// Change defaults so that all new tree components will have new icons
		UIManager.put("Tree.leafIcon", leafIcon);
		UIManager.put("Tree.openIcon", openIcon);
		UIManager.put("Tree.closedIcon", closedIcon);*/

	}

}
