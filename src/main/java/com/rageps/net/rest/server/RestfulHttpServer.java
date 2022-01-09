package com.rageps.net.rest.server;
/*
import com.google.gson.Gson;
import com.rageps.net.rest.RestfulHttpConfig;
import com.rageps.net.rest.RestfulHttpConfigProvider;
import com.rageps.util.ActionListener;
import com.rageps.world.World;
import com.rageps.world.attr.Attr;
import com.rageps.world.attr.AttributeKey;
import com.rageps.world.attr.Attributes;
import io.netty.handler.codec.http.HttpHeaders;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.MIMEHeader;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import javax.xml.soap.MimeHeader;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Tamatea <tamateea@gmail.com>
public class RestfulHttpServer extends AbstractVerticle {

    private final Gson GSON = new Gson();

    @Override
    public void start(Promise<Void> fut) {
        Router router = Router.router(vertx); // <1>
        // CORS support
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PUT);

        router.route().handler(CorsHandler.create("*") // <2>
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));
        router.route().handler(BodyHandler.create()); // <3>

        router.get("/players").handler(rc -> {
            if(validateAndHandle(rc))
                return;
            List<String> players = World.get().getPlayers().stream().map(player -> player.credentials.username).collect(Collectors.toList());
            rc.response().putHeader("content-type", "application/json")
            .end(GSON.toJson(players));
        });


        RestfulHttpConfigProvider provider = RestfulHttpConfigProvider.create();
        RestfulHttpConfig config = provider.get();

        World.getAttributeMap().set(API_KEY, config.getApiKey());

       HttpServer server = vertx.createHttpServer() // <4>
                .requestHandler(router::accept)
                .listen(config.getPort(), config.getHost(), result -> {
                    if (result.succeeded())
                        fut.complete();
                    else
                        fut.fail(result.cause());
                });

    }

    private boolean validateAndHandle(RoutingContext rc) {
        String expectedApiKey = World.getAttributeMap().getString(API_KEY);
        if (expectedApiKey == null) {
            return true;
        }

        MultiMap headers = rc.request().headers();
        if (headers.isEmpty()) {
            return false;
        }
        //rc.request().params().get("api_key"); might be like this
        String apiKey = rc.request().headers().get("api_key");
        return expectedApiKey.equals(apiKey);
    }

    public void players(RoutingContext rcx) {
        if(validateAndHandle(rcx))
            return;

    }

    @Attr
    private static final AttributeKey API_KEY = Attributes.define("api_key", null);

}
*/