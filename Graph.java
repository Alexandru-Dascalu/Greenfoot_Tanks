import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Write a description of class Graph here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class Graph  
{
    private GraphPoint[][] pointMatrix;

    private int matrixLength;
    private int matrixWidth;
    
    /**
     * Constructor for objects of class Graph
     */
    public Graph(TankWorld tankWorld)
    {
    	matrixLength=(TankWorld.LENGTH-2*WallBlock.SIDE)/GraphPoint.INTERVAL+1;
    	
    	matrixWidth=(TankWorld.WIDTH-2*WallBlock.SIDE)/GraphPoint.INTERVAL+1;
    	
    	pointMatrix=new GraphPoint[matrixWidth][matrixLength];
    
    	int x, y;
   
    	for(int i=0;i<matrixWidth;i++)
    	{
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
    				pointMatrix[i][j]=null;
    			}
    			
    		}
    	}
    }
    
    public GraphPoint getPoint(int rowIndex, int columnIndex)
    {
    	return pointMatrix[rowIndex][columnIndex];
    }
    
    public LinkedList<GraphPoint> getShortestPath(int startX, int startY,
    		GraphPoint target)
    {
    	GraphPoint source=getSourcePoint(startX, startY);
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
    		current.setVisited(true);
    		if(current.equals(target))
    		{
    			break;
    		}
    		
    		if(current.getDistance()==Integer.MAX_VALUE)
    		{
    			return null;
    		}
    		
    		for(GraphPoint neighbour: current.getNeighbours())
    		{
    			if(neighbour!=null && !neighbour.isVisited())
    			{
    				double tentativeDistance = current.getDistance()+
    						GraphPoint.getDistance(current, neighbour);
    				
    				if(tentativeDistance<neighbour.getDistance())
    				{
    					unvisitedNodes.remove(neighbour);
    					neighbour.setDistance(tentativeDistance);
    					neighbour.setBestPrevious(current);
    					
    					unvisitedNodes.add(neighbour);
    				}
    				
    			}
    		}
    	}
    	
    	LinkedList<GraphPoint> path=getPath(target);
    	resetGraph();
    	return path;
    }
    
    private GraphPoint getSourcePoint(int startX, int startY)
    {
    	startX-=WallBlock.SIDE;
    	startY-=WallBlock.SIDE;
    	
    	int rowIndex=startY/GraphPoint.INTERVAL;
    	int columnIndex=startX/GraphPoint.INTERVAL;
    	
    	GraphPoint approximateSource=pointMatrix[rowIndex][columnIndex];
    	approximateSource.setDistance(0);
    	
    	return approximateSource;
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
    	
    	return path;
    }
    
    private void resetGraph()
    {
    	for(GraphPoint[] pointRow: pointMatrix)
    	{
    		for(GraphPoint point: pointRow)
    		{
    			if(point!=null)
    			{
    				point.reset();
    			}
    		}
    	}
    }
}
