package modelos;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Copias")

public class Copia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "estado", length = 80)
    private String estado;

    @Column(name = "soporte", length = 80)
    private String soporte;


    @ManyToOne
    @JoinColumn(name = "id_pelicula")
    private Pelicula pelicula;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;



    public Copia() {
    }


    public Copia(String estado, String soporte) {
        this.estado = estado;
        this.soporte = soporte;
    }

    public void añadirPelicula(Pelicula pelicula) {
        pelicula.getCopias().add(this);
        this.setPelicula(pelicula);
    }
    public void añadirUsuario(Usuario usuario) {
        usuario.getCopias().add(this);
        this.setUsuario(usuario);
    }

    @Override
    public String toString() {
        return "Copia{" +
                "id=" + id +
                ", estado='" + estado + '\'' +
                ", soporte='" + soporte + '\'' +
                ", pelicula=" + pelicula.getTitulo() +
                ", usuario=" + usuario.getUsuario()+
                '}';
    }
}
