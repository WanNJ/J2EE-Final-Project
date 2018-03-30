package indi.jackwan.oleducation.repositories;

import indi.jackwan.oleducation.models.BankAccount;
import org.springframework.data.repository.CrudRepository;


public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
    BankAccount findById(int id);
    BankAccount findByAccountAddress(String accountAddress);
}
