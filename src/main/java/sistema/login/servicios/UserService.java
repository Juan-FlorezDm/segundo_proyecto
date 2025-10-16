package sistema.login.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sistema.login.entidades.Usuario;
import sistema.login.repositorios.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Usuario registerUser(Usuario user) {
        if (userRepository.existsByCedula(user.getCedula())) {
            throw new RuntimeException("La cédula ya está registrada");
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public Usuario findByCedula(String cedula) {
        return userRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void crearAdmin() {
    Usuario admin = new Usuario(
        "123456789",
        "admin2@empresa.com",
        passwordEncoder.encode("Admin123*"),
        "Administrador",
        Usuario.Role.ADMIN
    );
    userRepository.save(admin);
}

}