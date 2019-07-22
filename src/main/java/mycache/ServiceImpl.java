package mycache;

import java.io.NotSerializableException;

public class ServiceImpl implements Service{

	private ConfigurableCache cache;
	
	public ServiceImpl() {
		cache=ConfigurableCache.getInstance();
	}
	
	public ServiceImpl(long maxUseableMemory) {
		cache=ConfigurableCache.getInstance(maxUseableMemory);
	}
	
	@Override
	public Object get(String key) {
		checkKey(key);
		return cache.get(key);
	}

	@Override
	public void put(String key, Object value) {
		checkVal(value);
		try {
			cache.put(key, value);
		} catch (NotSerializableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void checkVal(Object value) {
		if(value==null) {
			throw new IllegalArgumentException("Value should not be null");
		}

	}
	
	private void checkKey(String key) {
		if(key==null||key.trim().isEmpty()) {
			throw new IllegalArgumentException("Key should not be empty or null");
		}
	}
	
}
