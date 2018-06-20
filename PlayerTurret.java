import greenfoot.*; 

public class PlayerTurret extends Turret
{
	private static final int DEFAULT_MOUSE_X = 200;
	private static final int DEFAULT_MOUSE_Y = 200;
	
    private static final int SHELLS_ALLOWED=6;
	private static TargetLine[] targetLines;
	
	public PlayerTurret(Tank tank)
	{
		super(tank);
		
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
	
	public void aim()
	{
		int mouseX, mouseY;
		MouseInfo mouseInfo=((PlayerTank)tank).getMouseInfo();
		
		if (mouseInfo == null)
		{
			mouseX = DEFAULT_MOUSE_X;
			mouseY = DEFAULT_MOUSE_Y;
		} 
		else
		{
			mouseX = mouseInfo.getX();
			mouseY = mouseInfo.getY();
		}

		this.turnTowards(mouseX, mouseY);

		TankWorld tankWorld = (TankWorld) this.getWorld();
		tankWorld.getTankTarget().setLocation(mouseX, mouseY);
	}

	public void fire()
	{
		if (liveShells < SHELLS_ALLOWED)
		{
			liveShells++;
			super.fire();
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
