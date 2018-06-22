import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * <p><b>File name: </b> Shell.java
 * @version 1.2
 * @since 04.06.2018
 * <p><b>Last modification date: </b> 10.06.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a land mine for a Greenfoot recreation of the Wii Tanks 
 * game for the Nintendo Wii. It explodes when hit by stepped on by a tank, when
 * hit by a shell. Otherwise, it start to flash red after a period and after a
 * while it explodes on it's own. When it explodes, it destroys tanks, shells,
 * wall blocks and othe mines in the vicinity.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created a land mine that flashes red after a perioed after it is 
 * laid down, then after a while it removes itself..
 * <p>	-1.1 - Made the mine explode and destroy walls, tanks, shells, mines in
 * it's vicinity. It also explodes when hit by a shell.
 * <p>	-1.2 - The mine now also explodes when stepped on by a tank.
 */

public class LandMine extends Actor
{
	/**The time in milliseconds from the moment the mine is laid down when the
	 * mine will explode. It's value is {@value}.*/
	private static final int COUNT_DOWN=12500;
	
	/**The time in milliseconds from the moment the mine is laid down when the
	 * mine will start to flash red It's value is {@value}..*/
	private static final int START_FLASHING=8000;
	
	/**The time interval in milliseconds when the mine will flash from red to 
	 * green or from green to red. It's value is {@value}.*/
	private static final int FLASH_INTERVAL=500;
	
	/**The radius around the mine where all tanks,shells,mines and walls will
	 * be destroyed if their centre is inside the radius. It's value is {@value}.*/
	private static final int EXPLOSION_RANGE=100;
	
	/**The system time in milliseconds when the mine was laid down.*/
	private final long creationTime;
	
	/**The tank that laid down this mine.*/
	private Tank parentTank;
	
	/**Since when it is first laid down this mine overlaps with it's parent tank,
	 * it would immediately destroy it since the tank "steps" on it. This boolean
	 * tells us if we need to destroy the parent tank.When the mine is made, it is
	 * set to false, and when the mine sees the parent tank does not overlap it
	 * anymore, it is set to true so that if the player steps on this mine again,
	 * it will explode.*/
	private boolean destroyParent;
	
	/**
	 * Make a new land mine, with the parent tank being the one given in the
	 * parameter. Needed because the mine needs to know not to explode right after
	 * it is laid (when it overlaps it's parent tank), and if afterwards the parent
	 * tank steps on it, to explode as it should.
	 * @param parentTank The tank that laid this mine.
	 */
	public LandMine(Tank parentTank)
	{
		creationTime=System.currentTimeMillis();
		this.parentTank=parentTank;
		destroyParent=false;
	}
	
    /**
     * Act - do whatever the LandMine wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * In this case, it flashes red if enough time has passed and it explodes if
     * a tank has stepped on it, if it has been hit by a shell or if it's count
     * down period has passed.
     */
	@Override
    public void act() 
    {
    	/*To decide if it needs to flash or explode, we need to know the current 
    	 * time.*/
    	long currentTime=System.currentTimeMillis();
    	
    	//Check if enough time has passed for the mine to flash.
    	if (currentTime>creationTime+START_FLASHING)
    	{
    		flashRed();
    	}
    	
    	/*Decide if the mine should explode.
    	 * It should only explode if the count down has passed or if it has been
    	 * hit by a shell or by a tank.*/
        if((currentTime>creationTime+COUNT_DOWN) || hitByShell() || 
        		steppedOnByTank())
        {
        	explode();
        }
    }    
    
	/** Makes the land mine flash red.*/
    private void flashRed()
    {
    	long timeSinceLaid=System.currentTimeMillis()-creationTime;
    	
    	/*We flash red by changing the image of the mine between an image where
    	 * the mine's center is red and one where the mine's center is green.
    	 * We do this every FLASH_INTERVAL milliseconds.*/
    	if(((timeSinceLaid-START_FLASHING)/FLASH_INTERVAL)%2==0)
    	{
    		/*To do this, we take the time passed since it started to flash, MOD
    		 * divide it by FLASH_INTERVAL to see how many intervals have passed.
    		 * If an even number of intervals have passed, the land mine is red.*/
    		setImage("redLandMine.png");
    	}
    	/* If an odd number of intervals have passed, the land mine is green.*/
    	else
    	{
    		setImage("greenLandMine.png");
    	}
    }
    
