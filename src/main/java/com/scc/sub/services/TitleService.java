package com.scc.sub.services;

import java.sql.Timestamp;
import java.util.List;

import com.scc.sub.repository.TitleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scc.sub.model.Event;
import com.scc.sub.model.Title;

@Service
public class TitleService extends AbstractGenericService<Title> {

    private static final Logger logger = LoggerFactory.getLogger(TitleService.class);

    @Autowired
    private TitleRepository titleRepository;
    
	@Override
	protected void loggerSink(Event<Title> event) {

        logger.debug("Received a message of type {} ", event.getType());
        
        ObjectMapper mapper = new ObjectMapper();
        List<Title> titles = mapper.convertValue(
        		event.getMessage(),
                new TypeReference<List<Title>>() { });
        
        for (Title title : titles) {
	        switch(event.getAction()){
		        case "GET":
		            logger.debug("Received a {} event - title id {}", event.getAction(), title.getId());
		            break;
		        case "SAVE":
		        case "UPDATE":
		            logger.debug("Received a {} event - title id {}", event.getAction(), title.toString());
		            save(title, event.getTimestamp());
		            break;
		        case "DELETE":
		            logger.debug("Received a {} event - title id {}", event.getAction(), title.getId());
		            deleteById(title.getId());
		            break;
		        default:
		            logger.error("Received an UNKNOWN event - title id {}", title.getId());
		            break;
	        }
        }

	}

	@Override
	protected void save(Title message, long timestamp) {

		try {
			Title title = titleRepository.findById(message.getId());
			if (title == null) {
				logger.debug("Title id {} not found", message.getId());
				message
						.withTimestamp(new Timestamp(timestamp))
				;
				titleRepository.save(message);
			} else {
				logger.debug("save dog id {}, {}, {}", title.getId(), title.getTimestamp().getTime(), timestamp);
				if (title.getTimestamp().getTime() < timestamp) {
					logger.debug("check queue OK ; call saving changes ");
					title
							.withId(message.getId())
							.withIdDog(message.getIdDog())
							.withIdTitle(message.getIdTitle())
							.withTitle(message.getTitle())
							.withName(message.getName())
							.withType(message.getType())
							.withCountry(message.getCountry())
							.withObtentionDate(message.getObtentionDate())
							.withTimestamp(new Timestamp(timestamp))
					;

					titleRepository.save(title);
				} else
					logger.debug("check queue KO : no changes saved");

			}
		} finally {

		}
		
	}

	@Override
	protected void deleteById(long id) {

		try {
    		titleRepository.deleteById(id);
    	} finally {
    		
    	}

	}

	@Override
	protected void deleteByIdDog(int idDog) {
	}
    
}
