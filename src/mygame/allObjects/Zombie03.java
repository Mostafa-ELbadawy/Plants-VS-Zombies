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

/**
 *
 * @author Nada youssef
 */
public class Zombie03 extends Zombie {

//private final AnimEventListener listener;`
    public Zombie03(AssetManager asset) {

        super(asset);
        Node node = (Node) assetManager.loadModel("Blender/zombie03/zombie.j3o");
        
            node=(Node)node.getChild("zombie");
        this.node = node;
        name = "Jill_HiRes_Teeth_Geo";

        this.node.addLight(new AmbientLight());
        phyControl = new RigidBodyControl(0);
        // phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);  

        phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);

        this.node.addControl(phyControl);

        node = (Node) node.getChild(name);
        control = node.getControl(AnimControl.class);
        channal = control.createChannel();
        phyControl.activate();

    }

}
