package io;

import functions.*;
import java.io.*;
import functions.factory.TabulatedFunctionFactory;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

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

    public static TabulatedFunction readTabulatedFunction(BufferedReader reader, TabulatedFunctionFactory factory) throws IOException {
        String line = reader.readLine();
        if (line == null) throw new IOException("No data");
        int count = Integer.parseInt(line.trim());
        double[] xs = new double[count];
        double[] ys = new double[count];
        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("ru"));
        for (int i = 0; i < count; i++) {
            line = reader.readLine();
            if (line == null) throw new IOException("Unexpected EOF");
            String[] parts = line.trim().split("\\s+");
            try {
                xs[i] = nf.parse(parts[0]).doubleValue();
                ys[i] = nf.parse(parts[1]).doubleValue();
            } catch (ParseException e) {
                throw new IOException(e);
            }
        }
        return factory.create(xs, ys);
    }
}
