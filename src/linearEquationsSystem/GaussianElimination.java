package linearEquationsSystem;

import matrix.Matrix;
import matrix.Vector;

public class GaussianElimination {

	//The Gaussian Elimination algorithm to solve the linear equations systems
	//高斯消元法求解线性方程组（主消元法）
	private int Row;//The rows of the coefficient matrix
	private int Column;//The columns of the coefficient matrix
	private Matrix CoefficientMatrix;//The Coefficient matrix of the linear equations system.
	private Vector ConstantVector;//The constant term of the linear equations system.
	private int[] Order;//Mark down the order of the element of solution vector when exchanging the rows or the columns.
	private Vector Solution;//The solution of the linear equations system.
	
	public GaussianElimination(int row, int col) {
		Row=row;
		Column=col;
		CoefficientMatrix=new Matrix(Row, Column);
		ConstantVector=new Vector(Row);
		Solution=new Vector(Row);
		Order=new int[Row];
		for(int i=0; i<Row; i++){
			Order[i]=i;
		}
	}
	
	public GaussianElimination(double[][] CoeMatrix, double[] ConstantTerm){
		Row=CoeMatrix.length;
		Column=CoeMatrix[0].length;
		CoefficientMatrix=new Matrix(Row, Column);
		ConstantVector=new Vector(Row);
		Solution=new Vector(Row);
		Order=new int[Row];
		for(int i=0; i<Row; i++){
			for(int j=0; j<Column; j++){
				CoefficientMatrix.SetElementValue(i, j, CoeMatrix[i][j]);
			}
			ConstantVector.SetElementValue(i, ConstantTerm[i]);
			Order[i]=i;
		}
	}
	
	//Check the coefficient Matrix valid or not
	/*private boolean CheckMatrix(){
		boolean isValid=true;
		
		return isValid;
	}*/
	
	private void SelectLeadingCoefficientFromColumn(int CurrentRow){
		//select the max leading coefficient(aii) from the column.
		//aii, row=column
		int MaxValueRow=CurrentRow;
		//search the max value from the matrix(n-k+1).
		double MaxValue=Math.abs(CoefficientMatrix.GetElementValue(CurrentRow, CurrentRow));
		for(int i=CurrentRow; i<Row; i++){
			if(MaxValue<Math.abs(CoefficientMatrix.GetElementValue(i, CurrentRow))){
				MaxValue=Math.abs(CoefficientMatrix.GetElementValue(i, CurrentRow));
				MaxValueRow=i;
			}
		}
		//exchange the rows.
		if(MaxValueRow!=CurrentRow){
			for(int j=CurrentRow; j<Column; j++){
				CoefficientMatrix.ExchangeElements(CurrentRow, j, MaxValueRow, j);
			}
			ConstantVector.ExchangeElements(CurrentRow, MaxValueRow);
		}
	}
	
	private void SelectLeadingCoefficient(int CurrentRow){
		//select the max leading coefficient(aii).
		//aii, row=column
		int CurrentColumn=CurrentRow;
		int MaxValueRow=CurrentRow;
		int MaxValueColumn=CurrentRow;
		//search the max value from the matrix(n-k+1).
		double MaxValue=Math.abs(CoefficientMatrix.GetElementValue(CurrentRow, CurrentColumn));
		for(int i=CurrentRow; i<Row; i++){
			for(int j=CurrentColumn; j<Column; j++){
				if(MaxValue<Math.abs(CoefficientMatrix.GetElementValue(i, j))){
					MaxValue=Math.abs(CoefficientMatrix.GetElementValue(i, j));
					MaxValueRow=i;
					MaxValueColumn=j;
				}
			}
		}
		//exchange the rows.
		if(MaxValueRow!=CurrentRow){
			for(int j=CurrentRow; j<Column; j++){
				CoefficientMatrix.ExchangeElements(CurrentRow, j, MaxValueRow, j);
			}
			ConstantVector.ExchangeElements(CurrentRow, MaxValueRow);
		}
		//exchange the columns.
		if(MaxValueColumn!=CurrentColumn){
			CoefficientMatrix.ExchangeColumns(CurrentColumn, MaxValueColumn);
			int pos1=-1;
			int pos2=-1;
			for(int i=0; i<Row; i++){
				if(Order[i]==CurrentColumn){
					pos1=i;continue;
				}
				else if(Order[i]==MaxValueColumn){
					pos2=i;continue;
				}
			}
			try{
				int temp=Order[pos1];Order[pos1]=Order[pos2];Order[pos2]=temp;
			}
			catch(IndexOutOfBoundsException ie){
				System.out.println("Exchanging matrix's columns failed.");System.exit(0);
			}
			//ConstantVector.ExchangeElements(CurrentRow, MaxValueRow);
		}
	}
	
