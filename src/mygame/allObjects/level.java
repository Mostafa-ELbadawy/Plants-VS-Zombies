/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.allObjects;


import addetions.pair;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.Timer;
import com.jme3.ui.Picture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author DELL
 */
////
public class level extends AbstractAppState implements PhysicsCollisionListener, AnimEventListener {

    private final float side = 5.8f;
    private plant[][] floor;

    private final SimpleApplication app;
    private final Camera camera;
    private final FlyByCamera flyByCamera;
    private final Node root,guiNode;
    private Node lvl;
    protected final AssetManager assetManager;
    private final InputManager inputManager;
    private BulletAppState bulletAppState;
    private float startgametime;
    private Spatial scane;
    private LinkedList<Zombie> zomb;
    private LinkedList<plant> plan;
    private ArrayList<Card> cardsVector;

    private HashMap<Geometry, Bullet> hashing;
    private HashMap<Geometry, Card> hashingCard;
    private HashMap<Node, Zombie> hashingzombie;
    private HashMap<Node, plant> hashingplant;
    private HashMap<AnimControl, Zombie> hashingzombiecontrol;
    private HashMap<AnimControl, plant> hashingPlantcontrol;

    private BitmapText scoreText;

    private int score = 15000;
    private PhysicsSpace space;
    private final Timer timer;
    private Card curCard = null;
    Random rand = new Random();
    private Geometry mark;
    private boolean dead = false;
    float right = 0, up = 0;
    private boolean mov = false;
    PriorityQueue<pair> pq;
    // public level(AssetManager ass) {

    public level(SimpleApplication app) {
        camera = app.getCamera();
        flyByCamera = app.getFlyByCamera();
        root = app.getRootNode();
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
        timer = app.getTimer();
        app.getStateManager();
      guiNode=app.getGuiNode();
      
      this.app = app;

    }

    //////
    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        space = bulletAppState.getPhysicsSpace();
        flyByCamera.setMoveSpeed(30f);
        flyByCamera.setZoomSpeed(30f);
        timer.reset();

        initAllObject();

        root.attachChild(lvl);
        scane = assetManager.loadModel("Scenes/level1.j3o");
        scane.setName("scane");
        lvl.attachChild(scane);

        addplant(Vector3f.ZERO);
        addzombie(1);
        /////////////////////////////////////////////////////////////////////////////////

        //bulletAppState.setDebugEnabled(true);
        space.setGravity(Vector3f.ZERO);
        space.addCollisionListener(this);

        //  camera.setLocation(new Vector3f(0.34607443f, 46.688816f, 33.021984f));
        //  camera.setRotation(new Quaternion(0.011682421f, 0.8854312f, -0.46462032f, 0.0017531696f));
        camera.setLocation(new Vector3f(10.465846f, 50.21445f, 5.6196184f));
        camera.setRotation(new Quaternion(0.0196439f, 0.843758f, -0.53620327f, 0.01313919f));

        // flyByCamera.setEnabled(false);
/////////////////////////////////////////////////////////////////////////////////////////////////



Node n=new Node("test");
//guiNode.attachChild(n);
Picture pic = new Picture("HUD Picture");
pic.setImage(assetManager, "Blender/wall1.png", true);
pic.setWidth(400);
pic.setHeight(300);
pic.setPosition(200, 150);

n.attachChild(pic);

////////////////////////////////////////////////////////////////////////////////////////////////////
        try {

            cardsVector = Card.loadCards(assetManager, hashingCard);

        } catch (Exception e) {
            System.out.println("exxxxxxxxxxxxxxxxxxxxx");
        }

guiNode.addLight(new AmbientLight());
Node cardsNode=new Node("cards");
//cardsNode.setLocalTranslation(50, 50, 1);
lvl.attachChild(cardsNode);

        for (int i = 0; i < cardsVector.size(); i++) {
            cardsNode.attachChild(cardsVector.get(i).getNode());
          //  guiNode.attachChild(cardsVector.get(i).getPic());
  
        }
//        cardsNode.updateGeometricState();

