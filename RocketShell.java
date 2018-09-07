
public class RocketShell extends Shell
{
	/**The speed with which all shells move. The value is {@value}.*/
    private static final int SHELL_SPEED=6;
    
    /**The number of times the shell is allowed to bounce off a wall.The value
     *  is {@value}.*/
    public static final int TIMES_ALLOWED_TO_BOUNCE=0;
    
    /**The average distance at which the shell looks ahead to check if it will
     * hit a wall. The value is {@value}, because the shell image is 10 by 11 pixels.*/
    private static final int LOOK_AHEAD=20;
    
	public RocketShell(int rotation, Tank parent, int x, int y)
	{
		super(rotation, parent, x ,y);
	}
	
	public int getSpeed()
    {
    	return SHELL_SPEED;
    }
    
    public int getBounceLimit()
    {
    	return TIMES_ALLOWED_TO_BOUNCE;
    }
    
    public int getLookAhead()
    {
    	return LOOK_AHEAD;
    }
}
