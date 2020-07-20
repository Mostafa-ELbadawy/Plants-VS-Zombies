/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.allObjects;

import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import java.util.HashMap;

/**
 *
 * @author DELL
 */
public abstract class Attackers extends plant {

    protected float attackPower, attackSpeed, lastAttack,effect,effectTime;

    public  Attackers(AssetManager asset) {
        super(asset);
        attackPower = 200;
        attackSpeed = 3;
        lastAttack = -1;
        effect=0;
         setAttackPower(0);
        

    }

    @Override
    public void setstatus(float tpf, float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {

        
        CollisionResults results = new CollisionResults();

        Ray sight = new Ray(node.getWorldTranslation().add(0, 5,0f), new Vector3f(1, 0, 0));

        node.getParent().collideWith(sight, results);
        boolean isAttack = false;
        for (int i = 0; i < results.size(); i++) {
       
            String hitName = results.getCollision(i).getGeometry().getName();
                //System.out.println("i= " + i + " name= " + hitName );

            if (hitName.equals("zombie")) {
                isAttack = true;
                break;

            }

        }

    ///      System.out.println("///////////////////////////////////////////////////////");
        if (isAttack) {
            attack(timeNow, space, hashing);
        } else {
            idel();
        }

    }

    public float getLastAttack() {
        return lastAttack;
    }

    public void setLastAttack(float lastAttack) {
        this.lastAttack = lastAttack;
    }

    public void attack(float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {

        if (timeNow - lastAttack >= attackSpeed) {

            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Brown);

            Bullet bullet = new Bullet(effect,attackPower,effectTime, node.getParent(), space, mat, node.getLocalTranslation().add(0.5f, 3f, 0));

            hashing.put(bullet.getNode(), bullet);
            lastAttack = timeNow;

            channal.setAnim("attacking");
            channal.setLoopMode(LoopMode.DontLoop);

        }

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
