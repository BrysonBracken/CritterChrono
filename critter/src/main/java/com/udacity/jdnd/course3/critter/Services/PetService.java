package com.udacity.jdnd.course3.critter.Services;

import com.udacity.jdnd.course3.critter.models.Customer;
import com.udacity.jdnd.course3.critter.models.Pet;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    @Autowired
    PetRepository petRepository;

    public Pet savePet(Pet pet){
        return petRepository.save(pet);
    }

    public Pet getPetById(Long petId){
        Optional<Pet> thePet = petRepository.findById(petId);
        if (!thePet.isPresent()){
            throw new IllegalArgumentException("No pet found");
        }else {
            return thePet.get();
        }
    }

    public List<Pet> getAllPets(){
        return petRepository.findAll();
    }

    public List<Pet> getAllPetsByCustomer(Customer owner){
        return petRepository.findPetsByOwner(owner);
    }
}
