package nc.uap.lfw.ra.render;

public class RenderHelper {
	public static String formatMeasureStr(String width) {
		if(width == null || width.equals(""))
			return "0px";
		if (width.indexOf("%") != -1 || width.indexOf("px") != -1)
			return width;
		return width + "px";
	}
}
