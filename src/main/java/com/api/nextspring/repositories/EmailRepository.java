package com.api.nextspring.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.nextspring.entity.EmailEntity;

/**
 * This interface represents the repository for EmailEntity objects. It extends
 * the JpaRepository interface
 * and inherits its methods for basic CRUD operations.
 * 
 * @see JpaRepository
 * @see EmailEntity
 * 
 * @implNote This interface represents the repository for EmailEntity objects.
 *           It extends the JpaRepository interface
 * 
 * @author Athirson Silva
 */
public interface EmailRepository extends JpaRepository<EmailEntity, UUID> {

}
