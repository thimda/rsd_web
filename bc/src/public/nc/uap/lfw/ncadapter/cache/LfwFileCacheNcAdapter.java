package nc.uap.lfw.ncadapter.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheException;
import nc.vo.cache.ICache;
/**
 * LFw���NC��������
 * @author dengjt
 *
 */
public class LfwFileCacheNcAdapter implements ILfwCache{
	private ICache nccache;
	public LfwFileCacheNcAdapter(ICache nccache){
		this.nccache = nccache;
	}
	public Object get(Object key) throws LfwCacheException {
		try{
			Object obj = nccache.get(key);
			if(nccache.getClass().getName().equals("nc.vo.cache.FileCache") && obj != null){
				byte[] bytes = (byte[]) obj;
				ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				ObjectInputStream oin = new ObjectInputStream(in);
				obj = oin.readObject();
			}
			return obj;
		}
		catch(Exception e){
			throw new LfwCacheException(e.getMessage(), e);
		}
	}

	public Object put(Object key, Object value) throws LfwCacheException {
		try{
			Object sObj = value;
			//TODO ������������л�����ʹ���������ԭ�����������ù�ϵ������LFWʹ���ļ�����ĳ���
			if(nccache.getClass().getName().equals("nc.vo.cache.FileCache")){
				if(value != null){
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					ObjectOutputStream oou = new ObjectOutputStream(out);
					oou.writeObject(value);
					sObj = out.toByteArray();
				}
			}
			return nccache.put(key, sObj);
		}
		catch(Exception e){
			throw new LfwCacheException(e.getMessage(), e);
		}
	}

	public Object remove(Object key) throws LfwCacheException {
		try{
			return nccache.remove(key);
		}
		catch(Exception e){
			throw new LfwCacheException(e.getMessage(), e);
		}
	}
	public Set<Object> getKeys() {
		//TODO need check
		return new HashSet<Object>();
	}

}
