package matrix;

public class TriangularMatrix {

	//The member of this class.
	private int Size;
	private double[] matrix;//using the one-dimension array to store the element to save the memory space.
	private String Type;//The type of the triangular matrix, upper or lower.
	
	public TriangularMatrix(int size, String type){
		  matrix=new double[size*(size+1)/2];
		  Size=size;
		  if(type.toLowerCase().equals("upper")){
			  Type="upper";//upper triangular matrix
		  }
		  else if(type.toLowerCase().equals("lower")){
			  Type="lower";//lower triangular matrix
		  }
		  else{
			  System.out.println("Creating triangular matrix failed due to inputting incorrect type.");
			  System.exit(0);
		  }
	}
	
	/*public TriangularMatrix(double[][] m){
		if(m.length!=m[0].length){
			System.out.println("The inputting array is not a square matrix.");
			System.exit(0);
		}
		int len=(int)(Math.sqrt(1+8*m.length)-1)/2;
		
	}*/
	
	public int GetElementPos(int row, int col){
		int pos=-1;
		if(Type.equals("upper")){
			pos=Size*row-row*(row+1)/2+col;
		}
		else if (Type.equals("lower")){
			pos=row*(row+1)/2+col;
		}
		else {
			System.out.println("There is an error happened in the type of the triangular matrix.");
			System.exit(0);
		}
		return pos;
	}
	
	public int GetMatrixSize(){
		return Size;
	}
	
	public void SetMatrixSize(int size){
		this.Size=size;
	}
	
	public double GetElementValue(int row, int col){
		double result=0.0;
		int pos=GetElementPos(row, col);
		try{
			result=matrix[pos];
		}
		catch(IndexOutOfBoundsException ie){
			System.out.println("The element's postion caculation error.");
			System.exit(0);
		}
		return result;
	}
	
	public void SetElementValue(int row, int col, double value){
		int pos=GetElementPos(row, col);
		try{
			matrix[pos]=value;
		}
		catch(IndexOutOfBoundsException ie){
			System.out.println("The element's postion caculation error.");
			System.exit(0);
		}
	}
	
	public SquareMatrix ToSquareMatrix(){
		SquareMatrix result=new SquareMatrix(Size);
		if(Type.equals("upper")){
			int k=0;
			for(int i=0; i<Size; i++){
				for(int j=i; j<Size; j++){
					result.SetElementValue(i, j, matrix[k]);
					k++;
				}
			}
		}
		else if(Type.equals("lower")){
			int k=0;
			for(int i=0; i<Size; i++){
				for(int j=0; j<=i; j++){
					result.SetElementValue(i, j, matrix[k]);
					k++;
				}
			}
		}
		else{
			System.out.println("There is an error happened in the type of the triangular matrix.");
			System.exit(0);
		}
		return result;
	}
	
	public double[][] To2DArray(){
		return ToSquareMatrix().ToArray();
	}
	
	
	
	
	
	
}
