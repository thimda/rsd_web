<?xml version="1.0" encoding='UTF-8'?>
<PageMeta>
    <Processor>nc.uap.lfw.core.processor.EventRequestProcessor</Processor>
    <PageStates>
    </PageStates>
    <Widgets>
        <Widget id="main" refId="main">
        </Widget>
         <Widget id="password" refId="password">
        </Widget>
    </Widgets>
    <Listeners>
        <PageListener id="loginPageListener"  serverClazz="nc.uap.lfw.login.listener.LfwLoginPageListener">
            <Events>
                <Event async="true" name="afterPageInit" onserver="true">
                    <SubmitRule cardSubmit="false" tabSubmit="false">
                        <Widget cardSubmit="false" id="main" tabSubmit="false">
                        </Widget>
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
                
                <Event async="false" name="onClosed" onserver="true">
                    <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                    </SubmitRule>
                    <Params>
                        <Param>
                            <Name>
                            </Name>
                            <Value></Value>
                            <Desc>                                <![CDATA[]]>
                            </Desc>
                        </Param>
                    </Params>
                    <Action>
                    </Action>
                </Event>
            </Events>
        </PageListener>
    </Listeners>
</PageMeta>
