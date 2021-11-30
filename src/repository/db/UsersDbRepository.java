package repository.db;

import domain.Friendship;
import domain.User;
import domain.validators.Validator;
import repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersDbRepository implements Repository<Long, User> {
    private final String url;
    private final String username;
    private final String password;

    private final Validator<User> validator;

    private final FriendshipsDbRepository friendshipsDbRepository;
    protected Map<Long, User> entities;

    public UsersDbRepository(String url, String username, String password, FriendshipsDbRepository friendshipsDbRepository, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.friendshipsDbRepository = friendshipsDbRepository;
        this.validator = validator;

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
        User user = new User(entity.getFirstName(), entity.getLastName());
        entities.put(entity.getId(), user);

        return null;
    }

    @Override
    public User delete(Long aLong) {
        if (aLong == null) {
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
        for (Friendship friendship : friendships) {
            if (friendship.getId1().equals(aLong) || friendship.getId2().equals(aLong)) {
                friendshipsDbRepository.delete(friendship.getId());
            }
        }
        entities.remove(aLong);

        return null;
    }

    @Override
    public User update(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        String sql = "update users set first_name = ?, last_name = ? where id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setLong(3, entity.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        entities.put(entity.getId(), entity);
        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
        }
        return null;
    }

    @Override
    public User findOne(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("ID must be not null");
        String sql = "select * from users where id = ? ";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, aLong);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");

            return new User(firstName, lastName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                User user = new User(firstName, lastName);
                user.setId(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> getFriends(User user) {
        List<Friendship> friendships = friendshipsDbRepository.findAll();
        List<User> users = new ArrayList<>();
        for (Friendship friendship : friendships) {
            if (friendship.getId1().equals(user.getId())) {
                User user2 = this.findOne(friendship.getId2());
                user2.setId(friendship.getId2());
                users.add(user2);
            }
            if (friendship.getId2().equals(user.getId())) {
                User user3 = this.findOne(friendship.getId1());
                user3.setId(friendship.getId1());
                users.add(user3);
            }
        }
        return users;
    }
}