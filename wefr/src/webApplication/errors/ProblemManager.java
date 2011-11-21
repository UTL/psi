package webApplication.errors;

import java.util.List;

/**
 * Manages errors
 * @author Andrea
 *
 */
public class ProblemManager {
	//TODO completare per aggiungere tutte le funzionalita che gli servono
	
	private List<Problem> errors;
	private List<Warning> warnings;
	
	/**
	 * Check if the tree has errors
	 * @return	True if there are errors, False otherwise
	 */
	public boolean hasErrors() {
		if (errors.size()!=0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check if the tree has warnings
	 * @return	True if there are warnings, False otherwise
	 */
	public boolean hasWarnings() {
		if (warnings.size()!=0) {
			return true;
		}
		return false;
	}

}
