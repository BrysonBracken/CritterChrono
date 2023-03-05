package com.udacity.jdnd.course3.critter.Services;

import com.udacity.jdnd.course3.critter.models.Customer;
import com.udacity.jdnd.course3.critter.models.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer getCustomerByPetId(Pet pet){
        Customer customer = customerRepository.findCustomerByPets(pet);
        if (customer == null){
            System.out.println("cust is a dud");
        }else { System.out.println("cust was found");}
        return customer;
    }

    public Customer getCustomer(Long id){
        Optional<Customer> theCustomer = customerRepository.findById(id);
        if (!theCustomer.isPresent()){
            throw new IllegalArgumentException("No customer found");
        }else {
            return theCustomer.get();
        }
    }
}
