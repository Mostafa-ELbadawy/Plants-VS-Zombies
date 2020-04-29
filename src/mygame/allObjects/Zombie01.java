/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.allObjects;

import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;

import com.jme3.scene.Node;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;

public class Zombie01 extends Zombie 
{
//private final AnimEventListener listener;`
     public Zombie01(AssetManager  asset ) {
       
         super(asset);
        
       
            Node node= (Node)assetManager.loadModel("Blender/zombie.j3o");
            name="zombie";
            node=(Node)node.getChild(name);
            this.node=node;
        this.node.setName("zombie");
          node.setLocalScale(0.05f, 0.05f, 0.05f);
       
            this.node.addLight(new AmbientLight());
         phyControl=new RigidBodyControl(0);
        // phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);  
        
         phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
         phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
       
         
         this.node.addControl(phyControl);
        
            control = node.getControl(AnimControl.class);
            channal=control.createChannel();
         phyControl.activate();
       
     
     }
    
}
