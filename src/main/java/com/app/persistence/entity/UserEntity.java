package com.app.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity {

    /*
    * ENTIDAD que usaremos para registonar usuarios
    * */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    /*Indica si la cuenta está habilitada o no*/
    @Column(name = "is_enabled")
    private boolean isEnabled;

    /*Verifica si la cuenta ha expirado*/
    @Column(name = "account_No_Expired")
    private boolean accountNoExpired;

    /*Indica si la cuenta está bloqueada.*/
    @Column(name = "account_No_Locked")
    private boolean accountNoLocked;

    /*Verifica si las credenciales han expirado.*/
    @Column(name = "credential_No_Expired")
    private boolean credentialNoExpired;

    // RELACION DE MUCHOS A MUCHOS
    // El "Set" no permite valores repetidos la "List" si
    // Con "fetch = FetchType.EAGER" cargamos toda la info de esta variable
    // Si guardamos un "UserEntity" en la tabla guardamos todos los roles que tenemos con "cascade = CascadeType.ALL"
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();
}
