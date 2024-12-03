package Outpatient.example.Intership_Backend.ServiceTest;

import Outpatient.example.Intership_Backend.Entity.AvailableDate;
import Outpatient.example.Intership_Backend.Entity.Doctor;
import Outpatient.example.Intership_Backend.Repository.AvailableDateRepository;
import Outpatient.example.Intership_Backend.Repository.DoctorRepository;
import Outpatient.example.Intership_Backend.Service.AvailableDateService;
import Outpatient.example.Intership_Backend.Service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailableDateServiceTest {

    @Mock
    private AvailableDateRepository availableDateRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private AvailableDateService availableDateService;

    private String doctorEmail;
    private Doctor mockDoctor;
    private AvailableDate mockAvailableDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorEmail = "doctor@example.com";

        // Mocked Doctor object
        mockDoctor = new Doctor();
        mockDoctor.setEmail(doctorEmail);

        // Mocked AvailableDate object
        mockAvailableDate = new AvailableDate();
        mockAvailableDate.setDoctor(mockDoctor);
        mockAvailableDate.setAppointmentFromdate(LocalDate.of(2024, 12, 1));
        mockAvailableDate.setAppointmentEnddate(LocalDate.of(2024, 12, 10));
        mockAvailableDate.setAmSlotTiming("09:00-12:00");
        mockAvailableDate.setPmSlotTiming("14:00-17:00");
    }

    @Test
    void testGetAvailabilityByDoctor() {
        // Mock the login email
        when(doctorService.getLoginEmail()).thenReturn(doctorEmail);
        when(availableDateRepository.findByDoctorEmail(doctorEmail)).thenReturn(mockAvailableDate);

        // Call the method
        AvailableDate result = availableDateService.getAvailabilityByDoctor();

        // Assertions
        assertNotNull(result);
        assertEquals(doctorEmail, result.getDoctor().getEmail());
        assertEquals("09:00-12:00", result.getAmSlotTiming());
        assertEquals("14:00-17:00", result.getPmSlotTiming());

        // Verify interactions
        verify(doctorService).getLoginEmail();
        verify(availableDateRepository).findByDoctorEmail(doctorEmail);
    }

    @Test
    void testUpdateAvailability_WhenExistingSlotExists() {
        // Mock the login email and repository calls
        when(doctorService.getLoginEmail()).thenReturn(doctorEmail);
        when(availableDateRepository.findByDoctorEmail(doctorEmail)).thenReturn(mockAvailableDate);

        // New available date to update
        AvailableDate updatedAvailableDate = new AvailableDate();
        updatedAvailableDate.setAppointmentFromdate(LocalDate.of(2024, 12, 5));
        updatedAvailableDate.setAppointmentEnddate(LocalDate.of(2024, 12, 15));
        updatedAvailableDate.setAmSlotTiming("08:00-12:00");
        updatedAvailableDate.setPmSlotTiming("13:00-18:00");

        // Mock the save method
        when(availableDateRepository.save(mockAvailableDate)).thenReturn(mockAvailableDate);

        // Call the method
        AvailableDate result = availableDateService.updateAvailability(updatedAvailableDate);

        // Assertions
        assertNotNull(result);
        assertEquals("08:00-12:00", result.getAmSlotTiming());
        assertEquals("13:00-18:00", result.getPmSlotTiming());
        assertEquals(LocalDate.of(2024, 12, 5), result.getAppointmentFromdate());
        assertEquals(LocalDate.of(2024, 12, 15), result.getAppointmentEnddate());

        // Verify interactions
        verify(doctorService).getLoginEmail();
        verify(availableDateRepository).findByDoctorEmail(doctorEmail);
        verify(availableDateRepository).save(mockAvailableDate);
    }

//    @Test
//    void testUpdateAvailability_WhenNoExistingSlot() {
//        // Mock the login email and repository calls
//        when(doctorService.getLoginEmail()).thenReturn(doctorEmail);
//        when(availableDateRepository.findByDoctorEmail(doctorEmail)).thenReturn(null);
//        when(doctorRepository.findByEmail(doctorEmail)).thenReturn(Optional.of(mockDoctor));
//
//        // New available date to save
//        AvailableDate newAvailableDate = new AvailableDate();
//        newAvailableDate.setAppointmentFromdate(LocalDate.of(2024, 12, 1));
//        newAvailableDate.setAppointmentEnddate(LocalDate.of(2024, 12, 10));
//        newAvailableDate.setAmSlotTiming("09:00-12:00");
//        newAvailableDate.setPmSlotTiming("14:00-17:00");
//
//        // Mock the save method
//        when(availableDateRepository.save(newAvailableDate)).thenReturn(newAvailableDate);
//
//        // Call the method
//        AvailableDate result = availableDateService.updateAvailability(newAvailableDate);
//
//        // Assertions
//        assertNotNull(result);
//        assertEquals(mockDoctor, result.getDoctor());
//        assertEquals("09:00-12:00", result.getAmSlotTiming());
//        assertEquals("14:00-17:00", result.getPmSlotTiming());
//
//        // Verify interactions
//        verify(doctorService).getLoginEmail();
//        verify(availableDateRepository).findByDoctorEmail(doctorEmail);
//        verify(doctorRepository).findByEmail(doctorEmail);
//        verify(availableDateRepository).save(newAvailableDate);
//    }


    @Test
    void testUpdateAvailability_DoctorNotFound() {
        // Mock the login email and repository calls
        when(doctorService.getLoginEmail()).thenReturn(doctorEmail);
        when(availableDateRepository.findByDoctorEmail(doctorEmail)).thenReturn(null);
        when(doctorRepository.findByEmail(doctorEmail)).thenReturn(Optional.empty());

        // Call the method and assert the exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            availableDateService.updateAvailability(new AvailableDate());
        });

        assertEquals("Doctor not found with email: doctor@example.com", exception.getMessage());

        // Verify interactions
        verify(doctorService, times(3)).getLoginEmail();
        verify(availableDateRepository).findByDoctorEmail(doctorEmail);
        verify(doctorRepository).findByEmail(doctorEmail);
    }

}
