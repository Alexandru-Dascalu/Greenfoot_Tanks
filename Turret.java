import greenfoot.*; 

public class Turret extends Actor
{
    private MouseInfo lastMouseInfo;
    private static final int RATE_OF_FIRE=1000;
    private long lastTimeFired;
    
    private static int DEFAULT_MOUSE_X=200;
    private static int DEFAULT_MOUSE_Y=210;
    
    public Turret(Tank tank)
    {
        lastMouseInfo=null;
        lastTimeFired=0;
        
        World world=tank.getWorld();
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
           if(lastMouseInfo.getButton()==1 && System.currentTimeMillis()-
                lastTimeFired>RATE_OF_FIRE)
           {
               Shell tankShell=new Shell(this.getRotation());
               this.getWorld().addObject(tankShell,this.getX(),this.getY());
               lastTimeFired=System.currentTimeMillis();
           }
       }
    }
    
    @Override
    protected void addedToWorld(World world)
    {
    	TankWorld tankWorld=(TankWorld) world;
    	Target tankTarget=tankWorld.getTankTarget();
    	turnTowards(tankTarget.getX(),tankTarget.getY());
    }
}
