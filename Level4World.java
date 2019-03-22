
public class Level4World extends TankWorld
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
		addWall(97, 340, false, true, 3);
    	addWall(97+3*WallBlock.SIDE, 340, true, true, 3);
    	addWall(97+6*WallBlock.SIDE, 340, false, true, 3);
    	
    	addWall(1504, 260, false, true, -3);
    	addWall(1504-3*WallBlock.SIDE, 260, true, true, -3);
    	addWall(1504-6*WallBlock.SIDE, 260, false, true, -4);
    	
    	addWall(97, 610, false, true, 3);
    	addWall(97+3*WallBlock.SIDE, 610, true, true, 3);
    	addWall(97+6*WallBlock.SIDE, 610, false, true, 4);
    	
    	addWall(1503, 545, false, true, -9);
        
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(140,190, 0);
        addObject(playerTank, 140,190);
       
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        TurquoiseTank enemy1=new TurquoiseTank(150,460, 0);
        addObject(enemy1, 150,460);
        
        TurquoiseTank enemy2=new TurquoiseTank(230,730, 0);
        addObject(enemy2, 230,730);
        
        TurquoiseTank enemy3=new TurquoiseTank(1380,145, 180);
        addObject(enemy3, 1380,145);
        
        TurquoiseTank enemy4=new TurquoiseTank(1410,720, 180);
        addObject(enemy4, 1410,720);
        
        YellowTank enemy5=new YellowTank(1415,405, 180);
        addObject(enemy5, 1415,405);
        enemyTanks=5;
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
		return new Level5World();
	}
}
