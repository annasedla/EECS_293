package parser;

import java.util.Optional;
import java.util.function.Function;

abstract class ObjectHandler {
	/**
	 * Return a value if something is not null, null otherwise
	 * @param valueToCheck The value that will be checked
	 * @param valueToReturn The value that will be returned if valueToReturn is not null
	 * @return Returns null if valueToCheck is null, valueToReturn otherwise
	 */
	public static <T> T returnIfNotNull(Object valueToCheck, T valueToReturn) {
		return valueToCheck == null ? null : valueToReturn;
	}
	
	/**
	 * Create an object with a given constructor if 
	 * @param objectToCheck The object to check if it's null
	 * @param constructor The constructor to use to create a new object if 
	 * @return Returns the constructor applied if the object is null, and the checked object otherwise
	 */
	public static <T> T createObjectIfNull(T objectToCheck, Function<Void, T> constructor) {
		return Optional.ofNullable(objectToCheck).orElse(constructor.apply(null));
	}
}
