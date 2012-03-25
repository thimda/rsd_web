/**
 * 
 * ListToList控件。
 * 
 * @author gd, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 2.修正以前的BUG . guoweic <b>modified</b>
 * 3.增加双击后移动区域功能 . guoweic <b>modified</b>
 * 
 */
ListToListComp.prototype = new BaseComponent;
ListToListComp.BUTTSWIDTH = 16;
ListToListComp.BUTTSHEIGHT = 16;

/**
 * ListToListComp构造函数
 * @class ListToList控件
 */
function ListToListComp(parent, name, left, top, width, height, position,
		leftLabelName, rightLabelName, isLeftTree) {
	if (arguments.length == 0)
		return;

	this.base = BaseComponent;
	this.base(name, left, top, width, height);

	this.position = getString(position, "absolute");
	this.parentOwner = parent;
	this.leftLabelName = getString(leftLabelName,
			trans("ml_allchooseablevalues"));
	this.rightLabelName = getString(rightLabelName,
			trans("ml_allchoosedvalues"));
	// 左边是否是树
	if (isLeftTree != null && isLeftTree)
		this.isLeftTree = true;
	else
		isLeftTree = false;
	this.create();
};

/**
 * ListToListComp的主体创建函数
 * @private
 */
ListToListComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.name;
	this.Div_gen.style.width = this.width + "px";
	this.Div_gen.style.height = this.height + "px";
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.position = this.position;
	if (this.parentOwner) {
		this.placeIn(this.parentOwner);
	}
};

/**
 * 设置左边树
 */
ListToListComp.prototype.setLeftTree = function(leftNewTree) {
	var oThis = this;
	this.leftPanel.removeChild(this.leftPanel.childNodes[0]);
	this.leftTree = leftNewTree;
	this.leftPanel.appendChild(this.leftTree.Div_gen);
	// 初始左树 add luoyf
	if (this.leftTree != null) {
		this.leftTree.onclick = function(node, isClickSameNode) {
			if (oThis.changeValues != null && oThis.changeValues.length > 0) {
				// 将选中项从选中数组中删除
				for ( var i = 0; i < oThis.changeValues.length; i++) {
					oThis.changeValues.splice(0, 1);
				}
			}
			if (oThis.getTreeNodeFilter(node)) {
				var nodeCoptionMap = oThis.getNodeOptionMap(node);
				var caption = null;
				var value = null;
				if (nodeCoptionMap != null) {
					var ds = node.dataset;
					var row = node.nodeData;
					caption = row.getCellValue(ds.nameToIndex(nodeCoptionMap[0]));
					value = row.getCellValue(ds.nameToIndex(nodeCoptionMap[1]));
				} else {
					caption = node.caption;
					value = node.value;
				}
				var treeitem = new OptionComp(caption, value, null, false);
				if (oThis.changeValues == null)
					oThis.changeValues = new Array();
				oThis.changeValues.push(treeitem);
			}
		}
	}
};

/**
 * 回掉函数,具体构造listToList中的所有组件
 * @private
 */
