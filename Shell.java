import greenfoot.*;

public class Shell extends Actor
{
    private static final int SHELL_SPEED=6;
    private static final int TIMES_ALLOWED_TO_BOUNCE=1;
    private int timesBounced;
    
    private final static int PI_RADIANS=180;
    
    public Shell(int rotation)
    {
        setRotation(rotation);
        timesBounced=0;
    }
    
    /**
     * Act - do whatever the Shell wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        move();
        
        if(isAtEdge())
        {
        	if(timesBounced<TIMES_ALLOWED_TO_BOUNCE)
        	{
        		bounce();
        	}
        	else
        	{
        		TankWorld tankWorld=(TankWorld) getWorld();
        		tankWorld.removeObject(this);
        		tankWorld.decrementPlayerShells();
        	}
        }
    } 
    
    private void move()
    {
        move(SHELL_SPEED);
    }
    
    private void bounce()
    {
    	if(getX()==0 || getX()==1099)
    	{
    		int newDirection=getMirroredVertically(getRotation());
    		setRotation(newDirection);
    	}
    	else if(getY()==0 || getY()==899)
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
