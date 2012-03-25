package nc.uap.lfw.login.login;

import nc.bcmanage.bs.IBusiCenterManageService;
import nc.bcmanage.vo.BusiCenterVO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.pub.BusinessException;

/**
 * NC������Ϣ��ȡ��
 * @author gd 2010-4-8
 * @version NC6.0
 */
public class AccountDatas extends ComboData
{
	private static final long serialVersionUID = -937084332629423579L;

	@Override
	public CombItem[] getAllCombItems() {
		try {
			//��ȡ��ǰ��������
			String[][] options = getAccOptions();
			CombItem[] items = new CombItem[options.length];
			for (int j = 0; j < options.length; j++) {
				CombItem item = new CombItem();
				item.setText(options[j][1]);
				item.setValue(options[j][0]);
				items[j] = item;
			}	
			return items;
		} catch (BusinessException e) {
			Logger.error(e.getMessage(),e);
			throw new LfwRuntimeException(e);
		}
	}
	
	/**
	 * ��ȡ��ǰ����������
	 * @return
	 * @throws BusinessException
	 */
	private String[][] getAccOptions() throws BusinessException {
		IBusiCenterManageService busiCenter = NCLocator.getInstance().lookup(IBusiCenterManageService.class);
		BusiCenterVO[] accounts = busiCenter.getBusiCenterVOs();
		String[][] options;
		if (accounts != null && accounts.length > 1) {
			// ȥ��ϵͳ��������
			options = new String[accounts.length - 1][2];
			int j = 0;
			for (int i = 0; i < accounts.length; i++) {
				// ϵͳ���������Ƿ�̶�Ϊ 0000
				if (accounts[i].getCode().equals("0000"))
					continue;
				options[j][0] = accounts[i].getCode();
				options[j][1] = accounts[i].getName();
				j++;
			}
		} else
			options = new String[0][2];
		return options;
	}
}
