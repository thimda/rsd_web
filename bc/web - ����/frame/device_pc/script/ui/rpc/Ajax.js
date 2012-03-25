/**
 * @fileoverview Ajax请求核心函数。lfw中所有ajax请求都应通过此文件中的函数发出。
 * @author dengjt
 */

/**
 * ajax请求封装
 * 
 * @param path 请求url，不能为空
 * @param queryStr 请求参数,如 a=1&b=2&c=3
 * @param returnFunc 回调函数。ajax请求完毕后会调用此函数
 * @param returnArgs 回调函数传入的参数
 * @param method 请求方式，"GET" or "POST",默认POST
 * @param asyn 是否异步，默认异步
 * @param format 是否是格式化数据，如果是，返回xml，否则返回text.默认为text
 * @class
 */
 
/**请求ID*/ 
Ajax.REQ_ID=1;
/**请求对队*/
Ajax.REQ_ARRAY = new Array();
/**等待中的请求MAP*/
Ajax.REQ_WAIT_MAP = new HashMap();

/**
 * 请求存储容器
 * 
 * @param {} xmlHttpReq
 * @param {} oThis
 */
function ReqContainer(xmlHttpReq, oThis) {
	this.xmlHttpReq = xmlHttpReq;
	this.oThis = oThis;
};


function Ajax() {
	this.path = null;
	this.queryStr = null;
	this.returnFunc = null;
	this.returnArgs = null;
	this.format = true;
	this.params = new HashMap();
	this.req_id = Ajax.REQ_ID++;
	Ajax.REQ_ARRAY.push(this.req_id);
};

Ajax.prototype.setPath = function(path) {
	this.path = path;
};

Ajax.prototype.setQueryStr = function(qryStr) {
	this.queryStr = qryStr;
};

Ajax.prototype.addParam = function(key, value) {
	this.params.put(key, value);
};

Ajax.prototype.setReturnFunc = function(returnFunc) {
	this.returnFunc = returnFunc;
};

Ajax.prototype.setReturnArgs = function(returnArgs) {
	this.returnArgs = returnArgs;
};

Ajax.prototype.setFormat = function(format) {
	this.format = format;
};

Ajax.prototype.getHttpProxy = function() {
	var xmlHttpReq;

	try {
		xmlHttpReq = new ActiveXObject("Msxml2.XMLHTTP");

	} catch (e) {
		try {
			xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");

		} catch (e) {
			try {
				xmlHttpReq = new XMLHttpRequest();
			} catch (e) {
			}
		}
	}
	return xmlHttpReq;
};

Ajax.prototype.get = function(asyn) {
	if (typeof (getStickString) != "undefined") {
		var stickStr = getStickString();
		if (stickStr != null && stickStr != "")
			this.queryStr = mergeParameter(this.queryStr, stickStr);
	}

	// 是否异步，默认异步
	asyn = getBoolean(asyn, true);
	if (this.queryStr == null) {
		this.queryStr = "";
	}

	if (this.returnFunc == null) {
		this.returnFunc = function() {
		};
	}

	try {
		// 标明是ajax请求,应为ajax请求传递的中文参数在后台不需要转化编码会正确获得
		var urlParam = this.path + "?" + this.queryStr + "&isAjax=1";
		if (this.queryStr != ""
				&& this.queryStr[this.queryStr.length - 1] != "&")
			urlParam += "&";
		// 由于get方法对同一个网址不会做第二次返回，所以为了请求正常进行，在网址后加随机参数
		urlParam += "tmprandid=" + Math.random();

		var xmlHttpReq = this.getHttpProxy();
		xmlHttpReq.open("GET", urlParam, asyn);
		xmlHttpReq.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
		xmlHttpReq.send(null);
		// 同步，直接返回结果
		if (!asyn) {
			//从请求队列中删除当前请求id
			if (this.req_id != null){
				for (i = 0 ; i < Ajax.REQ_ARRAY.length ; i++){
					if (Ajax.REQ_ARRAY[i] == this.req_id){
						Ajax.REQ_ARRAY.splice(i,1);
						break;
					}
				}
			}
			if (this.format)
				return xmlHttpReq.responseXML;
			return xmlHttpReq.responseText;
		}
		this.addCallBack(xmlHttpReq);
	} catch (e) {
		if (IS_IE)
			showErrorDialog("Ajax request error:" + e.name + " " + e.message);
		else
			showErrorDialog("Ajax request error:" + e);
	}
};

