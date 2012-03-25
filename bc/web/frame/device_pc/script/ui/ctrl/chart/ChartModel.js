/**
 * 统计图表数据模型
 * @param config 图表配置信息
 */
function ChartModel(config) {
	this.config = config;
	this.categories = null;
	this.datasets = null;
	this.colorrange = null;//仪表盘专用，颜色区间
	this.dials = null;//仪表盘专用，指针合集
	this.styles = null;//样式
	this.styleapplies = null;//样式应用
};

ChartModel.prototype.toXml = function() {
	// 系列类型
	var seriesType = getString(this.config.seriesType, ChartConfig.SINGLE_SERIES);
	var showType = this.config.showType;
	var bgcolor = this.config.bgcolor;
	if(!bgcolor)
		bgcolor = "FFFFFF";
	var showBorder = "0";
	if(seriesType != ChartConfig.Angular_SERIES){
		bgcolor = "ededed,fefefe"
		showBorder = "1";
	}
	var str = '<chart bgColor="'+ bgcolor +'"  bgAngle="270" borderThickness = "1" showBorder = "'+showBorder+'" borderColor="dce3e5"  labelDisplay="Rotate" slantLabels="1"  useRoundEdges ="1" '  
			+ ' canvasbgColor="FFFFFF"  canvasBorderThickness="1" canvasBorderColor="A5A6A6"   divLineIsDashed="1" divLineColor="E8E8E9" '
			+ ' zeroPlaneColor="000000" zeroPlaneThickness="1"  zeroPlaneAlpha="80" '
			+ ' showAlternateHGridColor="1" AlternateHGridColor="F7F7F7" '
			+ ' showPrintMenuItem="0" showAboutMenuItem="0" aboutMenuItemLabel = "关于用友" aboutMenuItemLink="n-http://www.ufida.com" ' ;
	
	if(this.config.clickUrl){
		var clickurl = this.config.clickUrl;
		//str += ' clickURL= "n-' + this.config.captionUrl + '"';
		str += ' clickURL= "' + clickurl + '" ';
	}
	var fontsize1 = 8;
	var fontsize2 = 12;
	var fontsize3 = 12;
	if(seriesType == ChartConfig.Angular_SERIES){
		var scale = this.config.sizescale;
		if(scale > 1.5)
			scale *= 0.8;
		var fontsize1 = parseInt(10 * scale);
		if(fontsize1 < 10)
			fontsize1 = 10
	
		var fontsize2 = parseInt(12 * scale);
		if(fontsize2 < 10)
			fontsize2 = 10;
			
		var fontsize3 = parseInt(12 * scale);
		if(fontsize3 < 12)
			fontsize3 = 12;
		
		if(GALLERY_RENDERER == 'javascript')
			str += ' displayValueDistance="15" minorTMHeight="6" ';
		else
			str += ' displayValueDistance="30" minorTMHeight=" 10" ';
		
		str += ' showShadow="0" fillAngle="45" upperLimit="' +
				this.config.maxvalue + '" lowerLimit= "' +
				this.config.minvalue + '" ' +
				'majorTMNumber="1" majorTMColor="767474" minorTMNumber="20"  ' +
				'minorTMColor="767474" majorTMHeight="8" showGaugeBorder="0" placeValuesInside="0" ' +
				'gaugeOuterRadius="63" gaugeInnerRadius="41" gaugeOriginX="93.5" gaugeOriginY="106.5" ' +
				'formatNumberScale="1"  decimalPrecision="2" tickMarkDecimalPrecision="2" ' +
				'pivotRadius="0" showPivotBorder="0" pivotBorderColor="fcf9f9" pivotBorderThickness="17" ' +
				'pivotBorderAlpha="50" pivotFillColor="b5b4b4" pivotFillMix="{818c92},{818c92},{light-10},{light-10},{light-50},{light-60},{eae9e9}" pivotFillRatio="15,5,5,15,50,7,3" pivotFillType="radial" '+
				'gaugeStartAngle="218" gaugeEndAngle="-38" showTickMarks="0" showTickValues="1" '+ 
				'gaugeFillMix="{dark-2},{light-2},{light-2},{light-20},{light-20},{light-80},{light-10},{dark-20}" ' +
				'gaugeFillRatio="5,5,20,30,10,20,5,5" autoScale="1" origW="186" origH="189">'
				
	}
	else if(seriesType == ChartConfig.Dual_SERIES){
		str += ' caption="' + this.config.caption + '" xAxisName="' + this.config.xAxisName 
			+ '" PYAxisName="' + this.config.yAxisName 
			+ '" SYAxisName="' + this.config.SYAxisName + '" numberPrefix="' + this.config.numberPrefix + '" showValues="0" ';
	}
	else
		str += ' caption="' + this.config.caption + '" xAxisName="' + this.config.xAxisName + '" yAxisName="' + this.config.yAxisName + '" numberPrefix="' + this.config.numberPrefix + '" showValues="0" ';
	
	if(showType == ChartConfig.ShowType_Doughnut_2D || showType == ChartConfig.ShowType_Doughnut_3D){
		if(this.config.rscale){
			var r = 0;
			if(this.config.offsetwidth > this.config.offsetheight * 1.3){
				r = this.config.offsetheight / 2.8;
			}
			else{
				r = this.config.offsetwidth / 3;
			}
			if(r && r> 0){
				var ir = r/this.config.rscale;
				if(ir > 0)
					str += ' pieRadius="' + r + '" doughnutRadius = "' + ir + '" ';
			}
		}
	}
		
	str += ' >\n';
	
	if (seriesType == ChartConfig.SINGLE_SERIES) {  // 单系列图形
		if (this.datasets != null && this.datasets.length >= 1) {
			str += this.datasets[0].toXml(this.config);
		}
		str += this.getStyleXml();
	}
	else if (seriesType == ChartConfig.MULTI_SERIES || seriesType == ChartConfig.Dual_SERIES) {  // 多系列图形
		if (this.categories != null) {
			str += "<categories>\n";
			for (var i = 0; i < this.categories.length; i ++) {
				str += this.categories[i].toXml();
			}
			str += "</categories>\n";
		}
		if (this.datasets != null) {
			
			for (var i = 0; i < this.datasets.length && i<20; i ++) {
				var dataset = this.datasets[i];
				str += '<dataset seriesName="' + dataset.seriesName + '" ';
				//获取render
				var renderas = this.config.getSeriesRender(dataset.seriesName);
				if(!renderas)
					if(this.config.datasetrender)
						renderas = this.config.datasetrender;
				
				if(renderas)
					str += ' renderAs = "' + renderas + '" ';

				//获取所属Y轴
				var parentYAxis =  this.config.getSeriesParentYAxis(dataset.seriesName);
				if(parentYAxis)
					str += ' parentYAxis = "' + parentYAxis + '" '
				str += ' color = "' + this.config.colorlist[i] + '" ';
				str +=' >\n';
				str += dataset.toXml();
				str += '</dataset>\n';
			}
		}
		str += this.getStyleXml();
		str += '<styles>'
    		 + '<definition>'
      		 + '<style name="datalable" type="font"  color="585656" bgColor="" isBold="0"/>'      		 
    		 + '</definition>'
    		 + '<application>'
    		 + '<apply toObject="DATALABELS" styles="datalable" />'      		 
    		 + '</application>'
  			 + '</styles>';
  			 
	}
	else if(seriesType == ChartConfig.Angular_SERIES){//仪表盘
		if(this.colorrange){
			var points = "<trendpoints> \n";
			str += " <colorRange>\n";
			var trendrmax = 70.0;
			var trendrmin = 58.0;
			for (var i = 0; i < this.colorrange.length; i ++) {				
				str += this.colorrange[i].toXml();
				if(this.colorrange[i].maxvalue && this.config.maxvalue){
					if(this.colorrange[i].maxvalue == this.config.maxvalue){
						continue;
					}
					var pr = 140;
					//仪表盘刻度点半径是 180度线最长，90度最短；需要计算出半径
					var angule =  218 - (this.colorrange[i].maxvalue - this.config.minvalue)/(this.config.maxvalue - this.config.minvalue) * 256.0;
					if(angule <= 0){
						angule *= -1;
					}
					else if(angule < 90){}
					else if(angule < 180){angule = 180 - angule;}
					else {angule = angule -180;};
					
					var trendr = trendrmax - (trendrmax - trendrmin)/90 * angule;
					/*
					if( this.config.sizescale > 1){
						trendr = trendr/ Math.pow(this.config.sizescale,1/2);
						if(this.config.sizescale > 1.6)
							trendr *= 1.2;
						else
							trendr *= 1.1;
					}
					*/
					if(this.config.sizescale < 1)
						trendr *=  Math.pow(this.config.sizescale,1/3);
					else
						trendr *=  Math.pow(this.config.sizescale,1/4);
						
					points += "<point startValue='" + this.colorrange[i].maxvalue
							+ "' color='666666' thickness='0' alpha='0' valueInside='0' radius='" 
							+ trendr + "' /> \n";
				}
			}
			str += " </colorRange>\n";
			points += "</trendpoints>\n";
			
			//str += points;
		}
		if(this.dials){
			
			str += " <dials> \n"
			for (var i = 0; i < this.dials.length; i ++) {
				str += this.dials[i].toXml(this.config.dialradius);
				
			}
			str += " </dials>\n";
			
			
		}
		
		str += ''
			+ '<annotations origW="186" origH="189" autoScale="1">'	
					
			+ '<annotationGroup id="Grp1" scaleImages="1">'
			+ '<annotation type="image" URL="' + window.frameGlobalPath + '/ui/ctrl/chart/angular.png" yScale="100" xScale="100"/>'
			+ '</annotationGroup>'
			+ '<annotationGroup id="Grp2" scaleImages="1" showBelowChart="0">'
			+ '<annotation type="image" URL="' +window.frameGlobalPath + '/ui/ctrl/chart/pivot.png" yScale="100" xScale="100"/>'
			+ '</annotationGroup>'
			
			+ '<annotationGroup id="Grp3" >'
			+ '<annotation type="arc" color="' 
			//+ this.dials[0].color + ',EEEEEE" x="93.5" y="106.5" radius="63" innerRadius="42" startAngle="240" endAngle="300" fillRatio="90,10"  fillPattern="Radial" showBorder="0"/>'
			+ this.dials[0].color + '" x="93.5" y="106.5" radius="63" innerRadius="42" startAngle="240" endAngle="300" fillRatio="100"  fillPattern="Radial" showBorder="0"/>'
    		+ '</annotationGroup>';
/*
		str += '<annotations origW="315" origH="315" autoScale="1">'
			+ '<annotationGroup xPos="155" yPos="148" showBelow="1">'
			+ '<annotation type="circle" color="EBF0F4,85898C,484C4F,C5C6C8" fillRatio="30,30,30,10" fillAngle="270" radius="150" fillPattern="linear" />'
			+ '<annotation type="circle"  color="8E8E8E,83878A,E7E7E7" fillAngle="270" radius="135" fillPattern="linear" />'
			+ '<annotation type="circle"  color="07476D,19669E,186AA6,D2EAF6" fillRatio="5,45,40,10" fillAngle="270" radius="132" fillPattern="linear" />'
			+ '<annotation type="circle"  color="07476D,19669E,07476D" fillRatio="5,90,5" fillAngle="270" radius="125" fillPattern="linear" />'
			+ '</annotationGroup>';
		 	*/
		 if(this.config.dialValue){
		 	/*
			str += '<annotationGroup xPos="118.7" yPos="157.2" showBelowChart="0">'
		 		+ '<annotation type="text"  label="' 
		 		//+  this.config.dialValue + '" fontColor="e4e4e6" fontSize="'
		 		+  this.config.dialValue + '" fontColor="ebebed" fontSize="'
		 		+ fontsize2 + '" isBold="1" font="Arial" />'
    			+ '</annotationGroup>';
		 	*/
		 	str += '<annotationGroup xPos="93.5" yPos="157" showBelowChart="0">'
		 		+ '<annotation type="text"  label="' 
		 		//+  this.config.dialValue + '" fontColor="e4e4e6" fontSize="'
		 		+  this.config.dialValue + '" fontColor="ebebed" fontSize="'
		 		+ fontsize2 + '" isBold="1" font="Arial" />'
    			+ '</annotationGroup>';
    		
		 }
		 if(this.config.caption){
		 	 var captionsize;
		 	 if(this.config.captionFont)
		 	 	captionsize = this.config.captionFont.size;
		 	 //if(!captionsize)
		 	 	captionsize = fontsize3;
		 	 str += '<annotationGroup xPos="93.5" yPos="15" showBelowChart="0">'
		 	 	+ '<annotation type="text" label="' 
		 	 	+ this.config.caption +'" fontColor="676666"  fontSize="'
		 	 	+ captionsize + '" isBold="1"/>'
    			+ '</annotationGroup>';		
		 }
		 str += "</annotations>";
		 str += '<styles>'
    		 + '<definition>'
      		 + '<style name="values" type="font"  size="' 
      		 + fontsize1 +'" color="585656" bgColor="" />'
      		 + '<style name="limitvalues" type="font"  size="'
      		 + fontsize2 +'" color="585656"  bgColor="" />'
    		 + '</definition>'
    		 + '<application>'
    		 + '<apply toObject="TICKVALUES" styles="values" />'
      		 + '<apply toObject="LIMITVALUES" styles="limitvalues" />'
      		 + '<apply toObject="TRENDVALUES" styles="limitvalues" />'
    		 + '</application>'
  			 + '</styles>';
	}
	
	str += " </chart>\n";
	return str;
};

