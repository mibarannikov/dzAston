package service;

import dto.UserDto;
import entity.User;
import repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private final UserRepository userRep;
    private final CommentService commentService;
    private final PostService postService;

    public UserService() {
        this.userRep = new UserRepository();
        this.commentService = new CommentService();
        this.postService = new PostService();
    }

    public UserService(UserRepository userRep, CommentService commentService, PostService postService) {
        this.userRep = userRep;
        this.commentService = commentService;
        this.postService = postService;
    }

    public UserDto getUser(Integer id) {
        UserDto userDto = new UserDto(userRep.find(id));
        userDto.setPosts(postService.findByUser(userDto.getId()));
        userDto.setComments(commentService.findByUser(userDto.getId()));
        return userDto;
    }

    public void creatUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        userRep.create(user);
    }

    public void editeUser(UserDto userDto) {
        User user = userRep.find(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        userRep.edit(user);
    }

    public void removeUser(UserDto userDto) {
        if (userDto.getId() == null) throw  new RuntimeException("в Dto нет id");
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        userRep.remove(user);
    }

    public List<UserDto> getAllUser() {
        return userRep.findAll().stream().map(UserDto::new).collect(Collectors.toList());
    }


}
