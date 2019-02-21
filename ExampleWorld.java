
public class ExampleWorld extends TankWorld 
{
	private static final int LEVEL_NUMBER = 1;
	
	static
	{
		if(LEVEL_NUMBER < gameLevels.size())
		{
			gameLevels.add(LEVEL_NUMBER, new ExampleWorld());
		}
		else
		{
			gameLevels.add(new ExampleWorld());
		}
	}
	
	 /**Prepares the world for the start of a new level, based on the value of the
     * level class variable. It removes all actors in the current world, adds
     * actors for the next level and updates the displays for the number of enemies
     * and the number of player lives left.*/
    protected void prepare()
    {
    	/*Add other walls in the level.*/
    	addWall(820, 315, false, false, 5);
    	addWall(820-WallBlock.SIDE, 315-WallBlock.SIDE, false, true, 3);
    	addWall(820-WallBlock.SIDE , 315+5*WallBlock.SIDE, false, true, 3);
        
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(190,300,0);
        addObject(playerTank,190,300);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        BrownTank enemy1=new BrownTank(1400,440,180);
        addObject(enemy1, 1400, 440);
        
        BrownTank enemy2=new BrownTank(800,170,90);
        addObject(enemy2, 800, 170);
  
        enemyTanks=2;
    }
}
