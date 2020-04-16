/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.allObjects;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.Timer;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author DELL
 */
public class theGameMenu extends AbstractAppState implements ScreenController{

    private Camera camera;
    private FlyByCamera flyByCamera;
    private Node root;
    private AssetManager assetManager;
    private InputManager inputManager;
    private Timer timer;
    private Node guiNode;
    private SimpleApplication app;
    private NiftyJmeDisplay niftyDisplay ;
    private Nifty nifty;
    public theGameMenu (SimpleApplication app) {
        camera = app.getCamera();
        flyByCamera = app.getFlyByCamera();
        root = app.getRootNode();
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
        timer = app.getTimer();
      guiNode=app.getGuiNode();
      
      this.app = app;

    }

    //////
    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);
        GUI();
    } 
    
      
    
    
    public  void test()
      {
       app.getStateManager().attach( new level(app));
          System.out.println("mygame.allObjects.theGameMenu.test()");
      }
      
      
      
      
      
      
    @Override
    public void bind(Nifty nifty, Screen screen) {
     System.out.println("You bind the screen");  
    }

    @Override
    public void onStartScreen() {
  System.out.println("You start the screen"); 
    }

    @Override
    public void onEndScreen() {
    System.out.println("You end the screen`");
    }
    
    
      public void GUI() {
          
    niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager,app.getInputManager(),app.getAudioRenderer(),app.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
        app.getGuiViewPort().addProcessor(niftyDisplay);
        app.getFlyByCamera().setDragToRotate(true);
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");



             mainMenu();

        nifty.gotoScreen("Main Screen"); // start the screen
   }
      private void mainMenu()
      {
          // <screen>
        nifty.addScreen("Main Screen", new ScreenBuilder("Hello Nifty Screen"){{
            controller(new mygame.allObjects.theGameMenu(app)); // Screen properties

            // <layer>
            layer(new LayerBuilder("Layer01") {{
                childLayoutVertical(); // layer properties, add more...

                // <panel>
                panel(new PanelBuilder("Panel01") {{
                 //  childLayoutCenter(); // panel properties, add more...
                 childLayoutHorizontal();
                  
                       
                   height("100%");
                        width("100%");
                    
                    // GUI elements
                    control(new ButtonBuilder("Button01", "Start Game"){{
                        alignCenter();
                       // valignCenter();
                       
                        height("5%");
                        width("15%");
                        interactOnClick("test()");
                        
                    }});
                    
                    
                    // GUI elements
                    

                    //.. add more GUI elements here

                  control(new ButtonBuilder("Button01", "Start Game2"){{
                        alignCenter();
                     //   valignTop();
                        
                       
                        height("5%");
                        width("15%");
                        interactOnClick("test()");
                        
                    }});
                }});
                  
              }});
          }}.build(nifty));

          
          
          
      }

    

}
