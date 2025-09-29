package sistema.login.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema.login.entidades.DisponibilidadServicio;
import java.util.List;
import java.util.Optional;

public interface DisponibilidadRepository extends JpaRepository<DisponibilidadServicio, Long> {
    Optional<DisponibilidadServicio> findByNombreServicio(String nombreServicio);
    List<DisponibilidadServicio> findByReservasActualesLessThan(Integer capacidadMaxima);
}