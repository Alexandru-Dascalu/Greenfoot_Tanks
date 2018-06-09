import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class LandMine here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LandMine extends Actor
{
	private static final int COUNT_DOWN=12500;
	private static final int START_FLASHING=8000;
	private static final int FLASH_INTERVAL=500;
	private static final int EXPLOSION_RANGE=80;
	
	private final long creationTime;
	
	public LandMine()
	{
		creationTime=System.currentTimeMillis();
	}
	
    /**
     * Act - do whatever the LandMine wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
    	long currentTime=System.currentTimeMillis();
    	
    	if (currentTime>creationTime+START_FLASHING)
    	{
    		flashRed();
    	}
    	
        if(currentTime>creationTime+COUNT_DOWN)
        {
        	explode();
        }
    }    
    
    private void flashRed()
    {
    	long currentTime=System.currentTimeMillis();
    	
    	if(((currentTime-START_FLASHING)/FLASH_INTERVAL)%2==0)
    	{
    		setImage("redLandMine.png");
    	}
    	else
    	{
    		setImage("greenLandMine.png");
    	}
    }
    
    private void explode()
    {
    	List<Tank> destroyedTanks=getObjectsInRange(EXPLOSION_RANGE, Tank.class);
    	List<Shell> destroyedShells=getObjectsInRange(EXPLOSION_RANGE, Shell.class);
    	List<WallBlock> destroyedWalls=getObjectsInRange(EXPLOSION_RANGE, WallBlock.class);
    	List<LandMine> destroyedMines=getObjectsInRange(EXPLOSION_RANGE, LandMine.class);
    	
    	TankWorld world=getWorldOfType(TankWorld.class);
    	
    	for(Tank t: destroyedTanks)
    	{
    		t.deleteTank();
    	}
    	
    	for(Shell s: destroyedShells)
    	{
    		world.removeObject(s);
    	}
    	
    	for(WallBlock wb: destroyedWalls)
    	{
    		world.removeObject(wb);
    	}
    	
    	for(LandMine lm: destroyedMines)
    	{
    		world.removeObject(lm);
    	}
    	
    	getWorld().removeObject(this);
    }
}
