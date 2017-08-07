package net.edge.world.entity.actor.mob.impl.skeletal;

public enum SkeletalHorrorStage {

	MAIN(9177),
	ARM_CUT(9178),
	NO_ARMS(9179),
	NO_TAIL(9180);
	
	protected int npc;
	
	SkeletalHorrorStage(int npc) {
		this.npc = npc;
	}

}
