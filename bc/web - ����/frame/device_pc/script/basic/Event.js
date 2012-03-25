/**
 * @fileoverview 事件传播处理函数
 * @author gd
 * @version 1.2
 */

/**
 * 不在分派事件
 * 
 * @param e 事件对象
 */
function stopEvent(e) {
	// 该方法将停止事件的传播,阻止它被分派到其他Document节点.在事件传播的任何阶段都可以调用它
	// stopPropagation()是DOM事件的核心方法,用于阻止将来事件的冒泡
	if (e.stopPropagation)
		e.stopPropagation();
	else {
		// cancelBubble是IE中事件的方法,将其设为true,将会停止事件向上冒泡
		e.cancelBubble = true;
	}
};

/**
 * 取消事件的默认动作. 2级DOM Events 该方法将通知Web浏览器不要执行与事件关联的默认动作(如果存在这样的动作)
 * 
 * @param e 事件对象
 */
function stopDefault(e) {
	//prevetnDefault()是DOM事件的核心方法,用于阻止事件的默认行为
	if (e.preventDefault)
		e.preventDefault();
	else {
		//returnValue是IE中事件的默认属性,将其设置为false以取消事件的默认动做
		e.returnValue = false;
	}
};

/**
 * 取消事件分派和默认动作
 * @param e
 * @return
 */
function stopAll(e) {
	stopEvent(e);
	stopDefault(e);
};

/**
 * 获取目标。在IE中目标包含在event对象的srcElement属性中 在DOM兼容的浏览器中,目标包含在target属性中
 * 注:位于事件中心的对象称为目标(target)
 * 
 * @param e 事件对象
 */
function getTarget(e) {
	return e.target || e.srcElement;
};
