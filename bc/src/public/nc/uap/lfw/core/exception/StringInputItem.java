package nc.uap.lfw.core.exception;

public class StringInputItem extends InputItem {

	public StringInputItem(String inputId, String labelText, boolean required)
	{
		super(inputId, labelText, required);
	}
	
	@Override
	public String getInputType() {
		return STRING_TYPE;
	}

}
