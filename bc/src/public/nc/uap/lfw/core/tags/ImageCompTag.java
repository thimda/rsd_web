package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * Image Comp Tag
 * @author dengjt
 *
 */
public class ImageCompTag extends NormalComponentTag {

	@Override
	//处理图片没有设高度宽度的情况
	public String generateBody() {
		
			return super.generateBody();
	}

	public String generateBodyScript() {
		StringBuffer buf = new StringBuffer();
		ImageComp image = (ImageComp) getComponent();
		//ImageComp(parent, name, refImg1, left, top, width, height, alt, refImg2, attrObj) 
		String id = getVarShowId();
		buf.append("window.")
		   .append(id)
		   .append(" = new ImageComp(document.getElementById('")
		   .append(getDivShowId())
		   .append("'),'")
		   .append(image.getId())
		   .append("','")
//		   .append(getRealImgPath(image.getImage1()))
		   .append(image.getRealImage1())
		   .append("','0','0','")
		   .append("100%")
		   .append("','")
		   .append("height")
		   .append("','")
		   .append(image.getAlt())
		   .append("',");
		if (image.getImage2() != null) {
			buf.append("'")
//			   .append(getRealImgPath(image.getImage2()))
			   .append(image.getRealImage2())
			   .append("'");
		} else {
			buf.append("null");
		}
		//TODO AttrObj
		if (image.getImageInact() != null && !"".equals(image.getImageInact()))
			buf.append(",{inactiveImg:\"" + image.getImageInact() + "\"});\n");
		else
			buf.append(",null);\n");
		   
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");
		return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_IMAGECOMP;
	}

}
