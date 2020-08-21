/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.PlantesPacket;

import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.allObjects.Bullet;

/**
 *
 * @author DELL
 */
public class Grean_Plant extends Attackers {

    private static Node model;

    public Grean_Plant() {
        super();

        Node node = (Node) model.clone();
        this.node = node;

        name = "plant";
        Node child = (Node) node.getChild(name);
        control = child.getControl(AnimControl.class);
        channal = control.createChannel();

        phyControl = node.getControl(RigidBodyControl.class);

    }

    @Override
    public void attack(float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {

        if (timeNow - lastAttack >= attackSpeed) {

            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Green);
            Bullet bullet = new Bullet(effect, attackPower, effectTime, node.getParent(), space, mat, node.getLocalTranslation().add(1.5f, 4.2f, 0));

            hashing.put(bullet.getNode(), bullet);
            lastAttack = timeNow;

            channal.setAnim("attacking");
            channal.setLoopMode(LoopMode.DontLoop);
        }

    }

    public static void loadmodle(AssetManager asset) {

        assetManager = asset;
        model = (Node) assetManager.loadModel("Blender/Green_plant/Green_plant.j3o");
        RigidBodyControl phyControl = new RigidBodyControl(0);
        phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        model.addControl(phyControl);
        model.setName("plant");
        model.setLocalScale(2.5f);
        model.rotate(0, -(float) Math.PI / 2, 0);
    }

}
