
public class Level7World extends TankWorld
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
		addWall(1200, 215+WallBlock.SIDE, false, false, 6);
    	addWall(1200, 215, false, true, -2);
    	addWall(1200-2*WallBlock.SIDE, 215, true, true, -2);
    	addWall(1200-4*WallBlock.SIDE, 215, false, true, -2);
    	addWall(1200-6*WallBlock.SIDE, 215, true, true, 1);
    	addWall(1200-7*WallBlock.SIDE, 215, false, true, -3);
    	addWall(1200-10*WallBlock.SIDE, 215, false, false, 2);
    	
    	addWall(1200-WallBlock.SIDE, 215+6*WallBlock.SIDE, true, true, -2);
    	addWall(1200-3*WallBlock.SIDE, 215+6*WallBlock.SIDE, false, true, -2);
    	addWall(1200-5*WallBlock.SIDE, 215+6*WallBlock.SIDE, true, true, 1);
    	addWall(1200-6*WallBlock.SIDE, 215+6*WallBlock.SIDE, false, true, -2);
    	addWall(1200-8*WallBlock.SIDE, 215+6*WallBlock.SIDE, true, true, -2);
    	addWall(1200-9*WallBlock.SIDE, 215+5*WallBlock.SIDE, false, false, 1);
    	
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(1450, 420, 180);
        addObject(playerTank, 1450, 420);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        GreenTank enemy1=new GreenTank(1010, 435, 180);
        addObject(enemy1, 1010, 435);
        
        TurquoiseTank enemy2=new TurquoiseTank(745, 520, 270);
        addObject(enemy2, 745, 520);
        
        TurquoiseTank enemy3=new TurquoiseTank(310, 680, 0);
        addObject(enemy3, 310, 680);
        
        YellowTank enemy4=new YellowTank(375, 170, 0);
        addObject(enemy4, 375, 170);
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
		return new Level8World();
	}
}
