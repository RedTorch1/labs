package io;

import functions.*;
import java.io.*;

public final class FunctionsIO {
    private FunctionsIO() { throw new UnsupportedOperationException(); }

    public static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) {
        PrintWriter pw = new PrintWriter(writer);
        int count = function.getCount();
        pw.println(count);
        for (Point p : function) {
            pw.printf("%f %f\n", p.x, p.y);
        }
        pw.flush();
    }
    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException{
        DataOutputStream dataOutputStream =new DataOutputStream(outputStream);
        int count=function.getCount();
        dataOutputStream.writeInt(count);
        for (Point point : function)
        {
            dataOutputStream.writeDouble(point.x);
            dataOutputStream.writeDouble(point.y);
        }
        dataOutputStream.flush();
    }
}
