package com.zinxshosting.backend.server.spring.controller;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.zinxshosting.backend.instance.config.TransactionConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/zinxshosting/server")
public class ServerController {

    private ChannelExec channelExec;

    public ServerController() throws JSchException {
        String username = "ubuntu";
        String hostname = "15.204.204.53";
        String password = "sfJEUswZYKPp";

        int port = 22;
        Session session = new JSch().getSession(username,hostname,port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking","no");
        session.connect();
        OutputStream out = new ByteArrayOutputStream();
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand("help");
        channelExec.setOutputStream(out);
        channelExec.connect();
        this.channelExec = channelExec;
    }

    @GetMapping("/poll")
    public String getData() throws IOException {


        Scanner scanner = new Scanner(channelExec.getInputStream());

        if(scanner.hasNext()){
            return scanner.nextLine();
        }else {
            return "Empty!";
        }

    }

}
