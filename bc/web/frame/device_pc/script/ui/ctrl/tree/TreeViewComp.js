/**
 * @fileoverview Tree控件
 * 
 * @author gd, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 */
TreeViewComp.prototype = new BaseComponent;
TreeViewComp.prototype.componentType = "TREEVIEW";
TreeViewComp.prototype.nodeInstance = 0;
TreeViewComp.NODEHEIGHT = 26;
TreeViewComp.RELOADNODE_suffix = "LoadingNode";
//复选状态对应的图标   window.themePath + "/images/treeview/" + this.checkArray[0]; 
TreeViewComp.checkArray=new Array("iconUncheckAll.gif","iconCheckAll.gif","iconCheckGray.gif","iconUncheckDis.gif","iconCheckDis.gif","iconCheckDis.gif");


/**
 * 树节点各个显示字段的默认分割符
 */
TreeViewComp.DEFAULT_DELIMS = " ";

//复选策略
//只设置自己
TreeViewComp.CHECKBOXMODEL_SELF = 0;
//设置自己和子
TreeViewComp.CHECKBOXMODEL_SELF_SUB = 1;
//设置自己和子和父
TreeViewComp.CHECKBOXMODEL_SELF_SUB_PARENT = 2;

/**
 * 树控件构造函数
 * @class 树控件类，用来将数据组织成树状。<br>
 * <b>Tree特性：</b><br>
 *  1、树节点采用缓加载模式加载数据，一次装载一屏，支持大数据量的数据展现<br>
 *  2、支持节点拖拽，可以动态改变树的结构<br>
 *  3、用户可以覆盖暴露的多种方法实现自己的功能，参与整棵树的构建过程<br>
 *  4、支持简单树、递归树<br>
 * <b>组件皮肤设置说明：</b><br>
 * treeview.css文件用来控制此控件的外观<br>
 * <br>
 * <b>Tree控件使用方法介绍：</b><br>
 * <b>1、使用lfw框架的用法</b><br>
 *    (1)前台jsp页面中写明&lt;lfw:tree id="">&lt;/lfw:tree>，id为引用的后台配置文件中配置的此Tree的id<br>
 *    (2)在后台配置文件中配置Tree的各个属性信息<br>
 * <b>Tree属性说明：</b><br>
 *    id-----------Tree控件的id，jsp文件的控件id必须和这个id相同才能引用到此控件<br>
 *    width--------Tree控件的宽度<br>
 *    withRoot-----是否显示根节点<br>
 *    rootOpen-----初始化tree的时候根节点是否打开<br>
 *    dragEnable---是否可以拖拽<br>
 * <br>
 * 
 * <b>简单树</b><br>
 * &lt;TreeViewComp id="" width="" withRoot="" rootOpen="" dragEnable=""><br>
 * &lt;SimpleTreeLevel id="" <b>type="Simple"</b> dataset="" labelFields=""
 * contextMenu=""/><br>
 * &lt;Events> &lt;Event name=""> &lt;/Event> &lt;/Events><br>
 * &lt;/TreeViewComp><br>
 * <b>递归树</b><br>
 * &lt;TreeViewComp id="" width="" withRoot="" rootOpen="" dragEnable=""><br>
 * &lt;RecursiveTreeLevel id="" <b>type="Recursive"</b> dataset=""
 * <b>recursiveKeyFields=""</b> <b>recursiveKeyParameters=""</b>
 * labelFields="" contextMenu=""/><br>
 * &lt;Events> &lt;Event name=""> &lt;/Event> &lt;/Events><br>
 * &lt;/TreeViewComp><br>
 * <b>编码树(一种特殊的递归树)</b><br>
 * &lt;TreeViewComp id="" width="" withRoot="" rootOpen="" dragEnable=""><br>
 * &lt;CodeTreeLevel id="" <b>type="Recursive"</b> dataset=""
 * <b>recursiveKeyFields=""</b> <b>codeField=""</b> labelFields=""
 * contextMenu=""/><br>
 * &lt;Events> &lt;Event name=""> &lt;/Event> &lt;/Events><br>
 * &lt;/TreeViewComp><br>
 * <br>
 * <b>使用js，直接通过new的用法生成tree实例</b><br>
 * var tree = new TreeViewComp(parent, name, left, top, width, height, position,
 * rootOpen, withRoot, null);<br>
 * tree.setDragEnabled(isDragEnabled);<br>
 * <b>// 关于TreeLevel各字段的说明详见TreeLevel</b><br>
 * var level = new TreeLevel(id, datasetId, type, recursivePkFields,
 * recursivePPkFields, labelField, masterKeyFields, detailKeyParameters,
 * contextMenu);<br>
 * tree.setTreeLevel(level);<br>
 * 
 * @constructor TreeViewComp的构造函数
 * @author gd
 * @see TreeLevel
 * @param parent 此控件的父控件
 * @param name 此控件的名称
 * @param left 控件左部x坐标
 * @param top 控件顶部y坐标
 * @param width 控件宽度
 * @param height 控件高度
 * @param position 控件的定位属性
 * @param rootOpen 初始化tree的时候根节点是否打开
 * @param withRoot 是否显示根节点
 * @param rootNode 根节点
 */
function TreeViewComp(parent, name, left, top, width, height, position, withCheckBox,
		rootOpen, withRoot, rootNode, className, attr) {
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.withRoot = getBoolean(withRoot, true);
	this.rootOpen = getBoolean(rootOpen, true);
	// 设置根节点不显示,则rootOpen设为true
	if (this.withRoot == false)
		this.rootOpen = true;
	this.rootNode = rootNode;
	// 表示此树是否可以拖拽
	this.isDragEnabled = false;
	// 表示此树是否为激活状态
	this.isTreeActive = true;
	// 临时无父节点的节点map,以父ID为key,以节点数组为value
	this.tmpNodeMap = new HashMap();
	// 建立ID和node的映射关系
	this.idNodeMap = new HashMap();
	// 记录导致当前树宽度出滚动条的节点
	this.resultScrollNodes = new Array();
	// 是否有checkbox
	this.withCheckBox = getBoolean(withCheckBox, false);
	//设置默认复选策略
	this.checkBoxModel = TreeViewComp.CHECKBOXMODEL_SELF; 
//	this.flowmode = false;
//	if(attr != null){
//		this.flowmode = attr.flowmode;
//	}
	this.canEdit = false;
	if(attr != null){
		this.canEdit = getBoolean(attr.canEdit, false);
	} 
	// 根节点默认显示值
	this.rootCaption = "ROOT";
	this.className = getString(className, "tree_div");
	this.create();
};

/**
 * 创建TreeViewComp的真正函数
 * 
 * @private
 */
TreeViewComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("div");
	this.Div_gen.className = this.className;
	// guoweic: modify start 2009-11-2
	//this.Div_gen.style.top = this.top;
	this.Div_gen.style.top = this.top + "px";
	//this.Div_gen.style.left = this.left;
	this.Div_gen.style.left = this.left + "px";
	if (this.width.toString().indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
//	if(!this.flowmode){
//		if (this.height.toString().indexOf("%") == -1)
//			this.Div_gen.style.height = this.height + "px";
//		else
//			this.Div_gen.style.height = this.height;
//	}
	// guoweic: modify end

	this.Div_gen.style.height = "100%";
	this.Div_gen.style.position = this.position;
	this.Div_gen.id = this.id;
	this.Div_gen.style.overflow = "auto";

	this.bgDiv = $ce("div");
	this.bgDiv.className = "treebg_div";
	this.Div_gen.appendChild(this.bgDiv);

	// 创建树控件显示对象
	this.treeDiv_gen = $ce("DIV");
	this.treeDiv_gen.className = "treeview_div";
	this.treeDiv_gen.style.position = this.position;
	this.bgDiv.appendChild(this.treeDiv_gen);
	if (this.rootNode)
		this.addRoot(this.rootNode);
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
	// 捕获tree滚动事件,采用缓加载技术加载树节点
	this.Div_gen.onscroll = function(e) {
		e = EventUtil.getEvent();
		e.triggerObj = oThis;
		handleTreeScrollEvent(e);
		// 删除事件对象（用于清除依赖关系）
//		clearEvent(e);
		e.triggerObj = null;
		clearEventSimply(e);
	};
};

/**
 * 添加根节点
 * 
 * @param rootNode 根节点
 */
TreeViewComp.prototype.addRoot = function(rootNode) {
	// 注意:this.rootNode始终代表"最后"添加的rootNode
	this.rootNode = rootNode;
	this.rootNode.refTree = this;
	this.rootNode.root = true;
};

/**
 * 画显示区域的树节点
 * 
 * @private
 */
TreeViewComp.prototype.paintZone = function() {
	var oThis = this;
	// 如果根节点没有画首先画根节点
	if (this.rootNode.painted == TreeNode.NOPAINT) {
		// 根结点首先画出
		this.rootNode.create1();
		this.rootNode.create2(0);
		
		if (this.withRoot == false)
			//this.rootNode.Div_gen.removeChild(this.rootNode.divRow);
			this.rootNode.wholeTableCell.removeChild(this.rootNode.divRow);
		else {
			var row = this.rootNode.divRowTable.firstChild.firstChild;
			row.deleteCell(this.rootNode.divRowTable.firstChild.firstChild.cells[0]);
//			this.rootNode.divContent.style.display = 'block';
			// 设置根节点不显示,则rootOpen设为true
			if (this.withRoot == false)
				this.rootNode.open = true;
		}
		this.treeDiv_gen.appendChild(this.rootNode.Div_gen);
	}
	
	if (this.rootNode.open && this.rootNode.childrenTreeNodes.length > 0) {
		// 记录画得节点数
		this.nodeCount = 0;
		this.paintNode(this.rootNode.childrenTreeNodes, this.Div_gen.scrollTop,
				this.Div_gen.offsetHeight,1);
	}
	// 计算所有打开的节点的个数
	var openedNodeCount = this.calculateOpenedNodesCount();
	// 记录树的高度(避免在树显示出来后又hidden掉,得到的offsetHeight为0的问题)
	this.constant_treeHeight = TreeViewComp.NODEHEIGHT * openedNodeCount;
//	this.treeDiv_gen.style.height = this.constant_treeHeight + "px";

	// guoweic: add 修正多出横向滚动条问题 start 2009-11-4
	if (IS_IE6 || IS_IE7) {
		if (!this.isAdjusted) {  // 只第一次页面加载时进行调整
			//if (this.Div_gen.offsetWidth != this.Div_gen.clientWidth) {  // 出现竖直滚动条
			if (isDivVScroll(this.Div_gen)) {  // 出现竖直滚动条	
				this.bgDiv.style.width = this.Div_gen.scrollWidth - BaseComponent.SCROLLWIDTH + "px";
				this.isAdjusted = true;
			}
		}
	}
	// guoweic: add end
};

/**
 * 画显示区界面上的树节点,采用缓画技术展现树节点
 * 
 * @private
 */
TreeViewComp.prototype.paintNode = function(nodeArr, divGenScrollTop,
		divGenOffsetHeight,levelNum) {
	
	for ( var i = 0; i < nodeArr.length; i++) {
		
		// 如果此节点不是最后一级level,且是文件夹节点,并且还没有画过,则添加"正在下载"子节点
		// 根据该节点下一级level的数量添加几个"正在下载"子节点
		if (nodeArr[i].reload == true && !nodeArr[i].isLeaf
				&& nodeArr[i].painted == TreeNode.NOPAINT) {
			// 当前节点所处level的子levels
			var childrenLevel = nodeArr[i].level.childrenLevel;
			for ( var j = 0; j < childrenLevel.length; j++) {
				// 创建挂于此节点下的"加载节点"
				var loadNodeId = nodeArr[i].id + "_" + childrenLevel[j].id
						+ "_" + TreeViewComp.RELOADNODE_suffix;
				// 将上一级level的masterKeyFields放在加载节点的value属性中
				var value = nodeArr[i].level.masterKeyField;
				var reloadNode = new TreeNode(loadNodeId, trans("ml_loading"),
						value, true, false, false, null, this.withCheckBox,this.checkBoxModel, this.canEdit, false);
				// 此节点绑定父级level,ds和nodeData
				reloadNode.level = nodeArr[i].level;
				reloadNode.dataset = nodeArr[i].level.dataset;
				reloadNode.nodeData = nodeArr[i].nodeData;
				// 记录该加载节点加载哪个level的数据
				reloadNode.loadLevel = childrenLevel[j];
				nodeArr[i].add(reloadNode);
			}
		}
		
		var currLevel = nodeArr[i].level;
		if(currLevel.loadField != null && currLevel.loadField != ""){
			if(nodeArr[i].loadLevel == null && nodeArr[i].painted == TreeNode.NOPAINT){
				var row = nodeArr[i].nodeData;
				var needLoad = row.getCellValue(currLevel.dataset.nameToIndex(currLevel.loadField));
				if(needLoad != null && needLoad == "1"){
					// 创建挂于此节点下的"加载节点"
					var loadNodeId = nodeArr[i].id + "_" + currLevel.id
							+ "_" + TreeViewComp.RELOADNODE_suffix;
					// 将上一级level的masterKeyFields放在加载节点的value属性中
					var value = currLevel.masterKeyField;
					var reloadNode = new TreeNode(loadNodeId, trans("ml_loading"),
							value, true, false, false, null, false,this.checkBoxModel,this.canEdit, false);
					// 此节点绑定父级level,ds和nodeData
					reloadNode.level = currLevel;
					reloadNode.dataset = currLevel.dataset;
					reloadNode.nodeData = nodeArr[i].nodeData;
					// 记录该加载节点加载哪个level的数据
					reloadNode.loadLevel = currLevel;
					nodeArr[i].add(reloadNode);
					nodeArr[i].reload = true;
				}
			}
		}
		if (nodeArr[i].painted == TreeNode.NOPAINT)
			nodeArr[i].create1();

		// 记录画了的节点数,便于定位当前节点的位置
		if (nodeArr[i].painted != TreeNode.NOPAINT)
			this.nodeCount++;

		if(true){
			if (nodeArr[i].painted == TreeNode.PAINTHALF)
				nodeArr[i].create2(levelNum);
		}
		else{
			var nodeOffsetTop = this.nodeCount * TreeViewComp.NODEHEIGHT;
	
			if ((nodeOffsetTop >= divGenScrollTop - TreeViewComp.NODEHEIGHT * 2)
					&& (nodeOffsetTop < divGenScrollTop + divGenOffsetHeight)) {
				if (nodeArr[i].painted == TreeNode.PAINTHALF)
					nodeArr[i].create2(levelNum);
			}
	
			if (nodeOffsetTop > (divGenScrollTop + divGenOffsetHeight))
				break;
		}

		// 文件夹节点若打开则去递归画它孩子
		if (nodeArr[i].open && nodeArr[i].childrenTreeNodes.length > 0)
			this.paintNode(nodeArr[i].childrenTreeNodes, divGenScrollTop,
					divGenOffsetHeight,levelNum + 1);
		
		// guoweic: add 处理firefox和IE8下divContent宽度不能自动扩展问题 start 2009-11-19
		if (!IS_IE || IS_IE8) {
			if (nodeArr[i].divContent.offsetWidth < (nodeArr[i].DIVT.offsetWidth + nodeArr[i].divChildren.offsetWidth)) {
				nodeArr[i].divContent.style.width = nodeArr[i].DIVT.offsetWidth + nodeArr[i].divChildren.offsetWidth + "px";
			}
		}
		// guoweic: add end
	}
};

/**
 * 计算打开的树节点的数目,用于改变树的高度
 * 
 * @private
 */
TreeViewComp.prototype.calculateOpenedNodesCount = function() {
	this.count = 0;
	if (this.rootNode.open && this.rootNode.childrenTreeNodes.length > 0) {
		this.count++;
		return this.calculateCount(this.rootNode.childrenTreeNodes, this.count);
	} else
		return ++this.count;
};

/**
 * @private
 */
TreeViewComp.prototype.calculateCount = function(nodes, nodeCount) {
	this.count = nodeCount;
	for ( var i = 0; i < nodes.length; i++) {
		this.count++;
		if (!nodes[i].isLeaf && nodes[i].open
				&& nodes[i].childrenTreeNodes.length > 0)
			this.calculateCount(nodes[i].childrenTreeNodes, this.count);
	}
	return this.count;
};

/**
 * 计算当前节点的距root节点的真实高度
 * @private
 */
TreeViewComp.prototype.calculateNodeRealHeight = function(node) {
	var nodeRVHeight = 0;
	while (node.parentTreeNode != null) {
		// 父节点打开
		if (node.parentTreeNode.open) {
			// 兄弟节点个数
			nodeRVHeight += (node.getPreVisibleSiblingCount() + 1)
					* TreeViewComp.NODEHEIGHT;
		} else
			nodeRVHeight += TreeViewComp.NODEHEIGHT;
		node = node.parentTreeNode;
	}
	return nodeRVHeight;
};

