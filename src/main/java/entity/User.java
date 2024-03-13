package entity;

import annotation.BdTable;
import annotation.TableColomn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@BdTable(name ="user")
public class User {
    @TableColomn(name ="id")
    private Integer id;
    @TableColomn(name ="username")
    private String username;
    @TableColomn(name ="name")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId().equals(user.getId()) && getUsername().equals(user.getUsername()) && Objects.equals(getName(), user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getName());
    }

    @Override
    public String toString(){
        return String.valueOf(id);
    }
}
