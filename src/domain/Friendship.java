package domain;


import java.time.LocalDate;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private Long id1;
    private Long id2;
    private LocalDate friendshipDate;

    public Friendship(Long id1, Long id2,LocalDate friendshipDate) {
        this.id1 = id1;
        this.id2 = id2;
        this.friendshipDate = friendshipDate;
    }

    public Long getId1() {
        return id1;
    }

    public Long getId2() {
        return id2;
    }

    public LocalDate getFriendshipDate(){return this.friendshipDate;}

    public void setFriendshipDate(LocalDate friendshipDate1){this.friendshipDate = friendshipDate1;}
    @Override
    public String toString() {
        return "Friendship{" +
                "id friendship=" + id + '\'' +
                ", id1=" + id1 + '\'' +
                ", id2=" + id2 + '\'' +
                ", friendshipDate=" + friendshipDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship friendship = (Friendship) o;
        return Objects.equals(id1, friendship.id1) && Objects.equals(id2, friendship.id2) && Objects.equals(friendshipDate, friendship.friendshipDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }
}
