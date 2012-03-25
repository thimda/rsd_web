package nc.uap.lfw.design.itf;

import java.util.List;

import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * ��ȡ����Ԫ���ݵ���Ϣ�ӿ�
 * @author zhangxya
 *
 */

public interface IDatasetProvider {
	public List getALlModuels() throws LfwBusinessException;
	public List getAllComponentByModuleId(String moduleId) throws LfwBusinessException;
	public List getAllComponents() throws LfwBusinessException;
	public List getAllClasses() throws LfwBusinessException;
	public List getAllClassByComId(String componentId) throws LfwBusinessException;
	/**
	 * ������Ԫ���ݵ����ݼ�
	 * @param mdds
	 * @return
	 */
	public MdDataset getMdDataset(MdDataset mdds);
	public MdDataset mergeDataset(MdDataset mdds) throws LfwBusinessException;
	
	public List getNcRefNodeList(MdDataset ds) throws LfwBusinessException;
	public List  getNcComoboDataList(MdDataset ds) throws LfwBusinessException;
	public List  getRefMdDatasetList(MdDataset ds) throws LfwBusinessException;
	public List getFieldRelations(MdDataset mdds) throws LfwBusinessException;
	
	public String getAggVo(String fullClassName) throws LfwBusinessException;
	
	public LfwWidget getMdDsFromComponent(LfwWidget widget, String componetId) throws LfwBusinessException;
}
