import greenfoot.*; 

public class Turret extends Actor
{
    protected Tank tank;
    protected int liveShells;
    
    public Turret(Tank tank)
    {
    	super();
        this.tank=tank;
        TankWorld world=tank.getWorldOfType(TankWorld.class);
        liveShells=0;
        
        if(world!=null)
        {
            world.addObject(this,tank.getX(),tank.getY());
            tank.getImage().drawImage(this.getImage(),tank.getX(),tank.getY());
        }
    }
     
    public void fire()
    {
    	Shell tankShell=new Shell(this.getRotation(), tank, getShellX(), getShellY());
    }
    
    public void aim()
    {
    	
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
    
    public void decLiveShells()
    {
    	liveShells--;
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
