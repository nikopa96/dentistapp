package com.cgi.dentistapp.service;

import com.cgi.dentistapp.entity.ClientEntity;
import com.cgi.dentistapp.repository.ClientRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    @NonNull
    private ClientRepository clientRepository;

    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    public ClientEntity addClient(ClientEntity client) {
        return clientRepository.save(client);
    }

    public void updateClient(Long clientId, ClientEntity newClient) {
        Optional<ClientEntity> requestedClient = clientRepository.findById(clientId);

        if (requestedClient.isPresent()) {
            ClientEntity client = requestedClient.get();

            client.setFirstName(newClient.getFirstName());
            client.setLastName(newClient.getLastName());
            client.setTelephone(newClient.getTelephone());
            client.setEmail(newClient.getEmail());
            client.setConnectionType(newClient.getConnectionType());

            clientRepository.save(client);
        }
    }
}
