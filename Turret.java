import greenfoot.*; 

public class Turret extends Actor
{
    private MouseInfo lastMouseInfo;
    private Tank tank;
    
    private static int DEFAULT_MOUSE_X=200;
    private static int DEFAULT_MOUSE_Y=210;
    private static TargetLine[] targetLines;
    
    public Turret(Tank tank)
    {
    	super();
        lastMouseInfo=null;
        this.tank=tank;
        TankWorld world=(TankWorld)tank.getWorld();
        if(world!=null)
        {
            world.addObject(this,tank.getX(),tank.getY());
            tank.getImage().drawImage(this.getImage(),tank.getX(),tank.getY());
        }
        
        targetLines=new TargetLine[TargetLine.NR_LINES];
        Target playerTarget=world.getTankTarget();
        
        for (int i = 0;  i< TargetLine.NR_LINES; i++)
        {
        	targetLines[i]=new TargetLine(this, playerTarget,i+1);
        	world.addObject(targetLines[i], targetLines[i].getNewX(), 
        			targetLines[i].getNewY());
        }
    }
    
    
    /**
     * Act - do whatever the Turret wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        aim();
        fire();
    }    
    
    public TargetLine[] getTargetLines()
    {
    	return targetLines;
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
       TankWorld tankWorld=(TankWorld)this.getWorld();
       tankWorld.getTankTarget().setLocation(mouseX,mouseY);
    }
    
    private void fire()
    {
       MouseInfo mouse=Greenfoot.getMouseInfo();
        
       if(mouse!=null)
       {
           lastMouseInfo=mouse;
       }
       
       if(lastMouseInfo!=null)
       {
    	   TankWorld tankWorld=(TankWorld) getWorld();
           if(lmbClicked() && tankWorld.numOfPlayerShells()<TankWorld.getPlayerShellsAllowed())
           {
        	   Shell tankShell=new Shell(this.getRotation(), tank, getShellX(), getShellY());
           }
       }
    }
    
    private boolean lmbClicked()
    {
    	TankWorld world=(TankWorld) getWorld();
    	MouseInfo mouse=Greenfoot.getMouseInfo();
    	
    	if(mouse!=null)
    	{
    		lastMouseInfo=mouse;
    	}
    	
    	if(Greenfoot.mouseClicked(world.getTankTarget()) && lastMouseInfo.getButton()==1)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    /*private boolean lmbClicked()
    {
    	MouseInfo mouse=Greenfoot.getMouseInfo();
        int mButtonClicked;
        
        
        if(mouse!=null)
        {
            mButtonClicked=mouse.getButton();
        }
        else
        {
        	mButtonClicked=0;
        }
        
        if(lmbPressStart!=0)
        {
        	long current=System.currentTimeMillis();
        	if(mButtonClicked!=1 && current<=lmbPressStart+165)
        	{
        		System.out.println(mouse.hashCode());
                System.out.println(mouse);
        		System.out.println("Time "+ current);
        		System.out.println("button:"+ mButtonClicked);
        		System.out.println("Start: "+lmbPressStart);
        		lmbPressStart=0;
        		return true;
        	}
        	else if(mButtonClicked!=1 && System.currentTimeMillis()>lmbPressStart+165)
        	{
        		lmbPressStart=0;
        	}
        }
        else
        {
        	if(mButtonClicked==1)
        	{
        		System.out.println("Pressed again: ");
        		System.out.println(mouse.hashCode());
        		System.out.println(mouse);
        		lmbPressStart=System.currentTimeMillis();
        	}
        }
        
        return false;
    }*/
    
    private int getShellX()
    {
    	int rotation=getRotation();
    	int shellX=getX()+(int)(30*Math.cos(Math.toRadians(rotation)));
    	
    	return shellX;
    }
    
    private int getShellY()
    {
    	int rotation=getRotation();
    	int shellY=getY()+(int)(30*Math.sin(Math.toRadians(rotation)));
    	
    	return shellY;
    }
    
    @Override
    protected void addedToWorld(World world)
    {
    	TankWorld tankWorld=(TankWorld) world;
    	Target tankTarget=tankWorld.getTankTarget();
    	turnTowards(tankTarget.getX(),tankTarget.getY());
    }
    
    public void deleteTurret()
    {
    	World world=getWorld();
    	
    	for(TargetLine tl: targetLines)
    	{
    		world.removeObject(tl);
    	}
    	
    	world.removeObject(this);
    }
}