/********************************************
*
*	多语资源库(中文)
*
*********************************************/
function trans(key){
	if(window.transMap == null){
		window.transMap = new Object;
		window.transMap.ml_ok = "确定";
		window.transMap.ml_cancel = "取消";
		window.transMap.ml_understand = "知道了";
		 
		window.transMap.ml_progressdialog = "进度";
		window.transMap.ml_messagedialog = "信息";
		window.transMap.ml_errordialog = "错误";
		window.transMap.ml_confirmdialog = "确认";
		window.transMap.ml_warningdialog = "警告";
		window.transMap.ml_exceptiondialog = "异常信息对话框";
		window.transMap.ml_qrydialog = "查询对话框";
		window.transMap.ml_approvedialog = "审批对话框";
		
		window.transMap.ml_friendmsg = "友好信息";
		window.transMap.ml_errormsg = "错误信息";
		window.transMap.ml_exceptionstack = "异常栈";
		
		window.transMap.ml_month = "月";
		window.transMap.ml_year = "年";
		window.transMap.ml_nextmonth = "下一月";
		window.transMap.ml_lastmonth = "上一月";
		
		window.transMap.ml_one = "一";
		window.transMap.ml_two = "二";
		window.transMap.ml_three = "三";
		window.transMap.ml_four = "四";
		window.transMap.ml_five = "五";
		window.transMap.ml_six = "六";
		window.transMap.ml_seven = "日";
		
		window.transMap.ml_thevaluebeyondthemaxlength = "输入值超过了最大长度限制!";
		
		window.transMap.ml_pagefirst="第一页";
		window.transMap.ml_pagepre="上一页";
		window.transMap.ml_pagenext="下一页";
		window.transMap.ml_pagelast="最末页";
		
		window.transMap.ml_sessioninvalid="您的会话已过期，请重新登录";
		//window.transMap.ml_success="您的操作成功";
		window.transMap.ml_loading = "正在加载数据...";
		
		window.transMap.ml_integermustbetween = "整数类型必须介于";
		window.transMap.ml_and = "和";
		window.transMap.ml_between = "之间!";
		window.transMap.ml_ajaxerror = "发送请求时出现错误";
		
		window.transMap.ml_nextfieldscannotnull = "如下字段不能为空";
		window.transMap.ml_thisfieldcannotnull = "此字段不能为空";
		window.transMap.ml_dateisinvalid = "日期格式无效";
		
		window.transMap.ml_allchooseablevalues = "全部可选的值";
		window.transMap.ml_allchoosedvalues = "已经选择的值";
		window.transMap.ml_toright = "向右";
		window.transMap.ml_toleft = "向左";
		window.transMap.ml_alltoleft = "全向左";
		window.transMap.ml_alltoright = "全向右";
		window.transMap.ml_totop = "向上";
		window.transMap.ml_tobottom = "向下";
		window.transMap.ml_toppest = "最上";
		window.transMap.ml_bottomest = "最下";
		
		window.transMap.ml_showcolums = "显示列";
		window.transMap.ml_lockcolums = "锁定列";
		window.transMap.ml_the = "第";
		window.transMap.ml_page = "页";
		window.transMap.ml_all = "共";
		window.transMap.ml_pageCount = "页码:";
		window.transMap.ml_rowCount = "行数:";
		window.transMap.ml_pageRowCount = "每页行数:";
		window.transMap.ml_pageRowCountLabel = "每页显示行数";
		window.transMap.ml_refresh = "刷新";
		window.transMap.ml_goto = "转到";
		window.transMap.ml_set = "设置";
		window.transMap.ml_setpagesize = "设置每页行数";
		window.transMap.ml_total = "合计";
		
		window.transMap.ml_confirmoperate = "确认执行该操作吗?";
		window.transMap.ml_noselrow = "没有选中行无法进行操作!";
		window.transMap.ml_nooperation = "没有执行任何操作,不能保存!";
		window.transMap.ml_wait = "操作进行中,请稍候...";
		window.transMap.ml_operationsuccess = "操作成功!";
		window.transMap.ml_chooseprinttemplate = "选择打印模版";
		window.transMap.ml_billcommited = "单据已经提交,不能重复提交!";
		window.transMap.ml_billapproved = "单据已经处于审批通过状态,不需要审批!";
		window.transMap.ml_assigninfo = "指派信息";
		window.transMap.ml_nobillstatus = "未指定当前单据状态!";
		window.transMap.ml_billstatusisnull = "当前指定单据状态为空!";
		window.transMap.ml_requestvalid = "请求已经过期，可能是停留时间过长或服务器重新启动，请重新登录!";

		window.transMap.ml_input_overflow = "输入长度超出范围";
		window.transMap.ml_rule_email = "以下字段不符合邮箱规范";
		window.transMap.ml_rule_number = "以下字段必须为数字";
		window.transMap.ml_rule_chn = "以下字段必须为中文字符";
		window.transMap.ml_rule_variable = "以下字段不符合变量规范";
		window.transMap.ml_rule_other = "以下字段不符合校验规则";
		window.transMap.ml_rule_phone = "以下字段需符合电话格式";
		window.transMap.ml_rule_reg = "以下字段不符合规范";
		window.transMap.ml_auditcontent="审批内容不能超多最大长度200";
	}
	return window.transMap[key];
}