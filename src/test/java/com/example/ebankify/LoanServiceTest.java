package com.example.ebankify;

import com.example.ebankify.domain.dtos.LoanDTO;
import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.entities.Loan;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.requests.LoanRequest;
import com.example.ebankify.exception.LoanNotFoundException;
import com.example.ebankify.mapper.LoanMapper;
import com.example.ebankify.mapper.UserMapper;
import com.example.ebankify.repository.LoanRepository;
import com.example.ebankify.repository.UserRepository;
import com.example.ebankify.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

    @InjectMocks
    private LoanService loanService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLoan() {
        Loan loan = new Loan();
        User user = new User();
        user.setId(1L);

        UserDto userDto = new UserDto();
        userDto.setId(1L);

        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setUser(userDto);

        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setPrincipal(1000);
        loanRequest.setInterestRate(3);
        loanRequest.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(loanMapper.toEntity(loanRequest)).thenReturn(loan);
        when(loanRepository.save(loan)).thenReturn(loan);
        when(loanMapper.toDto(loan)).thenReturn(loanDTO);
        when(userMapper.toDto(user)).thenReturn(userDto);

        LoanDTO result = loanService.saveLoan(loanRequest);

        assertNotNull(result);
        assertNotNull(result.getUser());
        assertEquals(user.getId(), result.getUser().getId());

        verify(userRepository, times(1)).findById(1L);
        verify(loanMapper, times(1)).toEntity(loanRequest);
        verify(loanRepository, times(1)).save(loan);
        verify(loanMapper, times(1)).toDto(loan);
        verify(userMapper, times(1)).toDto(user);
    }
    @Test
    void testGetLoanById() {
        Loan loan = new Loan();
        loan.setId(1L);
        User user = new User();
        user.setId(1L);
        loan.setUser(user);
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setUser(userDto);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanMapper.toDto(loan)).thenReturn(loanDTO);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(loanDTO.getUser());

        LoanDTO result = loanService.getLoanById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUser().getId());
        verify(loanRepository, times(1)).findById(1L);
        verify(loanMapper, times(1)).toDto(loan);
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testGetLoanById_NotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(LoanNotFoundException.class, () -> loanService.getLoanById(1L));
        verify(loanRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteLoan() {
        loanService.deleteLoan(1L);
        verify(loanRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAllLoans() {
        Loan loan1 = new Loan();
        Loan loan2 = new Loan();
        List<Loan> loans = Arrays.asList(loan1, loan2);
        LoanDTO loanDTO1 = new LoanDTO();
        LoanDTO loanDTO2 = new LoanDTO();
        List<LoanDTO> loanDTOs = Arrays.asList(loanDTO1, loanDTO2);

        when(loanRepository.findAllWithUser()).thenReturn(loans);
        when(loanMapper.toDtoList(loans)).thenReturn(loanDTOs);
        List<LoanDTO> result = loanService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(loanRepository, times(1)).findAllWithUser();
        verify(loanMapper, times(1)).toDtoList(loans);
    }
}
