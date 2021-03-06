package webApplication.grafica;

public class EUDFileFilter extends CustomFileFilter {

	public static final String EXTENSION = "eud";
	private static final String EUD_DATA = "EUDMamba data";

	@Override
	protected boolean validExtension(String extension) {
		if (extension.equals(EXTENSION))
			return true;
		return false;
	}

	@Override
	public String getDescription() {
		return EUD_DATA;
	}

}
