/*******************************************************************************
 * @fileoverview 有关Dataset的常量信息
 * @author lkp
 * @version 1.0
 * @date 20070315
 ******************************************************************************/

IDatasetConstant = new Object;

IDatasetConstant.PREFIX_SYSTEM_QUERY_PARAM = "$$";

IDatasetConstant.QUERY_PARAM_KEYS = "query_param_keys";

IDatasetConstant.QUERY_PARAM_VALUES = "query_param_values";

IDatasetConstant.QUERY_PARAM_KEYVALUE = "query_param_keyvalue";
IDatasetConstant.QUERY_KEYVALUE = "query_keyvalue";
IDatasetConstant.QUERY_PARAM_PAGEINDEX = "query_param_pageindex";

/* 此参数存储自拼装查询条件 */
IDatasetConstant.NORMAL_QUERY_CONDITiON = IDatasetConstant.PREFIX_SYSTEM_QUERY_PARAM
		+ "normal_query_condition";

/*
 * 在前台向后台发送查询请求时，通过该参数确定是否来自查询模版，如果来自查询模版则添加该参数，其值无意义。 如果不来自查询模版，可以不用设置。
 */
IDatasetConstant.FROM_QUERY_TEMPLATE = IDatasetConstant.PREFIX_SYSTEM_QUERY_PARAM
		+ "from_query_model";

/* 在前台dataset中，通过此key值存储通过查询模版获取的数据信息 */
IDatasetConstant.QUERY_TEMPLATE_KEYVALUES = "$%%$query_template_keyvalues";
/* 在查询模版中通过此参数传递被查询的datasetId,放到查询模版左边查询树的response-parameter中。 */
IDatasetConstant.QUERY_TEMPLATE_TARGET_DSID = IDatasetConstant.PREFIX_SYSTEM_QUERY_PARAM
		+ "query_template_target_dsid";
/* 通过此参数名称，在查询模版的handler处理时，将被查询dataset所在的pageId放到查询模版左边树形结构的response-parameter中。 */
IDatasetConstant.QUERY_TEMPLATE_TARGET_PAGEID = IDatasetConstant.PREFIX_SYSTEM_QUERY_PARAM
		+ "query_template_target_pageId";

/* 对于根一级的查询模版树记录的parentId设置为该默认值。 */
IDatasetConstant.QUERY_TEMPLATE_DEFAULT_ROOTPARENTID = "queryTemplateSpecialParentId";

/* 通过此参数存储通过查询模版生成的sql语句条件 */
IDatasetConstant.QUERY_TEMPLATE_CONDITION = IDatasetConstant.PREFIX_SYSTEM_QUERY_PARAM
		+ "query_template_condition";

IDatasetConstant.QUERY_TEMPLATE_LOGICCONDITION = IDatasetConstant.PREFIX_SYSTEM_QUERY_PARAM
		+ "query_template_logiccondition";

/* 用此参数表明当前发送的装载数据请求是否是装载的其他页面而不是当前处理页面。查询模版中会用到。 */
IDatasetConstant.IS_LOAD_OTHER_PAGE = "isLoadOtherPage";

/* 与上一参数配合使用，表明请求装载那个页面的数据 */
IDatasetConstant.OTHER_PAGE_ID = "otherPageId";

// 待审核的单据id
IDatasetConstant.APPROVE_BILLID = "approveid";
// 单据的审批类型 0=自己的单据，1=审批的单据
IDatasetConstant.APPROVE_TYPE = "approve_type";
