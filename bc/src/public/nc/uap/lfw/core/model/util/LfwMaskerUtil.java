package nc.uap.lfw.core.model.util;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.vo.bd.format.FormatDocVO;
import nc.vo.bd.format.FormatMeta;
import nc.vo.pub.format.meta.AddressFormatMeta;
import nc.vo.pub.format.meta.CurrencyFormatMeta;
import nc.vo.pub.format.meta.DateFormatMeta;
import nc.vo.pub.format.meta.NumberFormatMeta;
import nc.vo.pub.format.meta.TimeFormatMeta;

import org.apache.commons.lang.StringUtils;

/**
 * LFW��ʽ��������
 * 
 * @author licza
 * 
 */
public class LfwMaskerUtil {
	private static final String REGEX = ",";
	/** Ĭ����� **/
	private static final String DEFAULTKEYLEFT = "default";
	/** Ĭ���Ҽ� **/
	private static final long DEFAULTKEYRIGHT = 0L;
	/** ���� **/
	private static final String PUB_MASKER_CACHE = "PUB_MASKER_CACHE";
	/**
	 * ע��Masker��Ϣ����
	 * @param md
	 */
	@SuppressWarnings("unchecked")
	public static void registerMasker(FormatDocVO md){
		String keyL = DEFAULTKEYLEFT;
		Long keyR = DEFAULTKEYRIGHT;
		try {
			String dsName = LfwRuntimeEnvironment.getDatasource();
			ILfwCache pubCache = LfwCacheManager.getStrongCache(PUB_MASKER_CACHE, dsName);
			if(md != null && md.getPk_formatdoc() != null){
				keyL = md.getPk_formatdoc();
				keyR = md.getTs().getMillis();
			} 
			Map<Long,String> maskerCache = (Map<Long,String>) pubCache.get(keyL);
			if(maskerCache == null){
				maskerCache = new ConcurrentHashMap<Long, String>();
				pubCache.put(keyL, maskerCache);
			}
			//��������˵�ǰ�ļ�ֵ  ������
			if(maskerCache.containsKey(keyR))
				return;
			
			//�����������ǰ��ֵ 
			boolean hasKey = !maskerCache.isEmpty();
			if(hasKey){
				Long[] keyRSet = maskerCache.keySet().toArray(new Long[0]);
				Arrays.sort(keyRSet);
				//����Ҽ�
				//������Ҽ� ȡ�����Ҽ���ֵ����,�뵱ǰ��ֵ���Ƚ�
				Long lastKeyR =  keyRSet[keyRSet.length -1];
				//�����ǰ�ȽϾ�,��������
				if(lastKeyR > keyR)
					return;
			}
			//���û���Ҽ� ֱ��д��
			//�����ǰ�ıȽ���,��������Ҽ� ����ǰ��д��
			maskerCache.clear();
			maskerCache.put(keyR, genMaskerInfoScript(md));
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			// ע��key��sessionBean��
			LfwSessionBean seb = LfwRuntimeEnvironment.getLfwSessionBean();
			if(seb != null){
				StringBuffer maskerKeySb = new StringBuffer();
				maskerKeySb.append(keyL).append(",").append(keyR);
				seb.setMaskerkey(maskerKeySb.toString());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void makeMaskerScript(StringBuffer sb){
		String keyL =  DEFAULTKEYLEFT;
		Long keyR = DEFAULTKEYRIGHT;
		String dsName = LfwRuntimeEnvironment.getDatasource();
		ILfwCache pubCache = LfwCacheManager.getStrongCache(PUB_MASKER_CACHE, dsName);
		LfwSessionBean seb = LfwRuntimeEnvironment.getLfwSessionBean();
		if(seb != null){
			String Maskerkey = seb.getMaskerkey();
			if(StringUtils.isNotEmpty(Maskerkey)){
				String[] keys = Maskerkey.split(",");
				keyL = keys[0];
				if(keys.length == 2)
				keyR =  Long.parseLong(keys[1]); 
			}
		}
		//δ��¼ �޻���  ��ʼ��Ĭ�ϻ���
		if(seb == null && pubCache.get(keyL) == null)
			registerMasker(null);
		
		Map<Long,String> maskerCache = (Map<Long,String>) pubCache.get(keyL);
		if(maskerCache != null && !maskerCache.isEmpty()) {
			if(maskerCache.containsKey(keyR)){
				sb.append(maskerCache.get(keyR));
				return;
			}else{
				Long[] keyRSet = maskerCache.keySet().toArray(new Long[0]);
				Arrays.sort(keyRSet);
				//����Ҽ�
				//������Ҽ� ȡ�����Ҽ���ֵ����,�뵱ǰ��ֵ���Ƚ�
				Long lastKeyR =  keyRSet[keyRSet.length -1];
				//�����ǰ�ȽϾ� 
				if(lastKeyR > keyR){
					sb.append(maskerCache.get(lastKeyR));
					return;
				} 
			}
		}
		//Ĭ��
		sb.append(genMaskerInfoScript(null));
		return;
	}
	
	/**
	 * ����Masker��Ϣ�ű�
	 * @param md
	 */
	public static String genMaskerInfoScript(FormatDocVO md){
		StringBuffer sb = new StringBuffer();
		genHead(sb);
		try {
			genBody(md, sb);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}
		genTail(sb);
		return sb.toString();
	}
 
	/**
	 * ����Masker����ϸ��Ϣ
	 * @param md
	 * @param sb
	 */
	public static void genBody(FormatDocVO md ,StringBuffer sb){
		if(md == null || md.getPk_formatdoc() == null || md.getPk_formatdoc().equals(DEFAULTKEYLEFT))
			return;
		FormatMeta  fm = md.getFm();
		if(fm == null)
			return;
		AddressFormatMeta afm = fm.getAfm().toNCFormatMeta();
		CurrencyFormatMeta cfm = fm.getCfm().toNCFormatMeta();
		TimeFormatMeta tfm = fm.getTfm().toNCFormatMeta();
		DateFormatMeta dfm = fm.getDfm().toNCFormatMeta();
		NumberFormatMeta nfm = fm.getNfm().toNCFormatMeta();
		if(afm == null || cfm == null || tfm == null || dfm == null || nfm == null)
			return;
		//��ַ��ʽ����Ϣ
		sb.append("afm.express = '"+escape(afm.getExpress())+"';");
		sb.append("afm.separator = '"+escape(afm.getSeparator())+"';");
		
		//���Ҹ�ʽ��Ϣ
		sb.append("cfm.isNegRed = "+cfm.isNegRed()+";");
		sb.append("cfm.isMarkEnable = "+cfm.isMarkEnable()+";");
		sb.append("cfm.markSymbol = '"+escape(cfm.getMarkSymbol())+"';");
		sb.append("cfm.pointSymbol = '"+escape(cfm.getPointSymbol())+"';");
		sb.append("cfm.positiveFormat = '"+escape(cfm.getPositiveFormat())+"';");
		sb.append("cfm.negativeFormat = '"+escape(cfm.getNegativeFormat())+"';");
		
		//���ڸ�ʽ��Ϣ
		sb.append("dfm.format = '"+escape(dfm.getFormat())+"';");
		sb.append("dfm.speratorSymbol = '"+escape(dfm.getSperatorSymbol())+"';");
		sb.append("dtfm.format = '"+escape(dfm.getFormat())+" "+ escape(tfm.getFormat()) +"';");
		sb.append("dtfm.speratorSymbol = '"+escape(dfm.getSperatorSymbol())+"';");
		
		
		//ʱ���ʽ��Ϣ
		sb.append("tfm.format = '"+escape(tfm.getFormat())+"';");
		sb.append("tfm.speratorSymbol = '"+escape(tfm.getSperatorSymbol())+"';");
		
		//���ָ�ʽ��Ϣ
		sb.append("nfm.isNegRed = "+nfm.isNegRed()+";");
		sb.append("nfm.isMarkEnable = "+nfm.isMarkEnable()+";");
		sb.append("nfm.markSymbol = '"+escape(nfm.getMarkSymbol())+"';");
		sb.append("nfm.pointSymbol = '"+escape(nfm.getPointSymbol())+"';");
		sb.append("nfm.positiveFormat = '"+escape(nfm.getPositiveFormat())+"';");
		sb.append("nfm.negativeFormat = '"+escape(nfm.getNegativeFormat())+"';");
	}
	
	/**
	 * ����Masker���õ�ͷ
	 * @param sb
	 */
	public static void genHead(StringBuffer sb){
		sb.append("window.$maskerMeta = {};");
		sb.append("var afm = new AddressFormatMeta();\r\n");
		sb.append("var cfm = new CurrencyFormatMeta();\r\n");
		sb.append("var dfm = new DateFormatMeta();\r\n");
		sb.append("var tfm = new TimeFormatMeta();\r\n");
		sb.append("var nfm = new NumberFormatMeta();\r\n");
		sb.append("var dtfm = new DateTimeFormatMeta();\r\n");
	}
	/**
	 * ����Masker���õ�β
	 * @param sb
	 */
	public static void genTail(StringBuffer sb){
		sb.append("window.$maskerMeta.NumberFormatMeta = nfm;");
		sb.append("window.$maskerMeta.AddressFormatMeta = afm;");
		sb.append("window.$maskerMeta.CurrencyFormatMeta = cfm;");
		sb.append("window.$maskerMeta.DateFormatMeta = dfm;");
		sb.append("window.$maskerMeta.TimeFormatMeta = tfm;");
		sb.append("window.$maskerMeta.DateTimeFormatMeta = dtfm;");
	}
	
	/**
	 * ���ַ�������ת��
	 * @param str
	 * @return
	 */
	private static String escape(String str){
		return str.replace("'", "\'").replace("\"\"", "");
	}
}
