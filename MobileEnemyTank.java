import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * <p><b>File name: </b> MobileEnemyTank.java
 * @version 1.4
 * @since 14.08.2018
 * <p><b>Last modification date: </b> 02.10.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.* <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>MobileEnemyTank.java is part of Panzer Batallion.
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
 * <p> This class models a mobile enemy tank for a Greenfoot recreation of the 
 * Wii Tanks game for the Nintendo Wii. By default, it generates a path to 
 * follow in the game world using the world's graph, follows it, decides to 
 * move while turning or not, plays a sound when moving and tells it's turret 
 * to aim and fire. It is meant to be inherited always and you should not have
 * an actor that is just a MobileEnemyTank object since the class was not meant
 * to be used in this way.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Made the tanks move more naturally by being able to move while
 *  turning if they can still get to the desired rotation by doing so and made
 *  the tank register reaching a point by being close to it, and not 
 *  necessarily at the exact coordinates.
 *  <p>	-1.2 - Made tanks not run into walls when they move while turning.
 *  <p>	-1.3 - Made tanks avoid land mines.
 *  <p>	-1.4 - Made tanks avoid shell.
 */

public abstract class MobileEnemyTank extends Tank
{
	/**The minimal distance in cells or pixels the chosen target point can be 
	 * away from the player tank, so this tank will try to not get too close 
	 * to the player. It's value is {@value}.*/
	private static final int DISTANCE_FROM_PLAYER=150;
	
	/**The maximum distance in cells or pixels this tank needs to be away 
	 * from a graph point for it to consider it has "reached" that point and
	 * can move towards the next point. It's value is {@value}.*/
	private static final int POINT_RADIUS=22;
	
	/**The maximum value of the difference between the target rotation and the
	 * current rotation in degrees where the tank can still turn while moving
	 * and while it is avoiding a mine. Used to make the tank move more naturally 
	 * and efficiently around a mine. It's value is {@value}.*/
	private static final int MINE_AVOIDANCE_TURN_TOLERANCE=25;
	
	/**A random number generator used to choose a destination point for this 
	 * tank.*/
	private Random numberGenerator;
    
	/**Indicates if this tank is moving (if it is moving forwards, backwards,
	 * or turning).*/
	protected boolean isMoving;
	
	/**Indicates if this tank is moving forwards.*/
	protected boolean isMovingForward;
	
	/**Indicates if this tank is moving backwards.*/
	protected boolean isMovingBackward;
	
	/**A linked list of GraphPoint objects that is the path this tank is taking
	 * to it's chosen destination point.*/
	protected LinkedList<GraphPoint> path;
	
	/**The rotation this tank should have for it to continue moving in the 
	 * correct direction.*/
	private int targetRotation;
	
	/**The GraphPoint object that is the next point in the game world this tank
	 * needs to reach in it's path.*/
	private GraphPoint nextPoint;
	
	/**A flag that indicates if this tank is currently avoiding a mine.*/
	private boolean avoidingMine;
	
	/**A flag that indicates if this tank is currently avoiding a shell.*/
	private boolean avoidingShell;
	
	/**The next point in time in milliseconds when this tank will lay a mine.*/
	protected long nextMineLayingTime;
	
	/***
	 * Makes a new Mobile Enemy Tank whose starting location in the level is at
	 * the given coordinates.
	 * @param startX The x coordinate of the initial position of the tank in 
	 * the level.
	 * @param startY The y coordinate of the initial position of the tank in 
	 * the level.
	 * @param startRotation The starting rotation of this tank in the world.
	 */
    public MobileEnemyTank(int startX, int startY, int startRotation)
    {
    	//call superclass constructor
        super(startX, startY, startRotation);
        
        //initialize all instance variables of this subclass
        numberGenerator=new Random();
        path=null;
        avoidingMine=false;
        avoidingShell=false;
        
        //tank does not move initially
        isMoving=false;
        isMovingForward=false;
        isMovingBackward=false;
        
        //initialize the first point in time this tank will lay a mine
        generateNextMineLayingTime();
    }
    
