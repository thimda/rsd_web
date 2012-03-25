/**
 * @fileoverview 参照控件,此参照控件走WebFrame参照方式
 * 
 * @author dengjt,gd
 * @author lkp 增加用户输入匹配数据功能
 * @author guoweic
 * @version NC6.0
 * @since   NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 */
ReferenceTextComp.prototype = new TextComp;
ReferenceTextComp.prototype.componentType = "REFERENCETEXT";

//参照对话框标识
ReferenceTextComp.DIALOG_INDEX = 0;

/*add by chouhl 2012-1-10*/
ReferenceTextComp.prototype.inputClassName_init = IS_IE7? "text_input" : "input_normal_center_bg text_input";
ReferenceTextComp.prototype.inputClassName_inactive = IS_IE7? "text_input_inactive" : "input_normal_center_bg text_input_inactive";
//按钮图片宽高
ReferenceTextComp.prototype.imageWidth = 17;
ReferenceTextComp.prototype.imageHeight = 17;

//TODO 暂时写在这里
window.PAGEWIDTH = 1500;
window.PAGEHEIGHT = 1500;
/**
 * 参照输入框构造函数
 * @class 参照输入框
 */
function ReferenceTextComp(parent, name, left, top, width, position, attrArr, nodeInfo, className, isDialog) {
	this.refCodeName = null;
	this.refType = ReferenceTextComp.GRIDTREE;
	this.showValue = null;
	this.trueValue = null;
	TextComp.call(this, parent, name, left, top, width, "R", position, attrArr, className);
	this.nodeInfo = nodeInfo;
	if(this.nodeInfo != null)
		nodeInfo.bindReference(this);

	//默认以弹出框打开，设为为false的话，已下拉框打开
	this.isDialog = isDialog == false ? false : ((this.nodeInfo != null && this.nodeInfo.isDialog == false) ? false : true);
	
	//todo默认以dialog打开参照
	//this.isDialog = true;
	
	this.refIndex = "ref_" + (ReferenceTextComp.DIALOG_INDEX ++);
	this.refresh = false;
	
	this.dialogWidth = 700;
	if(this.dialogWidth >= window.PAGEWIDTH)
		this.dialogWidth = window.PAGEWIDTH - 100;
	this.dialogHeight = 400;
	if(this.dialogWidth >= window.PAGEHEIGHT)
		this.dialogHeight = window.PAGEHEIGHT - 100;
	
	this.divWidth = 550;
	this.divHeight = 350;
	
	// 是否每次打开参照刷新,默认不刷新
	this.refreshRefPage = false;
//	if (attrArr != null && attrArr.refreshRefPage) {
//		this.refreshRefPage = getBoolean(attrArr.refreshRefPage, false);
//	}
	if (this.nodeInfo != null && this.nodeInfo.refreshRefPage) {
		this.refreshRefPage = getBoolean(this.nodeInfo.refreshRefPage, false);
	}
	
	//利用此属性表示当前是用户手工输入还是通过参照框选择后输入的。
	//1:手工输入；2:通过参照框传入
	this.inputType = null;
	
	this.datasetId = null;
	this.field = null;
	// 注册外部回掉函数
	window.clickHolders.push(this);
};


/**
 * 二级回掉函数
 * @private
 */
ReferenceTextComp.prototype.manageSelf = function() {
	var oThis = this;
	TextComp.prototype.manageSelf.call(this); 
	
	//如果父容器目前不可见，此width取传入值。暂时不考虑百分比情况
	var width = this.Div_text.offsetWidth;
	if (this.Div_text.className == "text_div"){
		width = width - 2;
		if (IS_FF)	
			width = width - 5;
	}
	if (!(IS_IE7 || IS_FF || IS_CHROME))
		width = width - 2;
	//ipad需要再减10	
	if (IS_IPAD){
		width = width - 10;
	}
	if (width == 0)
		width = this.width.replace("px","");
	this.input.style.width = (width - this.imageWidth) + "px";
	this.divButton = $ce("DIV");
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
			this.divButton.style.styleFloat = "left";
		}
		else{
			this.input.style.cssFloat = "left";
			this.divButton.style.cssFloat = "left";
		}
	//}		
	this.divButton.style.width = this.imageWidth + "px";
	this.divButton.id = "ref_sel_button";
	
	this.refImg = $ce("IMG");
	this.refImg.src = window.themePath + '/images/text/ref_nm.png';
	this.refImg.style[ATTRFLOAT] = "right";
	//this.refImg.style.marginRight = "0px";
	this.refImg.style.height = this.imageHeight + "px";
	this.refImg.style.width = this.imageWidth + "px";
	this.divButton.appendChild(this.refImg);
	/*modify by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		this.Div_text.children[1].appendChild(this.divButton);
	}else{
		this.Div_text.appendChild(this.divButton);
	}
	/********/
	/*add by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		var centerWidth = width - 3*2;//3*2左右边框图片宽度
		this.Div_text.children[1].style.width = centerWidth + "px";
		var imgWidth = (this.Div_text.children[1].children.length - 1) * this.imageWidth;//与input输入框同一个DIV中图片的总宽度
		this.input.style.width = (centerWidth - 2*2 - imgWidth) + "px";//2*2 input输入框距离左右间距
	}
	/********/
	// 设置图片的竖直位置居中
	var offHgt = this.Div_text.offsetHeight;
	if (offHgt > 0 && (offHgt - this.refImg.height) > 0){
		this.refImg.style.marginTop = "1px";	
	}
	/*add by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		if(this.Div_text.children[1].offsetHeight > 0 && this.refImg.height > 0 && (this.Div_text.children[1].offsetHeight - this.refImg.height) > 0){
			this.refImg.style.marginTop = (this.Div_text.children[1].offsetHeight - this.refImg.height)/2 + "px";
		}
	}
	/********/
	// 获得焦点时，打开Div
