package linearEquationsSystem;

import matrix.SquareMatrix;
import matrix.Vector;

public class LUDecomposition {

	//LU factorization algorithm to solve the linear equations systems
	
		//the linear equations system's size, not including the vector rightside.
		private int Size;
		//The coefficient Matrix decomposed to the lower matrix(L) and the upper matrix(U).
		//Both of the triangular matrix's members store in a matrix(n*n).
		private SquareMatrix CoefficientMatrix;
		private Vector ConstantVector;
		private Vector Solution;
		
		
		//constructor
		public LUDecomposition(int size) {
			Size=size;
			//allocate the memory for the matrix
			CoefficientMatrix=new SquareMatrix(Size);
			ConstantVector=new Vector(Size);
			Solution=new Vector(Size);
		}
		
		public LUDecomposition(double[][] m, double[] ConstantTerm){
			if(m.length!=m[0].length){
				System.out.println("The coefficient matrix is not a square matrix.");
				System.exit(0);
			}
			if(m.length!=ConstantTerm.length){
				System.out.println("The dimension of the coefficient matrix does not match the constant vector's.");
				System.exit(0);
			}
			Size=m.length;
			CoefficientMatrix=new SquareMatrix(m);
			ConstantVector=new Vector(ConstantTerm);
			Solution=new Vector(Size);
		}
		
		public double[] GetSolution(){
			SquareMatrix[] LUP=CoefficientMatrix.LUPDecomposition();
			ConstantVector=LUP[2].MultiplyVector(ConstantVector);
			//LUX=b;UX=Y;LY=b;
			//solve the Y firstly.
			Solution.SetElementValue(0, ConstantVector.GetElementValue(0));
			for(int row=1; row<Size; row++){
				double temp=0.0;
				for(int k=0; k<=row-1; k++){
					if(row==k){
						temp+=Solution.GetElementValue(k);
					}
					else{
						temp+=LUP[0].GetElementValue(row, k)*Solution.GetElementValue(k);
					}
				}
				Solution.SetElementValue(row, ConstantVector.GetElementValue(row)-temp);
			}
			//UX=Y
			//solve x
			Solution.SetElementValue(Size-1, Solution.GetElementValue(Size-1)
					/LUP[1].GetElementValue(Size-1, Size-1));
			for(int row=Size-2; row>=0; row--){
				double temp=0.0;
				for(int k=row+1; k<Size; k++){
					temp+=LUP[1].GetElementValue(row, k)*Solution.GetElementValue(k);
				}
				Solution.SetElementValue(row, 
						(Solution.GetElementValue(row)-temp)/LUP[1].GetElementValue(row, row));
			}
			return Solution.ToArray();
		}	
	
	public static void main(String[] args) {
		//Test
		/*double[][] A={
				{0.2388, 0.2471, 0.2568, 1.2671},
				{0.1968, 0.2071, 1.2168, 0.2271},
				{0.1581, 1.1675, 0.1768, 0.1871},
				{1.1161, 0.1254, 0.1397, 0.1490}};
		double[] B={1.8471, 1.7471, 1.6471, 1.5471};*/
		double[][] A={
				{4 ,-1,   1},
				{-1,4.25,2.75},
				{1 ,2.75,3.5}
		};
		double[] B={6,-0.5,1.25};
		//double[] X=new double[size];
		LUDecomposition LU=new LUDecomposition(A, B);
		//GE.ToUpperTriangularMatrix();
		double[] X=LU.GetSolution();
		//print the result
		for(int i=0; i<A.length; i++){
			for(int j=0; j<A[0].length; j++){
				System.out.print(A[i][j]+"x"+j);
				if(j==A[0].length-1){
					System.out.print("=");
				}
				else{
					System.out.print("+");
				}
			}
			System.out.println(B[i]);
		}
		System.out.println("The results are:");
		for(int i=0; i<A.length; i++){
			System.out.print("x"+i+"=");
			System.out.printf("%10.5f",X[i]);
			System.out.println(";");
		}
		//System.out.println("Solving equations costing time:"+(FinishTime-StartTime)/1000+" seconds");
	}
	

}
