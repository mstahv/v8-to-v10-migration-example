package my.vaadin.app.data.v10app;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import my.vaadin.app.data.CustomerStatus;
import my.vaadin.app.data.CustomerService;
import my.vaadin.app.data.Customer;

public class CustomerForm extends VerticalLayout {

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email");
    private ComboBox<CustomerStatus> status = new ComboBox<>("Status");
    private DatePicker birthdate = new DatePicker("Birthday");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private CustomerService service = CustomerService.getInstance();
    private Customer customer;
    private MainView mainUI;
    private Binder<Customer> binder = new Binder<>(Customer.class);
    
    
    public CustomerForm() {
        this(null);
    }

    public CustomerForm(MainView myUI) {
        this.mainUI = myUI;

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        add(firstName, lastName, email, status, birthdate, buttons);

        status.setItems(CustomerStatus.values());
//        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
//        save.setClickShortcut(KeyCode.ENTER);
        save.getElement().getThemeList().add("primary");

        binder.bindInstanceFields(this);

        save.addClickListener(e -> this.save());
        delete.addClickListener(e -> this.delete());
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        binder.setBean(customer);

        // Show delete button for only customers already in the database
        delete.setVisible(customer.isPersisted());
        setVisible(true);
        firstName.focus();
    }

    private void delete() {
        service.delete(customer);
        setVisible(false);
        notifyMainView();
    }

    private void save() {
        service.save(customer);
        notifyMainView();
    }
    
    private void notifyMainView() {
        if(mainUI != null) {
            mainUI.updateList();
        }
    }

}
