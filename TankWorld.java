import greenfoot.*;  
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.*;
import greenfoot.core.WorldHandler;

public class TankWorld extends World
{
    private final Target tankTarget;
    private final PlayerTank playerTank;
    private Cursor customCursor;
    private JPanel panel;
    
    private static final int PLAYER_SHELLS_ALLOWED=6;
    private int numPlayerShells;
    
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public TankWorld()
    {    
        super(1000, 800, 1,true);
        tankTarget=new Target();
        numPlayerShells=0;
        playerTank=new PlayerTank();
        
        prepare(); 
        
        hideCursor();
    }

    public void act()
    {
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
   
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
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
        
        BrownTank enemyTank=new BrownTank();
        addObject(enemyTank, 300, 500);
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
    
    public static int getPlayerShellsAllowed()
    {
    	return PLAYER_SHELLS_ALLOWED;
    }
    
    public int numOfPlayerShells()
    {
    	return numPlayerShells;
    }
    
    public void addObject(Shell shell, int x, int y)
    {
    	super.addObject(shell, x, y);
    	numPlayerShells++;
    }
    
    public void removeObject(Shell shell)
    {
    	super.removeObject(shell);
    	numPlayerShells--;
    }
    
    public void removeObject(Tank tank)
    {
    	tank.deleteTank();
    }
}
