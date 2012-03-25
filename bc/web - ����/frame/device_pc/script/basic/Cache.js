/**
 * 打印代理
 * @class 打印代理
 */
function PrintProxy() {
	if (window.$pdb == null) {
		try {
			window.$pdb = new ActiveXObject("UFGears.PrintServer");
			window.$pdb.setDBName("PrintDB", "");
		} catch (error) {
			window.$pdb = null;
		}
	}
}

/**
 * 预览
 */
PrintProxy.prototype.preview = function(obj) {
	return window.$pdb.preview(obj);
};

/**
 * 获取打印机
 */
PrintProxy.prototype.getVPrinters = function() {
	return window.$pdb.getVPrinters();
};

/**
 * 获取打印机
 */
PrintProxy.prototype.getPhPrinters = function() {
	if (window.$pdb == null)
		return "";
	var pp = window.$pdb.getPhPrinters();
	eval("var data = " + pp);
	return data.data.join();
};

/**
 * 打印文件
 */
PrintProxy.prototype.doPrintFile = function(id) {
	var obj = getSessionAttribute("print_content_" + id);
	if (obj == null) {
		obj = parent.getSessionAttribute("print_content_" + id);
	}
	this.preview(obj);
};

/**
 * 打印输出
 */
PrintProxy.prototype.printOut = function(printerName, idArr) {
	for ( var i = 0; i < idArr.length; i++) {
		var obj = getSessionAttribute("print_content_" + idArr[i]);
		window.$pdb.printOut(printerName, obj);
	}
};

/**
 * 客户端缓存代理
 * @class 客户端缓存代理
 * @param dbName 数据库名称
 */
function CacheProxy(dbName) {
	if (window.db == null) {
		try {
			window.db = new ActiveXObject("UFGears.LocalDB");
			var path = "";
			if(window.clientMode){
				if(window.offlineCachePath)
					path = window.offlineCachePath;
				else
					path = "c:\\demo\\db";
			}
			window.db.setDbName(dbName, path);
		} catch (error) {
			window.db = null;
		}
	}
}

/**
 * 客户端缓存是否初始化过
 */
CacheProxy.prototype.isInitialized = function() {
	var rt = db.GetCacheObjs("1");
	eval("var obj = " + rt);
	return obj.data.length > 0;
};

/**
 * 客户端缓存是否可用
 */
CacheProxy.prototype.isCacheEnabled = function() {
	return window.db != null;
};

/**
 * 初始化客户端缓存 （调用nc.lfw.core.servlet.ClientCacheServlet）
 */
CacheProxy.prototype.initCache = function() {
	showProgressDialog("正在下载客户端缓存,请稍候...");
	var ajax = new Ajax();
	ajax.setPath(window.globalPath + "/cache");
	ajax.addParam("type", "fullinit");
	ajax.setReturnFunc(CacheProxy.fullInitReturnFunc);
	ajax.post();
};

/**
 * 初始化客户端缓存后的回调方法
 */
CacheProxy.fullInitReturnFunc = function(xmlHttpReq, returnArgs, tip, ajax) {
	try {
		var resp = xmlHttpReq.responseText;
		var init = db.initCache(resp);
	} finally {
		hideProgressDialog();
	}
};

/**
 * 更新客户端缓存数据
 */
CacheProxy.prototype.updateCache = function(versionMap) {
	var rt = db.GetCacheObjs("1");
	eval("var obj = " + rt);
	if (obj.data.length > 0) {
		var tableVersionStr = new Array;
		for ( var i = 0; i < obj.data.length; i++) {
			var data = obj.data[i];
			var version = versionMap.map[data[0]];
			if (version != null) {
				if (version != data[2])
					tableVersionStr.push(data[0] + "," + data[2]);
			}
		}
	}
	tableVersionStr = tableVersionStr.join(';');
	if (tableVersionStr != "") {
		showProgressDialog("正在更新客户端缓存,请稍候...");
		var ajax = new Ajax();
		ajax.setPath(window.globalPath + "/cache");
		ajax.addParam("type", "update");
		ajax.addParam("tableVersion", tableVersionStr);
		ajax.setReturnFunc(CacheProxy.fullInitReturnFunc);
		ajax.post();
	}
};

/**
 * @private
 */
CacheProxy.prototype.isTableCached = function(tableName) {
	return db.isTableCached(tableName);
};

/**
 * 执行查询（从缓存数据库中执行）
 */
CacheProxy.prototype.executeQuery = function(sql) {
	return db.queryAll(sql);
};

/**
 * 执行查询（从缓存数据库中执行）
 */
CacheProxy.prototype.executeQueryByPage = function(sql, size, index) {
	return db.queryByPage(sql, size, index);
};

/**
 * 执行Sql语句
 */
CacheProxy.prototype.executeSql = function(sql) {
	return db.executeSql(sql);
};

/**
 * 获取打印代理
 * @return
 */
function getPrintProxy() {
	if (window.globalObject.printInstance == null)
		window.globalObject.printInstance = new PrintProxy();
	return window.globalObject.printInstance;
}

/**
 * 根据数据库名称获取客户端缓存
 * 
 * @param dbName
 * @return
 */
function getCacheProxy(dbName) {
	if(window.clientMode)
		dbName = "pbase_design";
	if (window.globalObject.cacheInstance == null)
		window.globalObject.cacheInstance = new Object;
	if (window.globalObject.cacheInstance[dbName] == null) {
		window.globalObject.cacheInstance[dbName] = new CacheProxy(dbName);
	}
	return window.globalObject.cacheInstance[dbName];
};
