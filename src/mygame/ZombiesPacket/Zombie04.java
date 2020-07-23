
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ZombiesPacket;

import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.scene.Node;

/**
 *
 * @author DELL
 */
public class Zombie04 extends Zombie {

    private static Node model;

    public Zombie04() {

        super();
        Node node = (Node) model.clone();
        name = "zombie";
        this.node = node;

        phyControl = new RigidBodyControl(0);
        phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        node.addControl(phyControl);
        phyControl.activate();

        node = (Node) node.getChild("zombie2");
        control = node.getControl(AnimControl.class);
        channal = control.createChannel();

        health = 120;
        attackPower = 30;
        attackSpeed = 3;
        movingSpeed = 5.0f;

    }

    public static void loadmodle(AssetManager asset) {

        assetManager = asset;
        model = (Node) assetManager.loadModel("Blender/zombie04/zombie.j3o");
        model = (Node) model.getChild("zombie");
        model.addLight(new AmbientLight());
    }

}
