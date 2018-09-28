import greenfoot.*;
import java.util.List;

/**
 * <p><b>File name: </b> Shell.java
 * @version 1.3
 * @since 14.05.2018
 * <p><p><b>Last modification date: </b> 22.07.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a tank shell for a Greenfoot recreation of the Wii Tanks 
 * game for the Nintendo Wii. It moves, bounces (a limited number of times, else 
 * it dissapears) when hitting a wall or the boundary of the world and destroys
 * tanks and other shells that it hits. It also makes landmines that it intersects 
 * explode, but that is handled in the LandMine class.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created a shell.
 * <p>	-1.1 - Made the shell bounce.
 * <p>	-1.2 - The shell now destroys tanks and other shells it hits.
 * <p>	-1.3 - Changed the way the shell bounces so that it relies on code in the 
 * WallBlock class to detect in what quadrant of the block the shell hits. Now the
 * shells bounce more naturally.
 */
public class Shell extends Actor
{
   	/**The speed with which all shells move. The value is {@value}.*/
    private static final int SHELL_SPEED=4;
    
    /**The number of times the shell is allowed to bounce off a wall.The value
     *  is {@value}.*/
    public static final int TIMES_ALLOWED_TO_BOUNCE=1;
    
    /**The number of degrees in <i>pi</i> (or around 3.14) radians.*/
    private final static int PI_RADIANS=180;
    
    /**The average distance at which the shell looks ahead to check if it will
     * hit a wall. The value is {@value}, because the shell image is 10 by 11 pixels.*/
    private static final int LOOK_AHEAD=11;
    
    /**The number of times the shell has bounced off a wall so far.*/
    private int timesBounced;
    
    /**The tank object whose turret has fired this shell.*/
    private Tank parentTank;
    
    /**A boolean that tells the shells whether to destroy it's parent tank if
     * it collides with it. Needed because sometimes when the shell is fired
     * it intersects with it's parent tank and destroys it immediately, which
     * we do not want.*/
    private boolean destroyParent;
    
    /**The correct x position of the shell, represented by a real number.*/
	private double realX;
	
	/**The correct x position of the shell, represented by a real number.*/
	private double realY;
	
    /**
     * Makes a new shell at the given coordinates and with the rotation given.
     * @param rotation The rotation of the new shell, the same as the rotation 
     * of the turret that fired it when it was fired.
     * @param parent The tank whose turret fired this shell.
     * @param x The x coordinate where the new shell will be put.
     * @param y The y coordinate where the new shell will be put.
     */
    public Shell(int rotation, Tank parent, int x, int y)
    {
    	setRotation(rotation);
    	timesBounced=0;
        parentTank=parent;
        realX=x;
        realY=y;
        
        /*if the shell intersects it's parent tank, we do not that tank to be 
         *destroyed immediately*/
        destroyParent=false;
        
        //put the shell in the world
        TankWorld world=parentTank.getWorldOfType(TankWorld.class);
        world.addObject(this, x, y);
    }
    
    /**
     * Moves this shell by approximately the distance given as a parameter
     * in the direction it is currently facing. Overrides the default one so that
     * errors do not accumulate over time dues to the fact in Greenfoot actor
     * position is represented by integers and not real numbers. Since before
     * with each call of the method the rounding to the nearest integers would
     * add over time, we store the correct coordinates as doubles and set the 
     * location to a rounded integer of these values, precision is not lost 
     * with each call of this method. Makes the shells actually move how they
     * should move, not with deviations like before.
     * @param distance The distance the shell will be moved in it's current 
     * direction.
     */
    @Override
    public void move(int distance)
	{
    	//calculate the rotation of the shell in radians
    	double radians = Math.toRadians(getRotation());
    	
    	/*Calculate the distance the shell should move by in each axis.*/
    	double dx = Math.cos(radians) * distance;
    	double dy = Math.sin(radians) * distance;
    	
    	//update the real x and y coordinates of the shell
    	realX+=dx;
    	realY+=dy;
    	
    	/*A world in Greenfoot is made up of finite cells, so positions can only
    	 * be integers. So we round to the nearest integers the value of the real
    	 * coordinates and set the location of the shell to these numbers.*/
    	int tempX=(int) Math.round(realX);
    	int tempY=(int) Math.round(realY);
    	setLocation(tempX, tempY);
	}
    
