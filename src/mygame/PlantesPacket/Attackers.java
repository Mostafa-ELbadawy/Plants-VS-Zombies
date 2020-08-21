/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.PlantesPacket;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import java.util.HashMap;
import mygame.allObjects.Bullet;

/**
 *
 * @author DELL
 */
public abstract class Attackers extends plant {

    protected float attackPower, attackSpeed, lastAttack, effect, effectTime;

    public Attackers() {
        super();
        attackPower = 30;
        attackSpeed = 3;
        lastAttack = -1;
        effect = 0;

    }

    @Override
    public void setstatus(float tpf, float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {

        CollisionResults results = new CollisionResults();
        Ray sight = new Ray(node.getWorldTranslation().add(0, 5, 1f), new Vector3f(1, 0, 0));
        node.getParent().collideWith(sight, results);
        boolean isAttack = false;
        for (int i = 0; i < results.size(); i++) {

            String hitName = results.getCollision(i).getGeometry().getName();
            if (hitName.equals("zombie")) {
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

    public abstract void attack(float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing);
    
    
    
    public float getLastAttack() {
        return lastAttack;
    }

    public void setLastAttack(float lastAttack) {
        this.lastAttack = lastAttack;
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

    public float getEffect() {
        return effect;
    }

    public void setEffect(float effect) {
        this.effect = effect;
    }

    public float getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(float effectTime) {
        this.effectTime = effectTime;
    }

}
