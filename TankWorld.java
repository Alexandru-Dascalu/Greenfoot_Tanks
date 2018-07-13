import greenfoot.*;  
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.*;
import greenfoot.core.WorldHandler;
import java.util.List;

/**
 * <p><b>File name: </b> TankWorld.java
 * @version 1.5
 * @since 02.05.2018
 * <p><p><b>Last modification date: </b> 11.07.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a game world for a Greenfoot recreation of the Wii Tanks 
 * game for the Nintendo Wii. This game world is where all actors are placed, it
 * provides a tank target for the player, supports multiple levels displays
 * messages when a level is loaded or reloaded, when a mission is failed or 
 * cleared or when the player beats the game or loses.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Added a player tank target.
 * <p>	-1.2 - Made the mouse cursor to not show up next to the target.
 * <p>	-1.3 - Added external walls to the world.
 * <p>  -1.4 - Added code so that a message is displayed at the start of a level,
 * and appropriate messages are displayed when the mission is cleared or failed.
 * <p>  -1.5 - Made the game world support multiple levels and added a message for 
 * when the last level is beaten.
 */

public class TankWorld extends World
{
	/**The file name of the image displayed when the player dies.*/
    private static final String MISSION_FAILED="mission_failed.png";
    
    /**The file name of the image displayed when the player dies and there are 0
     * available player lives left.*/
    private static final String GAME_OVER="game_over.png";
    
    /**The file name of the image displayed when the player defeats all enemies 
     * in a level.*/
    private static final String MISSION_CLEARED="mission_cleared.png";
    
    /**The file name of the image displayed when the player beats the final level.*/
    private static final String GAME_WIN="game_Win.png";
    
    /**The horizontal length of the world. It's value is {@value}.*/
    private static final int LENGTH=1000;
    
    /**The vertical width of the world. It's value is {@value}.*/
    private static final int WIDTH=800;
    
	/**The tank target display used by the player and moved using the mouse.*/
    private final Target tankTarget;
    
    /**The player tank of this game world.*/
    private PlayerTank playerTank;
    
    /**A transparent custom cursor used to hide the mouse cursor when the mouse is
     * in the game world.*/
    private Cursor customCursor;
    
    /**A JPanel used to hide the mouse cursor when the mouse is in the game world.*/
    private JPanel panel;
    
    /**The number of the current level of the game the player is in.*/
    private int level;
    
    /**The number of enemy tanks left in the current level.*/
    private int enemyTanks;
    
    /**The number of lives the player has left.*/
    private int playerLives;
    
    /**
     * Makes a new TankWorld game world and initialises it's variables. 
     */
    public TankWorld()
    {    
    	/*make a new TankWorld world, which is always 1000x800 pixels with the 
    	 * cells being one pixels.It is also bounded so actors can not move 
    	 * outside the world.*/
        super(LENGTH, WIDTH, 1,true);
        
        //make a new tank target for this game world
        tankTarget=new Target();
        
        //the game has not started so the level is 0
        level=0;
        
        //the player has 3 lives in the beginning
        playerLives=3;
        
        /*Makes a custom transparent cursor to use to hide the default mouse cursor.*/
        hideCursor();
    }

    /**Ensures that the first level is initiated and that the mouse cursor is hidden
     * when the mouse is in the world.*/
    @Override
    public void act()
    {
    	/*If the game has just started, set the level to level 1 and load it.*/
    	if(level==0)
    	{
    		level++;
    		prepare();
    	}
    	
    	//hide the mouse cursor
        panel.setCursor(customCursor);
    }
    
