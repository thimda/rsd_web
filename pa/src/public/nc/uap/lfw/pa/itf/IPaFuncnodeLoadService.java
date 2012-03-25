package nc.uap.lfw.pa.itf;

import java.util.List;

import nc.uap.lfw.pa.pamgr.PaFuncNodeProxyVO;
import nc.vo.pub.lang.UFBoolean;

public interface IPaFuncnodeLoadService {
	
	public List<PaFuncNodeProxyVO> getAllFucnodeVO();
	
	public List<PaFuncNodeProxyVO> getFucnodeVOBySpecial(UFBoolean flag);
	
}
