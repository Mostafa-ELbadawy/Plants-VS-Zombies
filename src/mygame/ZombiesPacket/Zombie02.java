/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ZombiesPacket;

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
public class Zombie02 extends Zombie {

    public Zombie02(AssetManager asset) {

        super(asset);
        Node node = (Node) assetManager.loadModel("Blender/zombie02/zombie02.j3o");
        name = "zombie";
        node = (Node) node.getChild(name);
        this.node = node;
        this.node.addLight(new AmbientLight());
        
        phyControl = new RigidBodyControl(0);
        phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        this.node.addControl(phyControl);

        node = (Node) node.getChild("zombie2");
        control = node.getControl(AnimControl.class);
        channal = control.createChannel();
        phyControl.activate();
 
        health = 150;
        attackPower = 25;
        attackSpeed = 3;
        movingSpeed = 2.0f;
        


    }

}
