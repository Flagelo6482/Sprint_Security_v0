package com.app.config;

import com.app.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity      //Para habilitar la seguridad Web
@EnableMethodSecurity   //Nos permite realizar configuraciones con notaciones, para no configurar muchas cosas manualmente
public class SecurityConfig {

    /*
    * INYECCIÓN DE DEPENDENCIAS PARA EL COMPONENTE 2
    * Usamos la inyección de dependencias para el "AuthenticationManager"
    * //@Autowired
    * //AuthenticationConfiguration authenticationConfiguration;
    * */



    /*
    * COMPONENTE 1
    * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) //Preguntar a chatgpt, util en algunos casos en especial cuando tenemos formularios pero previene ataques cuando un usuario se logea
                .httpBasic(Customizer.withDefaults()) //Esta propiedad se usa cuando nos vamos a logear con un usuario y contaseña
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  //Al trabajar con app web lo recomendable es trabajar con una sesión "sin estado/STATELESS"
                //Configuramos los endpoints publicos y privados
                .authorizeHttpRequests(http -> {
                    /*
                    * 1.Si realizamos una petición GET al endpoint "/app/hola" permite a todo el mundo ingresar
                    * 2.Si realizamos una petición GET al endpoint "/app/holaseguridad" tenemos que tener la autorización "READ"
                    * 3.Cualquier otra solicutd que no especifiquemos como "/hola" o "/holaseguridad", etc, no le permitiremos el acceso
                    * */
                    http.requestMatchers(HttpMethod.GET, "/app/hola").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/app/holaSeguridad").hasAuthority("CREATE");
                    http.anyRequest().denyAll();
                })
                .build();
    }

    /*
     * COMPONENTE 2
     * Componente que ADMINISTRA LA AUTENTICACIÓN -> AuthenticationManager authenticationManager
     * Podemos agregarlo al parametro sin usar @Autowired -> AuthenticationConfiguration authenticationConfiguration
     * */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    /*
     * COMPONENTE 3 - PROVIDER
     * El DaoAuthenticationProvider encargado del proceso de autenticación, con los componentes que delega la responsabilidad que son
     * 3.UserDetailsService y 3.2PasswordEncoder para cargar los datos y verificar credenciales, luego todo esto se pasa al DaoAuthenticationProvider
     * */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder());      // 3.1 Método que establece el componente para cifrar contraseñas
        provider.setUserDetailsService(userDetailService);   // 3.2 Método que configura el servicio de cargar los datos del usuario de una fuente como una base de datos

        return provider;
    }

    /*
    * 3.1 PasswordEncoder
    * NoOpPasswordEncoder: No codifica, no hace nada, se usa solo para pruebas! OJO
    * */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    /*
    * 3.2 UserDetailsService
    * Por ahora guardaremos usuarios en memoria no los obtendremos de una base de datos
    * Al traer un usiario con UserDetailsService lo tenemos que convertir en UserDetails
    * */

}
