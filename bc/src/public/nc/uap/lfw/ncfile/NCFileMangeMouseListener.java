package nc.uap.lfw.ncfile;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import nc.uap.lfw.core.InteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.MouseServerListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.FileUploadHelper;
import nc.uap.lfw.file.action.FileSystemAction;
import nc.uap.lfw.file.helper.Scene;
import nc.uap.lfw.file.helper.SceneFactory;
import sun.misc.BASE64Encoder;
/**
 * nc�ļ�����Listener
 * @author zhangxya
 *
 */
public class NCFileMangeMouseListener extends MouseServerListener {
	public static final String MENU_UPLOAD = "menu_upload";
	public static final String MENU_DOWN = "menu_down";
	public static final String MENU_DELETE = "menu_delete";
	@Override
	public void onclick(MouseEvent e) {
		String menuItemId = ((MenuItem) e.getSource()).getId();
		//�ϴ�
		if (MENU_UPLOAD.equals(menuItemId)) {
			Scene scene = SceneFactory.useCompletListenerScene(NCFileUploadCompServerListener.class.getName(), "main", "ds_ncfile");
			scene.setFileMananger("nc.uap.lfw.ncfile.NCFileManage");
			String billitem = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(FileSystemAction.BILLITEM);
			scene.setBillItem(billitem);
			new FileUploadHelper(scene, getGlobalContext()).upload();
		} 
		//����
		else if (MENU_DOWN.equals(menuItemId)) {
			Dataset ds = getGlobalContext().getPageMeta().getWidget("main").getViewModels().getDataset("ds_ncfile");
			Row row = ds.getSelectedRow();
			if(row == null)
				throw new LfwRuntimeException("��ѡ������������!");
			String pk = (String) row.getValue(1);
			try {
				pk = new BASE64Encoder().encode(pk.getBytes("gbk"));
				pk = URLEncoder.encode(URLEncoder.encode(pk, "utf-8"), "utf-8");
			} catch (UnsupportedEncodingException e1) {}
			String downurl = LfwRuntimeEnvironment.getCorePath() + "/pt/file/down?id=" + pk + "&filemanager=nc.uap.lfw.ncfile.NCFileManage";
			getGlobalContext().downloadFileInIframe(downurl);//.addExecScript("window.open('" + downurl + "')")
			return;
		} 
		//ɾ��
		else if (MENU_DELETE.equals(menuItemId)) {
			Dataset ds = getGlobalContext().getPageMeta().getWidget("main").getViewModels().getDataset("ds_ncfile");
			Row row = ds.getSelectedRow();
			if(row == null)
				throw new LfwRuntimeException("��ѡ��ɾ����!");
			if (null == InteractionUtil.getConfirmDialogResult()) {
				// ǰ̨��ʾȷ�϶Ի���
				InteractionUtil.showConfirmDialog("ȷ�϶Ի���", "ȷ��Ҫɾ���ϴ��ļ���?");
			}else if (InteractionUtil.getConfirmDialogResult().equals(Boolean.TRUE)) {
				String pk = (String) row.getValue(1);
				NCFileManage fileMange = new NCFileManage();
				try {
					fileMange.delete(pk);
				} catch (Exception e1) {
					LfwLogger.error(e1.getMessage(), e1);
				}
				ds.removeRow(row);
			}
			else
				return;
		}
	}
	public NCFileMangeMouseListener(LfwPageContext context, String widgetId) {
		super(context, widgetId);
	}
}
