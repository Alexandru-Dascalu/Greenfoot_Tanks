import greenfoot.*;
import java.util.List;

/**
 * <p><b>File name: </b> Shell.java
 * @version 1.4
 * @since 04.06.2018
 * <p><b>Last modification date: </b> 05.10.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>LandMine.java is part of Panzer Batallion.
 * Panzer Batallion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * <p>You should have received a copy of the GNU General Public License v3
 * along with this program.  If not, see <a href="https://www.gnu.org/licenses/">https://www.gnu.org/licenses/</a> .
 * 
 * <p>A summary of the license can be found here: 
 * <a href="https://choosealicense.com/licenses/gpl-3.0/">https://choosealicense.com/licenses/gpl-3.0/</a> .
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
 * <p>	-1.3 - Fixed some bugs relating to exceptions appearing after a tank was
 *  destroyed by a landmine and as a result the level was cleared or reloaded. 
 *  Also fixed a bug that made the landmine not explode when being stepped on 
 *  by enemy tanks.
 * <p>	-1.4 - Added getters used by code for the suicide attack of Yellow Tanks.
 */

public class LandMine extends Actor
{
	/**The time in milliseconds from the moment the mine is laid down when the
	 * mine will explode. Its value is {@value}.*/
	protected static final int COUNT_DOWN=12500;
	
	/**The time in milliseconds from the moment the mine is laid down when the
	 * mine will staprotectedrt to flash red Its value is {@value}.*/
	protected static final int START_FLASHING=8000;
	
	/**The time interval in milliseconds when the mine will flash from red to 
	 * green or from green to red. Its value is {@value}.*/
	protected static final int FLASH_INTERVAL=500;
	
	/**The radius around the mine where all tanks,shells,mines and walls will
	 * be destroyed if their centre is inside the radius. Its value is {@value}.*/
	public static final int EXPLOSION_RANGE=140;
	
	/**The system time in milliseconds when the mine was laid down.*/
	protected final long creationTime;
	
	/**The tank that laid down this mine.*/
	protected Tank parentTank;
	
	/**Since when it is first laid down this mine overlaps with it's parent tank,
	 * it would immediately destroy it since the tank "steps" on it. This boolean
	 * tells us if we need to destroy the parent tank. When the mine is made, it is
	 * set to false, and when the mine sees the parent tank does not overlap it
	 * anymore, it is set to true so that if the player steps on this mine again,
	 * it will explode.*/
	protected boolean destroyParent;
	
	/**A flag that tells the parent tank if it can ignore this mine when it detects
	 * it being too close. Since immediately after being laid, the mine is too 
	 * close to it's parent tank, the tank would normally want to avoid it. But 
	 * since all nodes around the tank are all too close to the mine, the path
	 * returned would be null.To avoid this, this flag is set to true until the 
	 * first time the parent tank is safely away from the mine (so that the tank
	 * will just continue it's path), after which if it encounters it again the 
	 * tank will avoid it as normal.
	 * 
	 * If the parent tank is not a MobileEnemyTank, then this flag is set to 
	 * false the first time the act() method is called.*/
	protected boolean canBeIgnoredByParent;
	
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
		canBeIgnoredByParent=true;
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
		//Check if this mine can be ignored by it's parent.
		if(canBeIgnoredByParent)
		{
			/*If so, calculate if the flag should be set to false.*/
			updateIgnoredByParent();
		}
		
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
	protected void flashRed()
    {
    	long timeSinceLaid=System.currentTimeMillis()-creationTime;
    	
    	/*We flash red by changing the image of the mine between an image where
    	 * the mine's centre is red and one where the mine's centre is green.
    	 * We do this every FLASH_INTERVAL milliseconds.*/
    	if(((timeSinceLaid-START_FLASHING)/FLASH_INTERVAL)%2==0)
    	{
    		/*To do this, we take the time passed since it started to flash, MOD
    		 * divide it by FLASH_INTERVAL to see how many intervals have passed.
    		 * If an even number of intervals have passed, the land mine is red.*/
    		setImage("redMine.png");
    	}
    	/* If an odd number of intervals have passed, the land mine is green.*/
    	else
    	{
    		setImage("greenMine.png");
    	}
    }
    
