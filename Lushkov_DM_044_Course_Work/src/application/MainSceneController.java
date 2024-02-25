package application;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import firstmode.Paint.ColorFinder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import secondMode.HeadDetect.HeadDetection;
import converters.MatToBuffConverter;

public class MainSceneController implements Initializable {

	@FXML
	private AnchorPane scenePane;
	
    @FXML
    private ImageView ImageView;
    
    @FXML
    private Button exitButton, Button1;
    
    @FXML
    private RadioButton radiobuttonMode1, radiobuttonMode2, radiobuttonMode3;

    private  Thread cameraThread;
    private VideoCapture camera;
    
    Stage stage;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		camera = new VideoCapture(0);
		Mat frame = new Mat();
	    MatOfByte buf = new MatOfByte();
		
	    cameraThread = new Thread(() -> {
	    	while (camera.grab()) {
	    		camera.read(frame);
	    		BufferedImage buffimage = MatToBuffConverter.matToBuff(frame);
            	Imgcodecs.imencode(".png", frame, buf);
            	Image Image = convertToFxImage(buffimage);
            	ImageView.setImage(Image);
	    	}
	    });
	    
	    cameraThread.start();
	}
	
	@FXML
	public void login(ActionEvent event) {
			cameraThread.stop();
			radiobuttonMode1.setDisable(false);
			radiobuttonMode2.setDisable(false);
			camera = new VideoCapture(0);
			Mat frame = new Mat();
		    MatOfByte buf = new MatOfByte();
			
		    cameraThread = new Thread(() -> {
		    	while (camera.grab()) {
		    		camera.read(frame);
		    		BufferedImage buffimage = MatToBuffConverter.matToBuff(frame);
	            	Imgcodecs.imencode(".png", frame, buf);
	            	Image Image = convertToFxImage(buffimage);
	            	ImageView.setImage(Image);
		    	}
		    });
		    
		    cameraThread.start();
			
	}
	
	@FXML
	public void logout(ActionEvent event) {
		radiobuttonMode1.setSelected(false);
		radiobuttonMode2.setSelected(false);
		radiobuttonMode1.setDisable(true);
		radiobuttonMode2.setDisable(true);
		cameraThread.stop();
		camera.release();
	}
	
	@FXML
	public void clearImage(ActionEvent event) {
		ColorFinder.clearImage();
	}
	
	public void getMode(ActionEvent event) {
		if (radiobuttonMode1.isSelected()) {
			cameraThread.stop();
			Mat frame = new Mat();
		    MatOfByte buf = new MatOfByte();
			cameraThread = new Thread(() -> {
		    	while (camera.grab()) {
		    		camera.read(frame);
		    		ColorFinder.drawOnCanvas(frame);
		    		BufferedImage buffimage = MatToBuffConverter.matToBuff(frame);
	            	Imgcodecs.imencode(".png", frame, buf);
	            	Image Image = convertToFxImage(buffimage);
	            	ImageView.setImage(Image);
		    	}
			});
			cameraThread.start();
		}
		
		if (radiobuttonMode2.isSelected()) {
			cameraThread.stop();
			Mat frame = new Mat();
		    MatOfByte buf = new MatOfByte();
		    cameraThread = new Thread(() -> {
		    	while (camera.grab()) {
		    		camera.read(frame);
		    		BufferedImage buffimage = HeadDetection.faceDetector(frame, camera, ImageView);
	            	Imgcodecs.imencode(".png", frame, buf);
	            	Image Image = convertToFxImage(buffimage);
	            	ImageView.setImage(Image);
		    	}
			});
			cameraThread.start();
		}
	}
	
	private static Image convertToFxImage(BufferedImage image) {
	    WritableImage wr = null;
	    if (image != null) {
	        wr = new WritableImage(image.getWidth(), image.getHeight());
	        PixelWriter pw = wr.getPixelWriter();
	        for (int x = 0; x < image.getWidth(); x++) {
	            for (int y = 0; y < image.getHeight(); y++) {
	                pw.setArgb(x, y, image.getRGB(x, y));
	            }
	        }
	    }

	    return new ImageView(wr).getImage();
	}

}

