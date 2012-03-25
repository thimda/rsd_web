package nc.uap.lfw.core.comp;

public interface IContainerComp<T extends WebElement> {
	public T getElementById(String id);
}
