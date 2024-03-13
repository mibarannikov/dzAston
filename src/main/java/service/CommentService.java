package service;

import dto.CommentDto;
import dto.PostDto;
import entity.Comment;
import entity.Post;
import entity.User;
import repository.CommentRepository;
import repository.PostRepository;
import repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CommentService {
    private  final PostRepository postRep ;
    private  final UserRepository userRep ;
    private  final CommentRepository commentRep;

    public CommentService() {
        this.postRep = new PostRepository();
        this.userRep = new UserRepository();
        this.commentRep = new CommentRepository();
    }

    public CommentService(PostRepository postRep, UserRepository userRep, CommentRepository commentRep) {
        this.postRep = postRep;
        this.userRep = userRep;
        this.commentRep = commentRep;
    }


    public CommentDto getComment(Integer id) {
        return new CommentDto(commentRep.find(id));
    }

    public void creatComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        User user = userRep.find(commentDto.getUser().getId());
        comment.setUser(user);
        Post post = postRep.find(commentDto.getPost().getId());
        commentRep.create(comment);
    }

    public void editeComment(CommentDto commentDto) {
        if (commentDto.getId()==null) return;// todo throw
        Comment comment = commentRep.find(commentDto.getId());
        comment.setText(commentDto.getText());
        commentRep.edit(comment);
    }

    public void removeComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        commentRep.remove(comment);
    }

    public List<CommentDto> findByUser(Integer id){
        return commentRep.findAllByUser(userRep.find(id)).stream().map(CommentDto::new).collect(Collectors.toList());
    }
    public List<CommentDto> findByPost(Integer id){
        return commentRep.findAllByPost(postRep.find(id)).stream().map(CommentDto::new).collect(Collectors.toList());
    }

}
