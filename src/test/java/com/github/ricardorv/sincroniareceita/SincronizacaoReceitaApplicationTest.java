package com.github.ricardorv.sincroniareceita;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SincronizacaoReceitaApplicationTest {

    private static final String EXPECTED_FILE = "expected.csv";
    private static final String OUTPUT_FILE = "out.csv";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void testStep1() throws Exception {

        JobExecution jobExecution = jobLauncherTestUtils.launchStep("step1");

        Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
        AssertFile.assertFileEquals(new ClassPathResource(EXPECTED_FILE), new ClassPathResource(OUTPUT_FILE));

    }
}
