package mygame.ZombiesPacket;

import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;

import com.jme3.scene.Node;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;

public class Zombie01 extends Zombie {

    private static Node model;

    public Zombie01() {

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

        health = 100;
        attackPower = 30;
        attackSpeed = 3;
        movingSpeed = 3.0f;

    }

    public static void loadmodle(AssetManager asset) {

        assetManager = asset;
        model = (Node) assetManager.loadModel("Blender/zombie01/zombie.j3o");
        model = (Node) model.getChild("zombie");
        model.addLight(new AmbientLight());

    }

}
