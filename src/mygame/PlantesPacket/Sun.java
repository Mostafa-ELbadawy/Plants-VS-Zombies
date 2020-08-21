/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.PlantesPacket;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author DELL
 */
public class Sun {

    private Geometry node;
    private float score;
    private RigidBodyControl phyControl;

    private static AssetManager assetManager;
    private static LinkedList<Sun> sunsVector;
    private static Node lvl;
    private static PhysicsSpace space;
    private static HashMap<Geometry, Sun> hashingSun;
    
    public Sun() {
        this(25);
    }

    public Sun(float score) {

        this.score = score;
        Sphere sun = new Sphere(32, 32, 2f, true, false);
        sun.setTextureMode(Sphere.TextureMode.Projected);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Yellow);
        node = new Geometry("sun", sun);
        node.setMaterial(mat);

        phyControl = new RigidBodyControl(15);
        phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);

        phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
        phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_03);

        this.node.addControl(phyControl);

        phyControl.setLinearVelocity(new Vector3f(0, -5, 0));
    }

    public Geometry getNode() {
        return node;
    }

    public void setNode(Geometry node) {
        this.node = node;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public RigidBodyControl getPhyControl() {
        return phyControl;
    }

    public void setPhyControl(RigidBodyControl phyControl) {
        this.phyControl = phyControl;
    }

    public static void addSun(Vector3f pos) {

        sunsVector.add(new Sun());
        Sun newsun = sunsVector.getLast();
        lvl.attachChild(newsun.getNode());
        space.add(newsun.getNode());
        newsun.getPhyControl().setEnabled(false);
        newsun.getNode().setLocalTranslation(pos);
        hashingSun.put(newsun.getNode(), newsun);
        newsun.getPhyControl().setEnabled(true);

    }

    public static float removeSun(Geometry G) {
        try {

            Sun sun = hashingSun.get(G);
            lvl.detachChild(sun.getNode());
            space.remove(sun.getPhyControl());
            hashingSun.remove(G);
            sunsVector.remove(sun);
            return sun.getScore();

        } catch (Exception e) {
            return 0;
        }

    }

    public final static void initStaticSun(AssetManager assetManager, LinkedList<Sun> sunsVector, Node lvl, PhysicsSpace space, HashMap<Geometry, Sun> hashingSun) {
        Sun.assetManager = assetManager;
        Sun.sunsVector = sunsVector;
        Sun.lvl = lvl;
        Sun.hashingSun = hashingSun;
        Sun.space = space;
    }

}
