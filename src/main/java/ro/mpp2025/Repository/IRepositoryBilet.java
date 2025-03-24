package ro.mpp2025.Repository;

import ro.mpp2025.Domain.Bilet;

import java.util.Optional;

public interface IRepositoryBilet extends IRepository<Integer, Bilet> {

    Iterable<Bilet> findAllOneByName(String name);
}
