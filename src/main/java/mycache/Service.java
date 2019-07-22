package mycache;

public interface Service {
	Object get(String key);
	void put(String key, Object value);
}
