package repository.file;


import domain.Friendship;
import domain.validators.Validator;

import java.util.List;

public class FriendshipFileRepository extends AbstractFileRepository<Long, Friendship> {

    private static Long nextId = 0L;

    public FriendshipFileRepository(String fileName, Validator<Friendship> validator) {
        super(fileName,validator);
        nextId++;
    }

    @Override
    protected Friendship lineToEntity(String line) {
        String[] attributes = line.split(",");
        long id = Long.parseLong(attributes[0]);
        if (id > nextId) {
            nextId = id;
        }
        long friendOneId = Long.parseLong(attributes[1]);
        long friendTwoId = Long.parseLong(attributes[2]);
        Friendship friendship = new Friendship(friendOneId, friendTwoId);
        friendship.setId(id);
        return friendship;
    }

    @Override
    protected String entityToLine(Friendship friendship) {
        return friendship.getId() + "," + friendship.getId1() + "," + friendship.getId2();
    }


    @Override
    public Friendship save(Friendship entity) {
        entity.setId(nextId);
        nextId++;
        return super.save(entity);
    }

    @Override
    public Friendship delete(Long id) {
        return super.delete(id);
    }


    @Override
    public Friendship update(Friendship entity) {
        return null;
    }

    @Override
    public Friendship findOne(Long id) {
        return super.findOne(id);
    }

    @Override
    public List<Friendship> findAll() {
        return super.findAll();
    }
}

