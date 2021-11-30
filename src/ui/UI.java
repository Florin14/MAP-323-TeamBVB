package ui;

import domain.Friendship;
import domain.User;
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
        System.out.println("10. Exit\n");
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
                case "10" -> bool = false;
                default -> {
                }
            }
        }
    }

}
