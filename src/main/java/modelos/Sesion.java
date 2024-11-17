package modelos;

import lombok.Data;

@Data
public class Sesion {

    public static  String usuariosesion;
    public static String contrasenasesion;

    public static String getUsuariosesion() {
        return usuariosesion;
    }

    public static void setUsuariosesion(String usuariosesion) {
        Sesion.usuariosesion = usuariosesion;
    }

    public static String getContrasenasesion() {
        return contrasenasesion;
    }

    public static void setContrasenasesion(String contrasenasesion) {
        Sesion.contrasenasesion = contrasenasesion;
    }


}


