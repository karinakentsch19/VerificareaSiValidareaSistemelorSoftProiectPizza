package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PizzaServiceTest {
    private PizzaService pizzaService;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        File tempFile = tempDir.resolve("payments.txt").toFile();
        tempFile.createNewFile();


        PaymentRepository paymentRepository = new PaymentRepository(tempFile.getAbsolutePath());
        pizzaService = new PizzaService(null, paymentRepository);
    }

    @Test
    public void getTotalAmountTC01() {
        PaymentType paymentType = PaymentType.CASH;
        // payments  = null
        pizzaService = new PizzaService(null, null);
        assertEquals(0.0f, pizzaService.getTotalAmount(paymentType));
    }

    @Test
    public void getTotalAmountTC02() {
        PaymentType paymentType = PaymentType.CASH;
        // payments  = []
        assertEquals(0.0f, pizzaService.getTotalAmount(paymentType));
    }

    @Test
    public void getTotalAmountTC03() {
        PaymentType paymentType = PaymentType.CASH;
        pizzaService.addPayment(1, PaymentType.CASH, 12.3f);
        assertEquals(12.3f, pizzaService.getTotalAmount(paymentType));
    }

    @Test
    public void getTotalAmountTC04() {
        PaymentType paymentType = PaymentType.CARD;
        // payments  = null
        pizzaService = new PizzaService(null, null);
        assertEquals(0.0f, pizzaService.getTotalAmount(paymentType));
    }

    @Test
    public void getTotalAmountTC05() {
        PaymentType paymentType = PaymentType.CARD;
        // payments  = []
        assertEquals(0.0f, pizzaService.getTotalAmount(paymentType));
    }

    @Test
    public void getTotalAmountTC06() {
        PaymentType paymentType = PaymentType.CARD;
        pizzaService.addPayment(1, PaymentType.CASH, 12.3f);
        assertEquals(0.0f, pizzaService.getTotalAmount(paymentType));
    }
}