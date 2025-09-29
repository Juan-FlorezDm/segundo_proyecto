package sistema.login.repositorios;

import sistema.login.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCedula(String cedula);
    Optional<Usuario> findByEmail(String email);
    boolean existsByCedula(String cedula);
    boolean existsByEmail(String email);
}