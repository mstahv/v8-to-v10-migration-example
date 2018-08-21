package my.vaadin.app.v8app;

import my.vaadin.app.data.CustomerStatus;
import my.vaadin.app.data.CustomerService;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import my.vaadin.app.data.Customer;

public class CustomerForm extends FormLayout {

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email");
    private NativeSelect<CustomerStatus> status = new NativeSelect<>("Status");
    private DateField birthdate = new DateField("Birthday");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private CustomerService service = CustomerService.getInstance();
    private Customer customer;
    private MainUI mainUI;
    private Binder<Customer> binder = new Binder<>(Customer.class);
    
    
    public CustomerForm() {
        this(null);
    }

    public CustomerForm(MainUI myUI) {
        this.mainUI = myUI;

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        addComponents(firstName, lastName, email, status, birthdate, buttons);

        status.setItems(CustomerStatus.values());
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(KeyCode.ENTER);

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
        firstName.selectAll();
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
