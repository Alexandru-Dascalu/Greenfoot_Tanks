import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ExampleStaticTank here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GreenTank extends StaticEnemyTank
{
    /**
     * Makes a new GreenTank, with it's start x and y coordinates the ones given
     * as arguments.
     * @param startX The starting x coordinate of this tank.
     * @param startY The starting y coordinate of this tank.
     * @param startRotation The starting rotation of this tank in the world.
     */
    public GreenTank(int startX, int startY, int startRotation)
    {
    	//simply calls the constructor of the superclass.
    	super(startX,startY, startRotation);
    }
    
    /**
     * Overrides the superclass addedToWorld method so that a Brown Turret will be 
     * placed on this tank not a simple Turret object.
     * @param world The game world this tank has just been added to.
     */
    @Override
    protected void addedToWorld(World world)
    {
    	setRotation(startRotation);
    	tankTurret = new GreenTurret(this);
    }
}
