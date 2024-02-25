package firstmode.Paint;

public class PointDrawing {
	
	private double x;
	private double y;
	private int colorId;
	
	public PointDrawing(double x, double y, int colorId) {
		this.x = x;
		this.y = y;
		this.colorId = colorId;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getColorId() {
		return colorId;
	}

	public void setColorId(int colorId) {
		this.colorId = colorId;
	}
	
	
}
