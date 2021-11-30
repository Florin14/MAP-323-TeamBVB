import domain.Friendship;
import domain.User;
import domain.validators.FriendshipValidator;
import domain.validators.UserValidator;


import repository.Repository;
import repository.db.FriendshipsDbRepository;
import repository.db.UsersDbRepository;
import service.Service;
import ui.UI;

public class Main {

    public static void main(String[] args) {
 //        FriendshipFileRepository friendshipFileRepository1 = new FriendshipFileRepository("data/friendships.csv",new FriendshipValidator());
////        Repository<Long,User> userRepository = new UserFileRepository("data/users.csv",friendshipFileRepository1,new UserValidator());
////       Repository<Long,Friendship> friendshipRepository = new FriendshipFileRepository("data/friendships.csv",new FriendshipValidator());

        FriendshipsDbRepository friendshipRepository1 = new FriendshipsDbRepository("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgre", new FriendshipValidator());
        Repository<Long, Friendship> friendshipRepository = new FriendshipsDbRepository("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgre", new FriendshipValidator());
        Repository<Long, User> userRepository = new UsersDbRepository("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgre", friendshipRepository1, new UserValidator());

        Service service = new Service(userRepository, friendshipRepository);

        UI ui = new UI(service);

        ui.menu();

    }
}
