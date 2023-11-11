package com.bank.bank;

import com.bank.bank.domain.mapper.Mapper;
import com.bank.bank.domain.model.Client;
import com.bank.bank.domain.service.impl.ClientServiceImpl;
import com.bank.bank.infrastructure.dto.request.CreateClientRequest;
import com.bank.bank.infrastructure.dto.response.ClientResponse;
import com.bank.bank.infrastructure.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void testCreateClient() {
        // Arrange
        CreateClientRequest request = new CreateClientRequest();
        Client client = new Client();
        ClientResponse response = new ClientResponse();

        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(mapper.mapToResponse(any(Client.class))).thenReturn(response);

        // Act
        ClientResponse result = clientService.createClient(request);

        // Assert
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(mapper, times(1)).mapToResponse(client);
        Assertions.assertEquals(response, result);

    }

    @Test
    public void testGetAllClients() {
        // Arrange
        Client client = new Client();
        ClientResponse response = new ClientResponse();

        when(clientRepository.findAll()).thenReturn(Collections.singletonList(client));
        when(mapper.mapToResponse(any(Client.class))).thenReturn(response);

        // Act
        List<ClientResponse> result = clientService.getAllClients();

        // Assert
        verify(clientRepository, times(1)).findAll();
        verify(mapper, times(1)).mapToResponse(client);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void testUpdateClient() {
        // Arrange
        Long id = 1L;
        CreateClientRequest request = new CreateClientRequest();
        Client client = new Client();
        ClientResponse response = new ClientResponse();

        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(mapper.mapToResponse(any(Client.class))).thenReturn(response);

        // Act
        ClientResponse result = clientService.updateClient(id, request);

        // Assert
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).save(client);
        verify(mapper, times(1)).mapToResponse(client);
        Assertions.assertEquals(response, result);
    }

    @Test
    public void testDeleteClient() {
        // Arrange
        Long id = 1L;
        Client client = new Client();

        when(clientRepository.findById(any())).thenReturn(Optional.of(client));

        // Act
        clientService.deleteClient(id);

        // Assert
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetClient() {
        // Arrange
        Long id = 1L;
        Client client = new Client();
        ClientResponse response = new ClientResponse();

        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(mapper.mapToResponse(any(Client.class))).thenReturn(response);

        // Act
        ClientResponse result = clientService.getClient(id);

        // Assert
        verify(clientRepository, times(1)).findById(id);
        verify(mapper, times(1)).mapToResponse(client);
        Assertions.assertEquals(response, result);
    }

}
