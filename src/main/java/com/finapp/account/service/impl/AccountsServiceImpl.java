package com.finapp.account.service.impl;


import com.finapp.account.constants.AccountsConstants;
import com.finapp.account.dto.AccountsDto;
import com.finapp.account.dto.CustomerDto;
import com.finapp.account.entity.Account;
import com.finapp.account.entity.Customer;
import com.finapp.account.exception.CustomerAlreadyExistsException;
import com.finapp.account.mapper.AccountsMapper;
import com.finapp.account.mapper.CustomerMapper;
import com.finapp.account.repository.AccountsRepository;
import com.finapp.account.repository.CustomerRepository;
import com.finapp.account.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AccountsServiceImpl  implements IAccountsService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private CustomerRepository customerRepository;



    /**
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Account account = AccountsMapper.mapToAccount(customerDto.getAccountsDto(), new Account());
        Customer savedCustomer = customerRepository.save(customer);
        account.setCustomerId(savedCustomer.getCustomerId());
        Account newAccount = generateAccountId(account);
        accountsRepository.save(newAccount);
    }

    /**
     * @param account - Customer Object
     * @return the new account details
     */
    private Account generateAccountId(Account account) {
        long randomAccNumber = 1000000000 + new Random().nextInt(900000000);
        account.setAccountNumber(randomAccNumber);
        return account;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber);
        Account account = accountsRepository.findByCustomerId(customer.getCustomerId());

        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());

        AccountsDto accountsDto = new AccountsDto();
        accountsDto.setAccountType(account.getAccountType());
        accountsDto.setAccountNumber(account.getAccountNumber());
        accountsDto.setBranchAddress(account.getBranchAddress());

        customerDto.setAccountsDto(accountsDto);
        return customerDto;
    }

    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Account accounts = accountsRepository.findByAccountNumber(accountsDto.getAccountNumber());
            AccountsMapper.mapToAccount(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findByCustomerId(customerId);
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber);
        if(customer != null){
            accountsRepository.deleteByCustomerId(customer.getCustomerId());
            customerRepository.deleteById(customer.getCustomerId());
            return true;
        }
        return false;
    }


}
