package com.zapp.client_service.repository;

import com.zapp.client_service.entity.Client;
import com.zapp.client_service.enums.ClientStatus;
import com.zapp.client_service.enums.ClientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT c FROM Client c WHERE LOWER(c.name) = LOWER(:name)")
    Optional<Client> findByNameIgnoreCase(@Param("name") String name);

    boolean existsByNameAndClientIdNot(String name,Long clientId);

    @Query("SELECT c FROM Client c WHERE " +
            "(:status IS NULL OR c.status = :status) AND " +
            "(:clientType IS NULL OR c.clientType = :clientType) AND " +
            "(:industry IS NULL OR LOWER(c.industry) LIKE LOWER(CONCAT('%', :industry, '%'))) AND " +
            "(:country IS NULL OR LOWER(c.country) LIKE LOWER(CONCAT('%', :country, '%'))) AND " +
            "(:companyName IS NULL OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :companyName, '%')))")
    Page<Client> findClientsWithFilters(
            @Param("status") ClientStatus status,
            @Param("clientType") ClientType clientType,
            @Param("industry") String industry,
            @Param("country") String country,
            @Param("companyName") String companyName,
            Pageable pageable);

    List<Client> findByStatus(ClientStatus status);
}
