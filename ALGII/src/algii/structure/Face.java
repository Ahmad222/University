package algii.structure;

public class Face 
{
    private long id;
    private HalfEdge incident; // random HE incident to this face
    
    public Face(long id)
    {
        this.id = id;
    }
    
    public long getId()
    {
    	return id;
    }

    public HalfEdge getIncident() 
    {
        return incident;
    }

    public void setIncident(HalfEdge incident) 
    {
        this.incident = incident;
    }
    
}
