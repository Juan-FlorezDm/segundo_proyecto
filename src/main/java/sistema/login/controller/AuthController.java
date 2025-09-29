package sistema.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import sistema.login.DTO.RegisterDTO;
import sistema.login.entidades.Usuario;
import sistema.login.entidades.Usuario.Role;
import sistema.login.servicios.UserService;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;

    // ✅ NUEVO: Controlador para la página principal
    @GetMapping("/")
    public String home() {
        return "redirect:/login"; // Redirige automáticamente al login
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDTO registerDTO, Model model) {
        try {
            System.out.println("📝 Intentando registrar: " + registerDTO.getCedula());
            
            // Validar contraseñas
            if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
                model.addAttribute("error", "Las contraseñas no coinciden");
                return "register";
            }

            // Validar campos obligatorios
            if (registerDTO.getNombre() == null || registerDTO.getNombre().trim().isEmpty()) {
                model.addAttribute("error", "El nombre es obligatorio");
                return "register";
            }

            if (registerDTO.getEmail() == null || registerDTO.getEmail().trim().isEmpty()) {
                model.addAttribute("error", "El email es obligatorio");
                return "register";
            }

            // Crear usuario
            Usuario user = new Usuario();
            user.setCedula(registerDTO.getCedula());
            user.setNombre(registerDTO.getNombre());
            user.setEmail(registerDTO.getEmail());
            user.setPassword(registerDTO.getPassword());
            user.setRol(Role.USER);

            userService.registerUser(user);
            
            System.out.println("✅ Usuario registrado: " + registerDTO.getCedula());
            model.addAttribute("success", "Registro exitoso. Ahora puedes iniciar sesión.");
            return "login";
            
        } catch (Exception e) {
            System.out.println("❌ Error en registro: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}