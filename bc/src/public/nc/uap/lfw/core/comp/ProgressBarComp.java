package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ProgressBarContext;


/**
 * Button øÿº˛≈‰÷√
 * @author dengjt
 *
 */
public class ProgressBarComp extends WebComponent {

	private static final long serialVersionUID = -3640014425289622883L;
	
	public static final String VALUE_ALIGN_RIGHT = "right";
	public static final String VALUE_ALIGN_LEFT = "left";
	public static final String VALUE_ALIGN_CENTER = "center";
	
	private String value;
	
	private String valueAlign = VALUE_ALIGN_RIGHT;
	
	public ProgressBarComp() {
//		this.setHeight("22");
//		this.setWidth("120");
	}
	
	public ProgressBarComp(String id) {
		super(id);
//		this.setHeight("22");
//		this.setWidth("120");
	}

	public Object clone(){
		return super.clone();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (!value.equals(this.value)) {
			this.value = value;
			setCtxChanged(true);
		}
	}

	public String getValueAlign() {
		return valueAlign;
	}

	public void setValueAlign(String valueAlign) {
		this.valueAlign = valueAlign;
	}
	
	@Override
	public BaseContext getContext() {
		ProgressBarContext ctx = new ProgressBarContext();
		ctx.setId(this.getId());
		ctx.setValue(this.value);
		ctx.setVisible(this.isVisible());
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		ProgressBarContext pbCtx = (ProgressBarContext) ctx;
		this.setValue(pbCtx.getValue());
		this.setVisible(pbCtx.isVisible());
		this.setCtxChanged(false);
	}

}