    /**
     * Act - do whatever the Shell wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * In this case, it makes the shell move, destroy targets it finds and bounce 
     * off walls or be removed if it has bounced more than it is allowed.
     */
    @Override
    public void act() 
    {
    	move(getSpeed());
      
        /*If the shell has not destroyed any targets and hasn't been removed 
         * from the world as a result, then we can check if it needs to bounce.
         * If it has, we do nothing and the act method will not be called again
         * because the shell is not in the world anymore.*/
        if(!destroyTargets())
        {
        	/*Checks if the shell has hit a wall.*/
	        if(hitsWall())
	        {
	        	//If it has not reached it's number of bounces limit, it bounces.
	        	if(timesBounced<getBounceLimit())
	        	{
	        		bounce();
	        	}
	        	//if it has, the shell is removed from the world
	        	else
	        	{
	        		TankWorld tankWorld=getWorldOfType(TankWorld.class);
	        		tankWorld.removeObject(this);
	        	}
	        }
        }
    }
    
    /**
     * This methods checks to see if the shell has hit any tanks or other shells.
     * If it has, it destroys them, removes this shell from the world.
     * @return true if it has destroyed any targets (and thus has also removed 
     * itself from the world), false if not.
     */
    private boolean destroyTargets()
    {
    	//a list of all the shells that intersect this shell
    	List<Shell> intersectShells=getIntersectingObjects(Shell.class);
    	
    	//a list of all the tanks that intersect this shell
    	List<Tank> intersectTanks=getIntersectingObjects(Tank.class);
    	
    	/*A variable that will be true if any targets have been destroyed, 
    	 *false if not.*/
    	boolean removeShell=false;
    	TankWorld world=getWorldOfType(TankWorld.class);
    	
    	/*Checks if this shell intersects any other shell.*/
    	if(!intersectShells.isEmpty())
    	{
    		//for each shell it intersects, we remove that shell from the world.
    		for(Shell s: intersectShells)
    		{
    			world.removeObject(s);
    		}
    		
    		//we will have to remove this shell from the world
    		removeShell=true;
    	}
    	
    	/*If this shell does not intersect it's parent tank anymore, then if it 
    	 *intersects it again we can destroy the parent tank.*/
    	if(!intersectTanks.contains(parentTank))
    	{
    		destroyParent=true;
    	}
    	
    	/*Checks if this shell intersects any tank.*/
    	if(!intersectTanks.isEmpty())
    	{  		
    		/*For each tank it intersects, we destroy it if it is appropiate*/
    		for(Tank t: intersectTanks)
    		{
    			/*If the tank is not this shell's parent tank, we destroy it.
    			 * If it is, we only destroy it if destroyParent is true.*/
    			if((t.equals(parentTank) && destroyParent) || !t.equals(parentTank))
    			{
    				world.removeObject(t);
    				removeShell=true;
    			}
    		}
    	}
    	
    	/*if we have destroyed a tank or another shell, this shell is removed 
    	 * from the world.*/
    	if(removeShell)
    	{
    		world.removeObject(this);
    	}
    	
    	return removeShell;
    }
    
    /**
     * Checks if the shell has hit a wall or the boundary of the world.
     * @return true if it has hit the world's limit or a wall, false if not.
     */
    private boolean hitsWall()
    {	
    	/*Checks if the shell has hit the world's boundary.*/
    	if(isAtEdge())
    	{
    		return true;
    	}
    	/*If not, check if it is about to hit a wall. We do not check if it 
    	 * intersects a wall, because then directly after it bounces, it would 
    	 * still intersect a wall and since it can't bounce again, it removes 
    	 * itself from the world.So what we do instead is see if there is a wall
    	 * in front of the shell. getXOffset() and getYOffset() are the coordinates
    	 * (relative to this shell's position) of a point in front of the shell,
    	 * where we check for a wall.*/
    	else if(getOneObjectAtOffset(getXOffset(),getYOffset(),WallBlock.class)
    			!=null)
    	{
    		return true;
    	}
    	//if it has not hit a wall or the world's boundary, it returns false
    	else
    	{
    		return false;
    	}
    }
    
