package firstmode.Paint;

public class HSVColor {

	private double hMin;
	private double sMin;
	private double vMin;
	private double hMax;
	private double sMax;
	private double vMax;
	
	public HSVColor(double hMin, double sMin, double vMin, double hMax, double sMax, double vMax) {
		this.hMin = hMin;
		this.sMin = sMin;
		this.vMin = vMin;
		this.hMax = hMax;
		this.sMax = sMax;
		this.vMax = vMax;
	}

	public double gethMin() {
		return hMin;
	}

	public void sethMin(double hMin) {
		this.hMin = hMin;
	}

	public double getsMin() {
		return sMin;
	}

	public void setsMin(double sMin) {
		this.sMin = sMin;
	}

	public double getvMin() {
		return vMin;
	}

	public void setvMin(double vMin) {
		this.vMin = vMin;
	}

	public double gethMax() {
		return hMax;
	}

	public void sethMax(double hMax) {
		this.hMax = hMax;
	}

	public double getsMax() {
		return sMax;
	}

	public void setsMax(double sMax) {
		this.sMax = sMax;
	}

	public double getvMax() {
		return vMax;
	}

	public void setvMax(double vMax) {
		this.vMax = vMax;
	}
	
	
}
