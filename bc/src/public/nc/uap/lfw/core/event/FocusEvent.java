package nc.uap.lfw.core.event;


public class FocusEvent<T> extends AbstractServerEvent<T> {

	public FocusEvent(T webElement) {
		super(webElement);
	}

}