/**
 * 把node添加到parentId指定的父节点下
 * 
 * @param node 要添加的节点对象
 * @param parantId 要添加到的节点的id
 */
TreeViewComp.prototype.addNode = function(node, parentId) {
	var added = false;
	// 往树上添加节点就是往根节点上添加节点
	if (parentId == this.id) {
		this.addRoot(node);
		added = true;
	}
	// 没有parentId即往根节点上增加
	else if (parentId == null || parentId == "") {
		this.rootNode.add(node);
		added = true;
	} else {
		// 查找parentId指定的节点
		var rsNode = this.idNodeMap.get(parentId);
		// 以下两种情况均要把子放到父的临时池中
		// 1.没有查到说明parent节点还没有初始化,把node节点先放到临时池中,以parentId为key
		// 2.查到了,但是父的父还没有初始化
		if (rsNode == null || rsNode.refTree == null) {
			var nodeArr = this.tmpNodeMap.get(parentId);
			if (nodeArr == null) {
				nodeArr = new Array();
				this.tmpNodeMap.put(parentId, nodeArr);
			}
			nodeArr.push(node);
		} else {
			rsNode.add(node);
			added = true;
		}
	}
	// 一旦node节点挂到parentId下,得到池中以node为父节点的所有未挂的子节点列表
	if (added) {
		var cnodeList = this.tmpNodeMap.remove(node.id);
		if (cnodeList != null)
			this.addRelationNode(node, cnodeList);
	}
};

/**
 * 处理孤儿节点
 * 
 * @param pId 如果pId不为空,则最终将孤儿节点均挂于该节点下,否则挂于根结点下
 * @private
 */
TreeViewComp.prototype.manageLeastNode = function(pNode) {
	var keyArr = this.tmpNodeMap.keySet();
	if (keyArr != null) {
		for ( var i = 0; i < keyArr.length; i++) {
			var nodeArr = this.tmpNodeMap.remove(keyArr[i]);
			if (nodeArr != null) {
				for ( var j = 0; j < nodeArr.length; j++) {
					var node = nodeArr[j];
					if (pNode != null)
						pNode.add(node);
					else {
						// 首先判断node的父节点是否已经画出,如果没有最终画出则将该节点挂于根结点下
						var parentNode = this.idNodeMap.get(node.pNodeId);
						if (parentNode != null)
							this.addNode(node, node.pNodeId);
						else
							this.rootNode.add(node);
					}
					var cnodeList = this.tmpNodeMap.remove(node.id);
					if (cnodeList != null)
						this.addRelationNode(node, cnodeList);
				}
			}
		}
	}
};

/**
 * 将给定的一组Node节点添加到父节点下
 * 
 * @param pNode 父节点
 * @param cNodeList 要添加的子节点数组
 * @private
 */
TreeViewComp.prototype.addRelationNode = function(pNode, cNodeList) {
	if (cNodeList != null) {
		for ( var i = 0; i < cNodeList.length; i++) {
			// 通过.refTree属性判断此节点是否被父节点添加过,没添加过才添加
			if (cNodeList[i].refTree == null)
				this.addNode(cNodeList[i], pNode.id);
		}
	}
};

/**
 * 根据节点id查找节点
 * 
 * @param nodeId 要查找的节点id
 */
TreeViewComp.prototype.getNodeById = function(nodeId) {
	return this.idNodeMap.get(nodeId);
};

/**
 * 根据数据行的id查找节点
 * 
 * @param rowId ds中row的id
 */
TreeViewComp.prototype.getNodeByRowId = function(rowId) {
	var nodes = this.idNodeMap.values();
	for ( var i = 0; i < nodes.length; i++) {
		if (nodes[i].rowId == rowId)
			return nodes[i];
	}
};

/**
 * 得到下一个即将被选中的节点。在点击下一个节点时，如果用户没有阻止下一个节点选中，则可以通过该方法得到下一个将要选中的节点
 */
TreeViewComp.prototype.getNextSelectedNode = function() {
	return this.nextSelNode;
};

/**
 * 得到当前选中的节点
 */
TreeViewComp.prototype.getSelectedNode = function() {
	return this.selNode;
};

/**
 * 清空选中节点
 */
TreeViewComp.prototype.clearSelectedNode = function() {
	if (this.selNode != null && this.selNode.painted == TreeNode.PAINTALL)
		this.selNode.deSelNode();
};

/**
 * 删除选中的节点(会删除dataset中的数据)
 */
TreeViewComp.prototype.deleteSelectedNode = function() {
	var selNode = this.getSelectedNode();
	var index = selNode.level.dataset.getRowIndex(selNode.nodeData);
	selNode.level.dataset.deleteRow(index);
};

/**
 * 让给定的树节点显示
 * 
 * @param node 要显示出来的树节点
 * @private
 */
TreeViewComp.prototype.letNodeVisible = function(node) {
	// 得到此节点的所有祖先节点
	var parents = new Array();
	var parentNode = node.parentTreeNode;
	while (parentNode != null) {
		parents.push(parentNode);
		parentNode = parentNode.parentTreeNode;
	}
	var sTop = 0;
	while (parents.length > 0) {
		var pNode = parents.pop();
		// var nodeRealHeight = this.calculateNodeRealHeight(pNode);
		// var scrollTop = this.Div_gen.scrollTop;
		// // 如果父节点不在显示区域且父节点和该节点的距离小于可见区域高度才滚动到显示区域
		// if(nodeRealHeight > scrollTop + this.Div_gen.offsetHeight &&
		// getPreVisibleSiblingCount)
		// this.Div_gen.scrollTop = nodeRealHeight - this.Div_gen.offsetHeight +
		// 2*TreeViewComp.NODEHEIGHT;
		// else if(nodeRealHeight < scrollTop)
		// this.Div_gen.scrollTop = nodeRealHeight - 2*TreeViewComp.NODEHEIGHT;
		if (pNode.open == false) {
			if (pNode.painted == TreeNode.PAINTALL) {
				pNode.toggle();
			} else {
				pNode.open = true;
			}
		}
	}

	var nodeRealHeight = this.calculateNodeRealHeight(node);
	var scrollTop = this.Div_gen.scrollTop;
	// guoweic: modify start 2009-11-2
	if (nodeRealHeight > scrollTop + this.Div_gen.offsetHeight)
		//this.Div_gen.scrollTop = nodeRealHeight - this.Div_gen.offsetHeight + 4
		//		* TreeViewComp.NODEHEIGHT;
		this.Div_gen.scrollTop = (nodeRealHeight - this.Div_gen.offsetHeight + 4
				* TreeViewComp.NODEHEIGHT) + "px";
	else if (nodeRealHeight < scrollTop)
		//this.Div_gen.scrollTop = nodeRealHeight - 4 * TreeViewComp.NODEHEIGHT;
		this.Div_gen.scrollTop = (nodeRealHeight - 4 * TreeViewComp.NODEHEIGHT) + "px";
	// guoweic: modify end
};

/**
 * 设定树节点的第几级打开
 * 
 * @public 在树节点构件完成后可以调用此方法，如果构建的树没有root，调用此方法无效。root下第一级节点level参数为0，依次类推
 * @private
 */
TreeViewComp.prototype.openNodesByLevel = function(level) {
	if (this.rootNode == null)
		return;

	level = parseInt(level);

	// 获的所有的1级节点
	var levelOneNodes = null;
	if (this.withRoot) {
		// 根节点处于打开先关闭它
		if (this.rootNode.open == true)
			this.rootNode.toggle();
		levelOneNodes = [ this.rootNode ];
		this.openNodes(levelOneNodes, level);
	} else {
		levelOneNodes = this.rootNode.childrenTreeNodes;
		this.openNodes(levelOneNodes, level - 1);
	}
};

function openNodesByLevel(level, treeId){
	if (TreeViewComp.currTreeComp != null)
		TreeViewComp.currTreeComp.openNodesByLevel(level);
};

/**
 * @private 递归打开指定级数的所有节点
 */
TreeViewComp.prototype.openNodes = function(childNodes, level) {
	if (level < 0)
		return;
	level--;
	for ( var i = 0; i < childNodes.length; i++) {
		if (!childNodes[i].isLeafNode()) {
			childNodes[i].toggle();
			this.openNodes(childNodes[i].childrenTreeNodes, level);
		}
	}
};

/**
 * 设置此树节点是否可以拖拽
 * 
 * @param isDragEnabled true表示可以拖拽，否则表示不能拖拽
 */
TreeViewComp.prototype.setDragEnabled = function(isDragEnabled) {
	this.isDragEnabled = getBoolean(isDragEnabled, false);
};

/**
 * 设置此树节点复选模式
 * 
 * @param checkBoxModel 复选模式
 */
TreeViewComp.prototype.setCheckBoxModel = function(checkBoxModel) {
	this.checkBoxModel = parseInt(checkBoxModel);
};

/**
 * 为ds的一致性添加的方法，ds会掉用所有控件的此方法，但目前树本身并没有"editable"
 * 
 * @param isEditable
 * @private
 */
TreeViewComp.prototype.setEditable = function(isEditable) {
};

/**
 * 设置此树是否激活
 * 
 * @param isActive true表示处于激活状态,否则表示禁用状态
 */
TreeViewComp.prototype.setActive = function(isActive) {
	this.isTreeActive = getBoolean(isActive, true);
};

/**
 * 得到树当前是否为激活状态
 */
TreeViewComp.prototype.isActive = function() {
	return this.isTreeActive;
};

/**
 * 得到根节点
 * 
 * @return 树的根节点
 */
TreeViewComp.prototype.getRoot = function() {
	return this.rootNode;
};

/**
 * 树的选中节点改变前调用此方法
 * 
 * @param newNode 马上要选中的节点
 * @param oldNode 上一个选中的节点
 * @private
 */
TreeViewComp.prototype.beforeSelNodeChange = function(newNode, oldNode) {
	this.onBeforeSelNodeChange(newNode, oldNode);
};

/**
 * 鼠标点击的事件处理
 * 
 * @private
 */
TreeViewComp.prototype.click = function(node) {
	this.onclick(node);
	this.onAfterSelNodeChange(node);
};

/**
 * 鼠标双击的事件处理
 * 
 * @private
 */
TreeViewComp.prototype.dblclick = function(node) {
	return this.ondbclick(node);
};

/**
 * 根据dataset得到此dataset所属的level
 * 
 * @param ds 当前的dataset
 */
TreeViewComp.prototype.getLevelByDataset = function(ds) {
	var levels = this.getAllLevels();
	for ( var i = 0; i < levels.length; i++) {
		var levelArr = levels[i];
		for ( var j = 0; j < levelArr.length; j++) {
			if (levelArr[j].dataset == ds)
				return levelArr[j];
		}
	}
	return null;
};

/**
 * 得到所有的levels 注意:该结果是数组,数组每个元素是第几级的所有level的数组
 * 
 * @return 该树的所有levels
 */
TreeViewComp.prototype.getAllLevels = function() {
	// 得到顶层下的所有levels
	var levels = new Array();
	var topLevel = this.topTreeLevel;
	// 将最顶级level放入levels数组中
	levels.push( [ topLevel ]);
	levels = this.getLevelRecursive(topLevel.childrenLevel, levels);
	return levels;
};

/**
 * 得到递归树Level
 */
TreeViewComp.prototype.getLevelRecursive = function(nextLevels, levels) {
	if (nextLevels != null) {
		var tempCurLevels = [];
		var tempNextLevels = [];
		for ( var i = 0, count = nextLevels.length; i < count; i++) {
			tempCurLevels.push(nextLevels[i]);
			if (nextLevels[i].childrenLevel != null
					&& nextLevels[i].childrenLevel.length > 0)
				tempNextLevels = nextLevels[i].childrenLevel;
		}
		if (tempCurLevels.length > 0) {
			levels.push(tempCurLevels);
			if (tempNextLevels.length > 0)
				this.getLevelRecursive(tempNextLevels, levels);
		}
	}
	return levels;
};

/**
 * 判断给定level是否是最后一级
 */
TreeViewComp.prototype.isLastLevel = function(level) {
	var levels = this.getAllLevels();
	var lastLevels = levels[levels.length - 1];
	for ( var i = 0; i < lastLevels.length; i++) {
		if (lastLevels[i] == level)
			return true;
		else
			return false;
	}
};

/**
 * 设置dataset数据集
 * 
 * @param dataset 数据集对象
 */
TreeViewComp.prototype.setDataset = function(dataset) {
	if (this.topTreeLevel == null)
		throw new Error("must call setTreeLevel() first!");
	// 如果不是最顶层dataset,设置isChild属性为true
	if (this.topTreeLevel.dataset != dataset)
		dataset.isChild = true;

	dataset.bindComponent(this);
	var level = this.getLevelByDataset(dataset);
	// 建立rootNode
	if (this.rootNode == null) {
		var rootNode = new TreeNode("$root", this.rootCaption, "root",
				this.rootOpen, false, false, null, this.withCheckBox,this.checkBoxModel,this.canEdit, false);
		this.onRootNodeCreated(rootNode);
		this.addRoot(rootNode);
		this.idNodeMap.put(rootNode.id, rootNode);
	}
	this.createNodes(dataset.getRows(), dataset, level);
	this.manageLeastNode();
	// 树节点构建完成后向界面上画节点
	this.paintZone();
	// 根据dataset编辑属性设置tree的编辑属性
	if (dataset.isEditable() == false)
		this.setEditable(false);
};

/**
 * 根据传入的rows创建节点
 * 
 * @param rows 要创建的所有节点数据
 * @param dataset 所属的ds
 * @param currLevel 所属的level
 * @param parentNodeId 不为null则当前创建的节点均挂于此节点下
 * @private
 */
TreeViewComp.prototype.createNodes = function(rows, dataset, currLevel) {
	var nodeCount = rows == null?0:rows.length;
	if(nodeCount <= 0)
		return;
	// 此层是递归类型
	//if(currLevel != null && currLevel.type == "Recursive")
	//{
	var parentKeyFieldIndex = dataset.nameToIndex(currLevel.recursivePKeyField);
	var keyFieldIndex = dataset.nameToIndex(currLevel.masterKeyField);
	// 主键字段必须指定
	if (keyFieldIndex == -1) {
		alert("TreeLevel:" + currLevel.id + " must assign masterKeyField!");
		return;
	}
	for(var i = 0; i < nodeCount; i++) {	
		var nodeData = rows[i], nodeId = nodeData.getCellValue(keyFieldIndex);
		// 说明ds中增加的是一行空行,此时以rowId为节点id
		if (nodeId == null || nodeId == "")
			nodeId = nodeData.rowId;
		
		var pNodeId = null;
		if (parentKeyFieldIndex != -1) {
			pNodeId = nodeData.getCellValue(parentKeyFieldIndex);
		}
		var caption = this.generateCaptionByLabelFields(currLevel, nodeData), value = caption;
		// 树节点加上level标示,只要同一level内的节点不重复即可
		var realNodeId = nodeId + "_" + currLevel.id;
		var realpNodeId = null;
		
		// 父ID为null,说明该节点并不挂于currLevel的任何节点下,此时该节点肯定挂于上级level中,如果无法在上级level中找到该节点则挂于选中节点下
		if ((pNodeId == null || pNodeId == "")) {	
			if (currLevel.parentLevel != null) {	
				var fk = currLevel.detailKeyParameter;
				// 当前层的外键是不是ds中的真实字段
				var detailKeyParameterIndex = dataset.nameToIndex(fk);
				// 当前层节点的外键字段,该字段必须是ds中的真实存在的字段
				if (fk != null && detailKeyParameterIndex != -1) {	
					var fkValue = nodeData.getCellValue(detailKeyParameterIndex);
					var parentNode = this.idNodeMap.get(fkValue + "_" + currLevel.parentLevel.id);
					if (parentNode != null)
						realpNodeId = parentNode.id;
					else {
						if(this.selNode != null)
							realpNodeId = this.selNode.id;
					}	
				}
				else {
					if (this.selNode != null)
						realpNodeId = this.selNode.id;
				}
			}
		}
		else 	
			realpNodeId = pNodeId + "_" + currLevel.id;
		
		// 给用户参与节点构建的机会(可以个性化树节点的创建)
		var node = this.nodeCreated(realNodeId, caption, value, realpNodeId, nodeData, dataset, false);
		if (node == null)
			continue;
		//node.level = currLevel;
		// 最后一级level中的树节点不用下挂"正在加载节点",其它各级均要下挂"正在加载节点"
		if (this.isLastLevel(currLevel))
			node.reload = false;
		else 
			node.reload = true;
		if (node.isLeaf)
			node.open = false;
		// 重复id的节点不挂在树上
		if (this.idNodeMap.containsKey(realNodeId)) {
			//showWarningDialog("nodeId:" + nodeId + ",caption:" + this.idNodeMap.get(realNodeId).caption + " duplicated!");
		}
		else {
			this.idNodeMap.put(realNodeId, node);
			this.addNode(node, realpNodeId);
		}	
	}
};

