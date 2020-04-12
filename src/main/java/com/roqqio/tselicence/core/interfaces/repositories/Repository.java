package com.roqqio.tselicence.core.interfaces.repositories;

import com.roqqio.tselicence.core.interfaces.entities.IModified;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class Repository<T extends IModified> {
    private final EntityManager entityManager;
    private final Class<T> clazz;

    public Repository(EntityManager entityManager, Class<T> clazz) {
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    protected List<T> filterItems(T item, int limit) {
        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root).where(getPredicates(item, criteriaBuilder, root).toArray(new Predicate[]{}))
                .orderBy(criteriaBuilder.asc(root.get("modified")));
        Query<T> query = session.createQuery(criteriaQuery);
        return query.setMaxResults(limit).getResultList();
    }

    protected abstract List<Predicate> getPredicates(T item, CriteriaBuilder criteriaBuilder, Root<T> root);

    protected String contains(String value) {
        return "%" + value + "%";
    }

    protected boolean isAdd(int value) {
        return value > 0;
    }

    protected boolean isAdd(long value) {
        return value > 0;
    }

    protected boolean isAdd(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