    /**
	 * Act - do whatever the Mobile Enemy Tank wants to do. This method is 
	 * called whenever the 'Act' or 'Run' button gets pressed in the 
	 * environment. In this case, this method makes the tank generate a path 
	 * to a point in the game world if none exists or follow the current path,
	 * after which the super type act() method is called.
	 */
    @Override
	public void act()
	{
    	//Check if there is a land mine dangerously close to the tank
    	LandMine mine=detectLandMines();
    	
    	/*If there is a mine too close to the tank, and the is already not
    	 * avoiding a mine, make this tank avoid the mine.*/
		if(!avoidingMine && (mine!=null))
		{
			avoidLandMine(mine);
		}
		/*Else, if there is no mine in the vicinity of this tank, set the
		 * mine avoidance flag to false.*/
		else if(mine==null)
		{
			avoidingMine=false;
		}
		
		//See if there is a shell dangerously close to the tank
		Shell shell=detectIncomingShells();
		
		/*Check if there is an instance of a shell that is too close to the tank.*/
		if(shell!=null)
		{
			/*Check if the shell is moving away from the tank or not.*/
			if(!shellIsMovingAway(shell))
			{
				/*If the shell is moving closer to the tank, check if the flag 
				 * is set accordingly to trigger evasive action.*/
				if(!avoidingShell)
				{
					/*if the flag was not already set, set it to true and calculate
					 * how the tank should avoid the incoming shell.*/
					avoidingShell=true;
					calculateShellAvoidance(shell);
				}
			}
			/*If the shell is moving away from the tank, it cannot hit the tank,
			 * therefore there is no need to avoid it, so the flag is set accordingly.*/
			else
			{
				avoidingShell=false;
			}
		}
		/*If there is no shell dangerously close to the tank, make sure the flag
		 * is set accordingly.*/
		else
		{
			avoidingShell=false;
		}
		
		/*Check if the tank is supposed to avoid an incoming shell.*/
		if(avoidingShell)
		{
			//if so, avoid the shell
			avoidIncomingShell();
		}
		//Else, the tank should move normally.
		else
		{
			//Check if there  is a path to follow.
			if(path!=null)
			{
				followPath();
			}
			//Else, the tank has no path to follow
			else
			{
				//so we generate one
				generatePath();
			}
		}
		
		//lay mines if the tank decides so
		layMine();
		
		//do what all tanks do
		super.act();
	}
    
    /**Generates a path to a destination point in the game world for the tank 
     * to follow.*/
    private void generatePath()
    {
    	//Get a reference to the world this tank is in
    	TankWorld world=(TankWorld)getWorldOfType(TankWorld.class);
    	
    	/*choose a destination point and use the world's graph to get a path 
    	 * to it.*/
    	GraphPoint destination=chooseDestinationPoint();
    	path=world.getWorldGraph().getShortestPath(getX(), getY(), 
    			destination);
    }
    
    /**Chooses a random destination point for the tank to get to.*/
    private GraphPoint chooseDestinationPoint()
    {
    	//the destination point that will be returned eventually
    	GraphPoint destination=null;
    	
    	/*A destination point we do not want may be chosen so this loop repeats
    	 * the process until a suitable destination is chosen. If the 
    	 * destination is not valid, destination is left null. If the destination is
    	 * valid, destination is assigned that value, so the loop terminates.*/
    	while(destination==null)
    	{
    		//generate random coordinates within the world's walls
	    	int targetX=numberGenerator.nextInt(TankWorld.LENGTH-2*WallBlock.SIDE)
	    			+ WallBlock.SIDE;
	    	int targetY=numberGenerator.nextInt(TankWorld.WIDTH-2*WallBlock.SIDE)
	    			+ WallBlock.SIDE;
	    	
	    	//Get the node/point at the calculated indexes from the graph.
	    	TankWorld world=(TankWorld)getWorld();
	    	GraphPoint potentialTarget=world.getWorldGraph().getPoint(targetX,
	    			targetY);
	    
	    	/*Check if the value at those indexes is null. The random coordinates 
	    	 * might be to a point situated inside or too close to a wall, in
	    	 * which case the loop will repeat.*/
	    	if(potentialTarget!=null)
	    	{
	    		/*If there is a node at those indexes, check if the destination
	    		 * is not too close to the player tank.*/
	    		if(world.getPlayerTank().getDistanceFrom(targetX, targetY)>
	    			DISTANCE_FROM_PLAYER)
	    		{
	    			//If it is not too close, this destination is valid so we 
	    			//choose it and terminate the loop
	    			destination=potentialTarget;
	    		}
	    	}
    	}
    	
    	return destination;
    }
    
