
public class Level8World extends TankWorld
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
		addWall(680, 230, false, false, 2);
    	addWall(680+WallBlock.SIDE, 230+WallBlock.SIDE, false, false, 2);
    	addWall(680+2*WallBlock.SIDE, 230+2*WallBlock.SIDE, true, false, 2);
    	addWall(680+3*WallBlock.SIDE, 230+3*WallBlock.SIDE, false, false, 2);
    	addWall(680+4*WallBlock.SIDE, 230+4*WallBlock.SIDE, true, false, 2);
    	addWall(680+5*WallBlock.SIDE, 230+5*WallBlock.SIDE, false, false, 2);
    	addWall(680+6*WallBlock.SIDE, 230+6*WallBlock.SIDE, false, false, 2);
    	
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(870, 673, 210);
        addObject(playerTank, 870, 673);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        GreenTank enemy1=new GreenTank(225, 520, 0);
        addObject(enemy1, 225, 520);
        
        GreenTank enemy2=new GreenTank(1410, 580, 270);
        addObject(enemy2, 1410, 580);
        
        GreenTank enemy3=new GreenTank(460, 153, 90);
        addObject(enemy3, 460, 153);
        
        TurquoiseTank enemy4=new TurquoiseTank(190, 280, 0);
        addObject(enemy4, 190, 280);
        
        TurquoiseTank enemy5=new TurquoiseTank(1360, 335, 180);
        addObject(enemy5, 1360, 335);
        
        YellowTank enemy6=new YellowTank(1160, 140, 90);
        addObject(enemy6, 850, 140);
        enemyTanks=6;
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
		return new Level9World();
	}
}

