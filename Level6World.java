
public class Level6World extends TankWorld
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
		addWall(420, 220, false, true, 2);
    	addWall(420+2*WallBlock.SIDE, 220, true, true, 3);
    	addWall(420+5*WallBlock.SIDE, 220, false, true, 3);
    	addWall(420+8*WallBlock.SIDE, 220, true, true, 2);
    	addWall(420+10*WallBlock.SIDE, 220, false, true, 3);
    	
    	addWall(440, 450, true, true, 3);
    	addWall(440+3*WallBlock.SIDE, 450, false, true, 3);
    	addWall(440+6*WallBlock.SIDE, 450, true, true, 2);
    	addWall(440+8*WallBlock.SIDE, 450, false, true, 3);
    	addWall(440+11*WallBlock.SIDE, 450, true, true, 2);
    	
    	addWall(380, 650, false, true, 3);
    	addWall(380+3*WallBlock.SIDE, 650, true, true, 1);
    	addWall(380+4*WallBlock.SIDE, 650, false, true, 1);
    	addWall(380+5*WallBlock.SIDE, 650, true, true, 3);
    	addWall(380+8*WallBlock.SIDE, 650, false, true, 3);
    	addWall(380+11*WallBlock.SIDE, 650, true, true, 2);
    	addWall(380+13*WallBlock.SIDE, 650, false, true, 2);
    	
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(120, 360, 0);
        addObject(playerTank, 120, 360);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        BrownTank enemy1=new BrownTank(280, 130, 90);
        addObject(enemy1, 280, 130);
        
        GreenTank enemy2=new GreenTank(1070, 355, 180);
        addObject(enemy2, 1070, 355);
        
        YellowTank enemy3=new YellowTank(725, 116, 90);
        addObject(enemy3, 725, 116);
        
        YellowTank enemy4=new YellowTank(715, 555, 0);
        addObject(enemy4, 715, 555);
        
        YellowTank enemy5=new YellowTank(1330, 280, 180);
        addObject(enemy5, 1330, 280);
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
		return new Level7World();
	}
}
