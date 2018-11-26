package myPackage;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Map;

public class Solver {

    SAT sat;

    public Solver (){

    }

    public Boolean Solve()
    {
        for(int i = 1; i < 13; i++) {

            long start = System.currentTimeMillis();

            sat  = new SAT(i);

            Result r = sat.Solve(sat.GetClauses(), sat.GetSituation());

            long end = System.currentTimeMillis();

            if (r == null) {
                System.out.println(i + ". file is not satisfiable ! " + "Timer: " +  (end - start) + "ms");
            } else {
                System.out.println(i + ". file is satisfiable ! " + "Timer: " + (end - start) + "ms");

                PrintWriter out = null;
                try {
                    out = new PrintWriter(new File("map" + i + ".sol"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                for (Map.Entry<Integer, Boolean> entry : r.getResult().entrySet()) {
                    if(entry.getValue() == false)
                    {
                        out.println(entry.getKey() + " " + "0");
                    }
                    else
                    {
                        out.println(entry.getKey() + " " + "1");
                    }
                }

                out.close();

            }

        }
        return true;
    }


}
