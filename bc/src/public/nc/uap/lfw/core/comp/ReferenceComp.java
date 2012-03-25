package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ReferenceTextContext;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.KeyListener;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.conf.ReferenceTextListener;
import nc.uap.lfw.core.event.conf.TextListener;
import nc.uap.lfw.core.exception.LfwPluginException;
import nc.uap.lfw.reference.util.LfwRefUtil;

/**
 * 参照
 * @author zhangxya
 *
 */
public class ReferenceComp extends TextComp{
	private static final long serialVersionUID = 1531534959905591230L;
	//引用refnode
	private String refcode;
	private String showValue = null;
	
	public ReferenceComp() {
		setEditorType(EditorTypeConst.REFERENCE);
	}
	
	public String getRefcode() {
		return refcode;
	}
	public void setRefcode(String refcode) {
		this.refcode = refcode;
	};
	public String getShowValue() {
		return showValue;
	}
	public void setShowValue(String showValue) {
		if (showValue != null && !showValue.equals(this.showValue)) {
			this.showValue = showValue;
			setCtxChanged(true);
		}
	}


	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		list.add(KeyListener.class);
		list.add(TextListener.class);
		list.add(ReferenceTextListener.class);
		return list;
	}
	
	@Override
	public BaseContext getContext() {
		ReferenceTextContext textCtx = new ReferenceTextContext();
		textCtx.setReadOnly(this.isReadOnly());
		textCtx.setValue(this.getValue());
		LfwRefUtil.fetchRefShowValue(getWidget(), this);
		textCtx.setShowValue(this.getShowValue());
		textCtx.setEnabled(this.isEnabled());
		textCtx.setFocus(this.isFocus());
		textCtx.setVisible(this.isVisible());
		return textCtx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		ReferenceTextContext textCtx = (ReferenceTextContext) ctx;
		this.setEnabled(textCtx.isEnabled());
		this.setValue(textCtx.getValue());
		this.setShowValue(textCtx.getShowValue());
		this.setReadOnly(textCtx.isReadOnly());
		this.setVisible(textCtx.isVisible());
		this.setCtxChanged(false);
	}
	
	public void validate(){
		StringBuffer buffer = new StringBuffer();
		if(this.getId() == null || this.getId().equals("")){
			buffer.append("参照文本框的ID不能为空!\r\n");
		}
		if(this.getText() == null || this.getText().equals("")){
			buffer.append("参照文本框Text不能为空!\r\n");
		}
		if(this.getRefcode() == null || this.getRefcode().equals(""))
			buffer.append("参照文本框RefCode不能为空!\r\n");
		if(buffer.length() > 0)
			throw new  LfwPluginException(buffer.toString());
		
		
	}
	
}