package nc.uap.lfw.core.page;

public class RequestLifeCycleContext {
	private static ThreadLocal<RequestLifeCycleContext> threadLocal = new ThreadLocal<RequestLifeCycleContext>();
	private static RequestLifeCycleContext instance = new RequestLifeCycleContext();
	//生命周期所属阶段
	private LifeCyclePhase phase = LifeCyclePhase.render;
	public RequestLifeCycleContext() {
	}
	
	public static RequestLifeCycleContext get() {
		RequestLifeCycleContext ctx = threadLocal.get();
		return ctx == null ? instance : ctx;
	}
	
	public static void set(RequestLifeCycleContext ctx) {
		threadLocal.set(ctx);
	}
	

	public void setPhase(LifeCyclePhase phase){
		this.phase = phase;
	}
	
	public LifeCyclePhase getPhase() {
		return this.phase;
	}

}
