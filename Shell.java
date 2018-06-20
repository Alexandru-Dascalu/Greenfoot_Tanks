import greenfoot.*;
import java.util.List;

public class Shell extends Actor
{
    private static final int SHELL_SPEED=6;
    public static final int TIMES_ALLOWED_TO_BOUNCE=1;
    private final static int PI_RADIANS=180;
    private static final int LOOK_AHEAD=11;
    
    private int timesBounced;
    private Tank parentTank;
    private boolean destroyParent;
    
    public Shell(int rotation, Tank parent, int x, int y)
    {
        setRotation(rotation);
        timesBounced=0;
        parentTank=parent;
        destroyParent=false;
        TankWorld world=(TankWorld) parentTank.getWorld();
        world.addObject(this, x, y);
    }
    
    /**
     * Act - do whatever the Shell wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        move();
      
        if(!destroyTargets())
        {
	        if(hitsWall())
	        {
	        	if(timesBounced<TIMES_ALLOWED_TO_BOUNCE)
	        	{
	        		bounce();
	        	}
	        	else
	        	{
	        		TankWorld tankWorld=(TankWorld) getWorld();
	        		tankWorld.removeObject(this);
	        	}
	        }
        }
    }
    
    private boolean destroyTargets()
    {
    	List<Shell> intersectShells=getIntersectingObjects(Shell.class);
    	List<Tank> intersectTanks=getIntersectingObjects(Tank.class);
    	
    	TankWorld world=(TankWorld) getWorld();
    	boolean removeShell=false;
    	
    	if(!intersectShells.isEmpty())
    	{
    		for(Shell s: intersectShells)
    		{
    			world.removeObject(s);
    		}
    		
    		removeShell=true;
    	}
    	
    	if(!intersectTanks.contains(parentTank))
    	{
    		destroyParent=true;
    	}
    	
    	if(!intersectTanks.isEmpty())
    	{  		
    		for(Tank t: intersectTanks)
    		{
    			if((t.equals(parentTank) && destroyParent) || !t.equals(parentTank))
    			{
    				world.removeObject(t);
    				removeShell=true;
    			}
    		}
    	}
    	
    	if(removeShell)
    	{
    		world.removeObject(this);
    	}
    	
    	return removeShell;
    }
    
    private boolean hitsWall()
    {	
    	if(isAtEdge())
    	{
    		return true;
    	}
    	else if(getOneObjectAtOffset(getXOffset(),getYOffset(),WallBlock.class)
    			!=null)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    private void move()
    {
        move(SHELL_SPEED);
    }
    
    private void bounce()
    {
    	String quadrant="";
    	
    	WallBlock wallBlock=(WallBlock)getOneObjectAtOffset(getXOffset(),
    			getYOffset(),WallBlock.class);
    	if(wallBlock!=null)
    	{
    		quadrant=wallBlock.getQuadrant(this.getX(), this.getY());
    	}
    	else
    	{
    		if(getX()==0)
    		{
    			quadrant="left";
    		}
    		else if(getX()==999)
    		{
    			quadrant="right";
    		}
    		else if(getY()==0)
    		{
    			quadrant="top";
    		}
    		else if(getY()==799)
    		{
    			quadrant="bottom";
    		}
    	}
    	
    	if(quadrant.equals("left") || quadrant.equals("right"))
    	{
    		int newDirection=getMirroredVertically(getRotation());
    		setRotation(newDirection);
    	}
    	else if(quadrant.equals("top") || quadrant.equals("bottom"))
    	{
    		int newDirection=getMirroredHorizontally(getRotation());
    		setRotation(newDirection);
    	}
    	
    	timesBounced++;
    }
    
    private int getXOffset()
    {
    	double rotation=Math.toRadians(getRotation());
    	int xOffset=(int) Math.round(LOOK_AHEAD*Math.cos(rotation));
    	
    	if(xOffset==0)
    	{
    		xOffset=(int)Math.signum(rotation);
    	}
    	
    	return xOffset;
    }
    
    private int getYOffset()
    {
    	double rotation=Math.toRadians(getRotation());
    	int yOffset=(int) Math.round(LOOK_AHEAD*Math.sin(rotation));
    	
    	if(yOffset==0)
    	{
    		yOffset=(int)Math.signum(rotation);
    	}
    	
    	return yOffset;
    }
    
    private int getMirroredVertically(int direction)
    {
    	int newDirection=PI_RADIANS-direction;
    	return newDirection;
    }
    
    private int getMirroredHorizontally(int direction)
    {
    	int newDirection=2*PI_RADIANS-direction;
    	return newDirection;
    }
    
    public Tank getTank()
    {
    	return parentTank;
    }
}
