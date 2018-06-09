import greenfoot.*;

public class PlayerTank extends Tank
{
	@Override
	public void act()
	{
		moveAndTurn();
		//layMines();
		super.act();
	}
	
	private void moveAndTurn()
	{
		if (Greenfoot.isKeyDown("w") && canMoveForwards())
		{
			move(2);
			tankTurret.setLocation(this.getX(), this.getY());
		}

		if (Greenfoot.isKeyDown("s") && canMoveBackwards())
		{
			move(-2);
			tankTurret.setLocation(this.getX(), this.getY());
		}

		if (Greenfoot.isKeyDown("a") && canTurnLeft())
		{
			turn(-2);
		}

		if (Greenfoot.isKeyDown("d") && canTurnRight())
		{
			turn(2);
		}
	}
	
	/*private void layMines()
	{
		World world= getWorld();
		
		if(mmbClicked())
        {
     	   LandMine mine=new LandMine();
     	   world.addObject(mine, getX(), getY());
        }
	}
	
	private boolean mmbClicked()
	{
		PlayerTurret playerTurret=(PlayerTurret) tankTurret;
		TankWorld world=getWorldOfType(TankWorld.class);
		
		if(playerTurret.getMouseButton()==2)
		{
			System.out.println(Greenfoot.mouseClicked(world.getTankTarget()));
		}
		return (Greenfoot.mouseClicked(world.getTankTarget()) && playerTurret.getMouseButton()==2);
	}*/
	
	@Override
	public boolean isMoving()
	{
		boolean isMoving = Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("a")
				|| Greenfoot.isKeyDown("d");

		return isMoving;
	}
	
	@Override
	protected void addedToWorld(World world)
	{
		this.setRotation(180);
		tankTurret=new PlayerTurret(this);
	}
}
