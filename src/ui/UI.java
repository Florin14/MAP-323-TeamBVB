package ui;

import domain.*;
import domain.validators.ValidationException;
import service.Graph;
import service.Service;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class UI {
    Scanner scanner = new Scanner(System.in);
    Service service;

    public UI(Service service) {
        this.service = service;

    }

    private void addUserUI() {
        try {
            System.out.println("Firstname: ");
            String firstName = scanner.nextLine();

            System.out.println("Lastname:");
            String lastName = scanner.nextLine();

            this.service.saveUser(firstName, lastName);
            System.out.println("User added!");
        } catch (IllegalArgumentException | ValidationException e) {
            System.out.println(e.getMessage());
        }

    }

    private void deleteUI() {
        try {
            System.out.println("Give the user id to delete:");
            Long id = scanner.nextLong();
            this.service.deleteUser(id);
            System.out.println("The user was deleted!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    void updateUserNameUI() {
        System.out.println("Give the user id to update:");
        Long id = scanner.nextLong();
        System.out.println("New firstname: ");
        String newFirstName = scanner.nextLine();

        System.out.println("New lastname:");
        String newLastName = scanner.nextLine();
        service.updateUser(id, newFirstName, newLastName);
    }

    private void addFriendshipUI() {
        showAllUI();

        try {
            System.out.println("Give the id of the first user:");
            Long id1 = scanner.nextLong();
            System.out.println("Give the id of the second user: ");
            Long id2 = scanner.nextLong();
            this.service.saveFriendship(id1, id2);
            System.out.println("The friendship was added!");
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteFriendshipUI() {
        showAllFriendshipsUI();
        try {
            System.out.println("Give the id for the friendship to be deleted: ");
            Long id1 = scanner.nextLong();
            this.service.deleteFriendship(id1);
            System.out.println("The friendship was deleted!");
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addMessageUI() {
        showAllUI();
        try {
            System.out.println("Message from user with id: ");
            String from = scanner.nextLine();
            System.out.println(" to user with id:");
            String to = scanner.nextLine();
            System.out.println("What do you want to say?");
            String text = scanner.nextLine();
            this.service.sendMessage(Long.parseLong(from), Long.parseLong(to), text);
            System.out.println("The message was send!");

        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteMessageUI() {
        showAllMessagesUI();
        System.out.println("Give the message id to delete:");
        Long id = scanner.nextLong();
        this.service.deleteMessage(id);
        System.out.println("Message deleted!");

    }

    private void showConversationUI() {
        System.out.println("The conversation between user with id:");
        Long id1 = scanner.nextLong();
        System.out.println(" and user with id: ");
        Long id2 = scanner.nextLong();
        List<Message> conversation = this.service.showConversation(id1, id2);
        conversation.forEach(System.out::println);
    }


    public void getNrOfConnectedComponents() {
        Graph graph = new Graph(this.service);
        int nr = graph.getNrOfConnectedComponents();
        System.out.println("The number of communities: " + nr);
    }

    public void getLargestConnectedComponent() {
        Graph graph = new Graph(this.service);
        System.out.println("The most sociable community is : ");
        Set<Long> largestConnectedComponent = graph.getLargestConnectedComponent();
        for (Long x : largestConnectedComponent) {
            User user = service.findOne(x);
            user.setId(x);
            System.out.println(user);
        }

    }

    private void showAllUI() {
        List<User> users = this.service.printAll();
        users.forEach(System.out::println);
    }

    private void showAllFriendshipsUI() {
        List<Friendship> friendship = this.service.printAllFriendships();
        friendship.forEach(System.out::println);
    }

    public void getFriendshipRelationsUI() {
        showAllUI();
        System.out.println("Give user's id: ");
        Long id = scanner.nextLong();
        List<Friendship> relations = service.getFriendshipRelations(id);
        for (Friendship friendship : relations) {
            if (friendship.getId1().equals(id)) {
                User user = service.findOne(friendship.getId2());
                System.out.println("\nFirstname: " + user.getFirstName() + "\nLastname: " + user.getLastName() + "\nFriendship date: " + friendship.getFriendshipDate());
            } else {
                User user = service.findOne(friendship.getId1());
                System.out.println("\nFirstname: " + user.getFirstName() + "\nLastname: " + user.getLastName() + "\nFriendship date: " + friendship.getFriendshipDate());

            }
        }
    }

    public void getFriendshipRelationsByMonth() {
        showAllUI();
        System.out.println("Give user's id: ");
        String id = scanner.nextLine();
        System.out.println("Give friendship's month(1-12) ");
        String month = scanner.nextLine();
        List<Friendship> relations = service.getFriendshipRelationsByMonth(Long.valueOf(id), Integer.parseInt(month));
        for (Friendship friendship : relations) {
            if (friendship.getId1().equals(Long.valueOf(id))) {
                User user = service.findOne(friendship.getId2());
                System.out.println("\nFirstname: " + user.getFirstName() + "\nLastname: " + user.getLastName() + "\nFriendship date: " + friendship.getFriendshipDate());
            } else {
                User user = service.findOne(friendship.getId1());
                System.out.println("\nFirstname: " + user.getFirstName() + "\nLastname: " + user.getLastName() + "\nFriendship date: " + friendship.getFriendshipDate());

            }
        }
    }

    private void sendFriendRequestUI() {
        showAllUI();

        try {
            System.out.println("Give the id of the first user:");
            Long id1 = scanner.nextLong();
            System.out.println("Give the id of the second user: ");
            Long id2 = scanner.nextLong();
            this.service.sendFriendRequest(id1, id2);
            System.out.println("The friend request was sent!");
        } catch (IllegalArgumentException | NullPointerException | ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteFriendRequestUI() {
        showAllFriendRequestsUI();
        System.out.println("Give the id for the friend request to be deleted: ");
        Long id = scanner.nextLong();
        this.service.deleteFriendRequests(id);
    }

    private void updateFriendRequestsUI() {
        showAllFriendRequestsUI();
        System.out.println("Give the id for the friend request to be updated: ");
        String id = scanner.nextLine();
        System.out.println("Accept the friend request?(Y/N)");
        String accept = scanner.nextLine();
        if ("Y".equals(accept) || "y".equals(accept)) {
            this.service.updateFriendRequest(Long.valueOf(id), Status.APPROVED);
        } else if ("N".equals(accept) || "n".equals(accept)) {
            this.service.updateFriendRequest(Long.valueOf(id), Status.REJECTED);
        } else {
            System.out.println("Wrong option!");
        }
    }

    private void showAllFriendRequestsUI() {
        List<FriendRequest> getAll = this.service.printAllFriendRequests();
        getAll.forEach(System.out::println);
    }

    private void showAllMessagesUI() {
        List<Message> getAll = this.service.printAllMessages();
        getAll.forEach(System.out::println);
    }


    private void menuPrint() {
        System.out.println("Menu: ");
        System.out.println("1. Add an user");
        System.out.println("2. Delete an user");
        System.out.println("3. Add a friendship");
        System.out.println("4. Delete a friendship");
        System.out.println("5. Show the number of communities");
        System.out.println("6. Show the biggest community");
        System.out.println("7. Show all users");
        System.out.println("8. Show all friendships");
        System.out.println("9. Update an user");
        System.out.println("10. Get friendship relations");
        System.out.println("11. Get friendship relations By Month");
        System.out.println("----------------------------");
        System.out.println("12. Send a message");
        System.out.println("13. Delete message");
        System.out.println("14. Show conversation");
        System.out.println("15. Show all messages");
        System.out.println("----------------------------");
        System.out.println("16. Send friend request");
        System.out.println("17. Delete friend request");
        System.out.println("18. Update friend request");
        System.out.println("19. Show all friend requests");
        System.out.println("----------------------------");
        System.out.println("20. Exit\n");
        System.out.println("Choose option:");
    }

    public void menu() {
        boolean bool = true;
        while (bool) {
            menuPrint();
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> addUserUI();
                case "2" -> deleteUI();
                case "3" -> addFriendshipUI();
                case "4" -> deleteFriendshipUI();
                case "5" -> getNrOfConnectedComponents();
                case "6" -> getLargestConnectedComponent();
                case "7" -> showAllUI();
                case "8" -> showAllFriendshipsUI();
                case "9" -> updateUserNameUI();
                case "10" -> getFriendshipRelationsUI();
                case "11" -> getFriendshipRelationsByMonth();
                case "12" -> addMessageUI();
                case "13" -> deleteMessageUI();
                case "14" -> showConversationUI();
                case "15" -> showAllMessagesUI();
                case "16" -> sendFriendRequestUI();
                case "17" -> deleteFriendRequestUI();
                case "18" -> updateFriendRequestsUI();
                case "19" -> showAllFriendRequestsUI();
                case "20" -> bool = false;
                default -> {
                }
            }
        }
    }


}
