
public class Level10World extends TankWorld
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
		addWall(510, 230, true, false, 4);
    	addWall(510+WallBlock.SIDE, 230+3*WallBlock.SIDE, true, true, 8);
    	addWall(510+8*WallBlock.SIDE, 230+4*WallBlock.SIDE, true, false, 3);
    	
    	addWall(680, 793, true, false, -3);
    	addWall(1503, 246, true, true, -5);
    	
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(827, 244, 90);
        addObject(playerTank, 827, 244);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        GreenTank enemy1=new GreenTank(236, 688, 0);
        addObject(enemy1, 236, 688);
        
        GreenTank enemy2=new GreenTank(923, 744, 180);
        addObject(enemy2, 923, 744);
        
        BrownTank enemy3=new BrownTank(1413, 438, 180);
        addObject(enemy3, 1413, 438);
        
        TurquoiseTank enemy4=new TurquoiseTank(253, 175, 90);
        addObject(enemy4, 253, 175);
        
        TurquoiseTank enemy5=new TurquoiseTank(847, 543, 90);
        addObject(enemy5, 847, 543);
        
        YellowTank enemy6=new YellowTank(237, 457, 270);
        addObject(enemy6, 237, 457);
        
        YellowTank enemy7=new YellowTank(1386, 674, 270);
        addObject(enemy7, 1386, 674);
        enemyTanks=7;
	}
}

