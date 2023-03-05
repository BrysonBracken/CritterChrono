package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.Services.CustomerService;
import com.udacity.jdnd.course3.critter.Services.PetService;
import com.udacity.jdnd.course3.critter.models.Customer;
import com.udacity.jdnd.course3.critter.models.Pet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;
    @Autowired
    CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {

        Customer owner = customerService.getCustomer(petDTO.getOwnerId());
        Pet pet = new Pet(petDTO.getId(), petDTO.getType(), petDTO.getName(), owner, petDTO.getBirthDate(), petDTO.getNotes());
        owner.addPet(pet);


        return petDTOConverter(petService.savePet(pet));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return petDTOConverter(petService.getPetById(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<PetDTO> DTOlist = new ArrayList<>();
        for (Pet pet : petService.getAllPets()){
            DTOlist.add(petDTOConverter(pet));
        }
        return DTOlist;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<PetDTO> DTOlist = new ArrayList<>();
        for (Pet pet : petService.getAllPetsByCustomer(customerService.getCustomer(ownerId))){
            DTOlist.add(petDTOConverter(pet));
        }
        return DTOlist;
    }

    private PetDTO petDTOConverter(Pet pet){
        PetDTO petDTO = new PetDTO();

        petDTO.setId(pet.getId());
        petDTO.setName(pet.getName());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setNotes(pet.getNotes());
        petDTO.setType(pet.getType());
        petDTO.setOwnerId(pet.getOwner().getId());
        return petDTO;
    }
}