ChartModel.prototype.getStyleXml=function(){
	var str = "<styles> \n";
	if(this.styles){
		str += "<definition> \n";
		for(var i=0;i< this.styles.length;i++){
			str += this.styles[i].toXml();
		}
		str += "</definition> \n";
	}
	if(this.styleapplies){
		str += "<application> \n";
		for( var i=0;i< this.styleapplies.length;i++){
			str += this.styleapplies[i].toXml();
		}
		str += "</application> \n"; 
	}
	str += "</styles> \n";
	return str;
}

ChartModel.prototype.addCategory = function(cate) {
	if(this.categories == null)
		this.categories = new Array;
	this.categories.push(cate);
};

ChartModel.prototype.addChartDataset = function(ds) {
	if(this.datasets == null)
		this.datasets = new Array;
	this.datasets.push(ds);
};
ChartModel.prototype.addColorRange = function(range){
	if(this.colorrange == null)
		this.colorrange = new Array;
	this.colorrange.push(range);
}
ChartModel.prototype.addDial = function(dial){
	if(this.dials == null)
		this.dials = new Array;
	this.dials.push(dial);
}
ChartModel.prototype.getChartDataset = function(dsname) {
	var ds = null;
	if(this.datasets != null)
		for(var i=0;i<this.datasets.length;i++ ){
			if(this.datasets[i].seriesName == dsname){
				 ds = this.datasets[i];
				 break;
			}
		}
	return ds;
}
ChartModel.prototype.addStyle = function(style){
	if(this.styles == null)
		this.styles = new Array;
	this.styles.push(style);	
}
ChartModel.prototype.addStyleApply = function(apply){
	if(this.styleapplies == null)
		this.styleapplies = new Array;
		
	var flag = false;
	for(var i=0;i<this.styleapplies.length;i++){
		if(this.styleapplies[i].toObject == apply.toObject){
			this.styleapplies[i] = apply;
			flag = true;
			break;
		}
	}
	if(!flag)
		this.styleapplies.push(apply);
}
ChartModel.prototype.getStyleApply = function(toobject){
	var curapply = null;
	if(this.styleapplies){
		for(var i=0;i<this.styleapplies.length;i++){
			if(this.styleapplies[i].toObject == toobject){
				curapply = this.styleapplies[i];
				break;
			}			
		}
	}
	return curapply;
}
/**
 * 分组信息
 */
