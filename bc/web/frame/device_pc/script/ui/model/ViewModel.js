/**
 * 数据集Relation客户端定义
 * @class 数据集Relation客户端定义
 * @constructor
 * @param id relation Id
 * @param masterDataset 主表ID
 * @param masterKeyFields 主表关联keys
 * @param detailDataset 子表ID
 * @param detailForeignkeys 子表外键
 * 
 * @author dengjt
 */
function DsRelation(id, masterDataset, masterKeyField, detailDataset,
		detailForeignkey) {
	this.id = id;
	this.masterDataset = masterDataset;
	this.masterKeyField = masterKeyField;
	this.detailDataset = detailDataset;
	this.detailForeignkey = detailForeignkey;
};

/**
 * Dataset关联关系
 * @class 当前页面所有数据集Relation的包装，每个页面有唯一实例。通过它可方便的获取查询相应的Relation。
 * @constructor
 * @author dengjt
 */
function DsRelations() {
	//关系对照表，key为master dataset id, value 为relation
	this.relationMap = new HashMap();
};

/**
 * 向页面Relations中添加一个Relation。此方法一般只被Web Frame调用
 * 
 * @param relation 数据集Relation
 */
DsRelations.prototype.addRelation = function(relation) {
	var relationArr;
	if ((relationArr = this.relationMap.get(relation.masterDataset)) == null) {
		relationArr = new Array;
		this.relationMap.put(relation.masterDataset, relationArr);
	}
	relationArr.push(relation);
};

/**
 * 根据relation id删除相应的relation。 <b>注意，必须保证所有的relation id不重复</b>
 * 
 * @param id
 */
DsRelations.prototype.removeRelation = function(id) {
	var masterArr = this.relationMap.values();
	for ( var i = 0; i < masterArr.length; i++) {
		for ( var j = 0; j < masterArr[i].length; j++) {
			if (masterArr[i][j].id == id)
				break;
		}
	}
};

/**
 * 根据主Dataset获取relation
 */
DsRelations.prototype.getRelationsByMasterDataset = function(masterDsId) {
	return this.relationMap.get(masterDsId);
};

/**
 * 根据关联Dataset获取relation
 */
DsRelations.prototype.getRelationsBySlaveDataset = function(slaveDsId) {
	var relArr = null;
	var relations = this.relationMap.values();
	if (relations != null && relations.length > 0) {
		for ( var i = 0; i < relations.length; i++) {
			for ( var j = 0; j < relations[i].length; j++) {
				var relation = relations[i][j];
				if (relation.detailDataset == slaveDsId) {
					if (relArr == null)
						relArr = new Array;
					relArr.push(relation);
				}
			}
		}
	}
	return relArr;
};

/**
 * 聚合数据前台对应类
 * @class 聚合数据前台对应类。可通过getComboData(id)获取。
 * @constructor
 * @param id 聚合数据id
 * @author dengjt
 */
function ComboData(id) {
	this.id = id;
	this.combItems = new Array;
};

/**
 * 获得值数组
 * 
 * @return 值String数组
 */
ComboData.prototype.getValueArray = function() {
	if (this.combItems.length == 0)
		return [];
	var result = new Array;
	for ( var i = 0; i < this.combItems.length; i++)
		result.push(this.combItems[i].value);
	return result;
};

/**
 * 获得名称数组
 * 
 * @return 名称String数组
 */
ComboData.prototype.getNameArray = function() {
	if (this.combItems.length == 0)
		return [];
	var result = new Array;
	for ( var i = 0; i < this.combItems.length; i++){
		result.push(this.combItems[i].i18nName);
	}
	return result;
};

/**
 * 获得图片路径数组
 * 
 * @return 名称String数组
 */
ComboData.prototype.getImageArray = function() {
	if (this.combItems.length == 0)
		return [];
	var result = new Array;
	for ( var i = 0; i < this.combItems.length; i++){
		result.push(this.combItems[i].image);
	}
	return result;
};

/**
 * 根据值获得名称
 */
ComboData.prototype.getNameByValue = function(value) {
	if (this.combItems.length == 0)
		return [];
	for ( var i = 0; i < this.combItems.length; i++) {
		if (this.combItems[i].value == value)
			return this.combItems[i].i18nName;
	}
	return null;
};

