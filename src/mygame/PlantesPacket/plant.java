/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.PlantesPacket;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;

/**
 *
 * @author DELL
 */
public abstract class plant implements plantObject {

    protected static AssetManager assetManager;
    protected float health;
    protected Node node;
    protected AnimControl control;
    protected AnimChannel channal;
    protected String name;
    private int row, col;
    protected RigidBodyControl phyControl;

    public plant() {
        health = 100;
        node = null;
        name = null;
        row = 0;
        col = 0;

    }

    protected void idel() {
        if (channal != null) {
            if (channal.getAnimationName() == null || !channal.getAnimationName().equals("idel")) {
                channal.setAnim("idel");
                channal.setLoopMode(LoopMode.Loop);
            }

        }
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

    public Node getNode() {
        return node;
    }

    public void setNode(Node model) {
        this.node = model;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public AnimControl getControl() {
        return control;
    }

    public void setControl(AnimControl control) {
        this.control = control;
    }

}