/**
 * 根据labelFields字段数组生成节点caption
 * 
 * @param{TreeeLevel} level
 * @param nodeData ds中的一条数据
 * @private
 */
TreeViewComp.prototype.generateCaptionByLabelFields = function(level, nodeData) {
	// 构成caption的各个显示字段
	var labelFields = level.labelFields;
	if (labelFields == null || labelFields == "")
		throw new Error(
				"the argument level.labelField is null in method generateCaptionByLabelFields!");

	var labelDelims = level.labelDelims;
	var dataset = level.dataset;
	var tempLabel = "", caption = "";
	for ( var i = 0; i < labelFields.length; i++) {
		tempLabel = nodeData.getCellValue(dataset.nameToIndex(labelFields[i]));
		if (tempLabel != null) {
			if (labelDelims != null && labelDelims[i] != null)
				caption += tempLabel + labelDelims[i];
			else
				caption += tempLabel + TreeViewComp.DEFAULT_DELIMS;
		}
	}
	return caption;
};

/**
 * 设置顶层TreeLevel
 */
TreeViewComp.prototype.setTreeLevel = function(topLevel) {
	this.topTreeLevel = topLevel;
	this.setDatasetByLevel(topLevel);
};

/**
 * 根据Level设置Dataset
 * @private
 */
TreeViewComp.prototype.setDatasetByLevel = function(level) {
	var ds = level.dataset;
	this.setDataset(ds);
	var cls = level.childrenLevel;
	if(cls != null && cls.length != 0){
		for(var i = 0; i < cls.length; i ++){
			this.setDatasetByLevel(cls[i]);
		}
	}
};

/**
 * tree面板滚动处理函数。设置30毫秒延迟
 * 
 * @private
 */
function handleTreeScrollEvent(e) {
	handleTreeScrollEvent.triggerObj = e.triggerObj;
	if (handleTreeScrollEvent.timer != null)
		clearTimeout(handleTreeScrollEvent.timer);
	handleTreeScrollEvent.timer = setTimeout("doTreeScroll()", 30);
};

/**
 * 滚动后画当前显示区域
 * 
 * @private
 */
function doTreeScroll() {
	handleTreeScrollEvent.triggerObj.paintZone();
};

/**
 * Model发生变化时的回调函数
 * 
 * @private
 */
TreeViewComp.prototype.onModelChanged = function(event, dataset) {
	// 行选中事件
	if (RowSelectEvent.prototype.isPrototypeOf(event)) {
		// 当前选中节点的dsRow
		var currNodeData = event.currentRow;
		// 没有数据，认为是跟节点选中
		if (currNodeData == null) {
			// 没有数据不选中根结点,点击根结点时主动把根结点设置为选中(gd 2009-07-08)
			// this.rootNode.selNode();
			// this.click(this.rootNode);
			return;
		}
		// 根据节点的rowId来查找node
		var currNode = this.getNodeByRowId(currNodeData.rowId);
		
		if(currNode == null)
			return;
		// 选中节点改变之前都要调用用户的处理方法,返回false则阻止选中节点改变
		if (currNode != this.selNode
				&& this.beforeSelNodeChange(currNode, this.selNode) == false)
			return;
		// 选中节点
		currNode.selNode();
		
		// 设置聚焦行
		dataset.setFocusRowIndex(dataset.getRowIndexById(currNodeData.rowId));
		
		// 选中节点的checkbox
		currNode.setChecked(true);
		// 正式处理节点点击的动作
		this.click(currNode);
	}
	// 行反选事件
	else if (RowUnSelectEvent.prototype.isPrototypeOf(event)) {
		var rowIndex = event.currentRowIndex;
		var unSeleRow = dataset.getRow(rowIndex);
		var currNode = this.getNodeByRowId(unSeleRow.rowId);
		if (currNode != null) {
			// 不选中节点的checkbox
			currNode.setChecked(false);
			currNode.deSelNode();
		}
		// this.click(currNode);
	}
	// 行插入事件
	else if (RowInsertEvent.prototype.isPrototypeOf(event)) {
		var nodeDatas = event.insertedRows;
		var level = this.getLevelByDataset(dataset);
		this.createNodes(nodeDatas, dataset, level);
		this.paintZone();
	}
	// 行删除事件
	else if (RowDeleteEvent.prototype.isPrototypeOf(event)) {
		// 要删除的节点数组
		var deleNodeDatas = event.deletedRows;
		if (deleNodeDatas != null && deleNodeDatas.length > 0) {
			var delNodeData = null;
			// 要删除的节点id
			var deleNodeId = null;
			// 要删除的节点
			var deleNode = null;

			for ( var i = 0; i < deleNodeDatas.length; i++) {
				delNodeData = deleNodeDatas[i];
				deleNode = this.getNodeByRowId(delNodeData.rowId);

				if (deleNode != null) {
					deleNodeId = deleNode.id;
					if (deleNode.deleted != null && deleNode.deleted == true) {
						// 真正的删除此对象
						deleNode.parentTreeNode.remove(deleNodeId);
					} else {
						if (!event.deleteAll) {
							// 前序遍历该节点的所有孩子(包括它自己)
							var children = deleNode.preorderTraversal();
							// 先删除此节点的所有孩子
							if (children != null && children.length > 0) {
								var index = -1;
								// 如果此节点有孩子,则从idNodeMap中将所有孩子节点删除
								for ( var i = 0; i < children.length; i++) {
									// 最后一个节点直接删除(此节点为指定的要删除的节点)
									if (i == children.length - 1)
										deleNode.parentTreeNode
												.remove(deleNodeId);
									else {
										index = children[i].level.dataset
												.getRowIndex(
														children[i].nodeData);
										if (index == -1)
											return;
										children[i].deleted = true;
										children[i].level.dataset.removeRow(index);
									}
								}
							}
						} else {
							var tempLevel = this.topTreeLevel;
							this.removeUINodesByLevel(tempLevel);
						}
					}
				}
			}
		}
	}
	// 列数据改变时调用此方法
	else if (DataChangeEvent.prototype.isPrototypeOf(event)) {
		var row = event.currentRow;
		// 改变值的列
		var cellColIndex = event.cellColIndex;
		// 旧值
		var oldValue = event.oldValue;
		// 新值
		var newValue = event.currentValue;
		// 要改变属性的节点
		var node = this.getNodeByRowId(row.rowId);
		if(node != null)
		{
			var level = node.level;
			var ds = node.getDataset();
			if(level.type == "Recursive")
			{
				var pIdIndex = ds.nameToIndex(level.recursivePKeyField);
				var idIndex = ds.nameToIndex(level.recursiveKeyField);
				
				// pId列改变了
				if(pIdIndex != null && cellColIndex == pIdIndex)
				{
					var pNode = null;
					//如果 newValue为空，放到根节点下
					if(newValue == null || newValue == ""){
						pNode = this.rootNode;
//						if(level.parentLevel != null)
//						{	
//							var fk = level.detailKeyParameter;
//							// 当前层的外键是不是ds中的真实字段
//							var detailKeyParameterIndex = ds.nameToIndex(fk);
//							// 当前层节点的外键字段,该字段必须是ds中的真实存在的字段
//							if(fk != null && detailKeyParameterIndex != -1)
//							{	
//								var fkValue = node.nodeData.getCellValue(detailKeyParameterIndex);
//								pNode = this.idNodeMap.get(fkValue + "_" + level.parentLevel.id);
//							}
//						}
					}
					else
						pNode = this.idNodeMap.get(newValue + "_" + level.id);
					if(pNode == null) {
						ds.setValueAt(ds.getRowIndex(node.nodeData), pIdIndex, oldValue, true);
						//showErrorDialog("pId value changed to:" + newValue + ",but no tree node reference to this id!(error data)");
						return;
					}
					
					// 此节点要挂的父节点是他自己
					if(newValue != null && newValue != "" && newValue == node.nodeData.getCellValue(idIndex)) {
						//showErrorDialog("the node's parent id is itself!");
						ds.setValueAt(ds.getRowIndex(node.nodeData), pIdIndex, oldValue, true);
						return;
					}
					// 此节点要挂的父节点是他的子节点
					if (TreeViewComp.checkParentNode(newValue, oldValue, idIndex, pIdIndex, node, ds) == false) {
						ds.setValueAt(ds.getRowIndex(node.nodeData), pIdIndex, oldValue, true);
						return;
					}
					if(newValue != oldValue){ //newValue != pNode.nodeData.getCellValue(idIndex)
						pNode.insertBefore(node);
						// 对于pId列改变的情况从界面remove时会删除idNodeMap中该节点的信息,所以此处必须补上(insertBefore方法会删除idNodeMap中的信息)
						if(this.idNodeMap.get(node.id) == null){
							this.idNodeMap.put(node.id, node);
						}
						
						this.paintZone();
						// 让改变了父节点的node显示出来
						this.letNodeVisible(node);
						// 判断此node节点改变父节点前是否选中,如果选中则置于选中状态 gd check 09-06
						var dsSelRows = ds.getSelectedRows();
						if(dsSelRows != null && dsSelRows.length > 0)
						{	
							if(dsSelRows[0].rowId == node.nodeData.rowId)
								node.selNode();
						}
					}
				}
				// id列改变了
				else if(idIndex != null && cellColIndex == idIndex)
				{
					// oldValue为空说明是新增行
					if(oldValue == null)
						this.idNodeMap.remove(row.rowId + "_" + level.id);
					else
						this.idNodeMap.remove(oldValue + "_" + level.id);
					node.id = newValue + "_" + level.id;
					this.idNodeMap.put(node.id, node);
				}
			}	
			// 判断是否组成显示值的某个字段改变
			var labelFields = level.labelFields;
			var isLabelChange = false;
			for(var i = 0; i < labelFields.length; i ++)
			{	
				// caption值改变
				if(cellColIndex == ds.nameToIndex(labelFields[i]))
					isLabelChange = true;
			}
			if(isLabelChange == true)
			{
				var newCaption = this.generateCaptionByLabelFields(level, node.nodeData);
				// 调用用户的方法
				var changedCaption = this.onBeforeNodeCaptionChange(labelFields, level.labelDelims, node);
				if(changedCaption != null)
					newCaption = changedCaption;
				if(node.divText)
				{
					node.divText.removeChild(node.divText.childNodes[0]);
					node.divText.appendChild(document.createTextNode(newCaption));
				}	
				
				// 设置节点的caption值
				node.caption = newCaption;
				isLabelChange = false;
			}
		}
	} 
	else if (PageChangeEvent.prototype.isPrototypeOf(event)) {
		// 获得当前的"下载"节点(此"下载"节点的父节点正在加载数据)
		var userObj = event.userObject;
		// 加载下一级节点时的处理逻辑(多级树的缓加载情况)
		if (userObj != null && userObj.loadNode != null) {
			var node = userObj.loadNode;
			var pNode = node.parentTreeNode;
			// 获取正在加载节点加载的是哪个level的数据
			var level = node.loadLevel;
			var ds = level.dataset;
			var rows = ds.getRows(event.pageIndex);
			node.parentTreeNode.remove(node.id);
			this.createNodes(rows, ds, level);
			this.manageLeastNode(pNode);
			// 加载完数据后调整树的宽度
			var overWidth = pNode.getChildrenOverParentWidth();
			if (overWidth > 0)
				// guoweic: modify start 2009-11-2
				//this.treeDiv_gen.style.width = this.treeDiv_gen.offsetWidth + overWidth;
				this.treeDiv_gen.style.width = this.treeDiv_gen.offsetWidth + overWidth + "px";
				// guoweic: modify end
			this.paintZone();
		}
		// 其他处理逻辑
		else if(userObj == null){
			// 获取树有几级level
			if (this.levelCount == null)
				this.levelCount = this.getAllLevels().length;
			// 直接加载第一级树
			var ds = this.topTreeLevel.dataset;
			this.paintNodesByLevel(this.topTreeLevel, ds);
			// 当前选中节点设为null
			this.selNode = null;
			// 让选中行选中
			var selRows = dataset.getSelectedRows();
			if (selRows != null && selRows.length > 0)
				this.getNodeByRowId(selRows[0].rowId).selNode();
		}
	}
	// Dataset Undo事件
	else if (DatasetUndoEvent.prototype.isPrototypeOf(event)) {
		this.paintNodesByLevel(this.getLevelByDataset(dataset), dataset);
		// 当前选中节点设为null
		this.selNode = null;
		// 让选中行选中
		var selRows = dataset.getSelectedRows();
		if (selRows != null && selRows.length > 0)
			this.getNodeByRowId(selRows[0].rowId).selNode();
	}
};

/**
 * 此节点要挂的父节点是他的子节点，若是，则返回false，否则返回true
 * @private
 */
TreeViewComp.checkParentNode = function(newValue, oldValue, idIndex, pIdIndex, node, ds) {
	if (newValue != null && newValue != "" && node.childrenTreeNodes != null && node.childrenTreeNodes.length > 0) {
		for ( var i = 0, n = node.childrenTreeNodes.length; i < n; i++) {
			if (newValue == node.childrenTreeNodes[i].nodeData.getCellValue(idIndex)) {
				return false;
			}
			var result = TreeViewComp.checkParentNode(newValue, oldValue, idIndex, pIdIndex, node.childrenTreeNodes[i], ds);
			if (result == false)
				return false;
		}
	}
	return true;
};

/**
 * 重画level层的所有节点
 * @private
 */
TreeViewComp.prototype.paintNodesByLevel = function(level, dataset) {
	// 移除掉界面上已经画的此level中的所有节点
	this.removeUINodesByLevel(level);
	var rows = null;
	if (level.parentLevel != null)
		rows = dataset.getAllRows();
	else
		rows = dataset.getRows();
	this.createNodes(rows, dataset, level);
	this.manageLeastNode();
	// 树节点构建完成后向界面上画节点
	this.paintZone();
};

/**
 * 删除指定的level包含的所有树节点(删除UI节点)
 * 
 * @param{TreeLevel} level
 * @private
 */
TreeViewComp.prototype.removeUINodesByLevel = function(level) {
	var nodes = this.idNodeMap.values();
	if(nodes != null && nodes.length > 0)
	{
		for(var i = 0, count = nodes.length; i < count; i++) {
			if(nodes[i].level == level){
				nodes[i].parentTreeNode.remove(nodes[i].id, true);
			}
		}
		/*确保一下*/
		if(level == this.topTreeLevel){
			this.idNodeMap.clear();
		}
	}
};

/**
 * 节点创建时调用此方法
 * 
 * @private
 */
TreeViewComp.prototype.nodeCreated = function(nodeId, caption, value, pNodeId,
		nodeData, dataset, isBold) {
	if (!this.onBeforeNodeCreate(nodeData))
		return null;
	// 默认实现如下
	var level = this.getLevelByDataset(dataset);
	var isLeaf = null;

	// 最后一级level中的树节点isLeaf均为true,然后在add方法中根据条件改变图标.
	// 其他级中isLeaf均为false,用户可以覆盖此默认过程改变其他层级的图标显示
	if (this.isLastLevel(level))
		isLeaf = true;
	else
		isLeaf = false;
	var node = new TreeNode(nodeId, caption, value, false, isLeaf, "", nodeData, this.withCheckBox,this.checkBoxModel,this.canEdit,isBold);
	node.rowId = nodeData.rowId;
	node.level = level;
	node.pNodeId = pNodeId;
	// 调用用户的方法
	this.onNodeCreated(node);
	return node;
};

/**
 * 树的中序遍历方法
 * 
 * @return nodes数组
 */
TreeViewComp.prototype.inorderTraversal = function() {
	return this.rootNode.inorderTraversal();
};

/**
 * 改变根节点caption的显示内容
 * 
 * @param caption 要显示的新值
 */
TreeViewComp.prototype.changeRootCaption = function(rootCaption) {
	if (this.rootNode && this.rootNode.painted == TreeNode.PAINTALL) {
		this.rootCaption = rootCaption;
		this.rootNode.divText.removeChild(this.rootNode.divText.childNodes[0]);
		this.rootNode.divText.appendChild(document.createTextNode(rootCaption));
	}
};

/*------------------------------------------------
 *
 * 	树控件暴露给用户覆盖的事件接口
 *
 ------------------------------------------------*/

/**
 * 节点创建前事件
 * @private
 */
