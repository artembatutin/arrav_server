package tools.editor;

import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.object.ObjectNode;

import java.util.HashSet;
import java.util.Set;

import static net.edge.world.model.node.object.ObjectDirection.*;
import static net.edge.world.model.node.object.ObjectType.*;

class Block extends Rectangle {
	
	private static final Color DEFAULT = Color.color(0.274, 0.325, 0.384);
	private static final Color WALL_COLOR = Color.color(0, 0.098, 0.2117);
	
	public final int x;
	public final int y;
	
	public final int yOrigin;
	private int selected = 0;
	private boolean empty = true;//performance.
	
	private Set<ObjectNode> objects;
	private Group container = new Group();
	public BlockFloor floor;
	
	Block(RegionEditor edit, Set<ObjectNode> objects, BlockFloor floor, int x, int y, int yOrigin) {
		super(20, 20, DEFAULT);
		this.x = x;
		this.y = y;
		this.yOrigin = yOrigin;
		this.setX(512 + x * 20);
		this.setY(35 + ((63 - y) * 20));
		this.setStroke(Color.color(0.35, 0.35, 0.35));
		this.setStrokeWidth(1);
		this.objects = objects;
		this.floor = floor;
		this.floor.setBlock(this);
		this.setOnMouseMoved(event -> {
			if(edit.isPaintFloor()) {
				if(edit.getFloor() != null) {
					//setFloor(edit.getFloor());
					floor.heightmap[edit.getHeight()] = edit.getFloor().heightmap[edit.getHeight()];
					//floor.overlayFloorId[edit.getHeight()] = edit.getFloor().overlayFloorId[edit.getHeight()];
					//floor.underlayFloorId[edit.getHeight()] = edit.getFloor().underlayFloorId[edit.getHeight()];
					//floor.renderRuleFlag = edit.getFloor().renderRuleFlag;
					//floor.setBlock(this);
				}
			}
			if(edit.isUnselecting()) {
				for(ObjectNode o : objects) {
					if(edit.getList().getItems().contains(o) && o.getPosition().getZ() == edit.getHeight()) {
						edit.getList().getItems().remove(o);
						refreshFill(0, edit.getHeight());
					}
				}
			}
			if(edit.isHoverAdd()) {
				onClick(event.getButton(), edit);
			}
		});
		this.setOnMouseClicked(event -> onClick(event.getButton(), edit));
	}
	
	void refreshFill(int selected, int height) {
		this.selected = selected;
		Color color = DEFAULT;
		if(selected == 2) {
			color = Color.color(0.909, 0.7725, 0.2784);
		} else if(selected == 1) {
			color = Color.color(0.196, 0.576, 0.4352);
		} else if(floor.isClipped(height)) {
			color = Color.ORANGE;
		}
		this.setFill(color);
	}
	
	void refreshSelected(RegionEditor edit) {
		int selected = 0;
		if(objects.contains(edit.getSelected())) {
			selected = 2;
		} else {
			for(ObjectNode o : objects) {
				if(edit.getList().getItems().contains(o)) {
					selected = 1;
					break;
				}
			}
		}
		refreshFill(selected, edit.getHeight());
	}
	
