package nc.uap.lfw.ncadapter.billtemplate.gen;

public final class IdMaskUtil {
	public static String maskId(String oriId){
		oriId = oriId.replaceAll("\\.", "_");
		oriId = oriId.replaceAll("\\-", "_");
		return oriId;
	}
}
