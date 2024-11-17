package dao;

import modelos.Pelicula;
import org.hibernate.SessionFactory;

import java.util.List;

public class PeliculaDao implements DAO<Pelicula> {
    private SessionFactory sessionFactory;

    public PeliculaDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }



    @Override
    public List<Pelicula> findAll() {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("from Pelicula", Pelicula.class).list();
        }
    }

    @Override
    public Pelicula findById(Long id) {
        return null;
    }

    @Override
    public void save(Pelicula pelicula) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.persist(pelicula);
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Error al guardar Pelicula");
        }

    }

    @Override
    public void update(Pelicula pelicula) {

    }

    @Override
    public void delete(Pelicula pelicula) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.remove(pelicula);
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Error al eliminar Pelicula");
        }
    }
}
