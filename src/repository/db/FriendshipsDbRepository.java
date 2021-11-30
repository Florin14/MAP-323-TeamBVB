package repository.db;

import domain.Friendship;
import domain.validators.Validator;
import repository.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public Friendship save(Friendship entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        String sql = "insert into friendships ( friend_one_id, friend_two_id) values (?, ?)";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, entity.getId1());
            preparedStatement.setLong(2, entity.getId2());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Friendship delete(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("id must be not null");
        String sql = "delete from friendships where id = ?";
        try {Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    @Override
    public Friendship findOne(Long id) {
        return null;
    }

    @Override
    public List<Friendship> findAll() {
        return null;
    }
}