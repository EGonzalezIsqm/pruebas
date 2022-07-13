package co.gov.igac.snc.listenerTrasmision.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.gov.igac.snc.listenerTrasmision.service.IConsumerService;
import co.gov.igac.snc.listenerTrasmision.util.Utilidades;

@Service
public class ConsumerServiceImpl implements IConsumerService{

	@Override
	public ResponseEntity<?> consumeApiRest(Object peticion, String url) {
		return Utilidades.consumirApi(peticion, url);
	}

}
