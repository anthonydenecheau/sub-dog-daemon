package com.scc.sub.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scc.sub.model.Breeder;
import com.scc.sub.model.Dog;
import com.scc.sub.model.Event;
import com.scc.sub.model.Owner;
import com.scc.sub.model.Parent;
import com.scc.sub.model.Pedigree;
import com.scc.sub.model.Title;
import com.scc.sub.utils.TypeEvent;

@Service
public class SinkService {

	private static final Logger logger = LoggerFactory.getLogger(SinkService.class);
	
	@Autowired
	TitleService titleService;

	@Autowired
	PedigreeService pedigreeService;
	
	@Autowired
	ParentService parentService;

	@Autowired
	OwnerService ownerService;

	@Autowired
	BreederService breederService;

	@Autowired
	DogService dogService;
	
	@SuppressWarnings("unchecked")
	public <T> void handleMessage(Event<T> event) {
		
		logger.info("Receive : {} ", event.toString());
		TypeEvent type = TypeEvent.valueOf(event.getType());
		
		switch (type) {
			case Breeder:
				breederService.loggerSink((Event<Breeder>) event);
				break;
			case Dog:
				dogService.loggerSink((Event<Dog>) event);
	            break;
			case Owner: 
				ownerService.loggerSink((Event<Owner>) event);
	            break;
			case Parent: 
				parentService.loggerSink((Event<Parent>) event);
	            break;
			case Pedigree:
				pedigreeService.loggerSink((Event<Pedigree>) event);
	            break;
			case Title:
				titleService.loggerSink((Event<Title>) event);
	            break;
	        default:
	            throw new IllegalArgumentException("Event message not recognized !");
		}
	
	}

}
