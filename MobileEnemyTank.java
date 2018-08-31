import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;
import greenfoot.Greenfoot;

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
	
	protected LinkedList<GraphPoint> path;
	
    public MobileEnemyTank(int startX, int startY)
    {
        super(startX, startY);
        numberGenerator=new Random();
        
        targetRotation=0;
        isMoving=false;
        isMovingForward=false;
        isMovingBackward=false;
        nextPoint=null;
        path=null;
    }
    
    @Override
	public void act()
	{
		if(path!=null)
		{
			followPath();
		}
		else
		{
			generatePath();
		}
		
		super.act();
	}
    
    private void generatePath()
    {
    	TankWorld world=(TankWorld)getWorldOfType(TankWorld.class);
    	path=world.getWorldGraph().getShortestPath(getX(), getY(), chooseTargetPoint());
    }
    
    private GraphPoint chooseTargetPoint()
    {
    	GraphPoint target=null;
    	
    	while(target==null)
    	{
	    	int targetX=numberGenerator.nextInt(TankWorld.LENGTH-2*WallBlock.SIDE);
	    	int targetY=numberGenerator.nextInt(TankWorld.WIDTH-2*WallBlock.SIDE);
	    	
	    	int rowIndex=targetY/GraphPoint.INTERVAL;
	    	int columnIndex=targetX/GraphPoint.INTERVAL;
	    	
	    	targetX+=WallBlock.SIDE;
	    	targetY+=WallBlock.SIDE;
	    	
	    	TankWorld world=(TankWorld)getWorld();
	    	GraphPoint potentialTarget=world.getWorldGraph().getPoint(rowIndex, columnIndex);
	    	
	    	if(potentialTarget!=null)
	    	{
	    		if(world.getPlayerTank().getDistanceFrom(targetX, targetY)>DISTANCE_FROM_PLAYER)
	    		{
	    			target=potentialTarget;
	    		}
	    	}
    	}
    	
    	return target;
    }
    
    private void followPath()
    {
    	if(nextPoint==null)
    	{
    		try
        	{
        		nextPoint=path.getFirst();
        	}
        	catch(NoSuchElementException | NullPointerException e)
        	{
        		path=null;
        		return;
        	}
    	}
    	
	    if(reachedPoint(nextPoint))
	    {
	    	path.removeFirst();
	    	nextPoint=null;
	    }
	    else
	    {
	    	calculateTargetRotation(nextPoint);
	    	if(getRotation()==targetRotation)
	    	{
	   			isMoving=true;
	   			isMovingForward=true;
	   			move(this.getSpeed());
    		}
	    	else
	    	{
	    		int turnSpeed=calculateTurn();
	    		turn(turnSpeed);
	    		isMoving=true;
	    			
	    		if(canMoveWhileTurning(nextPoint))
	    		{
	    			move(this.getSpeed());
		    		isMovingForward=true;
	    		}
	    		else
	    		{
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
    
    private void calculateTargetRotation(GraphPoint nextPoint)
    {
    	double theta=Math.toDegrees(Math.atan2(nextPoint.getY()-getY(), 
    			nextPoint.getX()-getX()));
		targetRotation=(int)Math.round(normalizeAngle(theta));
    }
    
    private int calculateTurn()
    {
		int clockwiseDiff=(int)normalizeAngle(targetRotation-getRotation());
		int counterClockwiseDiff=(int)normalizeAngle(getRotation()-targetRotation);
		
		int maxTurnSpeed=getMaxTurnSpeed();
		int turnSpeed;
		if(clockwiseDiff<counterClockwiseDiff)
		{
			if(clockwiseDiff>=maxTurnSpeed)
			{
				turnSpeed=maxTurnSpeed;
			}
			else
			{
				turnSpeed=clockwiseDiff;
			}
		}
		else
		{
			if(counterClockwiseDiff>=maxTurnSpeed)
			{
				turnSpeed=-maxTurnSpeed;
			}
			else
			{
				turnSpeed=-counterClockwiseDiff;
			}
		}
		
		return turnSpeed;
    }
    
    private boolean canMoveWhileTurning(GraphPoint nextPoint)
    {
    	if(!canMoveForwards())
    	{
    		return false;
    	}
	
    	double radians=Math.toRadians(getRotation());
    	
    	double dx = Math.cos(radians) * getSpeed();
    	double dy = Math.sin(radians) * getSpeed();
    	
    	int tempX=(int) Math.round(realX+dx);
    	int tempY=(int) Math.round(realY+dy);
    	
		double nextTheta=Math.toDegrees(Math.atan2(nextPoint.getY()-tempY, 
				nextPoint.getX()-tempX));
		int nextTargetRotation=(int)Math.round(normalizeAngle(nextTheta));
		
		if(Math.abs(nextTargetRotation-targetRotation)>getMaxTurnSpeed())
		{
			return false;
		}
		else
		{
			return true;
		}
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
    
    public int getMaxTurnSpeed()
    {
    	return 1;
    }
}
