package matrix;

public class ArraySet {
	//
	//Two matrix are the members of the MatrixSet
	double[][] M1;
	double[][] M2;
	
	public ArraySet(int size1, int size2){
		M1=new double[size1][size1];
		M2=new double[size1][size1];			
	}
	public void SetElementValue(int num, double[][] m){
		if(num==1){
			M1=m;
		}
		else if(num==2){
			M2=m;
		}
		else{
			//error
		}
	}
	public double[][] GetElementValue(int num){
		double[][] result;
		if(num==1){
			result=M1;
		}
		else{
			result=M2;
		}
		return result;
	}
}
