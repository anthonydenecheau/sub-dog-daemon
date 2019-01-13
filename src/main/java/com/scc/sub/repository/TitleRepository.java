package com.scc.sub.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.scc.sub.model.Title;

@Repository
public interface TitleRepository extends CrudRepository<Title,String>  {
	
	public Title findById(long id);
	
    @Transactional
    public void deleteById(long id);
    
}