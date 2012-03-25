function getLoadDsParams() {
	var nodeInfo = parent.getRefNodeInfo(getParameter('nodeId'));
	if(nodeInfo != null && nodeInfo.filterSql != null && nodeInfo.filterSql != "")
		return "filterSql=" + nodeInfo.filterSql;
	return null;
}