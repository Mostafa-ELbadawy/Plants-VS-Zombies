/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.allObjects;

import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.HashMap;

/**
 *
 * @author DELL
 */
public class Jumper extends Attackers {

    public Jumper(AssetManager asset) {
        super(asset);

        Node node = (Node) assetManager.loadModel("Blender/Green_plant/plant.j3o");
        this.node = node;
        this.node.setName("plant");
        node.setLocalScale(5f);

        name = "MocapGuy_Teeth";
        Node zomb = (Node) node.getChild(name);
        control = zomb.getControl(AnimControl.class);
        channal = control.createChannel();

        phyControl = new RigidBodyControl(0);

        phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);

        this.node.addControl(phyControl);

        this.node.rotate(0, (float) Math.PI / 2, 0);
    }

    @Override
    public void attack(float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {

        if (!channal.getAnimationName().equals("attacking")) {
            channal.setAnim("attacking");
            channal.setLoopMode(LoopMode.DontLoop);
        }
    }

    @Override
    public void setstatus(float tpf, float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {
        CollisionResults results = new CollisionResults();

        Ray sight = new Ray(node.getWorldTranslation().add(0, 5, 0.25f), new Vector3f(1, 0, 0));

        node.getParent().collideWith(sight, results);
        boolean isAttack = false;
        for (int i = 0; i < results.size(); i++) {
            // For each hit, we know distance, impact point, name of geometry.

            String hitName = results.getCollision(i).getGeometry().getName();
            float dis = results.getCollision(i).getDistance();
//    System.out.println("i= " + i + " name= " + hitName );

            if (hitName.equals("zombie") && dis <= 5f) {
                isAttack = true;
                break;

            }

        }

        //  System.out.println("///////////////////////////////////////////////////////");
        if (isAttack) {
            attack(timeNow, space, hashing);
        } else {
            idel();
        }

    }

}
