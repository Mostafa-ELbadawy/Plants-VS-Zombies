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

    public Potato(AssetManager asset) {
        super(asset);

        Node node = (Node) assetManager.loadModel("Blender/potato/potato.j3o");
        this.node = node;
        this.node.setName("plant");

        control = null;
        channal = null;

        phyControl = new RigidBodyControl(0);
        phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        this.node.addControl(phyControl);

        this.node.rotate(0, (float) Math.PI / 2, 0);

        health = 150;
    }

}
