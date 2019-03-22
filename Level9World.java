
public class Level9World extends TankWorld
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
		addWall(650, 96, false, false, 3);
    	addWall(650, 96+3*WallBlock.SIDE, true, false, 2);
    	addObject(new DestroyableWallBlock(), 650-WallBlock.SIDE, 96+4*WallBlock.SIDE);
    	addWall(650-2*WallBlock.SIDE, 96+4*WallBlock.SIDE, false, true, -3);
    	
    	addWall(1095, 793, false, false, -4);
    	addWall(1095, 793-4*WallBlock.SIDE, true, true, 3);
    	
    	addWall(1085, 245, false, true, 4);
    	addWall(1085+4*WallBlock.SIDE, 245, true, false, 2);
    	
    	addWall(435, 670, false, false, -2);
    	addWall(435+WallBlock.SIDE, 670-WallBlock.SIDE, true, true, 3);
    	addWall(435+4*WallBlock.SIDE, 670-WallBlock.SIDE, false, true, 2);
    	
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(145, 670, 270);
        addObject(playerTank, 145, 670);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        GreenTank enemy1=new GreenTank(488, 157, 90);
        addObject(enemy1, 488, 157);
        
        GreenTank enemy2=new GreenTank(673, 515, 0);
        addObject(enemy2, 673, 515);
        
        GreenTank enemy3=new GreenTank(945, 255, 90);
        addObject(enemy3, 945, 255);
        
        GreenTank enemy4=new GreenTank(1415, 724, 270);
        addObject(enemy4, 1415, 724);
        
        GreenTank enemy5=new GreenTank(1348, 437, 180);
        addObject(enemy5, 1348, 437);
        
        GreenTank enemy6=new GreenTank(960, 730, 270);
        addObject(enemy6, 960, 730);
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
		return new Level10World();
	}
}

