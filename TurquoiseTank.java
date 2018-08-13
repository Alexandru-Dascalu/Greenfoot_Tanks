import greenfoot.*;

public class TurquoiseTank extends MobileEnemyTank
{
    private static final int SPEED=1;
    
    private final int MAX_TURN_SPEED=2;
    
    public TurquoiseTank(int startX, int startY)
    {
        super(startX, startY);
    }
    
    @Override
    public int getSpeed()
    {
    	return SPEED;
    }
    
    @Override
    public int getMaxTurnSpeed()
    {
    	return MAX_TURN_SPEED;
    }
    
    @Override
    protected void addedToWorld(World world)
    {
    	this.setRotation(270);
    	tankTurret=new BrownTurret(this);
    }
}
