import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.*;
import greenfoot.core.WorldHandler;

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{
    private Target tankTarget;
    private Cursor customCursor;
    private JPanel panel;
    
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1422, 800, 1); 
        prepare(); 
        Toolkit toolkit=Toolkit.getDefaultToolkit();
        Point defaultPoint=new Point(0,0);
        GreenfootImage emptyImage= new GreenfootImage(5,5);
        customCursor=toolkit.createCustomCursor(emptyImage.getAwtImage(),defaultPoint,
            "Target");
        panel=WorldHandler.getInstance().getWorldCanvas(); 
    }

    public void act()
    {
        panel.setCursor(customCursor);
    }
   
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {

        Tank tank = new Tank();
        addObject(tank,1290,231);
        tankTarget=new Target();
        addObject(tankTarget,200,200);
    }
    
    public Target getTankTarget()
    {
        return tankTarget;
    }
    
}
