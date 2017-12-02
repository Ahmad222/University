package algii.structure;

import java.util.Comparator;

public class HalfEdge {
    public String id;
    
    private HalfEdge twin;
    private HalfEdge prev;
    private HalfEdge next;
    
    private Vertex origin;
    private Vertex target;
    private Face face;

    public HalfEdge getTwin() 
    {
        return twin;
    }

    public void setTwin(HalfEdge twin) 
    {
        this.twin = twin;
    }

    public HalfEdge getPrev() 
    {
        return prev;
    }

    public void setPrev(HalfEdge prev) 
    {
        this.prev = prev;
    }
    
    public HalfEdge getNext() 
    {
        return next;
    }

    public void setNext(HalfEdge next) 
    {
        this.next = next;
    }

    public Vertex getOrigin() 
    {
        return origin;
    }

    public void setOrigin(Vertex origin) 
    {
        this.origin = origin;
    }
    
    public Vertex getTarget() 
    {
        return target;
    }

    public void setTarget(Vertex target) 
    {
        this.target = target;
    }

    public Face getFace() 
    {
        return face;
    }

    public void setFace(Face face) 
    {
        this.face = face;
    }
    
    public boolean isHorizontal()
    {
    	return origin.getY() == target.getY();
    }
    
    public long maxCordX()
    {
    	if(origin.getX() > target.getX())
    		return origin.getX();
    	else
    		return target.getX();
    }
    
    public long maxCordY()
    {
    	if(origin.getY() > target.getY())
    		return origin.getY();
    	else
    		return target.getY();
    }
    
    public static Comparator<HalfEdge> xComp = new Comparator<HalfEdge>(){
        public int compare(final HalfEdge a, final HalfEdge b){
            if (a.maxCordX() == b.maxCordX())
                    return a.maxCordY() < b.maxCordY() ? 1 : -1;
            return a.maxCordX() < b.maxCordX() ? 1 : -1;
        }
    };

    public static Comparator<HalfEdge> yComp = new Comparator<HalfEdge>(){
        public int compare(final HalfEdge a, final HalfEdge b){
            if (a.maxCordY() == b.maxCordY())
                    return a.maxCordX() < b.maxCordX() ? 1 : -1;
            return a.maxCordY() < b.maxCordY() ? 1 : -1;
        }
    };
}
