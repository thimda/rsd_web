/**
 * 
 */
package nc.uap.lfw.pa;

import java.io.Serializable;
import java.util.List;

import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-9-8
 * @since 1.6
 */
public class TemplateProperty implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	
	private SuperVO vo;
	
	private List<TemplateProperty> childList; 

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SuperVO getVo() {
		return vo;
	}

	public void setVo(SuperVO vo) {
		this.vo = vo;
	}

	public List<TemplateProperty> getChildList() {
		return childList;
	}

	public void setChildList(List<TemplateProperty> childList) {
		this.childList = childList;
	}
	
}