ListToListComp.prototype.manageSelf = function() {
	var oThis = this;

	var totalWidth = this.Div_gen.offsetWidth;
	var totalHeight = this.Div_gen.offsetHeight;
	// parent, name, left, top, width, height, position, scroll, className
	this.backGroundPanel = new PanelComp(this.Div_gen, "backgroud", 0, 0,
			totalWidth, totalHeight, false, "");

	// 存放目前选中的items
	this.changeValues = new Array();

	// 在最右侧再添加4个按钮,所以为了不重新计算,把width减去50,为最右边的按钮留出位置
	var cw = totalWidth - 50;

	var listWidth = (cw - 56) / 2 - 20;
	var listHeight = totalHeight - 20 - 20 - 40;
	var listTop = (totalHeight - listHeight) / 2;

	// 计算左list的left值
	var leftPanelLeft = ((cw - 80) / 2 - listWidth) / 2;
	// 计算左label的left值
	this.leftLabel = new LabelComp(this.backGroundPanel, "leftlabel",
			leftPanelLeft, 10, this.leftLabelName, "");
	// 计算右侧容器panel的left值
	var rightPanelLeft = 80 + (cw - 80) / 2 + leftPanelLeft - 20;
	// 构造右容器div,树或者list均放于此容器内
	this.rightPanel = $ce("div");
	this.rightPanel.style.position = "absolute";
	this.rightPanel.style.left = rightPanelLeft + "px";
	this.rightPanel.style.top = listTop + "px";
	this.rightPanel.style.width = listWidth + "px";
	this.rightPanel.style.height = listHeight + "px";
	this.rightPanel.style.overflow = "hidden";
	this.rightPanel.style.border = "solid red 1px";
	this.backGroundPanel.Div_gen.appendChild(this.rightPanel);
	// 构造rightList
	this.rightList = new ListComp(this.rightPanel, "rightlist", 0, 0,
			listWidth, listHeight, true, "");
	var oThisRightList = this.rightList;

	// 构造左容器div,树或者list均放于此容器内
	this.leftPanel = $ce("div");
	this.leftPanel.style.position = "absolute";
	this.leftPanel.style.left = leftPanelLeft + "px";
	this.leftPanel.style.top = listTop + "px";
	this.leftPanel.style.width = listWidth + "px";
	this.leftPanel.style.height = listHeight + "px";
	this.leftPanel.style.overflow = "hidden";
	this.leftPanel.style.border = "solid blue 1px";
	this.backGroundPanel.Div_gen.appendChild(this.leftPanel);

	// 构造leftList
	var oThisLeftList = null;

	if (!this.isLeftTree) {
		this.leftList = new ListComp(this.leftPanel, "leftlist", 0, 0,
				listWidth, listHeight, true, "absolute", null);
		oThisLeftList = this.leftList;
	}

	// 初始化leftList中的所有值
	if (this.leftList != null) {
		this.leftList.valueChanged = function(items) {
			// 先将rightList中的选中项清除
			if (oThis.changeValues != null && oThis.changeValues.length > 0) {
				// 将显示方式变为未选中
				for ( var i = 0; i < oThis.changeValues.length; i++) {
					oThis.changeValues[i].Div_gen.className = "option_unsel";
				}

				// 将选中项从选中数组中删除
				for ( var i = 0; i < oThis.changeValues.length; i++) {
					oThis.changeValues.splice(0, 1);
				}
			}
			oThis.changeValues = items;
		};
	}

	// 初始化rightList中的所有值
	this.rightList.valueChanged = function(items) {
		// 先将leftList中的选中项清除
		if (oThis.changeValues != null && oThis.changeValues.length > 0) {
			// 将显示方式变为未选中
			for ( var i = 0; i < oThis.changeValues.length; i++) {
				oThis.changeValues[i].Div_gen.className = "option_unsel";
			}

			// 将选中项从选中数组中删除
			for ( var i = 0; i < oThis.changeValues.length; i++) {
				oThis.changeValues.splice(0, 1);
			}
		}
		oThis.changeValues = items;
	};

	// 计算右label的left值
	var labelRightLeft = rightPanelLeft;
	this.rightLabel = new LabelComp(this.backGroundPanel, "rightlabel",
			labelRightLeft, 10, this.rightLabelName, "absolute", null);

	var buttLeft = listWidth + 30;
	var buttBaseTop = Math.round((listHeight - 94) / 2) + listTop;
	var buttInternal = 10;

	/*--------------------------------------
		点击左边的项后将左边的项打到右边 
	 *--------------------------------------*/
	// this.oneToRight = new ButtonComp(this.backGroundPanel, "oneToRight",
	// buttLeft, listTop, ListToListComp.BUTTSWIDTH, ListToListComp.BUTTSHEIGHT,
	// "向右", "向右", "", "", "");
	this.oneToRight = new ImageComp(this.backGroundPanel, "oneToRight",
			window.themePath + "/images/listtolist/torightnormal.gif",
			buttLeft, buttBaseTop, "", "", trans("ml_toright"),
			window.themePath + "/images/listtolist/torighthot.gif", "");
	this.oneToRight.onclick = function() {
		oThis.oneToRightFunc();
	};

	/*--------------------------------------
		将左边的items全部移到右边
	 *--------------------------------------*/
	// this.allToRight = new ButtonComp(this.backGroundPanel, "allToRight",
	// buttLeft, listTop + 2*ListToListComp.BUTTSHEIGHT + 2*buttInternal,
	// ListToListComp.BUTTSWIDTH, ListToListComp.BUTTSHEIGHT, ml_alltoright, "",
	// "", "", "");
	this.allToRight = new ImageComp(this.backGroundPanel, "allToRight",
			window.themePath + "/images/listtolist/alltorightnormal.gif",
			buttLeft, buttBaseTop + ListToListComp.BUTTSHEIGHT + buttInternal,
			"", "", trans("ml_alltoright"), window.themePath
					+ "/images/listtolist/alltorighthot.gif", "");
	this.allToRight.onclick = function() {
		var items = null;
		if (!oThis.isLeftTree) {
			// 左侧list中没有items了,则不能再向右移动
			if (oThisLeftList.allItems == null
					|| oThisLeftList.allItems.length == 0)
				return;
			items = oThisLeftList.allItems;
		} else {
			// 得到构造左树节点的所有optioncomp、除过右边没有的
			var rItems = oThisRightList.allItems;
			var tmpMap = new HashMap();// 临时Map，放右边的item
			if (rItems != null) {
				for ( var k = 0; k < rItems.length; k++) {
					tmpMap.put(rItems[k].value, rItems[k].value);
				}
			}
			var allNodes = oThis.leftTree.idNodeMap.values();
			items = new Array();
			for ( var kk = 0, len = allNodes.length; kk < len; kk++) {
				if (oThis.getTreeNodeFilter(allNodes[kk])) {
					var nodeCoptionMap = oThis.getNodeOptionMap(allNodes[kk]);
					var opCaption = null;
					var opValue = null;
					if (nodeCoptionMap != null) {
						var ds = allNodes[kk].dataset;
						var row = allNodes[kk].nodeData;
						opCaption = row.getCellValue(ds.nameToIndex(nodeCoptionMap[0]));
						opValue = row.getCellValue(ds.nameToIndex(nodeCoptionMap[1]));
					} else {
						opCaption = allNodes[kk].caption;
						opValue = allNodes[kk].value;
					}
					if (tmpMap.containsKey(opValue))
						continue;
					var treeOC = new OptionComp(opCaption, opValue, null, false);
					items.push(treeOC);
				}
			}
			tmpMap = null;
		}

		// 让items引用oThisLeftList.allItems对象
		if (items == null || items.length == 0)
			return;
		var tempArr = new Array();
		for ( var i = 0; i < items.length; i++) {
			tempArr.push(items[i]);
		}
		// 将allItems置空
		if (!oThis.isLeftTree)
			oThisLeftList.allItems = null;
		for ( var i = 0; i < tempArr.length; i++) {
			if (!oThis.isLeftTree) {
				// 先将点击的item项的显示对象移掉
				oThisLeftList.getObjHtml().removeChild(tempArr[i].getObjHtml());
			}
			
			// 将移掉的此项创建到另一侧
			//oThisRightList.createOption(tempArr[i].caption, tempArr[i].value,
			//		"", false, "");
			var optionComp = oThisRightList.createOption(tempArr[i].caption, tempArr[i].value,
					"", false, "");
			// 增加双击事件
			optionComp.Div_gen.ondblclick = function() {
				oThis.oneToLeftFunc();
			}
		}
	};
	/*--------------------------------------
		点击右边的一个项后将右边的打到左边
	 *--------------------------------------*/
	// this.oneToLeft = new ButtonComp(this.backGroundPanel, "oneToLeft",
	// buttLeft, listTop + ListToListComp.BUTTSHEIGHT + buttInternal,
	// ListToListComp.BUTTSWIDTH, ListToListComp.BUTTSHEIGHT, ml_toleft, "", "",
	// "", "");
	this.oneToLeft = new ImageComp(this.backGroundPanel, "oneToLeft",
			window.themePath + "/images/listtolist/toleftnormal.gif", buttLeft,
			buttBaseTop + 2 * ListToListComp.BUTTSHEIGHT + 2 * buttInternal,
			"", "", trans("ml_toleft"), window.themePath
					+ "/images/listtolist/tolefthot.gif", "");
	this.oneToLeft.onclick = function() {
		oThis.oneToLeftFunc();
	};

	/*--------------------------------------
		将右边的items全部移到左边
	 *--------------------------------------*/
	// this.allToLeft = new ButtonComp(this.backGroundPanel, "allToLeft",
	// buttLeft, listTop + 3*ListToListComp.BUTTSHEIGHT + 3*buttInternal,
	// ListToListComp.BUTTSWIDTH, ListToListComp.BUTTSHEIGHT, ml_alltoleft, "",
	// "", "", "");
	this.allToLeft = new ImageComp(this.backGroundPanel, "allToLeft",
			window.themePath + "/images/listtolist/alltoleftnormal.gif",
			buttLeft, buttBaseTop + 3 * ListToListComp.BUTTSHEIGHT + 3
					* buttInternal, "", "", trans("ml_alltoleft"),
			window.themePath + "/images/listtolist/alltolefthot.gif", "");
	this.allToLeft.onclick = function() {
		// 右侧list中没有items了,则不能再向左移动
		if (oThisRightList.allItems == null
				|| oThisRightList.allItems.length == 0) {
			return;
		}

		// 让items引用oThisRightList.allItems对象
		var items = oThisRightList.allItems;
		if (items == null || items.length == 0) {
			return;
		}

		var temp = new Array();

		for ( var i = 0; i < items.length; i++) {
			temp.push(items[i]);
		}
		// 将allItems置空
		oThisRightList.allItems = null;
		for ( var i = 0; i < temp.length; i++) {
			// 先将右侧全部item项的显示对象移掉
			oThisRightList.getObjHtml().removeChild(temp[i].getObjHtml());
			if (!oThis.isLeftTree) {
				
				// 将移掉的此项创建到另一侧
				//oThisLeftList.createOption(temp[i].caption, temp[i].value, "",
				//		false, "");
				var optionComp = oThisLeftList.createOption(temp[i].caption, temp[i].value, "",
						false, "");
				// 增加双击事件
				optionComp.Div_gen.ondblclick = function() {
					oThis.oneToRightFunc();
				}
			}
		}
	};

	// 所有最右边按钮的left值
	rightButtonsLeft = cw;

	/*--------------------------------------
		将选中项向上移动一个位置
	 *--------------------------------------*/
	// this.toTop = new ButtonComp(this.backGroundPanel, "toTop",
	// rightButtonsLeft, listTop, ListToListComp.BUTTSWIDTH, "20", ml_totop, "",
	// "", "", "")
	this.toTop = new ImageComp(this.backGroundPanel, "toTop", window.themePath
			+ "/images/listtolist/totopnormal.gif", rightButtonsLeft,
			buttBaseTop + ListToListComp.BUTTSHEIGHT + buttInternal, "", "",
			trans("ml_totop"), window.themePath
					+ "/images/listtolist/totophot.gif", "");
	this.toTop.onclick = function() {
		if (oThis.changeValues == null || oThis.changeValues.length < 1) {
			return;
		}

		// 若同时选中多个
		if (oThis.changeValues.length > 1) {

		} else if (oThis.changeValues.length == 1) {
			var posi;
			// 用于标志是当前选中值在leftlist(用0标志)还是在rightlist(用1标志)中
			var flag;

			if (oThisLeftList.allItems != null
					&& oThisLeftList.allItems.indexOf(oThis.changeValues[0]) != -1) {
				posi = oThisLeftList.allItems.indexOf(oThis.changeValues[0]);
				flag = 0;
			} else if (oThisRightList.allItems != null
					&& oThisRightList.allItems.indexOf(oThis.changeValues[0]) != -1) {
				posi = oThisRightList.allItems.indexOf(oThis.changeValues[0]);
				flag = 1;
			}

			if (posi == 0) {
				return;
			} else {
				if (flag == 0) {
					oThisLeftList.changeItemShowPosition(posi, posi - 1);
				} else if (flag == 1) {
					oThisRightList.changeItemShowPosition(posi, posi - 1);
				}
			}
		}
	};

	/*--------------------------------------
		将选中项向下移动一个位置
	 *--------------------------------------*/
	// this.toBottom = new ButtonComp(this.backGroundPanel, "toBottom",
	// rightButtonsLeft, listTop + ListToListComp.BUTTSHEIGHT + buttInternal,
	// ListToListComp.BUTTSWIDTH, "20", ml_tobottom, "", "", "", "");
	this.toBottom = new ImageComp(this.backGroundPanel, "toBottom",
			window.themePath + "/images/listtolist/tobottomnormal.gif",
			rightButtonsLeft, buttBaseTop + 2 * ListToListComp.BUTTSHEIGHT + 2
					* buttInternal, "", "", trans("ml_tobottom"),
			window.themePath + "/images/listtolist/tobottomhot.gif", "");
	this.toBottom.onclick = function() {
		if (oThis.changeValues == null || oThis.changeValues.length < 1) {
			return;
		}

		// 若同时选中多个
		if (oThis.changeValues.length > 1) {

		} else if (oThis.changeValues.length == 1) {
			var posi;
			// 用于标志是当前选中值在leftlist(用0标志)还是在rightlist(用1标志)中
			var flag;

			if (oThisLeftList.allItems != null
					&& oThisLeftList.allItems.indexOf(oThis.changeValues[0]) != -1) {
				posi = oThisLeftList.allItems.indexOf(oThis.changeValues[0]);
				flag = 0;
			} else if (oThisRightList.allItems != null
					&& oThisRightList.allItems.indexOf(oThis.changeValues[0]) != -1) {
				posi = oThisRightList.allItems.indexOf(oThis.changeValues[0]);
				flag = 1;
			}

			// 当前选中项在leftlist中
			if (flag == 0) {
				if (posi == oThisLeftList.allItems.length - 1) {
					return;
				} else {
					oThisLeftList.changeItemShowPosition(posi, posi + 1);
				}
			}
			// 当前选中项在rightlist中
			else if (flag == 1) {
				if (posi == oThisRightList.allItems.length - 1) {
					return;
				} else {
					oThisRightList.changeItemShowPosition(posi, posi + 1);
				}
			}
		}
	};

	/*--------------------------------------
		将选中项移动到最上方
	 *--------------------------------------*/
	// this.toMostTop = new ButtonComp(this.backGroundPanel, "toMostTop",
	// rightButtonsLeft, listTop + 2*ListToListComp.BUTTSHEIGHT +
	// 2*buttInternal, ListToListComp.BUTTSWIDTH, "20", ml_toppest, "", "", "",
	// "");
	this.toMostTop = new ImageComp(this.backGroundPanel, "toMostTop",
			window.themePath + "/images/listtolist/alltotopnormal.gif",
			rightButtonsLeft, buttBaseTop, "", "", trans("ml_toppest"),
			window.themePath + "/images/listtolist/alltotophot.gif", "");
	this.toMostTop.onclick = function() {
		if (oThis.changeValues == null || oThis.changeValues.length < 1) {
			return;
		}
		// 若同时选中多个
		if (oThis.changeValues.length > 1) {
		} else if (oThis.changeValues.length == 1) {
			var posi;
			// 用于标志是当前选中值在leftlist(用0标志)还是在rightlist(用1标志)中
			var flag;

			if (oThisLeftList.allItems != null
					&& oThisLeftList.allItems.indexOf(oThis.changeValues[0]) != -1) {
				posi = oThisLeftList.allItems.indexOf(oThis.changeValues[0]);
				flag = 0;
			} else if (oThisRightList.allItems != null
					&& oThisRightList.allItems.indexOf(oThis.changeValues[0]) != -1) {
				posi = oThisRightList.allItems.indexOf(oThis.changeValues[0]);
				flag = 1;
			}

			if (flag == 0) {
				oThisLeftList.changeItemShowPosition(posi, 0);
			} else if (flag == 1) {
				oThisRightList.changeItemShowPosition(posi, 0);
			}
		}
	};

	/*--------------------------------------
		将选中项移动到最下方
	 *--------------------------------------*/
	// this.toMostBottom = new ButtonComp(this.backGroundPanel, "toMostBottom",
	// rightButtonsLeft, listTop + 3*ListToListComp.BUTTSHEIGHT +
	// 3*buttInternal, ListToListComp.BUTTSWIDTH, "20", ml_bottomest, "", "",
	// "", "");
	this.toMostBottom = new ImageComp(this.backGroundPanel, "toMostBottom",
			window.themePath + "/images/listtolist/alltobottomnormal.gif",
			rightButtonsLeft, buttBaseTop + 3 * ListToListComp.BUTTSHEIGHT + 3
					* buttInternal, "", "", trans("ml_bottomest"),
			window.themePath + "/images/listtolist/alltobottomhot.gif", "");
	this.toMostBottom.onclick = function() {
		if (oThis.changeValues == null && oThis.changeValues.length < 1) {
			return;
		}

		// 若同时选中多个
		if (oThis.changeValues.length > 1) {

		} else if (oThis.changeValues.length == 1) {
			var posi;
			// 用于标志是当前选中值在leftlist(用0标志)还是在rightlist(用1标志)中
			var flag;

			if (oThisLeftList.allItems != null
					&& oThisLeftList.allItems.indexOf(oThis.changeValues[0]) != -1) {
				posi = oThisLeftList.allItems.indexOf(oThis.changeValues[0]);
				flag = 0;
			} else if (oThisRightList.allItems != null
					&& oThisRightList.allItems.indexOf(oThis.changeValues[0]) != -1) {
				posi = oThisRightList.allItems.indexOf(oThis.changeValues[0]);
				flag = 1;
			}

			// 当前选中项在leftlist中
			if (flag == 0) {
				oThisLeftList.changeItemShowPosition(posi,
						oThisLeftList.allItems.length - 1);
			}
			// 当前选中项在rightlist中
			else if (flag == 1) {
				oThisRightList.changeItemShowPosition(posi,
						oThisRightList.allItems.length - 1);
			}
		}
	};
};

