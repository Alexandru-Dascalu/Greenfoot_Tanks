
public class Level2World extends TankWorld 
{
	/**
	 * Prepares the world for the start of a new level, based on the value of the
	 * level class variable. It removes all actors in the current world, adds actors
	 * for the next level and updates the displays for the number of enemies and the
	 * number of player lives left.
	 */
	@Override
	protected void prepare() {
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
	
	/**
     * Returns an instance of a TankWorld that is the next level after this 
     * level. That world will be loaded after the current level is beaten by 
     * the player.
     * @return The next world of the next level of the game. Returns null unless 
     * overridden.
     */
	public TankWorld getNextWorld()
	{
		return new Level3World();
	}
}