    /**Makes the tank follow the path by passing by each node in the path until
     * the destination is reached.*/
    protected void followPath()
    {
    	/*Check if it the tank has a reference to the next point it needs to 
    	 * reach.*/
    	if(nextPoint==null)
    	{
    		//Try to get the first node in the LinkedList that is the path
    		try
        	{
        		nextPoint=path.getFirst();
        	}
    		/*The path might be empty at this point or be null, in which 
    		 * case we make sure the path is null so a new path will be 
    		 * generated in the next call of the act() method and terminate
    		 * this method.*/
        	catch(NoSuchElementException | NullPointerException e)
        	{
            	path=null;
        		return;
        	}
    	}
    	
    	/*At this point nextPoint is not null, so we check if this tank has 
    	 * reached this point.*/
	    if(reachedPoint(nextPoint))
	    {
	    	/*If it has, remove it from the path and set nextPoint to null so 
	    	 * a new point will be chosen next time this method is called.*/
	    	path.removeFirst();
	    	nextPoint=null;
	    }
	    /*If it has not reached the next point, make sure the tank is heading
	     * for it.*/
	    else
	    {
	    	/*Calculate the rotation needed for this tank to turn towards the 
	    	 * next point. This is recalculated every time since if the tank 
	    	 * is moving while turning, the target rotation changes. Or because
	    	 * Greenfoot works in integers, the target rotation may change by a
	    	 * few degrees even when the tank is facing the next point.*/
	    	targetRotation=calculateTargetRotation(nextPoint);
	    	
	    	/*Check if the target rotation is the same as this tank's rotation.*/
	    	if(getRotation()==targetRotation)
	    	{
	    		/*If so, this tank is just moving forward set the boolean 
	    		 * values to indicate it and then move the tank using the
	    		 * speed indicated by the subclass.*/
	   			isMoving=true;
	   			isMovingForward=true;
	   			isMovingBackward=false;
	   			move(getSpeed());
    		}
	    	/*Else, this tank needs to turn in the correct direction.*/
	    	else
	    	{
	    		/*Calculate which way and how much this tank should turn and 
	    		 * turn the tank using the returned number.*/
	    		int turnSpeed=calculateTurn();
	    		turn(turnSpeed);
	    		
	    		//if the tank is turning, it is considered to be moving
	    		isMoving=true;
	    		isMovingBackward=false;
	    		
	    		/*If the tank is very close to a mine, it can be dangerous for 
	    		 * it to turn while moving. The value of this flag will indicate
	    		 * if the tank can move while turning while it is avoiding a mine.*/
	    		boolean moveWhileTurning;
	    		
	    		/*Check if this tank is currently avoiding a mine.*/
	    		if(avoidingMine)
	    		{
	    			/*To make the tank move more naturally and efficiently, it 
	    			 * will only turn while moving if the difference between 
	    			 * it's rotation and the target rotation is under a certain 
	    			 * limit.*/
	    			if(normalizeAngle(getRotation()-targetRotation) < 
	    					MINE_AVOIDANCE_TURN_TOLERANCE)
	    			{
	    				//it is under the limit, so it can move while turning
	    				moveWhileTurning=true;
	    			}
	    			else
	    			{
	    				//it is over the limit, so it can not move while turning
	    				moveWhileTurning=false;
	    			}
	    		}
	    		/*If it is not avoiding a mine, this flag is not  */
	    		else
	    		{
	    			moveWhileTurning=true;
	    		}
	    		
	    		/*For the tank's movements to be more natural, the tank should
	    		 * move while turning. Sometimes if the tank does that it 
	    		 * cannot reached the rotation towards the next point or it runs 
	    		 * into a wall. So we check if this tank can move while turning.*/
	    		if(moveWhileTurning && canMoveWhileTurning())
	    		{
	    			//If so, move the tank forward
	    			move(this.getSpeed());
			    	isMovingForward=true;
	    		}
	    		//Otherwise, the tank just turned while stationary
	    		else
	    		{
	    			//indicate the tank is not moving forward currently
	    			isMovingForward=false;
	    		}
	    	}
	    }
    }
    
    /**
     * Detects if this tank has reached a point in the world graph, meaning it 
     * is within a certain distance from that node's coordinates.
     * @param point A point in the world graph the tank wants to reach.
     * @return True if the tank has reached, false if not.
     */
    private boolean reachedPoint(GraphPoint point)
    {
    	/*calculate the horizontal and vertical distances between this tank the
    	 * given graph point*/
    	int deltaX=this.getX()-point.getX();
    	int deltaY=this.getY()-point.getY();
    	
    	/*Calculate the total distance using Pythagora's theorem.*/
    	double distance=Math.sqrt(deltaX*deltaX + deltaY*deltaY);
    	
    	/*Check if the distance is small enough for this point to be considered
    	 * to have been reached by the tank.*/
    	if(distance<POINT_RADIUS)
    	{
    		//if so, return true because this tank has reached the point
    		return true;
    	}
    	//Else, the tank needs to be closer, so it has not reached the point
    	else
    	{
    		return false;
    	}
    }
    
