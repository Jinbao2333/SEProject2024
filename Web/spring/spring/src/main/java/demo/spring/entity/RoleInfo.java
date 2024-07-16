package demo.spring.entity;

import java.io.Serializable;
import java.util.List;

public class RoleInfo implements Serializable {
    private static final long serialVersionUID = 3497935890426858541L;

    int role_id;
    String name;
    String comment;
    List<Permission> permissions;

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
