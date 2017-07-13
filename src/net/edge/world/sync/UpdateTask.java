package net.edge.world.sync;

import net.edge.util.LoggerUtils;
import net.edge.world.node.entity.EntityNode;

import java.util.concurrent.Phaser;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateTask implements Runnable{
	
	private final static Logger logger = LoggerUtils.getLogger(UpdateTask.class);
	
	private final Phaser phaser;
	
	private final EntityNode entity;
	
	private final UpdateType type;
	
	public UpdateTask(UpdateType type, EntityNode entity, Phaser phaser) {
		this.type = type;
		this.entity = entity;
		this.phaser = phaser;
	}
	
	@Override
	public void run() {
		try {
			synchronized(entity) {
				switch(type) {
					case PRE_UPDATE:
						entity.preUpdate();
						break;
					case UPDATE:
						entity.update();
						break;
					case POST_UPDATE:
						entity.postUpdate();
						break;
				}
			}
		} catch(Exception e) {
			logger.log(Level.WARNING, type + " sync error " + entity, e);
		} finally {
			 phaser.arriveAndDeregister();
		}
	}
	
}
