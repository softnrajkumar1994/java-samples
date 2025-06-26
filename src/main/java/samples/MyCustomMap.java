package samples;

import lombok.ToString;

import java.util.Objects;


@ToString
public class MyCustomMap<K, V> {
    private static final int capacity = 16;
    private static final float load_factor = 0.75f;
    private Entry<K, V>[] buckets;
    private int size = 0;

    public MyCustomMap() {
        buckets = new Entry[capacity];
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    public void put(K key, V value) {
        int index = getBucketIndex(key);
        Entry<K, V> head = buckets[index];
        while (head != null) {
            if (Objects.equals(head.key, key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        Entry<K, V> newEntry = new Entry<K, V>(key, value);
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
        size++;

        // Check load factor
        if ((1.0 * size) / buckets.length >= load_factor) {
            resize();
        }
    }

    public V get(K key) {
        int index = getBucketIndex(key);
        Entry<K, V> head = buckets[index];

        while (head != null) {
            if (Objects.equals(head.key, key)) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = new Entry[oldBuckets.length * 2];
        size = 0;

        for (Entry<K, V> head : oldBuckets) {
            while (head != null) {
                put(head.key, head.value);
                head = head.next;
            }
        }
    }

    public int size() {
        return size;
    }

    @ToString
    class Entry<K, V> {

        K key;
        V value;

        Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }
}