/**
 * 将一项从左边打到右边
 * @author guoweic 2009-11-9
 * @private
 */
ListToListComp.prototype.oneToRightFunc = function() {
	var oThis = this;
	var oThisRightList = this.rightList;
	var oThisLeftList = this.leftList;
	if (this.changeValues == null || this.changeValues.length < 1) {
		return;
	}
	// 当前选中项在右侧,则不能再向右移动
	if (oThisRightList.allItems != null) {
		if (!this.isLeftTree) {
			if (oThisRightList.allItems.indexOf(this.changeValues[0]) != -1)
				return;
		} else {
			var b = oThisRightList.isExistInList(this.changeValues[0].value);
			if (b)
				return;
		}
	}
	for ( var i = 0; i < this.changeValues.length; i++) {
		// 如果是左树，则不删除左边的显示对象
		if (!this.isLeftTree) {
			// 先将点击的item项的显示对象移掉
			oThisLeftList.getObjHtml().removeChild(
					this.changeValues[i].getObjHtml());
			// 得到要删掉的元素在listLeft中的位置
			var posi = oThisLeftList.getAllItems().indexOf(
					this.changeValues[i]);
			// 删除掉listLeft数组中的数据对象
			oThisLeftList.getAllItems().splice(posi, 1);
		}
		// 将移掉的此项创建到另一侧
		var optionComp = oThisRightList.createOption(this.changeValues[i].caption,
				this.changeValues[i].value, "", false, "");
		// 增加双击事件
		optionComp.Div_gen.ondblclick = function() {
			oThis.oneToLeftFunc();
		}
	}
	// 删除掉当前可用数组中的所有值
	this.changeValues = null;
};

