<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="查看日志">
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
        <PageListener id="pageDefaultListener">
            <Events>
                <Event async="true" name="afterPageInit" onserver="false">
                    <Params>
                        <Param>
                            <Name>
                            </Name>
                            <Desc>                                <![CDATA[]]>
                            </Desc>
                        </Param>
                    </Params>
                    <Action>                        <![CDATA[pageUI.getWidget("main").getComponent("endBtn").setActive(false);]]>
                    </Action>
                </Event>
            </Events>
        </PageListener>
    </Listeners>
    <Containers>
    </Containers>
</PageMeta>
