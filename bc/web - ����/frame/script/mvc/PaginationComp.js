/**
 * @fileoverview 分页控件
 * 
 * @author dengjt
 * 
 */

PaginationComp.prototype = new BaseComponent;
PaginationComp.prototype.componentType = "PAGINATION";
function PaginationComp(parent, name, left, top, width, height, position,
		className) {
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");

	this.className = getString(className, "page_div");
	this.editable = true;
	this.active = true;
	this.create();
}

PaginationComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width;
	this.Div_gen.style.height = this.height;
	this.Div_gen.className = this.className;
	if (this.parentOwner) {
		this.placeIn(this.parentOwner);
	}
};

PaginationComp.prototype.manageSelf = function() {
	var oThis = this;
	var table = $ce("table");
	var row = table.insertRow(-1);
	var cell = row.insertCell(-1);
	this.imgFirst = new ImageComp(cell, "first", window.themePath
			+ "/images/pagination/firstpage.gif", 0, 0, "auto", "auto",
			trans("ml_pagefirst"), window.themePath
					+ "/images/pagination/firstpagehot.gif", {
				inactiveImg : window.themePath
						+ "/images/pagination/firstpagedisable.gif",
				position : "relative"
			});
	cell = row.insertCell(-1);
	this.imgFirst.onclick = function() {
		if (!oThis.active)
			return;
		if (oThis.dataset != null)
			oThis.dataset.setCurrentPage(null, 0);
	};

	this.imgPre = new ImageComp(cell, "pre", window.themePath
			+ "/images/pagination/previouspage.gif", 0, 0, "auto", "auto",
			trans("ml_pagepre"), window.themePath
					+ "/images/pagination/previouspagehot.gif", {
				inactiveImg : window.themePath
						+ "/images/pagination/previouspagedisable.gif",
				position : "relative"
			});
	this.imgPre.onclick = function() {
		if (!oThis.active)
			return;
		if (oThis.dataset != null)
			oThis.dataset.setCurrentPage(null,
					oThis.pageIndexComp.getValue() - 2);
	};

	cell = row.insertCell(-1);
	this.imgNext = new ImageComp(cell, "next", window.themePath
			+ "/images/pagination/nextpage.gif", 0, 0, "auto", "auto",
			trans("ml_pagenext"), window.themePath
					+ "/images/pagination/nextpagehot.gif", {
				inactiveImg : window.themePath
						+ "/images/pagination/nextpagedisable.gif",
				position : "relative"
			});
	this.imgNext.onclick = function() {
		if (!oThis.active)
			return;
		if (oThis.dataset != null)
			oThis.dataset.setCurrentPage(null, oThis.pageIndexComp.getValue());
	};

	cell = row.insertCell(-1);
	this.imgLast = new ImageComp(cell, "last", window.themePath
			+ "/images/pagination/lastpage.gif", 0, 0, "auto", "auto",
			trans("ml_pagelast"), window.themePath
					+ "/images/pagination/lastpagehot.gif", {
				inactiveImg : window.themePath
						+ "/images/pagination/lastpagedisable.gif",
				position : "relative"
			});
	this.imgLast.onclick = function() {
		if (!oThis.active)
			return;
		if (oThis.dataset != null) {
			var pageCount = oThis.dataset.getPageCount(null);
			oThis.dataset.setCurrentPage(null, pageCount - 1);
		}
	};

	cell = row.insertCell(-1);
	this.pageIndexComp = new IntegerTextComp(cell, "index", 0, 0, "16",
			"relative", null, 0, null);
	this.pageIndexComp.Div_gen.style.height = "16";
	this.pageIndexComp.onenter = function() {
		if (!oThis.active)
			return;
		var count = this.getValue();
		var pageCount = oThis.dataset.getPageCount(null);
		if (pageCount == -1)
			return;
		if (count < 1 || count > pageCount) {
			log("The page index is wrong:" + count);
			return;
		} else
			oThis.dataset.setCurrentPage(null, count - 1);
	};
	cell = row.insertCell(-1);
	this.splashDiv = $ce("DIV");
	this.splashDiv.appendChild(document.createTextNode("/"));
	cell.appendChild(this.splashDiv);
	cell = row.insertCell(-1);

	this.totalCount = $ce("DIV");
	cell.appendChild(this.totalCount);

	this.Div_gen.appendChild(table);
};

PaginationComp.prototype.setDataset = function(ds) {
	this.dataset = ds;
	this.dataset.bindComponent(this);
	this.pageIndexComp.setValue(this.dataset.getPageIndex(null) + 1);
	var count = this.dataset.getPageCount(null);
	if (count != -1)
		this.totalCount.appendChild(document.createTextNode(count));
	else
		this.totalCount.appendChild(document.createTextNode("0"));
	this.manageImages();
};

PaginationComp.prototype.manageImages = function() {
	var index = this.dataset.getPageIndex(null);
	var pageCount = this.dataset.getPageCount(null);
	if (index >= (pageCount - 1)) {
		this.imgLast.setActive(false);
		this.imgNext.setActive(false);
	} else {
		this.imgLast.setActive(true);
		this.imgNext.setActive(true);
	}
	if (index == 0 || pageCount == -1) {
		this.imgFirst.setActive(false);
		this.imgPre.setActive(false);
	} else {
		this.imgFirst.setActive(true);
		this.imgPre.setActive(true);
	}
};
/**
 * 设置控件的可编辑性，为了外观显示的需要。实际上调用的是disable属性而不是readonly属性
 * 
 */
PaginationComp.prototype.setEditable = function(editable) {
	this.editable = editable;
};

PaginationComp.prototype.setActive = function(active) {
	this.active = active;
};

PaginationComp.prototype.onModelChanged = function(event) {
	if (PageChangeEvent.prototype.isPrototypeOf(event)) {
		this.pageIndexComp.setValue(event.pageIndex + 1);
		if (this.totalCount.firstChild)
			this.totalCount.removeChild(this.totalCount.firstChild);
		this.totalCount.appendChild(document.createTextNode(this.dataset
				.getPageCount(null)));
		this.manageImages();
	}
};

/**
 * 获取对象信息
 */
PaginationComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.PaginationComp";
	context.c = "PaginationComp";
	return context;
};

/**
 * 设置对象信息
 */
PaginationComp.prototype.setContext = function(context) {

};
