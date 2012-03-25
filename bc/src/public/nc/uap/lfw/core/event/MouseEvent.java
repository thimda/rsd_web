package nc.uap.lfw.core.event;

public class MouseEvent<T> extends AbstractServerEvent<T>{

	public MouseEvent(T webElement) {
		super(webElement);
	}

}