Ajax.prototype.post = function(asyn) {
	if (typeof (getStickString) != "undefined") {
		var stickStr = getStickString();
		if (stickStr != null && stickStr != "")
			this.queryStr = mergeParameter(this.queryStr, stickStr);
	}

	// 是否异步，默认异步
	asyn = getBoolean(asyn, true);
	if (this.queryStr == null) {
		this.queryStr = "";
	}

	if (this.returnFunc == null) {
		this.returnFunc = function() {
		};
	}

	var paramStr = "";
	if (this.params.size() > 0) {
		var keys = this.params.keySet();
		for ( var i = 0, count = keys.length; i < count; i++) {
			paramStr += keys[i] + "="
					+ encodeURIComponent(this.params.get(keys[i]));
			if (i != count - 1)
				paramStr += "&";
		}
	}

	var xmlHttpReq = this.getHttpProxy();
	xmlHttpReq.open("POST", this.path, asyn);
	xmlHttpReq.setRequestHeader("Method", "POST " + this.path + " HTTP/1.1");
	xmlHttpReq.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded;charset=UTF-8");
	var qryStr = this.queryStr;
	if (paramStr != "")
		qryStr += "&" + paramStr;
	if (asyn)
		this.addCallBack(xmlHttpReq);
	xmlHttpReq.send(qryStr + "&isAjax=1");
	// 同步，直接返回结果
	if (!asyn){
		//从请求队列中删除当前请求id
		if (this.req_id != null){
			for (i = 0 ; i < Ajax.REQ_ARRAY.length ; i++){
				if (Ajax.REQ_ARRAY[i] == this.req_id){
					Ajax.REQ_ARRAY.splice(i,1);
					break;
				}
			}
		}
		return this.returnFunc(xmlHttpReq, this.returnArgs, null, this);
	}
};

Ajax.prototype.addCallBack = function(xmlHttpReq) {
	var oThis = this;
	xmlHttpReq.onreadystatechange = function() {
		if (xmlHttpReq.readyState == 4) {
			if (xmlHttpReq.status == 200) {
				//没有请求ID时，或者请求对队列为空时，直接执行请求
				if(oThis.req_id == null || Ajax.REQ_ARRAY.length == 0){
					oThis.returnFunc(xmlHttpReq, oThis.returnArgs, null, oThis);
				}
				//请求ID排在队列第一位时，执行请求，并在队列中移除ID 
				else if (oThis.req_id == Ajax.REQ_ARRAY[0]){
					Ajax.REQ_ARRAY.shift();
					oThis.returnFunc(xmlHttpReq, oThis.returnArgs, null, oThis);
					while (Ajax.REQ_WAIT_MAP.containsKey(Ajax.REQ_ARRAY[0])){
						var req = Ajax.REQ_WAIT_MAP.get(Ajax.REQ_ARRAY[0]);
						var _oThis = req.oThis;
						var _xmlHttpReq = req.xmlHttpReq;
						_oThis.returnFunc(_xmlHttpReq, _oThis.returnArgs, null, _oThis);
						Ajax.REQ_WAIT_MAP.remove(Ajax.REQ_ARRAY[0]);
						Ajax.REQ_ARRAY.shift();
					}
				}
				//非以上情况时，判断请求ID是否在队列中，如果在队列中，该请求暂存在REQ_WAIT_MAP中,
				//否则直接执行请求
				else{
					var wait = false;
					for (i = 1 ; i < Ajax.REQ_ARRAY.length ; i++){
						if (Ajax.REQ_ARRAY[i] == oThis.req_id){
							wait = true;
							break;
						}
					}
					if (wait == true){
						Ajax.REQ_WAIT_MAP.put(oThis.req_id,new ReqContainer(xmlHttpReq,oThis));
					}else{
						oThis.returnFunc(xmlHttpReq, oThis.returnArgs, null, oThis);
					}
				}
			} else if (this.status == 306) {
				oThis.returnFunc(xmlHttpReq, oThis.returnArgs, "会话已失效,请重新登录", oThis);
			} else {
				oThis.returnFunc(xmlHttpReq, oThis.returnArgs,
						"Error occurred,response status is:"
								+ xmlHttpReq.status, oThis);
			}
		}
	};
};

function mergeParameter(sourceParam, appendParam) {
	if (sourceParam == null || sourceParam == "")
		return appendParam;
	else if (appendParam == null || appendParam == "")
		return sourceParam;
	else {
		return sourceParam + "&" + appendParam;
	}
};
