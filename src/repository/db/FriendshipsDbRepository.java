package repository.db;

import domain.Friendship;
import domain.validators.Validator;
import repository.Repository;

import java.util.List;

public class FriendshipsDbRepository implements Repository<Long, Friendship> {

    private final String url;
    private final String username;
    private final String password;

    private final Validator<Friendship> validator;

    public FriendshipsDbRepository(String url, String username, String password,Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator=validator;

    }

    @Override
    public Friendship findOne(Long id) {
        return null;
    }

    @Override
    public List<Friendship> findAll() {
        return null;
    }

    @Override
    public Friendship save(Friendship entity) {
        return null;
    }

    @Override
    public Friendship delete(Long id) {
        return null;
    }

    @Override
    public Friendship update(Friendship entity) {
        return null;
    }

    @Override
    public List<Friendship> getFriends(Friendship user) {
        return null;
    }
}