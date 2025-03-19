package ro.mpp2025.Repository;

import ro.mpp2025.Domain.Client;
import ro.mpp2025.Domain.Meci;

import java.util.Optional;

public interface IRepositoryClient extends IRepository<Integer, Client>{

    Optional<Client> findOneClientByNumeAndAdresa(String nume, String adresa);
}