//	var focusListener = new FocusListener();
//	focusListener.onFocus = function(focusEvent) {
//		var e = focusEvent.event;
//		var obj = focusEvent.obj;
//		e.triggerObj = obj;
//		if (obj.isDialog == false) {
//			obj.openRefDiv(e, obj.refreshRefPage);
//		}
//		if (this.stopHideDiv != true) {
//			window.clickHolders.trigger = obj;
//			document.onclick();
//		}
//		stopAll(e);
//	};
//	this.addListener(focusListener);
	
	this.divButton.onclick = function(e) {
		e = EventUtil.getEvent();
		e.triggerObj = oThis;
		//TODO 调用用户的方法,若用户返回false则不能弹出参照对话框
		if (oThis.onclick() != false) {
			oThis.needRef = true;
			var pageWidth = document.body.offsetWidth;
			var pageHeight = document.body.offsetHeight;
			if (oThis.isDialog != false || oThis.divWidth > pageWidth || oThis.divHeight > pageHeight) {
				oThis.focus(e);
				oThis.input.onblur();
//				oThis.input.focus(e);
				oThis.openRefDialog(e, oThis.refreshRefPage || oThis.refresh);
			} else {
				if (oThis.divIsShown)
					oThis.hideRefDiv();
				else
					oThis.openRefDiv(e);
			}
		}
		
		if (this.stopHideDiv != true) {
			window.clickHolders.trigger = oThis;
			document.onclick();
		}
		stopAll(e);
		// 删除事件对象（用于清除依赖关系）
//		clearEvent(e);
		e.triggerObj = null;
		clearEventSimply(e);
	};
	
	this.divButton.onmouseover = function(e) {
		if (!this.disabled)	
			oThis.refImg.src = window.themePath + "/images/text/ref_on.png";
	};
	
	this.divButton.onmouseout = function(e) {
		if(!this.disabled)	
			oThis.refImg.src = window.themePath + "/images/text/ref_nm.png";
	};
	
