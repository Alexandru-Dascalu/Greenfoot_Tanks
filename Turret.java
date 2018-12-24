import greenfoot.Actor;
import greenfoot.World;

/**
 * <p><b>File name: </b> Turret.java
 * @version 1.3
 * @since 03.05.2018
 * <p><b>Last modification date: </b> 14.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>Turret.java is part of Panzer Batallion.
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
 * <p> This class models a general tank turret for a Greenfoot recreation of the 
 * Wii Tanks game for the Nintendo Wii. 
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Made this class a general tank turret and moved code to the 
 * PlayerTurret subclass.
 * <p>	-1.2 - Encapsulated keeping track of the number of shells fired by this
 * tank that are still in the world in this class and not the Tank class.
 * <p>	-1.3 - Made the shells be fired from the end of the turret's gun, not 
 * from the centre of the turret.
 */

public abstract class Turret extends Actor
{
	/**The length of this turret's gun barrel. It's value is {@value}.*/
	private static final int BARREL_LENGTH=52;
	
	/**The tank this shell is placed on.*/
    protected Tank tank;
    
    /**The number of shells still in the game world that have been fired by this
     * turret.*/
    protected int liveShells;
    
    /**
     * Makes a new turret on top of the given tank.
     * @param tank The tank of this turret, on top of which it will be placed.
     */
    public Turret(Tank tank)
    {
    	//make a new Turret and initialize attributes
    	super();
        this.tank=tank;
        liveShells=0;
        
        /*Add the turret to the world on this tank. The if statement is put to 
         * avoid potential NullPointerException, though the given tank should
         * already be in the world.*/
        TankWorld world=tank.getWorldOfType(TankWorld.class);
        if(world!=null)
        {
            world.addObject(this,tank.getX(),tank.getY());
            //make sure the image of the turret is on top of the tank's image
            tank.getImage().drawImage(this.getImage(),tank.getX(),tank.getY());
        }
    }
     
    /**
	 * Prepares the turret for the beginning of the game. Sets the rotation 
	 * to be that of it's tank. Method is called automatically after the turret
	 * is added to the world. 
	 * @param world The game world this tank has just been added to.
	 */
    @Override
    protected void addedToWorld(World world)
    {
    	setRotation(tank.getRotation());
    }
    
    /**Makes the turret fire a new shell.*/
    public void fire()
    {
    	/*Make a new shell at the end of the turret's gun barrel with the rotation
    	 * of this turret, the turret's tank as it's parent tank. The shell is
    	 * added to the world by it's constructor so nothing else is needed.*/
    	
    	/*Check if this turret is firing normal shells.*/
    	if(getShellType() == Shell.class)
    	{
    		//if so, fire a normal shell by making a new one at the end of the
    		//barrel of this turret
    		Shell tankShell = new Shell(getRotation(), tank, getShellX(), 
        			getShellY());
    	}
    	/*Else, check if this turret is firing rocket shells.*/
    	else if(getShellType() == RocketShell.class)
    	{
    		//if so, fire a rocket shell by making a new one at the end of the
    		//barrel of this turret
    		RocketShell tankShell = new RocketShell(getRotation(), tank, 
    				getShellX(), getShellY());
    	}
    	else if(getShellType()==RocketShellMk2.class)
    	{
    		//if so, fire a rocket shell mk2 by making a new one at the end of the
    		//barrel of this turret
    		RocketShell tankShell = new RocketShellMk2(getRotation(), tank, 
    				getShellX(), getShellY());
    	}
    	
    	//increment the number of shells in the world fired by this turret
    	liveShells++;
    }
    
    /**Makes the turret aim. */
    public abstract void aim();
    
    /**
     * Calculates the x position of the end of the turret's gun barrel.
     * @return The x coordinate where the turret's gun barrel ends.
     */
    protected int getShellX()
    {
    	/*The x position is that of the turret added with the projection of the
    	 * length of the gun barrel on the horizontal axis (calculated with the
    	 * cosine of the turret's rotation).*/
    	int rotation = getRotation();
    	int shellX = getX() + (int)(BARREL_LENGTH * Math.cos(Math.toRadians(rotation)));
    	
    	return shellX;
    }
    
    /**
     * Calculates the y position of the end of the turret's gun barrel.
     * @return The y coordinate where the turret's gun barrel ends.
     */
    protected int getShellY()
    {
    	/*The y position is that of the turret added with the projection of the
    	 * length of the gun barrel on the vertical axis (calculated with the
    	 * sine of the turret's rotation).*/
    	int rotation = getRotation();
    	int shellY = getY() + (int)(BARREL_LENGTH * Math.sin(Math.toRadians(rotation)));
    	
    	return shellY;
    }
    
    /**Gets the limit of how many shells fired by this turret can be in the world
	 * at the same time. This number is a static variable and is the same for
	 * all objects of this class.It returns 0 because this method is meant to 
	 * be always overriden.
	 * @return the limit of how many shells fired by this turret can be in the world
	 * at the same time.*/
	public abstract int getLiveShellLimit();
	
    /**Decrements the number of shells in the game world fired by this turret.
     * Since removing the shells is handled in the TankWorld class, we need
     * a public method to decrement this counter when a shell is removed.*/
    public void decLiveShells()
    {
    	if(liveShells < 1)
    	{
    		throw new IllegalStateException("Can not decrement the counter of " +
    				"live shells when it is 0 or less!");
    	}
    	
    	liveShells--;
    }
    
    /**Removes the turret from the game world along with all associated actors.
     * By default, it just removes the turret from the game world, but other
     * actors may be needed to be removed for some subclasses.*/
    public void deleteTurret()
    {
    	World world=getWorld();
    	
    	/*Check if the world is not null, since this method may be called after
    	 * the turret is not in the world after the level has ended because all
    	 * remaining enemies were destroyed by the land mine explosion.*/
    	if(world!=null)
    	{
    		world.removeObject(this);
    	}
    }
    
    /**
	 * Indicates the type of shell fired by this turret.
	 * @return The type of shell fired by this turret, indicated by a Class 
	 * object.
	 */
    public Class<? extends Shell> getShellType()
    {
    	return Shell.class;
    }
}
