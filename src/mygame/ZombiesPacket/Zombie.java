/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.ZombiesPacket;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.HashMap;
import java.util.LinkedList;
import mygame.PlantesPacket.plant;

/**
 *
 * @author DELL
 */
public abstract class Zombie {

    private int row;
    protected String name;
    protected float attackPower, attackSpeed, movingSpeed, lastattack, poisonEffect, poisonTime, lastPoison, health;

    protected Node node;
    protected AnimControl control;
    protected AnimChannel channal;
    protected RigidBodyControl phyControl;
    protected static AssetManager assetManager;

    public Zombie() {
        name = null;
        health = 100;
        attackPower = 30;
        attackSpeed = 3;
        movingSpeed = 1.0f;
        lastattack = -100;
        row = 0;
        poisonEffect = 0;
        poisonTime = 0;
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

    public boolean setstatus(float tpf, LinkedList<plant> plan, HashMap<Node, plant> hashingplant, float timeNow, PhysicsSpace space, plant[][] floor) {

        int home = 0;
        if (timeNow - lastPoison < poisonTime) {
            //color
            node.getLocalLightList().get(0).setColor(ColorRGBA.Blue);
        } else {
            //color wight
            node.getLocalLightList().get(0).setColor(ColorRGBA.White);
            poisonEffect = 0;
        }

        CollisionResults results = new CollisionResults();
        Ray sight = new Ray(node.getWorldTranslation().add(0, 4, 1f), new Vector3f(-1, 0, 0));
        node.getParent().collideWith(sight, results);
        boolean mov = true;
        for (int i = 0; i < results.size(); i++) {
            String hitName = results.getCollision(i).getGeometry().getName();
            float dist = results.getCollision(i).getDistance();
            if (hitName.equals("plant") && dist < 2.5f) {

                if (timeNow - lastattack >= attackSpeed - poisonEffect * 2) {
                    lastattack = timeNow;
                    plant p = hashingplant.get(results.getCollision(i).getGeometry().getParent().getParent().getParent());
                    attack(plan, space, p, hashingplant, floor);

                }
                return false;
            } else if (hitName.equals("Home")) {
                home++;
            }
        }
        move(tpf);
        return (home < 2);
    }

    public void move(float tpf) {

        if (channal.getAnimationName() == null || !channal.getAnimationName().equals("walking")) {
            channal.setAnim("walking");
            channal.setLoopMode(LoopMode.Loop);
        }

        phyControl.setEnabled(false);
        
        float speed= -(movingSpeed + poisonEffect);
        if (this instanceof Zombie04)
            speed-=poisonEffect;
        
       node.move(speed * tpf, 0, 0);
        phyControl.setEnabled(true);

    }

    public void attack(LinkedList<plant> plan, PhysicsSpace space, plant p, HashMap<Node, plant> hashingplant, plant[][] floor) {

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
                floor[p.getRow()][p.getCol()] = null;

            }
        } catch (Exception e) {
        }

    }

    public void damage(float dam) {
        health -= dam;
    }

    public boolean isDamaged() {
        return (health <= 0);
    }

    public void dying() {
        channal.setAnim("dying");
        channal.setLoopMode(LoopMode.DontLoop);

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

    public float getPoisonEffect() {
        return poisonEffect;
    }

    public void setPoisonEffect(float poisonEffect) {
        this.poisonEffect = poisonEffect;
    }

    public float getPoisonTime() {
        return poisonTime;
    }

    public void setPoisonTime(float poisonTime) {
        this.poisonTime = poisonTime;
    }

    public float getLastPoison() {
        return lastPoison;
    }

    public void setLastPoison(float lastPoison) {
        this.lastPoison = lastPoison;
    }

    public AnimControl getControl() {
        return control;
    }

    public void setControl(AnimControl control) {
        this.control = control;
    }

    public RigidBodyControl getPhyControl() {
        return phyControl;
    }

    public void setPhyControl(RigidBodyControl phyControl) {
        this.phyControl = phyControl;
    }

}
