package com.zapp.client_service.service.impl;

import com.zapp.client_service.dto.ClientDto;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.exception.ClientAlreadyExistsException;
import com.zapp.client_service.exception.ResourceNotFoundException;
import com.zapp.client_service.mapper.ClientMapper;
import com.zapp.client_service.repository.ClientRepository;
import com.zapp.client_service.service.IClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;

    /**
     *  @param ClientDto - ClientDto object
     */
    @Override
    public void createClient(ClientDto clientDto) {
        if (clientRepository.existsByName(clientDto.getName())) {
            throw new ClientAlreadyExistsException("Client name already exists");
        }
        Client client = ClientMapper.mapToClient(clientDto,new Client());
        clientRepository.save(client);
    }

    @Override
    public Page<ClientDto> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(c -> new ClientDto(c.getName(), c.getLocation(), c.getStatus()));
    }

    /**
     * @param clientId - Input ClientId
     * @return Client Details based on given ClientId
     */
    @Override
    public ClientDto fetchClientById(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() ->
                new ResourceNotFoundException("Client", "ClientId",clientId+""));
        return ClientMapper.mapToClientDto(client,new ClientDto());
    }

    /**
     * @param clientId - Input ClientId
     * @param clientDto - Input ClientDto Object
     * @return boolean indicating if the update of Client details is successful or not
     */
    @Override
    public boolean updateClient(Long clientId, ClientDto clientDto) {
        boolean isUpdated = false;

        if(clientDto!=null && clientId!=null){
            Client client = clientRepository.findById(clientId).orElseThrow(() ->
                    new ResourceNotFoundException("Client", "ClientId", clientId + ""));
            ClientMapper.mapToClient(clientDto,client);
            clientRepository.save(client);
            isUpdated = true;
        }
        return isUpdated;
    }

    /**
     * @param clientId - Input ClientId
     * @return boolean indicating if the delete of Client details is successful or not
     */
    @Override
    public boolean deleteClient(Long clientId) {
        boolean isDeleted = false;
        if(clientId!=null) {
            if (!clientRepository.existsById(clientId)) {
                throw new ResourceNotFoundException("Client", "ClientId", clientId + "");
            }
            clientRepository.deleteById(clientId);
            isDeleted = true;
        }
        return isDeleted;
    }

}
