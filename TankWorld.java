import greenfoot.*;  
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.*;
import greenfoot.core.WorldHandler;
import java.util.List;

public class TankWorld extends World
{
    private final Target tankTarget;
    private final PlayerTank playerTank;
    private Cursor customCursor;
    private JPanel panel;
    
    private static final String MISSION_FAILED="mission_failed.png";
    private static final String GAME_OVER="game_over.png";
    private static final String MISSION_CLEARED="mission_cleared.png";
   
    private int level;
    private int enemyTanks;
    private int playerLives;
    private boolean showStartScreen;
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public TankWorld()
    {    
        super(1000, 800, 1,true);
        tankTarget=new Target();
        playerTank=new PlayerTank(900,200);
        level=1;
        playerLives=3;
        showStartScreen=true;
        
        prepare(); 
        
        hideCursor();
    }

    public void act()
    {
    	if(showStartScreen)
    	{
    		showStartScreen();
    	}
    	
        panel.setCursor(customCursor);
    }
    
    private void hideCursor()
    {
    	Toolkit toolkit=Toolkit.getDefaultToolkit();
        Point defaultPoint=new Point(0,0);
        GreenfootImage emptyImage= new GreenfootImage(5,5);
        customCursor=toolkit.createCustomCursor(emptyImage.getAwtImage(),defaultPoint,
            "Target");
        panel=WorldHandler.getInstance().getWorldCanvas(); 
    }
   
    private void prepare()
    {
    	List<Actor> actors=getObjects(Actor.class);
    	
    	for(Actor a: actors)
    	{
    		removeObject(a);
    	}
    	
    	switch(level)
    	{
    		case 1:
    			prepareLevel1();
    			break;
    		default:
    			Greenfoot.stop();
    			break;
    	}
    	
    	LivesMeter livesMeter=new LivesMeter();
        addObject(livesMeter,100,25);
        livesMeter.act();
        
        EnemyCount enemyCount=new EnemyCount();
        addObject(enemyCount,500,23);
        enemyCount.act();
    }
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepareLevel1()
    {
    	addExternalWalls();
    	
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
    	
    	addObject(tankTarget,200,200);
        
        addObject(playerTank,900,200);
        
        BrownTank enemyTank=new BrownTank(300,500);
        addObject(enemyTank, 300, 500);
        enemyTanks=1;
    }
    
    private void showStartScreen()
    {
    	StartScreen levelStart=new StartScreen(this);
    	addObject(levelStart,500,400);
    	Greenfoot.delay(300);
    	removeObject(levelStart);
    	Greenfoot.delay(200);
    	showStartScreen=false;
    }
    
    private void missionCleared()
    {
    	Actor missionCleared=new WallBlock();
    	missionCleared.setImage(new GreenfootImage(MISSION_CLEARED));
    	addObject(missionCleared,500,400);
    	
    	Greenfoot.delay(300);
    	level++;
    	prepare();
    	
    	removeObject(missionCleared);
    	showStartScreen=true;
    }
    
    private void reloadLevel()
    {
    	Actor missionFail=new WallBlock();
    	missionFail.setImage(new GreenfootImage(MISSION_FAILED));
    	addObject(missionFail,500,400);
    	Greenfoot.delay(300);
    	removeObject(missionFail);
    	
    	List<Tank> tanks=getObjects(Tank.class);
    	List<Shell> shells=getObjects(Shell.class);
    	List<LandMine> mines=getObjects(LandMine.class);
    	
    	for(LandMine lm: mines)
    	{
    		removeObject(lm);
    	}
    	
    	for(Shell s: shells)
    	{
    		removeObject(s);
    	}
    	
    	for(Tank t: tanks)
    	{
    		t.reloadTank();
    	}
    	
    	addObject(playerTank,900,200);
    	tankTarget.setLocation(200, 200);
    	showStartScreen=true;
    }
    
    private void gameOver()
    {
    	Actor gameOver=new WallBlock();
    	gameOver.setImage(new GreenfootImage(GAME_OVER));
    	addObject(gameOver,500,400);
    	
    	Greenfoot.delay(300);
    	Greenfoot.stop();
    }
    
    private void addExternalWalls()
    {
    	for(int i=30;i<860;i+=60)
    	{
    		WallBlock wall=new WallBlock();
    		addObject(wall,30,i);
    		wall=new WallBlock();
    		addObject(wall,970,i);
    	}
    
    	for(int i=90;i+30<1000;i+=60)
    	{
    		WallBlock wall=new WallBlock();
    		addObject(wall,i,30);
    		wall=new WallBlock();
    		addObject(wall,i,770);
    	}
    }
    
    public Target getTankTarget()
    {
        return tankTarget;
    }
    
    public int getPlayerLives()
    {
    	return playerLives;
    }
    
    public int getLevel()
    {
    	return level;
    }
    
    public int getNrEnemyTanks()
    {
    	return enemyTanks;
    }
    
    public void removeObject(Shell shell)
    {
    	shell.getTank().getTurret().decLiveShells();
    	super.removeObject(shell);
    }
    
    public void removeObject(Tank tank)
    {
    	tank.deleteTank();
    	
    	if(tank.getClass()==PlayerTank.class)
    	{
    		playerLives--;
    		if(playerLives>0)
    		{
    			reloadLevel();
    		}
    		else
    		{
    			gameOver();
    		}
    	}
    	else
    	{
    		enemyTanks--;
    		
    		if(enemyTanks==0)
    		{
    			missionCleared();
    		}
    	}
    }
}
