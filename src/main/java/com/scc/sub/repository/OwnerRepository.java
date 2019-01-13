package com.scc.sub.repository;

import com.scc.sub.model.Owner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OwnerRepository extends CrudRepository<Owner,String>  {
	
    public Owner findByIdDog(int idDog);
    
    @Transactional
    public void deleteByIdDog(int idDog);
    
}