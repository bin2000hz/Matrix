package matrix;

public class SquareMatrix extends Matrix{

	private int Size;
	
	public SquareMatrix(int size) {
		super(size, size);
		Size=size;
	}
	
	public SquareMatrix(double[][] InputMatrix) {
		super(InputMatrix);
		if(InputMatrix.length!=InputMatrix[0].length){
			System.out.println("The inputting matrix is not a square matrix.");
			System.exit(0);
		}
		Size=InputMatrix.length;
	}
	
	public SquareMatrix(int size, String para){
		super(size, size);
		Size=size;
		//create the identity square matrix
		if(para.trim().toLowerCase().equals("identity")){
			for(int i=0; i<size; i++){
				super.SetElementValue(i, i, 1.0);
			}
		}
	}
	
	public int GetMatrixSize(){
		return Size;
	}
	
	public boolean IsSymmetric(){
		boolean result=true;
		outer:
		for(int i=0; i<Size-1; i++){
			for(int j=i+1; j<Size; j++){
				if(super.GetElementValue(i, j)!=super.GetElementValue(j, i)){
					result=false;
					break outer;
				}
			}
		}
		return result;
	}
	
		
	/*private SquareMatrix[] SchmidtProcess(double[][] InputMatrix){
		Vector[] vectors=new Vector[Size];
		for(int i=0; i<Size; i++){
			vectors[i]=new Vector(Size);
			for(int j=0; j<Size; j++){
				vectors[i].SetVectorElementValue(j, InputMatrix[j][i]);
			}
		}
		//start
		//Initialization
		Vector[] OrthogonalVector=new Vector[Size];
		for(int i=0; i<Size; i++){
			OrthogonalVector[i]=new Vector(Size);
		}
		for(int j=0; j<Size; j++){
			OrthogonalVector[0].SetVectorElementValue(j, vectors[0].GetVectorElementValue(j));
		}
		//caculate the orthogonal vector
		for(int j=1; j<Size; j++){
			OrthogonalVector[j].Copy(vectors[j]);
			for(int k=0; k<j; k++){
				double temp=vectors[j].InnerProduct(OrthogonalVector[k])
						/OrthogonalVector[k].InnerProduct(OrthogonalVector[k]);
				OrthogonalVector[j]=OrthogonalVector[j].Subtract
						(OrthogonalVector[k].MultiplyConst(temp));
			}
		}
		//normalization
		for(int i=0; i<Size; i++){
			OrthogonalVector[i].Normalization();
		}

		//The return result are the square matrixs(q and r),QR[0]=Q, QR[1]=R.
		SquareMatrix[] QR=new SquareMatrix[2];
		QR[0]=new SquareMatrix(Size);
		QR[1]=new SquareMatrix(Size);
		//caculate the orthogonal matrix(Q)
		for(int j=0; j<Size; j++){
			for(int i=0; i<Size; i++){
				QR[0].SetElementValue(i, j, OrthogonalVector[j].GetVectorElementValue(i));
			}
		}
		//caculate the R matrix
		for(int j=0; j<Size; j++){
			for(int i=0; i<=j; i++){
				QR[1].SetElementValue(i, j, OrthogonalVector[i].InnerProduct(vectors[j]));
			}
		}
		//return
		return QR;
	}*/
	
	public SquareMatrix ToUpperHessenbergMatrix(){
		//transform the square matrix to Hessenberg matrix by using Householder reflection method.
		Matrix Hessenberg=new SquareMatrix(Size);
		Hessenberg.SetMatrixValue(this.ToArray());
		for(int j=0; j<Size-2; j++){
			Vector u=new Vector(Size);
			for(int i=0; i<Size; i++){
				if(i<=j){
					u.SetElementValue(i, 0.0);
				}
				else{
					u.SetElementValue(i, Hessenberg.GetElementValue(i, j));
				}
			}
			double temp=0.0;
			if(Hessenberg.GetElementValue(j+1, j)<1e-5){
				temp=u.GetEuclideanNorm("2");
			}
			else{
				temp=(-1.0)*Math.signum(Hessenberg.GetElementValue(j+1, j))*u.GetEuclideanNorm("2");
			}
			Vector e=new Vector(Size);
			e.SetElementValue(j+1, 1.0);
			u=u.Subtract(e.MultiplyConst(temp));
			temp=u.InnerProduct(u);
			Matrix H=new SquareMatrix(Size, "identity");
			H=H.Subtract(u.MultiplyToMatrix(u).MultiplyConst(2.0/temp));
			Hessenberg=H.Multiply(Hessenberg).Multiply(H);
		}
		for(int j=0; j<Size-2; j++){
			for(int i=j+2; i<Size; i++){
				Hessenberg.SetElementValue(i, j, 0.0);
			}
		}
		SquareMatrix result=new SquareMatrix(Size);
		result.CopyData(Hessenberg);
		return result;
	}
	