    /**
     * Calculates the rotation this tank needs to have to turn toward the 
     * given graph point.
     * @param nextPoint The point this tank wants to turn towards.
     * @return The rotation, from 0 to 359, this needs to have.
     */
    private int calculateTargetRotation(GraphPoint nextPoint)
    {
    	/*Get the value of the angle between the horizontal axis and the 
		 * line between this tank and the given GraphPoint.*/
    	double theta=Math.toDegrees(Math.atan2(nextPoint.getY()-getY(), 
    			nextPoint.getX()-getX()));
    	
    	//turn that value into an integer between 0 and 359 inclusive
		int targetRotation=(int)Math.round(normalizeAngle(theta));
		
		return targetRotation;
    }
    
    /**
     * Calculates which way and how much this tank should turn to get to the 
     * desired target rotation.
     * @return The amount in degrees the tank should turn in this call of the 
     * act() method. Negative values mean the tank will turn counter clockwise,
     * positive values clockwise.
     */
    private int calculateTurn()
    {
    	/*We need to decide which way the tank will turn. So we calculate the 
    	 * number of degrees between the desired rotation and the tank's current
    	 * rotation in a clockwise way and in a counter clockwise way.*/
		int clockwiseDiff=(int)normalizeAngle(targetRotation-getRotation());
		int counterClockwiseDiff=(int)normalizeAngle(getRotation()-targetRotation);
	
		//the maximum number of degrees this tank can turn once
		int maxTurnSpeed=getMaxTurnSpeed();
		
		//the amount of degrees this tank will turn
		int turnSpeed;
		
		/*Check if the tank can reach the desired rotation faster if it turns
		 * clockwise.*/
		if(clockwiseDiff<counterClockwiseDiff)
		{
			/*Check if the tank can turn with it's maximum turn speed, if the
			 * difference to the target rotation is bigger than it's turn 
			 * speed.*/
			if(clockwiseDiff>=maxTurnSpeed)
			{
				turnSpeed=maxTurnSpeed;
			}
			/*If it cannot, turn by the amount of degrees left to the target
			 * rotation*/
			else
			{
				turnSpeed=clockwiseDiff;
			}
		}
		/*Else, the tank can reach the desired rotation faster if it moves 
		 * counterclockwise.*/
		else
		{
			/*Check if the tank can turn with it's maximum turn speed, if the
			 * difference to the target rotation is bigger than it's turn 
			 * speed.*/
			if(counterClockwiseDiff>=maxTurnSpeed)
			{
				/*turnSpeed is negative because the tank needs to turn 
				 * counterclockwise*/
				turnSpeed=-maxTurnSpeed;
			}
			/*If it cannot, turn by the amount of degrees left to the target
			 * rotation*/
			else
			{
				/*turnSpeed is negative because the tank needs to turn 
				 * counterclockwise*/
				turnSpeed=-counterClockwiseDiff;
			}
		}
		
		return turnSpeed;
    }
    
    /**
     * Decides whether this tank can move while it is turning. Sometimes if 
     * the tank does that it cannot reach the rotation towards the next 
     * point or it runs into a wall. So it is necessary to check this tank 
     * can move while turning before doing so.
     * @param targetRotation The rotation this tank needs to get to.
     * @param nextPoint The graphPoint whose position this tank needs to reach.
     * @return True if this tank can safely move while turning, false if not.
     */
    private boolean canMoveWhileTurning()
    {
    	/*Check if the tank can move forwards without hitting a wall.*/
    	if(!canMoveForwards())
    	{
    		//If it can not, it can not move while turning.
    		return false;
    	}
    	
    	/*Calculate the distances the tank would move by in each axis if the 
    	 * move(getSpeed()) method would be called now.*/
    	double radians=Math.toRadians(getRotation());
    	double dx = Math.cos(radians) * getSpeed();
    	double dy = Math.sin(radians) * getSpeed();
    	
    	/*Calculate the x and y position of this tank if the move(getSpeed())
    	 * method would be called now.*/
    	int tempX=(int) Math.round(realX+dx);
    	int tempY=(int) Math.round(realY+dy);
    	
    	/*Get the value of the angle between the horizontal axis and the 
		 * line between this tank and the given GraphPoint, if the tank would
		 * have just moved ahead by getSpeed().*/
		double nextTheta=Math.toDegrees(Math.atan2(nextPoint.getY()-tempY, 
				nextPoint.getX()-tempX));
		
		//turn that value into an integer between 0 and 359 inclusive
		int nextTargetRotation=(int)Math.round(normalizeAngle(nextTheta));
		
		/*See if the number of degrees by which the target rotation would 
		 * change if this tank would have just moved ahead by getSpeed() is 
		 * bigger than the maximum turn speed of this tank.*/
		if(Math.abs(nextTargetRotation-targetRotation)>getMaxTurnSpeed())
		{
			//If it is, this tank cannot safely move while turning
			return false;
		}
		//Else, it can do that safely.
		else
		{
			return true;
		}
    }
    
