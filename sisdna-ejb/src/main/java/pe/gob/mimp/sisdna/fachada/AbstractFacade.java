package pe.gob.mimp.sisdna.fachada;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import pe.gob.mimp.general.util.ParametroNodo;
import pe.gob.mimp.general.util.ParametroNodoObject;

/**
 * Clase: AbstractFacade.java<br>
 * Clase Abstracta que implementa los métodos CRUD.<br>
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    /**
     * Guarda el registro en la tabla
     * @param entity entidad
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Actualiza la información del registro en la tabla
     * @param entity entidad
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

     /**
      * Remueve el registro de la tabla
      * @param entity entidad
      */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Devuelve la lista completa de registros
     * @return lista de registros
     */
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<T> findAllByField(Object field, Object value) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        Root<T> entitie = criteriaQuery.from(entityClass);

        criteriaQuery.select(entitie).where(criteriaBuilder.equal(entitie.get(field.toString()), value));

        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    public List<T> findAllByFieldOrder(Object field, Object value, boolean order, String orderFiled, boolean todos) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Class<T>> registro = cq.from(entityClass);
        if (order) {
            cq.orderBy(cb.asc(registro.get(orderFiled)));
        } else {
            cq.orderBy(cb.desc(registro.get(orderFiled)));
        }
        if (todos) {
            cq.where(
                    cb.and(
                            cb.equal(registro.get(field.toString()), value)
                    ));
        } else {
            cq.where(
                    cb.and(
                            cb.equal(registro.get(field.toString()), value),
                            cb.equal(registro.get("flgActivo"), BigInteger.ONE)
                    ));
        }
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }

    public List<T> obtenerPorAproximacion(Object field, Object value) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        EntityType<T> type = getEntityManager().getMetamodel().entity(entityClass);
        Root<T> entitie = criteriaQuery.from(entityClass);

        criteriaQuery.select(entitie).where(
                criteriaBuilder.or(
                        criteriaBuilder.like(entitie.get(type.getDeclaredSingularAttribute(field.toString(), String.class)), "%" + value + "%")
                ),
                criteriaBuilder.and(
                        criteriaBuilder.equal(entitie.get(type.getDeclaredSingularAttribute(field.toString(), BigInteger.class)), BigInteger.ONE)
                )
        );

        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    public List<T> obtenerPorParametros(Object parametrosRecibidos) {
        javax.persistence.criteria.CriteriaQuery<T> criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        ArrayList<ArrayList<Object>> parametros = ((ParametroNodo) parametrosRecibidos).getParametros();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        List<Predicate> predicados = new ArrayList<Predicate>();
        Root<T> entitie = criteriaQuery.from(entityClass);

        for (int i = 0; i < parametros.size(); i++) {
            predicados.add(criteriaBuilder.equal(entitie.get(parametros.get(i).get(0).toString()), parametros.get(i).get(1)));
        }

        criteriaQuery.select(entitie).where(predicados.toArray(new Predicate[]{}));

        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }
    public T findPorParametros(Object parametrosRecibidos) {
        javax.persistence.criteria.CriteriaQuery<T> criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        ArrayList<ArrayList<Object>> parametros = ((ParametroNodo) parametrosRecibidos).getParametros();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        List<Predicate> predicados = new ArrayList<Predicate>();
        Root<T> entitie = criteriaQuery.from(entityClass);

        for (int i = 0; i < parametros.size(); i++) {
            predicados.add(criteriaBuilder.equal(entitie.get(parametros.get(i).get(0).toString()), parametros.get(i).get(1)));
        }

        criteriaQuery.select(entitie).where(predicados.toArray(new Predicate[]{}));

        return getEntityManager().createQuery(criteriaQuery).getSingleResult();
    }
    
    public List<T> obtenerPorParametrosObject(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos) {
        javax.persistence.criteria.CriteriaQuery<T> criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        ArrayList<ArrayList<Object>> parametros = ((ParametroNodoObject) parametrosRecibidos).getParametros();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        List<Predicate> predicados = new ArrayList<Predicate>();
        Root<T> entitie = criteriaQuery.from(entityClass);

        for (int i = 0; i < parametros.size(); i++) {
            predicados.add(criteriaBuilder.equal(entitie.get(parametros.get(i).get(0).toString()), parametros.get(i).get(1)));
        }

        if (!todos) {
            predicados.add(criteriaBuilder.equal(entitie.get("flgActivo"), BigInteger.ONE));
        }

        criteriaQuery.select(entitie).where(predicados.toArray(new Predicate[]{}));

        if (order) {
            criteriaQuery.orderBy(criteriaBuilder.asc(entitie.get(orderFiled)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(entitie.get(orderFiled)));
        }

        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    public List<T> obtenerPorAproximacionOrder(Object field, Object value, boolean order, String orderFiled, boolean todos) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        EntityType<T> type = getEntityManager().getMetamodel().entity(entityClass);
        Root<T> entitie = criteriaQuery.from(entityClass);
        if (!todos) {
            criteriaQuery.select(entitie).where(
                    criteriaBuilder.and(
                            criteriaBuilder.like(entitie.get(type.getDeclaredSingularAttribute(field.toString(), String.class)), "%" + value + "%"),
                            criteriaBuilder.equal(entitie.get("flgActivo"), BigInteger.ONE)
                    )
            );
        } else {
            criteriaQuery.select(entitie).where(
                    criteriaBuilder.and(
                            criteriaBuilder.like(entitie.get(type.getDeclaredSingularAttribute(field.toString(), String.class)), "%" + value + "%")
                    )
            );
        }
        if (order) {
            criteriaQuery.orderBy(criteriaBuilder.asc(entitie.get(orderFiled)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(entitie.get(orderFiled)));
        }

        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    public List<T> obtenerActivos(boolean order, String orderFiled) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Class<T>> registro = cq.from(entityClass);

        if (order) {
            cq.orderBy(cb.asc(registro.get(orderFiled)));
        } else {
            cq.orderBy(cb.desc(registro.get(orderFiled)));
        }

        cq.where(
                cb.and(
                        cb.equal(registro.get("flgActivo"), BigInteger.ONE)
                ));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }

    public List<T> obtenerTodos(boolean order, String orderFiled, boolean todos) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Class<T>> registro = cq.from(entityClass);

        if (order) {
            cq.orderBy(cb.asc(registro.get(orderFiled)));
        } else {
            cq.orderBy(cb.desc(registro.get(orderFiled)));
        }
        if (!todos) {
            cq.where(
                    cb.and(
                            cb.equal(registro.get("flgActivo"), BigInteger.ONE)
                    ));
        }

        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }

    public T obtenerObejto(Object field, Object value, boolean todos) {
        T clase = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Class<T>> registro = cq.from(entityClass);
        if (todos) {
            cq.where(
                    cb.and(
                            cb.equal(registro.get(field.toString()), value)
                    ));
        } else {
            cq.where(
                    cb.and(
                            cb.equal(registro.get("flgActivo"), BigInteger.ONE),
                            cb.equal(registro.get(field.toString()), value)
                    ));
        }
        try {
            javax.persistence.Query q = getEntityManager().createQuery(cq);
            clase = (T) q.getSingleResult();
        } catch (PersistenceException e) {
        }
        return clase;
    }

     public List<T> obtenerPorFiltro(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos) {
        javax.persistence.criteria.CriteriaQuery<T> criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        ArrayList<ArrayList<Object>> parametros = ((ParamFiltro) parametrosRecibidos).getParametros();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        List<Predicate> predicados = new ArrayList<Predicate>();
        EntityType<T> type = getEntityManager().getMetamodel().entity(entityClass);
        Root<T> entitie = criteriaQuery.from(entityClass);

        
        for (int i = 0; i < parametros.size(); i++) {
            if(parametros.get(i).get(2).equals(ParamFiltro.EQUAL))
               predicados.add(criteriaBuilder.equal(entitie.get(parametros.get(i).get(0).toString()), parametros.get(i).get(1)));
       
            if(parametros.get(i).get(2).equals(ParamFiltro.NOTEQUAL)) 
               predicados.add(criteriaBuilder.notEqual(entitie.get(parametros.get(i).get(0).toString()), parametros.get(i).get(1)));
            
            if(parametros.get(i).get(2).equals(ParamFiltro.LIKE)) 
               predicados.add(criteriaBuilder.like(entitie.get(type.getDeclaredSingularAttribute(parametros.get(i).get(0).toString(), String.class)), "%" + parametros.get(i).get(1) + "%"));
            
        }

        if (!todos) {
            predicados.add(criteriaBuilder.equal(entitie.get("flgActivo"), BigInteger.ONE));
        }

        criteriaQuery.select(entitie).where(predicados.toArray(new Predicate[]{}));

        if (order) {
            criteriaQuery.orderBy(criteriaBuilder.asc(entitie.get(orderFiled)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(entitie.get(orderFiled)));
        }

        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }
}
