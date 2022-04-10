package amigosecurity.example.jwt.model;

import lombok.Data;

@Data
public class AddRoleToUserForm {
    private String Username;
    private String roleName;
}
