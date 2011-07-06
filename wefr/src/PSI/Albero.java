package PSI;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class Albero extends JTree {

	public Albero() {
		super();
		init();
		// TODO Auto-generated constructor stub
	}

	public Albero(Object[] value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	public Albero(Vector<?> value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	public Albero(Hashtable<?, ?> value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	public Albero(TreeNode root) {
		super(root);
		// TODO Auto-generated constructor stub
	}

	public Albero(TreeModel newModel) {
		super(newModel);
		// TODO Auto-generated constructor stub
	}

	public Albero(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
		// TODO Auto-generated constructor stub
	}
	
	private void init(){
		this.setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("JTree") {
					{
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
	}

}