	private void onClick(MouseButton button, RegionEditor edit) {
		if(edit.move) {
			if(edit.x1 == -1) {
				edit.x1 = x;
				edit.y1 = yOrigin;
			} else if(edit.x2 == -1) {
				edit.x2 = x;
				edit.y2 = yOrigin;
				System.out.println(edit.x1 + " - " + edit.y1 + " | " + edit.x2 + " - " + edit.y2);
				edit.copiedBlocks = new Block[edit.x2 - edit.x1][edit.y2 - edit.y1];
				edit.copiedFloors = new BlockFloor[edit.x2 - edit.x1][edit.y2 - edit.y1];
				for(int x = 0; x < edit.copiedBlocks.length; x++) {
					for(int y = 0; y < edit.copiedBlocks[x].length; y++) {
						edit.copiedBlocks[x][y] = edit.blocks[edit.x1 + x][edit.y1 + y].copy(edit, x, y);
						edit.copiedFloors[x][y] = new BlockFloor(edit.blocks[edit.x1 + x][edit.y1 + y].floor);
					}
				}
				for(int x = 0; x < edit.blocks.length; x++) {
					for(int y = 0; y < edit.blocks[x].length; y++) {
						edit.blocks[x][y].getObjects().clear();
						edit.blocks[x][y].refresh(edit);
						edit.blocks[x][y].setFloor(new BlockFloor());
						edit.blocks[x][y].floor.setBlock(edit.blocks[x][y]);
					}
				}
				edit.create(new Position(3080, 3500));
			} else {
				for(int x = 0; x < edit.x2 - edit.x1; x++) {
					for(int y = 0; y < edit.y2 - edit.y1; y++) {
						//edit.blocks[this.x + x][this.y + y] = edit.copiedBlocks[x][edit.copiedBlocks[x].length - y - 1].copy(edit, this.x + x, this.y + y, floor);
						//edit.blocks[this.x + x][this.y + y].setFloor(new BlockFloor(edit.copiedFloors[x][edit.copiedBlocks[x].length - y - 1]));
						//System.out.println(edit.copiedFloors[x][edit.copiedBlocks[x].length - y - 1].overlayFloorId[0]);
						//edit.blocks[this.x + x][this.y + y].floor.setBlock(edit.blocks[this.x + x][this.y + y]);
						for(ObjectNode o : edit.copiedBlocks[x][edit.copiedFloors[x].length - y - 1].getObjects()) {
							edit.blocks[this.x + x][64 - (this.y + y)].addObject(edit, o);
						}
						edit.blocks[this.x + x][64 - (this.y + y)].floor.overlayFloorId = edit.copiedFloors[x][edit.copiedFloors[x].length - y - 1].overlayFloorId;
						edit.blocks[this.x + x][64 - (this.y + y)].floor.underlayFloorId = edit.copiedFloors[x][edit.copiedFloors[x].length - y - 1].underlayFloorId;
						edit.blocks[this.x + x][64 - (this.y + y)].floor.renderRuleFlag = edit.copiedFloors[x][edit.copiedFloors[x].length - y - 1].renderRuleFlag;
						edit.blocks[this.x + x][64 - (this.y + y)].floor.overlayClippingPath = edit.copiedFloors[x][edit.copiedFloors[x].length - y - 1].overlayClippingPath;
						edit.blocks[this.x + x][64 - (this.y + y)].floor.overlayRotation = edit.copiedFloors[x][edit.copiedFloors[x].length - y - 1].overlayRotation;
						
					}
				}
				for(int x = 0; x < edit.blocks.length; x++) {
					for(int y = 0; y < edit.blocks[x].length; y++) {
						edit.blocks[x][y].refresh(edit);
					}
				}
			}
		}
		if(edit.isUnselecting())
			return;
		if(button == MouseButton.SECONDARY && edit.getCopied() != null) {
			addObject(edit, edit.getCopied());
			edit.setCopied(edit.getCopied().copy());
			return;
		}
		edit.setF(floor);
		if(empty)
			return;
		objects.stream().filter(o -> !edit.getList().getItems().contains(o) && o.getPosition().getZ() == edit.getHeight()).forEach(o -> {
			edit.getList().getItems().add(o);
			if(edit.getSelected() == o)
				refreshFill(2, edit.getHeight());
			else if(selected != 2)
				refreshFill(1, edit.getHeight());
		});
	}
	
	void addObject(RegionEditor edit, ObjectNode o) {
		o.setPosition(new Position(x, y, o.getPosition().getZ()));
		if(objects.add(o))
			empty = false;
		if(edit.getHeight() != o.getPosition().getZ())
			return;
		if(edit.getSelected() == o)
			refreshFill(2, edit.getHeight());
		refresh(edit);
	}
	
	boolean removeObject(RegionEditor edit, ObjectNode o) {
		if(!objects.remove(o)) {
			System.out.println("can't remove " + o.toString());
			return false;
		} else {
			System.out.println("removed: " + o.toString());
		}
		if(edit.getSelected() == o)
			refreshFill(1, edit.getHeight());
		if(objects.stream().filter(ob -> ob.getPosition().getZ() == edit.getHeight()).count() == 0) {
			refreshFill(0, edit.getHeight());
			empty = true;
		}
		refresh(edit);
		return true;
	}
	
