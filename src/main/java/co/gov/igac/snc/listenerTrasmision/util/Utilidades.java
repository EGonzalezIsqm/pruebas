package co.gov.igac.snc.listenerTrasmision.util;

import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpClient;

public class Utilidades {

	public static ResponseEntity<?> consumirApi(Object peticion, String urlApi) {
		TcpClient tcpClient = TcpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000)
				.doOnConnected(connection -> {
					connection.addHandlerLast(new ReadTimeoutHandler(15000, TimeUnit.MILLISECONDS));
					connection.addHandlerLast(new WriteTimeoutHandler(15000, TimeUnit.MILLISECONDS));
				});
		WebClient webClient = WebClient.builder()
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();

		ResponseEntity<?> respuesta = webClient.method(HttpMethod.POST)
				.uri(urlApi)
				.body(Mono.just(peticion), Object.class)
				.retrieve()
		        .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.empty())
		        .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.empty())
				.toEntity(String.class)
				.block();

		return respuesta;
	}
}
