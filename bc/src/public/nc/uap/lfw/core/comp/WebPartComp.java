package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.comp.ctx.WebPartContext;

public class WebPartComp extends WebComponent {
	private static final long serialVersionUID = 6341974963624217504L;
	private String contentFetcher;
	private String innerHTML;
	public String getContentFetcher() {
		return contentFetcher;
	}
	public void setContentFetcher(String contentFetcher) {
		this.contentFetcher = contentFetcher;
	}
	 
	public String getInnerHTML() {
		return innerHTML;
	}
	public void setInnerHTML(String innerHTML) {
		this.innerHTML = innerHTML;
		setCtxChanged(true);
	}
	public void setContext(WebPartContext context){
	}
	public WebPartContext getContext() {
		WebPartContext ctx = new WebPartContext();
		ctx.setId(this.getId());
		ctx.setInnerHTML(this.getInnerHTML());
		return ctx;
	}
	
}
