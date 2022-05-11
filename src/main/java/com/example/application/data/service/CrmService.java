package com.example.application.data.service;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.example.application.data.repository.CompanyRepository;
import com.example.application.data.repository.ContactRepository;
import com.example.application.data.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final StatusRepository statusRepository;

    // Constructor
    public CrmService(ContactRepository contactRepository,
                      CompanyRepository companyRepository,
                      StatusRepository statusRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
        this.statusRepository = statusRepository;
    }

    // Filter Contacts
    public List<Contact> findAllContacts(String filterText) {
        if(filterText == null || filterText.isEmpty()) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(filterText);
        }
    }

    // Count contacts
    public long countContacts() {
        return contactRepository.count();
    }

    // Delete contact
    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    // Save contact
    public void saveContact(Contact contact) {
        if(contact == null) {
            System.err.println("Contact is null");
            return;
        }
        contactRepository.save(contact);
    }

    // Find All Companies
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    // Find All Statuses
    public List<Status> findAllStatues() {
        return statusRepository.findAll();
    }
}
