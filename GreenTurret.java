import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class GreenTurret here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GreenTurret extends EnemyTurret
{
    /**
     * Makes a new GreenTurret on the tank given as an argument.
     * @param tank The tank on which this Turret will be placed.
     */
    public GreenTurret(Tank tank)
    {
    	//just call the super type constructor
    	super(tank);
    }
    
    public int getLiveShellLimit()
    {
        return 2;
    }
    
    public int getFireCooldown()
    {
        return 1000;
    }
    
    public int getAimAngle()
    {
        return 40;
    }
}
