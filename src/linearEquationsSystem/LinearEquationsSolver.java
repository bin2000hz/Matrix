package linearEquationsSystem;

public class LinearEquationsSolver {

	private double[][] Coefficient;
	private double[] ConstantTerm;
	
	public LinearEquationsSolver(double[][] coeff, double[] constantterm){
		Coefficient=coeff.clone();
		ConstantTerm=constantterm.clone();
	}
	
	public double[] Solve(){
		return Solve("LU");
	}
	
	public double[] Solve(String method, double RelaxationFactor){
		double[] result;
		if(method.trim().toLowerCase().equals("sor")){
			SORMethod SOR=new SORMethod(Coefficient, ConstantTerm);
			SOR.SetRelaxationFactor(RelaxationFactor);
			result=SOR.GetSolution();
		}
		else{
			result=Solve(method);
		}
		return result;
	}
	
	public double[] Solve(String method){
		double[] result;
		method=method.trim().toLowerCase();
		switch(method){
		case "gaussian":
			GaussianElimination GE=new GaussianElimination(Coefficient, ConstantTerm);
			result=GE.GetSolution();
			break;
		case "cholesky":
			CholeskyDecomposition Cholesky=new CholeskyDecomposition(Coefficient, ConstantTerm);
			result=Cholesky.GetSolution();
			break;
		case "jacobi":
			JacobiIterativeMethod Jacobi=new JacobiIterativeMethod(Coefficient, ConstantTerm);
			result=Jacobi.GetSolution();
			break;
		case "gauss-seidel":
			GaussSeidelMethod GS=new GaussSeidelMethod(Coefficient, ConstantTerm);
			result=GS.GetSolution();
			break;
		case "LU":
			LUDecomposition LU=new LUDecomposition(Coefficient, ConstantTerm);
			result=LU.GetSolution();
			break;
		default:
			System.out.println("Unknown solving method.");
			System.exit(0);
			result=new double[ConstantTerm.length];
		}
		return result;
	}
}
