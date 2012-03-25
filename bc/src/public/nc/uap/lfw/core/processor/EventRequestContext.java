package nc.uap.lfw.core.processor;

import nc.uap.lfw.core.event.ctx.LfwPageContext;

public class EventRequestContext {
	private static ThreadLocal<LfwPageContext> threadLocal = new ThreadLocal<LfwPageContext>(){
		protected LfwPageContext initialValue() {
	       return null;
		}
	};
	
	public static LfwPageContext getLfwPageContext() {
		return threadLocal.get();
	}
	
	public static void setLfwPageContext(LfwPageContext ctx) {
		threadLocal.set(ctx);
	}
	
	public static void reset() {
		threadLocal.remove();
	}
}
