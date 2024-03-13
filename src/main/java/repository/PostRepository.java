package repository;

import entity.Post;
import entity.User;

import java.util.ArrayList;
import java.util.List;

public class PostRepository extends AbstractRepository<Post> {

    public PostRepository() {
        super(Post.class);
    }

    @Override
    protected Post createEntity() {
        return new Post();
    }

    public List<Post> findAllByUser(User user){
        return findAllBy(user);
    }
}
