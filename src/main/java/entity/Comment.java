package entity;

import annotation.BdTable;
import annotation.MyManyToOne;
import annotation.TableColomn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import repository.PostRepository;
import repository.UserRepository;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@BdTable(name = "comment")
public class Comment {
    @TableColomn(name = "id")
    private Integer id;
    @TableColomn(name = "text")
    private String text;
    @TableColomn(name = "post")
    @MyManyToOne(repository = PostRepository.class)
    private Post post;
    @TableColomn(name = "user")
    @MyManyToOne(repository = UserRepository.class)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(getId(), comment.getId()) && Objects.equals(getText(), comment.getText()) && Objects.equals(getPost(), comment.getPost()) && Objects.equals(getUser(), comment.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getPost(), getUser());
    }

    @Override
    public String toString(){
        return String.valueOf(id);
    }
}
