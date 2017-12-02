
package algii.structure;

import java.util.ArrayList;
import java.util.Collections;

public class DCEL {
	
	/** List of faces that form the polygon (always at least two: internal (F0) and external (F-1)). **/
    private ArrayList<Face> faces;
    /** List of Half-edges that connect the vertices of the polygon 
     * (always an even number since for each connection we have a main vertex and a twin vertex). **/
    private ArrayList<HalfEdge> edges;
    /** List of vertices that form the polygon. **/
    private ArrayList<Vertex> vertices;
    
    /**
     * Default constructor that initializes the face, edge and vertex lists of the DCEL.
     */
    public DCEL()
    {
        faces = new ArrayList<Face>();
        edges = new ArrayList<HalfEdge>();
        vertices = new ArrayList<Vertex>();
    }

    /**
     * @return current list of faces that form the polygon (always at least two: internal (F0) and external (F-1))
     */
    public ArrayList<Face> getFaces() 
    {
        return faces;
    }

    /**
     * Creates a new face (whose id will be the faces[last].id + 1) and adds it to the list of faces.
     */
    public void addFace()
    {
    	faces.add(new Face(faces.size()));
    }
    
    /**
     * @return current list of Half-edges that connect the vertices of the polygon
     */
    public ArrayList<HalfEdge> getEdges() 
    {
        return edges;
    }

    /**
     * @return current list of vertices that form the polygon
     */
    public ArrayList<Vertex> getVertices() 
    {
        return vertices;
    }

    /**
     * @param vertices list of vertices that will form the polygon (given in CCW order)
     */
    public void setVertices(ArrayList<Vertex> vertices)
    {
        this.vertices = vertices;
    }
    
    /**
     * <p> Initializes a DCEL structure.
     * 
     * <p> Called from a DCEL object with at least 3 vertices will create a proper DCEL structure connecting all
     * vertices through pair of half-edges (using CCW for the internal edges and CW for the external ones) and
     * will create two faces (internal (F0) and external (F-1)). 
     * 
     * <p> All references in every HalfEdge, Face and Vertex object will be updated accordingly to the information
     * supplied by the current list of vertices.
     */
    public void init() 
    {
    	// Cannot create polygon with less than 3 vertices
    	if(vertices.size() < 3)
    		return;
    	
        faces = new ArrayList<Face>(); // Clean faces
        edges = new ArrayList<HalfEdge>(); // Clean edges
    	
        faces.add(new Face(-1)); // exterior (F-1)
        faces.add(new Face(0)); // base interior face (F0)
        
        // Step 1) Initialize edges
        for(int i = 0; i < vertices.size(); i++)
        {
        	addEdge(vertices.get(i), vertices.get((i+1)%vertices.size()));
            edges.get(edges.size()-2).setFace(faces.get(1));
            edges.get(edges.size()-1).setFace(faces.get(0));
        	//vertices.get((i+1)%vertices.size()).setIncident(edges.get(edges.size()-2));
        }
        
        // Step 2) Update next and prev pointers
        edges.get(0).setPrev(edges.get(edges.size()-2));
        edges.get(0).setNext(edges.get(2)); 
        edges.get(1).setNext(edges.get(edges.size()-1));
        edges.get(1).setPrev(edges.get(3)); 
        
        for(int i = 2; i < edges.size(); i++)
        {
            if(i%2 == 0) // "Main" vertices (whose direction is CCW)
            {
                edges.get(i).setPrev(edges.get((i-2)%edges.size()));
                edges.get(i).setNext(edges.get((i+2)%edges.size()));
            }
            else// "Twin" vertices (whose direction is CW)
            {
                edges.get(i).setPrev(edges.get((i+2)%edges.size()));
                edges.get(i).setNext(edges.get((i-2)%edges.size()));
            }
        }  
        
        // Step 3) Calculate angles between groups of 3 vertices
        for(int i = 0; i < vertices.size(); i++)
        {     
            if(crossP(vertices.get((i==0)?vertices.size()-1:(i-1)),
                         vertices.get(i),
                         vertices.get((i+1)%vertices.size())) < 0)
                vertices.get(i).setIsReflex(true); // Left-turn
            else
                vertices.get(i).setIsReflex(false); // Right-turn
            
        }
        
        // Step 4) Update face incidents
        faces.get(0).setIncident(edges.get(1));
        faces.get(1).setIncident(edges.get(0));
    }

