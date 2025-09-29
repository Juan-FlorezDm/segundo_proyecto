package sistema.login.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;

import sistema.login.entidades.Reservas;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reservas, Long> {
    List<Reservas> findByUserId(Long userId);
}
