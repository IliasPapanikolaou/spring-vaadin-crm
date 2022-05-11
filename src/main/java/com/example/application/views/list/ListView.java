package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.context.annotation.Scope;

import javax.annotation.security.PermitAll;

 @org.springframework.stereotype.Component // For integration testing
 @Scope("prototype") // For integration testing
@PageTitle("Contacts | Vaadin CRM")
@Route(value = "", layout = MainLayout.class)
@PermitAll // Allow all logged-in users to access this resource
public class ListView extends VerticalLayout {

    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    ContactForm form;
    private final CrmService service;

    // Constructor
    public ListView(CrmService service) {
        this.service = service;

        addClassName("list-view"); // Class name for CSS
        setSizeFull(); // Take all available space

        configureGrid();
        configureForm();

        add(getToolBar(), getContent());

        // Assign data to grid and use contact filter
        updateList();

        // Close editor
        closeEditor();
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid); // 2/3 of the space for the grid
        content.setFlexGrow(1, form); // 1/3 of the space for the form
        content.addClassName("content"); // Class name for CSS
        content.setSizeFull(); // Get all available space

        return content;
    }

    private void configureForm() {
        form = new ContactForm(service.findAllCompanies(), service.findAllStatues());
        form.setWidth("25em");

        form.addListener(ContactForm.SaveEvent.class, this::saveContact);
        form.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        form.addListener(ContactForm.CloseEvent.class, event -> closeEditor());
    }

    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    private Component getToolBar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true); // Enable clear button
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // Do not fetch every keystroke from database
        // Update list in every filter filed entry
        filterText.addValueChangeListener(event -> updateList());

        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(event -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton); // Horizontal Layout
        toolbar.addClassName("toolbar"); // Class name for CSS

        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void configureGrid() {
        grid.addClassName("contact-grid"); // Class name for CSS
        grid.setSizeFull(); // Take all available space
        grid.setColumns("firstName", "lastName", "email"); // Set data from entity to grid
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status"); // Set custom headers
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        grid.getColumns().forEach(col -> col.setAutoWidth(true)); // Auto resize
        // Single component select - action when selecting items from grid
        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
    }

    private void editContact(Contact contact) {
        // Assign item values to form
        if(contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

}
