/**
 * @fileoverview 下拉框组件,支持文字子项和图片子项两种形式.
 * @author gd, dengjt, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 */

ComboComp.prototype = new TextComp;
ComboComp.prototype.componentType = "COMBOBOX";
ComboComp.dataDivDefaultHeight = 80;
// 如果没有指定数据区的高度,此属性表示数据区默认显示的option数目,超过这个数目数据区将出现滚动条
ComboComp.defaultVisibleOptionsNum = 10;

/*add by chouhl 2012-1-10*/
ComboComp.prototype.inputClassName_init = IS_IE7? "text_input" : "input_normal_center_bg text_input";
ComboComp.prototype.inputClassName_inactive = IS_IE7? "text_input_inactive" : "input_normal_center_bg text_input_inactive";
//按钮图片宽高
ComboComp.prototype.imageWidth = 7;
ComboComp.prototype.imageHeight = 5;

ComboComp.pushImgNMPath = window.themePath + "/ui/ctrl/text/images/combo_nm.png";
ComboComp.pushImgONPath = window.themePath + "/ui/ctrl/text/images/combo_on.png";
ComboComp.pushImgDisablePath = window.themePath + "/ui/ctrl/text/images/combo_disable.png";

/**
 * 下拉框控件构造函数
 * @class 下拉框类，此下拉框支持文字子项和图片子项的形式。
 * 
 * @constructor combocomp的构造函数
 * @param parent 下拉框控件的父控件
 * @param name 此控件的名字,是对此控件的引用
 * @param left 控件左部x坐标
 * @param top 控件顶部y坐标
 * @param width 控件宽度
 * @param position 控件的定位属性
 * @param dataDivHeight 下拉框的高度
 * @param readonly 输入框是否为只读的
 * @param selectOnly 在readOnly为false的情况下,selectOnly为true表示此下拉框是仅选择的,只能从下拉框中选择值;
 *                   为false表示允许在输入框输入值,如果输入值在options中会自动补全用户输入,如果不在会保留该值
 * @param attrArr 属性对象
 * @param className css文件的名字
 */
function ComboComp(parent, name, left, top, width, position, selectOnly,
		attrArr, className) {
	this.base = TextComp;
	// 保存所有的option子项
	this.options = new Array();
	// 保存当前选中的值索引
	this.selectedIndex = -1;
	// 保存上次选中的旧值索引
	this.lastSelectedIndex = -1;
	// 将此combo控件放入clickHolder,注册相应的动作事件
	window.clickHolders.push(this);
	// 仅显示图片时标志位
	this.showImgOnly = false;
	this.selectOnly = getBoolean(selectOnly, true);
	// 已经显示的option（已调整宽度）
	this.shownOptions = new Array();
	// 是否允许存在下拉数据之外的值
	this.allowExtendValue = false;
	//记录最近未找到下拉数据的值
	this.notFindComboDataValue = "";
	
	if (attrArr != null) {
		// 如果设置了数据区的高度,则用给定的高度值,超出部分出现滚动条,否则ComboComp.defaultVisibleOptionsNum个Item之内不出滚动条
		if (attrArr.dataDivHeight != null)
			this.dataDivHeight = parseInt(attrArr.dataDivHeight);
		if (attrArr.allowExtendValue == true)
			this.allowExtendValue = true;
	}
	this.base(parent, name, left, top, width, "A", position, attrArr, className);
};

/**
 * combocomp回调函数
 * @private
 */
