/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.allObjects;

import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.scene.Node;

/**
 *
 * @author DELL
 */
public class Potato extends  Defenders{
 private final AssetManager assetManager;   
    public Potato (AssetManager asset) {
        super(asset);
        assetManager = asset;
      
        setHealth(150);
        
        Node node = (Node) assetManager.loadModel("Blender/photoes2.j3o");
        this.node = node;
         this.node.setName("plant");

      
        control = null;
        channal = null;
        
        //this.node=(Node)node.getChild("Scene");
        this.node.addLight(new DirectionalLight());
        phyControl=new RigidBodyControl(0);
          
           phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
           phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
           phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        

         this.node.addControl(phyControl);
       // setCollisionShape(new BoxCollisionShape(node.getLocalScale()));
    

        this.node.rotate(0, (float) Math.PI / 2, 0);
    }
    
}
