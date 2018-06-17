import greenfoot.*;

public class BrownTank extends Tank
{
	public BrownTank(int startX, int startY)
	{
		super(startX,startY);
	}
	
	public void act()
	{
		super.act();
		
		tankTurret.aim();
		tankTurret.fire();
	}
	
	@Override
	public boolean isMoving()
	{
		return false;
	}
	
	protected void addedToWorld(World world)
	{
		this.setRotation(180);
		tankTurret=new BrownTurret(this);
	}
}