	public double[][] ToUpperTriangularMatrix(){
		double[][] result=ToUpperTriangularMatrix("all");
		return result;
	}
	
	public double[][] ToUpperTriangularMatrix(String method){
		//The Gaussian Elimination algorithm, which transform a matrix to the upper triangular matrix. 
		//Check Data
		//CheckData();
		//aii!=0
		//transform the coefficient matrix to the upper triangular matrix.
		for(int j=0; j<Math.min(Row, Column); j++){
			//The Gaussian Elimination algorithm performs better and stable, when the leading coefficient of the matrix (aii) reaches max.
			//select the max leading coefficient.
			if(method.toLowerCase().equals("parital")){
				SelectLeadingCoefficientFromColumn(j);//partial pivoting
			}
			else{//complete pivoting
				SelectLeadingCoefficient(j);
			}
			//eliminate aij(i>j)
			for(int i=j+1; i<Row; i++){
				double temp=0.0;
				try{
					temp=CoefficientMatrix.GetElementValue(i, j)/
							CoefficientMatrix.GetElementValue(j, j);
				}
				catch(ArithmeticException ae){
					//aii=0;
				}
				for(int k=j; k<Column; k++){
					CoefficientMatrix.SetElementValue(i, k, 
							CoefficientMatrix.GetElementValue(i, k)
							-temp*CoefficientMatrix.GetElementValue(j, k));
				}
				ConstantVector.SetElementValue(i, ConstantVector.GetElementValue(i)
						-temp*ConstantVector.GetElementValue(j));
			}
		}
		return CoefficientMatrix.ToArray();
	}
	
	public double[] GetSolution(){
		ToUpperTriangularMatrix();
		Solution.SetElementValue(Row-1, 
				ConstantVector.GetElementValue(Row-1)
				/CoefficientMatrix.GetElementValue(Row-1, Column-1));
		for(int i=Row-2; i>=0; i--){
			double temp=0.0;
			for(int j=i+1; j<Column; j++){
				temp+=CoefficientMatrix.GetElementValue(i, j)*Solution.GetElementValue(j);
			}
			Solution.SetElementValue(i, 
					(ConstantVector.GetElementValue(i)-temp)/CoefficientMatrix.GetElementValue(i, i));
		}
		//sort the element of the solution
		for(int i=1; i<Row; i++){
			for(int j=i; (j>0) && 
					(Order[j]<Order[j-1]); j--){
				int temp=Order[j];Order[j]=Order[j-1];Order[j-1]=temp;
				Solution.ExchangeElements(j, j-1);
			}
		}
		//return the result
		return Solution.ToArray();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//test
		//Clock clock=Clock.systemUTC();//calculate the wasting time of this program.
		//int size=3;
		/*double[][] A={
				{1.0, 2.0, 3.0},
				{5.0, 4.0, 10.0}, 
				{3.0, -0.1, 1.0}};
		double[] B=new double[]{1.0, 0.0, 2.0};*/
		double[][] A={
				{0.2388, 0.2471, 0.2568, 1.2671},
				{0.1968, 0.2071, 1.2168, 0.2271},
				{0.1581, 1.1675, 0.1768, 0.1871},
				{1.1161, 0.1254, 0.1397, 0.1490}};
		double[] B={1.8471, 1.7471, 1.6471, 1.5471};
		//double[] X=new double[size];
		GaussianElimination GE=new GaussianElimination(A, B);
		//GE.ToUpperTriangularMatrix();
		//long StartTime=clock.millis();
		double[] X=GE.GetSolution();
		//long FinishTime=clock.millis();
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
