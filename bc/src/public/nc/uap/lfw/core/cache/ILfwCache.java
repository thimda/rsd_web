package nc.uap.lfw.core.cache;

import java.util.Set;


public interface ILfwCache {
	/**
	 * �����ݶ�����뻺���С�
	 * @param key   ��ָ��ֵ������ļ���
	 * @param value ��ָ�����������ֵ��
	 * @return ��ǰ��ָ�����������ֵ�����û�иü���ӳ���ϵ���򷵻� null��
	 * 		   �����ʵ��֧�� null ֵ���򷵻� null Ҳ�ɱ�����ӳ����ǰ�� null ��ָ�����������
	 * @throws LfwCacheException    ���ò��������κ�RuntiemExceptionʱ��
	 */
    public Object put(Object key, Object value) throws LfwCacheException;
    
    /**
     * �ӻ����еõ����ݡ�
     * @param  key - Ҫ���������ֵ�ļ���
     * @return ��������ݶ�������������򷵻�null��
     * @throws LfwCacheException    ���ò��������κ�RuntiemExceptionʱ��
     */
    public Object get(Object key) throws LfwCacheException;

    /**
     *������ڴ˼���ӳ���ϵ������ӻ������Ƴ���
     *
     * @param key - �ӻ������Ƴ���ӳ���ϵ�ļ���
     * @return ������ǰ��ָ�����������ֵ�����û�иü���ӳ���ϵ���򷵻� null��
     * @throws LfwCacheException    ���ò��������κ�RuntiemExceptionʱ��
     */
    public Object remove(Object key) throws LfwCacheException;
    
	public Set<Object> getKeys();
}
