package ro.mpp2025.Repository;

import ro.mpp2025.Domain.User;

import java.util.Optional;

public interface IRepositoryUser extends IRepository<Integer , User>{
    Optional<User> findOneUserByEmail(String email);
}