	public SquareMatrix[] LUPDecomposition(){
		/*The coefficient Matrix decomposed to the lower matrix(L) , the permutation matrix(P)
		and the upper triangular matrix(U).   
		L*U=P*A, P used for algorithm stability.
		*/
		//The return result,result[0]=L,result[1]=U,result[2]=P.
		SquareMatrix result[]=new SquareMatrix[3];
		result[0]=new SquareMatrix(Size);
		result[1]=new SquareMatrix(Size);
		result[2]=new SquareMatrix(Size,"Identity");
		SquareMatrix LU=new SquareMatrix(Size);
		//exchange rows to make the leading coefficient of the matrix reaches the max,
		//which make sure the algorithm performing more stable.
		double MaxVal=Math.abs(super.GetElementValue(0, 0));
		int MaxValRow=0;
		for(int i=0; i<Size; i++){
			if(MaxVal<Math.abs(super.GetElementValue(i, 0))){
				MaxVal=Math.abs(super.GetElementValue(i, 0));
				MaxValRow=i;
			}
		}
		if(MaxValRow!=0){
			super.ExchangeRows(0, MaxValRow);
			result[2].ExchangeRows(0, MaxValRow);
		}
		//The first row of upper triangular matrix.
		for(int col=0; col<Size; col++){
			LU.SetElementValue(0, col, super.GetElementValue(0, col));
		}		
		//The first column of the lower triangular matrix, not including L[0][0].
		for(int row=1; row<Size; row++){
			LU.SetElementValue(row, 0, super.GetElementValue(row, 0)/super.GetElementValue(0, 0));
		}		
		//
		for(int row=1; row<Size; row++){
			//exchange the row
			MaxValRow=row;
			MaxVal=0.0;
			for(int i=row; i<Size; i++){
				double temp=0.0;
				for(int k=0; k<row; k++){
					temp+=LU.GetElementValue(i, k)*LU.GetElementValue(k, row);
					//temp+=LUMatrix[i][k]*LUMatrix[k][row];
				}
				double Val=Math.abs(super.GetElementValue(i, row)-temp);
				if(MaxVal<Val){
					MaxVal=Val;
					MaxValRow=i;
				}
			}
			if(row!=MaxValRow){
				LU.ExchangeRows(row, MaxValRow);
				super.ExchangeRows(row, MaxValRow);
				result[2].ExchangeRows(row, MaxValRow);
			}
			
			//calculate the row
			for(int column=row; column<Size; column++){
				double temp=0.0;
				for(int k=0; k<=column-1; k++){
					temp+=LU.GetElementValue(row, k)*LU.GetElementValue(k, column);
				}				
				LU.SetElementValue(row, column, super.GetElementValue(row, column)-temp);
			}
			//calculate the column
			for(int row1=row+1; row1<Size; row1++){
				double temp=0.0;
				for(int k=0; k<=row1-1; k++){
					temp+=LU.GetElementValue(row1, k)*LU.GetElementValue(k, row);
				}
				LU.SetElementValue(row1, row, 
						(super.GetElementValue(row1, row)-temp)/LU.GetElementValue(row, row));
			}
		}
		//return the result
		for(int i=0; i<Size; i++){
			for(int j=0; j<Size; j++){
				if(i==j){
					result[0].SetElementValue(i, j, 1.0);
					result[1].SetElementValue(i, j, LU.GetElementValue(i, j));
				}
				else if(i>j){
					result[0].SetElementValue(i, j, LU.GetElementValue(i, j));
				}
				else{//i<j
					result[1].SetElementValue(i, j, LU.GetElementValue(i, j));
				}
			}
		}
		return result;
	}
	
