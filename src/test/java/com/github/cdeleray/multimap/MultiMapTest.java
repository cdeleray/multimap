/*
 * MIT License
 *
 * Copyright (c) 2023 Christophe Deleray
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests class for {@link MultiMap}.
 *
 * @author Christophe Deleray
 */
public class MultiMapTest {
    private MultiMap<String, Integer> map;

    @BeforeEach
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
    @Test
    public void testContainsKey() {
        assertTrue(map.containsKey("a"));
        assertTrue(map.containsKey("b"));
        assertTrue(map.containsKey("c"));
    }

    /**
     * Test method for {@link MultiMap#containsValue(java.lang.Object)}.
     */
    @Test
    public void testContainsValue() {
        assertTrue(map.containsValue(1));
        assertTrue(map.containsValue(10));
        assertTrue(map.containsValue(2));
        assertTrue(map.containsValue(3));
    }

    /**
     * Test method for {@link MultiMap#containsValues(java.util.Collection)}.
     */
    @Test
    public void testContainsValues() {
        assertTrue(map.containsValues(asList(1, 10)));
        assertTrue(map.containsValues(singletonList(2)));
        assertTrue(map.containsValues(singletonList(3)));
    }

    /**
     * Test method for {@link MultiMap#entrySet()}.
     */
    @Test
    public void testEntrySet() {
        HashSet<Map.Entry<String, Collection<Integer>>> expected = new HashSet<>();
        expected.add(new AbstractMap.SimpleEntry<>("a", asList(1, 10)));
        expected.add(new AbstractMap.SimpleEntry<>("b", singletonList(2)));
        expected.add(new AbstractMap.SimpleEntry<>("c", singletonList(3)));

        assertEquals(map.entrySet(), expected);
    }

    /**
     * Test method for {@link MultiMap#get(java.lang.Object)}.
     */
    @Test
    public void testGet() {
        assertEquals(map.get("a"), asList(1, 10));
        assertEquals(map.get("b"), singletonList(2));
        assertEquals(map.get("c"), singletonList(3));
    }

    /**
     * Test method for {@link MultiMap#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        Assertions.assertFalse(map.isEmpty());
        assertTrue(new MultiMap<>().isEmpty());
    }

    /**
     * Test method for {@link MultiMap#keySet()}.
     */
    @Test
    public void testKeySet() {
        assertEquals(map.keySet(), Set.of("a", "b", "c"));
    }

    /**
     * Test method for {@link MultiMap#put(java.lang.Object, java.util.Collection)}.
     */
    @Test
    public void testPutKCollectionOfV() {
        MultiMap<String, Integer> multiMap = new MultiMap<>();
        multiMap.put("a", asList(1, 10));
        multiMap.putValue("b", 2);
        multiMap.putValue("c", 3);

        assertEquals(multiMap, this.map);
    }

    /**
     * Test method for {@link MultiMap#putAll(java.util.Map)}.
     */
    @Test
    public void testPutAllMap() {
        MultiMap<String, Integer> multiMap = new MultiMap<>();
        multiMap.putAll(this.map);

        assertEquals(multiMap, this.map);
    }

    /**
     * Test method for {@link MultiMap#putAll(Object, java.util.Collection)}.
     */
    @Test
    public void testPutAllCollection() {
        MultiMap<String, Integer> multiMap = new MultiMap<>();
        multiMap.putAll("a", asList(1, 10));
        multiMap.putAll("b", singletonList(2));
        multiMap.putAll("c", singletonList(3));

        assertEquals(multiMap, map);
    }

    /**
     * Test method for {@link MultiMap#clear()}.
     */
    @Test
    public void testClear() {
        map.clear();

        assertTrue(map.isEmpty());
        assertEquals(map.size(), 0);
    }

    /**
     * Test method for {@link MultiMap#remove(java.lang.Object)}.
     */
    @Test
    public void testRemove() {
        assertEquals(map.remove("a"), asList(1, 10));
        assertEquals(map.remove("b"), singletonList(2));
        assertEquals(map.remove("c"), singletonList(3));
        assertTrue(map.isEmpty());
    }

    /**
     * Test method for {@link MultiMap#removeValue(Object)}.
     */
    @Test
    public void testRemoveValue() {
        map.putValue("c", 1);

        map.removeValue(1);

        assertEquals(map.get("a"), singletonList(10));
        assertEquals(map.get("b"), singletonList(2));
        assertEquals(map.get("c"), singletonList(3));
    }

    /**
     * Test method for {@link MultiMap#size()}.
     */
    @Test
    public void testSize() {
        assertEquals(map.size(), 3);
        assertEquals(new MultiMap<>().size(), 0);
    }

    /**
     * Test method for {@link MultiMap#values()}.
     */
    @Test
    public void testValues() {
        List<Collection<Integer>> expected = new ArrayList<>(asList(asList(1, 10), singletonList(2), singletonList(3)));

        expected.removeAll(map.values());
        assertTrue(expected.isEmpty());
    }

