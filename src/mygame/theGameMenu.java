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
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 *
 * @author DELL
 */
public class theGameMenu extends AbstractAppState implements ScreenController {

    private AudioNode SoundNode;
    private SimpleApplication app;
    private Nifty nifty;
    private NiftyJmeDisplay niftyDisplay;

    public theGameMenu(SimpleApplication app) {

        this.app = app;
        System.out.println("YOua add manu");

    }

    //////
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        GUI();
    }

    int GetLevel() {
       
        Scanner input;
        try {
            input = new Scanner(new FileInputStream("assets/files/level.txt"));
        } catch (Exception e) {
            return 1;
        }
        return input.next().charAt(0);

    }


    public void StartGame() {

       
        ((AudioNode) app.getRootNode().getChild("sound")).stop();
        app.getRootNode().detachChildNamed("sound");
        app.getStateManager().attach(new level(app, GetLevel()));
        app.getGuiViewPort().removeProcessor(getNiftyDisplay());
    }

    public void endgame() {
        System.exit(0);
    }

    private NiftyJmeDisplay getNiftyDisplay() {
        for (int i = 0; i < app.getGuiViewPort().getProcessors().size(); i++) {
            if (app.getGuiViewPort().getProcessors().get(i) instanceof NiftyJmeDisplay) {
                return (NiftyJmeDisplay) app.getGuiViewPort().getProcessors().get(i);
            }
        }
        return null;

    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void GUI() {

        niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        nifty = niftyDisplay.getNifty();

        app.getGuiViewPort().addProcessor(niftyDisplay);

        app.getFlyByCamera().setDragToRotate(true);
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        SoundNode = new AudioNode(app.getAssetManager(), "Sounds/Main Menu.ogg", AudioData.DataType.Stream);
        SoundNode.setName("sound");
        SoundNode.setLooping(true);  // activate continuous playing
        SoundNode.setPositional(false);
        SoundNode.setVolume(3);

        app.getRootNode().attachChild(SoundNode);
        SoundNode.play();

        mainMenu();

    }

    private void mainMenu() {

        nifty.addScreen("Main Screen", new ScreenBuilder("Hello Nifty Screen") {
            {
                controller(new mygame.theGameMenu(app));

                layer(new LayerBuilder("background") {
                    {
                        childLayoutVertical();
                        image(new ImageBuilder() {
                            {
                                childLayoutCenter();
                                filename("photos/menu_img1.png");
                                height("100%");
                                width("100%");

                                panel(new PanelBuilder("Panel02") {
                                    {
                                        childLayoutCenter();
                                        alignCenter();
                                        height("40%");
                                        width("60%");
                                        
                                        control(new ButtonBuilder("Button02", "") {
                                            {                                              
                                                valignTop();
                                                height("0%");
                                                width("0%");
                                            }
                                        });
                                         
                                        control(new ButtonBuilder("Button01", "START GAME") {
                                            {
                                                valignCenter();
                                                backgroundColor("#CC5C5C");
                                                valignTop();
                                                height("30%");
                                                width("30%");
                                                interactOnClick("StartGame()");

                                            }
                                        });
                                        control(new ButtonBuilder("Button02", "EXIT") {
                                            {

                                                valignCenter();
                                                backgroundColor("#088DA5");
                                                height("30%");
                                                width("30%");
                                                interactOnClick("endgame()");

                                            }
                                        });

                                    }
                                });

                            }
                        });

                    }
                });

            }

        }.build(nifty));

        nifty.gotoScreen("Main Screen"); // start the screen

    }

    

}
