package com.example.ebankify.service;

import com.example.ebankify.domain.dtos.LoanDTO;
import com.example.ebankify.domain.entities.Loan;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.requests.LoanRequest;
import com.example.ebankify.exception.LoanNotFoundException;
import com.example.ebankify.exception.UserNotFoundException;
import com.example.ebankify.mapper.LoanMapper;
import com.example.ebankify.mapper.UserMapper;
import com.example.ebankify.repository.LoanRepository;
import com.example.ebankify.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoanService {
    private LoanRepository loanRepository;
    private LoanMapper loanMapper;
    private UserRepository userRepository;
    private UserMapper userMapper;

    public LoanDTO saveLoan(LoanRequest loanRequest) {
        Loan loan = loanMapper.toEntity(loanRequest);
        User user=userRepository.findById(loanRequest.getUserId())
                .orElseThrow(()->new UserNotFoundException("User not found"));
        loan.setUser(user);
        Loan savedLoan = loanRepository.save(loan);
        LoanDTO loanDTO = loanMapper.toDto(savedLoan);
        loanDTO.setUser(userMapper.toDto(user));
        return loanDTO;
    }
    public LoanDTO getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));
        LoanDTO loanDTO = loanMapper.toDto(loan);
        User user = userRepository.findById(loan.getUser().getId())
               .orElseThrow(() -> new UserNotFoundException("User not found"));
        loanDTO.setUser(userMapper.toDto(user));
        return loanDTO;
    }
    public void deleteLoan(Long loanId) {
        loanRepository.deleteById(loanId);
    }
    public List<LoanDTO> findAll(){
        List<Loan> loans = loanRepository.findAllWithUser();
        return loanMapper.toDtoList(loans);
    }
}
