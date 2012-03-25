package nc.uap.lfw.core.event;

/**
 * @author guoweic
 *
 */
public class SimpleEvent<T> extends AbstractServerEvent<T>{

	public SimpleEvent(T webElement) {
		super(webElement);
	}

}
