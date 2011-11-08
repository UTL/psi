package webApplication.grafica;

public class ImageFileFilter extends CustomFileFilter {

	private static final String JUST_IMAGES = "Just Images";

	@Override
	protected boolean validExtension(String extension) {
		if (extension.equals(Utils.tiff) || extension.equals(Utils.tif)
				|| extension.equals(Utils.gif) || extension.equals(Utils.jpeg)
				|| extension.equals(Utils.jpg) || extension.equals(Utils.png))
			return true;
		else
			return false;
	}

	@Override
	public String getDescription() {
		return JUST_IMAGES;
	}

}
