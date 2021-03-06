package matrix;

public class Matrix {

    private int row;
    private int column;
    private double[][] matrix;
    
    public Matrix(int row, int col)
    {
        this.row=row;
        this.column=col;
        matrix=new double[row][col];
    }
    
    public Matrix(double[][] InputMatrix){
        this.row=InputMatrix.length;
        this.column=InputMatrix[0].length;
        matrix=new double[this.row][this.column];
        for(int i=0; i<this.row; i++){
            for(int j=0; j<this.column; j++){
                matrix[i][j]=InputMatrix[i][j];
            }
        }
    }
    
    public Matrix(double[] inputMatrix) {
        this.row=inputMatrix.length;
        this.column=1;
        matrix=new double[this.row][this.column];
        for(int i=0; i<this.row; i++){
            matrix[i][0]=inputMatrix[i];
        }
    }
    
    public Matrix(int row, int col, double val) {
        this.row=row;
        this.column=col;
        matrix=new double[row][col];
        for(int i=0; i<this.row; i++){
            for(int j=0; j<this.column; j++){
                matrix[i][j]=val;
            }
        }
    }
    
    public Matrix(int row, int col, String para){
        this.row=row;
        this.column=col;
        matrix=new double[row][col];
        String parameter=para.trim().toLowerCase();     
        if(parameter.equals("identity")){
            //create the identity matrix
            for(int i=0; i<Math.min(row, col); i++){
                matrix[i][i]=1.0;
            }
        }else if(parameter.equals("random")){
            //create the random matrix
            for(int i=0; i<row; i++){
                for(int j=0; j<col; j++){
                    matrix[i][j]=Math.random();
                }
            }
        }else if(parameter.equals("ones")){
            //create the matrix, each element's value is one.
            for(int i=0; i<row; i++){
                for(int j=0; j<col; j++){
                    matrix[i][j]=1.0;
                }
            }
        }
    }
    
    public int GetMatrixRow(){
        return this.row;
    }
    
    public int GetMatrixColumn(){
        return this.column;
    }
    
    public void CopyData(Matrix m){
        if((this.row!=m.GetMatrixRow())||(this.column!=m.GetMatrixColumn())){
            throw new IllegalArgumentException("Unable to copy matrix data.");
        }
        else{
            for(int i=0; i<this.row; i++){
                for(int j=0; j<this.column; j++){
                    matrix[i][j]=m.GetElementValue(i, j);
                }
            }
        }
    }
    
    public void SetMatrixValue(double[][] m){
        int rows=m.length;
        int columns=m[0].length;
        if((rows!=this.row)||(columns!=this.column)){
            throw new IllegalArgumentException("Unable to generate matrix.");
        }
        else{
            for(int i=0;i<rows;i++){
                for(int j=0;j<columns;j++){
                    matrix[i][j]=m[i][j];
                }
            }
        }
    }
    
    public double[][] ToArray(){
        double[][] result=new double[this.row][this.column];
        for(int i=0; i<this.row; i++){
            for(int j=0; j<this.column; j++){
                result[i][j]=matrix[i][j];
            }
        }
        return result;
    }
    
    public Vector ToVector(){
        Vector result=new Vector(this.row);
        if(this.column!=1){
            throw new IllegalArgumentException("Unable to transform matrix to vector.");
        }
        else{
            for(int i=0; i<this.row; i++){
                result.SetElementValue(i, matrix[i][0]);
            }
        }
        return result;
    }
    
    public void SetElementValue(int row, int column, double value){
        try{
            matrix[row][column]=value;
        }
        catch(IndexOutOfBoundsException ie){
            throw new IllegalArgumentException("Unable to set the matrix element's value "
                    + "due to illegal index.");
        }
    }
    
    public double GetElementValue(int row, int col){
        double result=0.0;
        try{
            result=this.matrix[row][col];
        }
        catch(IndexOutOfBoundsException ie){
            throw new IllegalArgumentException("Unable to get matrix element's value "
                    + "due to illegal index.");
        }
        return result;
    }
    
    public void ExchangeElements(int ElementOneRow, int ElementOneCol, 
            int ElementTwoRow, int ElementTwoCol){
        double temp;
        try{
            temp=matrix[ElementOneRow][ElementOneCol];
            matrix[ElementOneRow][ElementOneCol]=matrix[ElementTwoRow][ElementTwoCol];
            matrix[ElementTwoRow][ElementTwoCol]=temp;
        }
        catch(IndexOutOfBoundsException ie){
            throw new IllegalArgumentException("Unable to exchange rows due to illegal index.");
        }
    }
    
