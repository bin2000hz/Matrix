package matrix;

import linearEquationsSystem.GaussianElimination;

public class EigenValue {

	private SquareMatrix matrix;
	private double[] EigenValue;
	private Vector[] EigenVector;
	private int Step=0;
	private double error=1e-3;
	private double Residual;
	private boolean IsConvergence=false;
	
	public EigenValue(int size) {
		matrix=new SquareMatrix(size);
		EigenValue=new double[size];
		EigenVector=new Vector[size];
		for(int i=0; i<size; i++){
			EigenVector[i]=new Vector(size);
		}
	}
	
	private class ValuePos{
		private int Row;
		private int Column;	
		public void SetRow(int row){
			Row=row;
		}
		public int GetRow(){
			return Row;
		}
		public void SetColumn(int column){
			Column=column;
		}
		public int GetColumn(){
			return Column;
		}
	}
	
	private ValuePos SearchMaxValue(){
		ValuePos pos=new ValuePos();
		double MaxValue=Math.abs(matrix.GetElementValue(0, 1));
		pos.SetRow(0);
		pos.SetColumn(1);
		for(int i=0; i<matrix.GetMatrixSize(); i++){
			for(int j=i+1; j<matrix.GetMatrixSize(); j++){
				if(MaxValue<Math.abs(matrix.GetElementValue(i, j))){
					MaxValue=Math.abs(matrix.GetElementValue(i, j));
					pos.SetRow(i);
					pos.SetColumn(j);
				}
			}
		}		
		return pos;
	}
	
	private boolean CheckConvergence(){
		boolean result=false;
		Residual=Math.abs(matrix.GetElementValue(1, 0));
		for(int i=0; i<matrix.GetMatrixSize(); i++){
			for(int j=0; j<i; j++){
				if(Residual<Math.abs(matrix.GetElementValue(i, j))){
					Residual=Math.abs(matrix.GetElementValue(i, j));
				}
			}
		}
		if(Residual<error){
			result=true;
		}
		return result;
	}
	
	private void JacobiRotation(){
		//the eigenvector caculation initialize
		for(int i=0; i<matrix.GetMatrixSize(); i++){
			EigenVector[i].SetElementValue(i, 1.0);;
		}
		//start rotate the matrix
		SquareMatrix TempMatrix=new SquareMatrix(matrix.GetMatrixSize());
		TempMatrix.CopyData(matrix);
		IsConvergence=false;
		Step=0;
		do{
			ValuePos MaxValuePos=SearchMaxValue();
			int p=MaxValuePos.GetRow();
			int q=MaxValuePos.GetColumn();
			//caculate the eigenvalue
			double angle=0.0;
			if(Math.abs(TempMatrix.GetElementValue(p, p)-TempMatrix.GetElementValue(q, q))<1e-3){
				angle=Math.PI/4;
			}
			else{
				angle=0.5*Math.atan(2.0*TempMatrix.GetElementValue(p, q)
						/(TempMatrix.GetElementValue(p, p)-TempMatrix.GetElementValue(q, q)));
			}
			matrix.SetElementValue(p, p, TempMatrix.GetElementValue(p, p)*Math.cos(angle)*Math.cos(angle)
					+TempMatrix.GetElementValue(q, q)*Math.sin(angle)*Math.sin(angle)
					+TempMatrix.GetElementValue(p, q)*Math.sin(2.0*angle));
			matrix.SetElementValue(q, q, TempMatrix.GetElementValue(p, p)*Math.sin(angle)*Math.sin(angle)
					+TempMatrix.GetElementValue(q, q)*Math.cos(angle)*Math.cos(angle)
					-TempMatrix.GetElementValue(p, q)*Math.sin(2.0*angle));
			for(int i=0; i<matrix.GetMatrixSize(); i++){
				if((i!=p)&&(i!=q)){
					matrix.SetElementValue(p, i, TempMatrix.GetElementValue(p, i)*Math.cos(angle)
							+TempMatrix.GetElementValue(q, i)*Math.sin(angle));
					matrix.SetElementValue(i, p, TempMatrix.GetElementValue(p, i));
					matrix.SetElementValue(q, i, (-1.00)*TempMatrix.GetElementValue(p, i)*Math.sin(angle)
							+TempMatrix.GetElementValue(q, i)*Math.cos(angle));
					matrix.SetElementValue(i, q, matrix.GetElementValue(q, i));
				}
			}
			matrix.SetElementValue(p, q, 0.5*(TempMatrix.GetElementValue(q, q)-TempMatrix.GetElementValue(p, p))
					*Math.sin(2.0*angle)+TempMatrix.GetElementValue(p, q)*Math.cos(2.0*angle));
			matrix.SetElementValue(q, p, matrix.GetElementValue(p, q));
			//calculate the eigen vector
			for(int i=0; i<matrix.GetMatrixSize(); i++){
				double r1=EigenVector[i].GetElementValue(p);
				double r2=EigenVector[i].GetElementValue(q);
				EigenVector[i].SetElementValue(p, r1*Math.cos(angle)+r2*Math.sin(angle));
				EigenVector[i].SetElementValue(q, 
						(-1.0)*r1*Math.sin(angle)+r2*Math.cos(angle));
			}
			//the iteration finish, copy the data for the next iteration.
			TempMatrix.CopyData(matrix);
			Step++;
			IsConvergence=CheckConvergence();
			System.out.println("Step "+Step+": The residual is "+Residual);
		}while((IsConvergence==false)&&(Step<1000));
	}
	
