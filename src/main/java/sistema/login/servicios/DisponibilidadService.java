package sistema.login.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sistema.login.entidades.DisponibilidadServicio;
import sistema.login.repositorios.DisponibilidadRepository;
import java.util.List;

@Service
public class DisponibilidadService {

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    public List<DisponibilidadServicio> obtenerServiciosDisponibles() {
        return disponibilidadRepository.findAll().stream()
                .filter(DisponibilidadServicio::estaDisponible)
                .toList();
    }

    public boolean verificarDisponibilidad(String servicio) {
        return disponibilidadRepository.findByNombreServicio(servicio)
                .map(DisponibilidadServicio::estaDisponible)
                .orElse(false);
    }

    public void incrementarReserva(String servicio) {
        DisponibilidadServicio disponibilidad = disponibilidadRepository.findByNombreServicio(servicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + servicio));
        
        if (!disponibilidad.estaDisponible()) {
            throw new RuntimeException("No hay disponibilidad para el servicio: " + servicio);
        }
        
        disponibilidad.incrementarReservas();
        disponibilidadRepository.save(disponibilidad);
    }

    public void decrementarReserva(String servicio) {
        DisponibilidadServicio disponibilidad = disponibilidadRepository.findByNombreServicio(servicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + servicio));
        
        disponibilidad.decrementarReservas();
        disponibilidadRepository.save(disponibilidad);
    }

    // Método para inicializar servicios (ejecutar una vez)
    public void inicializarServicios() {
        if (disponibilidadRepository.count() == 0) {
            DisponibilidadServicio servicio1 = new DisponibilidadServicio("Piscina", 2);
            DisponibilidadServicio servicio2 = new DisponibilidadServicio("Gimnasio", 2);
            DisponibilidadServicio servicio3 = new DisponibilidadServicio("Cancha Deportiva", 2);
            DisponibilidadServicio servicio4 = new DisponibilidadServicio("Salón de Eventos", 2);
            
            disponibilidadRepository.saveAll(List.of(servicio1, servicio2, servicio3, servicio4));
        }
    }
}