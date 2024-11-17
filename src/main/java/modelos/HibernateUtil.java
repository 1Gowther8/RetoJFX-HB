package modelos;

import lombok.Data;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Data

public class HibernateUtil {
    public static final SessionFactory sessionFactory;

    static{
        sessionFactory = new Configuration()
                .configure()
                .setProperty("hibernate.connection.password",System.getenv("MYSQL_ROOT_PASSWORD"))
                .setProperty("hibernate.connection.username",System.getenv("MYSQL_USER"))
                .buildSessionFactory();
    }




    public static void shutdown() {
        // Cerrar caché y conexión de la SessionFactory
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
