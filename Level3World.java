
public class Level3World extends TankWorld 
{
	/**
	 * Prepares the world for the start of a new level, based on the value of the
	 * level class variable. It removes all actors in the current world, adds actors
	 * for the next level and updates the displays for the number of enemies and the
	 * number of player lives left.
	 */
	@Override
	protected void prepare() 
	{
		/*Add other walls in the level.*/
		addWall(96, 620, false, true, 5);
		addWall(570, 210, false, true, 3);
		addWall(570, 210+WallBlock.SIDE, false, false, 4);
		addWall(570+WallBlock.SIDE, 210+4*WallBlock.SIDE, true, true, 5);
		addWall(570+6*WallBlock.SIDE, 210+4*WallBlock.SIDE, false, false, 3);
		addWall(570+6*WallBlock.SIDE, 210+7*WallBlock.SIDE, false, true, -3);
		addWall(1245, 250, false, true, 5);
    	
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(140,400, 0);
        addObject(playerTank, 140, 400);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        TurquoiseTank enemy1=new TurquoiseTank(355, 140, 90);
        addObject(enemy1, 355, 140);
        
        TurquoiseTank enemy2=new TurquoiseTank(730, 680, 270);
        addObject(enemy2, 730, 680);
        
        TurquoiseTank enemy3=new TurquoiseTank(1000, 150, 90);
        addObject(enemy3, 1000, 150);
        
        TurquoiseTank enemy4=new TurquoiseTank(1440, 420, 180);
        addObject(enemy4, 1440, 420);
        enemyTanks=4;
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
		return new Level4World();
	}
}
