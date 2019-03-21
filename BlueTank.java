import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BlueTank here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BlueTank extends MobileEnemyTank
{
    public BlueTank(int startX, int startY, int startRotation)
    {
        //simply calls the constructor of the superclass.
        super(startX, startY, startRotation);
        
        
    }
    
    @Override
    protected void addedToWorld(World world)
    {
        this.setRotation(startRotation);
        tankTurret=new YellowTurret(this);
    }
    
    public int getSpeed()
    {
        return 2;
    }
    
    public int getMaxTurnSpeed()
    {
        return 2;
    }
}
