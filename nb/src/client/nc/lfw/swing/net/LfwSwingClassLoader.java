package nc.lfw.swing.net;


public class LfwSwingClassLoader extends ClassLoader {

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if(name.startsWith("生成代码前缀")){
			byte[] clazzBytes = HttpProxy.pageRequest();
			return defineClass(name, clazzBytes, 0, clazzBytes.length);
		}
		return super.loadClass(name);
	}
}
