package com.github.cdeleray.multimap;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests class for {@link MultiMap}. 
 *
 * @author Christophe Deleray
 */
@Test
public class MultiMapTest {
  private MultiMap<String, Integer> map;

  @BeforeMethod
  protected void setUp() {
    map = new MultiMap<>();
    map.putValue("a", 1);
    map.putValue("a", 10);
    map.putValue("b", 2);    
    map.putValue("c", 3);
  }

  /**
   * Test method for {@link MultiMap#containsKey(java.lang.Object)}.
   */
  public void testContainsKey() {
    Assert.assertTrue(map.containsKey("a"));
    Assert.assertTrue(map.containsKey("b"));
    Assert.assertTrue(map.containsKey("c"));
  }

  /**
   * Test method for {@link MultiMap#containsValue(java.lang.Object)}.
   */
  public void testContainsValue() {
    Assert.assertTrue(map.containsValue(1));
    Assert.assertTrue(map.containsValue(10));
    Assert.assertTrue(map.containsValue(2));
    Assert.assertTrue(map.containsValue(3));
  }

  /**
   * Test method for {@link MultiMap#containsValues(java.util.Collection)}.
   */
  public void testContainsValues() {
    Assert.assertTrue(map.containsValues(asList(1, 10)));
    Assert.assertTrue(map.containsValues(singletonList(2)));
    Assert.assertTrue(map.containsValues(singletonList(3)));
  }

  /**
   * Test method for {@link MultiMap#entrySet()}.
   */
  public void testEntrySet() {
    HashSet<Map.Entry<String, Collection<Integer>>> expected = new HashSet<>();
    expected.add(new AbstractMap.SimpleEntry<>("a", asList(1, 10)));
    expected.add(new AbstractMap.SimpleEntry<>("b", singletonList(2)));
    expected.add(new AbstractMap.SimpleEntry<>("c", singletonList(3)));

    Assert.assertEquals(map.entrySet(), expected);
  }

  /**
   * Test method for {@link MultiMap#get(java.lang.Object)}.
   */
  public void testGet() {
    Assert.assertEquals(map.get("a"), asList(1, 10));
    Assert.assertEquals(map.get("b"), singletonList(2));
    Assert.assertEquals(map.get("c"), singletonList(3));
  }

  /**
   * Test method for {@link MultiMap#isEmpty()}.
   */
  public void testIsEmpty() {
    Assert.assertFalse(map.isEmpty());
    Assert.assertTrue(new MultiMap<>().isEmpty());
  }

  /**
   * Test method for {@link MultiMap#keySet()}.
   */
  public void testKeySet() {
    Assert.assertEquals(map.keySet(), asList("a", "b", "c"));
  }

  /**
   * Test method for {@link MultiMap#put(java.lang.Object, java.util.Collection)}.
   */
  public void testPutKCollectionOfV() {
    MultiMap<String, Integer> multiMap = new MultiMap<>();
    multiMap.put("a", asList(1, 10));
    multiMap.putValue("b", 2);    
    multiMap.putValue("c", 3);
    
    Assert.assertEquals(multiMap, this.map);
  }

  /**
   * Test method for {@link MultiMap#putAll(java.util.Map)}.
   */
  public void testPutAllMap() {
    MultiMap<String,Integer> multiMap = new MultiMap<>();
    multiMap.putAll(this.map);
    
    Assert.assertEquals(multiMap, this.map);
  }

  /**
   * Test method for {@link MultiMap#putAll(Object, java.util.Collection)}.
   */
  public void testPutAllCollection() {
    MultiMap<String, Integer> multiMap = new MultiMap<>();
    multiMap.putAll("a", asList(1,10));
    multiMap.putAll("b", singletonList(2));
    multiMap.putAll("c", singletonList(3));
    
    Assert.assertEquals(multiMap, map);
  }

  /**
   * Test method for {@link MultiMap#clear()}.
   */
  public void testClear() {
    map.clear();
    
    Assert.assertTrue(map.isEmpty());
    Assert.assertEquals(map.size(), 0);
  }

  /**
   * Test method for {@link MultiMap#remove(java.lang.Object)}.
   */
  public void testRemove() {
    Assert.assertEquals(map.remove("a"), asList(1, 10));
    Assert.assertEquals(map.remove("b"), singletonList(2));
    Assert.assertEquals(map.remove("c"), singletonList(3));
    Assert.assertTrue(map.isEmpty());
  }

  /**
   * Test method for {@link MultiMap#removeValue(Object)}.
   */
  public void testRemoveValue() {
    map.putValue("c", 1);
    
    map.removeValue(1);
    
    Assert.assertEquals(map.get("a"), singletonList(10));
    Assert.assertEquals(map.get("b"), singletonList(2));
    Assert.assertEquals(map.get("c"), singletonList(3));
  }

  /**
   * Test method for {@link MultiMap#size()}.
   */
  public void testSize() {
    Assert.assertEquals(map.size(), 3);
    Assert.assertEquals(new MultiMap<>().size(), 0);
  }

  /**
   * Test method for {@link MultiMap#values()}.
   */
  public void testValues() {
    List<Collection<Integer>> expected = new ArrayList<>(asList(asList(1, 10), singletonList(2), singletonList(3)));
    
    expected.removeAll(map.values());
    Assert.assertTrue(expected.isEmpty()); 
  }

  /**
   * Test method for {@link MultiMap#valueList()}.
   */
  public void testValueList() {
    Assert.assertEquals(map.valueList(), asList(1,10,2,3));
  }
  
