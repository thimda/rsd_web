package nc.uap.lfw.ncadapter.billtemplate.gen;

import java.util.Map;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.page.LfwWidget;
import nc.vo.pub.bill.BillTempletVO;

/**
 * �ӵ���ģ��vo������web frameԪ�ؽӿڡ�
 * @author dengjt
 *
 */
public interface IWebElementGenerator<T extends WebElement> {
	public T[] generateElements(BillTempletVO vo, int type, LfwWidget widget, Map<String, Object> paramMap);
}
