package nc.uap.lfw.render.observer;


public interface IUIElementObserver {
	
//	public Object notifyChange(String type, UIElement ele);
	public void notifyChange(String type, Object ele, Object userObject);
}
