package nc.uap.lfw.core.ctrlfrm.plugin;

import java.util.Arrays;
import java.util.LinkedHashSet;

import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.ctrlfrm.seria.IControlSerializer;
import nc.uap.lfw.core.ctrlfrm.uiseria.IUISerializer;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.group.PCUIRenderGroup;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.itf.IUIRenderGroup;
import nc.uap.lfw.util.LfwClassUtil;

public class ControlPluginImpl implements IControlPlugin {
	private ControlPluginConfig config;
	private IUIRenderGroup renderGroup;
	public ControlPluginImpl(ControlPluginConfig config){
		this.config = config;
	}
	
	@Override
	public String[] calculateDependences(boolean optimized) {
		String[] dependences = getDependences();
		if(dependences == null || dependences.length == 0)
			return null;
		LinkedHashSet<String> hs = new LinkedHashSet<String>();
		
		for (int i = 0; i < dependences.length; i++) {
			getDependences(hs, dependences[i], optimized);
		}
		return hs.toArray(new String[0]);
	}
	
	@Override
	public String[] calculateCssDependences(boolean optimized) {
		String[] dependences = getDependences();
		if(dependences == null || dependences.length == 0)
			return null;
		LinkedHashSet<String> hs = new LinkedHashSet<String>();
		
		for (int i = 0; i < dependences.length; i++) {
			getCssDependences(hs, dependences[i], optimized);
		}
		return hs.toArray(new String[0]);
	}
	
	private void getCssDependences(LinkedHashSet<String> hs, String id, boolean optimized) {
		IControlPlugin cPlugin = ControlFramework.getInstance().getControlPluginByType(id);
		String[] dependences = cPlugin.getDependences();
		if(dependences != null && dependences.length != 0){
			for (int i = 0; i < dependences.length; i++) {
				getCssDependences(hs, dependences[i], optimized);
			}
		}
		String[] imports = cPlugin.getCssImports(optimized);
		if(imports != null)
			hs.addAll(Arrays.asList(imports));
	}
	
	private void getDependences(LinkedHashSet<String> hs, String id, boolean optimized) {
		IControlPlugin cPlugin = ControlFramework.getInstance().getControlPluginByType(id);
		String[] dependences = cPlugin.getDependences();
		if(dependences != null && dependences.length != 0){
			for (int i = 0; i < dependences.length; i++) {
				getDependences(hs, dependences[i], optimized);
			}
		}
		String[] imports = cPlugin.getImports(optimized);
		if(imports != null)
			hs.addAll(Arrays.asList(imports));
	}

	@Override
	public <T>IControlSerializer<T> getControlSerializer() {
		String serializer = config.getSerializer();
		if(serializer == null || serializer.equals(""))
			return null;
		return (IControlSerializer<T>) LfwClassUtil.newInstance(serializer);
	}

	@Override
	public String[] getDependences() {
		String dependences = config.getDependences();
		if(dependences == null || dependences.equals(""))
			return null;
		return dependences.split(",");
	}

	@Override
	public String getFullName() {
		return config.getFullname();
	}

	@Override
	public String getId() {
		return config.getId();
	}

	@Override
	public IUIRender getUIRender(UIMeta uimeta, UIElement type, PageMeta pageMeta, IUIRender parentRender, DriverPhase dp) {
//		String uirenderClazz = config.getUirender();
//		if(uirenderClazz == null || uirenderClazz.equals(""))
//			return null;
//		Class<?> c = LfwClassUtil.forName(uirenderClazz);
//		try {
////			Class<?> prClazz = (parentRender == null) ? null : parentRender.getClass();
//			Constructor<?>[] cons = c.getConstructors();
////			Constructor<?> cs = c.getConstructor(new Class[]{type.getClass(), uimeta.getClass(), PageMeta.class, prClazz});
//			IUIRender render = (IUIRender) cons[0].newInstance(new Object[]{type, uimeta, pageMeta, parentRender});
//			render.setType(getId());
//			return render;
//		} 
//		catch (Exception e) {
//			LfwLogger.error(e);
//			throw new LfwRuntimeException(e);
//		}
		if(renderGroup == null)
			renderGroup = new PCUIRenderGroup();
		IUIRender render =  renderGroup.getUIRender(uimeta, type, pageMeta, parentRender);
		render.setControlPlugin(this);
		return render;
	}

	@Override
	public IUIRender getUIRender(UIMeta uiMeta, UIElement uiEle, WebComponent comp,
			PageMeta pageMeta, IUIRender parentRender, DriverPhase dp) {
		if(renderGroup == null)
			renderGroup = new PCUIRenderGroup();
		IUIRender render =  renderGroup.getUIRender(uiMeta, uiEle, comp, pageMeta, parentRender);
		render.setControlPlugin(this);
		return render;
	}
	
	@Override
	public <T>IUISerializer<T> getUISerializer() {
		String uiserializer = config.getUiserializer();
		if(uiserializer == null || uiserializer.equals(""))
			return null;
		return (IUISerializer<T>) LfwClassUtil.newInstance(uiserializer);
	}

	@Override
	public String getCssFullName() {
		return config.getCssfullname();
	}

	@Override
	public String[] getCssImports(boolean optimized) {
		String[] imports = config.getCssImports();
		String cstr = config.getCompress();
		boolean compress = (cstr == null ? true : (!cstr.equals("false"))); 
		if(optimized && compress){
			if(imports != null && imports.length > 0)
				return new String[]{"compressed/" + getId()};
			return null;
		}
		return config.getCssImports();
	}

	@Override
	public String[] getImports(boolean optimized) {
		String[] imports = config.getImports();
		String cstr = config.getCompress();
		boolean compress = (cstr == null ? true : (!cstr.equals("false"))); 
		if(optimized && compress){
			if(imports != null && imports.length > 0)
				return new String[]{"compressed/" + getId()};
			return null;
		}
		else
			return config.getImports();
	}

	@Override
	public String[] calculateDependenceIds() {
		String[] dependences = getDependences();
		if(dependences == null || dependences.length == 0)
			return null;
		LinkedHashSet<String> hs = new LinkedHashSet<String>();
		
		for (int i = 0; i < dependences.length; i++) {
			getIdDependences(hs, dependences[i]);
		}
		return hs.toArray(new String[0]);
	}
	
	private void getIdDependences(LinkedHashSet<String> hs, String id) {
		IControlPlugin cPlugin = ControlFramework.getInstance().getControlPluginByType(id);
		String[] dependences = cPlugin.getDependences();
		if(dependences != null && dependences.length != 0){
			for (int i = 0; i < dependences.length; i++) {
				getIdDependences(hs, dependences[i]);
			}
		}
		
		hs.add(id);
	}
}
