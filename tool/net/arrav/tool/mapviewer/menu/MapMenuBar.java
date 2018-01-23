package net.arrav.tool.mapviewer.menu;

import net.arrav.tool.mapviewer.MapPanel;
import net.arrav.tool.mapviewer.MapTool;
import net.arrav.tool.mapviewer.menu.mousetools.MoveTool;

import javax.swing.*;

public class MapMenuBar extends JMenuBar {
	
	private MapTool context;
	
	private JMenuItem coordItem;
	private JMenuItem zoomItem;
	private JMenu file;
	private JMenu tool;
	
	public MouseToolItem<MoveTool> drag;
	
	public MouseToolItem<?> currentItem;
	
	public MapMenuBar(MapTool mapViewer) {
		context = mapViewer;
		
		file = new JMenu("File");
		file.addSeparator();
		file.add("Cancel");
		add(file);
		
		tool = new JMenu();
		
		drag = new MouseToolItem<>(this, new MoveTool(context));
		
		tool.add(drag);
		add(tool);
		
		coordItem = new JMenuItem();
		setCoord(3200, 3200, 0);
		add(coordItem);
		
		zoomItem = new JMenuItem();
		setZoom(100);
		add(zoomItem);
	}
	
	public void setCoord(int x, int y, int z) {
		coordItem.setText("Coord: (" + x + ", " + y + ", " + z + ") (" + (x >> 6) + "_" + (y >> 6) + ")");
	}
	
	public void setZoom(int zoom) {
		zoomItem.setText("Zoom: " + zoom + "%");
	}
	
	public MapPanel getMap() {
		return context.getMap();
	}
	
}
