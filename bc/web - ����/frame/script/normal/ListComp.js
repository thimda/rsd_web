/******************************************
*													 
*	列表框控件。
*	控件可设置多选和单选。                        
*	                                                 
*   @author dengjt,gd 
*   @author gd 加入对ds的支持 2008-07-01
*   @version NC5.5                               
*                                                    
*******************************************/
ListComp.prototype = new BaseComponent;
ListComp.prototype.componentType = "LIST";

/**
*	ListComp构造函数
*   @param parent     父容器对象或者父容器本身                
*   @param name 	  此对象的名称                             
*	@param left		  控件左边界，整型                          
*   @param top		  控件上边界，整型                           
*　　@param width	  控件宽度．可为绝对值或百分比              
*	@param height	  控件高度.可为绝对值和百分比             
*	@param mutiSel	  是否多选                           
*	@param position	  css定位属性．默认absolute       
*   @param className  使用自定义css替换默认css          
*/
function ListComp(parent, name, left, top, width, height, multiSel, position, className)
{
	if (arguments.length == 0)
		return;
	this.base = BaseComponent;
	this.base(name, left, top, width, height);  
	this.parentOwner = parent;
	this.multiSel = getBoolean(multiSel, false);	
	this.position = getString(position, "absolute");
	this.className = getString(className, "list_div");
	
	//存放选中项的索引值
	this.selectedItemsIndex = null;
	//存放选中的项
	this.selectedItems = null;
	//存放所有项
	this.allItems = new Array();
	this.tabIndex = ListComp.prototype.tabIndex ++;
	this.create();
};

/**
*	ListComp主体创建函数
*/
ListComp.prototype.create = function()
{
	this.Div_gen = $ce("DIV");
	this.Div_gen.owner = this;
	this.Div_gen.id = this.name;
	this.Div_gen.className = this.className;
	//自动显示滑动按钮
	this.Div_gen.style.overflow = "auto";	
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left;
	this.Div_gen.style.top = this.top;
	this.Div_gen.style.width = "100px";//this.width;
	this.Div_gen.style.height = "200px";//this.height;
	this.Div_gen.onselectstart = function()
	{
		return false;
	};
	if(this.parentOwner)
		this.placeIn(this.parentOwner); 
};

/**
 * 设置list控件绑定的comboData
 */
ListComp.prototype.setComboData = function(comboData)
{
	if(!comboData)
		return;
	var nameArr = comboData.getNameArray();
	var valueArr = comboData.getValueArray();
	if(nameArr != null)
	{
		for(var i = 0, count = nameArr.length; i < count; i ++)
			this.createOption(nameArr[i], valueArr[i], null, false, null);
	}	
};


/**
 *	添加给定的option子项
 */
ListComp.prototype.addOptionItem = function(caption, value, refImg, selected, index)
{	
	caption = getString(caption, "default");
	if(value == null || value == "")
		return;
	this.createOption(caption, value, refImg, selected, index);
};

/**
*	创建option项
*	@param caption   显示值
*	@param value     真实值
*	@param refImg    显示在每个option项最左边的图片
*	@param selected  是否为选中状态
*	@param index     可以不指定index值.若指定,为此option项在所有options中的位置
* 	@private
*/
ListComp.prototype.createOption = function(caption, value, refImg, selected, index)
{		
	if(index == null || index == -1 || index == "")
	{
		var option = new OptionComp(caption, value, refImg, selected);
		if(this.allItems == null)
		{
			this.allItems = new Array();
		}	
			
		//保存新创建的item的对象
		this.allItems.push(option);
		option.setParentOwner(this);
		if(selected)
		{	
			if(this.selectedItemsIndex == null)
			{
				this.selectedItemsIndex = new Array();
				this.selectedItems = new Array();
			}
			
			this.selectedItemsIndex.push(this.allItems.indexOf(option));
			this.selectedItems.push(option);
			
			option.getObjHtml().className = "list_new";
		}
		this.Div_gen.appendChild(option.Div_gen);
		return option;
	}
	//若给定index参数
	else
	{	 
		if(this.allItems == null)
		{
			this.allItems = new Array();
		}	
		
		index = parseInt(index);
		if(index < 0 || index > this.allItems.length)
			return;
			
		var option = new OptionComp(caption, value, refImg, selected);
		//不等于-1说明此位置上已经有item项,这时要把此位置后的item统一向后移动
		if(this.allItems[index] != null)
		{	
			//保存删除掉的item的临时数组
			var tempItemsArr = new Array();
			var totalLength = this.allItems.length;
			
			//将要删除的items保存到临时数组中
			for(var i = index; i < totalLength; i++)
			{	
				tempItemsArr.push(this.allItems[i]);
			}
			
			//从要插入的位置删除掉以后的所有元素
			for(var i = index; i < totalLength; i++)
			{	
				this.allItems.splice(index, 1);
				
			}
			
			//将要插入的值放到全局数组中
			this.allItems.push(option);
			option.setParentOwner(this);
			
			//若此item被选中
			if(selected)
			{	
				if(this.selectedItemsIndex == null)
				{
					this.selectedItemsIndex = new Array();
					this.selectedItems = new Array();
				}
				
				this.selectedItemsIndex.push(this.allItems.indexOf(option));
				this.selectedItems.push(option);
				option.getObjHtml().className = "option_click";
			}
			
			//删除掉显示对象
			for(var i = 0; i < tempItemsArr.length; i++)
			{	
				this.Div_gen.removeChild(tempItemsArr[i].getObjHtml());
			}
			
			//将插入的item显示到他的插入位置
			this.Div_gen.appendChild(option.Div_gen);
			
			//将刚刚删掉的item放到插入全局数组中(item之后)并重新显示到界面上
			for(var i = 0; i < tempItemsArr.length; i++)
			{	
				this.allItems.push(tempItemsArr[i]);
				this.Div_gen.appendChild(tempItemsArr[i].getObjHtml());
			}
		}
		else
		{	
			//保存新创建的item的索引和对象
			this.allItems.push(option);
			option.setParentOwner(this);
			//若此item被选中
			if(selected)
			{	
				if(this.selectedItemsIndex == null)
				{
					this.selectedItemsIndex = new Array();
					this.selectedItems = new Array();
				}
				
				this.selectedItemsIndex.push(this.allItems.indexOf(option));
				this.selectedItems.push(option);
				option.getObjHtml().className = "option_click";
			}
			this.Div_gen.appendChild(option.Div_gen);
		}
		return option;
	}
};