    /**Makes the shell bounce.*/
    private void bounce()
    {
    	/*A string that tells what side of the wall block the shell will hit, or 
    	 *what edge of the world the shell will hit.*/
    	String quadrant="";
    	
    	/*We get the wall block that the shell is about to hit.*/
    	WallBlock wallBlock=(WallBlock)getOneObjectAtOffset(getXOffset(),
    			getYOffset(),WallBlock.class);
    	
    	/*Check if the shell is about to hit a wall or the world's edge*/
    	if(wallBlock!=null)
    	{
    		/*If the shell is about to hit a wall, we get the side it is about to 
        	 * hit based on the diagonal quarter of the wall block the shell will hit.*/
    		quadrant=wallBlock.getQuadrant(this.getX(), this.getY());
    	}
    	else
    	{
    		/*If the shell hits the edge of the world, we assign the correct 
    		 * edge to quadrant. Keep in mind x axis is from left to right,
    		 * and the y axis is from top to bottom.*/
    		if(getX()==0)
    		{
    			//x is 0 on the left side of the world
    			quadrant="left";
    		}
    		//x is length-1 on the right side of the world
    		else if(getX()==999)
    		{
    			quadrant="right";
    		}
    		//y is 0 on the top side of the world
    		else if(getY()==0)
    		{
    			quadrant="top";
    		}
    		//y is height-1 on the top side of the world
    		else if(getY()==799)
    		{
    			quadrant="bottom";
    		}
    	}
    	
    	//the new orientation of the shell, after it bounces
    	//initialised as 0 just so the compiler won't complain
    	int newRotation=0;
    	
    	/*Determine how to flip the shell, based on the value of quadrant.*/
    	if(quadrant.equals("left") || quadrant.equals("right"))
    	{
    		/*If we hit the left or right side of the world or wall block, then
    		 * the shell must be rotated along it's vertical axis.*/
    		newRotation=getMirroredVertically(getRotation());
    	}
    	/*Else if we hit the top or bottom side of the world or wall block, then
		 * the shell must be rotated along it's horizontal axis.*/
    	else if(quadrant.equals("top") || quadrant.equals("bottom"))
    	{
    		newRotation=getMirroredHorizontally(getRotation());
    	}
    	
    	/*rotate the shell accordingly and increment the counter for the number 
    	  of times it has bounced.*/
    	setRotation(newRotation);
    	timesBounced++;
    }
    
    /**
     * This method calculates the length of the x-axis component of a line of 
     * length LOOK_AHEAD from the center of the shell pointing ahead of the shell.
     * @return The approximate length of the x-axis component.
     */
    private int getXOffset()
    {
    	/*The x length is the length of LOOK_AHEAD multiplied by the cosinus of 
    	 * the angle of the shell (which in Greenfoot is a value from 0 to 360. 
    	 * The 0 angle is towards the left, and the angle increases in a clockwise
    	 * direction). We get the real value of the x component, and we round it
    	 * and cast it as an integer.*/
    	double rotation=Math.toRadians(getRotation());
    	int xOffset=(int) Math.round(getLookAhead()*Math.cos(rotation));
    	
    	/*The image of the shell is 10 by 11, so it roughly fits inside a circle 
    	 * with a radius of 6. After the rounding, the offset might be less than 
    	 * that, which means the image of the shell will intersect with the wall 
    	 * by the time a hit is detected. We do not want the shell to intersect 
    	 * the wall block, so if it's modulus is less than 6 (or LOOK_AHEAD/2+1)
    	 * we change to either 6 or -6.*/
    	if(Math.abs(xOffset)<(LOOK_AHEAD/2)+1)
    	{
    		/*We change the offset to either 6 or -6,depending on the cosinus of
    		 * the shell's angle*/
    		xOffset=(LOOK_AHEAD/2+1)*(int)Math.signum(Math.cos(rotation));
    	}
    	
    	return xOffset;
    }
    
