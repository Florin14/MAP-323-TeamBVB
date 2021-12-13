package service;

import domain.FriendRequest;
import domain.Friendship;
import domain.Status;
import domain.User;
import repository.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

public class Service {
    private final Repository<Long, User> userRepository;
    private final Repository<Long, Friendship> friendshipRepository;
    private final Repository<Long, FriendRequest> friendRequestRepository;

    public Service(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository, Repository<Long, FriendRequest> friendRequestRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    public void saveUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        this.userRepository.save(user);
    }

    public void updateUser(Long id, String firstName, String lastName) {
        for (User user : this.userRepository.findAll()) {
            if (user.getId().equals(id)) {
                User user1 = new User(firstName, lastName);
                user1.setId(user.getId());
                this.userRepository.update(user1);

            }
        }
    }

    public void saveFriendship(Long id1, Long id2) {
        boolean ok = true;
        if (id1 == null || id2 == null) {
            throw new IllegalArgumentException("Id's must be not null");
        }
        User user1 = userRepository.findOne(id1);
        User user2 = userRepository.findOne(id2);
        user1.setId(id1);
        user2.setId(id2);
        for (User user : user1.getFriends()) {
            if (Objects.equals(user.getId(), id2)) {
                ok = false;
                break;
            }
        }
        for (User user : user2.getFriends()) {
            if (Objects.equals(user.getId(), id1)) {
                ok = false;
                break;
            }
        }
        if (ok) {
            user1.makeFriend(user2);
            user2.makeFriend(user1);
            this.friendshipRepository.save(new Friendship(id1, id2, LocalDate.now()));
        }
    }

    public void deleteUser(Long id) {
        this.userRepository.delete(id);
    }

    public void deleteFriendship(Long id) {
        this.friendshipRepository.delete(id);
    }

    public User findOne(Long x) {
        return this.userRepository.findOne(x);
    }

    public List<User> printAll() {
        return this.userRepository.findAll();
    }

    public List<User> getFriends(User user) {
        return this.userRepository.getFriends(user);
    }

    public List<Friendship> printAllFriendships() {
        return this.friendshipRepository.findAll();
    }

    public List<Friendship> getFriendshipRelations(Long aLong) {
        List<Friendship> getAll = friendshipRepository.findAll();
        Predicate<Friendship> filterCriteria = x -> Objects.equals(x.getId1(), aLong) || Objects.equals(x.getId2(), aLong);
        return getAll.stream()
                .filter(filterCriteria)
                .toList();
    }
    public List<Friendship> getFriendshipRelationsByMonth(Long aLong, int month) {
        List<Friendship> getAll = friendshipRepository.findAll();
        Predicate<Friendship> filterById = x -> Objects.equals(x.getId1(), aLong) || Objects.equals(x.getId2(), aLong);
        Predicate<Friendship> filterByMonth = x -> x.getFriendshipDate().getMonthValue() == month;
        Predicate<Friendship> filterCriteria = filterById.and(filterByMonth);
        return getAll.stream()
                .filter(filterCriteria)
                .toList();
    }
    public void sendFriendRequest(Long id1, Long id2) {
        boolean ok = true;
        if (id1 == null || id2 == null) {
            throw new IllegalArgumentException("Id's must be not null");
        }
        List<Friendship> getAll = printAllFriendships();
        for (Friendship friendship : getAll) {
            if ((friendship.getId1().equals(id1) && friendship.getId2().equals(id2)) || friendship.getId1().equals(id2) && friendship.getId2().equals(id1)) {
                ok = false;
                break;
            }
        }
        if (ok) {
            String status = String.valueOf(Status.PENDING);
            this.friendRequestRepository.save(new FriendRequest(id1, id2, status));
        } else {
            throw new IllegalArgumentException("The friend request can't be sent!Friendship already exists");
        }
    }

    public List<FriendRequest> printAllFriendRequests() {
        return this.friendRequestRepository.findAll();
    }

    public void deleteFriendRequests(Long id) {
        this.friendRequestRepository.delete(id);
    }

    public void updateFriendRequest(Long id, Status status) {
        FriendRequest friendRequest = this.friendRequestRepository.findOne(id);
        if (status == Status.REJECTED) {
            this.friendRequestRepository.delete(id);
        } else if (status == Status.APPROVED) {
            deleteFriendRequests(id);
            this.saveFriendship(friendRequest.getId1(), friendRequest.getId2());
        }
    }

}
