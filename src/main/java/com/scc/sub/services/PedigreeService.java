package com.scc.sub.services;

import java.sql.Timestamp;
import java.util.List;

import com.scc.sub.repository.PedigreeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scc.sub.model.Event;
import com.scc.sub.model.Pedigree;

@Service
public class PedigreeService extends AbstractGenericService<Pedigree> {

    private static final Logger logger = LoggerFactory.getLogger(PedigreeService.class);

    @Autowired
    private PedigreeRepository pedigreeRepository;
    
	@Override
	protected void loggerSink(Event<Pedigree> event) {

        logger.debug("Received a message of type {} ", event.getType());
        
        ObjectMapper mapper = new ObjectMapper();
        List<Pedigree> pedigrees = mapper.convertValue(
        		event.getMessage(),
                new TypeReference<List<Pedigree>>() { });
        
        for (Pedigree pedigree : pedigrees) {
	        switch(event.getAction()){
		        case "GET":
		            logger.debug("Received a {} event - pedigree id {}", event.getAction(), pedigree.getId());
		            break;
		        case "SAVE":
		        case "UPDATE":
		            logger.debug("Received a {} event - pedigree id {}", event.getAction(), pedigree.toString());
		            save(pedigree, event.getTimestamp());
		            break;
		        case "DELETE":
		            logger.debug("Received a {} event - pedigree id {}", event.getAction(), pedigree.getId());
		            deleteById(pedigree.getId());
		            break;
		        default:
		            logger.error("Received an UNKNOWN event - pedigree id {}", pedigree.getId());
		            break;
	        }
        }
	}

	@Override
	protected void save(Pedigree message, long timestamp) {

		try {
			Pedigree pedigree = pedigreeRepository.findById(message.getId());
			if (pedigree == null) {
				logger.debug("Dog id {} not found", message.getId());
				message
						.withTimestamp(new Timestamp(timestamp))
				;
				pedigreeRepository.save(message);
			} else {
				logger.debug("save dog id {}, {}, {}", pedigree.getId(), pedigree.getTimestamp().getTime(), timestamp);
				if (pedigree.getTimestamp().getTime() < timestamp) {
					logger.debug("check queue OK ; call saving changes ");
					pedigree
							.withId(message.getId())
							.withIdDog(message.getIdDog())
							.withCountry(message.getCountry())
							.withType(message.getType())
							.withNumber(message.getNumber())
							.withObtentionDate(message.getObtentionDate())
							.withTimestamp(new Timestamp(timestamp))
					;

					pedigreeRepository.save(pedigree);
				} else
					logger.debug("check queue KO : no changes saved");

			}
		} finally {

		}
		
	}

	@Override
	protected void deleteById(long id) {

    	try {
    		pedigreeRepository.deleteById(id);
    	} finally {
    		
    	}
    	
	}

	@Override
	protected void deleteByIdDog(int idDog) {
	}  
    
}
