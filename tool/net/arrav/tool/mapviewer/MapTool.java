package net.arrav.tool.mapviewer;

import net.arrav.world.locale.Position;
import net.arrav.tool.mapviewer.menu.MapMenuBar;

import javax.swing.*;
import java.awt.*;

public class MapTool extends JFrame {
	
	private MapPanel map;
	private MapMenuBar menu;
	private Position current;
	
	public static void start() {
		MapTool frame = new MapTool();
		frame.initComponents();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		MapTool frame = new MapTool();
		frame.initComponents();
		frame.setVisible(true);
	}
	
	private void initComponents() {
		ToolTipManager.sharedInstance().setInitialDelay(0);
		map = new MapPanel(this);
		menu = new MapMenuBar(this);
		menu.drag.register();
		
		setJMenuBar(menu);
		getContentPane().add(BorderLayout.CENTER, map);
		setTitle("Map Viewer");
		setSize(1000, 800);
	}
	
	public MapPanel getMap() {
		return map;
	}
	
	@Override
	public MapMenuBar getJMenuBar() {
		return menu;
	}
	
	public Position getCurrent() {
		return current;
	}
	
	public void setCurrent(Position current) {
		this.current = current;
	}
	
}
