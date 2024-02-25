package firstmode.Paint;

import org.opencv.core.Scalar;

public class InitializeColor {
	
	private static final HSVColor ORANGE_COLOR_HSV = new HSVColor(5,0.44,0,18,0.9,1);
	private static final HSVColor BLUE_COLOR_HCV = new HSVColor(78.5,0.73,0,128,1,1);
	
	private static final HSVColor[] hsvColorPool = {ORANGE_COLOR_HSV, BLUE_COLOR_HCV};
	
	private static final Scalar ORANGE_COLOR_BGR = new Scalar(20,118,236);
	private static final Scalar BLUE_COLOR_BGR = new Scalar(126,51,29);
	
	private static final Scalar[] bgrCollorPool = {ORANGE_COLOR_BGR, BLUE_COLOR_BGR};
	
	public static HSVColor[] getColorPoolHSV() {
		return hsvColorPool;
	}
	
	public static Scalar[] getColorPoolBGR() {
		return bgrCollorPool;
	}
}
