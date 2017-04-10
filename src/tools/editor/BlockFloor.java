package tools.editor;

import static net.edge.world.model.node.region.RegionTile.FLAG_BLOCKED;

class BlockFloor {
	
	//Flooring
	public int[] heightmap = new int[4];
	public int[] overlayFloorId = new int[4];
	public int[] overlayClippingPath = new int[4];
	public int[] overlayRotation = new int[4];
	public int[] renderRuleFlag = new int[4];
	public int[] underlayFloorId = new int[4];
	public boolean[] autoHeight = new boolean[4];
	
	public Block block;
	
	public BlockFloor() {
		
	}
	
	public BlockFloor(Block block) {
		this.block = block;
	}
	
	public BlockFloor(BlockFloor floor) {
		this.block = null;
		this.heightmap = floor.heightmap;
		this.overlayFloorId = floor.overlayFloorId;
		this.overlayClippingPath = floor.overlayClippingPath;
		this.overlayRotation = floor.overlayRotation;
		this.renderRuleFlag = floor.renderRuleFlag;
		this.underlayFloorId = floor.underlayFloorId;
		this.autoHeight = floor.autoHeight;
	}
	
	public boolean isClipped(int height) {
		return ((renderRuleFlag[height] & FLAG_BLOCKED) != 0);
	}
	
	public void toggleClip(int height) {
		if(isClipped(height)) {
			renderRuleFlag[height] &= ~FLAG_BLOCKED;
		} else {
			renderRuleFlag[height] |= FLAG_BLOCKED;
		}
	}
	
	public void setBlock(Block block) {
		this.block = block;
	}
}
