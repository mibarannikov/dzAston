package repository;

import entity.Comment;
import entity.Post;
import entity.User;

import java.util.ArrayList;
import java.util.List;

public class CommentRepository extends AbstractRepository<Comment>{


    public CommentRepository() {
        super(Comment.class);
    }

    @Override
    protected Comment createEntity() {
        return new Comment();
    }

    public List<Comment> findAllByUser(User user){
        return findAllBy(user);
    }
    public List<Comment> findAllByPost(Post post){
        return findAllBy(post);
    }
}
