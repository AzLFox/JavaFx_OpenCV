package secondMode.HeadDetect;

import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import secondMode.configuration.ConfigPropReader;
import secondMode.GestureDetection.FaceGestureDetection;
import secondMode.GestureDetection.EyeGestureDetection;
import converters.MatToBuffConverter;
import secondMode.entityDetection.EyeDetection;
import secondMode.entityDetection.FaceDetection;
import javafx.scene.image.ImageView;

public class HeadDetection {

	int fistcount = 0;

	// Face Detection Method
	public static BufferedImage faceDetector(Mat mainMat, VideoCapture camera, ImageView view) {
		FaceGestureDetection head = new FaceGestureDetection();
		EyeGestureDetection eye = new EyeGestureDetection();
		BufferedImage buffimage = null;															
		if (camera.isOpened())
		{
			try {
				Robot robot = new Robot();
				while (true) {
					if (camera.read(mainMat))
					{
						if (ConfigPropReader.getInstance().getPropValues("face_enabled").equalsIgnoreCase("true")) {
							MatOfRect faceDetections = (new FaceDetection()).faceDetector(mainMat);

							for (Rect rect : faceDetections.toArray()) {
								Imgproc.rectangle(mainMat, new Point(rect.x, rect.y),
										new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
								int mouseX = (int) (rect.x + (rect.width / 2));
								int mouseY = (int) (rect.y + (rect.height / 2));
							//robot.mouseMove(getX(mouseX), gety(mouseY));
							}
							head.feedData(faceDetections);
							head.calculateMidPoint();

							if (ConfigPropReader.getInstance().getPropValues("eye_enabled").equalsIgnoreCase("true")) {
								// Eye detection
								if (faceDetections.toArray().length > 0) {
									MatOfRect eyeRects = (new EyeDetection()).detectEyesFromFace(mainMat,
											faceDetections.toArray()[0]);
									Rect[] eyesArray = eyeRects.toArray();
									for (int j = 0; j < eyesArray.length; j++) {
										Point center1 = new Point(
												faceDetections.toArray()[0].x + eyesArray[j].x
														+ eyesArray[j].width * 0.5,
												faceDetections.toArray()[0].y + eyesArray[j].y
														+ eyesArray[j].height * 0.5);
										int radius = (int) Math
												.round((eyesArray[j].width + eyesArray[j].height) * 0.25);
										Imgproc.circle(mainMat, center1, radius, new Scalar(255, 0, 0), 4, 8, 0);
									}
									eye.feedData(faceDetections.toArray()[0], eyeRects);
									boolean ans = eye.eyeSleep();
									if (ans)
										System.out.println("sleep");
									ans = eye.eyeLeftClosed();
									if (ans)
										System.out.println("left closed");
									ans = eye.eyeRightClosed();
									if (ans)
										System.out.println("right closed");
								}
							}

							
							
							boolean ans = head.headDownMove();
							if (ans)
								System.out.println("DOWN");
							boolean ans1 = head.headRightMove();
							if (ans1)
								System.out.println("Right");
							boolean ans2 = head.headUpMove();
							if (ans2)
								System.out.println("Up");

							ans = head.headLeftMove();
							if (ans)
								System.out.println("LEFT");
						}

					}

					buffimage = MatToBuffConverter.matToBuff(mainMat);
					return buffimage;
				}

			} catch (Exception e) {
				System.out.println("HeadDetect Exception " + e);
			}
		}
		return buffimage;
	}

	static int getX(int x) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int eWidth = (int) (width / 4);

		int c = 4;
		return (width / 2 + c * (x - width / 2));
		// return (2 * x - (width / 2));

		// if(x > width)
		// return (width -width*x/((width/2)+(eWidth/2)));
		// else
		// return ( width*x/((width/2)+(eWidth/2)));
	}

	static int gety(int y) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int) screenSize.getHeight();
		int eHeight = (int) height / 2;

		int c = 4;
		return (height / 2 + c * (y - height / 2));
		// return (2 * y - (height / 2));
		// if(y>height)
		// return (height - height*y/((height/2)+(eHeight/2)));
		// else
		// return ( height*y/((height/2)+(eHeight/2)));
	}
}
