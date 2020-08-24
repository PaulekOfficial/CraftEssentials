package pro.paulek.CraftEssentials.data.models.mysql;

import pro.paulek.CraftEssentials.user.IUser;
import pro.paulek.api.data.Data;

import java.util.Collection;
import java.util.UUID;

public class UserDataModel implements Data<IUser, UUID> {

    @Override
    public IUser load(UUID uuid) {
        return null;
    }

    @Override
    public void createTable() {

    }

    @Override
    public IUser load(int id) {
        return null;
    }

    @Override
    public void load() {

    }

    @Override
    public void save(Collection<IUser> collection, boolean ignoreNotChanged) {

    }

    @Override
    public void save(IUser iUser) {

    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public int count() {
        return 0;
    }
}
