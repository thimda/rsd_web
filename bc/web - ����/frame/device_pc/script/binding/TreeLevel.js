/**
 * @fileoverview Tree的"级"构造方式配置类
 * @author gd
 * @version 1.0
 */

/**
 * 树层级类构造函数
 * @class Tree的层级级构造方式配置类
 * @constructor
 * @param{String} id				  此level类的id
 * @param{String} datasetId		      所用dataset的id
 * @param{String} type				  只能是"Recursive"或者"Simple"类型
 * @param{Array}  recursivePkFields   递归字段(level内的关系)
 * @param{Array}  recursivePPkFields  递归层展本层时需要把父的recursivePkFields赋到该属性指定的名称中(level内关系)
 * @param{Array}  labelField		  树节点显示的时候自动从该字段中取值并拼接在一起作为节点的标题显示	
 * @param{Array}  labelDelims		  此字段用于指定显示的各个字段间的分割符
 * @param{Array}  masterKeyFields	  用于设定与上层节点间的关系,当该层节点被展开时,
 * 									  系统自动从上层节点绑定的record对象中获取masterkeyFields指定的字段的值.
 * 									  该属性可以配置为多个字段,中间用逗号","分割(level间的关系)
 * @param{Array}  detailKeyParameters 从上层masterKeyFields中获取的字段保存在本层此属性指定的变量中(level间的关系)
 */
function TreeLevel(id, dataset, type, recursivePkField, recursivePPkField, labelFields
		, masterKeyField, detailKeyParameter, labelDelims, loadField) {	
	this.id = id;
	// 此level所挂的contextMenu菜单
	this.contextMenu = null;
	this.dataset = dataset;
	this.type = type;
	this.recursiveKeyField = recursivePkField;
	this.recursivePKeyField = recursivePPkField;
	this.labelFields = labelFields;
	this.masterKeyField = masterKeyField;
	this.labelDelims = labelDelims;
	this.loadField = loadField;
	/**
	 * 用于设定与上层节点间的关系，当该层节点被展开时，系统自动从上层节点绑定的record
	 * 对象中获取masterkeyFields指定的字段的值并作为当前层绑定的dataseet的parameters
	 * 存储起来，parameter的名字就是detailkeyParameters指定的名称，多个参数名用逗号分割。
	 */
	this.detailKeyParameter = detailKeyParameter;
	this.parentLevel = null;
	this.childrenLevel = [];
};

/**
 * 设置右键菜单
 */
TreeLevel.prototype.setContextMenu = function(menu) {
	this.contextMenu = menu;
};

/**
 * 增加此level的子level
 * @param TreeLevel 要增加的子level对象
 */
TreeLevel.prototype.addTreeLevel = function(treeLevel) {
	treeLevel.parentLevel = this;
	this.childrenLevel.push(treeLevel);
};


function GridTreeLevel(id, recursivePkField, recursivePPkField, labelFields, loadField, leafField) {	
	this.id = id;
	// 此level所挂的contextMenu菜单
	this.contextMenu = null;
	this.recursiveKeyField = recursivePkField;
	this.recursivePKeyField = recursivePPkField;
	this.labelFields = labelFields;
	this.loadField = loadField;
	this.leafField = leafField;
};
