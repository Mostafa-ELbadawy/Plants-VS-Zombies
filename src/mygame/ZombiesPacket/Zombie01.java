package mygame.ZombiesPacket;

import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;

import com.jme3.scene.Node;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;

public class Zombie01 extends Zombie {

    public Zombie01(AssetManager asset) {

        super(asset);

        Node node = (Node) assetManager.loadModel("Blender/zombie01/zombie.j3o");
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
        
        health = 100;
        attackPower = 30;
        attackSpeed = 3;
        movingSpeed = 3.0f;
        

    }

}