//	//如果直接敲入key值，则将value设置为null
//	this.input.onkeydown = function(e) {
//		e = EventUtil.getEvent();
//		oThis.inputType = 1;
//		
//		// readOnly时不允许输入
//		if (this.readOnly == true) {
//			stopAll(e);
//			// 删除事件对象（用于清除依赖关系）
//			clearEventSimply(e);
//			return false;
//		}	
//				
//		// 调用用户的方法	
//		if (oThis.onkeydown(e) == false) {
//			stopAll(e);
//			// 删除事件对象（用于清除依赖关系）
//			clearEventSimply(e);
//			return;
//		}	
//		var keyCode = e.keyCode ? e.keyCode : e.charCode ? e.charCode : e.which ? e.which : void 0;	
//		if (keyCode == 8 || keyCode == 46)
//			oThis.haschanged(e);
//		// 删除事件对象（用于清除依赖关系）
//		clearEventSimply(e);
//	};
   
	
	//得到键盘焦点时调用
	this.input.onfocus = function (e) {
		if(this.readOnly)
			return;
		e = EventUtil.getEvent();
		oThis.oldValue = oThis.getValue();
		if(oThis.trueValue != null)
			oThis.setValue(oThis.trueValue);
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
		oThis.needRef = true;
		oThis.hideTip();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	//失去键盘焦点时调用
	this.input.onblur = function (e) {
		if(this.readOnly)
			return;
		e = EventUtil.getEvent();
		/*add by chouhl 2012-1-10*/
		if(oThis.Div_text.children.length == 3){
			var children = oThis.Div_text.children;
			for(var i=0;i<children.length;i++){
				children[i].className = children[i].className.replaceStr('input_highlight','input_normal');
			}
			oThis.input.className = oThis.input.className.replaceStr('input_highlight_center_bg','input_normal_center_bg');
		}
		/********/
//		if(oThis.timeoutFilterFunc)
//			clearTimeout(oThis.timeoutFilterFunc);
//		// 失去焦点时如果当前值和oldValue不等,则执行向后台发请求逻辑 
//		if (oThis.oldValue != this.value) {
//			oThis.innerProcessEnter();
//		}
//		if (!oThis.isDialog && oThis.divIsShown == true)
//			oThis.hideRefDiv();
		oThis.onblur(e);
		oThis.needRef = false;
		oThis.showTip();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	//初始控件为禁用状态
   	if (this.disabled) {
		this.input.disabled = true;
		this.input.className = this.inputClassName_inactive;//"text_input_inactive";
		/*
		if(!IS_IE7){
			this.input.className = "text_form_div_normal_center_bg " + this.input.className;
			this.input.style.marginTop = "0px";
		}else{
			this.input.style.marginTop = "5px";
		}
		*/
		this.Div_text.className = this.className + " text_inactive_bgcolor";
		this.refImg.src = window.themePath + "/images/text/ref_disable.png";
		//将与下拉按钮的各种动作事件保存
		this.divButtonClickFunc = this.divButton.onclick;
		this.divButtonMouseOutFuc = this.divButton.onmouseout;
		this.divButtonMouseOverFuc = this.divButton.onmouseover;
		 
		this.divButton.onclick = null;
		this.divButton.onmouseout = null;
		this.divButton.onmouseover = null;
	}
   	
   	// 增加事件，进行输入条件后过滤
   	if (!this.isDialog) {  // Div类型
	   	var valueChangeListener = new KeyListener();
	   	valueChangeListener.onKeyUp = function(keyEvent) {
	   		var e = keyEvent.event;
	   		if ((e.keyCode == 9 && e.shiftKey) || e.keyCode == 9) {  // "tab"键和"shift+tab"键处理
	   			if (oThis.divIsShown == true) {
					// 隐藏参照DIV
	   				oThis.hideRefDiv();
	   			}
	   		}
	   		else if (e.keyCode != 40 && e.keyCode != 38 && e.keyCode != 13) {  // 不是上下箭头或回车
		   		var value = oThis.input.value;
	   			// 自动打开选框
				if (!oThis.divIsShown && !(e.keyCode == 32 && e.ctrlKey) && e.keyCode != 20 && e.keyCode != 16 && e.keyCode != 17 && e.keyCode != 18 && e.keyCode != 33 && e.keyCode != 34 && e.keyCode != 91) {
					if (oThis.showRefDivTimeOut != null)
						clearTimeout(oThis.showRefDivTimeOut);
					oThis.needRef = true;
					oThis.showRefDivTimeOut = setTimeout("ReferenceTextComp.showRefDivAndDoFilter('" + oThis.id + "','" + value + "','" + oThis.divFrameId + "');", 300);
				} else {
			   		if (oThis.timeoutFilterFunc)
			   			clearTimeout(oThis.timeoutFilterFunc);
			   		oThis.timeoutFilterFunc = setTimeout("ReferenceTextComp.doFilter('" + value + "','" + oThis.divFrameId + "'," + oThis.divIsShown + ");", 300);
				}
	   		}
	   	};
	   	this.addListener(valueChangeListener);
	   	
	   	// 点击上下箭头时，选择数据行
	   	var keyDownListener = new KeyListener();
	   	keyDownListener.onKeyDown = function(keyEvent) {
	   		var e = keyEvent.event;
	   		if (e.keyCode == 40 || e.keyCode == 38) {  // 上下箭头
	   			if (oThis.divIsShown != true)  // 打开Div
	   				ReferenceTextComp.doOpenRefDiv(e, oThis);
	   			else {
	   				window[oThis.divFrameId].contentWindow.selectRow(e.keyCode);
	   			}
	   		} else if (e.keyCode == 113) {  // F2键
	   			if (oThis.divIsShown != true)  // 打开Div
	   				ReferenceTextComp.doOpenRefDiv(e, oThis);
	   		}
	   	};
	   	keyDownListener.onEnter = function(keyEvent) {
	   		var e = keyEvent.event;
   			if (oThis.divIsShown == true) {
   				var result = ReferenceTextComp.doEnterFunc(e.keyCode, oThis.divFrameId);
   				if (result == true) {
	   				// 从参照DIV中选择的标志位，用于在焦点移出后进行判断
	   				oThis.isFromDiv = true;
   				} else {
					// 隐藏参照DIV
	   				oThis.hideRefDiv();
   				}
   			}
	   	};
	   	this.addListener(keyDownListener);
	   	
	   	var focusListener = new FocusListener();
	   	focusListener.onBlur = function(focusEvent) {
//	   		if (oThis.isFromDiv != true && !oThis.divIsShown) {  // 如果不是从参照DIV中选择的行，则进行校验和匹配
	   		if (oThis.isFromDiv != true) {  // 如果不是从参照DIV中选择的行，则进行校验和匹配
	   			var value = oThis.input.value;
	   			oThis.doBlurSearch(value);
	   		} else {
	   			oThis.isFromDiv = false;
	   		}
	   	};
	   	this.addListener(focusListener);
   	} else {  // 对话框类型
   		// 点击F2时，打开对话框
	   	var keyDownListener = new KeyListener();
	   	keyDownListener.onKeyDown = function(keyEvent) {
	   		var e = keyEvent.event;
	   		if (e.keyCode == 113) {  // F2键
	   			// 打开对话框
	   			oThis.openRefDialog(e, oThis.refreshRefPage || oThis.refresh);
	   		}
	   	};
	   	this.addListener(keyDownListener);
   	}
};

/**
 * 设置只读状态
 */
ReferenceTextComp.prototype.setReadOnly = function(readOnly) {
//	if(this.readOnly==readOnly)
//		return;
//	if(this.disabled){
//		this.setActive(true);
//	}
	this.input.readOnly = readOnly;
	this.readOnly=readOnly;
	if(readOnly){
//		this.Div_text.style.borderWidth="0";
		this.Div_text.className = this.className + " " + this.className + "_readonly";
		this.input.className = this.inputClassName_init + " text_input_readonly";//"text_input text_input_readonly";
//		this.refImg.style.visibility="hidden";
		this.divButton.style.visibility="hidden";
	}else{
//		this.Div_text.style.borderWidth="1px";
		this.Div_text.className = this.className;
		this.input.className = this.inputClassName_init;//"text_input";
		/*
		if(!IS_IE7){
			this.input.className = "text_form_div_normal_center_bg " + this.input.className;
			this.input.style.marginTop = "0px";
		}else{
			this.input.style.marginTop = "5px";
		}*/
//		this.refImg.style.visibility="";
		this.divButton.style.visibility="";
	}
};

/**
 * 若没打开DIV，焦点移出后的执行方法
 * @private
 */
ReferenceTextComp.prototype.doBlurSearch = function(value) {
	if(this.oldValue == value)
		return;
//	if (this.nodeInfo.allowExtendValue && this.datasetId == null) {  // 如果允许手工输入额外值，且没绑定ds，则直接设值
	if (this.nodeInfo.allowExtendValue) {  // 如果允许手工输入额外值，则直接设值
		this.showValue = value;
		this.setValue(value);
		this.setShowValue(value);
		return;
	}
	if (this.doBlurSearchFromCache(value) != true) {  // 从前台缓存中获取不到，则到后台执行
		var rt = this.doMatchRefPkService(value);
		if (this.nodeInfo.allowExtendValue) {  // 如果允许手工输入额外值
			if (this.datasetId != null) {  // 绑定了ds的情况
				this.setDsRowValue(value, rt);
				if ((rt[1] == "" || rt[1] == null) && value != null && value != "")
					this.showValue = value;
				if ((rt[0] == "" || rt[0] == null) && value != null && value != "")
					this.setValue(value);
				else
					this.setValue(rt[0]);
				if ((rt[1] == "" || rt[1] == null) && value != null && value != "")
					this.setShowValue(value);
				else
					this.setShowValue(rt[1]);
			}
		}
		else {  // 如果不允许手工输入额外值
			if (this.datasetId != null) {  // 绑定了ds的情况
				this.setDsRowValue(value, rt);
//				this.setValue(rt[0]);
//				this.setShowValue(rt[1]);
				this.setValue(rt[1]);
			}
			else {  // 没绑定ds，直接设值
				this.showValue = rt[1];
				this.setValue(rt[0]);
				this.setShowValue(rt[1]);
			}
		}
	}
};

/**
 * 调用后台服务，根据过滤条件查询符合条件的结果
 * @private
 */
ReferenceTextComp.prototype.doMatchRefPkService = function(value) {
	var rt = null;
	if(value == null || value == "")
		rt = new Array();
	else{
		var service = getFormularService();
		rt = service.execute('matchRefPk', "$S_" + value, getPageUniqueId(), this.widget.id, this.nodeInfo.id);
	}
	return rt;
};

/**
 * 为对应数据集的当前行设值
 * @private
 */
ReferenceTextComp.prototype.setDsRowValue = function(value, rt) {
	var ds = this.widget.getDataset(this.datasetId);
	var rowIndex = ds.getSelectedIndex();

	var writeFields = this.nodeInfo.writeFields;
	var emptyRow = ds.getEmptyRow();
	if (value == null || value == "") {  // 输入值为空的情况，清空关联字段的值
		for (var i = 0, n = writeFields.length; i < n; i++) {
			var index = ds.nameToIndex(writeFields[i]);
			ds.setValueAt(rowIndex, index, emptyRow.getCellValue(index));
		}
	} else {  // 设置关联字段的值
		for (var i = 0, n = writeFields.length; i < n; i++) {
			var index = ds.nameToIndex(writeFields[i]);
			var fieldValue = (rt[i + 4] == null || rt[i + 4] == "") ? emptyRow.getCellValue(index) : rt[i + 4];
			ds.setValueAt(rowIndex, index, fieldValue);
		}
	}
};

/**
 * 父字段值改变后，将本字段值置空
 */
ReferenceTextComp.prototype.clearValue = function() {
	var ds = this.widget.getDataset(this.datasetId);
	var index = ds.nameToIndex(this.field);
	var rowIndex = ds.getSelectedIndex();
	ds.setValueAt(rowIndex, index - 1, "");
	ds.setValueAt(rowIndex, index, "");
	this.setValue("");
	this.setRefresh(true);
};

/**
 * 从前台缓存中对输入内容进行校验和匹配
 * @private
 */
ReferenceTextComp.prototype.doBlurSearchFromCache = function(value) {
	//TODO
	return false;
};

/**
 * 显示选项DIV并且进行过滤(setTimeOut调用)
 * @private
 */
ReferenceTextComp.showRefDivAndDoFilter = function(id, value, frameId) {
	var refComp = window.objects[id];
	if (refComp != null){
		if(!refComp.needRef)
			return;
		ReferenceTextComp.doOpenRefDiv(null, refComp, true);
	}
};

/**
 * 执行打开Div方法
 * @private
 */
ReferenceTextComp.doOpenRefDiv = function (e, obj, doFilter) {
	if (e)
		e.triggerObj = obj;
	if (obj.isDialog == false) {
		obj.openRefDiv(e, doFilter);
	}
	if (this.stopHideDiv != true) {
		window.clickHolders.trigger = obj;
		document.onclick();
	}
	if (e)
		stopAll(e);
};

/**
 * 执行回车事件方法
 * @private
 */
ReferenceTextComp.doEnterFunc = function (keyCode, iframeId) {
	if (ReferenceTextComp.waitEnterRt != null)
		clearTimeout(ReferenceTextComp.waitEnterRt);
	// 判断Div是否完全打开
	if (window[iframeId] == null || window[iframeId].contentWindow == null || window[iframeId].contentWindow.renderDone == null) {
		ReferenceTextComp.waitEnterRt = ReferenceTextComp.waitFilterRt = setTimeout("ReferenceTextComp.doEnterFunc(" + keyCode + "," + iframeId + ");", 50);
		return;
	}
	return window[iframeId].contentWindow.selectRow(keyCode);
};

/**
 * 过滤
 * @private
 */
ReferenceTextComp.doFilter = function(value, frameId, doIt) {
	if (doIt == true){
		if (ReferenceTextComp.waitFilterRt != null)
			clearTimeout(ReferenceTextComp.waitFilterRt);
		// 判断Div是否完全打开
		if (window[frameId] == null ||window[frameId].contentWindow == null || window[frameId].contentWindow.renderDone == null) {
			ReferenceTextComp.waitFilterRt = setTimeout("ReferenceTextComp.doFilter('" + value + "','" + frameId + "'," + doIt + ");", 50);
			return;
		}
		window[frameId].contentWindow.doFilter(value);
	}
};

/**
 * 显示控件(显示属性是visibility)
 */
ReferenceTextComp.prototype.showV = function() {
	var obj = this.getObjHtml();
	obj.style.visibility = "";
	this.visible = true;
	// 设置图片的竖直位置居中
	var offHgt = this.Div_text.offsetHeight;
	if (offHgt > 0 && (offHgt - this.refImg.height) > 0){
		this.refImg.style.marginTop = "1px";	
	}
	if(this.Div_text.children.length == 3){
		if(this.Div_text.children[1].offsetHeight > 0 && this.refImg.height > 0 && (this.Div_text.children[1].offsetHeight - this.refImg.height) > 0){
			this.refImg.style.marginTop = (this.Div_text.children[1].offsetHeight - this.refImg.height)/2 + "px";
		}
	}
};

/**
 * 设置大小和位置
 */
ReferenceTextComp.prototype.setBounds = function(left, top, width, height) {
	this.left = left;
	this.top = top;
	this.width = getString(width, this.Div_gen.offsetWidth);
	this.height = getString(height, this.Div_gen.offsetHeight);
	
	// 设置最外层的大小
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width + "px";
	this.Div_gen.style.height = this.height + "px";

	// 设置输入区域的大小
	this.Div_text.style.width = this.width - 4 + "px";
	if (this.hasLabel)
		this.Div_text.style.width = this.width - this.labelWidth - 10 + "px";
	//this.Div_text.style.width = (parseInt(this.Div_text.style.width) - 32) + "px";
	//this.Div_text.style.height = this.height - 4 + "px";
	
	var pixelHeight = this.Div_text.offsetHeight;
	var pixelWidth = this.Div_text.offsetWidth;
	if (this.Div_text.className == "text_div" || this.Div_text.className == "text_div text_div_focus_bgcolor"){
		pixelWidth = pixelWidth - 2;
		if (IS_FF)	
			pixelWidth = pixelWidth - 5;
	}
	if (!(IS_IE7 || IS_FF || IS_CHROME))
		pixelWidth = pixelWidth - 2;
	//ipad需要再减10	
	if (IS_IPAD){
		pixelWidth = pixelWidth - 10;
	}
	this.input.style.width = (pixelWidth - this.imageWidth) + "px";//2*2 input距离左右间距 modify by chouhl 2012-1-10
	if (IS_IE7)
		//this.divButton.style.left = (pixelWidth - 18) + "px";
	/*add by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		var centerWidth = pixelWidth - 3*2;//3*2左右边框图片宽度
		this.Div_text.children[1].style.width = centerWidth + "px";
		var imgWidth = (this.Div_text.children[1].children.length - 1) * this.imageWidth;//与input输入框同一个DIV中图片的总宽度
		this.input.style.width = (centerWidth - 2*2 - imgWidth) + "px";//2*2 input输入框距离左右间距
		alert("2:" + this.input.style.width);
	}
	/********/
};

/**
 * 设置对话框宽度
 */
ReferenceTextComp.prototype.setDialogWidth = function(width) {
	this.dialogWidth = width;	
};

/**
 * 设置对话框高度
 */
ReferenceTextComp.prototype.setDialogHeight = function(height) {
	this.dialogHeight = height;
};

/**
 * 设置DIV宽度
 */
ReferenceTextComp.prototype.setDivWidth = function(width) {
	this.divWidth = width;	
};

/**
 * 设置DIV高度
 */
ReferenceTextComp.prototype.setDivHeight = function(height) {
	this.divHeight = height;
};

/**
 * 设置打开参照后是否刷新
 */
ReferenceTextComp.prototype.setRefresh = function(refresh) {
	this.refresh = refresh;
	this.refreshRefPage = refresh;
	if(window[this.divFrameId] != null) {
		window[this.divFrameId].src = "";
	}
};

/**
 * 设置过滤SQL语句
 */
ReferenceTextComp.prototype.setFilterSql = function(filterSql) {
	this.nodeInfo.filterSql = filterSql;
	this.refresh = true;
};

/**
 * 打开参照对话框
 * @private
 */
ReferenceTextComp.prototype.openRefDialog = function(e, isRefreshDialog) {
	if(this.nodeInfo instanceof SelfRefNodeInfo){
		var appUniqueId = getAppUniqueId();
		var url = this.nodeInfo.url;
		if(url.indexOf("?") == -1)
			url += "?";
		else
			url += "&";
		url = url +  "widgetId=" + this.widget.id + "&otherPageId=" + getPageId() + "&otherPageUniqueId=" + getPageUniqueId() + "&nodeId=" + this.nodeInfo.id + "&owner=" + this.id + "&appUniqueId=" +appUniqueId + "&isReference=true";
		showDialog(url, this.nodeInfo.name, this.dialogWidth, this.dialogHeight, this.refIndex, true, true, null);
	}
	else{
		var param = this.beforeOpenRefPage();
		if (param == false)
			return;
		if (typeof globalBeforeOpenRefDialog != "undefined")
			param = globalBeforeOpenRefDialog(this);
		if (param == false)
			return;
		//记录当前参照refnode的id
		if (this.nodeInfo.id != null)
			window.$nowRefNodeId = this.nodeInfo.id;
		var oThis = this;
		
		var url = null;
		if (window.appType != null && window.appType == "true"){
			var appUniqueId = getAppUniqueId();
			url = window.globalPath + "/app/" + window.appId + "/reference" + "?pageId=" +  encodeURIComponent(this.nodeInfo.pageMeta) + "&widgetId=" + this.widget.id + "&otherPageId=" + getPageId() + "&otherPageUniqueId=" + getPageUniqueId() + "&nodeId=" + this.nodeInfo.id + "&owner=" + this.id + "&appUniqueId=" +appUniqueId + "&isReference=true";
		}
		else{
			if(window.clientMode)
				url =  this.nodeInfo.pageMeta+this.widget.id+this.id+this.nodeInfo.id + ".html";
			else if(this.nodeInfo.pageMeta == null || this.nodeInfo.pageMeta == "null")
				url = window.corePath + "/" + this.nodeInfo.path + "&widgetId=" + this.widget.id + "&otherPageId=" + getPageId() + "&otherPageUniqueId=" + getPageUniqueId() + "&nodeId=" + this.nodeInfo.id + "&owner=" + this.id;
			else	
				url = window.corePath + "/" + this.nodeInfo.path + "?pageId=" +  encodeURIComponent(this.nodeInfo.pageMeta) + "&widgetId=" + this.widget.id + "&otherPageId=" + getPageId() + "&otherPageUniqueId=" + getPageUniqueId() + "&nodeId=" + this.nodeInfo.id + "&owner=" + this.id;
		}
	
			
			
	 	if (param != null && param != true && param.trim().length != 0)
	  		url += "&param=" + param;
		if (this.refresh) {
			this.refIndex = "ref_" + (ReferenceTextComp.DIALOG_INDEX ++);
			this.refresh = false;
		}
		if (isRefreshDialog)
			showDialog(url, this.nodeInfo.name, this.dialogWidth, this.dialogHeight, this.refIndex, true, true, null);
		else	
			showDialog(url, this.nodeInfo.name, this.dialogWidth, this.dialogHeight, this.refIndex, false, false, null);
	
		// Dialog中的Iframe的ID（该格式在PageUtil.js中的showDialog()方法里定义）
		var iframeId = "$modalDialogFrame" + this.refIndex;
		// 调用方法加载数据
		//ReferenceTextComp.doFilter("", iframeId, true);
	}
};

/**
 * 打开参照选择Div
 * @private
 */
ReferenceTextComp.prototype.openRefDiv = function(e, doFilter) {
	if(this.nodeInfo instanceof SelfRefNodeInfo){
		var appUniqueId = getAppUniqueId();
		var url = this.nodeInfo.url;
		if(url.indexOf("?") == -1)
			url += "?";
		else
			url += "&";
		url = url +  "widgetId=" + this.widget.id + "&otherPageId=" + getPageId() + "&otherPageUniqueId=" + getPageUniqueId() + "&nodeId=" + this.nodeInfo.id + "&owner=" + this.id + "&appUniqueId=" +appUniqueId + "&isReference=true";
		if (this.refresh) {
			this.refIndex = "ref_" + (ReferenceTextComp.DIALOG_INDEX ++);
			this.refresh = false;
		}
		if (this.refreshRefPage)
			this.showDiv(url, this.nodeInfo.name, this.divWidth, this.divHeight, this.refIndex, true, true);
		else	
			this.showDiv(url, this.nodeInfo.name, this.divWidth, this.divHeight, this.refIndex, false);
	
		this.divIsShown = true;
		
		// 过滤内容
//		var value = "";
//		if (doFilter)
//			value = this.input.value;
//		ReferenceTextComp.doFilter(value, this.divFrameId, this.divIsShown);
		
		//guoweic: 设置当前打开参照div的参照对象（用在RefOkDelegator中，用于选择后关闭时使用）
		window.currentReferenceWithDivOpened = this;
	}
	else{
		var param = this.beforeOpenRefPage();
		if (param == false)
			return;
		if (typeof globalBeforeOpenRefDialog != "undefined")
			param = globalBeforeOpenRefDialog(this);
		if (param == false)
			return;
		//记录当前参照refnode的id
		if (this.nodeInfo.id != null)
			window.$nowRefNodeId = this.nodeInfo.id;
		var oThis = this;
		
		var url = null;
		if (window.appType != null && window.appType == "true"){
			var appUniqueId = getAppUniqueId();
			url = window.globalPath + "/app/" + window.appId + "/reference" + "?pageId=" +  encodeURIComponent(this.nodeInfo.pageMeta) + "&widgetId=" + this.widget.id + "&otherPageId=" + getPageId() + "&otherPageUniqueId=" + getPageUniqueId() + "&nodeId=" + this.nodeInfo.id + "&owner=" + this.id + "&appUniqueId=" +appUniqueId + "&isReference=true";
		}
		else{
			url = window.corePath + "/" + this.nodeInfo.path + "?pageId=" +  encodeURIComponent(this.nodeInfo.pageMeta) + "&widgetId=" + this.widget.id + "&otherPageId=" + getPageId() + "&otherPageUniqueId=" + getPageUniqueId() + "&nodeId=" + this.nodeInfo.id + "&owner=" + this.id;
			if(this.isDialog)
				url += "&showType=type_divbutton";
			else
				url += "&showType=type_div";	
			if(window.clientMode){
				url = "refs/" + this.nodeInfo.id + ".html?pageId=" +  encodeURIComponent(this.nodeInfo.pageMeta) + "&widgetId=" + this.widget.id + "&otherPageUniqueId=" + getPageUniqueId() + "&nodeId=" + this.nodeInfo.id + "&owner=" + this.id;
			}
		}	
		if (this.refresh) {
			this.refIndex = "ref_" + (ReferenceTextComp.DIALOG_INDEX ++);
			this.refresh = false;
		}
		if (this.refreshRefPage)
			this.showDiv(url, this.nodeInfo.name, this.divWidth, this.divHeight, this.refIndex, true, true);
		else	
			this.showDiv(url, this.nodeInfo.name, this.divWidth, this.divHeight, this.refIndex, false);
	
		this.divIsShown = true;
		
		// 过滤内容
		var value = "";
		if (doFilter)
			value = this.input.value;
		ReferenceTextComp.doFilter(value, this.divFrameId, this.divIsShown);
		
		//guoweic: 设置当前打开参照div的参照对象（用在RefOkDelegator中，用于选择后关闭时使用）
		window.currentReferenceWithDivOpened = this;
	}	
};

/**
 * 设置此参照输入框的激活状态
 */
ReferenceTextComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	//清除只读状态
	//modify by licza 2010-11-12
//	if(this.readOnly) 
//		this.setReadOnly(false); 
	
	// 控件处于激活状态变为非激活状态
	if ((this.disabled == false && isActive == false) || (this.divButtonClickFunc == null && isActive == false)) {
		TextComp.prototype.setActive.call(this, false);
		//将与下拉按钮的各种动作事件保存
		this.divButtonClickFunc = this.divButton.onclick;
		this.divButtonMouseOutFuc = this.divButton.onmouseout;
		this.divButtonMouseOverFuc = this.divButton.onmouseover;
		 
		this.divButton.onclick = null;
		this.divButton.onmouseout = null;
		this.divButton.onmouseover = null;
		
		this.refImg.src = window.themePath + "/images/text/ref_disable.png";
	}
	// 控件处于禁用状态变为激活状态
	else if (this.disabled == true && isActive == true) {
		TextComp.prototype.setActive.call(this, true);
		this.divButton.onclick = this.divButtonClickFunc;
		this.divButton.onmouseout = this.divButtonMouseOutFuc;
		this.divButton.onmouseover = this.divButtonMouseOverFuc;
		
		this.refImg.src = window.themePath + "/images/text/ref_nm.png";
	}
};