	public void refresh(RegionEditor edit) {
		edit.getRoot().getChildren().remove(container);
		container.getChildren().clear();
		objects.stream().filter(p -> p.getPosition().getZ() == edit.getHeight()).forEach(o -> {
			Rectangle add = null;
			if(o.getObjectType() == STRAIGHT_WALL) {
				if(o.getDirection() == NORTH) {
					add = new Rectangle(18, 3, WALL_COLOR);
					add.setX(this.getX() + 1);
					add.setY(this.getY() + 1);
				} else if(o.getDirection() == EAST) {
					add = new Rectangle(3, 18, WALL_COLOR);
					add.setX(this.getX() + 16);
					add.setY(this.getY() + 1);
				} else if(o.getDirection() == WEST) {
					add = new Rectangle(3, 18, WALL_COLOR);
					add.setX(this.getX() + 1);
					add.setY(this.getY() + 1);
				} else if(o.getDirection() == SOUTH) {
					add = new Rectangle(18, 3, WALL_COLOR);
					add.setX(this.getX() + 1);
					add.setY(this.getY() + 16);
				}
			}
			if(o.getObjectType() == DIAGONAL_WALL) {
				if(o.getDirection() == NORTH || o.getDirection() == SOUTH) {
					add = new Rectangle(20, 3, WALL_COLOR);
					add.setX(this.getX());
					add.setY(this.getY() + 8);
					add.setRotate(45);
				}
				if(o.getDirection() == EAST || o.getDirection() == WEST) {
					add = new Rectangle(20, 3, WALL_COLOR);
					add.setX(this.getX());
					add.setY(this.getY() + 8);
					add.setRotate(135);
				}
			}
			if(o.getObjectType() == WALKABLE_PROP) {
				add = new Rectangle(6, 12, Color.color(0.0235, 0.8392, 0.62745));
				add.setX(this.getX() + 4);
				add.setY(this.getY() + 4);
			}
			if(o.getObjectType() == GENERAL_PROP) {
				add = new Rectangle(6, 12, Color.color(0.93725, 0.2784, 0.43529));
				add.setX(this.getX() + 10);
				add.setY(this.getY() + 4);
			}
			if(add != null) {
				add.setOnMouseClicked(event -> onClick(event.getButton(), edit));
				container.getChildren().add(add);
			}
		});
		edit.getRoot().getChildren().add(container);
	}
	
	public Set<ObjectNode> getObjects() {
		return objects;
	}
	
	public void setFloor(BlockFloor floor) {
		this.floor = floor;
	}
	
	/*
	if(o.getObjectType() == ObjectType.WALL_CORNER) {
			switch(o.getDirection()) {
					case NORTH:
						add = new Rectangle(18, 3, Color.ORCHID);
						add.setX(this.getX() + 1);
						add.setY(this.getY() + 16);
						add2 = new Rectangle(3, 18, Color.ORCHID);
						add.setX(this.getX() + 1);
						add.setY(this.getY() + 1);
						break;
			        case EAST:
                        g.drawLine(19, 1, 19, 19);
						g.drawLine(18, 1, 18, 19);
						g.drawLine(17, 1, 17, 19);
						break;
					case WEST:
						g.drawLine(1, 1, 1, 19);
						g.drawLine(2, 1, 2, 19);
						g.drawLine(3, 1, 3, 19);
						break;
					case SOUTH:
						add = new Rectangle(18, 3, Color.DARKVIOLET);
						add.setX(this.getX() + 1);
						add.setY(this.getY() + 1);
						add2 = new Rectangle(3, 18, Color.DARKVIOLET);
						add2.setX(this.getX() + 1);
						add2.setY(this.getY() + 1);
						break;
}
		}
	 */
	
	public void rotateObjects() {
		objects.forEach(ObjectNode::rotate);
	}
	
	public Block copy(RegionEditor edit, int x, int y) {
		BlockFloor floor = new BlockFloor(this.floor);
		HashSet<ObjectNode> objs = new HashSet<>();
		objs.addAll(objects);
		return new Block(edit, objs, floor, x, y, y + 63);
	}
	
	public Block copy(RegionEditor edit, int x, int y, BlockFloor floor) {
		HashSet<ObjectNode> objs = new HashSet<>();
		objs.addAll(objects);
		return new Block(edit, objs, floor, x, y, y + 63);
	}
	
}
