import greenfoot.*;  

public class Tank extends Actor
{
    private Turret tankTurret;
    private GreenfootSound tankDriving;
    
    public Tank()
    {
        super();
        tankDriving=new GreenfootSound("tank_moving.wav");
    }
    
    /**
     * Act - do whatever the Tank wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
       moveAndTurn();
       playSound();
    }    
    
    public void moveAndTurn()
    {
       if(Greenfoot.isKeyDown("w"))
       {
           move(2);
           tankTurret.setLocation(this.getX(),this.getY());
       }
       
       if(Greenfoot.isKeyDown("s"))
       {
          move(-2);
         tankTurret.setLocation(this.getX(),this.getY());
       }
        
       if(Greenfoot.isKeyDown("a"))
       {
           turn(-2);
           tankTurret.turn(-2);
       }
       
       if(Greenfoot.isKeyDown("d"))
       {
          turn(2);
          tankTurret.turn(2);
       }
    }
    
    public void playSound()
    {
       if(isMoving())
       {
          if(!tankDriving.isPlaying())
          {
              tankDriving.play();
          }
       }
       else
       {
           tankDriving.stop();
       }
    }
    
    public boolean isMoving()
    {
        boolean isMoving=Greenfoot.isKeyDown("w")|| Greenfoot.isKeyDown("s")
           || Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("d");
           
        return isMoving;
    }
    
    protected void addedToWorld(World world)
    {
    	setRotation(180);
    	this.tankTurret=new Turret(this);
    }
}

