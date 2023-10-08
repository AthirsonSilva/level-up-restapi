package com.api.nextspring.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.nextspring.entity.AddressEntity;

public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {
}
