package matrix;

public class Vector {

	private int dimension;
	private double[] vector;
	
	public Vector(int dim){
		dimension=dim;
		vector=new double[dim];
	}
	
	public Vector(double[] v){
		dimension=v.length;
		vector=new double[dimension];
		for(int i=0; i<dimension; i++){
			vector[i]=v[i];
		}
	}
	
	public void SetDimension(int dim) throws Exception{
		if(dim<=0){
		    throw new Exception("The inputting number of dimension is not valid.");
			//System.out.println("The inputting number of dimension is not valid.");
			//System.exit(0);
		}
		dimension=dim;
	}
	
	public int GetDimension(){
		return dimension;
	}
	
	public void SetElementValue(int dim, double value){
		vector[dim]=value;
	}
	
	public double GetElementValue(int dim){
		double res=0.0;
		try{
			res=vector[dim];
		}
		catch(IndexOutOfBoundsException ie){
			System.out.println("The inputting index is out of the vector's boundary.");
			System.exit(0);
		}
		return res;
	}
	
	public void SetVectorValue(double[] v){
		if(dimension!=v.length){
			System.out.println("The inputting vector's dimension does not match the vector initialized.");
			System.exit(0);
		}
		else{
			for(int i=0; i<dimension; i++){
				vector[i]=v[i];
			}
		}
	}
	
	public double[] GetVectorValue(){
		return vector;
	}
	
	public double InnerProduct(Vector y){
		double res=0.0;
		if(dimension!=y.dimension){
			System.out.println("The inputtion vector's dimension does not match.");
			System.exit(0);
		}
		else{
			for(int i=0; i<dimension; i++){
				res+=vector[i]*y.GetElementValue(i);
			}
		}
		return res;
	}
	
	public Vector Add(Vector y){
		Vector result=new Vector(dimension);
		if(dimension!=y.dimension){
			System.out.println("The inputtion vector's dimension does not match.");
			System.exit(0);
		}
		else{
			for(int i=0; i<dimension; i++){
				result.SetElementValue(i, vector[i]+y.GetElementValue(i));
			}
		}
		return result;
	}
	
	public Vector Subtract(Vector y){
		Vector result=new Vector(dimension);
		if(dimension!=y.dimension){
			System.out.println("The inputted vector's dimension does not match.");
			System.exit(0);
		}
		else{
			for(int i=0; i<dimension; i++){
				result.SetElementValue(i, vector[i]-y.GetElementValue(i));
			}
		}
		return result;
	}
	
	public Vector MultiplyConst(double Const){
		Vector result=new Vector(dimension);
		for(int i=0; i<dimension; i++){
			result.SetElementValue(i, vector[i]*Const);
		}
		return result;
	}
	
	public void Copy(Vector y){
		if(dimension!=y.GetDimension()){
			System.out.println("Both of the Vectors' dimension do not equal.");
			System.out.println("The operation of copying failed.");
			System.exit(0);
		}
		else{
			for(int i=0; i<dimension; i++){
				vector[i]=y.GetElementValue(i);
			}
		}
	}
	
	public void Normalization(){
		double temp=0.0;
		for(int i=0; i<dimension; i++){
			temp+=Math.pow(vector[i], 2);
		}
		temp=Math.sqrt(temp);
		for(int i=0; i<dimension; i++){
			vector[i]/=temp;
		}
	}
	
	public double GetEuclideanNorm(String norm){
		double result=0.0;
		if(norm.trim().toLowerCase().equals("infinite")){
			result=Math.abs(vector[0]);
			for(int i=1; i<dimension; i++){
				if(result<Math.abs(vector[i])){
					result=Math.abs(vector[i]);
				}
			}
		}
		else if(norm.trim().equals("1")){
			for(int i=0; i<dimension; i++){
				result+=Math.abs(vector[i]);
			}
		}
		else if(norm.trim().equals("2")){
			for(int i=0; i<dimension; i++){
				result+=vector[i]*vector[i];
			}
			result=Math.sqrt(result);
		}
		else{
			System.out.println("Invalid parameter for calculate the vector's Euclidean norm.");
			System.exit(0);
		}		
		return result;
	}
	
	public Matrix MultiplyToMatrix(Vector y){
		//u(Tran) X u
		Matrix result=new Matrix(dimension, dimension);
		if(dimension!=y.GetDimension()){
			System.out.println("Both of the Vectors' dimension do not equal.");
			System.out.println("The operation of copying failed.");
			System.exit(0);
		}
		else{
			for(int i=0; i<dimension; i++){
				for(int j=0; j<dimension; j++){
					result.SetElementValue(i, j, vector[i]*y.GetElementValue(j));
				}
			}
		}
		return result;
	}
	
	public double[] ToArray(){
		double[] result=new double[dimension];
		for(int i=0; i<dimension; i++){
			result[i]=vector[i];
		}
		return result;
	}
	
	public Matrix ToMatrix(){
		Matrix result=new Matrix(dimension, 1);
		for(int i=0; i<dimension; i++){
			result.SetElementValue(i, 0, vector[i]);
		}
		return result;
	}
	
	public void ExchangeElements(int ElementOnePos, int ElementTwoPos){
		double temp;
		try{
			temp=vector[ElementOnePos];
			vector[ElementOnePos]=vector[ElementTwoPos];
			vector[ElementTwoPos]=temp;
		}
		catch(IndexOutOfBoundsException ie){
			System.out.println("Exchanging vector's element failed.");
			System.exit(0);
		}
	}
	
	@Override
	public String toString(){
	    StringBuilder result=new StringBuilder();
	    for(int i=0; i<dimension; i++){
	        result.append(String.valueOf(vector[i])+"\n");
	    }
	    return result.toString();
	}
}
