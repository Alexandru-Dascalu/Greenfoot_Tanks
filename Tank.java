import greenfoot.*;

public class Tank extends Actor
{
	protected Turret tankTurret;
	protected static final String DRIVING_SOUND_NAME="tank_moving_1.wav";
	protected GreenfootSound drivingSound;
	protected final static double DIAGONAL=35.362409;
	protected final static double ANGLE=43.85423781591219;
	protected final int startX;
	protected final int startY;
	
	public Tank(int startX, int startY)
	{
		super();
		this.startX=startX;
		this.startY=startY;
		drivingSound = new GreenfootSound(DRIVING_SOUND_NAME);
	}
	
	/**
	 * Act - do whatever the Tank wants to do. This method is called whenever the
	 * 'Act' or 'Run' button gets pressed in the environment.
	 */
	public void act()
	{
		playSound();
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
		return true;
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

	protected void playSound()
	{
		if (isMoving())
		{
			if (!drivingSound.isPlaying())
			{
				drivingSound.play();
			}
		} 
		else
		{
			drivingSound.stop();
		}
	}

	protected void addedToWorld(World world)
	{
		setRotation(180);
		this.tankTurret = new Turret(this);
	}
	
	public Turret getTurret()
	{
		return tankTurret;
	}
	
	public void deleteTank()
	{
		World world= getWorld();
		tankTurret.deleteTurret();
		world.removeObject(this);
	}
	
	public void reloadTank()
	{
		TankWorld world=getWorldOfType(TankWorld.class);
		if(world!=null)
		{
			setLocation(startX,startY);
			tankTurret.setLocation(startX, startY);
			setRotation(180);
			tankTurret.setRotation(180);
		}
	}
}