    /**
     * Test method for {@link MultiMap#valueList()}.
     */
    @Test
    public void testValueList() {
        assertEquals(map.valueList(), asList(1, 10, 2, 3));
    }

    /**
     * Test method for {@link MultiMap#equals(Object)} and
     * for {@link MultiMap#hashCode()}.
     */
    @Test
    public void testEquals() {
        MultiMap<Item, Item> mm1 = new MultiMap<>(HashMap::new, HashSet::new);
        for (int i = 0; i <= 5; i++) {
            for (int j = 10; j <= 12; j++) {
                mm1.putValue(new Item(i, "key" + i), new Item(j * 2, "value" + i));
            }
        }

        MultiMap<Item, Item> mm2 = new MultiMap<>(HashMap::new, HashSet::new);
        for (int i = 0; i <= 5; i++) {
            for (int j = 10; j <= 12; j++) {
                mm2.putValue(new Item(i, "key" + i), new Item(j * 2, "value" + i));
            }
        }

        MultiMap<Item, Item> mm3 = new MultiMap<>();
        for (int i = 4; i >= 0; i--) {
            mm3.putValue(new Item(i, "key" + i), new Item(i * 2, "value" + i));
        }

        assertEquals(mm1, mm1);
        assertEquals(mm2, mm1);
        assertEquals(mm1, mm2);
        Assertions.assertNotEquals(mm1, null);
        Assertions.assertNotEquals(mm1, "Hello");
        Assertions.assertNotEquals(mm3, mm1);
        Assertions.assertNotEquals(mm1, mm3);

        assertEquals(mm2.hashCode(), mm1.hashCode());
        Assertions.assertNotEquals(mm3.hashCode(), mm1.hashCode());

        Comparator<Item> c = Item::compareTo;

        assertEquals(mm2.valueList(), mm1.valueList());
        assertEquals(mm2.entrySet(), mm1.entrySet());
        assertEquals(mm2.valueList(c), mm1.valueList(c));
        assertEquals(mm2.entrySet(), mm1.entrySet());
    }

    /**
     * Test method for {@link MultiMap#equals(Object)} and
     * for {@link MultiMap#hashCode()}.
     */
    @Test
    public void testEquals_HashMap_HashSet() {
        MultiMap<Item, Item> mm1 = new MultiMap<>(HashMap::new, HashSet::new);
        for (int i = 0; i <= 5; i++) {
            for (int j = 10; j <= 12; j++) {
                mm1.putValue(new Item(i, "key" + i), new Item(j * 2, "value" + i));
            }
        }

        MultiMap<Item, Item> mm1bis = new MultiMap<>(HashMap::new, HashSet::new);
        for (int i = 0; i <= 5; i++) {
            for (int j = 10; j <= 12; j++) {
                mm1bis.putValue(new Item(i, "key" + i), new Item(j * 2, "value" + i));
            }
        }

        MultiMap<Item, Item> mm2 = new MultiMap<>(HashMap::new, HashSet::new);
        for (int i = 5; i >= 0; i--) {
            for (int j = 12; j >= 10; j--) {
                mm2.putValue(new Item(i, "key" + i), new Item(j * 2, "value" + i));
            }
        }

        MultiMap<Item, Item> mm3 = new MultiMap<>();
        for (int i = 4; i >= 0; i--) {
            mm3.putValue(new Item(i, "key" + i), new Item(i * 2, "value" + i));
        }

        assertEquals(mm1, mm1);

        assertEquals(mm1, mm1bis);
        assertEquals(mm1bis, mm1);
        assertEquals(mm1bis.hashCode(), mm1.hashCode());

        assertEquals(mm2, mm1);
        assertEquals(mm1, mm2);
        assertEquals(mm2.hashCode(), mm1.hashCode());

        Assertions.assertNotEquals(mm1, null);
        Assertions.assertNotEquals(mm1, "Hello world!");
        Assertions.assertNotEquals(mm3, mm1);
        Assertions.assertNotEquals(mm1, mm3);

        Comparator<Item> c = Item::compareTo;

        assertEquals(mm1bis.valueList(), mm1.valueList());
        assertEquals(mm1bis.entrySet(), mm1.entrySet());
        assertEquals(mm2.valueList(c), mm1.valueList(c));
        assertEquals(mm2.entrySet(), mm1.entrySet());
    }

    /**
     * Test for serialization.
     */
    @Test
    public void testSerializable() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(1 << 15);

        try (ObjectOutputStream out = new ObjectOutputStream(bout)) {
            out.writeObject(map);
        }

        assertTrue(bout.size() > 0);

        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray()))) {
            MultiMap<String, Integer> actual = (MultiMap<String, Integer>) in.readObject();
            assertEquals(actual, map);
        }
    }

    private record Item(Integer id, String text) implements Comparable<Item> {
        @Override
        public int compareTo(Item item) {
            var compareIds = Comparator.comparing(Item::id);
            var byText = Comparator.comparing(Item::text);
            var comp = compareIds.thenComparing(byText);
            return comp.compare(this, item);
        }
    }
}
