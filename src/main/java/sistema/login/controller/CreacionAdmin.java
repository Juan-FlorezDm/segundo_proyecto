package sistema.login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sistema.login.servicios.UserService;

@RestController
@RequestMapping("/admin")
public class CreacionAdmin {

    private UserService userService;

    public CreacionAdmin(UserService userService) {
        this.userService = userService;
    }
    
    
    @GetMapping("/crear")
    public String crearAdmin() {
        userService.crearAdmin();   
       return "Admin creado exitosamente";
    }
}
