package pro.paulek.CraftEssentials.cache;

import java.util.Map;

public interface ISimpleCache<K, V> extends ICache {

    void save(K n, V t);

    default void save(Map<K, V> collection, boolean ignoreNotChanged) {
        collection.keySet().forEach(value -> {
            V v = collection.get(value);
            if(v instanceof IDirtyObject) {
                if(ignoreNotChanged && ((IDirtyObject)v).isDirty()) {
                    save(value, v);
                }
            } else {
                save(value, v);
            }
        });
    }

    void delete(K n);

    void add(K n, V t);

    V get(K n);

    default int count() {
        return 0;
    }

}
