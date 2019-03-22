
public class Level5World extends TankWorld
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
		addWall(900, 96, false, false, 3);
    	addWall(900, 96+3*WallBlock.SIDE, true , false, 2);
    	addWall(900-WallBlock.SIDE, 96+3*WallBlock.SIDE, false, true, -5);
    	addWall(900+WallBlock.SIDE, 96+3*WallBlock.SIDE, true, true, 2);
    	addWall(900+3*WallBlock.SIDE, 96+3*WallBlock.SIDE, false, true, 3);
    	
    	addWall(775, 793, false, false, -3);
    	addWall(775, 793-3*WallBlock.SIDE, true , true, -4);
    	addWall(775-4*WallBlock.SIDE, 793-3*WallBlock.SIDE, false , true, -3);
    	addWall(775+WallBlock.SIDE, 793-3*WallBlock.SIDE, false , true, 5);
        
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(155,650, 0);
        addObject(playerTank, 155,650);
       
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        TurquoiseTank enemy1=new TurquoiseTank(700,150, 90);
        addObject(enemy1, 700,150);
        
        BrownTank enemy2=new BrownTank(600,500, 180);
        addObject(enemy2, 600,500);
        
        BrownTank enemy3=new BrownTank(1220, 490, 180);
        addObject(enemy3, 1220, 490);
        
        TurquoiseTank enemy4=new TurquoiseTank(1370,145, 180);
        addObject(enemy4, 1370,145);
        
        YellowTank enemy5=new YellowTank(1440,390, 180);
        addObject(enemy5, 1440,390);
        
        TurquoiseTank enemy6=new TurquoiseTank(1060,750, 0);
        addObject(enemy6, 1060,750);
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
		return new Level6World();
	}
}
