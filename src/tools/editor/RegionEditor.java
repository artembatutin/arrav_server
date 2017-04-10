package tools.editor;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.edge.fs.FileSystem;
import net.edge.fs.parser.MapDefinitionParser;
import net.edge.fs.parser.ObjectDefinitionParser;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.object.ObjectDefinition;
import net.edge.world.model.node.object.ObjectDirection;
import net.edge.world.model.node.object.ObjectNode;
import net.edge.world.model.node.object.ObjectType;
import net.edge.world.model.node.region.RegionDefinition;
import net.edge.utils.CompressionUtil;
import tools.editor.height.HeightCalculator;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import static net.edge.fs.parser.StaticObjectDefinitionParser.parseGameObject;

public class RegionEditor extends Application {
	
	//copying
	public int x1 = -1;
	public int y1 = -1;
	public int y2 = -1;
	public int x2 = -1;
	public boolean move = false;
	public Block[][] copiedBlocks;
	public BlockFloor[][] copiedFloors;
	
	//configs
	private int height = 0;
	public int objectFile = 0;
	public int floorFile = 0;
	
	//Manipulations
	private boolean hoverAdd;
	private boolean unselecting;
	private boolean paintFloor;
	
	//Decoding
	private FileSystem fileSystem;
	private Map<Integer, RegionDefinition> mapDefinitionList = new HashMap<>();
	
	//data
	private Group root = new Group();
	public Block[][] blocks = new Block[64][64];
	private ObjectNode selected;
	private BlockFloor floor;
	private ObjectNode copied;
	private BlockFloor copiedFloor;
	private ListView<ObjectNode> list;
	
	//information
	private Label copy = new Label("Right click set: none");
	private Label copyf = new Label("Right click set: none");
	private Label status = new Label("Status: ");
	private Label name = new Label("name: ");
	private Label type = new Label("type: ");
	private Label face = new Label("face: ");
	private Label pos = new Label("pos: ");
	
	private Label heightt = new Label("height: ");
	private Label under = new Label("over tex: ");
	private Label over = new Label("under tex: ");
	
	//buttons
	private Button clip = new Button("-");
	private Button copyF = new Button("Copy");
	private Button copyB = new Button("Copy");
	private Button clear = new Button("Clear List");
	private Button rotate = new Button("Rotate");
	private Button delete = new Button("Delete");
	private Button moveup = new Button("Move up");
	private Button typeToggle = new Button("Type");
	private Button deleteAll = new Button("Delete All");
	private IdChange changeId = new IdChange() {
		@Override
		public boolean check(int input) {
			return input < ObjectDefinition.DEFINITIONS.length;
		}
	};
	private IdChange undertex = new IdChange() {
		@Override
		public boolean check(int input) {
			return input >= 0 && input < 235;
		}
	};
	private IdChange overtex = new IdChange() {
		@Override
		public boolean check(int input) {
			return input >= 0 && input < 159;
		}
	};
	private IdChange floorHeight = new IdChange() {
		@Override
		public boolean check(int input) {
			return true;
		}
	};
	private IdChange floorRot = new IdChange() {
		@Override
		public boolean check(int input) {
			return true;
		}
	};
	
	Set<Position> added = new HashSet<>();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		/* creating the object list */
		ObservableList<ObjectNode> items = FXCollections.observableArrayList();
		list = new ListView<>(items);
		list.setCellFactory(cell -> new ListCell<ObjectNode>() {
			@Override
			protected void updateItem(ObjectNode o, boolean empty) {
				super.updateItem(o, empty);
				if(o != null) {
					String name = o.getDefinition().getName();
					setText((name != null ? name + "," : "") + "id:" + o.getId() + ",x:" + o.getPosition().getX() + ",y:" + o.getPosition().getY());
					setFont(Font.font(12));
				} else {
					setText("");
				}
			}
		});
		list.setEditable(true);
		list.maxHeight(1400);
		list.setPrefWidth(512);
		list.setVisible(true);
		list.setScaleShape(true);
		list.setOnMouseClicked(event -> setO(list.getSelectionModel().getSelectedItem()));
		root.getChildren().add(list);
		
