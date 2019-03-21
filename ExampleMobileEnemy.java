import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ExampleMobileEnemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ExampleMobileEnemy extends MobileEnemyTank
{
	/**
     * Make a new TurquoiseTank whose starting position will be at the given
     * coordinates.
     * @param startX The x coordinate the tank will be at the beginning of the
     * level.
     * @param startY The y coordinate the tank will be at the beginning of the
     * level.
	 * @param startRotation The starting rotation of this tank in the world.
     */
    public ExampleMobileEnemy(int startX, int startY, int startRotation)
    {
    	//simply calls the constructor of the superclass.
        super(startX, startY, startRotation);
        
        //initialise instance variables that you may add
    }
    
    /**
   	 * Prepares the turquoise tank for the beginning of the game. Sets the correct
   	 * starting orientation and gives this tank a TurquoiseTurret.
	 * @param world The game world this tank has just been added to.
   	 */
    @Override
    protected void addedToWorld(World world)
    {
    	this.setRotation(startRotation);
    	
    	//replace YellowTurret with any other type of turret that you want your new tank type to have
    	tankTurret=new YellowTurret(this);
    }
}
