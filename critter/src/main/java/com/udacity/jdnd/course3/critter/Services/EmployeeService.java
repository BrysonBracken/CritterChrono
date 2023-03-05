package com.udacity.jdnd.course3.critter.Services;

import com.udacity.jdnd.course3.critter.models.Employee;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public void setAvailability(Employee employee){
        employeeRepository.save(employee);
    }

    public Employee getEmployee(Long id){
        Optional<Employee> theEmployee = employeeRepository.findById(id);
        if (!theEmployee.isPresent()){
            throw new IllegalArgumentException("No employee found");
        }else {
            return theEmployee.get();
        }
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public List<Employee> getAvailEmployee(DayOfWeek day, Set<EmployeeSkill> skills){
        return employeeRepository.findByDaysAvailable(day).stream().
                filter(employee -> employee.getSkills().containsAll(skills))
                .collect(Collectors.toList());
    }
}