/**
 * 根据名称获得值
 */
ComboData.prototype.getValueByName = function(name) {
	if (this.combItems.length == 0)
		return [];
	for ( var i = 0; i < this.combItems.length; i++) {
		if (this.combItems[i].i18nName == name)
			return this.combItems[i].value;
	}
	return null;
};

/**
 * 向ComboData中增加一个item,此方法应该只被web frame调用。除非手工构造一ComboData
 * 
 * @param item ComboItem对象
 */
ComboData.prototype.addItem = function(item) {
	this.combItems.push(item);
};

/**
 * 聚合数据项
 * @class 聚合数据项
 * @constructor ComboItem构造函数
 * @param name 名称
 * @param value 值
 * @author dengjt
 */
function ComboItem(name, value, image) {
	this.i18nName = name;
	this.value = value;
	this.image = image;
};

/**
 * 参照信息
 * @class 参照信息
 */
function RefNodeInfo(id, name, pageMeta, path, readDs, writeDs, readFields,
		writeFields, filterSql, userObj, multiSel, usePower, selLeafOnly,
		refreshRefPage, isDialog, allowExtendValue) {
	this.id = id;
	this.name = name;
	this.pageMeta = pageMeta;
	this.path = path;
	this.readDs = readDs;
	this.writeDs = writeDs;
	this.readFields = readFields;
	this.writeFields = writeFields;
	this.userObj = userObj;
	this.filterSql = filterSql;
	this.multiSel = multiSel;
	this.referenceList = new Array();
	this.usePower = usePower;
	this.selLeafOnly = selLeafOnly;
	this.refreshRefPage = getBoolean(refreshRefPage, false);
	this.isDialog = getBoolean(isDialog, false);
	// 是否允许存在参照之外的值
	this.allowExtendValue = getBoolean(allowExtendValue, false);
};

/**
 * 设置过滤SQL语句
 */
RefNodeInfo.prototype.setFilterSql = function(filterSql) {
	this.filterSql = filterSql;
	for ( var i = 0; i < this.referenceList.length; i++) {
		this.referenceList[i].setFilterSql(filterSql);
	}
};

/**
 * 绑定参照
 */
RefNodeInfo.prototype.bindReference = function(reference) {
	if (this.referenceList.indexOf(reference) == -1)
		this.referenceList.push(reference);
};

SelfRefNodeInfo.prototype = new RefNodeInfo;
function SelfRefNodeInfo(id, text, url, isDialog){
	this.id = id;
	this.url = url;
	this.name = text;
	this.isDialog = isDialog;	
}


/**
 * 参照关联关系构造函数
 * @class 参照关联关系类
 * @return
 */
function RefNodeRelations() {
	this.relationMap = new HashMap();
};

/**
 * 向页面Relations中添加一个Relation。此方法一般只被Web Frame调用
 * 
 * @param relation 数据集Relation
 */
RefNodeRelations.prototype.addRelation = function(relation) {
	this.relationMap.put(relation.id, relation);
};

/**
 * 根据relation id删除相应的relation。 <b>注意，必须保证所有的relation id不重复</b>
 * 
 * @param id
 */
RefNodeRelations.prototype.removeRelation = function(id) {
	if (this.relationMap.containsKey(id))
		this.relationMap.remove(id);
};

/**
 * 根据主字段ID获取relation
 */
RefNodeRelations.prototype.getRelationsByMasterFieldId = function(
		masterFieldId, dsId) {
	var relArr = new Array;
	var relations = this.relationMap.values();
	for ( var i = 0, n = relations.length; i < n; i++) {
		var relation = relations[i];
		var masterFields = relation.masterFieldInfos;
		for ( var j = 0, m = masterFields.length; j < m; j++) {
			var masterField = masterFields[j];
			if (masterField.dsId == dsId
					&& masterField.fieldId == masterFieldId)
				relArr.push(relation);
		}
	}
	return relArr;
};

/**
 * 根据关联字段ID获取relation
 */
