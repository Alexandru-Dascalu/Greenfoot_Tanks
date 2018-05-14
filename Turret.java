import greenfoot.*; 
/**Write a description of class Turret here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Turret extends Actor
{
    private MouseInfo lastMouseInfo;
    private static final int RATE_OF_FIRE=1000;
    private long lastTimeFired;
    
    public Turret(Tank tank)
    {
        World world=tank.getWorld();
        if(world!=null)
        {
            world.addObject(this,tank.getX(),tank.getY());
            tank.getImage().drawImage(this.getImage(),tank.getX(),tank.getY());
        }
        lastMouseInfo=null;
        lastTimeFired=0;
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
           mouseX=10;
           mouseY=10;
       }
       else
       {
           mouseX=lastMouseInfo.getX();
           mouseY=lastMouseInfo.getY();
       }
        
       this.turnTowards(mouseX,mouseY);     
       MyWorld tankWorld=(MyWorld)this.getWorld();
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
    
}