import greenfoot.*;

public class BrownTurret extends Turret
{
	private static final int DETECT_INTERVAL=10;
	private static final int FIRE_COOLDOWN=1500;
	
	private boolean finishTurn;
	private int nextRotation;
	private int nextTurn;
	private long lastFiring;
	
	public BrownTurret(BrownTank brownTank)
	{
		super(brownTank);
		finishTurn=true;
		lastFiring=0;
	}
	
	public void act() 
	{
		aim();
		
		if(detectTarget())
		{
			if(lastFiring+FIRE_COOLDOWN<System.currentTimeMillis())
			{
				fire();
				lastFiring=System.currentTimeMillis();
			}
		}
	}
	
	private boolean detectTarget()
	{
		int bounces=0;
		double xInterval=DETECT_INTERVAL * Math.cos(Math.toRadians(getRotation()));
		double yInterval=DETECT_INTERVAL * Math.sin(Math.toRadians(getRotation()));
		
		double xRealOffset=xInterval;
		double yRealOffset=yInterval;
		
		boolean cont=true;
		boolean ignoreTank=true;
		
		while(cont)
		{
			int xOffset=(int) Math.ceil(xRealOffset);
			int yOffset=(int) Math.ceil(yRealOffset);
			
			PlayerTank target=(PlayerTank) getOneObjectAtOffset(xOffset,yOffset, PlayerTank.class);
			BrownTank brownTank=(BrownTank) getOneObjectAtOffset(xOffset,yOffset, BrownTank.class);
			WallBlock wall=(WallBlock) getOneObjectAtOffset(xOffset,yOffset,WallBlock.class);
			
			if(brownTank!=null)
			{
				if(!ignoreTank)
				{
					return false;
				}
			}
			else
			{
				ignoreTank=false;
			}
			
			if(target!=null)
			{
				return true;
			}
			else if(wall!=null)
			{
				if(bounces<Shell.TIMES_ALLOWED_TO_BOUNCE)
				{
					String quadrant=wall.getQuadrant(getX()+xOffset, getY()+yOffset);
					
					if(quadrant.equals("left") || quadrant.equals("right"))
					{
						xInterval= -xInterval;
					}
					else if(quadrant.equals("top") || quadrant.equals("bottom"))
					{
						yInterval= -yInterval;
					}
					
					bounces++;
				}
				else
				{
					cont=false;
				}
			}
			
			if((getX()+xOffset>=1000) || (getX()+xOffset<0) || (getY()+yOffset>=800)
					|| (getY()+yOffset<0))
			{
				cont=false;
			}
			
			xRealOffset+=xInterval;
			yRealOffset+=yInterval;
		}
		
		return false;
	}
	
	private void aim()
	{	
		if(finishTurn)
		{
			int turnChance=Greenfoot.getRandomNumber(100);
			int upperLimit;
			
			if(turnChance<60)
			{
				upperLimit=160;
			}
			else
			{
				upperLimit=720;
			}
		
			nextTurn=Greenfoot.getRandomNumber(upperLimit)-(upperLimit/2);
			
			nextRotation=(getRotation()+nextTurn)%360;
			
			if(nextRotation<0)
			{
				nextRotation=360+nextRotation;
			}
		
			finishTurn=false;
		}
		else
		{
			if(nextRotation==getRotation())
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
