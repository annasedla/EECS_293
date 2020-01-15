package parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class ListHandler {
	/**
	 * A constructor that cannot be accessed to prevent extending this class
	 */
	private ListHandler() {
		
	}
	
	/**
	 * Make a shallow copy of a list
	 * @param list The list to copy
	 * @return Returns a shallow copy of the list, or returns null if the list is null
	 */
	public static <T> List<T> shallowCopy(List<T> list) {
		return ObjectHandler.returnIfNotNull(list, Optional.ofNullable(list).map(List::stream).orElseGet(Stream::empty).collect(Collectors.toList()));
	}
	
	/**
	 * Get the first item in a list
	 * @param list The list we are checking
	 * @return The first item in the list
	 */
	public static <T> T listHead(List<T> list) {
		return list.get(0);
	}
	
	/**
	 * Check that a list is both non-empty and non-null
	 * @param list The list to be checked
	 * @return Returns true if the list is both non-null and non-empty, false otherwise
	 */
	public static <T> boolean nonEmptyList(List<T> list) {
		return list != null && !list.isEmpty();
	}
	
	/**
	 * Create an empty list
	 * @return An empty list
	 */
	public static <T> List<T> createEmptyList() {
		return new LinkedList<T>();
	}
	
	/**
	 * Replace items in a list matching a given predicate with some replacement function
	 * @param list The list to be evaluated
	 * @param predicate Replace items matching this predicate
	 * @param replacement Apply this function to the item to be replaced to generate its replacement
	 * @return Returns a copy if the given list where all items matching the predicate are replaced with the value of the replacement function applied to those items
	 */
	public static <T> List<T> replaceItemsMatching(List<T> list, Function<T, Boolean> predicate, Function<T, T> replacement) {
		return list.stream().map(item -> predicate.apply(item) ? replacement.apply(item) : item).collect(Collectors.toList());
	}
	
	/**
	 * Returns whether the list contains one single item
	 * @param list The list being checked
	 * @return Returns true if the list contains exactly one item, false otherwise
	 */
	public static <T> boolean containsSingleItem(List<T> list) {
		return list.size() == 1;
	}
}
