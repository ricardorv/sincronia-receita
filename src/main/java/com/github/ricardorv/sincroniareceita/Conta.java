package com.github.ricardorv.sincroniareceita;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conta {

    private String agencia;
    private String conta;
    private String saldo;
    private String status;
    private Boolean resultado;

}