    /**Creates a transparent mouse cursor used to cover and hide the mouse cursor
     * while the mouse is over the game world, so that it does not overlap the 
     * player target.*/
    private void hideCursor()
    {
    	/*I am not an expert on Java Swing, so this is just code I copied from
    	 * https://www.greenfoot.org/topics/821 and made changes for it to
    	 * work for this project.*/
    	Toolkit toolkit=Toolkit.getDefaultToolkit();
        Point defaultPoint=new Point(0,0);
        GreenfootImage emptyImage= new GreenfootImage(5,5);
        customCursor=toolkit.createCustomCursor(emptyImage.getAwtImage(),defaultPoint,
            "Target");
        panel=WorldHandler.getInstance().getWorldCanvas(); 
    }
   
    /**Prepares the world for the start of a new level, based on the value of the
     * level class variable. It removes all actors in the current world, adds
     * actors for the next level and updates the displays for the number of enemies
     * and the number of player lives left.*/
    private void prepare()
    {
    	//get a list of all actors currently in the world
    	List<Actor> actors=getObjects(null);
    	
    	//remove every actor from the world
    	for(Actor a: actors)
    	{
    		removeObject(a);
    	}
    	
    	/*We use a switch statement to call the appropiate method to prepare
    	 * the world for that specific level, or to display the message for
    	 * winning the game if the last level has been won.*/
    	switch(level)
    	{
    		case 1:
    			prepareLevel1();
    			break;
    		/*If the last level has been cleared, then the level counter has been
    		 * incremented beyond the number of levels in the game, so it will
    		 * resort to calling the method that display the game win message and 
    		 * stop the game.*/
    		default:
    			gameWin();
    			return;
    	}
    	
    	/*Re-add the display elements for the number of player lives and 
    	 * update it.*/
    	LivesMeter livesMeter=new LivesMeter();
        addObject(livesMeter,100,25);
        livesMeter.act();
        
        /*Re-add the display elements for the number of enemy tanks and update it.*/
        EnemyCount enemyCount=new EnemyCount();
        addObject(enemyCount,500,23);
        enemyCount.act();
        
        /*Show the updated start screen for the new level.*/
        showStartScreen();
    }
    
    /**
     * Prepare the world for the start of the first level.
     * That is: create the initial objects and add them to the world.
     */
    private void prepareLevel1()
    {
    	//add the walls along the edges of the game world
    	addExternalWalls();
    	
    	/*Add other walls in the level.*/
    	WallBlock wallBlock1=new WallBlock();
    	addObject(wallBlock1,370,360);
    	WallBlock wallBlock2=new WallBlock();
    	addObject(wallBlock2,430,360);
    	WallBlock wallBlock3=new WallBlock();
    	addObject(wallBlock3,490,360);
    	WallBlock wallBlock4=new WallBlock();
    	addObject(wallBlock4,550,360);
    	WallBlock wallBlock5=new WallBlock();
    	addObject(wallBlock5,610,360);
    	WallBlock wallBlock6=new WallBlock();
    	addObject(wallBlock6,670,360);
    	
    	//add the player tank target
    	addObject(tankTarget,200,200);
        
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(900,200);
        addObject(playerTank,900,200);
        
        /*make enemy tanks, add to the world and set number of enemy tanks 
         * accordingly.*/
        BrownTank enemyTank=new BrownTank(300,500);
        addObject(enemyTank, 300, 500);
        enemyTanks=1;
    }
    
    /**Shows the updated start screen display for the start of this level.
     * It displays the current level number and the number of remaining enemy
     * tanks (this method is also called after a level is reloaded.).*/
    private void showStartScreen()
    {
    	//make an updated start screen and add it in the middle of the world.
    	StartScreen levelStart=new StartScreen(this);
    	addObject(levelStart,LENGTH/2,WIDTH/2);
    	
    	/*Display the screen for a bit and stop the game meanwhile. Then remove it
    	 * and stop the game for a bit to allow the player to see the layout of
    	 * the level before the game starts.*/
    	Greenfoot.delay(50);
    	removeObject(levelStart);
    	Greenfoot.delay(50);
    }
    
