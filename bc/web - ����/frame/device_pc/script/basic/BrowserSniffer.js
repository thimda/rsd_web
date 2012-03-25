/**
 * @fileoverview 判断浏览器类型
 * @author guoweic
 * @version 
 */
//var IS_IE = IS_FF = IS_OPERA = IS_CHROME = IS_SAFARI = IS_WEBKIT = IS_IE6 = IS_IE7 = IS_IE8 = IS_IE9 = false;

/**
 * 是IE浏览器
 * @public
 */
var IS_IE = false;
/**
 * 是Firefox浏览器
 * @public
 */
var IS_FF = false;
/**
 * 是Opera浏览器
 * @public
 */
var IS_OPERA = false;
/**
 * 是Chrome浏览器
 * @public
 */
var IS_CHROME = false;
/**
 * 是Safari浏览器
 * @public
 */
var IS_SAFARI = false;
/**
 * 是Webkit浏览器
 * @public
 */
var IS_WEBKIT = false;
/**
 * 是IE6浏览器
 * @public
 */
var IS_IE6 = false;
/**
 * 是IE7浏览器
 * @public
 */
var IS_IE7 = false;
/**
 * 是IE8浏览器
 * @public
 */
var IS_IE8 = false;
/**
 * 是IE9浏览器
 * @public
 */
var IS_IE9 = false;
var	IS_IOS = false;
var	IS_IPHONE = false;
var	IS_IPAD = false;

var BROWSER_VERSION = 0;

var ua = navigator.userAgent.toLowerCase(), s, o = {};
if (s=ua.match(/opera.([\d.]+)/)) {
	IS_OPERA = true;
} else if (s=ua.match(/msie ([\d.]+)/)) {
	IS_IE = true;
} else if (s=ua.match(/iphone/i)){
	IS_IOS = true;
	IS_IPHONE = true;
} else if (s=ua.match(/ipad/i)){
	IS_IOS = true;
	IS_IPAD = true;
} else if (s=ua.match(/firefox\/([\d.]+)/)) {
	IS_FF = true;
} else if (s=ua.match(/chrome\/([\d.]+)/)) {
	IS_CHROME = true;
} else if (s=ua.match(/version\/([\d.]+).*safari/)) {
	IS_SAFARI = true;
} else if (s=ua.match(/webkit\/([\d.]+)/)) {
	IS_WEBKIT = true;
} 

if (s && s[1]) {
	BROWSER_VERSION = parseFloat( s[1] );
} else {
	BROWSER_VERSION = 0;
}
if (IS_IE) {
	var intVersion = parseInt(BROWSER_VERSION);
	if (intVersion == 6) {
		IS_IE6 = true;
	} else if (intVersion == 7) {
		IS_IE7 = true;
	} else if (intVersion == 8) {
		IS_IE8 = true;
	} else if (intVersion == 9) {
		IS_IE9 = true;
	}
}

var ATTRFLOAT = IS_IE ? "styleFloat" : "cssFloat";
