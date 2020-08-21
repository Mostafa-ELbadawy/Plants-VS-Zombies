/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.PlantesPacket;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;

/**
 *
 * @author DELL
 */
public class Potato extends Defenders {

    private static Node model;

    public Potato() {
        super();

        Node node = (Node) model.clone();
        this.node = node;

        control = null;
        channal = null;

        phyControl = node.getControl(RigidBodyControl.class);

        health = 200;
    }

    public static void loadmodle(AssetManager asset) {

        assetManager = asset;
        model = (Node) assetManager.loadModel("Blender/potato/potato.j3o");
        RigidBodyControl phyControl = new RigidBodyControl(0);
        phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        model.addControl(phyControl);
        model.setName("plant");
        model.rotate(0, (float) Math.PI / 2, 0);

    }

}
