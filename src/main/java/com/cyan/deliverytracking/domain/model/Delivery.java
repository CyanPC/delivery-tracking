package com.cyan.deliverytracking.domain.model;

public class Delivery {

    @EqualsAndHashCode.Include
    private UUID id;

    private UUID courierId;

    private DeliveryStatus status;

    private OffsetDateTime placedAt;
    private OffsetDateTime assignedAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fulfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    private ContactPoint sender;
    private List<ContactPoint> recipients;

    private List<Item> items = new ArrayList<>();

    public static Delivery draft() {
        var delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.DRAFT);
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setDistanceFee(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        return delivery;
    }

    public UUID addItem(String name, int quantity) {
        var item = Item.brandNew(name, quantity);
        items.add(item);
        calculateTotalItems();
        return item.getId();
    }

    public void removeItem(UUID itemId) {
        items.removeIf(item -> item.getId().equals(itemId));
        calculateTotalItems();
    }

    public void removeItem() {
        items.clear();
        calculateTotalItems();
    }

    void place() {
        this.setStatus(DeliveryStatus.WAITING_FOR_COURIER);
        this.setPlacedAt(OffsetDateTime.now());
    }

    public void pickUp(UUID courierId) {
        this.setCourierId(courierId);
        this.setStatus(DeliveryStatus.IN_TRANSIT);
        this.setAssignedAt(OffsetDateTime.now());
    }

    public void markAsDelivered() {
        this.setStatus(DeliveryStatus.DELIVERY);
        this.setAssignedAt(OffsetDateTime.now());
    }

    void changeItemQuantity(UUID itemId, int quantity) {
        var item = getItems().stream().filter(i -> i.getId().equals(itemId)).findFirst().orElseThrow();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    private void calculateTotalItems() {
        int totalItems = items.stream().mapToInt(Item::getQuantity).sum();
        setTotalItems(totalItems);
    }
}
