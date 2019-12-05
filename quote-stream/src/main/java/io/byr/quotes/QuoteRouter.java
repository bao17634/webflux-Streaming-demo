package io.byr.quotes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class QuoteRouter {

	@Bean
	public RouterFunction<ServerResponse> route(QuoteHandler quoteHandler) {
		return RouterFunctions
				/**
				 * 路由匹配成功之后则执行回调函数，并返回一个mono<HandlerFunction>
				 *     如匹配不到则返回mono<void>
				 */
				.route(GET("/quotes").and(accept(TEXT_EVENT_STREAM)), quoteHandler::fetchQuotesSSE)
				/**
				 *如果上一个路由不匹配，则进行下一个路由匹配
				 */
				.andRoute(GET("/quotes").and(accept(APPLICATION_STREAM_JSON)), quoteHandler::fetchQuotes);
	}
}
