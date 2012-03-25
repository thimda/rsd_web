package nc.uap.lfw.core.processor;

import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.processor.handler.EventHandlerFactory;
import nc.uap.lfw.core.serializer.IObject2XmlSerializer;
import nc.uap.lfw.core.serializer.IXml2ObjectSerializer;
import nc.uap.lfw.core.serializer.impl.EventContext2XmlSerializer;
import nc.uap.lfw.core.serializer.impl.Xml2EventContextSerializer;

public class EventRequestProcessor extends AbstractRequestProcessor<LfwPageContext> {

	protected IXml2ObjectSerializer<LfwPageContext> getRequestSerializer() {
		return new Xml2EventContextSerializer();
	}

	@Override
	protected IObject2XmlSerializer<LfwPageContext> getResponseSerializer() {
		return new EventContext2XmlSerializer();
	}
	public LfwPageContext processEvent() {
		LfwPageContext ctx = getModelObject();
		doProcessEvent(ctx);
		return ctx;
	}
	public void doProcessEvent(LfwPageContext ctx) {
		try{
			String sourceType = ctx.getParameter(LfwPageContext.SOURCE_TYPE);
			EventRequestContext.setLfwPageContext(ctx);
			IEventHandler eventHandler = EventHandlerFactory.getEventHandler(sourceType, ctx);
			eventHandler.execute();
		}
		finally{
			EventRequestContext.reset();
		}
	}

}
