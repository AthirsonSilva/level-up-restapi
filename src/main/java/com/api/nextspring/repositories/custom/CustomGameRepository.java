package com.api.nextspring.repositories.custom;

import com.api.nextspring.entity.GameEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a custom repository for GameEntity objects.
 * It provides methods for detaching an entity from the persistence context,
 * finding all games, and finding all games by filter.
 *
 * @author Athirson Silva
 * @implNote This class represents a custom repository for GameEntity objects.
 * @see EntityManager
 * @see GameEntity
 */
@Repository
@RequiredArgsConstructor
public class CustomGameRepository {

    private final EntityManager manager;

    /**
     * Detaches the given entity from the persistence context so that it can be used
     * in another context.
     *
     * @param entity the entity to detach
     */
    public void detach(Object entity) {
        manager.detach(entity);
    }

    /**
     * Finds all games and returns them as a page.
     *
     * @param pageable the pagination information
     * @return a page of GameEntity objects
     */
    public Page<GameEntity> findAll(Pageable pageable) {
        // Create the criteria builder, query and root objects to build the query
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);

        // Create the typed query and count query
        Root<GameEntity> root = query.from(GameEntity.class);
        query.where(root.get("id").isNotNull());

        // Create the typed query
        TypedQuery<GameEntity> typedQuery = manager.createQuery(query);

        // Create the page and return it
        Page<GameEntity> result = new PageImpl<>(typedQuery.getResultList(), pageable, pageable.getPageSize());

        return result;
    }

    /**
     * Finds games that match the given filter and returns them as a page.
     *
     * @param filter   the filter to apply to the search
     * @param pageable the pagination information
     * @return a page of GameEntity objects that match the filter
     */
    public Page<GameEntity> findAllByFilter(String filter, Pageable pageable) {
        // Create the criteria builder, query and root objects to build the query
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<GameEntity> query = builder.createQuery(GameEntity.class);
        Root<GameEntity> root = query.from(GameEntity.class);

        // Create a list of predicates to apply to the query
        String pattern = "%" + filter + "%";
        List<Predicate> predicates = new ArrayList<Predicate>();

        // Add the predicates to the list
        predicates.add(builder.like(root.get("name"), pattern));
        predicates.add(builder.like(root.get("description"), pattern));
        predicates.add(builder.like(root.get("grade"), pattern));
        predicates.add(builder.like(root.get("genre").get("name"), pattern));
        predicates.add(builder.like(root.get("genre").get("description"), pattern));

        // Apply the predicates to the query
        query.where(builder.or(predicates.toArray(new Predicate[predicates.size()])));

        // Create the typed query and count query
        TypedQuery<GameEntity> typedQuery = manager.createQuery(query);

        // Create the page and return it
        Page<GameEntity> result = new PageImpl<>(typedQuery.getResultList(), pageable, pageable.getPageSize());

        return result;
    }

}
