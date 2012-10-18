/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arrowpro.arrow;

import arrowpro.equations.StatSolver;

import net.minecraft.server.Entity;

/**
 * Holds abstract methods with the same name, return type, and parameters of all the methods that are to be modified in the net.minecraft.server.Arrow class.
 * 
 * ProArrow calls upon an instance of this class to run its methods.
 * 
 * @author Justin Stauch
 * @since August 30, 2012
 */
public abstract class ProArrowAction {
    private static StatSolver speed, damage;
    
    public void shoot(ProArrow arrow) {
        
    }
    
    public void inGround(ProArrow arrow) {
        
    }
    
    public void entityHit(ProArrow arrow, Entity entity) {
        
    }
    
    public static void setSpeedEquation(StatSolver equation) {
        speed = equation;
    }
    
    public static void setDamageEquation(StatSolver equation) {
        damage = equation;
    }
    
    public float getSpeed(int level, double def) {
        return (float) speed.solve(level, def);
    }
    
    public double getDamage(int level, double def) {
        return damage.solve(level, def);
    }
}