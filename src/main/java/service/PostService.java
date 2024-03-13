package service;

import dto.CommentDto;
import dto.PostDto;
import dto.UserDto;
import entity.Post;
import entity.User;
import repository.PostRepository;
import repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PostService {
    private final PostRepository postRep ;
    private final UserRepository userRep ;
    private final CommentService commentService ;

    public PostService() {
        this.postRep = new PostRepository();
        this.userRep = new UserRepository();
        this.commentService = new CommentService();
    }

    public PostService(PostRepository postRep, UserRepository userRep, CommentService commentService) {
        this.postRep = postRep;
        this.userRep = userRep;
        this.commentService = commentService;
    }

    public PostDto getPost(Integer id) {
        PostDto postDto = new PostDto(postRep.find(id));
        postDto.setComments(commentService.findByPost(postDto.getId()));
        return postDto;

    }

    public void creatPost(PostDto postDto) {
        Post post = new Post();
        post.setText(postDto.getText());
        User user = userRep.find(postDto.getUser().getId());
        post.setUser(user);
        postRep.create(post);
    }

    public void editePost(PostDto postDto) {
        if (postDto.getId()==null) return;
        Post post = postRep.find(postDto.getId());
        post.setText(postDto.getText());
        postRep.edit(post);
    }

    public void removePost(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        postRep.remove(post);
    }

    public List<PostDto> findByUser(Integer id){
        return postRep.findAllByUser(userRep.find(id)).stream().map(PostDto::new).collect(Collectors.toList());
    }

    public List<PostDto> getAllPost(){
        return postRep.findAll().stream().map(PostDto::new).collect(Collectors.toList());
    }
}
