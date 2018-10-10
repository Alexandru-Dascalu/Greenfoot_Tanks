import greenfoot.*;  
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.*;
import greenfoot.core.WorldHandler;
import java.util.List;

/**
 * <p><b>File name: </b> TankWorld.java
 * @version 1.6
 * @since 02.05.2018
 * <p><p><b>Last modification date: </b> 14.08.2018
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
 * <p>	-1.6 - Added a graph of points in the world that are used to find the 
 * shortest path between a moving enemy tank and a selected point.
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
    
    /**The horizontal length of the world. It's value is {@value}. Public so the
     * graph of points enemy tanks pass through is generated correctly based on
     * the size of the world.*/
    public static final int LENGTH=1000;
    
    /**The vertical width of the world. It's value is {@value}.Public so the
     * graph of points enemy tanks pass through is generated correctly based on
     * the size of the world.*/
    public static final int WIDTH=800;
    
    private Graph worldGraph;
    
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
    	
    	//add the walls along the edges of the game world
    	addExternalWalls();
    	
    	/*We use a switch statement to call the appropiate method to prepare
    	 * the world for that specific level, or to display the message for
    	 * winning the game if the last level has been won.*/
    	switch(level)
    	{
    		case 1:
    			prepareLevel1();
    			break;
    		case 2:
    			prepareLevel2();
    			break;
    		case 3: 
    			prepareLevel3();
    			break;
    		case 4: 
        		prepareLevel4();
        		break;
    		case 5: 
        		prepareLevel5();
        		break;
    		case 6:
    			prepareLevel6();
    			break;
    		case 7:
    			prepareLevel7();
    			break;
    		case 8:
    			prepareLevel8();
    			break;
    		case 9:
    			prepareLevel9();
    			break;
    		case 10:
    			prepareLevel10();
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
        
        /*rebuild the graph used for path finding for the current level that
         * was loaded.*/
        worldGraph=new Graph(this);
        
        /*Show the updated start screen for the new level.*/
        showStartScreen();
    }
    
    /**
     * Prepare the world for the start of the first level.
     * That is: create the initial objects and add them to the world.
     */
    private void prepareLevel1()
    {
    	/*Add other walls in the level.*/
    	addWall(580, 290, false, false, 5);
    	addWall(520, 260, false, true, 3);
    	addWall(520 , 560, false, true, 3);
    	
    	//add the player tank target
    	addObject(tankTarget,200,200);
        
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(190,300,0);
        addObject(playerTank,190,300);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        BrownTank enemy1=new BrownTank(910,400,180);
        addObject(enemy1, 910, 400);
        
        BrownTank enemy2=new BrownTank(450,190,90);
        addObject(enemy2, 450,190);
       
        enemyTanks=2;
    }
    
    /**
     * Prepare the world for the start of the second level.
     * That is: create the initial objects and add them to the world.
     */
    private void prepareLevel2()
    {
    	/*Add other walls in the level.*/
    	
    	addWall(310, 250, false, true, 3);
    	addWall(490, 250, true, true, 3);
    	addWall(670, 250, false, true, 3);
    	
    	addWall(280, 570, false, true, 3);
    	addWall(460, 570, true, true, 3);
    	addWall(640, 570, false, true, 3);
    	
    	//add the player tank target
    	addObject(tankTarget,200,200);
        
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(160,290, 0);
        addObject(playerTank, 160,290);
       
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        BrownTank enemy1=new BrownTank(460,480, 0);
        addObject(enemy1, 260,480);
        
        TurquoiseTank enemy2=new TurquoiseTank(860,200, 270);
        addObject(enemy2, 860,200);
        
        TurquoiseTank enemy3=new TurquoiseTank(400,150, 270);
        addObject(enemy3, 400,150);
        enemyTanks=3;
    }
    
    /**
     * Prepare the world for the start of the third level.
     * That is: create the initial objects and add them to the world.
     */
	private void prepareLevel3()
	{
		/*Add other walls in the level.*/
		addWall(90, 600, false, true, 3);
		addWall(320, 195, false, true, 2);
		addWall(320, 255, false, false, 4);
		addWall(380, 435, true, true, 4);
		addWall(620, 435, false, false, 3);
		addWall(620, 615, false, true, -2);
		addWall(790, 230, false, true, 3);
		
		//add the player tank target
    	addObject(tankTarget,200,200);
    	
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(126,400, 0);
        addObject(playerTank, 126, 400);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        TurquoiseTank enemy1=new TurquoiseTank(175, 140, 90);
        addObject(enemy1, 175, 140);
        
        TurquoiseTank enemy2=new TurquoiseTank(400, 665, 90);
        addObject(enemy2, 400, 665);
        
        TurquoiseTank enemy3=new TurquoiseTank(565, 135, 90);
        addObject(enemy3, 565, 135);
        
        TurquoiseTank enemy4=new TurquoiseTank(845, 390, 90);
        addObject(enemy4, 845, 390);
        enemyTanks=4;
	}
	
    /**
     * Prepare the world for the start of the fourth level.
     * That is: create the initial objects and add them to the world.
     */
    private void prepareLevel4()
    {
    	/*Add other walls in the level.*/
    	
    	addWall(90, 320, false, true, 2);
    	addWall(210, 320, true, true, 2);
    	addWall(330, 320, false, true, 3);
    	
    	addWall(670, 200, false, true, -2);
    	addWall(790, 200, true, true, -2);
    	addWall(910, 200, false, true, -2);
    	
    	addWall(90, 560, false, true, 3);
    	addWall(270, 560, true, true, 2);
    	addWall(390, 560, false, true, 2);
    	
    	addWall(910, 440, false, true, -6);
    	//add the player tank target
    	addObject(tankTarget,200,200);
        
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(120,190, 0);
        addObject(playerTank, 120,190);
       
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        TurquoiseTank enemy1=new TurquoiseTank(150,420, 0);
        addObject(enemy1, 150,420);
        
        TurquoiseTank enemy2=new TurquoiseTank(200,660, 0);
        addObject(enemy2, 200,660);
        
        TurquoiseTank enemy3=new TurquoiseTank(860,120, 180);
        addObject(enemy3, 860,120);
        
        TurquoiseTank enemy4=new TurquoiseTank(820,660, 180);
        addObject(enemy4, 820,660);
        
        YellowTank enemy5=new YellowTank(830,330, 180);
        addObject(enemy5, 830,330);
        enemyTanks=5;
    }
    
    /**
     * Prepare the world for the start of the fifth level.
     * That is: create the initial objects and add them to the world.
     */
    private void prepareLevel5()
    {
    	/*Add other walls in the level.*/
    	addWall(570, 90, false, false, 3);
    	addWall(570, 270, true , false, 2);
    	addWall(510, 270, false, true, -5);
    	addWall(630, 270, true, true, 2);
    	addWall(750, 270, false, true, 2);
    	
    	addWall(510, 710, false, false, -3);
    	addWall(510, 530, true , false, -2);
    	addWall(450, 530, true , true, -2);
    	addWall(330, 530, false , true, -3);
    	addWall(570, 530, false , true, 4);
    	
    	//add the player tank target
    	addObject(tankTarget,200,200);
        
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(155,650, 0);
        addObject(playerTank, 155,650);
       
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        TurquoiseTank enemy1=new TurquoiseTank(220,140, 90);
        addObject(enemy1, 220,140);
        
        BrownTank enemy2=new BrownTank(400,415, 180);
        addObject(enemy2, 400,415);
        
        BrownTank enemy3=new BrownTank(644, 400, 180);
        addObject(enemy3, 644, 400);
        
        TurquoiseTank enemy4=new TurquoiseTank(860,120, 180);
        addObject(enemy4, 860,120);
        
        YellowTank enemy5=new YellowTank(830,330, 180);
        addObject(enemy5, 830,330);
        
        TurquoiseTank enemy6=new TurquoiseTank(860,650, 180);
        addObject(enemy6, 860,650);
        enemyTanks=6;
    }
    
    /**
     * Prepare the world for the start of the sixth level.
     * That is: create the initial objects and add them to the world.
     */
    private void prepareLevel6()
    {
    	/*Add other walls in the level.*/
    	addWall(245, 205, false, true, 2);
    	addWall(365, 205, true, true, 2);
    	addWall(485, 205, false, true, 2);
    	addWall(605, 205, true, true, 2);
    	addWall(725, 205, false, true, 2);
    	
    	addWall(235, 425, true, true, 3);
    	addWall(415, 425, false, true, 2);
    	addWall(535, 425, true, true, 2);
    	addWall(655, 425, false, true, 2);
    	addWall(715, 425, true, true, 1);
    	
    	addWall(195, 597, false, true, 2);
    	addWall(315, 597, true, true, 1);
    	addWall(375, 597, false, true, 1);
    	addWall(435, 597, true, true, 2);
    	addWall(555, 597, false, true, 2);
    	addWall(675, 597, true, true, 2);
    	addWall(795, 597, false, true, 1);
    	
    	//add the player tank target
    	addObject(tankTarget,200,200);
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(120, 360, 0);
        addObject(playerTank, 120, 360);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        BrownTank enemy1=new BrownTank(137, 117, 90);
        addObject(enemy1, 137, 117);
        
        BrownTank enemy2=new BrownTank(875, 355, 180);
        addObject(enemy2, 875, 355);
        
        YellowTank enemy3=new YellowTank(518, 116, 90);
        addObject(enemy3, 518, 116);
        
        YellowTank enemy4=new YellowTank(510, 520, 0);
        addObject(enemy4, 510, 520);
        
        YellowTank enemy5=new YellowTank(871, 233, 180);
        addObject(enemy5, 871, 233);
        enemyTanks=5;
    }
    
    /**
     * Prepare the world for the start of the seventh level.
     * That is: create the initial objects and add them to the world.
     */
    private void prepareLevel7()
    {
    	/*Add other walls in the level.*/
    	addWall(780, 230, false, false, 7);
    	addWall(780, 200, false, true, -2);
    	addWall(660, 200, true, true, 1);
    	addWall(600, 200, false, true, -2);
    	addWall(480, 200, true, true, 1);
    	addWall(420, 200, false, true, 1);
    	addWall(360, 200, false, false, 2);
    	
    	addWall(720, 590, true, true, 1);
    	addWall(660, 590, false, true, -2);
    	addWall(540, 590, true, true, 1);
    	addWall(480, 590, false, true, 1);
    	addWall(420, 590, true, true, -2);
    	addWall(300, 590, false, false, -2);
    	
    	//add the player tank target
    	addObject(tankTarget,200,200);
    	
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(880, 375, 180);
        addObject(playerTank, 880, 375);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        BrownTank enemy1=new BrownTank(680, 395, 180);
        addObject(enemy1, 680, 395);
        
        TurquoiseTank enemy2=new TurquoiseTank(485, 490, 270);
        addObject(enemy2, 485, 490);
        
        TurquoiseTank enemy3=new TurquoiseTank(150, 647, 0);
        addObject(enemy3, 150, 647);
        
        YellowTank enemy4=new YellowTank(150, 240, 0);
        addObject(enemy4, 150, 240);
        enemyTanks=4;
    }
    
    /**
     * Prepare the world for the start of the eighth level.
     * That is: create the initial objects and add them to the world.
     */
    private void prepareLevel8()
    {
    	/*Add other walls in the level.*/
    	addObject(new WallBlock(), 305, 200);
    	addObject(new WallBlock(), 305, 250);
    	addWall(365, 250, false, false, 2);
    	addWall(425, 310, true, false, 2);
    	addWall(485, 370, false, false, 2);
    	addWall(545, 430, true, false, 2);
    	addWall(605, 490, false, false, 2);
    	addObject(new WallBlock(), 665, 550);
    	addObject(new WallBlock(), 665, 600);
    	
    	//add the player tank target
    	addObject(tankTarget,200,200);
    	
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(400, 640, 210);
        addObject(playerTank, 400, 640);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        BrownTank enemy1=new BrownTank(125, 450, 0);
        addObject(enemy1, 125, 450);
        
        BrownTank enemy2=new BrownTank(850, 666, 90);
        addObject(enemy2, 850, 666);
        
        BrownTank enemy3=new BrownTank(600, 130, 180);
        addObject(enemy3, 600, 130);
        
        TurquoiseTank enemy4=new TurquoiseTank(125, 280, 0);
        addObject(enemy4, 125, 280);
        
        TurquoiseTank enemy5=new TurquoiseTank(870, 390, 180);
        addObject(enemy5, 870, 390);
        
        YellowTank enemy6=new YellowTank(850, 140, 90);
        addObject(enemy6, 850, 140);
        enemyTanks=6;
    }
    
    /**
     * Prepare the world for the start of the eighth level.
     * That is: create the initial objects and add them to the world.
     */
    private void prepareLevel9()
    {
    	/*Add other walls in the level.*/
    	addWall(390, 90, false, false, 3);
    	addWall(390, 90+3*WallBlock.SIDE, true, false, 2);
    	addObject(new DestroyableWallBlock(), 390-WallBlock.SIDE, 90+4*WallBlock.SIDE);
    	addWall(390-2*WallBlock.SIDE, 90+4*WallBlock.SIDE, false, true, -2);
    	
    	addWall(630, 710, false, false, -3);
    	addWall(630, 710-3*WallBlock.SIDE, true, true, 3);
    	
    	addWall(620, 205, false, true, 3);
    	addWall(620+3*WallBlock.SIDE, 205, true, false, 2);
    	
    	addWall(212, 518, false, false, 2);
    	addWall(273, 592, true, true, 2);
    	addObject(new WallBlock(), 273+2*WallBlock.SIDE, 592);
    	
    	//add the player tank target
    	addObject(tankTarget,200,200);
    	
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(145, 670, 270);
        addObject(playerTank, 145, 670);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        BrownTank enemy1=new BrownTank(185, 110, 90);
        addObject(enemy1, 185, 110);
        
        BrownTank enemy2=new BrownTank(400, 445, 0);
        addObject(enemy2, 400, 445);
        
        BrownTank enemy3=new BrownTank(525, 125, 90);
        addObject(enemy3, 525, 125);
        
        BrownTank enemy4=new BrownTank(500, 688, 270);
        addObject(enemy4, 500, 688);
        
        BrownTank enemy5=new BrownTank(680, 340, 180);
        addObject(enemy5, 680, 340);
        
        BrownTank enemy6=new BrownTank(833, 665, 270);
        addObject(enemy6, 833, 665);
        enemyTanks=6;
    }
    
    /**
     * Prepare the world for the start of the eighth level.
     * That is: create the initial objects and add them to the world.
     */
    private void prepareLevel10()
    {
    	/*Add other walls in the level.*/
    	addWall(295, 215, true, false, 4);
    	addWall(295, 215+(int)3.5*WallBlock.SIDE, true, true, 3);
    	addWall(295+3*WallBlock.SIDE, 215+(int)3.5*WallBlock.SIDE, true, true, 4);
    	addWall(295+7*WallBlock.SIDE, 215+(int)3.5*WallBlock.SIDE, true, false, 3);
    	
    	addWall(390, 710, true, false, -2);
    	addWall(910, 220, true, true, -4);
    	
    	//add the player tank target
    	addObject(tankTarget,200,200);
    	
    	//make a player tank and add it to the world.
        playerTank=new PlayerTank(483, 139, 90);
        addObject(playerTank, 483, 139);
        
        /*make enemy tanks, add them to the world and set number of enemy tanks 
         * accordingly.*/
        BrownTank enemy1=new BrownTank(140, 640, 0);
        addObject(enemy1, 140, 640);
        
        BrownTank enemy2=new BrownTank(528, 690, 180);
        addObject(enemy2, 528, 690);
        
        BrownTank enemy3=new BrownTank(865, 345, 180);
        addObject(enemy3, 865, 345);
        
        TurquoiseTank enemy4=new TurquoiseTank(135, 145, 90);
        addObject(enemy4, 135, 145);
        
        TurquoiseTank enemy5=new TurquoiseTank(540, 550, 90);
        addObject(enemy5, 540, 550);
        
        YellowTank enemy6=new YellowTank(150, 390, 270);
        addObject(enemy6, 150, 390);
        
        YellowTank enemy7=new YellowTank(858, 648, 270);
        addObject(enemy7, 858, 648);
        enemyTanks=6;
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
    	
    	/*Check if the player should receive a bonus life. This happens every 3 levels.*/
    	if(level%3==0)
    	{
    		playerLives++;
    	}
    	
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
    	
    	/*Reload the player tank to it's original place, reset the location of the
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
    
    private void addWall(int startX, int startY, boolean destroyable,
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
