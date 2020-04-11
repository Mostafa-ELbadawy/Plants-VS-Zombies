/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.allObjects;

import addetions.PhysicsTestHelper;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.SkeletonDebugger;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.util.LinkedList;
import com.jme3.system.Timer;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;
import javax.xml.stream.XMLStreamConstants;
import org.lwjgl.Sys;

/**
 *
 * @author DELL
 */
public class level01 extends AbstractAppState implements AnimEventListener, PhysicsCollisionListener {

    
    private final SimpleApplication app;
    private final Camera camera;
    private final FlyByCamera flyByCamera;
    private final Node root;
    private final Node lvl = new Node("level1");
    private final AssetManager assetManager;
    private final InputManager inputManager;
    private BulletAppState bulletAppState;
    
    private Spatial scane;
    private LinkedList<Zombie> zomb;
    private LinkedList<plant> plan;
 
    private HashMap<Geometry,Bullet> hashing;
    private HashMap<Node,Zombie> hashingzombie;
   
    private final Timer timer;
    
    private Geometry mark;

    float right = 0, up = 0;
    private boolean mov = false;

    public level01(SimpleApplication app) {
        camera = app.getCamera();
        flyByCamera = app.getFlyByCamera();
        root = app.getRootNode();
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
        this.app=app;
        timer=app.getTimer();
          
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        initKeys();
        inivar();
        initMark();
        root.attachChild(lvl);
        scane = assetManager.loadModel("Scenes/level1.j3o");
        lvl.attachChild(scane);

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        addplant(0, 1, Vector3f.ZERO);
        addzombie(0, 1, new Vector3f(9, 0, 0));
        /////////////////////////////////////////////////////////////////////////////////
        
      
//////////////////////////////////////////////////////////////////////////////////////
        bulletAppState.setDebugEnabled(true);

        PhysicsTestHelper.createBallShooter(app, root, bulletAppState.getPhysicsSpace());
        PhysicsTestHelper.createPhysicsTestWorld(root, assetManager, bulletAppState.getPhysicsSpace());
        bulletAppState.getPhysicsSpace().setGravity(Vector3f.ZERO);
               
        initFloor();
        
        
        bulletAppState.getPhysicsSpace().addCollisionListener(this);

        //   System.out.println( zomb.getLast().getNode().getControl(RigidBodyControl.class).getCollisionShape().getScale().toString());
      

//Bullet bullet=new Bullet(assetManager.loadMaterial("Common/MatDefs/Misc/Unshaded.j3md"));
        //  plan.getLast().getNode().addControl(new KinematicRagdollControl());
        ///////////////////////////////////////////////////////////////////////////////////
        flyByCamera.setMoveSpeed(30f);
        flyByCamera.setZoomSpeed(30f);

        /*    camera.setLocation(new Vector3f(0, 10, 20));
        camera.setAxes(camera.getLeft(), new Vector3f(5.2E-8f,0.7f,-0.44f), camera.getDirection());
         */
          camera.setLocation(new Vector3f(10.465846f, 25.21445f, 5.6196184f));
        camera.setRotation(new Quaternion(0.0196439f, 0.843758f, -0.53620327f, 0.01313919f));

        // camera.setRotation(new Quaternion(0.014724265f, 0.83622825f, -0.54772323f, 0.022468943f));
        
//camera.setRotation(new Quaternion((float) Math.PI/2,(float) Math.PI/2, (float) Math.PI/2, 0.022468943f));

           flyByCamera.setEnabled(false);
        // System.out.println(camera.getUp().toString()+" dirsc "+camera.getDirection().toString());
    }

    private void inivar() {
        zomb = new LinkedList<>();
        plan = new LinkedList<>();
        hashing=new HashMap<>();
        hashingzombie=new HashMap<>();
        
    }

    @Override
    public void update(float tpf) {

 //plan.getLast().attack(timer.getTimeInSeconds());

        if (right != 0 || up != 0) {

            
            zomb.getLast().phyControl.setEnabled(false);
        
            zomb.getLast().getNode().move(right * tpf, 0, up * tpf);
            zomb.getLast().phyControl.setEnabled(true);
        }

        if (mov) {
        }
        if(zomb.size()==1)
       // zomb.getLast().setstatus(tpf,plan, timer.getTimeInSeconds(), bulletAppState.getPhysicsSpace() );
        if(plan.size()==1)
        plan.getLast().setstatus(tpf, timer.getTimeInSeconds(), bulletAppState.getPhysicsSpace(), hashing);

        
        
        
        //    zomb.getLast().getNode().getChild("zombie").move(0,0,tpf);
//         System.out.println(plan.toString()+" "+zomb.toString());

    }

    @Override
    public void cleanup() {
        root.detachChild(lvl);
        super.cleanup();
    }