    /**Displays a message for the player telling him he cleared this level for
     * a bit then removes it and calls the prepare method to load the next level.*/
    private void missionCleared()
    {
    	/*We just need to display an image that always is the same, but we need to 
    	 * remove it so we make an actor and set it's image to the display. And we
    	 * use a wall block actor because they do not override any Actor methods, 
    	 * so if it is casted as an Actor it will behave as a basic actor (Actor 
    	 * is an abstract class so we can not use it directly).*/
    	Actor missionCleared=new WallBlock();
    	
    	//set the image to be a display showing the mission has been cleared
    	missionCleared.setImage(new GreenfootImage(MISSION_CLEARED));
    	
    	//add the display, stop the game for a bit, then remove it
    	addObject(missionCleared,LENGTH/2,WIDTH/2);
    	Greenfoot.delay(300);
    	removeObject(missionCleared);
    	
    	//increment the level counter and load the next level.
    	level++;
    	prepare();
    }
    
    /**Displays a message that the player has beaten the game and stops the 
     * game indefinitely.*/
    private void gameWin()
    {
    	/*We just need to display an image that always is the same. We use a wall
    	 * block actor because they do not override any Actor methods, so if it 
    	 * is casted as an Actor it will behave as a basic actor (Actor  is an
    	 * abstract class so we can not use it directly).*/
    	Actor gameWin=new WallBlock();
    	
    	/*set the image to a display that congratulates the player, add it to the 
    	 * world and stop the game until the player resets the program.*/
    	gameWin.setImage(new GreenfootImage(GAME_WIN));
    	addObject(gameWin,LENGTH/2,WIDTH/2);
    	Greenfoot.stop();
    }
    
    /**Reload the level if the player tank has been destroyed but there are player
     *  lives left. This means the player tank is re-added to the world and enemy
     *  tanks that have not been destroyed are put back at their original place
     *  in the world.*/
    private void reloadLevel()
    {
    	/*Make the display that tells the player the mission has been failed, add 
    	 * it, stop the game for a bit then remove it.*/
    	Actor missionFail=new WallBlock();
    	missionFail.setImage(new GreenfootImage(MISSION_FAILED));
    	addObject(missionFail,LENGTH/2,WIDTH/2);
    	Greenfoot.delay(300);
    	removeObject(missionFail);
    	
    	/*We need to place tanks back at their original place and remove any remaining
    	 * shells and mines, so we get lists of each.*/
    	List<Tank> tanks=getObjects(Tank.class);
    	List<Shell> shells=getObjects(Shell.class);
    	List<LandMine> mines=getObjects(LandMine.class);
    	
    	//remove all land mines
    	for(LandMine lm: mines)
    	{
    		removeObject(lm);
    	}
    	
    	//remove all shells still in the world
    	for(Shell s: shells)
    	{
    		removeObject(s);
    	}
    	
    	//put remaining enemy tanks in their original place
    	for(Tank t: tanks)
    	{
    		t.reloadTank();
    	}
    	
    	/*Re-add the player tank to it's original place, reset the location of the
    	 * tank target and show the display for the (re)start of this level.*/
    	playerTank.reloadTank(this);
    	tankTarget.setLocation(200, 200);
    	showStartScreen();
    }
    
    /**Displays a message that the player has lost the game because it has no 
     * remaining lives left, then stops the game indefinitely.*/
    private void gameOver()
    {
    	/*Make the display telling the player the game is over, put it in the 
    	 * world and stop the game.*/
    	Actor gameOver=new WallBlock();
    	gameOver.setImage(new GreenfootImage(GAME_OVER));
    	addObject(gameOver,LENGTH/2,WIDTH/2);
    	Greenfoot.stop();
    }
    
