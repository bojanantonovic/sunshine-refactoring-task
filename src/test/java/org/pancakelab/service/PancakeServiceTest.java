package org.pancakelab.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.pancakelab.model.Order;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PancakeServiceTest {
    private final PancakeService pancakeService = new PancakeService();
    private Order          order          = null;

    private static final String DARK_CHOCOLATE_PANCAKE_DESCRIPTION           = "Delicious pancake with dark chocolate!";
    private static final String MILK_CHOCOLATE_PANCAKE_DESCRIPTION           = "Delicious pancake with milk chocolate!";
    private static final String MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION = "Delicious pancake with milk chocolate, hazelnuts!";

    @Test
    @org.junit.jupiter.api.Order(10)
    void GivenOrderDoesNotExist_WhenCreatingOrder_ThenOrderCreatedWithCorrectData_Test() {
        // setup

        // exercise
        order = pancakeService.createOrder(10, 20);

        assertEquals(10, order.getBuilding());
        assertEquals(20, order.getRoom());

        // verify

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(20)
    void GivenOrderExists_WhenAddingPancakes_ThenCorrectNumberOfPancakesAdded_Test() {
        // setup

        // exercise
        addPancakes();

        // verify
        final List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(30)
    void GivenPancakesExists_WhenRemovingPancakes_ThenCorrectNumberOfPancakesRemoved_Test() {
        // setup

        // exercise
        pancakeService.removePancakes(DARK_CHOCOLATE_PANCAKE_DESCRIPTION, order.getId(), 2);
        pancakeService.removePancakes(MILK_CHOCOLATE_PANCAKE_DESCRIPTION, order.getId(), 3);
        pancakeService.removePancakes(MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION, order.getId(), 1);

        // verify
        final List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(40)
    void GivenOrderExists_WhenCompletingOrder_ThenOrderCompleted_Test() {
        // setup

        // exercise
        pancakeService.completeOrder(order.getId());

        // verify
        final Set<UUID> completedOrdersOrders = pancakeService.listCompletedOrders();
        assertTrue(completedOrdersOrders.contains(order.getId()));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(50)
    void GivenOrderExists_WhenPreparingOrder_ThenOrderPrepared_Test() {
        // setup

        // exercise
        pancakeService.prepareOrder(order.getId());

        // verify
        final Set<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        final Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertTrue(preparedOrders.contains(order.getId()));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(60)
    void GivenOrderExists_WhenDeliveringOrder_ThenCorrectOrderReturnedAndOrderRemovedFromTheDatabase_Test() {
        // setup
        final List<String> pancakesToDeliver = pancakeService.viewOrder(order.getId());

        // exercise
        final Object[] deliveredOrder = pancakeService.deliverOrder(order.getId());

        // verify
        final Set<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        final Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertFalse(preparedOrders.contains(order.getId()));

        final List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(), ordersPancakes);
        assertEquals(order.getId(), ((Order) deliveredOrder[0]).getId());
        assertEquals(pancakesToDeliver, (List<String>) deliveredOrder[1]);

        // tear down
        order = null;
    }

    @Test
    @org.junit.jupiter.api.Order(70)
    void GivenOrderExists_WhenCancellingOrder_ThenOrderAndPancakesRemoved_Test() {
        // setup
        order = pancakeService.createOrder(10, 20);
        addPancakes();

        // exercise
        pancakeService.cancelOrder(order.getId());

        // verify
        final Set<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        final Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertFalse(preparedOrders.contains(order.getId()));

        final List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(), ordersPancakes);

        // tear down
    }

    private void addPancakes() {
        pancakeService.addDarkChocolatePancake(order.getId(), 3);
        pancakeService.addMilkChocolatePancake(order.getId(), 3);
        pancakeService.addMilkChocolateHazelnutsPancake(order.getId(), 3);
    }
}