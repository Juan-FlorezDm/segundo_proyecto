package sistema.login.entidades;


import jakarta.persistence.*;

@Entity
@Table(name = "disponibilidad_servicios")
public class DisponibilidadServicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombreServicio;

    @Column(nullable = false)
    private Integer capacidadMaxima;

    @Column(nullable = false)
    private Integer reservasActuales;

    public DisponibilidadServicio() {}

    public DisponibilidadServicio(String nombreServicio, Integer capacidadMaxima) {
        this.nombreServicio = nombreServicio;
        this.capacidadMaxima = capacidadMaxima;
        this.reservasActuales = 0;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreServicio() { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }

    public Integer getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(Integer capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }

    public Integer getReservasActuales() { return reservasActuales; }
    public void setReservasActuales(Integer reservasActuales) { this.reservasActuales = reservasActuales; }

    public Boolean estaDisponible() {
        return reservasActuales < capacidadMaxima;
    }

    public void incrementarReservas() {
        this.reservasActuales++;
    }

    public void decrementarReservas() {
        if (this.reservasActuales > 0) {
            this.reservasActuales--;
        }
    }
}
