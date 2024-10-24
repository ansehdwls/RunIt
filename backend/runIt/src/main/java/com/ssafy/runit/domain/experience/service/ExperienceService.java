package com.ssafy.runit.domain.experience.service;


import com.ssafy.runit.domain.experience.entity.Experience;
import com.ssafy.runit.domain.experience.entity.ExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExperienceService {
    private final ExperienceRepository experienceRepository;

    public Long experienceSave(Experience experience){
        experienceRepository.save(experience);

        return experience.getId();
    }

    public Long experienceChangedSum(Long id){

//        List<Experience> ls = experienceRepository.findAll(id);

//        for (int i =0; i < ls.size(); i++){
//            System.out.println("ls.get(i) = " + ls.get(i).getId() + " " + ls.get(i).getCreateAt()+ " " + ls.get(i).getChanged() + " " );
//        }

        return experienceRepository.getUserExperience(id);
    }



}
