package sistema.login.models;

import sistema.login.entidades.Usuario;
import sistema.login.repositorios.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {
    System.out.println("🎯 SPRING SECURITY ESTÁ BUSCANDO USUARIO CON: " + cedula);
    System.out.println("📝 Parámetro recibido: " + cedula);
    
    Usuario user = userRepository.findByCedula(cedula)
            .orElseThrow(() -> {
                System.out.println("❌ USUARIO NO ENCONTRADO CON CÉDULA: " + cedula);
                // También prueba buscar por email por si acaso
                userRepository.findByEmail(cedula).ifPresent(u -> {
                    System.out.println("💡 PERO SÍ EXISTE CON EMAIL: " + cedula);
                });
                return new UsernameNotFoundException("Usuario no encontrado: " + cedula);
            });

    System.out.println("✅ USUARIO ENCONTRADO:");
    System.out.println("   Cédula: " + user.getCedula());
    System.out.println("   Email: " + user.getEmail());
    System.out.println("   Rol: " + user.getRol());
    
    return new org.springframework.security.core.userdetails.User(
            user.getCedula(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRol().name()))
    );
}
}