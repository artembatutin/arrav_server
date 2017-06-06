package net.edge.net.voting;

import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;

import java.util.Optional;

public class Motivote extends Task {
	
	/**
	 * Voting platform.
	 */
	private static final com.motiservice.Motivote platform = new com.motiservice.Motivote("edgeville", "ffc180fe2ae2189f64a87f281d74d222");
	
	public Motivote() {
		super(500, false);//each 5 min
	}
	
	@Override
	protected void execute() {
		platform.checkUnredeemedPeriodically((result) -> result.votes().forEach((vote) -> {
			if (vote.username() != null) {
				Optional<Player> player = World.get().getPlayer(vote.username());
				
				if (player.isPresent()) {
					platform.redeemFuture(vote).thenAccept((r2) -> {
						if (r2.success()) {
							// reward user
							//player.addItem(995, 1000);
						}
					});
				}
			}
		}));
	}
}
