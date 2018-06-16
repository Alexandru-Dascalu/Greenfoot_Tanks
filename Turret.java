import greenfoot.*; 

public class Turret extends Actor
{
    protected Tank tank;
    
    public Turret(Tank tank)
    {
    	super();
        this.tank=tank;
        TankWorld world=tank.getWorldOfType(TankWorld.class);
        
        if(world!=null)
        {
            world.addObject(this,tank.getX(),tank.getY());
            tank.getImage().drawImage(this.getImage(),tank.getX(),tank.getY());
        }
    }
    
    
    /**
     * Act - do whatever the Turret wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    @Override
    public void act() 
    {
        
    }
     
    protected void fire()
    {
    	Shell tankShell=new Shell(this.getRotation(), tank, getShellX(), getShellY(),false);
    }
    
    protected int getShellX()
    {
    	int rotation=getRotation();
    	int shellX=getX()+(int)(30*Math.cos(Math.toRadians(rotation)));
    	
    	return shellX;
    }
    
    protected int getShellY()
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
    	world.removeObject(this);
    }
}
