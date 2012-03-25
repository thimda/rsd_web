<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="医疗主界面">
    <Processor>nc.uap.lfw.core.processor.EventRequestProcessor</Processor>
    <PageStates currentState="1">
        <PageState>
            <Key>1</Key>
            <Name>卡片显示</Name>
        </PageState>
        <PageState>
            <Key>2</Key>
            <Name>列表显示</Name>
        </PageState>
    </PageStates>
    <Widgets>
        <Widget id="main" refId="main">
        </Widget>
        
    </Widgets>
    <Listeners>
        <PageListener id="demo_defaultListener" serverClazz="nc.uap.lfw.core.event.deft.DefaultPageServerListener">
            <Events>
                <Event async="false" name="onClosed" onserver="true">
                    <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                    </SubmitRule>
                    <Params>
                        <Param>
                            <Name>
                            </Name>
                            <Desc>                                <![CDATA[]]>
                            </Desc>
                        </Param>
                    </Params>
                    <Action>
                    </Action>
                </Event>
                <Event async="true" name="afterPageInit" onserver="false">
                    <Params>
                        <Param>
                            <Name>
                            </Name>
                            <Desc>                                <![CDATA[]]>
                            </Desc>
                        </Param>
                    </Params>
                    <Action>                        <![CDATA[// 设置用户登陆信息
// 根据当前时间确定问候信息内容
var date = new Date();
var hours = date.getHours();
var helloWord = "";
if (hours < 5) {
	helloWord = "午夜好，";
} else if (hours >= 5 && hours < 12) {
	helloWord = "早上好，";
} else if (hours >= 12 && hours < 14) {
	helloWord = "中午好，";
} else if (hours >= 14 && hours < 18) {
	helloWord = "下午好，";
} else if (hours >= 18) {
	helloWord = "晚上好，";
}
	
var userInfoHTML = "<div style='text-align:right;padding-top:5px;'>" + helloWord + userName + "！ </div>";
pageUI.getWidget("main").getComponent("head").addSelfDefItem("userInfo", "right", "300px", userInfoHTML);
//pageUI.getWidget("main").getMenu("menubar_head").setSelfDefItem("300px", userInfoHTML);

// 增加toolbar分隔条
pageUI.getWidget("main").getComponent("btn_toolbar").addSep("left");
]]>
                    </Action>
                </Event>
                <Event async="false" name="onClosing" onserver="false">
                    <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                    </SubmitRule>
                    <Params>
                        <Param>
                            <Name>
                            </Name>
                            <Desc>                                <![CDATA[]]>
                            </Desc>
                        </Param>
                    </Params>
                    <Action>                        <![CDATA[pageUI.showCloseConfirm();]]>
                    </Action>
                </Event>
            </Events>
        </PageListener>
    </Listeners>
    <Containers>
    </Containers>
</PageMeta>