    /**
     * Detects if this tank is dangerously close to an incoming shell. It 
     * returns a reference to one of the shells that is too close to the tank
     * so that the tank will be able to avoid that shell. The tanks are not 
     * very smart, so if more than one shell is too close to the tank, it will 
     * only try to avoid one.
     * @return A reference to one of the shells that is dangerously close to 
     * this tank. Ignores shells fired by  this tank's turret.
     */
    private Shell detectIncomingShells()
    {
    	//get a list of all the shells within a circle whose radius is the 
    	//minimum distance this type of tank keeps away from incoming shells
    	List<Shell> shells=getObjectsInRange(getShellAvoidanceDistance(),Shell.class);
    	
    	/*A shell in this list might be a shell fired by this tank's turret. 
    	 * These do not need to be avoided, so we search for shells that were 
    	 * fired by other tanks's turrets. This loop goes through the list of
    	 * shells that are too close , checks if they were fired by this tank,
    	 * and exits prematurely if it finds a shell that was not.*/
    	for(Shell s: shells)
    	{
    		//Check if this shell was fired by a turret other than this tank's turret.
    		if(s.getParentTank()!=this)
    		{
    			//if so, return the shell
    			return s;
    		}
    	}
    	
    	/*If so a shell has not been returned, then no shell not fired by this 
    	 * tank is dangerously close to this tank.*/
    	return null;
    }
    
    /**
     * Calculate the way this tank should avoid the incoming shell given as
     * an argument.
     * @param incomingShell The shell that is to be avoided by this tank.
     */
    private void calculateShellAvoidance(Shell incomingShell)
    {
    	//the rotation of the incoming shell
    	int shellRotation=incomingShell.getRotation();
    	
    	//calculate the slope of the direction the shell
    	double shellSlope=Math.tan(Math.toRadians(shellRotation));
    	
    	/*This method uses the formula for the equation of a line: y=m*x+n . 
    	 * The n in the equation is also called the y intercept.*/
    	double yIntercept=incomingShell.getY()-shellSlope*incomingShell.getX();
    	
    	/*The tank avoids incoming shells by trying to move into a position 
    	 * where the tank's orientation is perpendicular to that of the shell's
    	 * direction. This is the quickest way the tank can get out of the 
    	 * shell's path. After reaching this orientation, the tank will move 
    	 * forwards or backwards until the shell is moving away from it. We need
    	 * to choose if the tank will move to a position that is to the right or
    	 * to the left of the direction of the shell. This boolean variable 
    	 * represents that choice.*/
    	boolean rightAvoidance;
    	
    	/*We use line equation to find out what side of the shell's direction 
    	 * this tank is located in. Check if the tank is under the direction 
    	 * of the shell (y axis is inverted in Greenfoot).*/
    	if(getY()>shellSlope*getX()+yIntercept)
    	{
    		/*Check if the rotation of the shell is betwen 0 and 90 or between
    		 * 270 and 359.*/
    		if(shellRotation<90 || shellRotation>270)
    		{
    			/*if so, the right side of the line of the direction of the shell
    			 * is under it, and that is where the tank should go.*/
    			rightAvoidance=true;
    		}
    		/*else the right side of the line of the direction of the shell
			 * is over it and the left side under it, and that is where the 
			 * tank should go.*/
    		else
    		{
    			rightAvoidance=false;
    		}
    	}
    	/*Else, the tank is located over the line of the direction of the shell.*/
    	else
    	{
    		/*Check if the rotation of the shell is betwen 0 and 90 or between
    		 * 270 and 359.*/
    		if(shellRotation<90 || shellRotation>270)
    		{
    			/*if so, the right side of the line of the direction of the shell
    			 * is under it, and that is where the tank should go.*/
    			rightAvoidance=false;
    		}
    		/*else the right side of the line of the direction of the shell
			 * is over it and the left side under it, and that is where the 
			 * tank should go.*/
    		else
    		{
    			rightAvoidance=true;
    		}
    	}
    	/*Check if the tank needs to turn towards the right side of the line 
    	 * of the shell's direction.*/
    	if(rightAvoidance)
    	{
    		/*if so, the target rotation of the tank is 90 degrees ahead clockwise 
    		 * of the rotation of the shell*/
    		targetRotation=(int)normalizeAngle(shellRotation+90);
    	}
    	/*else, the tank needs to turn to the left side of the line of the 
    	 * shell's direction*/
    	else
    	{
    		/*if so, the target rotation of the tank is 90 degrees behind clockwise 
    		 * of the rotation of the shell*/
    		targetRotation=(int)normalizeAngle(shellRotation-90);
    	}
    	
    	/*We need to decide which way the tank will turn. So we calculate the 
    	 * number of degrees between the desired rotation and the tank's current
    	 * rotation in a clockwise way and in a counter clockwise way.*/
		int clockwiseDiff=(int)normalizeAngle(targetRotation-getRotation());
		int counterClockwiseDiff=(int)normalizeAngle(getRotation()-targetRotation);
    	
		/*Check if the desired rotation is within 90 degrees of the current
		 * rotation of the tank.*/
		if((clockwiseDiff<=90) || (counterClockwiseDiff<=90))
		{
			/*if so, the front side of the tank is closer to the target 
			 * rotation, and the tank will move forward.*/
			isMovingForward=true;
			isMovingBackward=false;
		}
		/*else, the back side of the tank is closer to the target rotation, and
		 * the tank will move backwards.*/
		else
		{
			isMovingForward=false;
			isMovingBackward=true;
			
			/*Since the rotation of the tank is where it's front side faces,
			 * in order for the back side of the tank to face the calculated 
			 * target rotation, the target rotation is changed to the 
			 * diametrically opposite value.*/
			targetRotation=(int)normalizeAngle(targetRotation+180);
		}
		
		//in any case, the tank is moving
		isMoving=true;
    }
    
