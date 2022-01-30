package amigosecurity.example.jwt.controller;

import lombok.Data;

@Data
public class AddRoleToUserForm {
    private String Username;
    private String roleName;
}
