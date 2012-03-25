package nc.uap.lfw.core.data;

import java.io.Serializable;

import nc.uap.lfw.core.log.LfwLogger;

/**
 * DataSet�ķ�ҳ��Ϣ�йܣ�ͨ���������DataSet�ķ�ҳ��Ϣ��
 * 
 * @author lkp 
 * @author gd 2008-03-07 ����recordsCount.pageCount��ͨ��pageSize��recordsCount���Լ����ȡ,����ֻ�ṩget����
 *  
 */

public class PaginationInfo implements Cloneable, Serializable{

	private static final long serialVersionUID = 1L;

	/*ÿҳ��¼����, pageSize == -1, ��ʾ����ҳ*/
	private int pageSize = -1;

	/*��ǰҳ����ţ���0��ʼ��*/
	private int pageIndex;

	/*��ҳ����,��������ͨ��recordsCount��pageSize������,����ֻ�ṩgetPageCount����*/
	private int pageCount = 1;
	
	/*��¼����*/
	private int recordsCount = 0;
	
	/*�Ƿ�ͻ��˷�ҳ,Ĭ���Ƿ������˷�ҳ*/
	private boolean pageClient = false;
	
	/*�������÷�ҳ���ر��߼������ַ�������Ϣ����ԭ�ⲻ���ķŵ�*/
	private String pageInfo = null;

	/**
	 * ��ȡ��¼����
	 * @return
	 */
	public int getRecordsCount() {
		return recordsCount;
	}
	
	/**
	 * ���ü�¼����
	 * @param recordsCount
	 */
	public void setRecordsCount(int recordsCount) {
		if(recordsCount >= 0)
		{
			this.recordsCount = recordsCount;
			// ����ǰ̨�߼�,û�м�¼pageCountҲӦ����"1"������"0"
			if(pageSize > 0)
			{
				if(recordsCount != 0 && recordsCount % pageSize == 0)
					pageCount = recordsCount / pageSize;
				else
					pageCount = recordsCount / pageSize + 1;
			}
		}
		else
			this.recordsCount = -1;	
	}
	
	/**
	 * ��ȡ��ҳҳ���¼����
	 * ����viewModel�У�DataSet���д˷�ҳ������ԣ����
	 * ������Ϊ��pagesize==-1(��<0)ʱ��֧�ַ�ҳ��ʾ��       * 
	 * @return
	 */
	public int getPageSize() {

		return this.pageSize;
	}

	/**
	 * ���÷�ҳҳ������
	 * @param size
	 */
	public void setPageSize(int size) {
		if(size > 0)
		{	
			this.pageSize = size;
			if(recordsCount > 0)
			{
				if(recordsCount % pageSize == 0)
					pageCount = recordsCount / pageSize;
				else
					pageCount = recordsCount / pageSize + 1;
			}
		}
		else
			this.pageSize = -1;
	}

	/**
	 * ��ȡ��ǰʵ�ֵ�ҳ�����
	 * @return
	 */
	public int getPageIndex() {

		return this.pageIndex;
	}

	/**
	 * ���õ�ǰ��ҳ����
	 * @param index
	 */
	public void setPageIndex(int index) {

		this.pageIndex = index;
	}

	/**
	 * ��ȡҳ������
	 * @return
	 */
	public int getPageCount() {

		return this.pageCount;
	}
	
	public boolean isPageClient() {
		return pageClient;
	}
	
	public void setPageClient(boolean pageClient) {
		this.pageClient = pageClient;
	}

	/**
	 * ʵ�ֿ�¡����
	 */
	public Object clone(){
		
		PaginationInfo p = null;
		try {
			p = (PaginationInfo)super.clone();
		} catch (CloneNotSupportedException e) {
			LfwLogger.error(e.getMessage(), e);;
		}
	     return p;
	}

	public String getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(String pageInfo) {
		this.pageInfo = pageInfo;
	}


}
