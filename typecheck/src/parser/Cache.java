package parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Java class Cache
 * This class is used to save tokens already created in order to 
 * avoid duplicates.
 * 
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 * @version 1.00, 31 Jan 2019
 */
final class Cache<T, V> {
	//the Map representing our cached data, mapping the key to the stored object
	private Map<T, V> cache = new HashMap<T, V>();
	
	/**
	 * Get an item from the cache if one exists, and if not, call the provided constructor.
	 * @param key The key by which we are searching for an item in the cache
	 * @param constructor The constructor with which we will create a new item if there is no cached item available
	 * @return Returns the item with the given key from the cache if one is available, and returns a new object created with the given constructor if there is not a cached object available.
	 */
	V get(T key, Function<? super T, ? extends V> constructor) {
		Objects.requireNonNull(key, "key passed to Cache::get() cannot be null");
		Objects.requireNonNull(constructor, "constructor passed to Cache::get() cannot be null");
		
		if (cache.containsKey(key)) {
			return cache.get(key);
		} else {
			V initializedObject = constructor.apply(key);
			cache.put(key, initializedObject);
			return initializedObject;
		}
	}
}
