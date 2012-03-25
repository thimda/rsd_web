/********************************************
*
*	多语资源库(中文)
*
*********************************************/
function trans(key){
	if(window.transMap == null){
		window.transMap = new Object;
		window.transMap.ml_ok = "ok";
		window.transMap.ml_cancel = "cancel";
		 
		window.transMap.ml_progressdialog = "progress";
		window.transMap.ml_messagedialog = "infomation";
		window.transMap.ml_errordialog = "error";
		window.transMap.ml_confirmdialog = "confirm";
		window.transMap.ml_warningdialog = "warn";
		window.transMap.ml_exceptiondialog = "ExceptionDialog";
		window.transMap.ml_qrydialog = "QueryDialog";
		window.transMap.ml_approvedialog = "ApproveDialog";
		
		window.transMap.ml_friendmsg = "FriendInfo";
		window.transMap.ml_errormsg = "ErrorInfo";
		window.transMap.ml_exceptionstack = "ExpStack";
		
		window.transMap.ml_month = "month";
		window.transMap.ml_year = "year";
		window.transMap.ml_nextmonth = "next month";
		window.transMap.ml_lastmonth = "last month";
		
		window.transMap.ml_one = "one";
		window.transMap.ml_two = "two";
		window.transMap.ml_three = "three";
		window.transMap.ml_four = "four";
		window.transMap.ml_five = "five";
		window.transMap.ml_six = "six";
		window.transMap.ml_seven = "seven";
		
		window.transMap.ml_thevaluebeyondthemaxlength = "inputValue beyond maxLength!";
		
		window.transMap.ml_pagefirst="first page";
		window.transMap.ml_pagepre="pre page";
		window.transMap.ml_pagenext="next page";
		window.transMap.ml_pagelast="last page";
		
		window.transMap.ml_sessioninvalid="session invalid,please login";
		//window.transMap.ml_success="您的操作成功";
		window.transMap.ml_loading = "loading data...";
		
		window.transMap.ml_integermustbetween = "int type must between";
		window.transMap.ml_and = "and";
		window.transMap.ml_between = "between!";
		window.transMap.ml_ajaxerror = "send request error";
		
		window.transMap.ml_nextfieldscannotnull = "these fields cannot be null";
		window.transMap.ml_thisfieldcannotnull = "the field cannot be null";
		window.transMap.ml_dateisinvalid = "Date format invalid";
		
		window.transMap.ml_allchooseablevalues = "all values";
		window.transMap.ml_allchoosedvalues = "selected value";
		window.transMap.ml_toright = "right";
		window.transMap.ml_toleft = "left";
		window.transMap.ml_alltoleft = "all left";
		window.transMap.ml_alltoright = "all right";
		window.transMap.ml_totop = "top";
		window.transMap.ml_tobottom = "bottom";
		window.transMap.ml_toppest = "toppest";
		window.transMap.ml_bottomest = "bottomest";
		
		window.transMap.ml_showcolums = "visible columns";
		window.transMap.ml_lockcolums = "locked columns";
		window.transMap.ml_the = "the";
		window.transMap.ml_page = "page";
		window.transMap.ml_all = "count";
		window.transMap.ml_pageCount = "page number:";
		window.transMap.ml_rowCount = "row count:";
		window.transMap.ml_pageRowCount = "page row count:";
		window.transMap.ml_pageRowCountLabel = "page visible count";
		window.transMap.ml_refresh = "refresh";
		window.transMap.ml_goto = "goto";
		window.transMap.ml_set = "setting";
		window.transMap.ml_setpagesize = "set page count";
		window.transMap.ml_total = "sum";
		
		window.transMap.ml_confirmoperate = "Confirm this operation?";
		window.transMap.ml_noselrow = "No selected Row!";
		window.transMap.ml_nooperation = "No operation,cannot save!";
		window.transMap.ml_wait = "processing,wait...";
		window.transMap.ml_operationsuccess = "Operation success!";
		window.transMap.ml_chooseprinttemplate = "Choose print template!";
		window.transMap.ml_billcommited = "Bill already commit!";
		window.transMap.ml_billapproved = "Bill has been approved!";
		window.transMap.ml_assigninfo = "assign info";
		window.transMap.ml_nobillstatus = "Bill status is null!";
		window.transMap.ml_billstatusisnull = "Bill status is null!";
		window.transMap.ml_requestvalid = "Request is outdate,please login!";

		window.transMap.ml_input_overflow = "Input overflow";
		window.transMap.ml_rule_email = "These fields against email format";
		window.transMap.ml_rule_number = "These fields must be number";
		window.transMap.ml_rule_chn = "These fields must be Chinese character";
		window.transMap.ml_rule_variable = "These fields against variable format";
		window.transMap.ml_rule_other = "These fields against Validation Rule";
		window.transMap.ml_rule_phone = "These fields against tel format";
		window.transMap.ml_rule_reg = "以下字段不符合规范";
		window.transMap.ml_auditcontent="The auditContent's length can't exceed 200";
	}
	return window.transMap[key];
}