package com.scc.sub.services;

import java.sql.Timestamp;
import java.util.List;

import com.scc.sub.model.Owner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scc.sub.model.Event;
import com.scc.sub.repository.OwnerRepository;

@Service
public class OwnerService extends AbstractGenericService<Owner> {

    private static final Logger logger = LoggerFactory.getLogger(OwnerService.class);

    @Autowired
    private OwnerRepository ownerRepository;
    
	@Override
	protected void loggerSink(Event<Owner> event) {

        logger.debug("Received a message of type {} ", event.getType());
        
        ObjectMapper mapper = new ObjectMapper();
        List<Owner> owners = mapper.convertValue(
        		event.getMessage(),
                new TypeReference<List<Owner>>() { });
        
        for (Owner owner : owners) {
	        switch(event.getAction()){
		        case "GET":
		            logger.debug("Received a {} event - owner id {}", event.getAction(), owner.getId());
		            break;
		        case "SAVE":
		        case "UPDATE":
		            logger.debug("Received a {} event - owner id {}", event.getAction(), owner.toString());
		            save(owner, event.getTimestamp());
		            break;
		        case "DELETE":
		            logger.debug("Received a {} event - owner id {}", event.getAction(), owner.getId());
		            deleteByIdDog(owner.getId());
		            break;
		        default:
		            logger.error("Received an UNKNOWN - owner id {}", owner.getId());
		            break;
	        }
        }
	}

	@Override
	protected void save(Owner message, long timestamp) {

		try {
			Owner owner = ownerRepository.findByIdDog(message.getIdDog());
			if (owner == null) {
				logger.debug("Dog id {} not found", message.getId());
				message
						.withTimestamp(new Timestamp(timestamp))
				;
				ownerRepository.save(message);
			} else {
				logger.debug("save dog id {}, {}, {}", owner.getId(), owner.getTimestamp().getTime(), timestamp);
				if (owner.getTimestamp().getTime() < timestamp) {
					logger.debug("check queue OK ; call saving changes ");
					owner
							.withId(message.getId())
							.withLastName(message.getLastName())
							.withFirstName(message.getFirstName())
							.withAddress(message.getAddress())
							.withZipCode(message.getZipCode())
							.withTown(message.getTown())
							.withCountry(message.getCountry())
							.withIdDog(message.getIdDog())
							.withTimestamp(new Timestamp(timestamp))
					;

					ownerRepository.save(owner);
				} else
					logger.debug("check queue KO : no changes saved");

			}
		} finally {

		}
		
	}

	@Override
	protected void deleteById(long id) {		
	}

	@Override
	protected void deleteByIdDog(int idDog) {

    	try {
    		ownerRepository.deleteByIdDog(idDog);
    	} finally {
    		
    	}
		
	}
}