/**
*	设置选中的item项
*	@param private   由OptionComp调用此函数
*	@param itemOwner 要设置选中项的数据对象
*/
ListComp.prototype.setItemSelected = function(itemOwner)
{
	//若此时this.selectedItems不为null,说明此时有多行被选中,此时应该清掉所有的选择行
	if(this.selectedItems != null)
	{	
		for(var i = 0; i < this.allItems.length; i++)
		{	
			this.allItems[i].Div_gen.className = "option_unsel";
			this.selectedItems.splice(0, 1);	//将选择数组清空
			this.selectedItemsIndex.splice(0, 1);	//将索引数组清空
		}	
	}
	this.selectedItemsIndex = new Array();
	this.selectedItems = new Array();
	itemOwner.Div_gen.className = "option_click";
	
	this.selectedItemsIndex[0] = this.allItems.indexOf(itemOwner);
	this.selectedItems[0] = itemOwner;
	
	//调用用户重载的方法
	this.valueChanged(this.selectedItems);
};

/**
*	设置选中多个item项,按住ctrl按钮时调用此方法
*   @param private 由OptionComp调用此方法
*/
ListComp.prototype.addItemSelected = function(itemOwner)
{
	if(this.selectedItems == null)
	{	
		this.selectedItemsIndex = new Array();
		this.selectedItems = new Array();
	}
	
	//在list中的实际位置
	var posi = this.allItems.indexOf(itemOwner);
	var index = -1;
	//若不等于-1说明选中项中已经存在item,此时应使此项变为"未选择"状态
	if((index = this.selectedItemsIndex.indexOf(posi)) != -1)
	{	
		//从选择数组中删掉此item
		this.selectedItemsIndex.splice(index, 1);
		this.selectedItems.splice(index, 1);
		
		itemOwner.Div_gen.className = "option_unsel";
	}
	//此时说明此项未被选择过,应变为选择状态
	else
	{	
		this.selectedItemsIndex.push(posi);
		this.selectedItems.push(itemOwner);
		itemOwner.Div_gen.className = "option_click";
	}
	
	//调用用户重载的方法
	this.valueChanged(this.selectedItems);
};


ListComp.prototype.setValue = function(value){
	
};

ListComp.prototype.setActive = function(value){
	
};
/**
*	得到所有选中的项
*   @return 所有选中的item
*/
ListComp.prototype.getSelectedItems = function()
{
	return this.selectedItems;
};

/**
*	得到选中的项的索引值
*	@return 存放所有选中item项的index值数组
*/
ListComp.prototype.getSelectedIndex = function()
{	
	var indexArr = null;
	if(this.selectedItems != null)
	{	
		indexArr = new Array();
		for(var i = 0; i < this.selectedItems.length; i++)
		{	
			indexArr.push(this.allItems.indexOf(this.selectedItems[i]));
		}
	}	
	return indexArr;
};

/**
*	得到选中项的value
*	@return 存放所有选中item项的value值数组
*/
ListComp.prototype.getSelectedValue = function()
{	
	var valueArr = new Array();
	for(var i =0; i < this.selectedItems.length; i++)
	{	
		valueArr.push(this.selectedItems[i].value);
	}
	return valueArr;
};

/**
*	得到选中项的caption(显示值)
*	@return 存放所有选中item项的caption数组
*/
ListComp.prototype.getSelectedCaption = function()
{	
	var captionArr = new Array();
	for(var i =0; i < this.selectedItems.length; i++)
	{
		captionArr.push(this.selectedItems[i].caption);
	}
	return captionArr;
};


/************************************************	
*	用户可重载函数定义
*************************************************/
/**
*	单击item子项后的用户重载函数
*/
ListComp.prototype.valueChanged = function(item)
{
};

/**
*	双击item子项后的用户重载函数
*/
ListComp.prototype.dblvalueChange = function(item)
{
};