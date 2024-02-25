package secondMode.entityDetection;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

import secondMode.configuration.ConfigPropReader;

public class FaceDetection {
	int c = 0;

	// Face Detection Method
	public MatOfRect faceDetector(Mat currentImage) {

		CascadeClassifier FaceDetection = new CascadeClassifier(
				ConfigPropReader.getInstance().getPropValues("face_trainer"));
		MatOfRect faceDetections = new MatOfRect();

		// This Method detects faces
		FaceDetection.detectMultiScale(currentImage, faceDetections);

		Rect[] faces = faceDetections.toArray();

		if (ConfigPropReader.getInstance().getPropValues("multi_face_detection").equalsIgnoreCase("true")) {
			int maxIndex = 0;
			if (faces.length > 1) {
				for (int i = 1; i < faces.length; i++) {
					if ((faces[i].height + faces[i].width) > (faces[maxIndex].height + faces[maxIndex].width)) {
						maxIndex = i;
					}
				}
				faceDetections = null;
				return new MatOfRect(faces[maxIndex]);
			}
		}

		List<Rect> actualfaces = new ArrayList<Rect>();
		for (int i = 0; i < faces.length; i++) {
			if ((faces[i].height + faces[i].width) > Integer
					.parseInt(ConfigPropReader.getInstance().getPropValues("min_face_size"))) {
				actualfaces.add(faces[i]);
			}
		}

		faceDetections = new MatOfRect();
		faceDetections.fromList(actualfaces);
		return faceDetections;
	}
}
