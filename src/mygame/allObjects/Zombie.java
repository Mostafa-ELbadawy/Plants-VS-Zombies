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
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author DELL
 */
public class Zombie {

    private float health;
    private int row;
    protected String name;
    private float attackPower, attackSpeed, movingSpeed, lastattack;
    protected Node node;
    protected AnimControl control;
    protected AnimChannel channal;
    public RigidBodyControl phyControl;

    public Zombie(AssetManager assetManager) {
        name = null;
        health = 100;
        attackPower = 30;
        attackSpeed = 3;
        lastattack = -100;
        movingSpeed = 1.0f;
        row = 0;
        node = new Node();

    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setstatus(float tpf, LinkedList<plant> plan, HashMap<Node, plant> hashingplant, float timeNow, PhysicsSpace space, plant[][] floor) {

        CollisionResults results = new CollisionResults();

        Ray sight = new Ray(node.getWorldTranslation().add(0, 1, 0.05f), new Vector3f(-1, 0, 0));

        node.getParent().collideWith(sight, results);

        boolean mov = true;
        for (int i = 0; i < results.size(); i++) {
            // For each hit, we know distance, impact point, name of geometry.

            String hitName = results.getCollision(i).getGeometry().getName();
            float dist = results.getCollision(i).getDistance();
            // System.out.println("i= " + i + " name= " + hitName + " dist= " + dist);

            if (hitName.equals("MocapGuy_Body1") && dist < 1f) {

                if (timeNow - lastattack >= attackSpeed) {
                    lastattack = timeNow;
                    plant p = hashingplant.get(results.getCollision(i).getGeometry().getParent().getParent().getParent().getParent());
                    attack(plan, space, p, hashingplant, floor);

                }
                return;
            }
        }
        move(tpf);

    }

    public void move(float tpf) {

        if (channal != null) {

            if (channal.getAnimationName() == null || !channal.getAnimationName().equals("walking")) {
                channal.setAnim("walking");
                channal.setLoopMode(LoopMode.Loop);
            }

        } else {
            System.out.println("channal is NULL");
        }
        phyControl.setEnabled(false);
        node.move(-movingSpeed * tpf, 0, 0);
        phyControl.setEnabled(true);

    }

    public void attack(LinkedList<plant> plan, PhysicsSpace space, plant p, HashMap<Node, plant> hashingplant,plant[][] floor) {

        if (!channal.getAnimationName().equals("attacking")) {
            channal.setAnim("attacking");
            channal.setLoopMode(LoopMode.Loop);
            return;
        }

        try {

            p.damage(attackPower);

            if (p.isDamaged()) {
                p.getNode().getParent().detachChild(p.getNode());
                hashingplant.remove(p.getNode(), p);
                plan.remove(p);
                space.remove(p.getNode().getControl(RigidBodyControl.class));
              floor [p.getRow()][p.getCol()]=null;

            }
        } catch (Exception e) {
            System.out.println("saaaaaad");
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

    public float getLastattack() {
        return lastattack;
    }

    public void setLastattack(float lastattack) {
        this.lastattack = lastattack;
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

    public float getMovingSpeed() {
        return movingSpeed;
    }

    public void setMovingSpeed(float movingSpeed) {
        this.movingSpeed = movingSpeed;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node model) {
        this.node = model;
    }

}
