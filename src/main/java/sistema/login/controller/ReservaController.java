package sistema.login.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sistema.login.entidades.DisponibilidadServicio;
import sistema.login.entidades.Reservas;
import sistema.login.entidades.Usuario;
import sistema.login.repositorios.ReservaRepository;
import sistema.login.servicios.DisponibilidadService;
import sistema.login.servicios.UserService;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DisponibilidadService disponibilidadService;

    @GetMapping
    public String listReservas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = userService.findByCedula(auth.getName());
        
        List<Reservas> reservas = reservaRepository.findAll();
        
        model.addAttribute("reservas", reservas);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAdmin", currentUser.getRol() == Usuario.Role.ADMIN);
        return "reservas";
    }

    @GetMapping("/nueva")
    public String nuevaReservaForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = userService.findByCedula(auth.getName());
        
        // Obtener solo servicios disponibles
        List<DisponibilidadServicio> serviciosDisponibles = disponibilidadService.obtenerServiciosDisponibles();
        
        if (serviciosDisponibles.isEmpty()) {
            model.addAttribute("error", "No hay servicios disponibles en este momento");
        }
        
        Reservas reserva = new Reservas();
        reserva.setCedulaUsuario(currentUser.getCedula());
        
        model.addAttribute("reserva", reserva);
        model.addAttribute("serviciosDisponibles", serviciosDisponibles);
        return "reserva-form";
    }

    @PostMapping
    public String crearReserva(@ModelAttribute Reservas reserva, Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario currentUser = userService.findByCedula(auth.getName());
            
            // Verificar disponibilidad antes de crear la reserva
            if (!disponibilidadService.verificarDisponibilidad(reserva.getServicio())) {
                model.addAttribute("error", "El servicio " + reserva.getServicio() + " no tiene disponibilidad");
                model.addAttribute("serviciosDisponibles", disponibilidadService.obtenerServiciosDisponibles());
                return "reserva-form";
            }
            
            reserva.setCedulaUsuario(currentUser.getCedula());
            reserva.setUser(currentUser);
            
            // Guardar reserva y actualizar disponibilidad
            reservaRepository.save(reserva);
            disponibilidadService.incrementarReserva(reserva.getServicio());
            
            return "redirect:/reservas";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear la reserva: " + e.getMessage());
            model.addAttribute("serviciosDisponibles", disponibilidadService.obtenerServiciosDisponibles());
            return "reserva-form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarReservaForm(@PathVariable Long id, Model model) {
        Reservas reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = userService.findByCedula(auth.getName());
        
        // Verificar permisos
        if (currentUser.getRol() != Usuario.Role.ADMIN && 
            !reserva.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No tienes permisos para editar esta reserva");
        }
        
        // Obtener servicios disponibles + el servicio actual (por si quieren cambiarlo)
        List<DisponibilidadServicio> serviciosDisponibles = disponibilidadService.obtenerServiciosDisponibles();
        
        model.addAttribute("reserva", reserva);
        model.addAttribute("serviciosDisponibles", serviciosDisponibles);
        model.addAttribute("servicioActual", reserva.getServicio());
        return "reserva-form";
    }

    @PostMapping("/editar/{id}")
    public String actualizarReserva(@PathVariable Long id, @ModelAttribute Reservas reserva, Model model) {
        try {
            Reservas reservaExistente = reservaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario currentUser = userService.findByCedula(auth.getName());
            
            // Verificar permisos
            if (currentUser.getRol() != Usuario.Role.ADMIN && 
                !reservaExistente.getUser().getId().equals(currentUser.getId())) {
                throw new RuntimeException("No tienes permisos para editar esta reserva");
            }
            
            String servicioAnterior = reservaExistente.getServicio();
            String servicioNuevo = reserva.getServicio();
            
            // Si cambió el servicio, verificar disponibilidad del nuevo
            if (!servicioAnterior.equals(servicioNuevo)) {
                if (!disponibilidadService.verificarDisponibilidad(servicioNuevo)) {
                    model.addAttribute("error", "El servicio " + servicioNuevo + " no tiene disponibilidad");
                    model.addAttribute("serviciosDisponibles", disponibilidadService.obtenerServiciosDisponibles());
                    model.addAttribute("servicioActual", servicioAnterior);
                    return "reserva-form";
                }
                
                // Actualizar disponibilidades
                disponibilidadService.decrementarReserva(servicioAnterior);
                disponibilidadService.incrementarReserva(servicioNuevo);
            }
            
            reservaExistente.setServicio(reserva.getServicio());
            reservaExistente.setFechaHora(reserva.getFechaHora());
            
            reservaRepository.save(reservaExistente);
            return "redirect:/reservas";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar la reserva: " + e.getMessage());
            model.addAttribute("serviciosDisponibles", disponibilidadService.obtenerServiciosDisponibles());
            return "reserva-form";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        Reservas reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = userService.findByCedula(auth.getName());
        
        // Verificar permisos
        if (currentUser.getRol() != Usuario.Role.ADMIN && 
            !reserva.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No tienes permisos para eliminar esta reserva");
        }
        
        // Decrementar disponibilidad al eliminar
        disponibilidadService.decrementarReserva(reserva.getServicio());
        reservaRepository.delete(reserva);
        
        return "redirect:/reservas";
    }
}