RefNodeRelations.prototype.getRelationBySlaveRefNode = function(slaveRefNode,
		dsId) {
	var relations = this.relationMap.values();
	if (relations != null && relations.length > 0) {
		for ( var i = 0, n = relations.length; i < n; i++) {
			var relation = relations[i];
			// TODO
			if ((relation.targetDsId == null || relation.targetDsId == "null" || relation.targetDsId == dsId)
					&& relation.detailRefNode == slaveRefNode) {
				return relation;
			}
		}
	}
	return null;
};

/**
 * 将RefNodeRelations绑定到对应的Dataset的事件上
 * 
 * @return
 */
function bindRefNode2Dataset() {
	if (window.$refNodeRelations != null) {
		var widget = window.$refNodeRelations.widget;
		var relations = window.$refNodeRelations.relationMap.values();
		// Dataset的onAfterDataChange事件的相关字段
		var acceptFieldMap = new HashMap();
		for ( var i = 0, n = relations.length; i < n; i++) {
			var relation = relations[i];
			for ( var j = 0, m = relation.masterFieldInfos.length; j < m; j++) {
				var dsId = relation.masterFieldInfos[j].dsId;
				var arr = acceptFieldMap.get(dsId);
				if (arr == null) {
					arr = new Array;
					acceptFieldMap.put(dsId, arr);
				}
				arr.push(relation.masterFieldInfos[j].fieldId);
			}
		}
		// 为Dataset增加数据改变事件
		var keys = acceptFieldMap.keySet();
		for ( var i = 0, n = keys.length; i < n; i++) {
			var dsId = keys[i];
			var ds = widget.getDataset(dsId);
			ds.$temp_af = acceptFieldMap.get(dsId);

			var listener = new DatasetListener();
			listener.onAfterDataChange = function(dataChangeEvent) {
				var fields = this.$temp_af;
				var colIndex = dataChangeEvent.cellColIndex;
				var currentFieldName = this.metadata[colIndex].key;
				var find = false;
				for ( var i = 0, n = fields.length; i < n; i++) {
					if (fields[i] == currentFieldName){
						find = true;
						break;
					}
				}
				if(!find)
					return;
				doProcessFieldRelation(dataChangeEvent);
			};
			ds.addListener(listener);
		}
	}
};

/**
 * 执行字段关联操作
 * @return
 */
function doProcessFieldRelation(dataChangeEvent) {
	var newValue = dataChangeEvent.currentValue;
	var oldValue = dataChangeEvent.oldValue;
	if (window.$refNodeRelations != null && newValue != oldValue) { // 数据改变
		var colIndex = dataChangeEvent.cellColIndex;
		var widget = window.$refNodeRelations.widget;
		var fieldId = widget.getDataset(dataChangeEvent.datasetId).metadata[colIndex].key;
		var relations = window.$refNodeRelations.relationMap.values();
		for ( var i = 0, n = relations.length; i < n; i++) {
			var relation = relations[i];
			var masterFields = relation.masterFieldInfos;
			for ( var j = 0, m = masterFields.length; j < m; j++) {
				var masterField = masterFields[j];
				if (masterField.dsId == dataChangeEvent.datasetId
						&& masterField.fieldId == fieldId) {
					// 设置绑定字段值为空
					var refNodeId = relation.detailRefNode;
					var refNode = widget.getRefNode(refNodeId);
					for ( var k = 0, l = refNode.referenceList.length; k < l; k++) {
						refNode.referenceList[k].clearValue();
					}
				}
			}
		}
	}
};

/**
 * 参照关联关系
 * @class 参照关联关系
 * @param id
 * @param masterFieldInfos ：主字段信息对象数组，对象对应后台类：nc.lfw.core.refnode.MasterFieldInfo，包含内容：fieldId、filterSql、nullProcess
 * @param masterKeyField
 * @param detailRefNode
 * @param targetDsId
 * @param clearDetail
 * @return
 */
function RefNodeRelation(id, masterFieldInfos, detailRefNode, targetDsId,
		clearDetail) {
	this.id = id;
	this.masterFieldInfos = eval(masterFieldInfos);
	this.detailRefNode = detailRefNode;
	this.targetDsId = targetDsId;
	this.clearDetail = clearDetail;

};
