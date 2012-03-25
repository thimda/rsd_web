package nc.uap.lfw.core;

import java.io.Serializable;
/**
 * 客户端浏览器检测类
 * @author dengjt
 *
 */
public class BrowserSniffer implements Serializable {
	private static final long serialVersionUID = 1L;
	private String browserString;
	
	public BrowserSniffer(String browserString){
		this.browserString = browserString;
	}
	
	public boolean isIE() {
		return true;
	}
	
	public boolean isIE6() {
		return true;
	}
	
	public boolean isIE7() {
		return true;
	}
	
	public boolean isIE8() {
		return false;
	}
	
	public boolean isFirefox() {
		return true;
	}
	
	public boolean isOpera() {
		return true;
	}
	public boolean isGBrowser() {
		return true;
	}
	
	public boolean isWebkit() {
		return true;
	}

	public String getBrowserString() {
		return browserString;
	}

	public void setBrowserString(String browserString) {
		this.browserString = browserString;
	}
	/**
	 * 是否Ipad
	 * @return
	 */
	public boolean isIpad(){
		return  this.browserString != null && this.browserString.length() > 0 && (this.browserString.indexOf("iPad") != -1 );
	}
	/**
	 * 是否Iphone
	 * @return
	 */
	public boolean isIphone(){
		return  this.browserString != null && this.browserString.length() > 0 && (this.browserString.indexOf("iPhone") != -1);
	}
	/**
	 * 是否Ios
	 * @return
	 */
	public boolean isIos(){
		return  this.browserString != null && this.browserString.length() > 0 && ( this.browserString.indexOf("iPad") != -1 ||  this.browserString.indexOf("iPhone") != -1);
	}
}
