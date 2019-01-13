package com.scc.sub.services;

import java.sql.Timestamp;
import java.util.List;

import com.scc.sub.model.Parent;
import com.scc.sub.repository.ParentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scc.sub.model.Event;

@Service
public class ParentService extends AbstractGenericService<Parent> {

    private static final Logger logger = LoggerFactory.getLogger(ParentService.class);

    @Autowired
    private ParentRepository parentRepository;
    
	@Override
	protected void loggerSink(Event<Parent> event) {

        logger.debug("Received a message of type {} ", event.getType());
        
        ObjectMapper mapper = new ObjectMapper();
        List<Parent> parents = mapper.convertValue(
        		event.getMessage(),
                new TypeReference<List<Parent>>() { });
        
        for (Parent parent : parents) {
	        switch(event.getAction()){
		        case "GET":
		            logger.debug("Received a {} event - parent id {}", event.getAction(), parent.getId());
		            break;
		        case "SAVE":
		        case "UPDATE":
		            logger.debug("Received a {} event - parent id {}", event.getAction(), parent.toString());
		            save(parent, event.getTimestamp());
		            break;
		        case "DELETE":
		            logger.debug("Received a {} event - parent id {}", event.getAction(), parent.getId());
		            deleteById(parent.getId());
		            break;
		        default:
		            logger.error("Received an UNKNOWN event - parent id {}", parent.getId());
		            break;
	        }
        }
	
	}

	@Override
	protected void save(Parent message, long timestamp) {

		try {
			Parent parent = parentRepository.findById(message.getId());
			if (parent == null) {
				logger.debug("Dog id {} not found", message.getId());
				message
						.withTimestamp(new Timestamp(timestamp))
				;
				parentRepository.save(message);
			} else {
				logger.debug("save dog id {}, {}, {}", parent.getId(), parent.getTimestamp().getTime(), timestamp);
				if (parent.getTimestamp().getTime() < timestamp) {
					logger.debug("check queue OK ; call saving changes ");
					parent
							.withId(message.getId())
							.withName(message.getName())
							.withAffixe(message.getAffixe())
							.withOnSuffixe(message.getOnSuffixe())
							.withTimestamp(new Timestamp(timestamp))
					;

					parentRepository.save(parent);
				} else
					logger.debug("check queue KO : no changes saved");

			}
		} finally {

		}

	}

	@Override
	protected void deleteByIdDog(int idDog) {
	}

	@Override
	protected void deleteById(long id) {
    	try {
    		parentRepository.deleteById((int)id);
    	} finally {
    		
    	}
	}
}
