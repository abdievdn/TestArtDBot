package com.example.testartdbotapp.repository;

import com.example.testartdbotapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByChatId(Long chatId);


}
