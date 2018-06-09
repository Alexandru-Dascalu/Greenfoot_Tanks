import greenfoot.*;

public class BrownTurret extends Turret
{
	private boolean finishTurn;
	private int nextTurn;
	public BrownTurret(BrownTank brownTank)
	{
		super(brownTank);
		finishTurn=true;
	}
	
	public void act() 
	{
		aim();
	}
	
	private void aim()
	{
		if(finishTurn)
		{
			nextTurn=Greenfoot.getRandomNumber(720)-360;
			finishTurn=false;
		}
		else
		{
			if(Math.abs(nextTurn)==getRotation())
			{
				finishTurn=true;
			}
			else
			{
				if(nextTurn>=0)
				{
					turn(1);
				}
				else
				{
					turn(-1);
				}
			}
		}
	}
}
