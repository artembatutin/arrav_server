package tool.mapviewer.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Resources {
	
	public static BufferedImage DOT = null;
	
	static {
		try {
			DOT = ImageIO.read(new File("data/tool.mapviewer/dot/dot.png"));
		} catch(IOException e) {
			System.err.println("Could not load dot.png");
			e.printStackTrace();
		}
	}
	
}
