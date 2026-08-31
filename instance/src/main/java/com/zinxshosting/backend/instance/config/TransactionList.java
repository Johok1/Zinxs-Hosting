package com.zinxshosting.backend.instance.config;

import com.zinxshosting.backend.instance.config.htmltransaction.HTMLTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Getter
public class TransactionList {
    public final List<HTMLTransaction> requests;

    public Object run() throws IOException, InterruptedException {
        requests.get(0).run();

        return requests.get(requests.size()).getObject();
    }


}