    /**Makes the mine explode and destroy tanks, mines, shells and walls if their
     * center are in the explosion range of this mine.*/
    private void explode()
    {
    	/*When we remove a shell, we need to update the live shell counter of it's
    	 * parent tank. When we remove a tank, we need to remove their turret and
    	 * update UI elements, we make need to reload the level, etc. For this, 
    	 * TankWorld has overloaded removeObject methods for them, and we need 
    	 * these objects to be casted as Tanks or shells for this to work. So we make 4
    	 * different lists for the types of actors we need to remove, since we need
    	 * to treat shells and tanks differently.*/
    	List<Tank> destroyedTanks=getObjectsInRange(EXPLOSION_RANGE, Tank.class);
    	List<Shell> destroyedShells=getObjectsInRange(EXPLOSION_RANGE, Shell.class);
    	List<WallBlock> destroyedWalls=getObjectsInRange(EXPLOSION_RANGE, WallBlock.class);
    	List<LandMine> destroyedMines=getObjectsInRange(EXPLOSION_RANGE, LandMine.class);
    	
    	//we need a reference to the world of type TankWorld
    	TankWorld world=getWorldOfType(TankWorld.class);
    	
    	/*Remove each tank in the radius with the overloaded removeObject method.*/
    	for(Tank t: destroyedTanks)
    	{
    		world.removeObject(t);
    	}
    	
    	/*Remove each shell in the radius with the overloaded removeObject method.*/
    	for(Shell s: destroyedShells)
    	{
    		world.removeObject(s);
    	}
    	
    	/*Remove each wall block in the radius with the default removeObject method.*/
    	for(WallBlock wb: destroyedWalls)
    	{
    		world.removeObject(wb);
    	}
    	
    	/*Remove each wall block in the radius with the default removeObject method.*/
    	for(LandMine lm: destroyedMines)
    	{
    		world.removeObject(lm);
    	}
    	
    	//finally, remove this land mine from the world
    	getWorld().removeObject(this);
    }
    
    /**
     * Determines if this mine has just been hit by a shell.
     * @return True if it has been hit by a shell, false if not.
     */
    private boolean hitByShell()
    {
    	/*The mines is hit by a shell if the list of shells that intersect it
    	 * is not empty.*/
    	return !getIntersectingObjects(Shell.class).isEmpty();
    }
    
    /**
     * Determines if the mine is being stepped on by a tank.
     * @return True if the mine is being stepped on by a tank, false if not.
     */
    private boolean steppedOnByTank()
    {
    	//get all tanks that intersect this land mine
    	List<Tank> tanks=getIntersectingObjects(Tank.class);
    	
    	/*If the mine doesnt overlap with it's parent tank anymore, next if it
    	 * does, we can destroy the parent tank.*/
    	if(!tanks.contains(parentTank))
    	{
    		destroyParent=true;
    	}
    	
    	/*Check if it is stepped on by a tank, taking into account that it
    	 * should not return true when the mine overlaps it's parent just after
    	 * it is laid.*/
    	if(tanks.size()>1)
    	{

        	/*If there are more than one tank stepping on it, at least one tank that
        	 * is not it's parent tank does so, so it can explode.*/
    		return true;
    	}
    	/*If only one tank steps on the mine, we need to check if it is the parent
    	 * or not.*/
    	else if (tanks.size()==1 && tanks.get(0)==parentTank)
    	{
    		/*If it is just the parent tank, it should only explode if destroyParent
    		 * is true.*/
    		if(tanks.get(0)==parentTank)
    		{
    			return destroyParent;
    		}
    		/*If another tank steps on it, the mine should explode.*/
    		else
    		{
    			return true;
    		}
    	}
    	/*If the size of the list is smaller than 1, no tanks step on this shell.*/
    	else
    	{
    		return false;
    	}
    }
}