		/* creating text labels */
		name.setTranslateX(20);
		name.setTranslateY(450);
		name.setTextFill(Color.BLACK);
		name.setFont(new Font("Arial", 18));
		type.setTranslateX(20);
		type.setTranslateY(480);
		type.setTextFill(Color.BLACK);
		type.setFont(new Font("Arial", 18));
		face.setTranslateX(20);
		face.setTranslateY(510);
		face.setTextFill(Color.BLACK);
		face.setFont(new Font("Arial", 18));
		pos.setTranslateX(20);
		pos.setTranslateY(540);
		pos.setTextFill(Color.BLACK);
		pos.setFont(new Font("Arial", 18));
		status.setTranslateX(540);
		status.setTranslateY(5);
		status.setTextFill(Color.BLACK);
		status.setFont(new Font("Arial", 18));
		copy.setTranslateX(940);
		copy.setTranslateY(5);
		copy.setTextFill(Color.BLACK);
		copy.setFont(new Font("Arial", 14));
		copyf.setTranslateX(1330);
		copyf.setTranslateY(5);
		copyf.setTextFill(Color.BLACK);
		copyf.setFont(new Font("Arial", 14));
		root.getChildren().add(name);
		root.getChildren().add(type);
		root.getChildren().add(face);
		root.getChildren().add(pos);
		root.getChildren().add(status);
		root.getChildren().add(copy);
		
