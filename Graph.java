import java.util.LinkedList;
import java.util.PriorityQueue;

import greenfoot.Greenfoot;

/**
 * Write a description of class Graph here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class Graph  
{
    private GraphPoint[][] pointMatrix;

    private TankWorld world;
    private int matrixLength;
    private int matrixWidth;
    
    /**
     * Constructor for objects of class Graph
     */
    public Graph(TankWorld tankWorld)
    {
    	world=tankWorld;
    	matrixLength=(TankWorld.LENGTH-2*WallBlock.SIDE)/GraphPoint.INTERVAL+1;
    	
    	matrixWidth=(TankWorld.WIDTH-2*WallBlock.SIDE)/GraphPoint.INTERVAL+1;
    	
    	pointMatrix=new GraphPoint[matrixWidth][matrixLength];
    	
    	//System.out.println("width"+matrixWidth);
    	//System.out.println("length"+matrixLength);
    	int x, y;
    	
    	//System.out.print("   ");
    	
    	/*for(int j=0;j<matrixLength;j++)
    	{
    		System.out.println(j+"     ");
    	}*/
    	for(int i=0;i<matrixWidth;i++)
    	{
    		//System.out.print(i+": ");
    		y=WallBlock.SIDE+(GraphPoint.INTERVAL/2)+i*GraphPoint.INTERVAL;
    		
    		for(int j=0;j<matrixLength;j++)
    		{
    			x=WallBlock.SIDE+(GraphPoint.INTERVAL/2)+j*GraphPoint.INTERVAL;
    			
    			if(!GraphPoint.isIntersectingWall(x, y, tankWorld))
    			{
    				pointMatrix[i][j]=new GraphPoint(x,y);
    				
    				if(i!=0)
    				{
    					pointMatrix[i][j].addNeighbour(pointMatrix[i-1][j], "upper");
    				}
    				
    				if(j!=0)
    				{
    					pointMatrix[i][j].addNeighbour(pointMatrix[i][j-1], "left");
    				}
    				
    				if(j!=0 && i!=0)
    				{
    					pointMatrix[i][j].addNeighbour(pointMatrix[i-1][j-1], "upper left");
    				}
    				
    				if(i!=0 && j!=(matrixLength-1))
    				{
    					pointMatrix[i][j].addNeighbour(pointMatrix[i-1][j+1], "upper right");
    				}
    			}
    			else
    			{
    				//System.out.println("i :"+i);
    				//System.out.println("j :"+j);
    				pointMatrix[i][j]=null;
    			}
    			
    			//System.out.printf("%.2s "+x+" "+y+" ",pointMatrix[i][j]);
    		}
    		//System.out.println();
    	}
    	
    	for(int i=0;i<matrixWidth;i++)
    	{
    		y=WallBlock.SIDE+(GraphPoint.INTERVAL/2)+i*GraphPoint.INTERVAL;
    		
    		for(int j=0;j<matrixLength;j++)
    		{
    			x=WallBlock.SIDE+(GraphPoint.INTERVAL/2)+j*GraphPoint.INTERVAL;
    			
    			Letter letter=new Letter(x,y,pointMatrix[i][j],tankWorld);
    		}
    	}
    }
    
    public LinkedList<GraphPoint> getShortestPath(int startX, int startY, int finalX, int finalY)
    {
    	GraphPoint source=getSourcePoint(startX, startY);
    	GraphPoint target=getTargetPoint(finalX, finalY);
    	
    	PriorityQueue<GraphPoint> unvisitedNodes=new PriorityQueue<>();
    	
    	unvisitedNodes.add(source);
    	for(GraphPoint[] pointRow: pointMatrix)
    	{
    		for(GraphPoint point: pointRow)
    		{
    			if(point!=null)
    			{
    				unvisitedNodes.add(point);
    			}
    		}
    	}
    	
    	GraphPoint current;
    	while(!unvisitedNodes.isEmpty())
    	{
    		current=unvisitedNodes.poll();
    		current.letter.visited();
    		current.setVisited(true);
    		Greenfoot.delay(5);
    		/*if(current.getDistance()==Integer.MAX_VALUE);
    		{
    			Letter letter=new Letter(current.getX(),current.getY(),current,world);
    		}*/
    		if(current.equals(target))
    		{
    			break;
    		}
    		
    		/*if(current.getDistance()==Integer.MAX_VALUE)
    		{
    			return null;
    		}*/
    		
    		for(GraphPoint neighbour: current.getNeighbours())
    		{
    			if(neighbour!=null && !neighbour.isVisited())
    			{
    				neighbour.letter.selected();
    				Greenfoot.delay(5);
    				int tentativeDistance = current.getDistance()+
    						GraphPoint.getDistance(current, neighbour);
    				
    				if(tentativeDistance<neighbour.getDistance())
    				{
    					unvisitedNodes.remove(neighbour);
    					//int original =neighbour.getDistance();
    					neighbour.setDistance(tentativeDistance);
    					neighbour.setBestPrevious(current);
    					
    					unvisitedNodes.add(neighbour);
    					
    					/*if(tentativeDistance<Integer.MIN_VALUE+50 && original>0)
        				{
    						System.out.println("original"+original);
        					System.out.println("after update: "+neighbour.getDistance());
        				}*/
    				}
    				
    				neighbour.letter.reset();
    				Greenfoot.delay(5);
    			}
    		}
    	}
    	
    	LinkedList<GraphPoint> path=getPath(target);
    	
    	return path;
    }
    
    private GraphPoint getTargetPoint(int finalX, int finalY)
    {
    	int rowIndex=finalY/GraphPoint.INTERVAL;
    	int columnIndex=finalX/GraphPoint.INTERVAL;
    	
    	return pointMatrix[rowIndex][columnIndex];
    }
    
    private GraphPoint getSourcePoint(int startX, int startY)
    {
    	GraphPoint source=new GraphPoint(startX, startY);
    	source.setDistance(0);
    	Letter letter=new Letter(startX, startY,source,world);
    	
    	startX-=WallBlock.SIDE;
    	startY-=WallBlock.SIDE;
    	
    	int rowIndex=startY/GraphPoint.INTERVAL;
    	int columnIndex=startX/GraphPoint.INTERVAL;
    	
    	GraphPoint approximateSource=pointMatrix[rowIndex][columnIndex];
    	
    	source.addOneWayNeighbour(approximateSource.getNeighbour("upper left")
    			, "upper left");
    	source.addOneWayNeighbour(approximateSource.getNeighbour("upper")
    			, "upper");
    	source.addOneWayNeighbour(approximateSource.getNeighbour("upper right")
    			, "upper right");
    	source.addOneWayNeighbour(approximateSource.getNeighbour("left"), "left");
    	source.addOneWayNeighbour(approximateSource.getNeighbour("right"), "right");
    	source.addOneWayNeighbour(approximateSource.getNeighbour("lower left")
    			, "lower left");
    	source.addOneWayNeighbour(approximateSource.getNeighbour("lower"), "lower");
    	source.addOneWayNeighbour(approximateSource.getNeighbour("lower right")
    			, "lower right");
    	return source;
    }
    
    private LinkedList<GraphPoint> getPath(GraphPoint target)
    {
    	LinkedList<GraphPoint> path=new LinkedList<>();
    	GraphPoint current=target;
    	
    	while(current.getBestPrevious()!=null)
    	{
    		path.addFirst(current);
    		current=current.getBestPrevious();
    	}
    	path.addFirst(current);
    	
    	/*for(GraphPoint g: path)
    	{
    		Letter l=new Letter(g.getX(),g.getY(),g,world);
    	}*/
    	
    	return path;
    }
}