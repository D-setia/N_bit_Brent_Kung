import java.util.*;
import java.lang.*;

public class CodeGen {
    public static void main(String []args) {

        int n ;
        Scanner in = new Scanner(System.in);
        n = in.nextInt();

        setGenAndProp(n);

        Col[] cols= new Col[n];
        setInfo(cols);

        for(int i=1; i<n;i*=2) {
            int k =(int)Math.pow(2,i);
            for(int j=0; j<n/k; j+=k) {
                runOperation(cols[j+k-1],cols[j+k/2-1]);
            }
        }

        for(int i=1; i<n;i*=2) {
            int k =(int)Math.pow(2,i);
            for(int j=0; 3*n/(2*k)+j-1<n; j+=n/k) {
                runOperation(cols[3*n/(2*k)+j-1],cols[j+n/k-1]);
            }
        }

        assignSum(cols);

        System.out.println("endmodule");

    }

    private static void setGenAndProp(int n) {
        System.out.println("`timescale 1s / 1ps \n");
        System.out.println("module Brent_Kung_"+n+"(a,b,sum); \n");
        System.out.println("input ["+(n-1)+":0] a;");
        System.out.println("input ["+(n-1)+":0] b;");
        System.out.println("output ["+n+":0] sum; \n");
        System.out.println("wire ["+(n-1)+":0] p;");
        System.out.println("wire ["+(n-1)+":0] g; \n");
        System.out.println("assign p=a^b;");
        System.out.println("assign g=a^b; \n\n");
    }

    private static void setInfo(Col[] cols){
        for(int i=0; i<cols.length;i++) {
            cols[i] = new Col();
            cols[i].levelCounter=0;
            cols[i].colNo=i;
            System.out.println("wire cp_0"+"_"+i+"="+"p["+i+"]"+";");
            System.out.println("wire cg_0"+"_"+i+"="+"g["+i+"]"+";");
        }
    }

    private static int opNo=0;

    private static void runOperation(Col col1,Col col2) {
        String oldWire1, oldWire2,oldWire3, oldWire4;
        oldWire1="cp_"+col1.levelCounter+"_"+col1.colNo;
        oldWire2="cg_"+col1.levelCounter+"_"+col1.colNo;
        oldWire3="cp_"+col2.levelCounter+"_"+col2.colNo;
        oldWire4="cg_"+col2.levelCounter+"_"+col2.colNo;

        col1.levelCounter++;
        String newWire1, newWire2;
        newWire1="cp_"+col1.levelCounter+"_"+col1.colNo;
        newWire2="cg_"+col1.levelCounter+"_"+col1.colNo;
        System.out.println("wire "+newWire1+";");
        System.out.println("wire "+newWire2+";");
        System.out.println("GenNew"+" genNew_"+opNo+"("+oldWire1+","+oldWire2+","+oldWire3+","+oldWire4+","+newWire1+","+newWire2+"); \n");
        opNo++;
    }

    private static void assignSum(Col []cols) {
        System.out.println("assign sum[0] = p[0];");
        for(int i=1; i<cols.length;i++) {
            System.out.println("assign sum["+cols[i].colNo+"]=p["+cols[i].colNo+"]^cg_"+cols[i-1].levelCounter+"_"+cols[i-1].colNo+";");
        }
        System.out.println("assign sum["+cols.length+"]=cp_"+cols[cols.length-1].levelCounter+"_"+cols[cols.length-1].colNo+";");
    }

}

class Col {
    int levelCounter;
    int colNo;
}
