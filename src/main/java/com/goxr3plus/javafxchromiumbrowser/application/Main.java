/*
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>. Also(warning!): 1)You are not allowed to sell this product to third party. 2)You can't change license and made it
 * like you are the owner,author etc. 3)All redistributions of source code files must contain all copyright notices that are currently in this file,
 * and this list of conditions without modification.
 */

package main.java.com.goxr3plus.javafxchromiumbrowser.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.java.com.goxr3plus.javafxchromiumbrowser.chromium.WebBrowserController;
import main.java.com.goxr3plus.javafxchromiumbrowser.tools.Util;

/**
 * From here you start the application
 * 
 * @author GOXR3PLUS STUDIO ( your bro! )
 *
 */
public class Main extends Application {
	
	private WebBrowserController webBrowserMaster;
	
	/*
	 * (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		
		//WebBrowser
		webBrowserMaster = new WebBrowserController(primaryStage);
		
		//Root
		BorderPane root = new BorderPane();
		root.setCenter(webBrowserMaster);
		
		//Scene
		Scene scene = new Scene(root, getVisualScreenWidth() / 1.2, getVisualScreenHeight() / 1.2);
		
		//Prepare the Stage
		primaryStage.setTitle("JavaFX Chromium Browser - Made by GOXR3PLUS Studio");
		//primaryStage.getIcons().add(InfoTool.getImageFromResourcesFolder("logo.png"))
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(cl -> terminateBrowser(0));
		primaryStage.show();
		
	}
	
	/**
	 * Gets the visual screen width.
	 *
	 * @return The screen <b>Width</b> based on the <b>visual bounds</b> of the Screen.These bounds account for objects in the native windowing system
	 *         such as task bars and menu bars. These bounds are contained by Screen.bounds.
	 */
	public static double getVisualScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}
	
	/**
	 * Gets the visual screen height.
	 *
	 * @return The screen <b>Height</b> based on the <b>visual bounds</b> of the Screen.These bounds account for objects in the native windowing system
	 *         such as task bars and menu bars. These bounds are contained by Screen.bounds.
	 */
	public static double getVisualScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}
	
	/**
	 * Main Method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		launch(args);
	}
	
	/**
	 * Use this code to terminate XR3Player
	 * 
	 * @param code
	 */
	public void terminateBrowser(int code) {
		
		System.out.println("Dis All->" + Util.getOS());
		switch (Util.getOS()) {
			case WINDOWS:
				new Thread(() -> {
					//Disposing all Browsers...
					webBrowserMaster.disposeAllBrowsers();
					System.exit(code);
				}).start();
				break;
			case LINUX:
			case MAC:
				Platform.runLater(() -> {
					//Disposing all Browsers...
					webBrowserMaster.disposeAllBrowsers();
					System.exit(code);
				});
				break;
			default:
				System.out.println("Can't dispose browser instances!!!");
				break;
		}
		
	}
}
