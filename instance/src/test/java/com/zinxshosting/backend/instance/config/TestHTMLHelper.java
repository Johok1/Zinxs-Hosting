package com.zinxshosting.backend.instance.config;

import com.zinxshosting.backend.instance.config.htmltransaction.HTMLHelper;
import com.zinxshosting.backend.instance.config.htmltransaction.OVHApi;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.not;
import static org.assertj.core.api.ClassBasedNavigableIterableAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHTMLHelper {


    @Test
    public void sha1HexTest() throws NoSuchAlgorithmException {
        String input = "hello there";
        String expectedOutput = "6e71b3cac15d32fe2d36c270887df9479c25c640";

        String actualOutput = HTMLHelper.sha1Hex(input);

        assertEquals(expectedOutput,actualOutput);

    }

    @Test
    public void ovhAPITest() throws IOException, NoSuchAlgorithmException, InterruptedException {
        OVHApi api = new OVHApi("https://api.us.ovhcloud.com/1.0","C0TpC58bR73UAVpJ","HpOYQaWGon7TFrWu5ALyvnFn1CGyVtRF",
                "JSPZFwYvxoehrQbFcUfDgfgqWceh8ZMi", (int) System.currentTimeMillis(),10000);

        System.out.println("Raw Call: " + api.rawCall2("GET","/auth/currentCredential"));
    }

    @Test
    public void givenPythonScript_whenPythonProcessInvoked_thenSuccess() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "run.py");
        processBuilder.redirectErrorStream(true);


        Process process = processBuilder.start();


        System.out.println(process.inputReader().readLine());
        int exitCode = process.waitFor();
        System.out.println(process.errorReader().readLine());
        assertEquals(0, exitCode);
    }
}
