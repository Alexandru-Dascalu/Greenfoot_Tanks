import greenfoot.*;

/**
 * <p><b>File name: </b> PlayerTank.java
 * @version 1.4
 * @since 07.06.2018
 * <p><b>Last modification date: </b> 07.08.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a player tank for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It is a tank controller by the player.
 * The player can drive the tank, fire shells and lay mines.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Encapsulated the turret actions of firing and aiming into this classes,
 * meaning the aim() and fire() methods are called by the tank's act() method.
 * <p>	-1.2 - Encapsulated mouse tracking into this class. The tank detects a
 * mouse click and then tells the turret to fire or lays a mine , depending on
 * the mouse button clicked.
 * <p>	-1.3 - Changed the class so player tanks can push other tanks it encounters.
 * <p>	-1.4 - Added a method that calculates the distance between the point at the 
 * given coordinates and this tank.
 */

public class PlayerTank extends Tank
{
	/**The time in milliseconds from the start of a mouse press by which the 
	* mouse button must be released so that a click may be detected.*/
	private static final int CLICK_TIME_WINDOW=165;
	
	/**The distance measured in cell-size units by which this tank moves each 
	 * time it acts. It's value is {@value}.*/
	private static final int SPEED=2;
	
	/**The last information about the state of the mouse we have.*/
    private MouseInfo lastMouseInfo;
    
    /**The system time in milliseconds when the left mouse button has last been 
	* pressed. Used to ensure that a shell is only fired when the player quickly
	* clicks the left mouse button.*/
	private long leftMBPressStart;

	/**
	* Make a new player tank object.
	* @param startX The starting x coordinate in the world for this tank.
	* @param startY The starting x coordinate in the world for this tank.
	*/
	public PlayerTank(int startX, int startY)
	{
		super(startX,startY);
		lastMouseInfo=null;
		leftMBPressStart=0;
	}
	
	/**
	 * Prepares the player tank for the beginning of the game. Sets the correct
	 * starting orientation and gives this player tank a PlayerTurret.
	 */
	@Override
	protected void addedToWorld(World world)
	{
		this.setRotation(180);
		tankTurret=new PlayerTurret(this);
	}
	
	/**
	 * Method called by Greenfoot at each timestep to make the actor do what it
	 * is supposed to do. In this case, it allows the player to move the tank, aim
	 * and fire the turret, lay mines and it makes the tank play sound if it moves.
	 */
	@Override
	public void act()
	{
		/*in order to be able to detect left and middle mouse button clicks and 
		 *aim the turret, we need to have latest information about the state of 
		 *the mouse*/
		updateMouseInfo();
		
		moveAndTurn();
		pushOtherTanks();
		playSound();
		
		tankTurret.aim();
		
		/*Only tell the turret to fire if the left mouse button has been clicked.*/
		if(leftMBClicked()) 
		{
			tankTurret.fire();
		}
		
		/*Check if the player has clicked the middle mouse button in order to 
		 * lay a mine.*/
		if(middleMBClicked())
		{
			layMine();
		}
	}
	
	/**
	 * Makes the tank move in accordance with the player's keystrokes.
	 */
	private void moveAndTurn()
	{
		/*Check if the tank should move forwards. It should only if it there is
		 * no wall in front of it and if the player presses "w".*/
		if (Greenfoot.isKeyDown("w") && canMoveForwards())
		{
			/*If it should move, move the tank and it's turret.*/
			move(SPEED);
		}

		/*Check if the tank should move backwards. It should only if it there is
		 * no wall behind it and if the player presses "s".*/
		if (Greenfoot.isKeyDown("s") && canMoveBackwards())
		{
			/*If it should move, move the tank and it's turret.*/
			move(-SPEED);
		}

		/*Check if the tank should turn left. It should only if it there is
		 * no wall in the way and if the player presses "a".*/
		if (Greenfoot.isKeyDown("a") && canTurnLeft())
		{
			/*If it should turn, turn the tank and not also the turret (it should
			 * always face the player target).*/
			turn(-2);
		}

		/*Check if the tank should turn right. It should only if it there is
		 * no wall in the way and if the player presses "d".*/
		if (Greenfoot.isKeyDown("d") && canTurnRight())
		{
			/*If it should turn, turn the tank and not also the turret (it should
			 * always face the player target).*/
			turn(2);
		}
	}
	
	/**Updates the last known state of the mouse. Necessary in order to detect
	 * left and middle mouse button clicks and to aim the turret.*/
	private void updateMouseInfo()
	{
		/*getMouseInfo() returns a MouseInfo object with the latest information
		 * about the mouse, or null if the mouse has not been moved or clicked.*/
		MouseInfo mouse=Greenfoot.getMouseInfo();
	        
		/*Only update mouse info if the mouse has been used since the last tome
		 * this method was called.*/
		if(mouse!=null)
		{
			lastMouseInfo=mouse;
		} 
	}
	
	/**Makes the tank lay down a land mine.*/
	private void layMine()
	{
		World world= getWorld();
		
		//make a new land mine and put it in the game world
		LandMine mine=new LandMine(this);
		world.addObject(mine, getX(), getY());
	}
	
