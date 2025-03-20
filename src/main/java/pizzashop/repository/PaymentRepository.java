package pizzashop.repository;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.utilities.FileLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PaymentRepository {
    private static String filename = "data/payments.txt";
    private List<Payment> paymentList;
    private FileLogger fileLogger = new FileLogger("app.log");

    public PaymentRepository(){
        this.paymentList = new ArrayList<>();
        readPayments();
    }

    public PaymentRepository(String filename){
        this.paymentList = new ArrayList<>();
        PaymentRepository.filename = filename;
        readPayments();
    }

    private void readPayments(){
        File file = new File(filename);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while((line=br.readLine())!=null){
                Payment payment=getPayment(line);
                if(payment != null)
                    paymentList.add(payment);
            }
            br.close();
        } catch (FileNotFoundException e) {
            fileLogger.error("Payments file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            fileLogger.error("Error occurs while reading payments file.");
            e.printStackTrace();
        }
    }

    private Payment getPayment(String line){
        if(line != null && line.matches("[1-8],(CASH|CARD),\\d+(\\.\\d+)?")) {
            StringTokenizer st = new StringTokenizer(line, ",");
            int tableNumber = Integer.parseInt(st.nextToken());
            String type = st.nextToken();
            double amount = Double.parseDouble(st.nextToken());
            return new Payment(tableNumber, PaymentType.valueOf(type), amount);
        }else{
            fileLogger.error("Bad formatted line in payments file: " + line);
            return null;
        }
    }

    public void add(Payment payment){
        if(payment.getTableNumber() < 1 || payment.getTableNumber() > 8)
            throw new RuntimeException("Numarul mesei trebuie sa fie intre 1 si 8");
        if(payment.getAmount() <= 0)
            throw new RuntimeException("Valoarea achitata trebuie sa fie > 0");
        paymentList.add(payment);
        writeAll();
    }

    public List<Payment> getAll(){
        return paymentList;
    }

    public void writeAll(){
        File file = new File(filename);

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            for (Payment p:paymentList) {
                fileLogger.info(p.toString());
                bw.write(p.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            fileLogger.error("Error occurs while writing payments in file.");
            e.printStackTrace();
        }
    }

}