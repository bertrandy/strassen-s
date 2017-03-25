import java.io.*;

public class Matrix {	
	/*
	 * contains a 2d array and its dimension
	 */
	int matrix[][];	
	int dimension;
	
	/*
	 * constructor
	 */
	public Matrix(){}
	public Matrix(int d){
		matrix = new int [d][d];
		dimension = d;
	}
			
	/*
	 * addition
	 */
	public Matrix Add(Matrix matrix2){
		if((this.dimension != matrix2.dimension)){
			return null;
		}
		Matrix result = new Matrix(dimension);			
		for(int i=0; i<dimension; i++){
			for(int j=0; j<dimension; j++){ 
				result.matrix[i][j] = this.matrix[i][j] + matrix2.matrix[i][j];
			}
		}
		return result;
	}
	
	/*
	 * subtraction
	 */
	public Matrix Substract(Matrix matrix2){
		if(this.dimension != matrix2.dimension){
			return null;
		}
		Matrix result = new Matrix(dimension);			
		for(int i=0; i<dimension; i++){
			for(int j=0; j<dimension; j++){ 
				result.matrix[i][j] = this.matrix[i][j] - matrix2.matrix[i][j];
			}
		}
		return result;
	}
	
	/*
	 * conventional multiplication
	 */
	public Matrix conventionalMultiply(Matrix matrix2){
		if(this.dimension != matrix2.dimension){
			return null;
		}
		Matrix result = new Matrix(dimension);			
		for(int i=0; i<dimension; i++){
			//loop k first and then j(column) to save time.			
			for (int k=0; k<dimension; k++){
				int temp = this.matrix[i][k];
				for(int j=0; j<dimension; j++){ 	
					result.matrix[i][j] += temp * matrix2.matrix[k][j];
				}
			}				
		}				
		return result;
	}	
	
	/* 
	 * strassen's algorithm
	 */
	public static Matrix strassensAlgorithm(Matrix matrix1,Matrix matrix2,int n0){
		//check if the input matrices satisfy our requirement
		if((matrix1.dimension != matrix2.dimension)){
			return null;
		}
		
		//create a new Matrix of the same size to store the result
		int dimension = matrix1.dimension;
		Matrix result = new Matrix(dimension);	
		
		//check if the dimensions satisfy our requirement
		if(dimension < 1){
			return null;
		}
		//base case
		else if (dimension < n0){
			return matrix1.conventionalMultiply(matrix2);
		}
		//recursion	
		else{
			//split the matrices into 4 parts respectively			
			Matrix fst[] = matrix1.splitMatrix();			
			Matrix snd[] = matrix2.splitMatrix();
			matrix1 = null;//set previous matrices to null to save space
			matrix2 = null;			
			Matrix a1 = fst[0];
			Matrix b1 = fst[1];
			Matrix c1 = fst[2];
			Matrix d1 = fst[3];
			Matrix a2 = snd[0];
			Matrix b2 = snd[1];
			Matrix c2 = snd[2];
			Matrix d2 = snd[3];			
			fst = null;
			snd = null;
			
			//compute p1 to p7
			Matrix p1 = Matrix.strassensAlgorithm(a1,b2.Substract(d2),n0);
			Matrix p2 = Matrix.strassensAlgorithm(a1.Add(b1),d2,n0);
			Matrix p3 = Matrix.strassensAlgorithm(c1.Add(d1),a2,n0);
			Matrix p4 = Matrix.strassensAlgorithm(d1,c2.Substract(a2),n0);
			Matrix p5 = Matrix.strassensAlgorithm(a1.Add(d1),a2.Add(d2),n0);
			Matrix p6 = Matrix.strassensAlgorithm(b1.Substract(d1),c2.Add(d2),n0);
			Matrix p7 = Matrix.strassensAlgorithm(a1.Substract(c1),a2.Add(b2),n0);
			
			//compute the four parts
			Matrix m1 = p5.Add(p4).Substract(p2).Add(p6);
			Matrix m2 = p1.Add(p2);
			Matrix m3 = p3.Add(p4);
			Matrix m4 = p5.Add(p1).Substract(p3).Substract(p7);					
			
			//check if the matrix requires padding
			int status;
			if (dimension%2 == 0) status = 0;
			else status = 1;			
			
			//merge the four parts into one matrix
			result = Matrix.mergeMatrix(m1,m2,m3,m4,status);	
			return result;
		}				
	}
	/*
	 * merge 4 matrices into 1. Remove padding if needed.
	 */
	public static Matrix mergeMatrix(Matrix a, Matrix b, Matrix c, Matrix d, int status){
		//check if the dimension and status satisfy our requirement
		if(!(a.dimension == b.dimension && a.dimension == c.dimension && a.dimension == d.dimension)){
			return null;
		}
		if(!(status == 0) && !(status == 1)){
			return null;
		}
		
		//create a new Matrix to store the result
		Matrix result;
		int dimension = a.dimension;
		
		//if status is 0 then we do not need to remove padding
		if(status == 0){
			result = new Matrix(dimension*2);	
			
			for(int i=0;i<dimension;i++){
				for(int j=0;j<dimension;j++){
					result.matrix[i][j] = a.matrix[i][j];
				}
			}	
			for(int i=0;i<dimension;i++){
				for(int j=0;j<dimension;j++){
					result.matrix[i][j+dimension] = b.matrix[i][j];
				}
			}	
			for(int i=0;i<dimension;i++){
				for(int j=0;j<dimension;j++){
					result.matrix[i+dimension][j] = c.matrix[i][j];
				}
			}	
			for(int i=0;i<dimension;i++){
				for(int j=0;j<dimension;j++){
					result.matrix[i+dimension][j+dimension] = d.matrix[i][j];
				}
			}	
		}
		//if status is 1 then we need to remove padding
		else{
			result = new Matrix(dimension*2 - 1);	
			
			for(int i=0;i<dimension;i++){
				for(int j=0;j<dimension;j++){
					result.matrix[i][j] = a.matrix[i][j];
				}
			}	
			for(int i=0;i<dimension;i++){
				for(int j=0;j<dimension-1;j++){
					result.matrix[i][j+dimension] = b.matrix[i][j];
				}
			}	
			for(int i=0;i<dimension-1;i++){
				for(int j=0;j<dimension;j++){
					result.matrix[i+dimension][j] = c.matrix[i][j];
				}
			}	
			for(int i=0;i<dimension-1;i++){
				for(int j=0;j<dimension-1;j++){
					result.matrix[i+dimension][j+dimension] = d.matrix[i][j];
				}
			}	
		}
		
		return result;		
	}	
	
