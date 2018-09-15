import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import greenfoot.Greenfoot;

/**
 * <p><b>File name: </b> MobileEnemyTank.java
 * @version 1.3
 * @since 14.08.2018
 * <p><p><b>Last modification date: </b> 14.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
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
 *  <p>	-.3 - Made tanks avoid land mines.
 */

public class MobileEnemyTank extends Tank
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
	 * while it is avoiding a mine. Used to make the tank move more naturally 
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
	
	/**A flag that indicates if this tank is currently avoiding a mine.*/
	private boolean avoidingMine;
	
	/**The GraphPoint object that is the next point in the game world this tank
	 * needs to reach in it's path.*/
	protected GraphPoint nextPoint;
	
	/***
	 * Makes a new Mobile Enemy Tank whose starting location in the level is at
	 * the given coordinates.
	 * @param startX The x coordinate of the initial position of the tank in 
	 * the level.
	 * @param startY The y coordinate of the initial position of the tank in 
	 * the level.
	 */
    public MobileEnemyTank(int startX, int startY)
    {
    	//call superclass constructor
        super(startX, startY);
        
        //initialise all instance variables of this subclass
        numberGenerator=new Random();
        path=null;
        avoidingMine=false;
        
        //tank does not move initially
        isMoving=false;
        isMovingForward=false;
        isMovingBackward=false;
        
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
		if(!avoidingMine && mine!=null)
		{
			avoidLandMine(mine);
		}
		/*Else, if there is no mine in the vicinity of this tank, set the
		 * mine avoidance flag to false.*/
		else if(mine==null)
		{
			avoidingMine=false;
		}
		
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
    	path=world.getWorldGraph().getShortestPath(getX(), getY(), 
    			chooseDestinationPoint());
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
    		//generate random coordinates within the world's exterior walls
	    	int targetX=numberGenerator.nextInt(TankWorld.LENGTH-2*WallBlock.SIDE);
	    	int targetY=numberGenerator.nextInt(TankWorld.WIDTH-2*WallBlock.SIDE);
	    	
	    	/*divide the coordinates to get the corresponding indexes in the
	    	 * graph's matrix*/
	    	int rowIndex=targetY/GraphPoint.INTERVAL;
	    	int columnIndex=targetX/GraphPoint.INTERVAL;
	    	
	    	/*The generated coordinates start from the left and top exterior 
	    	 * walls of the world, and not from it's edges. So if they are 
	    	 * both 0, it is not the point on the edge of the world, but a 
	    	 * point at the inner edge of the exterior walls. Therefore we add
	    	 * the sides of a wall block to the coordinates to match the real
	    	 * coordinates in the game world.*/
	    	targetX+=WallBlock.SIDE;
	    	targetY+=WallBlock.SIDE;
	    	
	    	//Get the node/point at the calculated indexes from the graph.
	    	TankWorld world=(TankWorld)getWorld();
	    	GraphPoint potentialTarget=world.getWorldGraph().getPoint(rowIndex,
	    			columnIndex);
	    	
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
    private void followPath()
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
	    	int targetRotation=calculateTargetRotation(nextPoint);
	    	
	    	/*Check if the target rotation is the same as this tank's rotation.*/
	    	if(getRotation()==targetRotation)
	    	{
	    		/*If so, this tank is just moving forward set the boolean 
	    		 * values to indicate it and then move the tank using the
	    		 * speed indicated by the subclass.*/
	   			isMoving=true;
	   			isMovingForward=true;
	   			move(this.getSpeed());
    		}
	    	/*Else, this tank needs to turn in the correct direction.*/
	    	else
	    	{
	    		/*Calculate which way and how much this tank should turn and 
	    		 * turn the tank using the returned number.*/
	    		int turnSpeed=calculateTurn(targetRotation);
	    		turn(turnSpeed);
	    		
	    		//if the tank is turning, it is considered to be moving
	    		isMoving=true;
	    		
	    		/*If the tank is very close to a mine, it can be dangerous for 
	    		 * it to turn while moving. The value of this flag will indicate
	    		 * if the tank can move while turning while it is avoiding a mine.*/
	    		boolean turnIfCloseToMine;
	    		
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
	    				turnIfCloseToMine=true;
	    			}
	    			else
	    			{
	    				//it is over the limit, so it can not move while turning
	    				turnIfCloseToMine=false;
	    			}
	    		}
	    		/*If it is not avoiding a mine, this flag is not  */
	    		else
	    		{
	    			turnIfCloseToMine=true;
	    		}
	    		
	    		/*For the tank's movements to be more natural, the tank should
	    		 * move while turning. Sometimes if the tank does that it 
	    		 * cannot reached the rotation towards the next point or it runs 
	    		 * into a wall. So we check if this tank can move while turning.*/
	    		if(turnIfCloseToMine && canMoveWhileTurning(targetRotation, 
	    				nextPoint))
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
     * @param targetRotation The rotation the tank needs to get to in order to
     * reach the next GraphPoint.
     * @return The amount in degrees the tank should turn in this call of the 
     * act() method. Negative values mean the tank will turn counter clockwise,
     * positive values clockwise.
     */
    private int calculateTurn(int targetRotation)
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
     * the tank does that it cannot reached the rotation towards the next 
     * point or it runs into a wall. So it is necessary to check this tank 
     * can move while turning before doing so.
     * @param targetRotation The rotation this tank needs to get to.
     * @param nextPoint The graphPoint whose position this tank needs to reach.
     * @return True if this tank can safely move while turning, false if not.
     */
    private boolean canMoveWhileTurning(int targetRotation, GraphPoint nextPoint)
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
    	//Else, the tank has to avoid a mine
    	else
    	{
    		//return the first mine in the list
    		return mines.get(0);
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
    	catch(NoSuchElementException e)
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
    			(this, mine, getX(), getY(), target);
    	
    	/*insert the path returned by the getPathAvoidingMine method in the
    	 * beginning of the regular path of the tank*/
    	path.addAll(0, avoidancePath);
    	
    	//the tank is currently avoiding the mine, so set the flag accordingly.
    	avoidingMine=true;
    }
    
    /***
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
    protected boolean isMovingForward()
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
    protected boolean isMovingBackward()
    {
    	return isMovingBackward;
    }
    
    /**Method reloads this tank into the game world to prepare it for another start
	 * of the current level, meaning it resets the position and orientation of this
	 * tank and it's turret. */
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
     * @return 0, unless overriden, since this class should always be extended 
     * and there is no set behaviour for a default mobile enemy tank.
     */
    public int getMineAvoidanceDistance()
    {
    	return 0;
    }
}
