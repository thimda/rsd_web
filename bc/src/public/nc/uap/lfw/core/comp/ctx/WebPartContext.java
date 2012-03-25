package nc.uap.lfw.core.comp.ctx;

public class WebPartContext extends BaseContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2976668849241373885L;
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
	}

}
