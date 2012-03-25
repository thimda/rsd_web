<?xml version="1.0" encoding='UTF-8'?>
<PageMeta>
    <Processor>nc.uap.lfw.core.processor.EventRequestProcessor</Processor>
    <PageStates>
    </PageStates>
    <Services>
    	<Service>
    		<Name>shortcut_service</Name>
    		<ClassName>nc.uap.lfw.login.impl.ShortCutService</ClassName>
    	</Service>
    </Services>
    <Widgets>
        <Widget id="mainwidget" refId="mainwidget">
        </Widget>
        <Widget id="navwidget" refId="navwidget">
        </Widget>
    </Widgets>
    <Listeners>
        <PageListener id="mainPageListener" serverClazz="nc.uap.lfw.mainpage.listener.LfwMainPageListener">
            <Events>
                <Event async="true" name="afterPageInit" onserver="true">
                    <SubmitRule cardSubmit="false" tabSubmit="false">
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
            </Events>
        </PageListener>
		<PageListener id="afterInit">
            <Events>
                <Event async="true" name="afterPageInit" onserver="false">
                    <Params>
                        <Param>
                            <Name>
                            </Name>
                            <Value></Value>
                            <Desc>                                <![CDATA[]]>
                            </Desc>
                        </Param>
                    </Params>
                    <Action>                        <![CDATA[
						var url = window.globalPath + "/core/uimeta.um?pageId=quickdesk";
						$ge("showFrame").src = url;
					]]>
                    </Action>
                </Event>
            </Events>
        </PageListener>
    </Listeners>
</PageMeta>
