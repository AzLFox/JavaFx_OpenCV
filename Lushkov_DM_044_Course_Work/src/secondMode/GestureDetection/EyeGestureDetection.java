package secondMode.GestureDetection;

import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;

public class EyeGestureDetection {
	private int leftFrame, rightFrame;
	private int iframe; // initial frame no when eye is closed
	private boolean flag = true, leftFlag = true, rightFlag = true; 

	private MatOfRect eyes;
	private Rect face;
	private static int frame = 0;



	public void feedData(Rect face, MatOfRect detectedEyes) {
		this.face = face;
		this.eyes = detectedEyes;
		frame++;
	}
	
	// checks left eye is closed or not
	public boolean eyeLeftClosed() {
		// number of eyes detected
		Rect[] eyesArray = eyes.toArray();

		for (int j = 0; j < eyesArray.length; j++) {
			// center of the eye
			Point center1 = new Point(face.x + eyesArray[j].x
					+ eyesArray[j].width * 0.5, face.y + eyesArray[j].y
					+ eyesArray[j].height * 0.5);

			// checking for right eye open
			if (center1.x < FaceGestureDetection.intial_x_point
					&& eyesArray.length == 1) {
				if (leftFlag) {
					leftFrame = frame;
					leftFlag = false;
				} else {
					if ((frame - leftFrame) > 3) {
						leftFlag = true;
						return true;
					}
				}
			} else {
				leftFlag = true;
			}
		}
		return false;
	}

	// checks left eye is closed or not
	public boolean eyeRightClosed() {
		// number of eyes detected
		Rect[] eyesArray = eyes.toArray();

		for (int j = 0; j < eyesArray.length; j++) {
			// center of the eye
			Point center1 = new Point(face.x + eyesArray[j].x
					+ eyesArray[j].width * 0.5, face.y + eyesArray[j].y
					+ eyesArray[j].height * 0.5);

			// checking for left eye open
			if (center1.x > FaceGestureDetection.intial_x_point
					&& eyesArray.length == 1) {
				if (rightFlag) {
					rightFrame = frame;
					rightFlag = false;
				} else {
					if ((frame - rightFrame) > 3) {
						rightFlag = true;
						return true;
					}
				}
			} else {
				rightFlag = true;
			}
		}
		return false;
	}

	public boolean eyeSleep() {
		if (eyes.empty()) // if eyes are not detected they are closed
		{
			if (flag) // if it is called first time
			{
				flag = false;
				iframe = frame; // set the initial frame no
			} else {
				// if difference of current frame and intial frame > 25 then eye
				// is closed
				if ((frame - iframe) > 5) {
					System.out.println("sleep");
					// reset the variables and return true
					flag = true;
					return true;
				}
			}
		} else // if eyes are detected so they are open
		{
			iframe = frame; // reset the iframe
			flag = true; // reset flag
		}
		return false;
	}
}
