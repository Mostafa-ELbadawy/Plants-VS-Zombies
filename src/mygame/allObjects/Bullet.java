/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.allObjects;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author DELL
 */
public class Bullet {
  
    
    private float power,speed;
    protected  Geometry node;
    

   
    
    

    public Bullet(float power,Node root,PhysicsSpace space, Material mat, Vector3f v, float speed) {
     
        this.power=power;
        this.speed=speed;
        createBall(root, space, mat, v);
        
    }
    
    public Bullet(float power,Node root,PhysicsSpace space, Material mat, Vector3f v) {
         this(power, root, space, mat, v, 5);
    }
    
    private void createBall(Node root,PhysicsSpace space, Material mat, Vector3f v)
    {
            Sphere bullet = new Sphere(32, 32, 0.1f, true, false);
            bullet.setTextureMode(Sphere.TextureMode.Projected);
            Geometry bulletg = new Geometry("bullet", bullet);
            node=bulletg;
            bulletg.setMaterial(mat);
            bulletg.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            bulletg.setLocalTranslation(v);
            RigidBodyControl bulletControl = new RigidBodyControl(2);
            //bulletControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);

            bulletControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
            bulletControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
            
            bulletg.addControl(bulletControl);
            bulletControl.setLinearVelocity(new Vector3f(speed,0, 0));
             root.attachChild(node);
             space.add(bulletControl);
             
          
        
        
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Geometry getNode() {
        return node;
    }

    public void setNode(Geometry node) {
        this.node = node;
    }

  
    

    
    
    
}
