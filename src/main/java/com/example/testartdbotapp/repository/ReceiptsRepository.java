package com.example.testartdbotapp.repository;


import com.example.testartdbotapp.model.Receipt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptsRepository extends CrudRepository<Receipt, Long> {
}
