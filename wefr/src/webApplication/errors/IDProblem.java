package webApplication.errors;

import java.util.Vector;

/**
 * An error correlated to duplicated ID field
 * @author Andrea
 *
 */
public class IDProblem implements Problem {
	
	private static final String NAME = "Invalid ID name"; 
	private Vector<Integer> nodes;
	
	/**
	 * Create a new ID problem report
	 * @param nodes
	 */
	public IDProblem(int[]nodes) {
		for (int i=0;i<nodes.length;i++) {
			this.nodes.add(nodes[i]);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return NAME;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getDescription() {
		String description = "Nodes "+nodes.get(0);
		for (int i=0; i<nodes.size();i++) {
			description = description+", "+nodes.get(i);
		}
		description = description+"cannot have the same ID.";
		return description;
	}
	
	/**
	 * Get the indexes of the nodes connected with this error
	 * @return	The indexes of the nodes
	 */
	public Vector<Integer> getNodes() {
		return nodes;
	}

}
