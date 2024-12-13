package com.app.service;

import com.app.persistence.entity.UserEntity;
import com.app.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /*
        * El valor "UserEntity userEntity" tenemos que convertirlo a un "UserDetails" para que spring security lo maneje
        * Buscar el usuario en la base de datos
        * */
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario "+username+" no existe."));

        /*
        * Spring maneja los roles/permisos con "SimpleGrantedAuthority-PermisosConcedidos"
        * Mapear los roles del usuario a SimpleGrantedAuthority
        * */
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        /*
        * Obtenemos los roles para agregalos al "SimpleGrantedAuthority"
        * Con "authorityList.add" agregamos un objeto "SimpleGrantedAuthority", pero
        * agregar los roles spring security reconoce los roles con el prefio "ROLE_" luego
        * concatenariamos "concat(role.getRoleEnum().name())" el rol que tenemos "role" luego obtenemos el ENUM
        * para finalizar el nombre con ".name()"
        *
        * Asi tomamos los roles de la entidad que tenemos o tengamos y lo convertimos en un "SimpleGrantedAuthority"
        * */
        userEntity.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        /*
        * Obtenemos los permisos
        * Con el "getRoles" recorremos cada rol tiene su listado de roles, para esto usaremos ".stream()"
        * */
        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList);
    }

}
