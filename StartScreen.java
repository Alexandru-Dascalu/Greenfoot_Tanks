import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StartScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StartScreen extends Actor
{
	private static final String START_TEMPLATE="levelStart.png";
	
	public StartScreen(TankWorld world)
	{
    	Color background=new Color(0,0,0,0);
    	GreenfootImage levelNumber= new GreenfootImage(""+world.getLevel(),50,Color.WHITE,background);
    	GreenfootImage enemyTanks=new GreenfootImage(""+world.getNrEnemyTanks(),50,Color.WHITE,background);
    	
    	GreenfootImage newImage=new GreenfootImage(START_TEMPLATE);
    	newImage.drawImage(levelNumber, 480, 70);
    	newImage.drawImage(enemyTanks, 550, 130);
    	setImage(newImage);
	}
	
    /**
     * Act - do whatever the StartScreen wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
    	
    }    
}
