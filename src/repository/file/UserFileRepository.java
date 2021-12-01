package repository.file;

import domain.Friendship;
import domain.User;
import domain.validators.Validator;

import java.util.ArrayList;
import java.util.List;


public class UserFileRepository extends AbstractFileRepository<Long, User> {

    private static Long nextId = 0L;
    private final FriendshipFileRepository friendshipFileRepository;

    public UserFileRepository(String fileName, FriendshipFileRepository friendshipFileRepository, Validator<User> validator) {
        super(fileName, validator);
        this.friendshipFileRepository = friendshipFileRepository;
        nextId++;
    }


    @Override
    public User save(User entity) {
        entity.setId(nextId);
        nextId++;
        return super.save(entity);
    }

    public User delete(Long id) {
        List<Friendship> friendships = friendshipFileRepository.findAll();
        for (Friendship friendship : friendships) {
            if (friendship.getId1().equals(id) || friendship.getId2().equals(id)) {
                friendshipFileRepository.delete(friendship.getId());
            }

        }
        return super.delete(id);
    }


    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return super.findAll();
    }

    @Override
    public User findOne(Long aLong) {
        return super.findOne(aLong);
    }

    @Override
    public List<User> getFriends(User user) {
        List<User> users = new ArrayList<>();
        List<Friendship> friendships = friendshipFileRepository.findAll();
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

    @Override
    protected User lineToEntity(String line) {
        String[] attributes = line.split(",");
        long id = Long.parseLong(attributes[0]);
        if (id > nextId) {
            nextId = id;
        }
        String firstName = attributes[1];
        String lastName = attributes[2];
        User user = new User(firstName, lastName);
        user.setId(Long.parseLong(attributes[0]));
        return user;
    }

    @Override
    protected String entityToLine(User user) {
        return user.getId() + "," + user.getFirstName() + "," + user.getLastName();
    }

}