/**
 * 得到参照输入框的激活状态
 */
ReferenceTextComp.prototype.isActive = function() {
	return !this.disabled;
};

/**
 * 单击事件
 * @private
 */
ReferenceTextComp.prototype.onclick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
	return true;
};

/**
 * 获取参照对话框的Window对象
 * @private
 */
ReferenceTextComp.prototype.getRefDlgWindow = function() {
	return window["$modalDialogFrame" + this.refIndex].contentWindow;
};

/**
 * 设置该参照所在的Dataset与Field属性 
 */
ReferenceTextComp.prototype.setBindInfo = function(dataset, field) {
	this.datasetId = dataset;
	this.field = field;
};

/**
 * 设值
 */
ReferenceTextComp.prototype.setValue = function(value) {
	value = getString(value, "");
	this.setMessage(value);
	if (null != this.datasetId) {  // 绑定了dataset
		this.input.value = value;
		if (this.checkTip()) {
			if (this.input.value == "")
				this.showTip();
			else
				this.input.style.color = "black";
		}
	}
	this.value = value;

	this.inputType = 2;
	
	if (this.value != this.oldValue)
		this.valueChanged(this.oldValue, this.value);
	
	// 记录旧值
	this.oldValue = value;
};

/**
 * 得到value值
 */
ReferenceTextComp.prototype.getValue = function() {
	return this.value;
};

