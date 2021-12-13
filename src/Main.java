import domain.FriendRequest;
import domain.Friendship;
import domain.Message;
import domain.User;
import domain.validators.FriendRequestValidator;
import domain.validators.FriendshipValidator;
import domain.validators.MessageValidator;
import domain.validators.UserValidator;


import repository.Repository;
import repository.db.FriendRequestDbRepository;
import repository.db.FriendshipsDbRepository;
import repository.db.MessageDbRepository;
import repository.db.UsersDbRepository;
import service.Service;
import ui.UI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Data/database.csv"));
            String user = br.readLine();
            String password = br.readLine();
            String url = br.readLine();

            Connection connection = DriverManager.getConnection(url, user, password);

            FriendshipsDbRepository friendshipRepository1 = new FriendshipsDbRepository(connection, new FriendshipValidator());

            Repository<Long, Friendship> friendshipRepository = new FriendshipsDbRepository(connection, new FriendshipValidator());
            Repository<Long, User> userRepository = new UsersDbRepository(connection, friendshipRepository1, new UserValidator());
            Repository<Long, Message> messageRepository = new MessageDbRepository(connection, new MessageValidator());
            Repository<Long, FriendRequest> friendRequestRepository = new FriendRequestDbRepository(connection, new FriendRequestValidator());
            Service service = new Service(userRepository, friendshipRepository, friendRequestRepository, messageRepository);

            UI ui = new UI(service);

            ui.menu();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }
}
