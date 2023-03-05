package com.udacity.jdnd.course3.critter.schedule;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CustomerService customerService;

    @Autowired
    PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        List<Long> petIds = scheduleDTO.getPetIds();
        List<Employee> employees = new ArrayList<>();
        List<Pet> pets = new ArrayList<>();
        for (Long employeeId : employeeIds){
            employees.add(employeeService.getEmployee(employeeId));
        }
        for (Long petId : petIds){
            pets.add(petService.getPetById(petId));
        }

        Schedule schedule = new Schedule(scheduleDTO.getId(), employees, pets, scheduleDTO.getDate(), scheduleDTO.getActivities());
        return scheduleDTOConverter(scheduleService.saveSchedule(schedule));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleDTO> DTOList = new ArrayList<>();
        for (Schedule schedule : scheduleService.getAllSchedules()){
            DTOList.add(scheduleDTOConverter(schedule));
        }
        return DTOList;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<ScheduleDTO> DTOList = new ArrayList<>();
        for (Schedule schedule : scheduleService.getSchedulesByPet(petService.getPetById(petId))){
            DTOList.add(scheduleDTOConverter(schedule));
        }
        return DTOList;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<ScheduleDTO> DTOList = new ArrayList<>();
        Employee employee = employeeService.getEmployee(employeeId);
        List<Schedule> schedules = scheduleService.getSchedulesByEmployee(employee);
        for (Schedule schedule : schedules){
            DTOList.add(scheduleDTOConverter(schedule));
        }
        return DTOList;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Pet> pets = customerService.getCustomer(customerId).getPets();
        List<ScheduleDTO> DTOList = new ArrayList<>();
        for (Pet pet : pets){
            List<Schedule> schedules = scheduleService.getSchedulesByPet(pet);
            for (Schedule schedule : schedules){
                DTOList.add(scheduleDTOConverter(schedule));
            }
        }
        return DTOList;
    }

    private ScheduleDTO scheduleDTOConverter(Schedule schedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        List<Long> employeeIds = new ArrayList<>();
        List<Long> petIds = new ArrayList<>();

        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setActivities(schedule.getActivities());

        for (Employee employee: schedule.getEmployee()){
            employeeIds.add(employee.getId());
        }
        for (Pet pet: schedule.getPets()){
            petIds.add(pet.getId());
        }
        scheduleDTO.setEmployeeIds(employeeIds);
        scheduleDTO.setPetIds(petIds);
        return scheduleDTO;
    }
}
