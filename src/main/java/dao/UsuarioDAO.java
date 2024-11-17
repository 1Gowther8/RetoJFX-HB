package dao;

import modelos.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UsuarioDAO implements DAO<Usuario> {
    private SessionFactory sessionFactory;
    public UsuarioDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Usuario> findAll() {
        return null;
    }

    @Override
    public Usuario findById(Long id) {
        return null;
    }

    @Override
    public void save(Usuario usuario) {

    }

    @Override
    public void update(Usuario usuario) {

    }

    @Override
    public void delete(Usuario usuario) {

    }

    public boolean isAdmin(String username,String password) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Usuario WHERE usuario = :username AND contraseña = :password AND isAdmin = true";
            Usuario usuario = session.createQuery(hql, Usuario.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();
            return usuario != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validarUsuario(String username, String password) {
    try (Session session = sessionFactory.openSession()) {
        String hql = "FROM Usuario WHERE usuario = :usuario AND contraseña = :password";
        Usuario usuario = session.createQuery(hql, Usuario.class)
                .setParameter("usuario", username)
                .setParameter("password", password)
                .uniqueResult();
        return usuario != null;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }

}
}
