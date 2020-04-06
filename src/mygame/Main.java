package mygame;

import com.jme3.app.SimpleApplication;
import mygame.allObjects.level;
import mygame.allObjects.level01;


/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication{

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
  
    @Override
    public void simpleInitApp() {

             stateManager.attach(new level(this));        

//        initKeys();
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setColor("Color", ColorRGBA.Blue);
//
//      
//        DirectionalLight dl = new DirectionalLight();
//        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
//        rootNode.addLight(dl);
//
//        Node zomb = (Node) assetManager.loadModel("Blender/zombie.j3o");
//        zomb.setName("zombie");
//        
//        rootNode.attachChild(zomb);
//        scane = assetManager.loadModel("Scenes/level1.j3o");
//        rootNode.attachChild(scane);
//       mod.setMaterial(mat);
//       zomb.setMaterial(mat);
//     
//        BlenderKey blenderKey = new BlenderKey("Blender/2.4x/animtest.blend");
//        
//        Spatial scene = (Spatial) assetManager.loadModel(blenderKey);
//      
//       
//       Node spp=(Node) rootNode.getChild("Yaku_zombie");
//        /Spatial sp = spp.getChild("nnnnn");
//      
//       control = spp.getControl(AnimControl.class);
//    if(control==null)
//    {
//        System.out.println("mygame.Main.simpleInitApp() nuuuuuuuuuuuulllll");
//        
//    }
//    else
//    {       
//    channel = control.createChannel();
//    channel.setAnim("walking");
//    }   
//        
//        
//
//
//
//
//
//
//        
//        /*Box b = new Box(1, 1, 1);
//        Geometry geom = new Geometry("Box", b);
//*/
//          mat.setColor("Color", ColorRGBA.Blue);
//        /*      geom.setMaterial(mat);
//
//        rootNode.attachChild(geom);
//*/
//   /*
//    DirectionalLight l=new DirectionalLight();
//
//    rootNode.addLight(new DirectionalLight());
//
//rootNode.addLight(new DirectionalLight());
//
//rootNode.addLight(new DirectionalLight());
//
//rootNode.addLight(new DirectionalLight());
//
//rootNode.addLight(new AmbientLight());
//*/
//  
//        
//        Zombie01 z1=new Zombie01(assetManager, this);
//   
//        ColorRGBA rgb = new ColorRGBA(ColorRGBA.Cyan);
//       
//               
//      
//   
//        z1.getModel().setMaterial(mat);
//        rootNode.attachChild(z1.getModel());
//         scane = assetManager.loadModel("Scenes/level1.j3o");
//         rootNode.attachChild(scane);
//        
//        z1.move();
//        
//          MTLLoader loder=new MTLLoader();
//         Material mater=(Material)loder.load(assetInfo);
//     
//        
//        
//        
//        
//       Spatial mod= assetManager.loadModel("Models/knight/knight.j3o");
//        rootNode.attachChild(mod);
//   
//       
//mod.setMaterial(mat);
//
//Node lvl=new Node("lvl");
//              lvl.attachChild(mod);
//rootNode.addLight(new DirectionalLight());
//
//         zomb=new Zombie01(assetManager);
//         rootNode.attachChild(zomb.getNode());
//         zomb.getNode().setMaterial(mat);
//           plan=new plant01(assetManager);
//         rootNode.attachChild(plan.getNode());
//         plan.getNode().setMaterial(mat);
//         plan.getNode().move(3.0f,0.0f, 0.0f);
//
//
//


    }


}
