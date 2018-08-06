import greenfoot.*;  

public class Letter extends Actor
{
	private GraphPoint point;
	
	public Letter(int x, int y,GraphPoint point, TankWorld world)
	{
		if(point!=null)
		{
			setImage("O.png");
			point.letter=this;
		}
		else
		{
			setImage("X.png");
		}
		
		this.point=point;
		world.addObject(this, x, y);
		
		/*if(point!=null)
		{
			GraphPoint[] neighbours=point.getNeighbours();
			GreenfootImage background=world.getBackground();
			GreenfootImage dot=new GreenfootImage("dot.png");
			
			if(neighbours[0]!=null)
			{
				background.drawImage(dot, x-7, y-7);
				world.setBackground(background);
			}
			
			if(neighbours[1]!=null)
			{
				background.drawImage(dot, x, y-7);
				world.setBackground(background);
			}
			
			if(neighbours[2]!=null)
			{
				background.drawImage(dot, x+7, y-7);
				world.setBackground(background);
			}
			
			if(neighbours[3]!=null)
			{
				background.drawImage(dot, x-7, y);
				world.setBackground(background);
			}
			
			if(neighbours[4]!=null)
			{
				background.drawImage(dot, x+7, y);
				world.setBackground(background);
			}
			
			if(neighbours[5]!=null)
			{
				background.drawImage(dot, x-7, y+7);
				world.setBackground(background);
			}
			
			if(neighbours[6]!=null)
			{
				background.drawImage(dot, x, y+7);
				world.setBackground(background);
			}
			
			if(neighbours[7]!=null)
			{
				background.drawImage(dot, x+7, y+7);
				world.setBackground(background);
			}
		}*/
	}
	
	public void selected()
	{
		setImage("red_circle.png");
	}
	
	public void visited()
	{
		setImage("blue-circle.png");
	}
	
	public void reset()
	{
		setImage("O.png");
	}
   
   public GraphPoint getPoint()
   {
	   return point;
   }
}