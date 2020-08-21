/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import addetions.ScoreException;
import addetions.pair;
import java.io.IOException;
import mygame.allObjects.*;
import mygame.PlantesPacket.*;
import mygame.ZombiesPacket.*;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
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
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.Timer;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author DELL
 */
public class level extends AbstractAppState implements PhysicsCollisionListener, AnimEventListener {

    private final float side = 5.8f;
    private float soundVolume = 0;
    private int score = 1000, level = 0;
    private boolean dead = false, isSound = true, win = false;

    Random rand = new Random();
    private final Timer timer;

    private plant[][] floor;

    private final Camera camera;
    private final FlyByCamera flyByCamera;
    private final SimpleApplication app;

    private final Node root;
    private Node lvl;
    private AudioNode SoundNode;
    private Spatial scane;

    private final RenderManager renderManager;
    private final AssetManager assetManager;
    private final InputManager inputManager;
    private BulletAppState bulletAppState;

    private LinkedList<Zombie> zomb;
    private LinkedList<plant> plan;

    private LinkedList<Car> cars;
    private LinkedList<Sun> sunsVector;
    private LinkedList<BombEffect> bombVector;

    private ArrayList<Card> cardsVector;

    private HashMap<Geometry, Bullet> hashing;
    private HashMap<Geometry, Card> hashingCard;
    private HashMap<Geometry, Sun> hashingSun;
    private HashMap<Node, Zombie> hashingzombie;
    private HashMap<Node, plant> hashingplant;
    private HashMap<AnimControl, Zombie> hashingzombiecontrol;
    private HashMap<AnimControl, plant> hashingPlantcontrol;

    PriorityQueue<pair> pq;

    private BitmapText scoreText;

    private PhysicsSpace space;

    private Card curCard = null;

    private Vector3f[][] cameraCardsPositions;
    private Quaternion[] cameraRotation;
    private int cameraStatus = -1, loseStatus = 1;
    private boolean camerachanged = false, soundChanged = false;
    private Node cardsNode;
    BitmapText levelText, printText;
    BitmapFont font;

    public level(SimpleApplication app, int level) {
        this(app, level, 1);
    }

    public level(SimpleApplication app, int level, float volume) {

        camera = app.getCamera();
        flyByCamera = app.getFlyByCamera();
        root = app.getRootNode();
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
        timer = app.getTimer();
        this.app = app;

        renderManager = app.getRenderManager();
        this.level = level;
        this.soundVolume = volume;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);
        loadAllModels();

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        space = bulletAppState.getPhysicsSpace();
        space.setGravity(Vector3f.ZERO);
        space.addCollisionListener(this);

        initAllObject();
        timer.reset();
        root.attachChild(lvl);

        camera.setLocation(new Vector3f(20.877138f, 61.490482f, -21.619055f));
        camera.setRotation(new Quaternion(-0.010221233f, -0.69683564f, 0.7171501f, 0.0033561937f));

        /// bulletAppState.setDebugEnabled(true);
        flyByCamera.setEnabled(false);
        initStaticSun();

        try {
            cardsVector = Card.loadCards(assetManager, hashingCard);
        } catch (Exception e) {
        }

        cardsNode = new Node("cards");
        lvl.attachChild(cardsNode);

        flyByCamera.setMoveSpeed(30f);
        flyByCamera.setZoomSpeed(30f);

        cardsNode.setLocalTranslation(new Vector3f(44.0f, -4.0f, -28.0f));
        cardsNode.setLocalRotation(new Quaternion(-0.010221233f, -0.69683564f, 0.7171501f, 0.0033561937f));

        ///////////////////////////////////////////////////////////////////
        // show level
        BitmapFont fontLevel = assetManager.loadFont("Fonts/zeft4.fnt");

        levelText = new BitmapText(fontLevel);
        levelText.setSize(1.5f);

        levelText.setColor(ColorRGBA.White);
        levelText.setText("Level\n " + Integer.toString(level));

        levelText.setLocalTranslation(10.5f, 5f, -32);
        levelText.setLocalRotation(camera.getRotation());
        levelText.rotate((float) Math.PI / 2, (float) Math.PI, (float) Math.PI);

        //camera
        Box boxCamera = new Box(2f, 2f, 0.01f);
        Geometry cubeCamera = new Geometry("camera", boxCamera);

