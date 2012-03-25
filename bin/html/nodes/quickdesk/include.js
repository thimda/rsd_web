
	/**
	 * 我的单据
	 */
	function MyDjOperateRender(){}
	MyDjOperateRender.render = function(rowIndex, colIndex, value, header, cell)
	{	
		var acomp = $ce("A");  
		acomp.id = "pfinfo_acomp";
		acomp.appendChild(document.createTextNode("查看"));
		acomp.style.left = "0px";
		acomp.style.position="absolute";
		acomp.href = "#";
		acomp.onclick = function(e) {
			if(!e)
				e = window.event;
			try{
				var widget = pageUI.getWidget("mybillwidget");
				var ds = widget.getDataset("billds");
				var row = ds.getRow(rowIndex);
				openMyBillNew(ds,row);
			}
			catch(error){}
			stopAll(e);
		};	
		
		var acomp2 = $ce("A");  
		acomp2.id = "pfinfo_acomp2";
		acomp2.appendChild(document.createTextNode("打印"));
		acomp2.style.left = "40px";
		acomp2.style.position="absolute";
		acomp2.href = "#";
		acomp2.onclick = function(e) {
			/*
			if(!e)
				e = window.event;
			try{
				var widget = pageUI.getWidget("mybillwidget");
				var ds = widget.getDataset("billds");
				var row = ds.getRow(rowIndex);
				//获取nc节点
				var funnode = row.getCellValue(ds.nameToIndex("funcode"));
				var url = row.getCellValue(ds.nameToIndex("funurl"));
			 	var title = row.getCellValue(ds.nameToIndex("menuitemname"));
				var param = "funnode=" + funnode + "&pk_jkbx=" + pk_jkbx + "&djdl=" + djdl;
				var args = new Array;
				args.push("pk_jkbx=" + pk_jkbx + "&djdl=" + djdl);
				processCommonLogic(param, "doPrint", printReturnFun, args, true, null, null, null);
			}
			catch(error){}
			stopAll(e);
			* */
		};
		
		
		var acomp3 = $ce("A");  
		acomp3.id = "pfinfo_acomp3";
		acomp3.appendChild(document.createTextNode("流程"));
		acomp3.style.left = "80px";
		acomp3.style.position="absolute";
		acomp3.href = "#";
		acomp3.onclick = function(e) {
			try{
				if(!e)
				e = window.event;
				var widget = pageUI.getWidget("mybillwidget");
				var ds = widget.getDataset("billds");
				var row = ds.getRow(rowIndex);
				var pk_bill = row.getCellValue(ds.nameToIndex("pk_bill"));
				var billtype = row.getCellValue(ds.nameToIndex("billtype"));
				var url = getCorePath() + "/uimeta.um?pageId=pfinfo&billId="+ pk_bill +"&billType=" + billtype + "&model=nc.uap.lfw.billtemplate.workflow.WfInfoPageModel" + "&rand=" + (Math.random()*10000).toString().substring(0, 4);
				var dialogHeight = 480;					
				showDialog(url , "流程信息", -200, dialogHeight, "pfinfoDialog", true,true);
				}catch(error){}
				stopAll(e);
			/*
			if(!e)
				e = window.event;
			try{
				var widget = pageUI.getWidget("mybillwidget");
				var ds = widget.getDataset("billds");
				var row = ds.getRow(rowIndex);
				var billId = row.getCellValue(ds.nameToIndex("pk_jkbx"))
				var billType = row.getCellValue(ds.nameToIndex("djlxbm"));
				var url = getCorePath() + "/pfinfo/pfinfo.jsp?pageId=pfinfo&billId="+ billId +"&billType=" + billType + "&rand=" + (Math.random()*10000).toString().substring(0, 4);
				
				var width = screen.width;
				var height = screen.height;
				var dlgWidth = 985,dlgHeight = 450;

				if(dlgWidth > document.body.clientWidth){
					dlgWidth = document.body.clientWidth - 40;
				}			
								
				showDialog(url , "${ml:trans('yer_pfinfo')}", dlgWidth, dlgHeight, "pfinfoDialog", true,true);
			}
			catch(error){}
			stopAll(e);
			*/
		};
	
		cell.style.overflow = "hidden";
		cell.style.textOverflow = "ellipsis";
		cell.style.cursor = "default";
		cell.style.textAlign = "center";
		cell.appendChild(acomp);
		cell.appendChild(acomp2);
		cell.appendChild(acomp3);
	}
	
	function printReturnFun(result, args, exception)
	{
		if(!exception) 
		{ 
			var filePath = result[0];
			if(filePath != null && filePath != "" && filePath != "NEEDSELECT")
				window.open(getRootPath() + "/printfile.pdf?filePath=" + filePath, "xxx", "toolbar=no,menubar=yes,scrollbar=yes,resizable=yes,location=no,status=no,fullscreen=no");
			else if(filePath == "NEEDSELECT")
				this.showPrintDlg(result, args[0]);
		}
		hideProgressDialog();
	}
	
	function showPrintDlg(selectResult, param)
	{
		if(window["$print_Template_ids_select_dlg"] == null)
		{
			var dlg = new ModalDialogComp("print_Template_ids_select_dlg", "${ml:trans('yer_chooseprinttemplate')}", 0, 0, 300, 200);
			var labelComp = new LabelComp(dlg, "print_Template_ids_select_dlg_label", 40, 32, "${ml:trans('yer_choosetemplate')}", "absolute");
			var idsComboComp = new ComboComp(dlg, "print_Template_ids_select_dlg_printIdsCombo", 100, 30, 150, "absolute");
			var okBtn = new ButtonComp(dlg, "print_Template_ids_select_dlg_OkBtn", 70, 120, 70, 20, "${ml:trans('BxContrastPageModel-000000')}", "${ml:trans('BxContrastPageModel-000000')}", null, "absolute");
			var cancelBtn = new ButtonComp(dlg, "print_Template_ids_select_dlg_CancelBtn", 160, 120, 70, 20, "${ml:trans('BxContrastPageModel-000001')}", "${ml:trans('BxContrastPageModel-000001')}", null, "absolute");
			this.showPrintDlg.comboComp = idsComboComp;
			window["$print_Template_ids_select_dlg"] = dlg;
			
			var oThis = this;
			okBtn.onclick = function(e)
			{
				var selectedValue = idsComboComp.getValue();
				if(selectedValue == null)
				{
					showMessageDialog("${ml:trans('yer_chooseusedprinttemplate')}");
					return;	
				}
				
	//			var args = new Array;
	//			args.push(paramStr);
				// 再次向后台发送ajax请求,此时传递用户选择的模版id
				processCommonLogic(param + "&selectedTemplateId=" + selectedValue, "doPrint", oThis.printReturnFun, null, true, null, null, this);
				showProgressDialog("${ml:trans('yer_wait')}");
				dlg.hide();
			};
			
			cancelBtn.onclick = function(e)
			{
				dlg.hide();
			};
		}
		
		window["$print_Template_ids_select_dlg"].show();
		var combo = this.showPrintDlg.comboComp;
		combo.clearOptions();
		var keyValues = null;
		for(var i = 1, count = selectResult.length; i < count; i++)
		{
			keyValues = selectResult[i];
			keyValues = keyValues.split(";");
			if(i == 1)
				combo.createOption(keyValues[1], keyValues[0], null, true);	
			else
				combo.createOption(keyValues[1], keyValues[0]);
		}
	};

	/**
	 * 我的代办事务render
	 */
	function MyJobOperateRender(){}
	MyJobOperateRender.render = function(rowIndex, colIndex, value, header, cell)
	{	
		var acomp = $ce("A");  
		acomp.id = "pfinfo_acomp";
		acomp.appendChild(document.createTextNode("查看"));
		//acomp.className = "grid_operatecolumn"; 
		acomp.style.left = 0;
		acomp.href = "#";
		acomp.onclick = function(e) {
			if(!e)
				e = window.event;
			try{
				var widget = pageUI.getWidget("myjobwidget");
				var ds = widget.getDataset("jobds");
				var row = ds.getRow(rowIndex);
				openMyJobNew(ds,row);
			}
			catch(error){}
			stopAll(e);
		};	
		
		cell.style.overflow = "hidden";
		cell.style.textOverflow = "ellipsis";
		cell.style.cursor = "default";
		cell.style.textAlign = "center";
		cell.appendChild(acomp);
	}
	
	/**
	 * 单据状态Render
	 */
	function MyBillStateRender(){}
	MyBillStateRender.render = function(rowIndex, colIndex, value, header, cell)
	{	
		var widget = pageUI.getWidget("mybillwidget");
		var ds = widget.getDataset("billds");
		var caption = "";
		if(value == null || value == 0)
			caption = "启动";
		else if(value == 1)
			caption = "完成";
		else if(value == 2)	
			caption = "挂起";
		cell.style.overflow = "hidden";
		cell.style.textOverflow = "ellipsis";
		cell.style.cursor = "default";
		cell.style.textAlign = "center";
		cell.innerHTML  = "<center>" + caption + "<center>" ;
	}
	
	
	function MyJobStateRender(){}
	MyJobStateRender.render = function(rowIndex, colIndex, value, header, cell)
	{	
		var widget = pageUI.getWidget("myjobwidget");
		var ds = widget.getDataset("jobds");
		var caption = "";
		if(value == null || value == 0)
			caption = "启动";
		else if(value == 1)
			caption = "完成";
		else if(value == 2)	
			caption = "挂起";
		cell.style.overflow = "hidden";
		cell.style.textOverflow = "ellipsis";
		cell.style.cursor = "default";
		cell.style.textAlign = "center";
		cell.innerHTML  = "<center>" + caption + "<center>" ;
	}
	
	
	//支付状态
	function MyDjPayFlagRender(){}
	MyDjPayFlagRender.render = function(rowIndex, colIndex, value, header, cell)
	{	
		cell.style.overflow = "hidden";
		cell.style.textOverflow = "ellipsis";
		cell.style.cursor = "default";
		cell.style.textAlign = "center";
		var caption = "";
		if(value == 20)
			caption ="${ml:trans('WebBxMainPagePayFlag05')}";
		else if(value == 99)
			caption = "${ml:trans('WebBxMainPagePayFlag06')}";
		else{
			var arr = ["","${ml:trans('WebBxMainPagePayFlag01')}","${ml:trans('WebBxMainPagePayFlag02')}","${ml:trans('WebBxMainPagePayFlag03')}","${ml:trans('WebBxMainPagePayFlag04')}"];
			caption = arr[value];
		}
		cell.innerHTML  = "<center>" + caption + "<center>" ;
	}
	
	/**
	 * 打开我的代办事务
	 */
	function openMyJobNew(ds,row){
		var pk_job = row.getCellValue(ds.nameToIndex("pk_job"));
		var funnode = row.getCellValue(ds.nameToIndex("funcode"));
		var url = row.getCellValue(ds.nameToIndex("funurl"));
		var funcode = row.getCellValue(ds.nameToIndex("funcode"));
	 	var title = row.getCellValue(ds.nameToIndex("menuitemname"));
	 	var maindsid = row.getCellValue(ds.nameToIndex("maindsid"));
	  	var src = window.corePath + "/" + url + "&openBillId=" + pk_job + "&openDsId=" + maindsid;
	   	var openTabMax = 5;
	 	var widget = parent.pageUI.getWidget("mainwidget");
		var tab = parent.pageUI.getTab("pagetab");
		var items = tab.getTabItems();
		var count = items.length;
		for (var i = 0; i < count; i++) {
			// 如果已经打开过相同funcode的单据
			if (items[i].name == funcode) {
				tab.activeTab(tab.getItemIndex(items[i]));
				return;
			}
		}

		// 如果已经打开单据数超过设置的打开最大限度则进行提示
		if (count > openTabMax) 
		{
			continueOpen.tab = tab;
			continueOpen.funcode = funcode;
			continueOpen.title = title;
			continueOpen.src = src;
			ConfirmDialogComp.showDialog("已经打开了 " + (count - 1)
							+ "个布局,继续打开将影响客户端速度,确定打开吗?",
					continueOpen, null, null, null);
		}
		else {
			var item1 = tab.createItem(funcode, title, true);
			tab.activeTab(tab.getItemIndex(item1));
			item1.getObjHtml().appendChild(getNodeFrame(funcode, src));
		}			
					
		function continueOpen()
		{
			var	tab = continueOpen.tab;
			var	funcode = continueOpen.funcode;
			var	title = continueOpen.title;
			var	src = continueOpen.src;
			var item1 = tab.createItem(funcode, title, true);
			tab.activeTab(tab.getItemIndex(item1));
			var div = getNodeFrame(funcode, src);
			item1.getObjHtml().appendChild(div);
			delete(continueOpen.tab);
			delete(continueOpen.funcode);
			delete(continueOpen.title);
			delete(continueOpen.src);
		};
		
		function getNodeFrame(funcode,src)
		{
			var div = $ce("DIV");
		    div.id = "tab_item_div_" + funcode;
		    div.style.top = "0px";
		    div.style.left = "0px";                        
		    div.style.height = "100%";
		    div.style.width = "100%";
		    
		    var frame=document.createElement("iframe");
			frame.style.top = "0px";
		    frame.style.left = "0px";                        
		    frame.style.height = "100%";
		    frame.style.width = "100%";
		    frame.style.border = "0";
		    if(!IS_IE)
		    	frame.style.background = "#FFFFFF";
		    frame.frameBorder = "0";
			frame.src=src;
			div.appendChild(frame);
			return div;
		};
	}
	
	/**
	 * 打开我的单据
	 */
	function openMyBillNew(ds,row){
		var pk_bill = row.getCellValue(ds.nameToIndex("pk_bill"));
		var funnode = row.getCellValue(ds.nameToIndex("funcode"));
		var url = row.getCellValue(ds.nameToIndex("funurl"));
		var funcode = row.getCellValue(ds.nameToIndex("funcode"));
	 	var title = row.getCellValue(ds.nameToIndex("menuitemname"));
	 	var maindsid = row.getCellValue(ds.nameToIndex("maindsid"));
	  	var src = window.corePath + "/" + url + "&openBillId=" + pk_bill + "&openDsId=" + maindsid;
	   	var openTabMax = 5;
	 	var widget = parent.pageUI.getWidget("mainwidget");
		var tab = parent.pageUI.getTab("pagetab");
		var items = tab.getTabItems();
		var count = items.length;
		for (var i = 0; i < count; i++) {
			// 如果已经打开过相同funcode的单据
			if (items[i].name == funcode) {
				tab.activeTab(tab.getItemIndex(items[i]));
				return;
			}
		}

		// 如果已经打开单据数超过设置的打开最大限度则进行提示
		if (count > openTabMax) 
		{
			continueOpen.tab = tab;
			continueOpen.funcode = funcode;
			continueOpen.title = title;
			continueOpen.src = src;
			ConfirmDialogComp.showDialog("已经打开了 " + (count - 1)
							+ "个布局,继续打开将影响客户端速度,确定打开吗?",
					continueOpen, null, null, null);
		}
		else {
			var item1 = tab.createItem(funcode, title, true);
			tab.activeTab(tab.getItemIndex(item1));
			item1.getObjHtml().appendChild(getNodeFrame(funcode, src));
		}			
					
		function continueOpen()
		{
			var	tab = continueOpen.tab;
			var	funcode = continueOpen.funcode;
			var	title = continueOpen.title;
			var	src = continueOpen.src;
			var item1 = tab.createItem(funcode, title, true);
			tab.activeTab(tab.getItemIndex(item1));
			var div = getNodeFrame(funcode, src);
			item1.getObjHtml().appendChild(div);
			delete(continueOpen.tab);
			delete(continueOpen.funcode);
			delete(continueOpen.title);
			delete(continueOpen.src);
		};
		
		function getNodeFrame(funcode,src)
		{
			var div = $ce("DIV");
		    div.id = "tab_item_div_" + funcode;
		    div.style.top = "0px";
		    div.style.left = "0px";                        
		    div.style.height = "100%";
		    div.style.width = "100%";
		    
		    var frame=document.createElement("iframe");
			frame.style.top = "0px";
		    frame.style.left = "0px";                        
		    frame.style.height = "100%";
		    frame.style.width = "100%";
		    frame.style.border = "0";
		    if(!IS_IE)
		    	frame.style.background = "#FFFFFF";
		    frame.frameBorder = "0";
			frame.src=src;
			div.appendChild(frame);
			return div;
		};
	}
	
	/**
	 * 无需调用后台destroy方法
	 */
	function onBeforeDestroyPage()
	{
		return false;
	}
