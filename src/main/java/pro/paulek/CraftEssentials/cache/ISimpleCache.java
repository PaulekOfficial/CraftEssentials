package pro.paulek.CraftEssentials.cache;

public interface ISimpleCache<N, T> extends ICache {

    void save(N n, T t);

    void delete(N n);

    void add(N n, T t);

    T get(N n);

}