	private void QRMethod(){
		//Using the QR algorithm to calculate the eigen value of the matrix.
		Matrix[] QR=new Matrix[2];
		QR[0]=new Matrix(matrix.GetMatrixSize(), matrix.GetMatrixSize());
		QR[1]=new Matrix(matrix.GetMatrixSize(), matrix.GetMatrixSize());
		double[][] OriginalMatrix=matrix.ToArray();//copy the original data
		matrix=matrix.ToUpperHessenbergMatrix();
		//QR=matrix.QRdecomposition(InputMatrix, "givens");
		Matrix TempMatrix=new Matrix(matrix.GetMatrixSize(), matrix.GetMatrixSize());
		TempMatrix.CopyData(matrix);
		//int size=matrix.GetMatrixSize();
		Step=0;
		do{
			//using the givens method to decompose the matrix to QR matrixes.
			QR=matrix.QRdecomposition("givens");
			TempMatrix=QR[0].Transpose().Multiply(matrix).Multiply(QR[0]);
			matrix.CopyData(TempMatrix);
			
			//using the original QR method to calculate the eigenvalue of the square matrix 
			/*QR=matrix.QRdecomposition("householder");
			TempMatrix=QR[0].Transpose().Multiply(matrix).Multiply(QR[0]);
			matrix.CopyData(TempMatrix);*/
			
			//accelerate the speed to calculation
			/*double temp=TempMatrix.GetElementValue(size-1, size-1);
			for(int i=0; i<size; i++){
				TempMatrix.SetElementValue(i, i, TempMatrix.GetElementValue(i, i)-temp);
			}
			QR=TempMatrix.QRdecomposition("Householder");
			TempMatrix=QR[1].Multiply(QR[0]);
			for(int i=0; i<size; i++){
				TempMatrix.SetElementValue(i, i, TempMatrix.GetElementValue(i, i)+temp);
			}
			matrix.CopyData(TempMatrix)*/;
			
			IsConvergence=CheckConvergence();
			Step++;
			System.out.println("Step:"+Step+", residual:"+Residual);
		}while((IsConvergence==false)&&(Step<1000));	
		//check the iterative calculation converges or not.
		if(IsConvergence==true){
			for(int i=0; i<matrix.GetMatrixSize(); i++){
				EigenValue[i]=matrix.GetElementValue(i, i);
			}
			//calculate the eigenvectors
			for(int k=0; k<matrix.GetMatrixSize(); k++){
				Vector x=new Vector(matrix.GetMatrixSize());
				for(int i=0; i<matrix.GetMatrixSize(); i++){
					x.SetElementValue(i, 1.0);
				}
				Vector y=new Vector(matrix.GetMatrixSize());
				double[][] A=new double[matrix.GetMatrixSize()][matrix.GetMatrixSize()];
				for(int i=0; i<matrix.GetMatrixSize(); i++){
					for(int j=0; j<matrix.GetMatrixSize(); j++){
						A[i][j]=OriginalMatrix[i][j];
					}
				}
				for(int i=0; i<matrix.GetMatrixSize(); i++){
					A[i][i]-=EigenValue[k];
				}
				GaussianElimination GE=new GaussianElimination(A, x.ToArray());
				double[] uu=GE.GetSolution();
				y.SetVectorValue(uu);
				x=y.MultiplyConst(1.0/y.GetNorm("2"));
				EigenVector[k].Copy(x);
			}
			
		}
		else{
			System.out.println("The iterative calculation does not converge.");
			System.exit(0);
		}
	}
	
