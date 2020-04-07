/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.allObjects;

import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.HashMap;

/**
 *
 * @author DELL
 */
public abstract class Defenders extends plant{
    
     private final AssetManager assetManager;
   
      public Defenders(AssetManager asset) {
        super(asset);
        assetManager = asset;
    }
      
       @Override
     public void setstatus(float tpf, float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {
     idel();
     }
      
}
