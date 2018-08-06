import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class TurquoiseTank extends Tank
{
    private static final int SPEED=1;
    
    public TurquoiseTank(int startX, int startY)
    {
        super(startX, startY);
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
    		path=world.getWorldGraph().getShortestPath(getX(), getY(), 650, 200);
    	}
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
