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
import com.jme3.bullet.PhysicsSpace;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author DELL
 */
public class plant {

    private final AssetManager assetManager;
    private float health;
    private float attackPower, attackSpeed, lastAttack;
    protected Node node;
    protected AnimControl control;
    protected AnimChannel channal;
    protected String name;
    private int row;
    public RigidBodyControl phyControl;

    public plant(AssetManager asset) {
        health = 100;
        this.assetManager = asset;
        attackPower = 10;
        attackSpeed = 3;
        node = null;
        name = null;
        row = 0;
        lastAttack = -1;

    }

    public void setstatus(float tpf, float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {

        CollisionResults results = new CollisionResults();

        Ray sight = new Ray(node.getWorldTranslation().add(0, 1, 0.05f), new Vector3f(1, 0, 0));

        node.getParent().collideWith(sight, results);

        boolean isAttack = false;
        for (int i = 0; i < results.size(); i++) {
            // For each hit, we know distance, impact point, name of geometry.

            String hitName = results.getCollision(i).getGeometry().getName();
            //    System.out.println("i= " + i + " name= " + hitName );

            if (hitName.equals("Yaku_zombie1")) {
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

    private void idel() {
        if (channal != null) {
            if (channal.getAnimationName()==null||!channal.getAnimationName().equals("idel"))
            {
                channal.setAnim("idel");
                channal.setLoopMode(LoopMode.Loop);
            }
        
        } 
    }

    public float getLastAttack() {
        return lastAttack;
    }

    public void setLastAttack(float lastAttack) {
        this.lastAttack = lastAttack;
    }

    public RigidBodyControl getPhyControl() {
        return phyControl;
    }

    public void setPhyControl(RigidBodyControl phyControl) {
        this.phyControl = phyControl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void attack(float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {

        if (timeNow - lastAttack >= attackSpeed) {

            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Blue);

            Bullet bullet = new Bullet(attackPower, node.getParent(), space, mat, node.getLocalTranslation().add(0.5f, 1.5f, 0));

            hashing.put(bullet.getNode(), bullet);
            lastAttack = timeNow;

            channal.setAnim("attacking");
            channal.setLoopMode(LoopMode.DontLoop);

        }

    }
    public void damage(float dam) {
        health -= dam;
    }

    public boolean isDamaged() {
        return (health <= 0);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(float attackPower) {
        this.attackPower = attackPower;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node model) {
        this.node = model;
    }

}
