package pizzashop.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.io.*;
import java.nio.file.Path;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    private static File tempFile; // Store the reference to the temporary file

    @BeforeAll
    static void setUp(@TempDir Path tempDir) throws IOException {
        tempFile = tempDir.resolve("payments.txt").toFile();
        tempFile.createNewFile();

        paymentEcpTC1 = new Payment(5, PaymentType.CASH, 2);
        paymentEcpTC2 = new Payment(3, PaymentType.CASH, 0);
        paymentEcpTC3 = new Payment(0, PaymentType.CASH, 4.2);
        paymentEcpTC4 = new Payment(2, PaymentType.CASH, 12.5);

        paymentBvaTC1 = new Payment(0, PaymentType.CASH, 1);
        paymentBvaTC2 = new Payment(5, PaymentType.CASH, 2);
        paymentBvaTC3 = new Payment(1, PaymentType.CASH, 12.2);
        paymentBvaTC4 = new Payment(3, PaymentType.CASH, 0);

        paymentRepository = new PaymentRepository(tempFile.getAbsolutePath());
    }

    private static Payment getLastAddedPayment() {
        //File file = new File(String.valueOf(tempFile));
        BufferedReader br = null;
        Payment payment = null;
        try {
            br = new BufferedReader(new FileReader(tempFile));
            String line = null;
            while ((line = br.readLine()) != null) {
                payment = getPayment(line);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payment;
    }

    private static Payment getPayment(String line) {
        if (line != null) {
            StringTokenizer st = new StringTokenizer(line, ",");
            int tableNumber = Integer.parseInt(st.nextToken());
            String type = st.nextToken();
            double amount = Double.parseDouble(st.nextToken());
            return new Payment(tableNumber, PaymentType.valueOf(type), amount);
        } else {
            return null;
        }
    }

    private static Stream<Payment> provideEcpPayments() {
        return Stream.of(paymentEcpTC1, paymentEcpTC4);
    }

    private static Stream<Payment> provideBvaPayments() {
        return Stream.of(paymentBvaTC2, paymentBvaTC3);
    }

    @AfterEach
    void tearDown() {
    }

    @Tag("ECP_VALID")
    @DisplayName("ECP VALID Test Cases for Add Payment method")
    @ParameterizedTest
    @MethodSource("provideEcpPayments")
    public void addPaymentECPTest1(Payment payment) {
        paymentRepository.add(payment);
        Payment filePayment = getLastAddedPayment();
        assertEquals(payment.getTableNumber(), filePayment.getTableNumber());
        assertEquals(payment.getAmount(), filePayment.getAmount());
    }

    @Test
    @Tag("ECP_NEVALID")
    @DisplayName("ECP TC2 for Add Payment method")
    public void addPaymentECPTest2() {
        try {
            paymentRepository.add(paymentEcpTC2);
            assert false;
        }catch (RuntimeException e){
            assert true;
        }
        Payment filePayment = getLastAddedPayment();
        assert(filePayment == null || paymentEcpTC2.getAmount() != filePayment.getAmount());
    }

//    @Test
//    @Tag("ECP VALID")
//    @DisplayName("ECP TC4 for Add Payment method")
//    public void addPaymentECPTest4(){
//        paymentRepository.add(paymentEcpTC4);
//        Payment filePayment = getLastAddedPayment("data/payments.txt");
//        assertEquals(paymentEcpTC4.getTableNumber(), filePayment.getTableNumber());
//        assertEquals(paymentEcpTC4.getAmount(), filePayment.getAmount());
//    }

    @Test
    @Tag("ECP_NEVALID")
    @DisplayName("ECP TC3 for Add Payment method")
    public void addPaymentECPTest3() {
        try {
            paymentRepository.add(paymentEcpTC3);
            assert false;
        }catch (RuntimeException e){
            assert true;
        }
        Payment filePayment = getLastAddedPayment();
        assertNotEquals(paymentEcpTC3.getTableNumber(), filePayment.getTableNumber());
    }

    @Test
    @Tag("BVA_NEVALID")
    @DisplayName("BVA TC1 for Add Payment method")
    public void addPaymentBVATest1() {
        try {
            paymentRepository.add(paymentBvaTC1);
            assert false;
        }catch (RuntimeException e){
            assert true;
        }
        Payment filePayment = getLastAddedPayment();
        assert(filePayment == null || paymentBvaTC1.getTableNumber() != filePayment.getTableNumber());
    }

    @ParameterizedTest
    @MethodSource("provideBvaPayments")
    @Tag("BVA_VALID")
    @DisplayName("BVA VALID Test Cases for Add Payment method")
    public void addPaymentBVATest2(Payment payment) {
        paymentRepository.add(payment);
        Payment filePayment = getLastAddedPayment();
        assertEquals(payment.getTableNumber(), filePayment.getTableNumber());
        assertEquals(payment.getAmount(), filePayment.getAmount());
    }

    //    @Test
//    @Tag("BVA_VALID")
//    @DisplayName("BVA TC3 for Add Payment method")
//    public void addPaymentBVATest3(){
//        paymentRepository.add(paymentBvaTC3);
//        Payment filePayment = getLastAddedPayment("data/payments.txt");
//        assertEquals(paymentBvaTC3.getTableNumber(), filePayment.getTableNumber());
//        assertEquals(paymentBvaTC3.getAmount(), filePayment.getAmount());
//    }
    @Test
    @Tag("BVA_NEVALID")
    @DisplayName("BVA TC4 for Add Payment method")
    public void addPaymentBVATest4() {
        try {
            paymentRepository.add(paymentBvaTC4);
            assert false;
        }catch (RuntimeException e){
            assert true;
        }
        Payment filePayment = getLastAddedPayment();
        assert(filePayment == null || paymentBvaTC4.getAmount() != filePayment.getAmount());
    }


}