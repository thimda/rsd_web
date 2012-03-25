/**
 * @fileoverview 单据的操作类型。
 * nc.ui.trade.base.IBillOperate
 * @author dengjt
 */
var IBillOperate = new Object;
//初始化按钮处理
IBillOperate.OP_INIT = "0";
//非编辑时按钮处理
IBillOperate.OP_SINGLESEL = "1";
IBillOperate.OP_MULTISEL = "2";
//编辑时按钮处理
IBillOperate.OP_EDIT = "3";
//单据新增状态
IBillOperate.OP_ADD = "4";
IBillOperate.OP_ALL = "7";

////参照单据时新增单据状态
//IBillOperate.OP_REFADD = 3;
////不能增加、只能修改非编辑时按钮处理
//IBillOperate.OP_NOADD_NOTEDIT = 5;
////不能增加、修改时按钮处理
//IBillOperate.OP_NO_ADDANDEDIT = 6;
//所有的功能都可用

//
//IBillOperate.OP_CONDITION1 = 8;
//IBillOperate.OP_CONDITION2 = 9;
//IBillOperate.OP_CONDITION3 = 10;

/******************************************************
 *
 * 平台的动作类型,和nc.ui.trade.businessaction.IPFACTION
 * 保持一致
 * @author gd 2007-11-15 
 * 
 ******************************************************/
//var IPfAction = new Object;
//    IPfAction.SAVE = "WRITE";
//	IPfAction.APPROVE = "APPROVE";
//	IPfAction.COMMIT = "SAVE";
//	IPfAction.DELETE = "DELETE";
//	IPfAction.EDIT = "EDIT";
//	IPfAction.UNAPPROVE = "UNAPPROVE";
//	IPfAction.FREEZE = "FREEZE";
//	IPfAction.UNFREEZE = "UNFREEZE";
//	IPfAction.END = "END";
//	IPfAction.UNEND = "UNEND";
//	IPfAction.CX = "CX";
//	IPfAction.UNCX = "UNCX";	