    /**Makes the tank avoid the incoming shell, based on the targetRotation and 
     * the boolean flags set by the calculateShellAvoidance(Shell) method.*/
    private void avoidIncomingShell()
    {
    	//turn the tank towards's it's target rotation
    	turn(calculateTurn());
    	
    	/*Check if the tank is supposed to go forwards.*/
    	if(isMovingForward && !isMovingBackward)
    	{
    		/*Check if it can do so without hitting a wall.*/
    		if(canMoveForwards())
    		{
    			//if so, move forward at maximum speed
    			move(getSpeed());
    		}
    	}
    	/*else, check if the tank is supposed to go backwards*/
    	else if(!isMovingForward && isMovingBackward)
    	{
    		/*Check if it can do so without hitting a wall.*/
    		if(canMoveBackwards())
    		{
    			//if so, move backward at maximum speed
    			move(-getSpeed());
    		}
    	}
    	/*Else, if both flags have the same value, something is wrong and an 
    	 * exception is thrown.*/
    	else
    	{
    		throw new IllegalStateException("The boolean methods isMovingForward() "
    				+ "and isMovingBackward() return values that are either both "
    				+ "true (which is illogical) or both false (this method has"
    				+ " been called when it should not have been).");
    	}
    }
    
    /**
     * Computes whether or not the given shell is moving away from the current 
     * position of this tank or not.
     * @param shell The shell that we want to see if it is moving away from this tank.
     * @return True if it is moving away from tank, false if not.
     */
    private boolean shellIsMovingAway(Shell shell)
    {
    	//get the horizontal and vertical distances between the tank and the 
    	//shell based on their coordinates 
    	double xDistance=realX-shell.getRealX();
    	double yDistance=realY-shell.getRealY();
    	
    	/*Calculate the distance between the shell and the tank using 
    	 * Pythagora's theorem.*/
    	double distance=Math.sqrt((xDistance*xDistance)+(yDistance*yDistance));
    	
    	//calculate the rotation of the shell in radians
    	double radians = Math.toRadians(shell.getRotation());
    	
    	/*Calculate the future coordinates of the shell, as if it's move(int) 
    	 * method would be called again once.*/
    	double shellFutureX=shell.getRealX() + (Math.cos(radians) * shell.getSpeed());
    	double shellFutureY=shell.getRealY() + (Math.sin(radians) * shell.getSpeed());
    	
    	//get the horizontal and vertical distances between the tank and the 
    	//future position of the shell based on their coordinates
    	xDistance=realX-shellFutureX;
    	yDistance=realY-shellFutureY;
    	
    	/*Calculate the distance using Pythagora's theorem.*/
    	double futureDistance = Math.sqrt((xDistance * xDistance) + 
    			(yDistance * yDistance));
    	
    	/*Check if the distance of the future position of the shell to the tank is larger than
    	 * the distance of the shell to the tank.*/
    	if((futureDistance-distance)>0)
    	{
    		//if it is, then the shell is moving away from the tank
    		return true;
    	}
    	//else, the shell is moving closer to the tank
    	else
    	{
    		return false;
    	}
    }
    
