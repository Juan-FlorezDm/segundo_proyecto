package sistema.login.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservas")
public class Reservas {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "servicio", nullable = false) // ← CORREGIDO: nombre consistente
    private String servicio; // ← CORREGIDO: minúscula

    @Column(nullable = false)
    private String fechaHora;

    @Column(nullable = false)
    private String cedulaUsuario; // ← CAMBIADO: de Integer a String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Usuario user;

    public Reservas() {}
    
    public Reservas(Long id, String servicio, String fechaHora, String cedulaUsuario, Usuario user) {
        this.id = id;
        this.servicio = servicio; // ← CORREGIDO
        this.fechaHora = fechaHora;
        this.cedulaUsuario = cedulaUsuario; // ← CORREGIDO tipo
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServicio() {
        return servicio; // ← CORREGIDO
    }

    public void setServicio(String servicio) {
        this.servicio = servicio; // ← CORREGIDO
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getCedulaUsuario() { // ← CORREGIDO tipo de retorno
        return cedulaUsuario;
    }

    public void setCedulaUsuario(String cedulaUsuario) { // ← CORREGIDO tipo de parámetro
        this.cedulaUsuario = cedulaUsuario;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }
}