    public void ExchangeRows(int RowOne, int RowTwo){
        for(int j=0; j<this.column; j++){
            ExchangeElements(RowOne, j, RowTwo, j);
        }
    }
    
    public void ExchangeColumns(int ColumnOne, int ColumnTwo){
        for(int i=0; i<this.row; i++){
            ExchangeElements(i, ColumnOne, i, ColumnTwo);
        }
    }
    
    public boolean IsSquareMatrix(){
        boolean result=true;
        if(this.row!=this.column){
            result=false;
        }
        return result;
    }
    
    public SquareMatrix ToSquareMatrix(){
        if(IsSquareMatrix()==false){
            throw new IllegalArgumentException("Unable to covert to square matrix.");
        }
        SquareMatrix result=new SquareMatrix(this.row);
        for(int i=0; i<this.row; i++){
            for(int j=0; j<this.row; j++){
                result.SetElementValue(i, j, matrix[i][j]);
            }
        }
        return result;
    }
    
    public Matrix deleteColumn(int indexColumn) {
        if(indexColumn<0 || indexColumn>this.column-1) {
            throw new IllegalArgumentException("Unable to delete column due to invalid column index.");
        }
        Matrix result=new Matrix(this.row, this.column-1);
        for(int i=0; i<result.row; i++) {
            for(int j=0; j<result.column; j++) {
                if(j<indexColumn) {
                    result.SetElementValue(i, j, matrix[i][j]);
                } else if(j>indexColumn) {
                    result.SetElementValue(i, j, matrix[i][j+1]);
                }
            }
        }
        return result;
    }
    
    public Matrix deleteRow(int indexRow) {
        if(indexRow<0 || indexRow>this.column-1) {
            throw new IllegalArgumentException("Unable to delete row due to invalid row index.");
        }
        Matrix result=new Matrix(this.row, this.column-1);
        for(int j=0; j<result.column; j++) {
            for(int i=0; i<result.row; i++) {
                if(i<indexRow) {
                    result.SetElementValue(i, j, matrix[i][j]);
                } else if(i>indexRow) {
                    result.SetElementValue(i, j, matrix[i+1][j]);
                }
            }
        }
        return result;
    }
    
    public double[] getMaxElement() {
        /**
         * This function will return the maximum element in the whole matrix,
         * the first element of the return array is the maximum element, 
         * the second one is the row index, and the last one is column index. 
         * The row and column indexes should be cast into integer by hand.
         */
        double[] result=new double[3];
        result[0]=matrix[0][0];
        for(int i=0; i<row; i++) {
            for(int j=0; j<column; j++) {
                if(result[0]<matrix[i][j]) {
                    result[0]=matrix[i][j];
                    result[1]=i;
                    result[2]=j;
                }
            }
        }
        return result;
    }
    
    public double[] getMinElement() {
        /**
         * This function will return the minimum element in the whole matrix,
         * the first element of the return array is the maximum element, 
         * the second one is the row index, and the last one is column index. 
         * The row and column indexes should be cast into integer by hand.
         */
        double[] result=new double[3];
        result[0]=matrix[0][0];
        for(int i=0; i<row; i++) {
            for(int j=0; j<column; j++) {
                if(result[0]>matrix[i][j]) {
                    result[0]=matrix[i][j];
                    result[1]=i;
                    result[2]=j;
                }
            }
        }
        return result;
    }
    
    public double[] getMaxElmentInRow(int indexRow) {
        /**
         * This function will return the maximum element in the specified row,
         * the first element of the return array is the maximum element, 
         * and the second one is the column index, which should be cast into integer by hand.
         */
        if(indexRow<0 || indexRow>row-1) {
            throw new IllegalArgumentException("Unable to find maximum element in row"
                    + "due to invalid row index.");
        }
        double[] result=new double[2];
        result[0]=matrix[indexRow][0];
        result[1]=0;
        for(int j=0; j<column; j++) {
            if(result[0]<matrix[indexRow][j]) {
                result[0]=matrix[indexRow][j];
                result[1]=j;
            }
        }
        return result;
    }
    