TreeViewComp.prototype.onBeforeNodeCreate = function(row) {
	var treeRowEvent = {
			"obj" : this,
			"row" : row
		};
	this.doEventFunc("beforeNodeCreate", TreeRowListener.listenerType, treeRowEvent);
	return true;
};

/**
 * 在节点创建的时候调用这个方法
 * 
 * @param node 刚创建的节点
 * @private
 */
TreeViewComp.prototype.onNodeCreated = function(node) {
	var treeNodeEvent = {
			"obj" : this,
			"node" : node
		};
	this.doEventFunc("nodeCreated", TreeNodeListener.listenerType, treeNodeEvent);
};

/**
 * 删除节点
 * 
 * @param node
 * @private
 */
TreeViewComp.prototype.onNodeDelete = function(node) {
	var treeNodeEvent = {
			"obj" : this,
			"node" : node
		};
	this.doEventFunc("onNodeDelete", TreeNodeListener.listenerType, treeNodeEvent);
};

/**
 * 根节点创建的时候调用这个方法,用户可以改变根节点的caption值
 * @private
 */
TreeViewComp.prototype.onRootNodeCreated = function(rootNode) {
	var treeNodeEvent = {
			"obj" : this,
			"node" : rootNode
		};
	this.doEventFunc("rootNodeCreated", TreeNodeListener.listenerType, treeNodeEvent);
};

/**
 * 树的当前选中节点改变前调用此方法,用户返回false可以阻止新的节点被选中.
 * 
 * @param newNode 马上要选中的节点
 * @param oldNode 上一个选中的节点
 * @private
 */
TreeViewComp.prototype.onBeforeSelNodeChange = function(newNode, oldNode) {
	var treeNodeChangeEvent = {
			"obj" : this,
			"newNode" : newNode,
			"oldNode" : oldNode
		};
	this.doEventFunc("beforeSelNodeChange", TreeNodeListener.listenerType, treeNodeChangeEvent);
};

/**
 * 在树节点的某个显示字段改变时调用的方法，用户如果自定义了默认显示值需要返回此值
 * 
 * @param{Array} labelFields 显示值各字段
 * @param{Array} labelDelims 显示值各字段间的分隔符
 * @param{TreeNode} node 当前的节点
 * @private
 */
TreeViewComp.prototype.onBeforeNodeCaptionChange = function(labelFields,
		labelDelims, node) {
	var treeNodeCaptionChangeEvent = {
			"obj" : this,
			"labelFields" : labelFields,
			"labelDelims" : labelDelims,
			"node" : node
		};
	this.doEventFunc("beforeNodeCaptionChange", TreeNodeListener.listenerType, treeNodeCaptionChangeEvent);
};

/**
 * 树的当前选中节点改变后调用此方法
 * 
 * @param node 新的选中的节点
 * @private
 */
TreeViewComp.prototype.onAfterSelNodeChange = function(node) {
	var treeNodeEvent = {
			"obj" : this,
			"node" : node
		};
	this.doEventFunc("afterSelNodeChange", TreeNodeListener.listenerType, treeNodeEvent);
};
  
/**
 * 树节点文字的点击事件，用户覆盖此方法来处理自己的事件
 * 
 * @param node 点击的树节点
 * @param isClickSameNode 如果该参数不为null且为true，表示是点击树节点且点击的是同一个树节点时调用的该方法
 * @private
 */
TreeViewComp.prototype.onclick = function(node, isClickSameNode) {
	var treeNodeMouseEvent = {
			"obj" : this,
			"node" : node,
			"isClickSameNode" : isClickSameNode
		};
	this.doEventFunc("onclick", TreeNodeListener.listenerType, treeNodeMouseEvent);
};

/**
 * 树节点文字的双击事件，用户覆盖此方法来处理自己的事件，返回false会阻止节点默认事件的响应。
 * 注:节点双击事件的默认行为是打开或者收缩此节点的所有子节点。
 * @private
 */
TreeViewComp.prototype.ondbclick = function(node) {
	var treeNodeMouseEvent = {
			"obj" : this,
			"node" : node
		};
	this.doEventFunc("ondbclick", TreeNodeListener.listenerType, treeNodeMouseEvent);
};

/**
 * 拖拽开始时的调用方法，返回false禁止拖拽
 * 
 * @param sourceNode 要拖动的节点对象
 * @private
 */
TreeViewComp.prototype.onDragStart = function(sourceNode) {
	var treeNodeDragEvent = {
			"obj" : this,
			"sourceNode" : sourceNode
		};
	this.doEventFunc("onDragStart", TreeNodeListener.listenerType, treeNodeDragEvent);
};

/**
 * 拖拽结束时的调用方法，如果返回false表示不执行默认的将源节点挂在目标节点下的动作，而由用户决定如何处理，一般情况下不用自己处理。
 * 
 * @param sourceNode 要拖动的节点对象
 * @param targetNode 要附着到的目标节点对象
 * @private
 */
TreeViewComp.prototype.onDragEnd = function(sourceNode, targetNode) {
	var treeNodeDragEvent = {
			"obj" : this,
			"sourceNode" : sourceNode,
			"targetNode" : targetNode
		};
	this.doEventFunc("onDragEnd", TreeNodeListener.listenerType, treeNodeDragEvent);
};

/**
 * 节点的checkbox选中时事件
 * @private
 */
TreeViewComp.prototype.onChecked = function(node) {
	var treeNodeEvent = {
			"obj" : this,
			"node" : node
		};
	this.doEventFunc("onChecked", TreeNodeListener.listenerType, treeNodeEvent);
};

/**
 * 右键菜单显示前事件
 * @private
 */
TreeViewComp.prototype.onBeforeContextMenu = function(node, ds, level) {
	var treeContextMenuEvent = {
			"obj" : this,
			"node" : node,
			"ds" : ds,
			"level" : level
		};
	this.doEventFunc("beforeContextMenu", TreeContextMenuListener.listenerType, treeContextMenuEvent);
};

/**
 * @fileoverview 树结点类
 * @auther gd
 * @version 2.0
 */
TreeNode.prototype.componentType = "TREENODE";
TreeNode.DEFAULT_ID = "DEFAULT_ID";
TreeNode.IMAGE_WIDTH = 19;
// 节点没画
TreeNode.NOPAINT = 1;
// 节点画了一半(表示只画了框架)
TreeNode.PAINTHALF = 2;
// 节点全部画完
TreeNode.PAINTALL = 3;
// 复选状态隐藏复选框
TreeNode.hideCheckbox = true;

/**
 * TreeNode构造函数
 * @class 树节点
 * @constructor
 * @param id 树节点的id
 * @param caption 树节点的显示值
 * @param value 数节点的真实值
 * @param open 初始显示时是否以打开此节点的子节点方式显示
 * @param isLeaf 为真则显示文件图标,为假显示文件夹图标
 * @param reload 是否加一个"正在载入数据"子节点,不是最后一级level的node均要增加此节点
 * @param nodeData ds中此节点的数据
 * @param withCheckBox 是否包含checkbox
 */
function TreeNode(id, caption, value, open, isLeaf, reload, nodeData, withCheckBox, checkBoxModel, canEdit, isBold) {
	this.nodeData = nodeData;
	// 保存此节点的子树节点
	this.canEdit = getBoolean(canEdit, false);
	this.childrenTreeNodes = new Array();
	this.caption = caption;
	this.value = value;
	this.id = id;
	this.isLeaf = getBoolean(isLeaf, false);
	this.open = getBoolean(open, true);
	this.reload = getBoolean(reload, false);
	this.root = false;
	// 标识该节点是否被画过
	this.painted = TreeNode.NOPAINT;
	this.withCheckBox = getBoolean(withCheckBox, false);
	this.checkBoxModel = (checkBoxModel)?checkBoxModel:TreeViewComp.CHECKBOXMODEL_SELF;
	// 标识该节点前面得checkbox是否被选中
	this.checked = false;
	// 标识该节点文字是否加粗
	this.isBold = getBoolean(isBold, false);
	// 文件,文件夹关闭时图标
	if (this.isLeaf) {
		this.icon1 = window.themePath + "/ui/ctrl/tree/images/leaf.gif";
		this.icon2 = this.icon1;
	} else {
		this.icon1 = window.themePath + "/ui/ctrl/tree/images/folder_off.png";
		this.icon2 = window.themePath + "/ui/ctrl/tree/images/folder_on.png";
	}
};

/**
 * 设置文件夹图标为叶子图标
 * 
 * @param isLeaf true表示叶子,false表示文件夹
 */
TreeNode.prototype.setLeaf = function(isLeaf) {
	if (this.isLeaf & isLeaf)
		return;

	this.isLeaf = isLeaf;
	// 文件,文件夹关闭时图标
	if (this.isLeaf) {
		this.icon1 = window.themePath + "/ui/ctrl/tree/images/leaf.gif";
		this.icon2 = this.icon1;
	} else {
		this.icon1 = window.themePath + "/ui/ctrl/tree/images/folder_off.png";
		this.icon2 = window.themePath + "/ui/ctrl/tree/images/folder_on.png";
	}
};

/**
 * 创建
 * 
 * @author guoweic
 * 用DIV代替TABLE
 * 
 * @private
 */
TreeNode.prototype.create1 = function() {
	if (this.painted == TreeNode.NOPAINT) {
		var oThis = this;
		// 标示此节点画得状态
		this.painted = TreeNode.PAINTHALF;
		// 创建每个节点的总div
		this.Div_gen = $ce("DIV");
		// guoweic: modify start 2009-11-3
		// 设置高度从而撑开高度占位
		//this.Div_gen.style.height = TreeViewComp.NODEHEIGHT;
		this.Div_gen.id = this.id;
		this.Div_gen.owner = this;

		// add by chouhl 2012-3-7 divRow外增加table
		this.wholeTable = $ce("table");
		//this.divRowTable.style.marginLeft = (levelNum * 10) + "px";
		this.Div_gen.appendChild(this.wholeTable);
		this.wholeTable.cellSpacing = "0";
		this.wholeTable.cellPadding = "0";
		this.wholeTable.style.width = "100%";
		var oTBody = $ce("tbody");
		this.wholeTable.appendChild(oTBody);
		var row = oTBody.insertRow(-1);
		this.wholeTableCell = row.insertCell(-1);
		// end

		this.divRow = $ce("DIV");
		this.wholeTableCell.appendChild(this.divRow);
		this.divRow.style.position = "relative";
		//this.divRow.style.height = TreeViewComp.NODEHEIGHT;
		this.divRow.style.height = TreeViewComp.NODEHEIGHT + "px";
		this.divRow.style.width = "auto";
		
		this.divRowLeft = $ce("DIV");
		this.divRow.appendChild(this.divRowLeft);
		this.divRowRight = $ce("DIV");
		this.divRow.appendChild(this.divRowRight);
		
		this.divContent = $ce("DIV");
		this.wholeTableCell.appendChild(this.divContent);
		this.divContent.style.height = "100%";
		this.divContent.style.display = "none";

		this.DIVT = $ce("DIV");
		this.DIVT.style[ATTRFLOAT] = "left";
		//this.divContent.appendChild(this.DIVT);
		//this.divContent.style.marginLeft = "19px";/***/
		
		var divtStyle = this.DIVT.style;
		divtStyle.width = "19px";
		divtStyle.height = "0px";
		divtStyle.background = "url(" + window.themePath + "/ui/ctrl/tree/images/I.gif)";

		this.divChildren = $ce("DIV");
		//this.divChildren.style[ATTRFLOAT] = "left";
		//****this.divChildren.style.width = "100%";
		this.divContent.appendChild(this.divChildren);
		
		if (!IS_IE6 && !IS_IE7) {  // 增加一个空DIV行，使this.divContent高度为auto起作用
			this.divSpace = $ce("DIV");
			this.divSpace.style.clear = "both";
			this.divSpace.style.display = "block";
			this.divContent.appendChild(this.divSpace);
		}
		
		if (!this.root) {
			if (this.parentTreeNode.open && !this.parentTreeNode.isLeaf)
				this.parentTreeNode.divContent.style.display = 'block';
			this.parentTreeNode.divChildren.appendChild(this.Div_gen);
			
			if (IS_IE6 && this.parentTreeNode.DIVT.offsetHeight < TreeViewComp.NODEHEIGHT) {
				// guoweic: IE6中会将DIVT的高度初始一个值，所以要将其强行设置为0
				this.parentTreeNode.DIVT.style.height = "0px";
				var currentNode = this.parentTreeNode;
				while (currentNode) {
					if (currentNode.DIVT.style.height == "0px")
						currentNode.DIVT.style.height = TreeViewComp.NODEHEIGHT + "px";
					else {
						//guoweic: add start 2010-1-7
						if (currentNode.DIVT.offsetHeight < TreeViewComp.NODEHEIGHT * currentNode.getPaintedChildNodesSize(0))
						//guoweic: add end
							currentNode.DIVT.style.height = currentNode.DIVT.offsetHeight + TreeViewComp.NODEHEIGHT + "px";
					}
					currentNode = currentNode.parentTreeNode;
				}
			} else {
				var currentNode = this.parentTreeNode;
				while (currentNode) {
					//guoweic: add start 2010-1-7
					if (currentNode.DIVT.offsetHeight < TreeViewComp.NODEHEIGHT * currentNode.getPaintedChildNodesSize(0))
					//guoweic: add end
						currentNode.DIVT.style.height = currentNode.DIVT.offsetHeight + TreeViewComp.NODEHEIGHT + "px";
					currentNode = currentNode.parentTreeNode;
				}
				//this.parentTreeNode.DIVT.style.height = this.parentTreeNode.DIVT.offsetHeight + TreeViewComp.NODEHEIGHT + "px";
			}
	
		}
		
		// guoweic: modify end
	}
};

/**
 * 获得节点下所有已画出的子孙结点数量
 * @author guoweic
 * @param size: 结点数量
 */
TreeNode.prototype.getPaintedChildNodesSize = function(size) {
	var childNodes = this.childrenTreeNodes;
	for (var i = 0, n = childNodes.length; i < n; i++) {
		if (childNodes[i].painted != TreeNode.NOPAINT) { // 该节点已画出
			size++;
			size = childNodes[i].getPaintedChildNodesSize(size);
		}
	}
	return size;
};

/**
 * @private
 */
