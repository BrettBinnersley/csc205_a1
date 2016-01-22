
/* BVGRenderer.java

   BVG Renderer

   B. Bird - 01/03/2016

  -------------------------------
  -------------------------------

	Edit By: Brett Binnersley
	V00776751
	Csc 205
	2D Graphics (2nd year intro)

	Edited for the point of this course.

	Picture of example is on my phone. Herp Derp.

*/

import java.awt.Color;
import java.awt.Point;

public class bvg implements BVGRendererBase {

	public void CreateCanvas(Point dimensions, Color background_colour, int scale_factor){
		System.out.println("CreateCanvas " + dimensions + background_colour + scale_factor);
		this.width = dimensions.x;
		this.height = dimensions.y;
		this.canvas = new PNGCanvas(width + 1, height + 1);
		for (int y = 0; y < height; ++y)
			for (int x = 0; x < width; ++x)
				canvas.SetPixel(x, y, background_colour);
	}

	// BONUS #1 : Fade colors (IE: R->B)
	public void RenderLine(Point endpoint1, Point endpoint2, Color colour, int thickness) {
		if (endpoint1.x > endpoint2.x) {
			Point tep = endpoint1;
			endpoint1 = endpoint2;
			endpoint2 = tep;
		}
		int dx = endpoint2.x - endpoint1.x;
		int dy = endpoint2.y - endpoint1.y;
		int delta = 2 * dy - dx;  // if < 0, we draw vertically, else horizontally.

		canvas.SetPixel(endpoint1.x, endpoint1.y, colour);

		int itery = endpoint1.y;
		if (delta > 0) {
			++itery;
			delta = delta - (2 * dx);
		}

		for (int iterx = endpoint1.x + 1; iterx< endpoint2.x; ++iterx) {
			System.out.println(iterx + ',' + itery);
			canvas.SetPixel(iterx, itery, colour);
			delta = delta + (2 * dy);
			if (delta > 0) {
				++itery;
				delta = delta - (2*dx);
			}
		}
	}



	public void RenderCircle(Point center, int radius, Color line_colour, int line_thickness) {
		System.out.println("RenderCircle " + center + radius + line_colour + line_thickness);
		// Point center = o_center;
		//
		// for(y = center.y-radius; y < center.y+radius; ++y) {
		// 	int px1;
		// 	int px2;
		// 	// FIND DEGREES
		// 	Math.cos();  // Get X
		// 	// Add two points at (+-px, y);
		// 	// continue iterating.
		// }
	}


	public void RenderFilledCircle(Point center, int radius, Color line_colour, int line_thickness, Color fill_colour) {
		System.out.println("RenderFilledCircle " + center + radius + line_colour + line_thickness + fill_colour);
	}


	public void RenderTriangle(Point point1, Point point2, Point point3, Color line_colour,
														 int line_thickness, Color fill_colour) {
		System.out.println("RenderTriangle " + point1 + point2 + point3 + line_colour + line_thickness + fill_colour);
	}


	public void RenderGradientTriangle(Point point1, Point point2, Point point3, Color line_colour,
																		 int line_thickness, Color colour1, Color colour2, Color colour3) {
		System.out.println("RenderGradientTriangle " + point1 + point2 + point3 + line_colour + line_thickness + colour1 + colour2 + colour3);
	}


	public void SaveImage(String filename){
		System.out.println(filename);
		canvas.SaveImage(filename);
	}


	private int width,height;
	private PNGCanvas canvas;
}