  /**
   * Test method for {@link MultiMap#equals(Object)} and
   * for {@link MultiMap#hashCode()}.
   */
  public void testEquals() {
    MultiMap<Item, Item> mm1 = new MultiMap<>(HashMap::new, HashSet::new);
    for(int i = 0; i <= 5; i++) {
      for(int j = 10; j <= 12; j++) {
        mm1.putValue(new Item(i,"key"+i), new Item(j*2,"value"+i));
      }
    }

    MultiMap<Item, Item> mm2 = new MultiMap<>(HashMap::new, HashSet::new);
    for(int i = 0; i <= 5; i++) {
      for(int j = 10; j <= 12; j++) {
        mm2.putValue(new Item(i,"key"+i), new Item(j*2,"value"+i));
      }
    }

    MultiMap<Item, Item> mm3 = new MultiMap<>();
    for(int i = 4; i >=0; i--) {
      mm3.putValue(new Item(i,"key"+i), new Item(i*2,"value"+i));
    }

    Assert.assertEquals(mm1, mm1);
    Assert.assertEquals(mm2, mm1);
    Assert.assertEquals(mm1, mm2);
    Assert.assertNotEquals(mm1, null);
    Assert.assertNotEquals(mm1, "Hello");
    Assert.assertNotEquals(mm3, mm1);
    Assert.assertNotEquals(mm1, mm3);

    Assert.assertEquals(mm2.hashCode(), mm1.hashCode());
    Assert.assertNotEquals(mm3.hashCode(), mm1.hashCode());
    
    Comparator<Item> c = Item::compareTo;
    
    Assert.assertEquals(mm2.valueList(), mm1.valueList());
    Assert.assertEquals(mm2.entrySet(), mm1.entrySet());
    Assert.assertEquals(mm2.valueList(c), mm1.valueList(c));
    Assert.assertEquals(mm2.entrySet(), mm1.entrySet());
  }

  /**
   * Test method for {@link MultiMap#equals(Object)} and
   * for {@link MultiMap#hashCode()}.
   */
  public void testEquals_HashMap_HashSet() {
    MultiMap<Item, Item> mm1 = new MultiMap<>(HashMap::new, HashSet::new);
    for(int i = 0; i <= 5; i++) {
      for(int j = 10; j <= 12; j++) {
        mm1.putValue(new Item(i,"key"+i), new Item(j*2,"value"+i));
      }
    }

    MultiMap<Item, Item> mm1bis = new MultiMap<>(HashMap::new, HashSet::new);
    for(int i = 0; i <= 5; i++) {
      for(int j = 10; j <= 12; j++) {
        mm1bis.putValue(new Item(i,"key"+i), new Item(j*2,"value"+i));
      }
    }

    MultiMap<Item, Item> mm2 = new MultiMap<>(HashMap::new, HashSet::new);
    for(int i = 5; i >=0; i--) {
      for(int j = 12; j >= 10; j--) {
        mm2.putValue(new Item(i,"key"+i), new Item(j*2,"value"+i));
      }
    }

    MultiMap<Item, Item> mm3 = new MultiMap<>();
    for(int i = 4; i >=0; i--) {
      mm3.putValue(new Item(i,"key"+i), new Item(i*2,"value"+i));
    }

    Assert.assertEquals(mm1, mm1);
    
    Assert.assertEquals(mm1, mm1bis);
    Assert.assertEquals(mm1bis, mm1);
    Assert.assertEquals(mm1bis.hashCode(), mm1.hashCode());

    Assert.assertEquals(mm2, mm1);
    Assert.assertEquals(mm1, mm2);
    Assert.assertEquals(mm2.hashCode(), mm1.hashCode());

    Assert.assertNotEquals(mm1, null);
    Assert.assertNotEquals(mm1, "Hello world!");
    Assert.assertNotEquals(mm3, mm1);
    Assert.assertNotEquals(mm1, mm3);
    
    Comparator<Item> c = Item::compareTo;
    
    Assert.assertEquals(mm1bis.valueList(), mm1.valueList());
    Assert.assertEquals(mm1bis.entrySet(), mm1.entrySet());
    Assert.assertEquals(mm2.valueList(c), mm1.valueList(c));
    Assert.assertEquals(mm2.entrySet(), mm1.entrySet());
  }
  
  /**
   * Test for serialization.
   */
  public void testSerializable() throws Exception {
    ByteArrayOutputStream bout = new ByteArrayOutputStream(1<<15);
    
    try (ObjectOutputStream out = new ObjectOutputStream(bout)) {
      out.writeObject(map);
    }
    
    Assert.assertTrue(bout.size() > 0);
    
    try(ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray()))) {
      MultiMap<String,Integer> actual = (MultiMap<String,Integer>)in.readObject();
      Assert.assertEquals(actual, map);
    }
  }

  private static class Item implements Comparable<Item> {
    private final Integer  id;
    private final String text;

    Item(Integer id, String text) {
      this.id = id;
      this.text = text;
    }

    Integer getId() {
      return id;
    }

    String getText() {
      return text;
    }
    
    @Override
    public boolean equals(Object obj) {
      if(this == obj) {
        return true;
      }
      
      if(!(obj instanceof Item)) {
        return false;
      }
      
      Item item = (Item)obj;
      return Objects.equals(id, item.id) && Objects.equals(text, item.text);
    }

    @Override
    public int hashCode() {
      int result = 17;
      result = 37*result + Objects.hashCode(id);
      result = 37*result + Objects.hashCode(text);
      return result;
    }

    /**
     * Returns a String representation of this {@code Item}.
     * 
     * @return a String representation of this {@code Item}
     */
    @Override
    public String toString() {
      return "{" + id + ',' + text + '}';
    }

    @Override
    public int compareTo(Item item) {
      Comparator<Item> compareIds = Comparator.comparing(Item::getId);
      Comparator<Item> byText = Comparator.comparing(Item::getText);
      Comparator<Item> comp = compareIds.thenComparing(byText);
      return comp.compare(this, item);
    }
  }
}
