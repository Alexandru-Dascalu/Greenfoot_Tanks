import greenfoot.*;

public class LivesMeter extends UIDisplay
{
	private static final String TRAPEZOID_IMAGE="red_trapezoid.png";
	private static final String TEXT="Player lives: ";
	private static final int TEXT_XPOS=15;
	private static final int TEXT_YPOS=8;
	
	public int getNewData()
	{
		TankWorld world=getWorldOfType(TankWorld.class);
		return world.getPlayerLives();
	}
	
	public String getShapeImage()
	{
		return TRAPEZOID_IMAGE;
	}
	
	public String getText()
	{
		return TEXT;
	}
	
	public int getTextXPos()
    {
    	return TEXT_XPOS;
    }
    
    public int getTextYPos()
    {
    	return TEXT_YPOS;
    }
}
