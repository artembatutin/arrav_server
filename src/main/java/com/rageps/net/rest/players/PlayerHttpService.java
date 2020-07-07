//package com.rageps.net.rest.players;
//
//import com.rageps.world.World;
//import com.rageps.world.entity.actor.player.Player;
//import io.netty.handler.codec.http.HttpResponseStatus;
//import org.rakam.server.http.HttpRequestException;
//import org.rakam.server.http.HttpService;
//import org.rakam.server.http.RakamHttpRequest;
//import org.rakam.server.http.annotations.IgnoreApi;
//import org.speced.game.World;
//import org.speced.game.model.player.Player;
//import com.rageps.net.rest.RestfulHttpServer;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Path("/players")
//@IgnoreApi
//public final class PlayerHttpService extends HttpService {
//
//	@GET
//	@Path("/count")
//	public int count(RakamHttpRequest request) {
//		if (!RestfulHttpServer.validate(request)) {
//			throw new HttpRequestException("Unauthorized.", HttpResponseStatus.UNAUTHORIZED);
//		}
//
//		return World.get().getPlayers().size();
//	}
//
//	@GET
//	@Path("/list")
//	public List<String> list(RakamHttpRequest request) {
//		if (!RestfulHttpServer.validate(request)) {
//			throw new HttpRequestException("Unauthorized.", HttpResponseStatus.UNAUTHORIZED);
//		}
//
//		return World.get().getPlayers().stream().map(player -> player.credentials.username).collect(Collectors.toList());
//	}
//
//}
