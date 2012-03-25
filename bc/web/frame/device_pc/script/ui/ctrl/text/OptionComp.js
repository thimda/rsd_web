/**
 * @fileoverview Combobox和List选项类.
 * 不能被外部直接调用,而要用父控件的createOption方法间接调用,
 * 因为父控件要附加一些信息.
 *	
 * @author gd, dengjt, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 *
 */
OptionComp.DEFAULT_VALUE = "DEFAULT";
OptionComp.prototype.componentType = "OPTION";

OptionComp.ITEM_HEIGHT = 19;
OptionComp.IMAGE_WIDTH = 19;

/**
 * 选项控件构造函数
 * @class 选项控件
 * @param caption 显示值(showImgOnly为true时,capion为要显示图片的绝对路径)
 * @param value 真实值
 * @param refImg 显示图片的绝对路径
 * @param selected 初始是否选中
 * @param showImgOnly 此项若为true表示此option仅显示图片,此时caption值代表图片路径
 */
function OptionComp(caption, value, refImg, selected, showImgOnly) {
	var oThis = this;
	this.caption = getString(caption, "");
	this.value = value;
	this.selected = getBoolean(selected, false);
	this.refImg = refImg;
	this.showImgOnly = getBoolean(showImgOnly, false);
	
	// 每个option项的总体div
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.value;
	this.Div_gen.title = this.caption;
	this.Div_gen.style.width = "100%";
	this.Div_gen.style.height = OptionComp.ITEM_HEIGHT + "px";
	this.Div_gen.style.overflow = "hidden";
	this.Div_gen.style.position = "relative";	// 此控件定位属性为相对
	this.Div_gen.className = "option_unsel";
	this.index = -1;
	
	// 处理仅需显示图片的情况
	if (this.showImgOnly == true) {
		var img = "<img src='" + this.caption + "' width='16px' height='16px'/>";
		var table = "<table width='100%' height='" + OptionComp.ITEM_HEIGHT + "px'><tr><td align='center' valign='middle'>" + img +"</td></tr></table>";
		this.Div_gen.innerHTML = table;
		
		var oThis = this;
		// 鼠标移动到子项上的事件处理
		this.Div_gen.onmouseover = function() {
			if (typeof(ComboComp) != 'undefined' && ComboComp.prototype.isPrototypeOf(oThis.parentOwner)) {
				var options = oThis.parentOwner.options;
				for (var i = 0, n = options.length; i < n; i++) {
					if (options[i] == oThis)
						options[i].setSelStyle();
					else
						options[i].setUnSelStyle();
				}
			} else
				oThis.setSelStyle();
		};
		
		// 鼠标移开子项的事件处理	
		this.Div_gen.onmouseout = function() {
			if (ComboComp.prototype.isPrototypeOf(oThis.parentOwner)) {
				oThis.setUnSelStyle();
			}
		};
			
		// 处理点击了子控件的动作
		this.Div_gen.onclick = function(e) {
			e = EventUtil.getEvent();
					
			if (!oThis.parentOwner)
				alert("parent container is null, may be \r\nthis item is not create through method\r\n\"ComboComp.prototype.createOption\"");
				
			// combox的item子项被点击
			if (ComboComp.prototype.isPrototypeOf(oThis.parentOwner)) {
				oThis.parentOwner.itemclick(oThis);
			}
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
		};
	}
	// 显示文字的情况
	else {
		//创建显示在文字前的图片
		if (isNotNull(this.refImg, false))  {
			this.divImg = $ce("DIV");
			this.divImg.style.position = "absolute";
			this.divImg.style.left =  "0px";
			this.divImg.style.width = OptionComp.IMAGE_WIDTH - 4 + "px";
			this.divImg.style.height = OptionComp.IMAGE_WIDTH - 4 + "px";
			
			this.img = $ce("IMG");
			this.img.src = window.themePath + this.refImg;
			this.img.style.width = OptionComp.IMAGE_WIDTH - 4 + "px";
			this.img.style.height = OptionComp.IMAGE_WIDTH - 4 + "px";
			this.divImg.appendChild(this.img);
			this.Div_gen.appendChild(this.divImg);
		}
		
		// 创建显示文字的div
		this.divCaption = $ce("DIV");
		this.divCaption.style.position = "absolute";
		// 以下三行代码用来处理文本在一行上的截断显示,不允许换行
		this.divCaption.style.whiteSpace = "nowrap";
		//TODO guoweic
		this.divCaption.style.textOverflow = "ellipsis";
		this.divCaption.style.overflow = "hidden";
		if (isNotNull(this.refImg, false))
			this.divCaption.style.left = OptionComp.ITEM_HEIGHT + "px";
		else
			this.divCaption.style.left = "3px";
		this.divCaption.appendChild(document.createTextNode(this.caption));
		this.Div_gen.appendChild(this.divCaption);
		
		// 鼠标移动到子项上的事件处理
		this.Div_gen.onmouseover = function() {
			if (typeof(ComboComp) != 'undefined' && ComboComp.prototype.isPrototypeOf(oThis.parentOwner)) {
				var options = oThis.parentOwner.options;
				for (var i = 0, n = options.length; i < n; i++) {
					if (options[i] == oThis)
						options[i].setSelStyle();
					else
						options[i].setUnSelStyle();
				}
			} else
				oThis.setSelStyle();
		};
		
		// 鼠标移开子项的事件处理	
		this.Div_gen.onmouseout = function() {
			// list和listToList控件外观的变换
			if (typeof(ListComp) != 'undefined' && ListComp.prototype.isPrototypeOf(oThis.parentOwner)) {
				if (oThis.parentOwner.selectedItems != null && (oThis.parentOwner.selectedItems).indexOf(oThis) != -1)
					this.className = "option_click";
				else
					oThis.setUnSelStyle();
			} else if (typeof(ComboComp) != 'undefined' && ComboComp.prototype.isPrototypeOf(oThis.parentOwner)) {
				oThis.setUnSelStyle();
			}
		};
		
		// 处理点击了子控件的动作
		this.Div_gen.onclick = function(e) {
			e = EventUtil.getEvent();
				
			if (!oThis.parentOwner)
				alert("parent container is null, may be \r\nthis item is not create through method\r\n\"ComboComp.prototype.createOption\"");
			
			// combox的item子项被点击
			if (ComboComp.prototype.isPrototypeOf(oThis.parentOwner)) {
				oThis.parentOwner.itemclick(oThis);
			}
			// list的item子项被点击
			else {
				// 如果是ctrl按钮按下,则处理多选
				if (oThis.parentOwner.multiSel) {	
					if (e.ctrlKey)
						oThis.parentOwner.addItemSelected(oThis);
					else
						oThis.parentOwner.setItemSelected(oThis);
				} else {
					var ds = oThis.parentOwner.dataset;
					// 绑定ds
					if (ds != null) {
						ds.setRowSelected(ds.getRowIndex(oThis.optionData));	
					}
					else
						oThis.parentOwner.setItemSelected(oThis);
				}	
			}
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
		};
		
		//处理双击了子控件的动作
		this.Div_gen.ondblclick = function() {	
		};
	}
};

/**
 * 设置选中时样式
 * @private
 */
OptionComp.prototype.setSelStyle = function() {
	this.Div_gen.className = "option_sel";
};

/**
 * 设置非选中时样式
 * @private
 */
OptionComp.prototype.setUnSelStyle = function() {
	this.Div_gen.className = "option_unsel";
};


/**
 * 设置optioncomp的index值
 * @param private 在ComboComp.createOption()方法中内部调用了此方法
 * @private
 */
OptionComp.prototype.setIndex = function(index) {
	this.index = index;
};

/**
 * 设置所属父控件
 * @private
 */
OptionComp.prototype.setParentOwner = function(parentOwner) {
	this.parentOwner = parentOwner;
};

/**
 * 得到option的显示对象
 */
OptionComp.prototype.getObjHtml = function() {
	return this.Div_gen;
};