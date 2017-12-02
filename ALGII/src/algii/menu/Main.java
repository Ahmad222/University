package algii.menu;

import algii.structure.DCEL;
import algii.structure.Face;
import algii.structure.HalfEdge;
import algii.structure.Vertex;

import java.awt.PageAttributes.OriginType;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	
    public static void main(String args[]) {
    
       //Question 1
    	System.out.println("Welcome to our application\n Question 1: ");
    	DCEL Original_polygon = new DCEL();  // the original polygon Do not change
    	Original_polygon.setVertices(read());
    	Original_polygon.init();
        
    	Original_polygon.print();
    	
    	//Question 1
    	DCEL Polygon1 = new DCEL(); // to be used in Question 1
    	Polygon1 = Original_polygon;
    	
       
     //Questio 2
       System.out.println("The end of question: 1. \n Do you want to apply Question 2   on the same polygon ?  y/n");
       Scanner Main_input = new Scanner(System.in);
       DCEL square = new DCEL();
       DCEL polygon2 = new DCEL();
       if(Main_input.next().equals('y'))
       {
    	   polygon2 = Original_polygon;
    	   System.out.println("Now enter the Values of the square in the same way as the polygon");
    	   square.setVertices(read());
           System.out.println("The end11");
           square.init();
           System.out.println("Th1e end22");
           
           
       }
       else{
    	   
           polygon2.setVertices(read());
           System.out.println("The end333");
           polygon2.init();
           System.out.println("The end444");
          
           square.setVertices(read());
           System.out.println("The end11");
           square.init();
           System.out.println("The end22");
       }
       DCEL Q2Res = ques2(square, polygon2, square.getEdges().get(0).getOrigin().getX(), square.getEdges().get(3).getOrigin().getX());
       Q2Res.print();
       System.out.println("The end of question 2, \n welcome to guestion 3 \n Do you want to apply Question 3 on the same polygon and square ?  y/n");
   
       
       
       //Question 3
       DCEL Polygon3 = new DCEL();
       DCEL Square2 = new DCEL();
       DCEL Visible_Region = new DCEL();
       if( Main_input.next().equals('y'))
       {
    	   Polygon3 = Original_polygon;
    	   Square2 = square;
      }
       else {
    	   System.out.println("Since your answer was not yes, then enter the values of the new polygon \n");
    	   Polygon3.setVertices(read());
           System.out.println("The end333");
           Polygon3.init();
           
           System.out.println("\n\n Now define your environment as a square surrounding the polygon : \n");
           Square2.setVertices(read());
           System.out.println("The end11");
           Square2.init();
           System.out.println("The end22");
       }
       
       Visible_Region = Quest3_reading(Polygon3 , Square2);
     
           if(Visible_Region == null)
        	   {
        	   		return;
        	   }
           else
           {
        	   System.out.println("Question3 done \n Visible DCEL :\n " );
        	   Visible_Region.print();
           }
           
           System.out.println("The end");
           
    }
    
    public static  ArrayList<Vertex> read()
    {
        Scanner input = new Scanner(System.in);
        long nPoints;
        long x, y;
        
        //System.out.println("N points?");
        nPoints = input.nextInt();
        
        //System.out.println("Coordinates?");
        ArrayList<Vertex> vertices = new ArrayList();
        for(int i = 0; i < nPoints; i++) 
        {
            x = input.nextLong();
            y = input.nextLong();
            vertices.add(new Vertex(x, y));
            vertices.get(i).id = i;
        }
        
       // input.close();
        return vertices;
    }   
    
    public static DCEL op1(DCEL polygon)
    {    
        return polygon;
    }
    public static DCEL ques2(DCEL square, DCEL poly, long Xl, long Xr)
    {
    	Vertex CutOrigen = null, CutTarget = null;
    	boolean flag =false;
    	ArrayList<HalfEdge> AuxiliarArray = new ArrayList<HalfEdge>();
    	Face newFace= null;
    	HalfEdge TopEdge = square.getEdges().get(3);//topEdge
    	int TopEdgeIndex = 3; // for the first cut the this value will change
    	 ArrayList<HalfEdge> eventList = new ArrayList<HalfEdge>();
    	 for(HalfEdge h : poly.getEdges())
    	 {
    		 if(h.getFace().getId()==0)
    		 {
    			 eventList.add(h);
    		 }
    	 }
    	 
    	 //sort the list according to the y access
    	 Collections.sort(eventList, HalfEdge.yComp);
    	 boolean FaceFlag = false;
    	for(HalfEdge h : eventList)
    	{
    		
    		if(h.getOrigin().getY() == h.getTarget().getY())//main edge in the polygon face=1
    		{
    			
    			newFace = new Face(square.getFaces().size()-1);
        		
        		square.getFaces().add(newFace);
        		
        		
    			square.addEdge(h.getOrigin(),h.getTarget());
    			int HIndex = square.getEdges().size()-2;
    			newFace.setIncident(square.getEdges().get(HIndex));
    			//calculate the topedge index
    			/*
    			if(square.getFaces().size() == 2) // the first cut => the top edge is the top edge of the original square
    			{
    				TopEdgeIndex = 3;
    			}
    			else
    			{
    				for()
    			}
    			*/
    			if(!h.getOrigin().isReflex()) // here we will cut from the origin
    			{
    				int index ;
    				
    				if(h.getOrigin().getX() > h.getTarget().getX()) // cut to the right
    				{
    					index = 2;
    					AuxiliarArray.clear();
    					flag = false;
    					for(HalfEdge hc : eventList)
        				{
        					if((hc.getOrigin().getX() == hc.getTarget().getX()) && hc.getOrigin().getX() > h.getOrigin().getX()  &&(hc.getOrigin().getX() != Xl ) && (hc.getOrigin().getX() != Xr))
        					{
        						if((h.getOrigin().getY() <= hc.getOrigin().getY()  && h.getOrigin().getY() >= hc.getTarget().getY()) || (h.getOrigin().getY() >= hc.getOrigin().getY()  && h.getOrigin().getY() <= hc.getTarget().getY()))
        						{
        							AuxiliarArray.add(hc);
        							/*
        							CutOrigen = new Vertex(h.getOrigin().getX(),h.getOrigin().getY());
        							CutTarget = new Vertex(hc.getTarget().getX(), h.getOrigin().getY());
        							if(square.getEdges().contains(hc))
        							{
        								//index = square.getEdges().indexOf(hc);
        							}
        							else
        							{
        								//square.addEdge(hc.getOrigin(), hc.getTarget());
        								//index = square.getEdges().indexOf(hc);
        							}
        							*/
        							flag = true;
        							//break;
        						}
        					}
        				}
    					if(!flag) // we will cut on the square
        				{
        					
    						CutOrigen = new Vertex(h.getOrigin().getX(), h.getOrigin().getY());
        					CutTarget = new Vertex(Xr,h.getOrigin().getY());
        				}
    					else
    					{
    						Collections.sort(AuxiliarArray, HalfEdge.xComp);
    				    	 
    						CutOrigen = new Vertex(h.getOrigin().getX(),h.getOrigin().getY());
							CutTarget = new Vertex(AuxiliarArray.get(AuxiliarArray.size()-1).getTarget().getX(), h.getOrigin().getY());
    					}
    					if(!square.getVertices().contains(CutOrigen))
    					{
    						square.addVertex(CutOrigen.getX(), CutOrigen.getY());
    					}
    					if(!square.getVertices().contains(CutTarget))
    					{
    						square.addVertex(CutTarget.getX(), CutTarget.getY());
    					}
        				square.addEdge(CutOrigen,CutTarget);
        				// adding the vertical edges
        				square.addEdge(CutTarget,TopEdge.getOrigin());
        				
        				square.divideEdge(index, (square.getEdges().size()-4), CutTarget, true );
        				
        				
        				
        				square.getEdges().get(square.getEdges().size()-1).setFace(newFace);
        				square.getEdges().get(square.getEdges().size()-2).setFace(newFace);
        				square.getEdges().get(square.getEdges().size()-3).setFace(newFace);
        				square.getEdges().get(square.getEdges().size()-4).setFace(newFace);
        				
        				
        				
        				
        				
        				
    				}
    				else // cut to the left
    				{
    					AuxiliarArray.clear();
    						index = 0;
    						flag = false;
        					for(HalfEdge hc : eventList)
            				{
            					if((hc.getOrigin().getX() == hc.getTarget().getX()) && (hc.getOrigin().getX() < h.getOrigin().getX())&&(hc.getOrigin().getX() != Xl ) && (hc.getOrigin().getX() != Xr))
            					{
            						if((h.getOrigin().getY() <= hc.getOrigin().getY()  && h.getOrigin().getY() >= hc.getTarget().getY()) || (h.getOrigin().getY() >= hc.getOrigin().getY()  && h.getOrigin().getY() <= hc.getTarget().getY()))
            						{
            							AuxiliarArray.add(hc);
            							/*
            							CutOrigen = new Vertex(hc.getOrigin().getX(),h.getOrigin().getY());
            							CutTarget = new Vertex(h.getOrigin().getX(), h.getOrigin().getY());
            							if(square.getEdges().contains(hc))
            							{
            								//index = square.getEdges().indexOf(hc);
            							}
            							else
            							{
            								//square.addEdge(hc.getOrigin(), hc.getTarget());
            								//index = square.getEdges().indexOf(hc);
            							}
            							*/
            							flag = true;
            							//break;
            						}
            					}
            				}
        					if(!flag) // we will cut on the square
            				{
        						CutOrigen = new Vertex(h.getOrigin().getX(), h.getTarget().getY());
        						CutTarget = new Vertex(Xl,h.getOrigin().getY());
            					
            				}
        					else
        					{
        						Collections.sort(AuxiliarArray, HalfEdge.xComp);
    							
        						CutOrigen = new Vertex(AuxiliarArray.get(0).getOrigin().getX(),h.getOrigin().getY());
    							CutTarget = new Vertex(h.getOrigin().getX(), h.getOrigin().getY());
        					}
        					if(!square.getVertices().contains(CutOrigen))
        					{
        						square.addVertex(CutOrigen.getX(), CutOrigen.getY());
        					}
        					if(!square.getVertices().contains(CutTarget))
        					{
        						square.addVertex(CutTarget.getX(), CutTarget.getY());
        					}
            				square.addEdge(CutOrigen,CutTarget);
            				//  vertical left edge 
            				square.addEdge(CutOrigen,TopEdge.getTarget());
            				
            				square.divideEdge(index, (square.getEdges().size()-4), CutOrigen, false);
            				
            				
            				
            				square.getEdges().get(square.getEdges().size()-1).setFace(newFace);
            				square.getEdges().get(square.getEdges().size()-2).setFace(newFace);
            				square.getEdges().get(square.getEdges().size()-3).setFace(newFace);
            				square.getEdges().get(square.getEdges().size()-4).setFace(newFace);
    					
    				}
    			
    				/*
    				// edit the previous and next of the added main
    				square.getEdges().get(square.getEdges().size()-4).setPrev(square.getEdges().get(HIndex));///(h);
    				square.getEdges().get(square.getEdges().size()-4).setNext(square.getEdges().get(square.getEdges().size()-2));
    				// vertical right edge
    				square.getEdges().get(square.getEdges().size()-2).setPrev(square.getEdges().get(square.getEdges().size()-4));
    				square.getEdges().get(square.getEdges().size()-2).setNext(square.getEdges().get(TopEdgeIndex));//??? topedge
    				
    				//edit the previous and next of the added target
    				square.getEdges().get(square.getEdges().size()-1).setPrev(null);// we do not need to asign them here we will in the following cut
    				square.getEdges().get(square.getEdges().size()-1).setNext(null);
    				*/
    			}
    			if(!h.getTarget().isReflex())  // the cut from the target
    			{
    				int index;
    				
    				if(h.getTarget().getX() < h.getOrigin().getX()) // cut to the left
    				{	index = 0;
    					AuxiliarArray.clear();
    					flag = false;
    					for(HalfEdge hc : eventList)
        				{
        					if((hc.getOrigin().getX() == hc.getTarget().getX()) && hc.getOrigin().getX() < h.getTarget().getX()  &&(hc.getOrigin().getX() != Xl ) && (hc.getOrigin().getX() != Xr))
        					{
        						if((h.getOrigin().getY() <= hc.getOrigin().getY()  && h.getOrigin().getY() >= hc.getTarget().getY()) || (h.getOrigin().getY() >= hc.getOrigin().getY()  && h.getOrigin().getY() <= hc.getTarget().getY() ))
        						{
        							AuxiliarArray.add(hc);
        							/*
        							CutOrigen = new Vertex(hc.getOrigin().getX(),h.getTarget().getY());
        							CutTarget = new Vertex(h.getTarget().getX(), h.getTarget().getY());
        							if(square.getEdges().contains(hc))
        							{
        								//index = square.getEdges().indexOf(hc);
        							}
        							else
        							{
        								//square.addEdge(hc.getOrigin(), hc.getTarget());
        								//index = square.getEdges().indexOf(hc);
        							}
        							*/
        							flag = true;
        							//break;
        						}
        					}
        				}
    					if(!flag) // we will cut on the square
        				{
        					CutOrigen = new Vertex(Xl,h.getTarget().getY());
    						CutTarget = new Vertex(h.getTarget().getX(), h.getTarget().getY());
        				}
    					else
    					{
    						
        						Collections.sort(AuxiliarArray, HalfEdge.xComp);
        				    	 
        						CutOrigen = new Vertex(AuxiliarArray.get(0).getOrigin().getX(),h.getTarget().getY());
    							CutTarget = new Vertex(h.getTarget().getX(), h.getTarget().getY());
        						
        						
        					
    					}
    					if(!square.getVertices().contains(CutOrigen))
    					{
    						square.addVertex(CutOrigen.getX(), CutOrigen.getY());
    					}
    					if(!square.getVertices().contains(CutTarget))
    					{
    						square.addVertex(CutTarget.getX(), CutTarget.getY());
    					}
        				square.addEdge(CutOrigen,CutTarget);
        				//  vertical left edge 
        				square.addEdge(CutOrigen,TopEdge.getTarget());
        				
        				square.divideEdge(index, (square.getEdges().size()-4), CutOrigen, false);
        				
        				
        				
        				square.getEdges().get(square.getEdges().size()-1).setFace(newFace);
        				square.getEdges().get(square.getEdges().size()-2).setFace(newFace);
        				square.getEdges().get(square.getEdges().size()-3).setFace(newFace);
        				square.getEdges().get(square.getEdges().size()-4).setFace(newFace);
    				}
    				else // cut to the right
    				{
    					index = 2;
    					AuxiliarArray.clear();
    					flag = false;
    					for(HalfEdge hc : eventList)
        				{
        					if((hc.getOrigin().getX() == hc.getTarget().getX()) && hc.getOrigin().getX() > h.getTarget().getX()  &&(hc.getOrigin().getX() != Xl ) && (hc.getOrigin().getX() != Xr))
        					{
        						if((h.getOrigin().getY() <= hc.getOrigin().getY()  && h.getOrigin().getY() >= hc.getTarget().getY()) || (h.getOrigin().getY() >= hc.getOrigin().getY()  && h.getOrigin().getY() <= hc.getTarget().getY()))
        						{
        							AuxiliarArray.add(hc);
        							/*
        							CutOrigen = new Vertex(h.getTarget().getX(),h.getTarget().getY());
        							CutTarget = new Vertex(hc.getTarget().getX(), h.getTarget().getY());
        							
        							if(square.getEdges().contains(hc))
        							{
        								//index = square.getEdges().indexOf(hc);
        							}
        							else
        							{
        								//square.addEdge(hc.getOrigin(), hc.getTarget());
        								//index = square.getEdges().indexOf(hc);
        							}
        							*/
        							flag = true;
        							//break;
        						}
        					}
        				}
    					if(!flag) // we will cut on the square
        				{
        					
    						CutOrigen = new Vertex(h.getTarget().getX(), h.getTarget().getY());
        					CutTarget = new Vertex(Xr,h.getTarget().getY());
        				}
    					else
    					{
    						Collections.sort(AuxiliarArray, HalfEdge.xComp);
    						CutOrigen = new Vertex(h.getTarget().getX(),h.getTarget().getY());
							CutTarget = new Vertex(AuxiliarArray.get(AuxiliarArray.size()-1).getTarget().getX(), h.getTarget().getY());
							
    						
    					}
    					if(!square.getVertices().contains(CutOrigen))
    					{
    						square.addVertex(CutOrigen.getX(), CutOrigen.getY());
    					}
    					if(!square.getVertices().contains(CutTarget))
    					{
    						square.addVertex(CutTarget.getX(), CutTarget.getY());
    					}
        				
        				square.addEdge(CutOrigen,CutTarget);
        				//  vertical left edge 
        				square.addEdge(CutOrigen,TopEdge.getTarget());
        				
        				square.divideEdge(index, (square.getEdges().size()-4),  CutTarget, true );
        				
        				
        				
        				square.getEdges().get(square.getEdges().size()-1).setFace(newFace);
        				square.getEdges().get(square.getEdges().size()-2).setFace(newFace);
        				square.getEdges().get(square.getEdges().size()-3).setFace(newFace);
        				square.getEdges().get(square.getEdges().size()-4).setFace(newFace);
    				}
    				
    				
    				
    				/*
    				// edit the previous and next of the added main
    				square.getEdges().get(square.getEdges().size()-4).setPrev(square.getEdges().get(square.getEdges().size()-2)); //?? topedge
    				square.getEdges().get(square.getEdges().size()-4).setNext(square.getEdges().get(HIndex));
    				//vertical left edge
    				square.getEdges().get(square.getEdges().size()-2).setPrev(square.getEdges().get(TopEdgeIndex)); //?? topedge
    				square.getEdges().get(square.getEdges().size()-2).setNext(square.getEdges().get(square.getEdges().size()-4));
    				
    				// edit the previous and next of the added target --- no need here 
    				square.getEdges().get(square.getEdges().size()-1).setPrev(null);//???
    				square.getEdges().get(square.getEdges().size()-1).setNext(null);
    				
    				*/
    			}
    		}
    		else
    		{
    			//if(! square.getEdges().contains(h))
    			//{
    				square.addEdge(h.getOrigin(),h.getTarget());
    			//}
    			
    		}
    	}
    	
    	
    	return square;
    }
   public static DCEL Quest3_reading(DCEL Polygon, DCEL Square)
   {
	   Scanner input3 = new Scanner(System.in);
       int Direction = 0;
       long x, y;
       String dir;
       boolean isInterior = false;
       System.out.println("Now input the need value for Question 3  \n The Input should be like this: \n - first the (1 or 0)  to identify if interior or exterior (1 means interior) \n - the Direction (NE,NW,SW,SE) \n - the vertex (X Y)");
       if(input3.nextInt() == 1)
       {
       	isInterior = true;
       }
       //System.out.println("N points?");
       dir = input3.nextLine();
       switch(dir)
       {
	       case "NW"  : Direction = 1;
	       	break;
	       case "nw"  : Direction = 1;
	      	break;
	       case "NE" : Direction = 2;
	   		break;
	       case "ne" : Direction = 2;
	  		break;
	  
	       case "SW" : Direction = 3;
	   		break;
	       case "sw" : Direction = 3;
	  		break;
	       case "SE" : Direction = 4;
	       	break;
	       case "se" : Direction = 4;
	      	break;
	      	
	       default: System.out.println("The direction is not correct");
	       	break;
   
       }
       
      x = input3.nextLong();
      y = input3.nextLong();
      Vertex Visibility = new Vertex(x, y);
      if(Direction != 0)
      {
    	  return (Quest3_apply(Direction, isInterior, Visibility, Polygon, Square));
      }
      else return null;
   }
   public static DCEL Quest3_apply(int Direction, boolean isInterior, Vertex VisibilityVertex, DCEL Polygon, DCEL Square)
   {
	   DCEL Visible_region = new DCEL();
	   ArrayList<Face> Visible_Faces = new ArrayList<Face>();
	   ArrayList<Vertex> Visible_vertices = new ArrayList<Vertex>();
	   ArrayList<HalfEdge> Visible_edges = new ArrayList<HalfEdge>();
	   
	   if(isInterior)// we should apply grid partition (question 1)
	   {
		   
		  // if (VisibilityVertex.isReflex()) //one edge tied to the polygon
		//   {
			   boolean Flag = true;
			   DCEL res = new DCEL(); // here we should apply question 1 to the polygon   (res = result of Q1)
			   for(Face f : res.getFaces())
			   {
				   
					   switch (Direction) {
					case 1:
						Flag = true;
						HalfEdge h = f.getIncident();
						for(int i =0 ; i<4 ; i++)
						   {
							h= h.getNext();
							 if(!(h.getOrigin().getX() <= VisibilityVertex.getX()) &&(h.getOrigin().getY() >= VisibilityVertex.getY()))
							 {
								 Flag = false;
								 break;
							 }
							 else{
								 //check if the line intersects with the polygon
								 for(HalfEdge hh : Polygon.getEdges())
								 {
									 if(!(((hh.getOrigin().getX() == hh.getTarget().getX() )&& (hh.getTarget().getX()== VisibilityVertex.getX())) || ((hh.getOrigin().getY() == hh.getTarget().getY() )&& (hh.getTarget().getY()== VisibilityVertex.getY()))))
									 {
										 if( Line2D.linesIntersect(VisibilityVertex.getX(),VisibilityVertex.getY(),h.getOrigin().getX(),h.getOrigin().getY(),hh.getOrigin().getX(),hh.getOrigin().getY(),hh.getTarget().getX(),hh.getTarget().getY()))
										 {
											 Flag= false;
											 break;
										 }
									 }
								 }
								
							 }
							   
						   }
						if(Flag)
						{
							h=f.getIncident();
							Face fnew = new Face(Visible_region.getFaces().size());
							Face AuxFace = new Face(-3);
							int [] indexes = {0,0,0,0};
							int counterIndexes = 0;
							for(int i =0 ; i<4 ; i++)
							   {
									Vertex v1 = new Vertex(h.getOrigin().getX(), h.getOrigin().getY());
									Vertex v2 = new Vertex(h.getTarget().getX(), h.getTarget().getY());
									
									if(! Visible_region.getVertices().contains(v1))
									{
										Visible_region.addVertex(v1.getX(), v1.getY());
										if(! Visible_region.getVertices().contains(v2)) // the two vertices are new
										{
											Visible_region.addVertex(v2.getX(), v2.getY());
											
											Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-2), Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
											
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
											
													
										}
										else{
											//indexes[] = Visible_region.getVertices().indexOf(v2);
											Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-1), Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
											
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
											
										}
									}
									else{
										if(! Visible_region.getVertices().contains(v2))
										{
											Visible_region.addVertex(v2.getX(), v2.getY());
											
											Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
											
											
											
										}
										else{  // the two vertices exist already
											boolean Flag2= true;
											for(HalfEdge he : Visible_region.getEdges()) // test if there is an edge between them already
											{
												if((he.getOrigin() == v1 && he.getTarget() == v2)&& he.getFace().getId() == -3)
												{
													he.setFace(fnew);
													indexes[counterIndexes] = Visible_region.getEdges().indexOf(he);
													counterIndexes ++;
													Flag2 = false;
													break;
													
												}
											}
											if(Flag2)
											{
												Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
												Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
												Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
												indexes[counterIndexes] = Visible_region.getEdges().size()-2;
												counterIndexes ++;
											}
											
										}
									}
									
									
								
									h=h.getNext();
							   }
							fnew.setIncident(Visible_region.getEdges().get(indexes[0]));
							Visible_region.getEdges().get(indexes[0]).setNext(Visible_region.getEdges().get(indexes[1]));
							Visible_region.getEdges().get(indexes[0]).setPrev(Visible_region.getEdges().get(indexes[3]));
							
							Visible_region.getEdges().get(indexes[1]).setNext(Visible_region.getEdges().get(indexes[2]));
							Visible_region.getEdges().get(indexes[1]).setPrev(Visible_region.getEdges().get(indexes[0]));
							
							Visible_region.getEdges().get(indexes[2]).setNext(Visible_region.getEdges().get(indexes[3]));
							Visible_region.getEdges().get(indexes[2]).setPrev(Visible_region.getEdges().get(indexes[1]));
							
							Visible_region.getEdges().get(indexes[3]).setNext(Visible_region.getEdges().get(indexes[0]));
							Visible_region.getEdges().get(indexes[3]).setPrev(Visible_region.getEdges().get(indexes[2]));
							
							
						}
						break;
					case 2:
						Flag = true;
						HalfEdge h2 = f.getIncident();
						for(int i =0 ; i<4 ; i++)
						   {
							h2= h2.getNext();
							 if(!(h2.getOrigin().getX() >= VisibilityVertex.getX()) &&(h2.getOrigin().getY() >= VisibilityVertex.getY()))
							 {
								 Flag = false;
								 break;
							 }
							 else
							 { //check if the line intersects with the polygon
								 for(HalfEdge hh : Polygon.getEdges())
								 {
									 if(!(((hh.getOrigin().getX() == hh.getTarget().getX() )&& (hh.getTarget().getX()== VisibilityVertex.getX())) || ((hh.getOrigin().getY() == hh.getTarget().getY() )&& (hh.getTarget().getY()== VisibilityVertex.getY()))))
									 {
										 if( Line2D.linesIntersect(VisibilityVertex.getX(),VisibilityVertex.getY(),h2.getOrigin().getX(),h2.getOrigin().getY(),hh.getOrigin().getX(),hh.getOrigin().getY(),hh.getTarget().getX(),hh.getTarget().getY()))
										 {
											 Flag= false;
											 break;
										 }
									 }
								 }
								
							 }
							   
						   }
						if(Flag)
						{
							h2=f.getIncident();
							Face fnew = new Face(Visible_region.getFaces().size());
							Face AuxFace = new Face(-3);
							int [] indexes = {0,0,0,0};
							int counterIndexes = 0;
							for(int i =0 ; i<4 ; i++)
							   {
									Vertex v1 = new Vertex(h2.getOrigin().getX(), h2.getOrigin().getY());
									Vertex v2 = new Vertex(h2.getTarget().getX(), h2.getTarget().getY());
									
									if(! Visible_region.getVertices().contains(v1))
									{
										Visible_region.addVertex(v1.getX(), v1.getY());
										if(! Visible_region.getVertices().contains(v2)) // the two vertices are new
										{
											Visible_region.addVertex(v2.getX(), v2.getY());
											
											Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-2), Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
											
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
											
													
										}
										else{
											//indexes[] = Visible_region.getVertices().indexOf(v2);
											Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-1), Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
											
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
											
										}
									}
									else{
										if(! Visible_region.getVertices().contains(v2))
										{
											Visible_region.addVertex(v2.getX(), v2.getY());
											
											Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
											
											
											
										}
										else{  // the two vertices exist already
											boolean Flag2= true;
											for(HalfEdge he : Visible_region.getEdges()) // test if there is an edge between them already
											{
												if((he.getOrigin() == v1 && he.getTarget() == v2)&& he.getFace().getId() == -3)
												{
													he.setFace(fnew);
													indexes[counterIndexes] = Visible_region.getEdges().indexOf(he);
													counterIndexes ++;
													Flag2 = false;
													break;
													
												}
											}
											if(Flag2)
											{
												Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
												Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
												Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
												indexes[counterIndexes] = Visible_region.getEdges().size()-2;
												counterIndexes ++;
											}
											
										}
									}
									
									
								
									h2=h2.getNext();
							   }
							fnew.setIncident(Visible_region.getEdges().get(indexes[0]));
							Visible_region.getEdges().get(indexes[0]).setNext(Visible_region.getEdges().get(indexes[1]));
							Visible_region.getEdges().get(indexes[0]).setPrev(Visible_region.getEdges().get(indexes[3]));
							
							Visible_region.getEdges().get(indexes[1]).setNext(Visible_region.getEdges().get(indexes[2]));
							Visible_region.getEdges().get(indexes[1]).setPrev(Visible_region.getEdges().get(indexes[0]));
							
							Visible_region.getEdges().get(indexes[2]).setNext(Visible_region.getEdges().get(indexes[3]));
							Visible_region.getEdges().get(indexes[2]).setPrev(Visible_region.getEdges().get(indexes[1]));
							
							Visible_region.getEdges().get(indexes[3]).setNext(Visible_region.getEdges().get(indexes[0]));
							Visible_region.getEdges().get(indexes[3]).setPrev(Visible_region.getEdges().get(indexes[2]));
							
							
						}
						break;
					case 3:
						Flag = true;
						HalfEdge h3 = f.getIncident();
						for(int i =0 ; i<4 ; i++)
						   {
							h3= h3.getNext();
							 if(!(h3.getOrigin().getX() <= VisibilityVertex.getX()) &&(h3.getOrigin().getY() <= VisibilityVertex.getY()))
							 {
								 Flag = false;
								 break;
							 }
							 else
							 {
								//check if the line intersects with the polygon
								 for(HalfEdge hh : Polygon.getEdges())
								 {
									 if(!(((hh.getOrigin().getX() == hh.getTarget().getX() )&& (hh.getTarget().getX()== VisibilityVertex.getX())) || ((hh.getOrigin().getY() == hh.getTarget().getY() )&& (hh.getTarget().getY()== VisibilityVertex.getY()))))
									{
										 if( Line2D.linesIntersect(VisibilityVertex.getX(),VisibilityVertex.getY(),h3.getOrigin().getX(),h3.getOrigin().getY(),hh.getOrigin().getX(),hh.getOrigin().getY(),hh.getTarget().getX(),hh.getTarget().getY()))
										 {
											 Flag= false;
											 break;
										 }
									 }
								 }
								
							 }
							   
						   }
						if(Flag)
						{
							h3=f.getIncident();
							Face fnew = new Face(Visible_region.getFaces().size());
							Face AuxFace = new Face(-3);
							int [] indexes = {0,0,0,0};
							int counterIndexes = 0;
							for(int i =0 ; i<4 ; i++)
							   {
									Vertex v1 = new Vertex(h3.getOrigin().getX(), h3.getOrigin().getY());
									Vertex v2 = new Vertex(h3.getTarget().getX(), h3.getTarget().getY());
									
									if(! Visible_region.getVertices().contains(v1))
									{
										Visible_region.addVertex(v1.getX(), v1.getY());
										if(! Visible_region.getVertices().contains(v2)) // the two vertices are new
										{
											Visible_region.addVertex(v2.getX(), v2.getY());
											
											Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-2), Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
											
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
											
													
										}
										else{
											//indexes[] = Visible_region.getVertices().indexOf(v2);
											Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-1), Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
											
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
											
										}
									}
									else{
										if(! Visible_region.getVertices().contains(v2))
										{
											Visible_region.addVertex(v2.getX(), v2.getY());
											
											Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
											
											
											
										}
										else{  // the two vertices exist already
											boolean Flag2= true;
											for(HalfEdge he : Visible_region.getEdges()) // test if there is an edge between them already
											{
												if((he.getOrigin() == v1 && he.getTarget() == v2)&& he.getFace().getId() == -3)
												{
													he.setFace(fnew);
													indexes[counterIndexes] = Visible_region.getEdges().indexOf(he);
													counterIndexes ++;
													Flag2 = false;
													break;
													
												}
											}
											if(Flag2)
											{
												Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
												Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
												Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
												indexes[counterIndexes] = Visible_region.getEdges().size()-2;
												counterIndexes ++;
											}
											
										}
									}
									
									
								
									h3=h3.getNext();
							   }
							fnew.setIncident(Visible_region.getEdges().get(indexes[0]));
							Visible_region.getEdges().get(indexes[0]).setNext(Visible_region.getEdges().get(indexes[1]));
							Visible_region.getEdges().get(indexes[0]).setPrev(Visible_region.getEdges().get(indexes[3]));
							
							Visible_region.getEdges().get(indexes[1]).setNext(Visible_region.getEdges().get(indexes[2]));
							Visible_region.getEdges().get(indexes[1]).setPrev(Visible_region.getEdges().get(indexes[0]));
							
							Visible_region.getEdges().get(indexes[2]).setNext(Visible_region.getEdges().get(indexes[3]));
							Visible_region.getEdges().get(indexes[2]).setPrev(Visible_region.getEdges().get(indexes[1]));
							
							Visible_region.getEdges().get(indexes[3]).setNext(Visible_region.getEdges().get(indexes[0]));
							Visible_region.getEdges().get(indexes[3]).setPrev(Visible_region.getEdges().get(indexes[2]));
							
							
						}
						break;
					case 4:
						Flag = true;
						HalfEdge h4 = f.getIncident();
						for(int i =0 ; i<4 ; i++)
						   {
							h4= h4.getNext();
							 if(!(h4.getOrigin().getX() >= VisibilityVertex.getX()) &&(h4.getOrigin().getY() <= VisibilityVertex.getY()))
							 {
								 Flag = false;
								 break;
							 }
							 else
							 {//check if the line intersects with the polygon
								 for(HalfEdge hh : Polygon.getEdges())
								 {
									 if(!(((hh.getOrigin().getX() == hh.getTarget().getX() )&& (hh.getTarget().getX()== VisibilityVertex.getX())) || ((hh.getOrigin().getY() == hh.getTarget().getY() )&& (hh.getTarget().getY()== VisibilityVertex.getY()))))
									 {
										 if( Line2D.linesIntersect(VisibilityVertex.getX(),VisibilityVertex.getY(),h4.getOrigin().getX(),h4.getOrigin().getY(),hh.getOrigin().getX(),hh.getOrigin().getY(),hh.getTarget().getX(),hh.getTarget().getY()))
										 {
											 Flag= false;
											 break;
										 }
									 }
								 }
								
							 }
							   
						   }
						if(Flag)
						{
							h4=f.getIncident();
							Face fnew = new Face(Visible_region.getFaces().size());
							Face AuxFace = new Face(-3);
							int [] indexes = {0,0,0,0};
							int counterIndexes = 0;
							for(int i =0 ; i<4 ; i++)
							   {
									Vertex v1 = new Vertex(h4.getOrigin().getX(), h4.getOrigin().getY());
									Vertex v2 = new Vertex(h4.getTarget().getX(), h4.getTarget().getY());
									
									if(! Visible_region.getVertices().contains(v1))
									{
										Visible_region.addVertex(v1.getX(), v1.getY());
										if(! Visible_region.getVertices().contains(v2)) // the two vertices are new
										{
											Visible_region.addVertex(v2.getX(), v2.getY());
											
											Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-2), Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
											
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
											
													
										}
										else{
											//indexes[] = Visible_region.getVertices().indexOf(v2);
											Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-1), Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
											
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
											
										}
									}
									else{
										if(! Visible_region.getVertices().contains(v2))
										{
											Visible_region.addVertex(v2.getX(), v2.getY());
											
											Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
											
											
											
										}
										else{  // the two vertices exist already
											boolean Flag2= true;
											for(HalfEdge he : Visible_region.getEdges()) // test if there is an edge between them already
											{
												if((he.getOrigin() == v1 && he.getTarget() == v2)&& he.getFace().getId() == -3)
												{
													he.setFace(fnew);
													indexes[counterIndexes] = Visible_region.getEdges().indexOf(he);
													counterIndexes ++;
													Flag2 = false;
													break;
													
												}
											}
											if(Flag2)
											{
												Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
												Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
												Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
												indexes[counterIndexes] = Visible_region.getEdges().size()-2;
												counterIndexes ++;
											}
											
										}
									}
									
									
								
									h4=h4.getNext();
							   }
							fnew.setIncident(Visible_region.getEdges().get(indexes[0]));
							Visible_region.getEdges().get(indexes[0]).setNext(Visible_region.getEdges().get(indexes[1]));
							Visible_region.getEdges().get(indexes[0]).setPrev(Visible_region.getEdges().get(indexes[3]));
							
							Visible_region.getEdges().get(indexes[1]).setNext(Visible_region.getEdges().get(indexes[2]));
							Visible_region.getEdges().get(indexes[1]).setPrev(Visible_region.getEdges().get(indexes[0]));
							
							Visible_region.getEdges().get(indexes[2]).setNext(Visible_region.getEdges().get(indexes[3]));
							Visible_region.getEdges().get(indexes[2]).setPrev(Visible_region.getEdges().get(indexes[1]));
							
							Visible_region.getEdges().get(indexes[3]).setNext(Visible_region.getEdges().get(indexes[0]));
							Visible_region.getEdges().get(indexes[3]).setPrev(Visible_region.getEdges().get(indexes[2]));
							
							
						}
						break;
					default:
						break;
					}
					  
				   }
			   
		   
		   
		  //}
	   }
	   else // we apply question two 
	   {
		   boolean Flag = true;
		   DCEL res =  ques2(Square, Polygon, Square.getEdges().get(0).getOrigin().getX(), Square.getEdges().get(3).getOrigin().getX());
		   for(Face f : res.getFaces())
		   {
			   if(f.getId() != 0)// we do not need the interior face
			   {
				   switch (Direction) {
				case 1:
					Flag = true;
					HalfEdge h = f.getIncident();
					for(int i =0 ; i<4 ; i++)
					   {
						h= h.getNext();
						 if(!(h.getOrigin().getX() <= VisibilityVertex.getX()) &&(h.getOrigin().getY() >= VisibilityVertex.getY()))
						 {
							 Flag = false;
							 break;
						 }
						 else
						 {//check if the line intersects with the polygon
							 for(HalfEdge hh : Polygon.getEdges())
							 {
								
									 if(!(((hh.getOrigin().getX() == hh.getTarget().getX() )&& (hh.getTarget().getX()== VisibilityVertex.getX())) || ((hh.getOrigin().getY() == hh.getTarget().getY() )&& (hh.getTarget().getY()== VisibilityVertex.getY()))))
									 {
										 if( Line2D.linesIntersect(VisibilityVertex.getX(),VisibilityVertex.getY(),h.getOrigin().getX(),h.getOrigin().getY(),hh.getOrigin().getX(),hh.getOrigin().getY(),hh.getTarget().getX(),hh.getTarget().getY()))
										 {
											 Flag= false;
											 break;
										 }
									 }
								 
							 }
							
						 }
						   
					   }
					if(Flag)
					{
						h=f.getIncident();
						Face fnew = new Face(Visible_region.getFaces().size());
						Face AuxFace = new Face(-3);
						int [] indexes = {0,0,0,0};
						int counterIndexes = 0;
						for(int i =0 ; i<4 ; i++)
						   {
								Vertex v1 = new Vertex(h.getOrigin().getX(), h.getOrigin().getY());
								Vertex v2 = new Vertex(h.getTarget().getX(), h.getTarget().getY());
								
								if(! Visible_region.getVertices().contains(v1))
								{
									Visible_region.addVertex(v1.getX(), v1.getY());
									if(! Visible_region.getVertices().contains(v2)) // the two vertices are new
									{
										Visible_region.addVertex(v2.getX(), v2.getY());
										
										Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-2), Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
										
										Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
										Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
										indexes[counterIndexes] = Visible_region.getEdges().size()-2;
										counterIndexes ++;
										
												
									}
									else{
										//indexes[] = Visible_region.getVertices().indexOf(v2);
										Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-1), Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
										
										Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
										Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
										indexes[counterIndexes] = Visible_region.getEdges().size()-2;
										counterIndexes ++;
										
									}
								}
								else{
									if(! Visible_region.getVertices().contains(v2))
									{
										Visible_region.addVertex(v2.getX(), v2.getY());
										
										Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
										Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
										Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
										indexes[counterIndexes] = Visible_region.getEdges().size()-2;
										counterIndexes ++;
										
										
										
									}
									else{  // the two vertices exist already
										boolean Flag2= true;
										for(HalfEdge he : Visible_region.getEdges()) // test if there is an edge between them already
										{
											if((he.getOrigin() == v1 && he.getTarget() == v2)&& he.getFace().getId() == -3)
											{
												he.setFace(fnew);
												indexes[counterIndexes] = Visible_region.getEdges().indexOf(he);
												counterIndexes ++;
												Flag2 = false;
												break;
												
											}
										}
										if(Flag2)
										{
											Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
										}
										
									}
								}
								
								
							
								h=h.getNext();
						   }
						fnew.setIncident(Visible_region.getEdges().get(indexes[0]));
						Visible_region.getEdges().get(indexes[0]).setNext(Visible_region.getEdges().get(indexes[1]));
						Visible_region.getEdges().get(indexes[0]).setPrev(Visible_region.getEdges().get(indexes[3]));
						
						Visible_region.getEdges().get(indexes[1]).setNext(Visible_region.getEdges().get(indexes[2]));
						Visible_region.getEdges().get(indexes[1]).setPrev(Visible_region.getEdges().get(indexes[0]));
						
						Visible_region.getEdges().get(indexes[2]).setNext(Visible_region.getEdges().get(indexes[3]));
						Visible_region.getEdges().get(indexes[2]).setPrev(Visible_region.getEdges().get(indexes[1]));
						
						Visible_region.getEdges().get(indexes[3]).setNext(Visible_region.getEdges().get(indexes[0]));
						Visible_region.getEdges().get(indexes[3]).setPrev(Visible_region.getEdges().get(indexes[2]));
						
						
					}
					break;
				case 2:
					Flag = true;
					HalfEdge h2 = f.getIncident();
					for(int i =0 ; i<4 ; i++)
					   {
						h2= h2.getNext();
						 if(!(h2.getOrigin().getX() >= VisibilityVertex.getX()) &&(h2.getOrigin().getY() >= VisibilityVertex.getY()))
						 {
							 Flag = false;
							 break;
						 }
						 else
						 { //check if the line intersects with the polygon
							 for(HalfEdge hh : Polygon.getEdges())
							 {
								 if(!(((hh.getOrigin().getX() == hh.getTarget().getX() )&& (hh.getTarget().getX()== VisibilityVertex.getX())) || ((hh.getOrigin().getY() == hh.getTarget().getY() )&& (hh.getTarget().getY()== VisibilityVertex.getY()))))
								 {
									 if( Line2D.linesIntersect(VisibilityVertex.getX(),VisibilityVertex.getY(),h2.getOrigin().getX(),h2.getOrigin().getY(),hh.getOrigin().getX(),hh.getOrigin().getY(),hh.getTarget().getX(),hh.getTarget().getY()))
									 {
										 Flag= false;
										 break;
									 }
								 }
							 }
							
						 }
						   
					   }
					if(Flag)
					{
						h2=f.getIncident();
						Face fnew = new Face(Visible_region.getFaces().size());
						Face AuxFace = new Face(-3);
						int [] indexes = {0,0,0,0};
						int counterIndexes = 0;
						for(int i =0 ; i<4 ; i++)
						   {
								Vertex v1 = new Vertex(h2.getOrigin().getX(), h2.getOrigin().getY());
								Vertex v2 = new Vertex(h2.getTarget().getX(), h2.getTarget().getY());
								
								if(! Visible_region.getVertices().contains(v1))
								{
									Visible_region.addVertex(v1.getX(), v1.getY());
									if(! Visible_region.getVertices().contains(v2)) // the two vertices are new
									{
										Visible_region.addVertex(v2.getX(), v2.getY());
										
										Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-2), Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
										
										Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
										Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
										indexes[counterIndexes] = Visible_region.getEdges().size()-2;
										counterIndexes ++;
										
												
									}
									else{
										//indexes[] = Visible_region.getVertices().indexOf(v2);
										Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-1), Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
										
										Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
										Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
										indexes[counterIndexes] = Visible_region.getEdges().size()-2;
										counterIndexes ++;
										
									}
								}
								else{
									if(! Visible_region.getVertices().contains(v2))
									{
										Visible_region.addVertex(v2.getX(), v2.getY());
										
										Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
										Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
										Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
										indexes[counterIndexes] = Visible_region.getEdges().size()-2;
										counterIndexes ++;
										
										
										
									}
									else{  // the two vertices exist already
										boolean Flag2= true;
										for(HalfEdge he : Visible_region.getEdges()) // test if there is an edge between them already
										{
											if((he.getOrigin() == v1 && he.getTarget() == v2)&& he.getFace().getId() == -3)
											{
												he.setFace(fnew);
												indexes[counterIndexes] = Visible_region.getEdges().indexOf(he);
												counterIndexes ++;
												Flag2 = false;
												break;
												
											}
										}
										if(Flag2)
										{
											Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
										}
										
									}
								}
								
								
							
								h2=h2.getNext();
						   }
						fnew.setIncident(Visible_region.getEdges().get(indexes[0]));
						Visible_region.getEdges().get(indexes[0]).setNext(Visible_region.getEdges().get(indexes[1]));
						Visible_region.getEdges().get(indexes[0]).setPrev(Visible_region.getEdges().get(indexes[3]));
						
						Visible_region.getEdges().get(indexes[1]).setNext(Visible_region.getEdges().get(indexes[2]));
						Visible_region.getEdges().get(indexes[1]).setPrev(Visible_region.getEdges().get(indexes[0]));
						
						Visible_region.getEdges().get(indexes[2]).setNext(Visible_region.getEdges().get(indexes[3]));
						Visible_region.getEdges().get(indexes[2]).setPrev(Visible_region.getEdges().get(indexes[1]));
						
						Visible_region.getEdges().get(indexes[3]).setNext(Visible_region.getEdges().get(indexes[0]));
						Visible_region.getEdges().get(indexes[3]).setPrev(Visible_region.getEdges().get(indexes[2]));
						
						
					}
					break;
				case 3:
					Flag = true;
					HalfEdge h3 = f.getIncident();
					for(int i =0 ; i<4 ; i++)
					   {
						h3= h3.getNext();
						 if(!(h3.getOrigin().getX() <= VisibilityVertex.getX()) &&(h3.getOrigin().getY() <= VisibilityVertex.getY()))
						 {
							 Flag = false;
							 break;
						 }
						 else
						 {
							//check if the line intersects with the polygon
							 for(HalfEdge hh : Polygon.getEdges())
							 {
								 if(!(((hh.getOrigin().getX() == hh.getTarget().getX() )&& (hh.getTarget().getX()== VisibilityVertex.getX())) || ((hh.getOrigin().getY() == hh.getTarget().getY() )&& (hh.getTarget().getY()== VisibilityVertex.getY()))))
								 {
									 if( Line2D.linesIntersect(VisibilityVertex.getX(),VisibilityVertex.getY(),h3.getOrigin().getX(),h3.getOrigin().getY(),hh.getOrigin().getX(),hh.getOrigin().getY(),hh.getTarget().getX(),hh.getTarget().getY()))
									 {
										 Flag= false;
										 break;
									 }
								 }
							 }
							
						 }
						   
					   }
					if(Flag)
					{
						h3=f.getIncident();
						Face fnew = new Face(Visible_region.getFaces().size());
						Face AuxFace = new Face(-3);
						int [] indexes = {0,0,0,0};
						int counterIndexes = 0;
						for(int i =0 ; i<4 ; i++)
						   {
								Vertex v1 = new Vertex(h3.getOrigin().getX(), h3.getOrigin().getY());
								Vertex v2 = new Vertex(h3.getTarget().getX(), h3.getTarget().getY());
								
								if(! Visible_region.getVertices().contains(v1))
								{
									Visible_region.addVertex(v1.getX(), v1.getY());
									if(! Visible_region.getVertices().contains(v2)) // the two vertices are new
									{
										Visible_region.addVertex(v2.getX(), v2.getY());
										
										Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-2), Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
										
										Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
										Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
										indexes[counterIndexes] = Visible_region.getEdges().size()-2;
										counterIndexes ++;
										
												
									}
									else{
										//indexes[] = Visible_region.getVertices().indexOf(v2);
										Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-1), Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
										
										Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
										Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
										indexes[counterIndexes] = Visible_region.getEdges().size()-2;
										counterIndexes ++;
										
									}
								}
								else{
									if(! Visible_region.getVertices().contains(v2))
									{
										Visible_region.addVertex(v2.getX(), v2.getY());
										
										Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
										Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
										Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
										indexes[counterIndexes] = Visible_region.getEdges().size()-2;
										counterIndexes ++;
										
										
										
									}
									else{  // the two vertices exist already
										boolean Flag2= true;
										for(HalfEdge he : Visible_region.getEdges()) // test if there is an edge between them already
										{
											if((he.getOrigin() == v1 && he.getTarget() == v2)&& he.getFace().getId() == -3)
											{
												he.setFace(fnew);
												indexes[counterIndexes] = Visible_region.getEdges().indexOf(he);
												counterIndexes ++;
												Flag2 = false;
												break;
												
											}
										}
										if(Flag2)
										{
											Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
										}
										
									}
								}
								
								
							
								h3=h3.getNext();
						   }
						fnew.setIncident(Visible_region.getEdges().get(indexes[0]));
						Visible_region.getEdges().get(indexes[0]).setNext(Visible_region.getEdges().get(indexes[1]));
						Visible_region.getEdges().get(indexes[0]).setPrev(Visible_region.getEdges().get(indexes[3]));
						
						Visible_region.getEdges().get(indexes[1]).setNext(Visible_region.getEdges().get(indexes[2]));
						Visible_region.getEdges().get(indexes[1]).setPrev(Visible_region.getEdges().get(indexes[0]));
						
						Visible_region.getEdges().get(indexes[2]).setNext(Visible_region.getEdges().get(indexes[3]));
						Visible_region.getEdges().get(indexes[2]).setPrev(Visible_region.getEdges().get(indexes[1]));
						
						Visible_region.getEdges().get(indexes[3]).setNext(Visible_region.getEdges().get(indexes[0]));
						Visible_region.getEdges().get(indexes[3]).setPrev(Visible_region.getEdges().get(indexes[2]));
						
						
					}
					break;
				case 4:
					Flag = true;
					HalfEdge h4 = f.getIncident();
					for(int i =0 ; i<4 ; i++)
					   {
						h4= h4.getNext();
						 if(!(h4.getOrigin().getX() >= VisibilityVertex.getX()) &&(h4.getOrigin().getY() <= VisibilityVertex.getY()))
						 {
							 Flag = false;
							 break;
						 }
						 else
						 {//check if the line intersects with the polygon
							 for(HalfEdge hh : Polygon.getEdges())
							 {
								 if(!(((hh.getOrigin().getX() == hh.getTarget().getX() )&& (hh.getTarget().getX()== VisibilityVertex.getX())) || ((hh.getOrigin().getY() == hh.getTarget().getY() )&& (hh.getTarget().getY()== VisibilityVertex.getY()))))
								 {
									 if( Line2D.linesIntersect(VisibilityVertex.getX(),VisibilityVertex.getY(),h4.getOrigin().getX(),h4.getOrigin().getY(),hh.getOrigin().getX(),hh.getOrigin().getY(),hh.getTarget().getX(),hh.getTarget().getY()))
									 {
										 Flag= false;
										 break;
									 }
								 }
							 }
							
						 }
						   
					   }
					if(Flag)
					{
						h4=f.getIncident();
						Face fnew = new Face(Visible_region.getFaces().size());
						Face AuxFace = new Face(-3);
						int [] indexes = {0,0,0,0};
						int counterIndexes = 0;
						for(int i =0 ; i<4 ; i++)
						   {
								Vertex v1 = new Vertex(h4.getOrigin().getX(), h4.getOrigin().getY());
								Vertex v2 = new Vertex(h4.getTarget().getX(), h4.getTarget().getY());
								
								if(! Visible_region.getVertices().contains(v1))
								{
									Visible_region.addVertex(v1.getX(), v1.getY());
									if(! Visible_region.getVertices().contains(v2)) // the two vertices are new
									{
										Visible_region.addVertex(v2.getX(), v2.getY());
										
										Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-2), Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
										
										Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
										Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
										indexes[counterIndexes] = Visible_region.getEdges().size()-2;
										counterIndexes ++;
										
												
									}
									else{
										//indexes[] = Visible_region.getVertices().indexOf(v2);
										Visible_region.addEdge(Visible_region.getVertices().get(Visible_region.getVertices().size()-1), Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
										
										Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
										Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
										indexes[counterIndexes] = Visible_region.getEdges().size()-2;
										counterIndexes ++;
										
									}
								}
								else{
									if(! Visible_region.getVertices().contains(v2))
									{
										Visible_region.addVertex(v2.getX(), v2.getY());
										
										Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().size()-1));
										Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
										Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
										indexes[counterIndexes] = Visible_region.getEdges().size()-2;
										counterIndexes ++;
										
										
										
									}
									else{  // the two vertices exist already
										boolean Flag2= true;
										for(HalfEdge he : Visible_region.getEdges()) // test if there is an edge between them already
										{
											if((he.getOrigin() == v1 && he.getTarget() == v2)&& he.getFace().getId() == -3)
											{
												he.setFace(fnew);
												indexes[counterIndexes] = Visible_region.getEdges().indexOf(he);
												counterIndexes ++;
												Flag2 = false;
												break;
												
											}
										}
										if(Flag2)
										{
											Visible_region.addEdge( Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v1)),Visible_region.getVertices().get(Visible_region.getVertices().indexOf(v2)));
											Visible_region.getEdges().get(Visible_region.getEdges().size()-2).setFace(fnew);
											Visible_region.getEdges().get(Visible_region.getEdges().size()-1).setFace(AuxFace);
											indexes[counterIndexes] = Visible_region.getEdges().size()-2;
											counterIndexes ++;
										}
										
									}
								}
								
								
							
								h4=h4.getNext();
						   }
						fnew.setIncident(Visible_region.getEdges().get(indexes[0]));
						Visible_region.getEdges().get(indexes[0]).setNext(Visible_region.getEdges().get(indexes[1]));
						Visible_region.getEdges().get(indexes[0]).setPrev(Visible_region.getEdges().get(indexes[3]));
						
						Visible_region.getEdges().get(indexes[1]).setNext(Visible_region.getEdges().get(indexes[2]));
						Visible_region.getEdges().get(indexes[1]).setPrev(Visible_region.getEdges().get(indexes[0]));
						
						Visible_region.getEdges().get(indexes[2]).setNext(Visible_region.getEdges().get(indexes[3]));
						Visible_region.getEdges().get(indexes[2]).setPrev(Visible_region.getEdges().get(indexes[1]));
						
						Visible_region.getEdges().get(indexes[3]).setNext(Visible_region.getEdges().get(indexes[0]));
						Visible_region.getEdges().get(indexes[3]).setPrev(Visible_region.getEdges().get(indexes[2]));
						
						
					}
					break;
				default:
					break;
				}
				  
			   }
			   
			   
			   
		   }
		     
	   }
	   return  Visible_region;
   }

   public static void handleEventq1(DCEL polygon, HalfEdge event, HalfEdge toDivide, boolean wasOrigin, boolean isBackwards)
   {
		polygon.addVertex(toDivide.getOrigin().getX(), event.getOrigin().getY());

		if(isBackwards){
			if(wasOrigin)
			{
				polygon.addEdge(event.getOrigin(), polygon.getVertices().get(polygon.getVertices().size() - 1));
				//polygon.getVertices().get(polygon.getVertices().size() - 1).setIncident(polygon.getEdges().get(polygon.getEdges().size() - 2));
				polygon.getEdges().get(polygon.getEdges().size() - 2).setPrev(event.getPrev());
				polygon.getEdges().get(polygon.getEdges().size() - 1).setNext(event);
				polygon.divideEdge(toDivide, polygon.getEdges().get(polygon.getEdges().size() - 2), 
						polygon.getVertices().get(polygon.getVertices().size() - 1), true);
			}
			else
			{
				polygon.addEdge(polygon.getVertices().get(polygon.getVertices().size() - 1), event.getTarget());
				//polygon.getVertices().get(polygon.getVertices().size() - 1).setIncident(polygon.getEdges().get(polygon.getEdges().size() - 2));
				polygon.getEdges().get(polygon.getEdges().size() - 2).setNext(event.getNext());
				polygon.getEdges().get(polygon.getEdges().size() - 1).setPrev(event);
				polygon.divideEdge(toDivide, polygon.getEdges().get(polygon.getEdges().size() - 2), 
						polygon.getVertices().get(polygon.getVertices().size() - 1), false);
			}
		}
		else
		{
			if(wasOrigin)
			{
				polygon.addEdge(polygon.getVertices().get(polygon.getVertices().size() - 1), event.getOrigin());
				//polygon.getVertices().get(polygon.getVertices().size() - 1).setIncident(polygon.getEdges().get(polygon.getEdges().size() - 2));
				polygon.getEdges().get(polygon.getEdges().size() - 2).setNext(event);
				polygon.getEdges().get(polygon.getEdges().size() - 1).setPrev(event.getPrev());
				polygon.divideEdge(toDivide, polygon.getEdges().get(polygon.getEdges().size() - 2), 
						polygon.getVertices().get(polygon.getVertices().size() - 1), false);
			}
			else
			{
				polygon.addEdge(event.getTarget(), polygon.getVertices().get(polygon.getVertices().size() - 1));
				//polygon.getVertices().get(polygon.getVertices().size() - 1).setIncident(polygon.getEdges().get(polygon.getEdges().size() - 2));
				polygon.getEdges().get(polygon.getEdges().size() - 2).setPrev(event);
				polygon.getEdges().get(polygon.getEdges().size() - 1).setNext(event.getNext());
				polygon.divideEdge(toDivide, polygon.getEdges().get(polygon.getEdges().size() - 2), 
						polygon.getVertices().get(polygon.getVertices().size() - 1), true);
			}
		}
   }

}
