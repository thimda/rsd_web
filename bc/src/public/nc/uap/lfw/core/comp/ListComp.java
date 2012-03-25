package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.ListListener;

/**
 * List�ؼ���̨������
 * @author gd 2008-07-01
 *
 */
public class ListComp extends WebComponent implements IDataBinding {

	private static final long serialVersionUID = -5601349522686730685L;
	private String dataset;
	private ComboData comboData;
	// Ҫɾ��HTML�������λ��
	private int delOptionItemHtmlIndex;
	// Ҫɾ�����λ��
	private int delOptionItemIndex;
	// Ҫɾ�����λ�ü���
	private int[] delOptionItemIndexs;
	
	public String getDataset() {
		return dataset;
	}


	public void setDataset(String dataset) {
		this.dataset = dataset;
		setCtxChanged(true);
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(ListListener.class);
		return list;
	}


	public ComboData getComboData() {
		return comboData;
	}


	public void setComboData(ComboData comboData) {
		this.comboData = comboData;
		setCtxChanged(true);
	}


	public int getDelOptionItemHtmlIndex() {
		return delOptionItemHtmlIndex;
	}


	public void setDelOptionItemHtmlIndex(int delOptionItemHtmlIndex) {
		this.delOptionItemHtmlIndex = delOptionItemHtmlIndex;
		setCtxChanged(true);
	}


	public int getDelOptionItemIndex() {
		return delOptionItemIndex;
	}


	public void setDelOptionItemIndex(int delOptionItemIndex) {
		this.delOptionItemIndex = delOptionItemIndex;
		setCtxChanged(true);
	}


	public int[] getDelOptionItemIndexs() {
		return delOptionItemIndexs;
	}


	public void setDelOptionItemIndexs(int[] delOptionItemIndexs) {
		this.delOptionItemIndexs = delOptionItemIndexs;
		setCtxChanged(true);
	}

}
