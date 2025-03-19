package ro.mpp2025.Service;

import ro.mpp2025.Repository.*;
import ro.mpp2025.Repository.DataBase.*;
import ro.mpp2025.Domain.*;

import java.util.Optional;

public class Service {

    private IRepositoryBilet repositoryBilet;
    private IRepositoryClient repositoryClient;
    private IRepositoryEchipa repositoryEchipa;
    private IRepositoryMeci repositoryMeci;
    private IRepositoryUser repositoryUser;

    public Service(IRepositoryBilet repositoryBilet, IRepositoryClient repositoryClient , IRepositoryEchipa repositoryEchipa , IRepositoryMeci repositoryMeci , IRepositoryUser repositoryUser) {
        this.repositoryBilet = repositoryBilet;
        this.repositoryClient = repositoryClient;
        this.repositoryEchipa = repositoryEchipa;
        this.repositoryMeci = repositoryMeci;
        this.repositoryUser = repositoryUser;
    }

    public Optional<User> findOneUserByEmail(String email)
    {
        return repositoryUser.findOneUserByEmail(email);
    }

    public Optional<Meci> findOneMeciByName(String name){
        return repositoryMeci.findOneMeciByNume(name);
    }

    public Optional<Client> findOneClientByNameAndClient(String name , String adresa){
        return repositoryClient.findOneClientByNumeAndAdresa(name , adresa);
    }

    public void saveBilete(Meci meci, Client client){
        repositoryBilet.save(new Bilet(meci, client));
    }

    public void updateMeci(Meci meci){
        repositoryMeci.update(meci);
    }

    public Iterable<Meci> findAllMeci()
    {
        return repositoryMeci.findAll();
    }
}
