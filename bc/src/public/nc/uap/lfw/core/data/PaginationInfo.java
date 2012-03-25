package nc.uap.lfw.core.data;

import java.io.Serializable;

import nc.uap.lfw.core.log.LfwLogger;

/**
 * DataSet的分页信息托管，通过该类管理DataSet的分页信息。
 * 
 * @author lkp 
 * @author gd 2008-03-07 增加recordsCount.pageCount是通过pageSize和recordsCount属性计算获取,本身只提供get方法
 *  
 */

public class PaginationInfo implements Cloneable, Serializable{

	private static final long serialVersionUID = 1L;

	/*每页记录条数, pageSize == -1, 表示不分页*/
	private int pageSize = -1;

	/*当前页面序号，从0开始。*/
	private int pageIndex;

	/*分页总数,该属性是通过recordsCount和pageSize计算获得,本身只提供getPageCount方法*/
	private int pageCount = 1;
	
	/*记录总数*/
	private int recordsCount = 0;
	
	/*是否客户端分页,默认是服务器端分页*/
	private boolean pageClient = false;
	
	/*用于配置分页的特别逻辑，此字符串的信息，会原封不动的放到*/
	private String pageInfo = null;

	/**
	 * 获取记录总数
	 * @return
	 */
	public int getRecordsCount() {
		return recordsCount;
	}
	
	/**
	 * 设置记录总数
	 * @param recordsCount
	 */
	public void setRecordsCount(int recordsCount) {
		if(recordsCount >= 0)
		{
			this.recordsCount = recordsCount;
			// 根据前台逻辑,没有记录pageCount也应该是"1"而不是"0"
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
	 * 获取分页页面记录条数
	 * 因在viewModel中，DataSet仅有此分页相关属性，因此
	 * 可以认为当pagesize==-1(或<0)时不支持分页显示。       * 
	 * @return
	 */
	public int getPageSize() {

		return this.pageSize;
	}

	/**
	 * 设置分页页面条数
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
	 * 获取当前实现的页面号码
	 * @return
	 */
	public int getPageIndex() {

		return this.pageIndex;
	}

	/**
	 * 设置当前分页号码
	 * @param index
	 */
	public void setPageIndex(int index) {

		this.pageIndex = index;
	}

	/**
	 * 获取页面总数
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
	 * 实现克隆方法
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
