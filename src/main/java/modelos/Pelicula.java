package modelos;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Peliculas")
public class Pelicula implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "genero")
    private String genero;

    @Column(name = "año")
    private Integer año;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "director")
    private String director;



    @OneToMany(mappedBy = "pelicula", fetch=FetchType.EAGER)
    private List<Copia> copias = new ArrayList<>();

    public Pelicula() {
    }

    public void añadirCopia(Copia copias) {
        copias.setPelicula(this);
        this.copias.add(copias);
    }

    public Pelicula(String titulo, String genero, Integer año, String descripcion, String director) {
        this.titulo = titulo;
        this.genero = genero;
        this.año = año;
        this.descripcion = descripcion;
        this.director = director;
    }

    /*public void añadirOpinion(Opinion opinion) {
        opinion.setPelicula(this);
        this.opinions.add(opinion);
    }*/

    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", opinions=" + copias +
                '}';
    }

}
