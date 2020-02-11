package pro.paulek.CraftEssentials.cache;

public interface ISimpleCache<K, V> extends ICache {

    void save(K n, V t);

    void delete(K n);

    void add(K n, V t);

    V get(K n);

}
