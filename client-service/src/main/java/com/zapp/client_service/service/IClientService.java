package com.zapp.client_service.service;

import com.zapp.client_service.dto.ClientDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClientService {
    /**
     * @param ClientDto - ClientDto object
     */
    void createClient(ClientDto dto);
    Page<ClientDto> getAllClients(Pageable pageable);

    /**
     * @param clientId - Input ClientId
     * @return Client Details based on given ClientId
     */
    ClientDto fetchClientById(Long clientId);

    /**
     * @param clientId - Input ClientId
     * @param clientDto - Input ClientDto Object
     * @return boolean indicating if the update of Client details is successful or not
     */
    boolean updateClient(Long clientId, ClientDto clientDto);

    /**
     * @param clientId - Input ClientId
     * @return boolean indicating if the delete of Client details is successful or not
     */
    boolean deleteClient(Long clientId);
}
