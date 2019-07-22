package mycache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
/**
 * A singlton cache with configureable capcity
 * @author l1876
 *
 */
public class ConfigurableCache {
	private static final long DEFAULT_MIN_MOMERY = 1024 * 1024 * 1024;// 1M

	// private static final long DEFAULT_MAX_MOMERY = 1024 * 1024 * 1024 * 1024; //
	// 1G

	private static String FILE_PATH = "D:\\CacheTest\\%s.bat";

	private static ConfigurableCache cache;
	
	private volatile long maxMomery;

	private volatile AtomicLong currentMomery=new AtomicLong();

	private ConcurrentHashMap<String, CachableEntry> momeryData = new ConcurrentHashMap<>();

	public static ConfigurableCache getInstance(long maxUseableMomery) {
		if(cache==null) {
			synchronized (ConfigurableCache.class) {
				if(cache==null) {
					return new ConfigurableCache(maxUseableMomery);
				}
			}
			
		}
		return cache;
	}
	
	public static ConfigurableCache getInstance() {
		if(cache==null) {
			synchronized (ConfigurableCache.class) {
				if(cache==null) {
					return new ConfigurableCache();
				}
			}
			
		}
		return cache;
	}
	
	
	
	private ConfigurableCache() {
		this(DEFAULT_MIN_MOMERY);
	}

	private ConfigurableCache(long maxUseableMomery) {
		this.maxMomery = maxUseableMomery;
		if (maxMomery < DEFAULT_MIN_MOMERY) {
			maxMomery = DEFAULT_MIN_MOMERY;
		}
		if (maxMomery > Long.MAX_VALUE) {
			maxMomery =  Long.MAX_VALUE;
		}

	}

	public Object get(String key) {

		if (momeryData.containsKey(key)) {// if existing in memory

			return momeryData.get(key).getData();
		}
		// not existing in memory, find in disk

		return findObjDisk(key);

	}

	private Object findObjDisk(String key) {
		File file = new File(String.format(FILE_PATH, key));

		if (!file.exists()) {//not existing
			return null;
		}
		
		ObjectInputStream objIn=null;
		try {
			objIn=new ObjectInputStream(new FileInputStream(file));
			CachableEntry entry= (CachableEntry) objIn.readObject();
			return entry.getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				objIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
		
		

	}

	public void put(String key, Object value) throws NotSerializableException {
		if (!(value instanceof Serializable)) {
			throw new NotSerializableException("A serializable object needed!");
		}

		CachableEntry newEntry = new CachableEntry(key, value);

		if (newEntry.getSize() + currentMomery.get() <= maxMomery) {// memory cache
			momeryData.put(key, newEntry);
			currentMomery.getAndAdd(newEntry.getSize());
			return;

		}
		// memory used up, persist obj
		ObjectOutputStream ost = null;
		try {
			ost = new ObjectOutputStream(new FileOutputStream(new File(String.format(FILE_PATH, key))));
			ost.writeObject(newEntry);
			ost.flush();
			currentMomery.getAndAdd(newEntry.getSize());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ost.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// System.out.println(DEFAULT_MAX_MOMERY);
		ConfigurableCache cache = new ConfigurableCache();

		// System.out.println(cache.maxMomery);
		for (int i = 0; i < 1000000; i++) {
			try {
				cache.put("" + i, new byte[1024*1024]);
				System.out.println(cache.currentMomery.get());
			} catch (NotSerializableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