    /**
     * Detects if this tank is dangerously close to a mine. It returns a 
     * reference to one of the mines that is too close to the tank so that
     * the tank will be able to avoid that mine. The tanks are not very 
     * smart, so if more than one mine is too close to the tank, it will 
     * only try to avoid one.
     * @return A reference to one of the mines that is dangerously close to 
     * this tank.
     */
    private LandMine detectLandMines()
    {
    	//get a list of all the mines within a circle whose radius is the 
    	//minimum distance this type of tank keeps away from mines
    	List<LandMine> mines=getObjectsInRange(getMineAvoidanceDistance(), 
    			LandMine.class);
    	
    	//Check if the list is empty
    	if(mines.isEmpty())
    	{
    		//if it is, the tank has no mines to avoid, so it returns null
    		return null;
    	}
    	/*Else, the tank has to avoid a mine, unless the mine in question is
    	 * one that this tank has just laid.*/
    	else
    	{
    		//get a reference to the first mine in the list
    		LandMine mine=mines.get(0);
    		
    		/*The mine in question may be one this tank has just laid, in which
    		 * case the tank may not need to avoid it.*/
    		if(mine.getParentTank().equals(this))
    		{	
    			/*Check if the mine this tank has laid can be ignored by this tank.*/
    			if(mine.canBeIgnoredByParent())
    			{
    				//if it can, return null so the tank will continue it's path
    				return null;
    			}
    			/*Else, this tank is approaching this mine, so return it to 
    			 * avoid it.*/
    			else
    			{
    				return mine;
    			}
    		}
    		/*If it is not, the tank needs to avoid it, so a reference to the mine 
    		 * is returned.*/
    		else
    		{
    			return mine;
    		}
    	}
    }
    
    /**
     * This method modifies the path this tank follows so that it will avoid the
     * mine given as an argument.
     * @param mine The mine that is to be avoided by this tank.
     */
    private void avoidLandMine(LandMine mine)
    {
    	/*In order to avoid this mine, nodes that are too close to the mine must
    	 * be removed from the path.*/
    	try
		{
    		/*Removes the first node from the path until the first node is at 
    		 * a safe distance away from the mine given.*/
			while(path.getFirst().getDistanceFrom(mine)<getMineAvoidanceDistance())
	    	{
	    		path.removeFirst();
	    	}
		}
    	/*If in this process the path becomes empty,
   	     * we catch the exception that is thrown.*/
    	catch(NullPointerException | NoSuchElementException e)
		{
    		/*If so, set the path to null and return to make the tank generate 
    		 * a new path for it to follow.*/
    		path=null;
    		return;
		}
    	
    	//Get a reference to the world this tank is in and to the world graph
    	TankWorld world=(TankWorld)getWorldOfType(TankWorld.class);
    	Graph worldGraph=world.getWorldGraph();
    	
    	/*If the path has not become empty by removing nodes, then the 
    	 * destination point for the path that is avoiding the mine is the 
    	 * remaining first node of the regular path.*/
    	GraphPoint target=path.getFirst();
    	
    	/*Use the world graph method getPathAvoidingMine to generate a path of 
    	 * nodes that avoids the given mine and that leads the tank to the 
    	 * first node of the regular path. This method uses a modified version
    	 * of the Shortest Path Algorithm.*/
    	LinkedList<GraphPoint> avoidancePath=worldGraph.getPathAvoidingMine
    			(this, mine , target);
    	
    	/*Check if the returned path is null, which may happen if the mine blocks
    	 * the only path leading to the destination point and there is not enough 
    	 * space around it.*/
    	if(avoidancePath!=null)
    	{
    		//the tank is currently avoiding the mine, so set the flag accordingly.
        	avoidingMine=true;
        	
        	/*insert the path returned by the getPathAvoidingMine method in the
        	 * beginning of the regular path of the tank*/
    		path.addAll(0, avoidancePath);
    	}
    	/*If it is null, set the entire path to null so the tank will generate a
    	 * new path to follow.*/
    	else
    	{
    		path=null;
    	}
    }
    
    /**Makes the tank randomly lay mines, based on how many mines this type of 
     * tank can lay and how often it can do such.*/
    @Override
    protected void layMine()
    {
    	/*Check if this tank can lay any more mines.*/
    	if(minesLaid<getNumberOfMines())
    	{
    		/*Check if the next point in time this tank should lay a mine has 
    		 * passed.*/
    		if(System.currentTimeMillis()>=nextMineLayingTime && safeToLayMine())
    		{
    			//lay a mine and generate the time the next mine will be laid
    			super.layMine();
    			generateNextMineLayingTime();
    		}
    	}
    }
    
    /**Generates a new random point in time this tank will lay the next mine. 
     * This point in time will be in a random number of milliseconds from now
     * on. This random number is between half of the mine laying period of 
     * this tank and the mine laying period of this tank.*/
    private void generateNextMineLayingTime()
    {
    	/*Generate a new random number, that will be at least half of the mine 
         * laying period of this tank and at most equal to the mine laying 
         * period of this tank.*/
    	try
    	{
    		int number = numberGenerator.nextInt(getMineLayingPeriod() / 2) + 
        			(getMineLayingPeriod() / 2); 
    		
    		/*add the random number to the current time to get the next time a 
        	 * mine will be laid.*/
        	nextMineLayingTime=System.currentTimeMillis()+number;
    	}
    	catch(IllegalArgumentException e)
    	{
    		nextMineLayingTime=0;
    	}
    }
    
