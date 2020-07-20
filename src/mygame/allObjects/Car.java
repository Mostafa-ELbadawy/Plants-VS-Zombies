/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.allObjects;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author DELL
 */
public class Car {

    private Node node;
    private RigidBodyControl phyControl;
    private final float speed = 15f;
    private boolean mov = false;
    private final AnimControl animControl;
    private final AnimChannel animChannel;

    private final AssetManager assetManager;

    public Car(AssetManager asset) {
        assetManager = asset;

        node = (Node) assetManager.loadModel("Blender/car/car.j3o");
        this.node.setName("Car");

        phyControl = new RigidBodyControl(0);

        phyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        phyControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        node.addControl(phyControl);
        animControl = node.getChild("Bag").getControl(AnimControl.class);
        animChannel = animControl.createChannel();
    }

    public void move(float tpf) {
        phyControl.setEnabled(false);
        node.move(speed * tpf, 0, 0);
        phyControl.setEnabled(true);

        if (animChannel.getAnimationName() == null || !animChannel.getAnimationName().equals("move")) {
            animChannel.setAnim("move");
            animChannel.setLoopMode(LoopMode.Loop);
        }

    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public RigidBodyControl getPhyControl() {
        return phyControl;
    }

    public void setPhyControl(RigidBodyControl phyControl) {
        this.phyControl = phyControl;
    }

    public boolean setstatus(float tpf) {

        if (mov) {
            move(tpf);
            return mov;
        }
        CollisionResults results = new CollisionResults();
        Ray sight = new Ray(node.getWorldTranslation().add(0, 5, 1f), new Vector3f(1, 0, 0));
        node.getParent().collideWith(sight, results);

        for (int i = 0; i < results.size(); i++) {
            String hitName = results.getCollision(i).getGeometry().getName();
            float dist = results.getCollision(i).getDistance();

            if (hitName.equals("zombie") && dist < 5f) {
                move(tpf);
                return mov = true;
            } else if (dist > 5f) {
                break;
            }
        }
        return mov;

    }

}
