/* BVGRenderer.java

   BVG Renderer

   B. Bird - 01/03/2016
*/

import java.awt.Color;
import java.awt.Point;

public class BVGRenderer implements BVGRendererBase {

	private void SetPixelSafe(int x, int y, Color col) {
		if (x >=0 && y >=0 && x < width && y < height) {
			canvas.SetPixel(x, y, col);
		}
	}

	public void CreateCanvas(Point dimensions, Color background_colour, int scale_factor){
		this.width = dimensions.x;
		this.height = dimensions.y;
		canvas = new PNGCanvas(width,height);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				SetPixelSafe(x,y, background_colour);

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
    SetPixelSafe(x, y, colour);

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
        SetPixelSafe(x, y, colour);
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
        SetPixelSafe(x, y, colour);
      }
  	}
	}

	public void RenderCircle(Point center, int radius, Color colour, int line_thickness){
		// Shorter var names
		int xr = center.x;
		int yr = center.y;

		// vars for iterations.
		int x = radius;
		int y = 0;
		int dec = 1 - x;

		// Draw octrant parts
		while ( y <= x ) {
			SetPixelSafe(xr+x, yr+y, colour);
			SetPixelSafe(xr-x, yr+y, colour);
			SetPixelSafe(xr+x, yr-y, colour);
			SetPixelSafe(xr-x, yr-y, colour);
			SetPixelSafe(xr+y, yr+x, colour);
			SetPixelSafe(xr-y, yr+x, colour);
			SetPixelSafe(xr+y, yr-x, colour);
			SetPixelSafe(xr-y, yr-x, colour);
			++y;
			if (dec <= 0) {
				dec += 2*y + 1;
			} else {
				--x;
				dec += 2 * (y - x) + 1;
			}
		}
	}

	public void RenderFilledCircle(Point center, int radius, Color line_colour, int line_thickness, Color fill_colour){
		// vars for iterations.
		int x = radius;
		int y = 0;
		int dec = 1 - x;

		// Draw octrant parts. Same code as above, except we draw lines between the points on the circle.
		while ( y <= x ) {
		  for (int i = center.x - x; i <= center.x + x; ++i) {
        SetPixelSafe(i, center.y + y, fill_colour);
        SetPixelSafe(i, center.y - y, fill_colour);
    	}
      for (int i = center.x - y; i <= center.x + y; ++i) {
        SetPixelSafe(i, center.y + x, fill_colour);
        SetPixelSafe(i, center.y - x, fill_colour);
      }
			++y;
			if (dec <= 0) {
				dec += 2*y + 1;
			} else {
				--x;
				dec += 2 * (y - x) + 1;
			}
		}

		// Render outline with existing funtion.
		RenderCircle(center, radius, line_colour, line_thickness);
	}

	public boolean sideOfLine(Point p, Point p1, Point p2) {
		return ((p.x - p2.x) * (p1.y - p2.y) - (p1.x - p2.x) * (p.y - p2.y) < 0.0f);
	}

	public void RenderTriangle(Point point1, Point point2, Point point3, Color line_colour, int line_thickness, Color fill_colour){
		// Find bounding box.
		int boxLeft = Math.min(point1.x, Math.min(point2.x, point3.x));
		int boxTop = Math.min(point1.y, Math.min(point2.y, point3.y));
		int boxRight = Math.max(point1.x, Math.max(point2.x, point3.x));
		int boxBottom = Math.max(point1.y, Math.max(point2.y, point3.y));

		// Iterate over the points. If sign of point is same between the 3 lines it means it's inside the triangle.
		for (int x = boxLeft; x <= boxRight; ++x) {
			for (int y = boxTop; y <= boxBottom; ++y) {
				Point p = new Point(x,y);
				boolean sx = sideOfLine(p, point1, point2);
				boolean sy = sideOfLine(p, point2, point3);
				boolean sz = sideOfLine(p, point3, point1);
				if (sx == sy && sy == sz) {  // All signs the same => Inside the triangle
					SetPixelSafe(x, y, fill_colour);
				}
			}
		}

		// Render outline
		RenderLine(point1, point2, line_colour, line_thickness);
		RenderLine(point2, point3, line_colour, line_thickness);
		RenderLine(point3, point1, line_colour, line_thickness);
	}

	// Gets the area of a triangle.
	public float GetArea(Point center, Point px, Point py) {
		return Math.abs(0.5f * (
			(center.x * px.y) - (center.y * px.x) +
			(px.x * py.y) - (px.y * py.x) +
			(py.x * center.y) - (py.y * center.x)));  // We take the ABS incase the order of the verticies was not correct.
	}

	public void RenderGradientTriangle(Point point1, Point point2, Point point3, Color line_colour, int line_thickness, Color colour1, Color colour2, Color colour3){
		// Find bounding box.
		int boxLeft = Math.min(point1.x, Math.min(point2.x, point3.x));
		int boxTop = Math.min(point1.y, Math.min(point2.y, point3.y));
		int boxRight = Math.max(point1.x, Math.max(point2.x, point3.x));
		int boxBottom = Math.max(point1.y, Math.max(point2.y, point3.y));

		// Iterate over the points. If sign of point is same between the 3 lines it means it's inside the triangle.
		for (int x = boxLeft; x <= boxRight; ++x) {
			for (int y = boxTop; y <= boxBottom; ++y) {
				Point p = new Point(x,y);
				boolean sx = sideOfLine(p, point1, point2);
				boolean sy = sideOfLine(p, point2, point3);
				boolean sz = sideOfLine(p, point3, point1);
				if (sx == sy && sy == sz) {  // All signs the same => Inside the triangle

					float a1 = GetArea(p, point2, point3);  // Color1 Size
					float a2 = GetArea(p, point3, point1);  // Color2 Size
					float a3 = GetArea(p, point1, point2);  // Color3 Size
					float totalArea = a1 + a2 + a3;


					System.out.println("A: " + a1 + ',' + a2 + ',' + a3);

					a1 = a1 / totalArea;  // % of color1 (out of 1)
					a2 = a2 / totalArea;  // % of color1 (out of 1)
					a3 = a3 / totalArea;  // % of color1 (out of 1)

					// Interpolate colors.
					int r = (int)(a1 * colour1.getRed() + a2 * colour2.getRed() + a3 * colour3.getRed());
					int g = (int)(a1 * colour1.getGreen() + a2 * colour2.getGreen() + a3 * colour3.getGreen());
					int b = (int)(a1 * colour1.getBlue() + a2 * colour2.getBlue() + a3 * colour3.getBlue());

					// Bound colors (fix floating point errors)
					if (r < 0) { r = 0; }
					if (g < 0) { g = 0; }
					if (b < 0) { b = 0; }
					if (r > 255) { r = 255; }
					if (g > 255) { g = 255; }
					if (b > 255) { b = 255; }

					// Create interpolated color
					Color col = new Color(r, g, b);

					// Set Pixel
					SetPixelSafe(x, y, col);
				}
			}
		}

		// Render outline
		RenderLine(point1, point2, line_colour, line_thickness);
		RenderLine(point2, point3, line_colour, line_thickness);
		RenderLine(point3, point1, line_colour, line_thickness);
	}

	public void SaveImage(String filename){
		canvas.SaveImage(filename);
	}

	private int width,height;
	private PNGCanvas canvas;
}
