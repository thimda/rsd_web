/*****************************************************************
 * @fileoverview 用于完成对Dataset的校验,针对元数据而来的校验规则
 * @author lkp
 * @version 1.0
 * @date 20070718
 * @modified by dengjt 进行优化，去掉大量map的生成与array的引用，可能会在多次保存后
 * 导致内存占用剧增
 * @modified by luoyf 添加字段非空校验
 ******************************************************************/

//DatasetRuleChecker = new Object;
//NOT_NULL_RULE = "not_null_rule";
//DatasetRuleChecker.keyMap = new HashMap();
//DatasetRuleChecker.keyMap.put(DatasetRuleChecker.NOT_NULL_RULE, ml_nextfieldscannotnull);
/**
 * 对dataset的某行数据进行校验
 * 
 * @param dataset
 * @param row
 * @return 如果违反规则返回一个字符串数组，字符串是行号和出错信息拼接成的，中间以分号分隔，例如：3;不能大于5000 否则 返回NULL
 */
function checkDatasetRow(dataset, row) {
	//通用逻辑校验
	// if(typeof generalLogicCheck != "undefined")
	// generalLogicCheck();
	// 前台用户的业务校验
	return $rowRuleCheck(dataset, row);

};

/**
 * 对Dataset 单元格的增加和修改时进行校验
 * 
 * @param dataset
 * @param value
 * @param cellIndex
 * @param row
 * @return 如果违反规则返回一个出错信息字符串 否则 返回NULL
 */
function checkDatasetCell(dataset, value, cellIndex, row) {
	if (typeof generalLogicCheck != "undefined")
		generalLogicCheck();
	// 前台的业务校验
	if (typeof specialCellLogicCheck != "undefined") {
		var result = specialCellLogicCheck(dataset, value, cellIndex, row);
		if (result != null && typeof result == "string")
			return result;
		else
			return null;
	}
	return $cellRuleCheck(dataset, value, cellIndex);
};

/**
 * 对给定的dataset进行增加和修改数据的校验。
 * @param dataset,isCheckDsAllRows
 * @return 返回Map，如果没有违反规则的纪录，则内容为空。
 *         如果存在，则key:对应的错误行,value:是由上方法返回的一个Map。
 * 
 */
function checkDataset(dataset) {
	return $checkDsAllRows(dataset);
	/*
	 * var updateRows = dataset.getUpdatedRows(); var addRows =
	 * dataset.getNewAddedRows(); var resultArr = null; for(var i = 0, count =
	 * updateRows.length; i < count; i++) { var arr = checkDatasetRow(dataset,
	 * updateRows[i]); var rowIndex =
	 * dataset.getRowIndex(updateRows[i],null,null); if(arr != null){
	 * if(resultArr == null) resultArr = new Array(); for(var j = 0; j <
	 * arr.length; j ++){ if(resultArr.indexOf(arr[j]) == -1)
	 * resultArr.push(rowIndex + ";" + arr[j]); } } }
	 * 
	 * for(var i = 0; i < addRows.length; i++) { var arr =
	 * checkDatasetRow(dataset, addRows[i]); var rowIndex =
	 * dataset.getRowIndex(addRows[i],null,null); if(arr != null){ if(resultArr ==
	 * null) resultArr = new Array(); for(var j = 0; j < arr.length; j ++){
	 * if(resultArr.indexOf(arr[j]) == -1) resultArr.push(rowIndex + ";" +
	 * arr[j]); } } } return resultArr;
	 */
};

/**
 * 校验Dataset所有行
 * @param ds
 * @return
 * @private
 */
function $checkDsAllRows(ds) {
	var allRows = ds.getRows();
	var resultArr = null;
	for ( var i = 0, count = allRows.length; i < count; i++) {
		var arr = checkDatasetRow(ds, allRows[i]);
		var rowIndex = ds.getRowIndex(allRows[i], null, null);
		if (arr != null) {
			if (resultArr == null)
				resultArr = new Array();
			for ( var j = 0; j < arr.length; j++) {
				if (resultArr.indexOf(arr[j]) == -1)
					resultArr.push(rowIndex + ";" + arr[j]);
			}
		}
	}
	return resultArr;
};

/*
 * 通用业务逻辑校验
 * 
 * @param
 * @return 如果违反规则返回一个字符串数组，字符串是行号和出错信息拼接成的，中间以分号分隔，例如：3;不能大于5000 否则 返回NULL
 */
// function generalLogicCheck()
// {
// return null;
// }

/**
 * 检验cell
 * @private
 */
