package com.cyan.deliverytracking.domain.model;

import com.cyan.deliverytracking.domain.exeption.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {
    @Test
    void shouldChangeToPlaced() {
        Delivery delivery =  Delivery.draft();

        delivery.editPreparationDetails(createdValidPreparationDetails());

        delivery.place();

        assertEquals(DeliveryStatus.WAITING_FOR_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlacedAt());
    }
@Test
    void shouldNotPlace() {
        Delivery delivery =  Delivery.draft();

        assertThrows(DomainException.class, () -> {
            delivery.place();
        });

        assertEquals(DeliveryStatus.DRAFT, delivery.getStatus());
        assertNull(delivery.getPlacedAt());
    }

    private Delivery.PreparationDetails createdValidPreparationDetails() {
       ContactPoint sender = ContactPoint.builder()
               .zipCode("12345-678")
               .street("Rua Qualquer")
               .number("123")
               .complement("Ed. não é fácil")
               .name("Fulane")
               .phone("(61) 90000-1234")
               .build();

       ContactPoint recipient = ContactPoint.builder()
               .zipCode("12345-123")
               .street("Outra Rua")
               .number("1000")
               .complement("")
               .name("Mario")
               .phone("(71) 98000-4321")
               .build();


        return Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .distanceFee(new BigDecimal("15.00"))
                .courierPayout(new BigDecimal("5.00"))
                .expectedDeliveryTime(Duration.ofHours(5))
                .build();
    }

}