TreeNode.prototype.create2 = function(levelNum) {
	if (this.painted == TreeNode.PAINTHALF) {
		// 标示此节点画得状态
		this.painted = TreeNode.PAINTALL;
		
		//****
		/*
		var div_left = $ce("div");
		div_left.className = "treenode_left_off";
		var div_center = $ce("div");
		div_center.className = "treenode_off";
		var div_right = $ce("div");
		div_right.className = "treenode_right_off";
		
		this.divRow.appendChild(div_left);
		this.divRow.appendChild(div_center);
		this.divRow.appendChild(div_right);
		*/
		//****
		this.divRowTable = $ce("table");
		this.divRowTable.style.marginLeft = (levelNum * 10) + "px";
		
		//div_center.appendChild(this.divRowTable);
		this.divRow.appendChild(this.divRowTable);
		
		this.divRowTable.cellSpacing = "0";
		this.divRowTable.cellPadding = "0";
		this.divRowTable.style.height = "100%";
//		this.divRowTable.width = "100%";
		var oTBody = $ce("tbody");
		this.divRowTable.appendChild(oTBody);
		
		this.row = oTBody.insertRow(-1);
		// 加号,减号图标的cell
		//this.img1Cell = this.row.insertCell(-1);
		//this.img1Cell.width = "19px";
		//this.img1Cell.height = "23px";

		var oThis = this;
		
		if (this.withCheckBox) {
			//复选框状态  0:全部非选中,1:全部选中,2:部分子非选中,3:只读(当前状态为非选中),4,5:只读(当前状态为选中)
			this.checkstate = 0;
			// 放置checkbox的cell
			this.imgCheckCell = this.row.insertCell(-1);
			if(TreeNode.hideCheckbox){
				this.imgCheckCell.style.width = "0px";
			}else{
				this.imgCheckCell.style.width = "19px";
			}
			this.imgCheckCell.style.height = "26px";
			// checkbox
//			this.checkbox = $ce("input");
//			this.checkbox.type = "checkbox";
			this.checkbox = $ce("img");
			if(TreeNode.hideCheckbox){
				this.checkbox.style.display = "none";
			}
			this.imgCheckCell.appendChild(this.checkbox);
			// 设置选中状态
			if (!this.root){
//				this.checkbox.checked = this.isNodeDataSelected();
				this._setCheck(this.isNodeDataSelected()?1:0);
				this._correctCheckStates();
				//
			}else{
				this._setCheck(0);
			}
			// checkbox选择事件
			this.checkbox.onclick = function() {
				if (oThis.refTree) {
					oThis._setSubChecked(!oThis.checked,oThis.root);
					oThis._correctCheckStates();
//					oThis.setChecked(this.checked);
				}
			};
		}
		
		// 放置文件,文件夹图标的cell
		this.img2Cell = this.row.insertCell(-1);
		this.img2Cell.style.width = "13px";
		this.img2Cell.style.height = "26px";
		// 放置文字的cell
		this.textCell = this.row.insertCell(-1);
		
		// 文字的宽度
//		var textWidth = getTextWidth(this.caption, "treenode_text");

		// 加号,减号箭头图标(初始化为文件,文件夹图标changeIcon方法会替换为加号,减号图标)
		this.img1 = $ce("IMG");
		//this.img1Cell.appendChild(this.img1);
		this.img1.src = this.icon1;
		this.img1.className = "treenode_img";
		// this.img1.style.position = "relative";
		this.img1.style[ATTRFLOAT] = "left";
		this.img1.owner = this;
		this.img1.onclick = handleTreeNodeImg1Click;

		// 文件夹,文件图标
		this.img2 = $ce("IMG");
		this.img2Cell.appendChild(this.img2);
		this.img2.src = this.icon1;
		this.img2.className = "treenode_img";
		// this.img2.style.position = "relative";
		this.img2.style[ATTRFLOAT] = "left";
		this.img2.owner = this;
		this.img2.onclick = handleTreeNodeImg2Click;
		this.img2.ondblclick = handleTreeNodeImg2Dblclick;

		// 创建树每个节点的文字
		this.divText = $ce("SPAN");
		this.divTextOut = $ce("DIV");
		this.divTextOut.appendChild(this.divText);
		this.divTextOut.style.left = "0px";
		this.divTextOut.style.top = "0px";
		this.divTextOut.style.width = "100%";
		this.divTextOut.style.position = "relative";
		this.textCell.appendChild(this.divTextOut);
//		this.textCell.appendChild(this.divText);
		// 保存此节点对象
		this.divText.owner = this;
		this.divText.className = "treenode_text";
		if(this.isLeaf){
			this.divText.style.color = "#000000";
		}
		if(this.isBold){
			this.divText.style.fontWeight = "bold";
		}
		// guoweic: modify start 2009-11-2
		//this.divText.style.height = getTextHeight(this.caption,
		//		this.divText.className);
//		this.divText.style.height = getTextHeight(this.caption,
//						this.divText.className) + "px";
		//this.divText.style.width = textWidth + 10;
//		this.divText.style.width = textWidth + 10 + "px";
		
		//创建编辑，删除按钮div
		this.divHint = $ce("DIV");
		this.divHint.style.width = "40px";
		this.divHint.style.height = this.divText.style.height;
		this.divHint.style.paddingLeft = "15px";
		this.divHint.style.visibility = "hidden";
		this.imgEdit = $ce("IMG");
		this.imgEdit.src = window.themePath + "/ui/ctrl/tree/images/edit.gif";
		this.imgEdit.style[ATTRFLOAT] = "left";
		this.imgEdit.onmouseover = function(e){this.style.cursor="hand"};
		this.imgEdit.onmouseout = function(e){this.style.cursor="default"};
		this.imgEdit.owner = this;
		this.imgEdit.onclick = handleTreeNodeImgEditClick;
		
		this.divHint.appendChild(this.imgEdit);
		
		this.imgDelete = $ce("IMG");
		this.imgDelete.src = window.themePath + "/ui/ctrl/tree/images/delete.gif";
		this.imgDelete.style[ATTRFLOAT] = "left";
		this.imgDelete.onmouseover = function(e){this.style.cursor="hand"};
		this.imgDelete.onmouseout = function(e){this.style.cursor="default"};
		this.imgDelete.owner = this;
		this.imgDelete.onclick = handleTreeNodeImgDeleteClick;
		this.divHint.appendChild(this.imgDelete);
		
		this.hintCell = this.row.insertCell(-1);
		this.hintCell.appendChild(this.divHint);
		
		// guoweic: modify end
		this.divText.appendChild(document.createTextNode(this.caption));
		
		this.divText.onmousedown = function(e) {handleTreeNodeMouseDown(e, this, oThis);};
		this.divText.onmousemove = handleTreeNodeMouseMove;
		this.divText.onmouseup = handleTreeNodeMouseUp;
		this.divText.onmouseover = function(e){handleTreeNodeMouseOver(e, oThis);};
		this.divText.onmouseout = function(e){handleTreeNodeMouseOut(e, oThis);};
		
//		if (this.canEdit == true){
//			this.divRow.onmouseover =  function(e){oThis.showHint()};
//			this.divRow.onmouseout =  function(e){oThis.hideHint()};
//		}
		
		// 树节点的点击事件
		this.divText.onclick = function(e){
			//handleTreeNodeTextClick(e, this);
			this.owner.isClickTreeNode = true;
			handleTreeNodeClick(e, this);
			this.owner.isClickTreeNode = false;
		};//handleTreeNodeClick;
		// 文字双击效果等同于点击文件夹和加号按钮的效果
		this.divText.ondblclick = handleTreeNodeDblclick;
		// 显示右键菜单
		this.divText.oncontextmenu = handleTreeNodeContextMenu;

		if (!this.root) {
			if (this.refTree)
				this.changeChildrenIcon();
		}

		/* 树节点拖放处理函数 */
		this.refTree.oDrag = null;
		this.refTree.iDiffX = 0;
		this.refTree.iDiffY = 0;
		this.refTree.bDragStart = false;
	}
};

/**  
*     @desc: 设置节点以及子节点选中
*     @author: dingrf
*     @type: private
*     @param: state - 状态 true/false
*     @param: treeNode - 目标节点
*     @param: isRoot - 是否为根，为根时，不管哪种复选策略，都全选 
*/
TreeNode.prototype._setSubChecked = function(state,isRoot){
	//点根节点时全选
	if (this.checkBoxModel != TreeViewComp.CHECKBOXMODEL_SELF || isRoot){
		for (var i=0; i<this.childrenTreeNodes.length; i++){
			this.childrenTreeNodes[i]._setSubChecked(state,isRoot);
		};
	}
//	if (state) this._setCheck(treeNode,1);
//	else this._setCheck(treeNode,0);
	this.setChecked(state);
};

/**
*     @desc: 设置复选框状态
*     @author: dingrf
*     @type: private
*     @param: state - 状态 0/1/2
*     @param: treeNode - 目标节点
*/
TreeNode.prototype._setCheck=function(state){
	this.checkstate=(state != null)?state:0;
	if (this.checkbox)
		this.checkbox.src = window.themePath + "/ui/ctrl/tree/images/" + TreeViewComp.checkArray[this.checkstate];
};

/**
*     @desc: 修正父节点的复选框状态
*     @author: dingrf
*     @type: private
*     @param: treeNode - 目标节点
*/
TreeNode.prototype._correctCheckStates=function(){
	if (this.root) return;
	//只设置自己时,或者设置自己和子节点时,不做修正
	if (this.checkBoxModel == TreeViewComp.CHECKBOXMODEL_SELF ||
		this.checkBoxModel == TreeViewComp.CHECKBOXMODEL_SELF_SUB) return;
		
	var parentNode = this.parentTreeNode;
	if (!parentNode) return;
   
	//记录是否存在状态为完全非选中的子
	var flag1=0; 
	//记录是否存在状态为完全选中的子
	var flag2=0;
	for (var i=0; i<parentNode.childrenTreeNodes.length; i++){
		var node = parentNode.childrenTreeNodes[i];
		if (node.checkstate == null) return;
		if (node.checkstate == 0) flag1=1;
      	else if (node.checkstate==1) flag2=1;
        else { flag1=1; flag2=1; break; }
	}

	if ((flag1)&&(flag2)){
		parentNode.setChecked(true);
		parentNode._setCheck(2);
	}
	else if (flag1){
		parentNode.setChecked(false);
		parentNode._setCheck(0);
	}  
	else{
		parentNode.setChecked(true);
		parentNode._setCheck(1);
	}  

	parentNode._correctCheckStates();
};

/**
 * 打开或关闭节点后，重新计算父节点DIVT的高度
 * @author guoweic
 * @private
 */
TreeNode.prototype.resizeParentDIVT = function() {
	var childNodes = this.childrenTreeNodes;
	if (this.open) {  // 关闭节点
		var currentNode = this.parentTreeNode;
		var height = this.oldDivContentHeight;
		while (currentNode) {
			if (currentNode.DIVT.offsetHeight - height > 0) {
				currentNode.DIVT.style.height = currentNode.DIVT.offsetHeight - height + "px";
			}
			currentNode = currentNode.parentTreeNode;
		}
		
//		for (var i=0, n=childNodes.length; i<n; i++) {
//			if (childNodes[i].painted != TreeNode.NOPAINT) {  // 如果该节点打开过
//				var currentNode = this.parentTreeNode;
//				// guoweic: modify start 2010-1-7
//				//if (!IS_IE6) {
//					while (currentNode) {
//						if (currentNode.DIVT.offsetHeight - TreeViewComp.NODEHEIGHT > 0) {
//							currentNode.DIVT.style.height = currentNode.DIVT.offsetHeight - TreeViewComp.NODEHEIGHT + "px";
//						}
//						currentNode = currentNode.parentTreeNode;
//					}
//				//}
//				// guoweic: modify end 2010-1-7
//			}
//		}
	} else {  // 打开节点
		if (this.oldDivContentHeight){
			var currentNode = this.parentTreeNode;
			var height = this.oldDivContentHeight;
			while (currentNode) {
				currentNode.DIVT.style.height = currentNode.DIVT.offsetHeight + height + "px";
				currentNode = currentNode.parentTreeNode;
			}
		}
//		for (var i=0, n=childNodes.length; i<n; i++) {
//			if ((!IS_IE6 && childNodes[i].painted != TreeNode.NOPAINT) || IS_IE6) {  // 如果该节点不是第一次打开（第一次打开时已经重新算了高度）
//				var currentNode = this.parentTreeNode;
//				while (currentNode) {
//					currentNode.DIVT.style.height = currentNode.DIVT.offsetHeight + TreeViewComp.NODEHEIGHT + "px";
//					currentNode = currentNode.parentTreeNode;
//				}
//			}
//			// guoweic: modify end 2010-1-7
//		}	
	}
};

/**
 * 获取节点前的checkbox是否被选中
 */
TreeNode.prototype.isChecked = function() {
	return this.checked;
};

/**
 * 设置节点前的checkbox选中状态
 */
TreeNode.prototype.setChecked = function(checked) {
//	if (this.withCheckBox == true && this.checkbox != null && this.checked != checked) {
	if (this.withCheckBox == true && this.checked != checked) {
//		if (checked != null)
//			this.checkbox.checked = checked;
//		else
//			checked = this.checkbox.checked;
		if (checked) this._setCheck(1);
		else this._setCheck(0);
		
		this.checked = checked;
		
		var tree = this.refTree;
		if (tree.isTreeActive == false)
			return;
		
		// 增加选中内容
		if (!this.isRoot()) {
			// 当前ds所有选中行索引
			var selectedRowIndices = this.level.dataset.getSelectedIndices();
			if (selectedRowIndices == null || selectedRowIndices.length == 0) {
//				tree.multiSelectNodes.push(this);
				if (checked == true)
					this.level.dataset.addRowSelected(this.level.dataset.getRowIndex(this.nodeData));
			} else {
				// 当前点击节点index
				var currRowIndex = this.level.dataset.getRowIndex(this.nodeData);
				// 如果已经为选中节点则反选该节点
				if (selectedRowIndices.indexOf(currRowIndex) != -1) {
//					tree.multiSelectNodes.removeEle(this);
					if (checked == false)
						this.level.dataset.setRowUnSelected(currRowIndex);
				}
				// 继续添加选中节点
				else {
//					tree.multiSelectNodes.push(this);
					if (checked == true)
						this.level.dataset.addRowSelected(this.level.dataset.getRowIndex(this.nodeData));
				}
			}
		}
		this.refTree.onChecked(this);
	}
};

/**
 * 判断节点表示的行数据是否是选中状态
 */
TreeNode.prototype.isNodeDataSelected = function() {
	if (!this.isRoot()) {
		var rows = this.level.dataset.getSelectedRows();
		if (rows != null && rows.length > 0) {
			if (rows.indexOf(this.nodeData) != -1)
				return true;
		}
	}
	return false;
};

/**
 * 处理图片1点击方法
 * @private
 */
function handleTreeNodeImg1Click(e) {
	e = EventUtil.getEvent();
	var oThis = this.owner;
	if (oThis.refTree.isTreeActive == false) {
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		return;
	}
	oThis.toggle();

	// 在禁止掉事件进一步传播时先掉用系统注册方法
	document.onclick();
	stopEvent(e);
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};
/**
 * 节点文字点击方法
 */
