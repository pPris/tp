package seedu.cakecollate.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.cakecollate.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.cakecollate.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.cakecollate.testutil.Assert.assertThrows;
import static seedu.cakecollate.testutil.TypicalIndexes.INDEX_FIRST_ORDER;
import static seedu.cakecollate.testutil.TypicalIndexes.INDEX_THIRD_ORDER;
import static seedu.cakecollate.testutil.TypicalOrderItems.getTypicalOrderItemsModel;
import static seedu.cakecollate.testutil.TypicalOrders.getTypicalCakeCollate;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.cakecollate.commons.core.GuiSettings;
import seedu.cakecollate.commons.core.Messages;
import seedu.cakecollate.commons.core.index.IndexList;
import seedu.cakecollate.logic.commands.exceptions.CommandException;
import seedu.cakecollate.model.CakeCollate;
import seedu.cakecollate.model.Model;
import seedu.cakecollate.model.ModelManager;
import seedu.cakecollate.model.ReadOnlyCakeCollate;
import seedu.cakecollate.model.ReadOnlyUserPrefs;
import seedu.cakecollate.model.UserPrefs;
import seedu.cakecollate.model.order.Order;
import seedu.cakecollate.model.orderitem.OrderItem;
import seedu.cakecollate.testutil.AddOrderDescriptorBuilder;
import seedu.cakecollate.testutil.OrderBuilder;
import seedu.cakecollate.testutil.OrderItemBuilder;


/**
 * todo: Tests
 * - order desc that already exists in order item model
 * - new order desc + existing order desc + order index
 *
 * check if equivalent tests exist in parser
 */

public class AddCommandTest {
    private Model model = new ModelManager(getTypicalCakeCollate(), getTypicalOrderItemsModel(), new UserPrefs());
    private IndexList nonNullIndexList = new IndexList(new ArrayList<>());
    // todo should i pass in empty index list instead of null? is there a good practice

    @BeforeEach
    public void setUp() {
        nonNullIndexList.add(INDEX_FIRST_ORDER);
        nonNullIndexList.add(INDEX_THIRD_ORDER);
    }

    @Test
    public void constructor_nullAddOrderDescriptor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null, null));
        assertThrows(NullPointerException.class, () -> new AddCommand(nonNullIndexList, null));
    }

    @Test
    public void invalidListIndex_uhh() throws CommandException {
        // need to create own order item model -- or check edit tests
        Model emptyModel = new ModelManager();
        AddCommand.AddOrderDescriptor descriptor = new AddOrderDescriptorBuilder(new OrderBuilder().withOrderDescriptions().build()).build();

        Command addCommand = new AddCommand(nonNullIndexList, descriptor);

        assertCommandFailure(addCommand, emptyModel, Messages.MESSAGE_INVALID_ORDER_ITEM_INDEX);
    }

    @Test
    public void execute_inputNewOrderDescription_addedToOrderItemModel() {
        // need to create own order item model -- or check edit tests

        String value = "cake that does not exist in model yet";
        Order order = new OrderBuilder().withOrderDescriptions(value).build(); // cry names don't make sense
        AddCommand.AddOrderDescriptor descriptor = new AddOrderDescriptorBuilder(order).build();

        OrderItem newOrderItemToAdd = new OrderItemBuilder().withType(value).build();
        // does this stuff go into dg, e.g. how to test - use builders

        assert !model.hasOrderItem(newOrderItemToAdd)
                : "corresponding order description shouldn't be in the order item model";

        // int initialSize = model.getOrderItemLists.size();

        Command addCommand = new AddCommand(null, descriptor);

        Model expectedModel = new ModelManager(
                new CakeCollate(model.getCakeCollate()),
                //new OrderItems(model.getOrderItems()),
                getTypicalOrderItemsModel(),
                new UserPrefs()
        );

        expectedModel.addOrder(order);
        expectedModel.addOrderItem(newOrderItemToAdd);
        String expectedMessage = String.format(AddCommand.MESSAGE_SUCCESS, order);

        assertCommandSuccess(addCommand, model, expectedMessage, expectedModel);
        assertTrue(model.hasOrderItem(newOrderItemToAdd));
        // assertTrue()
        // assertEquals size has increased by one

    }

    @Test
    public void execute_orderAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingOrderAdded modelStub = new ModelStubAcceptingOrderAdded();
        Order validOrder = new OrderBuilder().build();
        AddCommand.AddOrderDescriptor descriptor = new AddOrderDescriptorBuilder(validOrder).build();

        CommandResult commandResult = new AddCommand(null, descriptor).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validOrder), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validOrder), modelStub.ordersAdded);
    }

    @Test
    public void execute_duplicateOrder_throwsCommandException() {
        Order validOrder = new OrderBuilder().build();
        AddCommand.AddOrderDescriptor descriptor = new AddOrderDescriptorBuilder(validOrder).build();
        AddCommand addCommand = new AddCommand(null, descriptor);
        ModelStub modelStub = new ModelStubWithOrder(validOrder);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_ORDER, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Order alice = new OrderBuilder().withName("Alice").build();
        Order bob = new OrderBuilder().withName("Bob").build();
        AddCommand.AddOrderDescriptor descriptorAlice = new AddOrderDescriptorBuilder(alice).build();
        AddCommand.AddOrderDescriptor descriptorBob = new AddOrderDescriptorBuilder(bob).build();
        AddCommand addAliceCommand = new AddCommand(null, descriptorAlice);
        AddCommand addBobCommand = new AddCommand(null, descriptorBob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(null, descriptorAlice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different order -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));

        // same order, different index list -> return false
        // todo

        // same order, null index list -> return false
        // todo

    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getCakeCollateFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setCakeCollateFilePath(Path cakeCollateFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addOrder(Order order) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setCakeCollate(ReadOnlyCakeCollate cakeCollate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyCakeCollate getCakeCollate() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasOrder(Order order) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteOrder(Order target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setOrder(Order target, Order editedOrder) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Order> getFilteredOrderList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortFilteredOrderList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredOrderList(Predicate<Order> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasOrderItem(OrderItem item) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addOrderItem(OrderItem item) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single order.
     */
    private class ModelStubWithOrder extends ModelStub {
        private final Order order;

        ModelStubWithOrder(Order order) {
            requireNonNull(order);
            this.order = order;
        }

        @Override
        public boolean hasOrder(Order order) {
            requireNonNull(order);
            return this.order.isSameOrder(order);
        }
    }

    /**
     * A Model stub that always accept the order being added.
     */
    private class ModelStubAcceptingOrderAdded extends ModelStub {
        final ArrayList<Order> ordersAdded = new ArrayList<>();
        final ArrayList<OrderItem> orderItemsStub = new ArrayList<>();

        @Override
        public boolean hasOrder(Order order) {
            requireNonNull(order);
            return ordersAdded.stream().anyMatch(order::isSameOrder);
        }

        @Override
        public void addOrder(Order order) {
            requireNonNull(order);
            ordersAdded.add(order);
        }

        @Override
        public ReadOnlyCakeCollate getCakeCollate() {
            return new CakeCollate();
        }

        // ======= NEEDED TO ADD NEW ORDER DESCRIPTIONS INTO ORDER ITEM MODEL

        @Override
        public boolean hasOrderItem(OrderItem item) {
            requireNonNull(item);
            return orderItemsStub.stream().anyMatch(item::isSameOrderItem);
        }

        @Override
        public void addOrderItem(OrderItem item) {
            orderItemsStub.add(item);
            // updateFilteredOrderList(PREDICATE_SHOW_ALL_ORDERS); ?
        }
    }

}
