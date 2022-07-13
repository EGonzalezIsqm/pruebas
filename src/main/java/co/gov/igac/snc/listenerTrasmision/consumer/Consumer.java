package co.gov.igac.snc.listenerTrasmision.consumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import co.gov.igac.snc.listenerTrasmision.dto.MensajeKafkaDTO;
import co.gov.igac.snc.listenerTrasmision.service.IConsumerService;

@Component
public class Consumer {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private IConsumerService service;

    @KafkaListener(topics = "TrasmisionArchivo", groupId = "group_id")
    public void listen(MensajeKafkaDTO mensaje,Acknowledgment acknowledgment) {
    	try {
        	acknowledgment.acknowledge();
        	log.info("TrasmisionArchivo: " + mensaje);
        	if(mensaje.getKey().equals("OK")) {
        		ResponseEntity<?> response = service.consumeApiRest(
        				mensaje.getJson(), 
        				"http://localhost:18080/recepcion");
            	log.info("response: " + response);
        	}else {
        		log.info("TrasmisionArchivo: Mensaje no procesado NOK - " + mensaje);
        	}    		
    	}catch(Exception ex) {
    		log.error(ex);
    	}finally {
        	log.info("     ## finally ##");
    	}
    }
}
