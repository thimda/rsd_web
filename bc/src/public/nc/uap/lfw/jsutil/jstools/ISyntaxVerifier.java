package nc.uap.lfw.jsutil.jstools;

import java.util.List;

import nc.uap.lfw.jsutil.jstools.vo.VerifyInfo;

public interface ISyntaxVerifier {
	public VerifyInfo[] verify(String[] lines);
	public VerifyInfo[] verify(List<String> lines);
}
