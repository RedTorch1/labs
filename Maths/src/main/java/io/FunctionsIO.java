package io;

import functions.*;
import java.io.*;
import functions.factory.TabulatedFunctionFactory;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FunctionsIO {
    private static final Logger logger = LoggerFactory.getLogger(FunctionsIO.class);
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
    public static TabulatedFunction readTabulatedFunction(BufferedInputStream inputStream, TabulatedFunctionFactory factory) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int count = dataInputStream.readInt();
        double[] xValues=new double[count];
        double[] yValues=new double[count];
        for (int i=0; i<count;i++)
        {
            xValues[i]=dataInputStream.readDouble();
            yValues[i]=dataInputStream.readDouble();
        }
        return factory.create(xValues,yValues);
    }

    public static void serialize(BufferedOutputStream stream, TabulatedFunction function) throws IOException {
        logger.debug("Начало сериализации функции типа {}", function.getClass().getSimpleName());
        ObjectOutputStream oos = new ObjectOutputStream(stream);
        oos.writeObject(function);
        oos.flush();
        logger.info("Функция успешно сериализована");
    }

    public static TabulatedFunction deserialize(BufferedInputStream stream)
            throws IOException, ClassNotFoundException {
        logger.debug("Начало десериализации функции");
        ObjectInputStream ois = new ObjectInputStream(stream);
        TabulatedFunction func = (TabulatedFunction) ois.readObject();
        logger.info("Функция типа {} успешно десериализована", func.getClass().getSimpleName());
        return func;
    } //Класс подвергся логированию
}
