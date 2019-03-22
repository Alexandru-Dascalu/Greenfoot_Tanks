import greenfoot.*;
import java.util.List;

/**
 * <p><b>File name: </b> TankWorld.java
 * @version 1.6
 * @since 02.05.2018
 * <p><b>Last modification date: </b> 14.08.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>TankWorld.java is part of Panzer Batallion.
 * Panzer Batallion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * <p>You should have received a copy of the GNU General Public License v3
 * along with this program.  If not, see <a href="https://www.gnu.org/licenses/">https://www.gnu.org/licenses/</a> .
 * 
 * <p>A summary of the license can be found here: 
 * <a href="https://choosealicense.com/licenses/gpl-3.0/">https://choosealicense.com/licenses/gpl-3.0/</a> .
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
 * <p>	-1.6 - Added a graph of points in the world that are used to find the 
 * shortest path between a moving enemy tank and a selected point.
 */

public abstract class TankWorld extends World
{
	/**The file name of the image displayed when the player dies.*/
	protected static final String MISSION_FAILED="mission_failed.png";
    
    /**The file name of the image displayed when the player dies and there are 0
     * available player lives left.*/
	protected static final String GAME_OVER="game_over.png";
    
    /**The file name of the image displayed when the player defeats all enemies 
     * in a level.*/
	protected static final String MISSION_CLEARED="mission_cleared.png";
    
    /**The file name of the image displayed when the player beats the final level.*/
	protected static final String GAME_WIN="game_Win.png";
    
    /**The number of time steps the mission clear or mission fail message will 
     * show up on the screen, during which the game is paused. Its value is {@value}.*/
	protected static final int END_LEVEL_DELAY = 200;
    
    /**The number of time steps the mission clear or mission fail message will 
     * show up on the screen, during which the game is paused. Its value is {@value}.*/
	protected static final int START_LEVEL_DELAY = 75;
    
    /**The horizontal length of the world. Its value is {@value}. Public so the
     * graph of points enemy tanks pass through is generated correctly based on
     * the size of the world.*/
    public static final int LENGTH=1600;
    
    /**The vertical width of the world. Its value is {@value}.Public so the
     * graph of points enemy tanks pass through is generated correctly based on
     * the size of the world.*/
    public static final int WIDTH=890;
    
    /**The current level the player is playing.*/
    protected static int currentLevel = 0;
    
    /**The graph of nodes used by mobile enemy tanks to generate their paths. 
     * Is remade each time a new level is loaded.*/
    protected Graph worldGraph;
    
    /**The player tank of this game world.*/
    protected PlayerTank playerTank;
    
    /**The number of enemy tanks left in the current level.*/
    protected int enemyTanks;
    
    /**The number of lives the player has left.*/
    protected static int playerLives;
    
    /**
     * Loads the next level of the game, or stops the game if the current level
     * is the last level of the game.
     * @param currentWorld The TankWorld object of the current level.
     */
    public static void loadNextLevel(TankWorld currentWorld)
    {
    	currentLevel++;
    	
    	TankWorld nextWorld;
    	if(currentWorld != null)
    	{
    		nextWorld = currentWorld.getNextWorld();
    	}
    	else
    	{
    		nextWorld = getFirstWorld();
    	}
    	
    	if(nextWorld != null)
    	{
    		Greenfoot.setWorld(nextWorld);
        	nextWorld.addExternalWalls();
        	nextWorld.prepare();
        	nextWorld.initializeLevelStartUI();
    	}
    	else
    	{
    		currentWorld.gameWin();
    	}
    }
    
    public static TankWorld getFirstWorld()
    {
    	return new ExampleWorld();
    }
    
    /**
     * Makes a new TankWorld game world and initialises it's variables. 
     */
    public TankWorld()
    {    
    	/*make a new TankWorld world, which is always LENGTH x WIDTH pixels with the 
    	 * cells being one pixels.It is also bounded so actors can not move 
    	 * outside the world.*/
        super(LENGTH, WIDTH, 1,true);
        
        //the player has 3 lives in the beginning
        playerLives=3;
    }

    /**Ensures that the first level is initiated and that the mouse cursor is hidden
     * when the mouse is in the world.*/
    @Override
    public void act()
    {
    	/*If the game has just started, set the level to level 1 and load it.*/
    	if(currentLevel == 0)
    	{
    		loadNextLevel(null);
    	}
    }
    
    /**Prepares the world for the start of a new level, based on the value of the
     * level class variable. It removes all actors in the current world, adds
     * actors for the next level and updates the displays for the number of enemies
     * and the number of player lives left.*/
    protected void prepare()
    {
    	
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
    	return null;
    }
    
    protected void initializeLevelStartUI()
    {
    	/*Re-add the display elements for the number of player lives and 
    	 * update it.*/
    	LivesMeter livesMeter=new LivesMeter();
        addObject(livesMeter,100,25);
        livesMeter.act();
        
        /*Re-add the display elements for the number of enemy tanks and update it.*/
        EnemyCount enemyCount=new EnemyCount();
        addObject(enemyCount,LENGTH/2,23);
        enemyCount.act();
        
        /*rebuild the graph used for path finding for the current level that
         * was loaded.*/
        worldGraph=new Graph(this);
        
        /*Show the updated start screen for the new level.*/
        showStartScreen();
    }
    
