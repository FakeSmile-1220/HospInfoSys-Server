package org.bedrock.hospinfosysserver;

import org.springframework.web.bind.annotation.*;

@RestController
public class DoctorController {

    final private DoctorRepository doctorRepository;

    public DoctorController(final DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/api/doctor")
    public Doctor getDoctor(@RequestParam String name) {
        return doctorRepository.getDoctor(name);
    }

    @PostMapping("api/doctor")
    public void addDoctor(@RequestBody final Doctor doctor) {
        doctorRepository.putDoctor(doctor);
    }

}