     private final AnalogListener analogListener=new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
     
        
        }
    };
     
     
     
    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Walk") && keyPressed) {

                mov = true;
                System.out.println(".onAction()");
            } else if (name.equals("Walk") && !keyPressed) {

                mov = false;
            } else if (name.equals("attack") && keyPressed) {
              //  zomb.getLast().attack();
               // plan.getLast().attack(timer.getTimeInSeconds());

            } else if (name.equals("P") && !keyPressed) {


                
                
                
                
                
                
                 Vector3f origin = camera.getWorldCoordinates(inputManager.getCursorPosition(), 0f);
        Vector3f direction = camera.getWorldCoordinates(inputManager.getCursorPosition(), 1f);
        direction.subtractLocal(origin).normalizeLocal();

        // 2. Aim the ray from cam loc to cam direction.        
        Ray ray=new Ray();
        ray.setOrigin(origin);
        ray.setDirection(direction);
        CollisionResults results = new CollisionResults();
        Vector3f vv=new Vector3f();
        // 3. Collect intersections between Ray and Shootables in results list.
        lvl.collideWith(ray, results);
                System.out.println("size= "+results.size() );
        // 5. Use the results (we mark the hit object)
        if (results.size() > 0) {
           for (int i = 0; i < results.size(); i++) {
            // For each hit, we know distance, impact point, name of geometry.

            String hitName = results.getCollision(i).getGeometry().getName();
               System.out.println("name "+ i +" "+ hitName);
            float dist = results.getCollision(i).getDistance();
            
  //          System.out.println("i= " + i + " name= " + hitName + " dist= " + dist);

            if (hitName.equals("Floor")) {
              vv=    results.getCollision(i).getContactPoint();
                break;
            }

        }


                
                
        }    
                
                
                
                
                mark.setLocalTranslation(vv);
                System.out.println(vv.toString());

                
                
                
                
                
                
                
                
                
                
                
                
                
                
                //System.out.println( inputManager.getCursorPosition().toString() );
            }
            if (name.equals("Shoot") && !keyPressed) {
                // 1. Reset results list.
                CollisionResults results = new CollisionResults();
                // 2. Aim the ray from cam loc to cam direction.
                
                
                ////////////////////////////////////////////////////////////////////////////////////
               
                Vector2f click2d = inputManager.getCursorPosition();
                
                Vector3f pos= new Vector3f(-click2d.x, 10, click2d.y), dir=new Vector3f(0f, -1f, 0f);
            //    Ray ray = new Ray(pos, dir);
                Ray ray = new Ray(camera.getLocation(), camera.getDirection());
                
                
                
                ////////////////////////////////////////////////////////////////////////////////////
                
                // 3. Collect intersections between Ray and Shootables in results list.
                lvl.collideWith(ray, results);
                // 4. Print the results
                System.out.println("----- Collisions? " + results.size() + "-----");
                for (int i = 0; i < results.size(); i++) {
                    // For each hit, we know distance, impact point, name of geometry.
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    String hit = results.getCollision(i).getGeometry().getName();
                    System.out.println("* Collision #" + i);
                    System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
                }
                // 5. Use the results (we mark the hit object)
                if (results.size() > 0) {
                    // The closest collision point is what was truly hit:
                    CollisionResult closest = results.getClosestCollision();
                    // Let's interact - we mark the hit with a red dot.
                    mark.setLocalTranslation(closest.getContactPoint());
                    root.attachChild(mark);
                } else {
                    // No hits? Then remove the red mark.
                    root.detachChild(mark);
                }
            } else if (name.equals("up")) {
                if (keyPressed) {
                    up = -3;
                } else {
                    up = 0;
                }

            } else if (name.equals("down")) {
                if (keyPressed) {
                    up = 3;
                } else {
                    up = 0;
                }

            } else if (name.equals("R")) {

                if (keyPressed) {
                    right = 3;
                } else {
                    right = 0;
                }
            } else if (name.equals("L")) {
                if (keyPressed) {
                    right = -3;
                } else {
                    right = 0;
                }
            } else if (name.equals("print") && !keyPressed) {

                System.out.println("postion" + zomb.getLast().getNode().getLocalTranslation().toString());
            }

        }
    };

    private void initKeys() {
        inputManager.addMapping("Walk", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "Walk");

        inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addListener(actionListener, "up");

        inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addListener(actionListener, "down");

        inputManager.addMapping("R", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(actionListener, "R");

        inputManager.addMapping("L", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addListener(actionListener, "L");

        inputManager.addMapping("print", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "print");

        inputManager.addMapping("attack", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "attack");

        inputManager.addMapping("P", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "P");

        inputManager.addMapping("mousexP", new MouseAxisTrigger(MouseInput.AXIS_X,true));
        inputManager.addListener(analogListener, "mousexP");
        inputManager.addMapping("mousexN", new MouseAxisTrigger(MouseInput.AXIS_X,false));
        inputManager.addListener(analogListener, "mousexN");
        inputManager.addMapping("mouseyP", new MouseAxisTrigger(MouseInput.AXIS_Y,true));
        inputManager.addListener(analogListener, "mouseyP");
        inputManager.addMapping("mouseyN", new MouseAxisTrigger(MouseInput.AXIS_Y,false));
        inputManager.addListener(analogListener, "mouseyN");
        
        
        inputManager.addMapping("Shoot",
                new KeyTrigger(KeyInput.KEY_Z));// trigger 1: spacebar
        inputManager.addListener(actionListener, "Shoot");

    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("attacking")) {
            channel.setAnim("idel");
            channel.setLoopMode(LoopMode.Loop);
            channel.setSpeed(1f);
        }
    }
    

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        // unused

    }
    
    public void addzombie(int row, int typ, Vector3f v) {
        if (typ == 1) {

            zomb.add(new Zombie01(assetManager));
            zomb.getLast().setRow(row);
            lvl.attachChild(zomb.getLast().getNode());
            bulletAppState.getPhysicsSpace().addAll(zomb.getLast().getNode());
            zomb.getLast().phyControl.setEnabled(false);
           zomb.getLast().getNode().rotate( (float) Math.PI / 2,-(float) Math.PI / 2,0);
            zomb.getLast().getNode().setLocalTranslation(v);

            hashingzombie.put( zomb.getLast().getNode(), zomb.getLast());
            zomb.getLast().phyControl.setEnabled(true);

        }

    }

    public void addplant(int row, int typ, Vector3f v) {
        if (typ == 1) {

            plan.add(new plant01(assetManager));
            plan.getLast().setRow(row);
            lvl.attachChild(plan.getLast().getNode());
            
             bulletAppState.getPhysicsSpace().addAll(plan.getLast().getNode());
            plan.getLast().phyControl.setEnabled(false);
        //    plan.getLast().getNode().rotate( 0,(float) Math.PI / 2,0);
            plan.getLast().getNode().setLocalTranslation(v);

            plan.getLast().phyControl.setEnabled(true);
            
            

        }

    }

    @Override
    public void collision(PhysicsCollisionEvent event) {

        
        
           //         System.out.println(event.getNodeA().getName()+" hit "+event.getNodeB().getName());
        
        if (zomb.getLast().getName().equals(event.getNodeA().getName()) || zomb.getLast().getName().equals(event.getNodeB().getName())) {
            if ("bullet".equals(event.getNodeA().getName()) || "bullet".equals(event.getNodeB().getName())) 
            {
                
                 if("bullet".equals(event.getNodeA().getName()))
                 {
                     
                     Bullet B=hashing.get(event.getNodeA());
                     Zombie z=hashingzombie.get(event.getNodeB());
                     z.damage(B.getPower());
                     if(z.isDamaged())
                     {
                               bulletAppState.getPhysicsSpace().remove(z.getNode().getControl(RigidBodyControl.class));
                            zomb.remove(z);
                         lvl.detachChild(z.getNode());
                     }
                             
                     
                     
                     lvl.detachChild(B.getNode());
                   
                     bulletAppState.getPhysicsSpace().remove(B.getNode().getControl(RigidBodyControl.class));
                    hashing.remove(event.getNodeA(),B);
                 
                    
                 
                 }
                 else
                 {
                     
                     Bullet B=hashing.get(event.getNodeB());
                     
                     hashing.remove(B.getNode());
                     lvl.detachChild(B.getNode());
                     
                     
                     
                 }
                    
                    
                    
                    
                
            }
        }
        
        if ("Box".equals(event.getNodeA().getName()) || "Box".equals(event.getNodeB().getName())) {
            if ("bullet".equals(event.getNodeA().getName()) || "bullet".equals(event.getNodeB().getName())) {
                System.out.println("You hit the box!");

            }
        }
    }

    protected void initMark() {
        Sphere sphere = new Sphere(30, 30, 0.2f);
        mark = new Geometry("BOOM!", sphere);
        Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);
    }

    private void initFloor()
    {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //    mat.setColor("Color", ColorRGBA.Blue);

        Box floorBox = new Box(20, 0.25f, 20);
        Geometry floorGeometry = new Geometry("Floor", floorBox);
        floorGeometry.setMaterial(mat);
        floorGeometry.setLocalTranslation(0, -0.25f, 0);
        floorGeometry.addControl(new RigidBodyControl(0));
        lvl.attachChild(floorGeometry);
          bulletAppState.getPhysicsSpace().add(floorGeometry);


    }

}



