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

    // Checks if a client exists with exact name (case-sensitive)
    boolean existsByName(String name);

    // Checks if a client exists with exact email (case-sensitive)
    boolean existsByEmail(String email);

    // Checks if a client exists with exact phone number (case-sensitive)
    boolean existsByPhoneNumber(String phoneNumber);

    // Finds a client by name ignoring case, returns Optional
    @Query("SELECT c FROM Client c WHERE LOWER(c.name) = LOWER(:name)")
    Optional<Client> findByNameIgnoreCase(@Param("name") String name);

    // Checks if a client with given name exists excluding a specific client id (for update uniqueness)
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") UUID id);

    // Checks if email exists ignoring case, for validation purposes
    boolean existsByEmailIgnoreCase(String email);

    // Fetch clients by status – if you expect large list, consider pagination for scalability
    List<Client> findByStatus(ClientStatus status);

    // Dynamic filtering by optional criteria with pagination support
    @Query("""
        SELECT c FROM Client c
        WHERE (:status IS NULL OR c.status = :status)
          AND (:clientType IS NULL OR c.clientType = :clientType)
          AND (:industry IS NULL OR LOWER(c.industry) LIKE LOWER(CONCAT('%', :industry, '%')))
          AND (:country IS NULL OR LOWER(c.country) LIKE LOWER(CONCAT('%', :country, '%')))
          AND (:companyName IS NULL OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :companyName, '%')))
        """)
    Page<Client> findClientsByFilters(
            @Param("status") ClientStatus status,
            @Param("clientType") ClientType clientType,
            @Param("industry") String industry,
            @Param("country") String country,
            @Param("companyName") String companyName,
            Pageable pageable);

}