        Material MatC = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        MatC.setColor("Color", ColorRGBA.White);
        MatC.setTexture("ColorMap", assetManager.loadTexture("photos/cameraphoto.png"));
        MatC.getAdditionalRenderState().setDepthWrite(false);
        MatC.getAdditionalRenderState().setDepthTest(false);
        MatC.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        cubeCamera.setQueueBucket(RenderQueue.Bucket.Transparent);
        cubeCamera.setMaterial(MatC);
        cubeCamera.setLocalTranslation(12f, 5f, -27);

        //sound
        Box boxSound = new Box(2f, 2f, 0.01f);
        Geometry cubeSound = new Geometry("sound", boxSound);

        Material MatS = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        MatS.setColor("Color", ColorRGBA.White);
        MatS.setTexture("ColorMap", assetManager.loadTexture("photos/sound.png"));
        MatS.getAdditionalRenderState().setDepthWrite(false);
        MatS.getAdditionalRenderState().setDepthTest(false);
        MatS.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        cubeSound.setQueueBucket(RenderQueue.Bucket.Transparent);
        cubeSound.setMaterial(MatS);
        cubeSound.setLocalTranslation(16f, 5f, -27);
//////////////////////////////////////////////////////////////////

        cardsNode.attachChild(levelText);
        cardsNode.attachChild(scoreText);
        cardsNode.attachChild(cubeCamera);
        cardsNode.attachChild(cubeSound);

        scoreText.setLocalTranslation(levelText.getLocalTranslation().add(-0.5f, -3.0f, 0.0f));
        scoreText.setLocalRotation(levelText.getLocalRotation());
        for (int i = 0; i < cardsVector.size(); i++) {
            cardsNode.attachChild(cardsVector.get(i).getNode());
        }

