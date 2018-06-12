import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Wall here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class WallBlock extends Actor
{
	private static final int LENGTH=60;
	private static final int HEIGHT=60;
	
    /**
     * Act - do whatever the Wall wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }
    
    public String getQuadrant(int x, int y)
    {
    	int diagSlope=HEIGHT/LENGTH;
    	
    	int topLeftCornerX=getX()-(LENGTH/2);
    	int topLeftCornerY=getY()-(HEIGHT/2);
    	
    	String[][] quadrants= { {"top","right"}, {"left","bottom"} };
    	String[] temp;
    	String corner;
    	
    	if(y-topLeftCornerY<=diagSlope*(x-topLeftCornerX))
    	{
    		temp=quadrants[0];
    	}
    	else
    	{
    		temp=quadrants[1];
    	}
    	
    	if(y-topLeftCornerY-HEIGHT<=(-diagSlope)*(x-topLeftCornerX))
    	{
    		corner=temp[0];
    	}
    	else
    	{
    		corner=temp[1];
    	}
    	
    	return corner;
    }
}
