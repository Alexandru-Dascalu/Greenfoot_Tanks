import greenfoot.*; 

/**
 * <p><b>File name: </b> Turret.java
 * @version 1.3
 * @since 03.05.2018
 * <p><p><b>Last modification date: </b> 11.07.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
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

public class Turret extends Actor
{
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
	 * is added to the world. */
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
    	Shell tankShell=new Shell(this.getRotation(), tank, getShellX(), getShellY());
    	
    	//increment the number of shells in the world fired by this turret
    	liveShells++;
    }
    
    /**Makes the turret aim. By default it does nothing and the method is just 
     * here so it can be overriden.*/
    public void aim()
    {
    	
    }
    
    /**
     * Calculates the x position of the end of the turret's gun barrel.
     * @return The x coordinate where the turret's gun barrel ends.
     */
    protected int getShellX()
    {
    	/*The x position is that of the turret added with the projection of the
    	 * length of the gun barrel on the horizontal axis (calculated with the
    	 * cosine of the turret's rotation).*/
    	int rotation=getRotation();
    	int shellX=getX()+(int)(30*Math.cos(Math.toRadians(rotation)));
    	
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
    	int rotation=getRotation();
    	int shellY=getY()+(int)(30*Math.sin(Math.toRadians(rotation)));
    	
    	return shellY;
    }
    
    /**Decrements the number of shells in the game world fired by this turret.
     * Since removing the shells is handled in the TankWorld class, we need
     * a public method to decrement this counter when a shell is removed.*/
    public void decLiveShells()
    {
    	liveShells--;
    }
    
    /**Removes the turret from the game world along with all associated actors.
     * By default, it just removes the turret from the game world, but other
     * actors may be needed to be removed for some subclasses.*/
    public void deleteTurret()
    {
    	World world=getWorld();
    	world.removeObject(this);
    }
}