    /**Makes the mine explode and destroy tanks, mines, shells and walls if their
     * centre are in the explosion range of this mine.*/
	protected void explode()
    {
    	/*When we remove a shell, we need to update the live shell counter of it's
    	 * parent tank. When we remove a tank, we need to remove their turret and
    	 * update UI elements, we make need to reload the level, etc. For this, 
    	 * TankWorld has overloaded removeObject methods for them, and we need 
    	 * these objects to be casted as Tanks or shells for this to work.
    	 * So we make 4 different lists for the types of actors we 
    	 * need to remove, since we need to treat shells and tanks differently.
    	 * We make a list for tanks later since we do that after making sure 
    	 * the player tank has been removed if needed. Because if an explosion
    	 * destroys the player tank and the last remaining enemy tank, this 
    	 * ensures the world knows the player lost and reloads the level.*/
    	List<Shell> destroyedShells=getObjectsInRange(EXPLOSION_RANGE, Shell.class);
    	List<DestroyableWallBlock> destroyedWalls=getObjectsInRange(EXPLOSION_RANGE,
    			DestroyableWallBlock.class);
    	List<LandMine> destroyedMines=getObjectsInRange(EXPLOSION_RANGE, LandMine.class);
    	
    	//we need a reference to the world of type TankWorld
    	TankWorld world = (TankWorld) getWorld();
    	
    	/*If the player tank is destroyed in the explosion, it does not matter 
    	 * what else happens, the level is lost and will be reloaded or the 
    	 * game is over. So we remove the player tank with the overloaded 
    	 * removeObject method which will also reload the level. Therefore, the 
    	 * player tank is removed first to avoid removing other actors that will 
    	 * be removed when the level is reloaded anyway.
    	 * Get a list that contains the player tank if it is in range and remove it if needed.*/
    	List<PlayerTank> playerTank=getObjectsInRange(EXPLOSION_RANGE, PlayerTank.class);
    	for(PlayerTank p: playerTank)
    	{
    		world.removeObject(p);
    		
    		/*If the player tank was destroyed, the overloaded removeObject() method
    		 * reloads the level. Since then this landmine has been removed by the 
    		 * reloadLevel() method and all other enemy tanks have been reset, we 
    		 * terminate the execution of this method to avoid exceptions caused 
    		 * by this land mine already not being in the world.*/
    		return;
    	}
    	
    	/*Remove each shell in the radius with the overloaded removeObject method.*/
    	for(Shell s: destroyedShells)
    	{
    		world.removeObject(s);
    	}
    	
    	/*Remove each destroyable wall block in the radius with the default 
    	 * removeObject method.*/
    	for(DestroyableWallBlock destroyableBlock: destroyedWalls)
    	{
    		world.removeObject(destroyableBlock);
    	}
    	
    	/*Remove each wall block in the radius with the default removeObject method.*/
    	for(LandMine landMine: destroyedMines)
    	{
    		world.removeObject(landMine);
    	}
    	
    	/*Remove each tank in the radius with the overloaded removeObject method.*/
    	List<Tank> destroyedTanks=getObjectsInRange(EXPLOSION_RANGE, Tank.class);
    	for(Tank t: destroyedTanks)
    	{
    		world.removeObject(t);
    	}
    	   	
    	//remove this land mine from the world
    	world.removeObject(this);
    }
    
    /**
     * Determines if this mine has just been hit by a shell.
     * @return True if it has been hit by a shell, false if not.
     */
	protected boolean hitByShell()
    {
    	/*The mines is hit by a shell if the list of shells that intersect it
    	 * is not empty.*/
    	return !getIntersectingObjects(Shell.class).isEmpty();
    }
    
    /*Decides if the canBeIgnoredByParent flag can be set to false.*/
	protected void updateIgnoredByParent()
    {
    	/*To get the mine avoidance distance, the parentTank should be cast as 
    	 * a MobileEnemyTank.*/
    	MobileEnemyTank tank=null;
    	try
    	{
    		//cast the parent tank as a MobileEnemyTank
    		tank=(MobileEnemyTank)parentTank;
	   	}
	   	/*If the parent tank is not a MobileEnemyTank, catch the exception, set
	   	 * the flag to false and terminate this method.*/
	   	catch(ClassCastException e)
	   	{
	   		canBeIgnoredByParent=false;
	   		return;
	   	}
    	
    	/*Check if the parent tank is not in the world.*/
    	if(parentTank.getWorld()==null)
    	{
    		/*if so, set the flag to false so this method will not be called again
    		 * so as to avoid exceptions*/
    		canBeIgnoredByParent=false;
    		return;
    	}
    	
    	//get the horizontal and vertical distances based on the coordinates 
    	//of each node.
    	int xDistance=parentTank.getX()-getX();
    	int yDistance=parentTank.getY()-getY();
    	
    	/*Calculate the distance using Pythagora's theorem.*/
    	int distance=(int)Math.sqrt((xDistance*xDistance)+(yDistance*yDistance));
    	
    	/*multiply the multiplier of this type of tank with the tanl length to 
    	 * get the actual mine avoidance distance*/
    	int mineAvoidanceDistance = (int)(tank.getMineAvoidanceDistance()*Tank.LENGTH);
    	
    	/*Check if the distance between this mine and it's parent tank is bigger
    	 * than this tank's mine avoidance distance.*/
    	if(distance>mineAvoidanceDistance)
    	{
    		canBeIgnoredByParent=false;
    	}
    }
    
    /**
     * Determines if the mine is being stepped on by a tank.
     * @return True if the mine is being stepped on by a tank, false if not.
     */
	protected boolean steppedOnByTank()
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
    	else if (tanks.size()==1)
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
    
    /**
     * Gets the tank that laid this mine.
     * @return the tank that laid this mine.
     */
    public Tank getParentTank()
    {
    	return parentTank;
    }
    
    /**Gets the flag that indicates if this mine can destroy it's parent tank if 
     * that is the only tank stepping on it.
     * @return True if this mine can destroy it's parent tank, false if not.
     */
    public boolean getDestroyParent()
    {
    	return destroyParent;
    }
    
    /**
     * Gets the flag that tells if the parent tank can safely ignore this mine.
     * Since immediately after being laid, the mine is too close to it's parent
     * tank, the tank would normally want to avoid it. But since all nodes 
     * around the tank are all too close to the mine, the path returned would 
     * be null.To avoid this, this flag is set to true until the first time the
     * parent tank is safely away from the mine (so that the tank will just 
     * continue it's path), after which if it encounters it again the tank will 
     * avoid it as normal.
     * @return the flag that tells if the parent tank can safely ignore this
     * mine.
     */
    public boolean canBeIgnoredByParent()
    {
    	return canBeIgnoredByParent;
    }
}

