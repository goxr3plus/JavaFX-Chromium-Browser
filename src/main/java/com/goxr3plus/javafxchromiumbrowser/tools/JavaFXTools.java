/**
 * 
 */
package main.java.com.goxr3plus.javafxchromiumbrowser.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Labeled;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class has some functions that are not there by default in JavaFX 8
 * 
 * @author GOXR3PLUS
 *
 */
public final class JavaFXTools {
	
	private JavaFXTools() {
	}
	
	/**
	 * Selects the Toogle with the given text from the toggle group or else selects nothing
	 * 
	 * @param toggleGroup
	 */
	public static void selectToogleWithText(ToggleGroup toggleGroup , String text) {
		toggleGroup.getToggles().forEach(toggle -> {
			if ( ( (Labeled) toggle ).getText().equals(text)) {
				toggle.setSelected(true);
			}
		});
	}
	
	/**
	 * Returns the Index of the Selected Toggle inside the ToggleGroup (counting from 0)
	 * 
	 * @param g
	 * @return The index of the Selected Toggle
	 */
	public static int getIndexOfSelectedToggle(ToggleGroup g) {
		return g.getToggles().indexOf(g.getSelectedToggle());
	}
	
	/**
	 * Selects the Toggle in position Index inside the toggle group (counting from 0 )
	 * 
	 * @param g
	 * @param index
	 */
	public static void selectToggleOnIndex(ToggleGroup g , int index) {
		g.selectToggle(g.getToggles().get(index));
	}
	
	/**
	 * Searches for any Image that contains the given title -> example ["background"] inside the given folder
	 * 
	 * @return The absolute path of the image file or null if not exists
	 */
	public static String getAbsoluteImagePath(String title , String folderToSearch) {
		String absolutePath = null;
		
		//If Folder not exists return null
		File searchingFolder = new File(folderToSearch);
		if (!searchingFolder.exists())
			return absolutePath;
		
		//Try to find the image
		try (Stream<Path> paths = Files.walk(Paths.get(searchingFolder.getPath()), 1)) {
			absolutePath = paths.filter(path -> {
				File file = path.toFile();
				return !file.isDirectory() && title.equals(InfoTool.getFileTitle(file.getAbsolutePath())) && InfoTool.isImageSupported(file.getAbsolutePath());
			}).findFirst().map(path -> path.toAbsolutePath().toString()).orElse(null);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return absolutePath;
	}
	
	/**
	 * Check if any image with that title exists -> for example ["background"] inside the Folder given , i don't have the extension
	 * 
	 * @param title
	 * @param folderToSearch
	 *            Absolute path of the Folder to Search
	 * @return
	 */
	public static Image findAnyImageWithTitle(String title , String folderToSearch) {
		//Check if any Image with that Title exists inside the given folder
		String imageAbsolutePath = getAbsoluteImagePath(title, folderToSearch);
		return imageAbsolutePath == null ? null : new Image(new File(imageAbsolutePath).toURI() + "");
	}
	
	/**
	 * Deletes any image which has that title , for example ["background"] searching on the given Folder
	 * 
	 * @param title
	 * @param folderToSearch
	 *            Absolute path of the Folder to Search
	 */
	public static void deleteAnyImageWithTitle(String title , String folderToSearch) {
		
		//If Folder not exists return
		File searchingFolder = new File(folderToSearch);
		if (!searchingFolder.exists())
			return;
		
		//Find and delete it
		try (Stream<Path> paths = Files.walk(Paths.get(searchingFolder.getPath()), 1)) {
			paths.forEach(path -> {
				File file = path.toFile();
				if (!file.isDirectory() && InfoTool.getFileTitle(file.getAbsolutePath()).equals(title) && InfoTool.isImageSupported(file.getAbsolutePath()))
					file.delete(); //-> to be fixed
			});
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		//---Something Experimental--------
		//		return title.equals(InfoTool.getFileTitle(file.getAbsolutePath())) && InfoTool.isImage(file.getAbsolutePath())
		//				&& !file.isDirectory();
		//		    }).findFirst().map(path->path.toFile().delete()).orElse(false);
		
	}
	

	/**
	 * Return the hex web string from the given color for example (#302015)
	 * 
	 * @param color
	 *            The given color
	 * @return The hex web string from the given color for example (#302015)
	 */
	public static String colorToWebColor(Color color) {
		return String.format("#%02X%02X%02X", (int) ( color.getRed() * 255 ), (int) ( color.getGreen() * 255 ), (int) ( color.getBlue() * 255 ));
		
	}
	
	//-----------------------------------------------------------------------------------------------------------
	
	/**
	 * Creates an Alert with the given parameters
	 * 
	 * @param title
	 * @param headerText
	 * @param contentText
	 * @param alertType
	 * @param stageStyle
	 * @param owner
	 * @param graphic
	 * @return The created Alert based on the given parameters
	 */
	public static Alert createAlert(String title , String headerText , String contentText , AlertType alertType , StageStyle stageStyle , Stage owner , ImageView graphic) {
		
		// Show Alert
		Alert alert = new Alert(alertType);
		if (title != null)
			alert.setTitle(title);
		if (headerText != null)
			alert.setHeaderText(headerText);
		if (contentText != null)
			alert.setContentText(contentText);
		if (stageStyle != null)
			alert.initStyle(stageStyle);
		if (owner != null)
			alert.initOwner(owner);
		if (graphic != null)
			alert.setGraphic(graphic);
		
		//Make sure alert is not outside the screen so app becomes unresponsible
		alert.heightProperty().addListener(l -> {
			
			// Width and Height of the Alert
			double alertWidth = alert.getWidth();
			double alertHeight = alert.getHeight();
			double alertScreenX = alert.getX();
			double alertScreenY = alert.getY();
			
			// Check if Alert goes out of the Screen on X Axis
			if (alertScreenX + alertWidth > InfoTool.getVisualScreenWidth())
				alertScreenX = (int) ( InfoTool.getVisualScreenWidth() - alertWidth );
			else if (alertScreenX < 0)
				alertScreenX = 0;
			
			// Check if Alert goes out of the Screen on Y AXIS
			if (alertScreenY + alertHeight > InfoTool.getVisualScreenHeight())
				alertScreenY = (int) ( InfoTool.getVisualScreenHeight() - alertHeight );
			else if (alertScreenY < 0)
				alertScreenY = 0;
			
			// Set the X and Y of the Alert
			alert.setX(alertScreenX);
			alert.setY(alertScreenY);
		});
		
		return alert;
	}
	
	//-----------------------------------------------------------------------------------------------------------
	
	/**
	 * Use this method to retrieve an ImageView from the resources of the application.
	 *
	 * @param imageName
	 *            the image name
	 * @return Returns an ImageView using method getImageFromResourcesFolder(String imageName);
	 */
	public static ImageView getImageViewFromResourcesFolder(String imageName , double width , double height) {
		ImageView imageView = new ImageView(InfoTool.getImageFromResourcesFolder(imageName));
		if (width == -1 || height == -1 || width == 0 || height == 0)
			return imageView;
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
		return imageView;
	}
	
	/**
	 * Returns an ImageView containing the given image fitting on the given size
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public static ImageView getImageView(Image image , double width , double height) {
		ImageView imageView = new ImageView(image);
		if (width == -1 || height == -1 || width == 0 || height == 0)
			return imageView;
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
		return imageView;
	}
	
}
