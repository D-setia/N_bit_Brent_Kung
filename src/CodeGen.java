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

        for(int i=1; Math.pow(2,i)<=n;i++) {
            int k =(int)Math.pow(2,i-1);
            for(int j=k-1; j<n; j+=2*k) {
                if(j+k<n){
                    runOperation(cols[j],cols[j+k]);
                }
            }
        }

        for(int i=2; i<n;i*=2) {
            int k =n/i;
            int l=k/2;
            for(int j=k-1; j<n; j+=k) {
                if(j+l<n){
                    runOperation(cols[j],cols[j+l]);
                }
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
        System.out.println("assign g=a&b; \n\n");
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

    private static int operationNo=0;

    private static void runOperation(Col col1,Col col2) {
        String oldWire1, oldWire2,oldWire3, oldWire4;
        oldWire1="cp_"+col1.levelCounter+"_"+col1.colNo;
        oldWire2="cg_"+col1.levelCounter+"_"+col1.colNo;
        oldWire3="cp_"+col2.levelCounter+"_"+col2.colNo;
        oldWire4="cg_"+col2.levelCounter+"_"+col2.colNo;

        col2.levelCounter++;
        String newWire1, newWire2;
        newWire1="cp_"+col2.levelCounter+"_"+col2.colNo;
        newWire2="cg_"+col2.levelCounter+"_"+col2.colNo;
        System.out.println("wire "+newWire1+";");
        System.out.println("wire "+newWire2+";");
        System.out.println("GenNew"+" genNew_"+operationNo+"("+oldWire1+","+oldWire2+","+oldWire3+","+oldWire4+","+newWire1+","+newWire2+"); \n");
        operationNo++;
    }

    private static void assignSum(Col []cols) {
        System.out.println("assign sum[0] = p[0];");
        for(int i=1; i<cols.length;i++) {
            System.out.println("assign sum["+cols[i].colNo+"]=p["+cols[i].colNo+"]^cg_"+cols[i-1].levelCounter+"_"+cols[i-1].colNo+";");
        }
        System.out.println("assign sum["+cols.length+"]=cg_"+cols[cols.length-1].levelCounter+"_"+cols[cols.length-1].colNo+";");
    }

}

class Col {
    int levelCounter;
    int colNo;
}
