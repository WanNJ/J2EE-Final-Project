package indi.jackwan.oleducation.service;

import indi.jackwan.oleducation.models.BankAccount;
import indi.jackwan.oleducation.repositories.BankAccountRepository;
import indi.jackwan.oleducation.repositories.ClassRepository;
import indi.jackwan.oleducation.repositories.OrderRepository;
import indi.jackwan.oleducation.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("paymentService")
public class PaymentService {
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public boolean validate(BankAccount bankAccount) {
        BankAccount account = bankAccountRepository.findByAccountAddress(bankAccount.getAccountAddress());
        return account.getPassword().equals(bankAccount.getPassword());
    }

    public synchronized boolean transfer(String payerAddress, String receiverAddress, double amount) {
        BankAccount payer = bankAccountRepository.findByAccountAddress(payerAddress);
        BankAccount receiver = bankAccountRepository.findByAccountAddress(receiverAddress);

        if (payer.getBalence() >= amount) {
            payer.pay(amount);
            receiver.receive(amount);

            bankAccountRepository.save(payer);
            bankAccountRepository.save(receiver);
            return true;
        } else {
            return false;
        }
    }
}
