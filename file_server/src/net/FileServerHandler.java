package net;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import dispatch.RequestDispatcher;
import net.ondemand.OnDemandRequest;
import net.service.ServiceResponse;
import net.service.ServiceRequest;
import net.jaggrab.JagGrabRequest;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

/**
 * An {@link IdleStateAwareChannelUpstreamHandler}.
 * @author Graham
 */
public final class FileServerHandler extends IdleStateAwareChannelUpstreamHandler {
	
	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(FileServerHandler.class.getName());

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		if(!(e.getCause() instanceof IOException)) { //Because there's no more data to read (happens when client has been forcibly exited via task manager)
			logger.log(Level.WARNING, "Exception occured for channel: " + e.getChannel() + ", closing...", e.getCause());
			ctx.getChannel().close();
		}
	}

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
		e.getChannel().close();
	}

	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		Object msg = e.getMessage();
		if(msg instanceof ServiceRequest) {
			ServiceRequest request = (ServiceRequest) msg;
			if(request.getId() != ServiceRequest.SERVICE_ONDEMAND) {
				e.getChannel().close();
			} else {
				e.getChannel().write(new ServiceResponse());
			}
		} else if(msg instanceof OnDemandRequest) {
			RequestDispatcher.dispatch(e.getChannel(), (OnDemandRequest) msg);
		} else if(msg instanceof JagGrabRequest) {
			RequestDispatcher.dispatch(e.getChannel(), (JagGrabRequest) msg);
		} else if(msg instanceof HttpRequest) {
			RequestDispatcher.dispatch(e.getChannel(), (HttpRequest) msg);
		} else {
			throw new Exception("unknown message type");
		}
	}

}
