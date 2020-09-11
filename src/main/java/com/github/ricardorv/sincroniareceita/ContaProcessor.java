package com.github.ricardorv.sincroniareceita;

import com.example.receita.ReceitaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import java.text.NumberFormat;
import java.util.Locale;

public class ContaProcessor implements ItemProcessor<Conta, Conta> {

    private static final Logger log = LoggerFactory.getLogger(ContaProcessor.class);

    @Override
    public Conta process(final Conta conta) throws Exception {

        log.info("Atualizando conta: " + conta.getConta());

        ReceitaService receitaService = new ReceitaService();

        Boolean resultado = receitaService.atualizarConta(
                conta.getAgencia(),
                conta.getConta().replaceAll("-", ""),
                NumberFormat.getInstance(Locale.ROOT).parse(conta.getSaldo()).doubleValue(),
                conta.getStatus()
        );

        conta.setResultado(resultado);

        return conta;
    }

}