    /**Shows the updated start screen display for the start of this level.
     * It displays the current level number and the number of remaining enemy
     * tanks (this method is also called after a level is reloaded.).*/
    protected void showStartScreen()
    {
    	//make an updated start screen and add it in the middle of the world.
    	StartScreen levelStart=new StartScreen(this);
    	addObject(levelStart,LENGTH/2,WIDTH/2);
    	
    	/*Display the screen for a bit and stop the game meanwhile. Then remove it
    	 * and stop the game for a bit to allow the player to see the layout of
    	 * the level before the game starts.*/
    	Greenfoot.delay(START_LEVEL_DELAY);
    	removeObject(levelStart);
    	Greenfoot.delay(START_LEVEL_DELAY);
    }
    
    /**Displays a message for the player telling him he cleared this level for
     * a bit then removes it and calls the prepare method to load the next level.*/
    protected void missionCleared()
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
    	Greenfoot.delay(END_LEVEL_DELAY);
    	removeObject(missionCleared);
    	
    	/*Check if the player should receive a bonus life. This happens every 3 levels.*/
    	if(currentLevel % 3 == 0)
    	{
    		playerLives++;
    	}
    	
    	//increment the level counter and load the next level.
    	loadNextLevel(this);
    }
    
    /**Displays a message that the player has beaten the game and stops the 
     * game indefinitely.*/
    protected void gameWin()
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
    protected void reloadLevel()
    {
    	/*Make the display that tells the player the mission has been failed, add 
    	 * it, stop the game for a bit then remove it.*/
    	Actor missionFail=new WallBlock();
    	missionFail.setImage(new GreenfootImage(MISSION_FAILED));
    	addObject(missionFail,LENGTH/2,WIDTH/2);
    	Greenfoot.delay(END_LEVEL_DELAY);
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
    	
    	/*Reload the player tank to it's original place, reset the location of the
    	 * tank target and show the display for the (re)start of this level.*/
    	playerTank.reloadTank(this);
    	showStartScreen();
    }
    
    /**Displays a message that the player has lost the game because it has no 
     * remaining lives left, then stops the game indefinitely.*/
    protected void gameOver()
    {
    	/*Make the display telling the player the game is over, put it in the 
    	 * world and stop the game.*/
    	Actor gameOver=new WallBlock();
    	gameOver.setImage(new GreenfootImage(GAME_OVER));
    	addObject(gameOver,LENGTH/2,WIDTH/2);
    	Greenfoot.stop();
    }
    
    /**Adds the wall blocks on the edges of the game world.*/
    protected void addExternalWalls()
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
     * Makes new wall starting from the coordinates given. The wall can be 
     * destroyable or not, can be horizontal or vertical.
     * @param startX The x position of the first block of the wall.
     * @param startY The y position of the first block of the wall.
     * @param destroyable True if the wall will be made from destroyable wall
     * blocks, false if not.
     * @param isHorizontal True if the wall will be horizontal, false if vertical.
     * @param nrOfBlocks The nr of blocks this wall will have. If the number is 
     * negative, the blocks will be put to the left (or above if the wall is
     * vertical) the previous block. If it is positive, they will be put to the
     * right (or under if the wall is vertical).
     */
    protected void addWall(int startX, int startY, boolean destroyable,
    		boolean isHorizontal, int nrOfBlocks)
    {
    	int xInterval;
    	int yInterval;
    	
    	if(isHorizontal)
    	{
    		yInterval=0;
    		xInterval=WallBlock.SIDE * (int)Math.signum(nrOfBlocks);
    	}
    	else
    	{
    		xInterval=0;
    		yInterval=WallBlock.SIDE * (int)Math.signum(nrOfBlocks);
    	}
    	
    	if(destroyable)
    	{
    		for(int i=0; i<Math.abs(nrOfBlocks);i++)
    		{
    			addObject(new DestroyableWallBlock(), startX+xInterval*i,
    					startY+yInterval*i);
    		}
    	}
    	else
    	{
    		for(int i=0; i<Math.abs(nrOfBlocks);i++)
    		{
    			addObject(new WallBlock(), startX+xInterval*i,
    					startY+yInterval*i);
    		}
    	}
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
    public static int getCurrentLevel()
    {
    	return currentLevel;
    }
    
    /**
     * Getter for number of enemy tanks left.
     * @return The number of enemy tanks remaining in the level.
     */
    public int getNrEnemyTanks()
    {
    	return enemyTanks;
    }
    
    /**
     * Getter for the reference to the player tank of this world.
     * @return The player's tank of this game world.
     */
    public PlayerTank getPlayerTank()
    {
    	return playerTank;
    }
    
    /**
     * Getter for the graph used for path finding by enemy tanks.
     * @return The reference to the path finding graph of this game world.
     */
    public Graph getWorldGraph()
    {
    	return worldGraph;
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