/**
 * 设置显示值
 */
ReferenceTextComp.prototype.setShowValue = function(showValue) {
	showValue = getString(showValue, "");
	this.setMessage(showValue);
	this.showValue = showValue;
	this.input.value = showValue;
	if (this.checkTip()) {
		if (this.input.value == "")
			this.showTip();
		else
			this.input.style.color = "black";
	}
};

/*
 * 内部处理事件
 * @private
 */
//ReferenceTextComp.prototype.innerProcessEnter = function() {
//	//只响应手工输入的信息匹配
//	if (this.inputType == null || this.inputType == 2) {
//		this.inputType = null;
//		return;	
//	}
//	
//	if (this.datasetId == null || this.field == null)
//		return; 
//	var inputValue = this.getValue();
//	processReferenceEnter(this.datasetId, this.field, inputValue, this.nodeInfo.readFields.join(","), this);
//	return false;			
//};

/*
 * 
ReferenceTextComp.prototype.processEnter = function() {

};
 */


/**
 * 暴露的给用户进行参照值设置的接口，如果返回false，参照对话框不会打开
 * @private
 */
ReferenceTextComp.prototype.beforeOpenRefPage = function() {
	var simpleEvent = {
			"obj" : this
		};
	//TODO event改名
	this.doEventFunc("beforeOpenReference", ReferenceTextListener.listenerType, simpleEvent);
	return true;
};