    public double[] getMinElmentInRow(int indexRow) {
        /**
         * This function will return the minimum element in the specified row,
         * the first element of the return array is the maximum element, 
         * and the second one is the column index, which should be cast into integer by hand.
         */
        if(indexRow<0 || indexRow>row-1) {
            throw new IllegalArgumentException("Unable to find maximum element in row"
                    + "due to invalid row index.");
        }
        double[] result=new double[2];
        result[0]=matrix[indexRow][0];
        result[1]=0;
        for(int j=0; j<column; j++) {
            if(result[0]>matrix[indexRow][j]) {
                result[0]=matrix[indexRow][j];
                result[1]=j;
            }
        }
        return result;
    }
    
    public Matrix Add(Matrix B){
        Matrix result=new Matrix(row, column);
        if((this.row!=B.GetMatrixRow())||(this.column!=B.GetMatrixColumn())){
            throw new IllegalArgumentException("The addition of matrices failed."
                    +"("+String.valueOf(row)+"x"+String.valueOf(column)+")+("
                    +String.valueOf(B.GetMatrixRow())+"x"+String.valueOf(B.GetMatrixColumn())+")");
        }
        else{
            for(int i=0; i<this.row; i++){
                for(int j=0; j<this.column; j++){
                    result.SetElementValue(i, j, matrix[i][j]+B.GetElementValue(i, j));
                }
            }
        }
        return result;
    }
    
    public Matrix Subtract(Matrix B){
        Matrix result=new Matrix(this.row, this.column);
        if((this.row!=B.GetMatrixRow())||(this.column!=B.GetMatrixColumn())){
            throw new IllegalArgumentException("The subtraction of matrices failed."
                    +"("+String.valueOf(row)+"x"+String.valueOf(column)+")-("
                    +String.valueOf(B.GetMatrixRow())+"x"+String.valueOf(B.GetMatrixColumn())+")");
        }
        else{
            for(int i=0; i<this.row; i++){
                for(int j=0; j<this.column; j++){
                    result.SetElementValue(i, j, matrix[i][j]-B.GetElementValue(i, j));
                }
            }
        }
        return result;
    }
    
    public Matrix MultiplyConst(double Const){
        Matrix result=new Matrix(this.row, this.column);
        for(int i=0; i<this.row; i++){
            for(int j=0; j<this.column; j++){
                result.SetElementValue(i, j, matrix[i][j]*Const);
            }
        }
        return result;
    }
    
    public Matrix Multiply(Matrix B){
        //result=matrix*B
        Matrix result=new Matrix(this.row, B.GetMatrixColumn());
        if(this.column!=B.GetMatrixRow()){
            throw new IllegalArgumentException("The mutiplication of matrices failed."
                    +"("+String.valueOf(row)+"x"+String.valueOf(column)+")X("
                    +String.valueOf(B.GetMatrixRow())+"x"+String.valueOf(B.GetMatrixColumn())+")");
        }
        else{
            for(int i=0; i<this.row; i++){
                for(int j=0; j<B.GetMatrixColumn(); j++){
                    double temp=0.0;
                    for(int k=0; k<this.column; k++){
                        temp+=matrix[i][k]*B.GetElementValue(k, j);
                    }
                    result.SetElementValue(i, j, temp);
                }
            }
        }
        return result;
    }
    
    public Vector MultiplyVector(Vector B){
        //result vector=matrix*B
        if(column!=B.GetDimension()){
            throw new IllegalArgumentException("The mutiplication of matrix and vector failed.");
        }
        Vector result=new Vector(this.row);
        for(int i=0; i<this.row; i++){
            double temp=0.0;
            for(int j=0; j<this.column; j++){
                temp+=matrix[i][j]*B.GetElementValue(j);
            }
            result.SetElementValue(i, temp);
        }
        return result;
    }
    
    public Matrix Transpose(){
        Matrix result=new Matrix(this.column, this.row);
        for(int i=0; i<this.row; i++){
            for(int j=0; j<this.column; j++){
                result.SetElementValue(j, i, matrix[i][j]);
            }
        }
        return result;
    }
    