    /**
     * Determines if the tank is safely away from other enemy tanks so it can 
     * lay a mine without potentially destroying other enemy tanks.
     * @return True if it is far away from other tanks, false if not.
     */
    private boolean safeToLayMine()
    {
    	//get a list of all mobile enemy tanks within a radius bigger than all 
    	//their mine avoidance distances could be.
    	List<MobileEnemyTank> neighbouringMobileTanks=getObjectsInRange
    			(4*Tank.LENGTH, MobileEnemyTank.class);
    	
    	for(MobileEnemyTank mobileTank: neighbouringMobileTanks)
    	{
    		/*Check if each mobile enemy tanks is further away from this tank
    		 * than each one's mine avoidance distance.*/
    		if(getDistanceFrom(mobileTank)<mobileTank.getMineAvoidanceDistance())
    		{
    			return false;
    		}
    	}
    	
    	/*get a list of all static enemy tanks inside a circle with the radius 
    	 *being the explosion range of the mine.*/
    	List<StaticEnemyTank> neighbouringStaticTanks=getObjectsInRange
    			(LandMine.EXPLOSION_RANGE, StaticEnemyTank.class);
    	
    	/*check if the list is empty, so a mine will not be placed if by doing so
    	 * the static tank would be destroyed.*/
    	if(!neighbouringStaticTanks.isEmpty())
    	{
    		return false;
    	}
    	
    	return true;
    }
    
    /**
	 * Checks if the tank is moving.
	 * @return True if the tank is moving, false if not. Returns a boolean set 
	 * according to the tank's movements in the private methods that control
	 * this tank's movements.
	 */
    @Override
    public boolean isMoving()
    {
    	return isMoving;
    }
    
    /***
   	 * Checks if the tank is moving forward.
   	 * @return True if the tank is moving forward, false if not. Returns a 
   	 * boolean set according to the tank's movements in the private methods
   	 * that control this tank's movements.
   	 */
    @Override
    public boolean isMovingForward()
    {
    	return isMovingForward;
    }
    
    /***
   	 * Checks if the tank is moving backward.
   	 * @return True if the tank is moving backward, false if not. Returns a 
   	 * boolean set according to the tank's movements in the private methods
   	 * that control this tank's movements.
   	 */
    @Override
    public boolean isMovingBackward()
    {
    	return isMovingBackward;
    }
    
    /**Method reloads this tank into the game world to prepare it for another start
	 * of the current level, meaning it resets the position and orientation of this
	 * tank and it's turret, and all other instance variables. */
    @Override
    public void reloadTank()
    {
    	/*We need to set the to null, since if the player lost and level was
    	 * reloaded, this tank would still try to follow the path it had before
    	 * the reload. This lead to the tank sometimes just driving thorough 
    	 * walls.*/
    	path=null;
    	nextPoint=null;
    	avoidingMine=false;
    	generateNextMineLayingTime();
    	
    	//tank does not move initially
        isMoving=false;
        isMovingForward=false;
        isMovingBackward=false;
        
    	//call superclass method to reload the tank
    	super.reloadTank();
    }
    
    /**
     * Indicates the safe distance this type of tank will keep from a mine when
     * it is avoiding a mine. 
     * @return the safe distance this type of tank will keep away from a mine.
     */
    public int getMineAvoidanceDistance()
    {
    	return (int)(getLengthMultiplier()*LENGTH);
    }
    
    /**
     * Indicates a real number that will be multiplied by the length of this 
     * tank and cast as an integer to get the mine avoidance distance of this tank
     * @return a number by which the length of this will be multiplied to get the 
     * mine avoidance distance of this tank.
     */
    public abstract double getLengthMultiplier();
    
    /**
     * Indicates the distance from which the tank starts evasive action to avoid
     * an incoming shell.
     * @return the distance from which the tank starts evasive action to avoid
     * an incoming shell.
     */
    public abstract int getShellAvoidanceDistance();
    
    /**
     * Gets a number that indicates the maximum period in milliseconds
     * between when mines are laid. The higher the number is , the tank will
     * lay mines more rarely.  Unless overridden, it returns 0, so that mines 
     * will not be laid. It should not be overriden if the tank extending this class 
     * does not lay mines.
     * @return The maximum period in milliseconds between when mines 
     * are laid by this tank.
     */
    public int getMineLayingPeriod()
    {
    	return 0;
    }
}
