import greenfoot.*;

public class Tank extends Actor
{
	private Turret tankTurret;
	private GreenfootSound tankDriving;
	private final static double DIAGONAL=35.362409;
	private final static double ANGLE=43.85423781591219;
	public Tank()
	{
		super();
		tankDriving = new GreenfootSound("tank_moving.wav");
	}

	/**
	 * Act - do whatever the Tank wants to do. This method is called whenever the
	 * 'Act' or 'Run' button gets pressed in the environment.
	 */
	public void act()
	{
		moveAndTurn();
		playSound();
	}

	public void moveAndTurn()
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
			tankTurret.turn(-2);
		}

		if (Greenfoot.isKeyDown("d") && canTurnRight())
		{
			turn(2);
			tankTurret.turn(2);
		}
	}

	public boolean canMoveForwards()
	{
		Actor forwardLeft;
		Actor forwardRight;
		Actor forward;

		forwardRight = getOneObjectAtOffset(getXOffset(true), getYOffset(true), WallBlock.class);
		forwardLeft = getOneObjectAtOffset(getXOffset(false), getYOffset(false), WallBlock.class);
		forward= getOneObjectAtOffset(getFrontXOffset(),getFrontYOffset(),WallBlock.class);
		

		if ((forwardRight == null) && (forwardLeft == null) && (forward==null))
		{
			return true;
		} 
		else
		{
			return false;
		}
	}

	public boolean canMoveBackwards()
	{
		Actor backwardLeft;
		Actor backwardRight;
		Actor backward;

		backwardRight = getOneObjectAtOffset(-getXOffset(false), -getYOffset(false), WallBlock.class);
		backwardLeft = getOneObjectAtOffset(-getXOffset(true), -getYOffset(true), WallBlock.class);
		backward= getOneObjectAtOffset(-getFrontXOffset(), -getFrontYOffset(), WallBlock.class);
		
		if ((backwardRight == null) && (backwardLeft == null) && (backward==null))
		{
			return true;
		} 
		else
		{
			return false;
		}
	}
	
	public boolean canTurnRight()
	{
		Actor forwardRight;
		Actor backwardLeft;
		
		forwardRight = getOneObjectAtOffset(getXOffset(true), getYOffset(true), WallBlock.class);
		backwardLeft = getOneObjectAtOffset(-getXOffset(true), -getYOffset(true), WallBlock.class);
		
		return ((forwardRight==null) && (backwardLeft==null));
	}
	
	public boolean canTurnLeft()
	{
		Actor forwardLeft;
		Actor backwardRight;
		
		forwardLeft = getOneObjectAtOffset(getXOffset(false), getYOffset(false), WallBlock.class);
		backwardRight = getOneObjectAtOffset(-getXOffset(false), -getYOffset(false), WallBlock.class);
		
		return ((forwardLeft==null) && (backwardRight==null));
	}

	public boolean isMoving()
	{
		boolean isMoving = Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("a")
				|| Greenfoot.isKeyDown("d");

		return isMoving;
	}

	private int getFrontXOffset()
	{
		int degree=getRotation();
		int xOffset=(int) Math.ceil(25.5*Math.cos(Math.toRadians(degree)));
		
		return xOffset;
	}
	
	private int getFrontYOffset()
	{
		int degree=getRotation();
		int yOffset=(int) Math.ceil(25.5*Math.sin(Math.toRadians(degree)));
		
		return yOffset;
	}
	
	private int getXOffset(boolean right)
	{
		double degree;
		if(right)
		{
			degree = ANGLE+getRotation();
		}
		else
		{
			degree=getRotation()-ANGLE;
		}

		int xOffset = (int) Math.ceil(DIAGONAL * Math.cos(Math.toRadians(degree)));

		return xOffset;
	}

	private int getYOffset(boolean right)
	{
		double degree;
		if(right)
		{
			degree = ANGLE+getRotation();
		}
		else
		{
			degree=getRotation()-ANGLE;
		}
		
		int yOffset = (int) Math.ceil(DIAGONAL * Math.sin(Math.toRadians(degree)));

		return yOffset;
	}

	public void playSound()
	{
		if (isMoving())
		{
			if (!tankDriving.isPlaying())
			{
				tankDriving.play();
			}
		} 
		else
		{
			tankDriving.stop();
		}
	}

	protected void addedToWorld(World world)
	{
		setRotation(180);
		this.tankTurret = new Turret(this);
	}
}
