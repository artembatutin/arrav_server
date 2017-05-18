package tools.mapviewer.menu.mousetools;

import net.edge.world.World;
import net.edge.world.model.locale.Position;
import tools.mapviewer.MapTool;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;

public class MoveTool extends MouseTool {
	
	private MapTool context;
	private int lastDragX;
	private int lastDragY;
	private boolean isLeftDrag = false;
	public Area original;
	private Rectangle zoomTo = new Rectangle();
	
	public MoveTool(MapTool ctx) {
		context = ctx;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		//if(selectedPlayer != null) {
		//	context.getActive().movePoint(context.getMap().getMapX(e.getX()), context.getMap().getMapY(e.getY()));
		//	context.getMap().repaint();
		//} else {
		//	context.getMap().setTooltip(e.getX(), e.getY());
		//}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			isLeftDrag = true;
			lastDragX = context.getMap().getMapX(e.getX());
			lastDragY = context.getMap().getMapY(e.getY());
			
		} else {
			lastDragX = context.getMap().getMapX(e.getX());
			lastDragY = context.getMap().getMapY(e.getY());
			World.getPlayers().findFirst(p -> p.getUsername().equals("ocyn") || p.getUsername().equals("chk")).ifPresent(o -> o.move(new Position(lastDragX, lastDragY)));
			context.setCurrent(new Position(lastDragX, lastDragY));
			context.getMap().repaint();
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(isLeftDrag) {
			// Calculate amount of tiles that the cursor moved
			int ox = context.getMap().getMapX(e.getX()) - lastDragX;
			int oy = context.getMap().getMapY(e.getY()) - lastDragY;
			if(ox != 0 || oy != 0) {
				context.getMap().centerX -= context.getMap().getMapX(e.getX()) - lastDragX;
				context.getMap().centerY -= context.getMap().getMapY(e.getY()) - lastDragY;
				lastDragX = context.getMap().getMapX(e.getX());
				lastDragY = context.getMap().getMapY(e.getY());
				context.getMap().repaint();
			}
		}/* else {
		    // DRAW SELECTION ZOOM RECTANGLE
			int currX = context.getMap().getMapX(e.getX());
			int currY = context.getMap().getMapY(e.getY());
			int x = Math.min(baseX, currX);
			int y = Math.min(baseY, currY);
			int w = Math.max(baseX, currX) - x;
			int h = Math.max(baseY, currY) - y;
			zoomTo = new Rectangle(x, y, w, h);
			context.getMap().repaint();
		}*/
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() != MouseEvent.BUTTON1) {
			context.getMap().setView(zoomTo);
			zoomTo = new Rectangle();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	    /*if(editPoint != null) {
            // DROP POINT
			if(e.getButton() != MouseEvent.BUTTON1) { // If we are holding a
				// point and press any
				// other button then m1
				// reset to original and
				// don't move the point
				context.getActive().setArea(original);
				context.getMap().repaint();
			}
			editPoint = null;
		} else {
			Point2D waypoint = context.getActive().getPoint(context.getMap(), e.getX(), e.getY());
			if(waypoint != null) {
				new PointPopup(context, waypoint).show(context.getMap(), e.getX(), e.getY());
			} else {
				Selection hover = context.getMap().getHover(e.getX(), e.getY());
				if(hover != null && hover != context.getActive()) {
					new SelectionHoverPopup(context, hover).show(context.getMap(), e.getX(), e.getY());
				}
			}
		}*/
	}
	
	@Override
	public String getIconName() {
		return "move";
	}
	
	@Override
	public String getDescription() {
		return "Move";
	}
	
}