ComboComp.prototype.manageSelf = function() {
	TextComp.prototype.manageSelf.call(this);
	// 保存数据对象
	var oThis = this;
	var width = this.Div_text.offsetWidth;
	if (this.Div_text.className == "text_div"){
		//width = width - 2;
//		if (IS_FF)	
//			width = width - 5;
	}
//	if (!(IS_IE7 || IS_FF || IS_CHROME))
//		width = width - 2;
	//ipad需要再减10	
	if (IS_IPAD){
		width = width - 10;
	}
	if (width == 0)
		width = this.width.replace("px","");
	var inputWidth = width - this.imageWidth;
	if(IS_IE)
		inputWidth -= 3;
	this.input.style.width = inputWidth - 2 + "px";//2 按钮图片右间距
	// 此comboComp不可编辑
	if (this.readOnly == true) {
		this.input.readOnly = true;
	}
	// 此comboComp可编辑(根据selectOnly分为两种情况)

	// 仅下拉(输入框不允许输入)
	if (this.selectOnly == true){
		this.input.readOnly = true;
	}

	// 输入框失去焦点时的动作
	this.input.onblur = function(e) {
		e = EventUtil.getEvent();
		// IE 下阻止失去焦点
		//TODO guoweic
		if (IS_IE) {
			if (document.activeElement && (document.activeElement.id == (oThis.id + "comb_sel_button")
					|| document.activeElement.parentNode.id == (oThis.id + "comb_data_div"))) {
				stopEvent(e);
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
				return false;
			}
		}
		/*add by chouhl 2012-1-10*/
		if(oThis.Div_text.children.length == 3){
			var children = oThis.Div_text.children;
			for(var i=0;i<children.length;i++){
				children[i].className = children[i].className.replaceStr('input_highlight','input_normal');
			}
			oThis.input.className = oThis.input.className.replaceStr('input_highlight_center_bg','input_normal_center_bg');
		}
		/********/
		// 调用程序内部自己的处理方法
		oThis.blur(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	// 由于特殊性覆盖TextComp基类中的此方法,点击向上,向下方向键选择上一个或者下一个option
	this.input.onkeydown = function(e) {
		e = EventUtil.getEvent();
		var ch = e.charCode;
		// 调用用户的方法
		if (oThis.onkeydown(e) == false) {
			stopAll(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}
		// 处理键盘的"上移","下移"事件
		var currIndex = oThis.getSelectedIndex();
		var nextIndex = -1;
		// 下移
		if (ch == 40) {
			if (!oThis.isShown) {
				oThis.showData();
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
				return;
			}
			if (currIndex < oThis.options.length - 1) {
				nextIndex = currIndex + 1;
				while (nextIndex < oThis.options.length && oThis.options[nextIndex].Div_gen.style.display == "none")
					nextIndex++;
				if (nextIndex < oThis.options.length)
					oThis.setSelectedItem(nextIndex);
				else if (nextIndex == oThis.options.length)  // 选中当前行
					oThis.setSelectedItem(currIndex);
			} else if (currIndex == oThis.options.length - 1)  // 选中当前行
				oThis.setSelectedItem(currIndex);
			// 调用用户重载的方法
			oThis.valueChanged(nextIndex == -1 ? null : oThis.options[nextIndex].value,
					currIndex == -1 ? null : oThis.options[currIndex].value);
		}
		// 上移
		else if (ch == 38) {
			if (currIndex > 0) {
				nextIndex = currIndex - 1;
				while (nextIndex >= 0 && oThis.options[nextIndex].Div_gen.style.display == "none")
					nextIndex--;
				if (nextIndex >= 0)
					oThis.setSelectedItem(nextIndex);
			}
			// 调用用户重载的方法
			oThis.valueChanged(nextIndex == -1 ? null : oThis.options[nextIndex].value,
					currIndex == -1 ? null : oThis.options[currIndex].value);
		} else if ((ch == 9 && e.shiftKey) || ch == 9 || ch == 13) {  // Tab或Shift+Tab或Enter，隐藏选项
			oThis.hideData();
		}
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	this.input.onkeyup = function(e) {
		e = EventUtil.getEvent();
		var ch = e.charCode;
		if (!oThis.selectOnly && !oThis.showImgOnly && !oThis.readOnly) {
			// 过滤
			if (ch != 38 && ch != 40 && ch != 37 && ch != 39 && ch != 9 && ch != 13) {
				oThis.isKeyPressed = true;
				// 自动打开选框
				if (!oThis.isShown && !(ch == 32 && e.ctrlKey) && ch != 20 && ch != 16 && ch != 17 && ch != 18 && ch != 33 && ch != 34 && ch != 91) {
					if (ComboComp.showDataDivTimeOut != null)
						clearTimeout(ComboComp.showDataDivTimeOut);
					ComboComp.showDataDivTimeOut = setTimeout("ComboComp.showDataDiv('" + oThis.id + "');", 300);
				}
				// 过滤
				oThis.doFilter();
			}
		}
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	//得到键盘焦点时调用
	this.input.onfocus = function (e) { 
		e = EventUtil.getEvent();
		/*add by chouhl 2012-1-10*/
		if(oThis.Div_text.children.length == 3){
			var children = oThis.Div_text.children;
			for(var i=0;i<children.length;i++){
				children[i].className = children[i].className.replaceStr('input_normal','input_highlight');
			}
			oThis.input.className = oThis.input.className.replaceStr('input_normal_center_bg','input_highlight_center_bg');
		}
		/********/
		oThis.focus(e);
		oThis.hideTip();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	// 创建显示数据区的点击按钮
	this.divButton = $ce("DIV");
	/*modify by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		this.Div_text.children[1].appendChild(this.divButton);
	}else{
		this.Div_text.appendChild(this.divButton);
	}
	/********/
	//if (IS_IE7){
	//	this.divButton.style.position = "absolute";
	//	this.divButton.style.left = width - 18 + "px";
	//}
	//else{
		this.divButton.style.position = "relative";
		this.input.style.position = "relative";
		//this.divButton.style.left = "0px";
		if (IS_IE){
			this.input.style.styleFloat = "left";
			this.divButton.style.styleFloat = "right";
		}
		else{
			this.input.style.cssFloat = "left";
			this.divButton.style.cssFloat = "right";
		}
	//}	
	this.divButton.style.width = this.imageWidth + "px";
	this.divButton.id = this.id + "comb_sel_button";
	this.divButton.style.marginRight = "2px";

	this.pushImg = $ce("IMG");
	this.pushImg.src = ComboComp.pushImgNMPath;
	this.pushImg.style[ATTRFLOAT] = "right";
	//this.pushImg.style.marginRight = "1px";
	this.pushImg.style.height = this.imageHeight + "px";
	this.pushImg.style.width = this.imageWidth + "px";
	this.divButton.appendChild(this.pushImg);
	// 设置图片的竖直位置聚中
	var offHgt = this.Div_text.offsetHeight;
	/*modify by chouhl 2012-1-10*/
	if (offHgt > 0 && (offHgt - this.pushImg.height) > 0) {
		this.pushImg.style.marginTop = "1px";
	}
	if(this.Div_text.children.length == 3){
		if(this.Div_text.children[1].offsetHeight > 0 && this.pushImg.height > 0 && (this.Div_text.children[1].offsetHeight - this.pushImg.height) > 0){
			this.pushImg.style.marginTop = (this.Div_text.children[1].offsetHeight - this.pushImg.height)/2 + "px";
		}
	}
	/********/
	/*add by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		var centerWidth = width - 3*2;//3*2左右边框图片宽度
		this.Div_text.children[1].style.width = centerWidth + "px";
		var imgWidth = (this.Div_text.children[1].children.length - 1) * this.imageWidth;//与input输入框同一个DIV中图片的总宽度
		var inputWidth = centerWidth - imgWidth;
		if(IS_IE){
			inputWidth -= 3;
		}
		this.input.style.width = inputWidth - 2 + "px";//2 按钮图片右间距
	}
	
	/********/
	if (this.readOnly != true) {
		// 点击下拉按扭后的动作
		this.divButton.onclick = function(e) {
			e = EventUtil.getEvent();
			oThis.click(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
		};

		// 鼠标移动到按钮上时的动作
		this.divButton.onmouseover = function(e) {
			e = EventUtil.getEvent();
			oThis.pushImg.src = ComboComp.pushImgONPath;
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);

		};

		// 鼠标离开按钮时的动作
		this.divButton.onmouseout = function(e) {
			e = EventUtil.getEvent();
			oThis.pushImg.src = ComboComp.pushImgNMPath;
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
		};
	} else {
		this.Div_text.className = this.className + " " + this.className + "_readonly";
		this.divButton.style.visibility="hidden";
	}

	// 创建显示数据的下拉区
	this.dataDiv = $ce("DIV");
	this.dataDiv.id = this.id + "comb_data_div";
	this.dataDiv.className = "combobox_data_div";
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。
//	this.dataDiv.style.zIndex = BaseComponent.STANDARD_ZINDEX + 1000000000;
	this.dataDiv.style.zIndex = getZIndex();
	document.body.appendChild(this.dataDiv);
	if (this.width.toString().indexOf("%") == -1 && this.width.toString().indexOf("px") == -1)
		this.dataDiv.style.width = this.width + "px";
	else 
		this.dataDiv.style.width = this.width;
	this.dataDiv.style.overflow = "auto";
	this.dataDiv.style.display = "none";
	this.dataDiv.style.position = "absolute";

	// 初始设置禁用
	if (this.disabled == true) {
		this.divButtonClickFunc = this.divButton.onclick;
		this.divButtonMouseOutFuc = this.divButton.onmouseout;
		this.divButtonMouseOverFuc = this.divButton.onmouseover;
		this.divButton.onclick = function() {
		};
		this.divButton.onmouseout = function() {
		};
		this.divButton.onmouseover = function() {
		};
		this.pushImg.src = ComboComp.pushImgDisablePath;
	}
	
	// guoweic: 阻止IE中滚轮的事件传播 
	this.dataDiv.onmousewheel = function(e) {
		e = EventUtil.getEvent();
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	}
};

/**
 * 聚焦后执行方法
 * @private
 */
ComboComp.prototype.focus = function(e) {
	this.oldValue = this.getValue();
	// 为避免tab键进入密码框时不能输入字符的bug
	if (this.visible == true) {
		this.isKeyPressed = false;
		this.onfocus(e);
	}
};

/**
 * 设置只读状态
 */
ComboComp.prototype.setReadOnly = function(readOnly) {
//	if (this.readOnly == readOnly)
//		return;
//	if (this.disabled) {
//		this.setActive(true);
//	}
	if (this.selectOnly == true && readOnly == false) {
		this.input.readOnly	= true;
	} 
	else{
		this.input.readOnly = readOnly;
	}
	this.readOnly=readOnly;
	if (readOnly) {
//		this.Div_text.style.borderWidth="0";
		this.Div_text.className = this.className + " " + this.className + "_readonly";
		this.input.className = this.inputClassName_init + " text_input_readonly";//"text_input text_input_readonly";
//		this.pushImg.style.visibility="hidden";
		this.divButton.style.visibility="hidden";
	} else {
//		this.Div_text.style.borderWidth="1px";
		this.Div_text.className = this.className;
		this.input.className = this.inputClassName_init;//"text_input";
//		this.pushImg.style.visibility="";
		this.divButton.style.visibility="";
	}
};

/**
 * 根据用户输入在输入框失去焦点时自动帮助用户补全剩余部分,如果用户输入的值在options中没 有则不设置该值.
 * 
 * @param private 私有方法,程序内部方法.
 * @private
 */
ComboComp.prototype.blur = function(e) {
	// 整体可以编辑
	if (this.readOnly == false) {
		// 仅对文字的下拉框有用
		if (this.showImgOnly == false && this.selectOnly == false) {
			var inputCaption = this.input.value;
			var options = this.getOptions();
			// 用户输入值为空则置空
			if (inputCaption == null || inputCaption.trim() == "")
				this.setNullValue(true);
			else {
				// 完全匹配则设置值或者按照最先匹配原则帮助用户设置值
				for ( var i = 0; i < options.length; i++) {
					if (options[i].caption == inputCaption
							|| (options[i].caption).startWith(inputCaption)) {
						this.setSelectedItem(i);
						break;
					} else {
						if (this.allowExtendValue)  // 允许存在范围外的值
							this.selectedIndex = -1;
						else  // 不允许存在范围外的值
							this.setNullValue(true);
					}
				}
			}
			this.isKeyPressed = false;
		}
	}
	if (this.visible == true) {
		this.setMessage(this.input.value);
	}
	// 调用用户的处理方法
	this.onblur(e);
};

/**
 * 由option组件调用这个函数
 * 
 * @param item 被点击的option的数据对象
 * @private
 */
ComboComp.prototype.itemclick = function(item) {
	// 只有选则项和选中的项不相同时才调用valueChanged()
	if (item.index != this.selectedIndex) {
//		// 仅显示图片的combox
//		if (item.showImgOnly == true)
//			this.input.style.background = "url(" + item.caption
//					+ ") no-repeat center";
//		// 显示文字的combox
//		else
//			this.input.value = item.caption;
//		this.lastSelectedIndex = this.selectedIndex;
//		if (this.lastSelectedIndex != -1)
//			lastItem = this.options[this.lastSelectedIndex];
//		else
//			lastItem = null;
//		this.selectedIndex = item.index;
//		this.hideData();
//		// 调用用户重载的方法
//		this.valueChanged(item, lastItem);
		// 设置选中项
		this.setSelectedItem(item.index);
	}
	this.hideData();
	this.setFocus();
};

/**
 * 创建option子项
 * 
 * @param caption 子项的显示值
 * @param value 子项的真实值
 * @param refImg showImgOnly为false时,表示要显示在文字前面的图片的绝对路径
 * @param selected 设置是否为初始选中项
 * @param index 索引值,一般初始时不用指定
 * @param showImgOnly 是否为只显示图片的combox类型
 */
ComboComp.prototype.createOption = function(caption, value, refImg, selected,
		index, showImgOnly) {
	if (index == null || index == -1) {
		var option = new OptionComp(caption, value, refImg, selected,
				showImgOnly);
		var index = this.options.push(option);
		option.setParentOwner(this);
		option.setIndex(index - 1);
		this.dataDiv.appendChild(option.Div_gen);
		if (selected)
			this.setSelectedItem(index - 1);
//		else
//			this.setSelectedItem(0);
	}
};

/**
 * 重写父类的setBounds方法
 * 
 * @param dataDivHeight 下拉数据区的高度
 */
ComboComp.prototype.setBounds = function(left, top, width, height) {
	this.left = left;
	this.top = top;
	this.width = getString(width, this.Div_gen.offsetWidth);
	this.height = getString(height, this.Div_gen.offsetHeight);
	// 设置最外层的大小
	this.Div_gen.style.left = left + "px";
	this.Div_gen.style.top = top + "px";
	this.Div_gen.style.width = this.width + "px";
	this.Div_gen.style.height = this.height + "px";
	
	// 设置输入区域的大小
	this.Div_text.style.width = this.width - 4 + "px";
	if (this.hasLabel)
		this.Div_text.style.width = this.width - this.labelWidth - 10 + "px";
	//this.Div_text.style.height = this.height - 4 + "px";

	var pixelHeight = this.Div_text.offsetHeight;
	var pixelWidth = this.Div_text.offsetWidth;
//	if (this.Div_text.className == "text_div" || this.Div_text.className == "text_div text_div_focus_bgcolor"){
//		pixelWidth = pixelWidth - 2;
//		if (IS_FF)	
//			pixelWidth = pixelWidth - 5;
//	}
//	if (!(IS_IE7 || IS_FF || IS_CHROME))
//		pixelWidth = pixelWidth - 2;
	//ipad需要再减10	
	if (IS_IPAD){
		pixelWidth = pixelWidth - 10;
	}
	this.input.style.width = pixelWidth - this.imageWidth + "px";
	//if (IS_IE7)
		//this.divButton.style.left = pWidth - 18 + "px";
	/*add by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		var centerWidth = pixelWidth - 3*2;//3*2左右边框图片宽度
		this.Div_text.children[1].style.width = centerWidth + "px";
		var imgWidth = (this.Div_text.children[1].children.length - 1) * this.imageWidth;//与input输入框同一个DIV中图片的总宽度
		var inputWidth = centerWidth - imgWidth;
		if(IS_IE){
			inputWidth -= 3;
		}
		this.input.style.width = inputWidth - 2 + "px";//2 按钮图片右间距
	}
	/********/
};

/**
 * 显示控件(显示属性是visibility)
 */
ComboComp.prototype.showV = function() {
	var obj = this.getObjHtml();
	obj.style.visibility = "";
	this.visible = true;
	// 设置图片的竖直位置聚中
	var offHgt = this.Div_text.offsetHeight;
	if (offHgt > 0 && (offHgt - this.pushImg.height) > 0) {
		this.pushImg.style.marginTop = "1px";
	}
	if(this.Div_text.children.length == 3){
		if(this.Div_text.children[1].offsetHeight > 0 && this.pushImg.height > 0 && (this.Div_text.children[1].offsetHeight - this.pushImg.height) > 0){
			this.pushImg.style.marginTop = (this.Div_text.children[1].offsetHeight - this.pushImg.height)/2 + "px";
		}
	}
};

/**
 * 隐藏控件(显示属性是visibility)
 */
ComboComp.prototype.hideV = function() {
	BaseComponent.prototype.hideV.call(this);
	this.hideData();
};

/**
 * combobox控件点击事件
 * 
 * @private
 */
ComboComp.prototype.click = function(e) {
	if (!this.isShown) {
		// 凡是要截断事件气泡的控件。必需要调用document.onclick方法。调用document的click方法。
		e = EventUtil.getEvent();
		this.showData();
		this.isShown = true;
	} else {
		this.hideData();
		this.isShown = false;
	}
	
	if (this.stopHideDiv != true) {
		window.clickHolders.trigger = this;
		document.onclick();
	}
	stopEvent(e);
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 显示选项DIV(setTimeOut)
 */
ComboComp.showDataDiv = function(id) {
	var combo = window.objects[id];
	if (combo != null)
		combo.showData();
};

/**
 * 显示选项DIV
 */
ComboComp.prototype.showData = function() {
	if (!this.isShown) {
		this.dataDiv.style.left = compOffsetLeft(this.Div_text) + "px";
		this.dataDiv.style.top = (compOffsetTop(this.Div_text) + this.Div_text.offsetHeight) + "px";
		this.dataDiv.style.zIndex = getZIndex(); 
		// 数据区宽度和输入框宽度要相等
		this.dataDiv.style.width = this.Div_text.offsetWidth + "px";
		if (this.Div_text.offsetWidth == 0) {
			if (this.width.toString().indexOf("%") == -1 && this.width.toString().indexOf("px") == -1)
				this.dataDiv.style.width = this.width + "px";
			else 
				this.dataDiv.style.width = this.width;
		}
	
		if (this.dataDivHeight != null)
			this.dataDiv.style.height = this.dataDivHeight + "px";
		else {
			if (this.options.length <= ComboComp.defaultVisibleOptionsNum)
				this.dataDiv.style.height = (this.options.length
						* (OptionComp.ITEM_HEIGHT + 5)) + "px";
			else
				this.dataDiv.style.height = ComboComp.dataDivDefaultHeight + "px";
		}
	
		this.dataDiv.style.display = "block";
		// 将控件放在视线内
		positionElementInView(this.dataDiv);
		
		//TODO IE6 和 IE7在option的div中出现竖向滚动条时，其option项div宽度计算有问题，会相应出现横向滚动条
		if (IS_IE6 || IS_IE7) {
			for (var i = 0, n = this.options.length; i < n; i++) {
				if (this.shownOptions.indexOf(this.options[i]) == -1) {  // 该选项没有被显示过
					this.options[i].Div_gen.style.width = this.options[i].Div_gen.offsetWidth - BaseComponent.SCROLLWIDTH + "px";
					this.shownOptions.push(this.options[i]);
				}
			}
		}
		this.isShown = true;
		if (!this.selectOnly && !this.showImgOnly && !this.readOnly) {
			if (this.isKeyPressed) {  // 如果打开之前输入了内容，则进行过滤
				this.doFilter();
			} else {  // 打开之前没有输入内容，显示全部选项
				for (var i = 0, n = this.options.length; i < n; i++) {
					this.options[i].Div_gen.style.display = "block";
				}
			}
		}

		// 重新设置所有选项的选中样式
		this.resetSelStyle();
	}
};

/**
 * 得到所有的option项
 */
ComboComp.prototype.getOptions = function() {
	if (this.options != null)
		return this.options;
	else
		return null;
};

/**
 * 清除所有的option
 */
ComboComp.prototype.clearOptions = function() {
	if (this.options == null)
		return;
	this.dataDiv.innerHTML = "";

	this.options = null;
	this.options = new Array();
	this.selectedIndex = -1;
	this.input.value = "";
	this.shownOptions.clear();
};

/**
 * 清除参数指定的option
 * 
 * @param value option的真实值
 */
ComboComp.prototype.clearOption = function(value) {
	if (value == null || value == "")
		return;

	var options = this.options;
	if (options != null && options.length > 0) {
		for ( var i = 0; i < options.length; i++) {
			if (options[i].value == value) {
				this.dataDiv.removeChild(options[i].Div_gen);
				this.options.splice(i, 0, 1);
				this.shownOptions.removeEle(options[i]);
				return;
			}
		}
	}
};

/**
 * 设值绑定数据
 */
ComboComp.prototype.setComboData = function(comboData) {
	var oldValue = (this.getValue()==null && this.notFindComboDataValue != "")?this.notFindComboDataValue:this.getValue();
	this.clearOptions();
	if (!comboData)
		return;
	this.comboData = comboData;
	var nameArr = comboData.getNameArray();
	var valueArr = comboData.getValueArray();
	var imageArr = comboData.getImageArray();
	if (nameArr != null) {
		for ( var i = 0; i < nameArr.length; i++) {
			var selected = false;
			this.createOption(nameArr[i], valueArr[i], imageArr[i], selected, -1,
					this.showImgOnly);
		}
	}
	//如果oldValue本来就是空值，不需要再设置一遍空值
	if (oldValue != null)
		this.setValue(oldValue);
};

/**
 * 设值提示信息
 */
ComboComp.prototype.setTitle = function(title) {
	if (!this.isError) {
		if (title != null && title != "") {
			var titleName;
			if (this.comboData)
				titleName = this.comboData.getNameByValue(title);
			if (titleName != null && titleName != "") {
				this.Div_gen.title = titleName;
				if (this.input!=null)
					this.input.title = titleName;
			} else {
				this.Div_gen.title = title;
				if (this.input!=null)
					this.input.title = title;
			}
		}
	} 
	else {
		if (title != null && title != "") {
			if (this.input!=null)
				this.input.title = title;
			this.Div_gen.title = title;
		} else if (title == "") {
			if (this.input!=null)
				this.input.title = "";
			this.Div_gen.title = "";
		}
	}
};

/**
 * 根据输入内容过滤选择条件
 */
ComboComp.prototype.doFilter = function() {
	if (this.isShown) {
		var inputCaption = this.input.value;
		if (inputCaption == "") {
			for (var i = 0, n = this.options.length; i < n; i++) {
				this.options[i].Div_gen.style.display = "block";
			}
		} else {
			for (var i = 0, n = this.options.length; i < n; i++) {
				if (this.options[i].caption == inputCaption 
						|| (this.options[i].caption).startWith(inputCaption)) {
					this.options[i].Div_gen.style.display = "block";
				} else {
					this.options[i].Div_gen.style.display = "none";
				}
			}
		}
	}
};

/**
 * 设置选中项
 * 
 * @param index 要设置选中项的索引值,index小于0表示不设置任何option为选中状态
 */
ComboComp.prototype.setSelectedItem = function(index) {
	index = parseInt(index);
	if (isNaN(index) || index > this.options.length)
		return;

	// index小于0表示不设置任何option为选中状态
	if (index < 0) {
		this.setNullValue(true);
		return;
	}

	var oldSelectedIndex = this.selectedIndex;
	var option = this.options[index];
	if(this.selectedIndex == option.index && this.input.value == option.caption)
		return;
	this.selectedIndex = option.index;

	// 仅显示图片的combo类型
	if (option.showImgOnly == true) {
		this.input.style.backgroundImage = "url(" + option.caption + ")";
		this.input.style.backgroundRepeat = "no-repeat";
		this.input.style.backgroundPosition = "center";
		this.input.className = this.inputClassName_init;//"text_input";
		if (this.disabled == true)
			this.input.className = this.inputClassName_init + " text_inactive_bgcolor";//"text_input text_inactive_bgcolor";
	}
	// 仅显示文字的combo类型
	else {
		this.input.value = option.caption;
		this.oldValue = option.value;
		if (this.disabled == true) {
			this.input.className = this.inputClassName_inactive;//"text_input_inactive";
			this.Div_text.className = this.className + " text_inactive_bgcolor";
		} else {
			this.input.className = this.inputClassName_init;//"text_input";
			this.Div_text.className = this.className;
		}
	}
	
	// 重新设置所有选项的选中样式
	if (this.isShown)
		this.resetSelStyle();

	// 选中项改变调用用户重载的方法
	if (index != oldSelectedIndex)
		this.valueChanged(option == null ? null : option.value, 
				this.options[oldSelectedIndex] == null ? null : this.options[oldSelectedIndex].value);
};

/**
 * 重新设置所有选项的选中样式
 * @private
 */
ComboComp.prototype.resetSelStyle = function() {
	for (var i = 0, n = this.options.length; i < n; i++) {
		if (this.selectedIndex == i)
			this.options[i].setSelStyle();
		else
			this.options[i].setUnSelStyle();
	}
};

/**
 * 设置value,如果value不是任何一个options的value,则设置为空
 * 
 * @param value 要设置的值
 */
ComboComp.prototype.setValue = function(value) {
	if (value == null) {
		this.setNullValue(true);
		return;
	}
	if (this.readOnly == true) {
		if (this.setValueByOption(value) != true){
			this.notFindComboDataValue = value;
			//如果下拉项为空，不触发valueChange
			if (this.options.length > 0)
				this.setNullValue(true);
			else	
				this.setNullValue(false);
		}
	} 
	else {
		if (this.selectOnly == true) {
			if (this.setValueByOption(value) != true){
				this.notFindComboDataValue = value;
				//如果下拉项为空，不触发valueChange
				if (this.options.length > 0)
					this.setNullValue(true);
				else	
					this.setNullValue(false);
			}
		} 
		else{
//			if (value == null) {
//				this.setNullValue();
//				return;
//			}
			if (this.setValueByOption(value) != true){
				this.input.value = value;
				this.notFindComboDataValue = "";
			}
		}
	}
};

/**
 * 设置空值,即不设置任何option选中
 * 
 * param {} isValueChanged 是否触发 valueChange事件
 * 
 * @private
 */
ComboComp.prototype.setNullValue = function(isValueChanged) {
	var oldValue = this.getValue();
	this.selectedIndex = -1;
	if (this.input.value != ""){
		this.input.value = "";
	}
	if (isValueChanged == true)
		this.valueChanged(null, oldValue);
};

/**
 * @private
 */
ComboComp.prototype.setValueByOption = function(value) {
	for ( var i = 0; i < this.options.length; i++) {
		if (value == this.options[i].value) {
			this.setSelectedItem(i);
			this.notFindComboDataValue = "";
			return true;
		}
	}
};

/**
 * 得到选择项的value值
 */
ComboComp.prototype.getValue = function() {
	return this.getSelectedValue();
};

/**
 * 设置下拉框为只显示图片的下拉框
 * 
 * @param show true为仅显示图片
 */
ComboComp.prototype.setShowImgOnly = function(show) {
	this.showImgOnly = show;
};

/**
 * 得到指定value的索引值
 * 
 * @param value 指定的value
 * @private
 */
ComboComp.prototype.getValueIndex = function(value) {
	if (value != null) {
		for ( var i = 0; i < this.options.length; i++) {
			if (this.options[i].value == value)
				return i;
		}
	}
	return -1;
};

/**
 * 得到选中的option的index值
 */
ComboComp.prototype.getSelectedIndex = function() {
	return this.selectedIndex;
};

/**
 * 得到选中的option的真实值 <strong>注意：</strong>
 * 该方法得到的是真实值而非显示值(输入框中的值)。但对于readOnly为false，并且selectOnly也为false的情况返回用户在输入框中输入的值。
 */
ComboComp.prototype.getSelectedValue = function() {
	if (this.readOnly == true) {
		if (this.selectedIndex == -1)
			return null;
		return this.options[this.selectedIndex].value;
	} 
	else {
		if (this.selectOnly == true) {
			if (this.selectedIndex == -1)
				return null;
			return this.options[this.selectedIndex].value;
		} 
		else {
			if (this.selectedIndex == -1) {
				if (this.input.value == "")
					return null;
				else
					return this.input.value;
			}
			return this.options[this.selectedIndex].value;
		}
	}
};

/**
 * 得到选择的option的caption
 */
ComboComp.prototype.getSelectedCaption = function() {
	if (this.selectedIndex == -1)
		return null;
	return this.options[this.selectedIndex].caption;
};

/**
 * 外部鼠标滚轮滑动时的回掉函数.当滑动鼠标滚轮时隐藏掉datadiv
 * @private
 */
ComboComp.prototype.outsideMouseWheelClick = function() {
	this.hideData();
};

/**
 * 外部右键点击事件发生时的回调函数.用来隐藏下拉框数据部分
 * @private
 */
ComboComp.prototype.outsideContextMenuClick = function() {
	this.hideData();
};

/**
 * 外部点击事件发生时的回调函数。用来隐藏下拉框数据部分
 * @private
 */
ComboComp.prototype.outsideClick = function() {
	if (window.clickHolders.trigger == this)
		return;
	this.hideData();
};

/**
 * 设置此输入框控件的激活状态
 * 
 * @param isActive true表示处于激活状态,否则表示禁用状态
 */
ComboComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	//清除只读状态
	//modify by licza 2010-11-12
//	if(this.readOnly) 
//		this.setReadOnly(false); 
	
	// 控件处于激活状态变为非激活状态
	if (this.disabled == false && isActive == false) {
		TextComp.prototype.setActive.call(this, false);
		// 将与下拉按钮的各种动作事件保存
		this.divButtonClickFunc = this.divButton.onclick;
		this.divButtonMouseOutFuc = this.divButton.onmouseout;
		this.divButtonMouseOverFuc = this.divButton.onmouseover;
		this.divButton.onclick = function() {
		};
		this.divButton.onmouseout = function() {
		};
		this.divButton.onmouseover = function() {
		};
		this.pushImg.src = ComboComp.pushImgDisablePath;
	}
	// 控件处于禁用状态变为激活状态
	else if (this.disabled == true && isActive == true) {
		TextComp.prototype.setActive.call(this, true);
		this.divButton.onclick = this.divButtonClickFunc;
		this.divButton.onmouseout = this.divButtonMouseOutFuc;
		this.divButton.onmouseover = this.divButtonMouseOverFuc;
		this.pushImg.src = ComboComp.pushImgNMPath;
	}
};

/**
 * 隐藏data list
 */
ComboComp.prototype.hideData = function() {
	if (this.dataDiv) {
		this.dataDiv.style.display = "none";
		this.isShown = false;
	}
};

/**
 * 暴露给用户重载的方法,当combo的选中值改变时会调用该方法
 * 
 * @param newItem 新选中的option对象
 * @param lastItem 上一个选中的option对象
 * @private
 */
ComboComp.prototype.valueChanged = function(newValue, oldValue) {
	var valueChangeEvent = {
			"obj" : this,
			"newValue" : newValue,
			"oldValue" : oldValue
		};
	this.doEventFunc("valueChanged", TextListener.listenerType, valueChangeEvent);
};

/**
 * 获取对象信息
 * @private
 */
ComboComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.ComboBoxContext";
	context.c = "ComboBoxContext";
	context.id = this.id;
	context.enabled = !this.disabled;
	context.value = this.getValue();
	context.visible = this.visible;
	context.readOnly = this.readOnly;
	return context;
};

/**
 * 设置对象信息
 * @private
 */
ComboComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	if (context.readOnly != null && this.readOnly != context.readOnly)
		this.setReadOnly(context.readOnly);
	if (context.visible != this.visible) {
		if (context.visible)
			this.showV();
		else
			this.hideV();
	}
	if(!context.isform)
		this.setValue(context.value);
};