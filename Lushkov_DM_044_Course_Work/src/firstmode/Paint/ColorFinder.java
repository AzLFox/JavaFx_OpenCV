package firstmode.Paint;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ColorFinder {

	private static List<PointDrawing> myPoints = new ArrayList<>();
	
	public static List<PointDrawing> findeColor(Mat mainMat) {
		
		Mat HSVMat = new Mat();
		Mat HSVMatBettwen = new Mat();
		
		mainMat.copyTo(HSVMat);
		mainMat.copyTo(HSVMatBettwen);
		
		Imgproc.cvtColor(mainMat, HSVMat, Imgproc.COLOR_BGR2HSV);
		Imgproc.cvtColor(mainMat, HSVMatBettwen, Imgproc.COLOR_BGR2HSV);
		
		ColorFinder colorFinder = new ColorFinder();
		HSVColor[] hsvColorArray = InitializeColor.getColorPoolHSV();
		Scalar[] bgrColorArray = InitializeColor.getColorPoolBGR();
		int count = 0;
		
		List<PointDrawing> drawPoint = new ArrayList<>();
		
		for (HSVColor color : hsvColorArray) {
			colorFinder.theresholding(HSVMat,color.gethMin(), color.getsMin(), color.getvMin(), color.gethMax(), color.getsMax(), color.getvMax());
			double[] circleLocationArray = getContours(HSVMat);
			Imgproc.circle(mainMat, new Point(circleLocationArray[0],circleLocationArray[1]),10,
					bgrColorArray[count],Imgproc.FILLED);
			HSVMatBettwen.copyTo(HSVMat);
			if (circleLocationArray[0] != 0 || circleLocationArray[1] != 0) {
				drawPoint.add(new PointDrawing(circleLocationArray[0],circleLocationArray[1],count));
			}
			count+=1;
		}
		
		return drawPoint;
	}
	
	public void theresholding(Mat NonTheresholded, double hmin, double smin, double vmin, double hmax, double smax, double vmax)
	{
		double hLow = hmin;
		double sLow = 255 * smin;
		double vLow = 255 * vmin;
		double hHigh = hmax;
		double sHigh = 255 * smax;
		double vHigh = 255 * vmax;

		Mat theresholded = new Mat();
		Mat kernel = new Mat(new Size(1, 1), CvType.CV_8U, new Scalar(255));

		Core.inRange(NonTheresholded, new Scalar(hLow, sLow, vLow, 0), new Scalar(hHigh, sHigh, vHigh, 0), theresholded);
		Imgproc.morphologyEx(theresholded, theresholded, 2, kernel);

		theresholded.copyTo(NonTheresholded);
	}

	public static double[] getContours(Mat HSVMat) {
		double width = 0;
		double x = 0;
		double y = 0;
		List<MatOfPoint> contours = new ArrayList<>();
		List<MatOfPoint> ListForBiggestCoutour = new ArrayList<MatOfPoint>();
		Mat ContourMat = new Mat(HSVMat.rows(), HSVMat.cols(), HSVMat.type());
		Imgproc.findContours(HSVMat, contours, ContourMat,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		MatOfPoint biggestContour = null; 
		
		double smallestArea = 0;
		for(MatOfPoint contour: contours) {
			double area = Imgproc.contourArea(contour);
			if (area > 500) {
				if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), false) > smallestArea) {
					smallestArea = Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), false);
					biggestContour = contour;
				}
			}
		}
		
		if (biggestContour != null) {
			ListForBiggestCoutour.add(biggestContour);
		}
		double[] rectangleSize = {0,0};
		try {
			Rect rectangle = Imgproc.boundingRect(ListForBiggestCoutour.get(0));
			width = rectangle.width;
			x = rectangle.x;
			y = rectangle.y;
			Imgproc.rectangle(HSVMat,new Point(rectangle.x, rectangle.y),
				new Point(rectangle.x + rectangle.width - 1, rectangle.y + rectangle.height - 1),
				new Scalar(0,255,0));
			Imgproc.drawContours(HSVMat, ListForBiggestCoutour, -1, new Scalar(255, 0, 0),3);
			rectangleSize[0] = (width/2)+x;
			rectangleSize[1] = y;
		}catch (Exception e) {
			
		}
		return rectangleSize;
	}
	
	public static void drawOnCanvas(Mat drawingMat) {
		List<PointDrawing> newPoints = findeColor(drawingMat);
		if (!(newPoints.isEmpty())) {
			for (PointDrawing point: newPoints) {
				myPoints.add(point);
			}
		}
		
		Scalar[] bgrColorArray = InitializeColor.getColorPoolBGR();
		
		for(PointDrawing point: myPoints) {
			Imgproc.circle(drawingMat, new Point(point.getX(),point.getY()),10,
					bgrColorArray[point.getColorId()],Imgproc.FILLED);
		}
	}
	
	public static void clearImage() {
		myPoints.clear();
	}
	
}
