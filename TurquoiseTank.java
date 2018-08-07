import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;

public class TurquoiseTank extends Tank
{
    private static final int SPEED=1;
    private Random numberGenerator;
    
    public TurquoiseTank(int startX, int startY)
    {
        super(startX, startY);
        numberGenerator=new Random();
    }
    
    @Override
	public void act()
	{
		playSound();
		pushOtherTanks();
		
		generatePath();
		
		if(path!=null)
		{
			followPath();
		}
		
		tankTurret.aim();
		tankTurret.fire();
	}
    
    @Override
    public void generatePath()
    {
    	if(path==null)
    	{
    		TankWorld world=(TankWorld)getWorldOfType(TankWorld.class);
    		path=world.getWorldGraph().getShortestPath(getX(), getY(), chooseTargetPoint());
    	}
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
	    		if(world.getPlayerTank().getDistanceFrom(targetX, targetY)>100)
	    		{
	    			target=potentialTarget;
	    		}
	    	}
    	}
    	
    	return target;
    }
    
    @Override
    public int getSpeed()
    {
    	return SPEED;
    }
    
    @Override
    protected void addedToWorld(World world)
    {
    	this.setRotation(270);
    	tankTurret=new BrownTurret(this);
    }
}
