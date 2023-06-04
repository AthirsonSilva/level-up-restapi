package com.api.nextspring.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.nextspring.entity.EmailEntity;

public interface EmailRepository extends JpaRepository<EmailEntity, UUID> {

}
