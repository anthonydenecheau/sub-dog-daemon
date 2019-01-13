package com.scc.sub.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scc.sub.model.Event;

public abstract class AbstractGenericService<T> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractGenericService.class);
   
	public AbstractGenericService() {
    	super();
	}
	
	protected abstract void loggerSink(Event<T> event);
	
	protected abstract void save( T message, long timestamp);
	
	protected abstract void deleteById(long id);

	protected abstract void deleteByIdDog(int idDog);

}