function handleTreeNodeTextClick(e, oThis) {
	e = EventUtil.getEvent();
	if (oThis.owner.refTree.isTreeActive == false) {
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		return;
	}
	oThis.owner.toggle();
	
	// 在禁止掉事件进一步传播时先掉用系统注册方法
	document.onclick();
	stopEvent(e);
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 处理图片2点击方法
 * @private
 */
function handleTreeNodeImg2Click(e) {
	e = EventUtil.getEvent();
	var oThis = this.owner;
	if (oThis.refTree.isTreeActive == false) {
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		return;
	}
	// 使文件夹的点击处理和加号的点击处理效果一样
	oThis.toggle();
	// 在禁止掉事件进一步传播时先掉用系统注册方法
	document.onclick();
	stopEvent(e);
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 处理图片2双击方法
 */
function handleTreeNodeImg2Dblclick(e) {
	e = EventUtil.getEvent();
	var oThis = this.owner;
	if (oThis.refTree.isTreeActive == false) {
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		return;
	}
	oThis.dblclick(oThis);
	stopEvent(e);
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 处理右键菜单方法
 * @private
 */
function handleTreeNodeContextMenu(e) {
	e = EventUtil.getEvent();
	var oThis = this.owner;
	if (oThis.refTree.isTreeActive == false) {
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		return;
	}
	if (oThis.root == false) {
		oThis.contextmenu(e);
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
}

/**
 * 处理树节点双击方法
 * @private
 */
function handleTreeNodeDblclick(e) {
//	e = EventUtil.getEvent();
	var oThis = this.owner;
	if (oThis.refTree.isTreeActive == false)
		return;
	// 用户阻止默认的双击行为执行
	if (oThis.dblclick(oThis) == false)
		return;
	// 使文字的双击处理和文件夹的点击处理效果一样
	else
		oThis.toggle();
};

/**
 * 处理树节点单击方法
 * @private
 */
function handleTreeNodeClick(e, treeNode) {
	var oThis = treeNode.owner, tree = oThis.refTree;
	if (tree.isTreeActive == false)
		return;
	var newNode = oThis, oldNode = tree.selNode;

	e = EventUtil.getEvent();

	// 如果当前点击节点不是选中的节点
	if (newNode != tree.selNode) {
		//复选状态隐藏复选框,调用复选框事件改变状态
		if(oThis.withCheckBox){
			oThis.checkbox.onclick(e);
			return;
		}
		// 在选中节点改变之前调用用户的处理方法,返回false则阻止选中节点改变
		if (tree.beforeSelNodeChange(newNode, oldNode) == false) {
			document.onclick();
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}
		// 将即将选中的节点存储在树的即将选中节点属性中
		tree.nextSelNode = newNode;

		// 内置根节点处于显示状态被点击
		if (newNode.root == true && tree.withRoot == true) {
			if (tree.withCheckBox == false) {  // 没有checkbox情况下
				// 选中节点从其他有ds的节点跳转到根节点的时候要设置oldNode节点不选中,避免再回到oldNode时不能选中的问题
				if (oldNode != null && oldNode != oThis) {
					var ds = oldNode.level.dataset;
					ds.setRowUnSelected(ds.getRowIndex(oldNode.nodeData));
					ds.setRowSelected(-1);

					// 选中根节点
					tree.rootNode.selNode();
					tree.click(tree.rootNode);
				}
				// 第一次在没有任何节点选中的情况下直接点击根结点
				else if (oldNode == null) {
					tree.rootNode.selNode();
					tree.click(tree.rootNode);
				}
			}
		} 
		else {
			// 清除根节点的选中状态
			if (tree.withRoot == true)
				tree.rootNode.deSelNode();
			// 通知dataset选中节点
			var ds = oThis.level.dataset;
			if (ds != null) {
				// 获取树有几级level
				if (tree.levelCount == null)
					tree.levelCount = tree.getAllLevels().length;
				var lastLevelNode = oThis;
				var index = null;
				// 从本级任何一个节点找到上一级的节点,得到keyValue(一级以上情况)
				if (tree.levelCount > 1) 
				{
					while (lastLevelNode.level == oThis.level) {
						// 避免lastLevelNode为根节点
						if (lastLevelNode.parentTreeNode.root == true)
							break;
						lastLevelNode = lastLevelNode.parentTreeNode;
					}

					// 第二级以后节点点击才需要设置keyValue
					if (oThis.level != tree.topTreeLevel) {
						var keyValue = lastLevelNode.keyValue;
						if (keyValue == null) {
							alert(lastLevelNode.caption + " keyValue is null!");
							// 删除事件对象（用于清除依赖关系）
							clearEventSimply(e);
							return;
						}

						index = ds.getRowIndex(oThis.nodeData, 0, keyValue);

						// 对于一个ds,对于keyvalue的每次切换都要调用setRowUnSelected把上次的选中节点置为不选中,
						// 避免在次选中此keyvalue下的同一个节点时不会选中的情况
						if (oldNode != null && oldNode.level == oThis.level && keyValue != ds.currentKey)
							oldNode.level.dataset.setRowUnSelected(
									oldNode.level.dataset.getRowIndex(oldNode.nodeData),
									0, ds.currentKey);
						
						if (tree.levelCount > 1 && keyValue != ds.currentKey)
							ds.setCurrentPage(keyValue, null, {'firstLoad':false});
					}
					else{
						//如果第二级树ds的currentKey不等于当前树节点key,更新第二级树的ds.currentkey
						var levels = tree.getAllLevels();
						for ( var i = 0; i < levels.length; i++) {
							if (levels[i][0].id == "level2"){
								subDs = levels[i][0].dataset;
								if (oThis.keyValue == null){
									var key = oThis.level.masterKeyField;
									var keyValue = oThis.nodeData.getCellValue(oThis.level.dataset
									.nameToIndex(key));
									if (keyValue != null) {
										var parameterMap = subDs.reqParameterMap;
										parameterMap.clear();
										parameterMap.put(IDatasetConstant.QUERY_PARAM_KEYS,
										levels[i][0].detailKeyParameter);
										parameterMap.put(IDatasetConstant.QUERY_PARAM_VALUES, keyValue);
										subDs.setReqParameterMap(parameterMap);
										subDs.setCurrentPage(keyValue, null, {'firstLoad':false});
										oThis.keyValue = keyValue;
									}	
								}
								else if (oThis.keyValue != subDs.currentKey)
									subDs.setCurrentPage(oThis.keyValue, null, {'firstLoad':false});
								break;	
							}
						}
						index = ds.getRowIndex(oThis.nodeData);
					}

					// 得到上一个选中的节点,如果不是同一个ds,要先设置上一个ds的此节点非选中,避免在次选中此ds下的同一个节点时不会选中的情况
					if (oldNode != null) {
						if (oldNode.root)  // 如果是根节点
							tree.rootNode.deSelNode();
						else if (oldNode.level && oldNode.level.dataset != null && oThis.level.dataset != null
								&& oldNode.level.dataset != oThis.level.dataset)
							oldNode.level.dataset.setRowUnSelected(oldNode.level.dataset
										.getRowIndex(oldNode.nodeData));
					}
				} else
					index = ds.getRowIndex(oThis.nodeData);
				if (tree.withCheckBox == true)
					ds.addRowSelected(index);
				else
					ds.setRowSelected(index);

				// 设置聚焦行
				ds.setFocusRowIndex(index);
			}
		}
	}
	// 点击的是同一个节点
	else {
		if(oThis.withCheckBox){//复选状态隐藏复选框,调用复选框事件改变状态
			oThis.checkbox.onclick(e);
		}else{
			// true表示点击树节点时点击的是同一个节点
			tree.onclick(newNode, true);				
		}
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
}

/**
 * 处理树节点鼠标移入方法
 * @private
 */
function handleTreeNodeMouseMove(e) {
	var oThis = this.owner;
	if (oThis.refTree.isDragEnabled == false)
		return;

	if (oThis.refTree.isTreeActive == false)
		return;

	e = EventUtil.getEvent();
	
	// IE中0表示无按键动作，1为鼠标左键；firefox中无按键动作和按鼠标左键都是0
	if ((e.button == 1 || e.button == 0) && window.dragStart) {
//	if (e.button == 1) {
		var tree = oThis.refTree;
		if (IS_IE)  // 仅IE中有此方法 
			window.oDrag.setCapture();
		oThis.refTree.bDragStart = true;
		tree.dragDiv.style.display = "block";
		//TODO
//		tree.dragDiv.style.left = e.x ? e.x : e.pageX - tree.iDiffX + "px";
//		tree.dragDiv.style.top = e.y ? e.y : e.pageY - tree.iDiffY + "px";
		if (IS_IE) {
			tree.dragDiv.style.left = e.x + 10 + "px";
			tree.dragDiv.style.top = e.y + "px";
		} else {
			tree.dragDiv.style.left = e.clientX - 185 + "px";
			tree.dragDiv.style.top = e.clientY - 25 + "px";
			if (tree.Div_gen.scrollWidth > tree.Div_gen.clientWidth) {  // 出现横向滚动条
				tree.dragDiv.style.left = e.clientX - 185 + (tree.Div_gen.scrollWidth - tree.Div_gen.clientWidth) + "px";
			}
			if (tree.Div_gen.scrollHeight > tree.Div_gen.clientHeight) {  // 出现纵向滚动条
				tree.dragDiv.style.top = e.clientY - 25 + (tree.Div_gen.scrollHeight - tree.Div_gen.clientHeight) + "px";
			}
		}
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
}

/**
 * 处理树节点鼠标点下方法
 * @private
 */
function handleTreeNodeMouseDown(e, divText, oThis) {

	// 设置不能拖动树则返回不处理
	if (divText.owner.refTree.isDragEnabled == false)
		return;

	if (divText.owner.refTree.isTreeActive == false)
		return;

	e = EventUtil.getEvent();
	
	// IE中0表示无按键动作，1为鼠标左键；firefox中无按键动作和按鼠标左键都是0
	if (e.button == 1 || e.button == 0) {
//	if (e.button == 1) {
		/* 处理拖拽初始化事件 */
		window.dragStart = true;
		var tree = divText.owner.refTree;
		window.oDrag = getTarget(e);

		var dragDiv = tree.onDragStart(divText.owner);
		// 调用用户的处理方法
		if (dragDiv == false) {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}

		if(dragDiv == null)
			dragDiv = tree.createDragDiv(divText);
		dragDiv.style.position = "absolute";
		dragDiv.style.display = "none";
		document.body.appendChild(dragDiv);
		tree.dragDiv = dragDiv;
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

TreeViewComp.prototype.createDragDiv = function(divText){
	var dragDiv = $ce("div");
	dragDiv.style.width = divText.offsetWidth + "px";
	dragDiv.style.height = divText.offsetHeight + "px";
	dragDiv.style.background = "yellow";
	dragDiv.appendChild(document.createTextNode(divText.firstChild.nodeValue));
	return dragDiv;
};

/**
 * 销毁自身
 * @private
 */
TreeNode.prototype.destroySelf = function(withChildren) {
	if(withChildren == null)
		withChildren = true;
	if(this.img1)
		this.img1.owner = null;
	if(this.img2)
		this.img2.owner = null;
	if(this.Div_gen)
		this.Div_gen.owner = null;
	if(this.divText)
		this.divText.owner = null;
	if(withChildren){
		var children = this.childrenTreeNodes;
		if(children != null){
			for(var i = 0; i < children.length; i ++)
				children[i].destroySelf();
		}
		delete this.childrenTreeNodes;
	}
};

/**
 * 处理树节点鼠标抬起方法
 * @private
 */
function handleTreeNodeMouseUp(e) {

	window.dragStart = false;
	var tree = this.owner.refTree;
	if (tree.dragDiv){
		var dragDiv = tree.dragDiv;
		tree.dragDiv = null;
		document.body.removeChild(dragDiv);
	}
	
	if (this.owner.refTree.isDragEnabled == false)
		return;

	if (this.owner.refTree.isTreeActive == false)
		return;

	e = EventUtil.getEvent();
	
	if (tree.bDragStart != null && tree.bDragStart == true) {
		// 得到要拖放到的目标位置
		var targetNode = getTarget(e).owner;
		// 要拖放的源节点
		var sourceNode = window.oDrag.owner;
		// 如果目标位置不是node,或者仍然是原来node则返回
		if (!TreeNode.prototype.isPrototypeOf(targetNode)
				|| targetNode == sourceNode) {
			if (IS_IE)
				window.oDrag.releaseCapture();
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}

		// 在全局中保存源节点和目标节点
		tree.targetNode = targetNode;
		tree.sourceNode = sourceNode;

		// 返回false表示要组织默认的处理事件,由用户处理
		tree.onDragEnd(sourceNode, targetNode);
//		if (tree.onDragEnd(sourceNode, targetNode) == false) {
//			if (IS_IE)
//				window.oDrag.releaseCapture();
//			tree.dragDiv.style.display = "none";
//			this.owner.refTree.bDragStart = false;
//			return;
//		} else {
//			if (IS_IE)
//				window.oDrag.releaseCapture();
//			tree.dragDiv.style.display = "none";
//			var ds = sourceNode.level.dataset;
//			var level = sourceNode.level;
//			// 获得行的index值
//			var index = ds.getRowIndex(sourceNode.nodeData);
//			// 通知ds树节点的父id改变
//			//TODO ??????????????
//			ds.setValueAt(index, ds.nameToIndex(level.recursivePKeyField),
//					targetNode.id);
//			this.owner.refTree.bDragStart = false;
//		}
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
}

/**
 * 处理树节点鼠标进入方法
 * @private
 */
function handleTreeNodeMouseOver(e, oThis) {
	if(!(oThis.divRow.className && oThis.divRow.className == 'treenode_on')){
		oThis.divRow.className = "treenode_over";
		oThis.divRowLeft.className = "treenode_left_over";
		oThis.divRowRight.className = "treenode_right_over"
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
}

/**
 * 处理树节点鼠标离开方法
 * @private
 */
function handleTreeNodeMouseOut(e, oThis) {
	if(!(oThis.divRow.className && oThis.divRow.className == 'treenode_on')){
		oThis.divRow.className = "";
		oThis.divRowLeft.className = "";
		oThis.divRowRight.className = ""
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
}

/**
 * 树节点编辑按钮
 * 
 * @param {} e
 */
function handleTreeNodeImgEditClick(e){
	var oThis = this.owner;
	e = EventUtil.getEvent();
	
	oThis.divText.style.visibility = "hidden";
	oThis.divHint.style.visibility = "hidden";
	
	//创建编辑输入框 absolute relative  oThis.divTextOut
	var width = oThis.divText.style.width.replace("px","");
	width = parseInt(width) + 10;
	width = 120;
	
	//当前值 
	var rowIndex = oThis.level.dataset.getRowIndex(oThis.nodeData);
	var colIndex = oThis.level.dataset.nameToIndex(oThis.level.labelFields[0]);
	var value = oThis.level.dataset.getValueAt(rowIndex, colIndex);
	
	if (oThis.comp == null){
		oThis.comp = new StringTextComp(oThis.divTextOut, "text", 0, 0, width,
					"absolute", "", "");
		oThis.comp.newValue = value;			
		var textListener = new TextListener();
		textListener.valueChanged = function(valueChangeEvent) {
			TreeViewComp.compValueChangeFun(oThis, valueChangeEvent);
			oThis.comp.hide(); 
			oThis.divText.style.visibility = "";
			oThis.divHint.style.visibility = "";
		};
		oThis.comp.addListener(textListener);
		// 焦点移出事件
//		var compFocusListener = new FocusListener();
//		compFocusListener.onBlur = function(keyEvent) {
//			oThis.comp.hide();
//		};
//		oThis.comp.addListener(compFocusListener);
	}
	else{
		oThis.comp.newValue = value;
		oThis.comp.show();
	}
	oThis.comp.setFocus();
};

function handleTreeNodeImgDeleteClick(e){
	var oThis = this.owner;
	e = EventUtil.getEvent();
	oThis.refTree.onNodeDelete(oThis);
}

/**
 * 值改变时事件处理
 * 
 * @private
 */
TreeViewComp.compValueChangeFun = function(oThis, valueChangeEvent) {
	var comp = valueChangeEvent.obj;
	var newValue = comp.getValue();
	
	var rowIndex = oThis.level.dataset.getRowIndex(oThis.nodeData);
	var colIndex = oThis.level.dataset.nameToIndex(oThis.level.labelFields[0]);
	
	oThis.level.dataset.setValueAt(rowIndex, colIndex, newValue);
};


/**
 * 显示hint
 */
TreeNode.prototype.showHint = function(){
	if (this.hintShow == true ) return;
//	alert(0);
//	if (!IS_IE){
//		this.divHint.style.left = (event.clientX + 10) + "px";
//		this.divHint.style.top = (event.clientY + 10) + "px";
//	}
//	else{
//		this.divHint.style.left = e.clientX + document.body.scrollLeft + 20 + "px";
////		this.divHint.style.top = this.divText.offsetTop;
//		this.divHint.style.top = e.clientY + document.body.scrollTop + "px";
//	}
	if (TreeNode.oldShowHintNode != null)
		TreeNode.oldShowHintNode.hideHint();
	TreeNode.oldShowHintNode = this;	
	this.divHint.style.visibility = "";
	this.hintShow = true;
};

/**
 * 隐藏hint
 */
TreeNode.prototype.hideHint = function(){
	this.divHint.style.visibility = "hidden";
	if (this.comp != null && this.comp.visible == true)
		this.comp.hide();
	this.divText.style.visibility = "";	
	this.hintShow = false;
};

/**
 * 判断节点是否是叶子节点
 */
TreeNode.prototype.isLeafNode = function() {
	return this.isLeaf;
};

/**
 * 判断此节点是否是根节点
 */
TreeNode.prototype.isRoot = function() {
	return this.root;
};

/**
 * treenode节点的鼠标双击处理函数
 * 
 * @private
 */
TreeNode.prototype.dblclick = function(node) {
	return this.refTree.dblclick(node);
};

/**
 * 根据根节点此时的状态在切换打开和关闭之间的图标
 * 
 * @private
 */
TreeNode.prototype.toggle = function() {
	if (this.hasChildren()) {
		if (this.painted == TreeNode.NOPAINT)
			return;
		if (this.open) {
			// 即将展开的节点的孩子的宽度从而减少树的宽度
			this.oldDivContentHeight = this.divContent.offsetHeight; 
			this.divContent.style.display = 'none';
			// guoweic modify firefox、IE8中有问题 start 2009-11-4
			if (IS_IE6 || IS_IE7)
				this.adjustTreeWidth(false);
			// guoweic modify end
		
			// guoweic: add start 2009-12-18
			// 重新计算父节点DIVT的高度
			this.resizeParentDIVT();
			// guoweic: add end
			
			this.open = false;
		
			this.refTree.paintZone();
		} 
		else {
			// 即将展开的节点的孩子的宽度从而增加树的宽度
			this.divContent.style.display = 'block';
			// guoweic modify firefox、IE8中有问题 start 2009-11-4
			if (IS_IE6 || IS_IE7)
				this.adjustTreeWidth(true);
			//this.adjustTreeWidth(false);
		
			// guoweic: add start 2009-12-18
			// 重新计算父节点DIVT的高度
			this.resizeParentDIVT();
			// guoweic: add end
			
			this.open = true;
			this.refTree.paintZone();
			// 此节点第一次打开的时候需要动态加载节点
			if (this.reload)
				this.reloadNode();
		}
		this.changeIcon();
		adjustContainerFramesHeight();
	}
};

/**
 * 此节点展开子节点时调整树的宽度
 * 
 * @param isAddWidth true表示增加树的宽度,false表示减小树的宽度
 * @private
*/
TreeNode.prototype.adjustTreeWidth = function(isAddWidth) {
	if (!this.root) {
		if (isAddWidth) {
			var overWidth = this.getChildrenOverParentWidth();
			if (overWidth > 0) {
				this.refTree.treeDiv_gen.style.width = (this.refTree.treeDiv_gen.offsetWidth
						+ overWidth) + "px";
				this.refTree.treeDiv_gen.scrollLeft = overWidth + "px";
			}
		} else {
			return;
		}
	}
};

/**
 * 此节点的展开时,子节点比此节点增加的宽度
 */
TreeNode.prototype.getChildrenOverParentWidth = function() {
	var allLevelNodes = this.getLevelChildrenNodesArray();
	if (allLevelNodes == null)
		return 0;

	var maxWidthArr = new Array();
	var maxWidthNodes = new Array();

	// 计算每层打开节点中标题文字最长的节点
	for ( var i = 0, count = allLevelNodes.length; i < count; i++) {
		var nodes = allLevelNodes[i], maxWidth = 0, tempWidth = 0, maxWidthNode = null;
		for ( var j = 0, count1 = nodes.length; j < count1; j++) {
			tempWidth = getTextWidth(nodes[j].caption, "treenode_text");
			if (tempWidth > maxWidth) {
				maxWidth = tempWidth;
				maxWidthNode = nodes[j];
			}
		}
		maxWidthNodes.push(maxWidthNode);
		maxWidthArr.push(maxWidth);
	}

	// 所有子层中宽度最大值
	var maxNodeWidth = 0;
	var tmpWidth = 0;
	// 宽度过长导致树出滚动条的节点
	var overWidthNode = null;
	for ( var i = 0; i < maxWidthArr.length; i++) {
		// 每一层比上层宽度多25px
		tmpWidth = (i + 1) * 25 + maxWidthArr[i];
		if (tmpWidth > maxNodeWidth) {
			maxNodeWidth = tmpWidth;
			overWidthNode = maxWidthNodes[i];
		}
	}
	// 计算超出的宽度
	var overWidth = maxNodeWidth + 60
			- this.parentTreeNode.Div_gen.offsetWidth;
	if (overWidth > 0) {
		// 保存此节点展开时子节点增加的宽度
		this.overWidth = overWidth;
		this.refTree.resultScrollNodes.push(overWidthNode);
		return overWidth;
	} else
		return 0;
};

/**
 * 得到此节点的所有孩子节点,按层返回,每层中包括的节点是已经打开的节点
 * 
 * @return{二级数组} 树组的第0个元素为孩子中第一级的所有打开节点的数组,第1个元素为孩子中第二级所有打开节点的数组
 * @private
 */
TreeNode.prototype.getLevelChildrenNodesArray = function() {
	var arr = new Array();
	var tempArr = new Array();
	if (this.hasChildren()) {
		for ( var i = 0; i < this.childrenTreeNodes.length; i++) {
			tempArr.push(this.childrenTreeNodes[i]);
		}
		arr.push(tempArr);
		var tArr = this.getChildrenNodes(arr);
		return tArr;
	} else
		return null;
};

/**
 * 得到该节点之前的所有兄弟节点和各个兄弟节点打开的子节点个数
 */
TreeNode.prototype.getPreVisibleSiblingCount = function() {
	var siblings = this.getPreviousSiblings();
	if (siblings == null || siblings.length == 0)
		return 0;
	var arr = [], tempArr = [];
	for ( var i = 0, count = siblings.length; i < count; i++) {
		tempArr.push(siblings[i]);
	}
	arr.push(tempArr);
	var tArr = this.getChildrenNodes(arr);
	var nodesCount = 0;
	for ( var i = 0; i < tArr.length; i++)
		nodesCount += tArr[i].length;
	return nodesCount;
};

/**
 * @private 递归获得所有子层中每层打开节点的数组
 */
TreeNode.prototype.getChildrenNodes = function(arr) {
	var lastLevelNodes = arr[arr.length - 1];
	var node = null;
	var tempArray = new Array();
	for ( var i = 0; i < lastLevelNodes.length; i++) {
		node = lastLevelNodes[i];
		// 此节点有孩子且处于打开状态,则存储此节点的所有孩子节点
		if (node.hasChildren() && node.open == true) {
			for ( var j = 0; j < node.childrenTreeNodes.length; j++)
				tempArray.push(node.childrenTreeNodes[j]);
		}
	}
	if (tempArray.length != 0) {
		arr.push(tempArray);
		return this.getChildrenNodes(arr);
	} else
		return arr;
};

/**
 * 动态请求并加载此节点下的数据
 * 
 * @private
 */
TreeNode.prototype.reloadNode = function() {
	if (this.reload == false)
		return;

	if (this.hasChildren()) {
		// 如果此节点下挂子节点中有"正在加载"节点,说明展开此节点时要去动态加载此节点下面的节点
		var reloadNodes = [];
		for ( var i = this.childrenTreeNodes.length - 1; i >= 0; i--) {
			if (this.childrenTreeNodes[i].id
					.endWith(TreeViewComp.RELOADNODE_suffix))
				reloadNodes.push(this.childrenTreeNodes[i]);
		}
		
		var currDs = this.level.dataset;
		for ( var i = 0; i < reloadNodes.length; i++) {
			var loadNode = reloadNodes[i];
			var childDataset = loadNode.loadLevel.dataset;
			var parameterMap = childDataset.reqParameterMap;
			parameterMap.clear();
			var dataset = loadNode.level.dataset;
			var key = loadNode.value;
			var keyValue = loadNode.nodeData.getCellValue(dataset
					.nameToIndex(key));
			if (keyValue != null) {
				parameterMap.put(IDatasetConstant.QUERY_PARAM_KEYS,
						loadNode.loadLevel.detailKeyParameter);
				parameterMap.put(IDatasetConstant.QUERY_PARAM_VALUES, keyValue);
				childDataset.setReqParameterMap(parameterMap);
				if(currDs == childDataset){
					// 记录当前的"数据下载"节点
					childDataset.appendCurrentPage({'firstLoad':false, 'loadNode':loadNode}, keyValue);
					loadNode.parentTreeNode.remove(loadNode.id);
				}
				else{
					// 记录当前的"数据下载"节点
					childDataset.setCurrentPage(keyValue, null, {'firstLoad':false, 'loadNode':loadNode});
					// 将ds的当前页的keyValue放在此节点的keyValue属性中
					this.keyValue = keyValue;
				}
			}
		}
	}
};

/**
 * 改变caption的显示内容
 * 
 * @param caption 要显示的新值
 */
TreeNode.prototype.changeCaption = function(caption) {
	this.caption = caption;
	if (this.painted == TreeNode.PAINTALL) {
		this.divText.removeChild(this.divText.childNodes[0]);
		this.divText.appendChild(document.createTextNode(caption));
	}
};

/**
 * 改变选中节点的外观(只负责使节点变为选中样式)
 */
TreeNode.prototype.selNode = function() {
	var tree = this.refTree;
	if(tree == null || tree.Div_gen == null || tree.Div_gen.offsetWidth == 0)
		return;
	if (tree.selNode != null && tree.selNode != this && tree.selNode.level) {
		var selNodeDs = tree.selNode.level.dataset;
		// 当前选中节点和要选中的节点不在一个ds,则要设置上一个选中节点对应的ds的行非选中 gd 2009-09-03
		if (selNodeDs != null && this.level != null && this.level.dataset != null
				&& selNodeDs != this.level.dataset)
			selNodeDs.setRowUnSelected(selNodeDs
					.getRowIndex(tree.selNode.nodeData));
		if (tree.selNode != null)
			tree.selNode.deSelNode();
	}

	// 如果该节点没有显示出来则先显示该节点
	if (this.parentTreeNode != null)
		tree.letNodeVisible(this);
	tree.selNode = this;

	// 改变选中节点外观
	if (this.hasChildren()) {
		if (this.divText){
			this.divText.className = "treenode_nodesel";
		}
		if(!(this.withCheckBox && !this.isClickTreeNode)){
			this.divRow.className = "treenode_on";
			this.divRowLeft.className = "treenode_left_on";
			this.divRowRight.className = "treenode_right_on";
		}
	} else {
		if (this.divText){
			this.divText.className = "treenode_textsel";
		}
		if(!(this.withCheckBox && !this.isClickTreeNode)){
			this.divRow.className = "treenode_on";
			this.divRowLeft.className = "treenode_left_on";
			this.divRowRight.className = "treenode_right_on";
		}
	}
	if (!IS_IE || IS_IE8)
		this.refTree.paintZone();
		//显示修改删除按钮
	if (this.canEdit == true){
		this.showHint();
	}
};

/**
 * 当前选中的节点调用此方法会使此节点变为未选中状态. 只能是当前树节点中处于选中状态的节点调用此方法.
 */
TreeNode.prototype.deSelNode = function() {
	if (this.divText){
		this.divText.className = "treenode_text";
		if(this.isBold){
			this.divText.style.fontWeight = "bold";
		}
		if(this.withCheckBox && this.checked){
			this.divText.className = "treenode_textsel";
		}
		if(!(this.withCheckBox && this.checked)){
			this.divRow.className = "";
			this.divRowLeft.className = "";
			this.divRowRight.className = "";
		}
	}
	if (this.refTree.selNode != null && this.refTree.selNode == this)
		this.refTree.selNode = null;
};

/**
 * 移除掉此节点下的给定id的子结点
 */
TreeNode.prototype.remove = function(id) {
	var ind = this.isExist(id);
	if (ind != -1) {
		
		// 得到删除节点
		var node = this.childrenTreeNodes[ind];
		
		/*
		 * 得到node的所有子孙节点,将此节点的painted属性设为未画,
		 * 等此节点重新挂到树的某个节点下的时候,这些子节点将重新painted自己
		 */
		var nodes = node.inorderTraversal();
		if (nodes != null && nodes.length > 0) {
			// 由于父节点改变,这些节点将等到父节点挂接后重新绘制
			var lossParentNodes = "";
			for ( var i = nodes.length - 1; i >= 0; i--) {
				nodes[i].painted = TreeNode.NOPAINT;
				lossParentNodes += nodes[i].caption + " | ";
			}
//			log("'" + node.caption
//					+ "' parentId change, this node's all children "
//					+ lossParentNodes + " loss parent!");
		}
		// 得到此节点上一个节点
		var preNode = node.previous();
		// 得到此节点下一个节点
		var nextNode = node.next();

		if (node.painted == TreeNode.PAINTALL) {

			// 从map中删除此节点的子节点
			TreeNode.removeChildNodeFromIdNodeMap(node);
				
			this.divChildren.removeChild(node.Div_gen);

			// 减少连接线高度
			if (this.DIVT.offsetHeight >= TreeViewComp.NODEHEIGHT)
				this.DIVT.style.height = this.DIVT.offsetHeight - TreeViewComp.NODEHEIGHT + "px";
			
			// 减少父节点连接线高度
			var parentNode = this.parentTreeNode;
			while (parentNode != null) {
				if (parentNode.DIVT.offsetHeight >= TreeViewComp.NODEHEIGHT)
					parentNode.DIVT.style.height = parentNode.DIVT.offsetHeight - TreeViewComp.NODEHEIGHT + "px";
				parentNode = parentNode.parentNode;
			}
			
			this.childrenTreeNodes.splice(ind, 1);
			// 没有孩子了则删除DIVT
			if (this.hasChildren() == false)
				this.divContent.style.display = "none";

			if (preNode != null)
				preNode.changeChildrenIcon();
			if (nextNode != null)
				nextNode.changeChildrenIcon();
			this.changeChildrenIcon();
			// 改变树的高度
			this.refTree.paintZone();
		}
		
		// 将该节点从父节点的子节点列表中删除
		var parentNode = node.parentTreeNode;
		if (parentNode) {
			var newChildNodesArray = new Array();
			for ( var i = 0, n = parentNode.childrenTreeNodes.length; i < n; i++) {
				if (node != parentNode.childrenTreeNodes[i])
					newChildNodesArray.push(parentNode.childrenTreeNodes[i]);
			}
			parentNode.childrenTreeNodes = newChildNodesArray;
		}
		
		// 从map中删除此节点
		this.refTree.idNodeMap.remove(id);
			
	}
};

/**
 * 从map中删除此节点的子节点
 */
TreeNode.removeChildNodeFromIdNodeMap = function(node) {
	var nodes = node.childrenTreeNodes;
	if (nodes != null) {
		for (var i = 0, n = nodes.length; i < n; i++) {
			var childNode = nodes[i];
			if (node.refTree.idNodeMap.containsKey(childNode.id))
				node.refTree.idNodeMap.remove(childNode.id);
			if (childNode.childrenTreeNodes != null && childNode.painted == TreeNode.PAINTALL)
				TreeNode.removeChildNodeFromIdNodeMap(childNode);
		}
	}
};

/**
 * 得到节点的所有孩子,按前序遍历算法
 * 
 * @return nodes树组
 */
TreeNode.prototype.preorderTraversal = function() {
	var children = new Array();
	var temp = this.getAllChildrenByPreorder(children);
	temp.push(this);
	return temp;
};

/**
 * @private
 */
TreeNode.prototype.getAllChildrenByPreorder = function(children) {
	if (this.hasChildren()) {
		for ( var i = this.childrenTreeNodes.length - 1; i >= 0; i--) {
			this.childrenTreeNodes[i].getAllChildrenByPreorder(children);
			children.push(this.childrenTreeNodes[i]);
		}
	}
	return children;
};

/**
 * 得到节点的所有孩子，按中序遍历算法
 */
TreeNode.prototype.inorderTraversal = function() {
	var children = new Array();
	return this.getAllChildrenByInorder(children);
};

/**
 * @private
 */
TreeNode.prototype.getAllChildrenByInorder = function(children) {
	if (this.hasChildren()) {
		for ( var i = 0; i < this.childrenTreeNodes.length; i++) {
			children.push(this.childrenTreeNodes[i]);
			this.childrenTreeNodes[i].getAllChildrenByInorder(children);
		}
	}
	return children;
};

/**
 * 判断指定node是否有孩子节点
 */
TreeNode.prototype.hasChildren = function() {
	if (this.childrenTreeNodes == null || this.childrenTreeNodes.length == 0)
		return false;
	else
		return true;
};

/**
 * 判断给定节点是否存在于此节点的子节点
 * 
 * @param nodeId 要查找的node的id
 * @return 找到的节点在此节点下的位置
 */
TreeNode.prototype.isExist = function(nodeID) {
	if (!this.hasChildren())
		return -1;
	var trouve = false;
	var i = 0;
	var ind = -1; // 保存找到的节点的index值
	while (!trouve && i < this.childrenTreeNodes.length) {
		var node = this.childrenTreeNodes[i];
		if (node.id == nodeID) {
			trouve = true;
			ind = i;
		}
		i++;
	}
	return ind;
};

/**
 * 设置此节点及其子节点的refTree属性
 * @private
 */
TreeNode.prototype.setRefTree = function(refTree) {
	for ( var i = 0; i < this.childrenTreeNodes.length - 1; i++) {
		var node = this.childrenTreeNodes[i];
		node.setRefTree(refTree);
	}
	this.refTree = refTree;
};

/**
 * 给指定节点添加子结点
 * 
 * @param node 树节点对象
 */
TreeNode.prototype.add = function(node) {
	this.insertBefore(node, null);
};

/**
 * 将给定节点node插入到本节点下的nodeId节点之前，如果不指定nodeId则插入到此节点孩子节点的最后一个
 * 
 * @param node 要插入的节点
 * @param nodeId 要插入在此id指定的节点之前
 */
TreeNode.prototype.insertBefore = function(node, nodeId) {
	if(node == null)
    	return;
    if(this.isExist(node.id) != -1)
    	return;
    if(node.parentTreeNode)
    	node.parentTreeNode.remove(node.id, false);
    node.parentTreeNode = this;
    node.refTree = this.refTree;
    // 把node的painted状态设为false
    node.painted = TreeNode.NOPAINT;
   	
    // 改变叶子节点为文件夹节点,只有最后一级的节点允许从叶子节点变为文件夹节点
    if(this.isLeafNode() && this.refTree.isLastLevel(this.level))
    	this.setLeaf(false);	
   	
    var ind = -1;
    if(nodeId != null)
    {
	    ind = this.isExist(nodeId);
	    if(ind != -1)
	    {
	        for(var i = this.childrenTreeNodes.length - 1; i >= ind; i--)
	            this.childrenTreeNodes[i+1] = this.childrenTreeNodes[i];        
	        this.childrenTreeNodes[ind] = node;             
	    }
	}
	// 不指定nodeId则插入到最后    
    else {
    	ind = this.childrenTreeNodes.push(node);
    }
    node.index = ind;
    if(this.painted == TreeNode.PAINTALL)
    	node.changeChildrenIcon();
};

/**
 * 改变此节点父节点img1的icon和本身img1的icon. 根据不同情况设置加号、减号图标的图片
 * 
 * @private
 */
TreeNode.prototype.changeChildrenIcon = function() {
	
};

TreeNode.prototype.changeChildrenIcon_backup = function() {
	// this是根节点则不改变外观
	if (this.id == this.refTree.rootNode.id)
		return;

	// 得到此节点的父节点
	var pNode = this.parentTreeNode;
	if (pNode.hasChildren()) {
		/* 改变父节点图标 */
		if (pNode.painted == TreeNode.PAINTALL) {
			// 父节点是同级节点中的最后一个节点
			if (pNode.isLast()) {
				pNode.srcPlus = window.themePath + "/ui/ctrl/tree/images/Lplus.gif";
				pNode.srcMoins = window.themePath + "/ui/ctrl/tree/images/Lminus.gif";
				if (pNode.id == this.refTree.rootNode.id)
					pNode.DIVT.style.display = "none";
				else {
					pNode.DIVT.style.background = "url(" + window.themePath + "/ui/ctrl/tree/images/I2.gif)";
					pNode.DIVT.style.display = "block";
				}
			} else {
				pNode.srcPlus = window.themePath + "/ui/ctrl/tree/images/Tplus.gif";
				pNode.srcMoins = window.themePath + "/ui/ctrl/tree/images/Tminus.gif";
				if (pNode.id == this.refTree.rootNode.id
						&& this.refTree.withRoot == false)
					pNode.DIVT.style.display = "none";
				else {
					pNode.DIVT.style.background = "url(" + window.themePath + "/ui/ctrl/tree/images/I.gif)";
					pNode.DIVT.style.display = "block";
				}
			}
			pNode.changeIcon();
		}

		/* 改变当前节点图标 */
		if (this.painted == TreeNode.PAINTALL) {
			if (this.hasChildren()) {
				// 有孩子并且没有下一个兄弟
				if (this.next() == null) {
					this.srcPlus = window.themePath + "/ui/ctrl/tree/images/Lplus.gif";
					this.srcMoins = window.themePath + "/ui/ctrl/tree/images/Lminus.gif";
					// 变此节点的divT为空白 gd check 09-06
					this.DIVT.style.background = "url(" + window.themePath + "/ui/ctrl/tree/images/I2.gif)";
				}
				// 有孩子并且有下一个兄弟
				else {
					this.srcPlus = window.themePath + "/ui/ctrl/tree/images/Tplus.gif";
					this.srcMoins = window.themePath + "/ui/ctrl/tree/images/Tminus.gif";
				}
			} else {
				// 没有孩子并且没有下一个兄弟
				if (this.next() == null) {
					this.srcPlus = window.themePath + "/ui/ctrl/tree/images/L.gif";
					this.srcMoins = window.themePath + "/ui/ctrl/tree/images/L.gif";
				}
				// 没有孩子并且有下一个兄弟
				else {
					this.srcPlus = window.themePath + "/ui/ctrl/tree/images/T.gif";
					this.srcMoins = window.themePath + "/ui/ctrl/tree/images/T.gif";
				}
			}
			this.changeIcon();
		}

		/* 改变此节点的上一个兄弟节点图标 */
		var previousNode = this.previous();
		if (previousNode != null) {
			if (previousNode.painted == TreeNode.PAINTALL) {
				if(previousNode.childrenTreeNodes != null && previousNode.childrenTreeNodes.length > 0) {
					previousNode.srcPlus = window.themePath + "/ui/ctrl/tree/images/Tplus.gif";
					previousNode.srcMoins = window.themePath + "/ui/ctrl/tree/images/Tminus.gif";
					if (previousNode.open == true)
						previousNode.DIVT.style.background = "url(" + window.themePath + "/ui/ctrl/tree/images/I.gif)";
				} else {
					previousNode.srcPlus = window.themePath + "/ui/ctrl/tree/images/T.gif";
					previousNode.srcMoins = window.themePath + "/ui/ctrl/tree/images/T.gif";
				}
				previousNode.changeIcon();
			}
		}
	}
}; 

/**
 * 加号、减号图片和文件夹图片的开关变换(根据创建节点时设置的叶子、文件夹图标改变img2,根据changeChildrenIcon中设置的加号、减号改变img1)
 * 
 * @private
 */
TreeNode.prototype.changeIcon = function() {
	if (this.painted != TreeNode.PAINTALL)
		return;

	if (this.open) {
		this.img1.src = this.srcMoins;
		this.img2.src = this.icon2;
		// if(!this.isLeaf)
		// this.img2.src = window.themePath + "/images/treeview/folder_on.gif";
	} else {
		this.img1.src = this.srcPlus;
		this.img2.src = this.icon1;
		// if(!this.isLeaf)
		// this.img2.src = window.themePath + "/images/treeview/folder_off.gif";
	}
};

/**
 * 得到此节点之前所有未画的兄弟节点
 */
TreeNode.prototype.getPreviousNoPaintSiblings = function() {
	var arr = [];
	var allSiblings = this.parentTreeNode.childrenTreeNodes;
	for ( var i = 0, count = allSiblings.length; i < count; i++) {
		if (allSiblings[i].id == this.id)
			break;
		else {
			if (allSiblings[i].painted == TreeNode.NOPAINT)
				arr.push(allSiblings[i]);
		}
	}
	return arr;
};

/**
 * 得到此节点之前的所有兄弟节点
 * 
 * @return 此节点的之前的所有兄弟节点.没有兄弟节点返回null
 */
TreeNode.prototype.getPreviousSiblings = function() {
	var arr = [];
	var allSiblings = this.parentTreeNode.childrenTreeNodes;
	for ( var i = 0, count = allSiblings.length; i < count; i++) {
		if (allSiblings[i].id == this.id)
			break;
		else
			arr.push(allSiblings[i]);
	}
	return arr;
};

/**
 * 返回给定孩子节点在父节点下的索引
 * 
 * @param TreeNode childNode 孩子节点
 */
TreeNode.prototype.getIndexOfChild = function(childNode) {
	if (!(childNode instanceof TreeNode))
		return;
	if (this.isExist(childNode.id) != -1)
		return this.childrenTreeNodes.indexOf(childNode);
};

/**
 * 返回指定索引处的孩子节点
 * 
 * @param int index 索引
 */
TreeNode.prototype.getChild = function(index) {
	if (this.hasChildren() && this.childrenTreeNodes.length - 1 >= index)
		return this.childrenTreeNodes[index];
};

/**
 * 返回此节点下的孩子数量
 * 
 * @return 如果没有子,或者是叶子节点返回0
 */
TreeNode.prototype.getChildCount = function() {
	if (this.hasChildren())
		return this.childrenTreeNodes.length;
	else
		return 0;
};

/**
 * 判断此节点是否是当前父节点的最后一个节点
 */
TreeNode.prototype.isLast = function() {
	if (!this.root)
		return (this.parentTreeNode.childrenTreeNodes[this.parentTreeNode.childrenTreeNodes.length - 1] == this);
	else
		return true;
};

/**
 * 判断此节点是否是当前父节点的第一个节点
 */
TreeNode.prototype.isFirst = function() {
	if (!this.root)
		return (this.parentTreeNode.childrenTreeNodes[0] == this);
	else
		return true;
};

/**
 * 若指定节点有子节点，则返回第一个子节点，否则返回null
 */
TreeNode.prototype.first = function() {
	if (this.hasChildren())
		return (this.childrenTreeNodes[0]);
	else
		return null;
};

/**
 * 若指定节点有子节点，则返回最后一个子节点，否则返回null
 */
TreeNode.prototype.last = function() {
	if (this.hasChildren())
		return (this.childrenTreeNodes[this.childrenTreeNodes.length - 1]);
	else
		return null;
};

/**
 * 若指定节点有下一个节点则返回下一个节点，否则返回null
 */
TreeNode.prototype.next = function() {
	if (!this.root) {
		var index = this.parentTreeNode.childrenTreeNodes.indexOf(this);
		if (index < this.parentTreeNode.childrenTreeNodes.length - 1)
			return this.parentTreeNode.childrenTreeNodes[index + 1];
		else
			return null;
	} else
		return null;
};

/**
 * 若指定节点有上一个节点则返回上一个节点，否则返回null
 */
TreeNode.prototype.previous = function() {
	if (!this.root) {
		var index = this.parentTreeNode.childrenTreeNodes.indexOf(this);
		if (index > 0)
			return this.parentTreeNode.childrenTreeNodes[index - 1];
		else
			return null;
	} else
		return null;
};

/**
 * 激发树节点的右键菜单
 * 
 * @private
 */
TreeNode.prototype.contextmenu = function(e) {
	var ds = this.level.dataset;
	
	// 选中该行
	var rowId = this.nodeData.rowId;
	var index = ds.getRowIndexById(rowId);
	ds.setRowSelected(index);
	
	// 从当前节点所属的level中获取contextMenu对象
	var level = this.refTree.getLevelByDataset(ds);
	// 用户可以阻止contextMenu的弹出
	if (this.refTree.onBeforeContextMenu(this, ds, level) == false) {
		stopEvent(e);
		stopDefault(e);
		return;
	}

	var contextMenu = level.contextMenu;
	if (contextMenu != null) {
		contextMenu.triggerObj = this;
		contextMenu.left = 0;
		contextMenu.show(e);
		stopEvent(e);
		stopDefault(e);
	}
};

/**
 * @private
 */
TreeNode.prototype.refreshReloadNode = function(node) {
	this.remove(this.noeudReload.id);
	this.add(node);
};

/**
 * 得到node所在的level对象
 * 
 * @return TreeLevel
 */
TreeNode.prototype.getTreeLevel = function() {
	return this.level;
};

/**
 * 得到node所在的dataset
 * 
 * @return Dataset
 */
TreeNode.prototype.getDataset = function() {
	return this.level.dataset;
};

/**
 * 生成完整节点的JSON对象
 * @private
 */
//TreeNode.generateAllNodeJsonObj = function(nodeJs, parentNodeId) {
//	var nodeJson = new Object;
//	nodeJson.javaClass = "nc.uap.lfw.core.comp.WebTreeNode";
//	nodeJson.id = nodeJs.id;
//	if (nodeJs.nodeData)
//		nodeJson.rowId = nodeJs.nodeData.rowId;
//	nodeJson.label = nodeJs.caption;
////	nodeJson.value = nodeJs.value;
//	nodeJson.parentNode = parentNodeId;
//	
//	var childNodeListObj = new Object;
//	childNodeListObj.javaClass = "java.util.ArrayList";
//	childNodeListObj.list = new Array();
//	
//	var childNodes = nodeJs.childrenTreeNodes;
//	if (childNodes) {
//		for (var i = 0, n = childNodes.length; i < n; i++) {
//			childNodeListObj.list.push(TreeNode.generateNodeJsonObj(childNodes[i], nodeJson.id));
//		}
//	}
//	nodeJson.childNodeList = childNodeListObj;
//	return nodeJson;
//};

/**
 * 生成节点的JSON对象及子节点
 * @private
 * 
 * genChildType: no_child/child_level1/child_allLevel
 */
TreeNode.generateNodeJsonObj = function(nodeJs, genParent, genChildType, nodeJson) {
	var rootJsonObj = null;
	if (nodeJson == null)
		nodeJson = new Object;
	nodeJson.javaClass = "nc.uap.lfw.core.comp.WebTreeNode";
	nodeJson.id = nodeJs.id;
	if (nodeJs.nodeData)
		nodeJson.rowId = nodeJs.nodeData.rowId;
	nodeJson.label = nodeJs.caption;
	if (genChildType != "no_child"){
		var childNodeListObj = new Object;
		childNodeListObj.javaClass = "java.util.ArrayList";
		childNodeListObj.list = new Array();
		
		var childNodes = nodeJs.childrenTreeNodes;
		if (childNodes) {
			for (var i = 0, n = childNodes.length; i < n; i++) {
				var childType = "no_child";
				if (genChildType == "child_allLevel")	
					childType = "child_allLevel";
				var childJsonObj = TreeNode.generateNodeJsonObj(childNodes[i], false, childType);
				childJsonObj.parentNode = nodeJson.id; 
				childNodeListObj.list.push(childJsonObj);
			}
		}
		nodeJson.childNodeList = childNodeListObj;
	}
	if (genParent && nodeJs.parentTreeNode != null){
		var parentJsonObj = TreeNode.generateNodeJsonObj(nodeJs.parentTreeNode, true, "no_child");			
//		nodeJson.parentNode = parentJsonObj.id;
//		var childNodeListObj = new Object;
//		childNodeListObj.javaClass = "java.util.ArrayList";
//		childNodeListObj.list = new Array();
//		childNodeListObj.list.push(nodeJson);
//		parentJsonObj.childNodeList = childNodeListObj;
//		nodeJson = parentJsonObj; 

		nodeJson.parentNode = nodeJs.parentTreeNode.id;
		var parentJsonObj = new Object;
		var childNodeListObj = new Object;
		childNodeListObj.javaClass = "java.util.ArrayList";
		childNodeListObj.list = new Array();
		childNodeListObj.list.push(nodeJson);
		parentJsonObj.childNodeList = childNodeListObj;
		
		rootJsonObj =  TreeNode.generateNodeJsonObj(nodeJs.parentTreeNode, true, "no_child", parentJsonObj);
		
	}
	if (rootJsonObj != null)
		return rootJsonObj;
	else	
		return nodeJson;
};

/**
 * 
 * @private
 */
TreeNode.getRootJsonObj = function(nodeJson) {
	var rootJson = nodeJson; 
	while (rootJson.parentNode != null){
		rootJson = rootJson.parentNode;
	}
	return rootJson;
};

/**
 * 获取对象信息
 * @private
 */
TreeViewComp.prototype.getContext = function(type) {
	var context = new Object;
	context.c = "TreeViewContext";
	context.enabled = this.isTreeActive;
	context.withRoot = this.withRoot;
	//当前节点
//	if(type == "tree_current" && this.selNode != null){
//		var treeModel = new Object;
//		treeModel.javaClass = "nc.uap.lfw.core.comp.WebTreeModel";
//		var rootNode = TreeNode.generateNodeJsonObj(this.selNode, false, "no_child");
////		treeModel.rootNode = TreeNode.getRootJsonObj(currentNode);
//		treeModel.rootNode = rootNode;
//		treeModel.currentNodeId = this.selNode.id;
//		context.treeModel = treeModel;
//	}
	//当前节点、所有父节点
//	else 
	if(type == "tree_current_parent" && this.selNode != null){
		var treeModel = new Object;
		treeModel.javaClass = "nc.uap.lfw.core.comp.WebTreeModel";
		var rootNode = TreeNode.generateNodeJsonObj(this.selNode, true, "no_child");
		treeModel.rootNode = rootNode;
		treeModel.currentNodeId = this.selNode.id;
		context.treeModel = treeModel;
	}
	//当前节点、当前节点的第一级子节点
//	else if(type == "tree_current_children" && this.selNode != null){
//		var treeModel = new Object;
//		treeModel.javaClass = "nc.uap.lfw.core.comp.WebTreeModel";
//		var rootNode = TreeNode.generateNodeJsonObj(this.selNode, false, "child_level1");
//		treeModel.rootNode = rootNode;
//		treeModel.currentNodeId = this.selNode.id;
//		context.treeModel = treeModel;
//	}
	//当前节点、所有父节点、当前节点的第一级子节点
	else if(type == "tree_current_parent_children" && this.selNode != null){
		var treeModel = new Object;
		treeModel.javaClass = "nc.uap.lfw.core.comp.WebTreeModel";
		var rootNode = TreeNode.generateNodeJsonObj(this.selNode, true, "child_level1");
		treeModel.rootNode = rootNode;
		treeModel.currentNodeId = this.selNode.id;
		context.treeModel = treeModel;
	}
	//所有节点
	else if(type == "tree_all"){
		var treeModel = new Object;
		treeModel.javaClass = "nc.uap.lfw.core.comp.WebTreeModel";
		var rootNode = TreeNode.generateNodeJsonObj(this.rootNode, false, "child_allLevel");
		treeModel.rootNode = rootNode;
		treeModel.currentNodeId = this.selNode ?  this.selNode.id : null;
		context.treeModel = treeModel;
	}
	
//	if(type == "tree_current_parent_root_tree"){
//		var treeModel = new Object;
//		treeModel.javaClass = "nc.uap.lfw.core.comp.WebTreeModel";
//		var rootNode = TreeNode.generateNodeJsonObj(this.rootNode, null);
//		treeModel.rootNode = rootNode;
//		context.treeModel = treeModel;
//	}
//	else if(type == "tree_current_root") {
//		var treeModel = new Object;
//		treeModel.javaClass = "nc.uap.lfw.core.comp.WebTreeModel";
//		var rootNode = TreeNode.generateNodeJsonObj(this.rootNode, null);
//		treeModel.rootNode = rootNode;
//		context.treeModel = treeModel;
//	}
//	else if(type == "tree_current_parent_root"){
//		var treeModel = new Object;
//		treeModel.javaClass = "nc.uap.lfw.core.comp.WebTreeModel";
//		var rootNode = TreeNode.generateNodeJsonObj(this.rootNode, null);
//		treeModel.rootNode = rootNode;
//		context.treeModel = treeModel;
//	}
	context.text = this.rootCaption;
	return context;
};

/**
 * 设置对象信息
 * @private
 */
TreeViewComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	if (context.text != null && context.text != this.rootCaption)
		this.changeRootCaption(context.text);
	//TODO
	
};

