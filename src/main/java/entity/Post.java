package entity;

import annotation.BdTable;
import annotation.MyManyToOne;
import annotation.TableColomn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import repository.UserRepository;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@BdTable(name ="post")
public class Post {
    @TableColomn(name ="id")
    private Integer id;
    @TableColomn(name ="text")
    private String text;
    @TableColomn(name ="user")
    @MyManyToOne(repository = UserRepository.class)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(getId(), post.getId()) && Objects.equals(getText(), post.getText()) && Objects.equals(getUser(), post.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getUser());
    }

    @Override
    public String toString(){
        return String.valueOf(id);
    }
}
