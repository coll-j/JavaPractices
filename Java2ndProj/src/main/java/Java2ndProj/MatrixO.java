/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Java2ndProj;

import java.util.Random;

/**
 *
 * @author ZK
 */
public class MatrixO {
    
    private double[][] matrix;
    private int n,m;
    
    private Random r = new Random();
    public int getN() { return n; }
    public int getM() { return m; }
    
    // constructors
    public MatrixO()
    {
        n = 2 + r.nextInt(9);
        m = 2 + r.nextInt(9);
        
        matrix = new double[n][m];
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < m; j++)
            {
                matrix[i][j] = -20 + r.nextInt(41);
            }
        }
    }
    
    public MatrixO(int mm, int nn)
    {
        m = mm;
        n = nn;
        
        matrix = new double[n][m];
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < m; j++)
            {
                matrix[i][j] = -30 + r.nextInt(61);
            }
        }
    }
    
    public MatrixO(int mm, int nn, String a)
    {
        m = mm;
        n = nn;
        
        matrix = new double[n][m];
        int index = 0;
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < m; j++)
            {
                int f = 10;
                boolean dec = false;
                boolean min = false;
                double temp = 0;
                while(index < a.length() && a.charAt(index) != ' ')
                {
                    if(a.charAt(index) == '.') dec = true;
                    if(a.charAt(index) == '-') min = true;
//                    System.out.println("temp: " + temp + " char: " + a.charAt(index));
                    if(!dec)temp *= f;
                    if(a.charAt(index) >= '0' && a.charAt(index) <= '9')
                    {
                        if(dec)
                        {
                            temp += Character.getNumericValue(a.charAt(index))/f;
                            f *= 10;
                        }
                        else
                            temp += Character.getNumericValue(a.charAt(index));
                    }
                    index++;
                }
                index++;
                
                if(min)
                {
                    temp *= -1;
                    min = false;
                }
                
                matrix[i][j] = temp;
            }
        }
    }
    
    public double elem_at(int n, int m)
    {
        return matrix[n][m];
    }
    
    // print functions
    public void print(int n, int m)
    {
        System.out.println(matrix[n][m]);
    }
    
    public void printM()
    {
        System.out.println();

        for(int i = 0; i < n; i++)
        {
            for(int p = 0; p < m; p++)
            {
                System.out.print(matrix[i][p] + " ");
            }
            System.out.println();
        }
    }
    
    public void printG(double wart)
    {
        for(int i = 0; i < n; i++)
        {
            for(int p = 0; p < m; p++)
            {
                if(matrix[i][p] > wart)System.out.print(matrix[i][p] + " ");
            }
        }
    }
    
    // matrices functions
    public MatrixO add (MatrixO x) throws DifferentSizeException
    {
        String res = "";
        if(x.getN() != this.n || x.getM() != this.m)
            throw new DifferentSizeException("Matrices size must be the same");
        else
        {
            for(int i = 0; i < n; i++)
            {
                for(int p = 0; p < m; p++)
                {
                    res += matrix[i][p] + x.elem_at(i, p);
                    res += " ";
                }
            }
        }
        return new MatrixO(m, n, res);
    }
    
    public MatrixO sub(MatrixO x) throws DifferentSizeException
    {
        String res = "";
        if(x.getN() != this.n || x.getM() != this.m)
            throw new DifferentSizeException("Matrices size must be the same");
        else
        {
            for(int i = 0; i < n; i++)
            {
                for(int p = 0; p < m; p++)
                {
                    res += matrix[i][p] - x.elem_at(i, p);
                    res += " ";
                }
            }
        }
        return new MatrixO(m, n, res);
    }
    
    public MatrixO odd ()
    {
        String res = "";
        for(int i = 0; i < n; i++)
        {
            for(int p = 0; p < m; p++)
            {
                res += -matrix[i][p];
                res += " ";
            }
        }
        return new MatrixO(m, n, res);
    }
    
    public MatrixO mult(MatrixO x) throws DifferentSizeException
    {
        String res = "";
        if(x.getN() != this.m)
            throw new DifferentSizeException("First matrix column and second matrix row must be the same");
        else
        {
            for(int tn = 0; tn < n; tn++)
            {
                for(int xm = 0; xm < x.getM(); xm++)
                {
                    double temp = 0;
                    for(int tm = 0; tm < m; tm++)
                    {
//                        System.out.println("mat: " + matrix[tn][tm] + " x: " + x.elem_at(tm,xm));
                        temp += matrix[tn][tm] * x.elem_at(tm,xm);
                    }
//                    System.out.println("temp: " + temp);
                    res+= temp;
                    res+= " ";
//                    System.out.println(res);
                }
            }
        }
        return new MatrixO(x.getM(), n, res);
    }
}

class DifferentSizeException extends Exception
{
    public DifferentSizeException(String msg)
    {
        super(msg);
    }
}
