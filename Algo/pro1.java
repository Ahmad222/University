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

public class pro1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FastScanner scan = new FastScanner();
		//System.out.println("Enter the number of levels");
		int numelements = scan.nextInt();
		long elements[][] = new long [numelements][numelements];
		
		//System.out.println("Enter the number of missing stones");
		//instead of identi
		int nummissings = scan.nextInt();
		//boolean missings[][] = new boolean [numelements][numelements];
		
		for(int i=0;i<numelements;i++)
		{
		
			for(int j=0;j<numelements;j++)
			{
				if(i==0)
				{
					elements[i][j] = 1;
				
				}
				else
					elements[i][j] = -1;
					
			}
		}
		for(int i=0;i<nummissings;i++)
		{
			
			int ii = scan.nextInt();
			int jj = scan.nextInt();
			//missings[ii -1][jj-1] = true;
			elements[ii-1][jj-1] = 0;
			//System.out.println("Enter the number of missing stones");
		}
		
		for(int i=1;i<numelements;i++)
		{
			for(int j=0;j<numelements-i;j++)
			{
				if(elements[i][j] != 0)
				{
					elements[i][j] = elements[i-1][j] + elements[i-1][j+1]; 
				}
				
					
				
			}
			
		}
		
		
		
		System.out.println(elements[numelements-1][0]);
		//return elements[numelements][0];

	}

}