/**
 * 将一项从右边打到左边
 * @author guoweic 2009-11-9
 * @private
 */
ListToListComp.prototype.oneToLeftFunc = function() {
	var oThis = this;
	var oThisRightList = this.rightList;
	var oThisLeftList = this.leftList;
	if (this.changeValues == null || this.changeValues.length < 1) {
		return;
	}
	if (!this.isLeftTree) {
		// 当前选中项在左侧,则不能再向左移动
		if (oThisLeftList.allItems != null) {
			if (oThisLeftList.allItems.indexOf(this.changeValues[0]) != -1)
				return;
		}
	}

	for ( var i = 0; i < this.changeValues.length; i++) {
		// 先将点击的item项的显示对象移掉
		oThisRightList.getObjHtml().removeChild(
				this.changeValues[i].getObjHtml());
		// 得到要删掉的元素在rightList中的位置
		var posi = oThisRightList.getAllItems().indexOf(
				this.changeValues[i]);
		// 删除掉rightList数组中的数据对象
		oThisRightList.getAllItems().splice(posi, 1);
		if (!this.isLeftTree) {
			// 将移掉的此项创建到另一侧
			var optionComp = oThisLeftList.createOption(this.changeValues[i].caption,
					this.changeValues[i].value, "", false, "");
			// 增加双击事件
			optionComp.Div_gen.ondblclick = function() {
				oThis.oneToRightFunc();
			}
		}
	}
	// 删除掉当前可用数组中的所有值
	this.changeValues = null;
};

