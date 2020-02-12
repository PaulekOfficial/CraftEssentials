package pro.paulek.CraftEssentials.cache;

public interface ISQLCache<K, V> extends ISimpleCache<K, V> {

    void delete(int id);

    V load(int id);

}
