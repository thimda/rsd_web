package nc.uap.lfw.core.cache;

import java.util.Set;


public interface ILfwCache {
	/**
	 * 把数据对象放入缓存中。
	 * @param key   与指定值相关联的键。
	 * @param value 与指定键相关联的值。
	 * @return 以前与指定键相关联的值，如果没有该键的映射关系，则返回 null。
	 * 		   如果该实现支持 null 值，则返回 null 也可表明此映射以前将 null 与指定键相关联。
	 * @throws LfwCacheException    当该操作发生任何RuntiemException时。
	 */
    public Object put(Object key, Object value) throws LfwCacheException;
    
    /**
     * 从缓存中得到数据。
     * @param  key - 要返回其相关值的键。
     * @return 缓存的数据对象，如果不存在则返回null。
     * @throws LfwCacheException    当该操作发生任何RuntiemException时。
     */
    public Object get(Object key) throws LfwCacheException;

    /**
     *如果存在此键的映射关系，则将其从缓存中移除。
     *
     * @param key - 从缓存中移除其映射关系的键。
     * @return 返回以前与指定键相关联的值，如果没有该键的映射关系，则返回 null。
     * @throws LfwCacheException    当该操作发生任何RuntiemException时。
     */
    public Object remove(Object key) throws LfwCacheException;
    
	public Set<Object> getKeys();
}
