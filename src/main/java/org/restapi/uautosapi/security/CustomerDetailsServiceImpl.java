package org.restapi.uautosapi.security;

import org.restapi.uautosapi.model.Customer;
import org.restapi.uautosapi.repository.CustomerRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomerDetailsServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(customer.getEmail())
                .password(customer.getPassword())
                .roles("CUSTOMER") // ESTE LO PDOEMOS REMPLAZAR LUEGO CON UN ENUM PARA MANEJAR LOS ROLES
                .build();
    }
}
