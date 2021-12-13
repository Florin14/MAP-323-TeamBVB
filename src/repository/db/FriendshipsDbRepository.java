package repository.db;

import domain.Friendship;
import domain.validators.Validator;
import repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendshipsDbRepository implements Repository<Long, Friendship> {

    private final Connection connection;

    private final Validator<Friendship> validator;

    public FriendshipsDbRepository(Connection connection, Validator<Friendship> validator) {
        this.connection = connection;
        this.validator = validator;

    }

    @Override
    public Friendship save(Friendship entity) {
        boolean exists = false;
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");

        for(Friendship friendship: findAll()){
            if (entity.equals(friendship) || (entity.getId1().equals(friendship.getId2()) && entity.getId2().equals(friendship.getId1()))) {
                exists = true;
                break;
            }
        }
        if(exists){
            throw new IllegalArgumentException("friendship already exists!");
        }


        validator.validate(entity);
        String sql = "insert into friendships ( friend_one_id, friend_two_id, friendship_date) values (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, entity.getId1());
            preparedStatement.setLong(2, entity.getId2());
            preparedStatement.setDate(3, Date.valueOf(String.valueOf(entity.getFriendshipDate())));
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
    public Friendship findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");

        String sql = "select * from friendships where id = ? ";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            Long friendOneId = resultSet.getLong("friend_one_id");
            Long friendTwoId = resultSet.getLong("friend_two_id");
            String friendshipDate1 = String.valueOf(resultSet.getDate("friendship_date"));
            LocalDate friendshipDate = LocalDate.parse(friendshipDate1);

            return new Friendship(friendOneId, friendTwoId, friendshipDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Friendship entity) {

    }

    @Override
    public List<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long friendOneId = resultSet.getLong("friend_one_id");
                Long friendTwoId = resultSet.getLong("friend_two_id");
                String friendshipDate1 = String.valueOf(resultSet.getDate("friendship_date"));
                LocalDate friendshipDate = LocalDate.parse(friendshipDate1);
                Friendship friendship = new Friendship(friendOneId, friendTwoId,friendshipDate);
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
    public List<Friendship> getFriends(Friendship friendship) {
        return null;
    }

}