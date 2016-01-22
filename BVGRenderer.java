/* BVGRenderer.java

   BVG Renderer

   B. Bird - 01/03/2016
*/

import java.awt.Color;
import java.awt.Point;

public class BVGRenderer implements BVGRendererBase {
	public void CreateCanvas(Point dimensions, Color background_colour, int scale_factor){
		System.out.println("CreateCanvas " + dimensions + background_colour + scale_factor);
		this.width = dimensions.x;
		this.height = dimensions.y;
		canvas = new PNGCanvas(width,height);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				canvas.SetPixel(x,y, background_colour);

	}

	public int ToInt(boolean bool) {
		return ((bool) ? 1 : 0);
	}

	public void RenderLine(Point endpoint1, Point endpoint2, Color colour, int thickness) {
		// Points for iterating over the line. They will move from endpoint1 to endpoint2.
		int x = endpoint1.x;
		int y = endpoint1.y;

		// Deltas to find out if the line is "steep" or not, and to know how far along the line to go.
		int delta_x = (endpoint2.x - x);
		int delta_y = (endpoint2.y - y);

		// Find out what octrant we are in.
    int oct_x = ToInt(delta_x > 0) - ToInt(delta_x < 0);  // Fancy math.
    int oct_y = ToInt(delta_y > 0) - ToInt(delta_y < 0);  // Fancy math.

		// Correct the deltas by multiplying them by 2 (Not done initially to find the octrant above)
		delta_x = Math.abs(delta_x) * 2;
    delta_y = Math.abs(delta_y) * 2;

		// Set the first pixel.
    canvas.SetPixel(x, y, colour);

		// "Shallow line" (slope <= 1)
    if (delta_x >= delta_y) {
      int error = (delta_y - (delta_x / 2));
      while (x != endpoint2.x) {
        if (error >= 0) {
          error -= delta_x;
          y += oct_y;
        }
        error += delta_y;
        x += oct_x;
        canvas.SetPixel(x, y, colour);
      }
    } else {  // "Steep Line" (Slope > 1)
      int error = (delta_x - (delta_y / 2));
      while (y != endpoint2.y) {
        if (error >= 0) {
          error -= delta_y;
          x += oct_x;
        }
      	error += delta_x;
       	y += oct_y;
        canvas.SetPixel(x, y, colour);
      }
  	}
	}

	public void RenderCircle(Point center, int radius, Color line_colour, int line_thickness){
		System.out.println("RenderCircle " + center + radius + line_colour + line_thickness);
	}
	public void RenderFilledCircle(Point center, int radius, Color line_colour, int line_thickness, Color fill_colour){
		System.out.println("RenderFilledCircle " + center + radius + line_colour + line_thickness + fill_colour);
	}
	public void RenderTriangle(Point point1, Point point2, Point point3, Color line_colour, int line_thickness, Color fill_colour){
		System.out.println("RenderTriangle " + point1 + point2 + point3 + line_colour + line_thickness + fill_colour);
	}
	public void RenderGradientTriangle(Point point1, Point point2, Point point3, Color line_colour, int line_thickness, Color colour1, Color colour2, Color colour3){
		System.out.println("RenderGradientTriangle " + point1 + point2 + point3 + line_colour + line_thickness + colour1 + colour2 + colour3);
	}

	public void SaveImage(String filename){
		canvas.SaveImage(filename);
	}

	private int width,height;
	private PNGCanvas canvas;
}