/**
 * 返回左树的Filter，根据这个来判断是否将左树移向右列表 根据传入的树节点返回布尔值
 * @private
 * 
 */
ListToListComp.prototype.getTreeNodeFilter = function(treeNode) {
	return true;
};

/**
 * 返回树节点和创建有列表项之间的关系 [option,value]
 * @private
 */
ListToListComp.prototype.getNodeOptionMap = function() {
	return null;
};

/**
 * 向左侧list中添加值
 * 
 * @param value 此item的id,必须和其他item不同
 * @param index 索引值,可以不输入
 * @private
 */
ListToListComp.prototype.addItemToLeft = function(caption, value, refImg,
		selected, index) {
	caption = getString(caption, "default");

	if (value == null)
		return;

	var optionComp = this.leftList.createOption(caption, value, "", false, "");
	var oThis = this;
	// 增加双击事件
	optionComp.Div_gen.ondblclick = function() {
		oThis.oneToRightFunc();
	}
};

/**
 * 向右侧list中添加值
 * @private
 */
ListToListComp.prototype.addItemToRight = function(caption, value, refImg,
		selected, index) {
	caption = getString(caption, "default");

	if (value == null)
		return;

	var optionComp = this.rightList.createOption(caption, value, "", false, "");
	var oThis = this;
	// 增加双击事件
	optionComp.Div_gen.ondblclick = function() {
		oThis.oneToLeftFunc();
	}
};

