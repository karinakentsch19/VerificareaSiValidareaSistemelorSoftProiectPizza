package pizzashop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.io.*;
import java.util.StringTokenizer;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {

    private static Payment paymentEcpTC1;
    private static Payment paymentEcpTC2;
    private static Payment paymentEcpTC3;
    private static Payment paymentEcpTC4;

    private static Payment paymentBvaTC1;
    private static Payment paymentBvaTC2;
    private static Payment paymentBvaTC3;
    private static Payment paymentBvaTC4;

    private static PaymentRepository paymentRepository;


    @BeforeAll
    static void setUp() {
        paymentEcpTC1 = new Payment(5, PaymentType.CASH, 2);
        paymentEcpTC2 = new Payment(3, PaymentType.CASH, 0);
        paymentEcpTC3 = new Payment(0, PaymentType.CASH, 4.2);
        paymentEcpTC4 = new Payment(2, PaymentType.CASH, 12.5);

        paymentBvaTC1 = new Payment(0, PaymentType.CASH, 1);
        paymentBvaTC2 = new Payment(5, PaymentType.CASH, 2);
        paymentBvaTC3 = new Payment(1, PaymentType.CASH, 12.2);
        paymentBvaTC4 = new Payment(3, PaymentType.CASH, 0);

        paymentRepository = new PaymentRepository();
    }

    @AfterEach
    void tearDown() {
    }

    private static Payment getLastAddedPayment(String filename){
        File file = new File(filename);
        BufferedReader br = null;
        Payment payment = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while((line=br.readLine())!=null){
                payment=getPayment(line);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payment;
    }

    private static Payment getPayment(String line){
        if(line != null) {
            StringTokenizer st = new StringTokenizer(line, ",");
            int tableNumber = Integer.parseInt(st.nextToken());
            String type = st.nextToken();
            double amount = Double.parseDouble(st.nextToken());
            return new Payment(tableNumber, PaymentType.valueOf(type), amount);
        }else{
            return null;
        }
    }

    @Test
    public void addPaymentECPTest1(){
        paymentRepository.add(paymentEcpTC1);
        Payment filePayment = getLastAddedPayment("data/payments.txt");
        assertEquals(paymentEcpTC1.getTableNumber(), filePayment.getTableNumber());
        assertEquals(paymentEcpTC1.getAmount(), filePayment.getAmount());
    }

    @Test
    public void addPaymentECPTest2(){
        paymentRepository.add(paymentEcpTC2);
        Payment filePayment = getLastAddedPayment("data/payments.txt");
        assertNotEquals(paymentEcpTC2.getAmount(), filePayment.getAmount());
    }

    @Test
    public void addPaymentECPTest3(){
        paymentRepository.add(paymentEcpTC3);
        Payment filePayment = getLastAddedPayment("data/payments.txt");
        assertNotEquals(paymentEcpTC3.getTableNumber(), filePayment.getTableNumber());
    }

    @Test
    public void addPaymentECPTest4(){
        paymentRepository.add(paymentEcpTC4);
        Payment filePayment = getLastAddedPayment("data/payments.txt");
        assertEquals(paymentEcpTC4.getTableNumber(), filePayment.getTableNumber());
        assertEquals(paymentEcpTC4.getAmount(), filePayment.getAmount());
    }

    @Test
    public void addPaymentBVATest1(){
        paymentRepository.add(paymentBvaTC1);
        Payment filePayment = getLastAddedPayment("data/payments.txt");
        assertNotEquals(paymentBvaTC1.getTableNumber(), filePayment.getTableNumber());
    }

    @Test
    public void addPaymentBVATest2(){
        paymentRepository.add(paymentBvaTC2);
        Payment filePayment = getLastAddedPayment("data/payments.txt");
        assertEquals(paymentBvaTC2.getTableNumber(), filePayment.getTableNumber());
        assertEquals(paymentBvaTC2.getAmount(), filePayment.getAmount());
    }
    @Test
    public void addPaymentBVATest3(){
        paymentRepository.add(paymentBvaTC3);
        Payment filePayment = getLastAddedPayment("data/payments.txt");
        assertEquals(paymentBvaTC3.getTableNumber(), filePayment.getTableNumber());
        assertEquals(paymentBvaTC3.getAmount(), filePayment.getAmount());
    }
    @Test
    public void addPaymentBVATest4(){
        paymentRepository.add(paymentBvaTC4);
        Payment filePayment = getLastAddedPayment("data/payments.txt");
        assertNotEquals(paymentBvaTC4.getAmount(), filePayment.getAmount());
    }


}