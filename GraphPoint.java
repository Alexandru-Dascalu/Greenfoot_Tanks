import greenfoot.*;
import java.lang.IllegalArgumentException;

/**
 * Write a description of class GraphPoint here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GraphPoint implements Comparable<GraphPoint>
{
	public static final int INTERVAL=30;
	private static final int WALL_DISTANCE=52;
	
    private final int x;
    private final int y;

    private GraphPoint bestPrevious;
    private double distance;
    private GraphPoint[] neighbours;
    private boolean visited;
    
    /**
     * Constructor for objects of class GraphPoint
     */
    public GraphPoint(int x, int y)
    {
    	this.x=x;
    	this.y=y;
    	
    	distance=Integer.MAX_VALUE;
    	bestPrevious=null;
    	visited=false;
    	neighbours=new GraphPoint[8];
    }

    public int getX()
    {
    	return x;
    }
    
    public int getY()
    {
    	return y;
    }
    
    public double getDistance()
    {
    	return distance;
    }
    
    public GraphPoint getBestPrevious()
    {
    	return bestPrevious;
    }
    
    public GraphPoint[] getNeighbours()
    {
    	return neighbours;
    }
    
    public boolean isVisited()
    {
    	return visited;
    }
    
    public GraphPoint getNeighbour(String position) throws IllegalArgumentException
    {
    	if(position.equals("upper left"))
    	{
    		return neighbours[0];
    	}
    	else if(position.equals("upper"))
    	{
    		return neighbours[1];
    	}
    	else if(position.equals("upper right"))
    	{
    		return neighbours[2];
    	}
    	else if(position.equals("left"))
    	{
    		return neighbours[3];
    	}
    	else if(position.equals("right"))
    	{
    		return neighbours[4];
    	}
    	else if(position.equals("lower left"))
    	{
    		return neighbours[5];
    	}
    	else if(position.equals("lower"))
    	{
    		return neighbours[6];
    	}
    	else if(position.equals("lower right"))
    	{
    		return neighbours[7];
    	}
    	else
    	{
    		throw new IllegalArgumentException("The location string argument"
					+ " has a value that is not one of: upper left, upper, "
					+ "upper right, left, right, lower left, lower or lower right.");
    	}
    }
    
    public void setDistance(double distance)
    {
    	this.distance=distance;
    }
    
    public void setVisited(boolean visited)
    {
    	this.visited=visited;
    }
    
    public void setBestPrevious(GraphPoint bestPrevious)
    {
    	this.bestPrevious=bestPrevious;
    }
    
    public void addNeighbour(GraphPoint neighbour, String location) 
    		throws IllegalArgumentException
    {
    	switch(location)
    	{
    		case "upper left":
    			neighbours[0]=neighbour;
    			
    			if(neighbour!=null && (neighbour.getNeighbour("lower right")!=this))
    			{
    				neighbour.addNeighbour(this, "lower right");
    			}
    			break;
    		case "upper":
    			neighbours[1]=neighbour;
    			
    			if(neighbour!=null && (neighbour.getNeighbour("lower")!=this))
    			{
    				neighbour.addNeighbour(this, "lower");
    			}
    			break;
    		case "upper right":
    			neighbours[2]=neighbour;
    			
    			if(neighbour!=null && (neighbour.getNeighbour("lower left")!=this))
    			{
    				neighbour.addNeighbour(this, "lower left");
    			}
    			break;
    		case "left":
    			neighbours[3]=neighbour;
    			
    			if(neighbour!=null && (neighbour.getNeighbour("right")!=this))
    			{
    				neighbour.addNeighbour(this, "right");
    			}
    			break;
    		case "right":
    			neighbours[4]=neighbour;
    			
    			if(neighbour!=null && (neighbour.getNeighbour("left")!=this))
    			{
    				neighbour.addNeighbour(this, "left");
    			}
    			break;
    		case "lower left":
    			neighbours[5]=neighbour;
    			
    			if(neighbour!=null && (neighbour.getNeighbour("upper right")!=this))
    			{
    				neighbour.addNeighbour(this, "upper right");
    			}
    			break;
    		case "lower":
    			neighbours[6]=neighbour;
    			
    			if(neighbour!=null && (neighbour.getNeighbour("upper")!=this))
    			{
    				neighbour.addNeighbour(this, "upper");
    			}
    			break;
    		case "lower right":
    			neighbours[7]=neighbour;
    			
    			if(neighbour!=null && (neighbour.getNeighbour("upper left")!=this))
    			{
    				neighbour.addNeighbour(this, "upper left");
    			}
    			break;
    		default:
    			throw new IllegalArgumentException("The location string argument"
    					+ " has a value that is not one of: upper left, upper, "
    					+ "upper right, left, right, lower left, lower or lower right.");
    	}
    }
    
    public void addOneWayNeighbour(GraphPoint neighbour, String location) 
    		throws IllegalArgumentException
    {
    	switch(location)
    	{
    		case "upper left":
    			neighbours[0]=neighbour;
    			
    			break;
    		case "upper":
    			neighbours[1]=neighbour;
    			
    			break;
    		case "upper right":
    			neighbours[2]=neighbour;
    			
    			break;
    		case "left":
    			neighbours[3]=neighbour;
    			
    			break;
    		case "right":
    			neighbours[4]=neighbour;
    			
    			break;
    		case "lower left":
    			neighbours[5]=neighbour;
    			
    			break;
    		case "lower":
    			neighbours[6]=neighbour;
    			
    			break;
    		case "lower right":
    			neighbours[7]=neighbour;
    			
    			break;
    		default:
    			throw new IllegalArgumentException("The location string argument"
    					+ " has a value that is not one of: upper left, upper, "
    					+ "upper right, left, right, lower left, lower or lower right.");
    	}
    }
    
    public void reset()
    {
    	bestPrevious=null;
    	distance=Integer.MAX_VALUE;
    	visited=false;
    }
    
    @Override
    public int compareTo(GraphPoint otherPoint)
    {
    	double difference=this.getDistance()-otherPoint.getDistance();
    	
    	if(difference>0)
    	{
    		return 1;
    	}
    	else if(difference<0)
    	{
    		return -1;
    	}
    	else
    	{
    		return 0;
    	}
    }
    
    public static boolean isIntersectingWall(int x, int y, World world)
    {
    	int xOffset=(int) Math.ceil(WALL_DISTANCE/2.0);
    	int yOffset=(int) Math.ceil(WALL_DISTANCE/2.0);
    	
    	if(!world.getObjectsAt(x+xOffset, y+yOffset, WallBlock.class).isEmpty())
    	{
    		return true;
    	}
    	else if(!world.getObjectsAt(x+xOffset, y-yOffset, WallBlock.class).isEmpty())
    	{
    		return true;
    	}
    	else if(!world.getObjectsAt(x-xOffset, y-yOffset, WallBlock.class).isEmpty())
    	{
    		return true;
    	}
    	else if(!world.getObjectsAt(x-xOffset, y+yOffset, WallBlock.class).isEmpty())
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public static double getDistance(GraphPoint a, GraphPoint b)
    {
    	int xDistance=a.getX()-b.getX();
    	int yDistance=a.getY()-b.getY();
    	
    	double distance=Math.sqrt((xDistance*xDistance)+(yDistance*yDistance));
    	return distance;
    }
}