function $cellRuleCheck(dataset, value, cellIndex) {
	var meta = dataset.metadata[cellIndex];
	// not null check
	var nullAble = meta.nullAble;
	if (nullAble != null && nullAble == false) {
		if (value == null || value == "") {
			return trans('ml_thisfieldcannotnull');
		}
	}
	var result;
	// 格式校验
	var fm = meta.formater;
	if (fm != null) {
		if (value != null)
			result = $$formaterCherk(fm, value);
	}
	if (result != null)
		return result;
	// 范围校验
	var maxValue = meta.maxValue;
	var minValue = meta.minValue;
	// TODO
	if (maxValue != null || minValue != null) {
		if (value != null)
			result = $$scopeCherk(value, meta.dataType, maxValue, minValue);
	}
	if (result != null)
		return result;
	return "";
};

/**
 * specialCellLogicCheck返回值规则:
 * 1、返回true表明没有校验错误,进行进一步默认校验。
 * 2、返回false或者不返回均不做默认检验
 * 3、返回字符串则表明出错,打印该错误信息 
 * @private
 */
function $rowRuleCheck(dataset, row) {
	var meta = dataset.metadata;
	var errorArray = null;
	var hasSelfChecker = (typeof specialCellLogicCheck != "undefined");
	for ( var i = 0, count = meta.length; i < count; i++) {
		var value = row.getCellValue(i);
		var errMsg = true;
		if (hasSelfChecker) {
			errMsg = specialCellLogicCheck(dataset, value, i, row);
		}
		//返回true，表明没有错误，并需要再进行默认检查
		if (errMsg == true) {
			if ((row.state == DatasetRow.STATE_NEW)
					|| (row.state == DatasetRow.STATE_UPD)) {
				if (meta[i].isForeignKey || meta[i].isPrimaryKey)
					continue;
			}
			//not null check	
			var nullAble = meta[i].nullAble;
			if (nullAble != null && nullAble == false) {
				if (value == null || value == "")
					errMsg = trans('ml_nextfieldscannotnull');
			}
			//formater check
			var fm = meta[i].formater;
			if (fm != null) {
				if (value != null && value != "")
					errMsg = $$formaterCherk(fm, value);
			}
		}
		if (errMsg != null && typeof errMsg == "string") {
			if (errorArray == null)
				errorArray = new Array;
			errorArray.push(i + ";" + errMsg);
		}
	}
	return errorArray;
};

/**
 * formater 校验
 * @param fm
 * @param value
 * @return
 * @private
 */
function $$formaterCherk(fm, value) {
	var returnstr = null;
	switch (fm) {
	case "email":
		if (!isEmail(value))
			returnstr = trans("ml_rule_email");
		break;
	case "number":
		if (!isNumber(value))
			returnstr = trans("ml_rule_number");
		break;
	case "chn":
		if (!isChinese(value))
			returnstr = trans("ml_rule_chn");
		break;
	case "variable":
		if (!isValidIdentifier(value))
			returnstr = trans("ml_rule_variable");
		break;
	case "phone":
		if (!isPhone(value))
			returnstr = trans("ml_rule_phone");
		break;
	default:
		if (fm != null && fm != '') {
			var f = eval(decodeURIComponent(fm));
			if (!f.test(value))
				returnstr = trans("ml_rule_other");
		}
		break;
	}
	return returnstr;
};

/**
 * 范围 校验
 * @param value
 * @param dataType
 * @param maxValue
 * @param minValue
 * @return
 * @private
 */
function $$scopeCherk(value, dataType, maxValue, minValue) {
	if (dataType) {
		if (dataType == DataType.STRING || dataType == DataType.CHAR
				|| dataType == DataType.CHARACTER) { // 字符串
			if (maxValue != null && value.length > maxValue)
				return "长度超出范围，最大值为:" + maxValue;
			if (minValue != null && value.length < minValue)
				return "长度超出范围，最小值为:" + minValue;
		} else if (dataType == DataType.INTEGER || dataType == DataType.INT
				|| dataType == DataType.DOUBLE || dataType == DataType.dOUBLE
				|| dataType == DataType.UFDOUBLE || dataType == DataType.FLOAT
				|| dataType == DataType.fLOAT || dataType == DataType.BOOLEAN
				|| dataType == DataType.bOOLEAN
				|| dataType == DataType.UFBOOLEAN
				|| dataType == DataType.BIGDECIMAL || dataType == DataType.LONG
				|| dataType == DataType.lONG
				|| dataType == DataType.UFNUMBERFORMAT
				|| dataType == DataType.Decimal
				|| dataType == DataType.UFDATETIME
				|| dataType == DataType.UFDATE || dataType == DataType.UFTIME) { // 数字
			if (maxValue != null && value > maxValue)
				return "超出范围，最大值为:" + maxValue;
			if (minValue != null && value < minValue)
				return "超出范围，最小值为:" + minValue;
		}
	}
	return null;
};
