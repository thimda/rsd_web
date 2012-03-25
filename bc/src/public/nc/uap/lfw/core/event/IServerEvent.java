package nc.uap.lfw.core.event;


public interface IServerEvent<T>{
	public T getSource();
	public boolean isStop();
}