	/*
	 * split one matrix into 4 parts, add padding if needed
	 */
	public Matrix[] splitMatrix(){
		//create 4 new matrices to store the result
		Matrix[] result = new Matrix[4];		
		int subdim = (dimension+1)/2;		
		Matrix a = new Matrix(subdim);
		Matrix b = new Matrix(subdim);
		Matrix c = new Matrix(subdim);
		Matrix d = new Matrix(subdim);
		
		//a
		for(int i=0;i<subdim;i++){
			for(int j=0;j<subdim;j++){
				a.matrix[i][j] = this.matrix[i][j];
			}
		}		
		//no padding
		if(dimension % 2 == 0){
			//b
			for(int i=0;i<subdim;i++){
				for(int j=0;j<subdim;j++){
					b.matrix[i][j] = this.matrix[i][j+subdim];
				}
			}
			//c
			for(int i=0;i<subdim;i++){
				for(int j=0;j<subdim;j++){
					c.matrix[i][j] = this.matrix[i+subdim][j];
				}
			}
			//d
			for(int i=0;i<subdim;i++){
				for(int j=0;j<subdim;j++){
					d.matrix[i][j] = this.matrix[i+subdim][j+subdim];
				}
			}				
		}	
		//padding
		else{	
			//b
			for(int i=0;i<subdim;i++){
				for(int j=0;j<subdim-1;j++){
					b.matrix[i][j] = this.matrix[i][j+subdim];
				}
				b.matrix[i][subdim-1] = 0;
			}
			//c
			for(int i=0;i<subdim-1;i++){
				for(int j=0;j<subdim;j++){
					c.matrix[i][j] = this.matrix[i+subdim][j];
				}
			}
			for(int j=0;j<subdim;j++){
				c.matrix[subdim-1][j] = 0;
			}
			//d
			for(int i=0;i<subdim-1;i++){
				for(int j=0;j<subdim-1;j++){
					d.matrix[i][j] = this.matrix[i+subdim][j+subdim];
				}
				d.matrix[i][subdim-1] = 0;
			}
			for(int j=0;j<subdim;j++){
				d.matrix[subdim-1][j] = 0;
			}
		}				
		result[0] = a;
		result[1] = b;
		result[2] = c;
		result[3] = d;
		return result;
	}
	
		
	public static void main(String args[]) throws IOException{
		//take command line arguments
		int dimension = Integer.parseInt(args[1]);
		String inputFile = args[2];	
		
		//initialize matrices
        Matrix matrix1 = new Matrix(dimension);
        Matrix matrix2 = new Matrix(dimension);		
		BufferedReader in = new BufferedReader(new FileReader(inputFile));		
		String line;				
		for(int i=0; i<dimension; i++){
			for(int j=0; j<dimension; j++){ 
				line = in.readLine();
				matrix1.matrix[i][j] = Integer.parseInt(line);				
			}
		}
		for(int i=0; i<dimension; i++){
			for(int j=0; j<dimension; j++){ 
				line = in.readLine();
				matrix2.matrix[i][j] = Integer.parseInt(line);				
			}
		}		
		in.close(); 
		
		
		//compute with strassen's algorithm
		int n0 = 80;
		Matrix result = new Matrix();
		result = Matrix.strassensAlgorithm(matrix1,matrix2,n0); 
		
		for (int i = 0; i < dimension; i++){
			for (int j = 0; j < dimension; j++){
				System.out.println(result.matrix[i][j]);
			}
		}
			
		/*
		//test
        	//compute conventional
		long startMili1=System.currentTimeMillis();	
		matrix1.conventionalMultiply(matrix2);		   
		long endMili1=System.currentTimeMillis();
		System.out.println("conventional" + (endMili1-startMili1));	        		
		
		//compute Strassen¡¯s		
		int times = 10;
		for(int n0 = 20; n0 < 768; n0 += 5 ){
			long startMili2=System.currentTimeMillis();			
			for(int i = 0; i < times; i ++){
				Matrix.strassensAlgorithm(matrix1,matrix2,n0); 
			}			    			                          
			long endMili2=System.currentTimeMillis();
			System.out.println((endMili2-startMili2)/times);	
		}      
		*/
	
	}
}