	public TriangularMatrix[] LUDecomposition(){
		//The coefficient Matrix decomposed to the lower matrix(L) and the upper matrix(U).
		SquareMatrix LU=new SquareMatrix(Size);
		
		//The first row of upper triangular matrix.
		for(int col=0; col<Size; col++){
			LU.SetElementValue(0, col, super.GetElementValue(0, col));
		}
		
		//The first column of the lower triangular matrix, not including L[0][0].
		for(int row=1; row<Size; row++){
			LU.SetElementValue(row, 0, super.GetElementValue(row, 0)/super.GetElementValue(0, 0));
		}
		
		//
		for(int row=1; row<Size; row++){
			//calculate the row
			for(int column=row; column<Size; column++){
				double temp=0.0;
				for(int k=0; k<=column-1; k++){
					temp+=LU.GetElementValue(row, k)*LU.GetElementValue(k, column);
				}
				LU.SetElementValue(row, column, super.GetElementValue(row, column)-temp);
			}
			//calculate the column
			for(int row1=row+1; row1<Size; row1++){
				double temp=0.0;
				for(int k=0; k<=row1-1; k++){
					temp+=LU.GetElementValue(row1, k)*LU.GetElementValue(k, row);
				}
				LU.SetElementValue(row1, row, 
						(super.GetElementValue(row1, row)-temp)/LU.GetElementValue(row, row));
			}
		}
		//return two triangular matrix
		TriangularMatrix[] result=new TriangularMatrix[2];
		//result[0]=L matrix, result[1]=U matrix.
		result[0]=new TriangularMatrix(Size, "lower");
		result[1]=new TriangularMatrix(Size, "upper");
		for(int i=0; i<Size; i++){
			for(int j=0; j<Size; j++){
				if(i==j){
					result[0].SetElementValue(i, j, 1.0);
					result[1].SetElementValue(i, j, LU.GetElementValue(i, j));
				}
				else if(i>j){
					result[0].SetElementValue(i, j, LU.GetElementValue(i, j));
				}
				else{//i<j
					result[1].SetElementValue(i, j, LU.GetElementValue(i, j));
				}
			}
		}
		return result;
	}
	
	public SquareMatrix[] CholeskyDecomposition(){
		/*
		 * The Cholesky decomposition algorithm only suits for the symmetric positive matrix,
		 * it will decompose the matrix to three matrixes(L, D, LT). L is the lower triangular
		 * matrix, D is the diagonal matrix, and LT is the transposed of the L.
		 * The return result is the array of the square matrix, including the L and D matrixes.
		 * The first member of the array is L, and the second member of the array is D.
		 * 
		 */
		if(IsSymmetric()==false){
			System.out.println("The Cholesky decomposition algorithm only suits for the symmetric positive matrix");
			System.exit(0);
		}
		SquareMatrix[] result=new SquareMatrix[2];
		result[0]=new SquareMatrix(Size, "Identity");//L
		result[1]=new SquareMatrix(Size);//D
		double temp;
		for(int i=0; i<Size; i++){
			//L
			for(int j=0; j<=i-1; j++){
				temp=0.0;
				for(int k=0; k<=i-1; k++){
					temp+=result[0].GetElementValue(j, k)*result[0].GetElementValue(i, k)
							*result[1].GetElementValue(k, k);
				}
				result[0].SetElementValue(i, j, (super.GetElementValue(i, j)-temp)
						/result[1].GetElementValue(j, j));
			}
			//D
			temp=0.0;
			for(int k=0; k<=i-1; k++){
				temp+=result[0].GetElementValue(i, k)*result[0].GetElementValue(i, k)
						*result[1].GetElementValue(k, k);
			}
			result[1].SetElementValue(i, i, super.GetElementValue(i, i)-temp);
		}
		return result;
	}
	