/**
 * 获取对象信息
 * @private
 */
ReferenceTextComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.ReferenceTextContext";
	context.c = "ReferenceTextContext";
	context.id = this.id;
	context.value = this.value;
	context.showValue = this.showValue;
	context.readOnly = this.readOnly;
	context.enabled = !this.disabled;
	context.visible = this.visible;
	return context;
};

/**
 * 设置对象信息
 * @private
 */
ReferenceTextComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	if (context.focus)
		this.setFocus();
	if (context.showValue && context.showValue != this.showValue) {
		this.showValue = context.showValue;
		if (context.value && context.value != this.value)
			this.setValue(context.value);
		this.setShowValue(context.showValue);
	} else if (context.value && context.value != this.value)
		this.setValue(context.value);
	if (context.readOnly != null && this.readOnly != context.readOnly)
		this.setReadOnly(context.readOnly);
//	if (context.visible != this.visible) {
		if (context.visible)
			this.showV();
		else
			this.hideV();
//	}
};

/**
 * 显示参照Div
 * @private
 */
ReferenceTextComp.prototype.showDiv = function(pageUrl, title, width, height, id, refresh, clearState) {
	if (!this.refDiv) {
		var refDiv = $ce("DIV");
		refDiv.id = id;
		refDiv.style.width = width + "px";
		refDiv.style.height = height + "px";
		refDiv.className = "reference_data_div";
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。		
//		refDiv.style.zIndex = BaseComponent.STANDARD_ZINDEX + 100000;
		refDiv.style.zIndex = getZIndex();
		
		refDiv.style.position = "absolute";
		refDiv.style.display = "none";
		this.refDiv = refDiv;
		
		document.body.appendChild(this.refDiv);
		
		var iframe = $ce("iframe");
		iframe.name = "in_frame";
		iframe.id = "in_frame";
		iframe.allowTransparency = "true";
		iframe.frameBorder = 0;
		iframe.style.width = "100%";
		iframe.style.height = "100%";
		this.divFrameId = "$divFrame" + id;
		window[this.divFrameId] = iframe;
		
		this.refDiv.appendChild(iframe);
	}
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。	
//	if (this.refDiv.style.zIndex != null && this.refDiv.style.zIndex > ModalDialogComp.ZINDEX){
//	   ModalDialogComp.ZINDEX = this.refDiv.style.zIndex;
//	}
//	else{
//	   this.refDiv.style.zIndex = ++ModalDialogComp.ZINDEX; 
//	}
	this.refDiv.style.zIndex = getZIndex();
	
	if (window[this.divFrameId].src == null || window[this.divFrameId].src == "" || refresh) {
		reload = true;
		window[this.divFrameId].src = pageUrl;
	}

	// 计算显示位置
	var pageWidth = document.body.offsetWidth;
	var pageHeight = document.body.offsetHeight;
	
	if (this.Div_text.clientTop + compOffsetTop(this.Div_text) + height > pageHeight) {
		var top = compOffsetTop(this.Div_text) - height + 1;
		if (top < 1)
			this.refDiv.style.top = "1px";
		else
			this.refDiv.style.top = top + "px";
	} else
		this.refDiv.style.top = compOffsetTop(this.Div_text) + this.Div_text.offsetHeight - 1 + "px";
	if (compOffsetLeft(this.Div_text) + width > pageWidth)
		this.refDiv.style.left = pageWidth - width - 10 + "px";
	else
		this.refDiv.style.left = compOffsetLeft(this.Div_text) + "px";
	
	this.refDiv.style.display = "block";
	
	//TODO 为参照页面设置RefNodeRelation
//	if (this.datasetId != null && window.$refNodeRelations != null)
//		window["$divFrame" + id].contentWindow.refNodeRelation = window.$refNodeRelations.getRelationBySlaveRefNode(this.nodeInfo.id, this.datasetId);
	
	// iframe显示之前调用此方法(父调用子)
	if (!reload && window["$divFrame" + id].contentWindow.onBeforeActive != null) {
		window["$divFrame" + id].contentWindow.onBeforeActive();
	}
	
	this.refDiv.clearState = clearState;
	return this.refDiv;
};

/**
 * 隐藏参照Div
 * @private
 */
ReferenceTextComp.prototype.hideRefDiv = function() {
	var iframeId = this.divFrameId;
	if (window[iframeId] == null || window[iframeId].contentWindow == null || window[iframeId].contentWindow.renderDone == null)
		return;
	if (this.refDiv) {
		this.refDiv.style.display = "none";
		if(this.refDiv.clearState)
			this.refDiv.firstChild.src = "";
		this.divIsShown = false;
	}
};

/**
 * 外部点击事件发生时的回调函数。用来隐藏下拉框数据部分
 * @private
 */
ReferenceTextComp.prototype.outsideClick = function(e) {
	if (window.clickHolders.trigger == this)
		return;
	this.hideRefDiv();
};
