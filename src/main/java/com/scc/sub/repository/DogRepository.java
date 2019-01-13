package com.scc.sub.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.scc.sub.model.Dog;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DogRepository extends CrudRepository<Dog,String>  {
	
    public Dog findById(int id);
    
    @Transactional
    public void deleteById(int id);
    
}