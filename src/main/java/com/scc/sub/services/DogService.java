package com.scc.sub.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scc.sub.model.Dog;
import com.scc.sub.model.Event;
import com.scc.sub.repository.DogRepository;

@Service
public class DogService extends AbstractGenericService<Dog> {

	private static final Logger logger = LoggerFactory.getLogger(DogService.class);
	
	@Autowired
    private DogRepository dogRepository;
	
	@Override
	protected void loggerSink(Event<Dog> event) {

        logger.debug("Received a message of type {} ", event.getType());
        
        ObjectMapper mapper = new ObjectMapper();
        List<Dog> dogs = mapper.convertValue(
        		event.getMessage(),
                new TypeReference<List<Dog>>() { });
        
        for (Dog dog : dogs) {
	        switch(event.getAction()){
	            case "GET":
	                logger.debug("Received a {} event - dog id {}", event.getAction(), dog.getId());
	                break;
	            case "SAVE":
	            case "UPDATE":
	                logger.debug("Received a {} event - dog id {}", event.getAction(), dog.toString());
	                save(dog, event.getTimestamp());
	                break;
	            case "DELETE":
	                logger.debug("Received a {} event - dog id {}", event.getAction(), dog.getId());
	                deleteById(dog.getId());
	                break;
	            default:
	                logger.error("Received an UNKNOWN event - dog id {}", dog.getId());
	                break;
	        }
        }
		
	}

	@Override
	protected void save(Dog message, long timestamp) {

		try {
			Dog dog = dogRepository.findById(message.getId());
			if (dog == null) {
				logger.debug("Dog id {} not found", message.getId());
				message
						.withTimestamp(new Timestamp(timestamp))
				;
				dogRepository.save(message);
			} else {
				logger.debug("save dog id {}, {}, {}", dog.getId(), dog.getTimestamp().getTime(), timestamp);
				if (dog.getTimestamp().getTime() < timestamp) {
					logger.debug("check queue OK ; call saving changes ");
					dog
							.withNom(message.getNom())
							.withAffixe(message.getAffixe())
							.withSexe(message.getSexe())
							.withDateNaissance(message.getDateNaissance())
							.withPays(message.getPays())
							.withTatouage(message.getTatouage())
							.withTranspondeur(message.getTranspondeur())
							.withCodeFci(message.getCodeFci())
							.withIdRace(message.getIdRace())
							.withIdVariete(message.getIdVariete())
							.withRace(message.getRace())
							.withVariete(message.getVariete())
							.withCouleur(message.getCouleur())
							.withCouleurAbr(message.getCouleurAbr())
							.withInscriptionCode(message.getInscriptionCode())
							.withIdEtalon(message.getIdEtalon())
							.withIdLice(message.getIdLice())
							.withTimestamp(new Timestamp(timestamp))
					;

					dogRepository.save(dog);
				} else
					logger.debug("check queue KO : no changes saved");

			}
		} finally {

		}
		
	}

	@Override
	protected void deleteById(long id) {
    	try {
    		dogRepository.deleteById((int)id);
    	} finally {
    		
    	}
    }

	@Override
	protected void deleteByIdDog(int idDog) {
	}

}
