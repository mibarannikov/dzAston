package repository;

import entity.User;

public class UserRepository extends AbstractRepository<User> {
    public UserRepository() {
        super(User.class);
    }

    @Override
    protected User createEntity() {
        return new User();
    }
}