        /***************************************************************************/
        
        
        
    }

    
    private void loadAllModels()
    {
        Bomb.loadmodle(assetManager);
        Jumper.loadmodle(assetManager);
        Potato.loadmodle(assetManager);
        Zombie01.loadmodle(assetManager);
        Zombie02.loadmodle(assetManager);
        Zombie03.loadmodle(assetManager);
        Zombie04.loadmodle(assetManager);
        SunFlower.loadmodle(assetManager);
        Grean_Plant.loadmodle(assetManager);
        Frozzen_plant.loadmodle(assetManager);
        
    }

    @Override
    public void update(float tpf) {
        if (dead) {
            sleep(5000);
            if (loseStatus == 1) {
                printText.setSize(8);
                printText.setText("Game Over");
            } else if (loseStatus == 2) {
                Close();
            }
            loseStatus++;
            return;
        }

        if (win) {
            sleep(5000);
            try {
                UpdateLevelFile();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            Close();

        }
        moveAllZombies(tpf);
        attackAllPlant(tpf);
        checkqueue();
        checkAllCards();
        checkAllCars(tpf);
        checkAllBombs();

        scoreText.setText("score:\n" + Integer.toString(score));
        if (dead) {
            playSound(2); // play lose sound
            printText.setSize(4);
            printTextOnScreen("THE ZOMBIES\nATE YOUR\nBRAINS!", ColorRGBA.Red);
        }
        if (win) {
            playSound(3); // play win sound
            printText.setSize(4);
            printTextOnScreen("YOU  WIN", ColorRGBA.Red);

        }

    }

    public void checkAllBombs() {
        for (int i = 0; i < bombVector.size(); i++) {
            if (bombVector.get(i).isBoom(timer.getTimeInSeconds())) {
                lvl.detachChild(bombVector.get(i).getNode());
                bombVector.remove(i--);
            }
        }

    }

    private void checkqueue() {
        while (!pq.isEmpty() && timer.getTimeInSeconds() >= pq.peek().first) {
            addzombie(pq.poll().second);
        }
        if (pq.isEmpty() && zomb.isEmpty()) {
            win = true;
        }
    }

    public void moveAllZombies(float tpf) {
        for (int i = 0; i < zomb.size(); i++) {
            if (!zomb.get(i).isDamaged()) {
                dead = (zomb.get(i).setstatus(tpf, plan, hashingplant, timer.getTimeInSeconds(), space, floor) || dead);
            }
        }

    }

    public void attackAllPlant(float tpf) {
        for (int i = 0; i < plan.size(); i++) {
            plan.get(i).setstatus(tpf, timer.getTimeInSeconds(), space, hashing);
        }

    }

    public void checkAllCars(float tpf) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).setstatus(tpf)) {
                killFrontZombies(cars.get(i).getNode().getLocalTranslation());

            }
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
        initcars();

        pq = Generator.genrate(level);

    }

    private void initcars() {

        for (int i = 0; i < 5; i++) {
            Car car = new Car(assetManager);

            space.add(car.getPhyControl());
            lvl.attachChild(car.getNode());
            cars.add(car);
            car.getPhyControl().setEnabled(false);
            car.getNode().setLocalTranslation(new Vector3f(-1.5f * side + side / 2, 0, -side / 2 - side * i));
            car.getPhyControl().setEnabled(true);
        }
    }

    private boolean is_valid(int x, int y) {

        if (x < 0 || x > 4 || y < 0 || y > 8) {
            return false;
        }
        return (floor[x][y] == null);
    }

    private void addzombie(int typ) {

        int row = FastMath.nextRandomInt(0, 4);
        if (typ == 1) {
            zomb.add(new Zombie03());
        } else if (typ == 2) {
            zomb.add(new Zombie01());
        } else if (typ == 3) {
            zomb.add(new Zombie02());
        } else if (typ == 4) {
            zomb.add(new Zombie04());
        }

        zomb.getLast().setRow(row);
        lvl.attachChild(zomb.getLast().getNode());
        space.addAll(zomb.getLast().getNode());

        zomb.getLast().getPhyControl().setEnabled(false);
        zomb.getLast().getNode().setLocalTranslation(10 * side, 0, -side / 2 - side * row);
        zomb.getLast().getPhyControl().setEnabled(true);

        hashingzombie.put(zomb.getLast().getNode(), zomb.getLast());
        hashingzombiecontrol.put(zomb.getLast().getControl(), zomb.getLast());
        zomb.getLast().getControl().addListener(this);

    }

    private void addplant(Vector3f v) throws ScoreException {

        int col = (int) (v.x / side), row = (int) -(v.z / side);
        if (curCard == null) {
            return;
        }
        if (curCard.getCost() > score) {
            throw new ScoreException();
        }
        if (is_valid(row, col)) {

            if (curCard.getTyp() == 1) {
                plan.add(new Grean_Plant());
            } else if (curCard.getTyp() == 2) {
                plan.add(new Potato());
            } else if (curCard.getTyp() == 3) {
                plan.add(new SunFlower());

            } else if (curCard.getTyp() == 4) {
                plan.add(new Frozzen_plant());

            } else if (curCard.getTyp() == 5) {
                plan.add(new Bomb());
                hashingPlantcontrol.put(plan.getLast().getControl(), plan.getLast());
                plan.getLast().getControl().addListener(this);
            } else if (curCard.getTyp() == 6) {
                plan.add(new Jumper());
                hashingPlantcontrol.put(plan.getLast().getControl(), plan.getLast());
                plan.getLast().getControl().addListener(this);
            }

            floor[row][col] = plan.getLast();
            lvl.attachChild(plan.getLast().getNode());
            space.addAll(plan.getLast().getNode());
            plan.getLast().getPhyControl().setEnabled(false);
            plan.getLast().setRow(row);
            plan.getLast().setCol(col);
            plan.getLast().getNode().setLocalTranslation(col * side + side / 2, 0, -side / 2 - side * row);
            plan.getLast().getPhyControl().setEnabled(true);
            hashingplant.put(plan.getLast().getNode(), plan.getLast());

            curCard.setLastTaken(timer.getTimeInSeconds());
            score -= curCard.getCost();
            curCard = null;

        }

    }

    private void inivar() {
        zomb = new LinkedList<>();
        plan = new LinkedList<>();
        cars = new LinkedList<>();
        sunsVector = new LinkedList<>();
        bombVector = new LinkedList<>();

        cardsVector = new ArrayList<>();

        hashing = new HashMap<>();
        hashingSun = new HashMap<>();
        hashingCard = new HashMap<>();
        hashingzombiecontrol = new HashMap<>();
        hashingzombie = new HashMap<>();
        hashingplant = new HashMap<>();
        hashingPlantcontrol = new HashMap<>();
        lvl = new Node("level1");
        lvl.addLight(new AmbientLight());
        floor = new plant[6][10];

        playSound(1);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                floor[i][j] = null;
            }
        }

        font = assetManager.loadFont("Fonts/zeft7.fnt");
        printText = new BitmapText(font);

        //col[0]->camerlocation , col[1]->cardslocation
        cameraCardsPositions = new Vector3f[5][2];
        cameraCardsPositions[0][0] = new Vector3f(62.338234f, 24.681103f, 14.634925f);
        cameraCardsPositions[0][1] = new Vector3f(28.0f, -4.0f, -55.0f);

        cameraCardsPositions[1][0] = new Vector3f(64.436935f, 30.658216f, -45.591244f);
        cameraCardsPositions[1][1] = new Vector3f(0.0f, -3.0f, -11.0f);

        cameraCardsPositions[2][0] = new Vector3f(-3.5778122f, 34.96735f, -49.976112f);
        cameraCardsPositions[2][1] = new Vector3f(9.0f, -6.5f, 18.0f);

        cameraCardsPositions[3][0] = new Vector3f(-1.4025922f, 36.037018f, 26.289604f);
        cameraCardsPositions[3][1] = new Vector3f(45.0f, 0.1f, -25.0f);

        cameraCardsPositions[4][0] = new Vector3f(20.877138f, 61.490482f, -21.619055f);
        cameraCardsPositions[4][1] = new Vector3f(44.0f, -4.0f, -28.0f);

        cameraRotation = new Quaternion[5];
        cameraRotation[0] = new Quaternion(-0.10186718f, 0.877377f, -0.24892709f, -0.39732596f);
        cameraRotation[1] = new Quaternion(0.26935488f, -0.40357396f, 0.13690536f, 0.8636162f);
        cameraRotation[2] = new Quaternion(0.34881547f, 0.26625514f, -0.09325592f, 0.8937222f);
        cameraRotation[3] = new Quaternion(0.091312274f, 0.91568655f, -0.30987224f, 0.23908037f);
        cameraRotation[4] = new Quaternion(-0.010221233f, -0.69683564f, 0.7171501f, 0.0033561937f);
    }

    private void UpdateLevelFile() throws IOException {

        FileWriter fileWriter = new FileWriter("assets/files/level.txt");
        fileWriter.write(level + 1);
        fileWriter.close();
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        
        
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
                    space.remove(z.getNode().getControl(RigidBodyControl.class));
                    z.dying();
                }

                lvl.detachChild(B.getNode());
                space.remove(B.getNode().getControl(RigidBodyControl.class));
                hashing.remove(B.getNode(), B);

            } catch (Exception e) {

            }

        }

    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("add") && !keyPressed) {
                Vector3f pos = getMousePos();
                flyByCamera.setEnabled(!flyByCamera.isEnabled());
                if (!pos.equals(new Vector3f(-100, -100, -100))) {
                    try {
                        addplant(pos);

                    } catch (ScoreException e) {
                        System.out.println(e.getMessage());
                    }

                }
            } else if (name.equals("exit") && !keyPressed) {
                Close();
            } else if (name.equals("cheating") && !keyPressed) {
                cheating();
            }

        }
    };

    private Vector3f getMousePos() {

        Vector3f pos = new Vector3f(-100, -100, -100);
        Vector3f origin = camera.getWorldCoordinates(inputManager.getCursorPosition(), 0f);
        Vector3f direction = camera.getWorldCoordinates(inputManager.getCursorPosition(), 1f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray();
        ray.setOrigin(origin);
        ray.setDirection(direction);
        CollisionResults results = new CollisionResults();

        lvl.collideWith(ray, results);

        for (int i = 0; i < results.size(); i++) {

            String hitName = results.getCollision(i).getGeometry().getName();
            float dist = results.getCollision(i).getDistance();

            if (hitName.equals("Floor")) {
                pos = results.getCollision(i).getContactPoint();

            } else if (hitName.equals("card")) {
                curCard = hashingCard.get(results.getCollision(i).getGeometry());
                if (!curCard.isValid(timer.getTimeInSeconds())) {
                    curCard = null;
                }
            } else if (hitName.equals("sun")) {
                score += Sun.removeSun(results.getCollision(i).getGeometry());
            } else if (hitName.equals("camera")) {
                camerachanged = !camerachanged;
                if (camerachanged) {
                    cameraStatus = ++cameraStatus % 5;
                    changeCameraPosition();
                }
            } else if (hitName.equals("sound")) {
                soundChanged = !soundChanged;
                if (soundChanged) {
                    isSound = !isSound;
                    ResetSound();
                }
            }
        }
        return pos;

    }

    private void changeCameraPosition() {
        camera.setLocation(cameraCardsPositions[cameraStatus][0]);
        camera.setRotation(cameraRotation[cameraStatus]);

        cardsNode.setLocalTranslation(cameraCardsPositions[cameraStatus][1]); //
        cardsNode.setLocalRotation(cameraRotation[cameraStatus]);
    }

    private void ResetSound() {
        if (isSound) {
            SoundNode.setVolume(3 * (soundVolume / 100));
        } else {
            SoundNode.setVolume(0);
        }
    }

    private void initKeys() {

        inputManager.addMapping("add", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "add");

        inputManager.addMapping("exit", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addListener(actionListener, "exit");
        inputManager.addMapping("cheating", new KeyTrigger(KeyInput.KEY_F2));
        inputManager.addListener(actionListener, "cheating");
    }

    private void initFloor() {

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Blender/mainBG.png"));

        Box floorBox = new Box(26, 0.25f, 18);
        Geometry floorGeometry = new Geometry("Floor", floorBox);

        floorGeometry.setMaterial(mat);
        floorGeometry.setLocalTranslation(25, -0.25f, -15);
        floorGeometry.addControl(new RigidBodyControl(0));
        floorGeometry.getControl(RigidBodyControl.class).setCollisionGroup(RigidBodyControl.COLLISION_GROUP_03);
        floorGeometry.getControl(RigidBodyControl.class).addCollideWithGroup(RigidBodyControl.COLLISION_GROUP_03);
        lvl.attachChild(floorGeometry);
        space.add(floorGeometry);

        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setTexture("ColorMap", assetManager.loadTexture("photos/Floor2.jpg"));

        //Material mat2 = assetManager.loadMaterial("Materials/road_mat.j3m");
        Box floorBox2 = new Box(100, 0.25f, 18);
        Geometry floorGeometry2 = new Geometry("Floor2", floorBox2);
        floorGeometry2.setMaterial(mat2);
        floorGeometry2.setLocalTranslation(15, -0.5f, -15);

        lvl.attachChild(floorGeometry2);

    }

    private void initHome() {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("photos/home.jpg"));

        Box HomeBox = new Box(26, 15, 18);
        Geometry HomeGeometry = new Geometry("Home", HomeBox);
        HomeGeometry.setMaterial(mat);
        HomeGeometry.setLocalTranslation(-35, 10, -15);
        lvl.attachChild(HomeGeometry);

        scane = assetManager.loadModel("Scenes/level1.j3o");
        scane.setName("scane");
        lvl.attachChild(scane);

    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {

        try {
            if (animName.equals("dying")) {
                Zombie z = hashingzombiecontrol.get(control);
                z.getNode().getParent().detachChild(z.getNode());
                zomb.remove(z);
                hashingzombie.remove(z.getNode(), z);
                hashingzombiecontrol.remove(z.getControl(), z);

            } else if (animName.equals("attacking")) {
                plant p = hashingPlantcontrol.get(control);
                lvl.detachChild(p.getNode());
                plan.remove(p);
                hashingplant.remove(p.getNode(), p);
                hashingPlantcontrol.remove(p.getControl(), p);
                floor[p.getRow()][p.getCol()] = null;
                space.remove(p.getPhyControl());

                if (p instanceof Jumper) {
                    killFrontZombies(p.getNode().getLocalTranslation());
                } else if (p instanceof Bomb) {
                    killAroundZombies(p.getNode().getLocalTranslation());
                    bombVector.addLast(new BombEffect(assetManager));
                    bombVector.getLast().getNode().setLocalTranslation(p.getNode().getLocalTranslation());
                    lvl.attachChild(bombVector.getLast().getNode());
                    renderManager.preloadScene(bombVector.getLast().getNode());

                }

            }
        } catch (Exception e) {

        }

    }

    private void killFrontZombies(Vector3f pos) {
        CollisionResults results = new CollisionResults();

        Ray sight = new Ray(pos.add(-0.5f * side, 5, 1f), new Vector3f(1, 0, 0));

        lvl.collideWith(sight, results);

        for (int i = 0; i < results.size(); i++) {
            String hitName = results.getCollision(i).getGeometry().getName();
            float dis = results.getCollision(i).getDistance();
            if (hitName.equals("zombie") && dis <= 2 * side) {
                try {
                    Zombie z = hashingzombie.get(results.getCollision(i).getGeometry().getParent().getParent());
                    bulletAppState.getPhysicsSpace().remove(z.getNode().getControl(RigidBodyControl.class));
                    z.getNode().getParent().detachChild(z.getNode());
                    zomb.remove(z);
                    hashingzombie.remove(z.getNode(), z);
                    hashingzombiecontrol.remove(z.getControl(), z);

                } catch (Exception e) {
                }
            }
            if (dis > 2 * side) {
                break;
            }

        }

    }

    private void killAroundZombies(Vector3f pos) {
        killFrontZombies(pos.add(-side, 0, -side));
        killFrontZombies(pos.add(0, 0, -side));
        killFrontZombies(pos.add(-side, 0, 0));
        killFrontZombies(pos.add(0, 0, 0));
        killFrontZombies(pos.add(-side, 0, side));
        killFrontZombies(pos.add(0, 0, side));

    }

    private void playSound(int stat) {
        if (SoundNode != null) {
            SoundNode.stop();
        }

        if (stat == 1) {
            SoundNode = new AudioNode(assetManager, "Sounds/GrasswalkPvZ1.ogg", DataType.Stream);
        } else if (stat == 2) {
            //lose sound
            SoundNode = new AudioNode(assetManager, "Sounds/lose.ogg", DataType.Stream);
        } else if (stat == 3) {
            //win sound
            SoundNode = new AudioNode(assetManager, "Sounds/winmusic.ogg", DataType.Stream);
        }

        SoundNode.setName("sound");
        SoundNode.setLooping(true);
        SoundNode.setPositional(false);
        SoundNode.setVolume(3);
        lvl.attachChild(SoundNode);
        SoundNode.play();

    }

    private void initScoreText() {

        BitmapFont font = assetManager.loadFont("Fonts/zeft6.fnt");

        scoreText = new BitmapText(font);
        scoreText.setSize(1f);
        scoreText.setText("score:\n" + Integer.toString(score));
        lvl.attachChild(scoreText);
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println("sleep error");
        }
    }

    private void printTextOnScreen(String text, ColorRGBA color) {

        printText.setColor(color);
        printText.setText(text);

        if (cameraStatus == 4 || cameraStatus == -1) {
            printText.setLocalTranslation(new Vector3f(-5.0f, 10.0f, -24.0f));
        } else if (cameraStatus == 0) {
            printText.setLocalTranslation(new Vector3f(10.0f, 12.0f, 0.0f));
        } else if (cameraStatus == 1) {
            printText.setLocalTranslation(new Vector3f(31.0f, 10.0f, 17.0f));
        } else if (cameraStatus == 2) {
            printText.setLocalTranslation(new Vector3f(50.0f, 10.0f, -20.0f));
        } else if (cameraStatus == 3) {
            printText.setLocalTranslation(new Vector3f(-1.0f, 10.0f, -30.0f));
        }
        printText.setLocalRotation(cardsNode.getLocalRotation());

        printText.rotate(0f, (float) Math.PI, 0f);
        lvl.attachChild(printText);
    }

    private void initStaticSun() {
        Sun.initStaticSun(assetManager, sunsVector, lvl, space, hashingSun);
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {

    }

    private void Close() {

        SoundNode.stop();
        SoundNode.setVolume(0);
        app.getRootNode().detachChild(lvl);
        app.getStateManager().attach(new theGameMenu(app));
        app.getStateManager().detach(app.getStateManager().getState(level.class));

    }

    private void cheating() {
        printText.setSize(4);
        printTextOnScreen("YOU Cheated", ColorRGBA.Red);
        while (!pq.isEmpty()) {
            pq.remove();
        }
        for (Zombie zomb1 : zomb) {
            zomb1.damage(zomb1.getHealth());
            zomb1.dying();
        }

    }

}
