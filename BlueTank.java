import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BlueTank here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BlueTank extends MobileEnemyTank
{
	/**
     * Make a new BlueTank whose starting position will be at the given
     * coordinates.
     * @param startX The x coordinate the tank will be at the beginning of the
     * level.
     * @param startY The y coordinate the tank will be at the beginning of the
     * level.
	 * @param startRotation The starting rotation of this tank in the world.
     */
    public BlueTank(int startX, int startY, int startRotation)
    {
    	//simply calls the constructor of the superclass.
        super(startX, startY, startRotation);
        
        //initialise instance variables that you may add
    }
    
    /**
   	 * Prepares the example tank for the beginning of the game. Sets the correct
   	 * starting orientation and gives this tank a TurquoiseTurret.
	 * @param world The game world this tank has just been added to.
   	 */
    @Override
    protected void addedToWorld(World world)
    {
    	this.setRotation(startRotation);
    	
    	tankTurret=new ExampleEnemyTurret(this);
    }
    
    public int getSpeed()
    {
        return 3;
    }
    
    public int getMaxTurnSpeed()
    {
        return 2;
    }
    
    public double getShellAvoidanceDistance()
    {
        return 2.3;
    }
    
    public double getMineAvoidanceDistance()
    {
        return 2.7;
    }
    
    public int getNumberOfMines()
    {
        return 3;
    }
    
    public int getMineLayingPeriod()
    {
        return 3000;
    }
}
