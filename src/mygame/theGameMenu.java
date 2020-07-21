/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

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
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.tools.SizeValue;

/**
 *
 * @author DELL
 */
public class theGameMenu extends AbstractAppState implements ScreenController {
//private Background manu_img;

    private Camera camera;
    private FlyByCamera flyByCamera;
    private Node root;
    private AssetManager assetManager;
    private InputManager inputManager;
    private Timer timer;
    private Node guiNode;
    private SimpleApplication app;
    private NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;
    private ButtonBuilder btn;
    private int level;

    public theGameMenu(SimpleApplication app) {
        camera = app.getCamera();
        flyByCamera = app.getFlyByCamera();
        root = app.getRootNode();
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
        timer = app.getTimer();
        guiNode = app.getGuiNode();

        this.app = app;

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);
        GUI();
    }

    public void StartGame() {
        
        
        app.getStateManager().attach(new level(app,/*level*/100));
        app.getGuiViewPort().removeProcessor(getNiftyDisplay());
        
    }

    private NiftyJmeDisplay getNiftyDisplay() {
        for (int i = 0; i < app.getGuiViewPort().getProcessors().size(); i++) {
            if (app.getGuiViewPort().getProcessors().get(i) instanceof NiftyJmeDisplay) {
                return (NiftyJmeDisplay) app.getGuiViewPort().getProcessors().get(i);
            }
        }
        return null;

    }

    public void cardlist() {

        niftyDisplay = getNiftyDisplay();
        nifty = niftyDisplay.getNifty();
        System.out.println("mygame.allObjects.theGameMenu.cardlist()");
        nifty.addScreen("card list", new ScreenBuilder("Hello Nifty Screen") {
            {
                controller(new DefaultScreenController());
                System.out.println(".cardlist()");
                layer(new LayerBuilder("background") {
                    {
                        childLayoutVertical(); // layer properties, add more...
                        backgroundColor("#0000ff");
                        // add image
                        image(new ImageBuilder() {
                            {
                                childLayoutCenter();
                                filename("photos/cards_img.jpg");
                                height("100%");
                                width("100%");
                            }
                        });
                    }
                });

            }
        }.build(nifty));
        //nifty.getCurrentScreen().endScreen("empty");
        nifty.gotoScreen("card list"); // start the screen
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

        niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        nifty = niftyDisplay.getNifty();

        app.getGuiViewPort().addProcessor(niftyDisplay);

        app.getFlyByCamera().setDragToRotate(true);
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");


        mainMenu();

    }

    private void mainMenu() {
        // <screen>
        nifty.addScreen("Main Screen", new ScreenBuilder("Hello Nifty Screen") {
            {
                controller(new mygame.theGameMenu(app)); // Screen properties

                // <layer>
                layer(new LayerBuilder("background") {
                    {
                        childLayoutVertical(); // layer properties, add more...
                        // backgroundColor("#00ff00");
                        // add image
                        image(new ImageBuilder() {
                            {
                                childLayoutCenter();
                                filename("photos/menu_img1.jpg");
                                height("100%");
                                width("100%");

                                // <panel>
                                panel(new PanelBuilder("Panel02") {
                                    {
                                        childLayoutCenter(); // panel properties, add more...
                                        //    backgroundColor("#88f8");

                                        height("40%");
                                        width("15%");

                                        // GUI elements
                                        control(new ButtonBuilder("Button01", "Start Game") {
                                            {
                                                // alignCenter();
                                                valignTop();
                                                // valignCenter();
                                                height("20%");
                                                width("100%");

                                                interactOnClick("StartGame()");

                                            }
                                        });

                                        // GUI elements
                                        //.. add more GUI elements here
                                        control(new ButtonBuilder("Button02", "Options") {
                                            {
                                                //alignCenter();
                                                //valignTop();
                                                //  backgroundColor(de.lessvoid.nifty.tools.Color.WHITE);
                                                valignCenter();
                                                System.out.println("////////////////");
                                                x(SizeValue.px(2));
                                                y(SizeValue.px(2));
                                                backgroundColor("#ff3CB371");

                                                height("20%");
                                                width("100%");
                                                interactOnClick("cardlist()");

                                            }
                                        });

                                        control(new ButtonBuilder("Button03", "Exit") {
                                            {
                                                //alignCenter();
                                                //valignTop();
                                                //  backgroundColor(de.lessvoid.nifty.tools.Color.WHITE);
                                                valignBottom();

                                                height("20%");
                                                width("100%");
                                                interactOnClick("test()");

                                            }
                                        });

                                    }
                                });

                            }
                        });

                    }
                });

                /* layer(new LayerBuilder("Layer02") {{
                  childLayoutVertical(); // layer properties, add more...
          
panel(new PanelBuilder("panel_up_left") {{
    childLayoutCenter();
                       childLayoutCenter(); // panel properties, add more...
                 childLayoutHorizontal();
    valignCenter();
   
    height("50%");
    width("60%");
    //panel(this);
    // add control
    control(new ButtonBuilder("StartButton", "Start") {{
    
        alignCenter();
       valignCenter();
     
       
       alignLeft();
        alignRight();
        height("10%");
        width("20%");
          interactOnClick("test()");
       
    }});
 
      control(new ButtonBuilder("QuitButton", "Quit") {{
          
           valignCenter();
        alignLeft();
        alignRight();
        alignCenter();
        
        height("10%");
        width("20%");
        interactOnClick("test()");
    }});
     }});*/
 /*panel(new PanelBuilder("panel_up_right") {{
    childLayoutCenter();
    valignCenter();
    backgroundColor("#88f8");
    height("20%");
    width("20%");

    // add control
    control(new ButtonBuilder("QuitButton", "Quit") {{
        alignCenter();
        valignCenter();
        height("100%");
        width("100%");
    }});
}});
 }}); */
                ///////////////////
                /*layer(new LayerBuilder("Layer01") {{

               
                childLayoutVertical(); // layer properties, add more...
                // <panel>
                
                panel(new PanelBuilder("Panel01") {{
                   childLayoutCenter(); // panel properties, add more...
                 childLayoutHorizontal();
                  
                       
                   height("10%");
                        width("10%");
                    
                          
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
                
                panel(new PanelBuilder("Panel02") {{
                   childLayoutCenter(); // panel properties, add more...
                 childLayoutHorizontal();
                  
                       
                   height("200%");
                        width("200%");
                    
                          
                    // GUI elements
                    control(new ButtonBuilder("Button02", "Start Game"){{
                        alignCenter();
                       // valignCenter();
                       
                        height("5%");
                        width("15%");
                        interactOnClick("test()");
                        
                    }});
                    
                    // GUI elements
                    

                    //.. add more GUI elements here

                  control(new ButtonBuilder("Button02", "Start Game2"){{
                        alignCenter();
                     //   valignTop();
                        
                       
                        height("5%");
                        width("15%");
                        interactOnClick("test()");
                        
                    }});
                  
                }});
                
            
                  
              }}); */
            }

            private void panel(PanelBuilder panelBuilder) {

                //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.build(nifty));

        nifty.gotoScreen("Main Screen"); // start the screen

    }

}
