import greenfoot.*;

public class BrownTank extends Tank
{
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
