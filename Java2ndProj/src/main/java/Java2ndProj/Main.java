/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Java2ndProj;

/**
 *
 * @author ZK
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws DifferentSizeException {
        String coba = "1 -2 3 4";
        MatrixO mat = new MatrixO(2, 2, coba);
        MatrixO mat1 = new MatrixO(1, 2, coba);
        MatrixO mat2 = mat.odd();
        
        mat.printM();
//        mat1.printM();
        mat2.printM();
        
        MatrixO mul = mat.mult(mat1);
        mul.printM();
        // TODO code application logic here
    }
    
}
