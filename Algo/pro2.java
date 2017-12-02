import java.util.*;
import java.io.*;


class FastScanner {
    BufferedReader br;
    StringTokenizer st;

    FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));        
    }

    String next() {
        while (st == null || !st.hasMoreTokens()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return st.nextToken();
    }

    int nextInt() {
        return Integer.parseInt(next());
    }
}
public class pro2 {

	/**
	 * @param args
	 */
	
	static int max(int [] arr,int from, int to)
	{
		//System.out.println("Max");
		int maxx = 0;
		int loc = -1;
		for(int j=from; j < to; j++)
		{
			if(arr[j]> maxx)
			{
				maxx = arr[j];
				loc = j;
			}
				
		}
		//System.out.println("Maxloc =  " + loc);
		return loc;
	}
	static int min(int [] arr,int from, int to)
	{
		//System.out.println("Min");
		int minn = Integer.MAX_VALUE;
		int loc = -1;
		for(int j=from; j<to; j++)
		{
			if(arr[j] < minn)
			{
				minn = arr[j];
				loc = j;
			}
				
		}
		//System.out.println("Min loc =  " + loc);
		return loc;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FastScanner scan = new FastScanner();
		
		int max_jump = scan.nextInt();
		int num_stones = scan.nextInt();
		int values[] = new int [num_stones];
		//System.out.println("Enter values");
		long sum=0;
		long sum2=0;
		
		
		for(int i=0;i<num_stones;i++)
		{
		
			values[i] = scan.nextInt();
			
		}
		
		//System.out.println("finish values");
			int loc=-1;
			int i=0;
			
			boolean flag = false;
			
		while(i<num_stones)
		{
			
			//System.out.println("enter while");
			
			// right foot
			if(i+max_jump>=num_stones)
			{
				flag= false;
				for(int m=i;m < num_stones-1 ;m++)
				{
					if(values[m]> values[m+1])
					{
						loc = m;
						flag=true;
						break;
						
						
					}
				   
				}
				if(!flag)
					loc=num_stones-1;
			}
			else
			{
				for(int m=i;m< i+max_jump;m++)
				{
					if(values[m]>values[m+1])
					{
						loc = m;
						break;
					}
				   loc= m;
				   
				   
				}
			}
			sum += values[loc];
			i=loc+1;
			//System.out.println("sum : " + sum);
			//left foot
			if(i+max_jump>=num_stones)
			{
				flag = false;
				for(int m=i;m < num_stones-1 ;m++)
				{
					if(values[m]<values[m+1])
					{
						loc = m;
						flag=true;
						break;
						
						
					}
				   
				}
				//we need to do a step
				if(!flag && i+max_jump==num_stones)
					{
						int dist = values[i] - values[i+1];
						loc = i;
						for(int m=i;m < num_stones-1 ;m++)
						{
							if((values[m]-values[m+1]) < dist)
							{
								loc = m;
								dist = (values[m]-values[m+1]);
								
								
							}
						   
						}
					
					}
				// we can skip
				if(!flag && i+max_jump > num_stones)
				{
					break;
				
				}
			}
			else
			{
					for(int m=i;m< i+max_jump;m++)
					{
						if(values[m]<values[m+1])
						{	loc = m;
							break;
						}
					 loc= m;
							   
							   
					}
			}
			sum -= values[loc];
			i=loc+1;
			
			//System.out.println("sum : " + sum);
		}
			
		i=0;
		while(i<num_stones)
		{
			
			//System.out.println("enter while");
			//left foot
			if(i+max_jump>=num_stones)
			{
				flag = false;
				for(int m=i;m < num_stones-1 ;m++)
				{
					if(values[m]<values[m+1])
					{
						loc = m;
						flag=true;
						break;
						
						
					}
				   
				}
				
				//we need to do a step
					if(!flag && i+max_jump==num_stones)
						{
							if(i+1 == num_stones)
							{
								loc=i;
							}
							else
							{
								int dist = values[i] - values[i+1];
								loc = i;
								for(int m=i;m < num_stones-1 ;m++)
								{
									if((values[m]-values[m+1]) < dist)
									{
										loc = m;
										dist = (values[m]-values[m+1]);
										
										
									}
								   
								}

								
							}
													
						}
					// we can skip
					if(!flag && i+max_jump > num_stones)
					{
						break;
					
					}
					
			}
			else
			{
					for(int m=i;m< i+max_jump;m++)
					{
						if(values[m]<values[m+1])
						{	loc = m;
							break;
						}
					 loc= m;
							   
							   
					}
			}
			sum2 -= values[loc];
			i=loc+1;
			//System.out.println("sum2 : " + sum2);
			// right foot
			if(i+max_jump>=num_stones)
			{
				flag= false;
				for(int m=i;m < num_stones-1 ;m++)
				{
					if(values[m]> values[m+1])
					{
						loc = m;
						flag=true;
						break;
						
						
					}
				   
				}
				if(!flag)
					loc=num_stones-1;
			}
			else
			{
				for(int m=i;m< i+max_jump;m++)
				{
					if(values[m]>values[m+1])
					{
						loc = m;
						break;
					}
				   loc= m;
				   
				   
				}
			}
			sum2 += values[loc];
			i=loc+1;
			//System.out.println("sum2 : " + sum2);
			
			
		}

		if(sum2>sum)
			sum = sum2;
		//System.out.println("Final sum : " + sum);
		System.out.println(sum);
		

	}

}