    /**Adds the wall blocks on the edges of the game world.*/
    private void addExternalWalls()
    {
    	/*Add the wall blocks on the left and right edges of the world. The blocks
    	 * are strictly in the world, so i starts from half of the side of block
    	 * and ends at 830, which is the width of this game world plus half of the 
    	 * side of the block (we add it to make sure we fill in all gaps even if 
    	 * the entire block does not fir i the game world). It is incremented
    	 * by the value of the side of the block, so blocks are put side by side
    	 * until the edge of the world is reached.*/
    	for(int i=(WallBlock.SIDE/2);i<WIDTH+WallBlock.SIDE/2;i+=WallBlock.SIDE)
    	{
    		/*make 2 new blocks, one for the left edge and one for the right edge
    		 * and add them to the world.*/
    		WallBlock wall=new WallBlock();
    		addObject(wall,WallBlock.SIDE/2,i);
    		wall=new WallBlock();
    		addObject(wall,LENGTH-(WallBlock.SIDE/2),i);
    	}
    
    	/*Do the same thing and add the blocks on th top and bottom edges of the 
    	 * world. i starts from 1.5*WallBlock.SIDE because there are already wall
    	 * blocks in the corners of the world, so we start a bit more to the right.*/
    	for(int i=(int)(WallBlock.SIDE*1.5);i<LENGTH;i+=WallBlock.SIDE)
    	{
    		/*make 2 new blocks, one for the bottomedge and for the top edge
    		 * and add them to the world.*/
    		WallBlock wall=new WallBlock();
    		addObject(wall,i,WallBlock.SIDE/2);
    		wall=new WallBlock();
    		addObject(wall,i,WIDTH-(WallBlock.SIDE/2));
    	}
    }
    
    /**
     * Getter for the player tank target.
     * @return A reference to the tank target used by the player to aim.
     */
    public Target getTankTarget()
    {
        return tankTarget;
    }
    
    /**
     * Getter for the number of player lives left.
     * @return The number of lives the player has left.
     */
    public int getPlayerLives()
    {
    	return playerLives;
    }
    
    /**
     * Getter for number of the current level.
     * @return The current level of the game.
     */
    public int getLevel()
    {
    	return level;
    }
    
    /**
     * Getter for number of enemy tanks left.
     * @return The number of enemy tanks remaining in the level.
     */
    public int getNrEnemyTanks()
    {
    	return enemyTanks;
    }
    
    /**Overloads the default removeObject method. This is so that when a shell
     * is removed, the counter of live shells of that shell's parent tank is
     * decremented, then the shell is removed from the game world.
     * @param shell The shell that is to be removed.*/
    public void removeObject(Shell shell)
    {
    	//decrement the counter of shells in the parent tank's turret
    	shell.getParentTank().getTurret().decLiveShells();
    	
    	//now we can remove the shell safely using the default method
    	super.removeObject(shell);
    }
    
    /**
     * Overloads the default removeObject method. This is so that when a tank is
     * removed from the world, we can decide if the mission has been cleared, failed
     * or if the game is over completely.
     * @param tank The tank that is to be removed.
     */
    public void removeObject(Tank tank)
    {
    	/*Call the tank's delete method so that it's turret is also removed.*/
    	tank.deleteTank();
    	
    	/*Check if the removed tank was a player tank.*/
    	if(tank.getClass()==PlayerTank.class)
    	{
    		/*If it is, we decrement the number of player lives left and see if
    		 * we reload the level or stop the game.*/
    		playerLives--;
    		
    		/*Check if there are any player lives left.*/
    		if(playerLives>0)
    		{
    			//if so, reload the current level.
    			reloadLevel();
    		}
    		//else, the game is over
    		else
    		{
    			gameOver();
    		}
    	}
    	/*If it is an enemy tank instead, we decrement the number of enemy tanks 
    	 * and check if the level has been cleared.*/
    	else
    	{
    		enemyTanks--;
    		
    		/*Checks if all enemy tanks have been destroyed.*/
    		if(enemyTanks==0)
    		{
    			//if so, the mission has been cleared.
    			missionCleared();
    		}
    	}
    }
}
