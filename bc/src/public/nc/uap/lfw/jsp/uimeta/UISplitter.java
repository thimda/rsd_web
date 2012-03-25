package nc.uap.lfw.jsp.uimeta;


public class UISplitter extends UILayout {
	private static final long serialVersionUID = -634167934328017861L;
	
	public static final Integer ORIENTATION_H = 1;
	public static final Integer ORIENTATION_V = 0;
	public static final Integer BOUNDMODE_PX = 1;
	public static final Integer BOUNDMODE_PERC = 0;
	public static final Integer ONETOUCH_TRUE = 1;
	public static final Integer ONETOUCH_FALSE = 0;
	public static final Integer INVERSE_FALSE = 0;
	public static final Integer INVERSE_TRUE = 0;
	public static final Integer HIDEDIRECTION_R = 1;
	public static final Integer HIDEDIRECTION_L = 0;
	
	public static final String DIVIDE_SIZE = "divideSize";
	public static final String ORIENTATION = "orientation";
	public static final String BOUNDMODE = "boundMode";
	public static final String ONETOUCH = "oneTouch";
//	public static final String SPLITERWIDTH = "spliterWidth";
	public static final String INVERSE = "inverse";
//	public static final String HIDEBAR = "hideBar";
	public static final String HIDEDIRECTION = "hideDirection";
	public static final String ID = "id";

	private UISplitterOne splitterOne;
	private UISplitterTwo splitterTwo;

	public UISplitter() {
		setDivideSize("0.1");
		setOrientation(ORIENTATION_H);
		setBoundMode(BOUNDMODE_PERC);
		setOneTouch(UIConstant.FALSE);
//		map.put(SPLITERWIDTH, "4");
		setInverse(UIConstant.FALSE);
//		map.put(HIDEBAR, 1);
		setHideDirection(HIDEDIRECTION_R); // 1 纵向 0横向	
	}
	
	public UISplitterOne getSplitterOne() {
		return splitterOne;
	}

	private void setSplitterOne(UISplitterOne splitterOne) {
		this.splitterOne = splitterOne;
	}

	public UISplitterTwo getSplitterTwo() {
		return splitterTwo;
	}

	private void setSplitterTwo(UISplitterTwo splitterTwo) {
		this.splitterTwo = splitterTwo;
	}

	public void addPanel(UILayoutPanel panel) {
		if (panel instanceof UISplitterTwo)
			this.setSplitterTwo((UISplitterTwo) panel);
		else
			this.setSplitterOne((UISplitterOne) panel);
		super.addPanel(panel);
	}

	public String getId() {
		return (String) getAttribute(ID);
	}

	public void setId(String value) {
		setAttribute(ID, value);
	}

	public String getDivideSize() {
		return (String) getAttribute(DIVIDE_SIZE);
	}

	public void setDivideSize(String divideSize) {
		setAttribute(DIVIDE_SIZE, divideSize);
//		notifyChange(UPDATE, DIVIDE_SIZE);
	}

	public Integer getOrientation() {
		return (Integer) getAttribute(ORIENTATION);
	}

	public void setOrientation(Integer orientation) {
		setAttribute(ORIENTATION, orientation);
//		notifyChange(UPDATE, ORIENTATION);
	}

	public Integer getBoundMode() {
		return (Integer) getAttribute(BOUNDMODE);
	}

	public void setBoundMode(Integer boundMode) {
		setAttribute(BOUNDMODE, boundMode);
//		notifyChange(UPDATE, BOUNDMODE);
	}

	public Integer getOneTouch() {
		return (Integer) getAttribute(ONETOUCH);
	}

	public void setOneTouch(Integer oneTouch) {
		setAttribute(ONETOUCH, oneTouch);
//		notifyChange(UPDATE, ONETOUCH);
	}

//	public String getSpliterWidth() {
//		return (String) getAttribute(SPLITERWIDTH);
//	}
//
//	public void setSpliterWidth(String spliterWidth) {
//		setAttribute(SPLITERWIDTH, spliterWidth);
////		notifyChange(UPDATE, SPLITERWIDTH);
//	}

	public Integer getInverse() {
		return (Integer) getAttribute(INVERSE);
	}

	public void setInverse(Integer inverse) {
		setAttribute(INVERSE, inverse);
//		notifyChange(UPDATE, INVERSE);
	}

//	public Integer getHideBar() {
//		return (Integer) getAttribute(HIDEBAR);
//	}
//
//	public void setHideBar(Integer hideBar) {
//		setAttribute(HIDEBAR, hideBar);
////		notifyChange(UPDATE, HIDEBAR);
//	}

	public Integer getHideDirection() {
		return (Integer) getAttribute(HIDEDIRECTION);
	}

	public void setHideDirection(Integer hideDirection) {
		setAttribute(HIDEDIRECTION, hideDirection);
//		notifyChange(UPDATE, HIDEDIRECTION);
	}

	public String getWidgetId() {
		return (String) getAttribute(WIDGET_ID);
	}

	public void setWidgetId(String widgetId) {
		setAttribute(WIDGET_ID, widgetId);
	}

//	@Override
//	protected Map<String, Serializable> createAttrMap() {
//		Map<String, Serializable> map = new HashMap<String, Serializable>();
//		map.put(DIVIDE_SIZE, "50");
//		map.put(ORIENTATION, 1); // 0 纵向， 1横向
//		map.put(BOUNDMODE, 0); // % 0, px 1
//		map.put(ONETOUCH, 0);
////		map.put(SPLITERWIDTH, "4");
//		map.put(INVERSE, 0);
////		map.put(HIDEBAR, 1);
//		map.put(HIDEDIRECTION, 1); // 1 纵向 0横向
//		return map;
//	}

	@Override
	public UISplitter doClone() {
		UISplitter uiSplitter = (UISplitter) super.doClone();
		if (this.splitterOne != null) {
			uiSplitter.splitterOne = (UISplitterOne) this.splitterOne.doClone();
		}

		if (this.splitterTwo != null) {
			uiSplitter.splitterTwo = (UISplitterTwo) this.splitterTwo.doClone();
		}
		return uiSplitter;
	}

}
