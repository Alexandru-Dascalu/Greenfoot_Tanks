import greenfoot.*;

public class Shell extends Actor
{
    private static final int SHELL_SPEED=6;
    private int direction;
    private static final int TIMES_ALLOWED_TO_BOUNCE=1;
    private int timesBounced;
    
    public Shell(int rotation)
    {
        direction=rotation;
        this.setRotation(direction);
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
        		this.getWorld().removeObject(this);
        	}
        }
    } 
    
    private void bounce()
    {
    	
    }
    
    private void move()
    {
        move(SHELL_SPEED);
    }
}
