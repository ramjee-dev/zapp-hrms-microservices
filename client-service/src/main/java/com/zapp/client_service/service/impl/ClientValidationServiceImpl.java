package com.zapp.client_service.service.impl;

import com.zapp.client_service.dto.CreateClientRequestDto;
import com.zapp.client_service.dto.PartialUpdateClientRequestDto;
import com.zapp.client_service.dto.UpdateClientRequestDto;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.enums.ClientStatus;
import com.zapp.client_service.exception.BusinessValidationException;
import com.zapp.client_service.exception.ResourceNotFoundException;
import com.zapp.client_service.repository.ClientRepository;
import com.zapp.client_service.service.IClientValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientValidationServiceImpl implements IClientValidationService {

    private final ClientRepository clientRepository;
    // Inject jobService or candidateService if needed for cross-entity validation,
    // e.g., private final JobService jobService;

    @Override
    public void validateCreateClientRequest(CreateClientRequestDto dto) {
        log.debug("Validating client creation: email={}", dto.email());

        List<String> errors = new ArrayList<>();

        // Trim and validate email uniqueness
        String email = dto.email() != null ? dto.email().trim() : null;
        if (email == null || email.isBlank()) {
            errors.add("Email is required.");
        } else if (clientRepository.existsByEmailIgnoreCase(email)) {
            errors.add("A client with this email already exists.");
        }

        // Validate employee count
        if (dto.employeeCount() != null && dto.employeeCount() < 1) {
            errors.add("Employee count must be at least 1.");
        }

        // Additional business validations
        // Example: validate companyName or industry format if necessary
        if (dto.companyName() == null || dto.companyName().isBlank()) {
            errors.add("Company name is required.");
        }

        // status validation if status is present in DTO
        // e.g. enforce new clients start with PENDING_APPROVAL or equivalent

        if (!errors.isEmpty()) {
            log.warn("Client creation validation failed: {}", errors);
            throw new BusinessValidationException("Client creation validation failed", errors);
        }

        log.info("Client creation validation passed for email: {}", email);
    }

    @Override
    public void validateUpdateClientRequest(UUID clientId, UpdateClientRequestDto dto) {
        log.debug("Validating update for client: {}", clientId);

        List<String> errors = new ArrayList<>();

        // Check the client exists (defensive)
        if (!clientRepository.existsById(clientId)) {
            log.warn("Client not found for update, id={}", clientId);
            throw new ResourceNotFoundException("Client", "id", clientId.toString());
        }

        // Trim and validate email uniqueness if email is modifiable in update DTO (adjust if applicable)
//        if (dto.email() != null) {
//            String email = dto.email().trim();
//            if (clientRepository.existsByEmailIgnoreCase(email)) {
//                // Make sure email does not belong to another client
//                var existing = clientRepository.findByEmailIgnoreCase(email);
//                if (existing.isPresent() && !existing.get().getId().equals(clientId)) {
//                    errors.add("Another client with this email already exists.");
//                }
//            }
//        }

        // Validate employee count
        if (dto.employeeCount() != null && dto.employeeCount() < 1) {
            errors.add("Employee count must be at least 1.");
        }

        // Add other necessary update validations here

        if (!errors.isEmpty()) {
            log.warn("Client update validation failed for client {}: {}", clientId, errors);
            throw new BusinessValidationException("Client update validation failed", errors);
        }

        log.info("Client update validation passed for client: {}", clientId);
    }

    /**
     * Optional: Adds validation support for PartialUpdateClientRequestDto
     */
    public void validatePartialUpdateClientRequest(UUID clientId, PartialUpdateClientRequestDto dto) {
        log.debug("Validating partial update for client: {}", clientId);

        List<String> errors = new ArrayList<>();

        if (!clientRepository.existsById(clientId)) {
            log.warn("Client not found for partial update: {}", clientId);
            throw new ResourceNotFoundException("Client", "id", clientId.toString());
        }

        // Example: email uniqueness if email is set in partial update
//        if (dto.email() != null) {
//            String email = dto.email().trim();
//            var existing = clientRepository.findByEmailIgnoreCase(email);
//            if (existing.isPresent() && !existing.get().getId().equals(clientId)) {
//                errors.add("Another client with this email already exists.");
//            }
//        }

        if (dto.employeeCount() != null && dto.employeeCount() < 1) {
            errors.add("Employee count must be at least 1.");
        }

        if (!errors.isEmpty()) {
            log.warn("Client partial update validation failed for client {}: {}", clientId, errors);
            throw new BusinessValidationException("Client partial update validation failed", errors);
        }

        log.info("Client partial update validation passed for client: {}", clientId);
    }

    @Override
    public void validateStatusTransition(Client client, ClientStatus newStatus) {
        log.debug("Validating client status transition from {} to {} for client {}",
                client.getStatus(), newStatus, client.getId());

        ClientStatus currentStatus = client.getStatus();

        Map<ClientStatus, Set<ClientStatus>> validTransitions = Map.of(
                ClientStatus.PENDING_APPROVAL, Set.of(ClientStatus.ACTIVE, ClientStatus.SUSPENDED),
                ClientStatus.ACTIVE, Set.of(ClientStatus.INACTIVE, ClientStatus.SUSPENDED),
                ClientStatus.INACTIVE, Set.of(ClientStatus.ACTIVE),
                ClientStatus.SUSPENDED, Set.of(ClientStatus.ACTIVE)
        );

        boolean isValidTransition = validTransitions.getOrDefault(currentStatus, Set.of()).contains(newStatus);

        if (!isValidTransition) {
            String message = String.format("Invalid client status transition from '%s' to '%s'", currentStatus, newStatus);
            log.warn("Status transition validation failed for client {}: {}", client.getId(), message);
            throw new BusinessValidationException("Invalid status transition", List.of(message));
        }

        log.info("Client status transition validation passed for client {} to status {}", client.getId(), newStatus);
    }

    @Override
    public void validateClientDeletion(Client client) {
        log.debug("Validating deletion for client: {}", client.getId());

        List<String> errors = new ArrayList<>();

        if (client.getStatus() == ClientStatus.ACTIVE) {
            errors.add("Cannot delete an active client.");
        }

        // Placeholder: check if client has associated jobs
        // long jobCount = jobService.countJobsByClientId(client.getId());
        // if (jobCount > 0) {
        //     errors.add("Cannot delete client with associated jobs.");
        // }

        if (!errors.isEmpty()) {
            log.warn("Client deletion validation failed for client {}: {}", client.getId(), errors);
            throw new BusinessValidationException("Client deletion validation failed", errors);
        }

        log.info("Client deletion validation passed for client: {}", client.getId());
    }
}

