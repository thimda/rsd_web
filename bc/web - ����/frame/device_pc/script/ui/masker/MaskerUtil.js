/**
 * 将结果输出成HTML代码
 * @param {} result
 * @return {String}
 */
function toColorfulString(result){
  var color;
  if (!result) {
    return '';
  }
  if (result.color == null) {
    return result.value;
  }
  color = result.color;
  return '<font color="' + color + '">' + result.value + '<\/font>';
};

/**
 * 格式解析后形成的单个格式单元  
 * 适用于基于拆分算法的AbstractSplitFormat，表示拆分后的变量单元
 */
StringElement.prototype = new Object();
function StringElement(value) {
	this.value = value;
};
StringElement.prototype.value = "";

StringElement.prototype.getValue = function(obj) {
	return this.value;
};
/**
*格式结果
*/
FormatResult.prototype = new Object ;
/**
*默认构造方法
*/
function FormatResult(value, color){
  this.value = value;
  this.color = color;
};
