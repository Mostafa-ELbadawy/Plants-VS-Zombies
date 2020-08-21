/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.PlantesPacket;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.scene.Geometry;
import java.util.HashMap;
import mygame.allObjects.Bullet;

/**
 *
 * @author DELL
 */
public abstract class Defenders extends plant{
    
   
      public Defenders() {
        super();
    }
      
     public void setstatus(float tpf, float timeNow, PhysicsSpace space, HashMap<Geometry, Bullet> hashing) {
     idel();
     }
      
}
