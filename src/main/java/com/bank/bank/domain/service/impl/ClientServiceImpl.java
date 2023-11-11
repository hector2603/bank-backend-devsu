package com.bank.bank.domain.service.impl;

import com.bank.bank.application.exception.NotFoundException;
import com.bank.bank.domain.mapper.Mapper;
import com.bank.bank.domain.model.Client;
import com.bank.bank.domain.service.ClientService;
import com.bank.bank.infrastructure.dto.request.CreateClientRequest;
import com.bank.bank.infrastructure.dto.response.ClientResponse;
import com.bank.bank.infrastructure.repository.ClientRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public ClientResponse createClient(CreateClientRequest request) {
        return Optional.of(request)
                .map(client -> updateClientDetails(new Client(), client))
                .map(clientRepository::save)
                .map(client -> {
                    client.setAccounts(List.of());
                    return client;
                })
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request"));
    }

    @Override
    public List<ClientResponse> getAllClients() {
        return ((List<Client>) clientRepository.findAll()).stream()
                .map(mapper::mapToResponse)
                .toList();
    }

    @Override
    public ClientResponse updateClient(Long id, CreateClientRequest request) {
        return clientRepository.findById(id)
                .map(client -> updateClientDetails(client, request))
                .map(clientRepository::save)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Client with id " + id + " not found"));
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.findById(id)
                .map(this::validateClientHasNoAccounts)
                .orElseThrow(() -> new NotFoundException("Client with id " + id + " not found"));
        clientRepository.deleteById(id);
    }

    @Override
    public ClientResponse getClient(Long id) {
        return clientRepository.findById(id)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Client with id " + id + " not found"));
    }


    private Client updateClientDetails(Client client, CreateClientRequest request) {
        client.setName(request.getName());
        client.setGender(request.getGender());
        client.setAge(request.getAge());
        client.setIdentification(request.getIdentification());
        client.setAddress(request.getAddress());
        client.setPhone(request.getPhone());
        client.setPassword(request.getPassword());
        client.setStatus(request.getStatus());
        return client;
    }

    private Client validateClientHasNoAccounts(Client client) {
        if (!ObjectUtils.isEmpty(client.getAccounts())) {
            throw new IllegalArgumentException("Client with id " + client.getClientId() + " has accounts");
        }else{
            return client;
        }
    }
}
