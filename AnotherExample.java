import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class AnotherExample here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AnotherExample extends TankWorld
{

    private static final int LEVEL_NUMBER = 2;
	
	static
	{
		System.out.println("nkjkkkk");
		if(LEVEL_NUMBER <= gameLevels.size())
		{
			gameLevels.add(LEVEL_NUMBER - 1, new AnotherExample());
		}
		else
		{
			gameLevels.add(new AnotherExample());
		}
	}
	
	 /**Prepares the world for the start of a new level, based on the value of the
     * level class variable. It removes all actors in the current world, adds
     * actors for the next level and updates the displays for the number of enemies
     * and the number of player lives left.*/
    protected void prepare()
    {
    	addWall(580, 270, false, true, 3);
    	addWall(580+3*WallBlock.SIDE, 270, true, true, 3);
    	addWall(580+6*WallBlock.SIDE, 270, false, true, 3);
    	
    	addWall(540, 600, false, true, 3);
    	addWall(540+3*WallBlock.SIDE, 600, true, true, 3);
    	addWall(540+6*WallBlock.SIDE, 600, false, true, 3);
        
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(160,290, 0);
        addObject(playerTank, 160,290);
       
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        BrownTank enemy1=new BrownTank(800,520, 180);
        addObject(enemy1, 800,520);
        
        TurquoiseTank enemy2=new TurquoiseTank(1220,210, 270);
        addObject(enemy2, 1220,210);
        
        TurquoiseTank enemy3=new TurquoiseTank(850,190, 90);
        addObject(enemy3, 850,190);
        enemyTanks=3;
    }
}
