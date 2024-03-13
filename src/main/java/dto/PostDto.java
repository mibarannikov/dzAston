package dto;

import entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDto {

    private Integer id;
    private String text;
    private UserDto user;
    private List<CommentDto> comments;

    public PostDto(Post post){
        this.id = post.getId();
        this.text = post.getText();
        this.user = new UserDto(post.getUser());
    }

    public PostDto(Integer id, String text) {
        this.id = id;
        this.text = text;
    }
}
