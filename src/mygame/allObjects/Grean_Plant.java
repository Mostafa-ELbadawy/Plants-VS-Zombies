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
import com.jme3.scene.Node;

/**
 *
 * @author DELL
 */
public class Grean_Plant extends Attackers{
    
    private final AssetManager assetManager;   
    public Grean_Plant (AssetManager asset) {
        super(asset);
        assetManager = asset;
      
        Node node = (Node) assetManager.loadModel("Blender/plant.j3o");
        this.node = node;
         this.node.setName("plant");

        name = "MocapGuy_Teeth";
        Node zomb = (Node) node.getChild(name);
        control = zomb.getControl(AnimControl.class);
        channal = control.createChannel();
        setEffect(-0.5f);
        setEffectTime(1.5f);
        
        
           phyControl=new RigidBodyControl(0);
          
           phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
           phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
           phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        

         this.node.addControl(phyControl);
       // setCollisionShape(new BoxCollisionShape(node.getLocalScale()));
        

        this.node.rotate(0, (float) Math.PI / 2, 0);
    }
    
}
