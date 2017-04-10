package tools.map;

import tools.map.util.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapPanel extends JPanel {
	
	/**
	 * The map panels context (parent)
	 */
	private MapTool context;
	
	/**
	 * The buffered image were we draw parts from
	 */
	private BufferedImage img;
	
	/**
	 * s The zoom percentage, 100% means 1 pixel per tile
	 */
	private int zoom = 100;
	
	/**
	 * X coordinate of the center of the map portion we are viewing
	 */
	public int centerX = 3080;
	
	/**
	 * Y coordinate of the center of the map portion we are viewing
	 */
	public int centerY = 3510;
	
	/**
	 * Map plane we are viewing
	 */
	private int plane = 0;
	
	/**
	 * Creates a MapPanel, loads the map and registers listeners for the
	 * component
	 */
	MapPanel(MapTool mapViewer) {
		this.context = mapViewer;
		loadMap();
		MouseAdapter ma = new MapPanelMouseAdapter();
		addMouseMotionListener(ma);
		addMouseWheelListener(ma);
		setFocusable(true); // focus is required for keyboard input
		addKeyListener(new MapPanelKeyAdapter());
	}
	
	/**
	 * Clears the panel, draws the section of the map being viewed and then
	 * draws the selections over the map
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.drawImage(img, 0, 0, getWidth(), getHeight(), centerX - getHorizontalViewSize(), img.getHeight() - (centerY + getVerticalViewSize()), centerX + getHorizontalViewSize(), img.getHeight() - (centerY - getVerticalViewSize()), null);
		Graphics2D g2d = (Graphics2D) g;
		
		if(context.getCurrent() != null) {
			int drawX = getPanelX(context.getCurrent().getX());
			int drawY = getPanelY(context.getCurrent().getY());
			double ox = Math.abs(drawX);
			double oy = Math.abs(drawY);
			int x = (int) ox;
			int y = (int) oy;
			g2d.drawImage(Resources.DOT, null, x - 3, y - 3);
		}
		
		g2d.setColor(new Color(0x4400FFFF, true));
		// Draw players/npcs etc if you want in realtime here
	}
	
	public void setView(Rectangle rect) {
		if(rect.getWidth() == 0 && rect.getHeight() == 0) {
			return;
		}
		// Calculate the preferred zoom for the rect size
		int zoomX = (int) (context.getMap().getWidth() * 100 / rect.getWidth());
		int zoomY = (int) (context.getMap().getHeight() * 100 / rect.getHeight());
		// Center the map on the center of the selected rectangle
		context.getMap().centerX = (int) rect.getCenterX();
		context.getMap().centerY = (int) rect.getCenterY();
		// Use the minimum zoom value to ensure everything selected is shown
		context.getMap().setZoom(Math.min(zoomX, zoomY));
		repaint();
	}
	
	private int getHorizontalViewSize() {
		return getWidth() * 100 / zoom / 2;
	}
	
	private int getVerticalViewSize() {
		return getHeight() * 100 / zoom / 2;
	}
	
	public int getMapX(int panelX) {
		return centerX - getHorizontalViewSize() + (2 * getHorizontalViewSize() * panelX / getWidth());
	}
	
	public int getMapY(int panelY) {
		return (int) ((centerY - getVerticalViewSize()) + getVerticalViewSize() / (getHeight() / 2D) * (getHeight() - panelY));
	}
	
	private int getPanelX(double mapX) {
		return ((int) mapX - centerX + getHorizontalViewSize()) * getWidth() / 2 / getHorizontalViewSize();
	}
	
	private int getPanelY(double mapY) {
		return -(((int) mapY - centerY - getVerticalViewSize()) * getHeight() / 2 / getVerticalViewSize());
	}
	
	public int getTileCenterX(double mapX) {
		return getPanelX(mapX) + context.getWidth() / getHorizontalViewSize() / 4;
	}
	
	public int getTileCenterY(double mapY) {
		return getPanelY(mapY) - context.getHeight() / getVerticalViewSize() / 4;
	}
	
	private void loadMap() {
		try {
			this.img = ImageIO.read(new File("./data/map/plane/map" + plane + ".png"));
		} catch(IOException e) {
			System.err.println("FATAL ERROR: Could not load map_" + plane + ".png");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void setZoom(int z) {
		zoom = z;
		if(zoom < 7) {
			zoom = 7;
		}
		if(zoom > 2000) {
			zoom = 2000;
		}
		context.getJMenuBar().setZoom(zoom);
		
		if(getMapX(getWidth()) < 0) {
			centerX = 0;
		} else if(getMapX(0) > 6400) {
			centerX = 6400;
		}
		if(getMapY(0) < 1000) {
			centerY = 1000;
		} else if(getMapY(getHeight()) > 11000) {
			centerY = 11000;
		}
	}
	
	private class MapPanelMouseAdapter extends MouseAdapter {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			context.getJMenuBar().setCoord(getMapX(e.getX()), getMapY(e.getY()), plane);
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			setZoom((int) (zoom - e.getWheelRotation() * Math.sqrt(Math.abs(zoom))));
			repaint();
		}
	}
	
	private class MapPanelKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_UP:
					centerY += getVerticalViewSize() / 8;
					repaint();
					break;
				case KeyEvent.VK_DOWN:
					centerY -= getVerticalViewSize() / 8;
					repaint();
					break;
				case KeyEvent.VK_LEFT:
					centerX -= getHorizontalViewSize() / 8;
					repaint();
					break;
				case KeyEvent.VK_RIGHT:
					centerX += getHorizontalViewSize() / 8;
					repaint();
					break;
				case KeyEvent.VK_PAGE_UP:
					if(plane < 3) {
						plane++;
						loadMap();
						repaint();
					}
					break;
				case KeyEvent.VK_PAGE_DOWN:
					if(plane > 0) {
						plane--;
						loadMap();
						repaint();
					}
					break;
				case KeyEvent.VK_ADD:
					setZoom((int) (zoom + Math.sqrt(Math.abs(zoom))));
					repaint();
					break;
				case KeyEvent.VK_SUBTRACT:
					setZoom((int) (zoom - Math.sqrt(Math.abs(zoom))));
					repaint();
					break;
			}
		}
	}
	
}