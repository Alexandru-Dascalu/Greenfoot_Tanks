import greenfoot.*;

public class PlayerTank extends Tank
{
    private MouseInfo lastMouseInfo;
    private long lmbPressStart;
    
	public PlayerTank(int startX, int startY)
	{
		super(startX,startY);
		lastMouseInfo=null;
		lmbPressStart=0;
	}
	
	@Override
	public void act()
	{
		updateMouseInfo();
		moveAndTurn();
		playSound();
		
		tankTurret.aim();
		if(lmbClicked()) 
		{
			tankTurret.fire();
		}
		
		if(mmbClicked())
		{
			layMine();
		}
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
	
	private void updateMouseInfo()
	{
		MouseInfo mouse=Greenfoot.getMouseInfo();
	        
		if(mouse!=null)
		{
			lastMouseInfo=mouse;
		} 
	}
	
	private void layMine()
	{
		World world= getWorld();
		LandMine mine=new LandMine(this);
		world.addObject(mine, getX(), getY());
	}
	
	private boolean mmbClicked()
	{
		if(lastMouseInfo!=null)
		{
			return (Greenfoot.mouseClicked(null) && lastMouseInfo.getButton()==2);
		}
		else
		{
			return false;
		}
		
	}
	
	private boolean lmbClicked()
	{
		if(lastMouseInfo!=null)
		{
			if(lmbPressStart!=0)
	    	{
	    		long currentTime=System.currentTimeMillis();
	    		
	    		if(currentTime<=lmbPressStart+165 && Greenfoot.mouseClicked(null) && lastMouseInfo.getButton()==1)
	    		{
	    			lmbPressStart=0;
	    			return true;
	    		}
	    		else if(currentTime>lmbPressStart+165 && Greenfoot.mouseClicked(null))
	    		{
	    			lmbPressStart=0;
	    		}
	    	}
	    	else
	    	{
	    		if(Greenfoot.mousePressed(null) && lastMouseInfo.getButton()==1)
	    		{
	    			lmbPressStart=System.currentTimeMillis();
	    		}
	    	}
		}

		return false;
	}

	@Override
	public boolean isMoving()
	{
		boolean isMoving = Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("s") 
				|| Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("d");

		return isMoving;
	}
	
	@Override
	protected void addedToWorld(World world)
	{
		this.setRotation(180);
		tankTurret=new PlayerTurret(this);
	}
	
	public MouseInfo getMouseInfo()
	{
		return lastMouseInfo;
	}
}
