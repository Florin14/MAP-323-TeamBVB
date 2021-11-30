package repository.db;

import domain.Friendship;
import domain.User;
import domain.validators.Validator;
import repository.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public User save(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        String sql = "insert into users (first_name, last_name ) values (?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        User user = new User(entity.getFirstName(),entity.getLastName());
        entities.put(entity.getId(), user);

        return null;
    }
    @Override
    public User delete(Long aLong) {
        if(aLong == null){
            throw new IllegalArgumentException("deleted entity doesn't exist");
        }

        String sql = "delete from users where id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Friendship> friendships = friendshipsDbRepository.findAll();
        for(Friendship friendship:friendships){
            if(friendship.getId1().equals(aLong) || friendship.getId2().equals(aLong) ){
                friendshipsDbRepository.delete(friendship.getId());
            }
        }
        entities.remove(aLong);

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

    @Override
    public User findOne(Long id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}