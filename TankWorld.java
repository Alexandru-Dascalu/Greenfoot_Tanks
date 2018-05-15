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
    
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public TankWorld()
    {    
        super(1100, 900, 1);
        tankTarget=new Target();
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
    	addObject(tankTarget,200,210);
        Tank tank = new Tank();
        addObject(tank,900,200);
    }
    
    public Target getTankTarget()
    {
        return tankTarget;
    }
}
