package nc.uap.lfw.core.tags;


/**
 * 容器型控件Tag接口
 * @author dengjt
 *
 */
public interface IContainerElementTag {
	/**
	 * 产生控件的头部部分
	 * @param parent
	 * @return
	 */
	public String generateHead();
	
	/**
	 * 产生控件的体部分
	 * @return
	 */
	public String generateTail();
	
	/**
	 * 产生控件script 头部脚本
	 * @return
	 */
	public String generateHeadScript();
	
	/**
	 * 产生控件Script 尾部脚本
	 * @return
	 */
	public String generateTailScript();
}