	public double[] GetEigenValue(double[][] InputMatrix, String Method){
		//copy data firstly
		matrix.SetMatrixValue(InputMatrix);
		if(Method.trim().toLowerCase().equals("jacobi")){
			//check data
			if(matrix.IsSymmetric()==false){
				System.out.println("The matrix's data is not valid, "
						+ "the matrix must be realistic and symmetric");
				System.exit(0);
			}
			//Using the Jacobi rotation method to get the eigenvalue of the matrix(start iteration).
			JacobiRotation();
		}
		else if(Method.trim().toLowerCase().equals("qr")){
			QRMethod();
		}
		else{//invalid parameter
			System.out.println("Invalid inputting parameters for Getting eigenvalue of the matrix.");
			System.exit(0);	
		}
		//sort
		for(int i=0; i<EigenValue.length-1; i++){
			for(int j=i+1; j<EigenValue.length; j++){
				if(EigenValue[i]<EigenValue[j]){
					//exchange the eigenvalue
					double temp=EigenValue[i];
					EigenValue[i]=EigenValue[j];
					EigenValue[j]=temp;
					//exchange the eigenvector
					for(int k=0; k<matrix.GetMatrixSize(); k++){
						temp=EigenVector[k].GetElementValue(i);
						EigenVector[k].SetElementValue(i, 
								EigenVector[k].GetElementValue(j));
						EigenVector[k].SetElementValue(j, temp);
					}
				}
			}
		}
		return EigenValue;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int size=4;
		/*double[][] A={
				{2.0, -1.0, 0.0},
				{-1.0, 2.0, -1.0},
				{0.0, -1.0, 2.0}
		};*/
		double[][] A={
				{81.4724,   63.2359,   95.7507,   95.7167},
				{90.5792,    9.7540,   96.4889,   48.5376},
				{12.6987,   27.8498,   15.7613,   80.0280},
				{91.3376,   54.6882,   97.0593,   14.1886}
			};
		EigenValue EV=new EigenValue(size);
		double[] X=EV.GetEigenValue(A,"qr");
		//double[][] EV=Jacobi.GetEigenVector();
		System.out.println("The eigenvalue of the matrix are:");
		for(int i=0; i<size; i++){
			System.out.print("Eigenvalue "+(i+1)+":");
			System.out.printf("%10.5f",X[i]);
			System.out.println(";");
		}
		/*System.out.println("The eigenvector are:");
		for(int j=0; j<size; j++){
			System.out.print("Eigenvector "+(j+1)+": ");
			for(int i=0; i<size; i++){
				System.out.printf("%10.5f", );
				System.out.print("   ");
			}
			System.out.println(";");
		}*/
	}

}