    /**
     * <p> Given three points, calculates their cross product.
     * 
     * <p> The result is given by the formula: <i>(b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x).</i>
     * 
     * @param a left-most point
     * @param b middle point
     * @param c right-most point
     * @return crossP result of the cross product formula
     */
    public long crossP(Vertex a, Vertex b, Vertex c)
    {
        return (b.getX() - a.getX())*(c.getY() - a.getY()) - (b.getY() - a.getY())*(c.getX() - a.getX());
    }
    
    /**
     * <p> Given a point (by its X and Y coordinates), creates a vertex and adds it to the end of list of vertices.
     * 
     * <p> Note that it only creates the vertex, any reference should be updated outside this method.
     * 
     * @param x X-coordinate
     * @param y Y-coordinate
     */
    public Vertex addVertex(long x, long y)
    {
    	Vertex point = new Vertex(x, y);
    	point.id = vertices.size();
    	vertices.add(point);
    	
    	return point;
    }
    
    /**
     * <p> Given two points, connects them using two half-edges. 
     * Those new half-edges will be added at the end of the list of half-edges as follows: 
     * edges[size -2] = main, edges [size - 1] = twin.
     * 
     * <p> Note that previous, next and face references are not set here, therefore
     * should be updated accordingly after calling this method. 
     * 
     * @param origin vertex that will be set as origin of the main half-edge
     * @param target vertex that will be set as target of the main half-edge
     */
    public HalfEdge addEdge(Vertex origin, Vertex target)
    {
        HalfEdge main = new HalfEdge();
        HalfEdge twin = new HalfEdge();
        
        main.id = origin.id + " => " + target.id;
        twin.id = target.id + " => " + origin.id;
        
        main.setOrigin(origin);
        main.setTarget(target);
        
        twin.setOrigin(target);
        twin.setTarget(origin);
        
        main.setTwin(twin);
        twin.setTwin(main);
        
        edges.add(main);
        edges.add(twin);
        
        return main;
    }
    
    /**
     * <p> Given an edge divides it in two edges.
     * Note that one of them is the old one with its references updated and the other is a new one that will be added at the end of the edges list.
     * 
     * <p> For example, giving:
     * 
     * <pre>
     * A
     * |
     * |          C       
     * |
     * B
     * </pre>
     * 
     * <p>toDivide corresponds to the edge between A and B, toAdd will be a point between them and divisor a edge between toAdd and C.
     * After applying this procedure we will have:
     * 
     * <pre>
     * A
     * |
     * toAdd------C
     * |
     * B
     * </pre>
     * 
     * @param toDivide index of the edge that needs to be divided
     * @param divisor index of the edge that causes the division
     * @param toAdd point that will connect the divided edges
     * @param isBackwards boolean that indicates the direction of the half-edge
     */
    public void divideEdge(HalfEdge toDivide, HalfEdge divisor, Vertex toAdd, boolean isRight)
    {
    	if(isRight)
    	{
    		addEdge(toAdd, toDivide.getTarget());
    		
    		// new main
    		edges.get(edges.size() - 2).setPrev(divisor);
    		edges.get(edges.size() - 2).setNext(toDivide.getNext());
    		
    		// new twin
    		edges.get(edges.size() - 1).setPrev(toDivide.getTwin().getPrev());
    		edges.get(edges.size() - 1).setNext(toDivide.getTwin());
    		
    		// main old
    		toDivide.setTarget(toAdd);
    		toDivide.id = toDivide.getOrigin().id + " => " + toDivide.getTarget().id;
    		toDivide.setNext(divisor.getTwin());
    		
    		//twin old
    		toDivide.getTwin().setOrigin(toAdd);
    		toDivide.getTwin().id = toDivide.getTwin().getOrigin().id + " => " + toDivide.getTwin().getTarget().id;
    		toDivide.getTwin().setPrev(edges.get(edges.size() - 1));
    		
    		// divisor main
    		divisor.setNext(edges.get(edges.size() - 2));
    		
    		// divisor twin
    		divisor.getTwin().setPrev(toDivide);
    	}
    	else
    	{
    		addEdge(toDivide.getOrigin() , toAdd);
    		
    		// new main
    		edges.get(edges.size() - 2).setPrev(toDivide.getPrev());
    		edges.get(edges.size() - 2).setNext(divisor);   
    		
    		// new twin
    		edges.get(edges.size() - 1).setPrev(toDivide.getTwin());
    		edges.get(edges.size() - 1).setNext(toDivide.getTwin().getNext());
    		
    		// main old
    		toDivide.setOrigin(toAdd);
    		toDivide.id = toDivide.getOrigin().id + " => " + toDivide.getTarget().id;
    		toDivide.setPrev(divisor.getTwin());
    		
    		//twin old
    		toDivide.getTwin().setTarget(toAdd);
    		toDivide.getTwin().id = toDivide.getTwin().getOrigin().id + " => " + toDivide.getTwin().getTarget().id;
    		toDivide.getTwin().setNext(edges.get(edges.size() - 1));
    		
    		// divisor main
    		divisor.setPrev(edges.get(edges.size() - 2));
    		
    		// divisor twin
    		divisor.getTwin().setNext(toDivide);
    	}
    }
    
