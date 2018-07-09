import greenfoot.*; 

/**
 * <p><b>File name: </b> PlayerTurret.java
 * @version 1.2
 * @since 07.06.2018
 * <p><b>Last modification date: </b> 06.07.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a player turret for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It aims depending on the movement of
 * the player's mouse and fires when told to by the tank it is installed on.s
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Encapsulated the turret actions of firing and aiming into the
 * tank class, and made the necessary changes here.
 * <p>	-1.2 - Encapsulated mouse tracking in the player tank class and made the 
 * necessary changes here.
 */

public class PlayerTurret extends Turret
{
	/**The default x position of the player tank target. Needed because in the
	 * beginning we do not have any information about the location of the mouse.*/
	private static final int DEFAULT_MOUSE_X = 200;
	
	/**The default y position of the player tank target. Needed because in the
	 * beginning we do not have any information about the location of the mouse.*/
	private static final int DEFAULT_MOUSE_Y = 200;
	
	/**The maximum amount of shells this turret has fired that can be in the game
	 * world at the same time. If the limit is reached, the turret will not fire
	 * even if it is told to.*/
    private static final int SHELLS_ALLOWED = 6;
    
    /**The array of target line actors that will form a line between the turret
     * and the player tank target.*/
	private TargetLine[] targetLines;
	
	/**
	 * Makes a new PlayerTurret on top of the tank actor given as a parameter.
	 * @param tank The parent tank of this turret, on top of which it will be
	 * placed.
	 */
	public PlayerTurret(Tank tank)
	{
		/*Make the new turret and put it on it's parent tank.*/
		super(tank);
		
		/*Initialise the array of target line actors.*/
		TankWorld world=getWorldOfType(TankWorld.class);
		targetLines=new TargetLine[TargetLine.NR_LINES];
        Target playerTarget=world.getTankTarget();
       
        /* Make new target line actors and place them in the world so that they
         * form a line between the turret and the player target.*/
        for (int i = 0;  i< TargetLine.NR_LINES; i++)
        {
        	targetLines[i]=new TargetLine(this, playerTarget,i+1);
        	world.addObject(targetLines[i], targetLines[i].getNewX(), 
        			targetLines[i].getNewY());
        	
        	/*call their act methods to make sure they have the correct rotation 
        	 * from the beginning.*/
        	targetLines[i].act();
        }
	}
	
	/**
	 * Once the turret is added to the game world, it makes sure the turret is
	 * facing the location of the player target.
	 * @param world The world in which the turret was added.
	 */
	@Override
	protected void addedToWorld(World world)
	{
		TankWorld tankWorld=(TankWorld) world;
		Target target=tankWorld.getTankTarget();
		turnTowards(target.getX(),target.getY());
	}
	
	/**
	 * Makes the turret aim towards the player target.
	 */
	@Override
	public void aim()
	{
		//get information about the mouse from the player tank of this turret
		int mouseX, mouseY;
		MouseInfo mouseInfo=((PlayerTank)tank).getMouseInfo();
		
		/*Check if we have information about the mouse in the first place.*/
		if (mouseInfo == null)
		{
			//if we don't, use the default coordinates for the player target
			mouseX = DEFAULT_MOUSE_X;
			mouseY = DEFAULT_MOUSE_Y;
		} 
		//If we do, use it to get correct coordinates for the target.
		else
		{
			mouseX = mouseInfo.getX();
			mouseY = mouseInfo.getY();
		}

		//place the player target where the mouse cursor is
		TankWorld tankWorld = getWorldOfType(TankWorld.class);
		tankWorld.getTankTarget().setLocation(mouseX, mouseY);
		
		//turn the turret towards the player target
		this.turnTowards(mouseX, mouseY);	
	}

	/**
	 * Makes the turret fire the shell if the limit of live shells currently in
	 * the game world has not been reached.
	 */
	@Override
	public void fire()
	{
		/*Check if the limit of live shells currently in the game world has 
		 * not been reached.*/
		if (liveShells < SHELLS_ALLOWED)
		{
			//If it has not, fire a shell.
			liveShells++;
			super.fire();
		} 
	}
  
	/**
	 * Removes the turret and the target line actors from it's game world.
	 */
	@Override
	public void deleteTurret()
	{
		//remove all the target line actors associated with this player turret.
		World world= getWorld();
		for(TargetLine tl: targetLines)
    	{
    		world.removeObject(tl);
    	}
		
		//call superclass method which simply removes this turret from the game world
		super.deleteTurret();
	}
}
