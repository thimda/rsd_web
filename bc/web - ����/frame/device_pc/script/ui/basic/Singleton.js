/**
 * 单例类
 * @class 单例类。须控制单例的对象可利用此类。比如日期控件。
 * @param create
 * @return
 */
function Singleton(create) {
	if (window.sys_singleMap == null)
		window.sys_singleMap = new HashMap();
	var instance = window.sys_singleMap.get(this.componentType);
	if (instance != null)
		return instance;
	else {
		create = getBoolean(create, true);
		if (!create)
			return null;
		window.sys_singleMap.put(this.componentType, this);
		return null;
	}
};

