package webApplication.errors;

public class ChildNumberProblem implements Problem {
	
	private static final String NAME = "Invalid child count";
	private int node;

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
		String description = "Node "+node+" has to have at least one internal element.";
		return description;
	}

}
