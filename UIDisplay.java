import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class LivesMeter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class UIDisplay extends Actor
{
	protected int data;
	
	public UIDisplay()
	{
		data=0;
	}
	
    /**
     * Act - do whatever the LivesMeter wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(data!=getNewData())
        {
        	update();
        }
    }    
    
    private void update()
    {
    	data=getNewData();
    	Color background=new Color(0,0,0,0);
    	GreenfootImage image=new GreenfootImage(getText()+data, 30,Color.WHITE,background);
    	
    	GreenfootImage trapezoid=new GreenfootImage(getShapeImage());
    	trapezoid.drawImage(image, getTextXPos(), getTextYPos());
    	setImage(trapezoid);
    }
    
    public int getNewData()
    {
    	return 0;
    }
    
    public String getShapeImage()
    {
    	return "a";
    }
    
    public String getText()
    {
    	return "b";
    }
    
    public int getTextXPos()
    {
    	return 0;
    }
    
    public int getTextYPos()
    {
    	return 0;
    }
}