function Category() {
	this.label = null;
};

Category.prototype.toXml = function() {
	var str = '<category label="' + this.label + '"';
	str += ' />\n';
	return str;
};

/**
 * 图表数据集合
 */
function ChartDataset(seriesName) {
	this.seriesName = seriesName;
	this.chartsets = null;
};

ChartDataset.prototype.toXml = function(config) {
	var str = "";
	if (this.chartsets != null) {
		for (var i = 0; i < this.chartsets.length && i<20; i ++) {
			if(config)
				str += this.chartsets[i].toXml(config.colorlist[i]);
			else
				str += this.chartsets[i].toXml();
		}
	}
	return str;
};

ChartDataset.prototype.addSet = function(set) {
	if (this.chartsets == null)
		this.chartsets = new Array;
	this.chartsets.push(set);
};

/**
 * 数据
 */
function ChartSet() {
	this.label = null;
	this.value = null;
	this.link = null;
	this.name = null;
	//TODO
//	this.toolTip = null;
//	this.link = null;
//	this.showLable = "1";
};

ChartSet.prototype.toXml = function(color) {
	var str = '<set value="' + this.value + '"';
	if (this.label != null)
		str += ' label="' + this.label + '"';
		
	if (this.name != null)
		str += ' name="' + this.name + '"';
	if (this.link != null)
		str += ' link = "' + this.link + '"';
	if(color)
		str += ' color = "' + color + '"';
		
		
	//TODO
//	if (this.toolTip != null)
//		str += ' toolTip="' + this.toolTip + '"';
//	if (this.link != null)
//		str += ' link="' + this.link + '"';
//	if (this.showLable != null)
//		str += ' showLable="' + this.showLable + '"';
	str += ' />\n';
	return str;
};
/**
 * 仪表盘颜色区间
 */
