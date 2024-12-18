package dao;

import Utilidades.Sesion;
import com.example.aplicacion.HibernateUtil;
import modelos.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class CopiaDao implements DAO<Copia> {
    private SessionFactory sessionFactory;

    public CopiaDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public CopiaDao() {
    }

    public List<Copia> findAll() {
        return null;
    }

    @Override
    public Copia findById(Long id) {
        return null;
    }

    public void save(Copia copia) {
        try (Session session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.persist(copia);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Copia copia) {


        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.merge(copia);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Copia> obtenerCopiasPorUsuario(Usuario usuario) {
        Session session = HibernateUtil.sessionFactory.openSession();
        List<Copia> copias = null;

        try {
            session.beginTransaction();
            Query<Copia> query = session.createQuery("SELECT c FROM Copia c JOIN FETCH c.pelicula p WHERE c.usuario.id = :usuarioId", Copia.class);
            query.setParameter("usuarioId", usuario.getId());
            copias = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return copias;
    }



   public void delete(Copia selectedCopia) {
    try (var session = sessionFactory.openSession()) {
        var transaction = session.beginTransaction();
        session.createQuery("delete from Copia where id = :copiaId")
                .setParameter("copiaId", selectedCopia.getId())
                .executeUpdate();
        transaction.commit();
    } catch (Exception e) {
        System.out.println("Error al eliminar Copia");
    }
}

    public String obtenerRutaAvatar() {
        int usuarioId = obtenerIdUsuarioPorCredenciales();
        String rutaAvatar = null;
        try (Session session = HibernateUtil.sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Usuario> query = session.createQuery("FROM Usuario u WHERE u.id = :id", Usuario.class);
            query.setParameter("id", usuarioId);
            List<Usuario> usuarios = query.list();
            if (!usuarios.isEmpty()) {
                rutaAvatar = usuarios.get(0).getAvatar();
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rutaAvatar;
    }


    public int obtenerIdUsuarioPorCredenciales() {
        int usuarioId = -1;
        try (Session session = HibernateUtil.sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Integer> query = session.createQuery("SELECT u.id FROM Usuario u WHERE u.usuario = :username AND u.contraseña = :password", Integer.class);
            query.setParameter("username", Sesion.getUsuariosesion());
            query.setParameter("password", Sesion.getContrasenasesion());
            usuarioId = query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarioId;
    }

    public void eliminarCopiasPorPelicula(int peliculaId) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "DELETE FROM Copia c WHERE c.pelicula.id = :peliculaId";
            Query query = session.createQuery(hql);
            query.setParameter("peliculaId", peliculaId);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
