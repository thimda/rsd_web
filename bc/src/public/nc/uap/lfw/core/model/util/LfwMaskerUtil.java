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
 * LFW格式化工具类
 * 
 * @author licza
 * 
 */
public class LfwMaskerUtil {
	private static final String REGEX = ",";
	/** 默认左键 **/
	private static final String DEFAULTKEYLEFT = "default";
	/** 默认右键 **/
	private static final long DEFAULTKEYRIGHT = 0L;
	/** 缓存 **/
	private static final String PUB_MASKER_CACHE = "PUB_MASKER_CACHE";
	/**
	 * 注册Masker信息缓存
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
			//如果包含了当前的键值  不处理
			if(maskerCache.containsKey(keyR))
				return;
			
			//如果不包含当前键值 
			boolean hasKey = !maskerCache.isEmpty();
			if(hasKey){
				Long[] keyRSet = maskerCache.keySet().toArray(new Long[0]);
				Arrays.sort(keyRSet);
				//最大右键
				//如果有右键 取所有右键键值最大的,与当前键值做比较
				Long lastKeyR =  keyRSet[keyRSet.length -1];
				//如果当前比较旧,不做处理
				if(lastKeyR > keyR)
					return;
			}
			//如果没有右键 直接写入
			//如果当前的比较新,清除所有右键 将当前键写入
			maskerCache.clear();
			maskerCache.put(keyR, genMaskerInfoScript(md));
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			// 注册key到sessionBean中
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
		//未登录 无缓存  初始化默认缓存
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
				//最大右键
				//如果有右键 取所有右键键值最大的,与当前键值做比较
				Long lastKeyR =  keyRSet[keyRSet.length -1];
				//如果当前比较旧 
				if(lastKeyR > keyR){
					sb.append(maskerCache.get(lastKeyR));
					return;
				} 
			}
		}
		//默认
		sb.append(genMaskerInfoScript(null));
		return;
	}
	
	/**
	 * 生成Masker信息脚本
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
	 * 生产Masker的详细信息
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
		//地址格式化信息
		sb.append("afm.express = '"+escape(afm.getExpress())+"';");
		sb.append("afm.separator = '"+escape(afm.getSeparator())+"';");
		
		//货币格式信息
		sb.append("cfm.isNegRed = "+cfm.isNegRed()+";");
		sb.append("cfm.isMarkEnable = "+cfm.isMarkEnable()+";");
		sb.append("cfm.markSymbol = '"+escape(cfm.getMarkSymbol())+"';");
		sb.append("cfm.pointSymbol = '"+escape(cfm.getPointSymbol())+"';");
		sb.append("cfm.positiveFormat = '"+escape(cfm.getPositiveFormat())+"';");
		sb.append("cfm.negativeFormat = '"+escape(cfm.getNegativeFormat())+"';");
		
		//日期格式信息
		sb.append("dfm.format = '"+escape(dfm.getFormat())+"';");
		sb.append("dfm.speratorSymbol = '"+escape(dfm.getSperatorSymbol())+"';");
		sb.append("dtfm.format = '"+escape(dfm.getFormat())+" "+ escape(tfm.getFormat()) +"';");
		sb.append("dtfm.speratorSymbol = '"+escape(dfm.getSperatorSymbol())+"';");
		
		
		//时间格式信息
		sb.append("tfm.format = '"+escape(tfm.getFormat())+"';");
		sb.append("tfm.speratorSymbol = '"+escape(tfm.getSperatorSymbol())+"';");
		
		//数字格式信息
		sb.append("nfm.isNegRed = "+nfm.isNegRed()+";");
		sb.append("nfm.isMarkEnable = "+nfm.isMarkEnable()+";");
		sb.append("nfm.markSymbol = '"+escape(nfm.getMarkSymbol())+"';");
		sb.append("nfm.pointSymbol = '"+escape(nfm.getPointSymbol())+"';");
		sb.append("nfm.positiveFormat = '"+escape(nfm.getPositiveFormat())+"';");
		sb.append("nfm.negativeFormat = '"+escape(nfm.getNegativeFormat())+"';");
	}
	
	/**
	 * 生产Masker配置的头
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
	 * 生产Masker配置的尾
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
	 * 对字符串进行转义
	 * @param str
	 * @return
	 */
	private static String escape(String str){
		return str.replace("'", "\'").replace("\"\"", "");
	}
}
