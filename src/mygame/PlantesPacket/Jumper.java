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
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.allObjects.Bullet;

/**
 *
 * @author DELL
 */
public class Jumper extends Attackers {

    private static Node model;

    public Jumper() {
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

        if (channal.getAnimationName() == null || !channal.getAnimationName().equals("attacking")) {
            channal.setAnim("attacking");
            channal.setLoopMode(LoopMode.DontLoop);
        }
    }

    @Override
    public void setstatus(float tpf, float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {

        CollisionResults results = new CollisionResults();
        Ray sight = new Ray(node.getWorldTranslation().add(0, 5, 1f), new Vector3f(1, 0, 0));
        node.getParent().collideWith(sight, results);
        boolean isAttack = false;

        for (int i = 0; i < results.size(); i++) {

            String hitName = results.getCollision(i).getGeometry().getName();
            float dis = results.getCollision(i).getDistance();

            if (hitName.equals("zombie") && dis <= 7f) {
                isAttack = true;
                break;
            }

        }

        if (isAttack) {
            attack(timeNow, space, hashing);
        } else {
            idel();
        }

    }

    public static void loadmodle(AssetManager asset) {

        assetManager = asset;
        model = (Node) assetManager.loadModel("Blender/Jumper/Jumper.j3o");
        RigidBodyControl phyControl = new RigidBodyControl(0);
        phyControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        model.addControl(phyControl);
        model.setName("plant");
        model.setLocalScale(2.5f);

    }

}
