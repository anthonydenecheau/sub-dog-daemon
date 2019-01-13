package com.scc.sub.repository;

import com.scc.sub.model.Breeder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreederRepository extends CrudRepository<Breeder,String>  {
	
    public Breeder findByIdDog(int idDog);
    
    @Transactional
    public void deleteByIdDog(int idDog);
    
}

 