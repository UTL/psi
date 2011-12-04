package webApplication.grafica;

public class ImageFileFilter extends CustomFileFilter {

	private static final String JUST_IMAGES = "Just Images";

	public final static String jpeg = "jpeg";
	public final static String jpg = "jpg";
	public final static String gif = "gif";
	public final static String tiff = "tiff";
	public final static String tif = "tif";
	public final static String png = "png";
	
	@Override
	protected boolean validExtension(String extension) {
		if (extension.equals(tiff) || extension.equals(tif)
				|| extension.equals(gif) || extension.equals(jpeg)
				|| extension.equals(jpg) || extension.equals(png))
			return true;
		else
			return false;
	}

	@Override
	public String getDescription() {
		return JUST_IMAGES;
	}

}
