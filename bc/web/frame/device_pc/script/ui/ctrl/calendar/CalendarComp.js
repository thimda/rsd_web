/**
 * 日期控件。控件状态分为弹出状态和静态状态。对于弹出状态， * 日期控件为一单例.对于静态状态,每个控件对应一个实例, 并 *
 * 且创建时应传入一个true参数 * *
 * 
 * @author dengjt, gd, guoweic
 * @version NC6.0
 * 
 * 1.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
*/
CalendarComp.prototype = new BaseComponent;
CalendarComp.prototype.componentType = "CALENDAR";

CalendarComp.imagePath = window.themePath + "/ui/ctrl/calendar/images/";
CalendarComp.prototype.upImagePath = CalendarComp.imagePath + "up.gif";
CalendarComp.prototype.downImagePath = CalendarComp.imagePath + "down.gif";
CalendarComp.prototype.leftImagePath = CalendarComp.imagePath + "left.gif";
CalendarComp.prototype.rightImagePath = CalendarComp.imagePath + "right.gif";

var MONTHS = new Array("January", "February", "March", "April", "May", "June",
		"July", "August", "September", "October", "November", "December");
var WEEKDAYS_FULL = new Array("Monday", "Tuesday", "Wednesday", "Thursday",
		"Friday", "Saturday", "Sunday");
var WEEKDAYS = null;
var MONTH_DAY_COUNT = new Array("31", "28", "31", "30", "31", "30", "31", "31",
		"30", "31", "30", "31");
var currDate = new Date();
var DimanchePaques = false;

/**
 * 日期控件构造函数
 * @class 日期控件
 */
function CalendarComp(isState, parent) {
	if (WEEKDAYS == null)
		WEEKDAYS = new Array(trans("ml_one"), trans("ml_two"),
				trans("ml_three"), trans("ml_four"), trans("ml_five"),
				trans("ml_six"), trans("ml_seven"));
	this.getInstance = Singleton;
	this.isState = getBoolean(isState, false);
	if (!this.isState) {
		if (instance = this.getInstance()) {
			return instance;
		} else {
			this.base = BaseComponent;
			this.base("Calendar", "", "", "", "");
			this.dateObject = new Date();
			this.day = this.dateObject.getDate();
			this.month = this.dateObject.getMonth() + 1;
			this.year = this.dateObject.getFullYear();
			this.parentOwner = document.body;
			this.create();
		}
	} else {
		this.base = BaseComponent;
		this.base("Calendar", "", "", "", "");
		this.dateObject = new Date();
		this.day = this.dateObject.getDate();
		this.month = this.dateObject.getMonth() + 1;
		this.year = this.dateObject.getFullYear();
		this.parentOwner = document.body;
		this.oldDayCell = null;
		this.oldDayCellColor = null;
		this.parentOwner = parent;
		this.create();
	}

	// 注册外部回掉函数
	window.clickHolders.push(this);
};

/**
 * CalendarComp创建函数
 * @private
 */
CalendarComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = "Calendar";
	this.Div_gen.className = "calendar_div";
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。	
//	this.Div_gen.style.zIndex = BaseComponent.STANDARD_ZINDEX + 1000000;
	this.Div_gen.style.zIndex = getZIndex();

	this.Div_gen.onclick = function(e) {
		e = EventUtil.getEvent();
		stopAll(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
};

/**
 * CalendarComp二级回调函数
 * @private
 */
CalendarComp.prototype.manageSelf = function() {
	// 操作菜单栏
	var oThis = this;
	this.opBar = $ce("DIV");
	this.opBar.style.backgroundColor = "#e9f2f9";
	this.opBar.style.width = "100%";
	this.opBar.style.border = "solid #b6cae1 1px";
	this.opBar.style.height = "30px";
	this.opBar.style.top = "0px";
	this.opBar.style.left = "0px";
	this.Div_gen.appendChild(this.opBar);

	// 年份选择框
	this.yearInput = $ce("input");
	this.yearInput.className = "calendar_bar_input";
	this.opBar.appendChild(this.yearInput);

	this.yearDiv = $ce("DIV");
	this.yearDiv.style.position = "absolute";
	this.yearDiv.className = "calendar_bar_img_div";
	this.opBar.appendChild(this.yearDiv);

	this.nextYearImg = $ce("img");
	this.nextYearImg.src = this.upImagePath;
	this.nextYearImg.className = "calendar_bar_imgup";
	this.yearDiv.appendChild(this.nextYearImg);

	this.preYearImg = $ce("img");
	this.preYearImg.src = this.downImagePath;
	this.preYearImg.className = "calendar_bar_imgdown";
	this.yearDiv.appendChild(this.preYearImg);

	this.yearInput.onchange=function(e){ 
		var currYear=this.value.replace("年","");
		oThis.changeDate(parseInt(currYear), oThis.month);
		e = EventUtil.getEvent();
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.nextYearImg.onclick = function(e) {
		e = EventUtil.getEvent();
		// 首先调用用户函数获取任务数据
		oThis.changeDate(oThis.year + 1, oThis.month);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	this.preYearImg.onclick = function(e) {
		e = EventUtil.getEvent();
		oThis.changeDate(oThis.year - 1, oThis.month);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	// 创建月份选择下拉框
	this.monthComb = new ComboComp(this.opBar, "monthcomb", 85, 6, "55",
			"absolute", false, {"stopHideDiv" : true}, null);
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。			
//	this.monthComb.dataDiv.style.zIndex = BaseComponent.STANDARD_ZINDEX + 1000001; 
	this.monthComb.dataDiv.style.zIndex = getZIndex(); 

	for ( var i = 0; i < 12; i++) {
		var selected = false;
		if (i + 1 == this.month)
			selected = true;
		this.monthComb.createOption((i + 1) + trans("ml_month"), (i + 1), "",
				selected, null);
	}

	this.monthComb.valueChanged = function(newValue, oldValue) {
		if (oThis.year && newValue != null)
			oThis.changeDate(oThis.year, newValue);
	};

	var oParent = this.opBar;

	// 创建分割左右箭头和左侧其他按钮的竖杠
	var sep = $ce("div");
	sep.style.width = "1px";
	sep.style.height = "18px";
	sep.style.position = "absolute";
	sep.style.backgroundColor = "b6cae1";
	sep.style.left = this.opBar.offsetWidth - 42 - 7 + "px";
	sep.style.top = "3px";
	oParent.appendChild(sep);

	// 创建下一月按钮
	var tmpLeft = this.opBar.offsetWidth - 22;
	this.nextMonButt = new ImageComp(oParent, trans("ml_nextmonth"),
			this.rightImagePath, tmpLeft, 9, "",
			"", trans("ml_nextmonth"), this.rightImagePath, null);
	this.nextMonButt.onclick = function() {
		oThis.changeDate(oThis.year, oThis.month + 1);
	};

	// 创建上一月按钮
	tmpLeft -= 20;
	this.preMonButt = new ImageComp(oParent, trans("ml_lastmonth"),
			this.leftImagePath, tmpLeft, 9, "", "",
			trans("ml_lastmonth"), this.leftImagePath, null);
	this.preMonButt.onclick = function() {
		oThis.changeDate(oThis.year, oThis.month - 1);
	};

	// 创建星期显示的每个div
	for (j = 0; j < 7; j++) {
		var divWeekday = $ce("DIV");
		divWeekday.id = "calendar_day";
		divWeekday.className = "calendar_day";
		divWeekday.style.left = (5 + (j * 30)) + 15 + "px";
		this.opBar.appendChild(divWeekday);
		divWeekday.appendChild(document.createTextNode(WEEKDAYS[j]));
	}

	var sepBelowDivWeek = $ce("hr");
	sepBelowDivWeek.style.position = "absolute";
	sepBelowDivWeek.style.width = this.opBar.offsetWidth - 40 + "px";
	if (!IS_IE || IS_IE8)
		sepBelowDivWeek.style.top = 55 + "px";
	else
		sepBelowDivWeek.style.top = 60 + "px";
	sepBelowDivWeek.style.left = "18px";
	sepBelowDivWeek.style.color = "#c6d6e9";
	sepBelowDivWeek.size = 1;
	this.opBar.appendChild(sepBelowDivWeek);

	div_Calendar = $ce("DIV");
	div_Calendar.style.width = "100%";
	div_Calendar.style.position = "absolute";
	div_Calendar.style.top = "50px";
	div_Calendar.style.left = "0px";
	this.Div_gen.appendChild(div_Calendar);

	this.resetCalendar();
};

/**
 * 外部mouseclick事件发生时的回调函数.用来隐藏弹出的calendar控件
 * @private
 */
CalendarComp.prototype.outsideClick = function(e) {
	this.hide();
};

CalendarComp.prototype.outsideMouseWheelClick = function(e) {
	this.hide();
};

/**
 * 显示日历控件
 */
CalendarComp.prototype.show = function(left, top, showTimeBar, dateValue) {
	var oThis = this;
	this.left = left;
	this.top = top;
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.left = this.left + "px";
	if (showTimeBar)
		this.showTimeBar = true;

	var oldDateObject = this.dateObject;	
	if (dateValue != null && dateValue != ""){
		if (dateValue.indexOf("-") > -1)
			dateValue = dateValue.replace(/\-/g,"/");
		this.dateObject = new Date(dateValue);
		if(isNaN(this.dateObject.getFullYear())){
			var values = dateValue.split("-");
			if(values && values.length == 3){
				var year = parseInt(values[0],10);
				var month = parseInt(values[1],10) - 1;
				var day = parseInt(values[2],10);
				if(isNaN(year) || isNaN(month) || isNaN(day)){
					this.dateObject = new Date();		
				}else{
					this.dateObject = new Date(year,month,day);			
				}
			}else{
				this.dateObject = new Date();
			}
		}
	}else{	
		this.dateObject = new Date();	
	}
	
	this.Div_gen.style.visibility = 'visible';
	
	if (this.parentOwner && !this.isPlaceIn) {
		this.placeIn(this.parentOwner);
		this.isPlaceIn = true;
	}
	
	if (this.dateObject.getAAAAMMJJ() != oldDateObject.getAAAAMMJJ()) {
		this.day = this.dateObject.getDate();
		this.month = this.dateObject.getMonth() + 1;
		this.year = this.dateObject.getFullYear();
//		this.resetCalendar();
	}
	
//	dateObject = new Date();
//	if (this.dateObject.getAAAAMMJJ() != dateObject.getAAAAMMJJ()) {
//		this.dateObject = new Date();
//		this.day = this.dateObject.getDate();
//		this.month = this.dateObject.getMonth() + 1;
//		this.year = this.dateObject.getFullYear();
//		this.resetCalendar();
//	}

	
	// 显示时间框
	if (showTimeBar) {
		if (this.timeBar == null) {
			this.timeBar = $ce("DIV");
			this.Div_gen.appendChild(this.timeBar);
			this.timeBar.style.backgroundColor = "#e9f2f9";
			this.timeBar.style.position = "absolute";
			this.timeBar.style.width = "100%";
			this.timeBar.style.border = "solid #b6cae1 1px";
			this.timeBar.style.height = "30px";
			this.timeBar.style.top = (this.Div_gen.offsetHeight - 34) + "px";
			this.timeBar.style.left = "0px";
			this.timeBar.style.visibility = "hidden";
//			this.dateObject = new Date();

			/* 小时输入框 */
			this.hourInput = $ce("input");
			this.hourInput.className = "calendar_bar_input";
			this.hourInput.style.width = "30px";
			this.hourInput.value = this.dateObject.getHours();
			this.timeBar.appendChild(this.hourInput);
			this.hourInput.onclick = function(e) {
				if (!e)
					e = window.event;
				stopAll(e);
			};
			// 上下移动图标
			this.hourDiv = $ce("DIV");
			this.hourDiv.style.position = "absolute";
			this.hourDiv.className = "calendar_bar_img_div";
			this.hourDiv.style.left = "40px";
			this.timeBar.appendChild(this.hourDiv);
			// 上一小时
			this.preHourImg = $ce("img");
			this.preHourImg.src = this.upImagePath;
			this.preHourImg.className = "calendar_bar_imgup";
			this.hourDiv.appendChild(this.preHourImg);
			this.preHourImg.onclick = function(e) {
				if (!e)
					e = window.event;
				var value = oThis.hourInput.value;
				if (value == null || value == "")
					value = 0;
				else
					value = parseInt(value,10);
				value++;
				if (value > 23)
					value = 23;
				else if (value < 0)
					value = 0;

				oThis.hourInput.value = value;
				stopAll(e);
			};
			// 下一小时
			this.nextHourImg = $ce("img");
			this.nextHourImg.src = this.downImagePath;
			this.nextHourImg.className = "calendar_bar_imgdown";
			this.hourDiv.appendChild(this.nextHourImg);
			this.nextHourImg.onclick = function(e) {
				if (!e)
					e = window.event;
				var value = oThis.hourInput.value;
				if (value == null || value == "")
					value = 0;
				else
					value = parseInt(value,10);
				value--;
				if (value < 0)
					value = 0;
				else if (value > 23)
					value = 23;

				oThis.hourInput.value = value;
				stopAll(e);
			};

			var hourText = $ce("DIV");
			hourText.style.position = "absolute";
			this.timeBar.appendChild(hourText);
			hourText.style.width = "10px";
			hourText.style.left = "57px";
			hourText.style.top = "8px";
//			hourText.innerHTML = trans("ml_hour");
			hourText.innerHTML = "时";

			/* 分钟输入框  */
			this.minInput = $ce("input");
			this.minInput.className = "calendar_bar_input";
			this.minInput.style.width = "30px";
			this.minInput.style.left = "72px";
			this.minInput.value = this.dateObject.getMinutes();
			this.timeBar.appendChild(this.minInput);
			this.minInput.onclick = function(e) {
				if (!e)
					e = window.event;
				stopAll(e);
			};

			this.minDiv = $ce("DIV");
			this.minDiv.style.position = "absolute";
			this.minDiv.className = "calendar_bar_img_div";
			this.minDiv.style.left = "102px";
			this.timeBar.appendChild(this.minDiv);

			// 上一分钟
			this.preMinImg = $ce("img");
			this.preMinImg.src = this.upImagePath;
			this.preMinImg.className = "calendar_bar_imgup";
			this.minDiv.appendChild(this.preMinImg);
			this.preMinImg.onclick = function(e) {
				if (!e)
					e = window.event;
				var value = oThis.minInput.value;
				if (value == null || value == "")
					value = 0;
				else
					value = parseInt(value,10);
				value++;
				if (value > 59)
					value = 59;
				else if (value < 0)
					value = 0;

				oThis.minInput.value = value;
				stopAll(e);
			};
			// 下一分钟
			this.nextMinImg = $ce("img");
			this.nextMinImg.src = this.downImagePath;
			this.nextMinImg.className = "calendar_bar_imgdown";
			this.minDiv.appendChild(this.nextMinImg);
			this.nextMinImg.onclick = function(e) {
				if (!e)
					e = window.event;
				var value = oThis.minInput.value;
				if (value == null || value == "")
					value = 0;
				else
					value = parseInt(value,10);
				value--;
				if (value < 0)
					value = 0;
				else if (value > 59)
					value = 59;

				oThis.minInput.value = value;
				stopAll(e);
			};

			var minText = $ce("DIV");
			minText.style.position = "absolute";
			this.timeBar.appendChild(minText);
			minText.style.width = "10px";
			minText.style.left = "119px";
			minText.style.top = "8px";
//			minText.innerHTML = trans("ml_min");
			minText.innerHTML = "分";

			/* 秒输入框  */
			this.secInput = $ce("input");
			this.secInput.className = "calendar_bar_input";
			this.secInput.style.width = "30px";
			this.secInput.style.left = "134px";
			this.secInput.value = this.dateObject.getSeconds();
			this.timeBar.appendChild(this.secInput);
			this.secInput.onclick = function(e) {
				if (!e)
					e = window.event;
				stopAll(e);
			};

			this.secDiv = $ce("DIV");
			this.secDiv.style.position = "absolute";
			this.secDiv.className = "calendar_bar_img_div";
			this.secDiv.style.left = "164px";
			this.timeBar.appendChild(this.secDiv);

			// 上一秒钟
			this.preSecImg = $ce("img");
			this.preSecImg.src = this.upImagePath;
			this.preSecImg.className = "calendar_bar_imgup";
			this.secDiv.appendChild(this.preSecImg);
			this.preSecImg.onclick = function(e) {
				if (!e)
					e = window.event;
				var value = oThis.secInput.value;
				if (value == null || value == "")
					value = 0;
				else
					value = parseInt(value,10);
				value++;
				if (value > 59)
					value = 59;
				else if (value < 0)
					value = 0;

				oThis.secInput.value = value;
				stopAll(e);
			};

			// 下一秒钟
			this.nextSecImg = $ce("img");
			this.nextSecImg.src = this.downImagePath;
			this.nextSecImg.className = "calendar_bar_imgdown";
			this.secDiv.appendChild(this.nextSecImg);
			this.nextSecImg.onclick = function(e) {
				if (!e)
					e = window.event;
				var value = oThis.secInput.value;
				if (value == null || value == "")
					value = 0;
				else
					value = parseInt(value,10);
				value--;
				if (value < 0)
					value = 0;
				else if (value > 59)
					value = 59;

				oThis.secInput.value = value;
				stopAll(e);
			};

			var secText = $ce("DIV");
			secText.style.position = "absolute";
			this.timeBar.appendChild(secText);
			secText.style.width = "10px";
			secText.style.left = "181px";
			secText.style.top = "8px";
//			secText.innerHTML = trans("ml_sec");
			secText.innerHTML = "秒";
		}
	}
	
	if (showTimeBar)
		this.timeBar.style.visibility = "visible";
	else {
		if (this.timeBar != null)
			this.timeBar.style.visibility = "hidden";
	}
	this.resetCalendar()	
};

/**
 * 设置日期
 */
CalendarComp.prototype.setDate = function(y, m, d) {
	this.day = d;
	this.month = m;
	this.year = y;
	this.dateObject = new Date(y, m - 1, d);
};

/**
 * 改变日期,年,月
 */
CalendarComp.prototype.changeDate = function(y, m) {
	if (this.dateObject == null)
		this.dateObject = currDate; 
	var d = this.dateObject.getDate();
	if (m == 13) {
		m = 1;
		y++;
	}
	if (m == 0) {
		m = 12;
		y--;
	}
	this.setDate(y, m, d);
	this.resetCalendar();
};

/**
 * 处理点击事件
 * @private
 */
CalendarComp.prototype.onClick = function(day) {
	var tmpDay = day;
	var tmpMonth = 0 + this.month;
	var tmpYear = this.year;
	if (tmpDay < 10) {
		tmpDay = "0" + tmpDay;
	}

	if (tmpMonth < 10) {
		tmpMonth = "0" + tmpMonth;
	}

	if (this.showTimeBar) {
		var hour = this.hourInput.value;
		if (hour != null) {
			hour = parseInt(hour,10);
			if (!isNumber(hour) || hour < 0 || hour >= 24)
				hour = "00";
			else if (hour < 10)
				hour = "0" + hour;
		} else {
			hour = "00";
		}
		var min = this.minInput.value;
		if (min != null) {
			min = parseInt(min,10);
			if (!isNumber(min) || min < 0 || min >= 60)
				min = "00";
			else if (min < 10)
				min = "0" + min;
		} else {
			min = "00";
		}
		var sec = this.secInput.value;
		if (sec != null) {
			sec = parseInt(sec,10);
			if (!isNumber(sec) || sec < 0 || sec >= 60)
				sec = "00";
			else if (sec < 10)
				sec = "0" + sec;
		} else {
			sec = "00";
		}
		this.onclick(tmpYear + "-" + tmpMonth + "-" + tmpDay + " " + hour + ":" + min + ":" + sec);
		this.hourInput.value = hour;
		this.minInput.value = min;
		this.secInput.value = sec;
	} else {
		this.onclick(tmpYear + "-" + tmpMonth + "-" + tmpDay);
	}
	if (!this.isState) {
		this.hide();
	}
};

/**
 * 隐藏日历控件
 */
CalendarComp.prototype.hide = function() {
	this.Div_gen.style.visibility = "hidden";
	if (this.timeBar)
		this.timeBar.style.visibility = "hidden";
};

/**
 * 重置日历控件
 */
CalendarComp.prototype.resetCalendar = function() {
	div_Calendar.innerHTML = "";
	this.yearInput.value = this.year + trans("ml_year");
	if (this.monthComb.getSelectedIndex() != (this.month - 1))
		this.monthComb.setSelectedItem(this.month - 1);

	var tmpDate = new Date(this.year, (this.month) - 1);
	this.currDay = tmpDate.getDay();
	if (this.currDay == 0) {
		this.currDay = 7;
	}

	if (isRunNian(this.year)) {
		MONTH_DAY_COUNT[1] = 29;
	} else {
		MONTH_DAY_COUNT[1] = 28;
	}

	var day = 0;

	if (((this.currDay == 6) && ((MONTH_DAY_COUNT[(this.month) - 1]) == 31)
			|| (this.currDay == 7)
			&& ((MONTH_DAY_COUNT[(this.month) - 1]) == 30) || (this.currDay == 7)
			&& ((MONTH_DAY_COUNT[(this.month) - 1]) == 31))) {
		if (this.showTimeBar == true)
			this.Div_gen.style.height = "245px";
		else
			this.Div_gen.style.height = "220px";
	} else {
		if (this.showTimeBar == true)
			this.Div_gen.style.height = "225px";
		else
			this.Div_gen.style.height = "200px";
	}
	if (this.timeBar)
		this.timeBar.style.top = (this.Div_gen.offsetHeight - 34) + "px";

	var oThis = this;
	for (s = 0; s < 6; s++) {
		for (j = 0; j < 7; j++) {
			day = 7 * s + j - this.currDay + 2;
			var dayCell = $ce("DIV");
			dayCell.id = "dayCell";
			dayCell.className = "calendar_day_cell";
			dayCell.style.position = "absolute";
			dayCell.style.left = (5 + (j * 30)) + 15 + "px"; // 将每个cell像右移动15px
			dayCell.style.top = (s * 24) + 20 + "px";
			dayCell.style.width = "30px";
			dayCell.style.height = "22px";

			if (isWeekEnd(j)) {
				dayCell.style.color = "#89abbe";
			}

			div_Calendar.appendChild(dayCell);
			if ((7 * s + j >= this.currDay - 1)
					&& (day <= MONTH_DAY_COUNT[(this.month) - 1])) {
				dayCell.appendChild(document.createTextNode(day));
				dayCell.onclick = function(e) {
					e = EventUtil.getEvent();
					if (oThis.isState) {
						if (oThis.oldDayCell) {
							oThis.oldDayCell.style.color = oThis.oldDayCellColor;
						}
						oThis.oldDayCell = this.firstChild.parentNode;
						oThis.oldDayCellColor = this.firstChild.parentNode.style.color;
						this.firstChild.parentNode.style.color = "yellow";
					}
					oThis.onClick(this.firstChild.nodeValue);
					// 阻止事件上传 gd 2008-01-03
					stopAll(e);
					// 删除事件对象（用于清除依赖关系）
					clearEventSimply(e);
				};
			}
			if (this.dateObject == null)
				this.dateObject = currDate; 
			if ((day == (this.dateObject.getDate()))
					&& (this.month == (this.dateObject.getMonth() + 1))
					&& (this.year == (this.dateObject.getFullYear()))) {
				dayCell.style.border = "solid #ff9955 1px";
			}
		}
		if (day >= MONTH_DAY_COUNT[(this.month) - 1]) {
			break;
		}
	}
};

/**
 * 判断是否是润年
 * @private
 */
function isRunNian(year) {
	return (((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)) ? true
			: false);
};

/**
 * 判断是否是星期六和星期日
 * @private
 */
function isWeekEnd(day) {
	return (((day == 5) || (day == 6)) ? true : false);
};