    public Matrix Dot(Matrix B) {
        if(this.row!=B.GetMatrixRow() || this.column!=B.GetMatrixColumn()){
            throw new IllegalArgumentException("The dot mutiplication of matrices failed."
                    +"("+String.valueOf(row)+"x"+String.valueOf(column)+")Dot("
                    +String.valueOf(B.GetMatrixRow())+"x"+String.valueOf(B.GetMatrixColumn())+")");
        }
        Matrix result=new Matrix(this.row, this.column);
        for(int i=0; i<this.row; i++){
            for(int j=0; j<this.column; j++){
                result.SetElementValue(i, j, matrix[i][j]*B.GetElementValue(i, j));
            }
        }
        return result;
    }
    
    public Matrix DotDivide(Matrix B) {
        if(this.row!=B.GetMatrixRow() || this.column!=B.GetMatrixColumn()){
            throw new IllegalArgumentException("The dot mutiplication of matrices failed."
                    +"("+String.valueOf(row)+"x"+String.valueOf(column)+")Dot Divide("
                    +String.valueOf(B.GetMatrixRow())+"x"+String.valueOf(B.GetMatrixColumn())+")");
        }
        Matrix result=new Matrix(this.row, this.column);
        for(int i=0; i<this.row; i++){
            for(int j=0; j<this.column; j++){
                result.SetElementValue(i, j, matrix[i][j]/B.GetElementValue(i, j));
            }
        }
        return result;
    }
    
    public double getNorm(String para) {
        double result=0;
        String p=para.trim().toLowerCase();
        switch(p) {
            case "0": {
                for(int i=0; i<row; i++) {
                    for(int j=0; j<column; j++) {
                        if(matrix[i][j]!=0.0) {
                            result++;
                        }
                    }
                }
                break;
            }
            case "1": {
                for(int i=0; i<row; i++) {
                    for(int j=0; j<column; j++) {
                        result+=Math.abs(matrix[i][j]);
                    }
                }
                break;
            }
            case "frobenius": {
                for(int i=0; i<row; i++) {
                    for(int j=0; j<column; j++) {
                        result+=Math.pow(matrix[i][j], 2);
                    }
                }
                result=Math.sqrt(result);
                break;
            }
            default: throw new IllegalArgumentException("invalid parameters or this function under developping.");
        }
        return result;
    }
    
    public Matrix[] QRdecomposition(String method){
        //QR decomposition
        //InputMatrix-the matrix decomposed.
        //method-the using method to decompose the matrix, including "Schmidt" and "Householder".
        //The return result are the square matrices(q and r),QR[0]=Q, QR[1]=R.
        int row=this.row;
        int col=this.column;
        Matrix[] QR=new Matrix[2];
        QR[0]=new Matrix(row, row);
        QR[1]=new Matrix(row, col);
        if(method.trim().toLowerCase().equals("householder")){
            QR=HouseholderReflection();
        }
        else if(method.trim().toLowerCase().equals("givens")){
            QR=GivensRotation();
        }
        else{//invalid parameter
            //System.out.println("Invalid inputting parameters of QR decomposition function");
            //System.exit(0);
            throw new IllegalArgumentException("Invalid inputting parameters of "
                    + "QR decomposition function");
        }   
        return QR;
    }
    
    private Matrix[] HouseholderReflection(){
        //decomposite the matrix to two matrix(QR) by using Householder reflection method.
        //The return result are the matrices(q and r),QR[0]=Q, QR[1]=R.
        int row=this.row;
        int col=this.column;
        Matrix Q=new Matrix(row, row);
        Matrix R=new Matrix(row, col);
        R.SetMatrixValue(matrix);
        int col1=col-2;
        if(row>col){
            col1=col-1;
        }
        for(int j=0; j<=col1; j++){
            Vector u=new Vector(row);
            for(int i=0; i<row; i++){
                if(i<j){
                    u.SetElementValue(i, 0.0);
                }
                else{
                    u.SetElementValue(i, R.GetElementValue(i, j));
                }
            }
            double temp=0.0;
            if(R.GetElementValue(j, j)<1e-5){
                temp=u.GetNorm("2");
            }
            else{
                temp=Math.signum(R.GetElementValue(j, j))*u.GetNorm("2");
            }
            Vector e=new Vector(row);
            e.SetElementValue(j, 1.0);
            u=u.Subtract(e.MultiplyConst(temp));
            temp=u.GetNorm("2");
            u=u.MultiplyConst(1/temp);
            Matrix H=new Matrix(row, row, "identity");
            H=H.Subtract(u.MultiplyToMatrix(u).MultiplyConst(2.0));
            R=H.Multiply(R);
            if(j==0){
                Q.SetMatrixValue(H.ToArray());
            }
            else{
                Q=Q.Multiply(H);
            }
        }
        //the first element member is the Q matrix.
        //the second element member is the R matrix.
        Matrix[] QR=new Matrix[2];
        QR[0]=Q;
        QR[1]=R;
        return QR;
    }
    
