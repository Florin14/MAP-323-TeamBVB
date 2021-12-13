import domain.Friendship;
import domain.User;
import domain.validators.FriendshipValidator;
import domain.validators.UserValidator;


import repository.Repository;
import repository.db.FriendshipsDbRepository;
import repository.db.UsersDbRepository;
import service.Service;
import ui.UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static final String url = "jdbc:postgresql://localhost:5432/laborator";
    public static final String user = "postgres";
    public static final String password = "postgres";

    public static void main(String[] args) throws SQLException {
//        FriendshipFileRepository friendshipFileRepository1 = new FriendshipFileRepository("data/friendships.csv", new FriendshipValidator());
//        Repository<Long, User> userRepository = new UserFileRepository("data/users.csv", friendshipFileRepository1, new UserValidator());
//        Repository<Long, Friendship> friendshipRepository = new FriendshipFileRepository("data/friendships.csv", new FriendshipValidator());
        Connection connection = DriverManager.getConnection(url, user, password);

        FriendshipsDbRepository friendshipRepository1 = new FriendshipsDbRepository(connection, new FriendshipValidator());
        Repository<Long, Friendship> friendshipRepository = new FriendshipsDbRepository(connection, new FriendshipValidator());
        Repository<Long, User> userRepository = new UsersDbRepository(connection, friendshipRepository1, new UserValidator());

        Service service = new Service(userRepository, friendshipRepository);

        UI ui = new UI(service);

        ui.menu();

    }
}
