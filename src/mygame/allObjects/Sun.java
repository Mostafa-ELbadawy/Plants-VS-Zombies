/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.allObjects;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
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
   private AssetManager assetManager;

    public Sun(AssetManager assetManager) {
          this(25,assetManager);
    }
    public Sun(float score,AssetManager assetManager) {
        this.assetManager = assetManager;
        this.score = score;
        Sphere sun = new Sphere(32, 32, 1f, true, false);
        sun.setTextureMode(Sphere.TextureMode.Projected);
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
           mat.setColor("Color", ColorRGBA.Yellow);
           node = new Geometry("sun", sun);
           node.setMaterial(mat);
        phyControl = new RigidBodyControl(5);
  

        // phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);  
        phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        this.node.addControl(phyControl);
        phyControl.setLinearVelocity(new Vector3f(0,-5, 0));
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
    
    
}
