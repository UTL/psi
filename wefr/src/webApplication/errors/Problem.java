package webApplication.errors;

/**
 * A general error
 * @author Andrea
 *
 */
public interface Problem {
	
	/**
	 * Get the name of the error
	 * @return	The name
	 */
	public String getName();
	
	/**
	 * Get the description of the error
	 * @return	The description
	 */
	public String getDescription();

}
