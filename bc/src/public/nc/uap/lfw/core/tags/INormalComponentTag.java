package nc.uap.lfw.core.tags;


/**
 * 普通控件接口
 * @author dengjt
 *
 */
public interface INormalComponentTag {
	public String generateBody();
	public String generateBodyScript();
	/**
	 * 如果控件绑定ds，需要在这里写setDataset脚本。之所以不能在generateBodyScript中一起
	 * 搞定，是因为往往需要在事件绑定之后绑定ds
	 * @return
	 */
	public String generateDsBindingScript();
}
