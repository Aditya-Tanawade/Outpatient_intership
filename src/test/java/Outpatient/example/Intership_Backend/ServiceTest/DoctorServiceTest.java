//package Outpatient.example.Intership_Backend.ServiceTest;
//
//
//import Outpatient.example.Intership_Backend.Advices.ApiError;
//import Outpatient.example.Intership_Backend.DTO.LoginRequest;
//import Outpatient.example.Intership_Backend.DTO.RegisterUserDTo;
//import Outpatient.example.Intership_Backend.Entity.Doctor;
//import Outpatient.example.Intership_Backend.Repository.DoctorRepository;
//import Outpatient.example.Intership_Backend.Service.DoctorService;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//        import static org.mockito.Mockito.*;
//
//class DoctorServiceTest {
//
//    @Mock
//    private DoctorRepository doctorRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private DoctorService doctorService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateDoctor() {
//        RegisterUserDTo registerUserDTo = new RegisterUserDTo();
//        registerUserDTo.setEmail("doctor@example.com");
//
//        doctorService.createDoctor(registerUserDTo);
//
//        ArgumentCaptor<Doctor> doctorCaptor = ArgumentCaptor.forClass(Doctor.class);
//        verify(doctorRepository, times(1)).save(doctorCaptor.capture());
//
//        Doctor savedDoctor = doctorCaptor.getValue();
//        assertEquals("doctor@example.com", savedDoctor.getEmail());
//    }
//
//    @Test
//    void testUpdateDoctorProfile_Success() {
//        Doctor existingDoctor = new Doctor();
//        existingDoctor.setEmail("doctor@example.com");
//
//        Doctor updatedDoctor = new Doctor();
//        updatedDoctor.setDoctorName("Updated Name");
//        updatedDoctor.setSpeciality("Cardiology");
//        updatedDoctor.setLocation("New York");
//        updatedDoctor.setMobileNo("1234567890");
//        updatedDoctor.setHospitalName("General Hospital");
//        updatedDoctor.setChargedPerVisit(500);
//
//        when(doctorRepository.findByEmail("doctor@example.com")).thenReturn(Optional.of(existingDoctor));
//
//        doctorService.setLoginEmail("doctor@example.com");
//        ResponseEntity<ApiError> response = doctorService.updateDoctorProfile(updatedDoctor);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Doctor profile updated successfully", response.getBody().getMessage());
//        verify(doctorRepository, times(1)).save(existingDoctor);
//
//        assertEquals("Updated Name", existingDoctor.getDoctorName());
//        assertEquals("Cardiology", existingDoctor.getSpeciality());
//        assertEquals("New York", existingDoctor.getLocation());
//        assertEquals("1234567890", existingDoctor.getMobileNo());
//        assertEquals("General Hospital", existingDoctor.getHospitalName());
//        assertEquals(500, existingDoctor.getChargedPerVisit());
//    }
//
//@Test
//void testUpdateDoctorProfile_DoctorNotFound() {
//    when(doctorRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
//
//    doctorService.setLoginEmail("unknown@example.com");
//    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//        doctorService.updateDoctorProfile(new Doctor());
//    });
//
//    assertEquals("DOCTOR NOT FOUND", exception.getMessage());
//}
//
//
//    @Test
//    void testLoginDoctor() {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("doctor@example.com");
//
//        doctorService.LoginDoctor(loginRequest);
//
//        assertEquals("doctor@example.com", doctorService.getLoginEmail());
//    }
//
//    @Test
//    void testGetDoctorProfile_Success() {
//        Doctor doctor = new Doctor();
//        doctor.setEmail("doctor@example.com");
//
//        when(doctorRepository.findByEmail("doctor@example.com")).thenReturn(Optional.of(doctor));
//
//        doctorService.setLoginEmail("doctor@example.com");
//        Doctor fetchedDoctor = doctorService.getDoctorProfile();
//
//        assertNotNull(fetchedDoctor);
//        assertEquals("doctor@example.com", fetchedDoctor.getEmail());
//    }
//
//    @Test
//    void testGetDoctorProfile_NotFound() {
//        when(doctorRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
//
//        doctorService.setLoginEmail("unknown@example.com");
//        Doctor fetchedDoctor = doctorService.getDoctorProfile();
//
//        assertNull(fetchedDoctor);
//    }
//
//    @Test
//    void testGetAllDoctors() {
//        List<Doctor> doctors = new ArrayList<>();
//        Doctor doctor1 = new Doctor();
//        doctor1.setEmail("doctor1@example.com");
//        Doctor doctor2 = new Doctor();
//        doctor2.setEmail("doctor2@example.com");
//        doctors.add(doctor1);
//        doctors.add(doctor2);
//
//        when(doctorRepository.findAll()).thenReturn(doctors);
//
//        List<Doctor> fetchedDoctors = doctorService.getAllDoctors();
//
//        assertEquals(2, fetchedDoctors.size());
//        assertEquals("doctor1@example.com", fetchedDoctors.get(0).getEmail());
//        assertEquals("doctor2@example.com", fetchedDoctors.get(1).getEmail());
//    }
//}
package Outpatient.example.Intership_Backend.ServiceTest;

