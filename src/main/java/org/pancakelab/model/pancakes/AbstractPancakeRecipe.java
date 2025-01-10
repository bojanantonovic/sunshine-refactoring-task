package org.pancakelab.model.pancakes;

import java.util.UUID;

public abstract class AbstractPancakeRecipe implements PancakeRecipe {
    private UUID orderId;

    @Override
    public final UUID getOrderId() {
        return orderId;
    }

    @Override
    public final void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

}
