package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * Link 控件Tag
 * @author dengjt
 *
 */
public class LinkCompTag extends NormalComponentTag {

	/**
	 * 统一渲染控件占位符
	 */
	public String generateBody() {
		return super.generateBody();
	}
	public String generateBodyScript() {
		//LinkComp(parent, name, left, top, href, text, hasImg, srcImg, target, position, className)
		StringBuffer buf = new StringBuffer();
		LinkComp link = (LinkComp) getComponent();
		String linkId = getVarShowId(); 
		buf.append("window.")
		   .append(linkId)
		   .append(" = new LinkComp(document.getElementById('" + getDivShowId() + "'),'")
		   .append(link.getId())
		   .append("','0','0','")
		   .append(link.getHref())
		   .append("','")
		   //.append(   .append(translate(button.getI18nName(), button.getText(), button.getLangDir())).getI18nName())
		   .append(translate(link.getI18nName(), link.getI18nName(), link.getLangDir()))
		   .append("',")
		   .append(link.isHasImg())
		   .append(",'")
//		   .append(getRealImgPath(link.getImage()))
		   .append(link.getRealImage())
		   .append("','")
		   .append(link.getTarget())
		   .append("','relative', null);\n");
		
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + linkId + ");\n");    
		
		return buf.toString();
	}
	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_LINKCOMP;
	}

}
