package linearEquationsSystem;

import matrix.Matrix;
import matrix.SquareMatrix;
import matrix.Vector;

public class CholeskyDecomposition {
	
	//Cholesky decomposition algorithm only suit to solve the linear equations system,
	//which the coefficient matrix positive-definite.
	
	//The size of the coefficient Matrix
	private int Size;
	private SquareMatrix CoefficientMatrix;
	private Vector ConstantVector;
	private Vector Solution;
	
	public CholeskyDecomposition(int size) {
		Size=size;
		//allocate the memory for the matrix
		CoefficientMatrix=new SquareMatrix(Size);
		ConstantVector=new Vector(Size);
		Solution=new Vector(Size);
	}
	
	public CholeskyDecomposition(double[][] m, double[] ConstantTerm){
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
		SquareMatrix[] LD=CoefficientMatrix.CholeskyDecomposition();
		Matrix upperM=LD[0].Transpose();
		Matrix lowerM=new Matrix(Size, Size);
		for(int j=0; j<Size; j++){
			for(int i=j; i<Size; i++){
				lowerM.SetElementValue(i, j, LD[0].GetElementValue(i, j)*LD[1].GetElementValue(j, j));
			}
		}
		//LUX=b;UX=Y;LY=b;
		//solve the Y firstly.
		Solution.SetElementValue(0, ConstantVector.GetElementValue(0)/lowerM.GetElementValue(0, 0));
		for(int row=1; row<Size; row++){
			double temp=0.0;
			for(int k=0; k<=row-1; k++){
				temp+=lowerM.GetElementValue(row, k)*Solution.GetElementValue(k);
			}
			Solution.SetElementValue(row, 
					(ConstantVector.GetElementValue(row)-temp)/lowerM.GetElementValue(row, row));
		}
		//UX=Y
		//solve x
		Solution.SetElementValue(Size-1, Solution.GetElementValue(Size-1)
				/upperM.GetElementValue(Size-1, Size-1));
		for(int row=Size-2; row>=0; row--){
			double temp=0.0;
			for(int k=row+1; k<Size; k++){
				temp+=upperM.GetElementValue(row, k)*Solution.GetElementValue(k);
			}
			Solution.SetElementValue(row, 
					(Solution.GetElementValue(row)-temp)/upperM.GetElementValue(row, row));
		}
		return Solution.ToArray();
	}

	public static void main(String[] args) {
		// Test
		double[][] A={
				{4 ,-1,   1},
				{-1,4.25,2.75},
				{1 ,2.75,3.5}
		};
		double[] B={6,-0.5,1.25};
		CholeskyDecomposition C=new CholeskyDecomposition(A, B);
		double[] X=C.GetSolution();
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
	}

	

}
