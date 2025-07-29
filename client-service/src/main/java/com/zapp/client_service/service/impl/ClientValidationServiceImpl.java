package com.zapp.client_service.service.impl;

import com.zapp.client_service.dto.CreateClientRequestDto;
import com.zapp.client_service.dto.UpdateClientRequestDto;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.enums.ClientStatus;
import com.zapp.client_service.exception.ClientValidationException;
import com.zapp.client_service.service.IClientValidationService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientValidationServiceImpl implements IClientValidationService {


    /**
     * Validates business rules for creating a new client.
     */
    @Override
    public void validateCreateClientRequest(CreateClientRequestDto dto) {

        log.debug("Validating client creation: {}", dto.email());

        List<String> errors = new ArrayList<>();

        // Example: Ensure client cannot be created with inactive status
        // (Usually, new clients are PENDING_APPROVAL, but you can modify as needed)
        // (If CreateClientDto contains status, uncomment below)
        // if (dto.status() != null && dto.status() != ClientStatus.PENDING_APPROVAL) {
        //     errors.add("New clients must be in PENDING_APPROVAL status");
        // }

        // Business validation: client email must be unique
        // (This is handled by @UniqueConstraint, but you can add explicit DB check here if needed)

        // Business validation: employee count must be at least 1 if present, not null
        if (dto.employeeCount() != null && dto.employeeCount() < 1) {
            errors.add("Employee count must be at least 1");
        }

        // Additional business logic can be added here
        // E.g., validate industry, country, company name format, etc.

        // Raise exception if any business rule is violated
        if (!errors.isEmpty()) {
            log.warn("Client creation validation failed: {}", errors);
            throw new ClientValidationException("Client validation failed", errors);
        }
        log.info("Client creation validation passed: {}", dto.email());
    }

    /**
     * Validates business rules for updating an existing client.
     */
    @Override
    public void validateUpdateClientRequest(UUID clientId, UpdateClientRequestDto dto) {
        log.debug("Validating update for client: {}", clientId);
        List<String> errors = new ArrayList<>();

        // Business validation: employee count must be at least 1 if present, not null
        if (dto.employeeCount() != null && dto.employeeCount() < 1) {
            errors.add("Employee count must be at least 1");
        }

        // Additional business logic can be added here
        // E.g., validate industry, country, company name format, etc.

        // Raise exception if any business rule is violated
        if (!errors.isEmpty()) {
            log.warn("Client update validation failed for client {}: {}", clientId, errors);
            throw new ClientValidationException("Client update validation failed", errors);
        }
        log.info("Client update validation passed: {}", clientId);
    }

    /**
     * Validates business rules for client status transition.
     * E.g., "Suspended" clients cannot directly become "Active"; only "Pending Approval" can become "Active".
     */
    @Override
    public void validateStatusTransition(Client client, ClientStatus newStatus) {
        log.debug("Validating client status transition from {} to {} for client {}",
                client.getStatus(), newStatus, client.getId());

        ClientStatus currentStatus = client.getStatus();
        boolean isValidTransition = false;

        // Define your business rules for valid status transitions here
        if (currentStatus == ClientStatus.PENDING_APPROVAL && newStatus == ClientStatus.ACTIVE) {
            isValidTransition = true;
        } else if (
                (currentStatus == ClientStatus.ACTIVE && newStatus == ClientStatus.INACTIVE) ||
                        (currentStatus == ClientStatus.ACTIVE && newStatus == ClientStatus.SUSPENDED) ||
                        (currentStatus == ClientStatus.INACTIVE && newStatus == ClientStatus.ACTIVE) ||
                        (currentStatus == ClientStatus.SUSPENDED && newStatus == ClientStatus.ACTIVE)
        ) {
            isValidTransition = true;
        }
        // Add more valid transition rules as needed

        if (!isValidTransition) {
            String message = String.format("Invalid status transition from %s to %s", currentStatus, newStatus);
            log.warn("Status transition validation failed for client {}: {}", client.getId(), message);
            throw new ClientValidationException("Invalid status transition", List.of(message));
        }
        log.info("Status transition validation passed for client {} to {}", client.getId(), newStatus);
    }

    /**
     * Validates business rules for client deletion.
     * E.g., active clients with jobs/candidates cannot be deleted.
     */
    @Override
    public void validateClientDeletion(Client client) {
        log.debug("Validating deletion for client: {}", client.getId());
        List<String> errors = new ArrayList<>();

        // Business rule: cannot delete client that is ACTIVE (or add your own logic)
        if (client.getStatus() == ClientStatus.ACTIVE) {
            errors.add("Active clients cannot be deleted");
        }

        // Business rule: cannot delete client with jobs (would need reference to job-service, so consider this as a placeholder)
        // if (jobService.countJobsByClientId(client.getId()) > 0) {
        //     errors.add("Client with associated jobs cannot be deleted");
        // }

        // Add your own business rules as needed

        // Raise exception if any business rule is violated
        if (!errors.isEmpty()) {
            log.warn("Client deletion validation failed for client {}: {}", client.getId(), errors);
            throw new ClientValidationException("Client deletion validation failed", errors);
        }
        log.info("Client deletion validation passed: {}", client.getId());
    }
}
