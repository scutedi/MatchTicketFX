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

    public Iterable<Bilet> findAllOneByName(String name){
        return repositoryBilet.findAllOneByName(name);
    }

    public int getNumarBilete(String numeClient, String numeMeci) {
        int count = 0;

        for (Bilet bilet : repositoryBilet.findAll()) {
            if (bilet.getClient_id().getNume().equals(numeClient) &&
                    bilet.getMeci_id().getNume_meci().equals(numeMeci)) {
                count++;
            }
        }

        return count;
    }


    public void saveBilete(Meci meci, Client client){
        repositoryBilet.save(new Bilet(meci, client));
    }

    public void saveUser(User user){
        repositoryUser.save(user);
    }

    public void updateMeci(Meci meci){
        repositoryMeci.update(meci);
    }

    public Iterable<Meci> findAllMeci()
    {
        return repositoryMeci.findAll();
    }
}