	/**Detects whether the middle mouse button has been clicked.*/
	private boolean middleMBClicked()
	{
		/*Check if lastMouseInfo is not null to avoid a NullPointerException.
		 * LastMouseInfo is null only if the mouse has not been in the game 
		 * world so far.*/
		if(lastMouseInfo!=null)
		{
			/*If it is no null, the middle mouse button has been pressed if the 
			 * a mouse button has been pressed and released and if that button
			 * was the second mouse button.*/
			return (Greenfoot.mouseClicked(null) && lastMouseInfo.getButton()==2);
		}
		/*If it is null, the middle mouse button can not have been clicked.*/
		else
		{
			return false;
		}
		
	}
	
	/**Detects whether the left mouse button has been clicked.*/
	private boolean leftMBClicked()
	{
		/*Check if lastMouseInfo is not null to avoid a NullPointerException.
		 * LastMouseInfo is null only if the mouse has not been in the game 
		 * world so far.*/
		if(lastMouseInfo!=null)
		{
			/*By left mouse button click, we mean that the left mouse button 
			 * has been pressed and then released right after (in this case it
			 * must be realeased within 165 milliseconds from when the mouse press
			 * started).
			 * 
			 * We check if we are currently tracking a mouse press.*/
			if(leftMBPressStart!=0)
	    	{
	    		long currentTime=System.currentTimeMillis();
	    		
	    		/*If we are tracking a mouse press, we check if the left MB has 
	    		 * been released shortly after the press started.*/
	    		if(currentTime<=leftMBPressStart+CLICK_TIME_WINDOW && Greenfoot.mouseClicked(null)
	    				&& lastMouseInfo.getButton()==1)
	    		{
	    			/*If it has been released shortly after the press started, we
	    			 * we reset the last start time of a left MB press and return
	    			 * true.*/
	    			leftMBPressStart=0;
	    			return true;
	    		}
	    		/*Else, if the left mouse button has been released, but long after
	    		 * the press started, we just reset the last start time of a left
	    		 * mouse button press.*/
	    		else if(currentTime>leftMBPressStart+CLICK_TIME_WINDOW && Greenfoot.mouseClicked(null))
	    		{
	    			leftMBPressStart=0;
	    		}
	    	}
			/*If we are not currently tracking a mouse press, we check if the 
			 * left mouse button has been pressed (changed from a non-pressed
			 * state to being pressed.)*/
	    	else
	    	{
	    		if(Greenfoot.mousePressed(null) && lastMouseInfo.getButton()==1)
	    		{
	    			/*If the left mouse button has just been pressed, we change
	    			 * the start of the last left mouse button press to the current
	    			 * time.*/
	    			leftMBPressStart=System.currentTimeMillis();
	    		}
	    	}
		}
		
		/*If it has not returned true so far, the left mouse button has not
		 * been clicked.*/
		return false;
	}

	/**
	 * Overrides the default isMoving method because a Player Tank only moves when
	 * the player presses one of the WASD keyboard keys.
	 */
	@Override
	public boolean isMoving()
	{
		/*The player tank moves only if the player is pressing W,A,S or D keys
		 * on the keyboard.*/
		boolean isMoving = Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("s") 
				|| Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("d");

		return isMoving;
	}
	
	/**
	 * Checks if the tank is moving forward.
	 * @return True if the "w" key is pressed, false if not.
	 */
	@Override
	protected boolean isMovingForward()
	{
		return Greenfoot.isKeyDown("w");
	}
	
	/**
	 * Checks if the tank is moving backward.
	 * @return True if the "s" key is pressed, false if not.
	 */
	@Override
	protected boolean isMovingBackward()
	{
		return Greenfoot.isKeyDown("s");
	}
	
	/**
	 * The speed of this tank, meaning the distance in cells that the tank moves
	 * each time the move(int) method is called.
	 * @return 	The speed of this tank.
	 */
	@Override
	public int getSpeed()
	{
		return SPEED;
	}
	
	/**Method reloads this tank into the game world to prepare it for another start
	 * of the current level. Overloaded the method from the Tank class because
	 * player tanks need to be readded to the world after they have been removed
	 * and we need a reference to the world as an argument. Using addObject does
	 * not work because that does not reset the real number values of the x and y
	 * coordinates of the tank used for accurate movement. */
	public void reloadTank(TankWorld world)
	{
		/*Check if the tank is in a game world to avoid exceptions.*/
		if(world!=null)
		{
			/*Reset the real number values of the tank's position.*/
			realX=startX;
			realY=startY;
			
			/*Place the tank and it's turret at it's original position and reset
			 * their orientation.*/
			world.addObject(this, startX, startY);
			world.addObject(tankTurret, startX, startY);
			setRotation(180);
			tankTurret.setRotation(180);
		}
	}
	
	/**Getter for the MouseInfo object of the player's tank. Needed so that the
	 * player turret can access the location of the cursor an know where to aim.
	 * @return The latest information about the mouse this tank tracks.*/
	public MouseInfo getMouseInfo()
	{
		return lastMouseInfo;
	}
	
	public double getDistanceFrom(int x, int y)
	{
		 int xDistance=x-this.getX();
		 int yDistance=y-this.getY();
		 
		 double distance=Math.sqrt((xDistance*xDistance)+(yDistance*yDistance));
		 
		 return distance;
	}
}
