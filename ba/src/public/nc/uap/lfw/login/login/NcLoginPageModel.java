package nc.uap.lfw.login.login;

import nc.bcmanage.bs.IBusiCenterManageService;
import nc.bcmanage.vo.BusiCenterVO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.PageMeta;
import nc.vo.pub.BusinessException;

/**
 * 登陆PageModel
 * 
 * @author luoyf
 * @version 6.0 
 */
public class NcLoginPageModel extends PageModel {

	public static final String NC_LOGIN_DS = "nclogin_ds";
	public static final String NC_LOGIN_FORM = "nclogin_form";
	public static final String ERROR_INFO_DS = "errorInfoDs";

	public NcLoginPageModel() {
		super();
	}

	@Override
	protected PageMeta createPageMeta() {
		
		PageMeta meta = super.createPageMeta();
		this.processPageMeta(meta);
		return meta;
	}


	/**
	 * 对系统配置的pageMeta进行个性化设置
	 * 
	 * @param meta
	 */
	protected void processPageMeta(PageMeta meta) {
		//添加帐套信息
		
		try {
			//获取当前所有帐套
			String[][] options = getAccOptions();
			//定义静态聚合数据
			StaticComboData comboData = new StaticComboData();
			comboData.setId("accountCombo");
			meta.getWidget("loginwidget").getViewModels().addComboData(comboData);
			for (int j = 0; j < options.length; j++) {
				CombItem item = new CombItem();
				item.setI18nName(options[j][1]);
				item.setValue(options[j][0]);
				comboData.addCombItem(item);
			}
//			Dataset ds = meta.getWidget("loginwidget").getViewModels().getDataset("nclogin_ds");
//			Row row = ds.getEmptyRow();
//			row.setString(0, options[0][0]);
//			row.setString(0, options[0][1]);
//			ds.getRowSet().addRow(row);
//			
		} catch (BusinessException e) {
			Logger.error(e.getMessage(),e);
		}
	}


	/**
	 * 获取当前的所有帐套
	 * @return
	 * @throws BusinessException
	 */
	private String[][] getAccOptions() throws BusinessException {
		IBusiCenterManageService busiCenter = NCLocator.getInstance().lookup(IBusiCenterManageService.class);
		BusiCenterVO[] accounts = busiCenter.getBusiCenterVOs();
		String[][] options;
		if (accounts != null && accounts.length > 1) {
			// 去掉系统管理帐套
			options = new String[accounts.length - 1][2];
			int j = 0;
			for (int i = 0; i < accounts.length; i++) {
				// 系统管理帐套是否固定为 0000
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
