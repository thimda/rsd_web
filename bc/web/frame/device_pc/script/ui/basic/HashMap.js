/**
 * 模拟HashMap
 * @class 模拟HashMap
 */
function HashMap() {
	this.length = 0;
	this.prefix = "js_hashmap_pre_";
	this.trueObj = new Object;
	this.keyObj = new Object;
};

/**
 * 向HashMap中添加键值对
 */
HashMap.prototype.put = function(key, value) {
	if (this.keyObj[key] != null)
		this.trueObj[key] = value;
	else {
		this.trueObj[key] = value;
		this.keyObj[key] = 1;
		this.length++;
	}
};

/**
 * 从HashMap中获取value值
 */
HashMap.prototype.get = function(key) {
	return this.trueObj[key];
};

/**
 * 从HashMap中获取所有key的集合，以数组形式返回
 */
HashMap.prototype.keySet = function() {
	var arrKeySet = new Array();
	var index = 0;
	for ( var strKey in this.keyObj) {
		arrKeySet[index++] = strKey;
	}
	return arrKeySet;
};

/**
 * 从HashMap中获取value的集合，以数组形式返回
 */
HashMap.prototype.values = function() {
	var arrValues = new Array();
	var index = 0;
	for ( var strKey in this.keyObj) {
		arrValues[index++] = this.trueObj[strKey];
	}
	return arrValues;
};

/**
 * 获取HashMap的value值数量
 */
HashMap.prototype.size = function() {
	return this.length;
};

/**
 * 删除指定的值
 */
HashMap.prototype.remove = function(key) {
	if (this.keyObj[key] != null) {
		var obj = this.trueObj[key];
		delete this.keyObj[key];
		delete this.trueObj[key];
		this.length--;
		return obj;
	}
	return null;
};

/**
 * 清空HashMap
 */
HashMap.prototype.clear = function() {
	this.keyObj = new Object;
	this.trueObj = new Object;
	this.length = 0;
};

/**
 * 判断HashMap是否为空
 */
HashMap.prototype.isEmpty = function() {
	return this.length == 0;
};

/**
 * 判断HashMap是否存在某个key
 */
HashMap.prototype.containsKey = function(key) {
	return this.keyObj[key] != null;
};

/**
 * 判断HashMap是否存在某个value
 */
HashMap.prototype.containsValue = function(value) {
	for ( var strKey in this.trueObj) {
		if (this.trueObj[strKey] == value)
			return true;
	}
	return false;
};

/**
 * 把一个HashMap的值加入到另一个HashMap中，参数必须是HashMap
 */
HashMap.prototype.putAll = function(map) {
	if (map == null)
		return;
	if (map.constructor != HashMap)
		return;
	var arrKey = map.keySet();
	var arrValue = map.values();
	for ( var i in arrKey) {
		this.put(arrKey[i], arrValue[i]);
	}
};

/**
 * toString()方法
 */
HashMap.prototype.toString = function() {
	var str = "";
	for ( var strKey in this.keyObj)

	{
		str += strKey + " : " + this.trueObj[strKey] + "\r\n";
	}
	return str;
};