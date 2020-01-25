/*
 * MIT License
 *
 * Copyright (c) 2020 Christophe Deleray
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.cdeleray.multimap;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * The {@code MultiMap} object represents a convenient implementation of a
 * <em>multimap</em>, that is a {@link Map} that can map each key to multiple 
 * values. In other words, in a map of such, there is no limit on the number 
 * of elements with the same key. 
 * <p>
 * By default, a {@code MultiMap} object is backed by an instance of 
 * {@link HashMap} and all entries are instances of {@link ArrayList}. 
 * This is can be overridden at constructor-level, by specifying 
 * <em>ad-hoc</em> {@link SerializableSupplier suppliers}. 
 * 
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * 
 * @author Christophe Deleray
 */
public class MultiMap<K, V> implements Map<K, Collection<V>>, Serializable {
  /** Private serial version unique ID to ensure serialization compatibility. */ 
  private static final long serialVersionUID = 2964487332273087760L;
  
  /** The internal map. */
  private final Map<K, Collection<V>> map;
  
  /** A supplier that creates a new {@link Collection} for each map entry. */
  private final SerializableSupplier<Collection<V>> collectionSupplier;
  
  /**
   * A {@code SerializableSupplier} object represents a {@link Supplier}  
   * instance that is also {@link Serializable}.
   *
   * @param <T> the type of results supplied by this supplier
   *
   * @author Christophe Deleray
   */
  public interface SerializableSupplier<T> extends Supplier<T>, Serializable {}
  
  /**
   * Creates a new {@code MultiMap} object backed by default by a {@link HashMap} 
   * which values are instances of {@link ArrayList} . 
   */
  public MultiMap() {
    this(HashMap::new, ArrayList::new);
  }
  
  /**
   * Creates a new {@code MultiMap} object backed by a {@link Map} created by
   * the supplier defined by {@code mapSupplier} and which values are instances
   * of a {@link Collection} created by the supplier defined by 
   * {@code collectionSupplier}.
   * 
   * @param mapSupplier a supplier that creates the internal {@link Map} instance
   * @param collectionSupplier a supplier that creates {@link Collection} instances 
   * to compose the values of the internal {@link Map}  
   */
  public MultiMap(SerializableSupplier<Map<K, Collection<V>>> mapSupplier, SerializableSupplier<Collection<V>> collectionSupplier) {
    this.map = mapSupplier.get();
    this.collectionSupplier = collectionSupplier;
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  /**
   * {@inheritDoc}
   * <p>
   * This implementation merely checks whether the specified value is 
   * contained among all values added into this {@code MultiMap}. 
   */
  @Override
  public boolean containsValue(Object value) {
    return map.values().stream().anyMatch(values -> values.contains(value));
  }

  /**
   * Determines whether there is a key that map the entire collection of values 
   * given by {@code values}.
   *  
   * @param values the values to check
   * @return {@code true} if there is a key that map the entire collection 
   * values given by {@code values}; {@code false} otherwise
   */
  public boolean containsValues(Collection<V> values) {
    return map.containsValue(values);
  }

  @Override
  public Set<Map.Entry<K, Collection<V>>> entrySet() {
    return map.entrySet();
  }

  /**
   * Determines whether the specified object is equals to this 
   * {@code MultiMap}.
   * 
   * @param obj the object to compare
   * @return {@code true} if {@code obj} is equals to this {@code MultiMap};
   * {@code false} otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    
    if (!(obj instanceof MultiMap)) {
      return false;
    }
    
    MultiMap<?, ?> mm = (MultiMap<?, ?>)obj;
    return map.equals(mm.map);
  }

  /**
   * {@inheritDoc}
   * <p>
   * This method never returns {@code null}. It returns an empty collection 
   * instead whenever there is no element bound with the specified key. 
   */
  @Override
  public Collection<V> get(Object key) {
    return map.getOrDefault(key, emptyList());
  }

  /**
   * Returns the hash code of this {@code MultiMap} object.
   * 
   * @return the hash code of this {@code MultiMap} object
   */
  @Override
  public int hashCode() {
    return map.hashCode();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public Set<K> keySet() {
    return map.keySet();
  }
  
  @Override
  public Collection<V> put(K key, Collection<V> value) {
    return map.put(key, value);
  }

  /**
   * Associates the specified value with the specified key in this map.
   * 
   * @param key key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @return {@code true} if the map did not already associate the specified
   * element with the specified key; {@code false} otherwise 
   */
  public boolean putValue(K key, V value) {
    return getOrCreate(key).add(value);
  }

  @Override
  public void putAll(Map<? extends K, ? extends Collection<V>> m) {
    map.putAll(m);
  }
    
  /**
   * Associates each value contained into the specified collection with the 
   * specified key in this map.
   * 
   * @param key key with which the specified value is to be associated
   * @param values values to be associated with the specified key
   */
  public void putAll(K key, Collection<? extends V> values) {
    getOrCreate(key).addAll(values);
  }  

  @Override
  public Collection<V> remove(Object key) {
    return map.remove(key);
  }

  /**
   * Removes from the {@code MultiMap} each occurrence of the specified 
   * value.
   * 
   * @param value the value to remove from the map
   */
  public void removeValue(V value) {
    map.values().forEach(set -> set.remove(value));
  }

  @Override
  public int size() {
    return map.size();
  }

  /**
   * Returns a String representation of this {@code MultiMap} object.
   * 
   * @return a String representation of this {@code MultiMap} object
   */
  @Override
  public String toString() {
    return map.entrySet()
              .stream()
              .map(entry -> entry.getKey() + " : " + entry.getValue())
              .collect(joining("\n"));
  }

  @Override
  public Collection<Collection<V>> values() {
    return map.values();
  }

  /**
   * Returns as a list all the values contained into this {@code MultiMap} 
   * object.  
   * 
   * @return as a list of all the values contained into this {@code MultiMap} 
   * object
   */
  public List<V> valueList() {
    return values().stream().flatMap(Collection::stream).collect(toList());
  }

  /**
   * Returns as a list all the values contained into this {@code MultiMap} 
   * object, ordered according to the specified comparator.  
   * 
   * @param c the comparator to determine the order of the list. A {@code null} 
   * value indicates that the elements' {@link Comparable <em>natural</em>} 
   * ordering should be used. 
   * @return as a list of all the values contained into this {@code MultiMap} 
   * object
   */
  public List<V> valueList(Comparator<? super V> c) {
    List<V> list = valueList();
    list.sort(c);
    return list;
  }

  /** Returns or creates the collection associated to the given key. */
  private Collection<V> getOrCreate(K key) {
    return map.computeIfAbsent(key, k -> collectionSupplier.get());
  }
}
