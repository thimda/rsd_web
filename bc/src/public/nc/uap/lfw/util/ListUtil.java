package nc.uap.lfw.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * List常用操作工具类
 * @author dengjt
 * 2006-1-24
 */
public class ListUtil {

	public static void distinct(List list) {
		distinct(list, null);
	}

	@SuppressWarnings("unchecked")
	public static void distinct(List list, Comparator comparator) {
		if ((list == null) || (list.size() == 0)) {
			return;
		}
		Set set = null;
		if (comparator == null) {
			set = new TreeSet();
		}
		else {
			set = new TreeSet(comparator);
		}
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			Object obj = itr.next();

			if (set.contains(obj)) {
				itr.remove();
			}
			else {
				set.add(obj);
			}
		}
	}

	public static <T> List<T> fromArray(T[] array) {
		if(array == null)
			return null;
		ArrayList<T> list = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static List fromCollection(Collection c) {
		if (c != null && c instanceof List) {
			return (List)c;
		}
		if ((c == null) || (c.size() == 0)) {
			return new ArrayList();
		}
		List list = new ArrayList(c.size());
		Iterator itr = c.iterator();
		while (itr.hasNext()) {
			list.add(itr.next());
		}
		return list;
	}

    @SuppressWarnings("unchecked")
	public static List fromEnumeration(Enumeration enu) {
		List list = new ArrayList();

		while (enu.hasMoreElements()) {
			Object obj = enu.nextElement();
			list.add(obj);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static List subList(List list, int begin, int end) {
		List newList = new ArrayList();
		int normalizedSize = list.size() - 1;

		if ((begin < 0) || (begin > normalizedSize) || (end < 0) ||
			(begin > end)) {
			return newList;
		}
		for (int i = begin; i < end && i <= normalizedSize; i++) {
			newList.add(list.get(i));
		}
		return newList;
	}
	
	public static boolean contains(Object[] objs, Object obj){
		for (int i = 0; i < objs.length; i++) {
			if(objs[i].equals(obj))
				return true;
		}
		return false;
	}

}