import Outpatient.example.Intership_Backend.Advices.ApiError;
import Outpatient.example.Intership_Backend.DTO.LoginRequest;
import Outpatient.example.Intership_Backend.DTO.RegisterUserDTo;
import Outpatient.example.Intership_Backend.Entity.Doctor;
import Outpatient.example.Intership_Backend.Repository.DoctorRepository;
import Outpatient.example.Intership_Backend.Service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    private RegisterUserDTo registerUserDTo;
    private LoginRequest loginRequest;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        registerUserDTo = new RegisterUserDTo();
        registerUserDTo.setEmail("doctor@example.com");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("doctor@example.com");
        loginRequest.setPassword("password");

        doctor = new Doctor();
        doctor.setEmail("doctor@example.com");
        doctor.setDoctorName("Dr. John");
        doctor.setSpeciality("Cardiologist");
        doctor.setLocation("City Hospital");
        doctor.setMobileNo("1234567890");
        doctor.setHospitalName("City Hospital");
        doctor.setChargedPerVisit(100.0);
    }

    @Test
    void testCreateDoctor() {
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        doctorService.createDoctor(registerUserDTo);

        verify(doctorRepository, times(1)).save(any(Doctor.class));
        assertEquals("doctor@example.com", doctor.getEmail());
    }

    @Test
    void testUpdateDoctorProfile_Success() {
        doctorService.LoginDoctor(loginRequest);

        when(doctorRepository.findByEmail("doctor@example.com")).thenReturn(doctor);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        doctor.setDoctorName("Dr. Updated Name");
        doctor.setSpeciality("Updated Speciality");

        ResponseEntity<ApiError> response = doctorService.updateDoctorProfile(doctor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Doctor profile updated successfully", response.getBody().getMessage());
        assertEquals("Dr. Updated Name", doctor.getDoctorName());
        assertEquals("Updated Speciality", doctor.getSpeciality());
    }

    @Test
    void testUpdateDoctorProfile_DoctorNotFound() {
        doctorService.LoginDoctor(loginRequest);

        when(doctorRepository.findByEmail("doctor@example.com")).thenReturn(null);

        ResponseEntity<ApiError> response = doctorService.updateDoctorProfile(doctor);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Doctor not found or unauthorized access", response.getBody().getMessage());
    }

    @Test
    void testGetDoctorProfile_Success() {
        doctorService.LoginDoctor(loginRequest);

        when(doctorRepository.findByEmail("doctor@example.com")).thenReturn(doctor);

        Doctor fetchedDoctor = doctorService.getDoctorProfile();

        assertNotNull(fetchedDoctor);
        assertEquals("doctor@example.com", fetchedDoctor.getEmail());
    }

    @Test
    void testGetDoctorProfile_DoctorNotFound() {
        doctorService.LoginDoctor(loginRequest);

        when(doctorRepository.findByEmail("doctor@example.com")).thenReturn(null);

        Doctor fetchedDoctor = doctorService.getDoctorProfile();

        assertNull(fetchedDoctor);
    }

    @Test
    void testGetAllDoctors() {
        List<Doctor> doctors = Arrays.asList(doctor);
        when(doctorRepository.findAll()).thenReturn(doctors);

        List<Doctor> fetchedDoctors = doctorService.getAllDoctors();

        assertNotNull(fetchedDoctors);
        assertEquals(1, fetchedDoctors.size());
        assertEquals("doctor@example.com", fetchedDoctors.get(0).getEmail());
    }
}
