package nc.uap.lfw.core.uif.listener;

public class SingleBodyInfo implements IBodyInfo {
	private static final long serialVersionUID = 8149430687381250235L;
	private String bodyDataset;
	public SingleBodyInfo(){
		
	}
	
	public SingleBodyInfo(String bodyDs){
		bodyDataset = bodyDs;
	}
	
	public String getBodyDataset() {
		return bodyDataset;
	}

	public void setBodyDataset(String bodyDataset) {
		this.bodyDataset = bodyDataset;
	}
}
