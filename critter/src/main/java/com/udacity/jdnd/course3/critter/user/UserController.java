package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.Services.CustomerService;
import com.udacity.jdnd.course3.critter.Services.EmployeeService;
import com.udacity.jdnd.course3.critter.Services.PetService;
import com.udacity.jdnd.course3.critter.Services.ScheduleService;
import com.udacity.jdnd.course3.critter.models.Customer;
import com.udacity.jdnd.course3.critter.models.Employee;
import com.udacity.jdnd.course3.critter.models.Pet;
import com.udacity.jdnd.course3.critter.models.Schedule;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ScheduleService scheduleService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        List<Pet> pets = new ArrayList<>();
        System.out.println(customerDTO.getId() + customerDTO.getName() + customerDTO.getPetIds());
        if (customerDTO.getPetIds() != null) {
            for (Long petId : customerDTO.getPetIds()) {
                pets.add(petService.getPetById(petId));
            }
        }

        Customer customer = new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getPhoneNumber(),
                customerDTO.getNotes(), pets);
        return customerDTOConverter(customerService.saveCustomer(customer));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<CustomerDTO> listDTO = new ArrayList<>();
        for (Customer customer: customerService.getAllCustomers()){
            listDTO.add(customerDTOConverter(customer));
        }
        return listDTO;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        return customerDTOConverter(customerService.getCustomerByPetId(petService.getPetById(petId)));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee(employeeDTO.getId(), employeeDTO.getName(), employeeDTO.getSkills(), employeeDTO.getDaysAvailable());

        return employeeDTOConverter(employeeService.saveEmployee(employee));
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.getEmployee(employeeId);
        return employeeDTOConverter(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = employeeService.getEmployee(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeService.setAvailability(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<EmployeeDTO> listDTO = new ArrayList<>();
        for (Employee employee : employeeService.getAvailEmployee(employeeDTO.getDate().getDayOfWeek(), employeeDTO.getSkills())){
            listDTO.add(employeeDTOConverter(employee));
        }
        return listDTO;
    }

    private CustomerDTO customerDTOConverter(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setNotes(customer.getNotes());

        List<Long> petIds = new ArrayList<>();
        for (Pet pet : customer.getPets()) {
            petIds.add(pet.getId());
        }
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }

    private EmployeeDTO employeeDTOConverter(Employee employee){
        EmployeeDTO newEmployeeDTO = new EmployeeDTO();

        newEmployeeDTO.setId(employee.getId());
        newEmployeeDTO.setName(employee.getName());
        newEmployeeDTO.setSkills(employee.getSkills());
        newEmployeeDTO.setDaysAvailable(employee.getDaysAvailable());
        return newEmployeeDTO;
    }
}
