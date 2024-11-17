package modelos;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Usuarios")

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "contraseña")
    private String contraseña;

    @Column(name = "isAdmin")
    private Boolean isAdmin;

    @Column(name = "avatar")
    private String avatar;   //Esto lo puse para mostrar su foto de avatar segun el usuario pero al final no lo consegui


    public void añadirCopia(Copia copias) {
        copias.setUsuario(this);
        this.copias.add(copias);
    }

    @OneToMany(mappedBy = "usuario", fetch=FetchType.EAGER)
    private List<Copia> copias = new ArrayList<>();
}
