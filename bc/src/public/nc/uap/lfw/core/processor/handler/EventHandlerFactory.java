package nc.uap.lfw.core.processor.handler;

import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.processor.IEventHandler;

public final class EventHandlerFactory {
	public static IEventHandler getEventHandler(String sourceType, LfwPageContext pageCtx){
		if(sourceType == null || sourceType.equals("")){
			return new ScriptEventHandler(pageCtx);
		}
		else if (sourceType.equals(LfwPageContext.SOURCE_TYPE_PAGEMETA)) {
			return new PagemetaEventHandler(pageCtx);
		} 
		else if (sourceType.equals(LfwPageContext.SOURCE_TYPE_MENUBAR_MENUITEM)) {
			return new MenubarItemEventHandler(pageCtx);
		} 
		else if (sourceType.equals(LfwPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM)) {
			return new ContextMenuItemEventHandler(pageCtx);
		} 
		else if (sourceType.equals(LfwPageContext.SOURCE_TYPE_TOOLBAR_BUTTON)) {
			return new ToolbarButtonEventHandler(pageCtx);
		} 
		else if (sourceType.equals(LfwPageContext.SOURCE_TYPE_TREE)) {
			return new TreeEventHandler(pageCtx);
		} 
		else if (sourceType.equals(LfwPageContext.SOURCE_TYPE_BUTTON)) {
			return new ButtonEventHandler(pageCtx);
		} 
		else if (sourceType.equals(LfwPageContext.SOURCE_TYPE_TEXT)) {
			return new TextEventHandler(pageCtx);
		} 
		else if (sourceType.equals(LfwPageContext.SOURCE_TYPE_DATASET)) {
			return new DatasetEventHandler(pageCtx);
		} 
		else if (sourceType.equals(LfwPageContext.SOURCE_TYPE_GRID)) {
			return new GridEventHandler(pageCtx);
		} 
		else if (sourceType.equals(LfwPageContext.SOURCE_TYPE_LINKCOMP)) {
			return new LinkEventHandler(pageCtx);
		} 
		else if (sourceType.equals(LfwPageContext.SOURCE_TYPE_IMAGECOMP)) {
			return new ImageEventHandler(pageCtx);
		} 
		else if (sourceType.equals(LfwPageContext.SOURCE_TYPE_SELF_DEF_COMP)) {
			return new SelfDefCompEventHandler(pageCtx);
		} else if(sourceType.equals(LfwPageContext.SOURCE_TYPE_WIDGT))
			return new WidgetDialogEventHandler(pageCtx);
		throw new LfwRuntimeException("不支持的元素类型");
	}
}