/**
 * 删除左侧list中的item
 * 
 * @param index 要删除的item的索引值(注意:索引值从0开始)
 * @private
 */
ListToListComp.prototype.deleteLeftListItem = function(index) {
	if (index == null || index < 0 || index > this.leftList.allItems.length - 1)
		return;
	this.leftList.deleteOptionItem(index);
};

/**
 * 删除左侧list中的多个item
 * 
 * @param indice 要删除的items的索引值数组(注意:索引值从0开始)
 * @private
 */
ListToListComp.prototype.deleteLeftListItems = function(indice) {
	if (indice == null || indice.length == 0)
		return;
	else if (indice.length == 1)
		this.deleteLeftListItem(indice[0]);
	else
		this.leftList.deleteOptionItems(indice);
};

/**
 * 删除右侧list中的item
 * 
 * @param index 要删除的item的索引值(注意:索引值从0开始)
 * @private
 */
ListToListComp.prototype.deleteRightListItem = function(index) {
	if (index == null || index < 0
			|| index > this.rightList.allItems.length - 1)
		return;
	this.rightList.deleteOptionItem(index);
};

/**
 * 删除右侧list中的多个item
 * 
 * @param indice 要删除的items的索引值数组(注意:索引值从0开始)
 * @private
 */
ListToListComp.prototype.deleteRightListItems = function(indice) {
	if (indice == null || indice.length == 0)
		return;
	else if (indice.length == 1)
		this.deleteRightListItem(indice[0]);
	else
		this.rightList.deleteOptionItems(indice);
};
