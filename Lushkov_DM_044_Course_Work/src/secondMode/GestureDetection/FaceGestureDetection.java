package secondMode.GestureDetection;

import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public class FaceGestureDetection {
	private int NO = 5;
	private int NO2 = 5;
	static Rect rect;

	// difference of mid points of initial and current frames
	private int difference_mid_x, difference_mid_y;

	// initial mid point of x and y axis
	static int intial_x_point, initial_y_point;

	// position of current frame respect to previous frame
	private int current_frame_right = 0, current_frame_left = 0,
			current_frame_up = 0, current_frame_down = 0;

	// movement of face
	private boolean right = false, left = false, up = false, down = false;

	// flags for left right up down movement
	private boolean left_after_right = false, right_after_left = false,
			up_after_down = false, down_after_up = false;


	MatOfRect faces;
	int frame;

	public void feedData(MatOfRect detectedFaces) {
		this.faces = detectedFaces;
		this.frame++;
	}

	public boolean headRightMove() {
		// calculateMidPoint();
		if (difference_mid_x > -25 && difference_mid_x < 50) {
			if ((frame - current_frame_right) > NO && right) {
				left_after_right = false;
				right = false;
				return true;
			}
		} else if (difference_mid_x >= 50) {
			current_frame_right = frame;
			right = true;
			left_after_right = false;
		} else {
			current_frame_right = frame;
			if ((frame - current_frame_right) < NO2)
				left_after_right = true;
			else
				left_after_right = false;
		}
		return false;
	}

	public boolean headLeftMove() {
		// calculateMidPoint();
		if (difference_mid_x > -50 && difference_mid_x < 25) {
			if ((frame - current_frame_left) > NO && left ) {
				right_after_left = false;
				left = false;
				return true;
			}
		} else if (difference_mid_x <= -50) {
			current_frame_left = frame;
			left = true;
			right_after_left = false;
		} else {
			current_frame_right = frame;
			if ((frame - current_frame_left) < NO2)
				right_after_left = true;
			else
				right_after_left = false;
		}
		return false;
	}

	public boolean headUpMove() {
		// calculateMidPoint();
		if (difference_mid_y > -25 && difference_mid_y <= 50) {
			if ((frame - current_frame_up) > NO && up) {
				down_after_up = false;
				up = false;
				return true;
			}
		} else if (difference_mid_y >= 50) {
			current_frame_up = frame;
			up = true;
			down_after_up = false;
		} else {
			current_frame_right = frame;
			if ((frame - current_frame_up) < NO2)
				down_after_up = true;
			else
				down_after_up = false;
		}
		return false;
	}

	public boolean headDownMove1()
	{
		return false;
	}
	
	public boolean headDownMove() {
		if (difference_mid_y > -50 && difference_mid_y < 25) {
			if ((frame - current_frame_down) > NO && down ) {
				up_after_down = false;
				down = false;
				return true;
			}
		} else if (difference_mid_y <= -50) {
			current_frame_down = frame;
			down = true;
			up_after_down = false;
		} else {
			current_frame_right = frame;
			if ((frame - current_frame_down) < NO2)
				up_after_down = true;
			else
				up_after_down = false; 
		}
		return false;
	}

	public void calculateMidPoint() {
		// width of recognized face and
		// current frame's x axis and y axis mid point
		int width = 0, current_x_point, current_y_point;
		rect = new Rect();

		// detect the biggest face detected
		for (Rect rect1 : faces.toArray()) {
			if (rect1.width > width)
				rect = rect1;
		}

		// for the first frame calculate initial points
		if (frame == 0) {
			intial_x_point = rect.x + rect.width / 2;
			initial_y_point = rect.y + rect.height / 2;
		}
		// if not first then for every 10th frame calculate difference of mid
		// points
		else if ((frame % 2) == 0) {
			current_x_point = rect.x + rect.width / 2; // Set mid point of
														// current x axis of
														// face
			current_y_point = rect.y + rect.height / 2; // Set mid point of
														// current y axis of
														// face

			// difference of mid points
			difference_mid_x = intial_x_point - current_x_point;
			difference_mid_y = initial_y_point - current_y_point;

			// set current points as initial points
			intial_x_point = current_x_point;
			initial_y_point = current_y_point;
		}
	}
}