function ColorRange(){
	this.minvalue = null;
	this.maxvalue = null;
	this.color = null;
}
ColorRange.prototype.toXml = function(){
	var str = '<color ';
	if(this.minvalue)
		str += ' minvalue = "' + this.minvalue + '"';
	else
		str += ' minvalue = "0"';
	if(this.maxvalue)
		str += ' maxvalue = "' + this.maxvalue + '"';
	if(this.color)
		str += ' code = "' + this.color + '"';
	str += '/>\n';
	return str;
}
/**
 * 指针数据
 */
function Dialset(){
	this.value = null;
	this.color = null;	
	this.link = null;
}
Dialset.prototype.toXml = function(radius){
	var str = '<dial ';
	if(this.value)
		str += ' value="' + this.value + '" ';
	
	str += ' radius = "50" ';		
	
	//if(this.color)
		//str += ' bgColor = "000000,' + this.color + ',000000" ';
		str += ' bgColor = "454444,292828,454444" ';
	if(this.link )
		str += ' link = "' + this.link + '" ';
		
	str += ' borderAlpha="100" baseWidth="6" topWidth="1"  '
	str += ' /> \n'; 
	return str;
}
/**
 * 字体
 * @param {} font 字体名称
 * @param {} size 大小
 * @param {} color 颜色
 */
