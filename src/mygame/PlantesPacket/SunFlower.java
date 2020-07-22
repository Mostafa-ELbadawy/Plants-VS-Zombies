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
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.allObjects.Bullet;

/**
 *
 * @author DELL
 */
public class SunFlower extends Attackers {

    public SunFlower(AssetManager asset) {
        super(asset);

        Node node = (Node) assetManager.loadModel("Blender/sun_flower/sun_flower.j3o");
        this.node = node;
        this.node.setName("plant");
        node.setLocalScale(3f);
        name = "plant";
        Node zomb = (Node) node.getChild(name);
        control = zomb.getControl(AnimControl.class);
        channal = control.createChannel();

        phyControl = new RigidBodyControl(0);

        phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);

        this.node.addControl(phyControl);
        this.node.rotate(0, (float) Math.PI / 2, 0);

        attackPower = 25;
        attackSpeed = 15;
    }

    @Override
    public void attack(float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {

        if (lastAttack == -1) {
            lastAttack = timeNow + 3;
        }
        if (timeNow - lastAttack >= attackSpeed) {

            channal.setAnim("attacking");
            channal.setLoopMode(LoopMode.DontLoop);
            lastAttack = timeNow;
            Sun.addSun(getNode().getLocalTranslation().add(1, 5, 0));

        } else {
            idel();
        }
    }

    @Override
    public void setstatus(float tpf, float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {
        attack(timeNow, space, hashing);
    }

}
