package co.gov.igac.snc.listenerTrasmision.service;

import org.springframework.http.ResponseEntity;

public interface IConsumerService {
	
	public ResponseEntity<?> consumeApiRest(Object peticion, String url);

}
