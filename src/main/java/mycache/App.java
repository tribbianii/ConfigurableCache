package mycache;


public class App {
	public static void main(String[] args) {
		// System.out.println(DEFAULT_MAX_MOMERY);
		Service cacheService=new ServiceImpl();
		//ConfigurableCache cache = new ConfigurableCache();

		// System.out.println(cache.maxMomery);
		for (int i = 0; i < 1000000; i++) {
			
				cacheService.put("" + i, new byte[1024*1024]);

		}

	}
}
