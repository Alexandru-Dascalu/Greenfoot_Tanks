import greenfoot.*;  
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.*;
import greenfoot.core.WorldHandler;

public class TankWorld extends World
{
    private Target tankTarget;
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
        super(1100, 900, 1);
        tankTarget=new Target();
        numPlayerShells=0;
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
    	
    	addObject(tankTarget,200,210);
        Tank tank = new Tank();
        addObject(tank,900,200);
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
}
