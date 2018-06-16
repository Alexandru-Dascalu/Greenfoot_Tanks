import greenfoot.*; 

public class PlayerTurret extends Turret
{
	private static final int DEFAULT_MOUSE_X=200;
	private static final int DEFAULT_MOUSE_Y=200;
    
	private static TargetLine[] targetLines;
	private static MouseInfo lastMouseInfo;
	private long lmbPressStart;
	
	public PlayerTurret(Tank tank)
	{
		super(tank);
		lastMouseInfo=null;
		lmbPressStart=0;
		
		TankWorld world=getWorldOfType(TankWorld.class);
		targetLines=new TargetLine[TargetLine.NR_LINES];
        	Target playerTarget=world.getTankTarget();
        
        	for (int i = 0;  i< TargetLine.NR_LINES; i++)
        	{
        		targetLines[i]=new TargetLine(this, playerTarget,i+1);
        		world.addObject(targetLines[i], targetLines[i].getNewX(), 
        			targetLines[i].getNewY());
        	}
	}
	
	@Override
	public void act()
	{
		aim();
		
		fire();
	}
	
	private void aim()
    	{
       		MouseInfo mouse=Greenfoot.getMouseInfo();
        
       		if(mouse!=null)
       		{
          		lastMouseInfo=mouse;
       		}
       
       		int mouseX, mouseY;
       
       		if(lastMouseInfo==null)
       		{
           		mouseX=DEFAULT_MOUSE_X;
          		mouseY=DEFAULT_MOUSE_Y;
       		}
       		else
       		{
          		mouseX=lastMouseInfo.getX();
           		mouseY=lastMouseInfo.getY();
       		}
        
       		this.turnTowards(mouseX,mouseY);
       
	      /* if(mouse!=null)
	       {
		   double deltaX=mouseX-getX();
		   double deltaY=mouseY-getY();
		   double hypotenuse=Math.sqrt(deltaX*deltaX+deltaY*deltaY);

		   double angle=Math.toDegrees(Math.asin(deltaX/hypotenuse));
		   System.out.println("Real angle: "+angle);
		   System.out.println("After aiming: "+getRotation());
	       }*/
	       TankWorld tankWorld=(TankWorld)this.getWorld();
	       tankWorld.getTankTarget().setLocation(mouseX,mouseY);
	 }
	
	protected void fire()
	{
	       MouseInfo mouse=Greenfoot.getMouseInfo();
	       World world= getWorld();

	       if(mouse!=null)
	       {
		   lastMouseInfo=mouse;
	       }

	       if(lastMouseInfo!=null)
	       {
		   TankWorld tankWorld=(TankWorld) getWorld();
		   if(lmbClicked() && tankWorld.numOfPlayerShells()<TankWorld.getPlayerShellsAllowed())
		   {
			   int rotation=this.getRotation();
			   //System.out.println("Before firing:"+rotation);
			   Shell tankShell=new Shell(rotation, tank, getShellX(), getShellY(),true);
		   }
		   else if(mmbClicked())
		   {
			   LandMine mine=new LandMine(tank);
			   world.addObject(mine, getX(), getY());
		   }
	       }
	    }
    
    private boolean lmbClicked()
    {
    	MouseInfo mouse=Greenfoot.getMouseInfo();
    	
    	if(mouse!=null)
    	{
    		lastMouseInfo=mouse;
    	}
    	
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
    	
    	return false;
    }
    
    private boolean mmbClicked()
    {
    	TankWorld world=getWorldOfType(TankWorld.class);
    
    	if(Greenfoot.mouseClicked(world.getTankTarget()) && lastMouseInfo.getButton()==2)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
	public int getMouseButton()
	{
		if(lastMouseInfo!=null)
		{
			return lastMouseInfo.getButton();
		}
		else
		{
			return 0;
		}
	}
	
	public TargetLine[] getTargetLines()
    {
    	return targetLines;
    }
	
	public void deleteTurret()
	{
		World world= getWorld();
		for(TargetLine tl: targetLines)
    	{
    		world.removeObject(tl);
    	}
		
		super.deleteTurret();
	}
}
