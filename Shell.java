import greenfoot.*;
import java.util.List;

public class Shell extends Actor
{
    private static final int SHELL_SPEED=6;
    public static final int TIMES_ALLOWED_TO_BOUNCE=1;
    private final static int PI_RADIANS=180;
    
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
    				t.deleteTank();
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
    	else
    	{
    		int xDirection, yDirection;
        	int rotation=getRotation();
        	
        	if(rotation>90 && rotation<270)
        	{
        		xDirection=-1;
        	}
        	else
        	{
        		xDirection=1;
        	}
        	
        	if(rotation<180)
        	{
        		yDirection=1;
        	}
        	else
        	{
        		yDirection=-1;
        	}
        	
	    	if(getOneObjectAtOffset(xDirection*SHELL_SPEED,0,WallBlock.class)!=null)
	    	{
	    		return true;
	    	}
	    	else if(getOneObjectAtOffset(0,yDirection*SHELL_SPEED,WallBlock.class)!=null)
	    	{
	    		return true;
	    	}
	    	else
	    	{
	    		return false;
	    	}
    	}
    }
    
    private void move()
    {
        move(SHELL_SPEED);
    }
    
    private void bounce()
    {
    	
    	int xDirection, yDirection;
    	int rotation=getRotation();
    	
    	if(rotation>90 && rotation<270)
    	{
    		xDirection=-1;
    	}
    	else
    	{
    		xDirection=1;
    	}
    	
    	if(rotation<180)
    	{
    		yDirection=1;
    	}
    	else
    	{
    		yDirection=-1;
    	}
    	
    	if((getX()==0 || getX()==999) || getOneObjectAtOffset(xDirection*SHELL_SPEED,0,WallBlock.class)!=null)
    	{
    		int newDirection=getMirroredVertically(getRotation());
    		setRotation(newDirection);
    	}
    	else if((getY()==0 || getY()==799) || getOneObjectAtOffset(0,yDirection*SHELL_SPEED,WallBlock.class)!=null)
    	{
    		int newDirection=getMirroredHorizontally(getRotation());
    		setRotation(newDirection);
    	}
    	
    	timesBounced++;
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
}
