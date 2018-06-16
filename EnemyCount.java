import greenfoot.*;

public class EnemyCount extends UIDisplay
{
	private static final String TRAPEZOID_IMAGE="blue_trapezoid.png";
	private static final String TEXT="Enemy tanks: ";
	private static final int TEXT_XPOS=38;
	private static final int TEXT_YPOS=8;
	
	public int getNewData()
	{
		TankWorld world=getWorldOfType(TankWorld.class);
		return world.getNrEnemyTanks();
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