    private Matrix[] GivensRotation(){
        //Using the Givens rotation method to decompose the matrix to Q and R matrixes.
        Matrix Q=new Matrix(this.row, this.row);
        Matrix R=new Matrix(matrix);
        double err=1e-5;
        int step=0;
        for(int j=0; j<Math.min(this.column, this.row); j++){
            for(int i=j+1; i<this.row; i++){
                //if the element value is less than 1e-5, it will converge to zero and skip rotation.
                if(Math.abs(R.GetElementValue(i, j))<err){
                    R.SetElementValue(i, j, 0.0);
                    break;
                }
                else{
                    //caculate the rotation parameters
                    double a=R.GetElementValue(j, j);
                    double b=R.GetElementValue(i, j);
                    //double r=Math.sqrt(a*a+b*b);
                    double cos,sin;
                    if(b==0){
                        cos=Math.copySign(a, 1);sin=0.0;//r=Math.abs(a);
                    }
                    else if(a==0){
                        cos=0.0;sin=Math.copySign(b, 1);//r=Math.abs(b);
                    }
                    else if(Math.abs(a)>Math.abs(b)){
                        double t=b/a;
                        double u=Math.copySign(Math.sqrt(1+t*t), a);
                        cos=1/u;sin=(-1.0)*cos*t;//r=a*u;   
                    }
                    else{
                        double t=a/b;
                        double u=Math.copySign(Math.sqrt(1+t*t), b);
                        sin=(-1.0/u);cos=(-1.0)*sin*t;//r=b*u;
                    }
                    Matrix G=new Matrix(this.row, this.row, "Identity");
                    G.SetElementValue(i, i, cos);
                    G.SetElementValue(j, j, cos);
                    G.SetElementValue(i, j, sin);
                    G.SetElementValue(j, i, -1.0*sin);
                    R=G.Multiply(R);
                    if(step==0){
                        Q.CopyData(G);
                        //Q=Q.Transpose();
                    }
                    else{
                        Q=G.Multiply(Q);
                        //Q=Q.Multiply(G.Transpose());
                    }
                }
                step++;
            }
        }
        Q=Q.Transpose();
        //the first element member is the Q matrix.
        //the second element member is the R matrix.
        Matrix[] QR=new Matrix[2];
        QR[0]=Q;
        QR[1]=R;
        return QR;
    }
    
    @Override
    public String toString(){
        StringBuilder result=new StringBuilder();
        for(int i=0; i<this.row; i++){
            for(int j=0; j<this.column; j++){
                result.append(String.valueOf(matrix[i][j])+"  ");
            }
            result.append("\n");
        }
        return result.toString();
    }
    
    public static void main(String[] args) {
        // Test
        //int row=4;
        //int col=3;
        /*double[][] A={
            {54, 40, 10, 76},
            {47, 20, 94, 49},
            {26, 80, 94, 70},
        };*/
        /*double[][] A={
            {54,40,10},
            {47,20,94},
            {26,80,94},
            {76,49,70}
        };*/
        double[][] A={
            {54, 40, 10, 76},
            {47, 20, 94, 49},
            {26, 80, 94, 70},
            {3,  92, 83, 45}
        };
        Matrix S=new Matrix(A);
        Matrix[] QR=new Matrix[2];
        QR=S.QRdecomposition("householder");
        double[][] res=QR[1].ToArray();
        System.out.println("R matrix:");
        for(int i=0; i<res.length; i++){
            for(int j=0; j<res[0].length; j++){
                System.out.printf("%10.5f", res[i][j]);
                System.out.print("   ");
            }
            System.out.println("");
        }
        System.out.println("---------------------------------------");
        res=QR[0].ToArray();
        System.out.println("Q matrix:");
        for(int i=0; i<res.length; i++){
            for(int j=0; j<res[0].length; j++){
                System.out.printf("%10.5f", res[i][j]);
                System.out.print("   ");
            }
            System.out.println("");
        }
    }
}
