package sistema.login.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import sistema.login.servicios.DisponibilidadService;
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initServicios(DisponibilidadService servicioService) {
        return args -> {
            servicioService.obtenerServiciosDisponibles();
        };
    }
}
