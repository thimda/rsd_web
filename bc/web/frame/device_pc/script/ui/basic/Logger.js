/**
 * @fileoverview 客户端日志方法
 * @author guoweic
 * @version 6.0
 */

/**
 * 日志对象
 * @class 日志对象
 */
var Logger = new Object;

/**
 * 得到当前页面控制台
 */
Logger.getConsole = function() {
	if (IS_FF) {
		if (window.console) {
			return window.console;
		} else {
			return null;
		}
	} else {
		return null;
	}
};

/**
 * 输出日志
 */
Logger.log = function(param1, param2) {
	if (Logger.getConsole()) {
		if (param1 && param2)
			Logger.getConsole().log(param1, param2);
		else if (param1)
			Logger.getConsole().log(param1);
	}
};

/**
 * 输出提示信息
 */
Logger.info = function(param1, param2) {
	if (Logger.getConsole()) {
		if (param1 && param2)
			Logger.getConsole().info(param1, param2);
		else if (param1)
			Logger.getConsole().info(param1);
	}
};

/**
 * 输出警告
 */
Logger.warn = function(param1, param2) {
	if (Logger.getConsole()) {
		if (param1 && param2)
			Logger.getConsole().warn(param1, param2);
		else if (param1)
			Logger.getConsole().warn(param1);
	}
};

/**
 * 输出调试信息
 */
Logger.debug = function(param1, param2) {
	if (Logger.getConsole()) {
		if (param1 && param2)
			Logger.getConsole().debug(param1, param2);
		else if (param1)
			Logger.getConsole().debug(param1);
	}
};

/**
 * 输出错误信息
 */
Logger.error = function(param1, param2) {
	if (Logger.getConsole()) {
		if (param1 && param2)
			Logger.getConsole().error(param1, param2);
		else if (param1)
			Logger.getConsole().error(param1);
	}
};

/**
 * @private
 */
log = function(param1, param2) {
	Logger.log(param1, param2);
};

/**
 * @private
 */
info = function(param1, param2) {
	Logger.info(param1, param2);
};

/**
 * @private
 */
warn = function(param1, param2) {
	Logger.warn(param1, param2);
};

/**
 * @private
 */
debug = function(param1, param2) {
	Logger.debug(param1, param2);
};

/**
 * @private
 */
error = function(param1, param2) {
	Logger.error(param1, param2);
};