function ChartFont(font,size,color,bgcolor){
	this.font = font;
	this.size = size;
	this.color  = color;
	this.bgcolor = bgcolor;
}
/**
 * 样式表
 */
function ChartStyle(){
	this.name = 'captionstyle';
	this.type = 'font';
	this.font = null;
	this.size = null;
	this.color = null;
	this.bgcolor = null; 
}
ChartStyle.prototype.setFont= function(name,font){
	this.name = name;
	this.type = 'font';
	this.font = font.font;
	this.size = font.size;
	this.color = font.color;
	this.bgcolor =font.bfcolor;
}
ChartStyle.prototype.toXml = function(){
	var str = '<style  name = "' + this.name;
		str += '" type = "' + this.type + '"';
		if(this.font)
			str += ' font = "' + this.font + '" '
		if(this.size)
			str += ' size = "' + this.size + '" '
		if(this.color)
			str += ' color = "' + this.color + '" '
		if(this.bgcolor)
			str += ' bgcolor = "' + this.bgcolor + '" '
		str += ' bold="1" /> \n'
	return str;
}
/**
 * 样式表应用
 */
function ChartStyleApply(){
	this.toObject = null;
	this.styles = null;	
}
ChartStyleApply.prototype.toXml = function(){
	var str = '<apply toObject="'+ this.toObject +'" '
		str += ' styles="' + this.styles + '" /> \n';
	return str;
}
ChartStyleApply.prototype.addStyles=function(style){
	if(style){
		if(this.styles)
			this.styles += "," + style
		else
			this.styles = style;
	}
}
