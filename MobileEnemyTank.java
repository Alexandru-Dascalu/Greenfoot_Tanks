import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import greenfoot.*;

public class MobileEnemyTank extends Tank
{
	private static final int DISTANCE_FROM_PLAYER=150;
	
	private static final int POINT_RADIUS=22;
	
	private Random numberGenerator;
    
	private int targetRotation;
	
	protected boolean isMoving;
	
	protected boolean isMovingForward;
	
	protected boolean isMovingBackward;
	
	protected GraphPoint nextPoint;
	
	protected LinkedList<GraphPoint> regularPath;
	
	protected LinkedList<GraphPoint> avoidancePath;
	
	private int i;
	
	private int j;
	
    public MobileEnemyTank(int startX, int startY)
    {
        super(startX, startY);
        numberGenerator=new Random();
        
        targetRotation=0;
        isMoving=false;
        isMovingForward=false;
        isMovingBackward=false;
        nextPoint=null;
        regularPath=null;
		avoidancePath=null;
    }
    
    @Override
	public void act()
	{
		LandMine mine=detectLandMines();
		if(mine!=null && avoidancePath==null)
		{
			avoidLandMine(mine);
		}
		
    	//Check if there  is a path to follow.
		if(regularPath!=null)
		{
			if(avoidancePath!=null)
			{
				followPath(true);
			}
			else
			{
				//if so, follow that path
				followPath(false);
			}
		}
		//Else, the tank has no path to follow
		else
		{
			//so we generate one
			generatePath();
		}
		
		super.act();
	}
    
    private void generatePath()
    {
    	TankWorld world=(TankWorld)getWorldOfType(TankWorld.class);
    	regularPath=world.getWorldGraph().getShortestPath(getX(), getY(), chooseDestinationPoint());
    	
    	for(GraphPoint gp: regularPath)
    	{
    		gp.setImage(new GreenfootImage("O.png"));
    	}
    	i=0;
    }
    
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
    
    private void followPath(boolean mineAvoidance)
    {
		LinkedList<GraphPoint> path;
		
    	if(mineAvoidance)
    	{
    		path=avoidancePath;
    	}
    	else
    	{
    		path=regularPath;
    	}
		
    	/*Check if it the tank has a reference to the next point it needs to 
    	 * reach.*/
    	if(nextPoint==null)
    	{
    		if(mineAvoidance)
    		{
            	if(j<path.size())
        	   	{
        	   		nextPoint=path.get(j);
        	    	j++;
        	    }
        	    else
        	    {
        	    	for(GraphPoint gp: path)
        	    	{
        	    		gp.getImage().clear();
        	    	}
        	    	
        	    	avoidancePath=null;
        	    	return;
        	    }
    		}
    		else
    		{
    			if(i<path.size())
    	    	{
    	    		nextPoint=path.get(i);
    	    		i++;
    	    	}
    	    	else
    	    	{
    	    		for(GraphPoint gp: path)
    	    		{
    	    			gp.getImage().clear();
    	    		}
    	    		
    	    		regularPath=null;
    	    		return;
    	    	}
    		}
    		
    	}
    	
	    if(reachedPoint(nextPoint))
	    {
	    	//path.removeFirst();
	    	nextPoint=null;
	    }
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
	    			
	    		boolean turnIfCloseToMine;
	    		
	    		if(mineAvoidance)
	    		{
	    			if(normalizeAngle(getRotation()-targetRotation)<10)
	    			{
	    				turnIfCloseToMine=true;
	    			}
	    			else
	    			{
	    				turnIfCloseToMine=false;
	    			}
	    		}
	    		else
	    		{
	    			turnIfCloseToMine=true;
	    		}
	    		/*For the tank's movements to be more natural, the tank should
	    		 * move while turning. Sometimes if the tank does that it 
	    		 * cannot reached the rotation towards the next point or it runs 
	    		 * into a wall. So we check if this tank can move while turning.*/
	    		if(turnIfCloseToMine && canMoveWhileTurning(targetRotation, nextPoint))
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
    
    private boolean reachedPoint(GraphPoint point)
    {
    	int deltaX=this.getX()-point.getX();
    	int deltaY=this.getY()-point.getY();
    	
    	double distance=Math.sqrt(deltaX*deltaX + deltaY*deltaY);
    	
    	if(distance<POINT_RADIUS)
    	{
    		return true;
    	}
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
    
	private LandMine detectLandMines()
    {
    	List<LandMine> mines=getObjectsInRange(2*LENGTH, LandMine.class);
    	
    	if(mines.isEmpty())
    	{
    		return null;
    	}
    	else
    	{
    		return mines.get(0);
    	}
    }
	
	private void avoidLandMine(LandMine mine)
    {
		/*try
		{
			while(regularPath.getFirst().getDistanceFrom(mine)<(2*LENGTH))
	    	{
	    		regularPath.removeFirst();
	    	}
		}
    	catch(NoSuchElementException e)
		{
    		regularPath=null;
    		return;
		}*/
		
		try 
		{
			while(regularPath.get(i).getDistanceFrom(mine)<(2*LENGTH))
			{
				i++;
			}
		}
		catch(IndexOutOfBoundsException e)
		{
    		regularPath=null;
    		return;
		}
    	
    	GraphPoint target=regularPath.get(i);
    	
    	//Get a reference to the world this tank is in
    	TankWorld world=(TankWorld)getWorldOfType(TankWorld.class);
    	Graph worldGraph=world.getWorldGraph();
    	avoidancePath=worldGraph.getPathAvoidingMine
    			(mine, getX(), getY(), target);
		
		for(GraphPoint gp: avoidancePath)
		{
			gp.setImage(new GreenfootImage("blue-circle.png"));
		}
		j=0;
    }
	
    @Override
    public boolean isMoving()
    {
    	return isMoving;
    }
    
    @Override
    protected boolean isMovingForward()
    {
    	return isMovingForward;
    }
    
    @Override
    protected boolean isMovingBackward()
    {
    	return isMovingBackward;
    }
    
	public void reloadTank()
    {
    	/*We need to set the to null, since if the player lost and level was
    	 * reloaded, this tank would still try to follow the path it had before
    	 * the reload. This lead to the tank sometimes just driving thorough 
    	 * walls.*/
    	regularPath=null;
    	avoidancePath=null;
    	nextPoint=null;
    	
    	//call superclass method to reload the tank
    	super.reloadTank();
    }
	
    public int getMaxTurnSpeed()
    {
    	return 0;
    }
}
