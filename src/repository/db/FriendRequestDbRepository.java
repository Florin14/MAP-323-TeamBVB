package repository.db;

import domain.FriendRequest;
import domain.validators.Validator;
import repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestDbRepository implements Repository<Long, FriendRequest> {

    private final Connection connection;
    private final Validator<FriendRequest> validator;


    public FriendRequestDbRepository(Connection connection, Validator<FriendRequest> validator) {
        this.connection = connection;
        this.validator = validator;

    }

    @Override
    public FriendRequest save(FriendRequest entity) {
        boolean exists = false;
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");

        for (FriendRequest friendRequest : findAll()) {
            if (entity.equals(friendRequest) || (entity.getId1().equals(friendRequest.getId2()) && entity.getId2().equals(friendRequest.getId1()))) {
                exists = true;
                break;
            }
        }
        if (exists) {
            throw new IllegalArgumentException("friendship already exists!");
        }

        validator.validate(entity);
        String sql = "insert into friend_requests ( friend_one_id, friend_two_id, status) values (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, entity.getId1());
            preparedStatement.setLong(2, entity.getId2());
            preparedStatement.setString(3, String.valueOf(entity.getStatus()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public FriendRequest delete(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("id must be not null");
        String sql = "delete from friend_requests where id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FriendRequest findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");

        String sql = "select * from friend_requests where id = ? ";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            Long friendOneId = resultSet.getLong("friend_one_id");
            Long friendTwoId = resultSet.getLong("friend_two_id");
            String status = resultSet.getString("status");

            return new FriendRequest(friendOneId, friendTwoId, status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(FriendRequest entity) {

    }

    @Override
    public List<FriendRequest> findAll() {
        List<FriendRequest> friendships = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from friend_requests");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long friendOneId = resultSet.getLong("friend_one_id");
                Long friendTwoId = resultSet.getLong("friend_two_id");
                String status = resultSet.getString("status");
                FriendRequest friendship = new FriendRequest(friendOneId, friendTwoId, status);
                friendship.setId(id);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public List<FriendRequest> getFriends(FriendRequest friendRequest) {
        return null;
    }

}
