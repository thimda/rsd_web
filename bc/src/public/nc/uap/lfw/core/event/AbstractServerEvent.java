package nc.uap.lfw.core.event;

public abstract class AbstractServerEvent<T> implements IServerEvent<T>{
	private T element;
	private boolean stop = false;
	public AbstractServerEvent(T element) {
		this.element = element;
	}
	
	public T getSource() {
		return element;
	}
	
	public boolean isStop() {
		return stop;
	}
	
	public void stop() {
		this.stop = true;
	}
}
