package linearEquationsSystem;

import matrix.SquareMatrix;
import matrix.Vector;

public class SORMethod {

	/*
	 * The SOR iterative method for solving the linear equations system 
	 */
	
	private int Size;//the size of the coefficient matrix
	private SquareMatrix CoefficientMatrix;//
	private Vector ConstantVector;
	private Vector Solution;//the result vector
	private int Step;//iterative steps
	private double Residual;//the residual
	private double Omiga=1.0;//the relaxation factor
	
	public SORMethod(int size) {
		Size=size;
		//allocate the memory for the matrix
		CoefficientMatrix=new SquareMatrix(Size);
		ConstantVector=new Vector(Size);
		Solution=new Vector(Size);
	}
	
	public SORMethod(double[][] m, double[] ConstantTerm){
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
	
	public void SetRelaxationFactor(double omiga){
		Omiga=omiga;
	}
	
	public double GetRelaxationFactor(){
		return Omiga;
	}
	
	public void SetInitialSolution(double[] InitialSolution){	
		try{
			Solution.SetVectorValue(InitialSolution);
		}
		catch(IndexOutOfBoundsException ie){
			System.out.println("The vector inputted does not match the size.");
			System.exit(0);
		}
	}
	
	private boolean CheckConvergence(){
		boolean result=false;
		Vector Value=CoefficientMatrix.MultiplyVector(Solution);
		Value=Value.Subtract(ConstantVector);
		Residual=Value.GetNorm("2");
		if(Residual<1e-5){
			result=true;
		}
		return result;
	}
	
	public double[] GetSolution(){
		//start iterate
		Step=0;
		boolean Isconvergence=false;
		do{
			for(int i=0; i<Size; i++){
				double temp=0.0;
				for(int j=0; j<Size; j++){
					if(i!=j){
						temp+=CoefficientMatrix.GetElementValue(i, j)*Solution.GetElementValue(j);
					}
				}
				Solution.SetElementValue(i, (1.0-Omiga)*Solution.GetElementValue(i)
						+Omiga*(ConstantVector.GetElementValue(i)-temp)
						/CoefficientMatrix.GetElementValue(i, i));
			}
			Step++;
			//check the convergence or not
			Isconvergence=CheckConvergence();
			//print the steps and the convergence
			System.out.println("Step "+Step+"; The residual of the equations system is:"+Residual);
		}while((Isconvergence==false)&&(Step<=100));
		if(Isconvergence==false){
			System.out.println("The iterative caculation does not converge.");
			System.exit(0);
		}
		return Solution.ToArray();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*double[][] A={
				{0.2388, 0.2471, 0.2568, 1.2671},
				{0.1968, 0.2071, 1.2168, 0.2271},
				{0.1581, 1.1675, 0.1768, 0.1871},
				{1.1161, 0.1254, 0.1397, 0.1490}};
		double[] B={1.8471, 1.7471, 1.6471, 1.5471};*/
		double[][] A={
				{4.00, 3.00, 0.00},
				{3.00, 4.00, -1.00}, 
				{0.00, -1.00, 4.00}
				};
		double[] B=new double[]{24.0, 30.0, -24.0};
		SORMethod SOR=new SORMethod(A, B);
		double[] X=SOR.GetSolution();
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
