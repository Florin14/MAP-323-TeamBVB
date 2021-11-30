package repository.db;

import domain.User;
import domain.validators.Validator;
import repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersDbRepository implements Repository<Long, User> {
    private final String url;
    private final String username;
    private final String password;

    private final Validator<User> validator;

    private final FriendshipsDbRepository friendshipsDbRepository ;
    protected Map<Long, User> entities;

    public UsersDbRepository(String url, String username, String password, FriendshipsDbRepository friendshipsDbRepository,Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.friendshipsDbRepository= friendshipsDbRepository;
        this.validator= validator;

        entities = new HashMap<>();
    }

    @Override
    public User findOne(Long id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User save(User entity) {
        return null;
    }

    @Override
    public User delete(Long id) {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public List<User> getFriends(User user) {
        return null;
    }
}