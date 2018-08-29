import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * <p><b>File name: </b> Graph.java
 * @version 1.0
 * @since 07.06.2018
 * <p><b>Last modification date: </b> 28.07.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class describes a graph used for finding the shortest path for a Greenfoot
 * recreation of the Wii Tanks game for the Nintendo Wii. It builds a network of
 * nodes base on an instance of a TankWorld so that nodes are where a tank can move
 * safely without hitting walls.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 */
public class Graph  
{
	/**The matrix of graph points which contains this graph. Since nodes are points
	 * on the game world, and each point has 8 neighbours which are the closest point
	 * to that point in the world, the graph is stored as a 2 dimensional array.*/
    private GraphPoint[][] pointMatrix;

    /**
     * Makes a new Graph that matches the layout of the given instance of TankWorld.
     * This means it will make a graph where if a point in the world is too close to
     * a wall, it will be a null value in the matrix so that the tank will not 
     * pass through walls. It also addes the edges between neighbouring nodes while
     * making new nodes.
     * @param tankWorld The world this graph is built for.
     */
    public Graph(TankWorld tankWorld)
    {
    	/*Since no nodes are created where a tank can not pass, no nodes need to 
    	 * be created on the edges of the world where the exterior walls are, so
    	 * the matrix is only big enough to hold points that would fill the are 
    	 * inside the world's exterior walls. Which is why it subtracts the length
    	 * of 2 wall blocks from the length and width of the world.*/
    	
    	/*The length of the matrix is the length of world without the length of the 
    	 * exterior walls divided by the interval at which graph points are made.*/
    	int matrixLength=(TankWorld.LENGTH-2*WallBlock.SIDE)/GraphPoint.INTERVAL+1;
    	
    	/*The width of the matrix is the length of world without the length of the 
    	 * exterior walls divided by the interval at which graph points are made.*/
    	int matrixWidth=(TankWorld.WIDTH-2*WallBlock.SIDE)/GraphPoint.INTERVAL+1;
    	
    	//make a new matrix with the calculated sizes
    	pointMatrix=new GraphPoint[matrixWidth][matrixLength];
    
    	//variables for the coordinates where each point will be made
    	int x, y;
   
    	/*For loop for creating each row of the matrix, loops for as many times
    	 * as the width of the matrix.*/
    	for(int i=0;i<matrixWidth;i++)
    	{
    		/*The y coordinate of the points in each row is the length of the wall
    		 * block on the left edge of the world, with half the length between
    		 * points(from the wall to the centre of the point) and the index of
    		 * this matrix row multiplied by the distance between points.*/
    		y=WallBlock.SIDE+(GraphPoint.INTERVAL/2)+i*GraphPoint.INTERVAL;
    		
    		/*For loop for creating each element of a row of the matrix, loops 
    		 * for as many times as the length of the matrix.*/
    		for(int j=0;j<matrixLength;j++)
    		{
    			/*The x coordinate of the points in each row is the length of the wall
        		 * block on the left edge of the world, with half the length between
        		 * points(from the wall to the centre of the point) and the index of
        		 * this matrix column multiplied by the distance between points.*/
    			x=WallBlock.SIDE+(GraphPoint.INTERVAL/2)+j*GraphPoint.INTERVAL;
    			
    			/*We need to check if a tank can pass through the centre of this
    			 * point and not hit a wall, to know if we need to add a point that
    			 * belong to a path a tank can follow.*/
    			if(!GraphPoint.isIntersectingWall(x, y, tankWorld))
    			{
    				/*If a tank that would through this point would not intersect 
    				 * with a wall, we make a new point here and add it's edges with
    				 * it's neighbours.*/
    				pointMatrix[i][j]=new GraphPoint(x,y);
    				
    				/*After putting a new node in the graph, we need to make the
    				 * edges to it's neighbours. Since nodes are built from top 
    				 * to bottom, left to right, when a new point is made it can 
    				 * only above, in it's upper left, upper right and to it's left.
    				 * Neighbours that are below it or to the right are added to the
    				 * new point's array of neighbours when those new neighbours are made.*/
    				
    				//Check if this point has a neighbour above it.
    				if(i!=0)
    				{
    					//If it has, add it to it's array of neighbours in the correct place
    					pointMatrix[i][j].addNeighbour(pointMatrix[i-1][j], "upper");
    				}
    				
    				//Check if this point has a neighbour to it's lefts.
    				if(j!=0)
    				{
    					//If it has, add it to it's array of neighbours in the correct place
    					pointMatrix[i][j].addNeighbour(pointMatrix[i][j-1], "left");
    				}
    				
    				//Check if this point has a neighbour to it's upper left.
    				if(j!=0 && i!=0)
    				{
    					//If it has, add it to it's array of neighbours in the correct place
    					pointMatrix[i][j].addNeighbour(pointMatrix[i-1][j-1], "upper left");
    				}
    				
    				//Check if this point has a neighbour to it's upper right.
    				if(i!=0 && j!=(matrixLength-1))
    				{
    					//If it has, add it to it's array of neighbours in the correct place
    					pointMatrix[i][j].addNeighbour(pointMatrix[i-1][j+1], "upper right");
    				}
    			}
    			/*If a tank can not pass through this point on the world because
    			 * of a wall, set this place in the matrix to null so the shortest 
    			 * path algorithm will avoid it.*/
    			else
    			{
    				pointMatrix[i][j]=null;
    			}
    		}
    	}
    }
    
    /**
     * Getter for individual nodes of this graph. Returns the node at the given
     * index in the matrix.
     * @param rowIndex The row index of the node to be returned.
     * @param columnIndex The column index of the node to be returned.
     * @return The point in the matrix at the given indexes.
     */
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