    /**
     * Prints the vertices and half-edges (by face) that form the object.
     */
    public void print()
    {
    	HalfEdge first, current;
    	
    	// Print all vertices and their coordinates
    	System.out.println("Vertices: ");
    	for(int i = 0; i < vertices.size(); i++)
    		System.out.println(vertices.get(i).id + " : ("  + 
    						   vertices.get(i).getX() + "," +
    						   vertices.get(i).getY() + ")");
    	
    	// Go through each face and print the edges (following "origin.id => target.id" format) that form it
    	System.out.println("Faces: ");
    	for(int i = 1; i < faces.size(); i++)
    	{
    		first = faces.get(i).getIncident();
    		current = faces.get(i).getIncident();
    		
    		System.out.print(faces.get(i).getId() + ": ");
    		
    		do {
    			System.out.print(current.id + " ");
    			current = current.getNext();
    		} while (first != current);
    		
    		System.out.println("");
    	}
    }
    
    /**
     * Given a half-edge and a face, will update the face of all the faces directly connected to the half-edge (using half-edge's next reference).
     * 
     * @param first half-edge where the update will start
     * @param newFace face that will be used to update
     */
    public void updateFaces(HalfEdge first, Face newFace)
    {
    	HalfEdge current = first;
    	do {
			current.setFace(newFace);
			current = current.getNext();
    	} while (first != current);
    }
    
    /**
     * According to the type of sweeping process, returns a list of possible event edges.
     * 
     * @param isHorizontal true if horizontal sweeping process, false otherwise
     * @return events list with all the half-edges that may trigger an event
     */
    public ArrayList<HalfEdge> getEventList(boolean isHorizontal)
    {
    	ArrayList<HalfEdge> events = new ArrayList<HalfEdge>();
    	
    	for(int i = edges.size() - 1; i >= 0; i--)
    		if(i%2 == 0 && edges.get(i).isHorizontal() == isHorizontal)
    			events.add(edges.get(i));

    	Collections.sort(events, HalfEdge.yComp);
    	
    	return events;
    }
    
    public void divideEdge(int toDivide, int divisor, Vertex toAdd, boolean isRight)
    {
    	System.out.println("toDivide: " + edges.get(toDivide).id);
    	System.out.println("divisor: " + edges.get(divisor).id);
    	
    	if(isRight)
    	{
    		addEdge(toAdd, edges.get(toDivide).getTarget());
    		
    		// new main
    		edges.get(edges.size() - 2).setPrev(edges.get(divisor));
    		edges.get(edges.size() - 2).setNext(edges.get(toDivide).getNext());
    		
    		// new twin
    		edges.get(edges.size() - 1).setPrev(edges.get(toDivide).getTwin().getPrev());
    		edges.get(edges.size() - 1).setNext(edges.get(toDivide).getTwin());
    		
    		// main old
    		edges.get(toDivide).setTarget(toAdd);
    		edges.get(toDivide).id = edges.get(toDivide).getOrigin().id + " => " + edges.get(toDivide).getTarget().id;
    		edges.get(toDivide).setNext(edges.get(divisor).getTwin());
    		
    		//twin old
    		edges.get(toDivide).getTwin().setOrigin(toAdd);
    		edges.get(toDivide).getTwin().id = edges.get(toDivide).getTwin().getOrigin().id + " => " + edges.get(toDivide).getTwin().getTarget().id;
    		edges.get(toDivide).getTwin().setPrev(edges.get(edges.size() - 1));
    		
    		// divisor main
    		edges.get(divisor).setNext(edges.get(edges.size() - 2));
    		
    		// divisor twin
    		edges.get(divisor).getTwin().setPrev(edges.get(toDivide));
    	}
    	else
    	{
    		addEdge(edges.get(toDivide).getOrigin() , toAdd);
    		
    		// new main
    		edges.get(edges.size() - 2).setPrev(edges.get(toDivide).getPrev());
    		edges.get(edges.size() - 2).setNext(edges.get(divisor).getTwin());   
    		
    		// new twin
    		edges.get(edges.size() - 1).setPrev(edges.get(toDivide).getTwin());
    		edges.get(edges.size() - 1).setNext(edges.get(toDivide).getTwin().getNext());
    		
    		// main old
    		edges.get(toDivide).setOrigin(toAdd);
    		edges.get(toDivide).id = edges.get(toDivide).getOrigin().id + " => " + edges.get(toDivide).getTarget().id;
    		edges.get(toDivide).setPrev(edges.get(divisor));
    		
    		//twin old
    		edges.get(toDivide).getTwin().setTarget(toAdd);
    		edges.get(toDivide).getTwin().id = edges.get(toDivide).getTwin().getOrigin().id + " => " + edges.get(toDivide).getTwin().getTarget().id;
    		edges.get(toDivide).getTwin().setNext(edges.get(edges.size() - 1));
    		
    		// divisor main
    		edges.get(divisor).setPrev(edges.get(edges.size() - 2));
    		
    		// divisor twin
    		edges.get(divisor).getTwin().setNext(edges.get(toDivide));
    	}
    }
    
}


