import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TestWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TestWorld extends TankWorld
{
    
    @Override
    protected void prepare()
    {
        /*Add other walls in the level.*/
        //addWall(820, 315, false, false, 5);
        addWall(820-WallBlock.SIDE, 315-WallBlock.SIDE, false, true, 3);
        addWall(820-WallBlock.SIDE, 315+5*WallBlock.SIDE, false, true, 3);
        
        //make a player tank and add it to the world.
        playerTank=new PlayerTank(190,300,0);
        addObject(playerTank,190,300);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
         BlueTank enemy1 = new BlueTank(1100, 300, 180);
         addObject(enemy1, 1100, 300);
         GreenTank enemy2 = new GreenTank(1200, 500, 90);
         addObject(enemy2, 1200, 500);
        enemyTanks=2;
    }
    
    public TankWorld getNextWorld()
    {
        return new ExampleWorld();
    }
}
