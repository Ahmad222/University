package algii.structure;

import java.util.Comparator;

public class Vertex 
{
    public long id;
    private long x, y; // Coordinates
    private HalfEdge incident; // Random HE that starts on this vertex
    
    private boolean isReflex;

    public Vertex(long x, long y)
    {
        this.x = x;
        this.y = y;
    }
    
    public HalfEdge getIncident() 
    {
        return incident;
    }

    public void setIncident(HalfEdge incident) 
    {
        this.incident = incident;
    }

    public boolean isReflex() 
    {
        return isReflex;
    }

    public void setIsReflex(boolean isReflex) 
    {
        this.isReflex = isReflex;
    }

    public long getX() 
    {
        return x;
    }

    public long getY() 
    {
        return y;
    }
    
    public static Comparator<Vertex> xComp = new Comparator<Vertex>(){
        public int compare(final Vertex a, final Vertex b){
            if (a.x == b.x)
                    return a.y < b.y ? 1 : -1;
            return a.x < b.x ? 1 : -1;
        }
    };

    public static Comparator<Vertex> yComp = new Comparator<Vertex>(){
        public int compare(final Vertex a, final Vertex b){
            if (a.y == b.y)
                    return a.x < b.x ? 1 : -1;
            return a.y < b.y ? 1 : -1;
        }
    };
}
