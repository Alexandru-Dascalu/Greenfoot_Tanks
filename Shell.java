import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Shell here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Shell extends Actor
{
    private static final int SHELL_SPEED=6;
    private int direction;
    
    public Shell(int rotation)
    {
        direction=rotation;
        this.setRotation(direction);
    }
    
    /**
     * Act - do whatever the Shell wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        move();
        
        if(isAtEdge())
        {
            this.getWorld().removeObject(this);
        }
    } 
    
    private void move()
    {
        move(SHELL_SPEED);
    }
    
    private boolean needsToBeRemoved()
    {
        if(this.getX()==0 || this.getX()==this.getWorld().getWidth())
        {
            return true;
        }
        else if(this.getY()==0 || this.getY()==this.getWorld().getHeight())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
}
