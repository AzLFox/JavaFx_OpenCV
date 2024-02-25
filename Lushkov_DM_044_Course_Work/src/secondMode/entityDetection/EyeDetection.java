package secondMode.entityDetection;

import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

import secondMode.configuration.ConfigPropReader;

public class EyeDetection {
	public static Rect rect;
	
	public MatOfRect detectEyesFromFace(Mat currentImage, Rect rect)
	{
		CascadeClassifier eye_cascade = new CascadeClassifier(ConfigPropReader.getInstance().getPropValues("eye_trainer"));
		MatOfRect eyedetections = new MatOfRect();
		Mat faceROI = currentImage.submat(rect);

		eye_cascade.detectMultiScale(faceROI, eyedetections);
		return eyedetections;
	}
}
