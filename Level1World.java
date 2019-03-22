/**
 * Write a description of class AnotherExample here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Level1World extends TankWorld
{
	/**Prepares the world for the start of a new level, based on the value of the
    * level class variable. It removes all actors in the current world, adds
    * actors for the next level and updates the displays for the number of enemies
    * and the number of player lives left.*/
	@Override
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
	
	/**
     * Returns an instance of a TankWorld that is the next level after this 
     * level. That world will be loaded after the current level is beaten by 
     * the player.
     * @return The next world of the next level of the game. Returns null unless 
     * overridden.
     */
	public TankWorld getNextWorld()
	{
		return new Level2World();
	}
}