		/* creating buttons */
		changeId.setLayoutX(20);
		changeId.setLayoutY(420);
		changeId.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.length() > 0 && changeId.validate(newValue) && selected != null) {
				selected.setId(Integer.parseInt(newValue));
				name.setText("name: " + selected.getDefinition().getName());
			}
		});
		clear.setLayoutX(200);
		clear.setLayoutY(420);
		clear.setOnMouseClicked(event -> {
			list.getItems().clear();
			for(Block[] b : blocks) {
				for(Block block : b) {
					block.refreshFill(0, height);
				}
			}
			setO(null);
		});
		rotate.setLayoutX(20);
		rotate.setLayoutY(580);
		rotate.setOnMouseClicked(event -> {
			if(selected != null) {
				selected.rotate();
				face.setText("face: " + selected.getDirection().toString());
				blocks[selected.getPosition().getX()][63 - selected.getPosition().getY()].refresh(this);
			}
		});
		moveup.setLayoutX(20);
		moveup.setLayoutY(620);
		moveup.setOnMouseClicked(event -> {
			if(selected != null) {
				if(selected.getPosition().getZ() < 4)
					selected.setPosition(selected.getPosition().move(0, 0, +1));
				else
					selected.setPosition(selected.getPosition().move(0, 0, -selected.getPosition().getZ()));
				blocks[selected.getPosition().getX()][63 - selected.getPosition().getY()].refresh(this);
			}
		});
		typeToggle.setLayoutX(100);
		typeToggle.setLayoutY(620);
		typeToggle.setOnMouseClicked(event -> {
			if(selected != null) {
				selected.toggleType();
				type.setText("type: " + selected.getObjectType().toString());
				blocks[selected.getPosition().getX()][63 - selected.getPosition().getY()].refresh(this);
			}
		});
		delete.setLayoutX(90);
		delete.setLayoutY(580);
		delete.setOnMouseClicked(event -> {
			if(selected != null) {
				if(blocks[selected.getPosition().getX()][63 - selected.getPosition().getY()].removeObject(this, selected))
					;
				setO(null);
			}
		});
		copyB.setLayoutX(165);
		copyB.setLayoutY(580);
		copyB.setOnMouseClicked(event -> {
			if(selected != null) {
				copied = selected.copy();
				String name = copied.getDefinition().getName();
				copy.setText("Right click set: " + (name != null ? name + ", " : "") + "id: " + copied.getId() + ", type: " + copied.getObjectType().name() + ", face: " + copied.getDirection().name());
			}
		});
		deleteAll.setLayoutX(415);
		deleteAll.setLayoutY(470);
		deleteAll.setOnMouseClicked(event -> {
			for(ObjectNode o : list.getItems()) {
				blocks[o.getPosition().getX()][63 - o.getPosition().getY()].removeObject(this, o);
			}
			list.getItems().clear();
			setO(null);
		});
		root.getChildren().add(changeId);
		root.getChildren().add(clear);
		root.getChildren().add(rotate);
		root.getChildren().add(moveup);
		root.getChildren().add(delete);
		root.getChildren().add(typeToggle);
		root.getChildren().add(copyB);
		root.getChildren().add(copyf);
		root.getChildren().add(deleteAll);
		
		heightt.setTranslateX(20);
		heightt.setTranslateY(760);
		heightt.setTextFill(Color.BLACK);
		heightt.setFont(new Font("Arial", 18));
		under.setTranslateX(20);
		under.setTranslateY(700);
		under.setTextFill(Color.BLACK);
		under.setFont(new Font("Arial", 18));
		undertex.setLayoutX(20);
		undertex.setLayoutY(720);
		undertex.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.length() > 0 && changeId.validate(newValue) && floor != null) {
				floor.underlayFloorId[height] = Integer.parseInt(newValue);
				floor.block.setFloor(floor);
			}
		});
		over.setTranslateX(220);
		over.setTranslateY(700);
		over.setTextFill(Color.BLACK);
		over.setFont(new Font("Arial", 18));
		overtex.setLayoutX(220);
		overtex.setLayoutY(720);
		overtex.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.length() > 0 && changeId.validate(newValue) && floor != null) {
				floor.overlayFloorId[height] = Integer.parseInt(newValue);
				floor.block.setFloor(floor);
			}
		});
		clip.setLayoutX(400);
		clip.setLayoutY(720);
		clip.setOnMouseClicked(event -> {
			if(floor != null) {
				floor.toggleClip(height);
				clip.setText("Clip:" + (floor != null ? floor.isClipped(height) : ""));
				floor.block.refresh(this);
			}
		});
		copyF.setLayoutX(400);
		copyF.setLayoutY(780);
		copyF.setOnMouseClicked(event -> {
			if(floor != null) {
				copiedFloor = new BlockFloor(floor);
				copyf.setText("floor: " + "over: " + floor.overlayFloorId[height] + ", under: " + floor.underlayFloorId[height] + ", height: " + floor.heightmap[height] + ", clip: " + floor.isClipped(height));
			}
		});
		floorRot.setLayoutX(220);
		floorRot.setLayoutY(780);
		floorRot.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.length() > 0 && changeId.validate(newValue) && floor != null) {
				floor.overlayRotation[height] = Integer.parseInt(newValue);
				floor.block.setFloor(floor);
			}
		});
		floorHeight.setLayoutX(20);
		floorHeight.setLayoutY(780);
		floorHeight.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.length() > 0 && changeId.validate(newValue) && floor != null) {
				floor.heightmap[height] = Integer.parseInt(newValue);
				floor.block.setFloor(floor);
			}
		});
		root.getChildren().add(under);
		root.getChildren().add(undertex);
		root.getChildren().add(over);
		root.getChildren().add(overtex);
		root.getChildren().add(clip);
		root.getChildren().add(floorHeight);
		root.getChildren().add(floorRot);
		root.getChildren().add(heightt);
		root.getChildren().add(copyF);
		
		/* creating grid blocks */
		for(int x = 0; x < 64; x++) {
			for(int y = 0; y < 64; y++) {
				blocks[x][y] = new Block(this, new HashSet<>(), new BlockFloor(), x, 63 - y, y);
				root.getChildren().add(blocks[x][y]);
			}
		}
		
		/* setting the scene */
		Scene scene = new Scene(root, 1792, 1900, Color.color(0.55, 0.55, 0.55));
		primaryStage.setTitle("Region Editor");
		primaryStage.setScene(scene);
		scene.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.S) {
				status.setText("Status: saving...");
				encodeObjects();
				encodeFloor();
				status.setText("Status: saving done.");
			}
			if(event.getCode() == KeyCode.A) {
				hoverAdd = true;
				unselecting = false;
				status.setText("Status: selecting on hover.");
			}
			if(event.getCode() == KeyCode.D) {
				hoverAdd = false;
				unselecting = true;
				status.setText("Status: unselecting on hover");
			}
			if(event.getCode() == KeyCode.F) {
				paintFloor = true;
				status.setText("Status: painting floor");
			}
			if(event.getCode() == KeyCode.H) {
				height += 1;
				if(height == 4)
					height = 0;
				list.getItems().clear();
				for(Block[] b : blocks) {
					for(Block block : b) {
						block.refreshFill(0, height);
						block.refresh(this);
					}
				}
				setO(null);
				status.setText("Status: height " + height);
			}
			if(event.getCode() == KeyCode.X) {
				move = true;
				status.setText("Status: copable ");
			}
		});
		scene.setOnKeyReleased(event -> {
			if(event.getCode() == KeyCode.A)
				hoverAdd = false;
			if(event.getCode() == KeyCode.D)
				unselecting = false;
			if(event.getCode() == KeyCode.F)
				paintFloor = false;
			if(event.getCode() == KeyCode.X)
				move = false;
			status.setText("Status:");
		});
		
		/* loading the map data */
		load();
		primaryStage.show();
	}
	
	public ObjectNode custom(Position pos) {
		return new ObjectNode(33757, pos, ObjectDirection.WEST, ObjectType.STRAIGHT_WALL);
	}
	
	public void load() {
		try {
			fileSystem = FileSystem.create("data/fs");
			ObjectDefinitionParser.parse(fileSystem);
			mapDefinitionList = MapDefinitionParser.parse(fileSystem);
			create(new Position(3080, 3500));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setO(ObjectNode o) {
		if(o == null && selected != null && list.getItems().contains(selected)) {
			list.getItems().remove(selected);
		}
		ObjectNode old = null;
		if(selected != null)
			old = selected.copy();
		selected = o;
		if(old != null)
			blocks[old.getPosition().getX()][63 - old.getPosition().getY()].refreshSelected(this);
		if(o != null)
			blocks[o.getPosition().getX()][63 - o.getPosition().getY()].refreshSelected(this);
		
		name.setText("name: " + (selected != null ? selected.getDefinition().getName() : ""));
		type.setText("type: " + (selected != null ? selected.getObjectType().toString() : ""));
		face.setText("face: " + (selected != null ? selected.getDirection().toString() : ""));
		pos.setText("pos: " + (selected != null ? selected.getPosition().toString() : ""));
		changeId.setText("" + (selected != null ? selected.getId() : ""));
		
		if(selected != null)
			setF(blocks[selected.getPosition().getX()][63 - selected.getPosition().getY()].floor);
		else
			setF(null);
	}
	
	public void setF(BlockFloor f) {
		floor = f;
		clip.setText("Clip:" + (floor != null ? floor.isClipped(height) : ""));
		undertex.setText("" + (floor != null ? floor.underlayFloorId[height] : ""));
		overtex.setText("" + (floor != null ? floor.overlayFloorId[height] : ""));
		floorHeight.setText("" + (floor != null ? floor.heightmap[height] : ""));
		floorRot.setText("" + (floor != null ? floor.overlayRotation[height] : ""));
	}
	
	public ListView<ObjectNode> getList() {
		return list;
	}
	
	public ObjectNode getSelected() {
		return selected;
	}
	
	public BlockFloor getFloor() {
		return floor;
	}
	
	public void setCopied(ObjectNode copied) {
		this.copied = copied;
	}
	
	public ObjectNode getCopied() {
		return copied;
	}
	
	public void setHoverAdd(boolean hoverAdd) {
		this.hoverAdd = hoverAdd;
	}
	
	public boolean isHoverAdd() {
		return hoverAdd;
	}
	
	public void setUnselecting(boolean unselecting) {
		this.unselecting = unselecting;
	}
	
	public boolean isUnselecting() {
		return unselecting;
	}
	
	public boolean isPaintFloor() {
		return paintFloor;
	}
	
	public Group getRoot() {
		return root;
	}
	
	public int getHeight() {
		return height;
	}
	
	void create(Position pos) {
		int id = pos.getRegion();
		RegionDefinition def = mapDefinitionList.get(id);
		if(def == null)
			return;
		objectFile = def.getObjectFile();
		floorFile = def.getTerrainFile();
		final int hash = def.getHash();
		final int x = (hash >> 8 & 0xFF) * 64;
		final int y = (hash & 0xFF) * 64;
		List<Position> downHeights = new LinkedList<>();
		try {
			ByteBuffer terrainData = fileSystem.getFile(FileSystem.MAP_INDEX, def.getTerrainFile());
			ByteBuffer terrainBuffer = ByteBuffer.wrap(CompressionUtil.gunzip(terrainData.array()));
			System.out.println("terrain");
			parseTerrain(terrainBuffer, x, y);
			System.out.println("done");
			ByteBuffer gameObjectData = fileSystem.getFile(FileSystem.MAP_INDEX, objectFile);
			ByteBuffer gameObjectBuffer = ByteBuffer.wrap(CompressionUtil.gunzip(gameObjectData.array()));
			System.out.println("obj");
			Set<ObjectNode> objects = parseGameObject(gameObjectBuffer, x, y, downHeights, false);
			objects.forEach(o -> {
				Position pos2 = o.getPosition().move(-x, -y);
				o.setPosition(pos2);
				blocks[pos2.getX()][63 - pos2.getY()].addObject(this, o);
			});
			System.out.println("done");
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("[RegionEditor] Loaded region:" + id + " x:" + x + " y:" + y);
	}
	
	private void parseTerrain(ByteBuffer mapBuffer, int baseX, int baseY) {
		for(int z = 0; z < 4; z++) {
			for(int x = 0; x < 64; x++) {
				for(int y = 0; y < 64; y++) {
					while(true) {
						int opcode = mapBuffer.get() & 0xFF;
						if(opcode == 0) {
							if(z == 0) {
								blocks[x][63 - y].floor.heightmap[z] = HeightCalculator.calculateVertexHeight(932731 + baseX + x, 556238 + baseY + (63 - y)) * 8;
							} else {
								blocks[x][63 - y].floor.heightmap[z] = blocks[x][63 - y].floor.heightmap[z - 1] + 240;
							}
							blocks[x][63 - y].floor.autoHeight[z] = true;
							break;
						} else if(opcode == 1) {
							int height = mapBuffer.get() & 0xFF;
							if(height == 1)
								height = 0;
							if(z == 0) {
								blocks[x][63 - y].floor.heightmap[z] = height * 8;
							} else {
								blocks[x][63 - y].floor.heightmap[z] = blocks[x][63 - y].floor.heightmap[z - 1] + (height * 8);
							}
							break;
						} else if(opcode <= 49) {
							blocks[x][63 - y].floor.overlayFloorId[z] = mapBuffer.get();
							blocks[x][63 - y].floor.overlayClippingPath[z] = (byte) ((opcode - 2) / 4);
							blocks[x][63 - y].floor.overlayRotation[z] = (byte) (opcode - 2);
						} else if(opcode <= 81) {
							blocks[x][63 - y].floor.renderRuleFlag[z] = (byte) (opcode - 49);
						} else {
							blocks[x][63 - y].floor.underlayFloorId[z] = (byte) (opcode - 81);
						}
					}
				}
			}
		}
	}
	
	private void encodeObjects() {
		List<ObjectNode> all = new ArrayList<>();
		for(Block[] b : blocks) {
			for(Block block : b) {
				all.addAll(block.getObjects());
			}
		}
		Comparator<ObjectNode> comp = Comparator.comparingInt(ObjectNode::getId).thenComparingInt(o -> o.getPosition().getZ()).thenComparingInt(o -> o.getPosition().getX()).thenComparingInt(o -> o.getPosition().getY());
		Collections.sort(all, comp);
		SortedMap<Integer, List<ObjectNode>> objs = new TreeMap<>();
		for(ObjectNode o : all) {
			List<ObjectNode> nodes = objs.computeIfAbsent(o.getId(), key -> new ArrayList<>());
			nodes.add(o);
		}
		try {
			DataOutputStream os = new DataOutputStream(new FileOutputStream("data/fs/" + objectFile + ".dat"));
			int id = -1;
			int oldHash;
			for(int i : objs.keySet()) {
				oldHash = 0;
				putSmart(os, i - id);
				List<ObjectNode> same = objs.get(i);
				Collections.sort(same, comp);
				for(ObjectNode o : same) {
					int newHash = o.getPosition().getZ() << 12 | (o.getPosition().getX() & 0x3f) << 6 | (o.getPosition().getY() & 0x3f);
					int delta = newHash - oldHash + 1;
					putSmart(os, delta);
					os.writeByte((byte) (o.getObjectType().getId() << 2 | (o.getDirection().getId())));
					oldHash = newHash;
				}
				id = i;
				os.writeByte((byte) 0);
			}
			os.writeByte((byte) 0);
			os.close();
		} catch(Exception ignored) {
			
		}
	}
	
	public void encodeFloor() {
		try {
			DataOutputStream os = new DataOutputStream(new FileOutputStream("data/fs/" + floorFile + ".dat"));
			for(int z = 0; z < 4; z++) {
				for(int x = 0; x < 64; x++) {
					for(int y = 0; y < 64; y++) {
						if(blocks[x][63 - y].floor.overlayFloorId[z] != 0) {
							os.writeByte((blocks[x][63 - y].floor.overlayClippingPath[z] * 4) + (blocks[x][63 - y].floor.overlayRotation[z] & 3) + 2);
							os.writeByte(blocks[x][63 - y].floor.overlayFloorId[z]);
						}
						if(blocks[x][63 - y].floor.renderRuleFlag[z] != 0)
							os.writeByte(blocks[x][63 - y].floor.renderRuleFlag[z] + 49);
						if(blocks[x][63 - y].floor.underlayFloorId[z] != 0)
							os.writeByte(blocks[x][63 - y].floor.underlayFloorId[z] + 81);
						if(blocks[x][63 - y].floor.autoHeight[z]) {
							os.writeByte(0);
						} else {
							if(z == 0) {
								os.writeByte(1);
								os.writeByte(blocks[x][63 - y].floor.heightmap[z] / 8);
							} else {
								os.writeByte(1);
								int height = (blocks[x][63 - y].floor.heightmap[z] - blocks[x][63 - y].floor.heightmap[z - 1]) / 8;
								if(height == 0)
									height = 1;
								os.writeByte(height);
							}
						}
					}
				}
			}
			
		} catch(Exception ignored) {
			
		}
	}
	
	private void putSmart(DataOutputStream os, int value) throws IOException {
		if(value < 128) {
			os.writeByte((byte) value);
		} else {
			value += 32768;
			os.writeByte((byte) (value >> 8));
			os.writeByte((byte) value);
		}
	}
	
}