	public double GetDet(){
		/* 
		 * The GetDet() function returns the determinant of the square matrix.
		 */
		TriangularMatrix[] LU=LUDecomposition();
		double result=1.0;
		for(int i=0; i<Size; i++){
			result*=LU[0].GetElementValue(i, i)*LU[1].GetElementValue(i, i);
		}
		return result;
	}
	
	public SquareMatrix Inverse(){
		/*
		 * The Inverse function returns the inverse matrix of this matrix, using Gaussian
		 * elimination algorithm, if it exists. 
		 */
		//copy the data
		SquareMatrix m=new SquareMatrix(super.ToArray());
		SquareMatrix Im=new SquareMatrix(Size, "Identity");
		try{
			for(int j=0; j<Size; j++){
				for(int i=j+1; i<Size; i++){
					//choose the pivoting coefficient and exchange the rows to make the algorithm run stably.
					double MaxVal=m.GetElementValue(j, j);
					int MaxRow=j;
					for(int k=j; k<Size; k++){
						if(MaxVal<m.GetElementValue(k, j)){
							MaxVal=m.GetElementValue(k, j);MaxRow=k;
						}
					}
					//exchange the rows
					if(MaxRow!=j){
						m.ExchangeRows(MaxRow, j);
						Im.ExchangeRows(MaxRow, j);
					}
					//start Gaussian elimination			
					double temp=m.GetElementValue(i, j)/m.GetElementValue(j, j);
					for(int k=0; k<Size; k++){
						m.SetElementValue(i, k, m.GetElementValue(i, k)-temp*m.GetElementValue(j, k));
						Im.SetElementValue(i, k, Im.GetElementValue(i, k)-temp*Im.GetElementValue(j, k));
					}
				}
			}
			
			for(int i=0; i<Size; i++){
				double temp=m.GetElementValue(i, i);
				for(int j=0; j<Size; j++){
					m.SetElementValue(i, j, m.GetElementValue(i, j)/temp);
					Im.SetElementValue(i, j, Im.GetElementValue(i, j)/temp);
				}
			}
			
			for(int j=Size-1; j>=0; j--){
				for(int i=j-1; i>=0; i--){
					double temp=m.GetElementValue(i, j)/m.GetElementValue(j, j);
					for(int k=Size-1; k>=0; k--){
						m.SetElementValue(i, k, m.GetElementValue(i, k)-temp*m.GetElementValue(j, k));
						Im.SetElementValue(i, k, Im.GetElementValue(i, k)-temp*Im.GetElementValue(j, k));
					}
				}
			}
		}
		catch(ArithmeticException ae){
			System.out.println("The inverse matrix does not exist.");
			System.exit(0);
		}
		return Im;
	}

	public static void main(String[] args) {
		// Test
		int size=4;
		/*double[][] A={
				{3, 14, 9},
				{6, 43, 3},
				{6, 22, 15}
		};*/
		double[][] A={
				{-4, -30, 60, -35},
				{-30, 300, -675, 420},
				{60, -675, 1620, -1050},
				{-35, 420, -1050, 700}
		};
		/*double[][] A={
			{54,    40,    10,    76},
		    {47,    20,    94,    49},
		    {26,    80,    94,    70},
		     {3,    92,    83,    45}
		};*/
		/*double[][] A={
				{1,2,3},
				{2,2,1},
				{3,4,3}
		};*/
		SquareMatrix S=new SquareMatrix(A);
		/*Matrix B=S.ToUpperHessenbergMatrix();
		double[][] res=B.ToArray();*/
		//TriangularMatrix[] C=S.LUDecomposition();
		//SquareMatrix[] C=S.LUPDecomposition();
		//SquareMatrix[] C=S.CholeskyDecomposition();
		//double[][] res=C[1].ToArray();
		double[][] res=S.Inverse().ToArray();
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				System.out.printf("%10.5f", res[i][j]);
				System.out.print("   ");
			}
			System.out.println("");
		}
		//System.out.println(S.GetDet());
	}

}
