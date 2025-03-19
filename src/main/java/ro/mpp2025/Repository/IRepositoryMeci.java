package ro.mpp2025.Repository;

import ro.mpp2025.Domain.Meci;

import java.util.Optional;

public interface IRepositoryMeci extends IRepository<Integer, Meci> {

    Optional<Meci> findOneMeciByNume(String nume);
}