/*
package algii.structure;

import java.util.ArrayList;

public class DCEL {
    private ArrayList<Face> faces;
    private ArrayList<HalfEdge> edges;
    private ArrayList<Vertex> vertices;
    
    public DCEL()
    {
        faces = new ArrayList<Face>();
        edges = new ArrayList<HalfEdge>();
        vertices = new ArrayList<Vertex>();
    }

    public ArrayList<Face> getFaces() 
    {
        return faces;
    }

    public void setFaces(ArrayList<Face> faces) 
    {
        this.faces = faces;
    }

    public void addFace()
    {
    	faces.add(new Face(faces.size()));
    }
    
    public ArrayList<HalfEdge> getEdges() 
    {
        return edges;
    }

    public void setEdges(ArrayList<HalfEdge> edges) 
    {
        this.edges = edges;
    }

    public ArrayList<Vertex> getVertices() 
    {
        return vertices;
    }

    public void setVertices(ArrayList<Vertex> vertices)
    {
        this.vertices = vertices;
    }
    
    public void init() 
   {
        faces.add(new Face(-1)); // exterior (F-1)
        faces.add(new Face(0)); // base interior face (F0)
        
        // Step 1) Initialize edges
        for(int i = 0; i < vertices.size(); i++)
        {
        	addEdge(vertices.get(i), vertices.get((i+1)%vertices.size()));
            edges.get(edges.size()-2).setFace(faces.get(1));
            edges.get(edges.size()-1).setFace(faces.get(0));
        	vertices.get((i+1)%vertices.size()).setIncident(edges.get(edges.size()-2));
        }
        
        // Step 2) Update next and prev pointers
        edges.get(0).setPrev(edges.get(edges.size()-2));
        edges.get(0).setNext(edges.get(2)); 
        edges.get(1).setNext(edges.get(edges.size()-1));
        edges.get(1).setPrev(edges.get(3)); 
        
        for(int i = 2; i < edges.size(); i++)
        {
            if(i%2 == 0)
            {
                edges.get(i).setPrev(edges.get((i-2)%edges.size()));
                edges.get(i).setNext(edges.get((i+2)%edges.size()));
            }
            else
            {
                edges.get(i).setPrev(edges.get((i+2)%edges.size()));
                edges.get(i).setNext(edges.get((i-2)%edges.size()));
            }
        }  
        
        // Step 3) Calculate angles between groups of 3 vertices
        for(int i = 0; i < vertices.size(); i++)
        {     
            if(crossP(vertices.get((i==0)?vertices.size()-1:(i-1)),
                         vertices.get(i),
                         vertices.get((i+1)%vertices.size())) < 0)
                vertices.get(i).setIsReflex(true);
            else
                vertices.get(i).setIsReflex(false);
            
        }
        
        faces.get(0).setIncident(edges.get(1));
        faces.get(1).setIncident(edges.get(0));
    }
   
    public long crossP(Vertex a, Vertex b, Vertex c)
    {
        return (b.getX() - a.getX())*(c.getY() - a.getY()) - (b.getY() - a.getY())*(c.getX() - a.getX());
    }
    
    public void addVertex(long x, long y)
    {
    	Vertex point = new Vertex(x, y);
    	point.id = vertices.size();
    	vertices.add(point);
    }
    
    public void addEdge(Vertex origin, Vertex target)
    {
        HalfEdge main = new HalfEdge();
        HalfEdge twin = new HalfEdge();
        
        main.id = origin.id + " => " + target.id;
        twin.id = target.id + " => " + origin.id;
        
        main.setOrigin(origin);
        main.setTarget(target);
        
        twin.setOrigin(target);
        twin.setTarget(origin);
        
        main.setTwin(twin);
        twin.setTwin(main);
        
        edges.add(main);
        edges.add(twin);
    }
    
    public void divideEdge(int toDivide, int divisor, Vertex toAdd, boolean isRight)
    {
    	System.out.println("toDivide: " + edges.get(toDivide).id);
    	System.out.println("divisor: " + edges.get(divisor).id);
    	
    	if(isRight)
    	{
    		addEdge(toAdd, edges.get(toDivide).getTarget());
    		
    		// new main
    		edges.get(edges.size() - 2).setPrev(edges.get(divisor));
    		edges.get(edges.size() - 2).setNext(edges.get(toDivide).getNext());
    		
    		// new twin
    		edges.get(edges.size() - 1).setPrev(edges.get(toDivide).getTwin().getPrev());
    		edges.get(edges.size() - 1).setNext(edges.get(toDivide).getTwin());
    		
    		// main old
    		edges.get(toDivide).setTarget(toAdd);
    		edges.get(toDivide).id = edges.get(toDivide).getOrigin().id + " => " + edges.get(toDivide).getTarget().id;
    		edges.get(toDivide).setNext(edges.get(divisor).getTwin());
    		
    		//twin old
    		edges.get(toDivide).getTwin().setOrigin(toAdd);
    		edges.get(toDivide).getTwin().id = edges.get(toDivide).getTwin().getOrigin().id + " => " + edges.get(toDivide).getTwin().getTarget().id;
    		edges.get(toDivide).getTwin().setPrev(edges.get(edges.size() - 1));
    		
    		// divisor main
    		edges.get(divisor).setNext(edges.get(edges.size() - 2));
    		
    		// divisor twin
    		edges.get(divisor).getTwin().setPrev(edges.get(toDivide));
    	}
    	else
    	{
    		addEdge(edges.get(toDivide).getOrigin() , toAdd);
    		
    		// new main
    		edges.get(edges.size() - 2).setPrev(edges.get(toDivide).getPrev());
    		edges.get(edges.size() - 2).setNext(edges.get(divisor).getTwin());   
    		
    		// new twin
    		edges.get(edges.size() - 1).setPrev(edges.get(toDivide).getTwin());
    		edges.get(edges.size() - 1).setNext(edges.get(toDivide).getTwin().getNext());
    		
    		// main old
    		edges.get(toDivide).setOrigin(toAdd);
    		edges.get(toDivide).id = edges.get(toDivide).getOrigin().id + " => " + edges.get(toDivide).getTarget().id;
    		edges.get(toDivide).setPrev(edges.get(divisor));
    		
    		//twin old
    		edges.get(toDivide).getTwin().setTarget(toAdd);
    		edges.get(toDivide).getTwin().id = edges.get(toDivide).getTwin().getOrigin().id + " => " + edges.get(toDivide).getTwin().getTarget().id;
    		edges.get(toDivide).getTwin().setNext(edges.get(edges.size() - 1));
    		
    		// divisor main
    		edges.get(divisor).setPrev(edges.get(edges.size() - 2));
    		
    		// divisor twin
    		edges.get(divisor).getTwin().setNext(edges.get(toDivide));
    	}
    }
    
    public void print()
    {
    	HalfEdge first, current;
    	
    	System.out.println("Vertices: ");
    	
    	for(int i = 0; i < vertices.size(); i++)
    		System.out.println(vertices.get(i).id + " : ("  + 
    						   vertices.get(i).getX() + "," +
    						   vertices.get(i).getY() + ")");
    	
    	System.out.println("Faces: ");
    	
    	for(int i = 1; i < faces.size(); i++)
    	{
    		first = faces.get(i).getIncident();
    		current = faces.get(i).getIncident();
    		
    		System.out.print(faces.get(i).getId() + ": ");
    		
    		do {
    			System.out.print(current.id + " ");
    			current = current.getNext();
    		} while (first != current);
    	}
    }
    
    public void updateFaces(HalfEdge first, Face newFace)
    {
    	HalfEdge current = first;
    	do {
			current.setFace(newFace);
			current = current.getNext();
    	} while (first != current);
    }
    
    public ArrayList<HalfEdge> getEventList(boolean isHorizontal)
    {
    	ArrayList<HalfEdge> events = new ArrayList<HalfEdge>();
    	return events;
    }
}
*/