package dto;

import annotation.MyManyToOne;
import annotation.TableColomn;
import entity.Comment;
import entity.Post;
import entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import repository.PostRepository;
import repository.UserRepository;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDto {
    private Integer id;
    private String text;
    private PostDto post;
    private UserDto user;

    public CommentDto(Comment comment){
        this.id = comment.getId();
        this.text = comment.getText();
        this.post = new PostDto(comment.getPost());
        this.user = new UserDto(comment.getUser());
    }
}
