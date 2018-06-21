import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TargetLine here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TargetLine extends Actor
{
	public static final int NR_LINES=9;
	private static PlayerTurret PLAYER_TURRET;
	private static Target PLAYER_TARGET;
	//private static int lastNumber=0;
	
	private final int number;
	
	public TargetLine(PlayerTurret playerTurret, Target playerTarget, int number)
	{
		PLAYER_TURRET=playerTurret;
		PLAYER_TARGET=playerTarget;
		this.number=number;
		//lastNumber++;
	}
	
    /**
     * Act - do whatever the TargetLine wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        setRotation(PLAYER_TURRET.getRotation());
        
        setLocation(getNewX(),getNewY());   
    }
    
    public int getNewX()
    {
    	 int xInterval=(int) (getInterval()*Math.cos(Math.toRadians(PLAYER_TURRET
         		.getRotation())));
    	 
    	 int xLine=PLAYER_TURRET.getX()+number*xInterval;
    	 
    	 return xLine;
    }
    
    public int getNewY()
    {
    	 int yInterval=(int) (getInterval()*Math.sin(Math.toRadians(PLAYER_TURRET
         		.getRotation())));
    	 int yLine=PLAYER_TURRET.getY()+number*yInterval;
    	 
    	 return yLine;
    }
    
    private int getInterval()
    {
    	int leg1=PLAYER_TARGET.getX()-PLAYER_TURRET.getX();
        int leg2=PLAYER_TARGET.getY()-PLAYER_TURRET.getY();
        
        int distance = (int)Math.sqrt((leg1 * leg1) + (leg2 * leg2));
        
        int interval=distance/(NR_LINES+1);
        
        return interval;
    }
}
