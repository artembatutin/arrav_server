//package com.rageps.net.rest;
//
//import com.google.common.collect.Sets;
//import com.rageps.world.World;
//import com.rageps.world.attr.Attr;
//import com.rageps.world.attr.AttributeKey;
//import com.rageps.world.attr.Attributes;
//import com.rageps.world.env.Environment;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.epoll.Epoll;
//import io.netty.channel.epoll.EpollEventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.handler.codec.http.HttpHeaders;
//import io.swagger.models.Contact;
//import io.swagger.models.Info;
//import io.swagger.models.License;
//import io.swagger.models.Swagger;
//import io.swagger.models.auth.ApiKeyAuthDefinition;
//import io.swagger.models.auth.In;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.rakam.server.http.HttpServer;
//import org.rakam.server.http.HttpServerBuilder;
//import org.rakam.server.http.HttpService;
//import org.rakam.server.http.RakamHttpRequest;
//import com.rageps.net.rest.players.PlayerHttpService;
//
//import java.util.Set;
//
//public final class RestfulHttpServer {
//	private static final Logger logger = LogManager.getLogger(RestfulHttpServer.class);
//
//	public static void init() {
//		Environment environment = World.get().getEnvironment();
//		if (environment.getType() != Environment.Type.LOCAL) {
//			return;
//		}
//
//		RestfulHttpConfigProvider provider = RestfulHttpConfigProvider.create();
//		RestfulHttpConfig config = provider.get();
//
//		Info info = new Info().title(environment.getName())
//								 .version(Integer.toString(environment.getVersion()))
//								 .description("A RuneScape emulation based on the #317 protocol.")
//								 .contact(new Contact().email("webmaster@disergent.ps"))
//								 .license(new License()
//													 .name("Apache License 2.0")
//													 .url("http://www.apache.org/licenses/LICENSE-2.0.html"));
//
//		Swagger swagger = new Swagger()
//											 .info(info)
//											 .host(config.getHost())
//											 .basePath("/")
//											 .securityDefinition("api_key", new ApiKeyAuthDefinition().in(In.HEADER).name("api_key"));
//
//		Set<HttpService> modules = Sets.newHashSet(new PlayerHttpService());
//
//		EventLoopGroup eventExecutors;
//		if (Epoll.isAvailable()) {
//			eventExecutors = new EpollEventLoopGroup();
//		} else {
//			eventExecutors = new NioEventLoopGroup();
//		}
//
//		World.getAttributeMap().set(API_KEY, config.getApiKey());
//
//		HttpServer build = new HttpServerBuilder()
//												.setHttpServices(modules)
//												.setSwagger(swagger)
//												.setMaximumBody(Runtime.getRuntime().maxMemory() / 10)
//												.setEventLoopGroup(eventExecutors).build();
//
//		try {
//			build.bind(config.getHost(), config.getPort());
//		} catch (InterruptedException cause) {
//			logger.error("Unable to bind RestfulHttpServer.", cause);
//		}
//	}
//
//	public static boolean validate(RakamHttpRequest request) {
//		String expectedApiKey = World.getAttributeMap().getString(API_KEY);
//		if (expectedApiKey == null) {
//			return true;
//		}
//
//		HttpHeaders headers = request.headers();
//		if (headers.isEmpty()) {
//			return false;
//		}
//
//		String apiKey = headers.get("api_key");
//		return expectedApiKey.equals(apiKey);
//	}
//
//	@Attr
//	private static final AttributeKey API_KEY = Attributes.define("api_key", null);
//}