    /**
     * This method calculates the length of the y-axis component of a line of 
     * length LOOK_AHEAD from the center of the shell pointing ahead of the shell.
     * @return The approximate length of the y-axis component.
     */
    private int getYOffset()
    {
    	/*The y length is the length of LOOK_AHEAD multiplied by the sinus of 
    	 * the angle of the shell (which in Greenfoot is a value from 0 to 360. 
    	 * The 0 angle is towards the left, and the angle increases in a clockwise
    	 * direction). We get the real value of the y component, and we round it
    	 * and cast it as an integer.*/
    	double rotation=Math.toRadians(getRotation());
    	int yOffset=(int) Math.round(getLookAhead()*Math.sin(rotation));
    	
    	/*The image of the shell is 10 by 11, so it roughly fits inside a circle 
    	 * with a radius of 6. After the rounding, the offset might be less than 
    	 * that, which means the image of the shell will intersect with the wall 
    	 * by the time a hit is detected. We do not want the shell to intersect 
    	 * the wall block, so if it's modulus is less than 6 we change to either 
    	 * 6 or -6.*/
    	if(Math.abs(yOffset)<6)
    	{
    		/*We change the offset to either 6 or -6,depending on the sinus of
    		 * the shell's angle*/
    		yOffset=6*(int)Math.signum(Math.sin(rotation));
    	}
    	
    	return yOffset;
    }
    
    /**
     * Calculates a new angle for the shell so that it will be flipped around 
     * it's vertical axis, and returns it.
     * @param direction The current angle of the object you want to flip, in
     * degrees from 0 to 359.
     * @return The value in degrees from 0 to 359 of the new angle.
     */
    private int getMirroredVertically(int direction)
    {
    	/*To flip an object around it's vertical axis, it's new angle must have 
    	 * the same sine but the opposite cosine. The angle that meets this 
    	 * criteria is 180-currentAngle. */
    	int newDirection=PI_RADIANS-direction;
    	return newDirection;
    }
    
    /**
     * Calculates a new angle for the shell so that it will be flipped around 
     * it's horizontal axis, and returns it.
     * @param direction The current angle of the object you want to flip, in
     * degrees from 0 to 359.
     * @return The value in degrees from 0 to 359 of the new angle.
     */
    private int getMirroredHorizontally(int direction)
    {
    	/*To flip an object around it's horizontal axis, it's new angle must have 
    	 * the same cosine but the opposite sine. The angle that meets this 
    	 * criteria is 360-currentAngle. */
    	int newDirection=2*PI_RADIANS-direction;
    	return newDirection;
    }
    
    public double getRealX()
    {
    	return realX;
    }
    
    public double getRealY()
    {
    	return realY;
    }
    
    /**
     * Returns the parent tank of this shell.
     * @return The parent tank of this shell.
     */
    public Tank getParentTank()
    {
    	return parentTank;
    }
    
    /**
	 * The speed of this shell, meaning the distance in cells that the shell moves
	 * each time the move(int) method is called.
	 * @return the speed of this type of shells, defined by a static constant.
	 */
    public int getSpeed()
    {
    	return SHELL_SPEED;
    }
    
    /**
     * Gets the number of times the shell is allowed to bounce off a wall. We 
     * have this method despite the fact it returns a public constant so that 
     * we can override for different types of shells.
     * @return the number of times the shell is allowed to bounce off a wall.
     */
    public int getBounceLimit()
    {
    	return TIMES_ALLOWED_TO_BOUNCE;
    }
    
    /**
     * Gets the average distance at which the shell looks ahead to check if 
     * it will hit a wall.
     * @return the average distance at which the shell looks ahead to check if 
     * it will hit a wall.
     */
    public int getLookAhead()
    {
    	return LOOK_AHEAD;
    }
}
