import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ExampleWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ExampleWorld extends TankWorld
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
  
        enemyTanks=0;
    }
}