        pq = Generator.genrate(44);
        // printqueue();
/////////////////////////////////////////////////////////////////////////////////////////////
        lvl.addLight(new AmbientLight());
    }

    public void update(float tpf) {
        moveAllZombies(tpf);
        attackAllPlanet(tpf);
        checkqueue();
        checkAllCards();

        scoreText.setText(Integer.toString(score));
        if (dead) {
         //   System.out.println("Dead");
        }

    }

    private void printqueue() {
        while (!pq.isEmpty()) {
            System.out.println("zomp at " + pq.peek().first + " type " + pq.poll().second);

        }
    }

    private void checkqueue() {
        while (!pq.isEmpty() && timer.getTimeInSeconds() >= pq.peek().first) {
            addzombie(pq.poll().second);

        }

    }

    public void moveAllZombies(float tpf) {
        for (int i = 0; i < zomb.size(); i++) {
            if (!zomb.get(i).isDamaged()) {
                dead = (zomb.get(i).setstatus(tpf, plan, hashingplant, timer.getTimeInSeconds(), space, floor) || dead);
            }
        }

    }

    public void attackAllPlanet(float tpf) {
        for (int i = 0; i < plan.size(); i++) {
            plan.get(i).setstatus(tpf, timer.getTimeInSeconds(), space, hashing);
        }

    }

    public void checkAllCards() {
        for (int i = 0; i < cardsVector.size(); i++) {
            cardsVector.get(i).checkColor(timer.getTimeInSeconds(), curCard);
        }

    }

    public void initAllObject() {
        inivar();
        initKeys();
        initFloor();
        initScoreText();
        initHome();
    }

    public boolean is_valid(int x, int y) {

        if (x < 0 || x > 4 || y < 0 || y > 8) {
            return false;
        }
        return floor[x][y] == null;
    }

    public void addzombie(int typ) {
        int row = FastMath.nextRandomInt(0, 4);
      
        if (typ == 1 &&false) {

            zomb.add(new Zombie01(assetManager));
       
        }
        else if(typ==4||true)
        {
            
            zomb.add(new Zombie04(assetManager));
            
        }
        
             zomb.getLast().setRow(row);
            lvl.attachChild(zomb.getLast().getNode());
            space.addAll(zomb.getLast().getNode());
            zomb.getLast().phyControl.setEnabled(false);
            zomb.getLast().getNode().setLocalTranslation(10 * side, 0, -side / 2 - side * row);
         
            //zomb.getLast().getNode().rotate(0, 0, (float) Math.PI / 2);

            zomb.getLast().phyControl.setEnabled(true);
            hashingzombie.put(zomb.getLast().getNode(), zomb.getLast());
            hashingzombiecontrol.put(zomb.getLast().getControl(), zomb.getLast());
            zomb.getLast().getControl().addListener(this);

    }

    public void addplant(Vector3f v) {

        int col = (int) (v.x / side), row = (int) -(v.z / side);
        if (curCard == null) {
            return;
        }
        if (is_valid(row, col) && curCard.getCost() <= score) {
            
            
            
            
            if (curCard.getTyp() == 1) {
                
                plan.add(new Grean_Plant(assetManager));
            } else if (curCard.getTyp() == 2) {
                plan.add(new Potato(assetManager));
            } else if (curCard.getTyp() == 6) {
                plan.add(new Jumper(assetManager));
                hashingPlantcontrol.put(plan.getLast().getControl(), plan.getLast());
                plan.getLast().getControl().addListener(this);
            }

            floor[row][col] = plan.getLast();
            lvl.attachChild(plan.getLast().getNode());
            space.addAll(plan.getLast().getNode());
            plan.getLast().phyControl.setEnabled(false);
            plan.getLast().setRow(row);
            plan.getLast().setCol(col);
            plan.getLast().getNode().setLocalTranslation(col * side + side / 2, 0, -side / 2 - side * row);
            plan.getLast().phyControl.setEnabled(true);
            hashingplant.put(plan.getLast().getNode(), plan.getLast());

            curCard.setLastTaken(timer.getTimeInSeconds());
            score -= curCard.getCost();
            curCard = null;

        }

    }

    private void inivar() {
        zomb = new LinkedList<>();
        plan = new LinkedList<>();
        cardsVector = new ArrayList<>();
        
        
        hashing = new HashMap<>();
        hashingCard = new HashMap<>();
        hashingzombiecontrol = new HashMap<>();
        hashingzombie = new HashMap<>();
        hashingplant = new HashMap<>(); 
        hashingPlantcontrol=new HashMap<>();
        lvl = new Node("level1");
        //lvl=root;
        floor = new plant[6][10];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                floor[i][j] = null;
            }
        }

    }

    @Override
    public void collision(PhysicsCollisionEvent event) {

        //   System.out.println(event.getNodeA().getName() + " hit " + event.getNodeB().getName());
        if (zomb.size() > 0) {

            if ((event.getNodeA().getName().equals("zombie") && event.getNodeB().getName().equals("bullet")) || (event.getNodeB().getName().equals("zombie") && event.getNodeA().getName().equals("bullet"))) {
                Bullet B;
                Zombie z;
                if ((event.getNodeA().getName().equals("zombie") && event.getNodeB().getName().equals("bullet"))) {
                    B = hashing.get(event.getNodeB());
                    z = hashingzombie.get(event.getNodeA());
                } else {
                    B = hashing.get(event.getNodeA());
                    z = hashingzombie.get(event.getNodeB());

                }

                try {

                    z.damage(B.getPower());
                    if (B.getEffect() != 0) {
                        z.setPoisonEffect(B.getEffect());
                        z.setPoisonTime(B.getEffectTime());
                        z.setLastPoison(timer.getTimeInSeconds());

                    }
                    if (z.isDamaged()) {
                        bulletAppState.getPhysicsSpace().remove(z.getNode().getControl(RigidBodyControl.class));
                        z.dying();
                    }

                    lvl.detachChild(B.getNode());
                    bulletAppState.getPhysicsSpace().remove(B.getNode().getControl(RigidBodyControl.class));
                    hashing.remove(B.getNode(), B);

                } catch (Exception e) {
                    System.out.println("not found ");

                }

            }

        }

    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("one") && !keyPressed) {
                //   curTyp=1;
            } else if (name.equals("two") && !keyPressed) {
                // curTyp=2;
            } else if (name.equals("P") && !keyPressed) {
                /* addplant(2, new Vector3f(5, 0,-1));
                for (int i = 0; i < 9; i++) {
                    if (is_valid(co, i)) {
                        addplant(1, new Vector3f(co * side, 0, side * i));
                        System.out.println("planet " + co + " " + i);
                        co = 0;

                        break;
                    }
                }
                 */
                
                flyByCamera.setEnabled(!flyByCamera.isEnabled());
                inputManager.setCursorVisible(flyByCamera.isEnabled());
    
            } else if (name.equals("Z") && !keyPressed) {
                int r = rand.nextInt();
                if (r < 0) {
                    r *= -1;
                }
                r %= 5;
                System.out.println("row = " + r);
                addzombie(1);
            } else if (name.equals("add") && !keyPressed) {
                Vector3f pos = getMousePos();
                addplant(pos);
            }
        }
    };

    private Vector3f getMousePos() {

        Vector3f v = new Vector3f();
        Vector3f origin = camera.getWorldCoordinates(inputManager.getCursorPosition(), 0f);
        Vector3f direction = camera.getWorldCoordinates(inputManager.getCursorPosition(), 1f);
        direction.subtractLocal(origin).normalizeLocal();

        // 2. Aim the ray from cam loc to cam direction.        
        Ray ray = new Ray();
        ray.setOrigin(origin);
        ray.setDirection(direction);
        CollisionResults results = new CollisionResults();
        // 3. Collect intersections between Ray and Shootables in results list.
        lvl.collideWith(ray, results);
        System.out.println("size= " + results.size());
        // 5. Use the results (we mark the hit object)
        if (results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                // For each hit, we know distance, impact point, name of geometry.

                String hitName = results.getCollision(i).getGeometry().getName();
                //  System.out.println("name " + i + " " + hitName);
                float dist = results.getCollision(i).getDistance();

                //     System.out.println("i= " + i + " name= " + hitName + " dist= " + dist);
                if (hitName.equals("Floor")) {
                    v = results.getCollision(i).getContactPoint();
                    //   break;
                } else if (hitName.equals("card")) {
                     System.out.println("ya rab");
                    curCard = hashingCard.get(results.getCollision(i).getGeometry());
                    if (!curCard.isValid(timer.getTimeInSeconds())) {
                        curCard = null;
                    }
                    //  System.out.println("#card = " +curCard.toString()+" typ=  "+curCard.getTyp());   
                }

            }

        }
        System.out.println("v= " + v.toString());
        return v;

    }

    private void initKeys() {

        inputManager.addMapping("one", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addListener(actionListener, "one");

        inputManager.addMapping("two", new KeyTrigger(KeyInput.KEY_B));
        inputManager.addListener(actionListener, "two");

        inputManager.addMapping("P", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "P");

        inputManager.addMapping("Z", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addListener(actionListener, "Z");

        inputManager.addMapping("add", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "add");

    }

    private void initFloor() {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//            mat.setColor("Color", ColorRGBA.Green);

        mat.setTexture("ColorMap", assetManager.loadTexture("Blender/mainBG.png"));
        Box floorBox = new Box(26, 0.25f, 18);
        Geometry floorGeometry = new Geometry("Floor", floorBox);
        floorGeometry.setMaterial(mat);
        floorGeometry.setLocalTranslation(25, -0.25f, -15);
        floorGeometry.addControl(new RigidBodyControl(0));
        lvl.attachChild(floorGeometry);
        space.add(floorGeometry);

    }

    private void initHome() {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);

        Box HomeBox = new Box(26, 15, 18);
        Geometry HomeGeometry = new Geometry("Home", HomeBox);
        HomeGeometry.setMaterial(mat);
        HomeGeometry.setLocalTranslation(-27, 10, -15);
        lvl.attachChild(HomeGeometry);
        //floorGeometry.addControl(new RigidBodyControl(0));
        //  space.add(floorGeometry);

    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {

        if (animName.equals("dying")) {
            try {
                Zombie z = hashingzombiecontrol.get(control);
                z.getNode().getParent().detachChild(z.getNode());
                zomb.remove(z);
                hashingzombie.remove(z.getNode(), z);
                hashingzombiecontrol.remove(z.getControl(),z);

            } catch (Exception e) {
            }

        } else if (animName.equals("attacking")) {
            try {
                plant p = hashingPlantcontrol.get(control);
                killFrontZombies(p.getNode().getLocalTranslation());
                lvl.detachChild(p.getNode());
                plan.remove(p);
                hashingplant.remove(p.getNode(), p);
                hashingPlantcontrol.remove(p.getControl(),p);
                space.remove(p.phyControl);
                System.out.println("Dooooooone");

            } catch (Exception e) {
               
            }

        }

    }

    private void killFrontZombies(Vector3f pos) {
        CollisionResults results = new CollisionResults();

        Ray sight = new Ray(pos.add(0, 3, 0.25f), new Vector3f(1, 0, 0));

        lvl.collideWith(sight, results);
        for (int i = 0; i < results.size(); i++) {
            String hitName = results.getCollision(i).getGeometry().getName();
            float dis = results.getCollision(i).getDistance();
            if (hitName.equals("zombie") && dis <= 6f) {
              try{
                Zombie z=hashingzombie.get(results.getCollision(i).getGeometry().getParent());
                bulletAppState.getPhysicsSpace().remove(z.getNode().getControl(RigidBodyControl.class));
                z.getNode().getParent().detachChild(z.getNode());
                zomb.remove(z);
                hashingzombie.remove(z.getNode(), z);
                hashingzombiecontrol.remove(z.getControl(),z);
              }catch (Exception e) {
                System.out.println("zombie dell");
            }   

            }
            if(dis>6)
                break;

        }

    }

    void initScoreText() {
        BitmapFont font = assetManager.loadFont("Interface/Fonts/Console.fnt");

        scoreText = new BitmapText(font);
        scoreText.setSize(5);

        scoreText.setColor(ColorRGBA.Blue);

        scoreText.setText(Integer.toString(score));
        lvl.attachChild(scoreText);

        scoreText.setLocalTranslation(2f, 5, -26.5f);
        scoreText.rotate(-(float) Math.PI / 2, 0, 0);

    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {

    }

    
}
    
    
    
    
    
    
    
