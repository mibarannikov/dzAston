package dto;

import annotation.TableColomn;
import entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import repository.CommentRepository;
import repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private static final PostRepository postRep = new PostRepository();
    private static final CommentRepository commRep = new CommentRepository();

    private Integer id;
    private String username;
    private String name;
    private List<CommentDto> comments;
    private List<PostDto> posts;

    public UserDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
    }

    public UserDto(Integer id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }
}
