SilverlightWidget.prototype = new BaseComponent;
SilverlightWidget.prototype.componentType = "SILVERLIGHTWIDGET";


function SilverlightWidget(parent, name, left, top, width, height, position, className) {
    this.base = BaseComponent;
    this.base(name, left, top, width, height);
    this.parentOwner = parent;
    this.position = position;
    this.overflow = 'auto';
    this.className = className;
    this.create();
    this.instance = null;
}

SilverlightWidget.prototype.create = function () {
    this.Div_gen = $ce("DIV");
    this.Div_gen.id = this.id;
    this.Div_gen.style.left = this.left + "px";
    this.Div_gen.style.top = this.top + "px";
    this.Div_gen.style.width = this.width;
    this.Div_gen.style.height = this.height;
    this.Div_gen.style.position = this.position;
    this.Div_gen.style.overflow = this.overflow;
    if (this.className && this.className != null) {
        this.Div_gen.className = this.className;
    }
    if (this.parentOwner)
        this.placeIn(this.parentOwner);

};
/**
* 获取对象信息
* 
* @private  
*/
SilverlightWidget.prototype.getContext = function () {
    var context = new Object;
    context.c = "SilverlightWidgetContext";
    return context;
}
/**
* 初始化
*/
SilverlightWidget.prototype.Init = function (instanceName, view) {
    silverlightObjects.push(this.id + "," + instanceName + "," + view);

};
/**
* 获取对象信息
* 
* @private  
*/
SilverlightWidget.prototype.MappingContext = function (sl) {
    var mappingData = this.getContextMapping();
    for (var i = 0; i < mappingData.views.length; i++) {
        var view = mappingData.views[i];
        var viewName = view.viewName;
        var viewComps = view.components;
        for (var j = 0; j < viewComps.length; j++) {
            //这里需要添加规则Mapping
            var gridLis = new GridCellListener();
            gridLis.onCellClick = function (e) {
                sl.Color = e.cell.innerText;
            }
            var grid1 = pageUI.getWidget(viewName).getComponent(viewComps[i].cName);
            grid1.addListener(gridLis);
            sl.ChangeColorHandler = function (source, arg) {
                var index = getrowIndex(source.Color);
                grid1.setFocusIndex(index);
                grid1.model.setRowSelected(index);
            }
            //
        }
    }
}
SilverlightWidget.prototype.getContextMapping = function () {
    //test data 
    //mappingdata
    return mappingData;
}
var silverlightObjects = new Array();
function onSilverlightLoad(sender) {
    for (var i = 0; i < silverlightObjects.length; i++) {
        var slInstance = silverlightObjects[i];
        var v = slInstance.split(",");
        var slHost = document.getElementById(v[1]);
        var sl = this.instance = eval("slHost.Content." + v[1]);
        slO = pageUI.getWidget(v[2]).getComponent(v[0]);
        slO.MappingContext(sl);
    }
}


/**********************************************test data****************************************************/



var mappingData = { "views": [{ "viewName": "view2", "components": [{ "cName": "grid1", "cValue": [{ "name": "color"}]}]}] }
function getrowIndex(color) {
    var result = 0;
    if (color == "blue") { result = 0; }
    if (color == "red") { result = 1; }
    if (color == "green") { result = 2; }
    if (color == "black") { result = 3; }
    if (color == "pink") { result = 4; }
    return result;
}