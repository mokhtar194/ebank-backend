package org.sid.ebankbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankbackend.dtos.BankAccountDTO;
import org.sid.ebankbackend.dtos.CustomerDTO;
import org.sid.ebankbackend.entities.BankAccount;
import org.sid.ebankbackend.entities.Customer;
import org.sid.ebankbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankbackend.services.BankAccoutService;
import org.sid.ebankbackend.services.BankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j

public class CustomerRestController {
    private BankAccoutService bankAccoutService;

   @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccoutService.listCustomers();
    }
    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword",defaultValue = "") String keyword){
        return bankAccoutService.searchCustomers("%"+keyword+"%");
    }
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException, CustomerNotFoundException {
        return bankAccoutService.getCustomer(customerId);
    }

    @GetMapping("/customers/{id}/accountsList")
    public List<BankAccountDTO> getCustomerAccount(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException, CustomerNotFoundException {
        return bankAccoutService.bankAccountListCostumer(customerId);
    }
    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccoutService.saveCustomer(customerDTO);
    }
    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return bankAccoutService.updateCustomer(customerDTO);
    }
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id){
        bankAccoutService.deleteCustomer(id);
    }
}
